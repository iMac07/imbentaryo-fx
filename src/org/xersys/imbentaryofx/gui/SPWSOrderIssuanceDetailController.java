package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.xersys.imbentaryofx.listener.DetailUpdateCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.StringUtil;

public class SPWSOrderIssuanceDetailController implements Initializable, ControlledScreen  {
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private DetailUpdateCallback _callback;
    private boolean _loaded = false;
    
    private int _row;
    private String _part_number;
    private String _description;    
    private int _on_hand;
    private double _srp;
    private int _order;
    private int _approved;
    private int _cancelled;
    private int _issued;
    private int _released;
    
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
    @FXML
    private TextField txtDetail08;
    @FXML
    private TextField txtDetail09;

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
        _on_hand = fnValue;
    }
    
    public void setSellingPrice(double fnValue){
        _srp = fnValue;
    }
    
    public void setQtyOrder(int fnValue){
        _order = fnValue;
    }
    
    public void setApproved(int fnValue){
        _approved = fnValue;
    }
    
    public void setCancelled(int fnValue){
        _cancelled = fnValue;
    }
    
    public void setIssued(int fnValue){
        _issued = fnValue;
    }
    
    public void setReleased(int fnValue){
        _released = fnValue;
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
        btn12.setText("Close");              
        
        
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
    
    private void initFields(){
        txtDetail01.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail02.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail03.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail04.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail05.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail06.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail07.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail08.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtDetail06.focusedProperty().addListener(txtField_Focus);
        txtDetail07.focusedProperty().addListener(txtField_Focus);
        txtDetail08.focusedProperty().addListener(txtField_Focus);
                
        txtDetail01.setText(_part_number);
        txtDetail02.setText(_description);
        txtDetail03.setText(String.valueOf(_on_hand));
        txtDetail04.setText(StringUtil.NumberFormat(_srp, "#,##0.00"));
        txtDetail05.setText(String.valueOf(_order));
        txtDetail06.setText(String.valueOf(_approved));
        txtDetail07.setText(String.valueOf(_cancelled));
        txtDetail08.setText(String.valueOf(_released));
        txtDetail09.setText(String.valueOf(_issued));
        
        txtDetail08.requestFocus();
        txtDetail08.selectAll();
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //okay    
                loadData();
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
                _callback.FormClosing();
                break;
        }
    }
    
    private void loadData(){      
        //load the data
        _callback.Result(_row, "nApproved", _approved);
        _callback.Result(_row, "nCancelld", _cancelled);
        _callback.Result(_row, "nReleased", _released);
        
        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
        _callback.FormClosing();
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        
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
        int lnIndex = Integer.parseInt(txtField.getId().substring(9, 11));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        
        if(!nv){
            switch (lnIndex){
                case 7: //cancelled
                    if (StringUtil.isNumeric(lsValue)){
                        int lnValue = Integer.parseInt(lsValue);
                        
                        if (lnValue > _order - _issued){
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Cancelled quantity is greater than the order less issued.", "Warning", "");
                            txtField.setText("0");
                            txtDetail06.setText(String.valueOf(_order - _issued));
                        } else
                            txtField.setText(lsValue);                    
                            _approved = _order - _issued - lnValue;
                            txtDetail06.setText(String.valueOf(_approved));
                    } else{
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Please encode a numeric value with correct format.", "Warning", "");
                        txtField.setText("0");
                    } 
                    
                    _cancelled = Integer.parseInt(txtField.getText());
                    break;
                case 8: //released
                    if (StringUtil.isNumeric(lsValue)){
                        int lnValue = Integer.parseInt(lsValue);
                        
                        if (lnValue > _approved - _issued){
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Release quantity is greater than the supposed max value.", "Warning", "");
                            txtField.setText(String.valueOf(_approved - _issued));
                        } else if (lnValue < _issued){
                            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Release quantity is less than the issued value.", "Warning", "");
                            txtField.setText(String.valueOf(_issued));
                        } else
                            txtField.setText(lsValue);                    
                    } else{
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Please encode a numeric value with correct format.", "Warning", "");
                        txtField.setText("0");
                    } 
                    
                    _released = Integer.parseInt(txtField.getText());
                    break;
            }
        } else{
            txtField.selectAll();
        }    
    };
}
