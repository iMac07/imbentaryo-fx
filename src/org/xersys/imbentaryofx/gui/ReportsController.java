package org.xersys.imbentaryofx.gui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.swing.JRViewer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.SQLUtil;

public class ReportsController implements Initializable, ControlledScreen{

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private AnchorPane ReportPane;

    private JasperPrint jasperPrint;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0); 
        
        //loadInventory();
        loadPayables();
        //loadFastMovingAccount();
        //loadPurchaseVSSales();
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
    
    private boolean loadInventory(){
        String lsReport = "D:/icarus/reports/Stocks.jasper";
        String lsSQL = "SELECT" +
                            "  b.sStockIDx sField00" +
                            ", b.sBarCodex sField01" +
                            ", b.sDescript sField02" +
                            ", b.sBrandCde sField03" +
                            ", a.cClassify sField04" +
                            ", a.nMinLevel nField01" +
                            ", a.nMaxLevel nField02" +
                            ", b.nSelPrce1 lField01" +
                            ", a.nQtyOnHnd nField03" +
                            ", b.nUnitPrce lField02" +
                        " FROM Inv_Master a" +
                            " LEFT JOIN Inventory b ON a.sStockIDx = b.sStockIDx" +
                        " WHERE a.sBranchCd = " + SQLUtil.toSQL((String) _nautilus.getBranchConfig("sBranchCd"));
        
        ResultSet loRS = _nautilus.executeQuery(lsSQL);
        //Convert the data-source to JasperReport data-source
        JRResultSetDataSource jrRS = new JRResultSetDataSource(loRS);
        
        //Create the parameter
        Map<String, Object> params = new HashMap<>();
        params.put("sReportNm", "Stocks");
        params.put("sPrintdBy", (String) _nautilus.getUserInfo("xClientNm"));
        params.put("sCompnyNm", "Moto Arena");
        params.put("sBranchNm", (String) _nautilus.getBranchConfig("sCompnyNm"));
        params.put("sAddressx", (String) _nautilus.getBranchConfig("sAddressx") + " " + (String) _nautilus.getBranchConfig("xTownName"));
        
        try {
            jasperPrint = JasperFillManager.fillReport(lsReport,
                                                        params, 
                                                        jrRS);
            
            if (jasperPrint != null) showReport();
        } catch (JRException e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
    private boolean loadFastMovingAccount(){
        String lsReport = "D:/icarus/reports/FastMoving.jasper";
        
        JSONArray loArray = new JSONArray();
        JSONObject loJSON = new JSONObject(); 
        loJSON.put("sField01", "90793-AP423-00");
        loJSON.put("sField02", "Yamalube Elite 800ml (24s)");
        loJSON.put("sField05", "YAMAHA");
        loJSON.put("nField01", 500);
        loJSON.put("nField02", 550);
        loJSON.put("nField03", 450);
        loJSON.put("nField04", 650);
        loJSON.put("sField03", "Low");
        loJSON.put("sField04", "Order Now");
        loArray.add(loJSON);
        
        loJSON = new JSONObject(); 
        loJSON.put("sField01", "90793-AP424-00");
        loJSON.put("sField02", "Yamalube Ellite 1L(24s)");
        loJSON.put("sField05", "YAMAHA");
        loJSON.put("nField01", 460);
        loJSON.put("nField02", 500);
        loJSON.put("nField03", 450);
        loJSON.put("nField04", 600);
        loJSON.put("sField03", "Low");
        loJSON.put("sField04", "Order Now");
        loArray.add(loJSON);
        
        loJSON = new JSONObject(); 
        loJSON.put("sField01", "90793-AP428-00");
        loJSON.put("sField02", "YM Performance SL 20L");
        loJSON.put("sField05", "YAMAHA");
        loJSON.put("nField01", 440);
        loJSON.put("nField02", 510);
        loJSON.put("nField03", 450);
        loJSON.put("nField04", 600);
        loJSON.put("sField03", "Low");
        loJSON.put("sField04", "Order Now");
        loArray.add(loJSON);
        
        loJSON = new JSONObject(); 
        loJSON.put("sField01", "90793-AP429-00");
        loJSON.put("sField02", "YM Automatic SL 800ml(24s)");
        loJSON.put("sField05", "YAMAHA");
        loJSON.put("nField01", 400);
        loJSON.put("nField02", 800);
        loJSON.put("nField03", 400);
        loJSON.put("nField04", 450);
        loJSON.put("sField03", "Medium");
        loJSON.put("sField04", "");
        loArray.add(loJSON);
        
        loJSON = new JSONObject(); 
        loJSON.put("sField01", "90793-AP807-00");
        loJSON.put("sField02", "YM Gear Oil SL100ml(36s)");
        loJSON.put("sField05", "YAMAHA");
        loJSON.put("nField01", 390);
        loJSON.put("nField02", 1000);
        loJSON.put("nField03", 400);
        loJSON.put("nField04", 450);
        loJSON.put("sField03", "High");
        loJSON.put("sField04", "");
        loArray.add(loJSON);
        
        //Create the parameter
        Map<String, Object> params = new HashMap<>();
        params.put("sReportNm", "Fast Moving Account");
        params.put("sPrintdBy", (String) _nautilus.getUserInfo("xClientNm"));
        params.put("sCompnyNm", "Moto Arena");
        params.put("sBranchNm", (String) _nautilus.getBranchConfig("sCompnyNm"));
        params.put("sAddressx", (String) _nautilus.getBranchConfig("sAddressx") + " " + (String) _nautilus.getBranchConfig("xTownName"));
        
        try {
            InputStream stream = new ByteArrayInputStream(loArray.toJSONString().getBytes("UTF-8"));
            JsonDataSource jrjson = new JsonDataSource(stream); 
            
            jasperPrint = JasperFillManager.fillReport(lsReport,
                                                        params, 
                                                        jrjson);
            
            if (jasperPrint != null) showReport();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        
        return true;
    }
    
    private boolean loadPurchaseVSSales(){
        String lsReport = "D:/icarus/reports/PurchaseSales.jasper";
        
        JSONArray loArray = new JSONArray();
        JSONObject loJSON = new JSONObject(); 
        loJSON.put("sField01", "Oct");
        loArray.add(loJSON);
        
        loJSON = new JSONObject(); 
        loJSON.put("sField01", "Nov");
        loArray.add(loJSON);
        
        loJSON.put("sField01", "Dec");
        loArray.add(loJSON);
        
        loJSON.put("sField01", "Jan");
        loArray.add(loJSON);
        
        loJSON.put("sField01", "Feb");
        loArray.add(loJSON);
        
        //Create the parameter
        Map<String, Object> params = new HashMap<>();
        params.put("sReportNm", "Purchase Vs Sales Analysis");
        params.put("sPrintdBy", (String) _nautilus.getUserInfo("xClientNm"));
        params.put("sCompnyNm", "Moto Arena");
        params.put("sBranchNm", (String) _nautilus.getBranchConfig("sCompnyNm"));
        params.put("sAddressx", (String) _nautilus.getBranchConfig("sAddressx") + " " + (String) _nautilus.getBranchConfig("xTownName"));
        
        
        params.put("sBarrCode", "90793-AP423-00");
        params.put("sDescript", "Yamalube Elite 800ml (24s)");
        params.put("sSupplier", "YAMAHA");
        params.put("nAveMonSl", 760);
        params.put("nQtyOnHnd", 600);
        params.put("nMinLevel", 500);
        
        try {
            InputStream stream = new ByteArrayInputStream(loArray.toJSONString().getBytes("UTF-8"));
            JsonDataSource jrjson = new JsonDataSource(stream); 
            
            jasperPrint = JasperFillManager.fillReport(lsReport,
                                                        params, 
                                                        jrjson);
            
            if (jasperPrint != null) showReport();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        
        return true;
    }
    
    private boolean loadPayables(){
        String lsReport = "D:/icarus/reports/MaturingPayables.jasper";
        
        String lsSQL = "SELECT" + 
                            "  d.sCompnyNm sField01" +
                            ", CONCAT(b.sInvTypCd, ' Purchase') sField02" + 
                            ", e.sClientNm sField03" +
                            ", b.sReferNox sField04" +
                            ", '' sField05" +
                            ", DATE_FORMAT(b.dTransact, '%Y-%m-%d') sField06" + 
                            ", DATE_FORMAT(b.dRefernce, '%Y-%m-%d') sField07" + 
                            ", DATE_ADD(DATE_FORMAT(b.dRefernce, '%Y-%m-%d'), INTERVAL IFNULL(c.nTermValx, 0) DAY) sField08" + 
                            ", 'OPEN' sField09" +
                            ", a.nCreditxx lField01" +  
                        " FROM AP_Ledger a" + 
                                " LEFT JOIN Client_Master e ON a.sClientID = e.sClientID" +
                            ", PO_Receiving_Master b" + 
                                " LEFT JOIN Term c ON b.sTermCode = c.sTermCode" + 
                                " LEFT JOIN xxxSysClient d ON b.sBranchCd = d.sBranchCd" +
                        " WHERE a.sSourceCd = 'DA'" + 
                            " AND a.sSourceNo = b.sTransNox" + 
                            " AND (a.nCreditxx - b.nAmtPaidx) > 0.00" + 
                            " AND b.cTranStat <> '3'";
//                        " UNION SELECT "+ 
//                            "  CONCAT(b.sInvTypCd, ' Purchase Return') xDescript" + 
//                            ", b.sTransNox sReferNox" + 
//                            ", DATE_FORMAT(b.dTransact, '%Y-%m-%d') dTransact" + 
//                            ", DATE_FORMAT(b.dTransact, '%Y-%m-%d') dRefernce" +
//                            ", DATE_FORMAT(b.dTransact, '%Y-%m-%d') dDueDatex" + 
//                            ", DATEDIFF(NOW(), DATE_FORMAT(b.dTransact, '%Y-%m-%d')) nAgexxxxx" +
//                            ", a.nCreditxx nDebitxxx" + 
//                            ", a.nDebitxxx - b.nAmtPaidx nCreditxx" + 
//                            ", a.nDebitxxx nAppliedx" + 
//                            ", a.sSourceNo sTransNox" + 
//                            ", a.sSourceCd" + 
//                            ", a.sClientID" + 
//                        " FROM AP_Ledger a" + 
//                            ", PO_Return_Master b" + 
//                                " LEFT JOIN xxxSysClient c ON a.sBranchCd = c.sBranchCd" +
//                        " WHERE a.sSourceCd = 'PR'" + 
//                            " AND a.sSourceNo = b.sTransNox" + 
//                            " AND a.dPostedxx IS NULL" + 
//                            " AND b.cTranStat <> '3'";
        
        ResultSet loRS = _nautilus.executeQuery(lsSQL);
        //Convert the data-source to JasperReport data-source
        JRResultSetDataSource jrRS = new JRResultSetDataSource(loRS);
        
        //Create the parameter
        Map<String, Object> params = new HashMap<>();
        params.put("sReportNm", "Maturing Payables");
        params.put("sPrintdBy", (String) _nautilus.getUserInfo("xClientNm"));
        params.put("sCompnyNm", "Moto Arena");
        params.put("sBranchNm", (String) _nautilus.getBranchConfig("sCompnyNm"));
        params.put("sAddressx", (String) _nautilus.getBranchConfig("sAddressx") + " " + (String) _nautilus.getBranchConfig("xTownName"));
        
        try {
            jasperPrint = JasperFillManager.fillReport(lsReport,
                                                        params, 
                                                        jrRS);
            
            if (jasperPrint != null) showReport();
        } catch (JRException e) {
            e.printStackTrace();
        }
        
        return true;
    }
    
    private void showReport(){
        SwingNode swingNode = new SwingNode();
        JRViewer jrViewer =  new JRViewer(jasperPrint);
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
