package org.micromanager.plugins.DisplayIlluminator;

import org.micromanager.Studio;
import java.awt.*;

// TODO: Perhaps rename this and so-called 'interface' class to more clearly reflect their purposes.
public class DisplayIlluminatorController extends DisplayIlluminatorInterface {

    private DisplayIlluminatorPreviewPane previewPane; // Currently only supports one preview pane

    DisplayIlluminatorController(Studio studio, String deviceName) {
        super(studio, deviceName);
    }

    public void addPreviewPane(DisplayIlluminatorPreviewPane newPreviewPane) {
        newPreviewPane.addChangeListener((c) ->
                setActiveImage(newPreviewPane.getTitleAt(newPreviewPane.getSelectedIndex())));
        previewPane = newPreviewPane;
    }

    @Override
    public void setActiveImage(String imageName) {
        try {
            super.setActiveImage(imageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setCenterX(int centerX, boolean previewOnly) {
        if (!previewOnly) {
            try {
                super.setCenterX(centerX);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        previewPane.setCenterX(centerX);
    }

    @Override
    public void setCenterX(int centerX) {
        try {
            super.setCenterX(centerX);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setCenterX(centerX);
    }

    public void setCenterY(int centerY, boolean previewOnly) {
        if (!previewOnly) {
            try {
                super.setCenterY(centerY);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        previewPane.setCenterY(-centerY);
    }

    @Override
    public void setCenterY(int centerY) {
        try {
            super.setCenterY(centerY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setCenterY(-centerY);
    }

    @Override
    public void setRotation(int angleDegrees) {
        try {
            super.setRotation(angleDegrees);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setRotation((float) angleDegrees); // TODO: Fix type mismatch
    }

    public void setRotation(int angleDegrees, boolean previewOnly) {
        if (!previewOnly) {
            try {
                super.setRotation(angleDegrees);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        previewPane.setRotation((float) angleDegrees); // TODO: Fix type mismatch

    }

    @Override
    public void setDpcDiameter(int diameter) {
        try {
            super.setDpcDiameter(diameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setDiameter(diameter); // TODO: Fix naming mismatches / make method name more precise
    }

    public void setDpcDiameter(int diameter, boolean previewOnly) {
        if (!previewOnly) {
            try {
                super.setDpcDiameter(diameter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        previewPane.setDiameter(diameter); // TODO: Fix naming mismatches / make method name more precise
    }

    @Override
    public void setColor(String colorHex) {
        setColor(Color.decode("#"+colorHex));
    }

    public void setColor(Color color) {
        try {
            super.setColor(Utilities.colorToHexString(color));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setSegmentColor(color); // TODO: Fix naming mismatches / make method name more precise
    }

    @Override
    public int getDpcDiameter(){
        try {
            return super.getDpcDiameter();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getRotation(){
        try {
            return super.getRotation();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Color getColor() {
        try {
            return super.getColor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCenterX() {
        try {
            return super.getCenterX();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCenterY() {
        try {
            return super.getCenterY();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DisplayIlluminatorPreviewPane createPreviewPane() {
        previewPane = new DisplayIlluminatorPreviewPane();
        try {
            previewPane.addDpcPanels(getDpcCount(), getDisplayWidthPx(), getDisplayHeightPx(), getDpcWidth(),
                    getDpcHeight(), getRotation(), getColor());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        addPreviewPane(previewPane);
        return previewPane;
    }

}
