package com.asalman.trellodoro.events.api;

/**
 * Created by asalman on 1/9/16.
 */
public class UpdateCardEvent {

    private String newColumnID;
    private String cardID;

    public UpdateCardEvent(String cardID, String newColumnID){
        this.newColumnID = newColumnID;
        this.cardID = cardID;
    }

    public String getNewColumnID(){
        return this.newColumnID;
    }

    public String getCardID(){
        return this.cardID;
    }

}
