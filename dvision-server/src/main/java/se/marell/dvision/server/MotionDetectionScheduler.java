/*
 * Created by Daniel Marell 15-06-30 18:01
 */
package se.marell.dvision.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to enable @Schedule with certain @Profile:s.
 */
@Component
@Profile({"local", "man", "prod"})
public class MotionDetectionScheduler {
    @Autowired
    private MotionDetectionController controller;

    @Scheduled(fixedRate = 1000)
    public void capture() {
        controller.capture();
    }
}
