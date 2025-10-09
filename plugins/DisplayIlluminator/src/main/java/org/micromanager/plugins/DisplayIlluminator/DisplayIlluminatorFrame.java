/**
 * ExampleFrame.java
 *
 * <p>This module shows an example of creating a GUI (Graphical User Interface).
 * There are many ways to do this in Java; this particular example uses the
 * MigLayout layout manager, which has extensive documentation online.
 *
 * <p>Nico Stuurman, copyright UCSF, 2012, 2015
 *
 * <p>LICENSE: This file is distributed under the BSD license. License text is
 * included with the source distribution.
 *
 * <p>This file is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.
 *
 * <p>IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.
 */

package org.micromanager.plugins.DisplayIlluminator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

import org.micromanager.plugins.DisplayIlluminator.ColorChooserButton.ColorChangedListener;

import net.miginfocom.swing.MigLayout;
import org.micromanager.data.Image;
import org.micromanager.internal.utils.WindowPositioning;

import static org.micromanager.plugins.DisplayIlluminator.Utilities.DevicePropertyName.*;
import static org.micromanager.plugins.DisplayIlluminator.DisplayIlluminatorController.UpdateSource.UI;


public class DisplayIlluminatorFrame extends JFrame {
    private DisplayIlluminatorController controller;
    private Font labelFont;

    private JTabbedPane createMainTabbedPane() {
        JTabbedPane mainTabbedPane = new JTabbedPane();
        JPanel prefacePane  = new JPanel();
        JPanel acquisitionPane = new JPanel();
        JPanel qdpcPane = new JPanel();
        mainTabbedPane.add("Source Pattern", createSourcePatternPanel());
        mainTabbedPane.add("PreFace", prefacePane);
        mainTabbedPane.add("qDPC", qdpcPane);
        mainTabbedPane.add("Acquisition", acquisitionPane);

        return mainTabbedPane;
    }

    private JPanel createSourcePatternPanel() {
        JPanel sourcePatternPanel = new JPanel(new MigLayout("wrap 2, fill, insets 2, gap 2", "[grow 1][grow 2]"));
        SourcePatternControlPanel controlPanel = new SourcePatternControlPanel(controller,
                new Utilities.ImageMode[]{
                        Utilities.ImageMode.BF,
                        Utilities.ImageMode.DF,
                        Utilities.ImageMode.DPC,
                        Utilities.ImageMode.PC,
                        Utilities.ImageMode.RB});
        controlPanel.add(createColorControlPanel(), "grow");
        controlPanel.add(createRotationControlPanel(), "grow");
        sourcePatternPanel.add(controlPanel, "grow");
        sourcePatternPanel.add(createPreviewPanel(), "grow");
        return sourcePatternPanel;
    }

    private JPanel createPreviewPanel() {
        JPanel previewPanel = new JPanel(new MigLayout("wrap 2, fill, gap 0", "[grow][]"));
        DisplayIlluminatorPreviewPane previewPane = new DisplayIlluminatorPreviewPane(controller);

        JPanel xPosPanel = new JPanel(new MigLayout("wrap 1, fillx"));
        JLabel xPosLabel = new JLabel("xPos:");
        xPosLabel.setFont(labelFont);
        LinkedSliderAndField xPosControls = new LinkedSliderAndField(controller, CENTER_X);
        xPosControls.textField.setColumns(3);
        JPanel xPosLabelAndFieldPanel = new JPanel(new MigLayout("wrap 2"));
        xPosLabelAndFieldPanel.add(xPosLabel);
        xPosLabelAndFieldPanel.add(xPosControls.textField);
        xPosPanel.add(xPosControls.slider, "growx");
        xPosPanel.add(xPosLabelAndFieldPanel, "align center");

        JPanel yPosPanel = new JPanel(new MigLayout("wrap 2, fill, insets 10 10 0 10", "[][]"));
        JLabel yPosLabel = new JLabel("yPos:");
        yPosLabel.setFont(labelFont);
        LinkedSliderAndField yPosControls = new LinkedSliderAndField(controller, CENTER_Y);
        yPosControls.textField.setColumns(3);
        JPanel yPosLabelAndFieldPanel = new JPanel(new MigLayout("wrap 1"));
        yPosLabelAndFieldPanel.add(yPosLabel);
        yPosLabelAndFieldPanel.add(yPosControls.textField);
        yPosControls.slider.setOrientation(JSlider.VERTICAL);
        yPosControls.slider.setInverted(true);
        yPosPanel.add(yPosControls.slider, "growy");
        yPosPanel.add(yPosLabelAndFieldPanel);

        previewPanel.add(previewPane, "push, grow");
        previewPanel.add(yPosPanel, "growy");
        previewPanel.add(xPosPanel, "growx");

        return previewPanel;
    }

    private JPanel createRotationControlPanel() {
        JPanel rotationPanel = new JPanel(new MigLayout("wrap 3, fill", "[150][grow][]68"));
        JLabel rotationLabel = new JLabel("Rotation:");
        rotationLabel.setFont(labelFont);
        LinkedSliderAndField rotationControls = new LinkedSliderAndField(controller, ROTATION);
        rotationControls.textField.setColumns(3);
        rotationPanel.add(rotationLabel, "align left");
        rotationPanel.add(rotationControls.slider, "grow");
        rotationPanel.add(rotationControls.textField, "");
        return rotationPanel;
    }
    private JPanel createColorControlPanel() {
        // Colour controls
        JPanel colorPanel = new JPanel(new MigLayout("wrap 3, fill", "[150]10[grow][grow]50"));
        JLabel colorLabel = new JLabel("Colour:");
        colorLabel.setFont(labelFont);
        Color initialColor = Color.decode("#" + controller.getProperty(COLOR));
        ColorChooserButton colorChooser = new ColorChooserButton(initialColor);
        JTextField colorField = new JTextField(6);
        colorField.setHorizontalAlignment(SwingConstants.CENTER);
        colorField.setText(Utilities.colorToHexString(initialColor));
        colorChooser.addColorChangedListener(new ColorChangedListener() {
            @Override
            public void colorChanged(Color newColor) {
                controller.setProperty(COLOR, newColor, UI);
                colorField.setText(Utilities.colorToHexString(newColor));
            }
        });
        colorField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newColorHex = colorField.getText();
                colorChooser.setSelectedColor(Color.decode("#" + newColorHex));
                controller.setProperty(COLOR, newColorHex, UI);
            }
        });

        colorPanel.add(colorLabel, "align left");
        colorPanel.add(colorChooser, "growx");
        colorPanel.add(colorField, "growx");

        return colorPanel;
    }

    public DisplayIlluminatorFrame(DisplayIlluminatorController controller) {
        super("DisplayIlluminator Plugin GUI");
        this.controller = controller;
        labelFont = new Font("Arial", Font.BOLD, 16);
        super.setLayout(new MigLayout("fill, insets 2, gap 2, flowx"));
        JTabbedPane mainTabbedPane = createMainTabbedPane();
        super.add(mainTabbedPane, "grow, push, wrap");


//        dpcControlPanel = new DpcControlPanel(controller);
//        BfControlPanel bfControlPanel = new BfControlPanel(controller);
//        PcControlPanel pcControlPanel = new PcControlPanel(controller);

//        previewPane.addChangeListener(e ->
//        {
//            String paneName = previewPane.getTitleAt(previewPane.getSelectedIndex());
//            controlPanel.remove(pcControlPanel);
//            controlPanel.remove(bfControlPanel);
//            controlPanel.remove(dpcControlPanel);
//            if (paneName.startsWith("DPC")) {
//                controlPanel.add(dpcControlPanel, "grow");
//            }
//            else if (paneName.startsWith("PC")) {
//                controlPanel.add(pcControlPanel, "grow");
//            }
//            else if (paneName.startsWith("BF")) {
//                controlPanel.add(bfControlPanel, "grow");
//            }
//            controlPanel.updateUI();
//        });

        // Rotation Controls
//        JPanel rotationPanel = new JPanel(new MigLayout("wrap 2, fill", "[150][grow]50"));
//        JLabel rotationLabel = new JLabel("Rotation:");
//        rotationLabel.setFont(labelFont);
//        LinkedSliderAndField rotationControl = new LinkedSliderAndField(controller::setRotation);
//        rotationControl.slider.setMinimum(0);
//        rotationControl.slider.setMaximum(360);
//        rotationControl.setValue(controller.getRotation());
//        rotationControl.addListeners();
//
//        rotationPanel.add(rotationLabel, "align left");
//        rotationPanel.add(rotationControl, "span 2, growx");
//        controlPanel.add(rotationPanel, "growx");

        // Snap an image, show the image in the Snap/Live view
        JButton snapButton = new JButton("Snap Image");
        snapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Multiple images are returned only if there are multiple
                // cameras. We only care about the first image.
                List<Image> images = controller.studio.live().snap(true);
            }
        });
        super.add(snapButton, "wrap");

        // Run an acquisition using the current MDA parameters.
        JButton acquireButton = new JButton("Run Acquisition");
        acquireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // All GUI event handlers are invoked on the EDT (Event Dispatch
                // Thread). Acquisitions are not allowed to be started from the
                // EDT. Therefore we must make a new thread to run this.
                Thread acqThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        controller.studio.acquisitions().runAcquisition();
                    }
                });
                acqThread.start();
            }
        });
        super.add(acquireButton, "wrap");

        super.setIconImage(Toolkit.getDefaultToolkit().getImage(
                getClass().getResource("/org/micromanager/icons/microscope.gif")));
        super.setLocation(100, 100);
        WindowPositioning.setUpLocationMemory(this, this.getClass(), null);
        super.setMinimumSize(new Dimension(1000, 600));
        super.pack();
    }
}