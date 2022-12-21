package org.xersys.imbentaryofx.gui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.iface.LApproval;
import org.xersys.imbentaryofx.listener.DetailUpdateCallback;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.LMasDetTrans;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.SQLUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.sales.base.SP_Sales;
import org.xersys.sales.search.SalesSearch;

public class SPSalesHistoryController implements Initializable, ControlledScreen{
    private ObservableList<String> _status = FXCollections.observableArrayList("Open", "Closed", "Posted", "Cancelled", "Void");
    
    private XNautilus _nautilus;
    private SP_Sales _trans;
    private LMasDetTrans _listener;
    private LApproval _approval;
    
    private SalesSearch _trans_search;
    private int _transtat;
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    private DetailUpdateCallback _detail_update_callback;
    
    private TableModel _table_model;
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    
    private boolean _loaded = false;
    private int _index;
    private int _detail_row;
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private VBox btnbox00;
    @FXML
    private HBox btnbox01;
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
    private TextField txtField10;
    @FXML
    private TextField txtField11;
    @FXML
    private TextField txtField12;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField07;
    @FXML
    private Label lblTranTotal;
    @FXML
    private Label lblTotalDisc;
    @FXML
    private Label lblPayable;
    @FXML
    private TableView _table;
    @FXML
    private Label lblFreight;
    @FXML
    private Label lblTranStat;
    @FXML
    private Label lblPaymTotl;
    @FXML
    private ComboBox cmbStatus;
    @FXML
    private TextField txtSeeks01;
    @FXML
    private TextField txtField05;
    @FXML
    private Label lblReferNox;
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
        initGrid();
        initListener();
        
        _trans_search = new SalesSearch(_nautilus, SalesSearch.SearchType.searchSPSales);
        
        _trans = new SP_Sales(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
        _trans.setSaveToDisk(false);
        _trans.setListener(_listener);
        _trans.setApprvListener(_approval);

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
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
        
        switch (event.getCode()){
            case ENTER:
                switch (lsTxt){
                    case "txtSeeks01":
                        searchTransaction(txtSeeks01, "a.sTransNox", txtSeeks01.getText().trim(), false);
                        event.consume();
                        break;
                    case "txtSeeks02":
                        searchTransaction(txtSeeks02, "IFNULL(b.sClientNm, '')", txtSeeks02.getText().trim(), false);
                        event.consume();
                        break;
                }
            case F3:
                switch (lsTxt){
                    case "txtSeeks01":
                        searchTransaction(txtSeeks01, "a.sTransNox", txtSeeks01.getText().trim(), false);
                        event.consume();
                        break;
                    case "txtSeeks02":
                        searchTransaction(txtSeeks02, "IFNULL(b.sClientNm, '')", txtSeeks02.getText().trim(), false);
                        event.consume();
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
        initGrid();
        
        txtSeeks01.setText("");
        txtSeeks02.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField10.setText("0.00");
        txtField11.setText("0.00");
        txtField12.setText("0.00");
        
        lblTranTotal.setText("0.00");
        lblTotalDisc.setText("0.00");
        lblFreight.setText("0.00");
        lblPayable.setText("0.00");
        
        lblReferNox.setText("N/A");
        lblPaymTotl.setText("0.00");

        _table_data.clear();
        
        cmbStatus.getSelectionModel().select(0);
        _transtat = 0;
        
        setTranStat("-1");
    }
    
    private void computeSummary(){
        double lnTranTotl = ((Number) _trans.getMaster("nTranTotl")).doubleValue();
        double lnDiscount = ((Number) _trans.getMaster("nDiscount")).doubleValue();
        double lnAddDiscx = ((Number) _trans.getMaster("nAddDiscx")).doubleValue();
        double lnFreightx = ((Number) _trans.getMaster("nFreightx")).doubleValue();
        double lnTotlDisc = (lnTranTotl * (lnDiscount / 100)) + lnAddDiscx;
        double lnPaymTotl = ((Number) _trans.getMaster("nAmtPaidx")).doubleValue();
        
        txtField10.setText(StringUtil.NumberFormat(lnDiscount, "##0.00"));
        txtField11.setText(StringUtil.NumberFormat(lnFreightx, "#,##0.00"));
        txtField12.setText(StringUtil.NumberFormat(lnAddDiscx, "#,##0.00"));
        
        lblTranTotal.setText(StringUtil.NumberFormat(lnTranTotl, "#,##0.00"));
        lblTotalDisc.setText(StringUtil.NumberFormat(lnTotlDisc, "#,##0.00"));
        lblFreight.setText(StringUtil.NumberFormat(lnFreightx, "#,##0.00"));
        lblPayable.setText(StringUtil.NumberFormat(lnTranTotl - lnTotlDisc + lnFreightx, "#,##0.00"));
        
        lblReferNox.setText((String) _trans.getMaster("xInvNumbr"));
        lblPaymTotl.setText(StringUtil.NumberFormat(lnPaymTotl, "#,##0.00"));
    }
    
    private void loadTransaction(){
        txtSeeks01.setText((String) _trans.getMaster("sTransNox"));
        txtSeeks02.setText((String) _trans.getMaster("xClientNm"));
        txtField05.setText((String) _trans.getMaster("xClientNm"));
        txtField06.setText((String) _trans.getMaster("sRemarksx"));
        txtField07.setText((String) _trans.getMaster("xSalesman"));
        
        txtField10.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nDiscount"), "##0.00"));
        txtField11.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nAddDiscx"), "#,##0.00"));
        txtField12.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nFreightx"), "#,##0.00"));
        
        computeSummary();
        
        loadDetail();
        setTranStat(String.valueOf(_trans.getMaster("cTranStat")));
        cmbStatus.getSelectionModel().select(Integer.parseInt((String) _trans.getMaster("cTranStat")));
        
        btn02.setVisible(Integer.parseInt((String) _trans.getMaster("cTranStat")) < 2);
        btn03.setVisible(Integer.parseInt((String) _trans.getMaster("cTranStat")) < 2);
        btn04.setVisible(Integer.parseInt((String) _trans.getMaster("cTranStat")) < 2);
        btn11.setVisible(Integer.parseInt((String) _trans.getMaster("cTranStat")) == 2);
    }
    
    private void searchTransaction(TextField foTextField, String fsField, String fsValue, boolean fbByCode){
        _trans_search.setKey(fsField);
        _trans_search.setValue(fsValue);
        _trans_search.setExact(fbByCode);
        _trans_search.addFilter("Status", cmbStatus.getSelectionModel().getSelectedIndex());
        
        JSONObject loJSON =  _trans_search.Search();
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        
                        if (_trans.OpenTransaction((String) loJSON.get("sTransNox"))){
                            loadTransaction();
                            loadDetail();
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

                            instance.setSearchObject(_trans_search);
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
    
    private void loadDetail(){
        int lnCtr;
        int lnRow = _trans.getItemCount();
        
        _table_data.clear();
        
        double lnUnitPrce;
        double lnDiscount;
        double lnAddDiscx;
        double lnTranTotl;
        int lnQuantity;
        
        for(lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++){           
            lnQuantity = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nQuantity")));
            lnUnitPrce = ((Number)_trans.getDetail(lnCtr, "nUnitPrce")).doubleValue();
            lnDiscount = ((Number)_trans.getDetail(lnCtr, "nDiscount")).doubleValue() / 100;
            lnAddDiscx = ((Number)_trans.getDetail(lnCtr, "nAddDiscx")).doubleValue();
            lnTranTotl = (lnQuantity * (lnUnitPrce - (lnUnitPrce * lnDiscount))) - (lnQuantity * lnAddDiscx);
            
            _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                        (String) _trans.getDetail(lnCtr, "sBarCodex"),
                        (String) _trans.getDetail(lnCtr, "sDescript"), 
                        StringUtil.NumberFormat(lnUnitPrce, "#,##0.00"),
                        String.valueOf(_trans.getDetail(lnCtr, "nQtyOnHnd")),
                        String.valueOf(lnQuantity),
                        StringUtil.NumberFormat(lnDiscount * 100, "#,##0.00") + "%",
                        StringUtil.NumberFormat(lnAddDiscx, "#,##0.00"),
                        StringUtil.NumberFormat(lnTranTotl, "#,##0.00"), 
                        ""));
        }

        if (!_table_data.isEmpty()){
            _table.getSelectionModel().select(lnRow - 1);
            _table.getFocusModel().focus(lnRow - 1); 
            _detail_row = _table.getSelectionModel().getSelectedIndex();           
        }
        
        computeSummary();
    }
    
    private void initListener(){      
        _approval = new LApproval() {
            @Override
            public void Request() {
                _main_screen_controller.seekApproval();
            }
            
            @Override
            public void ShowMessage(String fsValue) {
                ShowMessageFX.Okay(_main_screen_controller.getStage(), fsValue, "Notice", "");
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                switch (foField.getId()){
                    case "txtSeeks01":
                        if ("success".equals((String) foValue.get("result"))){
                            foValue = (JSONObject) foValue.get("payload");
                            
                            if (_trans.OpenTransaction((String) foValue.get("sTransNox"))){
                                loadTransaction();
                                loadDetail();
                            } else {
                                ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                                clearFields();
                            }
                        } else 
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) foValue.get("message"), "Warning", "");
                        
                        break;
                }
            }

            @Override
            public void FormClosing(TextField foField) {
                foField.requestFocus();
            }
        };
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
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false); index04.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index05.setSortable(false); index05.setResizable(false); index05.setStyle( "-fx-alignment: CENTER");
        index06.setSortable(false); index06.setResizable(false); index06.setStyle( "-fx-alignment: CENTER;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index08.setSortable(false); index08.setResizable(false); index08.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index09.setSortable(false); index09.setResizable(false); index09.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
        _table.getColumns().clear();        
        
        index01.setText("No."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(30);
        
        index02.setText("Part Number"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(130);
        
        index03.setText("Description"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(200);
        
        index04.setText("Unit Price"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(80);
        
        index05.setText("QOH"); 
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(60);
                
        index06.setText("Order"); 
        index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06"));
        index06.prefWidthProperty().set(60);
        
        index07.setText("Disc."); 
        index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06"));
        index07.prefWidthProperty().set(60);
        
        index08.setText("Adtl."); 
        index08.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index08"));
        index08.prefWidthProperty().set(60);
        
        index09.setText("Total"); 
        index09.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index09"));
        index09.prefWidthProperty().set(85);
        
        _table.getColumns().add(index01);
        _table.getColumns().add(index02);
        _table.getColumns().add(index03);
        _table.getColumns().add(index04);
        _table.getColumns().add(index05);
        _table.getColumns().add(index06);
        _table.getColumns().add(index07);
        _table.getColumns().add(index08);
        _table.getColumns().add(index09);
        
        _table.setItems(_table_data);
        _table.setOnMouseClicked(this::tableClicked);
    }
    
    private void tableClicked(MouseEvent event) { 
        _detail_row = _table.getSelectionModel().getSelectedIndex();
    }
    
    @FXML
    private void cmbStatus_Click(ActionEvent event) {
        _transtat = cmbStatus.getSelectionModel().getSelectedIndex();
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01":
                switch (_index){
                    case 1:
                        searchTransaction(txtSeeks01, "a.sTransNox", txtSeeks01.getText().trim(), false);
                        break;
                    case 2:
                        searchTransaction(txtSeeks02, "IFNULL(b.sClientNm, '')", txtSeeks02.getText().trim(), false);
                        break;
                }
                
                break;
            case "btn02": //pay
                payWithInvoice();
                break;
            case "btn03": //release
                payCharge();
                break;
            case "btn04": //cancel
                if (_trans.CancelTransaction()){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction cancelled successfully.", "Success", "");
                    
                    initGrid();
                    clearFields();
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
            case "btn11": //print invoice
                printInvoice();
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
    
    private void payCharge(){
        if (_trans.getEditMode() == EditMode.READY && "0".equals((String) _trans.getMaster("cTranStat"))){
            JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.PAYMENT_CHARGE);
            PaymentChargeController instance = new PaymentChargeController();

            instance.setNautilus(_nautilus);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            instance.setDashboardScreensController(_screens_dashboard_controller);
            instance.setSourceCd("SO");
            instance.setSourceNo((String) _trans.getMaster("sTransNox"));

            //close this screen
            _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
            //load the payment screen
            _screens_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) instance);
        } else{
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "No transaction was loaded or transaction loaded was already processed.", "Warning", "");
        }
    }
    
    private void payWithInvoice(){
        if (_trans.getEditMode() == EditMode.READY && "0".equals((String) _trans.getMaster("cTranStat"))){
                JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.PAYMENT);
                PaymentController instance = new PaymentController();
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);
                instance.setDashboardScreensController(_screens_dashboard_controller);
                instance.setSourceCd("SO");
                instance.setSourceNo((String) _trans.getMaster("sTransNox"));

                //close this screen
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                //load the payment screen
                _screens_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) instance);
        } else{
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "No transaction was loaded or transaction loaded was already processed.", "Warning", "");
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
        btn02.setText("Pay");
        btn03.setText("Release");
        btn04.setText("Cancel");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("");
        btn11.setText("Print");
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
    }
    
    private void initFields(){
        txtSeeks01.setOnKeyPressed(this::txtField_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);        
        
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        
        cmbStatus.setItems(_status);
        cmbStatus.getSelectionModel().select(0);
        _transtat = 0;
    }
    
    private void setTranStat(String fsValue){
        switch(fsValue){
            case "0":
                lblTranStat.setText("OPEN");
                break;
            case "1":
                lblTranStat.setText("CLOSED");
                break;
            case "2":
                lblTranStat.setText("POSTED");
                break;
            case "3":
                lblTranStat.setText("CANCELLED");
                break;
            case "4":
                lblTranStat.setText("VOIDED");
                break;
            default:
                lblTranStat.setText("UNKNOWN");
        }
    }
    
    private boolean printInvoice(){
        if (_trans.getEditMode() != EditMode.READY){
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "No transaction was loaded or transaction loaded was already processed.", "Warning", "");
            return false;
        }
        
        if ("2".equals((String) _trans.getMaster("cTranStat"))){
            JSONArray json_arr = new JSONArray();
            json_arr.clear();

            JSONObject json_obj;

            for (int lnCtr = 0; lnCtr <= _trans.getItemCount()-1; lnCtr++){
                json_obj = new JSONObject();
                
                json_obj.put("nField01", (int) _trans.getDetail(lnCtr, "nQuantity"));
                json_obj.put("sField01", (String) _trans.getDetail(lnCtr, "sBarCodex"));
                json_obj.put("sField02", (String) _trans.getDetail(lnCtr, "sDescript"));
                json_obj.put("lField01", Double.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nUnitPrce"))));
                json_obj.put("lField02", Double.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nDiscount"))));
                json_obj.put("lField03", Double.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nAddDiscx"))));
                json_arr.add(json_obj); 
            }

            //Create the parameter
            Map<String, Object> params = new HashMap<>();
            params.put("sAddressx", (String) _nautilus.getBranchConfig("sAddressx") + ", " + (String) _nautilus.getBranchConfig("xTownName"));
            params.put("sClientNm", (String) _trans.getMaster("vClientNm"));
            params.put("xAddressx", _trans.getMaster("xAddressx") != null ? (String) _trans.getMaster("xAddressx") : "");
            params.put("sReferNox", (String) _trans.getMaster("xInvNumbr"));
            params.put("nAmtPaidx", Double.valueOf((String.valueOf(_trans.getMaster("xAmtPaidx")))));
            params.put("dTransact", SQLUtil.dateFormat((Date) _trans.getMaster("dTransact"), SQLUtil.FORMAT_MEDIUM_DATE));
            params.put("sSalesman", (String) _trans.getMaster("xSalesman"));
            
            double lnTranTotl = Double.valueOf(String.valueOf(_trans.getMaster("nTranTotl")));
            params.put("nTranTotl", lnTranTotl);
            params.put("nFreightx", Double.valueOf(String.valueOf(_trans.getMaster("nFreightx"))));
            
            double lnDiscount = lnTranTotl * Double.valueOf(String.valueOf(_trans.getMaster("nDiscount"))) / 100;
            
            lnDiscount = lnDiscount + Double.valueOf(String.valueOf(_trans.getMaster("nAddDiscx")));
            params.put("nDiscount", lnDiscount);

            try {
                InputStream stream = new ByteArrayInputStream(json_arr.toJSONString().getBytes("UTF-8"));
                JsonDataSource jrjson = new JsonDataSource(stream); 

                JasperPrint _jrprint = JasperFillManager.fillReport(System.getProperty("sys.default.path.config") +
                                                                    "reports/SP_DR_new.jasper", params, jrjson);
                JasperViewer jv = new JasperViewer(_jrprint, false);
                jv.setVisible(true);
            } catch (JRException | UnsupportedEncodingException  ex) {
                ex.printStackTrace();
                ShowMessageFX.Error(ex.getMessage(), "Exception", "Warning");
                return false;
            }
        } else {    
            ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction was not yet invoiced.", "Notice", "");
            return false;
        }
        
        return true;
    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        if(!nv){ //Lost Focus           
            _index = lnIndex;
        } else{ //Got Focus
            _index = lnIndex;
            txtField.selectAll();
        }
        
    };    
}