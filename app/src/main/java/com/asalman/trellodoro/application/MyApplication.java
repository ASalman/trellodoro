package com.asalman.trellodoro.application;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.db.DatabaseHelper;
import com.asalman.trellodoro.pomodoro.DBPomodoroStorage;
import com.asalman.trellodoro.pomodoro.Pomodoro;
import com.asalman.trellodoro.preferences.Config;
import com.asalman.trellodoro.rest.AccessToken;
import com.asalman.trellodoro.rest.service.BoardServices;
import com.asalman.trellodoro.rest.service.CardServices;
import com.asalman.trellodoro.rest.service.ColumnServices;
import com.crashlytics.android.Crashlytics;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.TypiconsIcons;
import com.joanzapata.iconify.fonts.TypiconsModule;

import io.fabric.sdk.android.Fabric;
import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by asalman on 1/9/16.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    private static Pomodoro mPomodoro;
    private static AccessToken accessToken;
    private BoardServices boardServices;
    private CardServices cardServices;
    private ColumnServices columnServices;
    private DatabaseHelper databaseHelper;

    public MyApplication() {
        super();
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        JodaTimeAndroid.init(this);
        databaseHelper = new DatabaseHelper(this);
        boardServices = new BoardServices(BusProvider.getInstance());
        cardServices = new CardServices(BusProvider.getInstance());
        columnServices = new ColumnServices(BusProvider.getInstance());
        Iconify
                .with(new FontAwesomeModule())
                .with(new TypiconsModule());
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

    public static NotificationManagerCompat provideNotificationManager() {
        return NotificationManagerCompat.from(getApp());
    }

    public static AlarmManager provideAlarmManager() {
        return (AlarmManager) getApp().getSystemService(Context.ALARM_SERVICE);
    }

    public static Pomodoro getPomodoro(){
        if (mPomodoro == null  && ! "".equals(Config.getActiveCardID())) {
            mPomodoro = new Pomodoro(new DBPomodoroStorage(Config.getActiveCardID()));
        }
        return mPomodoro;
    }

    public static void setPomodoro(Pomodoro pomodoro){
        mPomodoro = pomodoro;
    }

    public DatabaseHelper getDatabaseHelper(){
        return this.databaseHelper;
    }

    @Override
    public void onTerminate() {
        this.databaseHelper.close();
        super.onTerminate();
    }
}
