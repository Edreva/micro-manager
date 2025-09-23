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


public class DisplayIlluminatorFrame extends JFrame {

    private Studio studio_;
    private CMMCore core_;
    private JTextField userText_;
    private JTextField dpcDiameterField_;
    private Map<String, EllipticalShapeImage> previewImages_ = new HashMap<String, EllipticalShapeImage>();
    private DisplayIlluminatorInterface displayIlluminator;

    public DisplayIlluminatorFrame(Studio studio) {
        super("DisplayIlluminator Plugin GUI");
        studio_ = studio;
        core_ = studio.getCMMCore();

        displayIlluminator = new DisplayIlluminatorInterface(studio_, "DisplayIlluminator");
        DisplayIlluminatorController controller = new DisplayIlluminatorController(studio_, "DisplayIlluminator");

        Font labelFont = new Font("Arial", Font.BOLD, 16);
        super.setLayout(new MigLayout("fill, insets 2, gap 2, flowx"));
        JTabbedPane mainTabbedPane = new JTabbedPane();
        JPanel illumPatternPane = new JPanel();
        JPanel prefacePane  = new JPanel();
        JPanel acquisitionPane = new JPanel();
        JPanel qdpcPane = new JPanel();
        mainTabbedPane.add("Source Pattern", illumPatternPane);
        mainTabbedPane.add("PreFace", prefacePane);
        mainTabbedPane.add("qDPC", qdpcPane);
        mainTabbedPane.add("Acquisition", acquisitionPane);
        illumPatternPane.setLayout(new MigLayout("wrap 2, fill, insets 2, gap 2", "[grow 1][grow 2]"));
        super.add(mainTabbedPane, "grow, wrap");

        JPanel previewPanel = new JPanel();
//        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview Panel"));
        JPanel controlPanel = new JPanel();
        previewPanel.setLayout(new MigLayout("wrap 2, fill, gap 0", "[grow][]"));
        controlPanel.setLayout(new MigLayout("wrap 1, fill, insets 2, gap 2", "[grow]"));
        illumPatternPane.add(controlPanel, "grow");
        illumPatternPane.add(previewPanel, "grow");


        DisplayIlluminatorPreviewPane previewPane = controller.createPreviewPane();

        JLabel xPosLabel = new JLabel("xPos:");
        xPosLabel.setFont(labelFont);
        LinkedSliderAndField xPosControls = new LinkedSliderAndField(
                new MigLayout("wrap 1, fillx"), controller::setCenterX, xPosLabel);
        xPosControls.slider.setMinimum(-controller.getDisplayWidthPx() / 2);
        xPosControls.slider.setMaximum(controller.getDisplayWidthPx() / 2);
        xPosControls.setValue(controller.getCenterX() - controller.getDisplayWidthPx()/2); // TODO: Revamp getCenter funcs to be more intuitively named
        xPosControls.setFieldConstraints("align center");
        xPosControls.addListeners();

        JLabel yPosLabel = new JLabel("yPos:");
        yPosLabel.setFont(labelFont);
        LinkedSliderAndField yPosControls = new LinkedSliderAndField(
                new MigLayout("wrap 2, fill, insets 10 10 0 10", "[][]"),
                controller::setCenterY, yPosLabel, JSlider.VERTICAL);
        yPosControls.slider.setMinimum(-controller.getDisplayHeightPx() / 2);
        yPosControls.slider.setMaximum(controller.getDisplayHeightPx() / 2);
        yPosControls.textField.setColumns(3);
        yPosControls.setValue(-controller.getCenterY() + controller.getDisplayHeightPx()/2); // TODO: Revamp getCenter funcs to be more intuitively named
        yPosControls.setSliderConstraints("growy");
        yPosControls.addListeners();


        JPanel offPanel = new JPanel();
        offPanel.setBackground(Color.BLACK);

        previewPanel.add(previewPane, "push, grow");
        previewPanel.add(yPosControls, "growy");
        previewPanel.add(xPosControls, "growx");

        DpcControlPanel dpcControlPanel = new DpcControlPanel(controller);
        previewPane.addChangeListener(e -> {
            if (previewPane.getTitleAt(previewPane.getSelectedIndex()).startsWith("DPC")) {
                controlPanel.add(dpcControlPanel, "grow");
            }
            else {
                controlPanel.remove(dpcControlPanel);
            }
        });

        BfControlPanel bfControlPanel = new BfControlPanel(controller);
        previewPane.addChangeListener(e -> {
            if (previewPane.getTitleAt(previewPane.getSelectedIndex()).startsWith("BF")) {
                controlPanel.add(bfControlPanel, "grow");
            }
            else {
                controlPanel.remove(bfControlPanel);
            }
        });

        PcControlPanel pcControlPanel = new PcControlPanel(controller);
        previewPane.addChangeListener(e -> {
            if (previewPane.getTitleAt(previewPane.getSelectedIndex()).startsWith("PC")) {
                controlPanel.add(pcControlPanel, "grow");
            }
            else {
                controlPanel.remove(pcControlPanel);
            }
        });

        // Rotation Controls
        JPanel rotationPanel = new JPanel(new MigLayout("wrap 2, fill", "[150][grow]50"));
        JLabel rotationLabel = new JLabel("Rotation:");
        rotationLabel.setFont(labelFont);
        LinkedSliderAndField rotationControl = new LinkedSliderAndField(controller::setRotation);
        rotationControl.slider.setMinimum(0);
        rotationControl.slider.setMaximum(360);
        rotationControl.setValue(controller.getRotation());
        rotationControl.addListeners();

        rotationPanel.add(rotationLabel, "align left");
        rotationPanel.add(rotationControl, "span 2, growx");
        controlPanel.add(rotationPanel, "growx");

        // Colour controls
        JPanel colorPanel = new JPanel(new MigLayout("wrap 3, fill", "[150]10[grow][grow]50"));
        JLabel colorLabel = new JLabel("Colour:");
        colorLabel.setFont(labelFont);
        Color initialColor = controller.getColor();
        ColorChooserButton colorChooser = new ColorChooserButton(initialColor);
        JTextField colorField = new JTextField(6);
        colorField.setHorizontalAlignment(SwingConstants.CENTER);
        colorField.setText(colorToHexString(initialColor));
        colorChooser.addColorChangedListener(new ColorChangedListener() {
            @Override
            public void colorChanged(Color newColor) {
                controller.setColor(newColor);
                colorField.setText(colorToHexString(newColor));
            }
        });
        colorField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newColorHex = colorField.getText();
                colorChooser.setSelectedColor(Color.decode("#" + newColorHex));
                controller.setColor(newColorHex);
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
                List<Image> images = studio_.live().snap(true);
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
                        studio_.acquisitions().runAcquisition();
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
        super.setMinimumSize(new Dimension(1500, 500));
        super.pack();


        // TODO: Remove temporary test code:
        try {
            core_.defineConfigGroup("IlluminationModes");
            core_.defineConfig("IlluminationModes","Off", "DisplayIlluminator", "ActiveImage", "Off");
            core_.defineConfig("IlluminationModes","DPC1", "DisplayIlluminator", "ActiveImage", "DPC1");
            core_.defineConfig("IlluminationModes","DPC2", "DisplayIlluminator", "ActiveImage", "DPC2");
            core_.defineConfig("IlluminationModes","DPC3", "DisplayIlluminator", "ActiveImage", "DPC3");
            core_.defineConfig("IlluminationModes","DPC4", "DisplayIlluminator", "ActiveImage", "DPC4");
            core_.defineConfig("IlluminationModes","BF", "DisplayIlluminator", "ActiveImage", "BF");
            core_.defineConfig("IlluminationModes","BF", "DisplayIlluminator", "ActiveImage", "PC");
            core_.setConfig("IlluminationModes", "Off");
            core_.waitForConfig("IlluminationModes", "Off");
//            core_.updateSystemStateCache();
            studio_.app().refreshGUI();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Registering this class for events means that its event handlers
        // (that is, methods with the @Subscribe annotation) will be invoked when
        // an event occurs. You need to call the right registerForEvents() method
        // to get events; this one is for the application-wide event bus, but
        // there's also Datastore.registerForEvents() for events specific to one
        // Datastore, and DisplayWindow.registerForEvents() for events specific
        // to one image display window.
        studio_.events().registerForEvents(this);
    }

    /**
     * To be invoked, this method must be public and take a single parameter
     * which is the type of the event we care about.
     *
     * @param event
     */
    @Subscribe
    public void onPropertyChanged(PropertyChangedEvent event) { // TODO: Update device adapter to fire propertyChanged events
        if (event.getDevice().equals("DisplayIlluminator")) {
            switch (event.getProperty()) {
                case "DpcDiameter":
                    dpcDiameterField_.setText(event.getValue());
            }
        }
    }

    public String colorToHexString(java.awt.Color color) {
        return String.format("%06x", color.getRGB() & 0xFFFFFF);
    }
}