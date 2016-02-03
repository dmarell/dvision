/*
 * Created by Daniel Marell 15-06-23 18:38
 */
package se.marell.dvision.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import se.marell.dvision.api.MotionDetectionRequest;
import se.marell.dvision.api.MotionDetectionResponse;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class MotionDetectionService {
    @Autowired
    private Environment environment;

    private String serviceUrl;

    private TestRestTemplate restTemplate;

    @PostConstruct
    public void init() {
        serviceUrl = environment.getRequiredProperty("dvision.baseurl");
        String apiuser = environment.getProperty("dvision.apiuser");
        String apipassword = environment.getProperty("dvision.apipassword");
        if (apiuser != null && apipassword != null) {
            restTemplate = new TestRestTemplate(apiuser, apipassword);
        } else {
            restTemplate = new TestRestTemplate();
        }
    }

    public void requestMotionDetection(MotionDetectionRequest request) throws IOException {
        try {
            restTemplate.postForEntity(serviceUrl + "/motiondetectionrequest", request, null);
        } catch (RestClientException e) {
            throw new IOException("Request failed: " + e.getMessage());
        }
    }

    /**
     * Retrieve motion detections for camera since timestamp.
     *
     * @param cameraName A camera name
     * @param since      Timestamp for earliest detection of interest. 0=returns all stored detections for this camera
     * @return A response object or null if no such jobId is known
     */
    public ResponseEntity<MotionDetectionResponse> getMotionDetections(String cameraName, long since) {
        return restTemplate.getForEntity(serviceUrl + "/motiondetectionresponse/{cameraName}?since={since}",
                MotionDetectionResponse.class, cameraName, since);
    }
}
