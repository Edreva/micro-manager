package org.micromanager.plugins.DisplayIlluminator;

public class Utilities {
    public static String colorToHexString(java.awt.Color color) {
        return String.format("%06x", color.getRGB() & 0xFFFFFF);
    }
}
