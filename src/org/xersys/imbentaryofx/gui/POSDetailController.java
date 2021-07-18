package org.xersys.imbentaryofx.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.DetailUpdateCallback;
import org.xersys.kumander.contants.SearchEnum;
import org.xersys.kumander.iface.XNautilus;
import org.xersys.kumander.util.FXUtil;
import org.xersys.kumander.util.MsgBox;
import org.xersys.kumander.util.StringUtil;

public class POSDetailController implements Initializable, ControlledScreen  {
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private DetailUpdateCallback _callback;
    private boolean _loaded = false;
    
    private int _row;
    private String _part_number;
    private String _description;
    private String _other_info;
    private int _order;
    private int _on_hand;
    private double _srp;
    private double _discount;
    private double _additional;
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private VBox btnbox00;
    @FXML
    private HBox btnbox01;
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
    private FontAwesomeIconView glyph01;
    @FXML
    private FontAwesomeIconView glyph02;
    @FXML
    private FontAwesomeIconView glyph03;
    @FXML
    private FontAwesomeIconView glyph04;
    @FXML
    private FontAwesomeIconView glyph05;
    @FXML
    private FontAwesomeIconView glyph06;
    @FXML
    private FontAwesomeIconView glyph07;
    @FXML
    private FontAwesomeIconView glyph08;
    @FXML
    private FontAwesomeIconView glyph09;
    @FXML
    private FontAwesomeIconView glyph10;
    @FXML
    private FontAwesomeIconView glyph11;
    @FXML
    private FontAwesomeIconView glyph12;
    @FXML
    private Label lblTotal;

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
    
    public void setQtyOrder(int fnValue){
        _order = fnValue;
    }
    
    public void setOnHand(int fnValue){
        _on_hand = fnValue;
    }
    
    public void setPartNumber(String fsValue){
        _part_number = fsValue;
    }
    
    public void setDescription(String fsValue){
        _description = fsValue;
    }
    
    public void setOtherInfo(String fsValue){
        _other_info = fsValue;
    }
    
    public void setSellingPrice(double fnValue){
        _srp = fnValue;
    }
    
    public void setDiscount(double fnValue){
        _discount = fnValue;
    }
    
    public void setAdditional(double fnValue){
        _additional = fnValue;
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
        btn03.setText("+5 Order");
        btn04.setText("+10 Order");
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
        btn03.setVisible(true);
        btn04.setVisible(true);
        btn05.setVisible(false);
        btn06.setVisible(false);
        btn07.setVisible(false);
        btn08.setVisible(false);
        btn09.setVisible(false);
        btn10.setVisible(false);
        btn11.setVisible(false);
        btn12.setVisible(true);
        
        glyph01.setIcon(FontAwesomeIcon.ANCHOR);
        glyph02.setIcon(FontAwesomeIcon.ANCHOR);
        glyph03.setIcon(FontAwesomeIcon.ANCHOR);
        glyph04.setIcon(FontAwesomeIcon.ANCHOR);
        glyph05.setIcon(FontAwesomeIcon.ANCHOR);
        glyph06.setIcon(FontAwesomeIcon.ANCHOR);
        glyph07.setIcon(FontAwesomeIcon.ANCHOR);
        glyph08.setIcon(FontAwesomeIcon.ANCHOR);
        glyph09.setIcon(FontAwesomeIcon.ANCHOR);
        glyph10.setIcon(FontAwesomeIcon.ANCHOR);
        glyph11.setIcon(FontAwesomeIcon.ANCHOR);
        glyph12.setIcon(FontAwesomeIcon.ANCHOR);
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
        txtDetail03.setText(_other_info);
        txtDetail04.setText(StringUtil.NumberFormat(_srp, "#,##0.00"));
        txtDetail05.setText(String.valueOf(_on_hand));
        txtDetail06.setText(String.valueOf(_order));
        txtDetail07.setText(StringUtil.NumberFormat(_discount, "##0.00"));
        txtDetail08.setText(StringUtil.NumberFormat(_additional, "##0.00"));
        
        computeTotal();
        
        txtDetail06.requestFocus();
        txtDetail06.selectAll();
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
            case "btn03": //+5 order
                _order += 5;
                txtDetail06.setText(String.valueOf(_order));
                break;
            case "btn04": //+10 order
                _order += 10;
                txtDetail06.setText(String.valueOf(_order));
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
        _callback.Result(_row, 5, _order);
        _callback.Result(_row, 8, _discount);
        _callback.Result(_row, 9, _additional);
        
        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
        _callback.FormClosing();
    }
    
    private void removeData(){
        //load the data
        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
        _callback.RemovedItem(0);
        _callback.FormClosing();
    }
    
    private void computeTotal(){        
        double lnTranTotl = (_order * (_srp - (_srp * _discount))) - _additional;
        
        lblTotal.setText(StringUtil.NumberFormat(lnTranTotl, "#,##0.00"));
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
                case 6:
                    if (StringUtil.isNumeric(lsValue))                    
                        txtField.setText(lsValue);                    
                    else{
                        MsgBox.showOk("Please encode a numeric value with correct format.", "Warning");
                        txtField.setText("0");
                    } 
                        
                    
                    _order = Integer.parseInt(txtField.getText());
                    break;
                case 7:
                    if (StringUtil.isNumeric(lsValue)){
                        double lnValue = Double.valueOf(lsValue);
                        
                        if (lnValue > 1)
                            txtField.setText("1.00");
                        else
                            txtField.setText(StringUtil.NumberFormat(lnValue, "##0.00"));
                    } else {
                        MsgBox.showOk("Please encode a numeric value with correct format.", "Warning");
                        txtField.setText("0.00");
                    }
                        
                    _discount = Double.valueOf(txtField.getText());
                    break;
                case 8:
                    if (StringUtil.isNumeric(lsValue)){
                        double lnValue = Double.valueOf(lsValue);
                        
                        txtField.setText(StringUtil.NumberFormat(lnValue, "##0.00"));                            
                    } else {
                        MsgBox.showOk("Please encode a numeric value with correct format.", "Warning");
                        txtField.setText("0.00");
                    }
                        
                    _additional = Double.valueOf(txtField.getText());
                    break;
            }
            
            computeTotal();
        }
    };
}
