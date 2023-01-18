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

public class CaptureHondaPriceUpdate {
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
            File file = new File("D:/icarus/temp/Honda Price Increase Effective January 16, 2023 - ASM.xlsx");   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            //creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);   
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file  
                        
            int lnRow = 0;
            String lsSQL = "";
            String lsTransNox = "";
            
            loNautilus.beginTrans();
            
            lsTransNox = MiscUtil.getNextCode("Price_Change_Master", "sTransNox", true, loNautilus.getConnection().getConnection(), (String) loNautilus.getBranchConfig("sBranchCd"));
            lsSQL = "INSERT INTO Price_Change_Master SET" +
                    "  sTransNox = " + SQLUtil.toSQL(lsTransNox) +
                    ", sBranchCd = " + SQLUtil.toSQL((String) loNautilus.getBranchConfig("sBranchCd")) +
                    ", dTransact = " + SQLUtil.toSQL(loNautilus.getServerDate()) +
                    ", dEffectve = '2023-01-17'" +
                    ", sReferNox = '0'" +
                    ", sRemarksx = 'Honda Price Increase Effective January 16, 2023'" +
                    ", cTranStat = '0'" +
                    ", sModified = " + SQLUtil.toSQL((String) loNautilus.getUserInfo("sUserIDxx"));
            
            if (loNautilus.executeUpdate(lsSQL, "Price_Change_Master", (String) loNautilus.getBranchConfig("sBranchCd"), "") <= 0){
                loNautilus.rollbackTrans();
                System.err.println(loNautilus.getMessage());
                System.exit(1);
            }
            
            while (itr.hasNext()){  
                Row row = itr.next();  
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  
                
                if (lnRow > 0){                    
                    int lnCtr = 0;
                    String lsValue;
                    String lsDescript = "";
                    String lsBarCodex = "";
                    String lsStockIDx = "";
                    double lnUnitPrce = 0.00;
                    double lnSelPrice = 0.00;
                    int lnQtyOnHnd = 0;
                    
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
                            case 1:
                                try {
                                    lsDescript = cell.getStringCellValue().trim();
                                } catch (Exception e) {
                                    lsDescript = String.valueOf(cell.getNumericCellValue()).replace(".0", "");
                                }
                                
                                break;
                            case 2:
                                lnSelPrice = cell.getNumericCellValue();
                                break;
                            case 3:
                                lnUnitPrce = cell.getNumericCellValue();
                                break;
                        }
                        
                        lnCtr ++;
                    }  
                    
                    lsValue = "SELECT * FROM Inventory WHERE sBarCodex = " + SQLUtil.toSQL(lsBarCodex);
                    ResultSet loDetail = loNautilus.executeQuery(lsValue);
                    
                    if (loDetail.next()){
                        lsSQL = "INSERT INTO Price_Change_Detail SET" + 
                                "  sTransNox = " + SQLUtil.toSQL(lsTransNox) +
                                ", nEntryNox = " + lnRow +
                                ", sStockIDx = " + SQLUtil.toSQL(loDetail.getString("sStockIDx")) +
                                ", nUnitPrce = " + lnUnitPrce +
                                ", nSelPrce1 = " + lnSelPrice + 
                                ", nSelPrce2 = 0.00" +
                                ", nSelPrce3 = 0.00" +
                                ", nDiscLev1 = 0.00" +
                                ", nDiscLev2 = 0.00" +
                                ", nDiscLev3 = 0.00";
                        
                        if (loNautilus.executeUpdate(lsSQL, "Price_Change_Detail", (String) loNautilus.getBranchConfig("sBranchCd"), "") <= 0){
                            loNautilus.rollbackTrans();
                            System.err.println(loNautilus.getMessage());
                            System.exit(1);
                        }
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
