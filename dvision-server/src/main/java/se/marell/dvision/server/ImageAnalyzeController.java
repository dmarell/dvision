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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ImageAnalyzeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<String/*cameraName*/, Slot> slots = new HashMap<>();
    private FaceDetector faceDetector;

    @Autowired
    private TimeSource timeSource;

    @RequestMapping(value = "/image-analyze-request/{cameraName}", method = RequestMethod.POST)
    public ResponseEntity<ImageAnalyzeResponse> postMotionDetectionRequest(
            @PathVariable String cameraName,
            @RequestParam MultipartFile file) throws IOException {

        // Face detection fails in docker container:
        // UnsatisfiedLinkError: no jniopencv_highgui in java.library.path] with root cause
        // UnsatisfiedLinkError: /tmp/javacpp845418228960/libjniopencv_highgui.so: libgtk-x11-2.0.so.0: cannot open shared object file: No such file or directory
        // at java.lang.ClassLoader$NativeLibrary.load(Native Method)
        // at java.lang.ClassLoader.loadLibrary0(ClassLoader.java:1938)
        // at java.lang.ClassLoader.loadLibrary(ClassLoader.java:1821)

        faceDetector = new FaceDetector();
        ImageAnalyzeRequest request = new ImageAnalyzeRequest(cameraName);
        Slot slot = slots.get(request.getCameraName());
        BufferedImage bImage = DvisionImageUtil.createBufferedImage(file.getBytes());
        ImageAnalyzeResponse emptyResponse = new ImageAnalyzeResponse(System.currentTimeMillis(),
                new ImageSize(bImage.getWidth(), bImage.getHeight()), new ArrayList<>(), new ArrayList<>());
        if (slot == null) {
            slots.put(request.getCameraName(), new Slot(request, bImage));
            return new ResponseEntity<>(emptyResponse, HttpStatus.OK);
        }

        // Search for motion between image in slot and image in request
        ImageAnalyzeResponse response = analyzeImage(slot, bImage, slot.image);
        slot.image = bImage;
        if (response.getMotionAreas().isEmpty()) {
            return new ResponseEntity<>(emptyResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ImageAnalyzeResponse analyzeImage(Slot slot, BufferedImage image1, BufferedImage image2) {
        log.debug("Camera {}, analyzing image", slot.request.getCameraName());
        List<ImageRectangle> motionAreas = slot.motionDetector.getMotionAreas(image1, image2);
        List<LabeledRectangle> faces = faceDetector.getFaces(image1);
        ImageAnalyzeResponse response;
        slot.markedImage = image1;
        response = new ImageAnalyzeResponse(
                timeSource.currentTimeMillis(),
                new ImageSize(image1.getWidth(), image1.getHeight()),
                motionAreas,
                faces);
        log.debug("Camera {}, found motion areas: {}", slot.request.getCameraName(), motionAreas.size());
        return response;
    }

    private static class Slot {
        ImageAnalyzeRequest request;
        BufferedImage image;
        MotionDetector motionDetector;
        BufferedImage markedImage;

        public Slot(ImageAnalyzeRequest request, BufferedImage image) {
            this.request = request;
            motionDetector = new MotionDetector();
            this.image = image;
        }
    }
}
