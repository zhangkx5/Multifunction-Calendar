package com.example.kaixin.mycalendar;

import java.io.Serializable;

/**
 * Created by kaixin on 2018/3/16.
 */

public class Task implements Serializable {
    private String id;
    private String date;
    private String name;
    private String notes;

    public Task(String id, String date, String name, String notes) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }
    public String getDate() {
        return date;
    }
    public String getName() {
        return name;
    }
    public String getNotes() {
        return notes;
    }
}
