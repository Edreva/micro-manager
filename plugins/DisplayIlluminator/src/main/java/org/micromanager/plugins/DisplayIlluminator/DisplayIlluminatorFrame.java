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

import mmcorej.CMMCore;
import net.miginfocom.swing.MigLayout;
import org.micromanager.Studio;
import org.micromanager.data.Image;
import org.micromanager.events.PropertyChangedEvent;
import org.micromanager.internal.utils.WindowPositioning;

import org.micromanager.plugins.DisplayIlluminator.ColorChooserButton.ColorChangedListener;

// Imports for MMStudio internal packages
// Plugins should not access internal packages, to ensure modularity and
// maintainability. However, this plugin code is older than the current
// MMStudio API, so it still uses internal classes and interfaces. New code
// should not imitate this practice.


public class DisplayIlluminatorFrame extends JFrame {

    private Studio studio_;
    private CMMCore core_;
    private JTextField userText_;
    private JTextField dpcDiameterField_;
    private Map<String, OvalSegmentImage> previewImages_ = new HashMap<String, OvalSegmentImage>();
    private DisplayIlluminatorInterface displayIlluminator;

    public DisplayIlluminatorFrame(Studio studio) {
        super("DisplayIlluminator Plugin GUI");
        studio_ = studio;
        core_ = studio.getCMMCore();

        displayIlluminator = new DisplayIlluminatorInterface(studio_, "DisplayIlluminator");
        DisplayIlluminatorController controller = new DisplayIlluminatorController(studio_, "DisplayIlluminator");

        super.setLayout(new MigLayout("fill, insets 2, gap 2, flowx"));
        JTabbedPane mainTabbedPane = new JTabbedPane();
        JPanel illumPatternPane = new JPanel();
        mainTabbedPane.add("Display Illuminator Settings", illumPatternPane);
        illumPatternPane.setLayout(new MigLayout("wrap 2, fill, insets 2, gap 2"));
        super.add(mainTabbedPane, "grow, wrap");

        JPanel previewPanel = new JPanel();
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview Panel"));
        JPanel controlPanel = new JPanel();
        previewPanel.setLayout(new MigLayout("wrap 3, fill, insets 2, gap 2"));
        controlPanel.setLayout(new MigLayout("wrap 3, fill, insets 2, gap 2"));
        illumPatternPane.add(controlPanel, "grow");
        illumPatternPane.add(previewPanel, "grow");


        DisplayIlluminatorPreviewPane previewPane = controller.createPreviewPane();

        JPanel xPosControls = new JPanel(new MigLayout("wrap 1, fill"));
        JPanel yPosControls = new JPanel(new MigLayout("wrap 2, fill"));
        JSlider xPosSlider = new JSlider(JSlider.HORIZONTAL, -displayIlluminator.getDisplayWidthPx() / 2, displayIlluminator.getDisplayWidthPx() / 2, 0);
        xPosSlider.addChangeListener((c) -> controller.setCenterX(xPosSlider.getValue(), xPosSlider.getValueIsAdjusting()));
        JSlider yPosSlider = new JSlider(JSlider.VERTICAL, -displayIlluminator.getDisplayHeightPx(), displayIlluminator.getDisplayHeightPx(), 0);
        yPosSlider.addChangeListener((c) -> controller.setCenterY(yPosSlider.getValue(), yPosSlider.getValueIsAdjusting()));
        JTextField xPosField = new JTextField(4);
        JTextField yPosField = new JTextField(4);

        JPanel offPanel = new JPanel();
        offPanel.setBackground(Color.BLACK);

        previewPanel.add(previewPane, "grow");
        yPosControls.add(yPosSlider, "growy");
        yPosControls.add(yPosField);
        previewPanel.add(yPosControls, "dock east");
        xPosControls.add(xPosSlider, "growx");
        xPosControls.add(xPosField, "align center");
        previewPanel.add(xPosControls, "dock south, span 3");


        Font labelFont = new Font("Arial", Font.BOLD, 14);
        JLabel diameterLabel = new JLabel("DPC Diameter (Pixels):");
        diameterLabel.setFont(labelFont);
        controlPanel.add(diameterLabel);

        dpcDiameterField_ = new JTextField(10); // TODO: Sanitise input: https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers
        try {
            dpcDiameterField_.setText(String.valueOf(controller.getDpcDiameter()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        dpcDiameterField_.addActionListener((a) -> controller.setDpcDiameter(Integer.parseInt(dpcDiameterField_.getText())));
        controlPanel.add(dpcDiameterField_, "span 2, growx");

        JLabel rotationLabel = new JLabel("Rotation (degrees):");
        rotationLabel.setFont(labelFont);
        controlPanel.add(rotationLabel);
        JTextField rotationField = new JTextField(3);
        rotationField.setText("0");
        JSlider rotationSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
        rotationSlider.addChangeListener((c) -> controller.setRotation(rotationSlider.getValue(), rotationSlider.getValueIsAdjusting()));
        controlPanel.add(rotationSlider, "growx");
        controlPanel.add(rotationField, "growx");

        Color initialColor;
        try {
            initialColor = controller.getColor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        JLabel colorLabel = new JLabel("Colour:");
        colorLabel.setFont(labelFont);
        ColorChooserButton colorChooser = new ColorChooserButton(initialColor);
        JTextField colorField = new JTextField(6);
        colorField.setText(colorToHexString(initialColor));
        colorChooser.addColorChangedListener(controller::setColor);

        controlPanel.add(colorLabel);
        controlPanel.add(colorChooser, "growx");
        controlPanel.add(colorField, "growx");

        LinkedSliderAndTextEdit test = new LinkedSliderAndTextEdit((x, y) -> controller.setCenterX(x, y));
        controlPanel.add(test);

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
        super.pack();

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