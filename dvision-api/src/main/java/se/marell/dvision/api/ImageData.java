/*
 * Created by Daniel Marell 20/03/16.
 */
package se.marell.dvision.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageData {
    private String mediaType;
    private String base64EncodedData;

    public static ImageData create(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            return new ImageData("image/png", Base64.getEncoder().encodeToString(baos.toByteArray()));
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static BufferedImage createBufferedImage(ImageData image) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(image.base64EncodedData)));
    }
}
