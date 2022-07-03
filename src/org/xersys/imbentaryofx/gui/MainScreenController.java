package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import org.xersys.commander.contants.UserLevel;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.MiscUtil;
import org.xersys.commander.util.SQLUtil;
import org.xersys.commander.util.StringHelper;

public class MainScreenController implements Initializable {
    @FXML
    private AnchorPane AnchorPaneMain;
    @FXML
    private StackPane StackPaneBody;
    @FXML
    private AnchorPane AnchorPaneHeader;
    @FXML
    private AnchorPane AnchorPaneFooter;
    @FXML
    public AnchorPane AnchorPaneMonitor;
    @FXML
    public AnchorPane AnchorPaneBody;
    @FXML
    public VBox SidePane;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblCompany;
    @FXML
    private Label lblUser;
    @FXML
    private AnchorPane btnOther01;
    @FXML
    private AnchorPane btnOther02;
    @FXML
    private AnchorPane btnOther03;
    @FXML
    private AnchorPane btnOther04;
    @FXML
    private AnchorPane btnOther05;
    @FXML
    private AnchorPane btnOther06;
    @FXML
    private AnchorPane btnOther07;
    @FXML
    private AnchorPane btnOther08;
    @FXML
    private AnchorPane btnOther09;
    @FXML
    private AnchorPane btnOther10;
    @FXML
    private AnchorPane btnOther;
    @FXML
    private AnchorPane btnOther11;
    @FXML
    private AnchorPane btnOther12;
    @FXML
    private AnchorPane btnOther13;
    @FXML
    private Label lblOther01;
    @FXML
    private ImageView imgOther01;
    @FXML
    private Label lblOther02;
    @FXML
    private ImageView imgOther02;
    @FXML
    private Label lblOther03;
    @FXML
    private ImageView imgOther03;
    @FXML
    private Label lblOther04;
    @FXML
    private ImageView imgOther04;
    @FXML
    private Label lblOther05;
    @FXML
    private ImageView imgOther05;
    @FXML
    private Label lblOther06;
    @FXML
    private ImageView imgOther06;
    @FXML
    private Label lblOther07;
    @FXML
    private ImageView imgOther07;
    @FXML
    private Label lblOther08;
    @FXML
    private ImageView imgOther08;
    @FXML
    private Label lblOther09;
    @FXML
    private ImageView imgOther09;
    @FXML
    private Label lblOther10;
    @FXML
    private ImageView imgOther10;
    @FXML
    private Label lblOther11;
    @FXML
    private ImageView imgOther11;
    @FXML
    private Label lblOther12;
    @FXML
    private ImageView imgOther12;
    @FXML
    private Label lblOther13;
    @FXML
    private ImageView imgOther13;
    @FXML
    private AnchorPane btnOther14;
    @FXML
    private Label lblOther14;
    @FXML
    private ImageView imgOther14;
    @FXML
    private AnchorPane btnOther15;
    @FXML
    private Label lblOther15;
    @FXML
    private ImageView imgOther15;
    @FXML
    private AnchorPane btnOther16;
    @FXML
    private Label lblOther16;
    @FXML
    private ImageView imgOther16;
    @FXML
    private AnchorPane btnOther17;
    @FXML
    private Label lblOther17;
    @FXML
    private ImageView imgOther17;
    @FXML
    private AnchorPane btnOther18;
    @FXML
    private Label lblOther18;
    @FXML
    private ImageView imgOther18;
    @FXML
    private AnchorPane btnOther19;
    @FXML
    private Label lblOther19;
    @FXML
    private ImageView imgOther19;
    @FXML
    private AnchorPane btnOther20;
    @FXML
    private Label lblOther20;
    @FXML
    private ImageView imgOther20;
    @FXML
    private AnchorPane btnOther21;
    @FXML
    private Label lblOther21;
    @FXML
    private ImageView imgOther21;
    
    public void setNautilus(XNautilus foValue){
        _nautilus = foValue;
    }
    
    public void addNoTabScreen(String fsValue){
        Iterator itr = _no_tab.iterator();
        boolean lbExist = false;
        
        while (itr.hasNext()) {
            String x = (String)itr.next();
            if (x.equals(fsValue)) {
                lbExist = true;
                break;
            }
        }
        
        if (!lbExist) _no_tab.add(fsValue);
        
//        if (!_no_tab_screen.contains(fsValue)) 
//            _no_tab_screen += fsValue;
    }
    
    public void delNoTabScreen(String fsValue){
        Iterator itr = _no_tab.iterator();
        
        while (itr.hasNext()) {
            String x = (String)itr.next();
            if (x.equals(fsValue))
                itr.remove();
        }
//        if (_no_tab_screen.contains(fsValue)) 
//            _no_tab_screen = _no_tab_screen.replace(fsValue, "");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (_nautilus  == null) {
            System.err.println("Application driver is not set.");
            System.exit(1);
        }

        _screens_controller = new ScreensController();
        _screens_controller.setParentPane(AnchorPaneBody);
        _screens_controller.setParentController(this);
        
        _screens_dashboard_controller = new ScreensController();
        _screens_dashboard_controller.setParentPane(AnchorPaneMonitor);
        _screens_dashboard_controller.setParentController(this);
        
        //keyboard events
        AnchorPaneMain.setOnKeyPressed(this::keyPressed);
        AnchorPaneMain.setOnKeyReleased(this::keyReleased);
        
        initButton();        
        load("");
    }    
    
    public void load(String fsUserID){
        if (!fsUserID.isEmpty()){
            _nautilus.setUserID(fsUserID);
            if (!_nautilus.load("icarus")){
                ShowMessageFX.Warning(getStage(), _nautilus.getMessage(), "Warning", "");
                return;
            }

            _logged = true;
        }
        
        initScreen();
        initMenu();
    }
    
    public void seekApproval(){
        try {
            SeekApproval loSeek = new SeekApproval();
            
            System.setProperty("sUserIDxx", "");
            System.setProperty("sUserName", "");
            System.setProperty("sPassword", "");
        
            Stage stage = new Stage();
            stage.initModality(Modality.NONE);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setAlwaysOnTop(true);
            loSeek.start(stage);  
            
            System.setProperty("sUserName", loSeek.getUsername());
            System.setProperty("sPassword", loSeek.getPassword());
            
            loSeek = null;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private void loadScreen(ScreenInfo.NAME  foValue){
        JSONObject loJSON = ScreenInfo.get(foValue);
        ControlledScreen instance;
        
        if (loJSON != null){
            instance = (ControlledScreen) CommonUtil.createInstance((String) loJSON.get("controller"));
            instance.setNautilus(_nautilus);
            instance.setParentController(this);
            instance.setScreensController(_screens_controller);
            instance.setDashboardScreensController(_screens_dashboard_controller);
            
            _screens_controller.loadScreen((String) loJSON.get("resource"), instance);
        }
    }
    
    private void loadDashboard(){
        //load the dashboard
        JSONObject loJSON = ScreenInfo.get(ScreenInfo.NAME.DASHBOARD);
        if (loJSON != null) _screens_dashboard_controller.loadScreen((String) loJSON.get("resource"), (ControlledScreen) CommonUtil.createInstance((String) loJSON.get("controller")));          
    }
    
    private void initScreen(){
        _screens_controller.clear();
        _screens_dashboard_controller.clear();
        
        showTime();
        if (_logged){
            lblUser.setText((String) _nautilus.getUserInfo("xClientNm"));
            
            loadScreen(ScreenInfo.NAME.SP_SALES);
            loadDashboard();
        } else{
            lblUser.setText("Not Logged In");
            
            loadScreen(ScreenInfo.NAME.BACKGROUND);
        }
        
        _no_tab = new ArrayList<>();
        _no_tab.add("POSDetail");
        _no_tab.add("SPCustomerOrderDetail");
        _no_tab.add("SPCustomerOrderIssuanceDetail");
        _no_tab.add("QuickSearch");
        _no_tab.add("QuickSearchFilter");
        _no_tab.add("PartsInquiry");
        _no_tab.add("PartsCatalogue");
        _no_tab.add("PartsCatalogueDetail");
        _no_tab.add("ClientAddress");
        _no_tab.add("ClientMobile");
        _no_tab.add("ClientEMail");
        _no_tab.add("Cashiering");
        _no_tab.add("Payment");
        _no_tab.add("PaymentJO");
        _no_tab.add("Login");
        _no_tab.add("Background");
        _no_tab.add("InvAdjustmentDetail");
        _no_tab.add("InvRequestDetail");
        _no_tab.add("InvTransferDetail");
        
//        //set screens that will not trigger on window tabs
//        _no_tab_screen = "";
//        _no_tab_screen += "POSDetail";
//        _no_tab_screen += "SPCustomerOrderDetail";
//        _no_tab_screen += "SPCustomerOrderIssuanceDetail";
//        _no_tab_screen += "QuickSearch";
//        _no_tab_screen += "QuickSearchFilter";
//        _no_tab_screen += "PartsInquiry";
//        _no_tab_screen += "PartsCatalogue";
//        _no_tab_screen += "PartsCatalogueDetail";
//        _no_tab_screen += "ClientAddress";
//        _no_tab_screen += "ClientMobile";
//        _no_tab_screen += "ClientEMail";
//        _no_tab_screen += "Cashiering";
//        _no_tab_screen += "Payment";
//        _no_tab_screen += "PaymentJO";
//        _no_tab_screen += "Login";
//        _no_tab_screen += "Background";
//        _no_tab_screen += "InvAdjustmentDetail";
//        _no_tab_screen += "InvRequestDetail";
//        _no_tab_screen += "InvTransferDetail";
    }
    
    private void initMenu(){        
        btn.put("btnOther01", btnOther01); lbl.put("lblOther01", lblOther01); img.put("imgOther01", imgOther01);
        btn.put("btnOther02", btnOther02); lbl.put("lblOther02", lblOther02); img.put("imgOther02", imgOther02);
        btn.put("btnOther03", btnOther03); lbl.put("lblOther03", lblOther03); img.put("imgOther03", imgOther03);
        btn.put("btnOther04", btnOther04); lbl.put("lblOther04", lblOther04); img.put("imgOther04", imgOther04);
        btn.put("btnOther05", btnOther05); lbl.put("lblOther05", lblOther05); img.put("imgOther05", imgOther05);
        btn.put("btnOther06", btnOther06); lbl.put("lblOther06", lblOther06); img.put("imgOther06", imgOther06);
        btn.put("btnOther07", btnOther07); lbl.put("lblOther07", lblOther07); img.put("imgOther07", imgOther07);
        btn.put("btnOther08", btnOther08); lbl.put("lblOther08", lblOther08); img.put("imgOther08", imgOther08);
        btn.put("btnOther09", btnOther09); lbl.put("lblOther09", lblOther09); img.put("imgOther09", imgOther09);
        btn.put("btnOther10", btnOther10); lbl.put("lblOther10", lblOther10); img.put("imgOther10", imgOther10);
        btn.put("btnOther11", btnOther11); lbl.put("lblOther11", lblOther11); img.put("imgOther11", imgOther11);
        btn.put("btnOther12", btnOther12); lbl.put("lblOther12", lblOther12); img.put("imgOther12", imgOther12);
        btn.put("btnOther13", btnOther13); lbl.put("lblOther13", lblOther13); img.put("imgOther13", imgOther13);
        btn.put("btnOther14", btnOther14); lbl.put("lblOther14", lblOther14); img.put("imgOther14", imgOther14);
        btn.put("btnOther15", btnOther15); lbl.put("lblOther15", lblOther15); img.put("imgOther15", imgOther15);
        btn.put("btnOther16", btnOther16); lbl.put("lblOther16", lblOther16); img.put("imgOther16", imgOther16);
        btn.put("btnOther17", btnOther17); lbl.put("lblOther17", lblOther17); img.put("imgOther17", imgOther17);
        btn.put("btnOther18", btnOther18); lbl.put("lblOther18", lblOther18); img.put("imgOther18", imgOther18);
        btn.put("btnOther19", btnOther19); lbl.put("lblOther19", lblOther19); img.put("imgOther19", imgOther19);
        btn.put("btnOther20", btnOther20); lbl.put("lblOther20", lblOther20); img.put("imgOther20", imgOther20);
        btn.put("btnOther21", btnOther21); lbl.put("lblOther21", lblOther21); img.put("imgOther21", imgOther21);
        
        AnchorPane button;
        Label label;
        ImageView image;
        
        int lnCtr;
        String lsCtr;
        
        for (lnCtr = 1; lnCtr <= 21; lnCtr++){
            lsCtr = StringHelper.prepad(String.valueOf(lnCtr), 2, '0');
            
            button = btn.get("btnOther" + lsCtr);
            button.setVisible(false);
        }
        
        ResultSet loRS;
        ResultSet loRSx;
        
        try {
            String lsMod = "SELECT" +
                            "  sModuleID" +
                            ", sBriefDsc" +
                            ", sDescript" +
                            ", nObjAcces" +
                            ", nOrderNox" +
                        " FROM xxxSysModule" +
                        " WHERE sMnModule IS NOT NULL" +
                            " AND cRecdStat = '1'" +
                        " ORDER BY nOrderNox";
        
            loRS = _nautilus.executeQuery(lsMod);
            
            String lsSQL;
            boolean lbShow;
            lnCtr = 0;
            
            while (loRS.next()){
                lbShow = false;
                
                switch (loRS.getString("sModuleID")){
                    case "026": //Login
                    case "028": //Exit
                        lbShow = !_logged;
                        break;
                    case "025": //Create User
                        if (_logged){
                            lbShow = ((int) _nautilus.getUserInfo("nUserLevl") & UserLevel.SYSADMIN + UserLevel.MASTER) != 0;
                        }
                        break;
                    case "021": //Reports
                    case "027": //Logout & Exit
                        lbShow = _logged;
                        break;
                    default:
                        if (_logged){
                            if ((loRS.getInt("nObjAcces") & (int) _nautilus.getUserInfo("nObjAcces")) != 0){
                                lbShow = true;
                            }
                        }
                }
                if (lbShow){
                    lnCtr++;
                    lsCtr = StringHelper.prepad(String.valueOf(lnCtr), 2, '0');

                    button = btn.get("btnOther" + lsCtr);
                    button.setVisible(true);

                    label = lbl.get("lblOther" + lsCtr);
                    label.setText(loRS.getString("sBriefDsc"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        loRS = null;
        loRSx = null;
    }
    
    private void initButton(){
        btnOther01.setOnMouseClicked(this::cmdMouse_Click);
        btnOther02.setOnMouseClicked(this::cmdMouse_Click);
        btnOther03.setOnMouseClicked(this::cmdMouse_Click);
        btnOther04.setOnMouseClicked(this::cmdMouse_Click);
        btnOther05.setOnMouseClicked(this::cmdMouse_Click);
        btnOther06.setOnMouseClicked(this::cmdMouse_Click);
        btnOther07.setOnMouseClicked(this::cmdMouse_Click);
        btnOther08.setOnMouseClicked(this::cmdMouse_Click);
        btnOther09.setOnMouseClicked(this::cmdMouse_Click);
        btnOther10.setOnMouseClicked(this::cmdMouse_Click);
        btnOther11.setOnMouseClicked(this::cmdMouse_Click);
        btnOther12.setOnMouseClicked(this::cmdMouse_Click);
        btnOther13.setOnMouseClicked(this::cmdMouse_Click);
        btnOther14.setOnMouseClicked(this::cmdMouse_Click);
        btnOther15.setOnMouseClicked(this::cmdMouse_Click);
        btnOther16.setOnMouseClicked(this::cmdMouse_Click);
        btnOther17.setOnMouseClicked(this::cmdMouse_Click);
        btnOther18.setOnMouseClicked(this::cmdMouse_Click);
        btnOther19.setOnMouseClicked(this::cmdMouse_Click);
        btnOther20.setOnMouseClicked(this::cmdMouse_Click);
    }
    
    private boolean canSwitch(){
        Node loNode = _screens_controller.getScreen(_screens_controller.getCurrentScreenIndex());

        Iterator itr = _no_tab.iterator();
        boolean lbExist = false;

        while (itr.hasNext()) {
            String x = (String)itr.next();
            if (x.equals(loNode.getId())) {
                lbExist = true;
                break;
            }
        }
        
        //_no_tab_screen.contains(loNode.getId())
        if (lbExist){
            ShowMessageFX.Warning(getStage(), "You are on a child form. Please finish the transaction first.", "Warning", "");
            return false;
        }
        
        return true;
    }
    
    private void cmdMouse_Click(MouseEvent event) {
        String lsButton = ((AnchorPane) event.getSource()).getId();       
        Label label = lbl.get("lblOther" + lsButton.substring(8));
        
        switch (label.getText().toLowerCase()){
            case "labor":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.LABOR); break;
            case "purchase order":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.PURCHASE_ORDER); break;
            case "point-of-sales":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.SP_SALES); break;
            case "customer order":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.CUSTOMER_ORDER); break;
            case "co releasing":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.CUSTOMER_ORDER_ISSUANCE); break;
            case "ws order":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.SP_WHOLESALE_ORDER); break;
            case "whole sale":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.SP_WHOLESALE); break;
            case "mc repair":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.JOB_ORDER); break;
            case "stocks":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.SP_INV_MASTER); break;
            case "inv. adjustment":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.INV_ADJUSTMENT); break;
            case "inv. request":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.INV_REQUEST); break;
            case "inv. transfer":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.INV_TRANSFER); break;
            case "pay/release":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.CASHIERING); break;
            case "reports":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.REPORTS); 
                return;
            case "clients":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.CLIENT_MASTER); break;
            case "ap payment":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.AP_PAYMENT); break;
            case "ar payment":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.AR_PAYMENT); break;
            case "mc serial":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.MC_SERIAL); break;
            case "manage user":
                if (canSwitch()) loadScreen(ScreenInfo.NAME.USER_MANAGER); break;
            case "login":
                loadScreen(ScreenInfo.NAME.LOGIN);
                return;
            case "logout & exit":
            case "exit":
                if (ShowMessageFX.YesNo(getStage(), "Do you want to exit the application?", "Please confirm", ""))
                    System.exit(0);
                else return;
        }
        
        loadDashboard();
    }
    
    public Stage getStage(){
        return (Stage) btnOther.getScene().getWindow();
    }
    
    public void keyReleased(KeyEvent event) {
        switch(event.getCode()){
            case ESCAPE:
                break; 
            case CONTROL:
                _control_pressed = false;
                break;
            case SHIFT:
                _shift_pressed = false;
                break;
            case TAB:
                //_control_pressed = false;
                //_shift_pressed = false;
                break;
        }
    }
    
    public void keyPressed(KeyEvent event) {
        switch(event.getCode()){
            case CONTROL:
                _control_pressed = true;
                break; 
            case SHIFT:
                _shift_pressed = true;
                break;
            case TAB:
                Node loNode = _screens_controller.getScreen(_screens_controller.getCurrentScreenIndex());
                
                //prevent some window to user prev/fwrd screen
                Iterator itr = _no_tab.iterator();
                boolean lbExist = false;

                while (itr.hasNext()) {
                    String x = (String)itr.next();
                    if (x.equals(loNode.getId())) {
                        lbExist = true;
                        break;
                    }
                }
                
                //_no_tab_screen.contains(loNode.getId())
                if (lbExist){
                    System.err.println("Request rejected.");
                } else {
                    if (_control_pressed){
                        if (_shift_pressed){
                            if (_screens_controller.getCurrentScreenIndex() != 0){
                                Node loPrev = _screens_controller.getScreen(_screens_controller.getCurrentScreenIndex()-1);
                                if (loPrev.getId().equalsIgnoreCase("reports")) AnchorPaneMonitor.getChildren().clear();
                            }
                            _screens_controller.prevScreen();
                        } else{
                            if (_screens_controller.getCurrentScreenIndex() != _screens_controller.getScreenCount() - 1){
                                Node loFrwrd = _screens_controller.getScreen(_screens_controller.getCurrentScreenIndex() + 1);
                                if (loFrwrd.getId().equalsIgnoreCase("reports")) AnchorPaneMonitor.getChildren().clear();
                            }
                            _screens_controller.fwrdScreen();
                        }
                            
                    } 
                }
                break;
        }
    }
    
    private void showTime(){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {            
            Date date = new Date();
            String strTimeFormat = "hh:mm:";
            String strDateFormat = "MMMM dd, yyyy";
            String secondFormat = "ss";

            DateFormat timeFormat = new SimpleDateFormat(strTimeFormat + secondFormat);
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);

            String formattedTime= timeFormat.format(date);
            String formattedDate= dateFormat.format(date);

            lblDate.setText(formattedDate+ " | " + formattedTime);

            }), new KeyFrame(Duration.seconds(1))
        );
        
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }   
    
    private static XNautilus _nautilus;
    private static ScreensController _screens_controller;
    private static ScreensController _screens_dashboard_controller;
    
    //public String _no_tab_screen;
    public ArrayList<String> _no_tab;
    
    private boolean _control_pressed;
    private boolean _shift_pressed;
    
    private boolean _logged;
    
    Map<String, AnchorPane> btn = new HashMap<String,AnchorPane>();
    Map<String, Label> lbl = new HashMap<String,Label>();
    Map<String, ImageView> img = new HashMap<String,ImageView>();
}