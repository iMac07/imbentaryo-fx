package org.xersys.imbentaryofx.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xersys.commander.base.Nautilus;
import org.xersys.commander.base.Property;
import org.xersys.commander.base.SQLConnection;
import org.xersys.commander.crypt.CryptFactory;
import org.xersys.commander.util.MiscUtil;
import org.xersys.commander.util.SQLUtil;
import org.xersys.commander.util.StringHelper;
import org.xersys.commander.util.StringUtil;

public class CapturePartsCatalogue {
    final static String BRANDCODE = "Yamaha";
    final static String PRODUCTID = "Daedalus";
    
    public static void main(String [] args){
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
        
        //captureFigures(loNautilus);
        captureParts(loNautilus);
    }
    
    private static void captureParts(Nautilus loNautilus){
        try  {  
            File file = new File("d:\\icarus\\temp\\1XBJN450E1 - Mio Gear.xlsx");   //creating a new file instance  
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
            //creating Workbook instance that refers to .xlsx file  
            XSSFWorkbook wb = new XSSFWorkbook(fis);   
            XSSFSheet sheet = wb.getSheetAt(1);     //creating a Sheet object to retrieve object  
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file  
            
            ResultSet loRS;
            
            int lnRow = 0;
            loNautilus.beginTrans();
            while (itr.hasNext()){  
                Row row = itr.next();  
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column  
                
                if (lnRow > 0){
                    int lnCtr = 0;
                    String lsCode = "";
                    int lnNmbr= 0;
                    int lnQty = 0;
                    String lsBarcodex = "";
                    String lsDescript = "";
                    String lsSeries = "";
                    String lsSQL = "";
                    String lsStockIDx = "";
                    int lnAdd = 0;

                    while (cellIterator.hasNext()){  
                        Cell cell = cellIterator.next();  

                        switch(lnCtr){
                            case 0:
                                lsCode = cell.getStringCellValue().trim();
                                break;
                            case 1:
                                lnNmbr = Integer.parseInt(String.valueOf(cell.getNumericCellValue()).replace(".0", ""));
                                break;
                            case 2:
                                lsBarcodex = cell.getStringCellValue().trim();
                                break;
                            case 3:
                                lsDescript = cell.getStringCellValue().trim();
                                break;
                            case 5:
                                lnQty = Integer.parseInt(String.valueOf(cell.getNumericCellValue()).replace(".0", ""));
                                break;
                        }
                        lnCtr ++;
                    }  
                    
                    lsSQL = "SELECT" +
                                " sStockIDx" +
                            " FROM Inventory" +
                            " WHERE sBarCodex = " + SQLUtil.toSQL(lsBarcodex) +
                                " AND sBrandCde = " + SQLUtil.toSQL(BRANDCODE.toUpperCase()) +
                                " AND sInvTypCd = 'SP'";
                    
                    loRS = loNautilus.executeQuery(lsSQL);
                    
                    if (!loRS.next()){
                        lsStockIDx = MiscUtil.getNextCode("Inventory", "sStockIDx", true, loNautilus.getConnection().getConnection(), (String) loNautilus.getBranchConfig("sBranchCd"));
                        
                        lsSQL = "INSERT INTO Inventory SET" +
                                    "  sStockIDx = " + SQLUtil.toSQL(lsStockIDx) +
                                    ", sBarCodex = " + SQLUtil.toSQL(lsBarcodex) +
                                    ", sDescript = " + SQLUtil.toSQL(lsDescript) +
                                    ", sBriefDsc = ''" +
                                    ", sAltBarCd = " + SQLUtil.toSQL("") +
                                    ", sCategrCd = ''" +
                                    ", sBrandCde = " + SQLUtil.toSQL(BRANDCODE.toUpperCase()) +
                                    ", sModelCde = ''" +
                                    ", sColorCde = ''" +
                                    ", sInvTypCd = 'SP'" +
                                    ", nUnitPrce = 0.00" +
                                    ", nSelPrce1 = 0.00" +
                                    ", cComboInv = '0'" +
                                    ", cWthPromo = '0'" +
                                    ", cSerialze = '0'" +
                                    ", cInvStatx = '1'" +
                                    ", sSupersed = ''" +
                                    ", cRecdStat = '1'" +
                                    ", dModified = " + SQLUtil.toSQL(loNautilus.getServerDate());
                        
                        if (loNautilus.executeUpdate(lsSQL) <= 0) {
                            System.err.println(loNautilus.getMessage());
                            loNautilus.rollbackTrans();
                            System.exit(1);
                        }
                    } else {
                        lsStockIDx = loRS.getString("sStockIDx");
                    }
                    
                    lsSQL = "INSERT INTO Inv_Master SET" +
                            "  sStockIDx = " + SQLUtil.toSQL(lsStockIDx) +
                            ", sBranchCd = " + SQLUtil.toSQL((String) loNautilus.getBranchConfig("sBranchCd")) +
                            ", sLocatnCd = ''" + 
                            ", nBinNumbr = 0" + 
                            ", dAcquired = NULL" +
                            ", dBegInvxx = " + SQLUtil.toSQL(SQLUtil.dateFormat(loNautilus.getServerDate(), SQLUtil.FORMAT_SHORT_DATE)) +
                            ", nBegQtyxx = 0" + 
                            ", nQtyOnHnd = 0" + 
                            ", nMinLevel = 0" + 
                            ", nMaxLevel = 0" + 
                            ", nAvgMonSl = 0.00" + 
                            ", nAvgCostx = 0.00" + 
                            ", cClassify = 'F'" + 
                            ", nBackOrdr = 0" + 
                            ", nResvOrdr = 0" + 
                            ", nFloatQty = 0" + 
                            ", cRecdStat = '1'" + 
                            ", dDeactive = NULL" + 
                            ", dModified = " + SQLUtil.toSQL(loNautilus.getServerDate()) +
                        " ON DUPLICATE KEY UPDATE" +
                            " dModified = " + SQLUtil.toSQL(loNautilus.getServerDate());
                    
                    if (loNautilus.executeUpdate(lsSQL) <= 0) {
                        System.err.println(loNautilus.getMessage());
                        loNautilus.rollbackTrans();
                        System.exit(1);
                    }
                    
                    
                    if (!lsCode.isEmpty()){
                        lnAdd = Integer.parseInt(lsCode) + 76; //38
                                
                        lsCode = StringHelper.prepad(String.valueOf(lnAdd), 6, '0');
                        
                        lsSQL = "INSERT INTO Catalog_Parts SET" +
                                "  sFigureID = " + SQLUtil.toSQL(lsCode) + 
                                ", nEntryNox = " + lnNmbr + 
                                ", sStockIDx = " + SQLUtil.toSQL(lsStockIDx) + 
                                ", nQuantity = " + lnQty +
                                ", dModified = " + SQLUtil.toSQL(loNautilus.getServerDate());

                        if (loNautilus.executeUpdate(lsSQL) <= 0) {
                            System.err.println(loNautilus.getMessage());
                            loNautilus.rollbackTrans();
                            System.exit(1);
                        }
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
    
    private static void captureFigures(Nautilus loNautilus) {
        try  {  
            File file = new File("d:\\icarus\\temp\\1XBJN450E1 - Mio Gear.xlsx");   //creating a new file instance  
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
                    String lsCode = "";
                    String lsImage = "";
                    String lsType = "";
                    String lsDescript = "";
                    String lsSeries = "";
                    String lsSQL = "";

                    while (cellIterator.hasNext()){  
                        Cell cell = cellIterator.next();  

                        switch(lnCtr){
                            case 0:
                                lsCode = cell.getStringCellValue().trim();
                                break;
                            case 1:
                                lsImage = String.valueOf(cell.getNumericCellValue()).replace(".0", ".jpg");
                                break;
                            case 2:
                                lsType = cell.getStringCellValue().trim();
                                break;
                            case 3:
                                lsDescript = cell.getStringCellValue().trim();
                                break;
                            case 4: //5,6
                                lsSeries = cell.getStringCellValue().trim();
                                break;
                        }
                        lnCtr ++;
                    }  
                    
                    lsSQL = "INSERT INTO Catalog_Figures SET" +
                            "  sFigureID = " + SQLUtil.toSQL(MiscUtil.getNextCode("Catalog_Figures", "sFigureID", false, loNautilus.getConnection().getConnection(), "")) +
                            ", sDescript = " + SQLUtil.toSQL(lsDescript) +
                            ", sImageNme = " + SQLUtil.toSQL("images/" + BRANDCODE + "/" + lsCode + "/" + lsType + "/" + lsImage) +
                            ", sCategrCd = " + SQLUtil.toSQL(lsType.toUpperCase()) +
                            ", sModelCde = " + SQLUtil.toSQL(lsCode) +
                            ", sBrandCde = " + SQLUtil.toSQL(BRANDCODE.toUpperCase()) +
                            ", sSeriesID = " + SQLUtil.toSQL(lsSeries);
                    
                    if (loNautilus.executeUpdate(lsSQL) <= 0) {
                        System.err.println(loNautilus.getMessage());
                        loNautilus.rollbackTrans();
                        System.exit(1);
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
