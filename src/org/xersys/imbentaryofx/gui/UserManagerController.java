package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
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
import org.xersys.clients.base.SysUser;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.iface.LRecordMas;

public class UserManagerController implements Initializable, ControlledScreen{    
    private ObservableList<String> _userlevel = FXCollections.observableArrayList("Encoder", "Supervisor", "Manager", "Sys Admin", "Owner", "Master");
    
    private XNautilus _nautilus;
    private LRecordMas _listener;
    
    private SysUser _trans;
    
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
    private TextField txtField01;
    @FXML
    private PasswordField txtField02;
    @FXML
    private ComboBox cmbUserLevel;
    @FXML
    private CheckBox chkSales;
    @FXML
    private CheckBox chkPurchasing;
    @FXML
    private CheckBox chkInventory;
    @FXML
    private CheckBox chkWarehousing;
    @FXML
    private CheckBox chkARAP;
    
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
        
        _trans = new SysUser(_nautilus);
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
            switch (lsTxt){
                case "txtField04":
                    searchClient(txtField, "a.sClientNm", lsValue, false);
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
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        
        chkSales.setSelected(false);
        chkPurchasing.setSelected(false);
        chkInventory.setSelected(false);
        chkWarehousing.setSelected(false);
        chkARAP.setSelected(false);
        
        cmbUserLevel.getSelectionModel().select(0);
    }
    
    private void loadRecord(){
        try {
            txtField01.setText((String) _trans.getMaster("sUsername"));
            txtField02.setText((String) _trans.getMaster("sPassword"));
            txtField03.setText((String) _trans.getMaster("sPassword"));
            txtField04.setText((String) _trans.getMaster("sClientNm"));
            
            switch ((int)_trans.getMaster("nUserLevl")){
                case 1:
                    cmbUserLevel.getSelectionModel().select(0); break;
                case 2:
                    cmbUserLevel.getSelectionModel().select(1); break;
                case 4:
                    cmbUserLevel.getSelectionModel().select(2); break;
                case 8:
                    cmbUserLevel.getSelectionModel().select(3); break;
                case 16:
                    cmbUserLevel.getSelectionModel().select(4); break;
                case 32:
                    cmbUserLevel.getSelectionModel().select(5); break;
            }
            chkSales.setSelected(((int) _trans.getMaster("nObjAcces") & 1) != 0);
            chkPurchasing.setSelected(((int) _trans.getMaster("nObjAcces") & 2) != 0);
            chkInventory.setSelected(((int) _trans.getMaster("nObjAcces") & 4) != 0);
            chkWarehousing.setSelected(((int) _trans.getMaster("nObjAcces") & 8) != 0);
            chkARAP.setSelected(((int) _trans.getMaster("nObjAcces") & 16) != 0);
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
            case "btn01": //browse
                searchTransaction("a.sUserIDxx", "", false);
                break;
            case "btn02":  //new
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
            case "btn03"://clear
                _trans = new SysUser(_nautilus);
                _trans.setListener(_listener);

                clearFields();
                initButton();
                break;
            case "btn04":  //search    
                break;
            case "btn05"://save 
                try {
                    if (!((String) _trans.getMaster("sPassword")).equals(txtField03.getText())){
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Passwords dit not match.", "Warning", "");
                        return;
                    }
                    
                    processLevel();
                    
                    if (_trans.SaveRecord()){
                        ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction saved successfully.", "Success", "");

                        _loaded = false;

                        initButton();
                        clearFields();

                       _loaded = true;
                    } else 
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                    break;
                } catch (ParseException e) {
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), e.getMessage(), "Warning", "");
                }
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
                if (_trans.UpdateRecord()) {
                    initButton();
                }  else 
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
    
    private void processLevel() throws ParseException{
        switch (cmbUserLevel.getSelectionModel().getSelectedIndex()){
            case 0:
                _trans.setMaster("nUserLevl", 1); break;
            case 1:
                _trans.setMaster("nUserLevl", 2); break;
            case 2:
                _trans.setMaster("nUserLevl", 4); break;
            case 3:
                _trans.setMaster("nUserLevl", 8); break;
            case 4:
                _trans.setMaster("nUserLevl", 16); break;
            case 5:
                _trans.setMaster("nUserLevl", 32); break;
            default:
                _trans.setMaster("nUserLevl", 0); break;
        }
        
        int lnAccess = 0;
        
        if (chkSales.selectedProperty().getValue()) lnAccess += 1;
        if (chkPurchasing.selectedProperty().getValue()) lnAccess += 2;
        if (chkInventory.selectedProperty().getValue()) lnAccess += 4;
        if (chkWarehousing.selectedProperty().getValue()) lnAccess += 8;
        if (chkARAP.selectedProperty().getValue()) lnAccess += 16;
        
        _trans.setMaster("nObjAcces", lnAccess);
    }
    private void initListener(){
        _listener = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch (fsFieldNm){
                    case "sUsername":
                        txtField01.setText((String) foValue); break;
                    case "sPassword":
                        txtField02.setText((String) foValue); break;
                    case "sClientID":
                    case "sClientNm":
                        txtField04.setText((String) foValue); break;
                }
            }

            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                switch (fnIndex){
                    case 4:
                        txtField01.setText((String) foValue); break;
                    case 5:
                        txtField02.setText((String) foValue); break;
                    case 6:
                        txtField04.setText((String) foValue); break;
                }
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if ("success".equals((String) foValue.get("result"))){
                    foValue = (JSONObject) foValue.get("payload");
                
                    switch (foField.getId()){
                        case "txtField01":
                            if (_trans.OpenRecord((String) foValue.get("sUserIDxx"))){
                                initButton();
                                loadRecord();
                            } else {
                                ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                                clearFields();
                            }
                            break;
                        case "txtField04":
                            try {
                                _trans.setMaster("sClientID", foValue.get("sClientID"));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
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
        
        btn01.setText("Browse");
        btn02.setText("New");
        btn03.setText("Clear");
        btn04.setText("Search");
        btn05.setText("Save");
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
        btn05.setVisible(true);
        btn06.setVisible(false);
        btn07.setVisible(false);
        btn08.setVisible(false);
        btn09.setVisible(false);
        btn10.setVisible(false);
        btn11.setVisible(true);
        btn12.setVisible(true);
        
        int lnEditMode = _trans.getEditMode();
        boolean lbShow = lnEditMode == EditMode.ADDNEW || lnEditMode == EditMode.UPDATE;
        
        btn01.setVisible(!lbShow);
        btn02.setVisible(!lbShow);
        btn03.setVisible(lbShow);
        btn04.setVisible(lbShow);
        btn05.setVisible(lbShow);

        txtField01.setDisable(!lbShow);       
        txtField02.setDisable(!lbShow);       
        txtField03.setDisable(!lbShow);       
        txtField04.setDisable(!lbShow);
        cmbUserLevel.setDisable(!lbShow);
        
        chkSales.setDisable(!lbShow);
        chkPurchasing.setDisable(!lbShow);
        chkInventory.setDisable(!lbShow);
        chkWarehousing.setDisable(!lbShow);
        chkARAP.setDisable(!lbShow);
        
        cmbUserLevel.setItems(_userlevel);

        if (lbShow)txtField01.requestFocus();
    }
       
    private void initFields(){              
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
                
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
    }
    
    private void searchClient(TextField foField, String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchClient(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        
                        _trans.setMaster("sClientID", loJSON.get("sClientID"));
                        FXUtil.SetNextFocus(foField);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchClient());
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
       
    private void searchTransaction(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchUser(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        
                        if (_trans.OpenRecord((String) loJSON.get("sUserIDxx"))){
                            initButton();
                            loadRecord();
                        } else {
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                            clearFields();
                        }
                        FXUtil.SetNextFocus(txtField01);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchUser());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtField01);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                txtField01.setText("");
                FXUtil.SetNextFocus(txtField01);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtField01.setText("");
            FXUtil.SetNextFocus(txtField01);
        }
    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        if(!nv){ //Lost Focus       
            try {
                switch (lnIndex){
                    case 1:
                        _trans.setMaster("sUsername", lsValue); break;
                    case 2:
                        _trans.setMaster("sPassword", lsValue); break;
                    case 3:
                        break;
                    default:
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Text field with name " + txtField.getId() + " not registered.", "Warning", "");
                }
            } catch (Exception e) {
                ShowMessageFX.Warning(_main_screen_controller.getStage(), e.getMessage(), "Warning", "");
            }
            
            _index = lnIndex;
        } else{ //Got Focus            
            _index = lnIndex;
            txtField.selectAll();
        }
    };        
}