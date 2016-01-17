package com.asalman.trellodoro.models;

/**
 * Created by asalman on 12/30/15.
 */

    public class Card {

    private String id;
    private String idList;
    private String name;
    private Double pos;

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
    public Double getPos() {
        return pos;
    }

    /**
     * @param pos The pos
     */
    public void setPos(Double pos) {
        this.pos = pos;
    }

    public Card withPos(Double pos) {
        this.pos = pos;
        return this;
    }

    @Override
    public String toString() {
        return getName();
    }
}