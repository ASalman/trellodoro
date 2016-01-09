package com.asalman.trellodoro.models;

/**
 * Created by asalman on 12/30/15.
 */

public class Column {

    private String mId;
    private String mName;
    private Integer mPos;

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

    public Column withId(String id) {
        this.mId = id;
        return this;
    }

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

    public Column withName(String name) {
        this.mName = name;
        return this;
    }

    /**
     * @return The mPos
     */
    public Integer getPos() {
        return mPos;
    }

    /**
     * @param pos The mPos
     */
    public void setPos(Integer pos) {
        this.mPos = pos;
    }

    public Column withPos(Integer pos) {
        this.mPos = pos;
        return this;
    }

}