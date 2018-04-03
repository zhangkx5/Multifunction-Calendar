package com.example.kaixin.mycalendar.Bean;

import cn.bmob.v3.BmobObject;

import java.io.Serializable;

/**
 * Created by kaixin on 2018/3/28.
 */

public class ClockingIn extends BmobObject implements Serializable {
    private String userId;
    private String habitId;
    private String date;

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setHabitId(String taskId) {
        this.habitId = taskId;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getUserId() {
        return userId;
    }
    public String getHabitId() {
        return habitId;
    }
    public String getDate() {
        return date;
    }
}
