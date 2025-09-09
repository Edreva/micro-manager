package org.micromanager.plugins.DisplayIlluminator;

import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;

public class ResizableImagePanel extends JPanel {
    private BufferedImage image;

    public ResizableImagePanel(BufferedImage image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // Calculate scaled dimensions maintaining aspect ratio
            Dimension scaledDim = getScaledDimension(
                    new Dimension(image.getWidth(), image.getHeight()),
                    new Dimension(getWidth(), getHeight())
            );

            // Center the image
            int x = (getWidth() - scaledDim.width) / 2;
            int y = (getHeight() - scaledDim.height) / 2;

            g2d.drawImage(image, x, y, scaledDim.width, scaledDim.height, this);
            g2d.dispose();
        }
    }

    private Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        double widthRatio = boundary.getWidth() / imgSize.getWidth();
        double heightRatio = boundary.getHeight() / imgSize.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);

        return new Dimension(
                (int) (imgSize.width * ratio),
                (int) (imgSize.height * ratio)
        );
    }
}