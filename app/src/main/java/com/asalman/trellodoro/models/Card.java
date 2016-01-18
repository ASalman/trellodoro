package com.asalman.trellodoro.models;

/**
 * Created by asalman on 12/30/15.
 */
public class Card {

    private String id;
    private String idList;
    private String name;
    private double pos;
    private int previousState;
    private int currentState;
    private long remainingTime;
    private long lastPomodoroTime;
    private long nextPomodoroTime;
    private int spentPomodoros;
    private long totalSpentTime;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(String id) {
        this.id = id;
    }

    public Card withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @return The idList
     */
    public String getIdList() {
        return idList;
    }

    /**
     * @param idList The idList
     */
    public void setIdList(String idList) {
        this.idList = idList;
    }

    public Card withIdList(String idList) {
        this.idList = idList;
        return this;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Card withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return The pos
     */
    public double getPos() {
        return pos;
    }

    /**
     * @param pos The pos
     */
    public void setPos(double pos) {
        this.pos = pos;
    }

    public Card withPos(int pos) {
        this.pos = pos;
        return this;
    }

    public int getPreviousState() {
        return previousState;
    }

    public void setPreviousState(int previousState) {
        this.previousState = previousState;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(long remainingTime) {
        this.remainingTime = remainingTime;
    }

    public long getNextPomodoroTime() {
        return nextPomodoroTime;
    }

    public void setNextPomodoroTime(long nextPomodoroTime) {
        this.nextPomodoroTime = nextPomodoroTime;
    }

    public int getSpentPomodoros() {
        return spentPomodoros;
    }

    public void setSpentPomodoros(int spentPomodoros) {
        this.spentPomodoros = spentPomodoros;
    }

    public long getTotalSpentTime() {
        return totalSpentTime;
    }

    public void setTotalSpentTime(long totalSpentTime) {
        this.totalSpentTime = totalSpentTime;
    }

    public long getLastPomodoroTime() {
        return lastPomodoroTime;
    }

    public void setLastPomodoroTime(long lastPomodoroTime) {
        this.lastPomodoroTime = lastPomodoroTime;
    }

    @Override
    public String toString() {
        return getName();
    }

}