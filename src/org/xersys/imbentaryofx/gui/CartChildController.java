package org.xersys.imbentaryofx.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.imbentaryofx.listener.CartCallback;
import org.xersys.imbentaryofx.listener.CartListener;
import org.xersys.kumander.util.StringUtil;

public class CartChildController implements Initializable, CartListener {
    @FXML
    private Label lblBarcode;
    @FXML
    private Label lblDescript;
    @FXML
    private Label lblOther;
    @FXML
    private Label lblOnHand;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblUnitPrice;
    @FXML
    private TextField txtField01;
    @FXML
    private Button btnChild01;
    @FXML
    private Button btnChild02;
    @FXML
    private Button btnChild03;
    @FXML
    private FontAwesomeIconView fxTrash;

    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        initButton();
        clearFields();
        loadData();
    }    
    
    @Override
    public void setCallback(CartCallback foValue) {
        _callback = foValue;
    }

    @Override
    public void setData(String fsValue) {
        try {
            _data = (JSONObject) _parser.parse(fsValue);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getData() {
        return _data.toJSONString();
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        int lnValue;
        
        switch (lsButton){
            case "btnChild01": //minus
                lnValue = (int) (long) _data.get("nQtyOrder");
                
                if (lnValue > 0){
                    lnValue -= 1;

                    _data.put("nQtyOrder", (long) lnValue);
                    computeTotal();
                    
                    _callback.QtyChanged(-1, (double) _data.get("nUnitPrce"));
                }
                break;
            case "btnChild02": //plus
                lnValue = (int) (long) _data.get("nQtyOrder");
                
                if (lnValue < (int) (long) _data.get("nQtyOnHnd")){
                    lnValue += 1;
                
                    _data.put("nQtyOrder", (long) lnValue);
                    computeTotal();
                    
                    _callback.QtyChanged(1, (double) _data.get("nUnitPrce"));
                }
                break;
            case "btnChild03": //remove
                lnValue = (int) (long) _data.get("nQtyOrder");
                _data.put("nQtyOrder", (long) 0);
                computeTotal();
                
                _callback.QtyChanged(lnValue * -1, (double) _data.get("nUnitPrce"));
                break;
        }
    }
    
    private void initButton(){
        btnChild01.setTooltip(new Tooltip("Deduct Qty"));
        btnChild02.setTooltip(new Tooltip("Add Qty"));
        btnChild03.setTooltip(new Tooltip("Remove"));
        
        btnChild01.setOnAction(this::cmdButton_Click);
        btnChild02.setOnAction(this::cmdButton_Click);
        btnChild03.setOnAction(this::cmdButton_Click);
    }
    
    private void clearFields(){
        lblBarcode.setText("");
        lblDescript.setText("");
        lblOther.setText("");
        lblOnHand.setText("0");
        lblUnitPrice.setText("0.00");
        lblTotal.setText("0.00");
        txtField01.setText("");
    }
    
    private void loadData(){
        int lnQtyOnHnd = (int) (long) _data.get("nQtyOnHnd");
        
        lblBarcode.setText((String) _data.get("sBarrCode"));
        lblDescript.setText((String) _data.get("sDescript"));
        lblOther.setText((String) _data.get("sOtherInf"));
        lblOnHand.setText(String.valueOf(lnQtyOnHnd));
        
        computeTotal();
    }
    
    private void computeTotal(){
        int lnQtyOrder = (int) (long) _data.get("nQtyOrder");
        double lnUnitPrce = (double) _data.get("nUnitPrce");
        
        lblUnitPrice.setText(StringUtil.NumberFormat(lnUnitPrce, "#,##0.00"));
        lblTotal.setText(StringUtil.NumberFormat(lnQtyOrder * lnUnitPrce, "#,##0.00"));
        txtField01.setText(String.valueOf(lnQtyOrder));
    }
    
    private JSONObject _data;
    private JSONParser _parser = new JSONParser();
    private CartCallback _callback;
}
