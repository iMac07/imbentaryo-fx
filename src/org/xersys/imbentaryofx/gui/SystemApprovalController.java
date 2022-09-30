package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.xersys.commander.util.FXUtil;

/**
 * FXML Controller class
 *
 * @author Mac
 */
public class SystemApprovalController implements Initializable {
    @FXML
    private AnchorPane AnchorPaneHeader;
    @FXML
    private Button btnOkay;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtField01;
    @FXML
    private PasswordField txtField02;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField01.setText("");
        txtField02.setText("");
        
        txtField01.requestFocus();
    }    

    @FXML
    private void btnOkay_Click(ActionEvent event) {
        logUser();
    }

    @FXML
    private void btnCancel_Click(ActionEvent event) {
        pbCancelled = true;
        unloadScene();
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        
        switch (event.getCode()){
        case ENTER:
        case DOWN:        
            if (txtField.getId().equals("txtField02")){
                logUser();
                event.consume();
                return;
            }
            
            FXUtil.SetNextFocus(txtField);
            break;
        case UP:
            FXUtil.SetPreviousFocus(txtField);
        }
    }
    
    private void logUser(){
        psUsername = txtField01.getText();
        psPassword = txtField02.getText();
        pbCancelled = false;
        unloadScene();
    }
    
    private void unloadScene(){
        Stage stage = (Stage) txtField01.getScene().getWindow();
        stage.close();
    }
    
    public String getUsername(){return psUsername;}
    public String getPassword(){return psPassword;}
    public boolean isCancelled(){return pbCancelled;}
    
    private String psUsername = "";
    private String psPassword = "";
    private boolean pbCancelled = true;
}
