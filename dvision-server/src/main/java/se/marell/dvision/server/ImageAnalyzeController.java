/*
 * Created by Daniel Marell 15-06-23 18:38
 */
package se.marell.dvision.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.marell.dcommons.time.TimeSource;
import se.marell.dvision.api.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ImageAnalyzeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<String/*cameraName*/, Slot> slots = new HashMap<>();

    @Autowired
    private TimeSource timeSource;

    @RequestMapping(value = "/image-analyze-request/{cameraName}", method = RequestMethod.POST)
    public ResponseEntity<ImageAnalyzeResponse> postMotionDetectionRequest(
            @PathVariable String cameraName,
            @RequestParam MultipartFile file) throws IOException {
        ImageAnalyzeRequest request = new ImageAnalyzeRequest(cameraName);
        Slot slot = slots.get(request.getCameraName());
        ImageData image = new ImageData("image/png", Base64.getEncoder().encodeToString(file.getBytes()));
        if (slot == null) {
            slots.put(request.getCameraName(), new Slot(request, image));
            return null;
        }

        // Search for motion between image in slot and image in request
        ImageAnalyzeResponse response = analyzeImage(slot, ImageData.createBufferedImage(image), ImageData.createBufferedImage(slot.image));
        slot.image = image;
        if (response.getMotionAreas().isEmpty()) {
            return null;
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ImageAnalyzeResponse analyzeImage(Slot slot, BufferedImage image1, BufferedImage image2) {
        log.debug("Camera {}, analyzing image", slot.request.getCameraName());
        List<ImageRectangle> areas = slot.detector.getMotionAreas(image1, image2);
        ImageAnalyzeResponse response;
        try {
            slot.markedImage = ImageData.create(image1);
        } catch (IOException e) {
            log.warn("Failed to unpack marked image", e);
        }
        response = new ImageAnalyzeResponse(
                timeSource.currentTimeMillis(),
                new ImageSize(image1.getWidth(), image1.getHeight()),
                areas);
        return response;
    }

    private static class Slot {
        ImageAnalyzeRequest request;
        ImageData image;
        MotionDetector detector;
        ImageData markedImage;

        public Slot(ImageAnalyzeRequest request, ImageData image) {
            this.request = request;
            detector = new MotionDetector();
            this.image = image;
        }
    }
}
