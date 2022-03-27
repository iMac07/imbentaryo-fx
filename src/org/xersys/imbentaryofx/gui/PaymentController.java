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

public class PaymentController implements Initializable, ControlledScreen {
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
    
    private String _source_code = "";
    private String _source_number = "";
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
    private Label lblAdvancePayment;
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
    private AnchorPane anchorPaymentType;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField12;

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
        
        switch (_source_code){
            case "SO":
            case "WS":
                _trans = new PaymentFactory().make(InvoiceType.SALES_INVOICE, _nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);   
                break;
            default:
                _trans = new PaymentFactory().make(InvoiceType.OFFICIAL_RECEIPT, _nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);   
        }
        
        _trans.setListener(_listener);
        _trans.setSourceCd(_source_code);
        _trans.setSourceNo(_source_number);
        
        if (_trans.NewTransaction()){
            loadTransaction();
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
            System.exit(1);
        }
        
        loadCreditCard();
        
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
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        
        _listener = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch (fsFieldNm){
                    case "sInvNumbr":
                        txtField04.setText((String) foValue);
                        break;
                    case "sClientID":
                        txtField05.setText((String) foValue);
                        break;
                    case "nCashAmtx":
                        txtField12.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00"));
                        displayTotalPayment();
                        break;
                    case "nVATSales":
                        lblVATableSales.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00"));
                        break;
                    case "nVATAmtxx":
                        lblVATAmount.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00"));
                        break;
                    case "nNonVATSl":
                        lblNonVATSales.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00"));
                        break;
                    case "nZroVATSl":
                        lblZeroRatedSales.setText(StringUtil.NumberFormat((Number) foValue, "#,##0.00"));
                        break;
                }
            }

            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        _credit_card_callback = new PaymentListener() {
            @Override
            public void Update() {
                lblCreditCardAmount.setText(StringUtil.NumberFormat((Number) _trans.getCreditCardInfo().getPaymentTotal(), "#,##0.00"));
                
                displayTotalPayment();
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if (!"success".equals((String) foValue.get("result"))) return;
                
                foValue = (JSONObject) foValue.get("payload");
                
                switch (foField.getId()){
                    case "txtField05":
                        _trans.setMaster("sClientID", (String) foValue.get("sClientID"));
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
        double lnCashAmtx = (double) _trans.getMaster("nCashAmtx");
        double lnCardPaym = _trans.getCreditCardInfo().getPaymentTotal();
        
        lblTotalPayment.setText(StringUtil.NumberFormat(lnCashAmtx + lnCardPaym, "#,##0.00"));
    }
    
    private void clearFields(){
        lblAdvancePayment.setText("0.00");
        lblChequeAmount.setText("0.00");
        lblCreditCardAmount.setText("0.00");
        lblGCAmount.setText("0.00");
        
        lblNetPayable.setText("0.00");
        lblVATableSales.setText("0.00");
        lblVATAmount.setText("0.00");
        lblNonVATSales.setText("0.00");
        lblZeroRatedSales.setText("0.00");
        
        lblTranTotal.setText("0.00");
        lblAdvancePayment.setText("0.00");
        lblNetPayable.setText("0.00");
        lblTotalPayment.setText("0.00");
        
        txtField05.setText("");
        txtAddress.setText("");
        txtTIN.setText("");
        
        txtField04.setText("");
        txtField12.setText("0.00");
    }
    
    private void loadTransaction(){
        lblAdvancePayment.setText("0.00");
        lblCreditCardAmount.setText(StringUtil.NumberFormat(_trans.getCreditCardInfo().getPaymentTotal(), "#,##0.00"));
        lblChequeAmount.setText("0.00");
        lblGCAmount.setText("0.00");
        
        lblNetPayable.setText("0.00");
        lblVATableSales.setText("0.00");
        lblVATAmount.setText("0.00");
        lblNonVATSales.setText("0.00");
        lblZeroRatedSales.setText("0.00");
        
        lblTranTotal.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nTranTotl"), "#,##0.00"));
        lblAdvancePayment.setText("0.00");
        lblNetPayable.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nTranTotl"), "#,##0.00"));
        lblTotalPayment.setText("0.00");
        
        txtField04.setText("");
        txtField12.setText("0.00");
        
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
        
        btn01.setText("Okay");
        btn02.setText("Card");
        btn03.setText("Cheque");
        btn04.setText("GC");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("");
        btn11.setText("");
        btn12.setText("Cancel");              
        
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
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01":
                if (_trans.SaveTransaction()){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Transaction saved successfully.", "Success", "");
                    
                    if (ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to print the invoice?", "Confirm", "")){
                        if (!_trans.PrintTransaction()) 
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                    }
                    
                    //close this screen
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                } else {
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                }
                break;
            case "btn02":
                loadCreditCard();
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
    
    private void loadDetail(){
    
    }
    
    private void loadCreditCard(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            
            PaymentCreditCardController loControl = new PaymentCreditCardController();
            loControl.setNautilus(_nautilus);
            loControl.setTransactionObject(_trans.getCreditCardInfo());
            loControl.setScreensController(_screens_controller);
            loControl.setCallback(_credit_card_callback);
            
            fxmlLoader.setLocation(loControl.getClass().getResource("PaymentCreditCard.fxml"));
            fxmlLoader.setController(loControl);
            
            AnchorPane root = (AnchorPane) fxmlLoader.load();
            anchorPaymentType.getChildren().clear();
            anchorPaymentType.getChildren().add(root);
        } catch (IOException ex) {
            ex.printStackTrace();
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Unable to load Credit Card Payment form.", "Warning", "");
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
                case "txtField05":
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

                            instance.setSearchObject((ClientSearch) _trans.getSearchClient());
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
                case 4: //sInvNumbr
                    if (!lsValue.isEmpty()){
                        if (!StringUtil.isNumeric(lsValue)){
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Invoice number value must be numeric.", "Warning", "");
                            _trans.setMaster("sInvNumbr", "");
                            return;
                        }
                    }
                    
                    _trans.setMaster("sInvNumbr", lsValue);
                    break;
                case 5: //sClientID
                    _trans.setMaster("nCashAmtx", lsValue);
                    break;
                case 12: //nCashAmtx
                    if (!StringUtil.isNumeric(lsValue)){
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Cash amount value must be numeric.", "Warning", "");
                        _trans.setMaster("nCashAmtx", 0.00);
                        return;
                    }
                    
                    _trans.setMaster("nCashAmtx", Double.valueOf(lsValue));
                    break;
                default:
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "Text field with name " + txtField.getId() + " not registered.", "Warning", "");
            }
            _index = lnIndex;
        } else{ //Got Focus     
            if (lnIndex == 12){
                txtField12.setText(String.valueOf(_trans.getMaster("nCashAmtx")));
            }
            _index = lnIndex;
            txtField.selectAll();
        }
    };    
}
