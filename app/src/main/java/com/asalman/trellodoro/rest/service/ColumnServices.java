package com.asalman.trellodoro.rest.service;

import com.asalman.trellodoro.events.api.APIErrorEvent;
import com.asalman.trellodoro.events.api.ColumnsLoadedEvent;
import com.asalman.trellodoro.events.api.LoadColumnsEvent;
import com.asalman.trellodoro.models.Column;
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
public class ColumnServices {
    private Bus mBus;

    public ColumnServices(Bus bus) {
        mBus = bus;
        bus.register(this);
    }

    @Subscribe
    public void onLoadStories(LoadColumnsEvent event) {
        RestClient.getColumnServices().getBoardLists(event.getBoardID(),
                new Callback<List<Column>>() {
                    @Override
                    public void success(List<Column> columnList, Response response) {
                        mBus.post(new ColumnsLoadedEvent(columnList));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mBus.post(new APIErrorEvent(error));
                    }
                });
    }
}