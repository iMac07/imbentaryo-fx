package org.xersys.imbentaryofx.gui;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.commander.iface.LRecordMas;
import org.xersys.imbentaryofx.listener.PartsCatalogueListener;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.FXUtil;
import org.xersys.imbentaryofx.listener.FormClosingCallback;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.sales.base.JobEstimate;
import org.xersys.sales.base.JobOrder;
import org.xersys.sales.base.PartsCatalogue;
import org.xersys.sales.base.SP_Sales;
import org.xersys.sales.base.SalesOrder;

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
    private TextField txtSeeks01;
    @FXML
    private TextField txtSeeks02;
    @FXML
    private TextField txtSeeks03;
    @FXML
    private TextField txtSeeks04;
    
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
        
        txtSeeks04.setOnKeyPressed(this::txtField_KeyPressed);
        
        _trans_listener = new LRecordMas() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch(fsFieldNm){
                    case "sCategrCd":
                        txtSeeks01.setText((String) foValue); break;
                    case "sBrandCde":
                        txtSeeks02.setText((String) foValue); break;
                    case "sModelCde":
                        txtSeeks03.setText((String) foValue); break;
                    case "sSeriesID":
                        txtSeeks04.setText((String) foValue); break;
                }
            }

            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                try {
                    if (!"success".equals((String) foValue.get("result"))) return;
                    
                    foValue = (JSONObject) foValue.get("payload");
                    
                    
                    switch (foField.getId()){
                        case "txtSeeks04":
                            _trans.setMaster("sSeriesID", (String) foValue.get("sSeriesID"));
                            break;
                    }
                } catch (SQLException | ParseException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void FormClosing(TextField foField) {
                if (foField.getId().equals("txtSeeks04")){
                    foField.requestFocus();
                } else{
                    FXUtil.SetNextFocus(foField);
                }    
            }
        };
        
        _trans = new PartsCatalogue(_nautilus);
        _trans.setListener(_trans_listener);
        
        if (_trans.NewTransaction()){
            System.setProperty("shopping.cart", "");
            JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.CART);
            if (loJSON != null) _screens_dashboard_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) CommonUtil.createInstance((String) loJSON.get("controller")));
        }
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
    
    public void setFormCloseListener(FormClosingCallback foValue){
        _close_listener = foValue;
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsProcName = this.getClass().getSimpleName();
        String lsButton = ((Button) event.getSource()).getId();
        
        switch (lsButton){
            case "btnSearch":               
                if (_trans.LoadFigures()){
                    displayImages();
                } else {
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                }
                break;
            case "btn01": //add to POS
                if (!System.getProperty("shopping.cart").isEmpty()){                    
                    JSONArray loArray;
                    JSONObject loJSON;
                    JSONParser loParser = new JSONParser();
                   
                    try {
                        loArray = (JSONArray) loParser.parse(System.getProperty("shopping.cart"));
                        
                        if (loArray.size() > 0){
                            SP_Sales loSales = new SP_Sales(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
                            loSales.setSaveToDisk(true);
                            
                            if (loSales.NewTransaction()){     
                                int lnRow = 0;
                                for (int lnCtr = 0; lnCtr <= loArray.size()-1; lnCtr++){
                                    loJSON = (JSONObject) loArray.get(lnCtr);                                   
                                    
                                    loSales.setDetail(lnRow, "sStockIDx", (String) loJSON.get("sStockIDx"));
                                    loSales.setDetail(lnRow, "nQuantity", (int) (long) loJSON.get("nQuantity")); 
                                    lnRow++;
                                }
                            }
                        }
                        
                        _close_listener.FormClosing(); //inform the parent the thhis form was closing
                        _screens_dashboard_controller.unloadScreen(_screens_dashboard_controller.getCurrentScreenIndex());
                        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException on " + lsProcName, "Notice", "");
                    }                    
                } else {
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No item on shopping cart.", "Notice", "");
                }
                break;
            case "btn02": //add to JO
                if (!System.getProperty("shopping.cart").isEmpty()){                    
                    JSONArray loArray;
                    JSONObject loJSON;
                    JSONParser loParser = new JSONParser();
                   
                    try {
                        loArray = (JSONArray) loParser.parse(System.getProperty("shopping.cart"));
                        
                        if (loArray.size() > 0){
                            JobOrder loSales = new JobOrder(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
                            loSales.setSaveToDisk(true);
                            
                            if (loSales.NewTransaction()){     
                                int lnRow = 0;
                                for (int lnCtr = 0; lnCtr <= loArray.size()-1; lnCtr++){
                                    loJSON = (JSONObject) loArray.get(lnCtr);                                   
                                    
                                    loSales.setParts(lnRow, "sStockIDx", (String) loJSON.get("sStockIDx"));
                                    loSales.setParts(lnRow, "nQuantity", (int) (long) loJSON.get("nQuantity")); 
                                    lnRow++;
                                }
                            }
                        }
                        
                        _close_listener.FormClosing(); //inform the parent the thhis form was closing
                        _screens_dashboard_controller.unloadScreen(_screens_dashboard_controller.getCurrentScreenIndex());
                        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException on " + lsProcName, "Notice", "");
                    }                    
                } else {
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No item on shopping cart.", "Notice", "");
                }
                break;
            case "btn03": //add to Estimate
                if (!System.getProperty("shopping.cart").isEmpty()){                    
                    JSONArray loArray;
                    JSONObject loJSON;
                    JSONParser loParser = new JSONParser();
                   
                    try {
                        loArray = (JSONArray) loParser.parse(System.getProperty("shopping.cart"));
                        
                        if (loArray.size() > 0){
                            JobEstimate loSales = new JobEstimate(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
                            loSales.setSaveToDisk(true);
                            
                            if (loSales.NewTransaction()){     
                                int lnRow = 0;
                                for (int lnCtr = 0; lnCtr <= loArray.size()-1; lnCtr++){
                                    loJSON = (JSONObject) loArray.get(lnCtr);                                   
                                    
                                    loSales.setParts(lnRow, "sStockIDx", (String) loJSON.get("sStockIDx"));
                                    loSales.setParts(lnRow, "nQuantity", (int) (long) loJSON.get("nQuantity")); 
                                    lnRow++;
                                }
                            }
                        }
                        
                        _close_listener.FormClosing(); //inform the parent the thhis form was closing
                        _screens_dashboard_controller.unloadScreen(_screens_dashboard_controller.getCurrentScreenIndex());
                        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException on " + lsProcName, "Notice", "");
                    }                    
                } else {
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No item on shopping cart.", "Notice", "");
                }
                break;
            case "btn04": //add to CO
                if (!System.getProperty("shopping.cart").isEmpty()){                    
                    JSONArray loArray;
                    JSONObject loJSON;
                    JSONParser loParser = new JSONParser();
                   
                    try {
                        loArray = (JSONArray) loParser.parse(System.getProperty("shopping.cart"));
                        
                        if (loArray.size() > 0){
                            SalesOrder loSales = new SalesOrder(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"), false);
                            loSales.setSaveToDisk(true);
                            
                            if (loSales.NewTransaction()){     
                                int lnRow = 0;
                                for (int lnCtr = 0; lnCtr <= loArray.size()-1; lnCtr++){
                                    loJSON = (JSONObject) loArray.get(lnCtr);                                   
                                    
                                    loSales.setDetail(lnRow, "sStockIDx", (String) loJSON.get("sStockIDx"));
                                    loSales.setDetail(lnRow, "nQuantity", (int) (long) loJSON.get("nQuantity")); 
                                    lnRow++;
                                }
                            }
                        }
                        
                        _close_listener.FormClosing(); //inform the parent the thhis form was closing
                        _screens_dashboard_controller.unloadScreen(_screens_dashboard_controller.getCurrentScreenIndex());
                        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException on " + lsProcName, "Notice", "");
                    }                    
                } else {
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), "No item on shopping cart.", "Notice", "");
                }
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
                System.setProperty("shopping.cart", "");
                if (_screens_controller.getScreenCount() > 1){
                    _screens_dashboard_controller.unloadScreen(_screens_dashboard_controller.getCurrentScreenIndex());
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                } else{
                    if (ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to exit the application?", "Please confirm", ""))
                        System.exit(0);
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
        btnSearch.setOnAction(this::cmdButton_Click);
        
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
        btn03.setText("To JEst");
        btn04.setText("To CO");
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
    }
    
    private void displayImages(){
        _listener = new PartsCatalogueListener() {
            @Override
            public void onClickListener(JSONObject foValue) {
                PartsCatalogueDetailController instance = new PartsCatalogueDetailController();
                instance.setData(foValue);
                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);
                instance.setDashboardScreensController(_screens_dashboard_controller);
                
                _screens_controller.loadScreen("PartsCatalogueDetail.fxml", (ControlledScreen) instance);
            }
        };
        
        int column = 0;
        int row = 1;
        
        try {
            for (int lnCtr = 1; lnCtr <= _trans.getFigureCount(); lnCtr ++){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("PartsCatalogueChild.fxml"));

                PartsCatalogueChildController controller = new PartsCatalogueChildController();
                controller.setData(_listener);
                controller.setImagePath((String) _nautilus.getAppConfig("sApplPath") +  _trans.getFigure(lnCtr, "sImageNme"));
                controller.setBlockNo("");
                controller.setBlockTitle((String) _trans.getFigure(lnCtr, "sDescript"));
                controller.setAddressNo("");
                controller.setParts(_trans.getFigureParts(lnCtr));

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
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    
    private void searchSeries(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON = _trans.searchSeries(fsKey, foValue, fbExact);
        
        if ("success".equals((String) loJSON.get("result"))){            
            JSONParser loParser = new JSONParser();
            
            try {
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                switch (loArray.size()){
                    case 1: //one record found
                        txtSeeks01.setText((String) loJSON.get("sDescript"));
                        FXUtil.SetNextFocus(txtSeeks04);
                        break;
                    default: //multiple records found
                        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                        if (loScreen != null){
                            QuickSearchNeoController instance = new QuickSearchNeoController();
                            instance.setNautilus(_nautilus);
                            instance.setParentController(_main_screen_controller);
                            instance.setScreensController(_screens_controller);

                            instance.setSearchObject(_trans.getSearchSeries());
                            instance.setSearchCallback(_search_callback);
                            instance.setTextField(txtSeeks04);

                            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                        }
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "ParseException detected.", "Warning", "");
                txtSeeks04.setText("");
                FXUtil.SetNextFocus(txtSeeks04);
            }
        } else {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), (String) loJSON.get("message"), "Warning", "");
            txtSeeks04.setText("");
            FXUtil.SetNextFocus(txtSeeks04);
        }
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.F3){
            switch (lsTxt){
                case "txtSeeks04":
                    searchSeries("sSeriesID", lsValue, false);
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
    
    private XNautilus _nautilus;
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    
    private PartsCatalogue _trans;
    private LRecordMas _trans_listener;
    private PartsCatalogueListener _listener;
    private FormClosingCallback _close_listener;
    private QuickSearchCallback _search_callback;
    
    private int _max_grid_column;

}