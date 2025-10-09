package org.micromanager.plugins.DisplayIlluminator;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static java.lang.Math.max;
import static java.lang.Math.min;

// TODO: Change name to something more precise
public class SyncedSliders {
    private LinkedSliderAndField widthSliderField;
    private LinkedSliderAndField heightSliderField;
    private final JCheckBox syncCheckBox;
    private final DisplayIlluminatorController controller;
    int maxWidth;
    int maxHeight;
    boolean lastCBState = false;
    PropertyChangeListener widthSliderSyncListener;
    PropertyChangeListener heightSliderSyncListener;

    SyncedSliders(DisplayIlluminatorController controller,
                  LinkedSliderAndField widthSliderField,
                  LinkedSliderAndField heightSliderField) {
        this.controller = controller;
        this.widthSliderField=widthSliderField;
        this.heightSliderField=heightSliderField;
        this.maxWidth = widthSliderField.slider.getMaximum();
        this.maxHeight = heightSliderField.slider.getMaximum();
        this.syncCheckBox = new JCheckBox("", lastCBState);



        widthSliderSyncListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                widthSliderField.onPropertyChangeEvent(evt);
            }
        };

        heightSliderSyncListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                heightSliderField.onPropertyChangeEvent(evt);
            }
        };


        this.syncCheckBox.addChangeListener(l -> {
            if (syncCheckBox.isSelected() && !lastCBState) {
                Utilities.DevicePropertyName widthPropertyName = widthSliderField.getPropertyToUpdate();
                Utilities.DevicePropertyName heightPropertyName = heightSliderField.getPropertyToUpdate();
                controller.addPropertyChangeListener(widthPropertyName, heightSliderSyncListener);
                controller.addPropertyChangeListener(heightPropertyName, widthSliderSyncListener);
                controller.setProperty(heightPropertyName,
                        controller.getProperty(widthPropertyName),
                        DisplayIlluminatorController.UpdateSource.UI);

                // Update the lower/upper limits of the sliders to match eachother.
                int minLimit = (int) min(
                        controller.getPropertyLowerLimit(widthPropertyName),
                        controller.getPropertyLowerLimit(heightPropertyName)
                );
                int maxLimit = (int) max(
                        controller.getPropertyUpperLimit(widthPropertyName),
                        controller.getPropertyUpperLimit(heightPropertyName));
                widthSliderField.setLimits(minLimit, maxLimit);
                heightSliderField.setLimits(minLimit, maxLimit);

                lastCBState = true;
            }
            else if (!syncCheckBox.isSelected() && lastCBState) {
                controller.removePropertyChangeListener(widthSliderField.getPropertyToUpdate(), heightSliderSyncListener);
                controller.removePropertyChangeListener(heightSliderField.getPropertyToUpdate(), widthSliderSyncListener);
                widthSliderField.resetSliderLimits();
                heightSliderField.resetSliderLimits();

                lastCBState = false;
            }
        });
    }

    public JCheckBox getSyncCheckBox() {
        return this.syncCheckBox;
    }
}
