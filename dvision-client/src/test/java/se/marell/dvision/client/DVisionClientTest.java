/*
 * Created by Daniel Marell 03/02/16.
 */
package se.marell.dvision.client;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DVisionSpringConfig.class})
@PropertySource("classpath:/test.properties")
@Configuration
public class DVisionClientTest {
    @Autowired
    private MotionDetectionService service;

    @Ignore // TODO start server, correct URLs
    @Test
    public void testName() throws Exception {
//        service.requestMotionDetection(new MotionDetectionRequest(
//                new NetworkCamera("cam1", "http://marell.se/cam1.jpg", 1000),
//                100, 100, Arrays.asList(new ImageRectangle(0, 0, 100, 100))));
//        ResponseEntity<MotionDetectionResponse> r = service.getMotionDetections("cam1", 0);
//
    }
}
