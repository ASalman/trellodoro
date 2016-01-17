package com.asalman.trellodoro.events.api;

/**
 * Created by asalman on 1/9/16.
 */
public class LoadColumnsEvent {

    private String boardID;

    public LoadColumnsEvent(String boardID){
        this.boardID = boardID;
    }

    public String getBoardID(){
        return this.boardID;
    }

}
