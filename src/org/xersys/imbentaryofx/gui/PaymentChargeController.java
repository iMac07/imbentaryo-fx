package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import org.xersys.commander.util.MsgBox;
import org.xersys.commander.util.StringUtil;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.payment.base.PaymentFactory;

public class PaymentChargeController implements Initializable, ControlledScreen {
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private QuickSearchCallback _search_callback;
    private XNautilus _nautilus;
    private LRecordMas _listener;
    
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
    private AnchorPane anchorPaymentType;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtAddress;
    @FXML
    private TextField txtTIN;

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
            MsgBox.showOk("Transaction source code was not found.", "Warning");
            System.exit(1);
        }
        
        if (_source_number.isEmpty()){
            MsgBox.showOk("Transaction source number was not found.", "Warning");
            System.exit(1);
        }

        _trans = new PaymentFactory().make(InvoiceType.CHARGE_INVOICE, _nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);   
        
        _trans.setListener(_listener);
        _trans.setSourceCd(_source_code);
        _trans.setSourceNo(_source_number);
        
        if (_trans.NewTransaction()){
            loadTransaction();
        } else {
            MsgBox.showOk(_trans.getMessage(), "Warning");
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
    
    public void setSourceCd(String fsValue){
        _source_code = fsValue;
    }
    
    public void setSourceNo(String fsValue){
        _source_number = fsValue;
    }
    
    private void initListener(){
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);        
        
        _listener = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch (fsFieldNm){
                    case "sClientID":
                        txtField01.setText((String) foValue);
                        break;
                }
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if (!"success".equals((String) foValue.get("result"))) return;
                
                foValue = (JSONObject) foValue.get("payload");
                
                switch (foField.getId()){
                    case "txtField01":
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
    
    private void clearFields(){
        lblAdvancePayment.setText("0.00");
        
        lblNetPayable.setText("0.00");
        
        lblTranTotal.setText("0.00");
        lblAdvancePayment.setText("0.00");
        lblNetPayable.setText("0.00");
    }
    
    private void loadTransaction(){
        lblAdvancePayment.setText("0.00");        
        lblTranTotal.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nAmountxx"), "#,##0.00"));
        lblNetPayable.setText(StringUtil.NumberFormat((Number) _trans.getMaster("nAmountxx"), "#,##0.00"));
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
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01":
                if (_trans.SaveTransaction()){
                    MsgBox.showOk("Transaction saved successfully.", "Success");
                    
                    //close this screen
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                } else {
                    MsgBox.showOk(_trans.getMessage(), "Warning");
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
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        if (event.getCode() == KeyCode.ENTER){
            switch (lsTxt){
                case "txtField01":
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
                        FXUtil.SetNextFocus(txtField01);
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
                            instance.setTextField(txtField01);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                MsgBox.showOk("ParseException detected.", "Warning");
                txtField01.setText("");
                FXUtil.SetNextFocus(txtField01);
            }
        } else {
            MsgBox.showOk((String) loJSON.get("message"), "Warning");
            txtField01.setText("");
            FXUtil.SetNextFocus(txtField01);
        }
    }
}
