/*
 * Created by Daniel Marell 13-01-20 12:08 PM
 */
package se.marell.dvision.server;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import se.marell.dvision.api.ImageRectangle;
import se.marell.dvision.api.LabeledRectangle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class FaceDetector {
    private opencv_objdetect.CvHaarClassifierCascade classifier;
    private opencv_objdetect.CascadeClassifier face_cascade;

    public FaceDetector() {
        loadHaarCascadeXml();
    }

    public List<LabeledRectangle> getFaces(BufferedImage bImage) {
        Mat inputMat = new OpenCVFrameConverter.ToMat().convert(new Java2DFrameConverter().convert(bImage));
        Mat matGray = new Mat();
        // Convert the current frame to grayscale:
        cvtColor(inputMat, matGray, COLOR_BGRA2GRAY);
        equalizeHist(matGray, matGray);

        RectVector faces = new RectVector();
        // Find the faces in the frame:
        face_cascade.detectMultiScale(matGray, faces);

        List<LabeledRectangle> result = new ArrayList<>();
        for (int i = 0; i < faces.size(); i++) {
            Rect r = faces.get(i);
            String label = null; // TODO implement face recognition
            result.add(new LabeledRectangle(new ImageRectangle(r.x(), r.y(), r.width(), r.height()), label));
        }
        return result;
    }

    private void loadHaarCascadeXml() {
        try {
            // Load the classifier file from Java resources.
            File classifierFile = Loader.extractResource(getClass(), "/haarcascade_frontalface_default.xml",
                    null, "classifier", ".xml");
            if (classifierFile == null || classifierFile.length() <= 0) {
                throw new IllegalStateException("Could not extract the classifier file from Java resource.");
            }

            // Preload the opencv_objdetect module to work around a known bug.
            face_cascade = new opencv_objdetect.CascadeClassifier(classifierFile.getAbsolutePath());

        } catch (IOException e) {
            throw new IllegalStateException("Could not extract the classifier file from Java resource.");
        }
    }
}