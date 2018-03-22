package com.example.kaixin.mycalendar;

import cn.bmob.v3.BmobUser;

/**
 * Created by kaixin on 2018/3/20.
 */

public class MyUser extends BmobUser {
    private String sex;
    private String urlPic;
    private String notes;

    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getUrlPic() {
        return this.urlPic;
    }
    public void setUrlPic(String urlPic) {
        this.urlPic = urlPic;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getNotes() {
        return this.notes;
    }
}
