package com.fors.erik.simpletodo;

public class ListItem {
    private String item;
    private String date;

    ListItem(String item, String date){
        if(date.isEmpty())
            date = "null";
        this.item = item;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getItem() {
        return item;
    }

}
