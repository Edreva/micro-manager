package org.micromanager.plugins.DisplayIlluminator;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

import static org.micromanager.plugins.DisplayIlluminator.DisplayIlluminatorController.DevicePropertyName.*;

public class SourcePatternControlPanel extends JPanel {
    SourcePatternControlPanel(MigLayout layout) {
        super(layout);
    }
    SourcePatternControlPanel(DisplayIlluminatorController controller, DisplayIlluminatorPreviewPane.ImageMode mode) {
        switch (mode) {
            case DPC:
                JLabel widthLabel = new JLabel("DpcWidth:");
                this.add(widthLabel);
                LinkedSliderAndField dpcWidthControls = new LinkedSliderAndField(controller, DPC_WIDTH);
                this.add(dpcWidthControls.slider);
                this.add(dpcWidthControls.textField);

                JLabel heightLabel = new JLabel("DpcHeight:");
                this.add(heightLabel);
                LinkedSliderAndField dpcHeightControls = new LinkedSliderAndField(controller, DPC_HEIGHT);
                this.add(dpcHeightControls.slider);
                this.add(dpcHeightControls.textField);

                SyncedSliders syncHandler = new SyncedSliders(controller, dpcWidthControls, dpcHeightControls);
                this.add(syncHandler.getSyncCheckBox());

        }
    }
}
