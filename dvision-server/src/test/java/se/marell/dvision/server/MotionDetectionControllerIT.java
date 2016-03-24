/*
 * Created by Daniel Marell 15-06-24 09:13
 */
package se.marell.dvision.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.marell.dvision.api.ImageData;
import se.marell.dvision.api.ImageRectangle;
import se.marell.dvision.api.MotionDetectionRequest;
import se.marell.dvision.api.MotionDetectionResponse;
import se.marell.dvision.client.DVisionSpringConfig;
import se.marell.dvision.client.MotionDetectionService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, DVisionSpringConfig.class})
@WebIntegrationTest({"server.port=0"})
public class MotionDetectionControllerIT {
    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Test
    public void shouldDetectMotion() throws Exception {
        MotionDetectionService service = beanFactory.createBean(MotionDetectionService.class);

        BufferedImage image1 = ImageIO.read(getClass().getResourceAsStream("/image1.png"));
        BufferedImage image2 = ImageIO.read(getClass().getResourceAsStream("/image2.png"));

        {
            MotionDetectionResponse r = service.motionDetectionRequest(
                    new MotionDetectionRequest(
                            "cam1",
                            1000,
                            100,
                            Arrays.asList(new ImageRectangle(0, 0, 640, 480)),
                            ImageData.create(image1))
            ).getBody();
            assertThat(r, nullValue());
        }
        {
            MotionDetectionResponse r = service.motionDetectionRequest(
                    new MotionDetectionRequest(
                            "cam1",
                            1000,
                            100,
                            Arrays.asList(new ImageRectangle(0, 0, 640, 480)),
                            ImageData.create(image1))
            ).getBody();
            assertThat(r, nullValue());
        }
        {
            MotionDetectionResponse r = service.motionDetectionRequest(
                    new MotionDetectionRequest(
                            "cam1",
                            1000,
                            100,
                            Arrays.asList(new ImageRectangle(0, 0, 640, 480)),
                            ImageData.create(image2))
            ).getBody();
            assertThat(r.getAreas().size(), greaterThan(0));
            assertThat(r.getImageSize().getWidth(), is(640));
            assertThat(r.getImageSize().getHeight(), is(480));
            assertThat(r.getImage(), notNullValue());
        }
    }
}