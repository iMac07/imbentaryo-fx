package org.xersys.imbentaryofx.app;

import javafx.application.Application;
import org.xersys.imbentaryofx.gui.Imbentaryo;
import org.xersys.kumander.base.Nautilus;
import org.xersys.kumander.base.Property;
import org.xersys.kumander.base.SQLConnection;
import org.xersys.kumander.crypt.CryptFactory;

public class Login {
    public static void main (String [] args){
        final String PRODUCTID = "AppX";
        
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
        
        if (!loNautilus.load(PRODUCTID)){
            System.err.println(loNautilus.getMessage());
            System.exit(1);
        } else
            System.out.println("Application driver successfully initialized.");
        
        
        Imbentaryo _instance = new Imbentaryo();
        _instance.setNautilus(loNautilus);
        
        Application.launch(_instance.getClass());
    }
}
