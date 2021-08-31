package org.xersys.imbentaryofx.gui;

import javafx.beans.property.SimpleStringProperty;

public class TableSearchFilter {  
    private SimpleStringProperty index01;
    private SimpleStringProperty index02;        
    private SimpleStringProperty index03;     

    TableSearchFilter(String index01, String index02, String index03){
        this.index01 = new SimpleStringProperty(index01);
        this.index02 = new SimpleStringProperty(index02);
        this.index03 = new SimpleStringProperty(index03);
    }
    
    public String getIndex01(){return index01.get();}
    public void setIndex01(String index01){this.index01.set(index01);}
    
    public String getIndex02(){return index02.get();}
    public void setIndex02(String index02){this.index02.set(index02);}
    
    public String getIndex03(){return index03.get();}
    public void setIndex03(String index03){this.index03.set(index03);}
}