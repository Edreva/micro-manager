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
        initialize(defaultLayout, new JLabel(""), JSlider.HORIZONTAL);
    }

    LinkedSliderAndField(MigLayout layout) { initialize(layout, new JLabel(""), JSlider.HORIZONTAL);}

    LinkedSliderAndField(MigLayout layout, JLabel label) { initialize(layout, label, JSlider.HORIZONTAL);}

    LinkedSliderAndField(MigLayout layout, JLabel label, int sliderOrientation) { initialize(layout, label, sliderOrientation);}

    private void initialize(MigLayout layout, JLabel label, int sliderOrientation) {
        String defaultSliderConstraints = "growx";
        String defaultFieldConstraints = "";
        int defaultFieldColumnCount = 3;

        slider = new JSlider(sliderOrientation);
        textField = new JTextField(defaultFieldColumnCount);  // TODO: Sanitise input: https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers

        String defaultlabelAndFieldConstraints = "wrap 2";
        MigLayout labelAndFieldLayout = new MigLayout(defaultlabelAndFieldConstraints);
        if (sliderOrientation == JSlider.VERTICAL) {
            labelAndFieldLayout.setLayoutConstraints("wrap 1");
        }

        JPanel labelAndField = new JPanel(labelAndFieldLayout);
        this.setLayout(layout);
        this.add(slider, defaultSliderConstraints);
        labelAndField.add(label);
        labelAndField.add(textField, defaultFieldConstraints);
        this.add(labelAndField, "align center");
    }

    public void setSliderConstraints(Object o) {
        layout.setComponentConstraints(slider, o);
    }

    public void setFieldConstraints(Object o) {
//        layout.setComponentConstraints(textField, o);
        ((MigLayout)textField.getParent().getLayout()).setComponentConstraints(textField, o);
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
