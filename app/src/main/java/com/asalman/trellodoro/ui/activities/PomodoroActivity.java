package com.asalman.trellodoro.ui.activities;


import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.asalman.trellodoro.R;
import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.events.PomodoroStateChangedEvent;
import com.asalman.trellodoro.events.TimerUpdateEvent;
import com.asalman.trellodoro.events.api.UpdateCardEvent;
import com.asalman.trellodoro.models.Card;
import com.asalman.trellodoro.pomodoro.DBPomodoroStorage;
import com.asalman.trellodoro.pomodoro.Pomodoro;
import com.asalman.trellodoro.preferences.Config;
import com.asalman.trellodoro.services.NotificationService;
import com.asalman.trellodoro.services.TimerService;
import com.asalman.trellodoro.utils.Analytics;
import com.asalman.trellodoro.utils.DateTimeUtils;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.TypiconsIcons;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import mehdi.sakout.fancybuttons.FancyButton;

public class PomodoroActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String EXTRA_CARD_ID = "EXTRA_CARD_ID";
    private final static String TAG = PomodoroActivity.class.getName();

    private class ButtonTags {
        private static final String TAG_POMODORO_START = "pomo_start";
        private static final String TAG_POMODORO_RESTART = "pomo_restat";
        private static final String TAG_POMODORO_PAUSE = "pomo_pause";
        private static final String TAG_BREAK_LONG = "break_long";
        private static final String TAG_BREAK_SHORT = "break_short";
        private static final String TAG_STOP = "pomo_stop";
    }

    Pomodoro mPomodoro;
    ArcProgress mProgress;
    Card mCard;
    Bus mBus = BusProvider.getInstance();
    FancyButton btnPrimary, btnSecondary;
    FloatingActionButton btnDone, btnTodo;
    TextView mTitle, mDescription;
    Icon icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (!this.getIntent().hasExtra(EXTRA_CARD_ID)) {
            Toast.makeText(this, "This card not advisable!", Toast.LENGTH_SHORT).show();
            finish();
        }

        String cardID = this.getIntent().getStringExtra(EXTRA_CARD_ID);
        DBPomodoroStorage pomodoroStorage = new DBPomodoroStorage(cardID);
        mCard = pomodoroStorage.getCard();
        if (mCard == null) {
            Toast.makeText(this, "This card not advisable!", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (MyApplication.getPomodoro() != null &&
                MyApplication.getPomodoro().getID().equals(mCard.getId())) {
            mPomodoro = MyApplication.getPomodoro();
        } else {
            mPomodoro = new Pomodoro(pomodoroStorage);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);

        btnPrimary = (FancyButton) findViewById(R.id.btn_primary);
        btnSecondary = (FancyButton) findViewById(R.id.btn_secondary);
        btnPrimary.setOnClickListener(this);
        btnSecondary.setOnClickListener(this);



        btnDone = (FloatingActionButton) findViewById(R.id.btn_task_done);
        btnTodo = (FloatingActionButton) findViewById(R.id.btn_task_todo);
        btnDone.setImageDrawable(new IconDrawable(this, TypiconsIcons.typcn_thumbs_up).colorRes(R.color.icons));
        btnTodo.setImageDrawable(new IconDrawable(this, TypiconsIcons.typcn_arrow_back_outline).colorRes(R.color.icons));
        mProgress = (ArcProgress) findViewById(R.id.progress);

        mDescription = (TextView) findViewById(R.id.pomodoro_description);
        mTitle = (TextView) findViewById(R.id.txt_title);
        mTitle.setText(mCard.getName());

    }

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getAnalytics().sendScreenView(TAG);
        mBus.register(this);
        startService(new Intent(this, TimerService.class));
        uodateUIOnCardStateChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
        stopService(new Intent(this, TimerService.class));
    }


    @Subscribe
    public void onTimerUpdate(TimerUpdateEvent timerUpdate){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUIOnTimerUpdate();
            }
        });

    }

    @Subscribe
    public void onCardStateChanged(PomodoroStateChangedEvent timerUpdate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                uodateUIOnCardStateChanged();
            }
        });
    }

    @UiThread
    public void updateUIOnTimerUpdate(){

        float progress = 1 -
                (float) (mPomodoro.getNextPomodoro().getMillis() - DateTime.now().getMillis())
                        / mPomodoro.getCurrentDuration();
        mProgress.setProgress((int) (progress * 100));
        if (mPomodoro.isOngoing()) {
            final DateTime nextPomodoro = mPomodoro.getNextPomodoro();

            mDescription.setText("Running"); // TODO: replace string
            mProgress.setBottomText(DateTimeUtils.getRemainingTime(mPomodoro));
        } else {
            mProgress.setBottomText("00:00");
            mDescription.setText("Start New"); // TODO: replace string
        }
    }

    @UiThread
    public void uodateUIOnCardStateChanged(){

        int nextState = mPomodoro.getNextActions();

        if ((nextState & Pomodoro.Actions.START_POMDORO) != 0) {
           btnPrimary.setIconResource(getString(R.string.icon_play));
            btnPrimary.setTag(ButtonTags.TAG_POMODORO_START);
        } else if ((nextState & Pomodoro.Actions.PAUSE_POMDORO) != 0) {
            btnPrimary.setIconResource(getString(R.string.icon_pause));
            btnPrimary.setTag(ButtonTags.TAG_POMODORO_PAUSE);
        } else if ((nextState & Pomodoro.Actions.RESTART_POMDORO) != 0) {
            btnPrimary.setIconResource(getString(R.string.icon_restart));
            btnPrimary.setTag(ButtonTags.TAG_POMODORO_RESTART);
        } else if ((nextState & Pomodoro.Actions.START_POMDORO) != 0) {
            btnPrimary.setIconResource(getString(R.string.icon_play));
            btnPrimary.setTag(ButtonTags.TAG_POMODORO_START);
        } else if ((nextState & Pomodoro.Actions.START_SHORT_BREAK) != 0) {
            btnPrimary.setIconResource(getString(R.string.icon_coffee));
            btnPrimary.setTag(ButtonTags.TAG_BREAK_SHORT);
        } else if ((nextState & Pomodoro.Actions.STOP_BREAK) != 0) {
            btnPrimary.setIconResource(getString(R.string.icon_stop));
            btnPrimary.setTag(ButtonTags.TAG_STOP);
        }

        if ((nextState & Pomodoro.Actions.STOP_POMDORO) != 0) {
            btnSecondary.setVisibility(View.VISIBLE);
            btnSecondary.setIconResource(getString(R.string.icon_stop));
            btnSecondary.setTag(ButtonTags.TAG_STOP);
        } else if ((nextState & Pomodoro.Actions.START_LONG_BREAK) != 0) {
            btnSecondary.setVisibility(View.VISIBLE);
            btnSecondary.setIconResource(getString(R.string.icon_beer));
            btnSecondary.setTag(ButtonTags.TAG_BREAK_LONG);
        } else if ((nextState & Pomodoro.Actions.STOP_POMDORO) != 0) {
            btnSecondary.setIconResource(getString(R.string.icon_stop));
            btnSecondary.setTag(ButtonTags.TAG_STOP);
        } else {
            btnSecondary.setVisibility(View.GONE);
        }


        if ((nextState & Pomodoro.Actions.DONE) != 0) {
            btnDone.show();
        } else {
            btnDone.hide();
        }
        if ((nextState & Pomodoro.Actions.TODO) != 0) {
            btnTodo.show();
        } else {
            btnTodo.hide();
        }

        btnDone.setOnClickListener(this);
        btnTodo.setOnClickListener(this);
    }

    public void start() {
        mProgress.setProgress(0);
        mBus.post(new UpdateCardEvent(mCard.getId(), Config.getDoingListID()));
        mCard.setIdList(Config.getTodoListID());


        if (mPomodoro.isOngoing()) {
            sendBroadcast(NotificationService.STOP_INTENT);
        } else {
            Config.setActiveCardID(mCard.getId());
            int activityType = mPomodoro.getState();
            if (activityType == Pomodoro.States.NONE) {
                activityType = Pomodoro.States.POMODORO;
            }
            final Intent startIntent = NotificationService.START_INTENT;
            startIntent.putExtra(NotificationService.EXTRA_POMODORO_STATE, activityType);
            sendBroadcast(startIntent);
        }
    }

    @Override
    public void onClick(View v) {
        MyApplication.getAnalytics().sendEvent(Analytics.AppCategories.CLICKS,
                getResources().getResourceEntryName(v.getId()),
                getResources().getResourceEntryName(v.getId()));
        MyApplication.setPomodoro(mPomodoro);
        if (ButtonTags.TAG_POMODORO_START.equals(v.getTag()) ){
            start();
        } else if (ButtonTags.TAG_STOP.equals(v.getTag())) {
            start();
        } else if (ButtonTags.TAG_BREAK_LONG.equals(v.getTag())) {
            final Intent startIntent = NotificationService.START_INTENT;
            startIntent.putExtra(NotificationService.EXTRA_POMODORO_STATE, Pomodoro.States.LONG_BREAK);
            sendBroadcast(startIntent);
        } else if (ButtonTags.TAG_BREAK_SHORT.equals(v.getTag())) {
            final Intent startIntent = NotificationService.START_INTENT;
            startIntent.putExtra(NotificationService.EXTRA_POMODORO_STATE, Pomodoro.States.SHORT_BREAK);
            sendBroadcast(startIntent);
        } else if (ButtonTags.TAG_POMODORO_PAUSE.equals(v.getTag())) {
            final Intent startIntent = NotificationService.PAUSE_INTENT;
            sendBroadcast(startIntent);
        } else if (ButtonTags.TAG_POMODORO_RESTART.equals(v.getTag())) {
            final Intent startIntent = NotificationService.RESTART_INTENT;
            sendBroadcast(startIntent);
        }

        if (v.getId() == R.id.btn_task_done){
            mBus.post(new UpdateCardEvent(mCard.getId(), Config.getDoneListID()));
            mCard.setIdList(Config.getDoneListID());
            finish();
        } else if (v.getId() == R.id.btn_task_todo){
            mBus.post(new UpdateCardEvent(mCard.getId(), Config.getTodoListID()));
            mCard.setIdList(Config.getTodoListID());
            finish();
        }

    }
}