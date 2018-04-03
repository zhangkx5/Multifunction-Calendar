package com.example.kaixin.mycalendar.Bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by kaixin on 2018/3/20.
 */

public class Diary extends BmobObject implements Serializable {

    private String userId;
    private String diary_date;
    private String diary_address;
    private String diary_weather;
    private String diary_title;
    private String diary_content;

    public Diary() {}
    public String getUserId() {
        return userId;
    }
    public String getDiaryDate() {
        return diary_date;
    }
    public String getDiaryAddress() {
        return diary_address;
    }
    public String getDiaryContent() {
        return diary_content;
    }
    public String getDiaryWeather() {
        return diary_weather;
    }
    public String getDiaryTitle() {
        return diary_title;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setDiaryDate(String date) {
        this.diary_date = date;
    }
    public void setDiaryAddress(String address) {
        this.diary_address = address;
    }
    public void setDiaryWeather(String weather) {
        this.diary_weather = weather;
    }
    public void setDiaryContent(String content) {
        this.diary_content = content;
    }
    public void setDiaryTitle(String title) {
        this.diary_title = title;
    }
}
