/*
 * Created by Daniel Marell 15-01-06 17:11
 */
package se.marell.dvision.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageRectangle {
    private int x;
    private int y;
    private int width;
    private int height;

    public int getArea() {
        return width * height;
    }

    /**
     * Checks if two boxes intersects.
     *
     * @param anotherRectangle Another rectangle
     * @return true if they intersects
     */
    public boolean intersect(ImageRectangle anotherRectangle) {
        if (getX() + getWidth() - 1 < anotherRectangle.getX()) {
            return false;
        }
        if (getY() + getHeight() - 1 < anotherRectangle.getY()) {
            return false;
        }
        if (anotherRectangle.getX() + anotherRectangle.getWidth() - 1 < getX()) {
            return false;
        }
        if (anotherRectangle.getY() + anotherRectangle.getHeight() - 1 < getY()) {
            return false;
        }
        return true;
    }
}
