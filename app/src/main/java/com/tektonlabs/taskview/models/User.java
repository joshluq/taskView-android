package com.tektonlabs.taskview.models;

/**
 * Created by JoshAndre on 25/10/2016.
 */
public class User {

    private String id;
    private String username;


    public User() {
    }

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
