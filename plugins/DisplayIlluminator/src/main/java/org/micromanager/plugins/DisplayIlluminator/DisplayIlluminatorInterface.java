package org.micromanager.plugins.DisplayIlluminator;

import mmcorej.CMMCore;

import java.awt.*;
import java.util.Arrays;

import static org.apache.commons.lang.StringUtils.capitalize;

public class DisplayIlluminatorInterface {
    private final CMMCore mmCore;
    private final String deviceLabel;
    private int displayWidthPx = 0;
    private int displayHeightPx = 0;
    private float pixelSize = 0;

    public DisplayIlluminatorInterface(CMMCore core, String deviceLabel) {
        this.mmCore = core;
        this.deviceLabel = deviceLabel;
        try {
            displayWidthPx = Integer.parseInt(mmCore.getProperty(this.deviceLabel, "DisplayWidth_pixels"));
            displayHeightPx = Integer.parseInt(mmCore.getProperty(this.deviceLabel, "DisplayHeight_pixels"));
            pixelSize = Float.parseFloat(mmCore.getProperty(this.deviceLabel, "PixelSize_um"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getDisplayWidthPx() {
        return displayWidthPx;
    }

    public int getDisplayHeightPx() {
        return displayHeightPx;
    }

    public float getPixelSize() {
        return pixelSize;
    }

    public int getCenterX() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "CenterX"));
    }

    public int getCenterY() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "CenterY"));
    }

    public int getDpcWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "DpcWidth"));
    }

    public int getDpcHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "DpcHeight"));
    }

    public int getDpcInnerWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "DpcInnerHeight"));
    }

    public int getDpcInnerHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "DpcInnerHeight"));
    }

    public int getDpcCount() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "DpcPatternCount"));
    }

    public int getPcWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "PcWidth"));
    }

    public int getPcHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "PcHeight"));
    }

    public int getPcInnerWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "PcInnerHeight"));
    }

    public int getPcInnerHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "PcInnerHeight"));
    }

    public int getDfWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "DfWidth"));
    }

    public int getDfHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "DfHeight"));
    }

    public int getDfInnerWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "DfInnerHeight"));
    }

    public int getDfInnerHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "DfInnerHeight"));
    }

    @Deprecated
    public int getBfWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "BfWidth"));
    }

    @Deprecated
    public int getBfHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "BfHeight"));
    }

    public int getWidth (String imageGroupPrefix) throws Exception {
        return Integer.parseInt(
                mmCore.getProperty(deviceLabel, capitalize(imageGroupPrefix.toLowerCase())+"Width"));
    }

    public int getHeight (String imageGroupPrefix) throws Exception {
        return Integer.parseInt(
                mmCore.getProperty(deviceLabel, capitalize(imageGroupPrefix.toLowerCase())+"Height"));
    }

    public int getRotation() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceLabel, "Rotation"));
    }

    public Color getColor() throws Exception {
        return Color.decode("#"+mmCore.getProperty(deviceLabel, "MonoColor"));
    }

    public Color getRbOuterColor() throws Exception {
        return Color.decode("#"+mmCore.getProperty(deviceLabel, "RbOuterColor"));
    }

    public String[] getAvailableImages() throws Exception {
        return mmCore.getAllowedPropertyValues(deviceLabel, "ActiveImage").toArray();
    }

    public String getActiveImageName() throws Exception {
        return mmCore.getProperty(deviceLabel, "ActiveImage");
    }

    public void setActiveImage(String imageName) throws Exception {
        if (Arrays.asList(getAvailableImages()).contains(imageName)) {
            mmCore.setProperty(deviceLabel, "ActiveImage", imageName);
        }
        // TODO: Handle error case
    }

    public void setCenterX(int centerX) throws Exception {
        mmCore.setProperty(deviceLabel, "CenterX", centerX+displayWidthPx/2); // TODO: Update device adapter to remove need for adjustment based on dims
    }

    public void setCenterY(int centerY) throws Exception {
        mmCore.setProperty(deviceLabel, "CenterY", centerY+displayHeightPx/2);
    }

    public void setCenter(int centerX, int centerY) throws Exception {
        setCenterX(centerX);
        setCenterY(centerY);
    }

    public void setRotation(int rotation) throws Exception {
        mmCore.setProperty(deviceLabel, "Rotation", rotation);
    }

    public void setWidth (String imageGroupPrefix, int width) throws Exception {
        mmCore.setProperty(deviceLabel, capitalize(imageGroupPrefix.toLowerCase())+"Width", width);
    }

    public void setHeight (String imageGroupPrefix, int height) throws Exception {
        mmCore.setProperty(deviceLabel, capitalize(imageGroupPrefix.toLowerCase())+"Height", height);
    }

    public void setInnerWidth (String imageGroupPrefix, int width) throws Exception {
        mmCore.setProperty(deviceLabel, capitalize(imageGroupPrefix.toLowerCase())+"InnerWidth", width);
    }

    public void setInnerHeight (String imageGroupPrefix, int height) throws Exception {
        mmCore.setProperty(deviceLabel, capitalize(imageGroupPrefix.toLowerCase())+"InnerHeight", height);
    }

    public void setDiameter(String imageGroupPrefix, int diameter) throws Exception {
        setWidth(imageGroupPrefix, diameter);
        setHeight(imageGroupPrefix, diameter);
    }

    public void setInnerDiameter(String imageGroupPrefix, int diameter) throws Exception {
        setInnerWidth(imageGroupPrefix, diameter);
        setInnerHeight(imageGroupPrefix, diameter);
    }

    public void setRbOuterColor(String colorHex) throws Exception {
        mmCore.setProperty(deviceLabel, "RbOuterColor", colorHex);
    }

    public void setColor(String colorHex) throws Exception {
        mmCore.setProperty(deviceLabel, "MonoColor", colorHex);
    }


}


