package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.CartCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.MsgBox;
import org.xersys.commander.util.StringUtil;

public class Cart2Controller implements Initializable, ControlledScreen {
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private HBox HBoxSummary;
    @FXML
    private Label lblTotalItems;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private Button btnChild01;
    @FXML
    private Button btnChild02;
    @FXML
    private Button btnChild03;
    @FXML
    private TableView table;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);
        
        initGrid();
        
        btnChild01.setOnAction(this::cmdButton_Click);
        btnChild02.setOnAction(this::cmdButton_Click);
        btnChild03.setOnAction(this::cmdButton_Click);
        
        lblTotalItems.setText(String.valueOf(_total_items));
        lblTotalAmount.setText(StringUtil.NumberFormat(_total_amount, "#,##0.00"));
        
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
    
    public void setData(JSONArray foValue){
        _data = foValue;
        
        if (_data != null) displayOrders();
    }
    
    private void displayOrders(){
        JSONObject loJSON;
        
        _table_data.clear();
        
        _total_items = 0;
        _total_amount = 0.00;
        
        for (int lnCtr = 0; lnCtr <= _data.size()-1; lnCtr++){
            loJSON = (JSONObject) _data.get(lnCtr);
            
            _total_items += (long) (int) loJSON.get("nQuantity");
            _total_amount += Double.valueOf(String.valueOf(loJSON.get("nUnitPrce"))) * (long) (int) loJSON.get("nQuantity");
            
            _table_data.add(new TableModel(
                                (String) loJSON.get("sBarCodex"), 
                                (String) loJSON.get("sDescript"), 
                                String.valueOf(loJSON.get("nUnitPrce")), 
                                String.valueOf(loJSON.get("nQuantity")), 
                                "", 
                                "", 
                                "", 
                                "", 
                                "", 
                                ""));
        }  
        
        //save the cart value to system variable
        System.setProperty("shopping.cart", _data.toJSONString());
        
        if (_loaded){
            if (_data.size() > 0) table.getSelectionModel().select(_selected);
            lblTotalItems.setText(String.valueOf(_total_items));
            lblTotalAmount.setText(StringUtil.NumberFormat(_total_amount, "#,##0.00"));
        }
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("");
        TableColumn index02 = new TableColumn("");
        TableColumn index03 = new TableColumn("");
        TableColumn index04 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false); index03.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index04.setSortable(false); index04.setResizable(false); index04.setStyle( "-fx-alignment: CENTER;");
        
        table.getColumns().clear();        
        
        index01.setText("Part No."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(85);
        
        index02.setText("Description"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(115);
        
        index03.setText("UPrice"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(45);
        
        index04.setText("Qty"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(40);
        
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
        table.getColumns().add(index04);
 
        table.setItems(_table_data);
    }
    
    private XNautilus _nautilus;
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private CartCallback _callback;

    private int _selected = 0;
    private int _total_items = 0;
    private double _total_amount = 0.00;
    
    private boolean _loaded = false;
    
    private JSONArray _data;
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    
    
    @FXML
    private void table_clicked(MouseEvent event) {
        _selected = table.getSelectionModel().getSelectedIndex();
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        JSONObject loJSON;
        int lnCtr;
        
        switch (lsButton){
            case "btnChild01": //minus
                if (_data.size() > 0) {
                    loJSON = (JSONObject) _data.get(_selected);
                    lnCtr = (int) loJSON.get("nQuantity");

                    if (lnCtr > 0) {
                        lnCtr -= 1;
                        loJSON.put("nQuantity", lnCtr);
                        _data.set(_selected, loJSON);
                        displayOrders();
                    }                
                }
                break;
            case "btnChild02": //plus
                if (_data.size() > 0) {
                    loJSON = (JSONObject) _data.get(_selected);
                    lnCtr = (int) loJSON.get("nQuantity");

                    lnCtr += 1;
                    loJSON.put("nQuantity", lnCtr);
                    _data.set(_selected, loJSON);
                    displayOrders();
                }
                break;
            case "btnChild03": //delete
                if (_data.size() > 0) {
                    _data.remove(_selected);
                    if (_selected > 0) _selected -= 1;
                    displayOrders();
                }
                
                break;
        }
    }
}