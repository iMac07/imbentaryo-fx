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
import org.xersys.commander.util.StringUtil;
import org.xersys.sales.base.WSOrder;
import org.xersys.sales.search.SalesSearch;

public class SPWSOrderIssuanceController implements Initializable, ControlledScreen{    
    private XNautilus _nautilus;
    private WSOrder _trans;
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
    
    private String _old_trans;
    
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
    private TextField txtField06;
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
    private Label lblPaymTotl;
    @FXML
    private TextField txtSeeks01;
    @FXML
    private TextField txtField09;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtSeeks02;
    @FXML
    private Label lblForCredit;
    
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
        
        _trans_search = new SalesSearch(_nautilus, SalesSearch.SearchType.searchCustomerOrder);
        
        _trans = new WSOrder(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
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
                        searchTransaction(txtSeeks01, "a.sTransNox", txtSeeks01.getText().trim(), true);
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
        txtField09.setText("0.00");
        txtField10.setText("0.00");
        txtField11.setText("0.00");
        
        lblTranTotal.setText("0.00");
        lblTotalDisc.setText("0.00");
        lblFreight.setText("0.00");
        lblPayable.setText("0.00");
        lblPaymTotl.setText("0.00");

        _table_data.clear();
        
        _transtat = 10;
        
        _old_trans = "";
    }
    
    private void computeSummary(){
        double lnTranTotl = ((Number) _trans.getMaster("nTranTotl")).doubleValue();
        double lnDiscount = ((Number) _trans.getMaster("nDiscount")).doubleValue();
        double lnAddDiscx = ((Number) _trans.getMaster("nAddDiscx")).doubleValue();
        double lnFreightx = ((Number) _trans.getMaster("nFreightx")).doubleValue();
        double lnTotlDisc = (lnTranTotl * (lnDiscount / 100)) + lnAddDiscx;
        double lnPaymTotl = ((Number) _trans.getMaster("nAmtPaidx")).doubleValue();
        double lnForCredt = ((Number) _trans.getMaster("nForCredt")).doubleValue();
        
        txtField09.setText(StringUtil.NumberFormat(lnDiscount, "##0.00"));
        txtField10.setText(StringUtil.NumberFormat(lnFreightx, "#,##0.00"));
        txtField11.setText(StringUtil.NumberFormat(lnAddDiscx, "#,##0.00"));
        
        lblTranTotal.setText(StringUtil.NumberFormat(lnTranTotl, "#,##0.00"));
        lblTotalDisc.setText(StringUtil.NumberFormat(lnTotlDisc, "#,##0.00"));
        lblFreight.setText(StringUtil.NumberFormat(lnFreightx, "#,##0.00"));        
        lblPayable.setText(StringUtil.NumberFormat(lnTranTotl - lnTotlDisc + lnFreightx, "#,##0.00"));
        
        lblPaymTotl.setText(StringUtil.NumberFormat(lnPaymTotl, "#,##0.00"));
        lblForCredit.setText(StringUtil.NumberFormat(lnForCredt, "#,##0.00"));
    }
    
    private void loadTransaction(){
        initButton();
        
        txtSeeks01.setText((String) _trans.getMaster("sTransNox"));
        txtSeeks02.setText((String) _trans.getMaster("xClientNm"));
        
        txtField05.setText((String) _trans.getMaster("xClientNm"));
        txtField06.setText((String) _trans.getMaster("sRemarksx"));
        
        txtField09.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nDiscount"), "##0.00"));
        txtField10.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nAddDiscx"), "#,##0.00"));
        txtField11.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nFreightx"), "#,##0.00"));
        
        computeSummary();
        
        loadDetail();
        
        _old_trans = (String) _trans.getMaster("sTransNox");
    }
    
    private void searchTransaction(TextField foTextField, String fsField, String fsValue, boolean fbByCode){
        _trans_search.setKey(fsField);
        _trans_search.setValue(fsValue);
        _trans_search.setExact(fbByCode);
        _trans_search.addFilter("Status", _transtat);
        
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
                        FXUtil.SetNextFocus(foTextField);
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
                            instance.setTextField(foTextField);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                foTextField.setText("");
                FXUtil.SetNextFocus(foTextField);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            foTextField.setText("");
            FXUtil.SetNextFocus(foTextField);
        }
    }
    
    private void loadDetail(){
        int lnCtr;
        int lnRow = _trans.getItemCount();
        
        _table_data.clear();
        
        double lnUnitPrce;
        int lnQuantity;
        int lnApproved;
        int lnReleased;
        int lnIssuedxx;
        int lnCancelld;
        
        for(lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++){           
            lnQuantity = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nQuantity")));
            lnApproved = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nApproved")));
            lnReleased = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nReleased")));
            lnIssuedxx = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nIssuedxx")));
            lnCancelld = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nCancelld")));
            lnUnitPrce = ((Number)_trans.getDetail(lnCtr, "nUnitPrce")).doubleValue();
            
            
            _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                        (String) _trans.getDetail(lnCtr, "sBarCodex"),
                        (String) _trans.getDetail(lnCtr, "sDescript"), 
                        StringUtil.NumberFormat(lnUnitPrce, "#,##0.00"),
                        String.valueOf(_trans.getDetail(lnCtr, "nQtyOnHnd")),
                        String.valueOf(lnQuantity),
                        String.valueOf(lnApproved),
                        String.valueOf(lnCancelld),
                        String.valueOf(lnReleased),
                        String.valueOf(lnIssuedxx)));
        }

        if (!_table_data.isEmpty()){
            _table.getSelectionModel().select(lnRow - 1);
            _table.getFocusModel().focus(lnRow - 1); 
            _detail_row = lnRow - 1;           
        }
        
        computeSummary();
        
        txtSeeks01.requestFocus();
    }
    
    private void loadDetail(int fnRow){
        int lnCtr;
        int lnRow = _trans.getItemCount();
        
        _table_data.clear();
        
        double lnUnitPrce;
        int lnQuantity;
        int lnApproved;
        int lnReleased;
        int lnIssuedxx;
        int lnCancelld;
        
        for(lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++){           
            lnQuantity = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nQuantity")));
            lnApproved = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nApproved")));
            lnReleased = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nReleased")));
            lnIssuedxx = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nIssuedxx")));
            lnCancelld = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nCancelld")));
            lnUnitPrce = ((Number)_trans.getDetail(lnCtr, "nUnitPrce")).doubleValue();
            
            
            _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                        (String) _trans.getDetail(lnCtr, "sBarCodex"),
                        (String) _trans.getDetail(lnCtr, "sDescript"), 
                        StringUtil.NumberFormat(lnUnitPrce, "#,##0.00"),
                        String.valueOf(_trans.getDetail(lnCtr, "nQtyOnHnd")),
                        String.valueOf(lnQuantity),
                        String.valueOf(lnCancelld),
                        String.valueOf(lnApproved),
                        String.valueOf(lnReleased),
                        String.valueOf(lnIssuedxx)));
        }

        if (!_table_data.isEmpty()){
            _table.getSelectionModel().select(fnRow);
            _table.getFocusModel().focus(fnRow); 
            _detail_row = lnRow - 1;           
        }
        
        computeSummary();
        
        txtSeeks01.requestFocus();
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
                    case "txtSeeks02":
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
        
        _detail_update_callback = new DetailUpdateCallback() {
            @Override
            public void Result(int fnRow, int fnIndex, Object foValue) {
                switch(fnIndex){
                    case 8:
                        _trans.setDetail(fnRow, "nReleased", foValue);
                        break;
                }
                loadDetail();
            }
            
            @Override
            public void Result(int fnRow, String fsIndex, Object foValue){
                switch(fsIndex){
                    case "nApproved":
                    case "nCancelld":
                    case "nReleased":
                        _trans.setDetail(fnRow, fsIndex, foValue);
                        break;
                }
                loadDetail();
            }

            @Override
            public void RemovedItem(int fnRow) {
            }

            @Override
            public void FormClosing() {
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
        TableColumn index10 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false); index04.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index05.setSortable(false); index05.setResizable(false); index05.setStyle( "-fx-alignment: CENTER");        
        index06.setSortable(false); index06.setResizable(false); index06.setStyle( "-fx-alignment: CENTER;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle( "-fx-alignment: CENTER;");
        index08.setSortable(false); index08.setResizable(false); index08.setStyle( "-fx-alignment: CENTER");
        index09.setSortable(false); index09.setResizable(false); index09.setStyle( "-fx-alignment: CENTER");
        index10.setSortable(false); index10.setResizable(false); index10.setStyle( "-fx-alignment: CENTER");
        
        _table.getColumns().clear();        
        
        index01.setText("No."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(30);
        
        index02.setText("Part Number"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(130);
        
        index03.setText("Description"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(325);
        
        index04.setText("Unit Price"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(80);
        
        index05.setText("QOH"); 
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(50);
        
        index06.setText("Qty"); 
        index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06"));
        index06.prefWidthProperty().set(50);
        
        index07.setText("Apr"); 
        index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index07"));
        index07.prefWidthProperty().set(50);
        
        index08.setText("Can"); 
        index08.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index08"));
        index08.prefWidthProperty().set(50);
        
        index09.setText("Rel"); 
        index09.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index09"));
        index09.prefWidthProperty().set(50);
        
        index10.setText("Iss"); 
        index10.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index10"));
        index10.prefWidthProperty().set(50);
        
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
        _table.setOnMouseClicked(this::tableClicked);
    }
    
    private void tableClicked(MouseEvent event) { 
        if (_trans.getEditMode() != EditMode.UPDATE) return;
        
        _detail_row = _table.getSelectionModel().getSelectedIndex();
        
        if (event.getClickCount() >= 2){
            if (_detail_row >= 0){
                //multiple result, load the quick search to display records
                JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.SP_WHOLESALE_ORDER_ISSUANCE_DETAIL);
                
                SPWSOrderIssuanceDetailController instance = new SPWSOrderIssuanceDetailController();
                
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);
                instance.setCallback(_detail_update_callback);
                
                instance.setDetailRow(_detail_row);
                instance.setPartNumber((String) _trans.getDetail(_detail_row, "sBarCodex"));
                instance.setDescription((String) _trans.getDetail(_detail_row, "sDescript"));
                instance.setOnHand(Integer.valueOf(String.valueOf( _trans.getDetail(_detail_row, "nQtyOnHnd"))));
                instance.setSellingPrice(Double.valueOf(String.valueOf(_trans.getDetail(_detail_row, "nUnitPrce"))));
                instance.setQtyOrder(Integer.valueOf(String.valueOf( _trans.getDetail(_detail_row, "nQuantity"))));
                instance.setApproved(Integer.valueOf(String.valueOf( _trans.getDetail(_detail_row, "nApproved"))));
                instance.setCancelled(Integer.valueOf(String.valueOf( _trans.getDetail(_detail_row, "nCancelld"))));
                instance.setIssued(Integer.valueOf(String.valueOf( _trans.getDetail(_detail_row, "nIssuedxx"))));
                instance.setReleased(Integer.valueOf(String.valueOf( _trans.getDetail(_detail_row, "nReleased"))));
                
                _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
            }
        }
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
            case "btn02": //update
                if (_trans.getEditMode() != EditMode.READY){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No transaction was loaded.", "Warning", "");
                    return;
                }
                
                if (_trans.UpdateTransaction()) 
                    initButton();
                else
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                
                break;
            case "btn03": //save
                if (_trans.SaveTransaction(true)){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction updated successfully.", "Warning", "");
                    
                    if (_trans.OpenTransaction(_old_trans)){
                        loadTransaction();
                        loadDetail();
                    } else {
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                        clearFields();
                    }
                    
//                    _loaded = false;
//                    clearFields();
//                    initButton();
//                    _loaded = true;
                } else
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                
                break;
            case "btn04": //cancel update
                _trans = new WSOrder(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
                _trans.setSaveToDisk(false);
                _trans.setListener(_listener);
                _trans.setApprvListener(_approval);

                clearFields();
                initButton();
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
                if (_trans.getEditMode() != EditMode.READY){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No transaction was loaded.", "Warning", "");
                    return;
                }
                
                ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction printed successfully.", "Success", "");
                break;
            case "btn11": //release
                if (_trans.getEditMode() != EditMode.READY){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No transaction was loaded.", "Warning", "");
                    return;
                }
                
                if (!_trans.isReleasable()){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "This transaction is NON RELEASABLE YET.", "Warning", "");
                    return;
                }
                
                double lnForCredt = ((Number) _trans.getMaster("nForCredt")).doubleValue();
                
                if (lnForCredt > 0.00){
                    if (!ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to use the whole available for credit amount.", "Warning", "")){
                        String lsValue = ShowMessageFX.InputText(_main_screen_controller.getStage(), "Please input the amount to credit.", "Credit Amount", "");

                        if (!StringUtil.isNumeric(lsValue)) return;

                        double lnValue = 0.00;

                        try {
                            lnValue = Double.valueOf(lsValue);
                        } catch (NumberFormatException e) {
                        }

                        if (lnValue > lnForCredt) {
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Amount to credit is greater than the available amount.", "Warning", "");
                            return;
                        }

                        lnForCredt = lnValue;
                    }
                }
                                
                if (_trans.ReleaseOrder(lnForCredt)){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Releasble items are sent to POS transaction list.", "Success", "");
                    
                    initGrid();
                    clearFields();
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
        btn02.setText("Update");
        btn03.setText("Save");
        btn04.setText("Cancel");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("Print");
        btn11.setText("Release");
        btn12.setText("Close");
        
        btn01.setVisible(true);
        btn02.setVisible(true);
        btn03.setVisible(true);
        btn04.setVisible(false);
        btn05.setVisible(false);
        btn06.setVisible(false);
        btn07.setVisible(false);
        btn08.setVisible(false);
        btn09.setVisible(false);
        btn10.setVisible(true);
        btn11.setVisible(true);
        btn12.setVisible(true);
        
        btn02.setVisible((int) _trans.getEditMode() == EditMode.READY || (int) _trans.getEditMode() == EditMode.UNKNOWN);
        btn10.setVisible((int) _trans.getEditMode() == EditMode.READY || (int) _trans.getEditMode() == EditMode.UNKNOWN);
        btn11.setVisible((int) _trans.getEditMode() == EditMode.READY || (int) _trans.getEditMode() == EditMode.UNKNOWN);
        btn03.setVisible((int) _trans.getEditMode() == EditMode.UPDATE);
        btn04.setVisible((int) _trans.getEditMode() == EditMode.UPDATE);
    }
    
    private void initFields(){
        txtSeeks01.setOnKeyPressed(this::txtField_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);        
        
        txtSeeks01.focusedProperty().addListener(txtField_Focus);
        txtSeeks02.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
    
        _transtat = 10;
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