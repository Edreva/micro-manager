package org.micromanager.plugins.DisplayIlluminator;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.function.BiConsumer;

// TODO: Change name to something more precise
public class SyncedSliders extends JPanel {
    LinkedSliderAndField widthSliderField;
    LinkedSliderAndField heightSliderField;
    int maxWidth;
    int maxHeight;
    JCheckBox circularityCB;
    BiConsumer<Integer, Boolean> updateMethod;
    MigLayout layout = new MigLayout("wrap 3, fill", "[150][grow][]");

    SyncedSliders() {
        this.setLayout(layout);
    }
    SyncedSliders(LinkedSliderAndField widthSliderField,
                  LinkedSliderAndField heightSliderField,
                  BiConsumer<Integer, Boolean> updateMethod) {
        this();
        this.widthSliderField=widthSliderField;
        this.heightSliderField=heightSliderField;
        this.maxWidth = widthSliderField.slider.getMaximum();
        this.maxHeight = heightSliderField.slider.getMaximum();
        this.circularityCB = new JCheckBox();

        this.circularityCB.addChangeListener(l -> {
            this.widthSliderField.removeAllListeners();
            this.heightSliderField.removeAllListeners();
            if (circularityCB.isSelected()) {
                this.heightSliderField.slider.setMaximum(Math.max(maxWidth, maxHeight));
                this.widthSliderField.slider.setMaximum(Math.max(maxWidth, maxHeight));
                this.widthSliderField.addListeners(updateMethod, heightSliderField);
                this.heightSliderField.addListeners(updateMethod, widthSliderField);
                this.heightSliderField.setValue(widthSliderField.getValue());
                updateMethod.accept(widthSliderField.getValue(), false);


            }
            else {
                this.heightSliderField.slider.setMaximum(maxHeight);
                this.widthSliderField.slider.setMaximum(maxWidth);
                this.widthSliderField.addListeners();
                this.heightSliderField.addListeners();
            }
        });
    }
    SyncedSliders(LinkedSliderAndField widthSliderField, JLabel widthLabel,
                  LinkedSliderAndField heightSliderField, JLabel heightLabel,
                  BiConsumer<Integer, Boolean> updateMethod) {
        this(widthSliderField, heightSliderField, updateMethod);
        this.add(widthLabel, "align left");
        this.add(widthSliderField, "growx");
        JPanel temp = new JPanel(new MigLayout("wrap 1, gap 0, insets 0"));
        temp.add(new JLabel("Sync?"), "align center");
        temp.add(circularityCB);
        this.add(temp, "spany 2");
        this.add(heightLabel, "align left");
        this.add(heightSliderField, "growx");

    }

}
