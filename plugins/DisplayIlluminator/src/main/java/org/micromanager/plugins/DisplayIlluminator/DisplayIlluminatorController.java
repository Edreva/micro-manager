package org.micromanager.plugins.DisplayIlluminator;

import com.google.common.eventbus.Subscribe;
import mmcorej.CMMCore;
import org.micromanager.Studio;
import org.micromanager.events.PropertyChangedEvent;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Objects;

import static org.micromanager.plugins.DisplayIlluminator.DisplayIlluminatorController.DevicePropertyName.*;

// TODO: Perhaps rename this and so-called 'interface' class to more clearly reflect their purposes.
public class DisplayIlluminatorController {

    private final CMMCore mmCore;  // In terms of MVC this is holds the model
    public final Studio studio;
    private final String deviceName;
    private final String deviceLabel;
    public enum UpdateSource {UI, MM_CORE, INTERNAL};
    private final boolean liveUpdateCore = false; // If set, the HW device will be updated in sync with UI changes, if not only on release.
    private boolean updateOngoing = false;
    private HashMap<String, HashMap<DevicePropertyName, String>> savedPropertyStates;
    private HashMap<DevicePropertyName, Double> propertiesLowerLimitsOverrides;
    private HashMap<DevicePropertyName, Double> propertiesUpperLimitsOverrides;
    private final DisplayIlluminatorFrame ui;
    private final PropertyChangeSupport pcs;

    DisplayIlluminatorController(Studio studio) {
        mmCore = studio.getCMMCore();
        this.studio = studio;
        deviceLabel = mmCore.getSLMDevice();
        deviceName = "DisplayIlluminator"; //mmCore.getDeviceName(deviceLabel);
        savedPropertyStates = new HashMap<>();
        propertiesLowerLimitsOverrides = new HashMap<DevicePropertyName, Double>();
        propertiesLowerLimitsOverrides.put(CENTER_X, 0.0);
        propertiesLowerLimitsOverrides.put(CENTER_Y, 0.0);
        propertiesUpperLimitsOverrides = new HashMap<DevicePropertyName, Double>();
        propertiesUpperLimitsOverrides.put(CENTER_X, 1000.0);
        propertiesUpperLimitsOverrides.put(CENTER_Y, 1000.0);

        propertiesLowerLimitsOverrides.put(DPC_HEIGHT, 0.0);
        propertiesLowerLimitsOverrides.put(DPC_WIDTH, 0.0);

        propertiesUpperLimitsOverrides.put(DPC_HEIGHT, 1000.0);
        propertiesUpperLimitsOverrides.put(DPC_WIDTH, 1000.0);


        pcs = new PropertyChangeSupport(this);
        ui = new DisplayIlluminatorFrame(this);

        ui.setVisible(true);

        // Registering this class for events means that its event handlers
        // (that is, methods with the @Subscribe annotation) will be invoked when
        // an event occurs. You need to call the right registerForEvents() method
        // to get events; this one is for the application-wide event bus, but
        // there's also Datastore.registerForEvents() for events specific to one
        // Datastore, and DisplayWindow.registerForEvents() for events specific
        // to one image display window.
        studio.events().registerForEvents(this);
    }

    public void addPropertyChangeListener(DevicePropertyName propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName.toString(), listener);
    }

    public void removePropertyChangeListener(DevicePropertyName propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName.toString(), listener);
    }
    /**
     * To be invoked, this method must be public and take a single parameter
     * which is the type of the event we care about.
     *
     * @param event
     */
    @Subscribe
    public void onPropertyChanged(PropertyChangedEvent event) throws Exception {
        if (event.getDevice().equals(deviceLabel)) {
            String propertyName = event.getProperty();
            setProperty(event.getProperty(), getProperty(propertyName), UpdateSource.MM_CORE, false);
        }
    }

    public void setUpdateOngoing(boolean updateOngoing) {
        this.updateOngoing = updateOngoing;
    }

    public boolean getUpdateOngoing() {
        return this.updateOngoing;
    }

    private void setProperty(String propertyName, String value, UpdateSource source, boolean updateOngoing) {
        setUpdateOngoing(updateOngoing);
        try {
            String oldValue = mmCore.getProperty(deviceLabel, propertyName);

            if (source == UpdateSource.UI && (!updateOngoing || liveUpdateCore)) {
                mmCore.setProperty(deviceLabel, propertyName, value);
            }
            if (source != UpdateSource.INTERNAL) {
                pcs.firePropertyChange(propertyName, oldValue, value);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setProperty(DevicePropertyName propertyName, String value, UpdateSource source, boolean updateOngoing) {
        setProperty(propertyName.toString(), value, source, updateOngoing);
    }

    public void setProperty(DevicePropertyName propertyName, String value, UpdateSource source) {
        setProperty(propertyName.toString(), value, source, false);
    }

    public void setProperty(DevicePropertyName propertyName, Color color, UpdateSource source) {
        setProperty(propertyName.toString(), Utilities.colorToHexString(color), source, false);
    }

    public String getProperty(DevicePropertyName propertyName) {
        return getProperty(propertyName.toString());
    }

    private String getProperty(String propertyName) {
        try {
            return mmCore.getProperty(deviceLabel, propertyName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double getPropertyLowerLimit(DevicePropertyName propertyName) {
        try {
            return propertiesLowerLimitsOverrides.getOrDefault(propertyName,
                    mmCore.getPropertyLowerLimit(deviceLabel, propertyName.toString())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double getPropertyUpperLimit(DevicePropertyName propertyName) {
        try {
            return propertiesUpperLimitsOverrides.getOrDefault(propertyName,
                    mmCore.getPropertyUpperLimit(deviceLabel, propertyName.toString())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void savePropertyState(String stateName) {
        try {
            savedPropertyStates.put(stateName, new HashMap<>());
//            for (String propertyName: mmCore.getDevicePropertyNames(deviceName)) {
            for (DevicePropertyName propertyName: DevicePropertyName.values()) {
                savedPropertyStates.get(stateName)
                        .put(propertyName, mmCore.getProperty(deviceName, propertyName.toString()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void restorePropertyState(String stateName) {
        try {
//            for (String propertyName: mmCore.getDevicePropertyNames(deviceName)) {
            for (DevicePropertyName propertyName: DevicePropertyName.values()) {
                mmCore.setProperty(deviceName, propertyName.toString(),
                        savedPropertyStates.get(stateName).get(propertyName));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public enum DevicePropertyName {
        DISPLAY_HEIGHT("DisplayHeight_pixels"),
        DISPLAY_WIDTH("DisplayWidth_pixels"),
        CENTER_X("CenterX"),
        CENTER_Y("CenterY"),
        ROTATION("Rotation"),
        COLOR("MonoColor"),
        ACTIVE_IMAGE("ActiveImage"),
        BF_HEIGHT("BfHeight"),
        BF_WIDTH("BfWidth"),
        DF_HEIGHT("DfHeight"),
        DF_WIDTH("DfWidth"),
        DF_INNER_HEIGHT("DfInnerHeight"),
        DF_INNER_WIDTH("DfInnerWidth"),
        DPC_COUNT("DpcPatternCount"),
        DPC_HEIGHT("DpcHeight"),
        DPC_WIDTH("DpcWidth"),
        DPC_INNER_HEIGHT("DpcInnerHeight"),
        DPC_INNER_WIDTH("DpcInnerWidth"),
        PC_HEIGHT("PcHeight"),
        PC_WIDTH("PcWidth"),
        PC_INNER_HEIGHT("PcInnerHeight"),
        PC_INNER_WIDTH("PcInnerWidth"),
        RB_OUTER_COLOR("RbOuterColor"),
        RB_INNER_COLOR("RbInnerColor");

        private final String propertyName;

        DevicePropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getName() {
            return propertyName;
        }

        @Override
        public String toString() {
            return propertyName;
        }
    }
}
