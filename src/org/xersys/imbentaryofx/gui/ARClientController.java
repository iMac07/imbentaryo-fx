package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import org.xersys.clients.base.ARClient;
import org.xersys.commander.util.StringUtil;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.iface.LRecordMas;

public class ARClientController implements Initializable, ControlledScreen{
    private XNautilus _nautilus;
    private ARClient _trans;
    private LRecordMas _listener;    
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    
    private int _index;
    private boolean _loaded = false;
    private String _clientid = "";
    
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
    private TextField txtField14;
    @FXML
    private TextField txtField21;
    @FXML
    private TextField txtField22;
    @FXML
    private TextField txtField23;
    @FXML
    private TextField txtField08;
    @FXML
    private TextField txtField09;
    @FXML
    private TextField txtField24;
    @FXML
    private TextField txtField25;
    @FXML
    private TextField txtField26;
    @FXML
    private TextField txtField11;
    @FXML
    private TextField txtField15;
    
    public void setClientID(String fsValue){
        _clientid = fsValue;
    }
    
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
        
        _trans = new ARClient(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
        _trans.setListener(_listener);
        
        openRecord();
        
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
    
    private void openRecord(){
        clearFields();        
        
        if (_trans.OpenRecord(_clientid)) {
            loadTransaction();
            initButton();
        } else
            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        if (event.getCode() == KeyCode.ENTER){
            switch (lsTxt){
                case "txtField09":
                    searchTerm("sTermCode", lsValue, "", "", false);
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
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField09.setText("");
        txtField10.setText("");
        txtField11.setText("");       
        txtField14.setText("");        
        txtField15.setText("");
        txtField21.setText("");
        txtField22.setText("");
        txtField23.setText("");
        txtField24.setText("");
        txtField25.setText("");
        txtField26.setText("");
    }
    
    private void loadTransaction(){
        txtField03.setText((String) _trans.getMaster(3));
        txtField04.setText((String) _trans.getMaster(4));
        txtField05.setText((String) _trans.getMaster(5));
        txtField06.setText((String) _trans.getMaster(6));
        txtField07.setText((String) _trans.getMaster(7));
        txtField08.setText((String) _trans.getMaster(8));
        txtField09.setText((String) _trans.getMaster(27));
        
        txtField10.setText(StringUtil.NumberFormat((Number) _trans.getMaster(10), "##0.00"));
        txtField11.setText(StringUtil.NumberFormat((Number) _trans.getMaster(11), "#,##0.00"));
        txtField14.setText(StringUtil.NumberFormat((Number) _trans.getMaster(14), "#,##0.00"));
        txtField15.setText(StringUtil.NumberFormat((Number) _trans.getMaster(15), "#,##0.00"));
        
        txtField21.setText((String) _trans.getMaster(21));
        txtField22.setText((String) _trans.getMaster(22));
        txtField23.setText((String) _trans.getMaster(23));
        txtField24.setText((String) _trans.getMaster(24));
        txtField25.setText((String) _trans.getMaster(25));
        txtField26.setText((String) _trans.getMaster(26));
    }    
    
    private void searchTerm(String fsKey, Object foValue, String fsFilter, String fsValue, boolean fbExact){
        JSONObject loJSON = _trans.searchTerm(fsKey, foValue, fsFilter, fsValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans.setMaster("sTermCode", (String) loJSON.get("sTermCode"));
                        FXUtil.SetNextFocus(txtField09);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchTerm());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtField09);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                txtField09.setText("");
                FXUtil.SetNextFocus(txtField09);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtField09.setText("");
            FXUtil.SetNextFocus(txtField09);
        }
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //update
                if (!_trans.UpdateRecord())
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                else
                    initButton();
                break;
            case "btn02": //cancel update
                openRecord();
                break;
            case "btn03": //search
                if (_index == 8){
                    searchTerm("sTermCode", txtField08.getText(), "", "", false);
                }
                break;
            case "btn04": //save
                if (!_trans.SaveRecord())
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                else {
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Update was saved successfully.", "Success", "");
                    openRecord();
                }
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
            case "btn11":
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
            public void MasterRetreive(String fsIndex, Object foValue) {
                switch (fsIndex){
                    case "xTermName":
                        txtField09.setText((String) foValue);
                        break;
                }
            }

            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                switch (fnIndex){
                    case 3:
                        txtField03.setText((String) foValue); break;
                    case 4:
                        txtField04.setText((String) foValue); break;
                    case 5:
                        txtField05.setText((String) foValue); break;
                    case 6:
                        txtField06.setText((String) foValue); break;
                    case 7:
                        txtField07.setText((String) foValue); break;
                    case 8:
                        txtField08.setText((String) foValue); break;
                    case 10:
                        txtField10.setText(StringUtil.NumberFormat((Number) foValue, "##0.00")); break;
                    case 11:
                        txtField11.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00")); break;
                    case 14:
                        txtField14.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00")); break;
                    case 15:
                        txtField15.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00")); break;
                }
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if ("success".equals((String) foValue.get("result"))){
                    foValue = (JSONObject) foValue.get("payload");
                
                    switch (foField.getId()){
                        case "txtField09":
                            _trans.setMaster("sTermCode", (String) foValue.get("sTermCode"));
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
        
        btn01.setText("Update");
        btn02.setText("Cnl Upd");
        btn03.setText("Search");
        btn04.setText("Save");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("");
        btn11.setText("");
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
        btn11.setVisible(false);
        btn12.setVisible(true);
        
        int lnEditMode = _trans.getEditMode();
        boolean lbShow = lnEditMode == EditMode.ADDNEW || lnEditMode == EditMode.UPDATE;
        
        btn01.setVisible(!lbShow);
        btn02.setVisible(lbShow);
        btn03.setVisible(lbShow);
        btn04.setVisible(lbShow);
        
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        txtField06.setDisable(!lbShow);
        txtField07.setDisable(!lbShow);
        txtField08.setDisable(!lbShow);
        txtField09.setDisable(!lbShow);
        txtField10.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        
        txtField14.setDisable(true);
        txtField15.setDisable(true);        
        txtField21.setDisable(true);                
        txtField22.setDisable(true);
        txtField23.setDisable(true);
        txtField24.setDisable(true);
        txtField25.setDisable(true);
        txtField26.setDisable(true);
    }
    
    private void initFields(){               
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if(!nv){ //Lost Focus          
            switch (lnIndex){
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    _trans.setMaster(lnIndex, lsValue);                
                    break;
                case 9:
                    break;
                case 10:
                    if (!StringUtil.isNumeric(lsValue))
                        _trans.setMaster(lnIndex, 0.00);
                    else {
                        if (Double.valueOf(lsValue) > 100)
                            _trans.setMaster(lnIndex, 100.00);
                        else
                            _trans.setMaster(lnIndex, Double.valueOf(lsValue));
                    }
                    break;
                case 11:
                    if (!StringUtil.isNumeric(lsValue))
                        _trans.setMaster(lnIndex, 0.00);
                    else
                        _trans.setMaster(lnIndex, Double.valueOf(lsValue));
                    break;
                default:
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Text field with name " + txtField.getId() + " not registered.", "Warning", "");
            }
        } else{ //Got Focus        
            switch (lnIndex){
                case 11:
                    txtField10.setText(StringUtil.NumberFormat((Number) _trans.getMaster(10), "###0.00"));
                    break;
            }
            txtField.selectAll();
        }
        _index = lnIndex;
    };    
}