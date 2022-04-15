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
import org.xersys.inventory.base.InvMaster;

public class CaptureSPHondaCatalog {
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
            File file = new File("D:/icarus/temp/ACB150CBTH - CLICK 150.xlsx");   //creating a new file instance  
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
                    String lsFigureID = "";
                    String lsStockIDx = "";
                    String lsBarCodex = "";
                    String lsDescript = "";
                    int lnReferNox = 0;
                    int lnQuantity = 0;
                    double lnUnitPrce = 0.00;
                    double lnSelPrice = 0.00;

                    while (cellIterator.hasNext()){  
                        Cell cell = cellIterator.next();  

                        switch(lnCtr){
                            case 0:
                                lsFigureID = cell.getStringCellValue().trim();
                                break;
                            case 1:
                                lnReferNox = (int) cell.getNumericCellValue();
                                break;
                            case 2:
                                lsBarCodex = cell.getStringCellValue().trim();
                                break;
                            case 3:
                                lsDescript = cell.getStringCellValue().trim();
                                break;
                            case 4:
                                lnQuantity = (int) cell.getNumericCellValue();
                                break;
                            case 5:
                                lnUnitPrce = cell.getNumericCellValue();
                                break;
                            case 6:
                                lnSelPrice = cell.getNumericCellValue();
                                break;
                        }
                        lnCtr ++;
                    }  

//                    System.err.println("Figure ID: " + lsFigureID);
//                    System.err.println("Refer No.: " + lnReferNox);
//                    System.err.println("Barcode: " + lsBarCodex);
//                    System.err.println("Description: " + lsDescript);
//                    System.err.println("Quantity: " + lnQuantity);
//                    System.err.println("Cost: " + lnUnitPrce);
//                    System.err.println("SRP: " + lnSelPrice);
                    //check if the part number already exist on our record
                    lsValue = "SELECT sStockIDx" +
                                " FROM Inventory" +
                                " WHERE sBarCodex = " + SQLUtil.toSQL(lsBarCodex) +
                                    " AND sBrandCde = " + SQLUtil.toSQL(BRANDCODE);
                    
                    ResultSet loRS = loNautilus.executeQuery(lsValue);
                    
                    if (loRS.next()){
                        lsStockIDx = loRS.getString("sStockIDx");
                        lsValue = "";
                        
                        if (lnUnitPrce > 0.00){
                            lsValue = lsValue + ", nUnitPrce = " + lnUnitPrce * 100 / 100;
                        }
                        
                        if (lnSelPrice > 0.00) {
                            lsValue = lsValue + ", nSelPrce1 = " + lnSelPrice * 100 / 100;
                        }
                        
                        if (!lsValue.isEmpty()){
                            lsValue = lsValue.substring(1);
                        
                            lsValue = "UPDATE Inventory SET" +
                                            lsValue +
                                        " WHERE sStockIDx = " + SQLUtil.toSQL(lsStockIDx);

                            System.out.println(lsValue);
                            if (loNautilus.executeUpdate(lsValue) <= 0) {
                                System.err.println(loNautilus.getMessage());
                                loNautilus.rollbackTrans();
                                System.exit(1);
                            }
                        }
                        
                        lsValue = "SELECT sStockIDx FROM Inv_Master" +
                                    " WHERE sStockIDx = " + SQLUtil.toSQL(lsStockIDx) +
                                        " AND sBranchCd = " + SQLUtil.toSQL((String) loNautilus.getBranchConfig("sBranchCd"));
                        
                        loRS = loNautilus.executeQuery(lsValue);
                        
                        if (!loRS.next()){
                            InvMaster loInv = new InvMaster(loNautilus, (String) loNautilus.getBranchConfig("sBranchCd"), true);
                            
                            if (loInv.NewRecord()){
                                loInv.setMaster("sStockIDx", lsStockIDx);
                                loInv.setMaster("sBranchCd", (String) loNautilus.getBranchConfig("sBranchCd"));
                                loInv.setMaster("dAcquired", loNautilus.getServerDate());
                                loInv.setMaster("dBegInvxx", loNautilus.getServerDate());
                               
                                if (!loInv.SaveRecord()){
                                    System.err.println(loInv.getMessage());
                                    loNautilus.rollbackTrans();
                                    System.exit(1);
                                }
                            }
                        }
                        
                        //insert into catalog parts
                        lsValue = "INSERT INTO Catalog_Parts SET" +
                                    "  sFigureID = " + SQLUtil.toSQL(lsFigureID) +
                                    ", nEntryNox = " + lnReferNox +
                                    ", sStockIDx = " + SQLUtil.toSQL(lsStockIDx) +
                                    ", nQuantity = " + lnQuantity +
                                    ", dModified = " + SQLUtil.toSQL(loNautilus.getServerDate());
                       
                        System.out.println(lsValue);
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
                                    ", sAltBarCd = ''" + 
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
                                    ", sSupersed = ''" +
                                    ", cRecdStat = '1'" +
                                    ", dModified = " + SQLUtil.toSQL(loNautilus.getServerDate());

                        System.out.println(lsValue);
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
