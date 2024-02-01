package org.example;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
public class ColorCollector {
    private BufferedImage image;
    public ColorCollector(File file) throws IOException {
        if (Utility.isValidImageFile(file)) {
            this.image = ImageIO.read(file);
        }else {
            throw new IOException("Unsupported file type: " + Utility.getFileExtension(file));
        }
    }
    public Set<Color> getUniqueColors() {
        Set<Integer> uniqueColors = new HashSet<>();
        // Iterate over each pixel to retrieve colors
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                // Get color at each pixel
                int pixelColor = image.getRGB(x, y);
                uniqueColors.add(pixelColor);
            }
        }
        // Convert integer values to Color objects
        Set<Color> colorObjects = new HashSet<>();
        for (int colorValue : uniqueColors) {
            colorObjects.add(new Color(colorValue, true)); // 'true' to handle alpha values
        }
        return colorObjects;
    }
}