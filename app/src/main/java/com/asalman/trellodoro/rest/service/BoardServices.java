package com.asalman.trellodoro.rest.service;


import com.asalman.trellodoro.models.Board;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by asalman on 12/30/15.
 */
public interface BoardServices {

    @GET("/members/me/boards")
    public void getAllBoards(Callback<List<Board>> response);
}
