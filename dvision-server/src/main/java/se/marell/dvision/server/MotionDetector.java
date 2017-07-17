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
    private CvConverter cvConverter = new CvConverter();

    public List<ImageRectangle> getMotionAreas(BufferedImage bImage1, BufferedImage bImage2) {
        List<ImageRectangle> result = new ArrayList<>();

        opencv_core.IplImage image1 = cvConverter.toIplImage(bImage1);
        opencv_core.IplImage image2 = cvConverter.toIplImage(bImage2);

        opencv_core.CvMemStorage storage = opencv_core.CvMemStorage.create();
        cvClearMemStorage(storage);

        cvSmooth(image1, image1, CV_GAUSSIAN, 9, 9, 2, 2);
        cvSmooth(image2, image2, CV_GAUSSIAN, 9, 9, 2, 2);

        opencv_core.IplImage image1Gray = opencv_core.IplImage.create(image1.width(), image1.height(), IPL_DEPTH_8U, 1);
        cvCvtColor(image1, image1Gray, CV_RGB2GRAY);

        opencv_core.IplImage image2Gray = opencv_core.IplImage.create(image2.width(), image2.height(), IPL_DEPTH_8U, 1);
        cvCvtColor(image2, image2Gray, CV_RGB2GRAY);

        opencv_core.IplImage diffImage = opencv_core.IplImage.create(image1.width(), image1.height(), IPL_DEPTH_8U, 1);

        // perform ABS difference
        cvAbsDiff(image1Gray, image2Gray, diffImage);
        // do some threshold for wipe away useless details
        cvThreshold(diffImage, diffImage, 64, 255, CV_THRESH_BINARY);

        // recognize contours
        opencv_core.CvSeq contour = new opencv_core.CvSeq(null);
        cvFindContours(diffImage, storage, contour, Loader.sizeof(opencv_core.CvContour.class), CV_RETR_LIST, CV_CHAIN_APPROX_SIMPLE);

        while (contour != null && !contour.isNull()) {
            if (contour.elem_size() > 0) {
                CvBox2D cvbox = cvMinAreaRect2(contour, storage);
                if (cvbox != null) {
                    int h = (int) (cvbox.size().height() + 0.5f);
                    int w = (int) (cvbox.size().width() + 0.5f);
                    ImageRectangle rectangle = new ImageRectangle(
                            (int) (cvbox.center().x() - (w / 2)),
                            (int) (cvbox.center().y() - (h / 2)),
                            w, h);
                    result.add(rectangle);
                }
            }
            contour = contour.h_next();
        }

        return result;
    }
}
