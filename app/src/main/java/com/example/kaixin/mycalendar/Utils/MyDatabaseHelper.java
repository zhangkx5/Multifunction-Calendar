package com.example.kaixin.mycalendar.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kaixin on 2018/2/5.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Calendar_Database";

    //日程备忘
    public static final String SCHEDULE_TABLE_NAME = "Table_Schedule";
    public static final String SCHEDULE_TABLE_CREATE = "create table " + SCHEDULE_TABLE_NAME
            + " (id TEXT primary key unique,"
            + " user_id TEXT,"
            + " schedule_title TEXT,"
            + " schedule_address TEXT,"
            + " schedule_startTime TEXT,"
            + " schedule_endTime TEXT,"
            + " schedule_notes TEXT)";
    public static final String SCHEDULE_TABLE_DROP = "drop table if exists " + SCHEDULE_TABLE_NAME;

    //纪念日
    public static final String ANNIVERSARY_TABLE_NAME = "Table_Anniversary";
    public static final String ANNIVERSARY_TABLE_CREATE = "create table " + ANNIVERSARY_TABLE_NAME
            + " (id TEXT primary key,"
            + " user_id TEXT,"
            + " anniversary_name TEXT,"
            + " anniversary_date TEXT,"
            + " anniversary_notes TEXT)";
    public static final String ANNIVERSARY_TABLE_DROP = "drop table if exists " + ANNIVERSARY_TABLE_NAME;

    //日记
    public static final String DIARY_TABLE_NAME = "Table_Diary";
    public static final String DIARY_TABLE_CREATE = "create table " + DIARY_TABLE_NAME
            + " (id TEXT primary key,"
            + " user_id TEXT,"
            + " diary_date TEXT,"
            + " diary_address TEXT,"
            + " diary_weather TEXT,"
            + " diary_title TEXT,"
            + " diary_content TEXT)";
    public static final String DIARY_TABLE_DROP = "drop table if exists " + DIARY_TABLE_NAME;
    //账本
    public static final String ACCOUNT_TABLE_NAME = "Table_AccountBill";
    public static final String ACCOUNT_TABLE_CREATE = "create table " + ACCOUNT_TABLE_NAME
            + "(id TEXT primary key,"
            + " user_id TEXT,"
            + " bill_type interger,"
            + " bill_label interger,"
            + " bill_date TEXT,"
            + " bill_money REAL,"
            + " bill_notes TEXT)";
    public static final String ACCOUNT_TABLE_DROP = "drop table if exists " + ACCOUNT_TABLE_NAME;

    //习惯
    public static final String HABIT_TABLE_NAME = "Table_Habit";
    public static final String HABIT_TABLE_CREATE = "create table " + HABIT_TABLE_NAME
            + " (id TEXT,"
            + " user_id TEXT,"
            + " habit_name TEXT,"
            + " habit_notes TEXT,"
            + " habit_img TEXT,"
            + " img_name TEXT)";
    public static final String HABIT_TABLE_DROP = "drop table if exists " + HABIT_TABLE_NAME;

    //打卡

    public static final String CLOCKINGIN_TABLE_NAME = "Table_ClockingIn";
    public static final String CLOCKINGIN_TABLE_CREATE = "create table " + CLOCKINGIN_TABLE_NAME
            + " (id TEXT primary key,"
            + " user_id TEXT,"
            + " habit_id TEXT,"
            + " date TEXT)";
    public static final String CLOCKINGIN_TABLE_DROP = "drop table if exists " + CLOCKINGIN_TABLE_NAME;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SCHEDULE_TABLE_CREATE);
        sqLiteDatabase.execSQL(ANNIVERSARY_TABLE_CREATE);
        sqLiteDatabase.execSQL(ACCOUNT_TABLE_CREATE);
        sqLiteDatabase.execSQL(DIARY_TABLE_CREATE);
        sqLiteDatabase.execSQL(HABIT_TABLE_CREATE);
        sqLiteDatabase.execSQL(CLOCKINGIN_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersion) {
        sqLiteDatabase.execSQL(SCHEDULE_TABLE_DROP);
        sqLiteDatabase.execSQL(ANNIVERSARY_TABLE_DROP);
        sqLiteDatabase.execSQL(ACCOUNT_TABLE_DROP);
        sqLiteDatabase.execSQL(DIARY_TABLE_DROP);
        sqLiteDatabase.execSQL(HABIT_TABLE_DROP);
        sqLiteDatabase.execSQL(CLOCKINGIN_TABLE_DROP);
        onCreate(sqLiteDatabase);
    }
}
