/*
 * Created by Daniel Marell 15-06-24 09:13
 */
package se.marell.dvision.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import se.marell.dvision.api.ImageRectangle;
import se.marell.dvision.api.MotionDetectionRequest;
import se.marell.dvision.api.MotionDetectionResponse;
import se.marell.dvision.api.NetworkCamera;
import se.marell.dvision.client.DVisionSpringConfig;
import se.marell.dvision.client.MotionDetectionService;

import java.io.IOException;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, DVisionSpringConfig.class, TestConfig.class})
@WebAppConfiguration
@IntegrationTest
public class MotionDetectionControllerIT {
    private static final int CAPTURE_RATE_OFF = 999999;

    @Autowired
    AutowireCapableBeanFactory beanFactory;

    @Autowired
    MotionDetectionService service;

    @Autowired
    CameraController cameraStub;

    @Autowired
    MotionDetectionController motionDetectorController;

    @Autowired
    TestConfig testConfig;

    @Test
    public void shouldDetectMotion() throws Exception {
        cameraStub.setImageName("/image1.png");
        MotionDetectionService service = beanFactory.createBean(MotionDetectionService.class);
        MotionDetectionRequest request = new MotionDetectionRequest(
                new NetworkCamera("cam1", "http://localhost:14562/image", CAPTURE_RATE_OFF),
                1000, 100, Arrays.asList(new ImageRectangle(0, 0, 640, 480)));
        service.requestMotionDetection(request);
        ResponseEntity<MotionDetectionResponse> responseEntity = service.getMotionDetections("cam1", 0);
        MotionDetectionResponse response = responseEntity.getBody();
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertThat(response, is(nullValue()));
        motionDetectorController.setCaptureInterval("cam1", 0);
        motionDetectorController.capture();
        testConfig.setTime("2015-06-20T12:00:02");
        cameraStub.setImageName("/image2.png");
        motionDetectorController.capture();
        testConfig.setTime("2015-06-20T12:00:04");
        response = service.getMotionDetections("cam1", 0).getBody();
        assertThat(response.getAreas().size(), greaterThan(0));
        assertThat(response.getImages().get(0).getImageUrl(), is("http://localhost:14562/images/20150620120000-cam1.png"));
        assertThat(response.getImages().get(1).getImageUrl(), is("http://localhost:14562/images/20150620120002-cam1.png"));
        assertThat(response.getImages().size(), is(2));
        //TODO check follow image URLs
    }

    @Test(expected = IOException.class)
    public void shouldThrowOnFaultyCameraUrl() throws Exception {
        MotionDetectionService service = beanFactory.createBean(MotionDetectionService.class);
        MotionDetectionRequest r = new MotionDetectionRequest(
                new NetworkCamera("cam1", "http://nothing/here", CAPTURE_RATE_OFF),
                100, 10, Arrays.asList(new ImageRectangle(0, 0, 10, 10)));
        service.requestMotionDetection(r);
    }

    @Test(expected = IOException.class)
    public void shouldThrowOnHttpError() throws Exception {
        MotionDetectionService service = beanFactory.createBean(MotionDetectionService.class);
        MotionDetectionRequest r = new MotionDetectionRequest(
                new NetworkCamera("cam1", "http://localhost:14562/non-existing-endpoint", CAPTURE_RATE_OFF),
                100, 10, Arrays.asList(new ImageRectangle(0, 0, 10, 10)));
        service.requestMotionDetection(r);
    }
}