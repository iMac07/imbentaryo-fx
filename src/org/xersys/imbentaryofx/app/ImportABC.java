package org.xersys.imbentaryofx.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xersys.commander.base.Nautilus;
import org.xersys.commander.base.Property;
import org.xersys.commander.base.SQLConnection;
import org.xersys.commander.crypt.CryptFactory;
import org.xersys.commander.util.SQLUtil;
import org.xersys.inventory.roq.SPROQProc;
import org.xersys.purchasing.base.PurchaseOrder;

public class ImportABC {
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
        
        Proc.loadProperties();
        PurchaseOrder loPurchase = new PurchaseOrder(loNautilus, "0001", false);
        loPurchase.setSaveToDisk(true);
        try {
            File file = new File(System.getProperty("app.path.export") + System.getProperty("app.abc.export"));   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            
            XSSFWorkbook wb = new XSSFWorkbook(fis);   
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
            Iterator<Row> itr = sheet.iterator(); 
            
            if (loPurchase.NewTransaction()){
                int lnRow = 0;                
                while (itr.hasNext()){  
                    Row row = itr.next();  
                    Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  

                    if (lnRow > 0){
                        int lnCtr = 0;
                        int lnQuantity = 0;
                        double lnUnitPrce = 0.00;
                        String lsBarCodex = "";

                        while (cellIterator.hasNext()){  
                            Cell cell = cellIterator.next();  

                            switch(lnCtr){
                                case 0:
                                    try {
                                        lsBarCodex = cell.getStringCellValue().trim();
                                    } catch (Exception e) {
                                        lsBarCodex = String.valueOf(cell.getNumericCellValue());
                                    }
                                    break;
                                case 8:
                                    lnUnitPrce = (double) cell.getNumericCellValue();
                                    break;
                                case 9:
                                    lnQuantity = (int) cell.getNumericCellValue();
                                    break;
                            }
                            lnCtr ++;
                        }
                        
                        ResultSet loRS = loNautilus.executeQuery("SELECT sStockIDx FROM Inventory WHERE sBarCodex = " + SQLUtil.toSQL(lsBarCodex));

                        if (loRS.next()){
                            loPurchase.setDetail(loPurchase.getItemCount() - 1, "sStockIDx", loRS.getString("sStockIDx"));
                            loPurchase.setDetail(loPurchase.getItemCount() - 1, "nQuantity", lnQuantity);
                            
                            if (System.getProperty("app.po.import.cost").equals("1")){
                                loPurchase.setDetail(loPurchase.getItemCount() - 1, "nUnitPrce", lnUnitPrce);
                            }
                        }
                    }
                    lnRow ++;
                }
            }
            
            wb.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
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
