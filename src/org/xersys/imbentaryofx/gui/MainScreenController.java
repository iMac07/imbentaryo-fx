package org.xersys.imbentaryofx.gui;

import org.xersys.imbentaryofx.gui.handler.ScreenInfo;
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
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.kumander.iface.XNautilus;
import org.xersys.kumander.util.CommonUtil;

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
    private Label lblDate;
    @FXML
    private Label lblCompany;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblDate1;
    @FXML
    private Label lblCompany1;
    @FXML
    private Label lblUser1;
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
        loadScreen(ScreenInfo.NAME.POS);
        
        //load the dashboard
        JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.DASHBOARD);
        if (loJSON != null) _screens_dashboard_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) CommonUtil.createInstance((String) loJSON.get("controller")));          
    }
    
    private void initButton(){
        btnOther01.setOnMouseClicked(this::cmdMouse_Click);
        btnOther02.setOnMouseClicked(this::cmdMouse_Click);
        btnOther03.setOnMouseClicked(this::cmdMouse_Click);
        btnOther04.setOnMouseClicked(this::cmdMouse_Click);
    }
    
    private void cmdMouse_Click(MouseEvent event) {
        String lsButton = ((AnchorPane) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch(lsButton){
            case "btnOther01": //point of sales
                loadScreen(ScreenInfo.NAME.POS);
                break;
            case "btnOther02": //customer order
                loadScreen(ScreenInfo.NAME.CUSTOMER_ORDER);
                break;
            case "btnOther03": //job order
                loadScreen(ScreenInfo.NAME.JOB_ORDER);
                break;
            case "btnOther04": //purchasing
                loadScreen(ScreenInfo.NAME.PURCHASE_ORDER);
                break;
            case "btnOther05": //wholesale
                break;
            case "btnOther06": //inventory
                break;
            case "btnOther07": //warehousing
                break;
            case "btnOther08": //cashflow
                break;
            case "btnOther09": //reports
                break;
            case "btnOther10": //parameters
                break;
            case "btnOther11":
                break;
            case "btnOther12":
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
                    case "PartsInquiry":
                    case "PartsCatalogue":
                    case "PartsCatalogueDetail":
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