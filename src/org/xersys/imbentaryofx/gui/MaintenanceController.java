package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONObject;
import org.xersys.commander.contants.UserLevel;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.parameters.base.Sizes;

public class MaintenanceController implements Initializable, ControlledScreen {
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private Button btn01;
    @FXML
    private Button btn02;
    @FXML
    private Button btn03;
    @FXML
    private TableView table;
    @FXML
    private ComboBox Combo1;
    
    private XNautilus _nautilus;
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    
    private ObservableList<String> _items = FXCollections.observableArrayList("Brand", "Labor", "Measurement", "Model", "Sizes", "System Users");
    
    private TableModel _table_model;
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);   
        
        if (_nautilus  == null) {
            System.err.println("Application driver is not set.");
            System.exit(1);
        }
        
        Combo1.setItems(_items);
        Combo1.getSelectionModel().select(0);
        
        btn01.setOnAction(this::cmdButton_Click);
        btn02.setOnAction(this::cmdButton_Click);
        btn03.setOnAction(this::cmdButton_Click);
        
        initGrid();
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
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //load records
                break;
            case "btn02": //load form
                switch (Combo1.getSelectionModel().getSelectedItem().toString().toLowerCase()){
                    case "brand":
                        loadScreen(ScreenInfo.NAME.BRAND); break;
                    case "labor":
                        loadScreen(ScreenInfo.NAME.LABOR); break;
                    case "measurement":
                        loadScreen(ScreenInfo.NAME.MEASURE); break;
                    case "model":
                        loadScreen(ScreenInfo.NAME.MODEL); break;
                    case "sizes":
                        loadScreen(ScreenInfo.NAME.SIZES); break;
                    case "system users":
                        if (((int) _nautilus.getUserInfo("nUserLevl") & UserLevel.SYSADMIN + UserLevel.MASTER) != 0){
                            loadScreen(ScreenInfo.NAME.USER_MANAGER); break;
                        } else {
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), "User is not allowed to access this feature.", "Warning", "");
                        }
                }
                break;
            case "btn03": //close
                if (_screens_controller.getScreenCount() > 1)
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                else{
                    if (ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to exit the application?", "Please confirm", ""))
                        System.exit(0);
                }
                break;
        }
    }
    
    private void initGrid(){
        table.getColumns().clear();
        
        table.setItems(_table_data);
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
            System.out.println((String) loJSON.get("controller"));
            _screens_controller.loadScreen((String) loJSON.get("resource"), instance);
        }
    }
}
