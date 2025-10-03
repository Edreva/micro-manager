//package org.micromanager.plugins.DisplayIlluminator;
//
//import net.miginfocom.swing.MigLayout;
//
//import javax.swing.*;
//import java.awt.*;
//
//
//public class PcControlPanel extends JPanel {
//    PcControlPanel(DisplayIlluminatorController controller) {
//        this(
//                controller,
//                new MigLayout("wrap 1, gap 0, insets 0, fill", "[grow]"),
//                new Font("Arial", Font.BOLD, 16)
//        );
//    }
//
//    PcControlPanel(DisplayIlluminatorController controller, MigLayout layout, Font font) {
//        setLayout(layout);
//
//        JLabel widthLabel = new JLabel("PC Width:");
//        widthLabel.setFont(font);
//        JLabel heightLabel = new JLabel("PC Height:");
//        heightLabel.setFont(font);
//        LinkedSliderAndField heightControl = new LinkedSliderAndField(
//                (d, b) -> controller.setHeight("PC", d, b));
//        LinkedSliderAndField widthControl = new LinkedSliderAndField(
//                (d, b) -> controller.setWidth("PC", d, b));
//        heightControl.slider.setMinimum(0);
//        heightControl.slider.setMaximum(controller.getDisplayHeightPx());
//        heightControl.setValue(controller.getHeight("PC"));
//        heightControl.addListeners();
//        widthControl.slider.setMinimum(0);
//        widthControl.slider.setMaximum(controller.getDisplayWidthPx());
//        widthControl.setValue(controller.getWidth("PC"));
//        widthControl.addListeners();
//
//        SyncedSliders pcDimensionSliders = new SyncedSliders(
//                widthControl, widthLabel,
//                heightControl, heightLabel,
//                (d, b) -> controller.setDiameter("PC", d, b));
//
//        this.add(pcDimensionSliders, "growx");
//
//        JLabel innerWidthLabel = new JLabel("PC Inner Width:");
//        innerWidthLabel.setFont(font);
//        JLabel innerHeightLabel = new JLabel("PC Inner Height:");
//        innerHeightLabel.setFont(font);
//        LinkedSliderAndField innerHeightControl = new LinkedSliderAndField(
//                (d, b) -> controller.setInnerHeight("PC", d, b));
//        LinkedSliderAndField innerWidthControl = new LinkedSliderAndField(
//                (d, b) -> controller.setInnerWidth("PC", d, b));
//        innerHeightControl.slider.setMinimum(0);
//        innerHeightControl.slider.setMaximum(controller.getDisplayHeightPx());
//        try {
//            innerHeightControl.setValue(controller.getPcInnerHeight());
//            innerHeightControl.addListeners();
//            innerWidthControl.slider.setMinimum(0);
//            innerWidthControl.slider.setMaximum(controller.getDisplayWidthPx());
//            innerWidthControl.setValue(controller.getPcInnerHeight());
//            innerWidthControl.addListeners();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        SyncedSliders pcInnerDimensionSliders = new SyncedSliders(
//                innerWidthControl, innerWidthLabel,
//                innerHeightControl, innerHeightLabel,
//                (d, b) -> controller.setInnerDiameter("PC", d, b));
//
//        this.add(pcInnerDimensionSliders, "growx");
//    }
//}