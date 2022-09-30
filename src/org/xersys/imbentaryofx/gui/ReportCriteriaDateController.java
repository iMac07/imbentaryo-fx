package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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

    @FXML
    private void dateFrom_Action(ActionEvent event) {
        System.err.println(dateFrom.valueProperty().getValue());
        System.err.println(SQLUtil.dateFormat(dateFrom.valueProperty().getValue(), SQLUtil.FORMAT_SHORT_DATE));
    }

    @FXML
    private void dateThru_Action(ActionEvent event) {
        System.err.println(dateThru.valueProperty().getValue());
        System.err.println( SQLUtil.dateFormat(dateThru.valueProperty().getValue(), SQLUtil.FORMAT_SHORT_DATE));
    }
}
