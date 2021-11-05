package org.xersys.imbentaryofx.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.listener.PartsCatalogueListener;


public class PartsCatalogueChildController implements Initializable {
    @FXML
    private ImageView image;
    @FXML
    private Label lblBlockTitle;
    @FXML
    private Label lblBlockNo;
    @FXML
    private Label lblAddressNo;
    
    private String _block_title = "";
    private String _block_no = "";
    private String _address_no = "";
    private JSONArray _parts = null;
    
    public void setBlockTitle(String fsValue){
        _block_title = fsValue;
    }
    
    public void setBlockNo(String fsValue){
        _block_no = fsValue;
    }
    
    public void setAddressNo(String fsValue){
        _address_no = fsValue;
    }
    
    public void setParts(JSONArray foValue){
        _parts = foValue;
    }
    
    @FXML
    private void click(MouseEvent mouseEvent) {
        JSONObject loJSON = new JSONObject();
        loJSON.put("block", _block_no);
        loJSON.put("title", _block_title);
        loJSON.put("address", _address_no);
        loJSON.put("path", _image_path);
        loJSON.put("parts", _parts);
        
        _listener.onClickListener(loJSON);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            lblBlockNo.setText(_block_no);
            lblBlockTitle.setText(_block_title);
            lblAddressNo.setText(_address_no);
            
            FileInputStream inputstream = new FileInputStream(_image_path);
//            Rectangle2D loRec = new Rectangle2D(140, 0, 632, 480);
//            //Rectangle2D loRec = new Rectangle2D(400, 200, 632, 480);
//            
            image.setImage(new Image(inputstream));
//            image.setPreserveRatio(true);
//            image.setSmooth(true);
//            image.setViewport(loRec);
            Image img = image.getImage();
            if (img != null) {
                double w = 0;
                double h = 0;

                double ratioX = image.getFitWidth() / img.getWidth();
                double ratioY = image.getFitHeight() / img.getHeight();

                double reducCoeff = 0;
                if(ratioX >= ratioY) {
                    reducCoeff = ratioY;
                } else {
                    reducCoeff = ratioX;
                }

                w = img.getWidth() * reducCoeff;
                h = img.getHeight() * reducCoeff;

                image.setX((image.getFitWidth() - w) / 2);
                image.setY((image.getFitHeight() - h) / 2);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
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
