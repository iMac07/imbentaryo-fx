package org.xersys.imbentaryofx.gui;

import static java.lang.Thread.sleep;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.xersys.commander.iface.XNautilus;

public class DashboardController implements Initializable, ControlledScreen {
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private TreeView tvReminders;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);
        
        createJSON();
        startSession();
    }       

    @Override
    public void setNautilus(XNautilus foValue) {
        _nautilus = foValue;
    }    

    @Override
    public void setParentController(MainScreenController foValue) {
        _main_screen_controller = foValue;
    }
    
    @Override
    public void setScreensController(ScreensController foValue) {
        _screens_controller = foValue;
    }
    
    @Override
    public void setDashboardScreensController(ScreensController foValue) {
    }
    
    XNautilus _nautilus;
    MainScreenController _main_screen_controller;
    ScreensController _screens_controller;

    private  void createJSON(){
        JSONArray laMaster, laDetail, laData;
        JSONObject loMaster, loDetail;
        
        laMaster = new JSONArray();
        
        laDetail = new JSONArray();
        loMaster = new JSONObject();
        
        laData = new JSONArray();
        laData.add("Amount: " + 1000.00);
        laData.add("Due Date: 2022-10-01");
        laData.add("Mobile No: 09260375777");
        
        loDetail = new JSONObject();
        loDetail.put("parent", "Sabiniano, Jonathan");
        loDetail.put("child", laData);
        laDetail.add(loDetail);
        
        laData = new JSONArray();
        laData.add("Amount: " + 1000.00);
        laData.add("Due Date: 2022-10-01");
        laData.add("Mobile No: 09260375777");
        
        loDetail = new JSONObject();
        loDetail.put("parent", "Garcia, Jonathan");
        loDetail.put("child", laData);
        laDetail.add(loDetail);
        
        laData = new JSONArray();
        laData.add("Amount: " + 1000.00);
        laData.add("Due Date: 2022-10-01");
        laData.add("Mobile No: 09260375777");
        
        loDetail = new JSONObject();
        loDetail.put("parent", "Valencia, Maynard");
        loDetail.put("child", laData);
        laDetail.add(loDetail);
        
        loMaster.put("parent", "Account Receivables");
        loMaster.put("child", laDetail);          
        laMaster.add(loMaster);
        
        loMaster = new JSONObject();
        laDetail = new JSONArray();
        
        loDetail = new JSONObject();
        loDetail.put("parent", "Check 1");
        laDetail.add(loDetail);
        
        loDetail = new JSONObject();
        loDetail.put("parent", "Check 2");
        laDetail.add(loDetail);
        
        loDetail = new JSONObject();
        loDetail.put("parent", "Check 3");
        laDetail.add(loDetail);
        
        loMaster.put("parent", "For Deposit - Check");
        loMaster.put("child", laDetail);
        laMaster.add(loMaster);
        
        loMaster = new JSONObject();
        laDetail = new JSONArray();
        
        loDetail = new JSONObject();
        loDetail.put("parent", "De Celis, Teejei");
        laDetail.add(loDetail);
        
        loDetail = new JSONObject();
        loDetail.put("parent", "Cerezo, Rowel");
        laDetail.add(loDetail);
        
        loDetail = new JSONObject();
        loDetail.put("parent", "De Vera, Genesis");
        laDetail.add(loDetail);
        
        loMaster.put("parent", "For Release Orders");
        loMaster.put("child", laDetail);
        laMaster.add(loMaster);
        
        loMaster = new JSONObject();
        laDetail = new JSONArray();
        
        loDetail = new JSONObject();
        loDetail.put("parent", "01110609 | WASHER-PLAIN");
        laDetail.add(loDetail);
        
        loDetail = new JSONObject();
        loDetail.put("parent", "11001-1499 | HEAD CYLINDER");
        laDetail.add(loDetail);
        
        loDetail = new JSONObject();
        loDetail.put("parent", "11002-0126 | HEAD-ASSY-CYLINDER");
        laDetail.add(loDetail);
        
        loMaster.put("parent", "For Replenisments Items");
        loMaster.put("child", laDetail);
        laMaster.add(loMaster);
        
        laDetail = new JSONArray();
        loMaster = new JSONObject();
        
        laData = new JSONArray();
        laData.add("Previous: 2022-10-01");
        laData.add("Engine No: 4D56AAG8242");
        laData.add("Mobile No: 09260375777");
        
        
        loDetail = new JSONObject();
        loDetail.put("parent", "Sabiniano, Jonathan");
        loDetail.put("child", laData);
        laDetail.add(loDetail);
        
        laData = new JSONArray();
        laData.add("Previous: 2022-10-01");
        laData.add("Engine No: 4D56AAG8242");
        laData.add("Mobile No: 09260375777");
        
        loDetail = new JSONObject();
        loDetail.put("parent", "Garcia, Michael");
        loDetail.put("child", laData);
        laDetail.add(loDetail);
        
        laData = new JSONArray();
        laData.add("Previous: 2022-10-01");
        laData.add("Engine No: 4D56AAG8242");
        laData.add("Mobile No: 09260375777");
        
        loDetail = new JSONObject();
        loDetail.put("parent", "Valencia, Maynard");
        loDetail.put("child", laData);
        laDetail.add(loDetail);
        
        loMaster.put("parent", "Service Reminder");
        loMaster.put("child", laDetail);      
        laMaster.add(loMaster);
                
        dissectJSON(laMaster.toJSONString()); 
        _seconds = 0;
    }
    
    private void dissectJSON(String fsValue){
        JSONParser loParser = new JSONParser();

        //convert string to JSONArray
        JSONArray laMaster;
        try {
            laMaster = (JSONArray) loParser.parse(fsValue);

            JSONArray laDetail;
            JSONArray laDetail2;
            JSONObject loParent;
            JSONObject loDetail;
            //we know that content of the json array is always a json object
            //with parent and child keys
            TreeItem<String> root = new TreeItem<>("root");

            for (int lnCtr = 0; lnCtr <= laMaster.size() - 1; lnCtr++){
                //convert the content to JSON
                loParent = (JSONObject) laMaster.get(lnCtr);
                TreeItem<String> parentnode = new TreeItem<>();
                
                parentnode.setValue((String) loParent.get("parent"));

                if (loParent.containsKey("child")){
                    if (loParent.get("child") instanceof String ||
                        loParent.get("child") instanceof Double ||
                        loParent.get("child") instanceof Integer){                        
                        parentnode.setValue((String) loParent.get("child"));
                    } else {
                        laDetail = (JSONArray) loParent.get("child");

                        //loop tayo sa laman ng array
                        for (int x = 0; x <= laDetail.size() - 1; x++){
                            loDetail = (JSONObject) laDetail.get(x);
                            
                            TreeItem<String> child = new TreeItem<>();
                            
                            if (loDetail.containsKey("link")){                                
                                child.setValue((String) loDetail.get("parent"));
                            } else {
                                child.setValue((String) loDetail.get("parent"));
                            }
                            
                            if (loDetail.containsKey("child")){
                                 
                                if (loDetail.get("child") instanceof String ||
                                    loDetail.get("child") instanceof Double ||
                                    loDetail.get("child") instanceof Integer){
                                    child.setValue(String.valueOf(loDetail.get("child")));
                                } else {
                                    laDetail2 = (JSONArray) loDetail.get("child");
                                    TreeItem<String> detail = new TreeItem<>();
                                    for (int y = 0; y <= laDetail2.size() - 1; y++){
                                        TreeItem<String> subdetail = new TreeItem<>();
                                        if (laDetail2.get(y) instanceof String ||
                                            laDetail2.get(y) instanceof Double ||
                                            laDetail2.get(y) instanceof Integer){
                                            subdetail.setValue(String.valueOf(laDetail2.get(y)));
                                        }
                                        detail.getChildren().add(subdetail);
                                    }
                                    child.getChildren().addAll(detail.getChildren());
                                }  
                            }
                            parentnode.getChildren().add(child);
                        }
                        
                    }
                }
                root.getChildren().add(parentnode);
            }
            
            tvReminders.setRoot(root);
            tvReminders.setShowRoot(false);
            tvReminders.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
                @Override
                public void changed(ObservableValue<? extends TreeItem<String>> observable,
                    TreeItem<String> oldValue, TreeItem<String> newValue) {
                    // newValue represents the selected itemTree
                    if(newValue.isLeaf()){
                        System.out.println(newValue.getValue());
                    }
                }

            });
            
        } catch (org.json.simple.parser.ParseException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            
        }
    }
    private void startSession(){
        _seconds = 0;
        
        _session = new Thread() {
            @Override
            public void run() {  // override the run() for the running behaviors
                while (true){
                    try {
                        sleep(1000);//1 second = 1000 milliseconds
                        _seconds++;
                        
                        if (_seconds == _limit){
                            createJSON();
                        }
                    } catch (InterruptedException ex) {}
                }
            }
        };
        _session.start();
    }
    private Thread _session;
    private int _seconds;
    private final int _limit = 900; //900 seconds is 5 minutes
}
