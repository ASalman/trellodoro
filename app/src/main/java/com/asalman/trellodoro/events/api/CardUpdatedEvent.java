package com.asalman.trellodoro.events.api;


import com.asalman.trellodoro.models.Card;

/**
 * Created by asalman on 1/9/16.
 */
public class CardUpdatedEvent {

    Card card;

    public CardUpdatedEvent(Card card){
        this.card = card;
    }

    public Card getCard(){
        return this.card;
    }

}
