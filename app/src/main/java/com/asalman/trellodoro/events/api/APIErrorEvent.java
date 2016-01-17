package com.asalman.trellodoro.events.api;

import retrofit.RetrofitError;

/**
 * Created by asalman on 1/9/16.
 */
public class APIErrorEvent {

    RetrofitError retrofitError;

    public APIErrorEvent(RetrofitError retrofitError){
        this.retrofitError = retrofitError;
    }
}
