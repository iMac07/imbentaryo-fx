package org.xersys.imbentaryofx.listener;

import javafx.scene.control.TextField;
import org.json.simple.JSONObject;

public interface QuickSearchCallback {
    public void Result(TextField foField,JSONObject foValue);
    public void FormClosing();
}
