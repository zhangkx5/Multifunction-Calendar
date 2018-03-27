package com.example.kaixin.mycalendar.Bean;

import java.io.Serializable;

/**
 * Created by kaixin on 2018/2/8.
 */

public class AccountBill implements Serializable {
    private String id;
    private int type;
    private int label;
    private String date;
    private double money;
    private String notes;

    public AccountBill(String id, int type, int label, String date, double money, String notes) {
        this.id = id;
        this.type = type;
        this.label = label;
        this.date = date;
        this.money = money;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getLabel() {
        return label;
    }
    public String getDate() {
        return date;
    }

    public double getMoney() {
        return money;
    }

    public String getNotes() {
        return notes;
    }
}
