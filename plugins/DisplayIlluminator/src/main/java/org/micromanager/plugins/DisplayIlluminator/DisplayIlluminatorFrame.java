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

import com.google.common.eventbus.Subscribe;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mmcorej.Configuration;
import org.micromanager.plugins.DisplayIlluminator.ColorChooserButton.ColorChangedListener;

import mmcorej.CMMCore;
import net.miginfocom.swing.MigLayout;
import org.micromanager.Studio;
import org.micromanager.data.Image;
import org.micromanager.events.PropertyChangedEvent;
import org.micromanager.internal.utils.WindowPositioning;

import static org.micromanager.plugins.DisplayIlluminator.DisplayIlluminatorController.DevicePropertyName.*;
import static org.micromanager.plugins.DisplayIlluminator.DisplayIlluminatorController.UpdateSource.UI;


public class DisplayIlluminatorFrame extends JFrame {
    private DisplayIlluminatorController controller;

    // Tabs to indicate encapsulation. Could make separate classes if required
    // Verify if each needs class-level scope
      private JTabbedPane mainTabbedPane;
        private JPanel sourcePatternPanel;
            private SourcePatternControlPanel controlPanel;
            private JPanel previewPanel;
                private DisplayIlluminatorPreviewPane previewPane;

    private void initializeMainTabbedPane() {
        mainTabbedPane = new JTabbedPane();
        JPanel prefacePane  = new JPanel();
        JPanel acquisitionPane = new JPanel();
        JPanel qdpcPane = new JPanel();
        initializeSourcePatternPanel();
        mainTabbedPane.add("Source Pattern", sourcePatternPanel);
        mainTabbedPane.add("PreFace", prefacePane);
        mainTabbedPane.add("qDPC", qdpcPane);
        mainTabbedPane.add("Acquisition", acquisitionPane);
    }

    private void initializeSourcePatternPanel() {
        sourcePatternPanel = new JPanel(new MigLayout("wrap 2, fill, insets 2, gap 2", "[grow 1][grow 2]"));
//        controlPanel = new SourcePatternControlPanel(new MigLayout("wrap 1, fill, insets 2, gap 2", "[grow]"));
        controlPanel = new SourcePatternControlPanel(controller, DisplayIlluminatorPreviewPane.ImageMode.DPC);
        sourcePatternPanel.add(controlPanel, "grow");
        initializePreviewPanel();
        sourcePatternPanel.add(previewPanel, "grow");
    }

    private void initializePreviewPanel() {
        previewPanel = new JPanel(new MigLayout("wrap 2, fill, gap 0", "[grow][]"));
        previewPane = new DisplayIlluminatorPreviewPane(controller);

        JLabel xPosLabel = new JLabel("xPos:");
//        xPosLabel.setFont(labelFont);
        LinkedSliderAndField xPosControls = new LinkedSliderAndField(controller, CENTER_X);

        JLabel yPosLabel = new JLabel("yPos:");
//        yPosLabel.setFont(labelFont);
        LinkedSliderAndField yPosControls = new LinkedSliderAndField(controller, CENTER_Y);
        yPosControls.slider.setOrientation(JSlider.VERTICAL);

        previewPanel.add(previewPane, "push, grow");
        previewPanel.add(yPosControls.slider, "growy");
        previewPanel.add(xPosControls.slider, "growx");
    }

    public DisplayIlluminatorFrame(DisplayIlluminatorController controller) {
        super("DisplayIlluminator Plugin GUI");
        this.controller = controller;
        Font labelFont = new Font("Arial", Font.BOLD, 16);
        super.setLayout(new MigLayout("fill, insets 2, gap 2, flowx"));
        initializeMainTabbedPane();
        super.add(mainTabbedPane, "grow, wrap");


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
        controlPanel.add(colorPanel, "growx");


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


//        // TODO: Remove temporary test code:
//        try {
//            core_.defineConfigGroup("IlluminationModes");
//            core_.defineConfig("IlluminationModes","Off", "DisplayIlluminator", "ActiveImage", "Off");
//            core_.defineConfig("IlluminationModes","DPC1", "DisplayIlluminator", "ActiveImage", "DPC1");
//            core_.defineConfig("IlluminationModes","DPC2", "DisplayIlluminator", "ActiveImage", "DPC2");
//            core_.defineConfig("IlluminationModes","DPC3", "DisplayIlluminator", "ActiveImage", "DPC3");
//            core_.defineConfig("IlluminationModes","DPC4", "DisplayIlluminator", "ActiveImage", "DPC4");
//            core_.defineConfig("IlluminationModes","BF", "DisplayIlluminator", "ActiveImage", "BF");
//            core_.defineConfig("IlluminationModes","PC", "DisplayIlluminator", "ActiveImage", "PC");
//            core_.setConfig("IlluminationModes", "Off");
//            core_.waitForConfig("IlluminationModes", "Off");
////            core_.updateSystemStateCache();
//            studio_.app().refreshGUI();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}