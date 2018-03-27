package com.example.kaixin.mycalendar.Bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by kaixin on 2018/3/13.
 */

public class Diary extends BmobObject implements Serializable {
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
    public void setDate(String date) {
        this.date = date;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getWeather() {
        return weather;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
