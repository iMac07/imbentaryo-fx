package org.xersys.imbentaryofx.gui;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.simple.parser.ParseException;
import org.xersys.clients.base.ClientAddress;
import org.xersys.commander.iface.LMasDetTrans;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.util.FXUtil;
import org.xersys.commander.util.MsgBox;
import javax.sql.rowset.CachedRowSet;
import org.json.simple.JSONObject;
import org.xersys.imbentaryofx.listener.CachedRowsetCallback;
import org.xersys.imbentaryofx.listener.QuickSearchCallback;

public class ClientAddressController implements Initializable, ControlledScreen{
    private XNautilus _nautilus;
    private ClientAddress _trans;
    private LMasDetTrans _listener;
    private CachedRowsetCallback _data_listener;
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private ScreensController _screens_dashboard_controller;
    private QuickSearchCallback _search_callback;
    
    private ObservableList<TableModel> _table_data = FXCollections.observableArrayList();
    private CachedRowSet _data; 
    
    private boolean _loaded = false;
    private int _detail_row = 0;
    
    @FXML
    private AnchorPane AnchorMain;
    @FXML
    private VBox btnbox00;
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
    private TableView _table;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;
    @FXML
    private CheckBox chkRecdStat;
    @FXML
    private Button btnChild01;
    
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
        
        _trans = new ClientAddress(_nautilus);     
        _trans.setListener(_listener);
        
        createNew();
        
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
    
    public void setData(CachedRowSet foValue){
        _data = foValue;
    }
    
    public void setDataListener(CachedRowsetCallback foValue){
        _data_listener = foValue;
    }
    
    private void createNew(){
        try {
            if (!_trans.LoadRecord(_data)){
                MsgBox.showOk(_trans.getMessage(), "Warning");
                System.exit(1);
            }
            
            clearFields();
            loadDetail();
        } catch (SQLException ex) {
            ex.printStackTrace();
            MsgBox.showOk(ex.getMessage(), "Warning");
            System.exit(1);
        }
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        String lsTxt = txtField.getId();
        String lsValue = txtField.getText();
                
        if (event.getCode() == KeyCode.ENTER){
            try{
                switch (lsTxt){
                    case "txtField01":
                        _trans.setDetail(_detail_row, "sHouseNox", lsValue);
                        break;
                    case "txtField02":
                        _trans.setDetail(_detail_row, "sAddressx", lsValue);
                        break;
                    case "txtField03":
                        searchMaster("sBrgyIDxx", lsValue, false);
                        break;
                    case "txtField04":
                        searchMaster("sTownIDxx", lsValue, false);
                        break;
                }
            } catch (SQLException | ParseException ex) {
                ex.printStackTrace();
                MsgBox.showOk(ex.getMessage(), "Warning");
                System.exit(1);
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
        initGrid();
        
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        
        txtField01.requestFocus();
    }
    
    private void chkRecdStat(ActionEvent event) {
        CheckBox loButton = (CheckBox) event.getSource();
        
        try {
            _trans.setDetail(_detail_row, "cRecdStat", loButton.isSelected() ? "1" : "0");
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
            MsgBox.showOk(ex.getMessage(), "Warning");
            System.exit(1);
        }
    }
    
    private void loadDetail(){
        int lnCtr;
        int lnRow = _trans.getItemCount();
        
        _table_data.clear();

        try {
            for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){           
                _table_data.add(new TableModel(String.valueOf(lnCtr + 1), 
                            (String) _trans.getDetail(lnCtr, "sHouseNox"),
                            (String) _trans.getDetail(lnCtr, "sAddressx"),
                            (String) _trans.getDetail(lnCtr, "xBrgyName"),
                            (String) _trans.getDetail(lnCtr, "xTownName"),
                            String.valueOf(_trans.getDetail(lnCtr, "nPriority")), 
                            ((String) _trans.getDetail(lnCtr, "cRecdStat")).equals("1") ? "Yes" : "No",
                            "",
                            "",
                            ""));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            MsgBox.showOk(ex.getMessage(), "Warning");
            System.exit(1);
        }

        if (!_table_data.isEmpty()){
            _table.getSelectionModel().select(_detail_row);
            _table.getFocusModel().focus(_detail_row); 
            _detail_row = _table.getSelectionModel().getSelectedIndex();           
        }
        
        setDetailInfo(_detail_row);
    }
    
    private void setDetailInfo(int fnRow){
        try {
            txtField01.setText((String) _trans.getDetail(fnRow, "sHouseNox"));
            txtField02.setText((String) _trans.getDetail(fnRow, "sAddressx"));
            txtField03.setText((String) _trans.getDetail(fnRow, "xBrgyName"));
            txtField04.setText((String) _trans.getDetail(fnRow, "xTownName"));
            txtField05.setText(String.valueOf(_trans.getDetail(fnRow, "nPriority")));

            chkRecdStat.setSelected("1".equals((String) _trans.getDetail(fnRow, "cRecdStat")));

            txtField01.setEditable("".equals((String) _trans.getDetail(fnRow, "sClientID")));
            txtField02.setEditable("".equals((String) _trans.getDetail(fnRow, "sClientID")));
            txtField03.setEditable("".equals((String) _trans.getDetail(fnRow, "sClientID")));
            txtField04.setEditable("".equals((String) _trans.getDetail(fnRow, "sClientID")));
            txtField01.requestFocus();
        } catch (SQLException ex) {
            ex.printStackTrace();
            MsgBox.showOk(ex.getMessage(), "Warning");
            System.exit(1);
        }
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("");
        TableColumn index02 = new TableColumn("");
        TableColumn index03 = new TableColumn("");
        TableColumn index04 = new TableColumn("");
        TableColumn index05 = new TableColumn("");
        TableColumn index06 = new TableColumn("");
        TableColumn index07 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false);
        index05.setSortable(false); index05.setResizable(false);
        index06.setSortable(false); index06.setResizable(false); index06.setStyle( "-fx-alignment: CENTER;");
        index07.setSortable(false); index07.setResizable(false); index07.setStyle( "-fx-alignment: CENTER;");
        
        _table.getColumns().clear();        
        
        index01.setText("No."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(30);
        
        index02.setText("House No."); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(80);
        
        index03.setText("Address"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(150);
        
        index04.setText("Barangay"); 
        index04.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index04"));
        index04.prefWidthProperty().set(150);
        
        index05.setText("Town/City"); 
        index05.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index05"));
        index05.prefWidthProperty().set(150);
        
        index06.setText("Priority"); 
        index06.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index06"));
        index06.prefWidthProperty().set(80);
        
        index07.setText("Active"); 
        index07.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index07"));
        index07.prefWidthProperty().set(80);
        
        _table.getColumns().add(index01);
        _table.getColumns().add(index02);
        _table.getColumns().add(index03);
        _table.getColumns().add(index04);
        _table.getColumns().add(index05);
        _table.getColumns().add(index06);
        _table.getColumns().add(index07);
        
        _table.setItems(_table_data);
        _table.setOnMouseClicked(this::tableClicked);
    }
    
    private void tableClicked(MouseEvent event) { 
        _detail_row = _table.getSelectionModel().getSelectedIndex();
        
        setDetailInfo(_detail_row);
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        try {
            switch (lsButton){
                case "btnChild01": //plus
                    if (_detail_row > 0) {
                        _detail_row -= 1;
                        _trans.setDetail(_detail_row + 1, "nPriority", 1);
                    }
                    break;
                case "btn01": //okay
                    _data_listener.Result(_trans.getData());

                    _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                    break;
                case "btn02": //add detail
                    if (_trans.AddItem()) {
                        loadDetail();
                        
                        _detail_row = _trans.getItemCount() - 1;
                        if (!_table_data.isEmpty()){
                            _table.getSelectionModel().select(_detail_row);
                            _table.getFocusModel().focus(_detail_row); 
                            _detail_row = _table.getSelectionModel().getSelectedIndex();           
                        }

                        setDetailInfo(_detail_row);
                    } else {
                        MsgBox.showOk(_trans.getMessage(), "Warning");
                        txtField04.requestFocus();
                    }
                    break;
                case "btn03":
                case "btn04":
                case "btn05":
                case "btn06":
                case "btn07":
                case "btn08":
                case "btn09":
                case "btn10":
                case "btn11":
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
        } catch (SQLException | ParseException ex) {
            ex.printStackTrace();
            MsgBox.showOk(ex.getMessage(), "Warning");
            System.exit(1);
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
    
    private void initListener(){
        _listener = new LMasDetTrans() {
            @Override
            public void MasterRetreive(String fsFieldNm, Object foValue) {
            }
            
            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
            }

            @Override
            public void DetailRetreive(int fnRow, String fsFieldNm, Object foValue) {
                loadDetail();
            }
        };
        
        _search_callback = new QuickSearchCallback() {
            @Override
            public void Result(TextField foField, JSONObject foValue) {
                if ("success".equals((String) foValue.get("result"))){
                    foValue = (JSONObject) foValue.get("payload");
                
                    try {
                        switch (foField.getId()){
                            case "txtField03":
                                _trans.setDetail(_detail_row , "sBrgyIDxx", (String) foValue.get("sBrgyIDxx"));
                                break;
                            case "txtField04":
                                _trans.setDetail(_detail_row, "sTownIDxx", (String) foValue.get("sTownIDxx"));
                                break;
                        }
                    } catch (SQLException | ParseException e) {
                        e.printStackTrace();
                        MsgBox.showOk(e.getMessage(), "Warning");
                    }
                }
            }

            @Override
            public void FormClosing(TextField foField) {
                FXUtil.SetNextFocus(foField);
            }
        };
    }
    
    private void initButton(){
        chkRecdStat.setOnAction(this::chkRecdStat);        
        
        btnChild01.setOnAction(this::cmdButton_Click);
        
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
        
        btn01.setText("Okay");
        btn02.setText("Add");
        btn03.setText("");
        btn04.setText("");
        btn05.setText("");
        btn06.setText("");
        btn07.setText("");
        btn08.setText("");
        btn09.setText("");
        btn10.setText("");
        btn11.setText("");
        btn12.setText("Close");              
        
        btn01.setVisible(true);
        btn02.setVisible(true);
        btn03.setVisible(false);
        btn04.setVisible(false);
        btn05.setVisible(false);
        btn06.setVisible(false);
        btn07.setVisible(false);
        btn08.setVisible(false);
        btn09.setVisible(false);
        btn10.setVisible(false);
        btn11.setVisible(false);
        btn12.setVisible(true);
    }
    
    private void initFields(){
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
    }
    
    private void searchMaster(String fsKey, Object foValue, boolean fbExact){
        JSONObject loJSON;
        JSONObject loScreen;
        
        switch (fsKey){
            case "sBrgyIDxx":
                loJSON = _trans.searchBarangay("a.sBrgyName", foValue, fbExact);
                
                if ("success".equals((String) loJSON.get("result"))){            
                    loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                    if (loScreen != null){
                        QuickSearchNeoController instance = new QuickSearchNeoController();
                        instance.setNautilus(_nautilus);
                        instance.setParentController(_main_screen_controller);
                        instance.setScreensController(_screens_controller);

                        instance.setSearchObject(_trans.getSearchBarangay());
                        instance.setSearchCallback(_search_callback);
                        instance.setTextField(txtField03);

                        _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                    }
                } else {
                    MsgBox.showOk((String) loJSON.get("message"), "Warning");
                    txtField03.setText("");
                    FXUtil.SetNextFocus(txtField03);
                }
                break;
            case "sTownIDxx":
                loJSON = _trans.searchTown("a.sTownName", foValue, fbExact);
                
                if ("success".equals((String) loJSON.get("result"))){            
                    loScreen = ScreenInfo.get(ScreenInfo.NAME.QUICK_SEARCH);

                    if (loScreen != null){
                        QuickSearchNeoController instance = new QuickSearchNeoController();
                        instance.setNautilus(_nautilus);
                        instance.setParentController(_main_screen_controller);
                        instance.setScreensController(_screens_controller);

                        instance.setSearchObject(_trans.getSearchTown());
                        instance.setSearchCallback(_search_callback);
                        instance.setTextField(txtField04);

                        _screens_controller.loadScreen((String) loScreen.get("resource"), (ControlledScreen) instance);
                    }
                } else {
                    MsgBox.showOk((String) loJSON.get("message"), "Warning");
                    txtField04.setText("");
                    FXUtil.SetNextFocus(txtField04);
                }
                break;
        }
    }
}