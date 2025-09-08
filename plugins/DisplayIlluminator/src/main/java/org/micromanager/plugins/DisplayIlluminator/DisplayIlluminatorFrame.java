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
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mmcorej.CMMCore;
import net.miginfocom.swing.MigLayout;
import org.micromanager.PropertyManager;
import org.micromanager.Studio;
import org.micromanager.data.Image;
import org.micromanager.events.ExposureChangedEvent;
import org.micromanager.events.PropertyChangedEvent;
import org.micromanager.internal.utils.WindowPositioning;

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

    public DisplayIlluminatorFrame(Studio studio) {
        super("DisplayIlluminator Plugin GUI");
        studio_ = studio;
        core_ = studio.getCMMCore();

        super.setLayout(new MigLayout("fill, insets 2, gap 2, flowx"));
        JTabbedPane mainTabbedPane = new JTabbedPane();
        JPanel illumPatternPane = new JPanel();
        mainTabbedPane.add("Display Illuminator Settings", illumPatternPane);
        illumPatternPane.setLayout(new MigLayout("wrap 3, fill, insets 2, gap 2"));
        super.add(mainTabbedPane, "growx, wrap");


        JLabel diameterLabel = new JLabel("DPC Diameter (Pixels):");
        diameterLabel.setFont(new Font("Arial", Font.BOLD, 14));
        illumPatternPane.add(diameterLabel);

        dpcDiameterField_ = new JTextField(10); // TODO: Sanitise input: https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers
        try {
            dpcDiameterField_.setText(core_.getProperty("DisplayIlluminator", "DpcDiameter"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        dpcDiameterField_.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fieldText = dpcDiameterField_.getText();
                try {
                    core_.setProperty("DisplayIlluminator", "DpcDiameter", fieldText);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        illumPatternPane.add(dpcDiameterField_, "span 2, growx");

        JLabel rotationLabel = new JLabel("Rotation (degrees):");
        rotationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        illumPatternPane.add(rotationLabel);
        JTextField rotationField = new JTextField(3);
        rotationField.setText("0");
        JSlider rotationSlider = new JSlider(JSlider.HORIZONTAL, 0, 360, 0);
        rotationSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int sliderInt = rotationSlider.getValue();
                rotationField.setText(String.valueOf(sliderInt));
                if (rotationSlider.getValueIsAdjusting()) {
                    return;
                }
                try {
                    core_.setProperty("DisplayIlluminator", "Rotation", sliderInt);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        illumPatternPane.add(rotationSlider);
        illumPatternPane.add(rotationField);

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
}
