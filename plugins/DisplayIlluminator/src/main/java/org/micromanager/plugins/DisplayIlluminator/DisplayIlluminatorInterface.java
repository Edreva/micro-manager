package org.micromanager.plugins.DisplayIlluminator;

import mmcorej.CMMCore;
import mmcorej.StrVector;
import org.micromanager.Studio;

import java.awt.*;
import java.util.Arrays;

public class DisplayIlluminatorInterface {
    private final CMMCore mmCore;
    private final String deviceName;
    private int displayWidthPx = 0;
    private int displayHeightPx = 0;
    private float pixelSize = 0;

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

    public int getDpcDiameter() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "DpcDiameter"));
    }

    public int getDpcWidth() throws Exception {
        return getDpcDiameter(); // TODO: Update when device adapter is updated
    }

    public int getDpcHeight() throws Exception {
        return getDpcDiameter(); // TODO: Update when device adapter is updated
    }

    public int getDpcCount() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "DpcPatternCount"));
    }

    public int getRotation() throws Exception {
        return Integer.parseInt(mmCore.getProperty(deviceName, "Rotation"));
    }

    public Color getColor() throws Exception {
        return Color.decode("#"+mmCore.getProperty(deviceName, "MonoColor"));
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

    public void setDpcDiameter(int diameterPixels) throws Exception {
        mmCore.setProperty(deviceName, "DpcDiameter", diameterPixels);
    }

    public void setColor(String colorHex) throws Exception {
        mmCore.setProperty(deviceName, "MonoColor", colorHex);
    }


}


