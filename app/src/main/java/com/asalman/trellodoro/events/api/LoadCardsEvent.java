package com.asalman.trellodoro.events.api;

/**
 * Created by asalman on 1/9/16.
 */
public class LoadCardsEvent {

    private String columnID;

    public LoadCardsEvent(String columnID){
        this.columnID = columnID;
    }

    public String getColumnID(){
        return this.columnID;
    }

}
