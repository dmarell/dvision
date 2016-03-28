/*
 * Created by Daniel Marell 28/03/16.
 */
package se.marell.dvision.api;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class ImageDrawer {
    public static void drawMotionRectangles(BufferedImage image, Color color, java.util.List<ImageRectangle> areas) {
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rectangles
        g.setColor(color);
        g.setStroke(new BasicStroke(1));
        for (ImageRectangle area : areas) {
            int x = area.getX();
            int y = area.getY();
            int w = area.getWidth();
            int h = area.getHeight();
            g.drawRect(x, y, w, h);
        }
        g.dispose();
    }
}
