package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.xersys.imbentaryofx.listener.DetailUpdateCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.StringUtil;

public class InvAdjustmentDetailController implements Initializable, ControlledScreen  {
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private DetailUpdateCallback _callback;
    private boolean _loaded = false;
    
    private int _row;
    private String _part_number;
    private String _description;
    private double _cost;
    private int _qoh;
    private int _debit;
    private int _credit;
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private VBox btnbox00;
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
    private TextField txtDetail01;
    @FXML
    private TextField txtDetail02;
    @FXML
    private TextField txtDetail03;
    @FXML
    private TextField txtDetail04;
    @FXML
    private TextField txtDetail05;
    @FXML
    private TextField txtDetail06;
    @FXML
    private TextField txtDetail07;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0); 
        
        initButton();
        initFields();
        
        _loaded = true;
    }    

    @Override
    public void setNautilus(XNautilus foValue) {
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
    
    public void setCallback(DetailUpdateCallback foValue){
        _callback = foValue;
    }
    
    public void setDetailRow(int fnValue){
        _row = fnValue;
    }
    
    public void setPartNumber(String fsValue){
        _part_number = fsValue;
    }
    
    public void setDescription(String fsValue){
        _description = fsValue;
    }
    
    
    public void setOnHand(int fnValue){
        _qoh = fnValue;
    }
    
    public void setUnitCost(double fnValue){
        _cost = fnValue;
    }
    
    public void setDebitQty(int fnValue){
        _debit = fnValue;
    }
    
    public void setCreditQty(int fnValue){
        _credit = fnValue;
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
        btn02.setText("Remove");
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
    }
    
    private void initFields(){
        txtDetail01.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail02.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail03.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail04.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail05.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail06.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail07.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtDetail05.focusedProperty().addListener(txtField_Focus);
        txtDetail06.focusedProperty().addListener(txtField_Focus);
                
        txtDetail01.setText(_part_number);
        txtDetail02.setText(_description);
        txtDetail03.setText(String.valueOf(_qoh));
        txtDetail04.setText(StringUtil.NumberFormat(_cost, "#,##0.00"));
        txtDetail05.setText(String.valueOf(_debit));
        txtDetail06.setText(String.valueOf(_credit));
        txtDetail07.setText(String.valueOf(_qoh - _credit + _debit));
        
        computeTotal();
        
        txtDetail05.requestFocus();
        txtDetail05.selectAll();
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //okay    
                loadData();
                break;
            case "btn02": //remove item
                removeData();
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
                _callback.FormClosing();
                break;
        }
    }
    
    private void loadData(){        
        //load the data
        _callback.Result(_row, "nDebitQty", _debit);
        _callback.Result(_row, "nCredtQty", _credit);
        
        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
        _callback.FormClosing();
    }
    
    private void removeData(){
        //load the data
        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
        _callback.RemovedItem(_row);
        _callback.FormClosing();
    }
    
    private void computeTotal(){        
        int lnTranTotl = _qoh - _credit + _debit;
        
        txtDetail07.setText(String.valueOf(lnTranTotl));
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            if (txtField.getId().equals("txtDetail08")){
                txtDetail06.selectAll();
                txtDetail06.requestFocus();
                event.consume();
                return;
            }
            
            FXUtil.SetNextFocus(txtField);
            break;
        case UP:
            FXUtil.SetPreviousFocus(txtField);
        }
    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(9, 11));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        
        if(!nv){
            switch (lnIndex){
                case 5:
                    if (StringUtil.isNumeric(lsValue))                    
                        txtField.setText(lsValue);                    
                    else{
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Please encode a numeric value with correct format.", "Warning", "");
                        txtField.setText("0");
                    } 
                        
                    
                    _debit = Integer.parseInt(txtField.getText());
                    break;
                case 6:
                    if (StringUtil.isNumeric(lsValue))                    
                        txtField.setText(lsValue);                    
                    else{
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Please encode a numeric value with correct format.", "Warning", "");
                        txtField.setText("0");
                    } 
                        
                    
                    _credit = Integer.parseInt(txtField.getText());
                    break;
            }
            
            computeTotal();
        }
    };
}
