/*
 * Created by Daniel Marell 15-01-06 11:35
 */
package se.marell.dvision.server;

import se.marell.dcommons.time.PassiveTimer;
import se.marell.dvision.api.NetworkCamera;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Sample test images with a time interval.
 */
public class TestImageSampler {
    public static void main(String[] args) throws Exception {
        List<NetworkCamera> networkCameras = new ArrayList<NetworkCamera>() {{
            add(new NetworkCamera("cam1", "http://83.140.123.181/ImageHarvester/Images/556-slussen_panorama_1.jpg", 3));
        }};

        PassiveTimer timer = new PassiveTimer(5 * 60 * 1000);
        PassiveTimer loopTimer = new PassiveTimer(1000);
        int n = 1;
        while (timer.isRunning()) {
            loopTimer.restart();
            for (NetworkCamera camera : networkCameras) {
                try {
                    System.out.print("Grabbing image from " + camera.getName() + "...");
                    BufferedImage image = getBufferedImage(camera.getUrl());
                    System.out.print("grabbed...");
                    saveImage(camera.getName(), image, n);
                    System.out.println("saved,n: " + n);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            n++;
            if (loopTimer.isRunning()) {
                Thread.sleep(loopTimer.getRemainingTime());
            }
        }
    }

    private static BufferedImage getBufferedImage(URL url) throws IOException {
        Authenticator.setDefault(new java.net.Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("user1", "pass1".toCharArray());
            }
        });
        return ImageIO.read(url);
    }

    private static void saveImage(String cameraName, BufferedImage img, int n) {
        try {
            File outputFile = new File(cameraName + "-" + String.format("%05d", n) + ".png");
            ImageIO.write(img, "png", outputFile);
        } catch (IOException e) {
            System.out.println("Failed to save image to png:" + e.getMessage());
        }
    }
}
