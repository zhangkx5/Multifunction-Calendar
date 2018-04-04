package com.example.kaixin.mycalendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.kaixin.mycalendar.Utils.AccountBillUtils;
import com.example.kaixin.mycalendar.Utils.AnniversaryUtils;
import com.example.kaixin.mycalendar.Utils.DiaryUtils;
import com.example.kaixin.mycalendar.Utils.HabitUtils;
import com.example.kaixin.mycalendar.Utils.ScheduleUtils;

import cn.bmob.v3.Bmob;

/**
 * Created by kaixin on 2018/4/4.
 */

public class LoadingActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);

        Bmob.initialize(this, "9b0b1ad0d1c9c081739ab99a3e05fe98");
        /*Boolean isFirst = getBoolean();
        if (isFirst) {
            saveBoolean(false);
            Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            turnToMain();
            *//*MyUser myUser = UserUtils.getCurrentUser();
            if (myUser != null) {
                turnToMain();
            } else {
                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(intent);
            }*//*
        }*/

        //loadDatas();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Boolean isFirst = getBoolean();
                if (isFirst) {
                    saveBoolean(false);
                    Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }, 1000);
    }
    public void loadDatas() {
        HabitUtils.queryAllBmobHabit(this);
        HabitUtils.queryAllAllBmobClockingIn(this);
        AccountBillUtils.queryAllBmobAccountBill(this);
        AnniversaryUtils.queryAllBmobAnniversaryDay(this);
        DiaryUtils.queryAllBmobDiary(this);
        ScheduleUtils.queryAllBmobSchedule(this);
    }
    public void turnToMain() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                Log.i("Loading", "跳转了");
                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(intent);
            }
            @Override
            protected Void doInBackground(String... params) {
                loadDatas();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
    public void saveBoolean(boolean value) {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("calendar", Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putBoolean("isFirst", value).commit();
    }
    public boolean getBoolean() {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("calendar", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean("isFirst", true);
    }
}
