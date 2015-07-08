/*
 * Created by Daniel Marell 14-09-14 18:31
 */
package se.marell.dvision.api;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkCamera {
    private String name;
    private URL url;
    private int captureRate;
    private String username;
    private String password;

    public NetworkCamera() {
    }

    /**
     * @param name Camera name
     * @param url URL to camera image
     * @param captureRate Capture rate in milli seconds
     */
    public NetworkCamera(String name, String url, int captureRate) {
        this.name = name;
        this.url = createUrl(url);
        this.captureRate = captureRate;
    }

    /**
     * @param name Camera name
     * @param url URL to camera image
     * @param captureRate Capture rate in milli seconds
     * @param username username or null if user/pass is not needed
     * @param password password or null if user/pass is not needed
     */
    public NetworkCamera(String name, String url, int captureRate, String username, String password) {
        this.name = name;
        this.url = createUrl(url);
        this.captureRate = captureRate;
        this.username = username;
        this.password = password;
    }

    private URL createUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getCaptureRate() {
        return captureRate;
    }

    public void setCaptureRate(int captureRate) {
        this.captureRate = captureRate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "NetworkCamera{" +
                "name='" + name + '\'' +
                ", url=" + url +
                ", captureRate=" + captureRate +
                ", username=" + username +
                ", password=" + password +
                '}';
    }
}

