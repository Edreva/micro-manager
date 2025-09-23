package org.micromanager.plugins.DisplayIlluminator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EllipticalShapeImage {
    private int frameWidth_;
    private int frameHeight_;
    private float ellipseOuterWidth_;
    private float ellipseInnerWidth_ = -1.0f;
    private float ellipseOuterHeight_;
    private float ellipseInnerHeight_ = -1.0f;
    private float ellipseRotation_; // Degrees
    private float segmentRotation_; // Degrees
    private Arc2D innerArc_ = null;
    private Arc2D outerArc_ = null;
    private BufferedImage image_;
    private float segmentRotationOffset_;
    private float segmentExtent_;
    private int xPos_;
    private int yPos_;
    private Color bkgColor_;
    private Color outerColor_ = null;
    private Color innerColor_ = null;
    private String groupName_;

    // Default
    public EllipticalShapeImage(int frameWidth, int frameHeight,
                                float ellipseWidth, float ellipseHeight,
                                float ellipseRotation, Color ellipseColor) {

        initializeImageFrame(frameWidth, frameHeight, Color.BLACK);
        initializeOval(ellipseWidth, ellipseHeight, ellipseRotation, 0,0, ellipseColor);
        updateArcs();
        updateImage();
    }

    private void initializeImageFrame(int frameWidth, int frameHeight, Color bkgColor) {
        frameWidth_ = frameWidth;
        frameHeight_ = frameHeight;
        bkgColor_ = bkgColor;
        image_ = new BufferedImage(frameWidth, frameHeight, java.awt.image.BufferedImage.TYPE_INT_RGB);
    }

    private void initializeOval(float ellipseWidth, float ellipseHeight, float ellipseRotation, int xPos, int yPos, Color ellipseColor) {
        ellipseOuterWidth_ = ellipseWidth;
        ellipseOuterHeight_ = ellipseHeight;
        ellipseRotation_ = ellipseRotation;
        outerColor_ = ellipseColor;
        xPos_ = xPos;
        yPos_ = yPos;
        segmentRotation_ = 0.0f;
        segmentRotationOffset_ = 0.0f;
        segmentExtent_ = 360.0f;
        outerArc_ = new Arc2D.Float();
    }

    private void initializeAnnulus(float annulusOuterWidth, float annulusOuterHeight, float annulusInnerWidth, float annulusInnerHeight,
                                   float annulusRotation, int xPos, int yPos, Color outerColor, Color innerColor) {
        initializeOval(annulusOuterWidth, annulusOuterHeight, annulusRotation, xPos, yPos, outerColor);
        ellipseInnerHeight_ = annulusInnerHeight;
        ellipseInnerWidth_ = annulusInnerWidth;
        innerColor_ = innerColor;
        innerArc_ = new Arc2D.Float();
    }

    // half-oval
    public EllipticalShapeImage(int frameWidth, int frameHeight,
                                float ellipseWidth, float ellipseHeight,
                                float ellipseRotation, float segmentRotation,
                                Color segmentColor) {
        initializeImageFrame(frameWidth, frameHeight, Color.BLACK);
        initializeOval(ellipseWidth, ellipseHeight, ellipseRotation, 0, 0, segmentColor);
        segmentRotation_ = segmentRotation;
        segmentRotationOffset_ = -90.0f;
        segmentExtent_ = 180.0f; // Half-circle

        updateArcs();
        updateImage();
    }

    // annulus
    public EllipticalShapeImage(int frameWidth, int frameHeight,
                                float ellipseOuterWidth, float ellipseOuterHeight,
                                float ellipseInnerWidth, float ellipseInnerHeight,
                                float ellipseRotation, Color outerColor, Color innerColor) {
        initializeImageFrame(frameWidth, frameHeight, Color.BLACK);
        initializeAnnulus(ellipseOuterWidth, ellipseOuterHeight, ellipseInnerWidth, ellipseInnerHeight, ellipseRotation, 0, 0, outerColor, innerColor);

        updateArcs();
        updateImage();
    }

    // half-annulus
    public EllipticalShapeImage(int frameWidth, int frameHeight,
                                float ellipseOuterWidth, float ellipseOuterHeight,
                                float ellipseInnerWidth, float ellipseInnerHeight,
                                float ellipseRotation, float segmentRotation,
                                Color segmentColor) {

        initializeImageFrame(frameWidth, frameHeight, Color.BLACK);
        initializeAnnulus(ellipseOuterWidth, ellipseOuterHeight, ellipseInnerWidth, ellipseInnerHeight,
                ellipseRotation, 0, 0, segmentColor, Color.BLACK);
        segmentRotation_ = segmentRotation;
        segmentRotationOffset_ = -90.0f;
        segmentExtent_ = 180.0f; // Half-circle

        updateArcs();
        updateImage();
    }

    public void setGroupName(String groupName) {
        groupName_ = groupName;
    }

    public String getGroupName(String groupName) {
        return groupName_;
    }

    public void setColor(Color color) {
        outerColor_ = color;
        innerColor_ = color;
        updateImage();
    }

    public void setOuterColor(Color color) {
        outerColor_ = color;
        updateImage();
    }

    public void setInnerColor(Color color) {
        innerColor_ = color;
        updateImage();
    }

    public void setRotation (float angle) {
        ellipseRotation_ = angle;
        updateImage();

    }

    public void setEllipseOuterHeight(float height) {
        ellipseOuterHeight_ = height;
        updateArcs();
        updateImage();
    }

    public void setEllipseOuterWidth(float width) {
        ellipseOuterWidth_ = width;
        updateArcs();
        updateImage();
    }

    public void setEllipseInnerHeight(float height) {
        ellipseInnerHeight_ = height;
        updateArcs();
        updateImage();
    }

    public void setEllipseInnerWidth(float width) {
        ellipseInnerWidth_ = width;
        updateArcs();
        updateImage();
    }

    public void setDiameter (float diameter) {
        ellipseOuterHeight_ = diameter;
        ellipseOuterWidth_ = diameter;
        updateArcs();
        updateImage();
    }

    public void setInnerDiameter (float diameter) {
        ellipseInnerHeight_ = diameter;
        ellipseInnerWidth_ = diameter;
        updateArcs();
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

    private void updateArcs() {
        outerArc_.setArc(-ellipseOuterWidth_/2.0, -ellipseOuterHeight_/2.0,
                ellipseOuterWidth_, ellipseOuterHeight_, segmentRotationOffset_ - segmentRotation_, segmentExtent_, Arc2D.PIE);
        if (innerArc_ != null) {
            innerArc_.setArc(-ellipseInnerWidth_/2.0, -ellipseInnerHeight_/2.0,
                    ellipseInnerWidth_, ellipseInnerHeight_, segmentRotationOffset_ - segmentRotation_, segmentExtent_, Arc2D.PIE);
        }
    }

    private void updateImage() {
        Graphics2D graphics = image_.createGraphics();
        graphics.setColor(bkgColor_);
        graphics.fillRect(0, 0, frameWidth_, frameHeight_);
        graphics.setColor(outerColor_);
        double xShift = frameWidth_/2.0 + xPos_;
        double yShift = frameHeight_/2.0 +yPos_;
        graphics.translate(xShift, yShift);
        graphics.rotate(Math.toRadians(ellipseRotation_));
//        graphics.draw(outerArc_);
        graphics.fill(outerArc_);
        if (innerArc_ != null) {
            graphics.setColor(innerColor_);
            graphics.draw(innerArc_);
            graphics.fill(innerArc_);
        }
        graphics.dispose();
        image_.flush();
    }
}
