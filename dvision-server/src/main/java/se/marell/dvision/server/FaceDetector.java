/*
 * Created by Daniel Marell 13-01-20 12:08 PM
 */
package se.marell.dvision.server;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_objdetect;
import se.marell.dvision.api.ImageRectangle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_objdetect.CV_HAAR_DO_CANNY_PRUNING;
import static org.bytedeco.javacpp.opencv_objdetect.cvHaarDetectObjects;

public class FaceDetector {
    private opencv_core.CvMemStorage storage;
    private opencv_objdetect.CvHaarClassifierCascade classifier;

    public FaceDetector() {
        // Preload the opencv_objdetect module to work around a known bug.
        Loader.load(opencv_objdetect.class);

        loadHaarCascadeXml();
    }

    public List<ImageRectangle> getFaceAreas(BufferedImage bImage) {
        // FAQ about IplImage:
        // - For custom raw processing of data, getByteBuffer() returns an NIO direct
        //   buffer wrapped around the memory pointed by imageData, and under Android we can
        //   also use that Buffer with Bitmap.copyPixelsFromBuffer() and copyPixelsToBuffer().
        // - To get a BufferedImage from an IplImage, we may call getBufferedImage().
        // - The createFrom() factory method can construct an IplImage from a BufferedImage.
        // - There are also a few copy*() methods for BufferedImage<->IplImage data transfers.
        opencv_core.IplImage grabbedImage = opencv_core.IplImage.createFrom(bImage);
        int width = grabbedImage.width();
        int height = grabbedImage.height();

        IplImage grayImage = IplImage.create(width, height, IPL_DEPTH_8U, 1);

        // Objects allocated with a create*() or clone() factory method are automatically released
        // by the garbage collector, but may still be explicitly released by calling release().
        // You shall NOT call cvReleaseImage(), cvReleaseMemStorage(), etc. on objects allocated this way.
        storage = CvMemStorage.create();

        cvClearMemStorage(storage);

        // Detect faces from grayscale image
        cvCvtColor(grabbedImage, grayImage, CV_BGR2GRAY);
        CvSeq faces = cvHaarDetectObjects(grayImage, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
        List<ImageRectangle> result = new ArrayList<>();
        int total = faces.total();
        for (int i = 0; i < total; i++) {
            CvRect r = new CvRect(cvGetSeqElem(faces, i));
            result.add(new ImageRectangle(r.x(), r.y(), r.width(), r.height()));
        }
        return result;
    }

    private void loadHaarCascadeXml() {
        try {
            // Load the classifier file from Java resources.
            File classifierFile = Loader.extractResource(getClass(), "/haarcascade_frontalface_alt.xml",
                    null, "classifier", ".xml");
            if (classifierFile == null || classifierFile.length() <= 0) {
                throw new IllegalStateException("Could not extract the classifier file from Java resource.");
            }

            // Preload the opencv_objdetect module to work around a known bug.
            Loader.load(opencv_objdetect.class);
            classifier = new opencv_objdetect.CvHaarClassifierCascade(cvLoad(classifierFile.getAbsolutePath()));
            classifierFile.delete();
            if (classifier.isNull()) {
                throw new IOException("Could not load the classifier file.");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not extract the classifier file from Java resource.");
        }
    }
}