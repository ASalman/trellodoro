package com.asalman.trellodoro.pomodoro;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.asalman.trellodoro.R;
import com.asalman.trellodoro.notification.BaseNotificationBuilder;
import com.asalman.trellodoro.services.NotificationService;
import com.asalman.trellodoro.utils.DateTimeUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asalman on 1/17/16.
 */
public class PomodoroNotificationBuilder extends BaseNotificationBuilder {

    private static final int ID_START = 1;
    private static final int ID_STOP = 2;
    private static final int ID_PAUSE = 4;
    private static final int ID_RESTART = 8;
    private static final int ID_DISMISS = 16;

    private final Pomodoro mPomodoro;
    private final boolean mAlarm;
    private int notificationIcon;
    private int actionStartIcon;
    private int actionStopIcon;
    private int actionPauseIcon;
    private int actionRestartIcon;

    public PomodoroNotificationBuilder(Context context, Pomodoro mPomodoro, boolean alarm){
        super(context);
        this.mPomodoro = mPomodoro;
        this.mAlarm = alarm;
        this.notificationIcon = R.drawable.ic_notification_trellodoro;
        this.actionStartIcon =  R.drawable.ic_notification_play;
        this.actionStopIcon = R.drawable.ic_notification_stop;
        this.actionPauseIcon = R.drawable.ic_notification_pause;
        this.actionRestartIcon = R.drawable.ic_notification_restart;
    }

    @Override
    protected NotificationCompat.Builder prepareNotificationBuilder(NotificationCompat.Builder builder) {
        if (!mPomodoro.isOngoing()) {
            builder.setDeleteIntent(PendingIntent.getBroadcast(context, ID_DISMISS,
                    new Intent(NotificationService.ACTION_DISMISS), PendingIntent.FLAG_UPDATE_CURRENT));
        }
        return super.prepareNotificationBuilder(builder);
    }

    @Override
    protected List<NotificationCompat.Action> getActions() {
        ArrayList<NotificationCompat.Action> actions = new ArrayList<>();
        int nextActions = mPomodoro.getNextActions();
        NotificationCompat.Action action;

        if ((nextActions & Pomodoro.Actions.START_POMDORO) != 0){
            action = buildStartAction(context, Pomodoro.States.POMODORO);
            actions.add(action);
        }
        if ((nextActions & Pomodoro.Actions.START_LONG_BREAK) != 0) {
            action = buildStartAction(context, Pomodoro.States.LONG_BREAK);
            actions.add(action);
        }
        if ((nextActions & Pomodoro.Actions.START_SHORT_BREAK) != 0) {
            action = buildStartAction(context, Pomodoro.States.SHORT_BREAK);
            actions.add(action);
        }
        if ((nextActions & Pomodoro.Actions.STOP_BREAK) != 0 ||
                (nextActions & Pomodoro.Actions.STOP_POMDORO) != 0) {
            action = buildStopAction(context);
            actions.add(action);
        }
        if ((nextActions & Pomodoro.Actions.PAUSE_POMDORO) != 0) {
            action = buildPauseAction(context);
            actions.add(action);
        }
        if ((nextActions & Pomodoro.Actions.RESTART_POMDORO) != 0) {
            action = buildRestartAction(context);
            actions.add(action);
        }

        return actions;
    }

    @Override
    protected boolean getOngoing() {
        return mPomodoro.isOngoing();
    }

    @Override
    protected String getContentTitle() {
        if (mPomodoro.isOngoing()) {
            if (mPomodoro.getNextPomodoro() == null) {
                return null;
            }
            final String minutesLeft = DateTimeUtils.convertTimeLeftToPrettyMinutesLeft(context,
                    mPomodoro.getNextPomodoro().minus(DateTime.now().getMillis()));
            return minutesLeft + " | " + "Running"; // TODO: replace string
        } else {
            if (mPomodoro.getState() == Pomodoro.States.NONE) {
                return "Start New"; // TODO: replace string
            } else {
                return "Done";  // TODO: replace string
            }
        }
    }

    @Override
    protected String getContentText() {
        if (mPomodoro.isOngoing()) {
            return "Ruuning"; // TODO: replace string
        } else {
            return "Done"; // TODO: replace string
        }
    }

    @Override
    protected int getColor() {
        return context.getResources().getColor(R.color.primary);
    }

    @Override
    protected int getDefaults() {
        return this.mAlarm ?  Notification.DEFAULT_ALL : 0;
    }

    @Override
    protected int getSmallIcon() {
        return notificationIcon;
    }

    @Override
    protected long getWhen() {
        final DateTime nextPomodoro = mPomodoro.getNextPomodoro().minus(2);
        return nextPomodoro != null ? nextPomodoro.getMillis() : System.currentTimeMillis();
    }

    private NotificationCompat.Action buildStartAction( Context context, int state) {
        final Intent startActionIntent = NotificationService.START_INTENT;
        startActionIntent.putExtra(NotificationService.EXTRA_POMODORO_STATE, state);
        final PendingIntent startActionPendingIntent =
                PendingIntent.getBroadcast(context, ID_START, startActionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action.Builder(actionStartIcon,
                context.getString(R.string.lbl_start_pomodoro), startActionPendingIntent).build();
    }

    private NotificationCompat.Action buildStopAction( Context context) {
        final Intent stopActionIntent = NotificationService.STOP_INTENT;
        final PendingIntent stopActionPendingIntent =
                PendingIntent.getBroadcast(context, ID_STOP, stopActionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action.Builder(actionStopIcon,
                context.getString(R.string.lbl_stop), stopActionPendingIntent).build();
    }

    private NotificationCompat.Action buildPauseAction( Context context) {
        final Intent pauseActionIntent = NotificationService.PAUSE_INTENT;
        final PendingIntent startActionPendingIntent =
                PendingIntent.getBroadcast(context, ID_PAUSE, pauseActionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action.Builder(actionPauseIcon,
                context.getString(R.string.lbl_pause), startActionPendingIntent).build();
    }

    private NotificationCompat.Action buildRestartAction( Context context) {
        final Intent restartActionIntent = NotificationService.RESTART_INTENT;
        final PendingIntent startActionPendingIntent =
                PendingIntent.getBroadcast(context, ID_RESTART, restartActionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action.Builder(actionRestartIcon,
                context.getString(R.string.lbl_restart), startActionPendingIntent).build();
    }
}