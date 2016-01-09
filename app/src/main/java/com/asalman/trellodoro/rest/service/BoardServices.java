package com.asalman.trellodoro.rest.service;


import com.asalman.trellodoro.events.api.APIErrorEvent;
import com.asalman.trellodoro.events.api.BoardsLoadedEvent;
import com.asalman.trellodoro.events.api.LoadBoardEvent;
import com.asalman.trellodoro.models.Board;
import com.asalman.trellodoro.rest.RestClient;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by asalman on 1/9/16.
 */
public class BoardServices {
    private Bus mBus;

    public BoardServices(Bus bus) {
        mBus = bus;
        bus.register(this);
    }

    @Subscribe
    public void onLoadBoards(LoadBoardEvent event) {
        RestClient.getBoardServices().getAllBoards(new Callback<List<Board>>() {
            @Override
            public void success(List<Board> boardList, Response response) {
                mBus.post(new BoardsLoadedEvent(boardList));
            }

            @Override
            public void failure(RetrofitError error) {
                mBus.post(new APIErrorEvent(error));
            }
        });
    }
}
