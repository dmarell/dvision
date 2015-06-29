/*
 * Created by Daniel Marell 15-06-23 18:50
 */
package se.marell.dvision.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MotionDetectionResponse {
    private long timestamp;
    private ImageSize imageSize;
    private List<ImageRectangle> areas;
    private List<CapturedImage> images;

    public MotionDetectionResponse() {
    }

    public MotionDetectionResponse(long timestamp, ImageSize imageSize, List<ImageRectangle> areas, Collection<CapturedImage> images) {
        this.timestamp = timestamp;
        this.imageSize = imageSize;
        this.areas = areas;
        this.images = new ArrayList<>(images);
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

    public List<CapturedImage> getImages() {
        return images;
    }

    public void setImages(Collection<CapturedImage> images) {
        this.images = new ArrayList<>(images);
    }
}

