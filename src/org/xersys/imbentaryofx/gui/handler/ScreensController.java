package org.xersys.imbentaryofx.gui.handler;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.xersys.imbentaryofx.gui.MainScreenController;
import org.xersys.kumander.base.Nautilus;

/**
 * 
 * @author Michael Torres Cuison
 * 
 * Multiple Screen Loading Handler
 */
public class ScreensController{
    private ArrayList<Node> _screens = new ArrayList<>(); //holds the screens to be displayed
    private int _index; //index of the currently load screen
    
    
    private AnchorPane _anchor; //the parent anchorpane
    private MainScreenController _controller;
    
    public ScreensController() {
        super();
    }
    
    //set the parent anchor pane
    public void setParentPane(AnchorPane foValue){
        _anchor = foValue;
    }
    
    public void setParentController(MainScreenController foValue){
        _controller = foValue;
    }
    
    //get the number of screens that the user already loaded for the session
    public int getScreenCount(){
        return _screens.size();
    }
    
    public int getCurrentScreenIndex(){
        return _index;
    }

    //sdd the screen to the collection
    public boolean addScreen(Node screen) {        
        //check if the screen to add was already on the list of screens previously loaded
        for (int lnCtr = 0; lnCtr <= _screens.size() - 1; lnCtr++){
            if (getScreen(lnCtr).getId() == null ? screen.getId() == null : getScreen(lnCtr).getId().equals(screen.getId())){
                //load the previously loaded screen
                setScreen(lnCtr);
                return false;
            }
        }
        
        //add the screen
        _screens.add(screen);
        setScreen(getScreenCount() - 1);
        return true;
    }

    //returns the Node with the appropriate index
    public Node getScreen(int index) {
        return _screens.get(index);
    }
    
    //loads the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the controller.
    public boolean loadScreen(String resource, ControlledScreen controller) {
        try {
            FXMLLoader myLoader = new FXMLLoader();
            
            myLoader.setLocation(getClass().getResource(resource));
            myLoader.setController(controller);
              
            Parent loadScreen = (Parent) myLoader.load();
            
            return addScreen(loadScreen);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //loads the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the controller.
    public boolean loadScreen(Nautilus nautilus, String resource, ControlledScreen controller) {
        try {
            FXMLLoader myLoader = new FXMLLoader();
            
            myLoader.setLocation(getClass().getResource(resource));
            controller.setNautilus(nautilus);
            controller.setParentController(_controller);
            controller.setScreensController(this);
            
            myLoader.setController(controller);
              
            Parent loadScreen = (Parent) myLoader.load();
            
            return addScreen(loadScreen);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //This method tries to displayed the screen with a predefined name.
    //First it makes sure the screen has been already loaded.  Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    //If there isn't any screen being displayed, the new screen is just added to the root.
    private boolean setScreen(final int index) {       
        if (_screens.get(index) != null) {   //screen loaded
            final DoubleProperty opacity = _anchor.opacityProperty();

            if (!_anchor.getChildren().isEmpty()) {    //if there is more than one screen
//                Timeline fade = new Timeline(
//                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
//                        new KeyFrame(new Duration(1), new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent t) {                                     
//                        _anchor.getChildren().remove(0);                        //remove the displayed screen
//                        _anchor.getChildren().add(0, _screens.get(index));      //add the screen
//                        Timeline fadeIn = new Timeline(
//                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
//                                new KeyFrame(new Duration(1), new KeyValue(opacity, 1.0)));
//                        fadeIn.play();
//                    }
//                }, new KeyValue(opacity, 0.0)));
//                fade.play();
                _anchor.getChildren().remove(0);                        //remove the displayed screen
                _anchor.getChildren().add(0, _screens.get(index));      //add the screen
            } else {
                _anchor.setOpacity(0.0);
                _anchor.getChildren().add(_screens.get(index));       //no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(1), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            
            _index = index;
            return true;
        } else {
            System.out.println("screen hasn't been loaded!!! \n");
            return false;
        }
    }

    //this method will remove the screen with the given index from the collection of screens
    public boolean unloadScreen(int index) {
        if (_screens.remove(index) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            if (index - 1 < 0)
                setScreen(0);
            else
                setScreen(index - 1);
            
            return true;
        }
    }
    
    public void prevScreen(){
        if (getScreenCount() == 0)
            System.err.println("No screen was loaded.");
        else if (getCurrentScreenIndex() == 0)
            System.err.println("We are on the first screen index. Nowhere to go backwards.");
        else{
            setScreen(getCurrentScreenIndex() - 1);
            System.out.println("Screen was moved to one index backwards.");
        }
    }
    
    public void fwrdScreen(){
        if (getCurrentScreenIndex() == getScreenCount() - 1)
            System.err.println("The screen loaded is only one or we are on the last screen index. Nowhere to go forward.");
        else{
            setScreen(getCurrentScreenIndex() + 1);
            System.out.println("Screen was moved to one index forward.");
        }
    }
}

