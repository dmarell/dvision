/*
 * Created by Daniel Marell 20/03/16.
 */
package se.marell.dvision.api;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class DvisionImageUtil {

    public static byte[] create(BufferedImage image, String imageSubType) throws IOException {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            ImageIO.write(image, imageSubType, baos);
            baos.flush();
            return baos.toByteArray();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static BufferedImage createBufferedImage(byte[] data) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(data));
    }
}
