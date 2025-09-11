package org.micromanager.plugins.DisplayIlluminator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

public class OvalSegmentImage {
    private int frameWidth_;
    private int frameHeight_;
    private float ovalWidth_;
    private float ovalHeight_;
    private float ovalRotation_; // Degrees
    private float segmentRotation_; // Degrees
    private Arc2D ovalSegment_;
    private BufferedImage image_;
    private float segmentRotationOffset_;
    private float segmentExtent_;
    private int xPos_;
    private int yPos_;
    private Color bkgColor_;
    private Color segmentColor_;

    public OvalSegmentImage(int frameWidth, int frameHeight,
                            float ovalWidth, float ovalHeight,
                            float ovalRotation, float segmentRotation,
                            Color segmentColor) {
        frameWidth_ = frameWidth;
        frameHeight_ = frameHeight;
        ovalWidth_ = ovalWidth;
        ovalHeight_ = ovalHeight;
        ovalRotation_ = ovalRotation;
        segmentRotation_ = segmentRotation;
        segmentColor_ = segmentColor;
        xPos_ = 0;
        yPos_ = 0;
        segmentRotationOffset_ = -90.0f;
        segmentExtent_ = 180.0f; // Half-circle
        bkgColor_ = Color.BLACK;

        ovalSegment_ = new Arc2D.Float((frameWidth - ovalWidth)/2, (frameHeight-ovalHeight)/2,
                ovalWidth, ovalHeight,segmentRotationOffset_ - segmentRotation_, segmentExtent_, Arc2D.PIE);
        image_ = new BufferedImage(frameWidth, frameHeight, java.awt.image.BufferedImage.TYPE_INT_RGB);

        // Draw to image
        updateImage();
    }

    public void setSegmentColor(Color color) {
        segmentColor_ = color;
        updateImage();
    }

    public void setRotation (float angle) {
        ovalRotation_ = angle;
        updateImage();

    }

    public void setDiameter (int diameter) {
        ovalHeight_ = diameter;
        ovalWidth_ = diameter;
        ovalSegment_.setArc((frameWidth_ - ovalWidth_)/2, (frameHeight_-ovalHeight_)/2,
                ovalWidth_, ovalHeight_, segmentRotationOffset_ + segmentRotation_, segmentExtent_, Arc2D.PIE);
        updateImage();
    }

    public void setXPos (int xPos) {
        xPos_ = xPos;
        updateImage();
    }

    public void setYPos (int yPos) {
        yPos_ = yPos;
        updateImage();
    }

    public BufferedImage getBufferedImage() {
        return image_;
    }

    public ImageIcon createIcon() {
        return new ImageIcon(image_);
    }

    private void updateImage() {
        Graphics2D graphics = image_.createGraphics();
        graphics.setColor(bkgColor_);
        graphics.fillRect(0, 0, frameWidth_, frameHeight_);
        graphics.setColor(segmentColor_);
        graphics.rotate(Math.toRadians(ovalRotation_), (double)frameWidth_/2 + xPos_, (double)frameHeight_/2 + yPos_);
        graphics.translate(xPos_, yPos_);
        graphics.draw(ovalSegment_);
        graphics.fill(ovalSegment_);
        graphics.dispose();
        image_.flush();
    }
}
