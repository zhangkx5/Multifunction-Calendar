package com.example.kaixin.mycalendar.Bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by kaixin on 2018/2/8.
 */

public class AccountBill extends BmobObject implements Serializable {
    private String userId;
    private int type;
    private int label;
    private String date;
    private double money;
    private String notes;


    public String getUserId() {
        return userId;
    }
    public int getAccountType() {
        return type;
    }
    public int getAccountLabel() {
        return label;
    }
    public String getAccountDate() {
        return date;
    }
    public double getAccountMoney() {
        return money;
    }
    public String getAccountNotes() {
        return notes;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setAccountType(int type) {
        this.type = type;
    }
    public void setAccountLabel(int label) {
        this.label = label;
    }
    public void setAccountDate(String date) {
        this.date = date;
    }
    public void setAccountMoney(double money) {
        this.money = money;
    }
    public void setAccountNotes(String notes) {
        this.notes = notes;
    }
}
