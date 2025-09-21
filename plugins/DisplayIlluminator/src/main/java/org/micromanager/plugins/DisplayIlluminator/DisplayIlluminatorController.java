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
                super.setCenterY(-centerY);
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
    public void setDiameter(String imageGroupPrefix, int diameter) {
        try {
            super.setDiameter(imageGroupPrefix, diameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setDiameter(imageGroupPrefix, diameter);
    }

    public void setDiameter(String imageGroupPrefix, int diameter, boolean previewOnly) {
        if (!previewOnly) {
            try {
                super.setDiameter(imageGroupPrefix, diameter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        previewPane.setDiameter(imageGroupPrefix, diameter);
    }

    @Override
    public void setInnerDiameter(String imageGroupPrefix, int diameter) {
        try {
            super.setInnerDiameter(imageGroupPrefix, diameter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setInnerDiameter(imageGroupPrefix, diameter);
    }

    public void setInnerDiameter(String imageGroupPrefix, int diameter, boolean previewOnly) {
        if (!previewOnly) {
            try {
                super.setInnerDiameter(imageGroupPrefix, diameter);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        previewPane.setInnerDiameter(imageGroupPrefix, diameter);
    }

    @Override
    public void setInnerWidth(String imageGroupPrefix, int width) {
        try {
            super.setInnerWidth(imageGroupPrefix, width);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setInnerWidth(imageGroupPrefix, width);
    }

    public void setInnerWidth(String imageGroupPrefix, int width, boolean previewOnly) {
        if (!previewOnly) {
            try {
                super.setInnerWidth(imageGroupPrefix, width);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        previewPane.setInnerWidth(imageGroupPrefix, width);
    }
    
    @Override
    public void setInnerHeight(String imageGroupPrefix, int height) {
        try {
            super.setInnerHeight(imageGroupPrefix, height);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setInnerHeight(imageGroupPrefix, height);
    }

    public void setInnerHeight(String imageGroupPrefix, int height, boolean previewOnly) {
        if (!previewOnly) {
            try {
                super.setInnerHeight(imageGroupPrefix, height);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        previewPane.setInnerHeight(imageGroupPrefix, height);
    }

    @Override
    public void setWidth(String imageGroupPrefix, int width) {
        try {
            super.setWidth(imageGroupPrefix, width);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setOuterWidth(imageGroupPrefix, width);
    }

    public void setWidth(String imageGroupPrefix, int width, boolean previewOnly) {
        if (!previewOnly) {
            try {
                super.setWidth(imageGroupPrefix, width);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        previewPane.setOuterWidth(imageGroupPrefix, width);
    }

    @Override
    public void setHeight(String imageGroupPrefix, int height) {
        try {
            super.setHeight(imageGroupPrefix, height);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setOuterHeight(imageGroupPrefix, height);
    }

    public void setHeight(String imageGroupPrefix, int height, boolean previewOnly) {
        if (!previewOnly) {
            try {
                super.setHeight(imageGroupPrefix, height);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        previewPane.setOuterHeight(imageGroupPrefix, height);
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
        previewPane.setOuterColor(color); // TODO: Fix naming mismatches / make method name more precise
    }

    @Override
    public void setRbOuterColor(String colorHex) {setColor(Color.decode("#"+colorHex));}

    public void setRbOuterColor(Color color) {
        try {
            super.setRbOuterColor(Utilities.colorToHexString(color));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        previewPane.setOuterColor("RB", color);
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

    @Override
    public int getDpcHeight() {
        try {
            return super.getDpcHeight();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getDpcWidth() {
        try {
            return super.getDpcWidth();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DisplayIlluminatorPreviewPane createPreviewPane() {
        previewPane = new DisplayIlluminatorPreviewPane();
        try {
            previewPane.addDpcPanels(getDpcCount(), getDisplayWidthPx(), getDisplayHeightPx(), getDpcWidth(),
                    getDpcHeight(), getRotation(), getColor());
            previewPane.addBfPanel(getDisplayWidthPx(), getDisplayHeightPx(), getBfWidth(), getBfHeight(), getRotation(), getColor());
            previewPane.setCenterX(getCenterX() - getDisplayWidthPx()/2); // TODO: Standardise coord system
            previewPane.setCenterY(getCenterY() - getDisplayHeightPx()/2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        addPreviewPane(previewPane);
        return previewPane;
    }

}
