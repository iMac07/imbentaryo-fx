package org.xersys.imbentaryofx.gui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.xersys.commander.iface.LOthTrans;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.SQLUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.imbentaryofx.listener.FormClosingCallback;
import org.xersys.sales.base.JobOrder;

public class JobOrderHistoryController implements Initializable, ControlledScreen{
    private ObservableList<String> _status = 
        FXCollections.observableArrayList("All", "New", "Printed", "Payed", "Cancelled", "Released");
    
    private XNautilus _nautilus;
    private JobOrder _trans;
    private LMasDetTrans _listener;
    private LOthTrans _oth_listener;
    private FormClosingCallback _close_listener;
    private LApproval _approval;
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    private DetailUpdateCallback _detail_update_callback;
    private DetailUpdateCallback _parts_update_callback;
    
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    private ObservableList<TableModel> _table_data2 = FXCollections.observableArrayList();
    
    private boolean _loaded = false;
    private int _index = 1;
    private int _parts_row;
    private int _labor_row;
    private int _transtat;
    
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
    private Label lblPayable;
    @FXML
    private TextField txtSeeks01;
    @FXML
    private TextField txtSeeks02;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField07;
    @FXML
    private TextField txtField05;
    @FXML
    private TableView _table;
    @FXML
    private TableView _table2;
    @FXML
    private ComboBox cmbStatus;
    @FXML
    private Label lblTranStat;
    @FXML
    private TextField txtField03;
    @FXML
    private Label lblLabrTotl;
    @FXML
    private Label lblPartTotl;
    @FXML
    private Label lblFreight;
    @FXML
    private Label lblTotalDisc;
    @FXML
    private TextField txtField09;
    @FXML
    private TextField txtField08;
    @FXML
    private TextField txtField11;
    @FXML
    private TextField txtField12;
    @FXML
    private CheckBox chkWarranty;
    @FXML
    private TextField txtField37;
    @FXML
    private TextField txtField38;
    @FXML
    private TextField txtField26;
    @FXML
    private TextField txtField24;
    @FXML
    private TextField txtField25;
    @FXML
    private Label lblStartTime;
    @FXML
    private Label lblEndTime;
    
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
        _trans.setSaveToDisk(false);
        _trans.setListener(_listener);
        _trans.setOtherListener(_oth_listener);
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
        
        if (event.getCode() == KeyCode.ENTER ||
            event.getCode() == KeyCode.F3){
            switch(lsTxt){
                case "txtSeeks01":
                    searchTransaction(txtSeeks01, "a.sTransNox", lsValue, false);
                    break;
                case "txtSeeks02":
                    searchTransaction(txtSeeks02,"IFNULL(b.sClientNm, '')", lsValue, false);
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
        
        _table_data.clear();
        _table_data2.clear();
        
        cmbStatus.getSelectionModel().select(0);
        
        _transtat = 12340;
        _trans.setTranStat(_transtat);
        
        setTranStat("-1");
        
        txtSeeks01.requestFocus();
    }
    
    private void searchTransaction(TextField foField, String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchTransaction(fsKey, foValue, fbExact);
        
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
                        FXUtil.SetNextFocus(foField);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchTransaction());
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
        
        Date lsDate = null;
        
        if (_trans.getMaster("dStartedx") != null){
            lsDate = SQLUtil.toDate(String.valueOf(_trans.getMaster("dStartedx")), SQLUtil.FORMAT_TIMESTAMP);
            lblStartTime.setText(SQLUtil.dateFormat(lsDate, SQLUtil.FORMAT_TIME));
        } else
            lblStartTime.setText("00:00:00");
        
        if (_trans.getMaster("dFinished") != null){
            lsDate = SQLUtil.toDate(String.valueOf(_trans.getMaster("dFinished")), SQLUtil.FORMAT_TIMESTAMP);
            lblEndTime.setText(SQLUtil.dateFormat(lsDate, SQLUtil.FORMAT_TIME));
        } else
            lblEndTime.setText("00:00:00");
        
        chkWarranty.setSelected("1".equals((String) _trans.getMaster("cWarranty")));
        
        setTranStat(String.valueOf(_trans.getMaster("cTranStat")));
        
        btn11.setVisible(Integer.parseInt((String) _trans.getMaster("cTranStat")) == 2);
        
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
    
    private void tableLaborClicked(MouseEvent event) { 
        _labor_row = _table2.getSelectionModel().getSelectedIndex();
    }
    
    private void tablePartsClicked(MouseEvent event) { 
        _parts_row = _table.getSelectionModel().getSelectedIndex();
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //browse
                if (_index == 1)
                    searchTransaction(txtSeeks01, "a.sTransNox", "", false);
                else
                    searchTransaction(txtSeeks02, "a.sTransNox", "", false);
                break;
            case "btn02": //print
                if (_trans.getEditMode() != EditMode.READY){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No transaction was loaded.", "Warning", "");
                    return;
                }
                
                if (printJO()){
                    if ("0".equals((String) _trans.getMaster("cTranStat"))){
                        if (!_trans.CloseTransaction())
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");                        
                    }
                }
                                
                initButton();
                clearFields();
                
                break;
            case "btn03": //cancel
                if (_trans.getEditMode() != EditMode.READY){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No transaction was loaded.", "Warning", "");
                    return;
                }
                
                if (_trans.CancelTransaction()){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction was cancelled successfully.", "Success", "");
                    
                    initGrid();
                    initButton();
                    clearFields();
                } else 
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
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
            case "btn11": //print invoices
                printReceipt();
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
    
    private boolean printReceipt(){
        if (_trans.getEditMode() != EditMode.READY){
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "No transaction was loaded or transaction loaded was already processed.", "Warning", "");
            return false;
        }
        
        if ("2".equals((String) _trans.getMaster("cTranStat"))){
            if (!ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to print Official Receipt?", "Confirm", "")) return false;
            
            try {
                ResultSet loRS = _trans.getReceipt_Info();
                
                if (!loRS.next()) return false;
                
                JSONArray json_arr = new JSONArray();
                json_arr.clear();

                JSONObject json_obj;

                for (int lnCtr = 0; lnCtr <= _trans.getItemCount()-1; lnCtr++){
                    json_obj = new JSONObject();
                    
                    json_obj.put("nField01", (int) _trans.getDetail(lnCtr, "nQuantity"));
                    json_obj.put("sField01", (String) _trans.getDetail(lnCtr, "sLaborNme"));
                    json_obj.put("lField01", Double.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nUnitPrce"))));
                    json_obj.put("lField02", Double.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nDiscount"))));
                    json_obj.put("lField03", Double.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nAddDiscx"))));
                    json_arr.add(json_obj); 
                }

                //Create the parameter
                Map<String, Object> params = new HashMap<>();
                params.put("sAddressx", (String) _nautilus.getBranchConfig("sAddressx") + ", " + (String) _nautilus.getBranchConfig("xTownName"));
                params.put("sClientNm", loRS.getString("sClientNm"));
                params.put("xAddressx", loRS.getString("xAddressx"));
                params.put("sReferNox", loRS.getString("sInvNumbr"));
                params.put("nAmtPaidx", Double.valueOf((String.valueOf(_trans.getMaster("nLabrPaid")))));
                params.put("dTransact", SQLUtil.dateFormat(loRS.getDate("dTransact"), SQLUtil.FORMAT_MEDIUM_DATE));
                params.put("sSalesman", (String) _trans.getMaster("xMechanic"));

                double lnTranTotl = Double.valueOf(String.valueOf(_trans.getMaster("nLabrTotl")));
                params.put("nTranTotl", lnTranTotl);
                                
                double lnDiscount = lnTranTotl * Double.valueOf(String.valueOf(_trans.getMaster("nLabrDisc"))) / 100;
                params.put("nDiscount", lnDiscount);

                InputStream stream = new ByteArrayInputStream(json_arr.toJSONString().getBytes("UTF-8"));
                JsonDataSource jrjson = new JsonDataSource(stream); 

                JasperPrint _jrprint = JasperFillManager.fillReport(System.getProperty("sys.default.path.config") +
                                                                    "reports/JO-OR.jasper", params, jrjson);
                JasperViewer jv = new JasperViewer(_jrprint, false);
                jv.setVisible(true);
            } catch (JRException | SQLException | UnsupportedEncodingException  ex) {
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
    
    private boolean printJO(){
        if (_trans.getEditMode() != EditMode.READY){
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "No transaction was loaded or transaction loaded was already processed.", "Warning", "");
            return false;
        }
        
        try {
            JSONArray json_arr = new JSONArray();
            json_arr.clear();

            JSONObject json_obj;

            for (int lnCtr = 0; lnCtr <= _trans.getItemCount()-1; lnCtr++){
                json_obj = new JSONObject();

                json_obj.put("nField01", (int) _trans.getDetail(lnCtr, "nQuantity"));
                json_obj.put("sField01", (String) _trans.getDetail(lnCtr, "sLaborNme"));
                json_obj.put("lField01", Double.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nUnitPrce"))));
                json_obj.put("lField02", Double.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nDiscount"))));
                json_obj.put("lField03", Double.valueOf(String.valueOf(_trans.getDetail(lnCtr, "nAddDiscx"))));
                json_arr.add(json_obj); 
            }
            
            for (int lnCtr = 0; lnCtr <= _trans.getPartsCount()-1; lnCtr++){
                json_obj = new JSONObject();

                json_obj.put("nField01", (int) _trans.getParts(lnCtr, "nQuantity"));
                json_obj.put("sField01", (String) _trans.getParts(lnCtr, "sDescript"));
                json_obj.put("lField01", Double.valueOf(String.valueOf(_trans.getParts(lnCtr, "nUnitPrce"))));
                json_obj.put("lField02", Double.valueOf(String.valueOf(_trans.getParts(lnCtr, "nDiscount"))));
                json_obj.put("lField03", Double.valueOf(String.valueOf(_trans.getParts(lnCtr, "nAddDiscx"))));
                json_arr.add(json_obj); 
            }

            String lsTranStat = "";
            switch((String) _trans.getMaster("cTranStat")){
                case "0":
                    lsTranStat = "NEW";
                    break;
                case "1":
                    lsTranStat = "PRINTED";
                    break;
                case "2":
                    lsTranStat = "PAYED";
                    break;
                case "3":
                    lsTranStat = "CANCELLED";
                    break;
                case "4":
                    lsTranStat = "RELEASED";
                    break;
                default:
                    lsTranStat = "UNKNOWN";
            }
            
            //Create the parameter
            Map<String, Object> params = new HashMap<>();
            params.put("sAddressx", (String) _nautilus.getBranchConfig("sAddressx") + ", " + (String) _nautilus.getBranchConfig("xTownName"));
            params.put("sClientNm", (String) _trans.getMaster("xClientNm"));
            params.put("xAddressx", "-");
            params.put("sReferNox", lsTranStat);
            params.put("nAmtPaidx", Double.valueOf((String.valueOf(_trans.getMaster("nLabrPaid")))));
            params.put("dTransact", SQLUtil.dateFormat((Date) _trans.getMaster("dTransact"), SQLUtil.FORMAT_MEDIUM_DATE));
            params.put("sSalesman", (String) _trans.getMaster("xMechanic"));

            double lnTranTotl = Double.valueOf(String.valueOf(_trans.getMaster("nLabrTotl"))) +
                Double.valueOf(String.valueOf(_trans.getMaster("nPartTotl")));
            params.put("nTranTotl", lnTranTotl);

            double lnDiscount = Double.valueOf(String.valueOf(_trans.getMaster("nLabrTotl"))) * Double.valueOf(String.valueOf(_trans.getMaster("nLabrDisc"))) / 100;
            lnDiscount += Double.valueOf(String.valueOf(_trans.getMaster("nPartTotl"))) * Double.valueOf(String.valueOf(_trans.getMaster("nPartDisc"))) / 100;
            
            params.put("nDiscount", lnDiscount);

            InputStream stream = new ByteArrayInputStream(json_arr.toJSONString().getBytes("UTF-8"));
            JsonDataSource jrjson = new JsonDataSource(stream); 

            JasperPrint _jrprint = JasperFillManager.fillReport(System.getProperty("sys.default.path.config") +
                                                                "reports/JOForm.jasper", params, jrjson);
            JasperViewer jv = new JasperViewer(_jrprint, false);
            jv.setVisible(true);
        } catch (JRException | UnsupportedEncodingException  ex) {
            ex.printStackTrace();
            ShowMessageFX.Error(ex.getMessage(), "Exception", "Warning");
            return false;
        }
        
        return true;
    }
    
    private boolean printInvoice(){
        if (_trans.getEditMode() != EditMode.READY){
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "No transaction was loaded or transaction loaded was already processed.", "Warning", "");
            return false;
        }
        
        if ("2".equals((String) _trans.getMaster("cTranStat"))){
            if (!ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to print Sales Invoice?", "Confirm", "")) return false;
            
            try {
                ResultSet loRS = _trans.getInvoice_Info();
                
                if (!loRS.next()) return false;
                
                JSONArray json_arr = new JSONArray();
                json_arr.clear();

                JSONObject json_obj;

                for (int lnCtr = 0; lnCtr <= _trans.getPartsCount()-1; lnCtr++){
                    json_obj = new JSONObject();
                    
                    json_obj.put("nField01", (int) _trans.getParts(lnCtr, "nQuantity"));
                    json_obj.put("sField01", (String) _trans.getParts(lnCtr, "sBarCodex"));
                    json_obj.put("sField02", (String) _trans.getParts(lnCtr, "sDescript"));
                    json_obj.put("lField01", Double.valueOf(String.valueOf(_trans.getParts(lnCtr, "nUnitPrce"))));
                    json_obj.put("lField02", Double.valueOf(String.valueOf(_trans.getParts(lnCtr, "nDiscount"))));
                    json_obj.put("lField03", Double.valueOf(String.valueOf(_trans.getParts(lnCtr, "nAddDiscx"))));
                    json_arr.add(json_obj); 
                }

                //Create the parameter
                Map<String, Object> params = new HashMap<>();
                params.put("sAddressx", (String) _nautilus.getBranchConfig("sAddressx") + ", " + (String) _nautilus.getBranchConfig("xTownName"));
                params.put("sClientNm", loRS.getString("sClientNm"));
                params.put("xAddressx", loRS.getString("xAddressx"));
                params.put("sReferNox", loRS.getString("sInvNumbr"));
                params.put("nAmtPaidx", Double.valueOf((String.valueOf(_trans.getMaster("nPartPaid")))));
                params.put("dTransact", SQLUtil.dateFormat(loRS.getDate("dTransact"), SQLUtil.FORMAT_MEDIUM_DATE));
                params.put("sSalesman", (String) _trans.getMaster("xMechanic"));

                double lnTranTotl = Double.valueOf(String.valueOf(_trans.getMaster("nPartTotl")));
                params.put("nTranTotl", lnTranTotl);
                
                params.put("nFreightx", Double.valueOf(String.valueOf(_trans.getMaster("nFreightx"))));
                
                double lnDiscount = lnTranTotl * Double.valueOf(String.valueOf(_trans.getMaster("nPartDisc"))) / 100;
                params.put("nDiscount", lnDiscount);

                InputStream stream = new ByteArrayInputStream(json_arr.toJSONString().getBytes("UTF-8"));
                JsonDataSource jrjson = new JsonDataSource(stream); 

                JasperPrint _jrprint = JasperFillManager.fillReport(System.getProperty("sys.default.path.config") +
                                                                    "reports/SP_DR.jasper", params, jrjson);
                JasperViewer jv = new JasperViewer(_jrprint, false);
                jv.setVisible(true);
            } catch (JRException | SQLException | UnsupportedEncodingException  ex) {
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
                            } else {
                                ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                                
                                _trans = new JobOrder(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
                                _trans.setSaveToDisk(false);
                                _trans.setListener(_listener);
                                _trans.setOtherListener(_oth_listener);
                                
                                initGrid();
                                initButton();
                                clearFields();
                            }
                        } else {                            
                            initGrid();
                            initButton();
                            clearFields();
                        }
                            
                        break;
                }
            }

            @Override
            public void FormClosing(TextField foField) {
                foField.requestFocus();
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
        
        btn01.setText("Browse");
        btn02.setText("Print");
        btn03.setText("Cancel");
        btn04.setText("");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("");
        btn11.setText("Invoice");
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
        btn10.setVisible(false);
        btn11.setVisible(false);
        btn12.setVisible(true);
    }
    
    private void initFields(){
        txtSeeks01.setOnKeyPressed(this::txtField_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtSeeks01.focusedProperty().addListener(txtField_Focus);
        txtSeeks02.focusedProperty().addListener(txtField_Focus);
        
        cmbStatus.setItems(_status);
        cmbStatus.getSelectionModel().select(0);
        _transtat = 12340;
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
    
    private void setTranStat(String fsValue){
        switch(fsValue){
            case "0":
                lblTranStat.setText("NEW");
                break;
            case "1":
                lblTranStat.setText("PRINTED");
                break;
            case "2":
                lblTranStat.setText("PAYED");
                break;
            case "3":
                lblTranStat.setText("CANCELLED");
                break;
            case "4":
                lblTranStat.setText("RELEASED");
                break;
            default:
                lblTranStat.setText("UNKNOWN");
        }
    }
    
    @FXML
    private void cmbStatus_Click(ActionEvent event) {
        switch (cmbStatus.getSelectionModel().getSelectedIndex()){
            case 0:
                _transtat = 12340;
                break;
            default:
                _transtat = cmbStatus.getSelectionModel().getSelectedIndex() - 1;
        }
        
        
        _trans.setTranStat(_transtat);
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

    @FXML
    private void chkWarranty_Click(ActionEvent event) {
    }
}
