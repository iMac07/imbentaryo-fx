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
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.MiscUtil;
import org.xersys.commander.util.SQLUtil;

public class CaptureShinko {
    public static void main(String [] args){
        final String BRANDCODE = "SHINKO";
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
            File file = new File("F:\\xurpas\\Shinko.xlsx");   //creating a new file instance  
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
                                lsDescript = cell.getStringCellValue().trim();
                                break;
                            case 1:
                                lnSelPrice = cell.getNumericCellValue();
                                break;
                        }
                        lnCtr ++;
                    }  
                    
                    lnUnitPrce = 0.00;
                    lsStockIDx = MiscUtil.getNextCode("Inventory", "sStockIDx", true, loNautilus.getConnection().getConnection(), (String) loNautilus.getBranchConfig("sBranchCd"));
                    lsBarCodex = MiscUtil.getNextCode("Inventory", "sBarCodex", true, loNautilus.getConnection().getConnection(), (String) loNautilus.getBranchConfig("sBranchCd"));
                    
                    lsValue = "SELECT" +
                                    " sStockIDx" +
                                " FROM Inventory" +
                                " WHERE sDescript = " + SQLUtil.toSQL(lsDescript) +
                                    " AND sBrandCde = " + SQLUtil.toSQL(BRANDCODE) +
                                    " AND sInvTypCd = 'SP'";
                    
                    loRS = loNautilus.executeQuery(lsValue);
                    
                    if (loRS.next()){
                        lsStockIDx = loRS.getString("sStockIDx");
                        
                        lsValue = "UPDATE Inventory SET" +
                                        "  nUnitPrce = " + lnUnitPrce * 100 / 100 +
                                        ", nSelPrce1 = " + lnSelPrice * 100 / 100 +
                                        ", cRecdStat = '1'" +
                                        ", dModified = " + SQLUtil.toSQL(loNautilus.getServerDate()) +
                                    " WHERE sStockIDx = " + SQLUtil.toSQL(lsStockIDx);
                    } else {
                        lsValue = "INSERT INTO Inventory SET" +
                                    "  sStockIDx = " + SQLUtil.toSQL(lsStockIDx) +
                                    ", sBarCodex = " + SQLUtil.toSQL(lsBarCodex) +
                                    ", sDescript = " + SQLUtil.toSQL(lsDescript) +
                                    ", sBriefDsc = ''" +
                                    ", sAltBarCd = " + SQLUtil.toSQL("") +
                                    ", sCategrCd = ''" +
                                    ", sBrandCde = " + SQLUtil.toSQL(BRANDCODE) +
                                    ", sModelCde = ''" +
                                    ", sColorCde = ''" +
                                    ", sInvTypCd = 'SP'" +
                                    ", nUnitPrce = " + lnUnitPrce * 100 / 100 +
                                    ", nSelPrce1 = " + lnSelPrice * 100 / 100 +
                                    ", cComboInv = '0'" +
                                    ", cWthPromo = '0'" +
                                    ", cSerialze = '0'" +
                                    ", cInvStatx = '1'" +
                                    ", sSupersed = " + SQLUtil.toSQL("") +
                                    ", cRecdStat = '1'" +
                                    ", dModified = " + SQLUtil.toSQL(loNautilus.getServerDate());
                    }
                    
                    if (loNautilus.executeUpdate(lsValue) <= 0) {
                        System.err.println(loNautilus.getMessage());
                        loNautilus.rollbackTrans();
                        System.exit(1);
                    }
//                    
//                    lsValue = "INSERT INTO Inv_Master SET" +
//                                    "  sStockIDx = " + SQLUtil.toSQL(lsStockIDx) +
//                                    ", sBranchCd = " + SQLUtil.toSQL((String) loNautilus.getBranchConfig("sBranchCd")) +
//                                    ", sLocatnCd = ''" + 
//                                    ", nBinNumbr = 0" + 
//                                    ", dAcquired = " + SQLUtil.toSQL(SQLUtil.dateFormat(loNautilus.getServerDate(), SQLUtil.FORMAT_SHORT_DATE)) +
//                                    ", dBegInvxx = " + SQLUtil.toSQL(SQLUtil.dateFormat(loNautilus.getServerDate(), SQLUtil.FORMAT_SHORT_DATE)) +
//                                    ", nBegQtyxx = " + lnQtyOnHnd +
//                                    ", nQtyOnHnd = " + lnQtyOnHnd +
//                                    ", nMinLevel = 0" + 
//                                    ", nMaxLevel = 0" + 
//                                    ", nAvgMonSl = 0.00" + 
//                                    ", nAvgCostx = 0.00" + 
//                                    ", cClassify = 'F'" + 
//                                    ", nBackOrdr = 0" + 
//                                    ", nResvOrdr = 0" + 
//                                    ", nFloatQty = 0" + 
//                                    ", cRecdStat = '1'" + 
//                                    ", dDeactive = NULL" + 
//                                    ", dModified = " + SQLUtil.toSQL(loNautilus.getServerDate()) +
//                                " ON DUPLICATE KEY UPDATE" +
//                                    "  dAcquired = " + SQLUtil.toSQL(SQLUtil.dateFormat(loNautilus.getServerDate(), SQLUtil.FORMAT_SHORT_DATE)) +
//                                    ", dBegInvxx = " + SQLUtil.toSQL(SQLUtil.dateFormat(loNautilus.getServerDate(), SQLUtil.FORMAT_SHORT_DATE)) +
//                                    ", nBegQtyxx = " + lnQtyOnHnd +
//                                    ", nQtyOnHnd = " + lnQtyOnHnd +
//                                    ", cRecdStat = '1'" + 
//                                    ", dDeactive = NULL" + 
//                                    ", dModified = " + SQLUtil.toSQL(loNautilus.getServerDate());
//                    
//                    if (loNautilus.executeUpdate(lsValue) <= 0) {
//                        System.err.println(loNautilus.getMessage());
//                        loNautilus.rollbackTrans();
//                        System.exit(1);
//                    }
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
