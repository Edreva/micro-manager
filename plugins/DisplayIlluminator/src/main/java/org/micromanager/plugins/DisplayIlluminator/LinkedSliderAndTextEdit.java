package org.micromanager.plugins.DisplayIlluminator;

import javax.swing.*;
import java.util.function.BiConsumer;

public class LinkedSliderAndTextEdit extends JPanel {
    public JSlider slider;
    public JTextField textField;

    LinkedSliderAndTextEdit(BiConsumer<Integer, Boolean> updateMethod) {
        slider = new JSlider(JSlider.HORIZONTAL, -200, 200, 0);
        textField = new JTextField(3);
        slider.addChangeListener((c) -> {
            textField.setText(String.valueOf(slider.getValue()));
            updateMethod.accept(slider.getValue(), slider.getValueIsAdjusting());});
        textField.addActionListener((a) -> {
            slider.setValue(Integer.parseInt(textField.getText()));
            updateMethod.accept(slider.getValue(), false);});
        this.add(slider);
        this.add(textField);
    }
}
