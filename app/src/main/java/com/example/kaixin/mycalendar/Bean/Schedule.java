package com.example.kaixin.mycalendar.Bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by kaixin on 2018/3/15.
 */

public class Schedule extends BmobObject implements Serializable{
    private String userId;
    private String schedule_title;
    private String schedule_address;
    private String schedule_start;
    private String schedule_end;
    private String schedule_call;
    private String schedule_notes;

    public String getUserId() {
        return userId;
    }
    public String getScheduleTitle() {
        return schedule_title;
    }
    public String getScheduleAddress() {
        return schedule_address;
    }
    public String getScheduleStart() {
        return schedule_start;
    }
    public String getScheduleEnd() {
        return schedule_end;
    }
    public String getScheduleCall() {
        return schedule_call;
    }
    public String getScheduleNotes() {
        return schedule_notes;
    }
    public void setUserId(String id) {
        this.userId = id;
    }
    public void setScheduleTitle(String title) {
        this.schedule_title = title;
    }
    public void setScheduleAddress(String address) {
        this.schedule_address = address;
    }
    public void setScheduleStart(String start) {
        this.schedule_start = start;
    }
    public void setScheduleEnd(String end) {
        this.schedule_end = end;
    }
    public void setScheduleCall(String call) {
        this.schedule_call = call;
    }
    public void setScheduleNotes(String notes) {
        this.schedule_notes = notes;
    }
}
