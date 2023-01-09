package org.xersys.imbentaryofx.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import javafx.application.Application;
import org.xersys.imbentaryofx.gui.Imbentaryo;
import org.xersys.commander.base.Nautilus;
import org.xersys.commander.base.Property;
import org.xersys.commander.base.SQLConnection;
import org.xersys.commander.crypt.CryptFactory;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.MiscUtil;
import org.xersys.commander.util.SQLUtil;
import org.xersys.inventory.roq.SPROQProc;

public class Login {
    public static void main (String [] args){
        final String PRODUCTID = "Daedalus";
               
        //get database property
        Property loConfig = new Property("db-config.properties", PRODUCTID);
        if (!loConfig.loadConfig()){
            System.err.println(loConfig.getMessage());
            System.exit(1);
        } else System.out.println("Database configuration was successfully loaded.");
        
        //connect to database
        SQLConnection loConn = new SQLConnection();
        loConn.setProperty(loConfig);
        if (loConn.getConnection() == null){
            System.err.println(loConn.getMessage());
            System.exit(1);
        } else
            System.out.println("Connection was successfully initialized.");        
        
        //load application driver
        Nautilus loNautilus = new Nautilus();
        
        loNautilus.setConnection(loConn);
        loNautilus.setEncryption(CryptFactory.make(CryptFactory.CrypType.AESCrypt));
        
        loNautilus.setUserID("000100210001");
        if (!loNautilus.load(PRODUCTID)){
            System.err.println(loNautilus.getMessage());
            System.exit(1);
        } else
            System.out.println("Application driver successfully initialized.");
        
        String path;
        if (System.getProperty("os.name").toLowerCase().contains("win")){
            path = (String) loNautilus.getAppConfig("sApplPath");
        } else {
            path = "/srv/icarus/";
        }
        
        System.setProperty("sys.default.path.config", path);
        System.setProperty("store.branch.code", (String) loNautilus.getBranchConfig("sBranchCd"));
        System.setProperty("store.branch.address", (String) loNautilus.getBranchConfig("sAddressx") + " " + (String) loNautilus.getBranchConfig("xTownName"));
        
        loadProperties();
        updateTables(loNautilus);
        
        Imbentaryo _instance = new Imbentaryo();
        _instance.setNautilus(loNautilus);
        
        Application.launch(_instance.getClass());
    }
    
    private static void updateTables(XNautilus foNautilus){
        String lsSQL;
        ResultSet loMaster, loDetail;
        String lsDate = SQLUtil.dateFormat(foNautilus.getServerDate(), SQLUtil.FORMAT_SHORT_DATE);                
        
        System.out.println("Deleting previous day transaction cache. Please wait.");
        lsSQL = "DELETE FROM xxxTempTransactions WHERE dCreatedx < " + SQLUtil.toSQL(lsDate + " 00:00:00");
        foNautilus.executeUpdate(lsSQL);
        
        try {
            System.out.println("Checking price change effective today.");
            lsSQL = "SELECT sTransNox" +
                    " FROM Price_Change_Master" +
                    " WHERE cTranStat = '1'" +
                        " AND dEffectve = " + SQLUtil.toSQL(lsDate);
            loMaster = foNautilus.executeQuery(lsSQL);

            while (loMaster.next()){                
                lsSQL = "SELECT" +
                            "  sStockIDx" +
                            ", nUnitPrce" +
                            ", nSelPrce1" + 
                        " FROM Price_Change_Detail" + 
                        " WHERE sTransNox = " + SQLUtil.toSQL(loMaster.getString("sTransNox"));
                
                loDetail = foNautilus.executeQuery(lsSQL);
                
                if (MiscUtil.RecordCount(loDetail) > 0){
                    System.out.println("Updating inventory price from " + loMaster.getString("sTransNox"));
                    
                    foNautilus.beginTrans();
                    
                    while (loDetail.next()){
                        lsSQL = "UPDATE Inventory SET" +
                                    "  nUnitPrce = " + loDetail.getDouble("nUnitPrce") +
                                    ", nSelPrce1 = " + loDetail.getDouble("nSelPrce1") +
                                    ", dModified = " + SQLUtil.toSQL(foNautilus.getServerDate()) +
                                " WHERE sStockIDx = " + SQLUtil.toSQL(loDetail.getString("sStockIDx"));
                        
                        foNautilus.executeUpdate(lsSQL, "Inventory", System.getProperty("store.branch.code"), "");
                        
                        if (!foNautilus.getMessage().isEmpty()){
                            System.err.println("Error updating...");
                            System.err.println(foNautilus.getMessage());
                            foNautilus.rollbackTrans();
                            System.exit(1);
                        }
                    }
                    
                    lsSQL = "UPDATE Price_Change_Master SET" +
                            " cTranStat = '2'" + 
                            " WHERE sTransNox = " + SQLUtil.toSQL(loMaster.getString("sTransNox"));
                    
                    foNautilus.executeUpdate(lsSQL, "Price_Change_Master", System.getProperty("store.branch.code"), "");
                        
                    if (!foNautilus.getMessage().isEmpty()){
                        System.err.println("Error updating...");
                        System.err.println(foNautilus.getMessage());
                        foNautilus.rollbackTrans();
                        System.exit(1);
                    }
                    
                    lsSQL = "INSERT INTO Price_Change_Update SET" +
                            "  sTransNox = " + SQLUtil.toSQL(MiscUtil.getNextCode("Price_Change_Update", "sTransNox", true, foNautilus.getConnection().getConnection(), System.getProperty("store.branch.code"))) +
                            ", sBranchCd = " + SQLUtil.toSQL(System.getProperty("store.branch.code")) +
                            ", sUpdatedx = " + SQLUtil.toSQL(loMaster.getString("sTransNox")) + 
                            ", dModified = " + SQLUtil.toSQL(foNautilus.getServerDate()); 
                    
                    foNautilus.executeUpdate(lsSQL, "Price_Change_Update", System.getProperty("store.branch.code"), "");
                        
                    if (!foNautilus.getMessage().isEmpty()){
                        System.err.println("Error updating...");
                        System.err.println(foNautilus.getMessage());
                        foNautilus.rollbackTrans();
                        System.exit(1);
                    }
                
                    foNautilus.commitTrans();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static boolean loadProperties(){
        try {
            Properties po_props = new Properties();
            po_props.load(new FileInputStream(System.getProperty("sys.default.path.config") + "app-config.properties"));
            
            System.setProperty("store.company.name", po_props.getProperty("store.company.name"));
            System.setProperty("app.sales.allow.backdate", po_props.getProperty("app.sales.allow.backdate"));
            
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
