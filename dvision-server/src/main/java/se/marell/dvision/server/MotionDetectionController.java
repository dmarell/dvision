/*
 * Created by Daniel Marell 15-06-23 18:38
 */
package se.marell.dvision.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import se.marell.dcommons.time.TimeSource;
import se.marell.dvision.api.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MotionDetectionController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<String/*cameraName*/, Slot> slots = new HashMap<>();
    @Autowired
    private TimeSource timeSource;

    @RequestMapping(value = "/motion-detection-request", method = RequestMethod.POST)
    public ResponseEntity<MotionDetectionResponse> getMotionDetectionRequest(
            @RequestBody MotionDetectionRequest request) throws IOException {
        Slot slot = slots.get(request.getCameraName());
        if (slot == null) {
            slots.put(request.getCameraName(), new Slot(request));
            return null;
        }

        // Search for motion between image in slot and image in request
        MotionDetectionResponse response = analyzeImage(slot, ImageData.createBufferedImage(request.getImage()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private MotionDetectionResponse analyzeImage(Slot slot, BufferedImage image) {
        log.debug("Camera {}, analyzing image", slot.request.getCameraName());
        List<ImageRectangle> areas = slot.detector.getMotionAreas(image);
        int areaSize = calcArea(areas);
        MotionDetectionResponse response = null;
        if (areaSize >= slot.request.getMinAreaSize()) {
            log.info("Detected motion, cam: {}, size: {}", slot.request.getCameraName(), areaSize);
            drawMotionRectangles(image, Color.green, areas);
            try {
                response = new MotionDetectionResponse(
                        timeSource.currentTimeMillis(),
                        new ImageSize(image.getWidth(), image.getHeight()),
                        areas,
                        ImageData.create(image));
            } catch (IOException e) {
                log.debug("Failed to convert image: {}", e.getMessage());
            }
        } else {
            log.debug("Camera {}, no change in image", slot.request.getCameraName());
        }
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

    private int calcArea(List<ImageRectangle> rectangles) {
        int area = 0;
        for (ImageRectangle b : rectangles) {
            area += b.getArea();
        }
        return area;
    }

    private class Slot {
        MotionDetectionRequest request;
        MotionDetector detector;

        public Slot(MotionDetectionRequest request) {
            this.request = request;
            detector = new MotionDetector(request.getAreaSizeThreshold(), request.getDetectionAreas());
        }
    }
}
