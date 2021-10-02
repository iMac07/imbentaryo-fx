package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.sql.SQLException;
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
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.MsgBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.commander.util.StringUtil;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.iface.LRecordMas;
import org.xersys.imbentaryofx.gui.handler.ScreenInfo;
import org.xersys.inventory.base.Inventory;

public class SPMasterController implements Initializable, ControlledScreen{
    private ObservableList<String> _inv_status = FXCollections.observableArrayList("Inactive", "Active", "Limited Inv", "Push Product", "Stop Production");
    
    private XNautilus _nautilus;
    private LRecordMas _listener;
    
    private Inventory _trans;
    
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
                
        if (event.getCode() == KeyCode.ENTER){
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
    }
    
    private void loadTransaction(){
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
        } catch (NumberFormatException | SQLException ex) {
            MsgBox.showOk(ex.getMessage(), "Warning");
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
                    System.err.println(_trans.getMessage());
                    MsgBox.showOk(_trans.getMessage(), "Warning");
                    System.exit(1);
                }

                initButton();
                clearFields();
                loadTransaction();
                
               _loaded = true;
                break;
            case "btn02": //clear
                _trans = new Inventory(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
                _trans.setListener(_listener);

                clearFields();
                initButton();
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
    
    private void initListener(){
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
                            if (_trans.OpenRecord((String) foValue.get("sStockIDx"))){
                                loadTransaction();
                            } else {
                                MsgBox.showOk(_trans.getMessage(), "Warning");
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
        
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        txtField06.setDisable(true);
        txtField07.setDisable(!lbShow);
        txtField08.setDisable(!lbShow);
        txtField09.setDisable(true);
        txtField10.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
        
        disableInvMasterFields(true);
        
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
    
    private void disableInvMasterFields(boolean fbDisable){
        txtField103.setDisable(fbDisable);
        txtField104.setDisable(fbDisable);
        txtField105.setDisable(fbDisable);
        txtField106.setDisable(fbDisable);
        txtField107.setDisable(fbDisable);
        txtField108.setDisable(fbDisable);
        txtField109.setDisable(fbDisable);
        txtField110.setDisable(fbDisable);
        txtField111.setDisable(fbDisable);
        txtField112.setDisable(fbDisable);
        txtField113.setDisable(fbDisable);
        txtField114.setDisable(fbDisable);
        txtField115.setDisable(fbDisable);
        txtField116.setDisable(fbDisable);
        
        chkActive1.setDisable(fbDisable);
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
            MsgBox.showOk((String) loJSON.get("message"), "Warning");
            txtField10.setText("");
            FXUtil.SetNextFocus(txtField10);
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
            MsgBox.showOk((String) loJSON.get("message"), "Warning");
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
            MsgBox.showOk((String) loJSON.get("message"), "Warning");
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
                            loadTransaction();
                        } else {
                            MsgBox.showOk(_trans.getMessage(), "Warning");
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
                MsgBox.showOk("ParseException detected.", "Warning");
                txtSeeks01.setText("");
                FXUtil.SetNextFocus(txtSeeks01);
            }
        } else {
            MsgBox.showOk((String) loJSON.get("message"), "Warning");
            txtSeeks01.setText("");
            FXUtil.SetNextFocus(txtSeeks01);
        }
    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
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
                    double lnSelPrce1 = 0.00;
                    
                    if (!StringUtil.isNumeric(lsValue))
                        _trans.setMaster("nSelPrce1", lnSelPrce1);
                    else
                        _trans.setMaster("nSelPrce1", Double.parseDouble(lsValue)); 
                    
                    break;
                default:
                    MsgBox.showOk("Text field with name " + txtField.getId() + " not registered.", "Warning");
            }
            _index = lnIndex;
        } else{ //Got Focus
            try{
                switch (lnIndex){
                    case 11:
                        txtField.setText(StringUtil.NumberFormat((double) _trans.getMaster("nUnitPrce"), "#,##0.00"));
                        break;
                    case 12:
                        txtField.setText(StringUtil.NumberFormat((double) _trans.getMaster("nSelPrce1"), "#,##0.00"));
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

    @FXML
    private void cmbInvStat_Click(ActionEvent event) {
    }
}