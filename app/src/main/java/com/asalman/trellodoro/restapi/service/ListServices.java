package com.asalman.trellodoro.restapi.service;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by asalman on 12/30/15.
 */
public interface ListServices {

    @GET("/boards/{board_id}/lists")
    public void getBoardLists(@Path("board_id") String board_id, Callback<List<com.asalman.trellodoro.models.List>> response);
}
