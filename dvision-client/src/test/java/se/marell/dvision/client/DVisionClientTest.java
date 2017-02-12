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

import javax.imageio.ImageIO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DVisionSpringConfig.class})
@PropertySource("classpath:/test.properties")
@Configuration
public class DVisionClientTest {
    @Autowired
    private ImageAnalyzeService service;

    @Ignore // TODO start server, correct URLs
    @Test
    public void testName() throws Exception {
        System.out.println("response: " + service.analyzeImage("cam1", ImageIO.read(this.getClass().getResourceAsStream("/image1.png"))).toString());
        System.out.println("response: " + service.analyzeImage("cam1", ImageIO.read(this.getClass().getResourceAsStream("/image2.png"))).toString());
    }
}
