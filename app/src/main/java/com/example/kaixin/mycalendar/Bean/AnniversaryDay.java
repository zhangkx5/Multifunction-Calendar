package com.example.kaixin.mycalendar.Bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by kaixin on 2018/2/4.
 */

public class AnniversaryDay extends BmobObject implements Serializable {
    private String userId;
    private String anniversary_name;
    private String anniversary_date;
    private String anniversary_notes;

    public String getUserId() {
        return userId;
    }
    public String getAnniversaryName() {
        return anniversary_name;
    }
    public String getAnniversaryDate() {
        return anniversary_date;
    }
    public String getAnniversaryNotes() {
        return anniversary_notes;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setAnniversaryName(String name) {
        this.anniversary_name = name;
    }
    public void setAnniversaryDate(String date) {
        this.anniversary_date = date;
    }
    public void setAnniversaryNotes(String notes) {
        this.anniversary_notes = notes;
    }
}
