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
import javafx.scene.control.CheckBox;
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
import org.xersys.commander.iface.LOthTrans;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.SQLUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.imbentaryofx.listener.DataCallback;
import org.xersys.imbentaryofx.listener.FormClosingCallback;
import org.xersys.sales.base.JobOrder;

public class JobOrderController implements Initializable, ControlledScreen{
    private XNautilus _nautilus;
    private JobOrder _trans;
    private LMasDetTrans _listener;
    private LOthTrans _oth_listener;
    private FormClosingCallback _close_listener;
    private DataCallback _data_callback;
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    private DetailUpdateCallback _detail_update_callback;
    private DetailUpdateCallback _parts_update_callback;
    
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    private ObservableList<TableModel> _table_data2 = FXCollections.observableArrayList();
    
    private boolean _loaded = false;
    private int _index;
    private int _labor_row;
    private int _parts_row;
    
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
    private Label lblPayable;
    @FXML
    private TableView _table;
    @FXML
    private TableView _table2;
    @FXML
    private TextField txtSeeks02;
    @FXML
    private TextField txtField37;
    @FXML
    private TextField txtField08;
    @FXML
    private TextField txtField38;
    @FXML
    private TextField txtField09;
    @FXML
    private TextField txtField07;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField05;
    @FXML
    private Label lblLabrTotl;
    @FXML
    private Label lblPartTotl;
    @FXML
    private CheckBox chkWarranty;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField24;
    @FXML
    private TextField txtField25;
    @FXML
    private TextField txtField26;
    @FXML
    private Label lblFreight;
    @FXML
    private Label lblTotalDisc;
    @FXML
    private Label lblStartTime;
    @FXML
    private Label lblEndTime;
    @FXML
    private TextField txtSeeks03;
    
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
        initGrid2();
        initFields();
        initListener();        
        
        _trans = new JobOrder(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
        _trans.setSaveToDisk(true);
        _trans.setListener(_listener);
        _trans.setOtherListener(_oth_listener);
        
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
    
    @FXML
    private void chkWarranty_Click(ActionEvent event) {
        _trans.setMaster("cWarranty", chkWarranty.isSelected() ? "1" : "0");
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
                    searchLabor("sLaborCde", lsValue, false);
                    event.consume();
                    return;
                case "txtSeeks02":
                    searchBranchInventory("sBarCodex", lsValue, false);
                    event.consume();
                    return;
                case "txtField03":
                    searchClient("a.sClientNm", lsValue, false);
                    event.consume();
                    return;
                case "txtField37":
                    searchSerial(txtField37, "a.sSerial01", lsValue, false);
                    event.consume();
                    return;
                case "txtField38":
                    searchSerial(txtField38, "a.sSerial02", lsValue, false);
                    event.consume();
                    return;
                case "txtField07":
                    searchMCDealer("sDescript", lsValue, false);
                    event.consume();
                    return;
                case "txtField08":
                    searchMechanic("a.sClientNm", lsValue, false);
                    event.consume();
                    return;
                case "txtField09":
                    searchAdvisor("a.sClientNm", lsValue, false);
                    event.consume();
                    return;
            }
        } else if (event.getCode() == KeyCode.F3){
            switch (lsTxt){
                case "txtSeeks01":
                    searchLabor("sDescript", lsValue, false);
                    event.consume();
                    return;
                case "txtSeeks02":
                    searchBranchInventory("sDescript", lsValue, false);
                    event.consume();
                    return;
                case "txtField03":
                    searchClient("a.sClientNm", lsValue, false);
                    event.consume();
                    return;
                case "txtField37":
                    searchSerial(txtField37, "a.sSerial01", lsValue, false);
                    event.consume();
                    return;
                case "txtField38":
                    searchSerial(txtField38, "a.sSerial02", lsValue, false);
                    event.consume();
                    return;
                case "txtField07":
                    searchMCDealer("sDescript", lsValue, false);
                    event.consume();
                    return;
                case "txtField08":
                    searchMechanic("a.sClientNm", lsValue, false);
                    event.consume();
                    return;
                case "txtField09":
                    searchAdvisor("a.sClientNm", lsValue, false);
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
        initGrid2();
        
        txtSeeks01.setText("");
        txtSeeks02.setText("");
        txtField03.setText("");
        txtField05.setText("");
        txtField06.setText("0");
        txtField07.setText("");
        txtField08.setText("");
        txtField09.setText("");
        txtField11.setText("");
        txtField12.setText("0");
        txtField37.setText("");
        txtField38.setText("");
        txtField24.setText("0.00");
        txtField25.setText("0.00");
        txtField26.setText("0.00");
        
        chkWarranty.setSelected(false);
        
        lblLabrTotl.setText("0.00");
        lblPartTotl.setText("0.00");
        lblTotalDisc.setText("0.00");
        lblFreight.setText("0.00");
        lblPayable.setText("0.00");
        
        lblStartTime.setText("00:00:00");
        lblEndTime.setText("00:00:00");
        
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
        _table_data2.clear();
        
        txtSeeks01.requestFocus();
    }
    
    private void computeSummary(){
        double lnLabrTotl = ((Number) _trans.getMaster("nLabrTotl")).doubleValue();
        double lnLabrDisc = ((Number) _trans.getMaster("nLabrDisc")).doubleValue();
        
        double lnPartTotl = ((Number) _trans.getMaster("nPartTotl")).doubleValue();
        double lnPartDisc = ((Number) _trans.getMaster("nPartDisc")).doubleValue();
        
        double lnTranTotl = ((Number) _trans.getMaster("nTranTotl")).doubleValue();
        
        double lnTotlDisc = 0.00;
        lnTotlDisc += lnLabrTotl * lnLabrDisc / 100;
        lnTotlDisc += lnPartTotl * lnPartDisc / 100;
        
        double lnFreightx = ((Number) _trans.getMaster("nFreightx")).doubleValue();
        
        txtField24.setText(StringUtil.NumberFormat(lnLabrDisc, "##0.00"));
        txtField25.setText(StringUtil.NumberFormat(lnPartDisc, "##0.00"));
        txtField26.setText(StringUtil.NumberFormat(lnFreightx, "#,##0.00"));
        
        lblLabrTotl.setText(StringUtil.NumberFormat(lnLabrTotl, "#,##0.00"));
        lblPartTotl.setText(StringUtil.NumberFormat(lnPartTotl, "#,##0.00"));
        lblTotalDisc.setText(StringUtil.NumberFormat(lnTotlDisc, "#,##0.00"));
        lblFreight.setText(StringUtil.NumberFormat(lnFreightx, "#,##0.00"));
        lblPayable.setText(StringUtil.NumberFormat(lnTranTotl + lnFreightx, "#,##0.00"));
    }
    
    private void loadTransaction(){        
        txtField03.setText((String) _trans.getMaster("xClientNm"));
        txtField05.setText((String) _trans.getMaster("sJobDescr"));
        txtField37.setText((String) _trans.getMaster("xEngineNo"));
        txtField38.setText((String) _trans.getMaster("xFrameNox"));
        txtField07.setText((String) _trans.getMaster("xDealerNm"));
        txtField06.setText(String.valueOf(_trans.getMaster("nKmReadng")));
        txtField11.setText((String) _trans.getMaster("sWrnCpnNo"));
        txtField12.setText((String.valueOf(_trans.getMaster("nWrnCpnNo"))));
        txtField08.setText((String) _trans.getMaster("xMechanic"));
        txtField09.setText((String) _trans.getMaster("xSrvcAdvs"));
        
        if (_trans.getMaster("dStartedx") != null){
            lblStartTime.setText(SQLUtil.dateFormat((Date) _trans.getMaster("dStartedx"), SQLUtil.FORMAT_TIME));
        } else
            lblStartTime.setText("00:00:00");
        
        if (_trans.getMaster("dFinished") != null){
            lblEndTime.setText(SQLUtil.dateFormat((Date) _trans.getMaster("dFinished"), SQLUtil.FORMAT_TIME));
        } else
            lblEndTime.setText("00:00:00");
            
        chkWarranty.setSelected("1".equals((String) _trans.getMaster("cWarranty")));
        
        _index = 1;
        
        computeSummary();
        
        loadParts();
        loadDetail();        
    }
    
    private void loadDetail(){
        int lnCtr;
        int lnRow = _trans.getItemCount();
        
        _table_data2.clear();
        
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
            
            _table_data2.add(new TableModel(String.valueOf(lnCtr + 1), 
                        (String) _trans.getDetail(lnCtr, "sLaborNme"), 
                        StringUtil.NumberFormat(lnUnitPrce, "#,##0.00"),
                        String.valueOf(lnQuantity),
                        StringUtil.NumberFormat(lnDiscount * 100, "#,##0.00") + "%",
                        StringUtil.NumberFormat(lnAddDiscx, "#,##0.00"),
                        StringUtil.NumberFormat(lnTranTotl, "#,##0.00"),
                        "",
                        "",
                        ""));
        }

        if (!_table_data2.isEmpty()){
            _table2.getSelectionModel().select(lnRow - 1);
            _table2.getFocusModel().focus(lnRow - 1); 
            _labor_row = _table2.getSelectionModel().getSelectedIndex();           
        }
        
        computeSummary();
        
        txtSeeks01.setText("");
        txtSeeks01.requestFocus();
    }
    
    private void loadParts(){
        int lnCtr;
        int lnRow = _trans.getPartsCount();
        
        _table_data.clear();
        
        double lnUnitPrce;
        double lnDiscount;
        double lnAddDiscx;
        double lnTranTotl;
        int lnQuantity;
        int lnIssuedxx;
        
        for(lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++){           
            lnQuantity = Integer.valueOf(String.valueOf(_trans.getParts(lnCtr, "nQuantity")));
            lnIssuedxx = Integer.valueOf(String.valueOf(_trans.getParts(lnCtr, "nIssuedxx")));
            lnUnitPrce = ((Number)_trans.getParts(lnCtr, "nUnitPrce")).doubleValue();
            lnDiscount = ((Number)_trans.getParts(lnCtr, "nDiscount")).doubleValue() / 100;
            lnAddDiscx = ((Number)_trans.getParts(lnCtr, "nAddDiscx")).doubleValue();
            lnTranTotl = (lnQuantity * (lnUnitPrce - (lnUnitPrce * lnDiscount))) - lnAddDiscx;
            
            _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                        (String) _trans.getParts(lnCtr, "sBarCodex"),
                        (String) _trans.getParts(lnCtr, "sDescript"), 
                        StringUtil.NumberFormat(lnUnitPrce, "#,##0.00"),
                        String.valueOf(_trans.getParts(lnCtr, "nQtyOnHnd")),
                        String.valueOf(lnQuantity),
                        String.valueOf(lnIssuedxx),
                        StringUtil.NumberFormat(lnDiscount * 100, "#,##0.00") + "%",
                        StringUtil.NumberFormat(lnAddDiscx, "#,##0.00"),
                        StringUtil.NumberFormat(lnTranTotl, "#,##0.00")));
        }

        if (!_table_data.isEmpty()){
            _table.getSelectionModel().select(lnRow - 1);
            _table.getFocusModel().focus(lnRow - 1); 
            _parts_row = _table.getSelectionModel().getSelectedIndex();           
        }
        
        computeSummary();
        
        txtSeeks02.setText("");
        txtSeeks02.requestFocus();
    }
    
    private void tableLaborClicked(MouseEvent event) { 
        _labor_row = _table2.getSelectionModel().getSelectedIndex();
        
        if (event.getClickCount() >= 2){
            if (_labor_row >= 0){                
                if (((String) _trans.getDetail(_labor_row, "sLaborCde")).isEmpty()) return;
                
                //multiple result, load the quick search to display records
                JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.JOB_ESTIMATE_DETAIL_UPDATE);
                
                JOEstimateDetailController instance = new JOEstimateDetailController();
                
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);
                instance.setCallback(_detail_update_callback);
                
                instance.setDetailRow(_labor_row);
                instance.setLaborCode((String) _trans.getDetail(_labor_row, "sLaborCde"));
                instance.setDescription((String) _trans.getDetail(_labor_row, "sLaborNme"));
                instance.setQtyOrder(Integer.valueOf(String.valueOf( _trans.getDetail(_labor_row, "nQuantity"))));
                instance.setSellingPrice(Double.valueOf(String.valueOf(_trans.getDetail(_labor_row, "nUnitPrce"))));
                instance.setDiscount(Double.valueOf(String.valueOf((double) _trans.getDetail(_labor_row, "nDiscount"))));
                instance.setAdditional(Double.valueOf(String.valueOf((double) _trans.getDetail(_labor_row, "nAddDiscx"))));
                
                _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
            }
        }
    }
    
    private void tablePartsClicked(MouseEvent event) { 
        _parts_row = _table.getSelectionModel().getSelectedIndex();
        
        if (event.getClickCount() >= 2){
            if (_parts_row >= 0){                
                if (((String) _trans.getParts(_parts_row, "sBarCodex")).isEmpty()) return;
                
                //multiple result, load the quick search to display records
                JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.JO_DETAIL_UPDATE);
                
                JODetailController instance = new JODetailController();
                
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);
                instance.setCallback(_parts_update_callback);
                
                instance.setDetailRow(_parts_row);
                instance.setNautilus(_nautilus);
                instance.setPartNumber((String) _trans.getParts(_parts_row, "sBarCodex"));
                instance.setDescription((String) _trans.getParts(_parts_row, "sDescript"));
                instance.setOtherInfo(0);
                instance.setOnHand(Integer.valueOf(String.valueOf( _trans.getParts(_parts_row, "nQtyOnHnd"))));
                instance.setQtyOrder(Integer.valueOf(String.valueOf( _trans.getParts(_parts_row, "nQuantity"))));
                instance.setIssuedQty(Integer.valueOf(String.valueOf( _trans.getParts(_parts_row, "nIssuedxx"))));
                instance.setSellingPrice(Double.valueOf(String.valueOf(_trans.getParts(_parts_row, "nUnitPrce"))));
                instance.setDiscount(Double.valueOf(String.valueOf((double) _trans.getParts(_parts_row, "nDiscount"))));
                instance.setAdditional(Double.valueOf(String.valueOf((double) _trans.getParts(_parts_row, "nAddDiscx"))));
                
                _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
            }
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
        index04.setSortable(false); index04.setResizable(false); index04.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index05.setSortable(false); index05.setResizable(false); index05.setStyle( "-fx-alignment: CENTER");
        index06.setSortable(false); index06.setResizable(false); index06.setStyle( "-fx-alignment: CENTER;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle( "-fx-alignment: CENTER;");
        index08.setSortable(false); index08.setResizable(false); index08.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index09.setSortable(false); index09.setResizable(false); index09.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index10.setSortable(false); index10.setResizable(false); index10.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
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
        
        index07.setText("Issued"); 
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
        _table.setOnMouseClicked(this::tablePartsClicked);
    }
    
    private void initGrid2(){
        TableColumn index01 = new TableColumn("");
        TableColumn index02 = new TableColumn("");
        TableColumn index03 = new TableColumn("");
        TableColumn index04 = new TableColumn("");
        TableColumn index05 = new TableColumn("");
        TableColumn index06 = new TableColumn("");
        TableColumn index07 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false); index03.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index04.setSortable(false); index04.setResizable(false); index04.setStyle( "-fx-alignment: CENTER;");
        index05.setSortable(false); index05.setResizable(false); index05.setStyle( "-fx-alignment: CENTER;");
        index06.setSortable(false); index06.setResizable(false); index06.setStyle( "-fx-alignment: CENTER;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
        _table2.getColumns().clear();        
        
        index01.setText("No."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(30);
        
        index02.setText("Labor Name"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(212);
        
        index03.setText("Unit Price"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(80);
        
        index04.setText("Qty"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(60);
        
        index05.setText("Disc."); 
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(60);
        
        index06.setText("Adtl."); 
        index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06"));
        index06.prefWidthProperty().set(60);
        
        index07.setText("Total"); 
        index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index07"));
        index07.prefWidthProperty().set(85);
        
        _table2.getColumns().add(index01);
        _table2.getColumns().add(index02);
        _table2.getColumns().add(index03);
        _table2.getColumns().add(index04);
        _table2.getColumns().add(index05);
        _table2.getColumns().add(index06);
        _table2.getColumns().add(index07);
        
        _table2.setItems(_table_data2);
        _table2.setOnMouseClicked(this::tableLaborClicked);
    }    
    
    private void searchMechanic(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchMechanic(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans.setMaster("sSrvcAdvs", (String) loJSON.get("sClientID"));       
                        FXUtil.SetNextFocus(txtField08);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchMechanic());
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
    
    private void searchAdvisor(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchAdvisor(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans.setMaster("sSrvcAdvs", (String) loJSON.get("sClientID"));       
                        FXUtil.SetNextFocus(txtField09);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchAdvisor());
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
    
    private void searchMCDealer(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchMCDealer(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans.setMaster("sDealerCd", (String) loJSON.get("sDealerCd"));
                        FXUtil.SetNextFocus(txtField07);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchMCDealer());
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
    
    private void searchSerial(TextField foField, String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchSerial(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans.setMaster("sSerialID", (String) loJSON.get("sSerialID"));       
                        FXUtil.SetNextFocus(foField);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchSerial());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(foField);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                foField.setText("");
                FXUtil.SetNextFocus(foField);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            foField.setText("");
            FXUtil.SetNextFocus(foField);
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
    
    private void searchLabor(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchLabor(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        txtSeeks01.setText((String) loJSON.get("sDescript"));
                        FXUtil.SetNextFocus(txtSeeks01);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchLabor());
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
    
    private void searchBranchInventory(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchParts(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        txtSeeks02.setText((String) loJSON.get("sDescript"));
                        FXUtil.SetNextFocus(txtSeeks02);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchParts());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtSeeks02);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                txtSeeks02.setText("");
                FXUtil.SetNextFocus(txtSeeks02);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtSeeks02.setText("");
            FXUtil.SetNextFocus(txtSeeks02);
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
                clearTransaction();
                break;
            case "btn03": //search
                switch (_index){
                    case 1:
                        searchLabor("sDescript", txtSeeks01.getText().trim(), false);
                        event.consume();
                        return;
                    case 2:
                        searchBranchInventory("sDescript", txtSeeks02.getText().trim(), false);
                        event.consume();
                        return;
                    case 3:
                        searchClient("a.sClientNm", txtField03.getText().trim(), false);
                        event.consume();
                        return;
                    case 37:
                        searchSerial(txtField37, "a.sSerial01", txtField37.getText().trim(), false);
                        event.consume();
                        return;
                    case 38:
                        searchSerial(txtField38, "a.sSerial02", txtField38.getText().trim(), false);
                        event.consume();
                        return;
                    case 7:
                        searchMCDealer("sDescript", txtField07.getText().trim(), false);
                        event.consume();
                        return;
                    case 8:
                        searchMechanic("a.sClientNm", txtField08.getText().trim(), false);
                        event.consume();
                        return;
                    case 9:
                        searchAdvisor("a.sClientNm", txtField09.getText().trim(), false);
                        event.consume();
                        return;
                }
                break;
            case "btn04": //start
                if (_trans.getItemCount() == 1 && 
                    "".equals((String) _trans.getDetail(0, "sLaborCde"))){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No labor was detected. Unable to start job.", "Notice", "");
                    return;
                }
                
                if (_trans.getMaster("dStartedx") != null)
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Job start time was already set.", "Notice", "");
                else
                    _trans.setMaster("dStartedx", _nautilus.getServerDate());
                
                break;
            case "btn05": //end
                if (_trans.getMaster("dStartedx") == null){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Job was not started. Unable to end job.", "Notice", "");
                    return;
                }
                
                if (_trans.getMaster("dFinished") != null)
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Job end time was already set.", "Notice", "");
                else{
                    _trans.setMaster("dFinished", _nautilus.getServerDate());
                }
                break;
            case "btn06": //save
                if (_trans.SaveTransaction(true)){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction saved successfully", "Success", "");
                    
                    _loaded = false;

                    initGrid();
                    initButton();
                    clearFields();

                    cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  

                   _loaded = true;
                } else 
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                break;
            case "btn07":
                break;
            case "btn08": //catalogue
                searchEstimate(txtSeeks03, "a.sTransNox", "", false);
                break;
            case "btn09": //load job estimate
                loadScreen(ScreenInfo.NAME.JOB_ESTIMATE);
                break;
            case "btn10": //job estimate
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
                loadScreen(ScreenInfo.NAME.JOB_ORDER_HISTORY);
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
        _data_callback = new DataCallback() {
            @Override
            public void Payload(JSONObject foValue) {
                _main_screen_controller.delNoTabScreen(_screens_controller.getScreen(_screens_controller.getCurrentScreenIndex()).getId());
                _trans.setMaster("sClientID", (String) foValue.get("id"));
                
            }
        };
        
        _close_listener = new FormClosingCallback() {
            @Override
            public void FormClosing() {
                _loaded = false;
                
                //reload temp transactions
                _trans.loadTempTransactions();
                
                cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  
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
                    case "nDiscount":
                    case "nAddDiscx":
                    case "nFreightx":
                        computeSummary();
                        break;
                    case "xDealerNm":
                        txtField07.setText((String) foValue);
                        break;
                    case "xSrvcAdvs":
                        txtField09.setText((String) foValue);
                        break;
                    case "xMechanic":
                        txtField08.setText((String) foValue);
                        break;
                    case "sJobDescr":
                        txtField05.setText((String) foValue);
                        break;
                    case "xEngineNo":
                        txtField37.setText((String) foValue);
                        break;
                    case "xFrameNox":
                        txtField38.setText((String) foValue);
                        break;
                    case "xClientNm":
                        txtField03.setText((String) foValue);
                        break;
                    case "dStartedx":
                        if (foValue != null)
                            lblStartTime.setText(SQLUtil.dateFormat((Date) foValue, SQLUtil.FORMAT_TIME));
                        else
                            lblStartTime.setText("00:00:00");
                        break;
                    case "dFinished":
                        if (foValue != null)
                            lblEndTime.setText(SQLUtil.dateFormat((Date) foValue, SQLUtil.FORMAT_TIME));
                        else
                            lblEndTime.setText("00:00:00");
                        break;
                }
            }
            
            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                switch(fnIndex){
                    case 20: //nTranTotl
                    case 24: //nDiscount
                    case 25: //nAddDiscx
                    case 26: //nFreightx
                        computeSummary();
                        break;
                    case 41: //xDealerNm
                        txtField07.setText((String) foValue);
                        break;
                    case 40: //xSrvcAdvs
                        txtField09.setText((String) foValue);
                        break;
                    case 39: //xMechanic
                        txtField08.setText((String) foValue);
                        break;
                    case 5: //sJobDescr
                        txtField05.setText((String) foValue);
                        break;
                    case 37: //xEngineNo
                        txtField37.setText((String) foValue);
                        break;
                    case 38: //xFrameNox
                        txtField38.setText((String) foValue);
                        break;
                    case 36: //xClientNm
                        txtField03.setText((String) foValue);
                        break;
                    case 16: //dStartedx
                        if (foValue != null)
                            lblStartTime.setText(SQLUtil.dateFormat((Date) foValue, SQLUtil.FORMAT_TIME));
                        else
                            lblStartTime.setText("00:00:00");
                        break;
                    case 17: //dFinished
                        if (foValue != null)
                            lblEndTime.setText(SQLUtil.dateFormat((Date) foValue, SQLUtil.FORMAT_TIME));
                        else
                            lblEndTime.setText("00:00:00");
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
        
        _oth_listener = new LOthTrans() {
            @Override
            public void OthersRetreive(int fnRow, String fsFieldNm, Object foValue) {
                loadDetail();
                loadParts();
            }

            @Override
            public void OthersRetreive(int fnRow, int fnIndex, Object foValue) {
                loadDetail();
                loadParts();
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if (!"success".equals((String) foValue.get("result"))) return;
                
                foValue = (JSONObject) foValue.get("payload");
                
                switch (foField.getId()){
                    case "txtSeeks01":
                        _trans.setDetail(_trans.getItemCount()- 1, "sLaborCde", (String) foValue.get("sLaborCde"));
                        loadDetail();
                        break;
                    case "txtSeeks02":
                        _trans.setParts(_trans.getPartsCount()- 1, "sStockIDx", (String) foValue.get("sStockIDx"));
                        loadParts();
                        break;
                    case "txtSeeks03":
                        if (_trans.loadEstimate((String) foValue.get("sTransNox"))){
                            loadTransaction();
                            loadDetail();
                            loadParts();
                        } else{
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                            clearTransaction();
                        }
                        break;
                    case "txtField03":
                        if (foValue == null){
                            createClient();
                        } else
                            _trans.setMaster("sClientID", (String) foValue.get("sClientID"));
                        break;
                    case "txtField07":
                        _trans.setMaster("sDealerCd", (String) foValue.get("sDealerCd"));
                        break;
                    case "txtField08":
                        _trans.setMaster("sMechanic", (String) foValue.get("sClientID"));
                        break;
                    case "txtField09":
                        _trans.setMaster("sSrvcAdvs", (String) foValue.get("sClientID"));
                        break;
                    case "txtField37":
                    case "txtField38":
                        _trans.setMaster("sSerialID", (String) foValue.get("sSerialID"));
                        break;
                }
            }

            @Override
            public void FormClosing(TextField foField) {
                if (foField.getId().equals("txtSeeks01") ||
                    foField.getId().equals("txtSeeks02")){
                    foField.requestFocus();
                } else{
                    FXUtil.SetNextFocus(foField);
                }    
            }
        };
        
        _detail_update_callback = new DetailUpdateCallback() {
            @Override
            public void Result(int fnRow, int fnIndex, Object foValue) {
                switch(fnIndex){
                    case 5:
                    case 8:
                    case 9:
                    case 17:
                        //_trans.setDetail(fnRow, fnIndex, foValue);
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
        
        _parts_update_callback = new DetailUpdateCallback() {
            @Override
            public void Result(int fnRow, int fnIndex, Object foValue) {
                switch(fnIndex){
                    case 4:
                    case 6:
                    case 7:
                        //_trans.setParts(fnRow, fnIndex, foValue);
                        break;
                }
                loadParts();
            }

            @Override
            public void Result(int fnRow, String fsIndex, Object foValue) {
                switch(fsIndex){
                    case "nQuantity":
                    case "nDiscount":
                    case "nAddDiscx":
                    case "nIssuedxx":
                        _trans.setParts(fnRow, fsIndex, foValue);
                        break;
                }
                loadParts();
            }

            @Override
            public void RemovedItem(int fnRow) {
                _trans.delParts(fnRow);
                loadParts();
                computeSummary();
                txtSeeks02.requestFocus();
            }

            @Override
            public void FormClosing() {
                txtSeeks02.requestFocus();
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
        btn04.setText("Start");
        btn05.setText("End");
        btn06.setText("Save");
        btn07.setText("");
        btn08.setText("Load Est");
        btn09.setText("Estimate");
        btn10.setText("Catalogue");
        btn11.setText("History");
        btn12.setText("Close");
        
        btn01.setVisible(true);
        btn02.setVisible(true);
        btn03.setVisible(true);
        btn04.setVisible(true);
        btn05.setVisible(true);
        btn06.setVisible(true);
        btn07.setVisible(false);
        btn08.setVisible(true);
        btn09.setVisible(true);
        btn10.setVisible(true);
        btn11.setVisible(true);
        btn12.setVisible(true);
        
        int lnEditMode = _trans.getEditMode();
        btn02.setVisible(lnEditMode == EditMode.ADDNEW);
        btn03.setVisible(lnEditMode == EditMode.ADDNEW);
        btn04.setVisible(lnEditMode == EditMode.ADDNEW);
        btn05.setVisible(lnEditMode == EditMode.ADDNEW);
        btn06.setVisible(lnEditMode == EditMode.ADDNEW);
        btn08.setVisible(lnEditMode == EditMode.ADDNEW);
        
        txtSeeks01.setDisable(lnEditMode != EditMode.ADDNEW);
        txtSeeks02.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField03.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField05.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField06.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField07.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField08.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField09.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField11.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField12.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField37.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField38.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField11.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField12.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField24.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField25.setDisable(lnEditMode != EditMode.ADDNEW);
        txtField26.setDisable(lnEditMode != EditMode.ADDNEW);
        chkWarranty.setDisable(lnEditMode != EditMode.ADDNEW);
    }
    
    private void initFields(){
        txtSeeks01.setOnKeyPressed(this::txtField_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField37.setOnKeyPressed(this::txtField_KeyPressed);
        txtField38.setOnKeyPressed(this::txtField_KeyPressed);
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        txtField24.setOnKeyPressed(this::txtField_KeyPressed);
        txtField25.setOnKeyPressed(this::txtField_KeyPressed);
        txtField26.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtSeeks01.focusedProperty().addListener(txtField_Focus);
        txtSeeks02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField37.focusedProperty().addListener(txtField_Focus);
        txtField38.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        txtField24.focusedProperty().addListener(txtField_Focus);
        txtField25.focusedProperty().addListener(txtField_Focus);
        txtField26.focusedProperty().addListener(txtField_Focus);
        
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
    
    private void searchEstimate(TextField foField, String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchEstimate(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        
                        if (_trans.loadEstimate((String) loJSON.get("sTransNox"))){
                            loadTransaction();
                            loadDetail();
                        } else {
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                            clearFields();
                        }
                        FXUtil.SetNextFocus(foField);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchEstimate());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(foField);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                foField.setText("");
                FXUtil.SetNextFocus(foField);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            foField.setText("");
            FXUtil.SetNextFocus(foField);
        }
    }
    
    private void createClient(){
        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.CLIENT_MASTER);
                
        ClientMasterController instance = new ClientMasterController();

        instance.setNautilus(_nautilus);
        instance.setParentController(_main_screen_controller);
        instance.setScreensController(_screens_controller);
        instance.setDataCallback(_data_callback);
        instance.isCustomer(true);

        _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
        _main_screen_controller.addNoTabScreen(_screens_controller.getScreen(_screens_controller.getCurrentScreenIndex()).getId());
    }
    
    private void clearTransaction(){
        if (_trans.DeleteTempTransaction(_trans.TempTransactions().get(cmbOrders.getSelectionModel().getSelectedIndex()))){
            _loaded = false;

            if (!_trans.TempTransactions().isEmpty()){
                createNew(_trans.TempTransactions().get(_trans.TempTransactions().size() - 1).getOrderNo());
                cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);
            } else {
                initButton();
                clearFields();

                cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  
            }

            _loaded = true;
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
                case 1:
                case 2:
                case 3:
                case 7:
                case 8:
                case 9:
                case 37:
                case 38:
                    break;
                case 5: //sJobDescr
                    _trans.setMaster(lnIndex, lsValue);
                    break;
                case 11: //sWrnCpnNo
                    _trans.setMaster(lnIndex, lsValue);
                    break;
                case 6: //nKmReadng    
                case 12: //nWrnCpnNo
                    int y = 0;
                    try {
                        //this must be numeric else it will throw an error
                        y = Integer.parseInt(lsValue);
                    } catch (NumberFormatException e) {
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Invalid numeric value.", "Warning", "");
                        txtField.requestFocus(); 
                        break;
                    }
                    
                    _trans.setMaster(lnIndex, y);
                    break;
                case 24: //nDiscount
                case 25: //nAddDiscx
                case 26: //nFreightx             
                    double x = 0.00;
                    try {
                        //this must be numeric else it will throw an error
                        x = Double.parseDouble(lsValue);
                    } catch (NumberFormatException e) {
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Invalid numeric value.", "Warning", "");
                        txtField.requestFocus(); 
                        break;
                    }
                    
                    _trans.setMaster(lnIndex, x);
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
