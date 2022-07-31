package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import javafx.scene.control.CheckBox;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.commander.contants.AccessLevel;
import org.xersys.commander.util.StringUtil;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.contants.UserLevel;
import org.xersys.commander.iface.LApproval;
import org.xersys.commander.iface.LRecordMas;
import org.xersys.commander.util.SQLUtil;
import org.xersys.inventory.base.Inventory;

public class SPMasterController implements Initializable, ControlledScreen{
    private ObservableList<String> _inv_status = FXCollections.observableArrayList("Inactive", "Active", "Limited Inv", "Push Product", "Stop Production");
    
    private XNautilus _nautilus;
    private LRecordMas _listener;
    private LRecordMas _listener_mas;
    private LApproval _approval;
    
    private Inventory _trans;
    private String _transno;
    
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
    @FXML
    private TextField txtField103;
    @FXML
    private TextField txtField104;
    @FXML
    private TextField txtField105;
    @FXML
    private TextField txtField106;
    @FXML
    private TextField txtField107;
    @FXML
    private TextField txtField109;
    @FXML
    private TextField txtField110;
    @FXML
    private TextField txtField111;
    @FXML
    private TextField txtField112;
    @FXML
    private TextField txtField113;
    @FXML
    private TextField txtField114;
    @FXML
    private TextField txtField108;
    @FXML
    private TextField txtField115;
    @FXML
    private TextField txtField116;
    @FXML
    private CheckBox chkActive1;
    @FXML
    private TextField txtSeeks01;
    @FXML
    private TextField txtSeeks02;
    
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
        
        _trans = new Inventory(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
        _trans.setListener(_listener);
        _trans.getInvMaster().setListener(_listener_mas);

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
                case "txtSeeks01":
                    searchStocks("a.sBarCodex", lsValue, false);
                    event.consume();
                    return;
                case "txtSeeks02":
                    searchStocks("a.sDescript", lsValue, false);
                    event.consume();
                    return;
                case "txtField07":
                    searchBrand("a.sDescript", lsValue, false);
                    event.consume();
                    return;
                case "txtField08":
                    searchModel("a.sDescript", lsValue, false);
                    event.consume();
                    return;
                case "txtField10":
                    searchInvType("sDescript", lsValue, false);
                    event.consume();
                    return;
                case "txtField103":
                    searchInvLocation("sBriefDsc", lsValue, false);
                    event.consume();
                    return;
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
        
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField09.setText("");
        txtField10.setText("");
        txtField11.setText("");
        txtField12.setText("");
        
        txtField103.setText("");
        txtField104.setText("");
        txtField105.setText("");
        txtField106.setText("");
        txtField107.setText("");
        txtField108.setText("");
        txtField109.setText("");
        txtField110.setText("");
        txtField111.setText("");
        txtField112.setText("");
        txtField113.setText("");
        txtField114.setText("");
        txtField115.setText("");
        txtField116.setText("");
        
        cmbInvStat.getSelectionModel().select(0);
        chkCombo.setSelected(false);
        chkSerialized.setSelected(false);
        chkPromo.setSelected(false);
        chkActive.setSelected(false);
        
        _transno = "";
    }
    
    private void loadRecord(){
        try {
            txtSeeks01.setText((String) _trans.getMaster("sBarCodex"));
            txtSeeks02.setText((String) _trans.getMaster("sDescript"));
            
            txtField02.setText((String) _trans.getMaster("sBarCodex"));
            txtField03.setText((String) _trans.getMaster("sDescript"));
            txtField04.setText((String) _trans.getMaster("sBriefDsc"));
            txtField05.setText((String) _trans.getMaster("sAltBarCd"));
            txtField06.setText("");
            txtField07.setText((String) _trans.getMaster("xBrandNme"));
            txtField08.setText((String) _trans.getMaster("xModelNme"));
            txtField09.setText("");
            txtField10.setText((String) _trans.getMaster("xInvTypNm"));
            txtField11.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nUnitPrce"), "#,##0.00"));
            txtField12.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nSelPrce1"), "#,##0.00"));
            
            int lnValue = Integer.parseInt((String) _trans.getMaster("cInvStatx"));
            cmbInvStat.getSelectionModel().select(lnValue);
            
            lnValue = Integer.parseInt((String) _trans.getMaster("cComboInv"));
            chkCombo.setSelected(lnValue == 1);
            
            lnValue = Integer.parseInt((String) _trans.getMaster("cSerialze"));
            chkSerialized.setSelected(lnValue == 1);
            
            lnValue = Integer.parseInt((String) _trans.getMaster("cWthPromo"));
            chkPromo.setSelected(lnValue == 1);
            
            lnValue = Integer.parseInt((String) _trans.getMaster("cRecdStat"));
            chkActive.setSelected(lnValue == 1);
            
            _transno = (String) _trans.getMaster("sStockIDx");
        } catch (NumberFormatException ex) {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), ex.getMessage(), "Warning", "");
            ex.printStackTrace();
            System.exit(1);
        }
    }    
    
    private void loadMaster(){
        txtField103.setText(String.valueOf(_trans.getInvMaster().getMaster("xLocatnNm")));
        txtField104.setText(String.valueOf(_trans.getInvMaster().getMaster("nBinNumbr")));
        txtField105.setText(SQLUtil.dateFormat((Date) _trans.getInvMaster().getMaster("dAcquired"), SQLUtil.FORMAT_MEDIUM_DATE));
        txtField106.setText(SQLUtil.dateFormat((Date) _trans.getInvMaster().getMaster("dBegInvxx"), SQLUtil.FORMAT_MEDIUM_DATE));
        txtField107.setText(String.valueOf(_trans.getInvMaster().getMaster("nBegQtyxx")));
        txtField108.setText(String.valueOf(_trans.getInvMaster().getMaster("nQtyOnHnd")));
        txtField109.setText(String.valueOf(_trans.getInvMaster().getMaster("nMinLevel")));
        txtField110.setText(String.valueOf(_trans.getInvMaster().getMaster("nMaxLevel")));
        txtField111.setText(StringUtil.NumberFormat((Number) _trans.getInvMaster().getMaster("nAvgMonSl"), "#,##0.00"));
        txtField112.setText(StringUtil.NumberFormat((Number) _trans.getInvMaster().getMaster("nAvgMonSl"), "#,##0.00"));
        txtField113.setText(String.valueOf(_trans.getInvMaster().getMaster("cClassify")));
        txtField114.setText(String.valueOf(_trans.getInvMaster().getMaster("nBackOrdr")));
        txtField115.setText(String.valueOf(_trans.getInvMaster().getMaster("nResvOrdr")));
        txtField116.setText(String.valueOf(_trans.getInvMaster().getMaster("nFloatQty")));
        
        chkActive1.setSelected(String.valueOf(_trans.getInvMaster().getMaster("cRecdStat")).equals("1"));
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
                _trans = new Inventory(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
                _trans.setListener(_listener);
                _trans.getInvMaster().setListener(_listener_mas);

                clearFields();
                initButton();
                break;
            case "btn03": //search
                break;
            case "btn04": //save                
                if (_trans.SaveRecord()){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Record saved successfully.", "Success", "");
                    
                    _loaded = false;

                    initButton();
                    
                    if (_trans.OpenRecord(_transno)){
                        loadRecord();

                        if (_trans.getInvMaster().getEditMode() == EditMode.READY) loadMaster();
                    } else clearFields();

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
            case "btn09": //deactivate
                if (_trans.getEditMode() != EditMode.READY){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No record was loaded.", "Warning", "");
                    return;
                }
                
                if (_trans.DeactivateRecord((String) _trans.getMaster("sStockIDx"))){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Record deactivated successfully.", "Success", "");
                    
                    _loaded = false;

                    initButton();
                    if (_trans.OpenRecord(_transno)){
                        loadRecord();

                        if (_trans.getInvMaster().getEditMode() == EditMode.READY) loadMaster();
                    } else clearFields();

                   _loaded = true;
                } else 
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                
                break;
            case "btn10": //activate
                if (_trans.getEditMode() != EditMode.READY){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No record was loaded.", "Warning", "");
                    return;
                }
                
                if (_trans.ActivateRecord((String) _trans.getMaster("sStockIDx"))){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Record activated successfully.", "Success", "");
                    
                    _loaded = false;

                    initButton();
                    if (_trans.OpenRecord(_transno)){
                        loadRecord();

                        if (_trans.getInvMaster().getEditMode() == EditMode.READY) loadMaster();
                    } else clearFields();

                   _loaded = true;
                } else 
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                
                break;
            case "btn11": //update
                if (_trans.getEditMode() != EditMode.READY){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No record was loaded.", "Warning", "");
                    return;
                }
                
                if (_trans.UpdateRecord()) {
                    initButton();
                    
                    if (_trans.getInvMaster().getEditMode() != EditMode.READY){
                        if (ShowMessageFX.YesNo(_main_screen_controller.getStage(), 
                                "This is item is not on your branch inventory.\n\n" +
                                "Do you want to add this item to inventory?", "Confirm", "")){
                            if (_trans.getInvMaster().NewRecord()){
                                loadMaster();
                                disableMasterFields(true);
                                disableInvMasterFields();
                            }
                        }
                    } else {
                        if (_trans.getInvMaster().UpdateRecord()){
                            txtField103.setDisable(false);
                            txtField02.requestFocus();
                            txtField103.selectAll();
                        }   
                    }
                } 
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
        _approval = new LApproval() {
            @Override
            public void Request() {
                _main_screen_controller.seekApproval();
            }
        };
        
        _listener_mas = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsIndex, Object foValue) {
                switch (fsIndex){
                    case "sLocatnCd":
                        txtField103.setText(String.valueOf(foValue)); break;
                }
            }

            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        };
        
        _listener = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch (fsFieldNm){
                    case "sBarCodex":
                        txtField02.setText((String) foValue); break;
                    case "sDescript":
                        txtField03.setText((String) foValue); break;
                    case "sBriefDsc":
                        txtField04.setText((String) foValue); break;
                    case "sAltBarCd":
                        txtField05.setText((String) foValue); break;
                    case "sCategrCd":
                        txtField06.setText((String) foValue); break;
                    case "sBrandCde":
                        txtField07.setText((String) foValue); break;
                    case "sModelCde":
                        txtField08.setText((String) foValue); break;
                    case "sColorCde":
                        txtField09.setText((String) foValue); break;
                    case "sInvTypCd":
                        txtField10.setText((String) foValue); break;
                    case "nUnitPrce":
                        txtField11.setText(StringUtil.NumberFormat((double) foValue, "#,##0.00")); break;
                    case "nSelPrce1":
                        txtField12.setText(StringUtil.NumberFormat((double) foValue, "#,##0.00")); break;
                    case "nBinNumbr":
                        txtField104.setText(String.valueOf(foValue)); break;
                    case "dAcquired":
                        txtField105.setText(SQLUtil.dateFormat((Date) foValue, SQLUtil.FORMAT_MEDIUM_DATE)); break;
                    case "dBegInvxx":
                        txtField106.setText(SQLUtil.dateFormat((Date) foValue, SQLUtil.FORMAT_MEDIUM_DATE)); break;
                    case "nBegQtyxx":
                        txtField107.setText(String.valueOf(foValue)); break;
                }
            }

            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                            if (_trans.OpenRecord((String) foValue.get("sStockIDx"))){
                                loadRecord();
                                
                                if (_trans.getInvMaster().getEditMode() == EditMode.READY) loadMaster();
                            } else {
                                ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                                clearFields();
                            }
                        case "txtField10":
                            _trans.setMaster("sInvTypCd", (String) foValue.get("sInvTypCd"));
                            break;
                        case "txtField07":
                            _trans.setMaster("sBrandCde", (String) foValue.get("sBrandCde"));
                            break;
                        case "txtField08":
                            _trans.setMaster("sModelCde", (String) foValue.get("sModelCde"));
                            break;
                        case "txtField103":
                            _trans.getInvMaster().setMaster("sLocatnCd", (String) foValue.get("sLocatnCd"));
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
        btn02.setText("Cancel");
        btn03.setText("Search");
        btn04.setText("Save");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("Deactivate");
        btn10.setText("Activate");
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
        btn09.setVisible(true);
        btn10.setVisible(true);
        btn11.setVisible(true);
        btn12.setVisible(true);
        
        int lnEditMode = _trans.getEditMode();
        boolean lbShow = lnEditMode == EditMode.ADDNEW || lnEditMode == EditMode.UPDATE;
        
        btn02.setVisible(lbShow);
        btn03.setVisible(lbShow);
        btn04.setVisible(lbShow);
        btn09.setVisible(!lbShow);
        btn10.setVisible(!lbShow);
        btn11.setVisible(!lbShow);
        
        txtSeeks01.setDisable(lnEditMode == EditMode.ADDNEW);
        txtSeeks02.setDisable(lnEditMode == EditMode.ADDNEW);
        
        txtField02.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField03.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField04.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        txtField06.setDisable(true);
        txtField07.setDisable(!lbShow);
        txtField08.setDisable(!lbShow);
        txtField09.setDisable(true);
        txtField10.setDisable(!lbShow);
        txtField11.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField12.setDisable(lnEditMode != EditMode.ADDNEW);
        
        disableInvMasterFields();
        
        //disable these fields temporarilly
        chkCombo.setDisable(true);
        chkSerialized.setDisable(true);
        chkPromo.setDisable(true);
        chkActive.setDisable(true);
        cmbInvStat.setDisable(true);
        
        if (lbShow)
            txtField02.requestFocus();
        else
            txtSeeks01.requestFocus();
    }
    
    private void disableMasterFields(boolean fbValue){
        txtField02.setDisable(fbValue);
        txtField03.setDisable(fbValue);
        txtField04.setDisable(fbValue);
        txtField05.setDisable(fbValue);
        txtField06.setDisable(fbValue);
        txtField07.setDisable(fbValue);
        txtField08.setDisable(fbValue);
        txtField09.setDisable(fbValue);
        txtField10.setDisable(fbValue);
        txtField11.setDisable(fbValue);
        txtField12.setDisable(fbValue);
    }
    
    private void disableInvMasterFields(){
        int lnEditMode = _trans.getInvMaster().getEditMode();
        
        txtField103.setDisable(lnEditMode != EditMode.ADDNEW || lnEditMode != EditMode.UPDATE);
        txtField104.setDisable(lnEditMode != EditMode.ADDNEW);
        
        chkActive1.setDisable(true);
        
        if (lnEditMode != EditMode.ADDNEW) {
            if (txtField103.disableProperty().get() == true)
                txtField104.requestFocus();
            else
                txtField103.requestFocus();
        }
    }
    
    private void initFields(){              
        txtSeeks01.setOnKeyPressed(this::txtField_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        
        txtField103.focusedProperty().addListener(txtField_Focus);
        txtField104.focusedProperty().addListener(txtField_Focus);
        txtField105.focusedProperty().addListener(txtField_Focus);
        txtField106.focusedProperty().addListener(txtField_Focus);
        txtField107.focusedProperty().addListener(txtField_Focus);
        
        txtField103.setOnKeyPressed(this::txtField_KeyPressed);
        txtField104.setOnKeyPressed(this::txtField_KeyPressed);
        txtField105.setOnKeyPressed(this::txtField_KeyPressed);
        txtField106.setOnKeyPressed(this::txtField_KeyPressed);
        txtField107.setOnKeyPressed(this::txtField_KeyPressed);
        
        cmbInvStat.setItems(_inv_status);
    }
    
    private void searchInvType(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchInvType(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

            if (loScreen != null){
                QuickSearchNeoController instance = new QuickSearchNeoController();
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);

                instance.setSearchObject(_trans.getSearchInvType());
                instance.setSearchCallback(_search_callback);
                instance.setTextField(txtField10);

                _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtField10.setText("");
            FXUtil.SetNextFocus(txtField10);
        }
    }
    
    private void searchInvLocation(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.getInvMaster().searchInvLocation(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

            if (loScreen != null){
                QuickSearchNeoController instance = new QuickSearchNeoController();
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);

                instance.setSearchObject(_trans.getInvMaster().getSearchInvLocation());
                instance.setSearchCallback(_search_callback);
                instance.setTextField(txtField103);

                _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtField103.setText("");
            FXUtil.SetNextFocus(txtField103);
        }
    }
    
    private void searchBrand(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchBrand(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

            if (loScreen != null){
                QuickSearchNeoController instance = new QuickSearchNeoController();
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);

                instance.setSearchObject(_trans.getSearchBrand());
                instance.setSearchCallback(_search_callback);
                instance.setTextField(txtField07);

                _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtField07.setText("");
            FXUtil.SetNextFocus(txtField07);
        }
    }
    
    private void searchModel(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchModel(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

            if (loScreen != null){
                QuickSearchNeoController instance = new QuickSearchNeoController();
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);

                instance.setSearchObject(_trans.getSearchModel());
                instance.setSearchCallback(_search_callback);
                instance.setTextField(txtField08);

                _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtField08.setText("");
            FXUtil.SetNextFocus(txtField08);
        }
    }
    
    private void searchStocks(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchStocks(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        
                        if (_trans.OpenRecord((String) loJSON.get("sStockIDx"))){
                            loadRecord();
                            
                            if (_trans.getInvMaster().getEditMode() == EditMode.READY) loadMaster();
                        } else {
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                            clearFields();
                        }
                        FXUtil.SetNextFocus(txtSeeks01);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchStocks());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtSeeks01);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                txtSeeks01.setText("");
                FXUtil.SetNextFocus(txtSeeks01);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtSeeks01.setText("");
            FXUtil.SetNextFocus(txtSeeks01);
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
                    _trans.setMaster("sBarCodex", lsValue); break;
                case 3:
                    _trans.setMaster("sDescript", lsValue); break;
                case 4:
                    _trans.setMaster("sBriefDsc", lsValue); break;
                case 5:
                    _trans.setMaster("sAltBarCd", lsValue); break;
                case 11:
                    double lnUnitPrce = 0.00;
                    
                    if (!StringUtil.isNumeric(lsValue))
                        _trans.setMaster("nUnitPrce", lnUnitPrce);
                    else
                        _trans.setMaster("nUnitPrce", Double.parseDouble(lsValue)); 
                    
                    break;
                case 12:
                    //check if user is allowed
                    if (!_nautilus.isUserAuthorized(_approval, 
                            UserLevel.MANAGER + UserLevel.SYSADMIN + UserLevel.OWNER, 
                            AccessLevel.SALES)){
                        txtField.setText(StringUtil.NumberFormat(Double.valueOf(String.valueOf(_trans.getMaster("nSelPrce1"))), "#,##0.00"));
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), System.getProperty("sMessagex"), "Warning", "");
                        System.setProperty("sMessagex", "");
                        return;
                    }
                    
                    double lnSelPrce1 = 0.00;
                    
                    if (!StringUtil.isNumeric(lsValue))
                        _trans.setMaster("nSelPrce1", lnSelPrce1);
                    else
                        _trans.setMaster("nSelPrce1", Double.parseDouble(lsValue)); 
                    
                    break;
                case 107:
                    int lnBegQtyxx = 0;
                    
                    if (!StringUtil.isNumeric(lsValue))
                        _trans.setMaster("nBegQtyxx", lnBegQtyxx);
                    else
                        _trans.setMaster("nBegQtyxx", Integer.valueOf(lsValue)); 
                    
                    break;
                case 104:
                    int lnBinNumbr = 0;
                    
                    if (!StringUtil.isNumeric(lsValue))
                        _trans.setMaster("nBinNumbr", lnBinNumbr);
                    else
                        _trans.setMaster("nBinNumbr", Integer.valueOf(lsValue)); 
                    
                    break;
                case 105:
                    if (!StringUtil.isDate(lsValue, SQLUtil.FORMAT_SHORT_DATE))
                        _trans.setMaster("dAcquired", _nautilus.getServerDate());
                    else
                        _trans.setMaster("dAcquired", SQLUtil.toDate(lsValue, SQLUtil.FORMAT_SHORT_DATE));
                    
                    break;
                case 106:
                    if (!StringUtil.isDate(lsValue, SQLUtil.FORMAT_SHORT_DATE))
                        _trans.setMaster("dBegInvxx", _nautilus.getServerDate());
                    else
                        _trans.setMaster("dBegInvxx", SQLUtil.toDate(lsValue, SQLUtil.FORMAT_SHORT_DATE));
                    
                    break;
                case 103:
                    break;
                default:
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Text field with name " + txtField.getId() + " not registered.", "Warning", "");
            }
            _index = lnIndex;
        } else{ //Got Focus
            switch (lnIndex){
                case 11:
                    txtField.setText(StringUtil.NumberFormat(Double.valueOf(String.valueOf(_trans.getMaster("nUnitPrce"))), "#,##0.00"));
                    break;
                case 12:
                    txtField.setText(StringUtil.NumberFormat(Double.valueOf(String.valueOf(_trans.getMaster("nSelPrce1"))), "#,##0.00"));
                    break;
                case 105:
                    txtField.setText(SQLUtil.dateFormat((Date) _trans.getMaster("dAcquired"), SQLUtil.FORMAT_SHORT_DATE));
                    break;
                case 106:
                    txtField.setText(SQLUtil.dateFormat((Date) _trans.getMaster("dBegInvxx"), SQLUtil.FORMAT_SHORT_DATE));
                    break;
            }
            
            _index = lnIndex;
            txtField.selectAll();
        }
    };    

    @FXML
    private void cmbInvStat_Click(ActionEvent event) {
    }
}