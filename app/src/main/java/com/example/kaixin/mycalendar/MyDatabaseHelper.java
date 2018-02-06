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
    public static final String ANNIVERSARY_TABLE_INSERT = "insert into " + ANNIVERSARY_TABLE_NAME + " (adid, name, date, notes) values (? ,?, ?, ?)";
    public static final String ANNIVERSARY_TABLE_SELECT = "select * from " + ANNIVERSARY_TABLE_NAME;
    public static final String ANNIVERSARY_TABLE_DELETE = "delete from " + ANNIVERSARY_TABLE_NAME + " where adid = ?";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ANNIVERSARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int newVersion, int oldVersion) {
        sqLiteDatabase.execSQL(ANNIVERSARY_TABLE_DROP);
        onCreate(sqLiteDatabase);
    }
}
