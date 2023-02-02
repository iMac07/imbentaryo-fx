package org.xersys.imbentaryofx.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.MiscUtil;
import org.xersys.commander.util.SQLUtil;

public class Proc {
    public static boolean loadProperties(){
        try {
            Properties po_props = new Properties();
            po_props.load(new FileInputStream(System.getProperty("sys.default.path.config") + "app-config.properties"));
            
            System.setProperty("store.company.name", po_props.getProperty("store.company.name"));
            
            System.setProperty("app.path.image", System.getProperty("sys.default.path.config") + po_props.getProperty("app.path.image"));
            System.setProperty("app.path.reports", System.getProperty("sys.default.path.config") + po_props.getProperty("app.path.reports"));
            System.setProperty("app.path.temp", System.getProperty("sys.default.path.config") + po_props.getProperty("app.path.temp"));
            System.setProperty("app.path.purchases", System.getProperty("sys.default.path.config") + po_props.getProperty("app.path.purchases"));
            System.setProperty("app.path.templates", System.getProperty("sys.default.path.config") + po_props.getProperty("app.path.templates"));
            System.setProperty("app.path.export", System.getProperty("sys.default.path.config") + po_props.getProperty("app.path.export"));            
            
            System.setProperty("app.sales.allow.backdate", po_props.getProperty("app.sales.allow.backdate"));
            
            System.setProperty("app.mail.purchases", po_props.getProperty("app.mail.purchases"));
            System.setProperty("app.mail.cc", po_props.getProperty("app.mail.cc"));
            System.setProperty("app.mail.bcc", po_props.getProperty("app.mail.bcc"));
            
            System.setProperty("app.abc.template", po_props.getProperty("app.abc.template"));
            System.setProperty("app.abc.export", po_props.getProperty("app.abc.export"));
            System.setProperty("app.po.import", po_props.getProperty("app.po.import"));
            System.setProperty("app.po.import.cost", po_props.getProperty("app.po.import.cost"));
            
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public static void updateTables(XNautilus foNautilus){
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
}
