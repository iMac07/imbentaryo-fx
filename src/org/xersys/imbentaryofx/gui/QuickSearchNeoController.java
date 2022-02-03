package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.iface.iSearch;
import org.xersys.imbentaryofx.listener.FormClosingCallback;

public class QuickSearchNeoController implements Initializable, ControlledScreen {
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
    private TableView table;
    @FXML
    private TextField txtSeeks01;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (_trans == null){
            System.err.println("UNSET Search Object.");
            _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
            return;
        }
        
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);   
        
        txtSeeks01.setOnKeyPressed(this::keyPressed);
        txtSeeks01.setOnKeyReleased(this::keyReleased);
        
        table.setOnMouseClicked(this::mouseClicked);
        
        _filter_closing = new FormClosingCallback() {
            @Override
            public void FormClosing() {
                System.out.println(_trans);
            }
        };
        
        initButton();
        initGrid();
        loadDetail();
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
    
    public void setSearchObject(iSearch foValue){
        _trans = foValue;
    }
    
    public void setSearchCallback(QuickSearchCallback foValue){
        _search_callback = foValue;
    }
    
    public void setTextField(TextField foValue){
        _text_field = foValue;
    }
    
    public void setAddRecord(boolean fbValue){
        _allow_create = fbValue;
    }
    
    private void loadDetail(){        
        if (_trans == null){
            System.err.println("UNSET Search Object.");
            _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
            return;
        }
        
        JSONObject loJSON = _trans.Search();
        
        _data.clear();
        
        if ("success".equals((String) loJSON.get("result"))){          
            
            try {
                JSONParser loParser = new JSONParser();
                JSONArray loArray = (JSONArray) loParser.parse((String) loJSON.get("payload"));
                
                ArrayList laFields = _trans.getColumns();
                
                //duplicate the result to class variable for loading
                _json = loJSON;
                
                for (Object obj : loArray){
                    loJSON = (JSONObject) obj;

                    _data.add(new TableModel(laFields.size() <= 0 ? "" : String.valueOf(loJSON.get(laFields.get(0))), 
                                            laFields.size() <= 1 ? "" : String.valueOf(loJSON.get(laFields.get(1))),
                                            laFields.size() <= 2 ? "" : String.valueOf(loJSON.get(laFields.get(2))),
                                            laFields.size() <= 3 ? "" : String.valueOf(loJSON.get(laFields.get(3))),
                                            laFields.size() <= 4 ? "" : String.valueOf(loJSON.get(laFields.get(4))),
                                            laFields.size() <= 5 ? "" : String.valueOf(loJSON.get(laFields.get(5))),
                                            laFields.size() <= 6 ? "" : String.valueOf(loJSON.get(laFields.get(6))),
                                            laFields.size() <= 7 ? "" : String.valueOf(loJSON.get(laFields.get(7))),
                                            laFields.size() <= 8 ? "" : String.valueOf(loJSON.get(laFields.get(8))),
                                            laFields.size() <= 9 ? "" : String.valueOf(loJSON.get(laFields.get(9)))));
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
            }
        }
        
        table.getSelectionModel().selectFirst();
        pnSelectd = table.getSelectionModel().getSelectedIndex();
        
        txtSeeks01.setText(String.valueOf(_trans.getValue()));
        txtSeeks01.requestFocus();
        txtSeeks01.end();
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("");
        TableColumn index02 = new TableColumn("");
        TableColumn index03 = new TableColumn("");
        TableColumn index04 = new TableColumn("");
        TableColumn index05 = new TableColumn("");
        TableColumn index06 = new TableColumn("");
        TableColumn index07 = new TableColumn("");
        TableColumn index08 = new TableColumn("");
        TableColumn index09 = new TableColumn("");
        TableColumn index10 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(true);
        index05.setSortable(false); index05.setResizable(true);
        index06.setSortable(false); index06.setResizable(true);
        index07.setSortable(false); index07.setResizable(true);
        index08.setSortable(false); index08.setResizable(true);
        index09.setSortable(false); index09.setResizable(true);
        index10.setSortable(false); index10.setResizable(true);
        
        table.getColumns().clear();        
        
        ArrayList laFields = _trans.getColumnNames();
        
        for(int lnCtr = 1; lnCtr <= laFields.size(); lnCtr++){
            switch (lnCtr){
                case 1: 
                    index01.setText((String) laFields.get(lnCtr -1)); table.getColumns().add(index01);
                    index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
                    
                    switch (laFields.size()){
                        case 1:
                            index01.prefWidthProperty().bind(table.widthProperty().multiply(0.995)); break;
                        case 2:
                        case 3:
                        case 4:
                            index01.prefWidthProperty().bind(table.widthProperty().multiply(0.245)); break;
                        default:
                            index01.prefWidthProperty().bind(table.widthProperty().multiply(0.195));
                    }
                    break;
                case 2: 
                    index02.setText((String) laFields.get(lnCtr -1)); table.getColumns().add(index02);
                    index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
                    
                    switch (laFields.size()){
                        case 2:
                            index02.prefWidthProperty().bind(table.widthProperty().multiply(0.75)); break;
                        case 3:
                            index02.prefWidthProperty().bind(table.widthProperty().multiply(0.50)); break;
                        case 4:
                            index02.prefWidthProperty().bind(table.widthProperty().multiply(0.40)); break;
                        default:
                            index02.prefWidthProperty().bind(table.widthProperty().multiply(0.35));
                    }                    
                    break;
                case 3: 
                    index03.setText((String) laFields.get(lnCtr -1)); table.getColumns().add(index03);
                    index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
                    
                    switch (laFields.size()){
                        case 3:
                            index03.prefWidthProperty().bind(table.widthProperty().multiply(0.25)); break;
                        case 4:
                            index03.prefWidthProperty().bind(table.widthProperty().multiply(0.20)); break;
                        default:
                            index03.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    }
                    break;
                case 4:
                    index04.setText((String) laFields.get(lnCtr -1)); table.getColumns().add(index04);
                    index04.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
                    index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
                    
                    switch (laFields.size()){
                        case 4:
                            index04.prefWidthProperty().bind(table.widthProperty().multiply(0.15)); break;
                        default:
                            index04.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    }
                    break;
                case 5: 
                    index05.setText((String) laFields.get(lnCtr -1)); table.getColumns().add(index05);
                    index05.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05")); 
                    break;
                case 6: 
                    index06.setText((String) laFields.get(lnCtr -1)); table.getColumns().add(index06);
                    index06.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06")); 
                    break;
                case 7: 
                    index07.setText((String) laFields.get(lnCtr -1)); table.getColumns().add(index07);
                    index07.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index07")); 
                    break;
                case 8: 
                    index08.setText((String) laFields.get(lnCtr -1)); table.getColumns().add(index08);
                    index08.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    index08.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index08")); 
                    break;
                case 9: 
                    index09.setText((String) laFields.get(lnCtr -1)); table.getColumns().add(index09);
                    index09.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    index09.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index09")); 
                    break;
                case 10: 
                    index10.setText((String) laFields.get(lnCtr -1)); table.getColumns().add(index10);
                    index10.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    index10.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index10")); 
                    break;
                default:
                    System.err.println("Columns exceeds the developer's max search limit.");
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                    return;
            }
            
            table.setItems(_data);
        }
    }
    
    private void keyReleased(KeyEvent event) {
        if (null != event.getCode())switch (event.getCode()) {
            case CONTROL:
                _control_pressed = false;
                return;
            case DOWN:
            case UP:
                return;
        }
        
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
        
        switch (lsTxt){
            case "txtSeeks01":
                _trans.setValue(lsValue);                
                loadDetail();
                break;
        }
    }
    
    private void keyPressed(KeyEvent event) {       
        if (null != event.getCode())switch (event.getCode()) {
            case CONTROL:
                _control_pressed = true;
                return;
            case DOWN:
                if(_control_pressed){
                    table.getSelectionModel().selectNext();
                    pnSelectd = table.getSelectionModel().getSelectedIndex();
                }
                return;
            case UP:
                if(_control_pressed){
                    table.getSelectionModel().selectPrevious();
                    pnSelectd = table.getSelectionModel().getSelectedIndex();
                }
                return;
            default:
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
        
        btn01.setText("Load");
        btn02.setText("Filter");
        btn03.setText("");
        btn04.setText("");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("");
        btn11.setText("Add");
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
        btn11.setVisible(_allow_create);
        btn12.setVisible(true);
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        JSONObject loJSON;
        
        switch (lsButton){
            case "btn01": //load
                loadData();
                break;
            case "btn02": //filter
                loadFilter();
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
            case "btn11": //add record
                loJSON = new JSONObject();
                loJSON.put("result", "success");
                loJSON.put("payload", null);
                
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                _search_callback.Result(_text_field, loJSON);
                break;
            case "btn12": //close screen
                loJSON = new JSONObject();
                loJSON.put("result", "error");
                loJSON.put("message", "No record to load.");
                _search_callback.Result(_text_field, loJSON);
                
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                _search_callback.FormClosing(_text_field);
                break;
        }
    }

    private void loadFilter(){
        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH_FILTER);

        if (loScreen != null){
            QuickSearchFilterController instance = new QuickSearchFilterController();
            instance.setNautilus(null);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            instance.setSearchObject(_trans);
            instance.setFormClosing(_filter_closing);

            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
        }
    }
    
    private void loadData(){
        try {
            JSONObject loJSON;
            JSONParser loParser = new JSONParser();
            JSONArray loArray = (JSONArray) loParser.parse((String) _json.get("payload"));
            
            if (loArray.size() > 0){
                loJSON = new JSONObject();
                loJSON.put("result", "success");
                loJSON.put("payload", (JSONObject) loArray.get(pnSelectd));
                _search_callback.Result(_text_field, loJSON);
            } else {
                loJSON = new JSONObject();
                loJSON.put("result", "error");
                loJSON.put("message", "No record to load.");
                _search_callback.Result(_text_field, loJSON);
            }    

            //load the data
            _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
            _search_callback.FormClosing(_text_field);
        } catch (ParseException ex) {
            Logger.getLogger(QuickSearchNeoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void mouseClicked(MouseEvent event) {
        pnSelectd = table.getSelectionModel().getSelectedIndex();
        
        if (event.getClickCount() >= 2){
            loadData();
        }
    }
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private QuickSearchCallback _search_callback;
    private FormClosingCallback _filter_closing;
    
    private ObservableList<TableModel> _data = FXCollections.observableArrayList();
    private JSONObject _json;
    
    private int pnSelectd = -1;
    
    private iSearch _trans;
    
    private TextField _text_field;
    private boolean _control_pressed;
    private boolean _allow_create = false;
    
}
