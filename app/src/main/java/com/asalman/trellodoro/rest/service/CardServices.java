package com.asalman.trellodoro.rest.service;


import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.db.PomodoroDAL;
import com.asalman.trellodoro.events.api.APIErrorEvent;
import com.asalman.trellodoro.events.api.CardUpdatedEvent;
import com.asalman.trellodoro.events.api.CardsLoadedEvent;
import com.asalman.trellodoro.events.api.LoadCardsEvent;
import com.asalman.trellodoro.events.api.UpdateCardEvent;
import com.asalman.trellodoro.models.Card;
import com.asalman.trellodoro.rest.RestClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by asalman on 1/9/16.
 */
public class CardServices {
    private Bus mBus;

    public CardServices(Bus bus) {
        mBus = bus;
        bus.register(this);
    }

    @Subscribe
    public void onLoadCards(final LoadCardsEvent event) {
        RestClient.getCardServices().getListCards(event.getColumnID(), new Callback<List<Card>>() {
            @Override
            public void success(List<Card> cardList, Response response) {
                ArrayList<Card> updatedCardList = new ArrayList<Card>();
                PomodoroDAL pomodoroDAL = new PomodoroDAL(MyApplication.getApp().getDatabaseHelper());
                for (Card card : cardList) {
                    Card tempCard = pomodoroDAL.sync(card);
                    updatedCardList.add(tempCard);
                }
                mBus.post(new CardsLoadedEvent(event.getColumnID(), updatedCardList));
            }

            @Override
            public void failure(RetrofitError error) {
                mBus.post(new APIErrorEvent(error));
            }
        });
    }

    @Subscribe
    public void onUpdateCard(UpdateCardEvent event) {
        RestClient.getCardServices().updateCardList(event.getCardID(),
                event.getNewColumnID(),
                new Callback<Card>() {
                    @Override
                    public void success(Card card, Response response) {
                        PomodoroDAL pomodoroDAL = new PomodoroDAL(MyApplication.getApp().getDatabaseHelper());
                        card = pomodoroDAL.sync(card);
                        mBus.post(new CardUpdatedEvent(card));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mBus.post(new APIErrorEvent(error));
                    }
                });
    }
}
