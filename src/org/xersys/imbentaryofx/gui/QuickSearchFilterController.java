package org.xersys.imbentaryofx.gui;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.xersys.imbentaryofx.gui.handler.ControlledScreen;
import org.xersys.imbentaryofx.gui.handler.ScreensController;
import org.xersys.commander.iface.XNautilus;
import org.xersys.commander.iface.iSearch;
import org.xersys.imbentaryofx.listener.FormClosingCallback;
import javafx.scene.control.TableColumn.CellEditEvent;

public class QuickSearchFilterController implements Initializable, ControlledScreen {
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
    private TableView table;
    @FXML
    private Button btnChild01;
    @FXML
    private Button btnChild02;
    @FXML
    private ComboBox cmbMax;
    @FXML
    private ComboBox cmbFilters;
    
    @Override
    public void setNautilus(XNautilus foValue) {
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
    
    public void setSearchObject(iSearch foValue){
        _trans = foValue;
    }
    
    public void setFormClosing(FormClosingCallback foValue){
        _callback = foValue;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (_trans == null){
            System.err.println("UNSET Search Object.");
            _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
            return;
        }
        
        //set the main anchor pane fit the size of its parent anchor pane
        AnchorMain.setTopAnchor(AnchorMain, 0.0);
        AnchorMain.setBottomAnchor(AnchorMain, 0.0);
        AnchorMain.setLeftAnchor(AnchorMain, 0.0);
        AnchorMain.setRightAnchor(AnchorMain, 0.0);   
        
        table.setOnMouseClicked(this::mouseClicked);
        
        initButton();
        initGrid();
        loadDetail();
        
        _loaded = true;
    }    
    
    @FXML
    private void cmbMax_Click(ActionEvent event) {
        if (!_loaded) return;
        switch(cmbMax.getSelectionModel().getSelectedIndex()){
            case 1:
                _trans.setMaxResult(50);
                break;
            case 2:
                _trans.setMaxResult(75);
                break;
            case 3:
                _trans.setMaxResult(100);
                break;
            default:
                _trans.setMaxResult(25);
        }
    }
    
    private void loadDetail(){        
        if (_trans == null){
            System.err.println("UNSET Search Object.");
            _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
            return;
        }
        
        switch(_trans.getMaxResult()){
            case 25:
                cmbMax.getSelectionModel().select(0);
                break;
            case 50:
                cmbMax.getSelectionModel().select(1);
                break;
            case 75:
                cmbMax.getSelectionModel().select(2);
                break;
            case 100:
                cmbMax.getSelectionModel().select(3);
                break;
        }
        
        _filter_list = _trans.getFilterListDescription();
        cmbFilters.setItems(FXCollections.observableArrayList(_filter_list));
        cmbFilters.getSelectionModel().select(0);
        
        ArrayList<String> loFilter = _trans.getFilter();
        
        _data.clear();
        for (int lnCtr = 0; lnCtr <= loFilter.size()-1; lnCtr++){
            _data.add(new TableSearchFilter(
                        String.valueOf(lnCtr + 1), 
                        loFilter.get(lnCtr), 
                        String.valueOf(_trans.getFilterValue(loFilter.get(lnCtr)))));
        }
        
        table.getSelectionModel().selectFirst();
        _selected = table.getSelectionModel().getSelectedIndex();
    }
    
    private void initButton(){
        btnChild01.setOnAction(this::cmdButton_Click);
        btnChild02.setOnAction(this::cmdButton_Click);
        
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
        
        btn01.setText("");
        btn02.setText("");
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
        
        
        btn01.setVisible(false);
        btn02.setVisible(false);
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
        
        cmbMax.setItems(_row_list);
        cmbMax.getSelectionModel().select(0);
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        System.out.println(this.getClass().getSimpleName() + " " + lsButton + " was clicked.");
        
        switch (lsButton){
            case "btnChild01": //add filter
                if (cmbFilters.getItems().size() > 0){
                    _trans.addFilter(_filter_list.get(cmbFilters.getSelectionModel().getSelectedIndex()), "");
                    loadDetail();
                }
                break;
            case "btnChild02": //remove filter
                if (cmbFilters.getItems().size() > 0){
                    _trans.removeFilter(_filter_list.get(cmbFilters.getSelectionModel().getSelectedIndex()));
                    loadDetail();
                }
                break;
            case "btn01":
                break;
            case "btn02":
                break;
            case "btn03":
                break;
            case "btn04":
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
            case "btn11":
                break;
            case "btn12": //close screen
                _callback.FormClosing();
                _screens_controller.unloadScreen(_screens_controller.getCurrentScreenIndex());
                break;
        }
    }
    
    private void mouseClicked(MouseEvent event) {
        _selected = table.getSelectionModel().getSelectedIndex();
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("");
        TableColumn index02 = new TableColumn("");
        TableColumn index03 = new TableColumn("");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);;
        
        table.getColumns().clear();        
        
        index01.setText("No."); 
        index01.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index01"));
        index01.prefWidthProperty().set(25);
        
        index02.setText("Parameter"); 
        index02.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index02"));
        index02.prefWidthProperty().set(150);
        
        index03.setText("Value"); 
        index03.setCellValueFactory(new PropertyValueFactory<TableModel,String>("index03"));
        index03.prefWidthProperty().set(150);
        
        table.setEditable(true);
        Callback<TableColumn, TableCell> cellFactory =
        new Callback<TableColumn, TableCell>() {
            public TableCell call(TableColumn p) {
               return new EditingCell();
            }
        };
        
        index03.setCellFactory(cellFactory);
        index03.setOnEditCommit(
            new EventHandler<CellEditEvent<TableSearchFilter, String>>() {
                @Override
                public void handle(CellEditEvent<TableSearchFilter, String> t) {
                    ((TableSearchFilter) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setIndex03(t.getNewValue());
                    
                    _trans.addFilter(_data.get(_selected).getIndex02(), t.getNewValue());
                    loadDetail();
                }
             }
        );
        
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
 
        table.setItems(_data);
    }
    
    private MainScreenController _main_screen_controller;
    private ScreensController _screens_controller;
    private FormClosingCallback _callback;
    
    private ObservableList<TableSearchFilter> _data = FXCollections.observableArrayList();
    private TableModel _model;
    
    private iSearch _trans;
    
    private int _selected = -1;
    private boolean _loaded = false;
    
    List<String> _filter_list;
    
    private ObservableList<String> _row_list = FXCollections.observableArrayList("25", "50", "75", "100");
    
    class EditingCell extends TableCell<TableSearchFilter, String> {
 
        private TextField textField;
 
        public EditingCell() {
        }
 
        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }
 
        @Override
        public void cancelEdit() {
            super.cancelEdit();
 
            setText((String) getItem());
            setGraphic(null);
        }
 
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
 
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }
 
        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap()* 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, 
                    Boolean arg1, Boolean arg2) {
                        if (!arg2) {
                            commitEdit(textField.getText());
                        }
                }
            });
        }
 
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
