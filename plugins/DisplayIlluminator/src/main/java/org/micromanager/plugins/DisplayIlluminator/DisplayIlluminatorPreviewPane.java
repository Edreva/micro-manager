package org.micromanager.plugins.DisplayIlluminator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.micromanager.plugins.DisplayIlluminator.DisplayIlluminatorController.DevicePropertyName;
import static org.micromanager.plugins.DisplayIlluminator.DisplayIlluminatorController.DevicePropertyName.*;
import static org.micromanager.plugins.DisplayIlluminator.DisplayIlluminatorController.UpdateSource.UI;

public class DisplayIlluminatorPreviewPane extends JTabbedPane {
    private Map< String, EllipticalShapeImage> previewImages_;
    private final Map<DevicePropertyName, Consumer<String>> setMethodMap;
    private boolean callbackActive = false;
    private DisplayIlluminatorController controller;

    public enum ImageMode { // TODO: Potentially relocate to controller
        DPC("DPC"),
        BF("BF"),
        DF("DF"),
        PC("PC"),
        RB("RB");

        private final String modeName;

        ImageMode(String modeName) {
            this.modeName = modeName;
        }

        @Override
        public String toString() {
            return modeName;
        }
    }

    DisplayIlluminatorPreviewPane(DisplayIlluminatorController controller) {
        previewImages_= new HashMap<String, EllipticalShapeImage>();
        this.controller = controller;

        JPanel offPanel = new JPanel();
        offPanel.setBackground(Color.BLACK);
        this.add("Off", offPanel);
        this.addDpcPanels();
        this.addBfPanel();
        this.addPcPanel();
        this.addDfPanel();

        this.setCenterX(Integer.parseInt(controller.getProperty(CENTER_X)));
        this.setCenterY(Integer.parseInt(controller.getProperty(CENTER_Y)));
//        previewPane.setCenterX(getCenterX() - getDisplayWidthPx()/2); // TODO: Standardise coord system
//        previewPane.setCenterY(getCenterY() - getDisplayHeightPx()/2);

        setMethodMap = new HashMap<>();
        setMethodMap.put(CENTER_X, s -> this.setCenterX(Integer.parseInt(s)));
        setMethodMap.put(CENTER_Y, s -> this.setCenterY(Integer.parseInt(s)));
        setMethodMap.put(ROTATION, s -> this.setRotation(Float.parseFloat(s)));
        setMethodMap.put(COLOR, s -> this.setOuterColor(Color.decode("#" + s)));
        setMethodMap.put(BF_HEIGHT, s -> this.setOuterHeight(ImageMode.BF, Integer.parseUnsignedInt(s)));
        setMethodMap.put(BF_WIDTH, s -> this.setOuterWidth(ImageMode.BF, Integer.parseUnsignedInt(s)));
        setMethodMap.put(DF_HEIGHT, s -> this.setOuterHeight(ImageMode.DF, Integer.parseUnsignedInt(s)));
        setMethodMap.put(DF_WIDTH, s -> this.setOuterWidth(ImageMode.DF, Integer.parseUnsignedInt(s)));
        setMethodMap.put(DPC_COUNT, s -> this.setDpcCount(Integer.parseUnsignedInt(s))); // TODO: Currently unimplemented
        setMethodMap.put(DPC_HEIGHT, s -> this.setOuterHeight(ImageMode.DPC, Integer.parseUnsignedInt(s)));
        setMethodMap.put(DPC_WIDTH, s -> this.setOuterWidth(ImageMode.DPC, Integer.parseUnsignedInt(s)));
        setMethodMap.put(DPC_INNER_HEIGHT, s -> this.setInnerHeight(ImageMode.DPC, Integer.parseUnsignedInt(s)));
        setMethodMap.put(DPC_INNER_WIDTH, s -> this.setInnerWidth(ImageMode.DPC, Integer.parseUnsignedInt(s)));
        setMethodMap.put(PC_HEIGHT, s -> this.setOuterHeight(ImageMode.PC, Integer.parseUnsignedInt(s)));
        setMethodMap.put(PC_WIDTH, s -> this.setOuterWidth(ImageMode.PC, Integer.parseUnsignedInt(s)));
        setMethodMap.put(PC_INNER_HEIGHT, s -> this.setInnerHeight(ImageMode.PC, Integer.parseUnsignedInt(s)));
        setMethodMap.put(PC_INNER_WIDTH, s -> this.setInnerWidth(ImageMode.PC, Integer.parseUnsignedInt(s)));
        setMethodMap.put(RB_INNER_COLOR, s -> this.setInnerColor(ImageMode.RB, Color.decode("#" + s)));
        setMethodMap.put(RB_OUTER_COLOR, s -> this.setOuterColor(ImageMode.RB, Color.decode("#" + s)));


        // Setup Change listeners

        // Listens for when user changes the active image via the plugin ui (clicks on a tab)
        // Controller propagates changes elsewhere
        this.addChangeListener(l -> {
            callbackActive = true;
            controller.setProperty(ACTIVE_IMAGE, getSelectedTabTitle(), UI);
            callbackActive = false;
        });

        // Listens for when active image was changed by controller.
        // If the change didn't originate in this class, synchronise the UI to new value.
        controller.addPropertyChangeListener(ACTIVE_IMAGE, event -> {
            if (!callbackActive) {
                setActiveImage((String)event.getNewValue());
            }
        });

        addImagePropertyChangeListeners();
    }

    // Setup listeners for when any property in setMethodMap is changed by controller.
    private void addImagePropertyChangeListeners() {
        for (DevicePropertyName propertyName: setMethodMap.keySet()) {
            controller.addPropertyChangeListener(propertyName, event -> setProperty(propertyName, event.getNewValue().toString()));
        }
    }

    private String getSelectedTabTitle() {
        return getTitleAt(getSelectedIndex());
    }

    private void addDpcPanels() {
        addDpcPanels(
                Integer.parseInt(controller.getProperty(DPC_COUNT)),
                Integer.parseInt(controller.getProperty(DISPLAY_WIDTH)),
                Integer.parseInt(controller.getProperty(DISPLAY_HEIGHT)),
                Float.parseFloat(controller.getProperty(DPC_WIDTH)),
                Float.parseFloat(controller.getProperty(DPC_HEIGHT)),
                Float.parseFloat(controller.getProperty(DPC_INNER_WIDTH)),
                Float.parseFloat(controller.getProperty(DPC_INNER_HEIGHT)),
                Float.parseFloat(controller.getProperty(ROTATION)),
                Color.decode("#" + controller.getProperty(COLOR))
        );
    }

    private void addBfPanel() {
        addBfPanel(
                Integer.parseInt(controller.getProperty(DISPLAY_WIDTH)),
                Integer.parseInt(controller.getProperty(DISPLAY_HEIGHT)),
                Float.parseFloat(controller.getProperty(DPC_WIDTH)),
                Float.parseFloat(controller.getProperty(DPC_HEIGHT)),
                Float.parseFloat(controller.getProperty(ROTATION)),
                Color.decode("#" + controller.getProperty(COLOR))
        );
    }

    private void addDfPanel() {
        addAnnulusPanel(
                ImageMode.DF,
                Integer.parseInt(controller.getProperty(DISPLAY_WIDTH)),
                Integer.parseInt(controller.getProperty(DISPLAY_HEIGHT)),
                Float.parseFloat(controller.getProperty(DF_WIDTH)),
                Float.parseFloat(controller.getProperty(DF_HEIGHT)),
                Float.parseFloat(controller.getProperty(DF_INNER_WIDTH)),
                Float.parseFloat(controller.getProperty(DF_INNER_HEIGHT)),
                Float.parseFloat(controller.getProperty(ROTATION)),
                Color.decode("#" + controller.getProperty(COLOR))
        );
    }

    private void addPcPanel() {
        addAnnulusPanel(
                ImageMode.PC,
                Integer.parseInt(controller.getProperty(DISPLAY_WIDTH)),
                Integer.parseInt(controller.getProperty(DISPLAY_HEIGHT)),
                Float.parseFloat(controller.getProperty(PC_WIDTH)),
                Float.parseFloat(controller.getProperty(PC_HEIGHT)),
                Float.parseFloat(controller.getProperty(PC_INNER_WIDTH)),
                Float.parseFloat(controller.getProperty(PC_INNER_HEIGHT)),
                Float.parseFloat(controller.getProperty(ROTATION)),
                Color.decode("#" + controller.getProperty(COLOR))
        );
    }

    private void addDpcPanels(int dpcCount, int displayWidthPx, int displayHeightPx,
                              float dpcWidth, float dpcHeight, float dpcInnerWidth, float dpcInnerHeight,
                              float dpcRotation, Color color) {
        for(int i = 0; i < dpcCount; i++)
        {
            String dpcKey = ImageMode.DPC.toString() + (i + 1);
            EllipticalShapeImage ellipticalShapeImage = new EllipticalShapeImage(
                    displayWidthPx, displayHeightPx, dpcWidth, dpcHeight, dpcInnerWidth, dpcInnerHeight,
                    dpcRotation,   (i + 1) * 360.0f / dpcCount, color);

            previewImages_.put(dpcKey, ellipticalShapeImage);
            JPanel dpcImagePanel = new ResizableImagePanel(ellipticalShapeImage.getBufferedImage());
            dpcImagePanel.setBackground(Color.BLACK);
            this.add(dpcKey, dpcImagePanel);
        }
    }

    private void addAnnulusPanel(ImageMode name,
                                 int displayWidthPx, int displayHeightPx,
                                 float outerWidth, float outerHeight,
                                 float innerWidth, float innerHeight,
                                 float rotation, Color color) {
        String modeKey = name.toString();
        EllipticalShapeImage annulusImage = new EllipticalShapeImage(
                displayWidthPx, displayHeightPx, outerWidth, outerHeight,
                innerWidth, innerHeight, rotation, color, Color.BLACK);
        previewImages_.put(modeKey, annulusImage);
        JPanel imagePanel = new ResizableImagePanel(annulusImage.getBufferedImage());
        imagePanel.setBackground(Color.BLACK);
        this.add(modeKey, imagePanel);

    }

    private void addBfPanel(int displayWidthPx, int displayHeightPx,
                           float ovalWidth, float ovalHeight, float ovalRotation, Color color) {
        String bfKey = ImageMode.BF.toString();
        EllipticalShapeImage bfImage = new EllipticalShapeImage(displayWidthPx, displayHeightPx, ovalWidth, ovalHeight, ovalRotation, color);
        previewImages_.put(bfKey, bfImage);
        JPanel bfImagePanel = new ResizableImagePanel(bfImage.getBufferedImage());
        bfImagePanel.setBackground(Color.BLACK);
        this.add(bfKey, bfImagePanel);
    }

    private void setActiveImage(String imageName) {
        for (int i = 0; i < this.getTabCount(); i++) // TODO: store a map somewhere to avoid this loop
        {
            if(this.getTitleAt(i).equals(imageName)) {
                this.setSelectedIndex(i);
            }
        }
    }

    private void forEachImageOfMode(ImageMode imageMode, Consumer<EllipticalShapeImage> method) {
        this.previewImages_.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith(imageMode.toString()))
                .forEach(e -> method.accept(e.getValue()));
    }

    public void setProperty(DevicePropertyName propertyName, String value) {
        this.setMethodMap.get(propertyName).accept(value);
    }


    private void setDpcCount(int dpcCount) {
        // TODO:
        // Remove DPC panels
        // Add new DPC panels
        // Set off
    }

    private void setCenterX(int centerX) {
        this.previewImages_.forEach((k, v) -> v.setXPos(centerX));
        this.repaint();
    }

    private void setCenterY(int centerY) {
        this.previewImages_.forEach((k, v) -> v.setYPos(centerY));
        this.repaint();
    }

    private void setRotation(float angleDegrees) {
        this.previewImages_.forEach((k, v) -> v.setRotation(angleDegrees));
        this.repaint();
    }

    private void setInnerHeight(ImageMode imageMode, int height) {
        forEachImageOfMode(imageMode, m -> m.setEllipseInnerHeight(height));
        this.repaint();
    }

    private void setInnerHeight(int height) {
        previewImages_.forEach((k,image) -> image.setEllipseInnerHeight(height));
        this.repaint();
    }

    private void setInnerWidth(ImageMode imageMode, int width) {
        forEachImageOfMode(imageMode, m -> m.setEllipseInnerWidth(width));
        this.repaint();
    }

    private void setInnerWidth(int width) {
        previewImages_.forEach((k,image) -> image.setEllipseInnerWidth(width));
        this.repaint();
    }

    private void setOuterHeight(ImageMode imageMode, int height) {
        forEachImageOfMode(imageMode, m -> m.setEllipseOuterHeight(height));
        this.repaint();
    }

    private void setOuterHeight(int height) {
        previewImages_.forEach((k,image) -> image.setEllipseOuterHeight(height));
        this.repaint();
    }

    private void setOuterWidth(ImageMode imageMode, int width) {
        forEachImageOfMode(imageMode, m -> m.setEllipseOuterWidth(width));
        this.repaint();
    }

    private void setOuterWidth(int width) {
        previewImages_.forEach((k,image) -> image.setEllipseOuterWidth(width));
        this.repaint();
    }

    private void setDiameter(int diameterInPixels) {
        this.previewImages_.forEach((k, v) -> v.setDiameter(diameterInPixels));
        this.repaint();
    }

    private void setDiameter(ImageMode imageMode, int diameterInPixels) {
        forEachImageOfMode(imageMode, m -> m.setDiameter(diameterInPixels));
        this.repaint();
    }

    private void setInnerDiameter(ImageMode imageMode, int diameterInPixels) {
        forEachImageOfMode(imageMode, m -> m.setInnerDiameter(diameterInPixels));
        this.repaint();
    }

    private void setOuterColor(Color color) {
        this.previewImages_.forEach((k, v) -> v.setOuterColor(color));
        this.repaint();
    }

    private void setInnerColor(Color color) {
        this.previewImages_.forEach((k, v) -> v.setInnerColor(color));
        this.repaint();
    }

    private void setOuterColor(ImageMode imageMode, Color color) {
        forEachImageOfMode(imageMode, m -> m.setOuterColor(color));
        this.repaint();
    }

    private void setInnerColor(ImageMode imageMode, Color color) {
        forEachImageOfMode(imageMode, m -> m.setInnerColor(color));
        this.repaint();
    }
}
