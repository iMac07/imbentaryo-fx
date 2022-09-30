package org.xersys.imbentaryofx.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.clients.search.ClientSearch;
import org.xersys.commander.contants.InvoiceType;
import org.xersys.commander.iface.LRecordMas;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.iface.XPayments;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.SQLUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.payment.base.PaymentFactory;
import org.xersys.imbentaryofx.listener.PaymentListener;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.payment.search.PaymentSearch;
import org.xersys.sales.search.SalesSearch;

public class SIHistoryController implements Initializable, ControlledScreen {
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private QuickSearchCallback _search_callback;
    private XNautilus _nautilus;
    private LRecordMas _listener;
    
    private PaymentListener _credit_card_callback;
    private PaymentListener _cheque_callback;
    private PaymentListener _gc_callback;
    
    private int _index;
    private boolean _loaded = false;
       
    private XPayments _trans;
    private PaymentSearch _trans_search;
    
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
    private Label lblVATableSales;
    @FXML
    private Label lblVATAmount;
    @FXML
    private Label lblNonVATSales;
    @FXML
    private Label lblZeroRatedSales;
    @FXML
    private Label lblCreditCardAmount;
    @FXML
    private Label lblChequeAmount;
    @FXML
    private Label lblGCAmount;
    @FXML
    private Label lblTotalPayment;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtTIN;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;
    @FXML
    private Label lblCash;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtSeeks01;
    @FXML
    private TextField txtSeeks02;
    @FXML
    private Label lblTranStat;

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
        
        initButton();
        clearFields();
        
        txtSeeks01.setOnKeyPressed(this::txtField_KeyPressed);
        txtSeeks02.setOnKeyPressed(this::txtField_KeyPressed);
        txtSeeks01.focusedProperty().addListener(txtField_Focus);
        txtSeeks02.focusedProperty().addListener(txtField_Focus);

        _trans_search = new PaymentSearch(_nautilus, PaymentSearch.SearchType.searchSIPayment);
        
        _trans = new PaymentFactory().make(InvoiceType.SALES_INVOICE, _nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);   

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
    
    private void displayTotalPayment(){
        double lnCashAmtx = (double) _trans.getMaster("nCashAmtx");
        double lnCardPaym = _trans.getCreditCardInfo().getPaymentTotal();
        
        lblTotalPayment.setText(StringUtil.NumberFormat(lnCashAmtx + lnCardPaym, "#,##0.00"));
    }
    
    private void clearFields(){
        lblCash.setText("0.00");
        lblChequeAmount.setText("0.00");
        lblCreditCardAmount.setText("0.00");
        lblGCAmount.setText("0.00");

        lblVATableSales.setText("0.00");
        lblVATAmount.setText("0.00");
        lblNonVATSales.setText("0.00");
        lblZeroRatedSales.setText("0.00");

        lblTotalPayment.setText("0.00");
        
        txtField04.setText("");
        txtField05.setText("");
        txtAddress.setText("");
        txtTIN.setText("");
        
        txtSeeks01.setText("");
        txtSeeks02.setText("");
        lblTranStat.setText("UNKOWN");
    }
    
    private void loadTransaction(){
        double lnCashAmntx = Double.valueOf(String.valueOf(_trans.getMaster("nCashAmtx")));
        double lnCreditCrd = _trans.getCreditCardInfo().getPaymentTotal();
        double lnCheckPaym = 0.00;
        double lnGCPayment = 0.00;
        double lnTotalPaym = lnCashAmntx + lnCreditCrd + lnCheckPaym + lnGCPayment;
        
        lblCash.setText(StringUtil.NumberFormat(lnCashAmntx, "#,##0.00"));
        lblCreditCardAmount.setText(StringUtil.NumberFormat(lnCreditCrd, "#,##0.00"));        
        lblChequeAmount.setText(StringUtil.NumberFormat(lnCheckPaym, "#,##0.00"));
        lblGCAmount.setText(StringUtil.NumberFormat(lnGCPayment, "#,##0.00"));
        
        double lnTranTotl = (double) _trans.getMaster("nTranTotl");        
        
        lblTotalPayment.setText(StringUtil.NumberFormat(lnTotalPaym, "#,##0.00"));
        
        double lnVATSales = Double.valueOf(String.valueOf(_trans.getMaster("nVATSales")));
        double lnVATAmtxx = Double.valueOf(String.valueOf(_trans.getMaster("nVATAmtxx")));
        double lnNonVATSl = Double.valueOf(String.valueOf(_trans.getMaster("nNonVATSl")));
        double lnZroVATSl = Double.valueOf(String.valueOf(_trans.getMaster("nZroVATSl")));
        
        lblVATableSales.setText(StringUtil.NumberFormat(lnVATSales, "#,##0.00"));
        lblVATAmount.setText(StringUtil.NumberFormat(lnVATAmtxx, "#,##0.00"));
        lblNonVATSales.setText(StringUtil.NumberFormat(lnNonVATSl, "#,##0.00"));
        lblZeroRatedSales.setText(StringUtil.NumberFormat(lnZroVATSl, "#,##0.00"));
        
        txtField05.setText((String) _trans.getMaster("sClientNm"));
        txtAddress.setText("");
        txtTIN.setText("");
        
        txtField04.setText((String) _trans.getMaster("sInvNumbr"));
        txtField03.setText(SQLUtil.dateFormat((Date) _trans.getMaster("dTransact"), SQLUtil.FORMAT_MEDIUM_DATE));
        
        txtSeeks01.setText(txtField05.getText());
        txtSeeks02.setText(txtField04.getText());
        
        setTranStat((String) _trans.getMaster("cTranStat"));
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
        
        btn01.setText("Print");
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
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01":
                if (ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to print the invoice?", "Confirm", "")){
                    if (!_trans.PrintTransaction()) 
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                    else {
                        loadTransaction();
                        ShowMessageFX.Information(_main_screen_controller.getStage(), "Invoice printed successfully.", "Success", "");
                    }
                }
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
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                break;
        }
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
    
    private void searchTransaction(TextField foTextField, String fsField, String fsValue, boolean fbByCode){
        _trans_search.setKey(fsField);
        _trans_search.setValue(fsValue);
        _trans_search.setExact(fbByCode);
        
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
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
        
        switch (event.getCode()){
            case ENTER:
                switch (lsTxt){
                    case "txtSeeks01":
                        searchTransaction(txtSeeks01, "sClientNm", txtSeeks01.getText().trim(), false);
                        event.consume();
                        break;
                    case "txtSeeks02":
                        searchTransaction(txtSeeks02, "sInvNumbr", txtSeeks02.getText().trim(), false);
                        event.consume();
                        break;
                }
            case F3:
                switch (lsTxt){
                    case "txtSeeks01":
                        searchTransaction(txtSeeks01, "sClientNm", txtSeeks01.getText().trim(), false);
                        event.consume();
                        break;
                    case "txtSeeks02":
                        searchTransaction(txtSeeks02, "sInvNumbr", txtSeeks02.getText().trim(), false);
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
