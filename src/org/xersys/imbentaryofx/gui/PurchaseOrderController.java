package org.xersys.imbentaryofx.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xersys.bili.bo.PurchaseOrder;
import org.xersys.lib.dto.Temp_Transactions;
import org.xersys.kumander.contants.SearchEnum;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreenInfo;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.DetailUpdateCallback;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.kumander.iface.LMasDetTrans;
import org.xersys.kumander.iface.XNautilus;
import org.xersys.kumander.util.FXUtil;
import org.xersys.kumander.util.MsgBox;
import org.xersys.kumander.util.SQLUtil;
import org.xersys.kumander.util.StringUtil;

public class PurchaseOrderController implements Initializable, ControlledScreen{
    private XNautilus _nautilus;
    private PurchaseOrder _trans;
    private LMasDetTrans _listener;
    
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
    private FontAwesomeIconView glyph01;
    @FXML
    private FontAwesomeIconView glyph02;
    @FXML
    private FontAwesomeIconView glyph03;
    @FXML
    private FontAwesomeIconView glyph04;
    @FXML
    private FontAwesomeIconView glyph05;
    @FXML
    private FontAwesomeIconView glyph06;
    @FXML
    private FontAwesomeIconView glyph07;
    @FXML
    private FontAwesomeIconView glyph08;
    @FXML
    private FontAwesomeIconView glyph09;
    @FXML
    private FontAwesomeIconView glyph10;
    @FXML
    private FontAwesomeIconView glyph11;
    @FXML
    private FontAwesomeIconView glyph12;
    @FXML
    private TextField txtSeeks01;
    @FXML
    private ComboBox cmbOrders;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField07;
    @FXML
    private Label lblPayable;
    @FXML
    private TableView _table;
    @FXML
    private TextField txtField10;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField16;
    @FXML
    private TextField txtField08;
    
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
        
        initButton();
        initFields();
        initListener();
        
        _trans = new PurchaseOrder(_nautilus, (String) _nautilus.getSysConfig("sBranchCd"), false);
        _trans.setSaveToDisk(true);
        _trans.setListener(_listener);
        
        if (_trans.TempTransactions().isEmpty())
            createNew("");
        else
            createNew(_trans.TempTransactions().get(0).getOrderNo());
        
        cmbOrders.getSelectionModel().select(0);
        
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
            System.err.println(_trans.getMessage());
            MsgBox.showOk(_trans.getMessage(), "Warning");
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
                    System.out.println(this.getClass().getSimpleName() + " " + lsTxt + " was used for searching");                    
                    quickSearch(txtField, SearchEnum.Type.searchInvBranchComplex, lsValue, "sBarCodex", "", 15, false);
                    event.consume();
                    return;
                case "txtField05":
                case "txtField06":
                case "txtField08":
                case "txtField16":
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
        txtField06.setText("");
        txtField07.setText("");

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
        
        txtSeeks01.requestFocus();
    }
    
    private void computeSummary(){
        double lnTranTotl = ((Number) _trans.getMaster("nTranTotl")).doubleValue();
        
        lblPayable.setText(StringUtil.NumberFormat(lnTranTotl, "#,##0.00"));
    }
    
    private void loadTransaction(){
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText((String) _trans.getMaster("sReferNox"));
        txtField08.setText("");
        txtField10.setText((String) _trans.getMaster("sRemarksx"));
        txtField16.setText("");
        
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
            lnQuantity = (int) _trans.getDetail(lnCtr, "nQuantity");
            lnUnitPrce = ((Number)_trans.getDetail(lnCtr, "nUnitPrce")).doubleValue();
            lnTranTotl = lnQuantity * lnUnitPrce;
            
            _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                        (String) _trans.getDetail(lnCtr, 100),
                        (String) _trans.getDetail(lnCtr, 101), 
                        (String) _trans.getDetail(lnCtr, 102),
                        StringUtil.NumberFormat(lnUnitPrce, "#,##0.00"),
                        String.valueOf(_trans.getDetail(lnCtr, 103)),
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
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(true);
        index05.setSortable(false); index05.setResizable(true); index05.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index06.setSortable(false); index06.setResizable(true); index06.setStyle( "-fx-alignment: CENTER;");
        index07.setSortable(false); index07.setResizable(true); index07.setStyle( "-fx-alignment: CENTER;");
        index08.setSortable(false); index08.setResizable(true); index08.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
        _table.getColumns().clear();        
        
        index01.setText("No."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(30);
        
        index02.setText("Part Number"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(130);
        
        index03.setText("Description"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(185);
        
        index04.setText("Other Info"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(137);
        
        index05.setText("Unit Price"); 
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(80);
        
        index06.setText("QOH"); 
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
                JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.POS_DETAIL_UPDATE);
                
                POSDetailController instance = new POSDetailController();
                
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);
                instance.setCallback(_detail_update_callback);
                
                instance.setDetailRow(_detail_row);
                instance.setPartNumber((String) _trans.getDetail(_detail_row, 100));
                instance.setDescription((String) _trans.getDetail(_detail_row, 101));
                instance.setOtherInfo((String) _trans.getDetail(_detail_row, 102));
                instance.setOnHand((int) _trans.getDetail(_detail_row, 103));
                instance.setQtyOrder((int) _trans.getDetail(_detail_row, "nQuantity"));
                instance.setSellingPrice((double) _trans.getDetail(_detail_row, "nUnitPrce"));
                instance.setDiscount((double) _trans.getDetail(_detail_row, "nDiscount"));
                instance.setAdditional((double) _trans.getDetail(_detail_row, "nAddDiscx"));
                
                _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
            }
        }
    }
    
    private void quickSearch(TextField foField, SearchEnum.Type foType, String fsValue, String fsKey, String fsFilter, int fnMax, boolean fbExact){        
        //pass the initial value do initial search
        JSONObject loJSON = _trans.Search(foType, fsValue, fsKey, fsFilter, fnMax, fbExact);
        
        //null result, return to callee
        if (loJSON == null) {
            System.err.println("Initial search result was null.");
            return;
        }
        
        //error result, return to callee
        if ("error".equals((String) loJSON.get("result"))) {
            System.err.println((String) loJSON.get("message"));
            return;
        }
        
        JSONArray loArr = (JSONArray) loJSON.get("payload");
        
        //only one record was retreived, load the data
        if (loArr.size() == 1) {
            loJSON = (JSONObject) loArr.get(0);
            _trans.setDetail(_trans.getItemCount() - 1, "sStockIDx", (String) loJSON.get("sStockIDx"));
            loadDetail();
            return;
        }
        
        //multiple result, load the quick search to display records
        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);
        
        if (loScreen != null){
            QuickSearchController instance = new QuickSearchController();
            instance.setNautilus(_nautilus);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            
            instance.setTransObject(_trans);
            instance.setSearchCallback(_search_callback);
            
            instance.setSearchType(foType);
            instance.setSearchValue(fsValue);
            instance.setSearchKey(fsKey);
            instance.setSearchFilter(fsFilter);
            instance.setSearchMaxRow(fnMax);
            instance.setSearchExact(fbExact);
            instance.setSearchResult(loJSON);
            instance.setTextField(foField);
            
            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
        }
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //clear
                if (_trans.DeleteTempTransaction(_trans.TempTransactions().get(cmbOrders.getSelectionModel().getSelectedIndex()))){
                    _loaded = false;
                    
                    if (_trans.TempTransactions().isEmpty()){
                        createNew("");
                        cmbOrders.getSelectionModel().select(0);
                    } else {
                        createNew(_trans.TempTransactions().get(_trans.TempTransactions().size() - 1).getOrderNo());
                         cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);
                    }
                        
                    _loaded = true;
                }
                break;
            case "btn02": //new
                _loaded = false;
                
                createNew("");
                clearFields();
                loadTransaction();
                
               cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  
               
               _loaded = true;
                break;
            case "btn03": //save
                if (_trans.SaveTransaction(true)){
                    MsgBox.showOk("Transaction saved successfully and ready for paying.", "Success");
                    
                    _loaded = false;

                    createNew("");
                    clearFields();
                    loadTransaction();

                   cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  

                   _loaded = true;
                } else 
                    MsgBox.showOk(_trans.getMessage(), "Warning");
                break;
            case "btn04": //search
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
        _listener = new LMasDetTrans() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch(fsFieldNm){
                    case "nTranTtal":
                    case "nDiscount":
                    case "nAddDiscx":
                    case "nFreightx":
                        computeSummary();
                        break;
                }
            }

            @Override
            public void DetailRetreive(int fnRow, String fsFieldNm, Object foValue) {
                loadDetail();
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                switch (foField.getId()){
                    case "txtSeeks01":
                        _trans.setDetail(_trans.getItemCount() - 1, "sStockIDx", (String) foValue.get("sStockIDx"));
                        loadDetail();
                        break;
                }
            }

            @Override
            public void FormClosing() {
                txtSeeks01.requestFocus();
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
        
        btn01.setText("Clear");
        btn02.setText("New");
        btn03.setText("Save");
        btn04.setText("Search");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("Cashier");
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
        
        glyph01.setIcon(FontAwesomeIcon.ANCHOR);
        glyph02.setIcon(FontAwesomeIcon.ANCHOR);
        glyph03.setIcon(FontAwesomeIcon.ANCHOR);
        glyph04.setIcon(FontAwesomeIcon.ANCHOR);
        glyph05.setIcon(FontAwesomeIcon.ANCHOR);
        glyph06.setIcon(FontAwesomeIcon.ANCHOR);
        glyph07.setIcon(FontAwesomeIcon.ANCHOR);
        glyph08.setIcon(FontAwesomeIcon.ANCHOR);
        glyph09.setIcon(FontAwesomeIcon.ANCHOR);
        glyph10.setIcon(FontAwesomeIcon.ANCHOR);
        glyph11.setIcon(FontAwesomeIcon.ANCHOR);
        glyph12.setIcon(FontAwesomeIcon.ANCHOR);
    }
    
    private void initFields(){
        txtSeeks01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);
        txtField16.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField16.focusedProperty().addListener(txtField_Focus);
        
        cmbOrders.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (_loaded) createNew(_trans.TempTransactions().get(cmbOrders.getSelectionModel().getSelectedIndex()).getOrderNo());
            }
        });
    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        if(!nv){ //Lost Focus           
            switch (lnIndex){
                case 6: //po number
                case 10: //remarks
                    _trans.setMaster(lnIndex, lsValue);
                    break;
                case 5:
                case 7:
                case 8:
                case 9:
                case 16:
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
}