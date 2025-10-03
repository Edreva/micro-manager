package org.micromanager.plugins.DisplayIlluminator;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.micromanager.plugins.DisplayIlluminator.DisplayIlluminatorController.DevicePropertyName;

public class LinkedSliderAndField {
    // Keeping public for ease.
    public JSlider slider;
    public JTextField textField;

    private int mouseWheelIncrement = 5;
    public enum UpdateSource {SLIDER, FIELD, CONTROLLER};
    private DisplayIlluminatorController controller;
    private DevicePropertyName propertyToListenFor;
    private DevicePropertyName propertyToUpdate;
    private boolean callbackActive = false;
    PropertyChangeListener propertyChangeListener;

    LinkedSliderAndField(DisplayIlluminatorController controller, DevicePropertyName propertyName) {
        slider = new JSlider(
                (int) controller.getPropertyLowerLimit(propertyName),
                (int) controller.getPropertyUpperLimit(propertyName),
                Integer.parseInt(controller.getProperty(propertyName)));
        textField = new JTextField();
        propertyToUpdate = propertyName;
        propertyToListenFor = propertyToUpdate;
        this.controller = controller;

        propertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                onPropertyChangeEvent(event);
            }
        };

        controller.addPropertyChangeListener(propertyToListenFor, propertyChangeListener);

        slider.addChangeListener(this::sliderChangeListener);
        slider.addMouseWheelListener(this::sliderMouseWheelListener);
        slider.addMouseListener(new sliderMouseListener());
        textField.addActionListener(this::fieldActionListener);
    }

    public void onPropertyChangeEvent(PropertyChangeEvent event) {
        if (!callbackActive) {
            setValue((String) event.getNewValue(), UpdateSource.CONTROLLER, controller.getUpdateOngoing());
        }
    }

    public void setValue(String valueStr, UpdateSource source, boolean updateOngoing) {
        if (source != UpdateSource.SLIDER) {
            slider.setValueIsAdjusting(updateOngoing); // Set here to avoid next line from potentially overwriting updateOngoing
            slider.setValue(Integer.parseInt(valueStr));
        }
        if (source != UpdateSource.FIELD) {
            textField.setText(valueStr);
        }

        DisplayIlluminatorController.UpdateSource controllerUpdateSource = DisplayIlluminatorController.UpdateSource.UI;
        if (source == UpdateSource.CONTROLLER) {
            controllerUpdateSource = DisplayIlluminatorController.UpdateSource.INTERNAL;
        }

        callbackActive = true;
        controller.setProperty(propertyToUpdate, valueStr, controllerUpdateSource, updateOngoing);
        callbackActive = false;
    }

    private void sliderChangeListener(ChangeEvent event) {
        setValue(String.valueOf(slider.getValue()), UpdateSource.SLIDER, slider.getValueIsAdjusting());
    }

    private void fieldActionListener(ActionEvent event) {
        setValue(textField.getText(), UpdateSource.FIELD, false);
    }

    private void sliderMouseWheelListener(MouseWheelEvent event) {
        slider.setValueIsAdjusting(true);
        slider.setValue(slider.getValue() + event.getWheelRotation() * mouseWheelIncrement);
    }

    private class sliderMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {
            setValue(String.valueOf(slider.getValue()), LinkedSliderAndField.UpdateSource.SLIDER, false);
            slider.setValueIsAdjusting(false);
        }
    }

    public void setMouseWheelIncrement(int mouseWheelIncrement) {
        this.mouseWheelIncrement = mouseWheelIncrement;
    }

    public DevicePropertyName getPropertyToUpdate() {
        return propertyToUpdate;
    }

}
