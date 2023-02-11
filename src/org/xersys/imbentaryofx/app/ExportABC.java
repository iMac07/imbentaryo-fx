package org.xersys.imbentaryofx.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xersys.commander.base.Nautilus;
import org.xersys.commander.base.Property;
import org.xersys.commander.base.SQLConnection;
import org.xersys.commander.crypt.CryptFactory;
import org.xersys.commander.util.MiscUtil;
import org.xersys.inventory.roq.SPROQProc;

public class ExportABC {
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

        //test classify
        SPROQProc loClassify = new SPROQProc(loNautilus, System.getProperty("store.branch.code"));
        ResultSet loRS = loClassify.getLastClassify();
        
        try {            
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(System.getProperty("app.path.temp") + "/template/PO.xlsx"));
            Sheet sheet = workbook.getSheetAt(0);
            int lnRow = sheet.getLastRowNum() + 1;
            
            while (loRS.next()){
                Row row = sheet.createRow(lnRow++);
                Cell cell = row.createCell(0);
                cell.setCellValue(loRS.getString("sBarCodex"));
                cell = row.createCell(1);
                cell.setCellValue(loRS.getString("sDescript"));
                cell = row.createCell(2);
                cell.setCellValue(loRS.getString("sBrandNme"));
                cell = row.createCell(3);
                cell.setCellValue(loRS.getString("cClassify"));
                cell = row.createCell(4);
                cell.setCellValue(loRS.getInt("nAvgMonSl"));
                cell = row.createCell(5);
                cell.setCellValue(loRS.getInt("nMinLevel"));
                cell = row.createCell(6);
                cell.setCellValue(loRS.getInt("nMaxLevel"));
                cell = row.createCell(7);
                cell.setCellValue(loRS.getInt("nQtyOnHnd"));
                cell = row.createCell(8);
                cell.setCellValue(loRS.getDouble("nUnitPrce"));
                cell = row.createCell(9);
                cell.setCellValue((int) 0);
            }
            
            FileOutputStream out = new FileOutputStream("D:/icarus/temp/export/PO-New.xlsx");
            workbook.write(out);
            out.close();
            
            ArrayList<String> loArr = new ArrayList<>();
            loRS.first();
            while(loRS.next()){
                if (loArr.isEmpty()){
                    loArr.add(loRS.getString("sBrandNme"));
                } else {
                    boolean lbExist = false;
                    for (int lnCtr = 0; lnCtr <= loArr.size()-1; lnCtr++){
                        if (loArr.get(lnCtr).equals(loRS.getString("sBrandNme"))){
                            lbExist = true;
                        }
                    }
                    if (!lbExist) loArr.add(loRS.getString("sBrandNme"));
                }
            }
            
            for (int lnCtr = 0; lnCtr <= loArr.size()-1; lnCtr++){
                workbook = new XSSFWorkbook(new FileInputStream(System.getProperty("app.path.temp") + "/template/PO.xlsx"));
                sheet = workbook.getSheetAt(0);
                lnRow = sheet.getLastRowNum() + 1;
                
                loRS.first();
                while (loRS.next()){
                    if (loRS.getString("sBrandNme").equals(loArr.get(lnCtr))){
                        Row row = sheet.createRow(lnRow++);
                        Cell cell = row.createCell(0);
                        cell.setCellValue(loRS.getString("sBarCodex"));
                        cell = row.createCell(1);
                        cell.setCellValue(loRS.getString("sDescript"));
                        cell = row.createCell(2);
                        cell.setCellValue(loRS.getString("sBrandNme"));
                        cell = row.createCell(3);
                        cell.setCellValue(loRS.getString("cClassify"));
                        cell = row.createCell(4);
                        cell.setCellValue(loRS.getInt("nAvgMonSl"));
                        cell = row.createCell(5);
                        cell.setCellValue(loRS.getInt("nMinLevel"));
                        cell = row.createCell(6);
                        cell.setCellValue(loRS.getInt("nMaxLevel"));
                        cell = row.createCell(7);
                        cell.setCellValue(loRS.getInt("nQtyOnHnd"));
                        cell = row.createCell(8);
                        cell.setCellValue(loRS.getDouble("nUnitPrce"));
                        cell = row.createCell(9);
                        cell.setCellValue((int) 0);
                    }
                }
                
                out = new FileOutputStream("D:/icarus/temp/export/" + loArr.get(lnCtr) + ".xlsx");
                workbook.write(out);
                out.close();
            }
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
