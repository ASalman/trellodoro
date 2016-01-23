package com.asalman.trellodoro.application;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.asalman.trellodoro.BuildConfig;
import com.asalman.trellodoro.R;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.db.DatabaseHelper;
import com.asalman.trellodoro.events.api.APIErrorEvent;
import com.asalman.trellodoro.pomodoro.DBPomodoroStorage;
import com.asalman.trellodoro.pomodoro.Pomodoro;
import com.asalman.trellodoro.preferences.Config;
import com.asalman.trellodoro.rest.AccessToken;
import com.asalman.trellodoro.rest.service.BoardServices;
import com.asalman.trellodoro.rest.service.CardServices;
import com.asalman.trellodoro.rest.service.ColumnServices;
import com.asalman.trellodoro.ui.activities.ConfigWizardActivity;
import com.asalman.trellodoro.ui.widgets.Dialog;
import com.asalman.trellodoro.utils.Analytics;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.squareup.otto.Subscribe;

import io.fabric.sdk.android.Fabric;
import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by asalman on 1/9/16.
 */
public class MyApplication extends Application {
    private static MyApplication instance;
    private static Pomodoro mPomodoro;
    private static AccessToken accessToken;
    private static Analytics mAnalytics;
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
        Tracker tracker = GoogleAnalytics.getInstance(this).newTracker(BuildConfig.DEBUG?
                R.xml.debug_tracker :
                R.xml.prod_tracker);
        mAnalytics = new Analytics(tracker);
        JodaTimeAndroid.init(this);
        BusProvider.getInstance().register(this);
        databaseHelper = new DatabaseHelper(this);
        boardServices = new BoardServices(BusProvider.getInstance());
        cardServices = new CardServices(BusProvider.getInstance());
        columnServices = new ColumnServices(BusProvider.getInstance());
        Iconify.with(new FontAwesomeModule())
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

    public synchronized static Analytics getAnalytics(){
        return mAnalytics;
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

    @Subscribe
    public void handleAPIErrors(APIErrorEvent apiErrorEvent){
        if (apiErrorEvent.getRetrofitError().getResponse().getStatus() == 401 ) {
            getAccessToken().setValue("");
            Dialog dialog = new Dialog(this);
            dialog.showDialog();
        }
        // @// TODO: 1/23/16 handle other http errors 
    }

    @Override
    public void onTerminate() {
        this.databaseHelper.close();
        super.onTerminate();
    }
}
