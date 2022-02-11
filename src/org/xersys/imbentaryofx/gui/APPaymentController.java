package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.xersys.commander.iface.XNautilus;

public class APPaymentController implements Initializable, ControlledScreen {

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private VBox btnbox00;
    @FXML
    private Button btn01;
    @FXML
    private Button btn02;
    @FXML
    private Button btn03;
    @FXML
    private Button btn04;
    @FXML
    private Button btn05;
    @FXML
    private Button btn06;
    @FXML
    private Button btn07;
    @FXML
    private Button btn08;
    @FXML
    private Button btn09;
    @FXML
    private Button btn10;
    @FXML
    private Button btn11;
    @FXML
    private Button btn12;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField10;
    @FXML
    private TextField txtField051;
    @FXML
    private TextField txtField0511;
    @FXML
    private Label lblPayable;
    @FXML
    private TableView<?> _table;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @Override
    public void setNautilus(XNautilus foValue) {
        
    }

    @Override
    public void setParentController(MainScreenController foValue) {
        
    }

    @Override
    public void setScreensController(ScreensController foValue) {
        
    }

    @Override
    public void setDashboardScreensController(ScreensController foValue) {
        
    }
    
}
