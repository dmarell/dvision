/*
 * Created by Daniel Marell 15-06-23 18:50
 */
package se.marell.dvision.api;

import java.util.List;

public class MotionDetectionResponse {
    private long timestamp;
    private ImageSize imageSize;
    private List<ImageRectangle> areas;

    public MotionDetectionResponse() {
    }

    public MotionDetectionResponse(long timestamp, ImageSize imageSize, List<ImageRectangle> areas) {
        this.timestamp = timestamp;
        this.imageSize = imageSize;
        this.areas = areas;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public void setImageSize(ImageSize imageSize) {
        this.imageSize = imageSize;
    }

    public List<ImageRectangle> getAreas() {
        return areas;
    }

    public void setAreas(List<ImageRectangle> areas) {
        this.areas = areas;
    }
}
