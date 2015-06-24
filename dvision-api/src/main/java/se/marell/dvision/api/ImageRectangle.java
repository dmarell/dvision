/*
 * Created by Daniel Marell 15-01-06 17:11
 */
package se.marell.dvision.api;

public class ImageRectangle {
    private int x;
    private int y;
    private int width;
    private int height;

    public ImageRectangle() {
    }

    public ImageRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return String.format("{%d,%d,%d,%d}", x, y, width, height);
    }

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
