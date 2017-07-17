/*
 * Created by Daniel Marell 2017-02-08.
 */
package se.marell.dvision.server;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;

import java.awt.image.BufferedImage;

public class CvConverter {
    private OpenCVFrameConverter.ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();

    public opencv_core.IplImage toIplImage(BufferedImage bufImage) {
        Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
        return iplConverter.convert(java2dConverter.convert(bufImage));
    }
}
