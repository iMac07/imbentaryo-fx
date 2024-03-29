package org.xersys.imbentaryofx.gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.MiscUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.inventory.roq.SPROQProc;

public class ABCClassifyController implements Initializable, ControlledScreen {
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
    private GridPane gridRemarks;
    @FXML
    private Label lblRemarks;

    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);   

        initButton();
        initGrid();
        
        _trans = new SPROQProc(_nautilus, (String) _nautilus.getBranchConfig("sBranchCd"));
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
        
        btn01.setText("Classify");
        btn02.setText("Load Lst.");
        btn03.setText("Export");
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
    }
    
    private boolean exportABC(){
        ResultSet loRS = _trans.getLastClassify();
        
        if (MiscUtil.RecordCount(loRS) <= 0){
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "No record to export.", "Warning", "");
            return false;
        }
        
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(System.getProperty("app.path.templates") + System.getProperty("app.abc.template")));
            Sheet sheet = workbook.getSheetAt(0);
            int lnRow = sheet.getLastRowNum() + 1;
            
            while (loRS.next()){
                Row row = sheet.createRow(lnRow++);
                Cell cell = row.createCell(0);
                cell.setCellValue(loRS.getString("sBarCodex"));
                cell = row.createCell(1);
                cell.setCellValue(loRS.getString("sDescript"));
                cell = row.createCell(2);
                cell.setCellValue(loRS.getString("sBrandNme"));
                cell = row.createCell(3);
                cell.setCellValue(loRS.getString("cClassify"));
                cell = row.createCell(4);
                cell.setCellValue(loRS.getInt("nAvgMonSl"));
                cell = row.createCell(5);
                cell.setCellValue(loRS.getInt("nMinLevel"));
                cell = row.createCell(6);
                cell.setCellValue(loRS.getInt("nMaxLevel"));
                cell = row.createCell(7);
                cell.setCellValue(loRS.getInt("nQtyOnHnd"));
                cell = row.createCell(8);
                cell.setCellValue(loRS.getDouble("nUnitPrce"));
                
                cell = row.createCell(9);
                if (loRS.getInt("nRecOrder")  <= 0){
                    cell.setCellValue((int) 0);
                } else{
                    cell.setCellValue(loRS.getInt("nRecOrder"));
                }
            }
            
            FileOutputStream out = new FileOutputStream(System.getProperty("app.path.export") + System.getProperty("app.abc.export"));
            workbook.write(out);
            out.close();
            
            ArrayList<String> loArr = new ArrayList<>();
            loRS.first();
            while(loRS.next()){
                if (loArr.isEmpty()){
                    loArr.add(loRS.getString("sBrandNme"));
                } else {
                    boolean lbExist = false;
                    for (int lnCtr = 0; lnCtr <= loArr.size()-1; lnCtr++){
                        if (loArr.get(lnCtr).equals(loRS.getString("sBrandNme"))){
                            lbExist = true;
                        }
                    }
                    if (!lbExist) loArr.add(loRS.getString("sBrandNme"));
                }
            }
            
            for (int lnCtr = 0; lnCtr <= loArr.size()-1; lnCtr++){
                workbook = new XSSFWorkbook(new FileInputStream(System.getProperty("app.path.templates") + System.getProperty("app.abc.template")));
                sheet = workbook.getSheetAt(0);
                lnRow = sheet.getLastRowNum() + 1;
            
                loRS.first();
                while (loRS.next()){
                    if (loRS.getString("sBrandNme").equals(loArr.get(lnCtr))){
                        Row row = sheet.createRow(lnRow++);
                        Cell cell = row.createCell(0);
                        cell.setCellValue(loRS.getString("sBarCodex"));
                        cell = row.createCell(1);
                        cell.setCellValue(loRS.getString("sDescript"));
                        cell = row.createCell(2);
                        cell.setCellValue(loRS.getString("sBrandNme"));
                        cell = row.createCell(3);
                        cell.setCellValue(loRS.getString("cClassify"));
                        cell = row.createCell(4);
                        cell.setCellValue(loRS.getInt("nAvgMonSl"));
                        cell = row.createCell(5);
                        cell.setCellValue(loRS.getInt("nMinLevel"));
                        cell = row.createCell(6);
                        cell.setCellValue(loRS.getInt("nMaxLevel"));
                        cell = row.createCell(7);
                        cell.setCellValue(loRS.getInt("nQtyOnHnd"));
                        cell = row.createCell(8);
                        cell.setCellValue(loRS.getDouble("nUnitPrce"));
                        
                        cell = row.createCell(9);
                        if (loRS.getInt("nRecOrder")  <= 0){
                            cell.setCellValue((int) 0);
                        } else{
                            cell.setCellValue(loRS.getInt("nRecOrder"));
                        }
                    }
                }
                
                out = new FileOutputStream(System.getProperty("app.path.export") + loArr.get(lnCtr) + ".xlsx");
                workbook.write(out);
                out.close();
            }
        } catch (IOException | SQLException e) {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), e.getMessage(), "Warning", "");
            return false;
        }
        
        return true;
    }
    
    private void loadLedger(){
        ResultSet loRS = _trans.getLastClassify();
        
        
        if (loRS == null){
            ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
            return;
        }
        
        gridRemarks.setVisible(true);
        
        _data.clear();
        
        try {
            int lnCtr = 0;
            while (loRS.next()){
                if (lnCtr == 0){
                    lblRemarks.setText(loRS.getString("sPeriodxx"));
                }
                
                _data.add(new TableModel(String.valueOf(lnCtr + 1), 
                    loRS.getString("sBarCodex"),
                    loRS.getString("sDescript"), 
                    loRS.getString("sBrandNme"), 
                    loRS.getString("cClassify"),
                    String.valueOf(loRS.getInt("nAvgMonSl")),
                    String.valueOf(loRS.getInt("nMinLevel")),
                    String.valueOf(loRS.getInt("nMaxLevel")),
                    String.valueOf(loRS.getInt("nQtyOnHnd")),
                    StringUtil.NumberFormat(loRS.getDouble("nSelPrce1"), "#,##0.00"),
                    StringUtil.NumberFormat(loRS.getDouble("nUnitPrce"), "#,##0.00"),
                    "",
                    "",
                    "",
                    ""));
                lnCtr += 1;
            }
            
            if (!_data.isEmpty()){
                table.getSelectionModel().select(0);
                table.getFocusModel().focus(0);        
            }
        } catch (SQLException e) {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), e.getMessage(), "Warning", "");
            e.printStackTrace();
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
        TableColumn index08 = new TableColumn("");
        TableColumn index09 = new TableColumn("");
        TableColumn index10 = new TableColumn("");
        TableColumn index11 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false); 
        index05.setSortable(false); index05.setResizable(false); index05.setStyle("-fx-alignment: CENTER;");
        index06.setSortable(false); index06.setResizable(false); index06.setStyle("-fx-alignment: CENTER;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle("-fx-alignment: CENTER;");
        index08.setSortable(false); index08.setResizable(false); index08.setStyle("-fx-alignment: CENTER;");
        index09.setSortable(false); index09.setResizable(false); index09.setStyle("-fx-alignment: CENTER;");
        index10.setSortable(false); index10.setResizable(false); index10.setStyle("-fx-alignment: CENTER-RIGHT;");
        index11.setSortable(false); index11.setResizable(false); index11.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        table.getColumns().clear();        
        
        index01.setText("NO."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(30);
        
        index02.setText("PART NUMBER"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(150);
        
        index03.setText("DESCRIPTION"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(200);
        
        index04.setText("BRAND"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(105);
        
        index05.setText("CLASS"); 
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(50);
        
        index06.setText("AMC"); 
        index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06"));
        index06.prefWidthProperty().set(50);
        
        index07.setText("MIN"); 
        index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index07"));
        index07.prefWidthProperty().set(50);
        
        index08.setText("MAX"); 
        index08.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index08"));
        index08.prefWidthProperty().set(50);
        
        index09.setText("QOH"); 
        index09.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index09"));
        index09.prefWidthProperty().set(50);
        
        index10.setText("SRP"); 
        index10.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index10"));
        index10.prefWidthProperty().set(70);
        
        index11.setText("COST"); 
        index11.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index11"));
        index11.prefWidthProperty().set(70);
        
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
        table.getColumns().add(index04);
        table.getColumns().add(index05);
        table.getColumns().add(index06);
        table.getColumns().add(index07);
        table.getColumns().add(index08);
        table.getColumns().add(index09);
        table.getColumns().add(index10);
        table.getColumns().add(index11);
        
        table.setItems(_data);
        
        gridRemarks.setVisible(false);
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        JSONObject loJSON;
        
        switch (lsButton){
            case "btn01"://classify
                if (!_trans.ClassifyABC()){
                    ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Warning", "");
                } else {
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "ABC Classification was successfully completed.", "Notice", "");
                };
                break;
            case "btn02"://load
                loadLedger();
                break;
            case "btn03"://export
                if (exportABC()){
                    ShowMessageFX.Information(_main_screen_controller.getStage(), "Records export success.", "Warning", "");
                }
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
            case "btn12":
                if (_screens_controller.getScreenCount() > 1)
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                else{
                    if (ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to exit the application?", "Please confirm", ""))
                        System.exit(0);
                }
                break;
        }
    }

    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private QuickSearchCallback _search_callback;
    
    private SPROQProc _trans;
    private boolean _loaded = false;
    private XNautilus _nautilus;
    
    private ObservableList<TableModel> _data = FXCollections.observableArrayList();
}
