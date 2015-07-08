/*
 * Created by Daniel Marell 15-06-23 18:38
 */
package se.marell.dvision.server;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.*;
import se.marell.dcommons.time.DateUtils;
import se.marell.dcommons.time.PassiveTimer;
import se.marell.dcommons.time.TimeSource;
import se.marell.dvision.api.*;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class MotionDetectionController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final int MAX_IDLE_TIME_MSEC = 10000;
    private static final int MAX_HISTORY_TIME_MSEC = 10000;

    private class Slot {
        MotionDetectionRequest request;
        long lastRequestTimestamp;
        PassiveTimer captureTimer;
        Future<BufferedImage> image;
        BufferedImage previousBufferedImage;
        CapturedImage previousCapturedImage;
        MotionDetector detector;
        List<MotionDetectionResponse> responses = new ArrayList<>();

        public Slot(MotionDetectionRequest request) {
            this.request = request;
            lastRequestTimestamp = timeSource.currentTimeMillis();
            captureTimer = new PassiveTimer(request.getCamera().getCaptureRate(), timeSource);
            detector = new MotionDetector(request.getAreaSizeThreshold(), request.getDetectionAreas());
        }
    }

    private DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private Map<String/*cameraName*/, Slot> slots = new HashMap<>();

    @Autowired
    private TimeSource timeSource;

    @Autowired
    private Environment environment;

    private File outputDirectory;
    private String baseUrl;

    @PostConstruct
    public void init() {
        Authenticator.setDefault(new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                for (Slot slot : slots.values()) {
                    if (getRequestingURL().equals(slot.request.getCamera().getUrl())) {
                        return new PasswordAuthentication(
                                slot.request.getCamera().getUsername(),
                                slot.request.getCamera().getPassword().toCharArray());
                    }
                }
                return null;
            }
        });
        outputDirectory = new File(environment.getRequiredProperty("dvision.out-directory"));
        baseUrl = environment.getRequiredProperty("motion-detection-service.baseurl");
    }

    public void setCaptureInterval(String cameraName, long durationMsec) {
        Slot slot = slots.get(cameraName);
        if (slot != null) {
            slot.captureTimer.restart(durationMsec);
        }
    }

    @Async
    public Future<BufferedImage> getImage(NetworkCamera cam) throws IOException {
        return new AsyncResult<>(ImageIO.read(cam.getUrl()));
    }

    @RequestMapping(value = "/motiondetectionrequest", method = RequestMethod.POST)
    public ResponseEntity<Void> requestMotionDetection(@RequestBody MotionDetectionRequest request) throws IOException {
        slots.put(request.getCamera().getName(), new Slot(request));
        // Try reading camera, throwing IOException in case of failure
        try {
            ImageIO.read(request.getCamera().getUrl());
            log.info("motiondetectionrequest, " + request.getCamera().getName());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw e;
        }
    }

    public void capture() {
        log.debug("capture, slots: " + slots.size());
        removeOldSlotsAndResponses();
        for (Slot slot : slots.values()) {
            captureImage(slot);
        }
    }

    private void captureImage(Slot slot) {
        log.debug("captureImage: " + slot.request.getCamera().getName());
        if (slot.image == null) {
            if (slot.captureTimer.hasExpired()) {
                slot.captureTimer.restart();
                try {
                    log.debug("Camera {}, grabbing image", slot.request.getCamera().getName());
                    slot.image = getImage(slot.request.getCamera());
                } catch (IOException e) {
                    log.error("Failed to get image from camera " + slot.request.getCamera().getName() + ":" + e.getMessage());
                }
            }
            if (slot.image != null && slot.image.isDone()) {
                captureAndAnalyzeImage(slot);
            }
        }
    }

    private void captureAndAnalyzeImage(Slot slot) {
        log.debug("Camera {}, analyzing image", slot.request.getCamera().getName());
        try {
            BufferedImage image = slot.image.get();
            slot.image = null;
            List<ImageRectangle> areas = slot.detector.getMotionAreas(image);
            int areaSize = calcArea(areas);
            final long millisNow = timeSource.currentTimeMillis();
            LocalDateTime localDateTimeNow = DateUtils.getLocalDateTime(millisNow);
            String imageFilename = getDateTimeString(localDateTimeNow) + "-" + slot.request.getCamera().getName() + ".png";
            CapturedImage capturedImage = new CapturedImage(baseUrl + "/images/" + imageFilename, millisNow);
            if (areaSize >= slot.request.getMinAreaSize()) {
                log.info("Detected motion, cam: {}, size: {}", slot.request.getCamera().getName(), areaSize);
                LocalDateTime dateTimePrevImage = DateUtils.getLocalDateTime(slot.previousCapturedImage.getTimestamp());
                String prevImageFilename = getDateTimeString(dateTimePrevImage) + "-" + slot.request.getCamera().getName() + ".png";
                saveCameraImage(prevImageFilename, slot.previousBufferedImage, dateTimePrevImage);
                drawMotionRectangles(image, areas);
                saveCameraImage(imageFilename, image, localDateTimeNow);
                slot.responses.add(new MotionDetectionResponse(
                        timeSource.currentTimeMillis(),
                        new ImageSize(image.getWidth(), image.getHeight()), areas,
                        Arrays.asList(slot.previousCapturedImage, capturedImage)));
            } else {
                log.debug("Camera {}, no change in image", slot.request.getCamera().getName());
            }
            slot.previousCapturedImage = capturedImage;
            slot.previousBufferedImage = image;
        } catch (InterruptedException | ExecutionException e) {
            log.error("Unexpected error", e);
        }
    }

    private void drawMotionRectangles(BufferedImage image, List<ImageRectangle> areas) {
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw green rectangles
        g.setColor(Color.green);
        g.setStroke(new BasicStroke(1));
        for (ImageRectangle area : areas) {
            int x = area.getX();
            int y = area.getY();
            int w = area.getWidth();
            int h = area.getHeight();
            g.drawRect(x, y, w, h);
        }
        g.dispose();
    }

    private void removeOldSlotsAndResponses() {
        Iterator<Slot> iter = slots.values().iterator();
        while (iter.hasNext()) {
            Slot slot = iter.next();
            if (timeSource.currentTimeMillis() - slot.lastRequestTimestamp > MAX_IDLE_TIME_MSEC) {
                iter.remove();
                log.info("Camera " + slot.request.getCamera().getName() + " removed, consumer timeout");
            } else {
                // Remove old responses
                Iterator<MotionDetectionResponse> responsesIter = slot.responses.iterator();
                while (responsesIter.hasNext()) {
                    MotionDetectionResponse response = responsesIter.next();
                    if (timeSource.currentTimeMillis() - response.getTimestamp() > MAX_HISTORY_TIME_MSEC) {
                        responsesIter.remove();
                    }
                }
            }
        }
    }

    private int calcArea(List<ImageRectangle> rectangles) {
        int area = 0;
        for (ImageRectangle b : rectangles) {
            area += b.getArea();
        }
        return area;
    }

    private void saveCameraImage(String imageFilename, BufferedImage image, LocalDateTime now) {
        try {
            File directory = getDirectory(now);
            directory.mkdirs();
            if (directory.exists() && directory.canWrite()) {
                File outputfile = new File(directory, imageFilename);
                if (!outputfile.exists()) {
                    ImageIO.write(image, "png", outputfile);
                }
            } else {
                log.error("Failed to save image because output directory does not exist or is not writable: " + directory);
            }
        } catch (IOException e) {
            log.error("Failed to save image to png:" + e.getMessage());
        }
    }

    private File getDirectory(LocalDateTime now) {
        return new File(outputDirectory,
                now.getYear() + "/" +
                        String.format("%02d", now.getMonthValue()) + "/" +
                        String.format("%02d", now.getDayOfMonth()));
    }

    private String getDateTimeString(LocalDateTime now) {
        return now.format(df);
    }

    /**
     * Retrieve motion detections for camera since timestamp.
     *
     * @param cameraName A camera name
     * @param since      Timestamp in milliseconds for earliest detection of interest. 0=returns all stored
     *                   detections for this camera
     * @return A response object or null if no such jobId is known
     */
    @RequestMapping(value = "/motiondetectionresponse/{cameraName}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<MotionDetectionResponse> getMotionDetections(@PathVariable String cameraName,
                                                                       @RequestParam long since) {
        Slot slot = slots.get(cameraName);
        if (slot == null) {
            log.error("getMotionDetections: Unknown camera " + cameraName);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        slot.lastRequestTimestamp = timeSource.currentTimeMillis();
        ImageSize imageSize = null;
        Set<CapturedImage> capturedImages = new TreeSet<>();
        List<ImageRectangle> allAreas = new ArrayList<>();
        for (MotionDetectionResponse r : slot.responses) {
            if (r.getTimestamp() >= since) {
                allAreas.addAll(r.getAreas());
                imageSize = r.getImageSize();
                capturedImages.addAll(r.getImages());
            }
        }
        if (imageSize == null) {
            log.debug("getMotionDetections: no motion, camera " + cameraName);
            return null;
        }
        return new ResponseEntity<>(new MotionDetectionResponse(slot.lastRequestTimestamp, imageSize, allAreas,
                capturedImages), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/images/{imageName}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String imageName) throws IOException {
        if (imageName == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String s = imageName.substring(0, imageName.indexOf("-"));
        LocalDateTime now = LocalDateTime.parse(s, df);
        String path = getDirectory(now) + "/" + imageName + ".png";
        InputStream in = new FileInputStream(path);
        return new ResponseEntity<>(IOUtils.toByteArray(in), HttpStatus.OK);
    }
}
