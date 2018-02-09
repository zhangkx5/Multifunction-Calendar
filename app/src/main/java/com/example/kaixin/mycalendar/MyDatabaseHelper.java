package com.example.kaixin.mycalendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kaixin on 2018/2/5.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myCalendar_database";
    public static final String ANNIVERSARY_TABLE_NAME = "anniversary_table";
    public static final String ANNIVERSARY_TABLE_CREATE = "create table " + ANNIVERSARY_TABLE_NAME
            + " (id interger primary key,"
            + " adid TEXT,"
            + " name TEXT, "
            + " date TEXT, "
            + " notes TEXT)";
    public static final String ANNIVERSARY_TABLE_DROP = "drop table if exists " + ANNIVERSARY_TABLE_NAME;
    public static final String ANNIVERSARY_TABLE_INSERT = "insert into " + ANNIVERSARY_TABLE_NAME
            + " (adid, name, date, notes) values (? ,?, ?, ?)";
    public static final String ANNIVERSARY_TABLE_SELECT = "select * from " + ANNIVERSARY_TABLE_NAME;
    public static final String ANNIVERSARY_TABLE_DELETE = "delete from " + ANNIVERSARY_TABLE_NAME + " where adid = ?";

    public static final String ACCOUNT_TABLE_NAME = "account_table";
    public static final String ACCOUNT_TABLE_CREAT = "create table " + ACCOUNT_TABLE_NAME
            + "( id interger primary key,"
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
    public static final String ACCOUNT_TABLE_DELECT = "delete from " + ACCOUNT_TABLE_NAME + " where acid = ?";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 4);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ANNIVERSARY_TABLE_CREATE);
        sqLiteDatabase.execSQL(ACCOUNT_TABLE_CREAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersion) {
        sqLiteDatabase.execSQL(ANNIVERSARY_TABLE_DROP);
        sqLiteDatabase.execSQL(ACCOUNT_TABLE_DROP);
        onCreate(sqLiteDatabase);
    }
}
