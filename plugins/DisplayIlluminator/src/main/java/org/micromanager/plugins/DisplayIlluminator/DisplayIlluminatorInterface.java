package org.micromanager.plugins.DisplayIlluminator;

import mmcorej.CMMCore;
import mmcorej.StrVector;
import org.micromanager.Studio;

import java.awt.*;
import java.util.Arrays;

import static org.apache.commons.lang.StringUtils.capitalize;

public class DisplayIlluminatorInterface {
    private final CMMCore mmCore;
    private final String deviceName;
    private int displayWidthPx = 0;
    private int displayHeightPx = 0;
    private float pixelSize = 0;
    private String[] imageGroups = {"DPC", "BF", "DF", "PC"}; // TODO: Probably best to change to enum throughout

    public DisplayIlluminatorInterface(Studio studio, String deviceName) {
        this.mmCore = studio.getCMMCore();
        this.deviceName = deviceName;
        try {
            displayWidthPx = Integer.parseInt(mmCore.getProperty(deviceName, "DisplayWidth_pixels"));
            displayHeightPx = Integer.parseInt(mmCore.getProperty(deviceName, "DisplayHeight_pixels"));
            pixelSize = Float.parseFloat(mmCore.getProperty(deviceName, "PixelSize_um"));
        } catch (Exception e) {
            studio.logs().logError(e);
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
        return Integer.parseInt(mmCore.getProperty(deviceName, "CenterX"));
    }

    public int getCenterY() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "CenterY"));
    }

    public int getDpcWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "DpcWidth"));
    }

    public int getDpcHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "DpcHeight"));
    }

    public int getDpcInnerWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "DpcInnerHeight"));
    }

    public int getDpcInnerHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "DpcInnerHeight"));
    }

    public int getDpcCount() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "DpcPatternCount"));
    }

    public int getPcWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "PcWidth"));
    }

    public int getPcHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "PcHeight"));
    }

    public int getPcInnerWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "PcInnerHeight"));
    }

    public int getPcInnerHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "PcInnerHeight"));
    }

    public int getDfWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "DfWidth"));
    }

    public int getDfHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "DfHeight"));
    }

    public int getDfInnerWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "DfInnerHeight"));
    }

    public int getDfInnerHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "DfInnerHeight"));
    }

    @Deprecated
    public int getBfWidth() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "BfWidth"));
    }

    @Deprecated
    public int getBfHeight() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "BfHeight"));
    }

    public int getWidth (String imageGroupPrefix) throws Exception {
        return Integer.parseInt(
                mmCore.getProperty(deviceName, capitalize(imageGroupPrefix.toLowerCase())+"Width"));
    }

    public int getHeight (String imageGroupPrefix) throws Exception {
        return Integer.parseInt(
                mmCore.getProperty(deviceName, capitalize(imageGroupPrefix.toLowerCase())+"Height"));
    }

    public int getRotation() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "Rotation"));
    }

    public Color getColor() throws Exception {
        return Color.decode("#"+mmCore.getProperty(deviceName, "MonoColor"));
    }

    public Color getRbOuterColor() throws Exception {
        return Color.decode("#"+mmCore.getProperty(deviceName, "RbOuterColor"));
    }

    public String[] getAvailableImages() throws Exception {
        return mmCore.getAllowedPropertyValues(deviceName, "ActiveImage").toArray();
    }

    public String getActiveImageName() throws Exception {
        return mmCore.getProperty(deviceName, "ActiveImage");
    }

    public void setActiveImage(String imageName) throws Exception {
        if (Arrays.asList(getAvailableImages()).contains(imageName)) {
            mmCore.setProperty(deviceName, "ActiveImage", imageName);
        }
        // TODO: Handle error case
    }

    public void setCenterX(int centerX) throws Exception {
        mmCore.setProperty(deviceName, "CenterX", centerX+displayWidthPx/2); // TODO: Update device adapter to remove need for adjustment based on dims
    }

    public void setCenterY(int centerY) throws Exception {
        mmCore.setProperty(deviceName, "CenterY", centerY+displayHeightPx/2);
    }

    public void setCenter(int centerX, int centerY) throws Exception {
        setCenterX(centerX);
        setCenterY(centerY);
    }

    public void setRotation(int rotation) throws Exception {
        mmCore.setProperty(deviceName, "Rotation", rotation);
    }

    public void setWidth (String imageGroupPrefix, int width) throws Exception {
        mmCore.setProperty(deviceName, capitalize(imageGroupPrefix.toLowerCase())+"Width", width);
    }

    public void setHeight (String imageGroupPrefix, int height) throws Exception {
        mmCore.setProperty(deviceName, capitalize(imageGroupPrefix.toLowerCase())+"Height", height);
    }

    public void setInnerWidth (String imageGroupPrefix, int width) throws Exception {
        mmCore.setProperty(deviceName, capitalize(imageGroupPrefix.toLowerCase())+"InnerWidth", width);
    }

    public void setInnerHeight (String imageGroupPrefix, int height) throws Exception {
        mmCore.setProperty(deviceName, capitalize(imageGroupPrefix.toLowerCase())+"InnerHeight", height);
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
        mmCore.setProperty(deviceName, "RbOuterColor", colorHex);
    }

    public void setColor(String colorHex) throws Exception {
        mmCore.setProperty(deviceName, "MonoColor", colorHex);
    }


}


