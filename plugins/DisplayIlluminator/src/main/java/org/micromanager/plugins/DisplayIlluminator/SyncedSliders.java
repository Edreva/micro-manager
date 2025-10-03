package org.micromanager.plugins.DisplayIlluminator;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
                controller.addPropertyChangeListener(widthSliderField.getPropertyToUpdate(), heightSliderSyncListener);
                controller.addPropertyChangeListener(heightSliderField.getPropertyToUpdate(), widthSliderSyncListener);

                lastCBState = true;
                // TODO: Update slider limits
            }
            else if (!syncCheckBox.isSelected() && lastCBState) {
                controller.removePropertyChangeListener(widthSliderField.getPropertyToUpdate(), heightSliderSyncListener);
                controller.removePropertyChangeListener(heightSliderField.getPropertyToUpdate(), widthSliderSyncListener);
                lastCBState = false;
                // TODO: Update slider limits
            }
        });
    }

    public JCheckBox getSyncCheckBox() {
        return this.syncCheckBox;
    }
}
