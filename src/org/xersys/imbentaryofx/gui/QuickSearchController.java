package org.xersys.imbentaryofx.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.kumander.contants.SearchEnum;
import org.xersys.kumander.iface.XMasDetTrans;
import org.xersys.kumander.iface.XNautilus;

public class QuickSearchController implements Initializable, ControlledScreen {
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
    private TableView table;
    @FXML
    private TextField txtSeeks01;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (_json == null){
            System.err.println("No initial search result was passed.");
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
    
    public void setTransObject(XMasDetTrans foValue){
        _trans = foValue;
    }
    
    public void setSearchType(SearchEnum.Type foType){
        _type = foType;
    }
    
    public void setSearchValue(String fsValue){
        _value = fsValue;
    }
    
    public void setSearchKey(String fsValue){
        _key = fsValue;
    }
    
    public void setSearchFilter(String fsValue){
        _filter = fsValue;
    }
    
    public void setSearchMaxRow(int fnValue){
        _maxrow = fnValue;
    }
    
    public void setSearchExact(boolean fbValue){
        _exact = fbValue;
    }
    
    public void setSearchCallback(QuickSearchCallback foValue){
        _search_callback = foValue;
    }
    
    public void setSearchResult(JSONObject foValue){
        _json = foValue;
    }
    
    public void setTextField(TextField foField){
        _text_field = foField;
    }
    
    private void loadDetail(){
        JSONObject loJSON;
        
        if (_json == null){
            System.err.println("No initial search result was passed.");
            _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
            return;
        }
        
        _data.clear();
        
        JSONArray loArray = (JSONArray) _json.get("payload");
        String lsHeader = (String) _json.get("headers");
        String lsColNme = (String) _json.get("colname");
        
        String [] lasHeader = lsHeader.split("»");
        String [] lasColNme = lsColNme.split("»");
        
        for (Object obj : loArray){
            loJSON = (JSONObject) obj;
            
            _data.add(new TableModel((lasHeader.length <= 0 ? "" : String.valueOf(loJSON.get(lasColNme[0]))), 
                                    (lasHeader.length <= 1 ? "" : String.valueOf(loJSON.get(lasColNme[1]))), 
                                    (lasHeader.length <= 2 ? "" : String.valueOf(loJSON.get(lasColNme[2]))), 
                                    (lasHeader.length <= 3 ? "" : String.valueOf(loJSON.get(lasColNme[3]))), 
                                    (lasHeader.length <= 4 ? "" : String.valueOf(loJSON.get(lasColNme[4]))),
                                    (lasHeader.length <= 5 ? "" : String.valueOf(loJSON.get(lasColNme[5]))), 
                                    (lasHeader.length <= 6 ? "" : String.valueOf(loJSON.get(lasColNme[6]))), 
                                    (lasHeader.length <= 7 ? "" : String.valueOf(loJSON.get(lasColNme[7]))), 
                                    (lasHeader.length <= 8 ? "" : String.valueOf(loJSON.get(lasColNme[8]))),
                                    (lasHeader.length <= 9 ? "" : String.valueOf(loJSON.get(lasColNme[9])))));
        }
        
        table.getSelectionModel().selectFirst();
        pnSelectd = table.getSelectionModel().getSelectedIndex();
        
        txtSeeks01.setText(_value);
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
        
        String lsHeader = (String) _json.get("headers");
        String [] lasHeader = lsHeader.split("»");
        
        for(int lnCtr = 1; lnCtr <= lasHeader.length; lnCtr++){
            switch (lnCtr){
                case 1: 
                    index01.setText(lasHeader[lnCtr -1]); table.getColumns().add(index01);
                    index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
                    
                    switch (lasHeader.length){
                        case 1:
                            index01.prefWidthProperty().bind(table.widthProperty().multiply(1)); break;
                        case 2:
                        case 3:
                        case 4:
                            index01.prefWidthProperty().bind(table.widthProperty().multiply(0.25)); break;
                        default:
                            index01.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
                    }
                    break;
                case 2: 
                    index02.setText(lasHeader[lnCtr -1]); table.getColumns().add(index02);
                    index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
                    
                    switch (lasHeader.length){
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
                    index03.setText(lasHeader[lnCtr -1]); table.getColumns().add(index03);
                    index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
                    
                    switch (lasHeader.length){
                        case 3:
                            index03.prefWidthProperty().bind(table.widthProperty().multiply(0.25)); break;
                        case 4:
                            index03.prefWidthProperty().bind(table.widthProperty().multiply(0.20)); break;
                        default:
                            index03.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    }
                    break;
                case 4:
                    index04.setText(lasHeader[lnCtr -1]); table.getColumns().add(index04);
                    index04.prefWidthProperty().bind(table.widthProperty().multiply(0.20));
                    index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
                    
                    switch (lasHeader.length){
                        case 4:
                            index04.prefWidthProperty().bind(table.widthProperty().multiply(0.15)); break;
                        default:
                            index04.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    }
                    break;
                case 5: 
                    index05.setText(lasHeader[lnCtr -1]); table.getColumns().add(index05);
                    index05.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05")); 
                    break;
                case 6: 
                    index06.setText(lasHeader[lnCtr -1]); table.getColumns().add(index06);
                    index06.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06")); 
                    break;
                case 7: 
                    index07.setText(lasHeader[lnCtr -1]); table.getColumns().add(index07);
                    index07.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index07")); 
                    break;
                case 8: 
                    index08.setText(lasHeader[lnCtr -1]); table.getColumns().add(index08);
                    index08.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    index08.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index08")); 
                    break;
                case 9: 
                    index09.setText(lasHeader[lnCtr -1]); table.getColumns().add(index09);
                    index09.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
                    index09.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index09")); 
                    break;
                case 10: 
                    index10.setText(lasHeader[lnCtr -1]); table.getColumns().add(index10);
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
        
        JSONObject loJSON;
        
        switch (lsTxt){
            case "txtSeeks01":
                _value = lsValue;
                _json = _trans.Search(_type, _value, _key, _filter, _maxrow, _exact);
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
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //load
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
                _search_callback.FormClosing();
                break;
        }
    }

    
    private void loadData(){
        JSONArray loArray = (JSONArray) _json.get("payload");
        
        if (loArray.size() > 0)
            _search_callback.Result(_text_field, (JSONObject) loArray.get(pnSelectd));

        //load the data
        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
        _search_callback.FormClosing();
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
    
    private ObservableList<TableModel> _data = FXCollections.observableArrayList();
    private TableModel _model;
    private JSONObject _json;
    
    private int pnSelectd = -1;
    
    private XMasDetTrans _trans;

    private SearchEnum.Type _type;
    private String _value;
    private String _key;
    private String _filter;
    private int _maxrow;
    private boolean _exact;
    
    private TextField _text_field;
    private boolean _control_pressed;
}
