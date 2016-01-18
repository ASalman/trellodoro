package com.asalman.trellodoro.notification;

/**
 * Created by asalman on 1/17/16.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import java.util.List;

public abstract class BaseNotificationBuilder {

    protected final Context context;

    public BaseNotificationBuilder( Context context) {
        if (context == null){
            throw new NullPointerException("Context can't be null, it's required to build BaseNotificationBuilder.");
        }
        this.context = context;
    }

    private NotificationCompat.Builder getNotificationBuilder(PendingIntent contentIntent) {

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(contentIntent)
                .setWhen(getWhen())
                .setColor(getColor())
                .setContentTitle(getContentTitle())
                .setContentText(getContentText())
                .setDefaults(getDefaults())
                .setSmallIcon(getSmallIcon())
                .setOnlyAlertOnce(true)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(Notification.PRIORITY_MAX)
                .setOngoing(getOngoing())
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setLocalOnly(true);

        List<NotificationCompat.Action> actions = getActions();
        for (NotificationCompat.Action action: actions) {
            builder.addAction(action);
        }

        return builder;
    }

    public Notification getNotification(PendingIntent contentIntent) {
        NotificationCompat.Builder builder = getNotificationBuilder(contentIntent);
        builder = prepareNotificationBuilder(builder);
        return builder.build();
    }

    protected NotificationCompat.Builder prepareNotificationBuilder(final NotificationCompat.Builder builder) {
        return builder;
    }

    protected abstract List<NotificationCompat.Action>  getActions();

    protected abstract boolean getOngoing();

    protected abstract String getContentTitle();

    protected abstract String getContentText();

    protected abstract int getColor();

    protected abstract int getDefaults();

    protected abstract int getSmallIcon();

    protected abstract long getWhen();
}