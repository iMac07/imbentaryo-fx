package org.xersys.imbentaryofx.gui;

import java.io.IOException;
import java.net.URL;
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
import org.xersys.commander.util.StringUtil;
import org.xersys.payment.base.PaymentFactory;
import org.xersys.imbentaryofx.listener.PaymentListener;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;

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
    private Label lblTranTotal;
    @FXML
    private Label lblNetPayable;
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
    private Label lblAdvPaymt;
    @FXML
    private Label lblCash;

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

        _trans = new PaymentFactory().make(InvoiceType.SALES_INVOICE, _nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);   

        if (_trans.OpenTransaction("000122000002"))
            loadTransaction();
        else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
            System.exit(1);
        }
            
        
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
        lblAdvPaymt.setText("0.00");
        
        lblNetPayable.setText("0.00");
        lblVATableSales.setText("0.00");
        lblVATAmount.setText("0.00");
        lblNonVATSales.setText("0.00");
        lblZeroRatedSales.setText("0.00");
        
        lblTranTotal.setText("0.00");
        lblNetPayable.setText("0.00");
        lblTotalPayment.setText("0.00");
        
        txtField04.setText("");
        txtField05.setText("");
        txtAddress.setText("");
        txtTIN.setText("");
    }
    
    private void loadTransaction(){
        lblCash.setText(StringUtil.NumberFormat(Double.valueOf(String.valueOf(_trans.getMaster("nCashAmtx"))), "#,##0.00"));
        lblCreditCardAmount.setText(StringUtil.NumberFormat(_trans.getCreditCardInfo().getPaymentTotal(), "#,##0.00"));
        lblCreditCardAmount.setText(StringUtil.NumberFormat(Double.valueOf(String.valueOf(_trans.getMaster("nAdvPaymx"))), "#,##0.00"));
        lblChequeAmount.setText("0.00");
        lblGCAmount.setText("0.00");
        
        lblNetPayable.setText("0.00");
        lblVATableSales.setText("0.00");
        lblVATAmount.setText("0.00");
        lblNonVATSales.setText("0.00");
        lblZeroRatedSales.setText("0.00");
        
        double lnTranTotl = (double) _trans.getMaster("nTranTotl");        
        
        lblTranTotal.setText(StringUtil.NumberFormat(lnTranTotl, "#,##0.00"));
        lblNetPayable.setText(StringUtil.NumberFormat(lnTranTotl, "#,##0.00"));
        lblTotalPayment.setText("0.00");
        
        txtField05.setText((String) _trans.getMaster("sClientNm"));
        txtAddress.setText("");
        txtTIN.setText("");
        
        txtField04.setText("");
        
        txtField05.setDisable(!txtField05.getText().isEmpty());
        txtField04.requestFocus();
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
}
