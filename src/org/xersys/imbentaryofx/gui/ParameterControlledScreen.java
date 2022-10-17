package org.xersys.imbentaryofx.gui;

import org.xersys.commander.iface.XNautilus;

public interface ParameterControlledScreen {
    public void setNautilus(XNautilus foValue);
    public void setParentController(ParametersController foValue);
    public void setScreensController(ParameterScreenController foValue);
}
