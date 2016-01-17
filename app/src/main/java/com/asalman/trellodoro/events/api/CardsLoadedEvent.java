package com.asalman.trellodoro.events.api;

import com.asalman.trellodoro.models.Card;

import java.util.List;

/**
 * Created by asalman on 1/9/16.
 */
public class CardsLoadedEvent {

    List<Card> cardList;
    String columnID;

    public CardsLoadedEvent(String columnID, List<Card> boardList){
        this.columnID = columnID;
        this.cardList = boardList;
    }

    public String getColumnID(){
        return this.columnID;
    }

    public List<Card> getCards(){
        return this.cardList;
    }

}
