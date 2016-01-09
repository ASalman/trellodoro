package com.asalman.trellodoro.rest.service;

import com.asalman.trellodoro.models.Column;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by asalman on 12/30/15.
 */
public interface IColumnServices {

    @GET("/boards/{board_id}/lists")
    public void getBoardLists(@Path("board_id") String board_id, Callback<List<Column>> response);
}
