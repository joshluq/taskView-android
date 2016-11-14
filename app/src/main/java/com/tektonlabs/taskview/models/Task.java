package com.tektonlabs.taskview.models;

import java.util.ArrayList;

/**
 * Created by JoshAndre on 31/10/2016.
 */

public class Task {

    private String id;
    private String taskName;
    private ArrayList<User> users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
