package com.example.kaixin.mycalendar;

import java.io.Serializable;

/**
 * Created by kaixin on 2018/3/15.
 */

public class Schedule implements Serializable{
    private String id;
    private String title;
    private String address;
    private String start;
    private String end;
    private String call;
    private String notes;

    public Schedule(String id, String title, String address, String start, String end, String call, String notes) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.start = start;
        this.end = end;
        this.call = call;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getCall() {
        return call;
    }
    public String getNotes() {
        return notes;
    }
}
