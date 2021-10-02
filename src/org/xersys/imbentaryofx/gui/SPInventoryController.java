package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreenInfo;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.MsgBox;
import org.xersys.commander.util.SQLUtil;
import java.util.Date;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import org.xersys.clients.base.ClientMaster;
import org.xersys.commander.util.StringUtil;
import org.xersys.imbentaryofx.listener.CachedRowsetCallback;
import javax.sql.rowset.CachedRowSet;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.iface.LRecordMas;

public class SPInventoryController implements Initializable, ControlledScreen{
    private ObservableList<String> _gendercd = FXCollections.observableArrayList("Male", "Female", "LGBTQ+");
    private ObservableList<String> _clienttp = FXCollections.observableArrayList("Individual", "Institution");
    private ObservableList<String> _cvilstat = FXCollections.observableArrayList("Single", "Married", "Separated", "Widowed", "Single Parent", "Single Parent w/ Live-in Partner");
    
    private XNautilus _nautilus;
    private ClientMaster _trans;
    private LRecordMas _listener;
    private CachedRowsetCallback _mobile_listener;
    private CachedRowsetCallback _address_listener;
    private CachedRowsetCallback _email_listener;
    
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
    private TextField txtField05;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField07;
    @FXML
    private TextField txtField10;
    @FXML
    private TextField txtField11;
    @FXML
    private TextField txtField12;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField08;
    @FXML
    private TextField txtField09;
    @FXML
    private ComboBox cmbInvStat;
    @FXML
    private CheckBox chkCombo;
    @FXML
    private CheckBox chkSerialized;
    @FXML
    private CheckBox chkPromo;
    @FXML
    private CheckBox chkActive;
    
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
        
        _trans = new ClientMaster(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
        _trans.setSaveToDisk(true);
        _trans.setListener(_listener);
        
        clearFields();
        
        initButton();
        
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
    
    private void createNew(String fsOrderNox){
        if (!_trans.NewRecord(fsOrderNox)){
            System.err.println(_trans.getMessage());
            MsgBox.showOk(_trans.getMessage(), "Warning");
            System.exit(1);
        }
        
        clearFields();
        loadTransaction();
    }
    
    private void txtArea_KeyPressed(KeyEvent event) {
        TextArea txtField = (TextArea) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
        
        if (event.getCode() == KeyCode.ENTER) event.consume();
        
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            FXUtil.SetNextFocus(txtField);
            break;
        case UP:
            FXUtil.SetPreviousFocus(txtField);
        }
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        if (event.getCode() == KeyCode.ENTER){
            switch (lsTxt){
                case "txtField10":
                    System.out.println(this.getClass().getSimpleName() + " " + lsTxt + " was used for searching");                    
                    searchCitizenship("sCntryNme", lsValue, "", "", false);
                    event.consume();
                    return;
                case "txtField12":
                    System.out.println(this.getClass().getSimpleName() + " " + lsTxt + " was used for searching");                    
                    searchTownCity("sTownName", lsValue, "", "", false);
                    event.consume();
                    return;
                case "txtField14":
                    System.out.println(this.getClass().getSimpleName() + " " + lsTxt + " was used for searching");                    
                    break;
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
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField10.setText("");
        txtField11.setText("");
        txtField12.setText("");
    }
    
    private void loadTransaction(){
        try {
            txtField03.setText((String) _trans.getMaster("sLastName"));
            txtField04.setText((String) _trans.getMaster("sFrstName"));
            txtField05.setText((String) _trans.getMaster("sMiddName"));
            txtField06.setText((String) _trans.getMaster("sSuffixNm"));
            txtField07.setText((String) _trans.getMaster("sClientNm"));
            

            txtField10.setText((String) _trans.getMaster("sCntryNme"));
            txtField12.setText((String) _trans.getMaster("sTownName"));
        } catch (NumberFormatException | SQLException ex) {
            MsgBox.showOk(ex.getMessage(), "Warning");
            ex.printStackTrace();
            System.exit(1);
        }
    }    
    
    private void searchCitizenship(String fsKey, Object foValue, String fsFilter, String fsValue, boolean fbExact){
        JSONObject loJSON = _trans.searchCountry(fsKey, foValue, fsFilter, fsValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans.setMaster("sCitizenx", (String) loJSON.get("sCntryCde"));
                        FXUtil.SetNextFocus(txtField10);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchCountry());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtField10);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                MsgBox.showOk("ParseException detected.", "Warning");
                txtField10.setText("");
                FXUtil.SetNextFocus(txtField10);
            }
        } else {
            MsgBox.showOk((String) loJSON.get("message"), "Warning");
            txtField10.setText("");
            FXUtil.SetNextFocus(txtField10);
        }
    }
    
    private void searchTownCity(String fsKey, Object foValue, String fsFilter, String fsValue, boolean fbExact){
        JSONObject loJSON = _trans.searchTownCity(fsKey, foValue, fsFilter, fsValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans.setMaster("sBirthPlc", (String) loJSON.get("sTownIDxx"));
                        FXUtil.SetNextFocus(txtField12);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchTownCity());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtField12);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                MsgBox.showOk("ParseException detected.", "Warning");
                txtField12.setText("");
                FXUtil.SetNextFocus(txtField10);
            }
        } else {
            MsgBox.showOk((String) loJSON.get("message"), "Warning");
            txtField12.setText("");
            FXUtil.SetNextFocus(txtField12);
        }
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btnChild01": //mobile
                loadMobile();
                break;
            case "btnChild02": //address
                break;
            case "btnChild03": //email
                loadEMail();
                break;
            case "btn01": //new
                _loaded = false;
                
                createNew("");
                initButton();
                clearFields();
                loadTransaction();
               
               _loaded = true;
                break;
            case "btn02":                
                break;
            case "btn03": //search
                break;
            case "btn04": //save                
                if (_trans.SaveRecord(true)){
                    MsgBox.showOk("Transaction saved successfully.", "Success");
                    
                    _loaded = false;

                    initButton();
                    clearFields();

                   _loaded = true;
                } else 
                    MsgBox.showOk(_trans.getMessage(), "Warning");
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
            case "btn11": //history
                break;
            case "btn12": //close screen
                if (_screens_controller.getScreenCount() > 1)
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                else{
                    if (MsgBox.showOkCancel("This action will exit the application.", "Please confirm...") == MsgBox.RESP_YES_OK){
                        System.exit(0);
                    }
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
    
    private void loadMobile(){
        JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.CLIENT_MOBILE);

        if (loJSON != null){
            ClientMobileController instance = new ClientMobileController();
            instance.setNautilus(_nautilus);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            instance.setDashboardScreensController(_screens_dashboard_controller);
            instance.setDataListener(_mobile_listener);

            instance.setData(_trans.getMobile());

            _screens_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) instance);
        }
    }
    
    private void loadEMail(){
        JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.CLIENT_EMAIL);

        if (loJSON != null){
            ClientEMailController instance = new ClientEMailController();
            instance.setNautilus(_nautilus);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            instance.setDashboardScreensController(_screens_dashboard_controller);
            instance.setDataListener(_email_listener);

            instance.setData(_trans.getEMail());

            _screens_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) instance);
        }
    }
    
    private void initListener(){
        _listener = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch (fsFieldNm){
                    case "sLastName":
                        txtField03.setText((String) foValue); break;
                    case "sFrstName":
                        txtField04.setText((String) foValue); break;
                    case "sMiddName":
                        txtField05.setText((String) foValue); break;
                    case "sSuffixNm":
                        txtField06.setText((String) foValue); break;
                    case "sClientNm":
                        txtField07.setText((String) foValue); break;
                    case "dBirthDte":
                        txtField11.setText(SQLUtil.dateFormat((Date) foValue, SQLUtil.FORMAT_MEDIUM_DATE)); break;                   
                    case "sCntryNme":
                        txtField10.setText((String) foValue); break;
                    case "sTownName":
                        txtField12.setText((String) foValue); break;
                }
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if ("success".equals((String) foValue.get("result"))){
                    foValue = (JSONObject) foValue.get("payload");
                
                    switch (foField.getId()){
                        case "txtField10":
                            _trans.setMaster("sCitizenx", (String) foValue.get("sCntryCde"));
                            break;
                        case "txtField12":
                            _trans.setMaster("sBirthPlc", (String) foValue.get("sTownIDxx"));
                            break;
                    }
                }
            }

            @Override
            public void FormClosing(TextField foField) {
                FXUtil.SetNextFocus(foField);
            }
        };
        
        _mobile_listener = new CachedRowsetCallback() {
            @Override
            public void Result(CachedRowSet foValue) {
                _trans.setMaster("xMobileNo", foValue);
            }

            @Override
            public void FormClosing() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        _email_listener = new CachedRowsetCallback() {
            @Override
            public void Result(CachedRowSet foValue) {
                _trans.setMaster("xEmailAdd", foValue);
            }

            @Override
            public void FormClosing() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        btn11.setText("History");
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
        
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        txtField06.setDisable(!lbShow);
        txtField07.setDisable(!lbShow);
        txtField10.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
    }
    
    private void initFields(){              
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        
        txtField07.setEditable(false);
    }
    
    private void cmbGenderCd_Click(Event event) {
        ComboBox loButton = (ComboBox) event.getSource();

        int lnIndex = loButton.getSelectionModel().getSelectedIndex();
        if (lnIndex >= 0) _trans.setMaster("cGenderCd", String.valueOf(lnIndex));
    }
    
    private void cmbCvilStat_Click(Event event) {
        ComboBox loButton = (ComboBox) event.getSource();

        int lnIndex = loButton.getSelectionModel().getSelectedIndex();
        if (lnIndex >= 0) _trans.setMaster("cCvilStat", String.valueOf(lnIndex));
    }
        
    private void cmbBox_KeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER){
            ComboBox loButton = (ComboBox) event.getSource();
            FXUtil.SetNextFocus(loButton);
        }
    }
    
    private void cmbClientTp_Click(Event event) {
        ComboBox loButton = (ComboBox) event.getSource();

        int lnIndex = loButton.getSelectionModel().getSelectedIndex();
        if (lnIndex >= 0) {
            _trans.setMaster("cClientTp", String.valueOf(lnIndex));
            txtField07.setEditable(lnIndex == 1);
        }
    }
    
    final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        if(!nv){ //Lost Focus      
            switch (lnIndex){
                case 13: //additional info
                    _trans.setMaster("sAddlInfo", lsValue);
                    break;
                default:
                    MsgBox.showOk("Text field with name " + txtField.getId() + " not registered.", "Warning");
            }
            _index = lnIndex;
        } else{ //Got Focus
            _index = lnIndex;
            txtField.selectAll();
        }
    };
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        if(!nv){ //Lost Focus          
            switch (lnIndex){
                case 3: //last name
                    _trans.setMaster("sLastName", lsValue);
                    break;
                case 4: //first name
                    _trans.setMaster("sFrstName", lsValue);
                    break;
                case 5: //middle name
                    _trans.setMaster("sMiddName", lsValue);
                    break;
                case 6: //suffix name
                    _trans.setMaster("sSuffixNm", lsValue);
                    break;
                case 7: //full name/company name
                    _trans.setMaster("sClientNm", lsValue);
                    break;
                case 11: //birthday                        
                    if (StringUtil.isDate(lsValue, SQLUtil.FORMAT_SHORT_DATE)){
                        _trans.setMaster("dBirthDte", SQLUtil.toDate(lsValue, SQLUtil.FORMAT_SHORT_DATE));
                    } else {
                        MsgBox.showOk("Please encode a date with this format " + SQLUtil.FORMAT_SHORT_DATE + ".", "Warning");
                        txtField.requestFocus();
                    }
                    break;
                default:
                    MsgBox.showOk("Text field with name " + txtField.getId() + " not registered.", "Warning");
            }
            _index = lnIndex;
        } else{ //Got Focus
            try{
                switch (lnIndex){
                    case 11:
                        txtField.setText(SQLUtil.dateFormat((Date) _trans.getMaster("dBirthDte"), SQLUtil.FORMAT_SHORT_DATE));
                        break;
                }
            } catch (SQLException ex) {
                MsgBox.showOk(ex.getMessage(), "Warning");
                ex.printStackTrace();
                System.exit(1);
            }
            
            _index = lnIndex;
            txtField.selectAll();
        }
    };    
}