package com.example.kaixin.mycalendar;

import java.io.Serializable;

/**
 * Created by kaixin on 2018/3/13.
 */

public class Diary implements Serializable {
    private String id;
    private String date;
    private String address;
    private String weather;
    private String content;

    public Diary(String id, String date, String address, String weather, String content) {
        this.id = id;
        this.address = address;
        this.weather = weather;
        this.date = date;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
    public String getAddress() {
        return address;
    }
    public String getWeather() {
        return weather;
    }
    public String getContent() {
        return content;
    }
}
