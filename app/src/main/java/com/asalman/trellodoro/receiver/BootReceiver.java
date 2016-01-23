package com.asalman.trellodoro.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.asalman.trellodoro.services.NotificationService;

/**
 * Created by asalman on 1/23/16.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, NotificationService.class);
        i.setAction(NotificationService.ACTION_VALIDATE);
        context.startService(i);
    }

}
