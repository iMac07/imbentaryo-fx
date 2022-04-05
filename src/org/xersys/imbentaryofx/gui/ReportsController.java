package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.iface.XReport;
import org.xersys.reports.ReportMaster;
import org.xersys.reports.ReportsFactory;

public class ReportsController implements Initializable, ControlledScreen{
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private AnchorPane ReportPane;
    @FXML
    private AnchorPane AnchorCriteria;
    @FXML
    private ComboBox cmbReport;
    @FXML
    private Button btn01;
    @FXML
    private Button btn02;
    @FXML
    private RadioButton rad01;
    @FXML
    private RadioButton rad03;
    @FXML
    private RadioButton rad02;
    @FXML
    private RadioButton rad04;
    
    private ReportMaster _trans;
    
    private ArrayList<String> _list;
    private int _index;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0); 
        
        _main_screen_controller.AnchorPaneMonitor.getChildren().clear();
        
        if (_nautilus  == null) {
            System.err.println("Application driver is not set.");
            System.exit(1);
        }
        
        btn01.setOnAction(this::cmdButton_Click);
        btn02.setOnAction(this::cmdButton_Click);
        
        ToggleGroup toggleGroup = new ToggleGroup();

        rad01.setUserData(1);
        rad02.setUserData(2);
        rad03.setUserData(3);
        rad04.setUserData(4);
        
        rad01.setToggleGroup(toggleGroup);
        rad02.setToggleGroup(toggleGroup);
        rad03.setToggleGroup(toggleGroup);
        rad04.setToggleGroup(toggleGroup);
        
        rad01.setSelected(true);
        _index = 1;
        
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                _index = (int) newValue.getUserData();
            }
        });
        
        _trans = new ReportMaster(_nautilus);
        
        if (_trans.LoadReports()){
            _list = new ArrayList<>();
            ArrayList<String> loList = new ArrayList<>();
            
            
            int lnRow = _trans.getItemCount();
            
            for (int lnCtr = 0; lnCtr<=lnRow-1; lnCtr++){
                _list.add((String) _trans.getMaster(lnCtr, "sReportID"));
                loList.add((String) _trans.getMaster(lnCtr, "sReportNm"));
            }
            
            ObservableList<String> list = FXCollections.observableArrayList(loList);
            cmbReport.setItems(list);
            cmbReport.getSelectionModel().select(0);
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
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //load
                processReport();
                break;
            case "btn02": //cloase
                if (_screens_controller.getScreenCount() > 1)
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                else{
                    if (ShowMessageFX.YesNo(_main_screen_controller.getStage(), "Do you want to exit the application?", "Please confirm", ""))
                        System.exit(0);
                }
                break;
        }
    }
    
    private void processReport(){
        int lnIndex = cmbReport.getSelectionModel().getSelectedIndex();        
        
        XReport loReport = ReportsFactory.make(_list.get(lnIndex));
        
        if (loReport == null) {
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Object for this report is not set. Please contact your system administrator.", "Warning", "");
            return;
        }
        
        System.setProperty("store.report.criteria.presentation", String.valueOf(_index));
        
        loReport.setNautilus(_nautilus);
        JasperPrint loPrint = loReport.processReport();
        
        if (loPrint == null){
            ShowMessageFX.Warning(_main_screen_controller.getStage(), loReport.getMessage(), "Warning", "");
            return;
        }
        
        showReport(loPrint);
    }
    
    private void showReport(JasperPrint foPrint){
        SwingNode swingNode = new SwingNode();
        JRViewer jrViewer =  new JRViewer(foPrint);
        jrViewer.setOpaque(true);
        jrViewer.setVisible(true);
        jrViewer.setFitPageZoomRatio();
        
        swingNode.setContent(jrViewer);
        ReportPane.setTopAnchor(swingNode,0.0);
        ReportPane.setBottomAnchor(swingNode,0.0);
        ReportPane.setLeftAnchor(swingNode,0.0);
        ReportPane.setRightAnchor(swingNode,0.0);
        ReportPane.getChildren().add(swingNode);
    }
    
    XNautilus _nautilus;
    MainScreenController _main_screen_controller;
    ScreensController _screens_controller;  
    ScreensController _screens_dashboard_controller;
}
