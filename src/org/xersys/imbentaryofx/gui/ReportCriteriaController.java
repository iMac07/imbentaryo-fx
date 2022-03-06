package org.xersys.imbentaryofx.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.xersys.commander.iface.XNautilus;

public class ReportCriteriaController implements Initializable, ControlledScreen  {

    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private AnchorPane AnchorCriteria;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);
        
        loadCriteria();
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
    
    private void loadCriteria(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ReportCriteriaDate.fxml"));

            ReportCriteriaDateController loCriteria = new ReportCriteriaDateController();
            fxmlLoader.setController(loCriteria);

            Parent loadScreen = (Parent) fxmlLoader.load();

            AnchorCriteria.getChildren().add(loadScreen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    XNautilus _nautilus;
    MainScreenController _main_screen_controller;
    ScreensController _screens_controller;  
}
