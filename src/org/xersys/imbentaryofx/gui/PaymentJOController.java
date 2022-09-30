package org.xersys.imbentaryofx.gui;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import org.xersys.clients.search.ClientSearch;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.contants.InvoiceType;
import org.xersys.commander.iface.LRecordMas;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.iface.XPayments;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.SQLUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.payment.base.PaymentFactory;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.sales.base.JobOrder;

public class PaymentJOController implements Initializable, ControlledScreen {
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private QuickSearchCallback _search_callback;
    private XNautilus _nautilus;
    
    private LRecordMas _listener_si;
    private LRecordMas _listener_or;

    private int _index;
    private boolean _loaded = false;
    
    private String _source_code = "";
    private String _source_number = "";
    
    private JobOrder _trans;
    private XPayments _trans_si;
    private XPayments _trans_or;
    
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
    private Label lblNetPayable;
    @FXML
    private Label lblTotalPayment;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtTIN;
    @FXML
    private AnchorPane anchorPaymentType;
    @FXML
    private TextField txtField01;
    @FXML
    private Label lblLabor;
    @FXML
    private TextField txtField02;
    @FXML
    private Label lblParts;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;

    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        if (_nautilus  == null) {
            System.err.println("Application driver is not set.");
            System.exit(1);
        }
        
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);   
        
        initListener();
        initButton();
        clearFields();
        
        if (_source_code.isEmpty()){
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Transaction source code was not found.", "Warning", "");
            System.exit(1);
        }
        
        if (_source_number.isEmpty()){
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Transaction source number was not found.", "Warning", "");
            System.exit(1);
        }
        
        _trans = new JobOrder(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), true, 12);
                
        _trans_or = new PaymentFactory().make(InvoiceType.OFFICIAL_RECEIPT, _nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);   
        _trans_or.setListener(_listener_or);
        _trans_or.setSourceCd(_source_code);
        _trans_or.setSourceNo(_source_number);
        
        if (!_trans_or.NewTransaction()){
            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans_or.getMessage(), "Warning", "");
            System.exit(1);
        }
        
        _trans_si = new PaymentFactory().make(InvoiceType.SALES_INVOICE, _nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);   
        _trans_si.setListener(_listener_si);
        _trans_si.setSourceCd(_source_code);
        _trans_si.setSourceNo(_source_number);
        
        if (!_trans_si.NewTransaction()){
            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans_si.getMessage(), "Warning", "");
            System.exit(1);
        }
        
        loadTransaction();
        
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
    }
    
    public void setSourceCd(String fsValue){
        _source_code = fsValue;
    }
    
    public void setSourceNo(String fsValue){
        _source_number = fsValue;
    }
    
    private void initListener(){
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        
        _listener_si = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsIndex, Object foValue) {
                switch (fsIndex){
                    case "sInvNumbr":
                        txtField02.setText((String) foValue);
                        break;
                    case "nCashAmtx":
                        txtField05.setText(String.valueOf((double) foValue));
                        
                        lblTotalPayment.setText(StringUtil.NumberFormat(
                                                (double) _trans_or.getMaster("nCashAmtx") +
                                                (double) _trans_si.getMaster("nCashAmtx"), "#,##0.00"));
                        break;
                    case "sClientID":
                        txtField03.setText((String) foValue);
                        break;
                }
            }

            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        };
        
        _listener_or = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch (fsFieldNm){
                    case "sInvNumbr":
                        txtField01.setText((String) foValue);
                        break;
                    case "nCashAmtx":
                        txtField04.setText(String.valueOf((double) foValue));
                        
                        lblTotalPayment.setText(StringUtil.NumberFormat(
                                                (double) _trans_or.getMaster("nCashAmtx") +
                                                (double) _trans_si.getMaster("nCashAmtx"), "#,##0.00"));
                        break;
                    case "sClientID":
                        txtField03.setText((String) foValue);
                        break;
                }
            }

            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if (!"success".equals((String) foValue.get("result"))) return;
                
                foValue = (JSONObject) foValue.get("payload");
                
                switch (foField.getId()){
                    case "txtField03":
                        _trans_or.setMaster("sClientID", (String) foValue.get("sClientID"));
                        break;
                }
            }

            @Override
            public void FormClosing(TextField foField) {
                foField.requestFocus();
            }
        };
    }
    
    private void displayTotalPayment(){
        double lnCashOR = (double) _trans_or.getMaster("nCashAmtx");
        double lnCashSI = (double) _trans_si.getMaster("nCashAmtx");
        
        lblTotalPayment.setText(StringUtil.NumberFormat(lnCashOR + lnCashSI, "#,##0.00"));
    }
    
    private void clearFields(){
        lblNetPayable.setText("0.00");
        lblTotalPayment.setText("0.00");
        
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtAddress.setText("");
        txtTIN.setText("");
        txtField04.setText("0.00");
        txtField05.setText("0.00");
    }
    
    private void loadTransaction(){        
        lblLabor.setText("0.00");
        lblParts.setText("0.00");   
        lblNetPayable.setText("0.00");
        lblTotalPayment.setText("0.00");
        
        lblLabor.setText(StringUtil.NumberFormat((double) _trans_or.getMaster("nTranTotl"), "#,##0.00"));
        lblParts.setText(StringUtil.NumberFormat((double) _trans_si.getMaster("nTranTotl"), "#,##0.00"));
        
        txtField01.setDisable((double) _trans_or.getMaster("nTranTotl") <= 0.00);
        txtField04.setDisable((double) _trans_or.getMaster("nTranTotl") <= 0.00);
        
        txtField02.setDisable((double) _trans_si.getMaster("nTranTotl") <= 0.00);
        txtField05.setDisable((double) _trans_si.getMaster("nTranTotl") <= 0.00);
                
        double lnTranTotl = (double) _trans_or.getMaster("nTranTotl") +
                            (double) _trans_si.getMaster("nTranTotl");        
        
        lblNetPayable.setText(StringUtil.NumberFormat(lnTranTotl, "#,##0.00"));
        
        txtField03.setText((String) _trans_or.getMaster("sClientNm"));
        txtAddress.setText("");
        txtTIN.setText("");
        
        txtField01.setText("");        
        txtField02.setText("");        
        txtField04.setText("0.00");        
        txtField05.setText("0.00");
        
        txtField03.setDisable(!txtField03.getText().isEmpty());
        txtField01.requestFocus();
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
        
        btn01.setText("Okay");
        btn02.setText("");
        btn03.setText("");
        btn04.setText("");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("");
        btn11.setText("");
        btn12.setText("Cancel");              
        
        btn01.setVisible(true);
        btn02.setVisible(false);
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
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();       
        
        switch (lsButton){
            case "btn01":        
                if ((double) _trans_or.getMaster("nTranTotl") > (double) _trans_or.getMaster("nCashAmtx")){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Cash amount is less than the total labor.", "Warning", "");
                    return;
                }
                
                if ((double) _trans_si.getMaster("nTranTotl") > (double) _trans_si.getMaster("nCashAmtx")){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Cash amount is less than the total parts.", "Warning", "");
                    return;
                }
                
                if ((double) _trans_si.getMaster("nCashAmtx") > 0.00){
                    if (!_trans_si.SaveTransaction()){
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans_si.getMessage(), "Warning", "");
                        return;
                    }
                    
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction saved successfully.", "Success", "");
                }
                
                if ((double) _trans_or.getMaster("nCashAmtx") > 0.00){
                    if (!_trans_or.SaveTransaction()){
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans_or.getMessage(), "Warning", "");
                        return;
                    }
                }           
                
                if (_trans.OpenTransaction(_source_number)){
                    if ((double) _trans_or.getMaster("nCashAmtx") > 0.00) printReceipt(); 

                    if ((double) _trans_si.getMaster("nCashAmtx") > 0.00) printInvoice();
                }
                
                //close this screen
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                break;
            case "btn02":
                break;
            case "btn03":
                loadCheque();
                break;
            case "btn04":
                loadGC();
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
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                break;
        }
    }
    
    private void loadCheque(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            
            PaymentChequeController loControl = new PaymentChequeController();
            fxmlLoader.setLocation(loControl.getClass().getResource("PaymentCheque.fxml"));
            fxmlLoader.setController(loControl);
            
            AnchorPane root = (AnchorPane) fxmlLoader.load();
            anchorPaymentType.getChildren().clear();
            anchorPaymentType.getChildren().add(root);
        } catch (IOException ex) {
            ex.printStackTrace();
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Unable to load Cheque Payment form.", "Warning", "");
        }
    }
    
    private void loadGC(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            
            PaymentGCController loControl = new PaymentGCController();
            fxmlLoader.setLocation(loControl.getClass().getResource("PaymentGC.fxml"));
            fxmlLoader.setController(loControl);
            
            AnchorPane root = (AnchorPane) fxmlLoader.load();
            anchorPaymentType.getChildren().clear();
            anchorPaymentType.getChildren().add(root);
        } catch (IOException ex) {
            ex.printStackTrace();
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Unable to load Gift Cheque Payment form.", "Warning", "");
        }
    }  
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        if (event.getCode() == KeyCode.ENTER){
            switch (lsTxt){
                case "txtField03":
                    searchClient("a.sClientNm", lsValue, false);
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
    
    private void searchClient(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans_or.searchClient(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        loJSON = (JSONObject) loArray.get(0);
                        _trans_or.setMaster("sClientID", (String) loJSON.get("sClientID"));
                        FXUtil.SetNextFocus(txtField03);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject((ClientSearch) _trans_or.getSearchClient());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtField03);

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
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        if(!nv){ //Lost Focus          
            switch (lnIndex){
                case 1: //sInvNumbr - OR
                    if (!lsValue.isEmpty()){
                        if (!StringUtil.isNumeric(lsValue)){
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Invoice number value must be numeric.", "Warning", "");
                            _trans_or.setMaster("sInvNumbr", "");
                            return;
                        }
                    }
                    
                    _trans_or.setMaster("sInvNumbr", lsValue);
                    break;
                case 2: //sInvNumbr - SI
                    if (!lsValue.isEmpty()){
                        if (!StringUtil.isNumeric(lsValue)){
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Invoice number value must be numeric.", "Warning", "");
                            _trans_si.setMaster("sInvNumbr", "");
                            return;
                        }
                    }
                    
                    _trans_si.setMaster("sInvNumbr", lsValue);
                    break;
                case 3: //sClientID
                    _trans_or.setMaster("nCashAmtx", lsValue);
                    break;
                case 4: //nCashAmtx - OR
                    if (!StringUtil.isNumeric(lsValue)){
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Cash amount value must be numeric.", "Warning", "");
                        _trans_or.setMaster("nCashAmtx", 0.00);
                        return;
                    }
                    
                    _trans_or.setMaster("nCashAmtx", Double.valueOf(lsValue));
                    break;
                case 5: //nCashAmtx - SI
                    if (!StringUtil.isNumeric(lsValue)){
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Cash amount value must be numeric.", "Warning", "");
                        _trans_si.setMaster("nCashAmtx", 0.00);
                        return;
                    }
                    
                    _trans_si.setMaster("nCashAmtx", Double.valueOf(lsValue));
                    break;
                default:
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Text field with name " + txtField.getId() + " not registered.", "Warning", "");
            }
            _index = lnIndex;
        } else{ //Got Focus 
            switch (lnIndex){
                case 4:
                    txtField04.setText(String.valueOf(_trans_or.getMaster("nCashAmtx")));
                    break;
                case 5:
                    txtField05.setText(String.valueOf(_trans_si.getMaster("nCashAmtx")));
                    break;
            }
            
            _index = lnIndex;
            txtField.selectAll();
        }
    };    
}
