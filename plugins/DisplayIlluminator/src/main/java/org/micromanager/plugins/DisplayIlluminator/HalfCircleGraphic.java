package org.micromanager.plugins.DisplayIlluminator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HalfCircleGraphic {
    public static ImageIcon createIcon(Color color, int diameter, int width, int height) {
        return new ImageIcon(createBufferedImage(color, diameter, width, height));
    }
    public static BufferedImage createBufferedImage(Color color, int diameter, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(color);
        graphics.fillOval((width-diameter)/2,(height-diameter)/2, diameter, diameter);
        image.flush();
        return image;
    }
}
