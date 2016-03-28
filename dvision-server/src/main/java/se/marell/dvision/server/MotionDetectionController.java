/*
 * Created by Daniel Marell 15-06-23 18:38
 */
package se.marell.dvision.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.marell.dcommons.time.TimeSource;
import se.marell.dvision.api.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

@RestController
public class MotionDetectionController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<String/*cameraName*/, Slot> slots = new HashMap<>();
    @Autowired
    private TimeSource timeSource;

    @RequestMapping(value = "/motion-detection-request/{cameraName}", method = RequestMethod.POST)
    public ResponseEntity<MotionDetectionResponse> postMotionDetectionRequest(
            @PathVariable String cameraName,
            @RequestParam MultipartFile file) throws IOException {
        MotionDetectionRequest request = new MotionDetectionRequest(cameraName);
        Slot slot = slots.get(request.getCameraName());
        ImageData image = new ImageData("image/png", Base64.getEncoder().encodeToString(file.getBytes()));
        if (slot == null) {
            slots.put(request.getCameraName(), new Slot(request, image));
            return null;
        }

        // Search for motion between image in slot and image in request
        MotionDetectionResponse response = analyzeImage(slot, ImageData.createBufferedImage(image), ImageData.createBufferedImage(slot.image));
        if (response.getAreas().isEmpty()) {
            return null;
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/camera-image/{cameraName}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getCameraImage(
            @PathVariable String cameraName) {
        Slot slot = slots.get(cameraName);
        if (slot == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType(slot.image.getMediaType()));
        return new ResponseEntity<>(
                Base64.getDecoder().decode(slot.markedImage.getBase64EncodedData()),
                responseHeaders, HttpStatus.OK);
    }

    private MotionDetectionResponse analyzeImage(Slot slot, BufferedImage image1, BufferedImage image2) {
        log.debug("Camera {}, analyzing image", slot.request.getCameraName());
        List<ImageRectangle> areas = slot.detector.getMotionAreas(image1, image2);
        MotionDetectionResponse response;
        drawMotionRectangles(image1, Color.green, areas);
        try {
            slot.markedImage = ImageData.create(image1);
        } catch (IOException e) {
            log.warn("Failed to unpack marked image", e);
        }
        response = new MotionDetectionResponse(
                timeSource.currentTimeMillis(),
                new ImageSize(image1.getWidth(), image1.getHeight()),
                areas);
        return response;
    }

    private void drawMotionRectangles(BufferedImage image, Color color, List<ImageRectangle> areas) {
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rectangles
        g.setColor(color);
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

    private class Slot {
        MotionDetectionRequest request;
        ImageData image;
        MotionDetector detector;
        ImageData markedImage;

        public Slot(MotionDetectionRequest request, ImageData image) {
            this.request = request;
            detector = new MotionDetector();
            this.image = image;
        }
    }
}
