package com.asalman.trellodoro.services;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.pomodoro.Pomodoro;
import com.asalman.trellodoro.pomodoro.PomodoroNotificationBuilder;
import com.asalman.trellodoro.receiver.NotificationReceiver;
import com.asalman.trellodoro.ui.activities.PomodoroActivity;
import com.asalman.trellodoro.utils.DateTimeUtils;

import org.joda.time.DateTime;

/**
 * Created by asalman on 1/16/16.
 */
public class NotificationService extends IntentService{
    public static final String EXTRA_POMODORO_STATE = "com.asalman.trellodoro.extra.CURRENT_STATE";

    public static final String ACTION_START = "com.asalman.trellodoro.action.START";
    public static final String ACTION_STOP = "com.asalman.trellodoro.action.STOP";
    public static final String ACTION_PAUSE = "com.asalman.trellodoro.action.PAUSE";
    public static final String ACTION_RESTART = "com.asalman.trellodoro.action.RESTART";
    public static final String ACTION_DISMISS = "com.asalman.trellodoro.action.DISMISS";
    public static final String ACTION_UPDATE = "com.asalman.trellodoro.action.UPDATE";
    public static final String ACTION_FINISH_ALARM = "com.asalman.trellodoro.action.ALARM";

    public static final Intent START_INTENT = new Intent(ACTION_START);
    public static final Intent STOP_INTENT = new Intent(ACTION_STOP);
    public static final Intent PAUSE_INTENT = new Intent(ACTION_PAUSE);
    public static final Intent RESTART_INTENT = new Intent(ACTION_RESTART);
    public static final Intent UPDATE_INTENT = new Intent(ACTION_UPDATE);
    public static final Intent FINISH_ALARM_INTENT = new Intent(ACTION_FINISH_ALARM);

    private static final int REQUEST_UPDATE = 1;
    private static final int REQUEST_FINISH = 2;

    private static final int NOTIFICATION_ID_ONGOING = 10001;
    private static final int NOTIFICATION_ID_NORMAL = 10002;

    protected Pomodoro mPomodoro;
    NotificationManagerCompat notificationManager;
    AlarmManager alarmManager;

    public NotificationService() {
        super("NotificationService");
        notificationManager = MyApplication.provideNotificationManager();
        alarmManager = MyApplication.provideAlarmManager();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent == null) {
            return;
        }

        mPomodoro = MyApplication.getPomodoro();
        boolean alarm = false;

        switch (intent.getAction()) {
            case ACTION_START:
                final int activityType = intent.getIntExtra(EXTRA_POMODORO_STATE, 0);
                start(this, activityType);
                break;
            case ACTION_STOP:
                stop(this);
                break;
            case ACTION_PAUSE:
                pause(this);
                break;
            case ACTION_RESTART:
                restart(this);
                break;
            case ACTION_FINISH_ALARM:
                alarm = true;
                finishAlarm(this);
                break;
            case ACTION_DISMISS:
                notificationManager.cancel(NOTIFICATION_ID_NORMAL);
                notificationManager.cancel(NOTIFICATION_ID_ONGOING);
                break;
            default:
                break;
        }

        if (!intent.getAction().equals(ACTION_DISMISS)) {
            updateNotification(this, mPomodoro, alarm);
        }

        boolean complete = NotificationReceiver.completeWakefulIntent(intent);
        Log.d(NotificationService.class.getName(), "Action Called: " + intent.getAction());
    }


    private void updateNotification(Context context, Pomodoro pomodoro, boolean alarm) {
        Notification notification = null;
        PomodoroNotificationBuilder builder = new PomodoroNotificationBuilder(context, pomodoro, alarm);
        Intent contentIntent = new Intent(context, PomodoroActivity.class);
        notification = builder.getNotification(PendingIntent.getActivity(context, 0, contentIntent, 0));


        if (notification != null) {
            notificationManager.notify(pomodoro.isOngoing()
                            ? NOTIFICATION_ID_ONGOING
                            : NOTIFICATION_ID_NORMAL,
                    notification);
        }
    }

    private void start(Context context, int state) {
        notificationManager.cancel(NOTIFICATION_ID_NORMAL);
        if (state != Pomodoro.States.NONE
                && !mPomodoro.isOngoing()) {
            DateTime nextPomodoro = mPomodoro.start(state, null);

            setAlarm(context, REQUEST_FINISH, FINISH_ALARM_INTENT, nextPomodoro);
            setRepeatingAlarm(context, REQUEST_UPDATE, UPDATE_INTENT);
        }
    }

    private void stop(Context context) {
        mPomodoro.stop(false);

        notificationManager.cancel(NOTIFICATION_ID_NORMAL);
        notificationManager.cancel(NOTIFICATION_ID_ONGOING);
        cancelAlarm(context, REQUEST_FINISH, FINISH_ALARM_INTENT);
        cancelAlarm(context, REQUEST_UPDATE, UPDATE_INTENT);
    }

    private void pause(Context context){
        mPomodoro.pause();
        notificationManager.cancel(NOTIFICATION_ID_NORMAL);
        notificationManager.cancel(NOTIFICATION_ID_ONGOING);
        cancelAlarm(context, REQUEST_FINISH, FINISH_ALARM_INTENT);
        cancelAlarm(context, REQUEST_UPDATE, UPDATE_INTENT);
    }

    private void restart(Context context) {
        notificationManager.cancel(NOTIFICATION_ID_NORMAL);
        if (mPomodoro.getState() != Pomodoro.States.NONE
                && !mPomodoro.isOngoing()) {
            DateTime nextPomodoro = mPomodoro.restart();

            setAlarm(context, REQUEST_FINISH, FINISH_ALARM_INTENT, nextPomodoro);
            setRepeatingAlarm(context, REQUEST_UPDATE, UPDATE_INTENT);
        }
    }

    private void finishAlarm(Context context) {
        notificationManager.cancel(mPomodoro.isOngoing()
                ? NOTIFICATION_ID_ONGOING
                : NOTIFICATION_ID_NORMAL);

        cancelAlarm(context, REQUEST_FINISH, FINISH_ALARM_INTENT);
        cancelAlarm(context, REQUEST_UPDATE, UPDATE_INTENT);

        mPomodoro.stop(true);
    }

    private void setRepeatingAlarm(Context context, int requestCode, Intent intent) {
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + DateTimeUtils.MINUTE_Millis + 1000, DateTimeUtils.MINUTE_Millis, pendingIntent);
    }

    @TargetApi(19)
    private void setAlarm(Context context, int requestCode, Intent intent, DateTime time) {
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time.getMillis(), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, time.getMillis(), pendingIntent);
        }
    }

    private void cancelAlarm(Context context, int requestCode, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
