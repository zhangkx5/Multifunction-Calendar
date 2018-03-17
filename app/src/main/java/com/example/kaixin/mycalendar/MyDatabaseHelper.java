package com.example.kaixin.mycalendar;

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
            + " (id interger primary key,"
            + " scid TEXT,"
            + " title TEXT,"
            + " address TEXT,"
            + " start TEXT,"
            + " end TEXT,"
            + " call TEXT,"
            + " notes TEXT)";
    public static final String SCHEDULE_TABLE_DROP = "drop table if exists " + SCHEDULE_TABLE_NAME;
    public static final String SCHEDULE_TABLE_INSERT = "insert into " + SCHEDULE_TABLE_NAME
            + " (scid, title, address, start, end, call, notes) values (?, ?, ?, ?, ?, ?, ?)";
    public static final String SCHEDULE_TABLE_SELECT = "select * from " + SCHEDULE_TABLE_NAME;
    public static final String SCHEDULE_TABLE_DELETE = "delete from " + SCHEDULE_TABLE_NAME + " where scid = ?";

    //周年纪念
    public static final String ANNIVERSARY_TABLE_NAME = "anniversary_table";
    public static final String ANNIVERSARY_TABLE_CREATE = "create table " + ANNIVERSARY_TABLE_NAME
            + " (id interger primary key,"
            + " adid TEXT,"
            + " name TEXT,"
            + " date TEXT,"
            + " notes TEXT)";
    public static final String ANNIVERSARY_TABLE_DROP = "drop table if exists " + ANNIVERSARY_TABLE_NAME;
    public static final String ANNIVERSARY_TABLE_INSERT = "insert into " + ANNIVERSARY_TABLE_NAME
            + " (adid, name, date, notes) values (? ,?, ?, ?)";
    public static final String ANNIVERSARY_TABLE_SELECT = "select * from " + ANNIVERSARY_TABLE_NAME;
    public static final String ANNIVERSARY_TABLE_DELETE = "delete from " + ANNIVERSARY_TABLE_NAME + " where adid = ?";

    //日记
    public static final String DIARY_TABLE_NAME = "diary_table";
    public static final String DIARY_TABLE_CREATE = "create table " + DIARY_TABLE_NAME
            + " (id interger primary key,"
            + " dyid TEXT,"
            + " date TEXT,"
            + " address TEXT,"
            + " weather TEXT,"
            + " content TEXT)";
    public static final String DIARY_TABLE_DROP = "drop table if exists " + DIARY_TABLE_NAME;
    public static final String DIARY_TABLE_INSERT = "insert into " + DIARY_TABLE_NAME
            + "(dyid, date, address, weather, content) values (?, ?, ?, ?, ?)";
    public static final String DIARY_TABLE_SELECT = "select * from " + DIARY_TABLE_NAME;
    public static final String DIARY_TABLE_DELETE = "delete from " + DIARY_TABLE_NAME + " where dyid = ?";
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
            + " (id interger primary key,"
            + " tsid TEXT,"
            + " date TEXT,"
            + " name TEXT,"
            + " notes TEXT)";
    public static final String TASK_TABLE_DROP = "drop table if exists " + TASK_TABLE_NAME;
    public static final String TASK_TABLE_INSERT = "insert into " + TASK_TABLE_NAME
            + "(tsid, date, name, notes) values (?, ?, ?, ?)";
    public static final String TASK_TABLE_SELECT = "select * from " + TASK_TABLE_NAME;
    public static final String TASK_TABLE_DELETE = "delete from " + TASK_TABLE_NAME + " where tsid = ?";

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
