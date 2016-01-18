package com.asalman.trellodoro.pomodoro;

import com.asalman.trellodoro.application.MyApplication;
import com.asalman.trellodoro.bus.BusProvider;
import com.asalman.trellodoro.db.PomodoroDAL;
import com.asalman.trellodoro.events.PomodoroStateChangedEvent;
import com.asalman.trellodoro.models.Card;
import com.squareup.otto.Bus;

import org.joda.time.DateTime;

/**
 * Created by asalman on 1/17/16.
 */
public class DBPomodoroStorage implements IPomodoroStorage {

    private PomodoroDAL mPomodoroDAL;
    private Card mCard = null;
    private Bus mBus = BusProvider.getInstance();

    private DBPomodoroStorage() {
        mPomodoroDAL = new PomodoroDAL(MyApplication.getApp().getDatabaseHelper());
    }

    public DBPomodoroStorage(String cardID) {
        this();
        this.mCard = this.mPomodoroDAL.get(cardID);
    }

    public DBPomodoroStorage(Card card) {
        this();
        this.mCard = this.mPomodoroDAL.sync(card);
    }

    public Card getCard(){
        return this.mCard;
    }

    public String getCardID() {
        return this.mCard.getId();
    }

    public void setLastPomodoro(DateTime lastPomodoro) {
        this.mCard.setLastPomodoroTime(lastPomodoro.getMillis());
        mPomodoroDAL.update(this.mCard);
    }

    public DateTime getLastPomodoro() {
        return new DateTime(this.mCard.getLastPomodoroTime());
    }

    public void setNextPomodoro(DateTime nextPomodoro) {
        this.mCard.setNextPomodoroTime(nextPomodoro == null ? 0 : nextPomodoro.getMillis());
        mPomodoroDAL.update(this.mCard);
    }

    public DateTime getNextPomodoro() {
        return new DateTime(this.mCard.getNextPomodoroTime());
    }

    public void setState(int state) {
        this.setState(state, this.mCard.getCurrentState());
    }

    public void setState(int state, int previousState) {
        this.setPreviousState(previousState);

        this.mCard.setPreviousState(previousState);
        this.mCard.setCurrentState(state);
        this.mPomodoroDAL.update(this.mCard);

        this.mBus.post(new PomodoroStateChangedEvent(this, previousState, state));
    }

    public int getState() {
        return this.mCard.getCurrentState();
    }

    public void setPreviousState(int state) {
        this.mCard.setPreviousState(state);
        this.mPomodoroDAL.update(this.mCard);
    }

    public int getPreviousState() {
        return this.mCard.getPreviousState();
    }

    public void setRemainingTime(long remainingTime) {
        this.mCard.setRemainingTime(remainingTime);
        this.mPomodoroDAL.update(mCard);
    }

    public long getRemainingTime() {
        return this.mCard.getRemainingTime();
    }

    public void increaseSpentPomodoros() {
        int totalPomodoros = getSpentPomodoros() + 1;
        this.mCard.setSpentPomodoros(totalPomodoros);
        this.mPomodoroDAL.update(mCard);
    }

    public int getSpentPomodoros() {
        return this.mCard.getSpentPomodoros();
    }

    public void addSpentTime(long time) {
        long totalTime = time + getSpentTime();
        this.mCard.setTotalSpentTime(totalTime);
        this.mPomodoroDAL.update(this.mCard);
    }

    public long getSpentTime() {
        return this.mCard.getTotalSpentTime();
    }

}