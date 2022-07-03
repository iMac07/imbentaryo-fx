package org.xersys.imbentaryofx.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        delPrevDayTrans(loNautilus);
        
        Imbentaryo _instance = new Imbentaryo();
        _instance.setNautilus(loNautilus);
        
        Application.launch(_instance.getClass());
    }
    
    private static void delPrevDayTrans(XNautilus foNautilus){
        String lsDate = SQLUtil.dateFormat(foNautilus.getServerDate(), SQLUtil.FORMAT_SHORT_DATE);                
                
        String lsSQL = "DELETE FROM xxxTempTransactions WHERE dCreatedx < " + SQLUtil.toSQL(lsDate + " 00:00:00");
        
        foNautilus.executeUpdate(lsSQL);
    }
    
    private static boolean loadProperties(){
        try {
            Properties po_props = new Properties();
            po_props.load(new FileInputStream(System.getProperty("sys.default.path.config") + "app-config.properties"));
            
            System.setProperty("store.company.name", po_props.getProperty("store.company.name"));
            
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
