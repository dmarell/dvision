/*
 * Created by Daniel Marell 15-06-24 09:13
 */
package se.marell.dvision.server;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.marell.dvision.api.MotionDetectionRequest;
import se.marell.dvision.api.MotionDetectionResponse;
import se.marell.dvision.client.DVisionSpringConfig;
import se.marell.dvision.client.MotionDetectionService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, DVisionSpringConfig.class})
@WebIntegrationTest({"server.port=23465"})
@TestPropertySource(properties = "dvision.baseurl=http://localhost:23465")
public class MotionDetectionControllerIT {
    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Test
    public void shouldDetectMotion() throws Exception {
        MotionDetectionService service = beanFactory.createBean(MotionDetectionService.class);
        byte[] image1Bytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/image1.png"));
        byte[] image2Bytes = IOUtils.toByteArray(this.getClass().getResourceAsStream("/image2.png"));

        {
            ResponseEntity<MotionDetectionResponse> r = service.motionDetectionRequest(
                    new MotionDetectionRequest("cam1"),
                    "image/png",
                    image1Bytes);
            assertThat(r.getStatusCode().is2xxSuccessful(), is(true));
            assertThat(r.getBody(), nullValue());
        }
        {
            ResponseEntity<MotionDetectionResponse> r = service.motionDetectionRequest(
                    new MotionDetectionRequest("cam1"),
                    "image/png",
                    image1Bytes);
            assertThat(r.getStatusCode().is2xxSuccessful(), is(true));
            assertThat(r.getBody(), nullValue());
        }
        {
            ResponseEntity<MotionDetectionResponse> r = service.motionDetectionRequest(
                    new MotionDetectionRequest("cam1"),
                    "image/png",
                    image2Bytes);
            assertThat(r.getStatusCode().is2xxSuccessful(), is(true));
            assertThat(r.getBody().getAreas().size(), greaterThan(0));
            assertThat(r.getBody().getImageSize().getWidth(), is(640));
            assertThat(r.getBody().getImageSize().getHeight(), is(480));
        }
    }
}