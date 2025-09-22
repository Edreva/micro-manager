package org.micromanager.plugins.DisplayIlluminator;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DisplayIlluminatorPreviewPane extends JTabbedPane {
    private Map< String, EllipticalShapeImage> previewImages_;

    DisplayIlluminatorPreviewPane() {
        JPanel offPanel = new JPanel();
        offPanel.setBackground(Color.BLACK);
        this.add("Off", offPanel);
        previewImages_= new HashMap<String, EllipticalShapeImage>();
    }

    public void addDpcPanels(int dpcCount, int displayWidthPx, int displayHeightPx,
                             float ovalWidth, float ovalHeight, float ovalRotation, Color color) {
        for(int i = 0; i < dpcCount; i++)
        {
            String dpcKey = String.format("DPC%d", i+1);
            EllipticalShapeImage ellipticalShapeImage = new EllipticalShapeImage(
                    displayWidthPx, displayHeightPx, ovalWidth, ovalHeight, 0.0f, 0.0f,
                    ovalRotation,   (i + 1) * 360.0f / dpcCount, color);

            previewImages_.put(dpcKey, ellipticalShapeImage);
            JPanel dpcImagePanel = new ResizableImagePanel(ellipticalShapeImage.getBufferedImage());
            dpcImagePanel.setBackground(Color.BLACK);
            this.add(dpcKey, dpcImagePanel);
        }
    }

    public void addBfPanel(int displayWidthPx, int displayHeightPx,
                           float ovalWidth, float ovalHeight, float ovalRotation, Color color) {
        String BF_KEY = "BF"; // TODO: Define an enum
        EllipticalShapeImage bfImage = new EllipticalShapeImage(displayWidthPx, displayHeightPx, ovalWidth, ovalHeight, ovalRotation, color);
        previewImages_.put(BF_KEY, bfImage);
        JPanel bfImagePanel = new ResizableImagePanel(bfImage.getBufferedImage());
        bfImagePanel.setBackground(Color.BLACK);
        this.add(BF_KEY, bfImagePanel);
    }


    private void forEachImageWithPrefix(String prefix, Consumer<EllipticalShapeImage> method) {
        this.previewImages_.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith(prefix))
                .forEach(e -> method.accept(e.getValue()));
    }

    public void setCenterX(int centerX) {
        this.previewImages_.forEach((k, v) -> v.setXPos(centerX));
        this.repaint();
    }

    public void setCenterY(int centerY) {
        this.previewImages_.forEach((k, v) -> v.setYPos(centerY));
        this.repaint();
    }

    public void setRotation(float angleDegrees) {
        this.previewImages_.forEach((k, v) -> v.setRotation(angleDegrees));
        this.repaint();
    }

    public void setInnerHeight(String imageGroupPrefix, int height) {
        forEachImageWithPrefix(imageGroupPrefix, m -> m.setEllipseInnerHeight(height));
        this.repaint();
    }

    public void setInnerHeight(int height) {
        previewImages_.forEach((k,image) -> image.setEllipseInnerHeight(height));
        this.repaint();
    }

    public void setInnerWidth(String imageGroupPrefix, int width) {
        forEachImageWithPrefix(imageGroupPrefix, m -> m.setEllipseInnerWidth(width));
        this.repaint();
    }

    public void setInnerWidth(int width) {
        previewImages_.forEach((k,image) -> image.setEllipseInnerWidth(width));
        this.repaint();
    }

    public void setOuterHeight(String imageGroupPrefix, int height) {
        forEachImageWithPrefix(imageGroupPrefix, m -> m.setEllipseOuterHeight(height));
        this.repaint();
    }

    public void setOuterHeight(int height) {
        previewImages_.forEach((k,image) -> image.setEllipseOuterHeight(height));
        this.repaint();
    }

    public void setOuterWidth(String imageGroupPrefix, int width) {
        forEachImageWithPrefix(imageGroupPrefix, m -> m.setEllipseOuterWidth(width));
        this.repaint();
    }

    public void setOuterWidth(int width) {
        previewImages_.forEach((k,image) -> image.setEllipseOuterWidth(width));
        this.repaint();
    }

    public void setDiameter(int diameterInPixels) {
        this.previewImages_.forEach((k, v) -> v.setDiameter(diameterInPixels));
        this.repaint();
    }

    public void setDiameter(String imageGroupPrefix, int diameterInPixels) {
        forEachImageWithPrefix(imageGroupPrefix, m -> m.setDiameter(diameterInPixels));
        this.repaint();
    }

    public void setInnerDiameter(String imageGroupPrefix, int diameterInPixels) {
        forEachImageWithPrefix(imageGroupPrefix, m -> m.setInnerDiameter(diameterInPixels));
        this.repaint();
    }

    public void setOuterColor(Color color) {
        this.previewImages_.forEach((k, v) -> v.setOuterColor(color));
        this.repaint();
    }

    public void setInnerColor(Color color) {
        this.previewImages_.forEach((k, v) -> v.setInnerColor(color));
        this.repaint();
    }

    public void setOuterColor(String imageGroupPrefix, Color color) {
        forEachImageWithPrefix(imageGroupPrefix, m -> m.setOuterColor(color));
        this.repaint();
    }

    public void setInnerColor(String imageGroupPrefix, Color color) {
        forEachImageWithPrefix(imageGroupPrefix, m -> m.setInnerColor(color));
        this.repaint();
    }
}
