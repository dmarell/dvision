/*
 * Created by Daniel Marell 15-01-06 13:53
 */
package se.marell.dvision.server;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MemoryLeakTest {

    @Ignore // Run manually and set low heap, like -Xmx60m or some low working amount
    @Test
    public void testMemoryLeak() throws Exception {
        long t = System.currentTimeMillis();
        BufferedImage bImage = ImageIO.read(this.getClass().getResourceAsStream("/image1.png"));

        OpenCVFrameConverter.ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();

        final int numTimes = 100000;
        for (int i = 0; i < numTimes; ++i) {
            if (System.currentTimeMillis() - t > 1000) {
                System.out.printf("i: %d\n", i);
                t = System.currentTimeMillis();
            }

            Java2DFrameConverter java2dConverter = new Java2DFrameConverter();
            opencv_core.IplImage image = iplConverter.convert(java2dConverter.convert(bImage));

            System.gc();
        }
    }
}
