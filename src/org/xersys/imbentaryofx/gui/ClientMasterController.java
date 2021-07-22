package org.xersys.imbentaryofx.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.xersys.clients.base.Clients;
import org.xersys.clients.base.LClients;
import org.xersys.lib.pojo.Temp_Transactions;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreenInfo;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.imbentaryofx.listener.DetailUpdateCallback;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.CommonUtil;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.MsgBox;
import org.xersys.commander.util.SQLUtil;
import java.util.Date;

public class ClientMasterController implements Initializable, ControlledScreen{
    private XNautilus _nautilus;
    private Clients _trans;
    private LClients _listener;
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    private DetailUpdateCallback _detail_update_callback;
    
    private TableModel _table_model;
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    
    ObservableList<String> _gendercd = FXCollections.observableArrayList("Male", "Female", "LGBTQ+");
    ObservableList<String> _clienttp = FXCollections.observableArrayList("Individual", "Institution");
    ObservableList<String> _cvilstat = FXCollections.observableArrayList("Single", "Marrie", "Separated", "Widowed", "Single Parent", "Single Parent w/ Live-in Partner");
    
    private boolean _loaded = false;
    private int _index;
    private int _detail_row;
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private VBox btnbox00;
    @FXML
    private HBox btnbox01;
    @FXML
    private Button btn01;
    @FXML
    private Button btn02;
    @FXML
    private Button btn03;
    @FXML
    private Button btn04;
    @FXML
    private Button btn05;
    @FXML
    private Button btn06;
    @FXML
    private Button btn07;
    @FXML
    private Button btn08;
    @FXML
    private Button btn09;
    @FXML
    private Button btn10;
    @FXML
    private Button btn11;
    @FXML
    private Button btn12;
    @FXML
    private FontAwesomeIconView glyph01;
    @FXML
    private FontAwesomeIconView glyph02;
    @FXML
    private FontAwesomeIconView glyph03;
    @FXML
    private FontAwesomeIconView glyph04;
    @FXML
    private FontAwesomeIconView glyph05;
    @FXML
    private FontAwesomeIconView glyph06;
    @FXML
    private FontAwesomeIconView glyph07;
    @FXML
    private FontAwesomeIconView glyph08;
    @FXML
    private FontAwesomeIconView glyph09;
    @FXML
    private FontAwesomeIconView glyph10;
    @FXML
    private FontAwesomeIconView glyph11;
    @FXML
    private FontAwesomeIconView glyph12;
    @FXML
    private ComboBox cmbOrders;
    @FXML
    private ComboBox cmbClientTp;
    @FXML
    private ComboBox cmbCvilStat;
    @FXML
    private ComboBox cmbGenderCd;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField07;
    @FXML
    private TextField txtField10;
    @FXML
    private TextField txtField12;
    @FXML
    private TextField txtField101;
    @FXML
    private TextField txtField102;
    @FXML
    private TextField txtField103;
    @FXML
    private TextField txtField14;
    @FXML
    private TextField txtField13;
    @FXML
    private DatePicker dpBirthDte;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);   
        
        if (_nautilus  == null) {
            System.err.println("Application driver is not set.");
            System.exit(1);
        }
        
        initButton();
        initFields();
        initListener();
        
        _trans = new Clients(_nautilus, (String) _nautilus.getSysConfig("sBranchCd"), false);
        _trans.setSaveToDisk(true);
        _trans.setListener(_listener);
        
        if (_trans.TempTransactions().isEmpty())
            createNew("");
        else
            createNew(_trans.TempTransactions().get(0).getOrderNo());
        
        cmbOrders.getSelectionModel().select(0);
        
        _loaded = true;
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
        _screens_dashboard_controller = foValue;
    }
    
    private void createNew(String fsOrderNox){
        if (!_trans.NewRecord(fsOrderNox)){
            System.err.println(_trans.getMessage());
            MsgBox.showOk(_trans.getMessage(), "Warning");
            System.exit(1);
        }
        
        clearFields();
        loadTransaction();
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        if (event.getCode() == KeyCode.ENTER){
            switch (lsTxt){
                case "txtField10":
                    System.out.println(this.getClass().getSimpleName() + " " + lsTxt + " was used for searching");                    
                    break;
                case "txtField12":
                    System.out.println(this.getClass().getSimpleName() + " " + lsTxt + " was used for searching");                    
                    break;
                case "txtField14":
                    System.out.println(this.getClass().getSimpleName() + " " + lsTxt + " was used for searching");                    
                    break;
            }
        }
        
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            FXUtil.SetNextFocus(txtField);
            break;
        case UP:
            FXUtil.SetPreviousFocus(txtField);
        }
    }
    
    private void clearFields(){        
        //load temporary transactions
        ArrayList<Temp_Transactions> laTemp = _trans.TempTransactions();
        ObservableList<String> lsOrderNox = FXCollections.observableArrayList();
        
        if (laTemp.size() > 0){    
            for (int lnCtr = 0; lnCtr <= laTemp.size()-1; lnCtr ++){
                lsOrderNox.add(laTemp.get(lnCtr).getOrderNo() + " (" +SQLUtil.dateFormat(laTemp.get(lnCtr).getDateCreated(), SQLUtil.FORMAT_TIMESTAMP) + ")");
            }
        }
        
        cmbOrders.setItems(lsOrderNox);  
    }
    
    
    private void loadTransaction(){
        txtField03.setText((String) _trans.getMaster("sLastName"));
        txtField04.setText((String) _trans.getMaster("sFrstName"));
        txtField05.setText((String) _trans.getMaster("sMiddName"));
        txtField06.setText((String) _trans.getMaster("sSuffixNm"));
        txtField07.setText((String) _trans.getMaster("sClientNm"));
        txtField13.setText((String) _trans.getMaster("sAddlInfo"));
        
        if (_trans.getMaster("dBirthDte") != null){
            LocalDate date = ((Date) _trans.getMaster("dBirthDte")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            dpBirthDte.setValue(date);
        }

        cmbGenderCd.requestFocus();
    }
    


    private void quickSearch(TextField foField, Enum foType, String fsValue, String fsKey, String fsFilter, int fnMax, boolean fbExact){        
        //pass the initial value do initial search
        JSONObject loJSON = _trans.Search(foType, fsValue, fsKey, fsFilter, fnMax, fbExact);
        
        //null result, return to callee
        if (loJSON == null) {
            System.err.println("Initial search result was null.");
            return;
        }
        
        //error result, return to callee
        if ("error".equals((String) loJSON.get("result"))) {
            System.err.println((String) loJSON.get("message"));
            return;
        }
        
        JSONArray loArr = (JSONArray) loJSON.get("payload");
        
        //only one record was retreived, load the data
        if (loArr.size() == 1) {
        }
        
        //multiple result, load the quick search to display records
        JSONObject loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);
        
        if (loScreen != null){
            QuickSearchController instance = new QuickSearchController();
            instance.setNautilus(_nautilus);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            
            instance.setTransObject(_trans);
            instance.setSearchCallback(_search_callback);
            
            instance.setSearchType(foType);
            instance.setSearchValue(fsValue);
            instance.setSearchKey(fsKey);
            instance.setSearchFilter(fsFilter);
            instance.setSearchMaxRow(fnMax);
            instance.setSearchExact(fbExact);
            instance.setSearchResult(loJSON);
            instance.setTextField(foField);
            
            _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
        }
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btn01": //clear
                if (_trans.DeleteTempTransaction(_trans.TempTransactions().get(cmbOrders.getSelectionModel().getSelectedIndex()))){
                    _loaded = false;
                    
                    if (_trans.TempTransactions().isEmpty()){
                        createNew("");
                        cmbOrders.getSelectionModel().select(0);
                    } else {
                        createNew(_trans.TempTransactions().get(_trans.TempTransactions().size() - 1).getOrderNo());
                         cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);
                    }
                        
                    _loaded = true;
                }
                break;
            case "btn02": //new
                _loaded = false;
                
                createNew("");
                clearFields();
                loadTransaction();
                
               cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  
               
               _loaded = true;
                break;
            case "btn03": //save
                if (_trans.SaveRecord(true)){
                    MsgBox.showOk("Transaction saved successfully.", "Success");
                    
                    _loaded = false;

                    createNew("");
                    clearFields();
                    loadTransaction();

                   cmbOrders.getSelectionModel().select(_trans.TempTransactions().size() - 1);  

                   _loaded = true;
                } else 
                    MsgBox.showOk(_trans.getMessage(), "Warning");
                break;
            case "btn04": //search
                break;
            case "btn05":
                break;
            case "btn06":
                break;
            case "btn07":
                break;
            case "btn08":
                break;
            case "btn09":
                break;
            case "btn10":
                break;
            case "btn11": //history
                break;
            case "btn12": //close screen
                if (_screens_controller.getScreenCount() > 1)
                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                else{
                    if (MsgBox.showOkCancel("This action will exit the application.", "Please confirm...") == MsgBox.RESP_YES_OK){
                        System.exit(0);
                    }
                }
                break;
        }
    }
    
    public void keyReleased(KeyEvent event) {
        switch(event.getCode()){
            case F1:
                break;
            case F2: 
                break;
            case F3:
                break;
            case F4:
                break;
            case F5: 
                break;
            case F6:
                break;
            case F7:
                break;
            case F8: 
                break;
            case F9:
                break;
            case F10:
                break;
            case F11:
                break;
            case F12: 
                break;

        }
    }
    
    private void loadScreen(ScreenInfo.NAME  foValue){
        JSONObject loJSON = ScreenInfo.get(foValue);
        ControlledScreen instance;
        
        if (loJSON != null){
            instance = (ControlledScreen) CommonUtil.createInstance((String) loJSON.get("controller"));
            instance.setNautilus(_nautilus);
            instance.setParentController(_main_screen_controller);
            instance.setScreensController(_screens_controller);
            instance.setDashboardScreensController(_screens_dashboard_controller);
            
            _screens_controller.loadScreen((String) loJSON.get("resource"), instance);
        }
    }
    
    private void initListener(){
        _listener = new LClients() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
                switch (fsFieldNm){
                    case "sLastName":
                        txtField03.setText((String) foValue); break;
                    case "sFrstName":
                        txtField04.setText((String) foValue); break;
                    case "sMiddName":
                        txtField05.setText((String) foValue); break;
                    case "sSuffixNm":
                        txtField06.setText((String) foValue); break;
                    case "sClientNm":
                        txtField07.setText((String) foValue); break;
                    case "dBirthDte":
                        break;
                    case "sAddlInfo":
                        txtField13.setText((String) foValue); break;
                    
                }
            }

            @Override
            public void MobileRetreive(int fnRow, String fsFieldNm, Object foValue) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void AddressRetreive(int fnRow, String fsFieldNm, Object foValue) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void EMailRetreive(int fnRow, String fsFieldNm, Object foValue) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
            }

            @Override
            public void FormClosing() {
            }
        };
        
        _detail_update_callback = new DetailUpdateCallback() {
            @Override
            public void Result(int fnRow, int fnIndex, Object foValue) {
            }

            @Override
            public void RemovedItem(int fnRow) {
            }

            @Override
            public void FormClosing() {
 
            }
        };
    }
    
    private void initButton(){
        btn01.setOnAction(this::cmdButton_Click);
        btn02.setOnAction(this::cmdButton_Click);
        btn03.setOnAction(this::cmdButton_Click);
        btn04.setOnAction(this::cmdButton_Click);
        btn05.setOnAction(this::cmdButton_Click);
        btn06.setOnAction(this::cmdButton_Click);
        btn07.setOnAction(this::cmdButton_Click);
        btn08.setOnAction(this::cmdButton_Click);
        btn09.setOnAction(this::cmdButton_Click);
        btn10.setOnAction(this::cmdButton_Click);
        btn11.setOnAction(this::cmdButton_Click);
        btn12.setOnAction(this::cmdButton_Click);
        
        btn01.setTooltip(new Tooltip("F1"));
        btn02.setTooltip(new Tooltip("F2"));
        btn03.setTooltip(new Tooltip("F3"));
        btn04.setTooltip(new Tooltip("F4"));
        btn05.setTooltip(new Tooltip("F5"));
        btn06.setTooltip(new Tooltip("F6"));
        btn07.setTooltip(new Tooltip("F7"));
        btn08.setTooltip(new Tooltip("F8"));
        btn09.setTooltip(new Tooltip("F9"));
        btn10.setTooltip(new Tooltip("F10"));
        btn11.setTooltip(new Tooltip("F11"));
        btn12.setTooltip(new Tooltip("F12"));
        
        btn01.setText("Clear");
        btn02.setText("New");
        btn03.setText("Save");
        btn04.setText("Search");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("");
        btn11.setText("History");
        btn12.setText("Close");              
        
        
        btn01.setVisible(true);
        btn02.setVisible(true);
        btn03.setVisible(true);
        btn04.setVisible(true);
        btn05.setVisible(false);
        btn06.setVisible(false);
        btn07.setVisible(false);
        btn08.setVisible(false);
        btn09.setVisible(false);
        btn10.setVisible(false);
        btn11.setVisible(true);
        btn12.setVisible(true);
        
        glyph01.setIcon(FontAwesomeIcon.ANCHOR);
        glyph02.setIcon(FontAwesomeIcon.ANCHOR);
        glyph03.setIcon(FontAwesomeIcon.ANCHOR);
        glyph04.setIcon(FontAwesomeIcon.ANCHOR);
        glyph05.setIcon(FontAwesomeIcon.ANCHOR);
        glyph06.setIcon(FontAwesomeIcon.ANCHOR);
        glyph07.setIcon(FontAwesomeIcon.ANCHOR);
        glyph08.setIcon(FontAwesomeIcon.ANCHOR);
        glyph09.setIcon(FontAwesomeIcon.ANCHOR);
        glyph10.setIcon(FontAwesomeIcon.ANCHOR);
        glyph11.setIcon(FontAwesomeIcon.ANCHOR);
        glyph12.setIcon(FontAwesomeIcon.ANCHOR);
        
        cmbGenderCd.setItems(_gendercd);
        cmbClientTp.setItems(_clienttp);
        cmbCvilStat.setItems(_cvilstat);
        
        cmbGenderCd.getSelectionModel().select(0);
        cmbClientTp.getSelectionModel().select(0);
        cmbCvilStat.getSelectionModel().select(0);
    }
    
    private void initFields(){       
        cmbOrders.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (_loaded) createNew(_trans.TempTransactions().get(cmbOrders.getSelectionModel().getSelectedIndex()).getOrderNo());
            }
        });
        
        dpBirthDte.setEditable(false);
        
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        txtField13.setOnKeyPressed(this::txtField_KeyPressed);
        txtField14.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField13.focusedProperty().addListener(txtField_Focus);
        
        dpBirthDte.setOnAction(new EventHandler() {
            public void handle(Event t) {
                _trans.setMaster("dBirthDte", dpBirthDte.getValue());
            }
        });

    }
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!_loaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        if(!nv){ //Lost Focus           
            switch (lnIndex){
                case 3: //last name
                case 4: //first name
                case 5: //middle name
                case 6: //suffix name
                case 7: //full name/company name
                case 11: //birth date
                case 13: //other info
                    _trans.setMaster(lnIndex, lsValue);
                    break;
                default:
                    MsgBox.showOk("Text field with name " + txtField.getId() + " not registered.", "Warning");
            }
            _index = lnIndex;
        } else{ //Got Focus
            _index = lnIndex;
            txtField.selectAll();
        }
        
    };    
}