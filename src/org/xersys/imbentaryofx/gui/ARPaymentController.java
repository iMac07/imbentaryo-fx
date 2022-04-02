package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.accounts.APPayment;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.iface.LMasDetTrans;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;

public class ARPaymentController implements Initializable, ControlledScreen {
    private XNautilus _nautilus;
    private APPayment _trans;
    private LMasDetTrans _listener;    
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    
    private int _index;
    private int _active_row;
    private boolean _loaded = false;
    private boolean _record_loaded = false;
    
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
    private TextField txtField05;
    @FXML
    private Label lblPayable;
    @FXML
    private TableView _table;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField13;
    @FXML
    private TextField txtField14;
    @FXML
    private TextField txtAppliedx;
    @FXML
    private CheckBox chkApplyAll;

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
        
        clearFields();
        initFields();
        initListener();
        
        _trans = new APPayment(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
        _trans.setListener(_listener);
        
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
        btn02.setVisible(lnEditMode == EditMode.ADDNEW);
        btn03.setVisible(lnEditMode == EditMode.ADDNEW);
        btn04.setVisible(lnEditMode == EditMode.ADDNEW);
        
        txtField03.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField05.setDisable(lnEditMode != EditMode.ADDNEW);
        txtAppliedx.setDisable(lnEditMode != EditMode.ADDNEW);
        chkApplyAll.setDisable(lnEditMode != EditMode.ADDNEW);
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //new
                if (_trans.getEditMode() == EditMode.ADDNEW) return;
                
                _loaded = false;
                
                if (_trans.NewTransaction()){
                    initButton();
                    clearFields();
                    loadTransaction();
                } else 
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");

               _loaded = true;
                break;    
            case "btn02": //clear
                _trans = new APPayment(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
                _trans.setListener(_listener);
                
                initGrid();
                initButton();
                clearFields();
                break;
            case "btn03": //search
                break;
            case "btn04": //save
                if (_trans.SaveTransaction()){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction saved successfully.", "Success", "");
                    
                    _loaded = false;

                    initGrid();
                    initButton();
                    clearFields();

                   _loaded = true;
                } else 
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                break;
            case "btn05":
            case "btn06":
            case "btn07":
            case "btn08":
            case "btn09":
            case "btn10":
                break;
            case "btn11": //History
                loadScreen(ScreenInfo.NAME.PURCHASE_ORDER_HISTORY);
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
    
    private void loadTransaction(){
        txtField03.setText((String) _trans.getMaster("xClientNm"));
        txtField05.setText((String) _trans.getMaster("sRemarksx"));
        txtField13.setText(StringUtil.NumberFormat(Double.valueOf(String.valueOf(_trans.getMaster("xCredLimt"))), "#,##0.00"));
        txtField14.setText(StringUtil.NumberFormat(Double.valueOf(String.valueOf(_trans.getMaster("xABalance"))), "#,##0.00"));
        txtAppliedx.setText(StringUtil.NumberFormat(Double.valueOf(String.valueOf(_trans.getMaster("nTranTotl"))), "#,##0.00"));
        
        if (_trans.LoadTransaction()) loadDetail();
        
        _record_loaded = true;
    }
    
    private void loadDetail(){
        int lnCtr;
        int lnRow = _trans.getItemCount();
        
        _table_data.clear();
        
        double lnDebitxxx;
        double lnCreditxx;
        double lnAppliedx;
        
        for(lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++){           
            lnDebitxxx = ((Number)_trans.getDetail(lnCtr, "nDebitAmt")).doubleValue();
            lnCreditxx = ((Number)_trans.getDetail(lnCtr, "nCredtAmt")).doubleValue();
            lnAppliedx = ((Number)_trans.getDetail(lnCtr, "nAppliedx")).doubleValue();
            
            _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                        String.valueOf(_trans.getDetail(lnCtr, "dTransact")),
                        (String) _trans.getDetail(lnCtr, "xDescript"), 
                        (String) _trans.getDetail(lnCtr, "xReferNox"), 
                        String.valueOf(_trans.getDetail(lnCtr, "dRefernce")),
                        String.valueOf(_trans.getDetail(lnCtr, "dDueDatex")),
                        String.valueOf(CommonUtil.dateDiff((Date)_trans.getDetail(lnCtr, "dRefernce"), (Date) _trans.getDetail(lnCtr, "dDueDatex"))),
                        StringUtil.NumberFormat(lnDebitxxx, "#,##0.00"),
                        StringUtil.NumberFormat(lnCreditxx, "#,##0.00"),
                        StringUtil.NumberFormat(lnAppliedx, "#,##0.00")));
        }

        if (!_table_data.isEmpty()){
            _table.getSelectionModel().select(_active_row);
            _table.getFocusModel().focus(_active_row);        
            
            lnAppliedx = ((Number)_trans.getDetail(_active_row, "nAppliedx")).doubleValue();
            txtAppliedx.setText(String.valueOf(lnAppliedx));
        }
    }
    
    private void clearFields(){
        initGrid();
        
        txtField03.setText("");
        txtField05.setText("");
        txtField13.setText("0.00");
        txtField14.setText("0.00");
        txtAppliedx.setText("0.00");

        lblPayable.setText("0.00");
        
        _table_data.clear();
        _active_row = 0;
        _record_loaded = false;
        
        if (!txtField03.isDisable()){
            txtField03.selectAll();
            txtField03.requestFocus();
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
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("");
        TableColumn index02 = new TableColumn("");
        TableColumn index03 = new TableColumn("");
        TableColumn index04 = new TableColumn("");
        TableColumn index05 = new TableColumn("");
        TableColumn index06 = new TableColumn("");
        TableColumn index07 = new TableColumn("");
        TableColumn index08 = new TableColumn("");
        TableColumn index09 = new TableColumn("");
        TableColumn index10 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false); index02.setStyle( "-fx-alignment: CENTER;");
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false); index04.setStyle( "-fx-alignment: CENTER;");
        index05.setSortable(false); index05.setResizable(false); index05.setStyle( "-fx-alignment: CENTER;");
        index06.setSortable(false); index06.setResizable(false); index06.setStyle( "-fx-alignment: CENTER;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle( "-fx-alignment: CENTER;");
        index08.setSortable(false); index08.setResizable(false); index08.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index09.setSortable(false); index09.setResizable(false); index09.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index10.setSortable(false); index10.setResizable(false); index10.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
        _table.getColumns().clear();        
        
        index01.setText("No."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(30);
        
        index02.setText("Date"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(100);
        
        index03.setText("Source"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(150);
        
        index04.setText("Refer. No."); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(90);
        
        index05.setText("Refer. Date"); 
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(90);
        
        index06.setText("Due Date"); 
        index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06"));
        index06.prefWidthProperty().set(90);
        
        index07.setText("Age"); 
        index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index07"));
        index07.prefWidthProperty().set(40);
        
        index08.setText("Debit"); 
        index08.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index08"));
        index08.prefWidthProperty().set(100);
        
        index09.setText("Credit"); 
        index09.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index09"));
        index09.prefWidthProperty().set(100);
        
        index10.setText("Applied"); 
        index10.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index10"));
        index10.prefWidthProperty().set(100);
        
        _table.getColumns().add(index01);
        _table.getColumns().add(index02);
        _table.getColumns().add(index03);
        _table.getColumns().add(index04);
        _table.getColumns().add(index05);
        _table.getColumns().add(index06);
        _table.getColumns().add(index07);
        _table.getColumns().add(index08);
        _table.getColumns().add(index09);
        _table.getColumns().add(index10);
        
        _table.setItems(_table_data);
    }
    
    private void initListener(){
        _listener = new LMasDetTrans() {
            @Override
            public void MasterRetreive(String fsIndex, Object foValue) {
                switch (fsIndex){
                    case "sClientID":
                    case "xClientNm":
                        txtField03.setText((String) foValue);
                        
                        if (_trans.LoadTransaction()) 
                            loadDetail();
                        else
                            initGrid();
                        
                        break;
                }
            }

            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                switch (fnIndex){
                    case 3:
                    case 12:
                        txtField03.setText((String) foValue); 
                        
                        if (_trans.LoadTransaction()) 
                            loadDetail();
                        else
                            initGrid();
                        
                        break;
                    case 6:
                        lblPayable.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00")); break;
                    case 13:
                        txtField13.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00")); break;
                    case 14:
                        txtField14.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00")); break;
                }
            }

            @Override
            public void DetailRetreive(int fnRow, String fsFieldNm, Object foValue) {
                //if (_record_loaded) loadDetail();
            }

            @Override
            public void DetailRetreive(int fnRow, int fnIndex, Object foValue) {
                //if (_record_loaded) loadDetail();
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if ("success".equals((String) foValue.get("result"))){
                    foValue = (JSONObject) foValue.get("payload");
                
                    switch (foField.getId()){
                        case "txtField03":
                            _trans.setMaster("sClientID", (String) foValue.get("sClientID"));
                            break;
                    }
                }
            }

            @Override
            public void FormClosing(TextField foField) {
                FXUtil.SetNextFocus(foField);
            }
        };
        
        chkApplyAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue ov, Boolean old_val, Boolean new_val) {
                try {
                    _trans.applyAll(!new_val);
                    loadDetail();
                    
                    txtAppliedx.requestFocus();
                    txtAppliedx.selectAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void initFields(){               
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtAppliedx.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        if (event.getCode() == KeyCode.ENTER){
            switch (lsTxt){
                case "txtField03":
                    searchSupplier("a.sClientID", lsValue, false);
                    event.consume();
                    return;
            }
        }
        
        switch (event.getCode()){
        case TAB:
            if (lsTxt.equals("txtAppliedx")) event.consume(); return;
        case ENTER:
        case DOWN:
            if (lsTxt.equals("txtAppliedx")){
                if (StringUtil.isNumeric(lsValue))
                    _trans.setDetail(_active_row, "nAppliedx", Double.valueOf(lsValue));
                else
                    _trans.setDetail(_active_row, lsValue, 0.00);
                
                if (_active_row < _table_data.size()-1)
                    _active_row++;
                else
                    _active_row = 0;
                
                loadDetail();
                txtField.selectAll();
            } else
                FXUtil.SetNextFocus(txtField);
            break;
        case UP:
            FXUtil.SetPreviousFocus(txtField);
        }
    }
    
    private void searchSupplier(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchClient(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans.setMaster("sClientID", (String) loJSON.get("sClientID"));
                        FXUtil.SetNextFocus(txtField03);
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
                            instance.setTextField(txtField03);
                            instance.setAddRecord(true);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                txtField03.setText("");
                FXUtil.SetNextFocus(txtField03);
            }
        } else {            
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtField03.setText("");
            FXUtil.SetNextFocus(txtField03);
        }
    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if(!nv){ //Lost Focus          
            switch (lnIndex){
                case 3:
                    break;
                case 5:
                    _trans.setMaster(lnIndex, lsValue);
                    break;
                default:
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Text field with name " + txtField.getId() + " not registered.", "Warning", "");
            }
        } else{ //Got Focus        
            _index = lnIndex;
            txtField.selectAll();
        }
    };   
}
