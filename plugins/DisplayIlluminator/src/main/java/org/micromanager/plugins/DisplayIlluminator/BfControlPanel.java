//package org.micromanager.plugins.DisplayIlluminator;
//
//import net.miginfocom.swing.MigLayout;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class BfControlPanel extends JPanel {
//    BfControlPanel(DisplayIlluminatorController controller) {
//        this(
//                controller,
//                new MigLayout("wrap 1, gap 0, insets 0, fill", "[grow]"),
//                new Font("Arial", Font.BOLD, 16)
//        );
//    }
//    BfControlPanel(DisplayIlluminatorController controller, MigLayout layout, Font font) {
//        setLayout(layout);
//
//        // Diameter Controls
//        JLabel widthLabel = new JLabel("BF Width:");
//        widthLabel.setFont(font);
//        JLabel heightLabel = new JLabel("BF Height:");
//        heightLabel.setFont(font);
//        LinkedSliderAndField heightControl = new LinkedSliderAndField(
//                (d,b) -> controller.setHeight("BF", d, b));
//        LinkedSliderAndField widthControl = new LinkedSliderAndField(
//                (d,b) -> controller.setWidth("BF", d, b));
//        heightControl.slider.setMinimum(0);
//        heightControl.slider.setMaximum(controller.getDisplayHeightPx());
//        heightControl.setValue(controller.getHeight("BF"));
//        heightControl.addListeners();
//        widthControl.slider.setMinimum(0);
//        widthControl.slider.setMaximum(controller.getDisplayWidthPx());
//        widthControl.setValue(controller.getWidth("BF"));
//        widthControl.addListeners();
//
//        SyncedSliders dimensionSliders = new SyncedSliders(
//                widthControl, idthLabel,
//                heightControl, heightLabel,
//                (d,b) -> controller.setDiameter("BF", d, b));
//
//        this.add(dimensionSliders, "growx");
//    }
//}
