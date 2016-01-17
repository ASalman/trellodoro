package com.asalman.trellodoro.application;

import android.app.Application;
import android.content.Context;

import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.rest.AccessToken;
import com.asalman.trellodoro.rest.service.BoardServices;
import com.asalman.trellodoro.rest.service.CardServices;
import com.asalman.trellodoro.rest.service.ColumnServices;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by asalman on 1/9/16.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    private static AccessToken accessToken;
    private BoardServices boardServices;
    private CardServices cardServices;
    private ColumnServices columnServices;

    public MyApplication() {
        super();
        instance = this;
        boardServices = new BoardServices(BusProvider.getInstance());
        cardServices = new CardServices(BusProvider.getInstance());
        columnServices = new ColumnServices(BusProvider.getInstance());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }

    public static MyApplication getApp() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    public static AccessToken getAccessToken(){
        if (accessToken == null){
            accessToken = new AccessToken();
        }
        return accessToken;
    }
}
