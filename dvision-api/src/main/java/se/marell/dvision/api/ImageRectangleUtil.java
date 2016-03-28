/*
 * Created by Daniel Marell 27/03/16.
 */
package se.marell.dvision.api;

import java.util.ArrayList;
import java.util.List;

public final class ImageRectangleUtil {
    private ImageRectangleUtil() {
    }

    /**
     * @param areaSizeThreshold Ignore areas smaller than this
     */
    public static List<ImageRectangle> filterByAreaSize(List<ImageRectangle> areas, int areaSizeThreshold) {
        List<ImageRectangle> result = new ArrayList<>();
        for (ImageRectangle a : areas) {
            if (a.getWidth() * a.getHeight() >= areaSizeThreshold) {
                result.add(a);
            }
        }
        return result;
    }

    /**
     * @param detectionAreas Areas of interest. If empty list, whole image is of interest
     */
    public static List<ImageRectangle> filterByDetectionAreas(List<ImageRectangle> areas, List<ImageRectangle> detectionAreas) {
        List<ImageRectangle> result = new ArrayList<>();
        for (ImageRectangle a : areas) {
            if (detectionAreas.isEmpty() || intersectAny(a, detectionAreas)) {
                result.add(a);
            }
        }
        return result;
    }

    public static int calculateAreaSize(List<ImageRectangle> rectangles) {
        int area = 0;
        for (ImageRectangle b : rectangles) {
            area += b.getArea();
        }
        return area;
    }

    public static boolean intersectAny(ImageRectangle rectangle, List<ImageRectangle> areas) {
        for (ImageRectangle b : areas) {
            if (rectangle.intersect(b)) {
                return true;
            }
        }
        return false;
    }
}
