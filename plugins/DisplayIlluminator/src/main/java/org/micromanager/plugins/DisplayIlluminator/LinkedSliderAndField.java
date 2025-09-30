package org.micromanager.plugins.DisplayIlluminator;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.function.BiConsumer;

public class LinkedSliderAndField extends JPanel {
    public JSlider slider;
    public JTextField textField;
    public MigLayout layout;
    ArrayList<EventListener> userListeners;
    private int value;
    private boolean updateDisplay = false; // If false, only update the preview
    private BiConsumer<Integer, Boolean> updateMethod;
    LinkedSliderAndField() { // TODO: Rework these constructors
        MigLayout defaultLayout = new MigLayout("wrap 2, fill", "[grow][]");
        initialize(defaultLayout, null, new JLabel(""), JSlider.HORIZONTAL);
    }
    LinkedSliderAndField(BiConsumer<Integer, Boolean> updateMethod) {
        MigLayout defaultLayout = new MigLayout("wrap 2, fill", "[grow][]");
        initialize(defaultLayout, updateMethod, new JLabel(""), JSlider.HORIZONTAL);
    }
    LinkedSliderAndField(MigLayout layout, BiConsumer<Integer, Boolean> updateMethod) {
        initialize(layout, updateMethod, new JLabel(""), JSlider.HORIZONTAL);}

    LinkedSliderAndField(MigLayout layout, BiConsumer<Integer, Boolean> updateMethod, JLabel label) {
        initialize(layout, updateMethod, label, JSlider.HORIZONTAL);}

    LinkedSliderAndField(MigLayout layout, BiConsumer<Integer, Boolean> updateMethod, JLabel label, int sliderOrientation) {
        initialize(layout, updateMethod, label, sliderOrientation);}


    private void initialize(MigLayout layout, BiConsumer<Integer, Boolean> updateMethod,
                            JLabel label, int sliderOrientation) {
        String defaultSliderConstraints = "growx";
        String defaultFieldConstraints = "";
        int defaultFieldColumnCount = 3;
        this.updateMethod = updateMethod;
        this.userListeners = new ArrayList<EventListener>();
        slider = new JSlider(sliderOrientation);
        textField = new JTextField(defaultFieldColumnCount);  // TODO: Sanitise input: https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers
        value = slider.getValue();
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
        this.value = value;
        slider.setValue(value);
        textField.setText(String.valueOf(value));
    }
    
    public void setValue(int value, boolean updateDisplay) {
        // TODO: Perhaps instead of flag, temporarily disable the listeners?
//        boolean prevState = this.updateDisplay;
//        this.updateDisplay = updateDisplay;
        setValue(value);
        updateMethod.accept(value, !updateDisplay);
//        this.updateDisplay = prevState;
    }

    public int getValue() {
        return this.value;
    }

    public BiConsumer<Integer, Boolean> getUpdateMethod() {
        return this.updateMethod;
    }
    public void getUpdateMethod(BiConsumer<Integer, Boolean> updateMethod) {
        this.updateMethod = updateMethod;
    }

    public void removeAllListeners() {
        for (EventListener el : userListeners) {
            if (el instanceof ChangeListener) {
                slider.removeChangeListener((ChangeListener) el);
            } else if (el instanceof  ActionListener) {
                textField.removeActionListener((ActionListener) el);
            } else if (el instanceof MouseWheelListener) {
                slider.removeMouseWheelListener((MouseWheelListener) el);
            } else if (el instanceof MouseListener) {
                slider.removeMouseListener((MouseListener) el);
            }
        }
    }
    public void addChangeListener(ChangeListener changeListener) {
        slider.addChangeListener(changeListener);
        this.userListeners.add(changeListener);
    }

    public void addActionListener(ActionListener actionListener) {
        textField.addActionListener(actionListener);
        this.userListeners.add(actionListener);
    }

    public void addMouseWheelListener(MouseWheelListener mouseWheelListener) {
        slider.addMouseWheelListener(mouseWheelListener);
        this.userListeners.add(mouseWheelListener);
    }

    public void addMouseListener(MouseListener mouseListener) {
        slider.addMouseListener(mouseListener);
        this.userListeners.add(mouseListener);
    }

    public void addListeners() {
        this.addChangeListener((c) -> {
            int sliderValue = slider.getValue();
            if (sliderValue != this.value) {
                textField.setText(String.valueOf(sliderValue));
                updateMethod.accept(sliderValue, updateDisplay);
                this.value = sliderValue;
            }
        });
        this.addMouseWheelListener((l) -> {
            int newSliderValue = slider.getValue() + l.getWheelRotation();
            this.setValue(newSliderValue);
            updateMethod.accept(slider.getValue(), updateDisplay);
        });
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                updateMethod.accept(slider.getValue(), true);
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {
                updateMethod.accept(slider.getValue(), true);
            }
        });
        this.addActionListener((a) -> {
            int textFieldValue = Integer.parseInt(textField.getText());
            slider.setValue(textFieldValue);
            updateMethod.accept(textFieldValue, true);
            });
    }

    // Add listeners such that this ui element is kept in sync with the passed syncedSliderField, both controlling the hardware via updateMethod
    public void addListeners(BiConsumer<Integer, Boolean> updateMethod, LinkedSliderAndField syncedSliderField) {
        this.addChangeListener((c) -> {
            int sliderValue = slider.getValue();
            if (sliderValue != this.value) {
                textField.setText(String.valueOf(sliderValue));
                syncedSliderField.setValue(sliderValue);
                updateMethod.accept(sliderValue, true);
                this.value = sliderValue;
            }
        });
        this.addMouseWheelListener((l) -> {
            int newSliderValue = slider.getValue() + l.getWheelRotation();
            this.setValue(newSliderValue);
            syncedSliderField.setValue(newSliderValue);
            updateMethod.accept(slider.getValue(), true);
            });
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {
                updateMethod.accept(slider.getValue(), false);
            }
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {
                updateMethod.accept(slider.getValue(), false);
            }
        });
        this.addActionListener((a) -> {
            int textFieldValue = Integer.parseInt(textField.getText());
            slider.setValue(textFieldValue);
            syncedSliderField.slider.setValue(textFieldValue);
            updateMethod.accept(textFieldValue, false);});
    }
}
