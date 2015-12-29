package com.asalman.trellodoro.models;

/**
 * Created by asalman on 12/30/15.
 */

public class Board {

    private String name;
    private String id;

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

    public Board withName(String name) {
        this.name = name;
        return this;
    }

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

    public Board withId(String id) {
        this.id = id;
        return this;
    }
}