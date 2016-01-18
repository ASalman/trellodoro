package com.asalman.trellodoro.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.asalman.trellodoro.services.NotificationService;

/**
 * Created by asalman on 1/16/16.
 */
public class NotificationReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.setAction(intent.getAction());
        serviceIntent.setData(intent.getData());
        serviceIntent.putExtras(intent);
        startWakefulService(context, serviceIntent);
    }

}
