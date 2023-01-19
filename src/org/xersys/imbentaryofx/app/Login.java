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
        
        if (!Proc.loadProperties()){
            System.err.println("Unable to load configuration. Please inform you SYSTEM ADMIN.");
            System.exit(1);
        }
                
        Proc.updateTables(loNautilus);
        
        Imbentaryo _instance = new Imbentaryo();
        _instance.setNautilus(loNautilus);
        
        Application.launch(_instance.getClass());
    }
}
