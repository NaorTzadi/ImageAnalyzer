package org.example;
import java.awt.*;
import java.awt.image.BufferedImage;
public class FindPosition {
    public Point findColor(BufferedImage image, Color color, int tolerance) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                if (isColorClose(pixelColor, color, tolerance)) {
                    return new Point(x, y); // Return the first matching position
                }
            }
        }
        return null;
    }
    private boolean isColorClose(Color c1, Color c2, int tolerance) {
        return Math.abs(c1.getRed() - c2.getRed()) <= tolerance &&
                Math.abs(c1.getGreen() - c2.getGreen()) <= tolerance &&
                Math.abs(c1.getBlue() - c2.getBlue()) <= tolerance;
    }
    static class Point {
        int x, y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

}
