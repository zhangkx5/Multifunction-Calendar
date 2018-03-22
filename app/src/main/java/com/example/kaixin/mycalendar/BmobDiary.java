package com.example.kaixin.mycalendar;

import cn.bmob.v3.BmobObject;

/**
 * Created by kaixin on 2018/3/20.
 */

public class BmobDiary extends BmobObject {

    private String userId;
    private String date;
    private String address;
    private String weather;
    private String content;


    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
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
