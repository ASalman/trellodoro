package com.asalman.trellodoro.pomodoro;

import org.joda.time.DateTime;

/**
 * Created by asalman on 1/16/16.
 */
public interface IPomodoroStorage {

    String getCardID();

    void setLastPomodoro(DateTime nextPomodoro);

    DateTime getLastPomodoro();

    void setNextPomodoro(DateTime nextPomodoro);

    DateTime getNextPomodoro();

    void setState(int state);

    void setState(int state, int previousState);

    int getState() ;

    void setPreviousState(int state);

    int getPreviousState();

    void setRemainingTime(long remainingTime);

    long getRemainingTime();

    void increaseSpentPomodoros();

    int getSpentPomodoros();

    void addSpentTime(long time);

    long getSpentTime();
}