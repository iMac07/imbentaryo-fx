package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreenInfo;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.kumander.base.Nautilus;
import org.xersys.kumander.iface.XNautilus;
import org.xersys.kumander.util.CommonUtil;

public class JobOrderController implements Initializable, ControlledScreen{
    @FXML
    private AnchorPane AnchorMain;
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
    private HBox btnbox01;
    @FXML
    private HBox btnbox02;
    @FXML
    private VBox btnbox00;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);
        
        //keyboard events
        AnchorMain.setOnKeyPressed(this::keyPressed);
        AnchorMain.setOnKeyReleased(this::keyReleased);
        
        //initialize buttons
        initButton();
    }    

    @Override
    public void setNautilus(XNautilus foValue) {
        _nautilus = foValue;
    }    

    @Override
    public void setParentController(MainScreenController foValue) {
        _main_screen_controller = foValue;
    }
    
    @Override
    public void setScreensController(ScreensController foValue) {
        _screens_controller = foValue;
    }
    
    @Override
    public void setDashboardScreensController(ScreensController foValue) {
        _screens_dashboard_controller = foValue;
    }
    
    private void initButton(){
        btn01.setText("F1 - Confirm JO");
        btn02.setText("F2 - New JO");
        btn03.setText("F3 - Customer Order");
        btn04.setText("F4 - Point-of-Sales");
        btn05.setText("F5 - Parts Catalogue");
        btn06.setText("F6 - Parts Inquiry");
        btn07.setText("F7 - Reports");
        btn08.setText("F8 - Close");
        btn09.setText("");
        btn10.setText("");
        btn11.setText("");
        btn12.setText("");
        
        //Set action event handler for the buttons
        btn01.setOnAction(this::cmdButton_Click);
        btn02.setOnAction(this::cmdButton_Click);
        btn03.setOnAction(this::cmdButton_Click);
        btn04.setOnAction(this::cmdButton_Click);
        btn05.setOnAction(this::cmdButton_Click);
        btn06.setOnAction(this::cmdButton_Click);
        btn07.setOnAction(this::cmdButton_Click);
        btn08.setOnAction(this::cmdButton_Click);
        btn09.setOnAction(this::cmdButton_Click);
        btn10.setOnAction(this::cmdButton_Click);
        btn11.setOnAction(this::cmdButton_Click);
        btn12.setOnAction(this::cmdButton_Click);
        
        if (btn06.getText().isEmpty()){
            btnbox00.setPrefHeight(btnbox00.getPrefHeight() / 2);
            btnbox00.setPadding(new Insets(0, 0, 0, 0));
            btnbox02.setPadding(new Insets(0, 0, 0, 0));
            btnbox02.getChildren().clear();
            btnbox02.setPrefHeight(0);
        }
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        JSONObject loJSON;
        
        switch (lsButton){
            case "btn01": //confirm JO
                System.out.println("Confirm transaction.");
                break;
            case "btn02": //new JO
                System.out.println("Add new transaction.");
                break;
            case "btn03": //customer order
                loadScreen(ScreenInfo.NAME.CUSTOMER_ORDER);
                break;
            case "btn04":
                loadScreen(ScreenInfo.NAME.POS);
                break;
            case "btn05":
                loadScreen(ScreenInfo.NAME.PARTS_CATALOGUE);
                break;
            case "btn06":
                loadScreen(ScreenInfo.NAME.PARTS_INQUIRY);
                break;
            case "btn07": //reports
                break;
            case "btn08": //close
                System.exit(0);
                break;
            case "btn09":
            case "btn10":
            case "btn11":
            case "btn12":
        }
    }
    
    private void loadScreen(ScreenInfo.NAME  foValue){
        JSONObject loJSON = ScreenInfo.get(foValue);
        ControlledScreen instance;
        
        if (loJSON != null){
            instance = (ControlledScreen) CommonUtil.createInstance((String) loJSON.get("controller"));
            instance.setNautilus(_nautilus);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            instance.setDashboardScreensController(_screens_dashboard_controller);
            
            _screens_controller.loadScreen((String) loJSON.get("resource"), instance);
        }
    }
    
    public void keyReleased(KeyEvent event) {
        JSONObject loJSON;
        
        switch(event.getCode()){
            case F1:
                System.out.println("Confirm transaction.");
                break;
            case F2:
                System.out.println("Add new transaction.");
                break;
            case F3:
                loadScreen(ScreenInfo.NAME.CUSTOMER_ORDER);
                break;
            case F4:
                loadScreen(ScreenInfo.NAME.POS);
                break;
            case F5:
                loadScreen(ScreenInfo.NAME.PARTS_CATALOGUE);
                break;
            case F6:
                loadScreen(ScreenInfo.NAME.PARTS_INQUIRY);
                break;
            case F7:
                break;
            case F8:
                System.exit(0);
                break;
            case F9:
            case F10:
            case F11:
            case F12:
            case ESCAPE:
                break; 
            case CONTROL:
                _control_pressed = false;
                break;
            case SHIFT:
                _shift_pressed = false;
                break;
            case TAB:
                _control_pressed = false;
                _shift_pressed = false;
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
                if (_control_pressed){
                    if (_shift_pressed)
                        _screens_controller.prevScreen();
                    else
                        _screens_controller.fwrdScreen();
                }
                break;
        }
    }
    
    private XNautilus _nautilus;
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    
    private boolean _control_pressed;
    private boolean _shift_pressed;

    
}
