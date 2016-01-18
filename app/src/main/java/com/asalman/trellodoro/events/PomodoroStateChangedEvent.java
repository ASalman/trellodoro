package com.asalman.trellodoro.events;

import com.asalman.trellodoro.pomodoro.IPomodoroStorage;

/**
 * Created by asalman on 1/16/16.
 */
public class PomodoroStateChangedEvent {

    int mPreviousState;
    int mCurrentState;
    IPomodoroStorage mPomodoro;

    public PomodoroStateChangedEvent(IPomodoroStorage pomodoro, int previousState, int currentState){
        this.mPomodoro = pomodoro;
        this.mPreviousState = previousState;
        this.mCurrentState = currentState;
    }

    public int getPreviousState() {
        return mPreviousState;
    }

    public int getCurrentState() {
        return mCurrentState;
    }

    public IPomodoroStorage getPomodoro() {
        return mPomodoro;
    }
}
