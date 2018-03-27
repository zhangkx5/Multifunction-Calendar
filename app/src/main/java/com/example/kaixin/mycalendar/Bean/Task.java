package com.example.kaixin.mycalendar.Bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by kaixin on 2018/3/16.
 */

public class Task extends BmobObject implements Serializable {
    private String taskId;
    private String userId;
    private String task_name;
    private String task_notes;
    private String task_img;

    public Task() {}
    public Task(String id, String userId, String name, String notes, String img) {
        this.taskId = id;
        this.userId = userId;
        this.task_name = name;
        this.task_notes = notes;
        this.task_img = img;
    }
    public String getTaskId() {
        return taskId;
    }
    public String getUserId() {
        return userId;
    }
    public String getTaskName() {
        return task_name;
    }
    public String getTaskNotes() {
        return task_notes;
    }
    public String getTaskImg() {
        return task_img;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setTaskName(String name) {
        this.task_name = name;
    }
    public void setTaskNotes(String notes) {
        this.task_notes = notes;
    }
    public void setTaskImg(String img) {
        this.task_img = img;
    }
}
