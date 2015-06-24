/*
 * Created by Daniel Marell 14-11-16 22:20
 */
package se.marell.dvision.server;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_core;
import se.marell.dvision.api.ImageRectangle;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.bytedeco.javacpp.helper.opencv_imgproc.cvFindContours;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class MotionDetector {
    private opencv_core.IplImage image;
    private opencv_core.IplImage prevImage;
    private opencv_core.IplImage diff;
    private List<ImageRectangle> detectionAreas;
    private int areaSizeThreshold;

    /**
     * @param areaSizeThreshold Ignore areas smaller than this
     * @param detectionAreas Areas of interest. If empty list, whole image is of interest
     */
    public MotionDetector(int areaSizeThreshold, List<ImageRectangle> detectionAreas) {
        this.areaSizeThreshold = areaSizeThreshold;
        this.detectionAreas = detectionAreas;
    }

    public List<ImageRectangle> getMotionAreas(BufferedImage inputImage) {
        List<ImageRectangle> result = new ArrayList<>();

        opencv_core.IplImage frame = opencv_core.IplImage.createFrom(inputImage);
        opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();
        cvClearMemStorage(storage);
        cvSmooth(frame, frame, CV_GAUSSIAN, 9, 9, 2, 2);
        if (image == null) {
            image = opencv_core.IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
            cvCvtColor(frame, image, CV_RGB2GRAY);
        } else {
            prevImage = opencv_core.IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
            prevImage = image;
            image = opencv_core.IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
            cvCvtColor(frame, image, CV_RGB2GRAY);
        }
        if (prevImage != null && (image.height() != prevImage.height() || image.width() != prevImage.width())) {
            prevImage = null;
            diff = null;
        }
        if (diff == null) {
            diff = opencv_core.IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
        }
        if (prevImage != null) {
            // perform ABS difference
            cvAbsDiff(image, prevImage, diff);
            // do some threshold for wipe away useless details
            cvThreshold(diff, diff, 64, 255, CV_THRESH_BINARY);

            // recognize contours
            opencv_core.CvSeq contour = new opencv_core.CvSeq(null);
            cvFindContours(diff, storage, contour, Loader.sizeof(opencv_core.CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);

            while (contour != null && !contour.isNull()) {
                if (contour.elem_size() > 0) {
                    CvBox2D cvbox = cvMinAreaRect2(contour, storage);
                    // test intersection
                    if (cvbox != null) {
                        int h = (int) (cvbox.size().height() + 0.5f);
                        int w = (int) (cvbox.size().width() + 0.5f);
                        if (w * h >= areaSizeThreshold) {
                            ImageRectangle rectangle = new ImageRectangle(
                                    (int) (cvbox.center().x() - (w / 2)),
                                    (int) (cvbox.center().y() - (h / 2)),
                                    w, h);
                            if (detectionAreas.isEmpty() || intersectAny(rectangle, detectionAreas)) {
                                result.add(rectangle);
                            }
                        }
                    }
                }
                contour = contour.h_next();
            }
        }
        return result;
    }

    private boolean intersectAny(ImageRectangle rectangle, List<ImageRectangle> areas) {
        for (ImageRectangle b : areas) {
            if (rectangle.intersect(b)) {
                return true;
            }
        }
        return false;
    }
}
