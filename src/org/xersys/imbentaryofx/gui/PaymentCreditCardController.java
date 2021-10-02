package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.commander.iface.LRecordMas;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.iface.XPaymentInfo;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.MsgBox;
import org.xersys.commander.util.StringUtil;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreenInfo;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.PaymentListener;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.payment.base.CreditCardTrans;

public class PaymentCreditCardController implements Initializable {
    private XNautilus p_oNautilus;
    
    private LRecordMas p_oListener;
    private CreditCardTrans p_oTrans;
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    private PaymentListener _parent_callback;
    
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    
    private int _index;
    private int _detail_row;
    private boolean _loaded;
    
    @FXML
    private Button btn01;
    @FXML
    private Button btn02;
    @FXML
    private TableView _table;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;
    @FXML
    private Label lblTranTotl;

    public void setNautilus(XNautilus foValue){
        p_oNautilus = foValue;
    }
    
    public void setTransactionObject(XPaymentInfo foValue){
        p_oTrans = (CreditCardTrans) foValue;
    }
    
    public void setScreensController(ScreensController foValue) {
        _screens_controller = foValue;
    }
    
    public void setCallback(PaymentListener foValue){
        _parent_callback = foValue;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (p_oNautilus  == null) {
            System.err.println("Application driver is not set.");
            System.exit(1);
        }
        
        if (p_oTrans  == null) {
            System.err.println("Trasaction Object is not set.");
            System.exit(1);
        }
        
        initGrid();
        clearFields();
        initListener();
        
        p_oTrans.setListener(p_oListener);
        
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);

        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);

        btn01.setOnAction(this::cmdButton_Click);
        btn02.setOnAction(this::cmdButton_Click);
        
        loadDetail();
        
        txtField01.requestFocus();
        txtField01.selectAll();
        
        _loaded = true;
    }    
    
    
    
    private void initListener(){
        p_oListener = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                String lsValue;
                double lnValue;
                
                switch(fsFieldNm){
                    case "sTermnlID":
                        txtField01.setText((String) foValue);
                        break;
                    case "sBankName":
                        txtField02.setText((String) foValue);
                        break;
                    case "sCardNoxx":
                        lsValue = (String) foValue;
                        
                        if (!lsValue.isEmpty())
                            txtField03.setText("************" + lsValue.substring(lsValue.length() - 4));
                            
                        break;
                    case "sApprovNo":
                        lsValue = (String) foValue;
                        
                        if (!lsValue.isEmpty())
                            txtField04.setText((String) foValue);
                        break;
                    case "nAmountxx":
                        lnValue = ((Number) foValue).doubleValue();
                        
                        if (lnValue != 0.00)
                            txtField05.setText(StringUtil.NumberFormat(lnValue, "#,##0.00"));
                        break;
                    case "nPaymTotl":
                        lnValue = ((Number) foValue).doubleValue();
                        lblTranTotl.setText(StringUtil.NumberFormat(lnValue, "#,##0.00"));
                        
                        break;
                }
                
                loadDetail();
            }
        };
        
        _search_callback = new QuickSearchCallback() {
                @Override
                public void Result(TextField foField, JSONObject foValue) {
                    if ("success".equals((String) foValue.get("result"))){
                        foValue = (JSONObject) foValue.get("payload");
                        
                        switch (foField.getId()){
                            case "txtField02":
                                p_oTrans.setDetail(_detail_row + 1, "sBankCode", (String) foValue.get("sBankCode"));
                                break;
                        }
                    }
                    
                    
                }

                @Override
                public void FormClosing(TextField foField) {
                    switch (foField.getId()){
                        case "txtField02":
                            txtField03.requestFocus();
                            txtField03.selectAll();
                            break;
                    }
                }
            };
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //add
                if (!p_oTrans.addDetail())
                    MsgBox.showOk(p_oTrans.getMessage(), "Warning");
                else{
                    loadDetail();
                    clearFields();
                    
                    txtField01.requestFocus();
                    txtField01.selectAll();
                }
                break;
            case "btn02": //remove
                if (!p_oTrans.delDetail(_detail_row + 1)) 
                    MsgBox.showOk(p_oTrans.getMessage(), "Warning");
                else{
                    loadDetail();
                    clearFields();
                    
                    txtField01.requestFocus();
                    txtField01.selectAll();
                }
                break;
        }
    }
    
    private void loadDetail(){
        int lnCtr;
        int lnRow = p_oTrans.getItemCount();
        
        _table_data.clear();
        
        for(lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++){        
            String lsCardNoxx = (String) p_oTrans.getDetail(lnCtr + 1, "sCardNoxx");
                        
            if (!lsCardNoxx.isEmpty())
                lsCardNoxx = "****" + lsCardNoxx.substring(lsCardNoxx.length() - 4);
            
            
            _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                        (String) p_oTrans.getDetail(lnCtr + 1, "sBankName"),
                        lsCardNoxx,
                        StringUtil.NumberFormat((Number) p_oTrans.getDetail(lnCtr + 1, "nAmountxx"), "#,##0.00"),
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""));
        }

        if (!_table_data.isEmpty()){
            _table.getSelectionModel().select(lnRow - 1);
            _table.getFocusModel().focus(lnRow - 1); 
            _detail_row = _table.getSelectionModel().getSelectedIndex();
        }
        
        _parent_callback.Update();
    }
    
    private void clearFields(){
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("0.00");
    }
    
    private void setFieldInfo(){
        txtField01.setText((String) p_oTrans.getDetail(_detail_row + 1, "sTermnlID"));
        txtField02.setText((String) p_oTrans.getDetail(_detail_row + 1, "sBankName"));
        
        String lsCardNoxx = (String) p_oTrans.getDetail(_detail_row + 1, "sCardNoxx");
                        
        if (!lsCardNoxx.isEmpty())
            lsCardNoxx = "********" + lsCardNoxx.substring(lsCardNoxx.length() - 4);
        
        txtField03.setText(lsCardNoxx);
        txtField04.setText((String) p_oTrans.getDetail(_detail_row + 1, "sApprovNo"));
        
        
        double lnValue = ((Number) p_oTrans.getDetail(_detail_row + 1, "nAmountxx")).doubleValue();                
        txtField05.setText(StringUtil.NumberFormat(lnValue, "#,##0.00"));
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("");
        TableColumn index02 = new TableColumn("");
        TableColumn index03 = new TableColumn("");
        TableColumn index04 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false); index01.setStyle( "-fx-alignment: CENTER-LEFT;");
        index02.setSortable(false); index02.setResizable(false); index02.setStyle( "-fx-alignment: CENTER-LEFT;");
        index03.setSortable(false); index03.setResizable(false); index03.setStyle( "-fx-alignment: CENTER;");
        index04.setSortable(false); index04.setResizable(false); index04.setStyle( "-fx-alignment: CENTER-RIGHT;");
        
        _table.getColumns().clear();        
        
        index01.setText("No."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(30);
        
        index02.setText("Bank"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(200);
        
        index03.setText("Card No."); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(100);
        
        index04.setText("Amount"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(100);
        
        _table.getColumns().add(index01);
        _table.getColumns().add(index02);
        _table.getColumns().add(index03);
        _table.getColumns().add(index04);
        
        _table_data.clear();
        _table.setItems(_table_data);
        _table.setOnMouseClicked(this::tableClicked);
    }
    
    private void tableClicked(MouseEvent event) { 
        _detail_row = _table.getSelectionModel().getSelectedIndex();
        
        setFieldInfo();
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        JSONObject loJSON;
        
        if (event.getCode() == KeyCode.ENTER){
            switch (lsTxt){
                case "txtField02":
                    System.out.println(this.getClass().getSimpleName() + " " + lsTxt + " was used for searching");                    
                    searchBanks("sBankName", lsValue, "", "", false);
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
    
    private void searchBanks(String fsKey, Object foValue, String fsFilter, String fsValue, boolean fbExact){
        JSONObject loJSON = p_oTrans.searchBanks(fsKey, foValue, fsFilter, fsValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        txtField02.setText((String) loJSON.get("sBankName"));
                        FXUtil.SetNextFocus(txtField02);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(p_oNautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(p_oTrans.getSearchBanks());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtField02);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                MsgBox.showOk("ParseException detected.", "Warning");
                txtField02.setText("");
                FXUtil.SetNextFocus(txtField02);
            }
        } else {
            MsgBox.showOk((String) loJSON.get("message"), "Warning");
            txtField02.setText("");
            FXUtil.SetNextFocus(txtField02);
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
                case 1: //sTermnlID                    
                    p_oTrans.setDetail(_detail_row + 1, "sTermnlID", lsValue);
                    break;
                case 3: //sCardNoxx
                    if (!lsValue.isEmpty()){
                        if (!StringUtil.isNumeric(lsValue)){
                            MsgBox.showOk("Card number value must be numeric.", "Notice");
                            p_oTrans.setDetail(_detail_row + 1, "sCardNoxx", "");
                            return;
                        }
                        
                        if (lsValue.length() < 4){
                            MsgBox.showOk("Invalid card number.", "Notice");
                            p_oTrans.setDetail(_detail_row + 1, "sCardNoxx", "");
                            return;
                        }
                        
                        if (lsValue.length() > 16){
                            MsgBox.showOk("Invalid card number size.", "Notice");
                            p_oTrans.setDetail(_detail_row + 1, "sCardNoxx", "");
                            return;
                        }
                    }
                    
                    p_oTrans.setDetail(_detail_row + 1, "sCardNoxx", lsValue);
                    break;
                case 4: //sApprovNo
                    if (!lsValue.isEmpty()){
                        if (!StringUtil.isNumeric(lsValue)){
                            MsgBox.showOk("Approval number value must be numeric.", "Notice");
                            p_oTrans.setDetail(_detail_row + 1, "sApprovNo", "");
                            return;
                        }
                    }
                    
                    p_oTrans.setDetail(_detail_row + 1, "sApprovNo", lsValue);
                    break;
                case 5: //nAmountxx
                    if (!StringUtil.isNumeric(lsValue)){
                        MsgBox.showOk("Cash amount value must be numeric.", "Notice");
                        p_oTrans.setDetail(_detail_row + 1, "nAmountxx", 0.00);
                        return;
                    }
                    
                    p_oTrans.setDetail(_detail_row + 1, "nAmountxx", Double.valueOf(lsValue));
                    break;
                default:
                    MsgBox.showOk("Text field with name " + txtField.getId() + " not registered.", "Warning");
            }
            _index = lnIndex;
        } else{ //Got Focus     
            switch (lnIndex){
                case 3:
                    txtField03.setText((String) p_oTrans.getDetail(_detail_row + 1, "sCardNoxx"));
                    break;
                case 5:
                    txtField05.setText(String.valueOf(p_oTrans.getDetail(_detail_row + 1, "nAmountxx")));
                    break;
            }
            txtField.selectAll();
        }
    };    
}
