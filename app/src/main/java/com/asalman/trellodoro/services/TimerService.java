package com.asalman.trellodoro.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.events.PomodoroStateChangedEvent;
import com.asalman.trellodoro.events.TimerUpdateEvent;
import com.asalman.trellodoro.pomodoro.Pomodoro;
import com.asalman.trellodoro.utils.DateTimeUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by asalman on 1/17/16.
 */
public class TimerService extends Service {

    private Bus mBus = BusProvider.getInstance();
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    private Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            mBus.post(new TimerUpdateEvent());
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mBus.register(this);
        mBus.post(new TimerUpdateEvent());
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mUpdateRunnable);
        mBus.unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onCardStateChanged(PomodoroStateChangedEvent pomodoroStateChanged){
        Pomodoro mPomodoro = MyApplication.getPomodoro();
        if (mPomodoro != null && mPomodoro.isOngoing()) {
            mBus.post(new TimerUpdateEvent());
        } else {
            mHandler.removeCallbacks(mUpdateRunnable);
            mBus.post(new TimerUpdateEvent());
        }
    }

    @Subscribe
    public void onTimerUpdate(TimerUpdateEvent timerUpdateEvent){
        Pomodoro mPomodoro = MyApplication.getPomodoro();
        if (mPomodoro != null && mPomodoro.isOngoing()) {
            mHandler.removeCallbacks(mUpdateRunnable);
            mHandler.postDelayed(mUpdateRunnable, DateTimeUtils.SECOND_Millis);
        }
    }

}
