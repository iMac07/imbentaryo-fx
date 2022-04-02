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

public class CaptureSPYamaha {
    public static void main(String [] args){
        final String BRANDCODE = "YAMAHA";
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
            File file = new File("F:\\xurpas\\Yamaha Pricelist 2022.xlsx");   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            //creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);   
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object  
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file  
            
            int lnRow = 0;
            loNautilus.beginTrans();
            
            while (itr.hasNext()){  
                Row row = itr.next();  
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  
                
                if (lnRow > 0){
                    int lnCtr = 0;
                    String lsValue;
                    String lsBarCodex = "";
                    String lsBarCodey = "";
                    String lsAltBarCd = "";
                    String lsSupersed = "";
                    String lsDescript = "";
                    double lnUnitPrce = 0.00;
                    double lnSelPrice = 0.00;

                    while (cellIterator.hasNext()){  
                        Cell cell = cellIterator.next();  

                        switch(lnCtr){
                            case 0:
                                lsBarCodex = cell.getStringCellValue().trim();
                                break;
                            case 1:
                                lsBarCodey = cell.getStringCellValue().trim();
                                break;
                            case 2:
                                lsSupersed = cell.getStringCellValue().trim();
                                if (lsSupersed.length() == 1)
                                    if (lsSupersed.equals("-"))
                                        lsSupersed = "";
                                break;
                            case 3:
                                lsDescript = cell.getStringCellValue().trim();
                                break;                            
                            case 4:
                                lnSelPrice = cell.getNumericCellValue();
                                break;
                            case 5:
                                lnUnitPrce = cell.getNumericCellValue();
                                break;
//                            case 6:
//                                lsAltBarCd = cell.getStringCellValue().trim();
//                                if (lsAltBarCd.length() == 1)
//                                    if (lsAltBarCd.equals("-"))
//                                        lsAltBarCd = "";
//                                break;
                        }
                        lnCtr ++;
                    }  

                    //check if the part number already exist on our record
                    lsValue = "SELECT sStockIDx" +
                                " FROM Inventory" +
                                " WHERE sBarCodex = " + SQLUtil.toSQL(lsBarCodex) +
                                    " AND sBrandCde = " + SQLUtil.toSQL(BRANDCODE);
                    
                    ResultSet loRS = loNautilus.executeQuery(lsValue);
                    
                    if (loRS.next()){
                        lsValue = "";
                        
                        if (!lsSupersed.isEmpty())
                            lsValue = ", sSupersed = " + SQLUtil.toSQL(lsSupersed);
                        
                        if (!lsAltBarCd.isEmpty())
                            lsValue = lsValue + ", sAltBarCd = " + SQLUtil.toSQL(lsAltBarCd);
                        
                        lsValue = lsValue + 
                                    ", nUnitPrce = " + lnUnitPrce * 100 / 100 +
                                    ", nSelPrce1 = " + lnSelPrice * 100 / 100;
                        
                        lsValue = lsValue.substring(1);
                        
                        lsValue = "UPDATE Inventory SET" +
                                        lsValue +
                                    " WHERE sStockIDx = " + SQLUtil.toSQL(loRS.getString("sStockIDx"));
                           
                        if (loNautilus.executeUpdate(lsValue) <= 0) {
                            System.err.println(loNautilus.getMessage());
                            loNautilus.rollbackTrans();
                            System.exit(1);
                        }
                    } else {
                        lsValue = CommonUtil.getNextCode("Inventory", "sStockIDx", true, loNautilus.getConnection().getConnection(), (String) loNautilus.getBranchConfig("sBranchCd"));                    
                        lsValue = "INSERT INTO Inventory SET" +
                                    "  sStockIDx = " + SQLUtil.toSQL(lsValue) +
                                    ", sBarCodex = " + SQLUtil.toSQL(lsBarCodex) +
                                    ", sDescript = " + SQLUtil.toSQL(lsDescript) +
                                    ", sBriefDsc = ''" +
                                    ", sAltBarCd = " + SQLUtil.toSQL(lsAltBarCd) +
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
                                    ", sSupersed = " + SQLUtil.toSQL(lsSupersed) +
                                    ", cRecdStat = '1'" +
                                    ", dModified = " + SQLUtil.toSQL(loNautilus.getServerDate());

                        if (loNautilus.executeUpdate(lsValue) <= 0) {
                            System.err.println(loNautilus.getMessage());
                            loNautilus.rollbackTrans();
                            System.exit(1);
                        }
                    }
                    
                    MiscUtil.close(loRS);
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
