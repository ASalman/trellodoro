package com.asalman.trellodoro.pomodoro;

import android.app.Application;

import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.events.PomodoroStateChangedEvent;
import com.asalman.trellodoro.preferences.HelperSharedPreferences;
import com.squareup.otto.Bus;

import org.joda.time.DateTime;

/**
 * Created by asalman on 1/16/16.
 */
public class PreferencesPomodoroStorage implements IPomodoroStorage {

    private final Application app;
    private Bus mBus = BusProvider.getInstance();
    private String cardID = "";

    private static class PreferencesKeys{
        public static final String CARD_ID = "card_id";
        public static final String PREVIOUS_STATE = "pomodoro_previous_state";
        public static final String CURRENT_STATE = "pomodoro_current_state";
        public static final String LAST_POMODORO = "last_pomodoro";
        public static final String NEXT_POMODORO = "next_pomodoro";
        public static final String REMAINING_TIME = "remaining_time";
        public static final String SPENT_POMODOROS = "spent_pomodoros";
        public static final String TOTAL_TIME = "total_time";
    }

    public PreferencesPomodoroStorage(Application app, String cardID){
        this.app = app;
        this.cardID = cardID;
    }

    public String getCardID(){
        return this.cardID;
    }

    public void setLastPomodoro(DateTime nextPomodoro) {
        HelperSharedPreferences.putDateTime(app.getBaseContext(), cardID + PreferencesKeys.LAST_POMODORO, nextPomodoro);
    }

    public DateTime getLastPomodoro() {
        return HelperSharedPreferences.getDateTime(app.getBaseContext(), cardID + PreferencesKeys.LAST_POMODORO, null);
    }

    public void setNextPomodoro(DateTime nextPomodoro) {
        HelperSharedPreferences.putDateTime(app.getBaseContext(), cardID + PreferencesKeys.NEXT_POMODORO, nextPomodoro);
    }

    public DateTime getNextPomodoro() {
        return HelperSharedPreferences.getDateTime(app.getBaseContext(), cardID + PreferencesKeys.NEXT_POMODORO, null);
    }

    public void setState(int state) {
        int previousState = getState();
        this.setState(state, previousState);
    }

    public void setState(int state, int previousState){
        this.setPreviousState(previousState);
        HelperSharedPreferences.putInt(app.getBaseContext(),
                cardID + PreferencesKeys.CURRENT_STATE, state);
        this.mBus.post(new PomodoroStateChangedEvent(this, previousState, state ));
    }

    public int getState() {
        return HelperSharedPreferences.getInt(app.getBaseContext(),
                cardID + PreferencesKeys.CURRENT_STATE, Pomodoro.States.NONE);
    }

    public void setPreviousState(int state){
        HelperSharedPreferences.putInt(app.getBaseContext(),
                cardID + PreferencesKeys.PREVIOUS_STATE, state);
    }

    public int getPreviousState(){
        return HelperSharedPreferences.getInt(app.getBaseContext(),
                cardID + PreferencesKeys.PREVIOUS_STATE, Pomodoro.States.NONE);
    }

    public void setRemainingTime(long remainingTime){
        HelperSharedPreferences.putLong(app.getBaseContext(),
                cardID + PreferencesKeys.REMAINING_TIME, remainingTime);
    }

    public long getRemainingTime() {
        return HelperSharedPreferences.getLong(app.getBaseContext(),
                cardID + PreferencesKeys.REMAINING_TIME, 0);
    }

    public void increaseSpentPomodoros() {
        int totalPomodoros =  getSpentPomodoros() + 1;
        HelperSharedPreferences.putInt(app.getBaseContext(),
                cardID + PreferencesKeys.SPENT_POMODOROS, totalPomodoros);
    }

    public int getSpentPomodoros() {
        return HelperSharedPreferences.getInt(app.getBaseContext(),
                cardID + PreferencesKeys.SPENT_POMODOROS, 0);
    }

    public void addSpentTime(long time) {
        long totalTime = time + getSpentTime();
        HelperSharedPreferences.putLong(app.getBaseContext(),
                cardID + PreferencesKeys.TOTAL_TIME, totalTime);
    }

    public long getSpentTime() {
        return HelperSharedPreferences.getLong(app.getBaseContext(),
                cardID + PreferencesKeys.TOTAL_TIME, 0);
    }
}
