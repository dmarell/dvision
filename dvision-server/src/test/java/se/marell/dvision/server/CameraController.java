/*
 * Created by Daniel Marell 15-06-24 10:44
 */
package se.marell.dvision.server;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class CameraController {
    private String imageName;

    @ResponseBody
    @RequestMapping(value = "/image", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage() throws IOException {
        if (imageName == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        InputStream in = getClass().getResourceAsStream(imageName);
        return new ResponseEntity<>(IOUtils.toByteArray(in), HttpStatus.OK);
    }

    public void setImageSource(String imageName) {
        this.imageName = imageName;
    }
}
