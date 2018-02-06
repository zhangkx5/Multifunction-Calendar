package com.example.kaixin.mycalendar;

import java.io.Serializable;

/**
 * Created by kaixin on 2018/2/4.
 */

public class AnniversaryDay implements Serializable {
    private String id;
    private String name;
    private String date;
    private String notes;

    public AnniversaryDay(String id, String name, String date, String notes) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getNotes() {
        return notes;
    }
}
