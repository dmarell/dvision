/*
 * Created by Daniel Marell 15-06-23 18:38
 */
package se.marell.dvision.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import se.marell.dvision.api.ImageAnalyzeResponse;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Service
public class ImageAnalyzeService {
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
     * Analyze uploaded images.
     *
     * @param cameraName Camera name
     * @param mediaType  Content type (image/png, image/jpeg, ...)
     * @param imageData  Image data
     * @return Response object
     */
    public ResponseEntity<ImageAnalyzeResponse> analyzeImage(String cameraName, String mediaType, byte[] imageData) {
        HttpHeaders partHeaders = new HttpHeaders();
        partHeaders.setContentType(MediaType.parseMediaType(mediaType));
       MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        HttpEntity<Resource> partEntity = new HttpEntity<>(new ByteArrayResource(imageData) {
            @Override
            public String getFilename() {
                return "image";
            }
        }, partHeaders);
        parts.add("file", partEntity);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(parts, headers);
        return restTemplate.exchange(
                serviceUrl + "/image-analyze-request/{cameraName}",
                HttpMethod.POST, entity,
                ImageAnalyzeResponse.class,
                cameraName);
    }
}
