/*
 * Created by Daniel Marell 15-06-23 18:38
 */
package se.marell.dvision.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import se.marell.dvision.api.MotionDetectionRequest;
import se.marell.dvision.api.MotionDetectionResponse;

import javax.annotation.PostConstruct;
import java.util.Arrays;

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

    /**
     * Retrieve motion detections between images.
     *
     * @param request   Motion detection request
     * @param mediaType Content type (png, jpeg, ...)
     * @param imageData Image data
     * @return A response object or null
     */
    public ResponseEntity<MotionDetectionResponse> motionDetectionRequest(MotionDetectionRequest request,
                                                                          String mediaType, byte[] imageData) {
        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", new ByteArrayResource(imageData) {
            @Override
            public String getFilename() {
                return "image.png";
            }
        });
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(parts, headers);
        return restTemplate.exchange(
                serviceUrl + "/motion-detection-request/{cameraName}",
                HttpMethod.POST, entity,
                MotionDetectionResponse.class,
                request.getCameraName());
    }
}
