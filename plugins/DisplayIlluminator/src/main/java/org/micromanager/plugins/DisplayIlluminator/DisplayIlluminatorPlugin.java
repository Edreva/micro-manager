package org.micromanager.plugins.DisplayIlluminator;

import mmcorej.CMMCore;
import org.micromanager.MenuPlugin;
import org.micromanager.Studio;
import org.scijava.plugin.Plugin;
import org.scijava.plugin.SciJavaPlugin;


/** The Projector plugin provides a user interface for calibration and control
 * of SLM- and Galvo-based phototargeting devices. Phototargeting can be
 * ad-hoc or planned as part of a multi-dimensional acquisition.
 */
@Plugin(type = MenuPlugin.class)
public class DisplayIlluminatorPlugin implements MenuPlugin, SciJavaPlugin {
    public static final String MENUNAME = "DisplayIlluminator";
    public static final String TOOLTIP_DESCRIPTION =
            "Test";

    private Studio app_;
    private CMMCore core_;
    private DisplayIlluminatorFrame frame_;
//    public void dispose() {
//        ProjectorControlForm pcf = ProjectorControlForm.getSingleton();
//        if (pcf != null) {
//            pcf.dispose();
//        }
//    }

    @Override
    public void setContext(Studio app) {
        app_ = app;
        core_ = app_.getCMMCore();
    }

    @Override
    public void onPluginSelected() {
        if (core_.getSLMDevice().isEmpty()) { // TODO: Perform a check specifically for DeviceIlluminator instance
            app_.logs().showMessage("Please load a display before using plugin");
            return;
        }
        else if (frame_ == null) {
            frame_ = new DisplayIlluminatorFrame(app_);
        }
        frame_.setVisible(true);
        System.out.println("Plugin " + MENUNAME + " has been launched.");
    }

    public void configurationChanged() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        return MENUNAME;
    }

    @Override
    public String getSubMenu() {
        return "Device Control";
    }

    @Override
    public String getHelpText() {
        return TOOLTIP_DESCRIPTION;
    }

    @Override
    public String getVersion() {
        return "V0.1";
    }

    @Override
    public String getCopyright() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}