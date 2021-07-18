package org.xersys.imbentaryofx.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreenInfo;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.PartsCatalogueListener;
import org.xersys.kumander.base.Nautilus;
import org.xersys.kumander.iface.XNautilus;
import org.xersys.kumander.util.CommonUtil;

public class PartsCatalogueController implements Initializable, ControlledScreen{
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private ScrollPane scroll;
    @FXML
    private GridPane grid;
    @FXML
    private HBox HBoxSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private AnchorPane btnOther01;
    @FXML
    private AnchorPane btnOther02;
    @FXML
    private AnchorPane btnOther03;
    @FXML
    private AnchorPane btnOther04;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);
        
        btnOther01.setOnMouseClicked(this::cmdMouse_Click);
        btnOther02.setOnMouseClicked(this::cmdMouse_Click);
        btnOther03.setOnMouseClicked(this::cmdMouse_Click);
        btnOther04.setOnMouseClicked(this::cmdMouse_Click);
        
        //initialize grid
        initGrid();
        
        JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.CART);
                
        if (loJSON != null) _screens_dashboard_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) CommonUtil.createInstance((String) loJSON.get("controller")));
        
        displayImages();
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
        _screens_dashboard_controller = foValue;
    }
    
    private void cmdMouse_Click(MouseEvent event) {
        String lsButton = ((AnchorPane) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch(lsButton){
            case "btnOther01": //add to POS
                break;
            case "btnOther02": //add to JO
                break;
            case "btnOther03": //add to CO
                break;
            case "btnOther04": //exit window
                _screens_dashboard_controller.unloadScreen(_screens_dashboard_controller.getCurrentScreenIndex());
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                break;
        }
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
    }
    
    private void initGrid(){
        grid.getChildren().clear();
        
        if (Double.valueOf(System.getProperty("system.screen.width")) >= 1920.0) //1920 x 1080
            _max_grid_column = 4;
        else if (Double.valueOf(System.getProperty("system.screen.width")) >= 1366.0) //1366 x 768
            _max_grid_column = 3;
        else
            _max_grid_column = 1;
        
    }
    
    private void displayImages(){
        _listener = new PartsCatalogueListener() {
            @Override
            public void onClickListener() {
                PartsCatalogueDetailController instance = new PartsCatalogueDetailController();
                instance.setData();
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);
                instance.setDashboardScreensController(_screens_dashboard_controller);
                
                _screens_controller.loadScreen("../PartsCatalogueDetail.fxml", (ControlledScreen) instance);
                
            }
        };
        
        int column = 0;
        int row = 1;
        
        try {
            for (int i = 0; i < 3; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("PartsCatalogueChild.fxml"));
                
                PartsCatalogueChildController controller = new PartsCatalogueChildController();
                controller.setData(_listener);
                controller.setImagePath("org/xersys/imbentaryo/images/e-" + (i+1) + ".png");
                
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

                GridPane.setMargin(anchorPane, new Insets(15));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private XNautilus _nautilus;
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private PartsCatalogueListener _listener;
    
    private int _max_grid_column;

}