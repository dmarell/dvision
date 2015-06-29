/*
 * Created by Daniel Marell 15-06-28 17:53
 */
package se.marell.dvision.api;

public
class CapturedImage implements Comparable<CapturedImage> {
    private String imageUrl;
    private long timestamp;

    public CapturedImage() {
    }

    public CapturedImage(String imageUrl, long timestamp) {
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(CapturedImage o) {
        return imageUrl.compareTo(o.imageUrl);
    }
}
