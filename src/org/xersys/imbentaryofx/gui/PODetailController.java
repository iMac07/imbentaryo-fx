package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.listener.DetailUpdateCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.StringUtil;

public class PODetailController implements Initializable, ControlledScreen  {
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private DetailUpdateCallback _callback;
    private boolean _loaded = false;
    
    private int _row;
    private String _part_number;
    private String _description;
    private int _roq;
    private int _order;
    private int _on_hand;
    private double _srp;
    private JSONArray _history;
    
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    
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
    private Label lblTotal;
    @FXML
    private TableView _table;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0); 
        
        initButton();
        initFields();
        initGrid();
        
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
    
    public void setOtherInfo(int fnValue){
        _roq = fnValue;
    }
    
    public void setSellingPrice(double fnValue){
        _srp = fnValue;
    }
    
    public void setHistory(JSONArray foValue){
        _history = foValue;
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("");
        TableColumn index02 = new TableColumn("");
        TableColumn index03 = new TableColumn("");
        TableColumn index04 = new TableColumn("");
        TableColumn index05 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false); index01.setStyle( "-fx-alignment: CENTER;");
        index02.setSortable(false); index02.setResizable(false); index02.setStyle( "-fx-alignment: CENTER;");
        index03.setSortable(false); index03.setResizable(false); index03.setStyle( "-fx-alignment: CENTER;");
        index04.setSortable(false); index04.setResizable(false); index04.setStyle( "-fx-alignment: CENTER;");
        index05.setSortable(false); index04.setResizable(false); index05.setStyle( "-fx-alignment: CENTER;");
        
        _table.getColumns().clear();
        
        index01.setText("MONTH"); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(99);
        
        index02.setText("CLASS"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(99);
        
        index03.setText("AMC"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(99);
        
        index04.setText("MIN LVL"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(99);
        
        index05.setText("MAX LVL"); 
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(99);
        
        _table.getColumns().add(index01);
        _table.getColumns().add(index02);
        _table.getColumns().add(index03);
        _table.getColumns().add(index04);
        _table.getColumns().add(index05);
        
        _table.setItems(_table_data);
        
        JSONObject loJSON;
        for(int lnCtr = 1; lnCtr <= 6; lnCtr++){  
            if (lnCtr <= _history.size()){
                loJSON = (JSONObject) _history.get(lnCtr - 1);
                
                _table_data.add(new TableModel(
                            CommonUtil.getMonth(Integer.parseInt(String.valueOf(loJSON.get("sPeriodxx")).substring(4))).toUpperCase(), 
                            (String) loJSON.get("cClassify"),
                            String.valueOf(loJSON.get("nAvgMonSl")),
                            String.valueOf(loJSON.get("nMinLevel")),
                            String.valueOf(loJSON.get("nMaxLevel")),
                            "",
                            "",
                            "",
                            "",
                            ""));
            } else {
                _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""));
            }
            
            
            
            
            
        }
        
        if (!_table_data.isEmpty()){
            _table.getSelectionModel().select(0);
            _table.getFocusModel().focus(0); 
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
        
        btn01.setText("Okay");
        btn02.setText("Remove");
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
        btn02.setVisible(true);
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
    
    private void initFields(){
        txtDetail01.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail02.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail03.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail04.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail05.setOnKeyPressed(this::txtField_KeyPressed);
        txtDetail06.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtDetail04.focusedProperty().addListener(txtField_Focus);
        txtDetail06.focusedProperty().addListener(txtField_Focus);
                
        txtDetail01.setText(_part_number);
        txtDetail02.setText(_description);
        txtDetail03.setText(String.valueOf(_roq));
        txtDetail04.setText(StringUtil.NumberFormat(_srp, "###0.00"));
        txtDetail05.setText(String.valueOf(_on_hand));
        txtDetail06.setText(String.valueOf(_order));
        
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
                _callback.FormClosing();
                break;
        }
    }
    
    private void loadData(){
        if (_order <= 0){
            removeData();
            return;
        }
        
        //load the data
        _callback.Result(_row, "nQuantity", _order);
        _callback.Result(_row, "nUnitPrce", _srp);
        
        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
        _callback.FormClosing();
    }
    
    private void removeData(){
        //load the data
        _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
        _callback.RemovedItem(_row);
        _callback.FormClosing();
    }
    
    private void computeTotal(){        
        double lnTranTotl = _order * _srp;
        
        lblTotal.setText(StringUtil.NumberFormat(lnTranTotl, "#,##0.00"));
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            if (txtField.getId().equals("txtDetail04")){
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
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Please encode a numeric value with correct format.", "Warning", "");
                        txtField.setText("0");
                    } 
                    
                    _order = Integer.parseInt(txtField.getText());
                    break;
                case 4:
                    if (StringUtil.isNumeric(lsValue)){
                        double lnValue = Double.valueOf(lsValue);
                        
                        txtField.setText(StringUtil.NumberFormat(lnValue, "###0.00"));                            
                    } else {
                        ShowMessageFX.Warning(_main_screen_controller.getStage(), "Please encode a numeric value with correct format.", "Warning", "");
                        txtField.setText("0.00");
                    }
                        
                    _srp = Double.valueOf(txtField.getText());
                    break;
            }
            
            computeTotal();
        } else {
            txtField.requestFocus();
            txtField.selectAll();
        }
    };
}
