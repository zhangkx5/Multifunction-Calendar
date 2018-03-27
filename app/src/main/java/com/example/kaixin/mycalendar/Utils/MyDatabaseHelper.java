package com.example.kaixin.mycalendar.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kaixin on 2018/2/5.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myCalendar_database";

    //日程备忘
    public static final String SCHEDULE_TABLE_NAME = "schedule_table";
    public static final String SCHEDULE_TABLE_CREATE = "create table " + SCHEDULE_TABLE_NAME
            + " (id TEXT primary key,"
            + " user_id TEXT,"
            + " schedule_title TEXT,"
            + " schedule_address TEXT,"
            + " schedule_start TEXT,"
            + " schedule_end TEXT,"
            + " schedule_call TEXT,"
            + " schedule_notes TEXT)";
    public static final String SCHEDULE_TABLE_DROP = "drop table if exists " + SCHEDULE_TABLE_NAME;

    //周年纪念
    public static final String ANNIVERSARY_TABLE_NAME = "anniversary_table";
    public static final String ANNIVERSARY_TABLE_CREATE = "create table " + ANNIVERSARY_TABLE_NAME
            + " (id TEXT primary key,"
            + " user_id TEXT,"
            + " anniversary_name TEXT,"
            + " anniversary_date TEXT,"
            + " anniversary_notes TEXT)";
    public static final String ANNIVERSARY_TABLE_DROP = "drop table if exists " + ANNIVERSARY_TABLE_NAME;

    //日记
    public static final String DIARY_TABLE_NAME = "diary_table";
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
    public static final String ACCOUNT_TABLE_NAME = "account_table";
    public static final String ACCOUNT_TABLE_CREATE = "create table " + ACCOUNT_TABLE_NAME
            + "(id interger primary key,"
            + " acid TEXT,"
            + " type interger,"
            + " label interger,"
            + " date TEXT,"
            + " money REAL,"
            + " notes TEXT)";
    public static final String ACCOUNT_TABLE_DROP = "drop table if exists " + ACCOUNT_TABLE_NAME;
    public static final String ACCOUNT_TABLE_INSERT = "insert into " + ACCOUNT_TABLE_NAME
            + " (acid, type, label, date, money, notes) values (?, ?, ?, ?, ?, ?)";
    public static final String ACCOUNT_TABLE_SELECT = "select * from " + ACCOUNT_TABLE_NAME;
    public static final String ACCOUNT_TABLE_DELETE = "delete from " + ACCOUNT_TABLE_NAME + " where acid = ?";

    //任务
    public static final String TASK_TABLE_NAME = "task_table";
    public static final String TASK_TABLE_CREATE = "create table " + TASK_TABLE_NAME
            + " (id TEXT,"
            + " user_id TEXT,"
            + " task_name TEXT,"
            + " task_notes TEXT,"
            + " task_img TEXT)";
    public static final String TASK_TABLE_DROP = "drop table if exists " + TASK_TABLE_NAME;
    public static final String TASK_TABLE_INSERT = "insert into " + TASK_TABLE_NAME
            + "(id, user_id, task_name, task_notes, task_img) values (?, ?, ?, ?, ?)";
    public static final String TASK_TABLE_SELECT = "select * from " + TASK_TABLE_NAME;

    //打卡
    public static final String CHECK_TABLE_NAME = "check_table";
    public static final String CHECK_TABLE_CREATE = "create table " + CHECK_TABLE_NAME
            + " (id interger primary key,"
            + " chid TEXT,"
            + " date TEXT,"
            + " task TEXT,"
            + " notes TEXT)";
    public static final String CHECK_TABLE_DROP = "drop table if exists " + CHECK_TABLE_NAME;
    public static final String CHECK_TABLE_INSERT = "insert into " + CHECK_TABLE_NAME
            + "(chid, date, task, notes) values (?, ?, ?, ?)";
    public static final String CHECK_TABLE_SELECT = "select * from " + CHECK_TABLE_NAME + " where task = ?";
    public static final String CHECK_TABLE_DELETE = "delete from " + CHECK_TABLE_NAME + " where chid = ?";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SCHEDULE_TABLE_CREATE);
        sqLiteDatabase.execSQL(ANNIVERSARY_TABLE_CREATE);
        sqLiteDatabase.execSQL(ACCOUNT_TABLE_CREATE);
        sqLiteDatabase.execSQL(DIARY_TABLE_CREATE);
        sqLiteDatabase.execSQL(TASK_TABLE_CREATE);
        sqLiteDatabase.execSQL(CHECK_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersion) {
        sqLiteDatabase.execSQL(SCHEDULE_TABLE_DROP);
        sqLiteDatabase.execSQL(ANNIVERSARY_TABLE_DROP);
        sqLiteDatabase.execSQL(ACCOUNT_TABLE_DROP);
        sqLiteDatabase.execSQL(DIARY_TABLE_DROP);
        sqLiteDatabase.execSQL(TASK_TABLE_DROP);
        sqLiteDatabase.execSQL(CHECK_TABLE_DROP);
        onCreate(sqLiteDatabase);
    }
}
