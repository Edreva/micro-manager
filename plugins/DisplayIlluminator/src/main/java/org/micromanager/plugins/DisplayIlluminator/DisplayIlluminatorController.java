package org.micromanager.plugins.DisplayIlluminator;

import com.google.common.eventbus.Subscribe;
import mmcorej.CMMCore;
import org.micromanager.Studio;
import org.micromanager.events.PropertyChangedEvent;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

import static org.micromanager.plugins.DisplayIlluminator.Utilities.DevicePropertyName.*;

// TODO: Perhaps rename this and so-called 'interface' class to more clearly reflect their purposes.
public class DisplayIlluminatorController {

    private final CMMCore mmCore;  // In terms of MVC this is holds the model
    public final Studio studio;
    private final String deviceName;
    private final String deviceLabel;
    public enum UpdateSource {UI, MM_CORE, INTERNAL};
    private final boolean liveUpdateCore = false; // If set, the HW device will be updated in sync with UI changes, if not only on release.
    private boolean updateOngoing = false;
    private HashMap<String, HashMap<Utilities.DevicePropertyName, String>> savedPropertyStates;
    private HashMap<Utilities.DevicePropertyName, Double> propertiesLowerLimitsOverrides;
    private HashMap<Utilities.DevicePropertyName, Double> propertiesUpperLimitsOverrides;
    private final DisplayIlluminatorFrame ui;
    private final PropertyChangeSupport pcs;

    DisplayIlluminatorController(Studio studio) {
        mmCore = studio.getCMMCore();
        this.studio = studio;
        deviceLabel = mmCore.getSLMDevice();
        deviceName = "DisplayIlluminator"; //mmCore.getDeviceName(deviceLabel);
        savedPropertyStates = new HashMap<>();

        initializeLimitOverrides();

        pcs = new PropertyChangeSupport(this);
        ui = new DisplayIlluminatorFrame(this);

        ui.setVisible(true);

        createConfigGroup();

        // Registering this class for events means that its event handlers
        // (that is, methods with the @Subscribe annotation) will be invoked when
        // an event occurs. You need to call the right registerForEvents() method
        // to get events; this one is for the application-wide event bus, but
        // there's also Datastore.registerForEvents() for events specific to one
        // Datastore, and DisplayWindow.registerForEvents() for events specific
        // to one image display window.
        studio.events().registerForEvents(this);
    }

    private void initializeLimitOverrides() {
        Double displayWidth = Double.parseDouble(getProperty(DISPLAY_WIDTH));
        Double displayHeight = Double.parseDouble(getProperty(DISPLAY_HEIGHT));
        propertiesLowerLimitsOverrides = new HashMap<Utilities.DevicePropertyName, Double>();
        propertiesUpperLimitsOverrides = new HashMap<Utilities.DevicePropertyName, Double>();
        propertiesLowerLimitsOverrides.put(CENTER_X, -displayWidth);
        propertiesLowerLimitsOverrides.put(CENTER_Y, -displayHeight);
        propertiesUpperLimitsOverrides.put(CENTER_X, displayWidth);
        propertiesUpperLimitsOverrides.put(CENTER_Y, displayHeight);
        propertiesLowerLimitsOverrides.put(ROTATION, 0.0);
        propertiesUpperLimitsOverrides.put(ROTATION, 360.0);
        propertiesLowerLimitsOverrides.put(DPC_COUNT, 0.0);
        propertiesUpperLimitsOverrides.put(DPC_COUNT, 12.0);
        propertiesLowerLimitsOverrides.put(DPC_HEIGHT, 0.0);
        propertiesLowerLimitsOverrides.put(DPC_WIDTH, 0.0);
        propertiesUpperLimitsOverrides.put(DPC_HEIGHT, displayHeight);
        propertiesUpperLimitsOverrides.put(DPC_WIDTH, displayWidth);
        propertiesLowerLimitsOverrides.put(DPC_INNER_HEIGHT, 0.0);
        propertiesLowerLimitsOverrides.put(DPC_INNER_WIDTH, 0.0);
        propertiesUpperLimitsOverrides.put(DPC_INNER_HEIGHT, displayHeight);
        propertiesUpperLimitsOverrides.put(DPC_INNER_WIDTH, displayWidth);
        propertiesLowerLimitsOverrides.put(PC_HEIGHT, 0.0);
        propertiesLowerLimitsOverrides.put(PC_WIDTH, 0.0);
        propertiesUpperLimitsOverrides.put(PC_HEIGHT, displayHeight);
        propertiesUpperLimitsOverrides.put(PC_WIDTH, displayWidth);
        propertiesLowerLimitsOverrides.put(PC_INNER_HEIGHT, 0.0);
        propertiesLowerLimitsOverrides.put(PC_INNER_WIDTH, 0.0);
        propertiesUpperLimitsOverrides.put(PC_INNER_HEIGHT, displayHeight);
        propertiesUpperLimitsOverrides.put(PC_INNER_WIDTH, displayWidth);
        propertiesLowerLimitsOverrides.put(DF_HEIGHT, 0.0);
        propertiesLowerLimitsOverrides.put(DF_WIDTH, 0.0);
        propertiesUpperLimitsOverrides.put(DF_HEIGHT, displayHeight);
        propertiesUpperLimitsOverrides.put(DF_WIDTH, displayWidth);
        propertiesLowerLimitsOverrides.put(DF_INNER_HEIGHT, 0.0);
        propertiesLowerLimitsOverrides.put(DF_INNER_WIDTH, 0.0);
        propertiesUpperLimitsOverrides.put(DF_INNER_HEIGHT, displayHeight);
        propertiesUpperLimitsOverrides.put(DF_INNER_WIDTH, displayWidth);
        propertiesLowerLimitsOverrides.put(BF_HEIGHT, 0.0);
        propertiesLowerLimitsOverrides.put(BF_WIDTH, 0.0);
        propertiesUpperLimitsOverrides.put(BF_HEIGHT, displayHeight);
        propertiesUpperLimitsOverrides.put(BF_WIDTH, displayWidth);
    }

    public void addPropertyChangeListener(Utilities.DevicePropertyName propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName.toString(), listener);
    }

    public void removePropertyChangeListener(Utilities.DevicePropertyName propertyName, PropertyChangeListener listener) {
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
            if (source == UpdateSource.MM_CORE) {
                oldValue = null;  // pcs won't fire property change if old value is same as new. Could avoid this by storing old values in this class.
            }

            if (source == UpdateSource.UI && (!updateOngoing || liveUpdateCore)) {
                mmCore.setProperty(deviceLabel, propertyName, value);
                studio.app().refreshGUI();
            }
            if (source != UpdateSource.INTERNAL) {
                pcs.firePropertyChange(propertyName, oldValue, value);
                ui.repaint();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setProperty(Utilities.DevicePropertyName propertyName, String value, UpdateSource source, boolean updateOngoing) {
        setProperty(propertyName.toString(), value, source, updateOngoing);
    }

    public void setProperty(Utilities.DevicePropertyName propertyName, String value, UpdateSource source) {
        setProperty(propertyName.toString(), value, source, false);
    }

    public void setProperty(Utilities.DevicePropertyName propertyName, Color color, UpdateSource source) {
        setProperty(propertyName.toString(), Utilities.colorToHexString(color), source, false);
    }

    public String getProperty(Utilities.DevicePropertyName propertyName) {
        return getProperty(propertyName.toString());
    }

    private String getProperty(String propertyName) {
        try {
            return mmCore.getProperty(deviceLabel, propertyName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double getPropertyLowerLimit(Utilities.DevicePropertyName propertyName) {
        try {
            return propertiesLowerLimitsOverrides.getOrDefault(propertyName,
                    mmCore.getPropertyLowerLimit(deviceLabel, propertyName.toString())
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double getPropertyUpperLimit(Utilities.DevicePropertyName propertyName) {
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
            for (Utilities.DevicePropertyName propertyName: Utilities.DevicePropertyName.values()) {
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
            for (Utilities.DevicePropertyName propertyName: Utilities.DevicePropertyName.values()) {
                mmCore.setProperty(deviceName, propertyName.toString(),
                        savedPropertyStates.get(stateName).get(propertyName));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void createConfigGroup() {
        try {
            String groupName = "IlluminationModes";
            mmCore.defineConfigGroup(groupName);
            for (String imageName: mmCore.getAllowedPropertyValues(deviceLabel, ACTIVE_IMAGE.toString())) {
                mmCore.defineConfig(groupName, imageName, deviceLabel, ACTIVE_IMAGE.toString(), imageName);
            }
            mmCore.setConfig(groupName, "Off");
            mmCore.waitForConfig(groupName, "Off");
            studio.app().refreshGUI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
