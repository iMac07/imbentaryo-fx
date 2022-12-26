package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import org.xersys.commander.contants.EditMode;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.MiscUtil;
import org.xersys.commander.util.SQLUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.imbentaryofx.listener.FormClosingCallback;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;

public class SPHistoryController implements Initializable, ControlledScreen {

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
    private TextField txtField00;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0); 
        
        txtField00.setText(_barcode.toUpperCase());
        txtField01.setText(_descript.toUpperCase());
        txtField02.setText(String.valueOf(_begqty));
        txtField03.setText(StringUtil.NumberFormat(_cost, "#,##0.00"));
        txtField04.setText(StringUtil.NumberFormat(_srp, "#,##0.00"));
        
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
    
    public void setBarCode(String fsValue){
        _barcode = fsValue;
    }
    
    public void setDescription(String fsValue){
        _descript = fsValue;
    }
    
    public void setBegQty(int fnValue){
        _begqty = fnValue;
    }
    
    public void setCost(double fnValue){
        _cost = fnValue;
    }
    
    public void setSRP(double fnValue){
        _srp = fnValue;
    }
    
    public void setData(ResultSet foValue){
        _ors = foValue;
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("");
        TableColumn index02 = new TableColumn("");
        TableColumn index03 = new TableColumn("");
        TableColumn index04 = new TableColumn("");
        TableColumn index05 = new TableColumn("");
        TableColumn index06 = new TableColumn("");
        TableColumn index07 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false); index01.setStyle("-fx-font-family: 'Lucida Console', Courier New, monospace;");
        index02.setSortable(false); index02.setResizable(false); index02.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Lucida Console', Courier New, monospace;");
        index03.setSortable(false); index03.setResizable(false); index03.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Lucida Console', Courier New, monospace;");
        index04.setSortable(false); index04.setResizable(false); index04.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Lucida Console', Courier New, monospace;");
        index05.setSortable(false); index05.setResizable(false); index05.setStyle("-fx-alignment: CENTER; -fx-font-family: 'Lucida Console', Courier New, monospace;");
        index06.setSortable(false); index06.setResizable(false); index06.setStyle("-fx-font-family: 'Lucida Console', Courier New, monospace;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle("-fx-font-family: 'Lucida Console', Courier New, monospace;");
        
        table.getColumns().clear();        
        
        index01.setText("NO."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(45);
        
        index02.setText("DATE"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(150);
        
        index03.setText("QTY IN"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(100);
        
        index04.setText("QTY OUT"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(100);
        
        index05.setText("ENDING QOH"); 
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(100);
        
        index06.setText("SOURCE DESCRIPTION"); 
        index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06"));
        index06.prefWidthProperty().set(200);
        
        index07.setText("SOURCE NUMBER"); 
        index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index07"));
        index07.prefWidthProperty().set(195);
        
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
        table.getColumns().add(index04);
        table.getColumns().add(index05);
        table.getColumns().add(index06);
        table.getColumns().add(index07);
        
        table.setItems(table_data);
    }
    
    private void loadDetail(){
        table_data.clear();
        
        int lnCtr = 1;
        int lnQOH = _begqty;
        try {
            while (_ors.next()){
                lnQOH = lnQOH + _ors.getInt("nDebitQty") - _ors.getInt("nCredtQty");
                
                table_data.add(new TableModel(String.valueOf(lnCtr), 
                                SQLUtil.dateFormat(_ors.getDate("dTransact"), SQLUtil.FORMAT_MEDIUM_DATE),
                                String.valueOf(_ors.getInt("nDebitQty")), 
                                String.valueOf(_ors.getInt("nCredtQty")),
                                String.valueOf(lnQOH),
                                _ors.getString("xSourceNm"),
                                _ors.getString("sTransNox"),                                
                                "",
                                "",
                                ""));
                lnCtr++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        
        btn01.setText("Export");
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
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        JSONObject loJSON;
        
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
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                break;
        }
    }
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private QuickSearchCallback _search_callback;
    private FormClosingCallback _filter_closing;
    
    private ResultSet _ors;
    private String _barcode;
    private String _descript;
    private int _begqty;
    private double _srp;
    private double _cost;
    
    private TableModel table_model;
    private ObservableList<TableModel> table_data = FXCollections.observableArrayList();
}
