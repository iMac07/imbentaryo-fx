/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xersys.imbentaryofx.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.kumander.iface.XNautilus;

public class Imbentaryo extends Application {
    private final String FORM_TITLE = "Imbentaryo v1.0";
    private final String FORM_NAME = "MainScreen.fxml";
    
    private static XNautilus _instance;
    
    public void setNautilus(XNautilus foValue){
        _instance = foValue;
    }
    
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(FORM_NAME));
        
        ScreensController loScreens = new ScreensController();
        
        //get the controller of the main interface
        MainScreenController loControl = new MainScreenController();
        //set the GRider Application Driver to the controller
        loControl.setNautilus(_instance);
        
        //the controller class to the main interface
        fxmlLoader.setController(loControl);
        
        //load the main interface
        Parent parent = fxmlLoader.load();
        //set the main interface as the scene
        Scene scene = new Scene(parent);
        
        //get the screen size
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        //stage.getIcons().add(new Image(pxeStageIcon));
        stage.setTitle(FORM_TITLE);
        
        //set stage as maximized but not full screen
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        
        System.setProperty("system.screen.width", String.valueOf(bounds.getWidth()));
        System.setProperty("system.screen.height", String.valueOf(bounds.getHeight()));
        
        System.out.println("PC screen width is: " + System.getProperty("system.screen.width"));
        System.out.println("PC screen height is: " + System.getProperty("system.screen.height"));
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
