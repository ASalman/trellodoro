package com.asalman.trellodoro.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by asalman on 1/22/16.
 */
public class Analytics {

    private Tracker mTracker;

    public static class AppCategories{
        public final static String CLICKS = "clicks";
        public final static String NOTIFICATION = "notification";
    }


     public Analytics(Tracker tracker) {
        this.mTracker = tracker;
    }

    public void sendScreenView(String screenName) {
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void sendEvent(String category, String action, String label, long value) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(value)
                .build());
    }

    public void sendEvent(String category, String action, String label) {
        sendEvent(category, action, label, 0);
    }
}
