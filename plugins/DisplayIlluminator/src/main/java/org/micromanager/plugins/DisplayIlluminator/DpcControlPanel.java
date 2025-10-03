//package org.micromanager.plugins.DisplayIlluminator;
//
//import net.miginfocom.swing.MigLayout;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class DpcControlPanel extends JPanel {
//    private SyncedSliders dimensionSliders;
//    private SyncedSliders innerDimensionSliders;
//
//    void setWidth(int width, boolean updateDisplay) {
//        dimensionSliders.setWidth(width, updateDisplay);
//    }
//    void setInnerWidth(int width, boolean updateDisplay) {
//        innerDimensionSliders.setWidth(width, updateDisplay);
//    }
//    void setHeight(int height, boolean updateDisplay) {
//        dimensionSliders.setHeight(height, updateDisplay);
//    }
//    void setInnerHeight(int height, boolean updateDisplay) {
//        innerDimensionSliders.setHeight(height, updateDisplay);
//    }
//
//    DpcControlPanel(DisplayIlluminatorController controller) {
//        this(
//                controller,
//                new MigLayout("wrap 1, gap 0, insets 0, fill", "[grow]"),
//                new Font("Arial", Font.BOLD, 16)
//        );
//    }
//    DpcControlPanel(DisplayIlluminatorController controller, MigLayout layout, Font font) {
//        setLayout(layout);
//
//        // Outer diameter controls
//        JLabel widthLabel = new JLabel("DPC Width:");
//        widthLabel.setFont(font);
//        JLabel heightLabel = new JLabel("DPC Height:");
//        heightLabel.setFont(font);
//
//        LinkedSliderAndField heightControl = new LinkedSliderAndField(
//                (d, b) -> controller.setHeight("DPC", d, b));
//        LinkedSliderAndField widthControl = new LinkedSliderAndField(
//                (d, b) -> controller.setWidth("DPC", d, b));
//
//        heightControl.slider.setMinimum(0);
//        heightControl.slider.setMaximum(controller.getDisplayHeightPx());
//        heightControl.setValue(controller.getHeight("DPC"));
//        heightControl.addListeners();
//        widthControl.slider.setMinimum(0);
//        widthControl.slider.setMaximum(controller.getDisplayWidthPx());
//        widthControl.setValue(controller.getWidth("DPC"));
//        widthControl.addListeners();
//
//        dimensionSliders = new SyncedSliders(
//                widthControl, widthLabel,
//                heightControl, heightLabel,
//                (d, b) -> controller.setDiameter("DPC", d, b));
//
//        this.add(dimensionSliders, "growx");
//
//        JLabel innerWidthLabel = new JLabel("DPC Inner Width:");
//        innerWidthLabel.setFont(font);
//        JLabel innerHeightLabel = new JLabel("DPC Inner Height:");
//        innerHeightLabel.setFont(font);
//        LinkedSliderAndField innerHeightControl = new LinkedSliderAndField(
//                (d, b) -> controller.setInnerHeight("DPC", d, b));
//        LinkedSliderAndField innerWidthControl = new LinkedSliderAndField(
//                (d, b) -> controller.setInnerWidth("DPC", d, b));
//        innerHeightControl.slider.setMinimum(0);
//        innerHeightControl.slider.setMaximum(controller.getDisplayHeightPx());
//        innerHeightControl.setValue(0);
//        innerHeightControl.addListeners();
//        innerWidthControl.slider.setMinimum(0);
//        innerWidthControl.slider.setMaximum(controller.getDisplayWidthPx());
//        innerWidthControl.setValue(0);
//        innerWidthControl.addListeners();
//
//        innerDimensionSliders = new SyncedSliders(
//                innerWidthControl, innerWidthLabel,
//                innerHeightControl, innerHeightLabel,
//                (d, b) -> controller.setInnerDiameter("DPC", d, b));
//
//        this.add(innerDimensionSliders, "growx");
//        }
//    }
