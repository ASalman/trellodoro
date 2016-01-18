package com.asalman.trellodoro.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.asalman.trellodoro.R;
import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.events.PomodoroStateChangedEvent;
import com.asalman.trellodoro.events.TimerUpdateEvent;
import com.asalman.trellodoro.models.Card;
import com.asalman.trellodoro.pomodoro.DBPomodoroStorage;
import com.asalman.trellodoro.pomodoro.Pomodoro;
import com.asalman.trellodoro.services.NotificationService;
import com.asalman.trellodoro.services.TimerService;
import com.asalman.trellodoro.utils.DateTimeUtils;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.joda.time.DateTime;

import mehdi.sakout.fancybuttons.FancyButton;

public class PomodoroActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String EXTRA_CARD_ID = "EXTRA_CARD_ID";

    Pomodoro mPomodoro;
    RoundCornerProgressBar mProgress;
    Card mCard;
    Bus mBus = BusProvider.getInstance();
    FancyButton btnPomodoroStart,btnPomodoroStop, btnPomodoroRestart, btnPomodoroPause,
            btnLongBreak, btnShortBreak, btnDone, btnTodo;
    TextView mTime;
    TextView mDescription;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnDone = (FancyButton) findViewById(R.id.btn_task_done);
        btnTodo = (FancyButton) findViewById(R.id.btn_task_todo);
        btnLongBreak = (FancyButton) findViewById(R.id.btn_long_break);
        btnShortBreak = (FancyButton) findViewById(R.id.btn_short_break);
        btnPomodoroPause = (FancyButton) findViewById(R.id.btn_pomodoro_pause);
        btnPomodoroRestart = (FancyButton) findViewById(R.id.btn_pomodoro_restart);
        btnPomodoroStart = (FancyButton) findViewById(R.id.btn_pomodoro_start);
        btnPomodoroStop = (FancyButton) findViewById(R.id.btn_pomodoro_stop);
        mProgress = (RoundCornerProgressBar) findViewById(R.id.progress);
        this.setTitle(mCard.getName());

        mTime = (TextView) findViewById(R.id.pomodoro_time);
        mDescription = (TextView) findViewById(R.id.pomodoro_description);

    }

    @Override
    public void onResume() {
        super.onResume();
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
        mProgress.setProgress(progress * 100);
        if (mPomodoro.isOngoing()) {
            final DateTime nextPomodoro = mPomodoro.getNextPomodoro();

            mTime.setText(DateTimeUtils.getRemainingTime(mPomodoro));
            mDescription.setText("Running"); // TODO: replace string
        } else {
            mTime.setText("00:00");
            mDescription.setText("Start New"); // TODO: replace string
        }
    }

    @UiThread
    public void uodateUIOnCardStateChanged(){

        int nextState = mPomodoro.getNextActions();
        btnDone.setVisibility((nextState & Pomodoro.Actions.DONE) != 0 ? View.VISIBLE : View.GONE);
        btnTodo.setVisibility((nextState & Pomodoro.Actions.TODO) != 0 ? View.VISIBLE : View.GONE);
        btnLongBreak.setVisibility((nextState & Pomodoro.Actions.START_LONG_BREAK) != 0 ? View.VISIBLE : View.GONE);
        btnShortBreak.setVisibility((nextState & Pomodoro.Actions.START_SHORT_BREAK) != 0 ? View.VISIBLE : View.GONE);
        btnPomodoroStart.setVisibility((nextState & Pomodoro.Actions.START_POMDORO) != 0 ? View.VISIBLE : View.GONE);
        btnPomodoroStop.setVisibility(((nextState & Pomodoro.Actions.STOP_POMDORO) |
                (nextState & Pomodoro.Actions.STOP_BREAK)
        ) != 0 ? View.VISIBLE : View.GONE);
        btnPomodoroPause.setVisibility((nextState & Pomodoro.Actions.PAUSE_POMDORO) != 0 ? View.VISIBLE : View.GONE);
        btnPomodoroRestart.setVisibility((nextState & Pomodoro.Actions.RESTART_POMDORO) != 0 ? View.VISIBLE : View.GONE);

        btnDone.setOnClickListener(this);
        btnTodo.setOnClickListener(this);
        btnLongBreak.setOnClickListener(this);
        btnShortBreak.setOnClickListener(this);
        btnPomodoroStart.setOnClickListener(this);
        btnPomodoroStop.setOnClickListener(this);
        btnPomodoroPause.setOnClickListener(this);
        btnPomodoroRestart.setOnClickListener(this);

    }

    public void start() {
        mProgress.setProgress(0);
        mProgress.setMax(100);
        if (mPomodoro.isOngoing()) {
            sendBroadcast(NotificationService.STOP_INTENT);
        } else {
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
        MyApplication.setPomodoro(mPomodoro);
        if (v.getId() == R.id.btn_pomodoro_start){
            start();
        } else if (v.getId() == R.id.btn_pomodoro_stop){
            start();
        } else if (v.getId() == R.id.btn_long_break) {
            final Intent startIntent = NotificationService.START_INTENT;
            startIntent.putExtra(NotificationService.EXTRA_POMODORO_STATE, Pomodoro.States.LONG_BREAK);
            sendBroadcast(startIntent);
        } else if (v.getId() == R.id.btn_short_break) {
            final Intent startIntent = NotificationService.START_INTENT;
            startIntent.putExtra(NotificationService.EXTRA_POMODORO_STATE, Pomodoro.States.SHORT_BREAK);
            sendBroadcast(startIntent);
        } else if (v.getId() == R.id.btn_pomodoro_pause) {
            final Intent startIntent = NotificationService.PAUSE_INTENT;
            sendBroadcast(startIntent);
        } else if (v.getId() == R.id.btn_pomodoro_restart) {
            final Intent startIntent = NotificationService.RESTART_INTENT;
            sendBroadcast(startIntent);
        }

    }
}