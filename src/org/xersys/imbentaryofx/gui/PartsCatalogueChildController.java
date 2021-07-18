package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.xersys.imbentaryofx.listener.PartsCatalogueListener;


public class PartsCatalogueChildController implements Initializable {
    @FXML
    private ImageView image;
    
    @FXML
    private void click(MouseEvent mouseEvent) {
        _listener.onClickListener();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        image.setImage(new Image(_image_path));
        
    }    
    
    public void setData(PartsCatalogueListener foValue){
        _listener = foValue;
    }
    
    public void setImagePath(String fsValue){
        _image_path = fsValue;
    }
    
    PartsCatalogueListener _listener;
    String _image_path;
}
