package org.xersys.imbentaryofx.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import org.xersys.commander.contants.EditMode;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreenInfo;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.PartsCatalogueListener;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.MsgBox;

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
    private VBox btnbox00;
    @FXML
    private HBox btnbox01;
    @FXML
    private Button btn01;
    @FXML
    private FontAwesomeIconView glyph01;
    @FXML
    private Button btn02;
    @FXML
    private FontAwesomeIconView glyph02;
    @FXML
    private Button btn03;
    @FXML
    private FontAwesomeIconView glyph03;
    @FXML
    private Button btn04;
    @FXML
    private FontAwesomeIconView glyph04;
    @FXML
    private Button btn05;
    @FXML
    private FontAwesomeIconView glyph05;
    @FXML
    private Button btn06;
    @FXML
    private FontAwesomeIconView glyph06;
    @FXML
    private Button btn07;
    @FXML
    private FontAwesomeIconView glyph07;
    @FXML
    private Button btn08;
    @FXML
    private FontAwesomeIconView glyph08;
    @FXML
    private Button btn09;
    @FXML
    private FontAwesomeIconView glyph09;
    @FXML
    private Button btn10;
    @FXML
    private FontAwesomeIconView glyph10;
    @FXML
    private Button btn11;
    @FXML
    private FontAwesomeIconView glyph11;
    @FXML
    private Button btn12;
    @FXML
    private FontAwesomeIconView glyph12;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);
        
        //initialize grid
        initGrid();
        initButton();
        
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
        
        switch (lsButton){
            case "btn01":
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
                if (_screens_controller.getScreenCount() > 1)
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                else{
                    if (MsgBox.showOkCancel("This action will exit the application.", "Please confirm...") == MsgBox.RESP_YES_OK){
                        System.exit(0);
                    }
                }
                break;
        }
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
        
        btn01.setText("To POS");
        btn02.setText("To JO");
        btn03.setText("To CO");
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
        btn03.setVisible(true);
        btn04.setVisible(false);
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
                controller.setImagePath("org/xersys/imbentaryofx/images/e-" + (i+1) + ".png");
                
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