package com.example.kaixin.mycalendar.Bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by kaixin on 2018/3/16.
 */

public class Habit extends BmobObject implements Serializable {
    private String userId;
    private String habit_name;
    private String habit_notes;
    private String habit_img;
    private String img_name;

    public Habit() {}
    public String getUserId() {
        return userId;
    }
    public String getHabitName() {
        return habit_name;
    }
    public String getHabitNotes() {
        return habit_notes;
    }
    public String getHabitImg() {
        return habit_img;
    }
    public String getHabitImgName() {
        return img_name;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setHabitName(String name) {
        this.habit_name = name;
    }
    public void setHabitNotes(String notes) {
        this.habit_notes = notes;
    }
    public void setHabitImg(String img) {
        this.habit_img = img;
    }
    public void setHabitImgName(String img_name) {
        this.img_name = img_name;
    }
}
