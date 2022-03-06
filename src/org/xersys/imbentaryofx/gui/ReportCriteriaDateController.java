package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import org.xersys.commander.util.SQLUtil;

public class ReportCriteriaDateController implements Initializable {
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private DatePicker dateFrom;
    @FXML
    private DatePicker dateThru;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);
    }    
    
    public String getDateFrom(){
        return SQLUtil.dateFormat(dateFrom.valueProperty().getValue(), SQLUtil.FORMAT_SHORT_DATE);
    }
    
    public String getDateThru(){
        return SQLUtil.dateFormat(dateThru.valueProperty().getValue(), SQLUtil.FORMAT_SHORT_DATE);
    }
}
