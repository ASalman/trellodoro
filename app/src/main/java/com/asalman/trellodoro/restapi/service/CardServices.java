package com.asalman.trellodoro.restapi.service;



import com.asalman.trellodoro.models.Card;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by asalman on 12/30/15.
 */
public interface CardServices {

    @GET("/lists/{list_id}/cards")
    public void getListCards(@Path("list_id") String list_id, Callback<List<Card>> response);
}
