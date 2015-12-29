package com.asalman.trellodoro.models;

/**
 * Created by asalman on 12/30/15.
 */

public class List {

    private String id;
    private String name;
    private Integer pos;

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

    public List withId(String id) {
        this.id = id;
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

    public List withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return The pos
     */
    public Integer getPos() {
        return pos;
    }

    /**
     * @param pos The pos
     */
    public void setPos(Integer pos) {
        this.pos = pos;
    }

    public List withPos(Integer pos) {
        this.pos = pos;
        return this;
    }

}