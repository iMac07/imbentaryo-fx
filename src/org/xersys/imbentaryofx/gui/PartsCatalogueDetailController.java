package org.xersys.imbentaryofx.gui;

import javafx.scene.control.CheckBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xersys.commander.iface.XNautilus;

public class PartsCatalogueDetailController implements Initializable, ControlledScreen{
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private ImageView image;
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
    private TableView _table;
    @FXML
    private Label lblBlockTitle;
    @FXML
    private ScrollPane scroll;
    @FXML
    private BorderPane border;
    @FXML
    private GridPane grid;
    @FXML
    private StackPane imageHolder;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);
        
        initButton();
        initGrid();
        
        if (_data != null){
            try {            
                FileInputStream inputstream = new FileInputStream((String) _data.get("path"));
                
                image.setPreserveRatio(true);
                
                if (Double.valueOf(System.getProperty("system.screen.height")) >= 1050.0){
                    image.setFitWidth(1200.00);
                    image.setFitHeight(1200.00);
                } else {
                    image.setFitWidth(600.00);
                    image.setFitHeight(600.00);
                }
                
                image.setImage(new Image(inputstream));
                
                final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
                zoomProperty.addListener((Observable arg0) -> {
                    image.setFitWidth(zoomProperty.get() * 4);
                    image.setFitHeight(zoomProperty.get() * 3);
                });

                imageHolder.minWidthProperty().bind(Bindings.createDoubleBinding(() -> 
                    scroll.getViewportBounds().getWidth(), scroll.viewportBoundsProperty()));
                
                grid.setAlignment(Pos.CENTER);
        
                scroll.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        if (event.getDeltaY() > 0) {
                            zoomProperty.set(zoomProperty.get() * 1.1);
                        } else if (event.getDeltaY() < 0) {
                            zoomProperty.set(zoomProperty.get() / 1.1);
                        }
                    }
                });
                
                lblBlockTitle.setText((String) _data.get("title"));
                _parts = (JSONArray) _data.get("parts");
                
                if (_parts != null) loadData();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
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
    
    public void setData(JSONObject foValue){
        _data = foValue;
    }
    
    private void addToCart(){
        CheckBox loCheck;
        JSONArray loArray = new JSONArray();
        
        for (int lnCtr = 0; lnCtr <= _parts.size()-1; lnCtr++){
            loCheck = _table_data.get(lnCtr).getIndex07();
            if (loCheck.isSelected()){
                loArray.add(_parts.get(lnCtr));
            }
        }
        
        if (loArray.size() > 0) {
            JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.CART);
            Cart2Controller instance = new Cart2Controller();
            instance.setData(loArray);
            if (loJSON != null) {
                _screens_dashboard_controller.unloadScreen(_screens_dashboard_controller.getCurrentScreenIndex());
                _screens_dashboard_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) instance);
            }
        }
        
        loadData();
    }
    
    private void selectAll(){        
        CheckBox loCheck = new CheckBox();
        loCheck.setSelected(true);
        for (int lnCtr = 0; lnCtr <= _parts.size()-1; lnCtr++){
            _table_data.get(lnCtr).setIndex07(loCheck);
            _table.refresh();
        }
    }
        
    private void loadData(){
        JSONObject loJSON;
        
        _table_data.clear();
        for (int lnCtr = 0; lnCtr <= _parts.size()-1; lnCtr++){
            loJSON = (JSONObject) _parts.get(lnCtr);
            
            _table_data.add(new TableCatalogParts(
                                String.valueOf(loJSON.get("nEntryNox")), 
                                (String) loJSON.get("sBarCodex"), 
                                (String) loJSON.get("sDescript"), 
                                String.valueOf(loJSON.get("nQtyOnHnd")), 
                                String.valueOf(loJSON.get("nQuantity")),
                                String.valueOf(loJSON.get("sSeriesDs"))));
        }
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("");
        TableColumn index02 = new TableColumn("");
        TableColumn index03 = new TableColumn("");
        TableColumn index04 = new TableColumn("");
        TableColumn index05 = new TableColumn("");
        TableColumn index06 = new TableColumn("");
        TableColumn index07 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false); index04.setStyle( "-fx-alignment: CENTER;");
        index05.setSortable(false); index05.setResizable(false); index05.setStyle( "-fx-alignment: CENTER;");
        index06.setSortable(false); index06.setResizable(false); index06.setStyle( "-fx-alignment: CENTER;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle( "-fx-alignment: CENTER;"); index07.setEditable(true);
        
        _table.getColumns().clear();        
        
        index01.setText("Ref"); 
        index01.setCellValueFactory(new PropertyValueFactory<TableCatalogParts,String>("index01"));
        index01.prefWidthProperty().set(30);
        
        index02.setText("Part Number"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableCatalogParts,String>("index02"));
        index02.prefWidthProperty().set(150);
        
        index03.setText("Description"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableCatalogParts,String>("index03"));
        index03.prefWidthProperty().set(200);
        
        index04.setText("QOH"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableCatalogParts,String>("index04"));
        index04.prefWidthProperty().set(100);
        
        index05.setText("ROQ"); 
        index05.setCellValueFactory(new PropertyValueFactory<TableCatalogParts,String>("index05"));
        index05.prefWidthProperty().set(100);
        
        index06.setText("Series"); 
        index06.setCellValueFactory(new PropertyValueFactory<TableCatalogParts,String>("index06"));
        index06.prefWidthProperty().set(150);
        
        index07.setText("Select"); 
        index07.setCellValueFactory(new PropertyValueFactory<TableCatalogParts,Boolean>("index07"));
        index07.prefWidthProperty().set(100);
        
        _table.getColumns().add(index01);
        _table.getColumns().add(index02);
        _table.getColumns().add(index03);
        _table.getColumns().add(index04);
        _table.getColumns().add(index05);
        _table.getColumns().add(index06);
        _table.getColumns().add(index07);
 
        _table.setItems(_table_data);
        //_table.setOnMouseClicked(this::tableClicked);
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //add to cart
                addToCart();
                break;
            case "btn02": //select all
                selectAll();
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
                    if (ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to exit the application?", "Please confirm", ""))
                        System.exit(0);
                }
                break;
        }
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
        
        btn01.setText("To Cart");
        btn02.setText("Select All");
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
    
    private XNautilus _nautilus;
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller; 
    
    private JSONObject _data = null;
    private JSONArray _parts = null;
    private ObservableList<TableCatalogParts> _table_data = FXCollections.observableArrayList();
}
