package org.micromanager.plugins.DisplayIlluminator;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DisplayIlluminatorPreviewPane extends JTabbedPane {
    private Map< String, OvalSegmentImage > previewImages_;

    DisplayIlluminatorPreviewPane() {
        JPanel offPanel = new JPanel();
        offPanel.setBackground(Color.BLACK);
        this.add("Off", offPanel);
    }

    public void addDpcPanels(int dpcCount, int displayWidthPx, int displayHeightPx,
                             float ovalWidth, float ovalHeight, float ovalRotation, Color color) {
        previewImages_= new HashMap<String, OvalSegmentImage>();
        for(int i = 0; i < dpcCount; i++)
        {
            String dpcKey = String.format("DPC%d", i+1);
            OvalSegmentImage ovalSegmentImage = new OvalSegmentImage(
                    displayWidthPx, displayHeightPx, ovalWidth, ovalHeight,
                    ovalRotation, (90.0f * i) % 360, color);
            previewImages_.put(dpcKey, ovalSegmentImage);
            JPanel dpcImagePanel = new ResizableImagePanel(ovalSegmentImage.getBufferedImage());
            dpcImagePanel.setBackground(Color.BLACK);
            this.add(dpcKey, dpcImagePanel);
        }
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

    public void setOvalHeight(int heightInPixels) {
        this.previewImages_.forEach((k, v) -> v.setOvalHeight(heightInPixels));
        this.repaint();
    }

    public void setOvalWidth(int widthInPixels) {
        this.previewImages_.forEach((k, v) -> v.setOvalWidth(widthInPixels));
        this.repaint();
    }

    public void setDiameter(int diameterInPixels) {
        this.previewImages_.forEach((k, v) -> v.setDiameter(diameterInPixels));
        this.repaint();
    }

    public void setSegmentColor(Color color) {
        this.previewImages_.forEach((k, v) -> v.setSegmentColor(color));
        this.repaint();
    }
}
