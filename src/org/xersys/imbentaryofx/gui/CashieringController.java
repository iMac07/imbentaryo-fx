package org.xersys.imbentaryofx.gui;

import java.net.URL;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.StringUtil;
import org.xersys.payment.base.CashierTrans;

public class CashieringController implements Initializable, ControlledScreen {
    private XNautilus _nautilus;
    private CashierTrans _trans;
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_dashboard_controller;
    private ScreensController _screens_controller;
    private QuickSearchCallback _search_callback;
    
    private String _source_code = "";
    private String _source_number = "";
    private double _adv_payment = 0.00;
    
    private ObservableList<TableModel> _data = FXCollections.observableArrayList();
    private TableModel _model;
    
    private int pnSelectd = -1;
    
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {    
        if (_nautilus  == null) {
            System.err.println("Application driver is not set.");
            System.exit(1);
        }
        
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);   
        
        table.setOnMouseClicked(this::mouseClicked);
        
        _trans = new CashierTrans(_nautilus);
        _trans.setSourceCd("SO»JO»WS»CO");
        
        initButton();
        initGrid();
        
        try {
            if (_trans.LoadTransactions())
                loadDetail();
//            else
//                ShowMessageFX.Warning(_main_screen_controller.getStage(), _trans.getMessage(), "Notice", "");
        } catch (SQLException ex) {
            ex.printStackTrace();
            ShowMessageFX.Warning(_main_screen_controller.getStage(), "Error loading cashier transactions.", "Warning", "");
            System.exit(1);
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
    }
    
    private void loadDetail() throws SQLException{       
        _data.clear();        
        
        int lnRow = _trans.getItemCount();
        
        for (int lnCtr = 1; lnCtr <= lnRow; lnCtr ++){
            _data.add(new TableModel(String.valueOf(lnCtr),
                                    String.valueOf(_trans.getDetail(lnCtr, "dTransact")),
                                    String.valueOf(_trans.getDetail(lnCtr, "sTranType")),
                                    String.valueOf(_trans.getDetail(lnCtr, "sOrderNox")),
                                    StringUtil.NumberFormat((Number) _trans.getDetail(lnCtr, "nTranTotl") , "#,##0.00"),
                                    StringUtil.NumberFormat((Number) _trans.getDetail(lnCtr, "xPayablex") , "#,##0.00"),
                                    String.valueOf(_trans.getDetail(lnCtr, "sClientNm")),
                                    "",
                                    "",
                                    ""));
        }
                
        table.getSelectionModel().selectFirst();
        pnSelectd = table.getSelectionModel().getSelectedIndex();
        
        _source_code = (String) _trans.getDetail(pnSelectd + 1, "sSourceCd");
        _source_number = (String) _trans.getDetail(pnSelectd + 1, "sTransNox");
        _adv_payment = Double.valueOf(String.valueOf(_trans.getDetail(pnSelectd + 1, "nDeductnx")));
    }
    
    private void mouseClicked(MouseEvent event) {
        pnSelectd = table.getSelectionModel().getSelectedIndex();
        
        _source_code = "";
        _source_number = "";
        
        if (pnSelectd >= 0){
            try {
                _source_code = (String) _trans.getDetail(pnSelectd + 1, "sSourceCd");
                _source_number = (String) _trans.getDetail(pnSelectd + 1, "sTransNox");
            } catch (SQLException ex) {
                ex.printStackTrace();
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "Error in retreiving transaction information.", "Warning", "");
                System.exit(1);
            }
        }
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("D/T Created");
        TableColumn index03 = new TableColumn("Type");
        TableColumn index04 = new TableColumn("Order No.");
        TableColumn index05 = new TableColumn("Amount");
        TableColumn index06 = new TableColumn("Payable");
        TableColumn index07 = new TableColumn("Mechanic/Saleman");        
        
        index01.setSortable(false); index01.setResizable(false); index01.setStyle( "-fx-alignment: CENTER-LEFT;");
        index02.setSortable(false); index02.setResizable(false); index02.setStyle( "-fx-alignment: CENTER;");
        index03.setSortable(false); index03.setResizable(false); index03.setStyle( "-fx-alignment: CENTER;");
        index04.setSortable(false); index04.setResizable(false); index04.setStyle( "-fx-alignment: CENTER;");
        index05.setSortable(false); index05.setResizable(false); index05.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index06.setSortable(false); index06.setResizable(false); index06.setStyle( "-fx-alignment: CENTER-RIGHT;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle( "-fx-alignment: CENTER-LEFT;");
        
        table.getColumns().clear();        
        
        table.getColumns().add(index01);
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(30);
        
        table.getColumns().add(index02);
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(180);
        
        table.getColumns().add(index03);
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(140);
        
        table.getColumns().add(index04);
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(100);
        
        table.getColumns().add(index05);
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(100);
        
        table.getColumns().add(index06);
        index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06"));
        index06.prefWidthProperty().set(100);
        
        table.getColumns().add(index07);
        index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index07"));
        index07.prefWidthProperty().set(240);
        
        table.setItems(_data);
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
        
        btn01.setText("Pay");
        btn02.setText("Release");
        btn03.setText("");
        btn04.setText("");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("OR");
        btn11.setText("SI");
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
        btn10.setVisible(true);
        btn11.setVisible(true);
        btn12.setVisible(true);
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //pay
                if (!_source_code.isEmpty() && !_source_number.isEmpty()) {
                    payWithInvoice();
                }
                break;
            case "btn02": //release
                if (!_source_code.isEmpty() && !_source_number.isEmpty()) 
                    payCharge();
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
                loadScreen(ScreenInfo.NAME.OR_HISTORY);
                break;
            case "btn11":
                loadScreen(ScreenInfo.NAME.SI_HISTORY);
                break;
            case "btn12": //close screen
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                break;
        }
    }
    
    private void loadScreen(ScreenInfo.NAME  foValue){
        JSONObject loJSON = ScreenInfo.get(foValue);
        ControlledScreen instance;
        
        if (loJSON != null){
            instance = (ControlledScreen) CommonUtil.createInstance((String) loJSON.get("controller"));
            instance.setNautilus(_nautilus);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            instance.setDashboardScreensController(_screens_dashboard_controller);
            
            _screens_controller.loadScreen((String) loJSON.get("resource"), instance);
        }
    }
    
    private void payWithInvoice(){
        if (!_source_code.isEmpty() && !_source_number.isEmpty()){
            JSONObject loJSON;
                    
            if (_source_code.equals("JO")){
                loJSON = ScreenInfo.get(ScreenInfo.NAME.PAYMENT_JO);
                PaymentJOController instance = new PaymentJOController();
                instance.setSourceCd(_source_code);
                instance.setSourceNo(_source_number);

                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);
                instance.setDashboardScreensController(_screens_dashboard_controller);
                instance.setSourceCd(_source_code);
                instance.setSourceNo(_source_number);
                
                //close this screen
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                //load the payment screen
                _screens_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) instance);
            } else{
                loJSON = ScreenInfo.get(ScreenInfo.NAME.PAYMENT);
                PaymentController instance = new PaymentController();
                instance.setSourceCd(_source_code);
                instance.setSourceNo(_source_number);

                instance.setNautilus(_nautilus);
                instance.setParentController(_main_screen_controller);
                instance.setScreensController(_screens_controller);
                instance.setDashboardScreensController(_screens_dashboard_controller);
                instance.setSourceCd(_source_code);
                instance.setSourceNo(_source_number);
                instance.setAdvancePayment(_adv_payment);
                
                //close this screen
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                //load the payment screen
                _screens_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) instance);
            }
        }
    }
    
    private void payNoInvoice(){
        if (!_source_code.isEmpty() && !_source_number.isEmpty()){           
            JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.PAYMENT_NO_INVOICE);
            PaymentNoInvoiceController instance = new PaymentNoInvoiceController();
            instance.setSourceCd(_source_code);
            instance.setSourceNo(_source_number);

            instance.setNautilus(_nautilus);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            instance.setDashboardScreensController(_screens_dashboard_controller);
            instance.setSourceCd(_source_code);
            instance.setSourceNo(_source_number);

            //close this screen
            _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
            //load the payment screen
            _screens_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) instance);
        }
    }
    
    private void payCharge(){
        if (!_source_code.isEmpty() && !_source_number.isEmpty()){
            if (_source_code.equals("CO")) {
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "Customer orders are not allowede for charge invoice.", "Notice", "");
                return;
            }
            
            if (_source_code.equals("JO")) {
                ShowMessageFX.Warning(_main_screen_controller.getStage(), "Job orders are not allowede for charge invoice.", "Notice", "");
                return;
            }
            
            
            JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.PAYMENT_CHARGE);
            PaymentChargeController instance = new PaymentChargeController();
            instance.setSourceCd(_source_code);
            instance.setSourceNo(_source_number);

            instance.setNautilus(_nautilus);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            instance.setDashboardScreensController(_screens_dashboard_controller);
            instance.setSourceCd(_source_code);
            instance.setSourceNo(_source_number);

            //close this screen
            _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
            //load the payment screen
            _screens_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) instance);
        }
    }
}
