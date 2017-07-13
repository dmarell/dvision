/*
 * Created by Daniel Marell 15-06-23 18:38
 */
package se.marell.dvision.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import se.marell.dvision.api.DvisionImageUtil;
import se.marell.dvision.api.ImageAnalyzeResponse;

import javax.annotation.PostConstruct;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

@Service
public class ImageAnalyzeService {
    @Autowired
    private Environment environment;

    private String serviceUrl;

    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @PostConstruct
    public void init() {
        serviceUrl = environment.getRequiredProperty("dvision.baseurl");
        String apiuser = environment.getProperty("dvision.apiuser");
        String apipassword = environment.getProperty("dvision.apipassword");
        if (apiuser != null && apipassword != null) {
            restTemplate = restTemplateBuilder.basicAuthorization(apiuser, apipassword).build();
        } else {
            restTemplate = restTemplateBuilder.build();
        }
    }

    /**
     * Analyze uploaded images.
     *
     * @param cameraName Camera name
     * @param image      Image data
     * @return Response object
     */
    public ResponseEntity<ImageAnalyzeResponse> analyzeImage(String cameraName, BufferedImage image) {
        MultiValueMap<String, Object> parts;
        try {
            HttpHeaders partHeaders = new HttpHeaders();
            byte[] imageData = DvisionImageUtil.create(image, "png");
            partHeaders.setContentType(MediaType.IMAGE_PNG);
            parts = new LinkedMultiValueMap<>();
            HttpEntity<Resource> partEntity = new HttpEntity<>(new ByteArrayResource(imageData) {
                @Override
                public String getFilename() {
                    return "image";
                }
            }, partHeaders);
            parts.add("file", partEntity);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected IOException converting BufferedImage", e);
        }

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
