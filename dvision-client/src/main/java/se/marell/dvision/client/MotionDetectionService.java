/*
 * Created by Daniel Marell 15-06-23 18:38
 */
package se.marell.dvision.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import se.marell.dvision.api.MotionDetectionRequest;
import se.marell.dvision.api.MotionDetectionResponse;

import javax.annotation.PostConstruct;

@Service
public class MotionDetectionService {
    @Autowired
    private Environment environment;

    private TestRestTemplate restTemplate;

    @PostConstruct
    public void init() {
        String apiuser = environment.getProperty("dvision.apiuser");
        String apipassword = environment.getProperty("dvision.apipassword");
        if (apiuser != null && apipassword != null) {
            restTemplate = new TestRestTemplate(apiuser, apipassword);
        } else {
            restTemplate = new TestRestTemplate();
        }
    }

    /**
     * Retrieve motion detections for camera.
     *
     * @param request Motion detection request
     * @return A response object or null
     */
    public ResponseEntity<MotionDetectionResponse> motionDetectionRequest(MotionDetectionRequest request) {
        return restTemplate.postForEntity("http://localhost:8080/motion-detection-request",
                request, MotionDetectionResponse.class);
    }
}
