package org.xersys.imbentaryofx.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.CartCallback;
import org.xersys.kumander.base.Nautilus;
import org.xersys.kumander.iface.XNautilus;
import org.xersys.kumander.util.StringUtil;

public class CartController implements Initializable, ControlledScreen {
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private ScrollPane scroll;
    @FXML
    private GridPane grid;
    @FXML
    private HBox HBoxSummary;
    @FXML
    private Label lblTotalItems;
    @FXML
    private Label lblTotalAmount;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);
        
        displayOrders();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void displayOrders(){
        grid.getChildren().clear();
        _max_grid_column = 1;
        
        _total_items = 0;
        _total_amount = 0.00;
        
        String lsOrders = "[{\"nQtyOrder\":1,\"sDescript\":\"Item 1\",\"nQtyOnHnd\":50,\"nUnitPrce\":100.0,\"sBarrCode\":\"000-000-000-01\",\"sOtherInf\":\"Item 1 other info\"},{\"nQtyOrder\":2,\"sDescript\":\"Item 2\",\"nQtyOnHnd\":100,\"nUnitPrce\":200.0,\"sBarrCode\":\"000-000-000-02\",\"sOtherInf\":\"Item 2 other info\"},{\"nQtyOrder\":3,\"sDescript\":\"Item 3\",\"nQtyOnHnd\":150,\"nUnitPrce\":300.0,\"sBarrCode\":\"000-000-000-03\",\"sOtherInf\":\"Item 3 other info\"}]";
        
        int column = 0;
        int row = 1;
        
        try {
            JSONObject loJSON;
            JSONParser loParser = new JSONParser();
            JSONArray loArr = (JSONArray) loParser.parse(lsOrders);
        
            _callback = new CartCallback() {
                @Override
                public void QtyChanged(int fnQtyOrder, double fnUnitPrice) {
                    _total_items += fnQtyOrder;
                    _total_amount += fnQtyOrder * fnUnitPrice;
                    
                    lblTotalItems.setText(String.valueOf(_total_items));
                    lblTotalAmount.setText(StringUtil.NumberFormat(_total_amount, "#,##0.00"));
                }
            };

            for (int i = 0; i < loArr.size(); i++) {
                loJSON = (JSONObject) loArr.get(i);
                
                if (loJSON != null){
                    _total_items += (int) (long) loJSON.get("nQtyOrder");
                    _total_amount += (int) (long) loJSON.get("nQtyOrder") * (double) loJSON.get("nUnitPrce");
                    
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("CartChild.fxml"));

                    CartChildController controller = new CartChildController();
                    controller.setCallback(_callback);
                    controller.setData(loJSON.toJSONString());

                    fxmlLoader.setController(controller);

                    AnchorPane anchorPane = fxmlLoader.load();

                    if (column == _max_grid_column) {
                        column = 0;
                        row++;
                    }

                    grid.add(anchorPane, column++, row); //(child,column,row)
                    //set grid width
                    grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                    grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    grid.setMaxWidth(Region.USE_PREF_SIZE);

                    //set grid height
                    grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                    grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    grid.setMaxHeight(Region.USE_PREF_SIZE);

                    GridPane.setMargin(anchorPane, new Insets(5));
                }
            }
            
            lblTotalItems.setText(String.valueOf(_total_items));
            lblTotalAmount.setText(StringUtil.NumberFormat(_total_amount, "#,##0.00"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    
    private XNautilus _nautilus;
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private CartCallback _callback;

    private int _max_grid_column;
    private int _total_items;
    private double _total_amount;
}
