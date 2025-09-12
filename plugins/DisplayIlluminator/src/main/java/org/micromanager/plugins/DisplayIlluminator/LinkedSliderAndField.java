package org.micromanager.plugins.DisplayIlluminator;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.function.BiConsumer;

public class LinkedSliderAndField extends JPanel {
    public JSlider slider;
    public JTextField textField;
    public MigLayout layout;

    LinkedSliderAndField() {
        MigLayout defaultLayout = new MigLayout("wrap 2, fill", "[grow][]");
        initialize(defaultLayout);
    }

    LinkedSliderAndField(MigLayout layout) {
        initialize(layout);
    }

    private void initialize(MigLayout layout) {
        String defaultSliderConstraints = "growx";
        String defaultFieldConstraints = "";
        int defaultFieldColumnCount = 6;

        slider = new JSlider(JSlider.HORIZONTAL);
        textField = new JTextField(defaultFieldColumnCount);  // TODO: Sanitise input: https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers

        this.setLayout(layout);
        this.add(slider, defaultSliderConstraints);
        this.add(textField, defaultFieldConstraints);
    }

    public void setSliderConstraints(Object o) {
        layout.setComponentConstraints(slider, o);
    }

    public void setFieldConstraints(Object o) {
        layout.setComponentConstraints(textField, o);
    }

    public void setLayout(MigLayout layout) {
        super.setLayout(layout);
        this.layout = layout;
    }

    public void setValue(int value) {
        slider.setValue(value);
        textField.setText(String.valueOf(value));
    }

    public void addListeners(BiConsumer<Integer, Boolean> updateMethod) {
        slider.addChangeListener((c) -> {
            textField.setText(String.valueOf(slider.getValue()));
            updateMethod.accept(slider.getValue(), slider.getValueIsAdjusting());});
        textField.addActionListener((a) -> {
            slider.setValue(Integer.parseInt(textField.getText()));
            updateMethod.accept(slider.getValue(), false);});
    }
}
