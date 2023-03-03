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
import org.xersys.inventory.base.InvPriceUpdate;

public class CaptureUpdateDesc {
    public static void main(String [] args){
        final String BRANDCODE = "HONDA";
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
            File file = new File("D:/icarus/temp/lingunan-UPDATED.xlsx");   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            //creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);   
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file  
                        
            int lnRow = 0;
            String lsSQL = "";
            String lsTransNox = "";
            
            loNautilus.beginTrans();
            
            while (itr.hasNext()){  
                Row row = itr.next();  
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  
                
                if (lnRow > 0){                    
                    int lnCtr = 0;                    
                    String lsBarCodex = "";
                    String lsBrand = "";
                    String lsNewBCode = "";
                    String lsNewDescx = "";
                    
                    while (cellIterator.hasNext()){  
                        Cell cell = cellIterator.next();  

                        switch(lnCtr){
                            case 0:
                                try {
                                    lsBarCodex = cell.getStringCellValue().trim();
                                } catch (Exception e) {
                                    lsBarCodex = String.valueOf(cell.getNumericCellValue()).replace(".0", "");
                                }
                                break;
                            case 10:
                                try {
                                    lsBrand = cell.getStringCellValue().trim();
                                } catch (Exception e) {
                                    lsBrand = String.valueOf(cell.getNumericCellValue()).replace(".0", "");
                                }
                                
                                break;
                            case 8:
                                try {
                                    lsNewBCode = cell.getStringCellValue().trim();
                                } catch (Exception e) {
                                    lsNewBCode = String.valueOf(cell.getNumericCellValue()).replace(".0", "");
                                }
                                break;
                            case 9:
                                try {
                                    lsNewDescx = cell.getStringCellValue().trim();
                                } catch (Exception e) {
                                    lsNewDescx = String.valueOf(cell.getNumericCellValue()).replace(".0", "");
                                }
                                break;
                        }
                        lnCtr ++;
                    } 
                    
                    if (!lsBrand.equals("-")){
                        lsSQL = "SELECT * FROM Brand" +
                                " WHERE sDescript = " + SQLUtil.toSQL(lsBrand) +
                                    " AND sInvTypCd = 'SP'";
                        ResultSet loRS = loNautilus.executeQuery(lsSQL);

                        if (loRS.next())
                            lsBrand = loRS.getString("sBrandCde");
                        else
                            lsBrand = "";
                    } else lsBrand = "";
                    
                    lsSQL = "UPDATE Inventory SET";
                    if (!lsBrand.equals("")){
                        lsSQL += "  sBrandCde = " + SQLUtil.toSQL(lsBrand);
                    } else {
                        lsSQL += "  sBrandCde = sBrandCde";
                    }
                    
                    if (!lsNewBCode.equals("-")){
                        lsSQL += ", sBarCodex = " + SQLUtil.toSQL(lsNewBCode);
                    }
                    
                    if (!lsNewDescx.equals("-")){
                        lsSQL += ", sDescript = " + SQLUtil.toSQL(lsNewDescx);
                    }
                    
                    lsSQL += " WHERE sBarCodex = " + SQLUtil.toSQL(lsBarCodex);
                    
                    if (loNautilus.executeUpdate(lsSQL, "Inventory", (String) loNautilus.getBranchConfig("sBranchCd"), "") <= 0){
                        System.err.println(loNautilus.getMessage());
                        loNautilus.rollbackTrans();
                        System.exit(1);
                    }
                }
                lnRow += 1;
            }
            loNautilus.commitTrans();
            
        } catch(Exception e) {  
            e.printStackTrace();  
            loNautilus.rollbackTrans();
            System.exit(1);
        }  
    }
}
