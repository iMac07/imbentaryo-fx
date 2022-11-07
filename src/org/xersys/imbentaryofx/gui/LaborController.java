package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.iface.LRecordMas;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.parameters.base.Labor;

public class LaborController implements Initializable, ControlledScreen{    
    private XNautilus _nautilus;
    private LRecordMas _listener;
    
    private Labor _trans;
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    
    private int _index;
    private boolean _loaded = false;
    
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
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtSeeks02;
    @FXML
    private TextField txtSeeks01;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    
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
        
        initFields();
        initListener();
        
        _trans = new Labor(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
        _trans.setListener(_listener);

        initButton();
        clearFields();
        
        _loaded = true;
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
    
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3){
            if (lsValue == null) lsValue = "";
            
            switch (lsTxt){
                case "txtSeeks01":
                    searchSerial(txtField, "sLaborCde", lsValue, false); break;
                case "txtSeeks02":
                    searchSerial(txtField, "sDescript", lsValue, false); break;
            }
        }
        
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            FXUtil.SetNextFocus(txtField);
            break;
        case UP:
            FXUtil.SetPreviousFocus(txtField);
        }
    }
    
    private void clearFields(){      
        txtSeeks01.setText("");
        txtSeeks02.setText("");
        
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
    }
    
    private void loadRecord(){
        try {
            txtSeeks01.setText((String) _trans.getMaster("sLaborCde"));
            txtSeeks02.setText((String) _trans.getMaster("sDescript"));
            
            txtField01.setText((String) _trans.getMaster("sLaborCde"));
            txtField02.setText((String) _trans.getMaster("sDescript"));
            txtField03.setText((String) _trans.getMaster("sBriefDsc"));
            txtField04.setText(StringUtil.NumberFormat(Double.valueOf(String.valueOf(_trans.getMaster("nPriceLv1"))), "#,##0.00"));
        } catch (NumberFormatException ex) {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), ex.getMessage(), "Warning", "");
            ex.printStackTrace();
            System.exit(1);
        }
    }    
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //new
                _loaded = false;
                
                if (!_trans.NewRecord()){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                    System.exit(1);
                }

                initButton();
                clearFields();
                loadRecord();
                
               _loaded = true;
                break;
            case "btn02": //clear
                _trans = new Labor(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
                _trans.setListener(_listener);

                clearFields();
                initButton();
                break;
            case "btn03": //search
                break;
            case "btn04": //save                
                if (_trans.SaveRecord()){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction saved successfully.", "Success", "");
                    
                    _loaded = false;

                    initButton();
                    clearFields();

                   _loaded = true;
                } else 
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                break;
            case "btn05":
                break;
            case "btn06":
                break;
            case "btn07":
                break;
            case "btn08":
                break;
            case "btn09":
                break;
            case "btn10":
                break;
            case "btn11": //update
                if (_trans.UpdateRecord()){
                    initButton();
                    txtField02.requestFocus();
                    txtField02.selectAll();
                } else 
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                
                break;
            case "btn12": //close screen
                if (_screens_controller.getScreenCount() > 1)
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                else{
                    if (ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to exit the application?", "Please confirm", ""))
                        System.exit(0);
                }
                break;
        }
    }
    
    public void keyReleased(KeyEvent event) {
        switch(event.getCode()){
            case F1:
                break;
            case F2: 
                break;
            case F3:
                break;
            case F4:
                break;
            case F5: 
                break;
            case F6:
                break;
            case F7:
                break;
            case F8: 
                break;
            case F9:
                break;
            case F10:
                break;
            case F11:
                break;
            case F12: 
                break;

        }
    }
    
    private void initListener(){
        _listener = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch (fsFieldNm){
                    case "sDescript":
                        txtField02.setText((String) foValue); break;
                    case "sBriefDsc":
                        txtField03.setText((String) foValue); break;
                    case "nPriceLv1":
                        txtField04.setText(StringUtil.NumberFormat(Double.valueOf(String.valueOf(_trans.getMaster("nPriceLv1"))), "#,##0.00"));
                }
            }

            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                switch (fnIndex){
                    case 2:
                        txtField02.setText((String) foValue); break;
                    case 3:
                        txtField03.setText((String) foValue); break;
                    case 4:
                        txtField04.setText(StringUtil.NumberFormat(Double.valueOf(String.valueOf(_trans.getMaster("nPriceLv1"))), "#,##0.00"));
                }
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if ("success".equals((String) foValue.get("result"))){
                    foValue = (JSONObject) foValue.get("payload");
                
                    switch (foField.getId()){
                        case "txtSeeks01":
                        case "txtSeeks02":
                            if (_trans.OpenRecord((String) foValue.get("sLaborCde"))){
                                loadRecord();
                            } else {
                                ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                                clearFields();
                            }
                            break;
                    }
                }
            }

            @Override
            public void FormClosing(TextField foField) {
                FXUtil.SetNextFocus(foField);
            }
        };
    }
    
    private void initButton(){        
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
        
        btn01.setTooltip(new Tooltip("F1"));
        btn02.setTooltip(new Tooltip("F2"));
        btn03.setTooltip(new Tooltip("F3"));
        btn04.setTooltip(new Tooltip("F4"));
        btn05.setTooltip(new Tooltip("F5"));
        btn06.setTooltip(new Tooltip("F6"));
        btn07.setTooltip(new Tooltip("F7"));
        btn08.setTooltip(new Tooltip("F8"));
        btn09.setTooltip(new Tooltip("F9"));
        btn10.setTooltip(new Tooltip("F10"));
        btn11.setTooltip(new Tooltip("F11"));
        btn12.setTooltip(new Tooltip("F12"));
        
        btn01.setText("New");
        btn02.setText("Clear");
        btn03.setText("Search");
        btn04.setText("Save");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("");
        btn11.setText("Update");
        btn12.setText("Close");              
        
        btn01.setVisible(true);
        btn02.setVisible(true);
        btn03.setVisible(true);
        btn04.setVisible(true);
        btn05.setVisible(false);
        btn06.setVisible(false);
        btn07.setVisible(false);
        btn08.setVisible(false);
        btn09.setVisible(false);
        btn10.setVisible(false);
        btn11.setVisible(true);
        btn12.setVisible(true);
        
        int lnEditMode = _trans.getEditMode();
        boolean lbShow = lnEditMode == EditMode.ADDNEW || lnEditMode == EditMode.UPDATE;
        
        btn02.setVisible(lbShow);
        btn03.setVisible(lbShow);
        btn04.setVisible(lbShow);
        btn11.setVisible(!lbShow);
        
        txtSeeks01.setDisable(lbShow);
        txtSeeks02.setDisable(lbShow);
        
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);       
        txtField04.setDisable(!lbShow);
        
        if (lbShow)
            txtField02.requestFocus();
        else
            txtSeeks01.requestFocus();
    }
       
    private void initFields(){              
        txtSeeks01.setOnKeyPressed(this::txtField_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
                
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
    }
    
    private void searchSerial(TextField foField, String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchLabor(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        if (_trans.OpenRecord((String) loJSON.get("sLaborCde"))){
                                loadRecord();
                            } else {
                                ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                                clearFields();
                            }
                        FXUtil.SetNextFocus(foField);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchLabor());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(foField);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                foField.setText("");
                FXUtil.SetNextFocus(foField);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            foField.setText("");
            FXUtil.SetNextFocus(foField);
        }
    }
       
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        if(!nv){ //Lost Focus          
            switch (lnIndex){
                case 2:
                    _trans.setMaster("sDescript", lsValue); break;
                case 3:
                    _trans.setMaster("sBriefDsc", lsValue); break;
                case 4:
                    try {
                        double lnValue = Double.valueOf(lsValue);
                        _trans.setMaster("nPriceLv1", lnValue); break;
                    } catch (NumberFormatException e) {
                        _trans.setMaster("nPriceLv1", 0.00); break;
                    }
                default:
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Text field with name " + txtField.getId() + " not registered.", "Warning", "");
            }
            _index = lnIndex;
        } else{ //Got Focus      
            switch (lnIndex){
                case 4:
                    txtField04.setText(String.valueOf(_trans.getMaster("nPriceLv1")));
            }
            
            _index = lnIndex;
            txtField.selectAll();
        }
    };        
}