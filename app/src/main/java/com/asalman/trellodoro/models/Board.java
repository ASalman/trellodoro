package com.asalman.trellodoro.models;

/**
 * Created by asalman on 12/30/15.
 */

public class Board {

    private String mName;
    private String mId;

    /**
     * @return The mName
     */
    public String getName() {
        return mName;
    }

    /**
     * @param name The mName
     */
    public void setName(String name) {
        this.mName = name;
    }

    public Board withName(String name) {
        this.mName = name;
        return this;
    }

    /**
     * @return The mId
     */
    public String getId() {
        return mId;
    }

    /**
     * @param id The mId
     */
    public void setId(String id) {
        this.mId = id;
    }

    public Board withId(String id) {
        this.mId = id;
        return this;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}