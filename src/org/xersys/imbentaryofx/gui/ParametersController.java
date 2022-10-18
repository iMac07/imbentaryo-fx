/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.xersys.imbentaryofx.gui;

import static java.lang.Thread.sleep;
import java.net.URL;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.clients.base.APClient;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.contants.UserLevel;
import org.xersys.commander.iface.LRecordMas;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.StringHelper;
import org.xersys.commander.util.StringUtil;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;

/**
 * FXML Controller class
 *
 * @author user1
 */
public class ParametersController implements Initializable, ControlledScreen{
    @FXML
    private AnchorPane AnchorParameterMain;
    @FXML
    private ComboBox cmbParameters;
    @FXML
    private Label lblTitle;
    @FXML
    public AnchorPane AnchorPaneParamBody;
    
    public void setNautilus(XNautilus foValue){
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorParameterMain.setTopAnchor(AnchorParameterMain, 0.0);
        AnchorParameterMain.setBottomAnchor(AnchorParameterMain, 0.0);
        AnchorParameterMain.setLeftAnchor(AnchorParameterMain, 0.0);
        AnchorParameterMain.setRightAnchor(AnchorParameterMain, 0.0);  
        if (_nautilus  == null) {
            System.err.println("Application driver is not set.");
            System.exit(1);
        }
        
        cmbParameters.setItems(_lsParams);
        cmbParameters.getSelectionModel().select(0);
        loadScreen(ScreenInfo.NAME.MODEL);
//        cmbParameters.valueProperty().addListener(new ChangeListener<String>() {
//            @Override public void changed(ObservableValue ov, String t, String t1) {    
//                loadParamScreen();
//            }    
//        });
//        
        cmbParameters.setOnAction(this::cmbParameters_Click);
        
    }    
    
    
    
    
    private void loadScreen(ScreenInfo.NAME  foValue){
        
        
        JSONObject loJSON = ScreenInfo.get(foValue);
        ParameterControlledScreen instance;
        if (loJSON != null){
            
            _param_screens_controller = new ParameterScreenController();
            _param_screens_controller.setParentPane(AnchorPaneParamBody);
            _param_screens_controller.setParentController(this);
            String lsValue = (String) cmbParameters.getSelectionModel().getSelectedItem();
        
            lblTitle.setText(lsValue.toUpperCase());  
            instance = (ParameterControlledScreen) CommonUtil.createInstance((String) loJSON.get("controller"));
            instance.setNautilus(_nautilus);
//            instance.setParentPane(AnchorPaneParamBody);
            instance.setParentController(this);
            instance.setScreensController(_param_screens_controller);
            
            _param_screens_controller.loadScreen((String) loJSON.get("resource"), instance);
        }
    }
    
    
   
    
    private void cmbParameters_Click(Event event) {
        ComboBox loButton = (ComboBox) event.getSource();
        
        int lnIndex = loButton.getSelectionModel().getSelectedIndex();
        switch (lnIndex){
            case 0:
                loadScreen(ScreenInfo.NAME.MODEL); break;
            case 1:
                loadScreen(ScreenInfo.NAME.BRAND); break;
            case 2:
                loadScreen(ScreenInfo.NAME.COLOR); break;
            case 3:
                loadScreen(ScreenInfo.NAME.BARANGAY); break;
            case 4:
                loadScreen(ScreenInfo.NAME.TOWN); break;
            case 5:
                loadScreen(ScreenInfo.NAME.PROVINCE); break;
        }
    }
    
    public Stage getStage(){
        return (Stage) AnchorParameterMain.getScene().getWindow();
    }
    
    
    private static XNautilus _nautilus;
    private static ScreensController _screens_controller;
    private static ParameterScreenController _param_screens_controller;
    private static ScreensController _screens_dashboard_controller;
    
    private static MainScreenController _main_screen_controller;
    
    //public String _no_tab_screen;
    public ArrayList<String> _no_tab;
    
    private boolean _control_pressed;
    private boolean _shift_pressed;
    
    private boolean _logged;
    
    private Thread _session;
    private int _seconds;
    private final int _limit = 900; //900 seconds is 5 minutes
     private ObservableList<String> _lsParams = FXCollections.observableArrayList("Model", "Brand", "Color", "Barangay", "Town", "Province");
    
    Map<String, AnchorPane> btn = new HashMap<String,AnchorPane>();
    Map<String, Label> lbl = new HashMap<String,Label>();
    Map<String, ImageView> img = new HashMap<String,ImageView>();
}