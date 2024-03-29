package org.xersys.imbentaryofx.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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
import javafx.scene.layout.VBox;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.xersys.imbentaryofx.listener.DataCallback;
import org.xersys.imbentaryofx.listener.FormClosingCallback;
import org.xersys.purchasing.base.PurchaseOrder;

public class PurchaseOrderController implements Initializable, ControlledScreen{
    private XNautilus _nautilus;
    private PurchaseOrder _trans;
    private LMasDetTrans _listener;
    private FormClosingCallback _close_listener;
    private DataCallback _data_callback;
    
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
    private TextField txtSeeks01;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField07;
    @FXML
    private TextField txtField10;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField08;
    @FXML
    private ComboBox cmbOrders;
    @FXML
    private Label lblPayable;
    @FXML
    private TableView _table;
    
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
        
        _trans = new PurchaseOrder(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
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
            switch (lsTxt){
                case "txtSeeks01":
                    searchBranchInventory("sBarCodex", lsValue, false);
                    event.consume();
                    return;
                case "txtField06":
                    searchSupplier("a.sClientNm", lsValue, false);
                    event.consume();
                    return;
                case "txtField08":
                    searchTerm("sDescript", lsValue, false);
                    event.consume();
                    return;
            }
        } else if (event.getCode() == KeyCode.F3){
            switch (lsTxt){
                case "txtSeeks01":
                    searchBranchInventory("sDescript", lsValue, false);
                    event.consume();
                    return;
                case "txtField06":
                    searchSupplier("a.sClientNm", lsValue, false);
                    event.consume();
                    return;
                case "txtField08":
                    searchTerm("sDescript", lsValue, false);
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
        initGrid();
        
        txtSeeks01.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField10.setText("");

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
        
        if (txtField05.isDisable())
            txtField06.requestFocus();
        else
            txtField05.requestFocus();
    }
    
    private void computeSummary(){
        double lnTranTotl = ((Number) _trans.getMaster("nTranTotl")).doubleValue();
        
        lblPayable.setText(StringUtil.NumberFormat(lnTranTotl, "#,##0.00"));
    }
    
    private void loadTransaction(){
        txtField05.setText((String) _trans.getMaster("xDestinat"));
        txtField06.setText((String) _trans.getMaster("sClientNm"));
        txtField07.setText((String) _trans.getMaster("sReferNox"));
        txtField08.setText((String) _trans.getMaster("sTermName"));
        txtField10.setText((String) _trans.getMaster("sRemarksx"));
        
        computeSummary();
        
        loadDetail();
    }
    
    private void loadDetail(){
        int lnCtr;
        int lnRow = _trans.getItemCount();
        
        _table_data.clear();
        
        double lnUnitPrce;
        double lnTranTotl;
        int lnQuantity;
        
        for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){           
            lnQuantity = Integer.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nQuantity")));
            lnUnitPrce = ((Number)_trans.getDetail(lnCtr, "nUnitPrce")).doubleValue();
            lnTranTotl = lnQuantity * lnUnitPrce;
            
            _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                        (String) _trans.getDetail(lnCtr, "sBarCodex"),
                        (String) _trans.getDetail(lnCtr, "sDescript"), 
                        String.valueOf(_trans.getDetail(lnCtr, "nQtyOnHnd")),
                        StringUtil.NumberFormat(lnUnitPrce, "#,##0.00"),
                        String.valueOf(_trans.getDetail(lnCtr, "nRecOrder")),
                        String.valueOf(lnQuantity),
                        StringUtil.NumberFormat(lnTranTotl, "#,##0.00"),
                        "",
                        ""));
        }

        if (!_table_data.isEmpty()){
            _table.getSelectionModel().select(_detail_row);
            _table.getFocusModel().focus(_detail_row); 
            _detail_row = _table.getSelectionModel().getSelectedIndex();           
        }
        
        computeSummary();
        
        txtSeeks01.setText("");
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
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false); index04.setStyle( "-fx-alignment: CENTER;");
        index05.setSortable(false); index05.setResizable(false); index05.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index06.setSortable(false); index06.setResizable(false); index06.setStyle( "-fx-alignment: CENTER;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle( "-fx-alignment: CENTER;");
        index08.setSortable(false); index08.setResizable(false); index08.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
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
        
        index04.setText("QOH"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(60);
        
        index05.setText("Unit Price"); 
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(80);
        
        index06.setText("ROQ"); 
        index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06"));
        index06.prefWidthProperty().set(60);
        
        index07.setText("Order"); 
        index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index07"));
        index07.prefWidthProperty().set(60);
        
        index08.setText("Total"); 
        index08.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index08"));
        index08.prefWidthProperty().set(85);
        
        _table.getColumns().add(index01);
        _table.getColumns().add(index02);
        _table.getColumns().add(index03);
        _table.getColumns().add(index04);
        _table.getColumns().add(index05);
        _table.getColumns().add(index06);
        _table.getColumns().add(index07);
        _table.getColumns().add(index08);
        
        _table.setItems(_table_data);
        _table.setOnMouseClicked(this::tableClicked);
    }
    
    private void tableClicked(MouseEvent event) { 
        _detail_row = _table.getSelectionModel().getSelectedIndex();
        
        if (event.getClickCount() >= 2){
            if (_detail_row >= 0){
                //multiple result, load the quick search to display records
                JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.PO_DETAIL_UPDATE);
                
                PODetailController instance = new PODetailController();
                
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);
                instance.setCallback(_detail_update_callback);
                
                instance.setDetailRow(_detail_row);
                instance.setPartNumber((String) _trans.getDetail(_detail_row, "sBarCodex"));
                instance.setDescription((String) _trans.getDetail(_detail_row, "sDescript"));
                instance.setOtherInfo(Integer.parseInt(String.valueOf(_trans.getDetail(_detail_row, "nRecOrder"))));
                instance.setOnHand(Integer.parseInt(String.valueOf(_trans.getDetail(_detail_row, "nQtyOnHnd"))));
                instance.setQtyOrder(Integer.parseInt(String.valueOf(_trans.getDetail(_detail_row, "nQuantity"))));
                instance.setSellingPrice((double) _trans.getDetail(_detail_row, "nUnitPrce"));
                instance.setHistory(_trans.getHistory((String) _trans.getDetail(_detail_row, "sStockIDx")));
                
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
                        txtSeeks01.requestFocus();
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
    
    private void searchSupplier(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchSupplier(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans.setMaster("sSupplier", (String) loJSON.get("sClientID"));
                        FXUtil.SetNextFocus(txtField06);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchSupplier());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtField06);
                            instance.setAddRecord(true);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                txtField06.setText("");
                FXUtil.SetNextFocus(txtField06);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtField06.setText("");
            FXUtil.SetNextFocus(txtField06);
        }
    }
    
    private void searchTerm(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchTerm(fsKey, foValue, fbExact);
        
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
            case "btn01": //new
                _loaded = false;
                
                createNew("");
                initButton();
                clearFields();
                loadTransaction();
                
               cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  
               
               _loaded = true;
                break;    
            case "btn02": //clear
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
                switch (_index){
                    case 1:
                        searchBranchInventory("sDescript", txtSeeks01.getText().trim(), false);
                        break;
                    case 6:
                        searchSupplier("a.sClientNm", txtField06.getText().trim(), false);
                        event.consume();
                        break;
                    case 8:
                        searchTerm("sDescript", txtField08.getText().trim(), false);
                        break;
                }
                break;
            case "btn04": //pay
                if (_trans.SaveTransaction(true)){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction saved successfully.", "Success", "");
                    
                    _loaded = false;

                    initGrid();
                    initButton();
                    clearFields();

                    cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  

                   _loaded = true;
                } else 
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                break;
            case "btn05": //import from file
                if (importFromFile()){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "File contents successfully imported.", "Success", "");
                }
                break;
            case "btn06":
                break;
            case "btn07":
                break;
            case "btn08":
                break;
            case "btn09": //PO Receiving
                loadScreen(ScreenInfo.NAME.PO_RECEIVING);
                break;
            case "btn10": //PO Return
                loadScreen(ScreenInfo.NAME.PO_RETURN);
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
    
    private boolean importFromFile(){
        if (!String.valueOf(_trans.getDetail(0, "sStockIDx")).isEmpty()){
            ShowMessageFX.Warning(_main_screen_controller.getStage(),
                                    "Importing is only allowed on empty detail transactions.\n" +
                                    "Please create a separate transaction and retry importing.",
                                    "Warning", "");
            return false;
        }
        
        try {
            File file = new File(System.getProperty("app.path.export") + System.getProperty("app.po.import"));   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            
            XSSFWorkbook wb = new XSSFWorkbook(fis);   
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
            Iterator<Row> itr = sheet.iterator(); 
            
            if (_trans.NewTransaction()){
                int lnRow = 0;                
                while (itr.hasNext()){  
                    Row row = itr.next();  
                    Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  

                    if (lnRow > 0){
                        int lnCtr = 0;
                        int lnQuantity = 0;
                        double lnUnitPrce = 0.00;
                        String lsBarCodex = "";

                        while (cellIterator.hasNext()){  
                            Cell cell = cellIterator.next();  

                            switch(lnCtr){
                                case 0:
                                    try {
                                        lsBarCodex = cell.getStringCellValue().trim();
                                    } catch (Exception e) {
                                        lsBarCodex = String.valueOf(cell.getNumericCellValue());
                                    }
                                    break;
                                case 8:
                                    lnUnitPrce = (double) cell.getNumericCellValue();
                                    break;
                                case 9:
                                    lnQuantity = (int) cell.getNumericCellValue();
                                    break;
                            }
                            lnCtr ++;
                        }
                        
                        ResultSet loRS = _nautilus.executeQuery("SELECT sStockIDx FROM Inventory WHERE sBarCodex = " + SQLUtil.toSQL(lsBarCodex));

                        if (loRS.next()){
                            _trans.setDetail(_trans.getItemCount() - 1, "sStockIDx", loRS.getString("sStockIDx"));
                            _trans.setDetail(_trans.getItemCount() - 1, "nQuantity", lnQuantity);
                            
                            if (System.getProperty("app.po.import.cost").equals("1")){
                                _trans.setDetail(_trans.getItemCount() - 1, "nUnitPrce", lnUnitPrce);
                            }
                        }
                    }
                    lnRow ++;
                }
            }
            
            wb.close();
        } catch (IOException | SQLException e) {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), e.getMessage(), "Warning", "");
            e.printStackTrace();
            return false;
        }
        
        return true;
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
    
    private void initListener(){
        _data_callback = new DataCallback() {
            @Override
            public void Payload(JSONObject foValue) {
                _main_screen_controller.delNoTabScreen(_screens_controller.getScreen(_screens_controller.getCurrentScreenIndex()).getId());
                _trans.setMaster("sSupplier", (String) foValue.get("id"));
                
            }
        };
        
        _close_listener = new FormClosingCallback() {
            @Override
            public void FormClosing() {
                _loaded = false;
                
                createNew(_trans.TempTransactions().get(_trans.TempTransactions().size() - 1).getOrderNo());
                initButton();
                clearFields();
                loadTransaction();
                
               cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  
               
               _loaded = true;

                initButton();
            }
        };
        
        _listener = new LMasDetTrans() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch(fsFieldNm){
                    case "nTranTotl":
                        computeSummary();
                        break;
                    case "sSupplier":
                        txtField06.setText((String) foValue);
                        break;
                    case "sTermCode":
                        txtField08.setText((String) foValue);
                        break;
                }
            }
            
            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                switch(fnIndex){
                    case 9: //nTranTotl                    
                        computeSummary();
                        break;
                    case 6: //sSupplier
                        txtField06.setText((String) foValue);
                        break;
                    case 8: //sTermCode
                        txtField08.setText((String) foValue);
                        break;
                }
            }

            @Override
            public void DetailRetreive(int fnRow, String fsFieldNm, Object foValue) {
                loadDetail();
            }

            @Override
            public void DetailRetreive(int fnRow, int fnIndex, Object foValue) {
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
                        loadDetail();
                        txtSeeks01.requestFocus();
                        break;
                    case "txtField06":
                        if (foValue == null){
                            createClient();
                        } else
                            _trans.setMaster("sSupplier", (String) foValue.get("sClientID"));
                        break;
                    case "txtField08":
                        _trans.setMaster("sTermCode", (String) foValue.get("sTermCode"));
                        break;
                }
            }

            @Override
            public void FormClosing(TextField foField) {
                switch (foField.getId()){
                case "txtField06":    
                    foField.setText((String) _trans.getMaster("sClientNm")); break;
                case "txtField08":
                    foField.setText((String) _trans.getMaster("sTermName")); break;
                }
                foField.requestFocus();
            }
        };
        
        _detail_update_callback = new DetailUpdateCallback() {
            @Override
            public void Result(int fnRow, int fnIndex, Object foValue) {
                switch(fnIndex){
                    case 4:
                    case 5:
                        _trans.setDetail(fnRow, fnIndex, foValue);
                        break;
                }
                loadDetail();
            }
            
            @Override
            public void Result(int fnRow, String fsIndex, Object foValue){
                switch(fsIndex){
                    case "nQuantity":
                    case "nUnitPrce":
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
        btn05.setText("Import");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("Receiving");
        btn10.setText("Return");
        btn11.setText("History");
        btn12.setText("Close");              
        
        btn01.setVisible(true);
        btn02.setVisible(true);
        btn03.setVisible(true);
        btn04.setVisible(true);
        btn05.setVisible(true);
        btn06.setVisible(false);
        btn07.setVisible(false);
        btn08.setVisible(false);
        btn09.setVisible(true);
        btn10.setVisible(true);
        btn11.setVisible(true);
        btn12.setVisible(true);
        
        int lnEditMode = _trans.getEditMode();
        btn02.setVisible(lnEditMode == EditMode.ADDNEW);
        btn03.setVisible(lnEditMode == EditMode.ADDNEW);
        btn04.setVisible(lnEditMode == EditMode.ADDNEW);
        btn05.setVisible(lnEditMode == EditMode.ADDNEW);
        
        txtSeeks01.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField05.setDisable(true);
        txtField06.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField07.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField08.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField10.setDisable(lnEditMode != EditMode.ADDNEW);
    }
    
    private void initFields(){
        txtSeeks01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtSeeks01.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        
        cmbOrders.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (_loaded) createNew(_trans.TempTransactions().get(cmbOrders.getSelectionModel().getSelectedIndex()).getOrderNo());
            }
        });
    }
    
    private void createClient(){
        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.CLIENT_MASTER);
                
        ClientMasterController instance = new ClientMasterController();

        instance.setNautilus(_nautilus);
        instance.setParentController(_main_screen_controller);
        instance.setScreensController(_screens_controller);
        instance.setDataCallback(_data_callback);
        instance.isSupplier(true);

        _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
        _main_screen_controller.addNoTabScreen(_screens_controller.getScreen(_screens_controller.getCurrentScreenIndex()).getId());
    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        if(!nv){ //Lost Focus           
            switch (lnIndex){
                case 1:
                case 6:
                case 8:
                    break;
                case 7: //po number
                    _trans.setMaster("sReferNox", lsValue);
                    break;
                case 10: //remarks
                    _trans.setMaster("sRemarksx", lsValue);
                    break;
                default:
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Text field with name " + txtField.getId() + " not registered.", "Warning", "");
            }
            _index = lnIndex;
        } else{ //Got Focus
            _index = lnIndex;
            txtField.selectAll();
        }
    };    
}