package org.xersys.imbentaryofx.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xersys.lib.pojo.Temp_Transactions;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.DetailUpdateCallback;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.LMasDetTrans;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.MsgBox;
import org.xersys.commander.util.SQLUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.imbentaryofx.gui.handler.ScreenInfo;
import org.xersys.sales.base.SP_Sales;
import org.xersys.sales.search.SalesSE;
import org.xersys.sales.search.SalesSF;

public class SPSalesHistoryController implements Initializable, ControlledScreen{
    private ObservableList<String> _status = FXCollections.observableArrayList("Open", "Closed", "Posted", "Cancelled", "Void");
    
    private XNautilus _nautilus;
    private SP_Sales _trans;
    private LMasDetTrans _listener;
    
    private SalesSE _trans_search;
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
        
        _trans_search = new SalesSE(_nautilus);
        
        _trans = new SP_Sales(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
        _trans.setSaveToDisk(true);
        _trans.setListener(_listener);

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
        txtField10.setText("0.00");
        txtField11.setText("0.00");
        txtField12.setText("0.00");
        
        lblTranTotal.setText("0.00");
        lblTotalDisc.setText("0.00");
        lblFreight.setText("0.00");
        lblPayable.setText("0.00");
        
        //load temporary transactions
        ArrayList<Temp_Transactions> laTemp = _trans.TempTransactions();
        ObservableList<String> lsOrderNox = FXCollections.observableArrayList();
        
        if (laTemp.size() > 0){    
            for (int lnCtr = 0; lnCtr <= laTemp.size()-1; lnCtr ++){
                lsOrderNox.add(laTemp.get(lnCtr).getOrderNo() + " (" +SQLUtil.dateFormat(laTemp.get(lnCtr).getDateCreated(), SQLUtil.FORMAT_TIMESTAMP) + ")");
            }
        }
        
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
        lblPaymTotl.setText(StringUtil.NumberFormat(lnPaymTotl, "#,##0.00"));
    }
    
    private void loadTransaction(){
        txtField06.setText((String) _trans.getMaster("sRemarksx"));
        txtField07.setText((String) _trans.getMaster("sSalesman"));
        
        txtField10.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nDiscount"), "##0.00"));
        txtField11.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nAddDiscx"), "#,##0.00"));
        txtField12.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nFreightx"), "#,##0.00"));
        
        computeSummary();
        
        loadDetail();
        setTranStat(String.valueOf(_trans.getMaster("cTranStat")));
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
            lnTranTotl = (lnQuantity * (lnUnitPrce - (lnUnitPrce * lnDiscount))) - lnAddDiscx;
            
            _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                        (String) _trans.getDetail(lnCtr, "sBarCodex"),
                        (String) _trans.getDetail(lnCtr, "sDescript"), 
                        "TODO:",
                        StringUtil.NumberFormat(lnUnitPrce, "#,##0.00"),
                        String.valueOf(_trans.getDetail(lnCtr, "nQtyOnHnd")),
                        String.valueOf(lnQuantity),
                        StringUtil.NumberFormat(lnDiscount * 100, "#,##0.00") + "%",
                        StringUtil.NumberFormat(lnAddDiscx, "#,##0.00"),
                        StringUtil.NumberFormat(lnTranTotl, "#,##0.00")));
        }

        if (!_table_data.isEmpty()){
            _table.getSelectionModel().select(lnRow - 1);
            _table.getFocusModel().focus(lnRow - 1); 
            _detail_row = _table.getSelectionModel().getSelectedIndex();           
        }
        
        computeSummary();
    }
    
    private void initListener(){        
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
                                MsgBox.showOk(_trans.getMessage(), "Warning");
                                clearFields();
                            }
                        } else 
                            MsgBox.showOk((String) foValue.get("message"), "Warning");
                        
                        break;
                }
            }

            @Override
            public void FormClosing(TextField foField) {
                foField.requestFocus();
            }
        };
    }
    
    private void searchTrans(TextField foField, Enum foType, String fsValue, String fsKey, String fsFilter, int fnMax, boolean fbExact){        
        _trans_search.setSearchType(foType);
        _trans_search.setKey(fsKey);
        _trans_search.setFilter(fsFilter);
        _trans_search.setMax(fnMax);
        _trans_search.setExact(fbExact);
        
        JSONObject loJSON = _trans_search.Search(fsValue);

        //error result, return to callee
        if ("error".equals((String) loJSON.get("result"))) {
            System.err.println((String) loJSON.get("message"));

            switch (foField.getId()){
                case "txtSeeks01":
                    clearFields();
                    return;
            }
        }

        JSONArray loArr = (JSONArray) loJSON.get("payload");

        //only one record was retreived, load the data
        if (loArr.size() == 1) {
            loJSON = (JSONObject) loArr.get(0);

            switch (foField.getId()){
                case "txtSeeks01":
                    if (_trans.OpenTransaction((String) loJSON.get("sTransNox"))){
                        loadTransaction();
                        loadDetail();
                        
                        foField.setText((String) loJSON.get("sTransNox"));
                    } else {
                        MsgBox.showOk(_trans.getMessage(), "Warning");
                        clearFields();
                    }
                    
                    FXUtil.SetNextFocus(foField);
                    return;
            }
        }
        
        //multiple result, load the quick search to display records
        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);
        
        if (loScreen != null){
            QuickSearchNeoController instance = new QuickSearchNeoController();
            instance.setNautilus(_nautilus);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            
            instance.setSearchObject(_trans_search);
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
        index04.setSortable(false); index04.setResizable(true);
        index05.setSortable(false); index05.setResizable(true); index05.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index06.setSortable(false); index06.setResizable(true); index06.setStyle( "-fx-alignment: CENTER;");
        index07.setSortable(false); index07.setResizable(true); index07.setStyle( "-fx-alignment: CENTER;");
        index08.setSortable(false); index08.setResizable(true); index08.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index09.setSortable(false); index09.setResizable(true); index09.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index10.setSortable(false); index10.setResizable(true); index10.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
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
        
        index08.setText("Disc."); 
        index08.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index08"));
        index08.prefWidthProperty().set(60);
        
        index09.setText("Adtl."); 
        index09.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index09"));
        index09.prefWidthProperty().set(60);
        
        index10.setText("Total"); 
        index10.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index10"));
        index10.prefWidthProperty().set(85);
        
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
                searchTrans(txtSeeks01, SalesSF.Type.searchSPSales, "", "sTransNox", "a.cTranStat = " + SQLUtil.toSQL(String.valueOf(_transtat)), 50, false);
                break;
            case "btn02":
                break;
            case "btn03":
                break;
            case "btn04":
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
        btn03.setText("");
        btn04.setText("");
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
        btn03.setVisible(false);
        btn04.setVisible(false);
        btn05.setVisible(false);
        btn06.setVisible(false);
        btn07.setVisible(false);
        btn08.setVisible(false);
        btn09.setVisible(false);
        btn10.setVisible(false);
        btn11.setVisible(false);
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
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);        
        
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