package org.xersys.imbentaryofx.gui.handler;

import org.xersys.imbentaryofx.gui.MainScreenController;
import org.xersys.kumander.iface.XNautilus;

public interface ControlledScreen {
    public void setNautilus(XNautilus foValue);
    public void setParentController(MainScreenController foValue);
    public void setScreensController(ScreensController foValue);
    public void setDashboardScreensController(ScreensController foValue);
}
