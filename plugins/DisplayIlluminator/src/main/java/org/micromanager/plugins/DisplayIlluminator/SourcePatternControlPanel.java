package org.micromanager.plugins.DisplayIlluminator;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

import org.micromanager.plugins.DisplayIlluminator.Utilities.DevicePropertyName;

import java.util.HashMap;

import static org.micromanager.plugins.DisplayIlluminator.Utilities.DevicePropertyName.*;

public class SourcePatternControlPanel extends JPanel {
    private static final String[] defaultLayout_inner = {"wrap 2, fill", "[grow][]"}; // Can't give inner class statics in Java8
    private static final String[] defaultLayout = {"wrap 1, fill, insets 2, gap 2", "[grow]"};
    private final DisplayIlluminatorController controller;

    private final HashMap<Utilities.ImageMode, ModeSpecificControlPanel> modeSpecificControlPanels;

    public class ModeSpecificControlPanel extends JPanel {
        ModeSpecificControlPanel(MigLayout layout, Utilities.ImageMode mode) {
            super(layout);
            switch (mode) {
                case DPC:
                    addDpcCountPanel();
                    addSyncedControl(DPC_WIDTH, DPC_HEIGHT);
                    addSyncedControl(DPC_INNER_WIDTH, DPC_INNER_HEIGHT);
                    break;
                case BF:
                    addSyncedControl(BF_WIDTH, BF_HEIGHT);
                    break;
                case DF:
                case RB:
                    addSyncedControl(DF_WIDTH, DF_HEIGHT);
                    addSyncedControl(DF_INNER_WIDTH, DF_INNER_HEIGHT);
                    break;
                case PC:
                    addSyncedControl(PC_WIDTH, PC_HEIGHT);
                    addSyncedControl(PC_INNER_WIDTH, PC_INNER_HEIGHT);
                    break;
            }

        }
        ModeSpecificControlPanel(Utilities.ImageMode mode) {
            this(new MigLayout(defaultLayout_inner[0], defaultLayout_inner[1]),  mode);
        }

        private void addDpcCountPanel() {
            JPanel dpcCountPanel = new JPanel(new MigLayout("wrap 2, fill, insets 0", "[150][grow]65"));
            JLabel dpcCountLabel = new JLabel("DpcCount:");
            JSpinner dpcCountSpinner = new JSpinner(new SpinnerNumberModel(
                    Integer.parseInt(controller.getProperty(DPC_COUNT)),
                    (int) controller.getPropertyLowerLimit(DPC_COUNT),
                    (int) controller.getPropertyUpperLimit(DPC_COUNT),
                    1)
            );
            dpcCountPanel.add(dpcCountLabel, "align left");
            dpcCountPanel.add(dpcCountSpinner, "grow");
            this.add(dpcCountPanel, "grow, span 2");
            controller.addPropertyChangeListener(DPC_COUNT, e -> dpcCountSpinner.setValue(e.getNewValue()));
            dpcCountSpinner.addChangeListener(e -> controller.setProperty(DPC_COUNT, dpcCountSpinner.getValue().toString(), DisplayIlluminatorController.UpdateSource.UI));
        }

        private void addSyncedControl(DevicePropertyName firstPropertyName, DevicePropertyName secondPropertyName) {
            JPanel leftPanel = new JPanel(new MigLayout("wrap 3, fill, gap 0, insets 0", "[150][grow][]"));
            JLabel firstControlLabel = new JLabel(firstPropertyName.toString() + ":");
            LinkedSliderAndField firstControlSliderAndField = new LinkedSliderAndField(controller, firstPropertyName);
            firstControlSliderAndField.textField.setColumns(3);


            JLabel secondControlLabel = new JLabel(secondPropertyName.toString() + ":");
            LinkedSliderAndField secondControlSliderAndField = new LinkedSliderAndField(controller, secondPropertyName);
            secondControlSliderAndField.textField.setColumns(3);

            JPanel checkboxPanel = new JPanel(new MigLayout("wrap 1"));
            checkboxPanel.add(new JLabel("Sync?"), "align center");
            checkboxPanel.add(new SyncedSliders(controller, firstControlSliderAndField, secondControlSliderAndField).getSyncCheckBox());

            leftPanel.add(firstControlLabel, "align left");
            leftPanel.add(firstControlSliderAndField.slider, "grow");
            leftPanel.add(firstControlSliderAndField.textField, "");
            leftPanel.add(secondControlLabel, "align left");
            leftPanel.add(secondControlSliderAndField.slider, "grow");
            leftPanel.add(secondControlSliderAndField.textField, "wrap");

            this.add(leftPanel, "grow");
            this.add(checkboxPanel, "growy");
        }
    }

    SourcePatternControlPanel(MigLayout layout, DisplayIlluminatorController controller, Utilities.ImageMode[] modes) {
        super(layout);
        this.controller = controller;

        modeSpecificControlPanels = new HashMap<>();
        for (Utilities.ImageMode mode: modes) {
            modeSpecificControlPanels.put(mode, new ModeSpecificControlPanel(mode));
        }

        controller.addPropertyChangeListener(ACTIVE_IMAGE, pce -> {
            setActiveModePanel(Utilities.getImageModeFromImageName(pce.getNewValue().toString()));
        });
    }

    SourcePatternControlPanel(DisplayIlluminatorController controller, Utilities.ImageMode[] modes) {
        this(new MigLayout(defaultLayout[0], defaultLayout[1]), controller, modes);
    }

    private void removeAllModePanels() {
        modeSpecificControlPanels.forEach((mode,panel) -> this.remove(panel));
    }

    private void setActiveModePanel(Utilities.ImageMode mode) {
        removeAllModePanels();
        if (mode != Utilities.ImageMode.OFF) {
            this.add(modeSpecificControlPanels.get(mode), "grow");
        }
    }
}
