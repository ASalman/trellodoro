package com.asalman.trellodoro.pomodoro;

import com.asalman.trellodoro.BuildConfig;
import com.asalman.trellodoro.bus.BusProvider;
import com.squareup.otto.Bus;

import org.joda.time.DateTime;

/**
 * Created by asalman on 1/18/16.
 */
public class Pomodoro {


    public static class States {
        public static final int NONE = 0;
        public static final int POMODORO = 1;
        public static final int PAUSED = 2;
        public static final int SHORT_BREAK = 3;
        public static final int LONG_BREAK = 4;
    }

    public static class Actions {
        public static final int START_POMDORO = 1;
        public static final int STOP_POMDORO = 2;
        public static final int PAUSE_POMDORO = 4;
        public static final int RESTART_POMDORO = 8;
        public static final int START_SHORT_BREAK = 16;
        public static final int START_LONG_BREAK = 32;
        public static final int STOP_BREAK = 64;
        public static final int DONE = 128;
        public static final int TODO = 256;
    }

    private static class StateDuration {
        public static final int POMODORO_MS = BuildConfig.DEBUG ? 25 * 1000 : 25 * 60 * 1000;
        public static final int SHORT_BREAK_MS = BuildConfig.DEBUG ? 5 * 1000 : 5 * 60 * 1000;
        public static final int LONG_BREAK_MS = BuildConfig.DEBUG ? 15 * 1000 : 15 * 60 * 1000;
    }

    private Bus mBus = BusProvider.getInstance();
    private IPomodoroStorage pomodoroStorage;

    public Pomodoro(IPomodoroStorage pomodoroStorage) {
        this.pomodoroStorage = pomodoroStorage;
    }

    /**
     * Starts a pomodoro session with a given finish time.fa
     *
     * @param nextState State to start.
     * @param nextPomodoro     next pomodoro time.
     */
    public DateTime start(int nextState, DateTime nextPomodoro) {
        if (nextPomodoro == null) {
            nextPomodoro =  DateTime.now().plus(Pomodoro.getStateDurationInMillis(nextState));
        }
        pomodoroStorage.setLastPomodoro(DateTime.now());
        pomodoroStorage.setNextPomodoro(nextPomodoro);
        pomodoroStorage.setState(nextState);
        return  nextPomodoro;
    }

    public void pause(){
        DateTime nextPomodoro = pomodoroStorage.getNextPomodoro();
        long remainingTime = nextPomodoro.getMillis() - DateTime.now().getMillis();
        long spentTime = DateTime.now().minus(pomodoroStorage.getLastPomodoro().getMillis()).getMillis();
        pomodoroStorage.setRemainingTime(remainingTime);
        pomodoroStorage.addSpentTime(spentTime);
        pomodoroStorage.setState(States.PAUSED);
    }

    public DateTime restart(){
        long remainingTime = getRemainingTime();
        DateTime nextPomodoro = DateTime.now().plus(remainingTime);
        pomodoroStorage.setLastPomodoro(DateTime.now());
        pomodoroStorage.setRemainingTime(0);
        return start(States.POMODORO, nextPomodoro);
    }

    /**
     * Stop a pomodoro and set the type to NONE.
     *
     * @return Returns the Pomodoro that is just stopped.
     */

    public void stop() {
        boolean pomodoroCompleted = false;
        pomodoroStorage.setNextPomodoro(null);
        pomodoroStorage.setRemainingTime(0);
        long stopTime = DateTime.now().getMillis();
        if (stopTime > pomodoroStorage.getNextPomodoro().getMillis()){
            stopTime = pomodoroStorage.getNextPomodoro().getMillis();
            pomodoroCompleted = true;
        }
        long spentTime = stopTime - pomodoroStorage.getLastPomodoro().getMillis();
        if (pomodoroStorage.getState() == States.POMODORO) {
            pomodoroStorage.addSpentTime(spentTime);
            if (!pomodoroCompleted){
                pomodoroStorage.setState(States.NONE, States.SHORT_BREAK);
                return;
            } else {
                pomodoroStorage.increaseSpentPomodoros();
            }
        }

        pomodoroStorage.setState(States.NONE);
    }

    public int getNextActions() {
        int currentState = getState();
        int previousState = getPreviousState();

        if (previousState == States.NONE && currentState == States.NONE){
            return Actions.START_POMDORO;
        } else if ((previousState == States.SHORT_BREAK || previousState == States.LONG_BREAK) &&
                currentState == States.NONE) {
            return Actions.START_POMDORO | Actions.DONE | Actions.TODO;
        } else if (currentState == States.PAUSED) {
            return Actions.RESTART_POMDORO;
        } else if (previousState == States.POMODORO && currentState == States.NONE) {
            return Actions.START_LONG_BREAK | Actions.START_SHORT_BREAK | Actions.DONE | Actions.TODO;
        } else if (currentState == States.POMODORO) {
            return Actions.PAUSE_POMDORO | Actions.STOP_POMDORO;
        } else if (currentState == States.SHORT_BREAK || currentState == States.LONG_BREAK) {
            return Actions.STOP_BREAK;
        }
        return 0;
    }

    public static long getStateDurationInMillis(int state) {
        long length = 0;
        switch (state){
            case States.POMODORO: length = StateDuration.POMODORO_MS;
                break;
            case States.SHORT_BREAK: length = StateDuration.SHORT_BREAK_MS;
                break;
            case States.LONG_BREAK: length = StateDuration.LONG_BREAK_MS;
                break;
        }
        return length;
    }

    public String getID(){
        return pomodoroStorage.getCardID();
    }

    public DateTime getNextPomodoro() {
        return pomodoroStorage.getNextPomodoro();
    }

    public long getCurrentDuration() {
        return  getStateDurationInMillis(this.getState());
    }

    public int getState() {
        return pomodoroStorage.getState();
    }

    public int getPreviousState(){
        return pomodoroStorage.getPreviousState();
    }

    public long getRemainingTime() {
        return pomodoroStorage.getRemainingTime();
    }

    /**
     * The state of the current pomodoro.
     *
     * @return true if the count down is active.
     */
    public boolean isOngoing() {
        int state = pomodoroStorage.getState();
        if (state == States.POMODORO || state == States.SHORT_BREAK || state == States.LONG_BREAK){
            return true;
        }
        return false;
    }
}