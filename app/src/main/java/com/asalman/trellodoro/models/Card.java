package com.asalman.trellodoro.models;

/**
 * Created by asalman on 12/30/15.
 */

    public class Card {

    private String mId;
    private String mIdList;
    private String mName;
    private Double mPos;

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

    public Card withId(String id) {
        this.mId = id;
        return this;
    }

    /**
     * @return The mIdList
     */
    public String getIdList() {
        return mIdList;
    }

    /**
     * @param idList The mIdList
     */
    public void setIdList(String idList) {
        this.mIdList = idList;
    }

    public Card withIdList(String idList) {
        this.mIdList = idList;
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

    public Card withName(String name) {
        this.mName = name;
        return this;
    }

    /**
     * @return The mPos
     */
    public Double getmPos() {
        return mPos;
    }

    /**
     * @param mPos The mPos
     */
    public void setmPos(Double mPos) {
        this.mPos = mPos;
    }

    public Card withPos(Double pos) {
        this.mPos = pos;
        return this;
    }

    @Override
    public String toString() {
        return getName();
    }
}