package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.commander.contants.EditMode;
import org.xersys.lib.pojo.Temp_Transactions;
import org.xersys.imbentaryofx.listener.DetailUpdateCallback;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.LMasDetTrans;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.SQLUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.imbentaryofx.listener.FormClosingCallback;
import org.xersys.sales.base.SP_Sales;

public class SPSalesController implements Initializable, ControlledScreen{
    private XNautilus _nautilus;
    private SP_Sales _trans;
    private LMasDetTrans _listener;
    private FormClosingCallback _close_listener;
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    private DetailUpdateCallback _detail_update_callback;
    
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    
    private boolean _loaded = false;
    private int _index;
    private int _detail_row;
    
    @FXML
    private AnchorPane AnchorMain;
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
    private TextField txtSeeks01;
    @FXML
    private ComboBox cmbOrders;
    @FXML
    private TextField txtField11;
    @FXML
    private TextField txtField12;
    @FXML
    private TextField txtField13;
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
    private TextField txtField10;
    @FXML
    private Label lblAdvPaym;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField03;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);   
        
        //set function keys active
        AnchorMain.setOnKeyReleased(this::keyReleased);
        
        if (_nautilus  == null) {
            System.err.println("Application driver is not set.");
            System.exit(1);
        }
        
        initGrid();
        initFields();
        initListener();        
        
        _trans = new SP_Sales(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
        _trans.setSaveToDisk(true);
        _trans.setListener(_listener);
        
        clearFields();
        
        if (!_trans.TempTransactions().isEmpty())
            createNew(_trans.TempTransactions().get(0).getOrderNo());
        
        cmbOrders.getSelectionModel().select(0);
        
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
        if (!_trans.NewTransaction(fsOrderNox)){        
            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
            System.exit(1);
        }
        
        clearFields();
        loadTransaction();
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        if (event.getCode() == KeyCode.ENTER){
            
        }
        switch (event.getCode()){
            case ENTER:
                switch (lsTxt){
                    case "txtSeeks01":
                        searchBranchInventory("sBarCodex", lsValue, false);
                        event.consume();
                        return;
                    case "txtField05":
                        searchClient("a.sClientNm", lsValue, false);
                        event.consume();
                        return;
                    case "txtField07":
                        searchSalesman("a.sClientNm", lsValue, false);
                        event.consume();
                        return;
                }
                break;
            case F3:
                switch (lsTxt){
                    case "txtSeeks01":
                        searchBranchInventory("sDescript", lsValue, false);
                        event.consume();
                        return;
                    case "txtField05":
                        searchClient("a.sClientNm", lsValue, false);
                        event.consume();
                        return;
                    case "txtField07":
                        searchSalesman("a.sClientNm", lsValue, false);
                        event.consume();
                        return;
                }
                break;
            case DELETE:
                switch (lsTxt){
                    case "txtSeeks01":
                        _trans.delDetail(_detail_row);
                        loadDetail();
                        computeSummary();
                        txtSeeks01.requestFocus();
                        event.consume();
                        return;
                }
                break;
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
        txtField03.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField10.setText("0.00");
        txtField11.setText("0.00");
        txtField12.setText("0.00");
        txtField13.setText("0.00");
        
        lblTranTotal.setText("0.00");
        lblTotalDisc.setText("0.00");
        lblFreight.setText("0.00");
        lblAdvPaym.setText("0.00");
        lblPayable.setText("0.00");
        
        //load temporary transactions
        ArrayList<Temp_Transactions> laTemp = _trans.TempTransactions();
        ObservableList<String> lsOrderNox = FXCollections.observableArrayList();
        
        if (laTemp.size() > 0){    
            for (int lnCtr = 0; lnCtr <= laTemp.size()-1; lnCtr ++){
                lsOrderNox.add(laTemp.get(lnCtr).getOrderNo() + " (" +SQLUtil.dateFormat(laTemp.get(lnCtr).getDateCreated(), SQLUtil.FORMAT_TIMESTAMP) + ")");
            }
        }
        
        cmbOrders.setItems(lsOrderNox);  
        _table_data.clear();
        
        txtSeeks01.requestFocus();
    }
    
    private void computeSummary(){
        double lnTranTotl = ((Number) _trans.getMaster("nTranTotl")).doubleValue();
        double lnDiscount = ((Number) _trans.getMaster("nDiscount")).doubleValue();
        double lnAddDiscx = ((Number) _trans.getMaster("nAddDiscx")).doubleValue();
        double lnFreightx = ((Number) _trans.getMaster("nFreightx")).doubleValue();
        double lnDeductnx = ((Number) _trans.getMaster("nDeductnx")).doubleValue();
        double lnTotlDisc = (lnTranTotl * (lnDiscount / 100)) + lnAddDiscx;
        
        txtField10.setText(StringUtil.NumberFormat(lnDiscount, "##0.00"));
        txtField11.setText(StringUtil.NumberFormat(lnAddDiscx, "#,##0.00"));
        txtField12.setText(StringUtil.NumberFormat(lnFreightx, "#,##0.00"));
        
        lblTranTotal.setText(StringUtil.NumberFormat(lnTranTotl, "#,##0.00"));
        lblTotalDisc.setText(StringUtil.NumberFormat(lnTotlDisc, "#,##0.00"));
        lblFreight.setText(StringUtil.NumberFormat(lnFreightx, "#,##0.00"));
        lblAdvPaym.setText(StringUtil.NumberFormat(lnDeductnx, "#,##0.00"));
        lblPayable.setText(StringUtil.NumberFormat(lnTranTotl - lnTotlDisc + lnFreightx - lnDeductnx, "#,##0.00"));
    }
    
    private void loadTransaction(){
        txtField03.setText(SQLUtil.dateFormat((Date) _trans.getMaster("dTransact"), SQLUtil.FORMAT_MEDIUM_DATE));
        txtField05.setText((String) _trans.getMaster("xClientNm"));
        txtField06.setText((String) _trans.getMaster("sRemarksx"));
        txtField07.setText((String) _trans.getMaster("xSalesman"));
        
        computeSummary();
        
        loadDetail();
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
            _detail_row = lnRow - 1;           
        }
        
        computeSummary();
        
        txtSeeks01.setText("");
        txtSeeks01.requestFocus();
    }
    
    private void loadDetail(int fnRow){
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
            _table.getSelectionModel().select(fnRow);
            _table.getFocusModel().focus(fnRow); 
            _detail_row = lnRow - 1;           
        }
        
        computeSummary();
        
        txtSeeks01.setText("");
        txtSeeks01.requestFocus();
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
        txtSeeks01.requestFocus();
        
        if (event.getClickCount() >= 2){
            if (_detail_row >= 0){
                //multiple result, load the quick search to display records
                JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.POS_DETAIL_UPDATE);
                
                POSDetailController instance = new POSDetailController();
                
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);
                instance.setCallback(_detail_update_callback);
                
                instance.setDetailRow(_detail_row);
                instance.setPartNumber((String) _trans.getDetail(_detail_row, "sBarCodex"));
                instance.setDescription((String) _trans.getDetail(_detail_row, "sDescript"));
                instance.setOtherInfo(1);
                instance.setOnHand(Integer.valueOf(String.valueOf( _trans.getDetail(_detail_row, "nQtyOnHnd"))));
                instance.setQtyOrder(Integer.valueOf(String.valueOf( _trans.getDetail(_detail_row, "nQuantity"))));
                instance.setSellingPrice(Double.valueOf(String.valueOf(_trans.getDetail(_detail_row, "nUnitPrce"))));
                instance.setDiscount(Double.valueOf(String.valueOf((double) _trans.getDetail(_detail_row, "nDiscount"))));
                instance.setAdditional(Double.valueOf(String.valueOf((double) _trans.getDetail(_detail_row, "nAddDiscx"))));
                
                _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
            }
        }
    }
    
    private void searchBranchInventory(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchBranchInventory(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        
                        _trans.setDetail(_trans.getItemCount() - 1, "sStockIDx", (String) loJSON.get("sStockIDx"));
                        loadDetail();
                        FXUtil.SetNextFocus(txtSeeks01);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchBranchInventory());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtSeeks01);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();                
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected", "Warning", "");
                txtSeeks01.setText("");
                FXUtil.SetNextFocus(txtSeeks01);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtSeeks01.setText("");
            FXUtil.SetNextFocus(txtSeeks01);
        }
    }
    
    private void doEvent(String fsValue){
        switch (fsValue.toLowerCase()){
            case "btn01": //new
            case "f1":
                _loaded = false;
                
                createNew("");
                initButton();
                clearFields();
                loadTransaction();
                
                cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  
               
                _loaded = true;
                break;
            case "btn02": //clear
            case "f2":
                if (_trans.DeleteTempTransaction(_trans.TempTransactions().get(cmbOrders.getSelectionModel().getSelectedIndex()))){
                    _loaded = false;
                    
                    if (!_trans.TempTransactions().isEmpty()){
                        createNew(_trans.TempTransactions().get(_trans.TempTransactions().size() - 1).getOrderNo());
                        cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);
                    } else {
                        initGrid();
                        initButton();
                        clearFields();

                        cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  
                    }
                        
                    _loaded = true;
                }
                break;
            case "btn03": //search
            case "f3":
                switch (_index){
                    case 1:
                        searchBranchInventory("sDescript", txtSeeks01.getText().trim(), false);
                        return;
                    case 7:
                        searchSalesman("a.sClientNm", txtField07.getText().trim(), false);
                }
                break;
            case "btn04": //pay
            case "f4":
                if (_trans.SaveTransaction(true)){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction saved successfully.", "Success", "");
                    
                    if (ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Proceed to payment?", "Confirm", "")) loadScreen(ScreenInfo.NAME.CASHIERING);
                    
                    _loaded = false;

                    initGrid();
                    initButton();
                    clearFields();

                    cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  

                   _loaded = true;
                } else 
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                break;
            case "btn05":
            case "f5":
                break;
            case "btn06":
            case "f6":
                break;
            case "btn07":
            case "f7":
                break;
            case "btn08":
            case "f8":
                break;
            case "btn09"://parts inquiry
            case "f9":
                break;
            case "btn10"://parts catalogue
            case "f10":
                JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.PARTS_CATALOGUE);

                if (loJSON != null){
                    PartsCatalogueController instance = new PartsCatalogueController();
                    instance.setNautilus(_nautilus);
                    instance.setParentController(_main_screen_controller);
                    instance.setScreensController(_screens_controller);
                    instance.setDashboardScreensController(_screens_dashboard_controller);
                    instance.setFormCloseListener(_close_listener);

                    _screens_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) instance);
                }
                break;
            case "btn11": //history
            case "f11":
                loadScreen(ScreenInfo.NAME.SP_SALES_HISTORY);
                break;
            case "btn12": //close screen
            case "f12":
                if (_screens_controller.getScreenCount() > 1)
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                else{
                    if (ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to exit the application?", "Please confirm", ""))
                        System.exit(0);
                }
                break;
        }
    }
                 
    private void cmdButton_Click(ActionEvent event) {
        doEvent(((Button) event.getSource()).getId());
    }
    
    private void keyReleased(KeyEvent event) {
        doEvent(event.getCode().toString());
    }
    
    private void initListener(){
        _close_listener = new FormClosingCallback() {
            @Override
            public void FormClosing() {
                _loaded = false;
                
                clearFields();
                initButton();
                
                //reload temp transactions
                _trans.loadTempTransactions();
                
                cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  
                createNew(_trans.TempTransactions().get(_trans.TempTransactions().size() - 1).getOrderNo());    
                loadTransaction();

               _loaded = true;

                initButton();
            }
        };
        
        _listener = new LMasDetTrans() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch(fsFieldNm){
                    case "nTranTotl":
                    case "nDiscount":
                    case "nAddDiscx":
                    case "nFreightx":
                        computeSummary();
                        break;
                    case "sClientID":
                        txtField05.setText((String) foValue);
                        break;
                    case "sSalesman":
                        txtField07.setText((String) foValue);
                        break;
                    case "sSourceNo":
                        loadTransaction();
                        loadDetail();
                        break;
                    case "dTransact":
                        txtField03.setText(SQLUtil.dateFormat((Date) foValue, SQLUtil.FORMAT_MEDIUM_DATE)); break;
                }
            }
            
            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                switch(fnIndex){
                    case 8: //nTranTotl
                    case 10: //nDiscount
                    case 11: //nAddDiscx
                    case 12: //nFreightx
                        computeSummary();
                        break;
                    case 5:
                        txtField05.setText((String) foValue);
                        break;
                    case 7:
                        txtField07.setText((String) foValue);
                        break;
                    case 3:
                        txtField03.setText(SQLUtil.dateFormat((Date) foValue, SQLUtil.FORMAT_MEDIUM_DATE)); break;
                }
            }

            @Override
            public void DetailRetreive(int fnRow, String fsFieldNm, Object foValue) {
                loadDetail(fnRow);
            }

            @Override
            public void DetailRetreive(int fnRow, int fnIndex, Object foValue) {
                loadDetail(fnRow);
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if (!"success".equals((String) foValue.get("result"))) return;
                
                foValue = (JSONObject) foValue.get("payload");
                
                switch (foField.getId()){
                    case "txtSeeks01":
                        _trans.setDetail(_trans.getItemCount() - 1, "sStockIDx", (String) foValue.get("sStockIDx"));
                        break;
                    case "txtField05":
                        _trans.setMaster("sClientID", (String) foValue.get("sClientID"));
                        break;
                    case "txtField07":
                        _trans.setMaster("sSalesman", (String) foValue.get("sClientID"));
                        break;
                    case "txtField18":
                        _trans.setMaster("sSourceNo", (String) foValue.get("sTransNox"));
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
                    case 5:
                    case 8:
                    case 9:
                        _trans.setDetail(fnRow, fnIndex, foValue);
                        break;
                }
                loadDetail();
            }
            
            @Override
            public void Result(int fnRow, String fsIndex, Object foValue){
                switch(fsIndex){
                    case "nQuantity":
                    case "nDiscount":
                    case "nAddDiscx":
                        _trans.setDetail(fnRow, fsIndex, foValue);
                        break;
                }
                loadDetail();
            }

            @Override
            public void RemovedItem(int fnRow) {
                _trans.delDetail(fnRow);
                loadDetail();
                computeSummary();
                txtSeeks01.requestFocus();
            }

            @Override
            public void FormClosing() {
                txtSeeks01.requestFocus();
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
        btn09.setText("Inquiry"); //hidden for a while
        btn10.setText("Catalogue");
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
        btn10.setVisible(true);
        btn11.setVisible(true);
        btn12.setVisible(true);
        
        int lnEditMode = _trans.getEditMode();
        btn02.setVisible(lnEditMode == EditMode.ADDNEW);
        btn03.setVisible(lnEditMode == EditMode.ADDNEW);
        btn04.setVisible(lnEditMode == EditMode.ADDNEW);
        
        txtSeeks01.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField03.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField05.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField06.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField07.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField10.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField11.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField12.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField13.setDisable(lnEditMode != EditMode.ADDNEW);
    }
    
    private void initFields(){
        txtSeeks01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        txtField13.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtSeeks01.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        
        cmbOrders.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (_loaded) 
                    if (cmbOrders.getItems().size() > 0)
                        createNew(_trans.TempTransactions().get(cmbOrders.getSelectionModel().getSelectedIndex()).getOrderNo());
            }
        });
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
    
    private void searchSalesman(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchSalesman(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans.setMaster("sSalesman", (String) loJSON.get("sClientID"));
                        FXUtil.SetNextFocus(txtField07);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchSalesman());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtField07);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                txtField07.setText("");
                FXUtil.SetNextFocus(txtField07);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtField07.setText("");
            FXUtil.SetNextFocus(txtField07);
        }
    }
    
    private void searchClient(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchClient(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans.setMaster("sClientID", (String) loJSON.get("sClientID"));
                        FXUtil.SetNextFocus(txtField05);
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
                            instance.setTextField(txtField05);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                txtField05.setText("");
                FXUtil.SetNextFocus(txtField05);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtField05.setText("");
            FXUtil.SetNextFocus(txtField05);
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
                case 3:
                    if (StringUtil.isDate(lsValue, SQLUtil.FORMAT_SHORT_DATE)){
                        _trans.setMaster("dTransact", SQLUtil.toDate(lsValue, SQLUtil.FORMAT_SHORT_DATE));
                    } else {
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Please encode a date with this format " + SQLUtil.FORMAT_SHORT_DATE + ".", "Warning", "");
                        txtField.requestFocus();
                    }
                    break;
                case 1:
                case 5:
                case 7:
                case 18:
                    break;
                case 6: //remarks
                    _trans.setMaster("sRemarksx", lsValue);
                    break;
                case 10: //discount rate
                case 11: //additional discount
                case 12: //freight charge                    
                    double x = 0.00;
                    try {
                        //this mus be numeric else it will throw an error
                        x = Double.parseDouble(lsValue);
                    } catch (NumberFormatException e) {
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Input was not numeric.", "Warning", "");
                        txtField.requestFocus(); 
                        break;
                    }
                    
                    switch (lnIndex) {
                        case 10:
                            _trans.setMaster("nDiscount", x);
                            break;
                        case 11:
                            _trans.setMaster("nAddDiscx", x);
                            break;
                        default:
                            _trans.setMaster("nFreightx", x);
                            break;
                    }
                    break;
                default:
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Text field with name " + txtField.getId() + " not registered.", "Warning", "");
            }
            _index = lnIndex;
        } else{ //Got Focus
            switch (lnIndex){
                case 3:
                    txtField.setText(SQLUtil.dateFormat((Date) _trans.getMaster("dTransact"), SQLUtil.FORMAT_SHORT_DATE));
                    break;
            }
            
            _index = lnIndex;
            txtField.selectAll();
        }
        
    };    
}
