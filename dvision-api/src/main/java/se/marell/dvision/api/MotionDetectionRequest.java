/*
 * Created by Daniel Marell 15-06-23 18:34
 */
package se.marell.dvision.api;

import java.util.List;

public class MotionDetectionRequest {
    private NetworkCamera camera;
    private int minAreaSize;
    private int areaSizeThreshold;
    private List<ImageRectangle> detectionAreas;

    public MotionDetectionRequest() {
    }

    /**
     * @param camera Camera name
     * @param minAreaSize Min sum of all areas in order to detect motion
     * @param areaSizeThreshold Ignore areas smaller than this
     * @param detectionAreas List of areas of interest
     */
    public MotionDetectionRequest(NetworkCamera camera, int minAreaSize, int areaSizeThreshold,
                                  List<ImageRectangle> detectionAreas) {
        this.camera = camera;
        this.minAreaSize = minAreaSize;
        this.areaSizeThreshold = areaSizeThreshold;
        this.detectionAreas = detectionAreas;
    }

    public NetworkCamera getCamera() {
        return camera;
    }

    public void setCamera(NetworkCamera camera) {
        this.camera = camera;
    }

    public int getMinAreaSize() {
        return minAreaSize;
    }

    public void setMinAreaSize(int minAreaSize) {
        this.minAreaSize = minAreaSize;
    }

    public int getAreaSizeThreshold() {
        return areaSizeThreshold;
    }

    public void setAreaSizeThreshold(int areaSizeThreshold) {
        this.areaSizeThreshold = areaSizeThreshold;
    }

    public List<ImageRectangle> getDetectionAreas() {
        return detectionAreas;
    }

    public void setDetectionAreas(List<ImageRectangle> detectionAreas) {
        this.detectionAreas = detectionAreas;
    }

    @Override
    public String toString() {
        return "MotionDetectionRequest{" +
                "detectionAreas=" + detectionAreas +
                ", areaSizeThreshold=" + areaSizeThreshold +
                ", minAreaSize=" + minAreaSize +
                ", camera=" + camera +
                '}';
    }
}
