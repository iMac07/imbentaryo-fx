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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import org.xersys.clients.base.APClient;
import org.xersys.commander.util.StringUtil;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.iface.LRecordMas;

public class APClientController implements Initializable, ControlledScreen{
    private XNautilus _nautilus;
    private APClient _trans;
    private LRecordMas _listener;    
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    
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
    private TextField txtField13;
    @FXML
    private TextField txtField19;
    @FXML
    private TextField txtField20;
    @FXML
    private TextField txtField21;
    @FXML
    private TextField txtField22;
    @FXML
    private TextField txtField18;
    @FXML
    private TextField txtField23;
    @FXML
    private TextField txtField08;
    @FXML
    private TextField txtField09;
    @FXML
    private TableView _table;
    
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
        
        initGrid();
        initFields();
        initListener();
        
        _trans = new APClient(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
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
            
            if (_trans.getEditMode() == EditMode.READY)
                loadLedger();
            else
                _table.setVisible(false);
            
        }
        
        initButton();
    }
    
    private void loadLedger(){
        int lnCtr;
        int lnRow = _trans.getItemCount();
        
        _table_data.clear();
        
        double lnDebitxxx;
        double lnCreditxx;
        
        for(lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++){           
            lnDebitxxx = ((Number)_trans.getDetail(lnCtr, "nDebitxxx")).doubleValue();
            lnCreditxx = ((Number)_trans.getDetail(lnCtr, "nCreditxx")).doubleValue();
            
            _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                        String.valueOf(_trans.getDetail(lnCtr, "dTransact")),
                        (String) _trans.getDetail(lnCtr, "xDescript"), 
                        (String) _trans.getDetail(lnCtr, "sTransNox"), 
                        "0".equals((String) _trans.getDetail(lnCtr, "cReversex")) ? "+" : "-",
                        StringUtil.NumberFormat(lnDebitxxx, "#,##0.00"),
                        StringUtil.NumberFormat(lnCreditxx, "#,##0.00"),
                        "",
                        "",
                        ""));
        }

        if (!_table_data.isEmpty()){
            _table.getSelectionModel().select(lnRow - 1);
            _table.getFocusModel().focus(lnRow - 1);        
        }
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        if (event.getCode() == KeyCode.ENTER){
            switch (lsTxt){
                case "txtField08":
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
        txtField13.setText("");
        txtField14.setText("");        
        txtField18.setText("");                
        txtField19.setText("");
        txtField20.setText("");
        txtField21.setText("");
        txtField22.setText("");
        txtField23.setText("");
    }
    
    private void loadTransaction(){
        txtField03.setText((String) _trans.getMaster(3));
        txtField04.setText((String) _trans.getMaster(4));
        txtField05.setText((String) _trans.getMaster(5));
        txtField06.setText((String) _trans.getMaster(6));
        txtField07.setText((String) _trans.getMaster(7));
        txtField08.setText((String) _trans.getMaster(24));
        txtField09.setText(StringUtil.NumberFormat((Number) _trans.getMaster(9), "##0.00"));
        txtField10.setText(StringUtil.NumberFormat((Number) _trans.getMaster(10), "#,##0.00"));
        txtField13.setText(StringUtil.NumberFormat((Number) _trans.getMaster(13), "#,##0.00"));
        txtField14.setText(StringUtil.NumberFormat((Number) _trans.getMaster(14), "#,##0.00"));
        txtField18.setText((String) _trans.getMaster(18));
        txtField19.setText((String) _trans.getMaster(19));
        txtField20.setText((String) _trans.getMaster(20));
        txtField21.setText((String) _trans.getMaster(21));
        txtField22.setText((String) _trans.getMaster(22));
        txtField23.setText((String) _trans.getMaster(23));
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
                        FXUtil.SetNextFocus(txtField08);
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
                            instance.setTextField(txtField08);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                txtField08.setText("");
                FXUtil.SetNextFocus(txtField08);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtField08.setText("");
            FXUtil.SetNextFocus(txtField08);
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
                        txtField08.setText((String) foValue);
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
                    case 9:
                        txtField09.setText(StringUtil.NumberFormat((Number) foValue, "##0.00")); break;
                    case 10:
                        txtField10.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00")); break;
                    case 13:
                        txtField13.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00")); break;
                    case 14:
                        txtField14.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00")); break;
                }
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if ("success".equals((String) foValue.get("result"))){
                    foValue = (JSONObject) foValue.get("payload");
                
                    switch (foField.getId()){
                        case "txtField08":
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
        txtField13.setDisable(true);
        txtField14.setDisable(true);        
        txtField18.setDisable(true);                
        txtField19.setDisable(true);
        txtField20.setDisable(true);
        txtField21.setDisable(true);
        txtField22.setDisable(true);
        txtField23.setDisable(true);
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
        
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("");
        TableColumn index02 = new TableColumn("");
        TableColumn index03 = new TableColumn("");
        TableColumn index04 = new TableColumn("");
        TableColumn index05 = new TableColumn("");
        TableColumn index06 = new TableColumn("");
        TableColumn index07 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false); index02.setStyle( "-fx-alignment: CENTER;");
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false); 
        index05.setSortable(false); index05.setResizable(false); index05.setStyle( "-fx-alignment: CENTER;");
        index06.setSortable(false); index06.setResizable(false); index06.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
        _table.getColumns().clear();        
        
        index01.setText("No."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(30);
        
        index02.setText("Date"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(130);
        
        index03.setText("Particular"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(200);
        
        index04.setText("Source No."); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(150);
        
        index05.setText("+/-"); 
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(30);
        
        index06.setText("Debit"); 
        index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06"));
        index06.prefWidthProperty().set(150);
        
        index07.setText("Credit"); 
        index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index07"));
        index07.prefWidthProperty().set(150);
        
        _table.getColumns().add(index01);
        _table.getColumns().add(index02);
        _table.getColumns().add(index03);
        _table.getColumns().add(index04);
        _table.getColumns().add(index05);
        _table.getColumns().add(index06);
        _table.getColumns().add(index07);
        
        _table.setItems(_table_data);
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
                    _trans.setMaster(lnIndex, lsValue);
                    break;
                case 8:
                    break;
                case 9:
                    if (!StringUtil.isNumeric(lsValue))
                        _trans.setMaster(lnIndex, 0.00);
                    else {
                        if (Double.valueOf(lsValue) > 100)
                            _trans.setMaster(lnIndex, 100.00);
                        else
                            _trans.setMaster(lnIndex, Double.valueOf(lsValue));
                    }
                    break;
                case 10:
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
                case 10:
                    txtField10.setText(StringUtil.NumberFormat((Number) _trans.getMaster(10), "###0.00"));
                    break;
            }
            txtField.selectAll();
        }
        _index = lnIndex;
    };    
}