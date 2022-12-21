package org.xersys.imbentaryofx.utility;

import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xersys.commander.base.Nautilus;
import org.xersys.commander.base.SQLConnection;
import org.xersys.commander.crypt.CryptFactory;
import org.xersys.commander.base.Property;
import org.xersys.commander.util.MiscUtil;
import org.xersys.commander.util.SQLUtil;

public class CaptureMasterList {
    public static void main(String [] args){
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
        
        
        try  {  
            File file = new File("D:/icarus/temp/Cabuyao Final.xlsx");   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            //creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);   
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file  
            
            ResultSet loRS;
            
            int lnRow = 0;
            loNautilus.beginTrans();
            while (itr.hasNext()){  
                Row row = itr.next();  
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  
                
                if (lnRow > 0){                   
                    int lnCtr = 0;
                    String lsValue;
                    
                    String lsStockIDx = "";
                    double lnUnitPrce = 0.00;
                    double lnSelPrice = 0.00;
                    
                    while (cellIterator.hasNext()){  
                        Cell cell = cellIterator.next();  

                        switch(lnCtr){
                            case 5:
                                try {
                                    lsStockIDx = cell.getStringCellValue().trim();
                                } catch (Exception e) {
                                    lsStockIDx = String.valueOf(cell.getNumericCellValue()).replace(".0", "");
                                }
                                break;
                            case 4: 
                                lnUnitPrce = cell.getNumericCellValue();
                                break;
                            case 3: 
                                lnSelPrice = cell.getNumericCellValue();
                                break;
                        }
                        lnCtr ++;
                    }  
                    
                    lsValue = "SELECT" +
                                    "  a.sStockIDx" +
                                " FROM Inventory a" +
                                    ", Inv_Master b" +
                                " WHERE a.sStockIDx = b.sStockIDx" +
                                    " AND b.sStockIDx = " + SQLUtil.toSQL(lsStockIDx) +
                                    " AND b.sBranchCd = '0004'" +
                                    " AND a.cRecdStat = '1'";
                    loRS = loNautilus.executeQuery(lsValue);
                    
                    if (loRS.next()){
                        lsValue = "UPDATE Inventory SET" +
                                        "  nSelPrce1 = " + lnSelPrice +
                                        ", nUnitPrce = " + lnUnitPrce +
                                        ", cRecdStat = '1'" +
                                        ", dModified = " + SQLUtil.toSQL(loNautilus.getServerDate()) +
                                    " WHERE sStockIDx = " + SQLUtil.toSQL(lsStockIDx);
                        
                        if (loNautilus.executeUpdate(lsValue, "Inventory", "0004", "0004") <= 0) {
                            System.err.println(loNautilus.getMessage());
                            loNautilus.rollbackTrans();
                            System.exit(1);
                        }            
                    } else {
                        System.err.println("No record found for " + lsStockIDx);
                    } 
                }
                
                lnRow++;
            }  
            loNautilus.commitTrans();
        } catch(Exception e) {  
            e.printStackTrace();  
            loNautilus.rollbackTrans();
            System.exit(1);
        }  
    }
}
