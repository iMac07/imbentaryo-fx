package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;

public class MainScreenController implements Initializable {
    @FXML
    private AnchorPane AnchorPaneMain;
    @FXML
    private StackPane StackPaneBody;
    @FXML
    private AnchorPane AnchorPaneHeader;
    @FXML
    private AnchorPane AnchorPaneFooter;
    @FXML
    public AnchorPane AnchorPaneMonitor;
    @FXML
    public AnchorPane AnchorPaneBody;
    @FXML
    public VBox SidePane;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblCompany;
    @FXML
    private Label lblUser;
    @FXML
    private AnchorPane btnOther01;
    @FXML
    private AnchorPane btnOther02;
    @FXML
    private AnchorPane btnOther03;
    @FXML
    private AnchorPane btnOther04;
    @FXML
    private AnchorPane btnOther05;
    @FXML
    private AnchorPane btnOther06;
    @FXML
    private AnchorPane btnOther07;
    @FXML
    private AnchorPane btnOther08;
    @FXML
    private AnchorPane btnOther09;
    @FXML
    private AnchorPane btnOther10;
    @FXML
    private AnchorPane btnOther;
    @FXML
    private AnchorPane AnchorPaneSeparator;
    @FXML
    private AnchorPane btnOther11;
    @FXML
    private AnchorPane btnOther12;
    
    public void setNautilus(XNautilus foValue){
        _nautilus = foValue;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (_nautilus  == null) {
            System.err.println("Application driver is not set.");
            System.exit(1);
        }

        _screens_controller = new ScreensController();
        _screens_controller.setParentPane(AnchorPaneBody);
        _screens_controller.setParentController(this);
        
        _screens_dashboard_controller = new ScreensController();
        _screens_dashboard_controller.setParentPane(AnchorPaneMonitor);
        _screens_dashboard_controller.setParentController(this);
        
        //keyboard events
        AnchorPaneMain.setOnKeyPressed(this::keyPressed);
        AnchorPaneMain.setOnKeyReleased(this::keyReleased);
        
        initScreen();
        initButton();
    }    
    
    private void loadScreen(ScreenInfo.NAME  foValue){
        JSONObject loJSON = ScreenInfo.get(foValue);
        ControlledScreen instance;
        
        if (loJSON != null){
            instance = (ControlledScreen) CommonUtil.createInstance((String) loJSON.get("controller"));
            instance.setNautilus(_nautilus);
            instance.setParentController(this);
            instance.setScreensController(_screens_controller);
            instance.setDashboardScreensController(_screens_dashboard_controller);
            
            _screens_controller.loadScreen((String) loJSON.get("resource"), instance);
        }
    }
    
    private void initScreen(){
        //load main form and request focus on its button
        //loadScreen(ScreenInfo.NAME.POS);
        loadScreen(ScreenInfo.NAME.SP_SALES);
        
        //load the dashboard
        JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.DASHBOARD);
        if (loJSON != null) _screens_dashboard_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) CommonUtil.createInstance((String) loJSON.get("controller")));          
    }
    
    private void initButton(){
        btnOther01.setOnMouseClicked(this::cmdMouse_Click);
        btnOther02.setOnMouseClicked(this::cmdMouse_Click);
        btnOther03.setOnMouseClicked(this::cmdMouse_Click);
        btnOther04.setOnMouseClicked(this::cmdMouse_Click);
        btnOther05.setOnMouseClicked(this::cmdMouse_Click);
        btnOther06.setOnMouseClicked(this::cmdMouse_Click);
        btnOther07.setOnMouseClicked(this::cmdMouse_Click);
        btnOther08.setOnMouseClicked(this::cmdMouse_Click);
        btnOther09.setOnMouseClicked(this::cmdMouse_Click);
        btnOther10.setOnMouseClicked(this::cmdMouse_Click);
    }
    
    private void cmdMouse_Click(MouseEvent event) {
        String lsButton = ((AnchorPane) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch(lsButton){
            case "btnOther01": //purchasing
                loadScreen(ScreenInfo.NAME.PURCHASE_ORDER);
                break;
            case "btnOther02": //sales
                loadScreen(ScreenInfo.NAME.SP_SALES);
                break;
            case "btnOther03": //job order
                loadScreen(ScreenInfo.NAME.JOB_ORDER);
                break;
            case "btnOther04": //customer order
                //loadScreen(ScreenInfo.NAME.CUSTOMER_ORDER);
                break;
            case "btnOther05": //wholesale
                break;
            case "btnOther06": //inventory
                loadScreen(ScreenInfo.NAME.SP_INV_MASTER);
                break;
            case "btnOther07": //warehousing
                break;
            case "btnOther08": //payment
                loadScreen(ScreenInfo.NAME.CASHIERING);
                break;
            case "btnOther09": //cashflow
                break;
            case "btnOther10": //clients
                loadScreen(ScreenInfo.NAME.CLIENT_MASTER);                
                break;
        }
    }
    
    public void keyReleased(KeyEvent event) {
        switch(event.getCode()){
            case ESCAPE:
                break; 
            case CONTROL:
                _control_pressed = false;
                break;
            case SHIFT:
                _shift_pressed = false;
                break;
            case TAB:
                //_control_pressed = false;
                //_shift_pressed = false;
                break;
        }
    }
    
    public void keyPressed(KeyEvent event) {
        switch(event.getCode()){
            case CONTROL:
                _control_pressed = true;
                break; 
            case SHIFT:
                _shift_pressed = true;
                break;
            case TAB:
                Node loNode = _screens_controller.getScreen(_screens_controller.getCurrentScreenIndex());
                
                //prevent some window to user prev/fwrd screen
                switch(loNode.getId()){
                    case "POSDetail":
                    case "QuickSearch":
                    case "QuickSearchFilter":
                    case "PartsInquiry":
                    case "PartsCatalogue":
                    case "PartsCatalogueDetail":
                    case "ClientAddress":
                    case "ClientMobile":
                    case "ClientEMail":
                    case "Cashiering":
                    case "Payment":     
                        System.err.println("Request rejected.");
                        break;
                    default:
                        if (_control_pressed){
                            if (_shift_pressed)
                                _screens_controller.prevScreen();
                            else
                                _screens_controller.fwrdScreen();
                        }   
                }

                break;
        }
    }
    
    private static XNautilus _nautilus;
    private static ScreensController _screens_controller;
    private static ScreensController _screens_dashboard_controller;
    
    private boolean _control_pressed;
    private boolean _shift_pressed;
}