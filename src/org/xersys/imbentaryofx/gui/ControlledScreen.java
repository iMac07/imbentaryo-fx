package org.xersys.imbentaryofx.gui;

import org.xersys.imbentaryofx.gui.MainScreenController;
import org.xersys.commander.iface.XNautilus;

public interface ControlledScreen {
    public void setNautilus(XNautilus foValue);
    public void setParentController(MainScreenController foValue);
    public void setScreensController(ScreensController foValue);
    public void setDashboardScreensController(ScreensController foValue);
}
