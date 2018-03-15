package com.example.kaixin.mycalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kaixin on 2018/2/4.
 */

public class ScheduleActivity extends AppCompatActivity {

    private MyDatabaseHelper myDatabaseHelper;
    private ListView listView;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> list;

    @Override
    protected void onResume() {
        super.onResume();
        list = readDB();
        scheduleAdapter = new ScheduleAdapter(ScheduleActivity.this, list);
        listView.setAdapter(scheduleAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        getSupportActionBar().setTitle("日程备忘");
        myDatabaseHelper = new MyDatabaseHelper(this);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScheduleActivity.this, ScheduleEditActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        list = readDB();
        scheduleAdapter = new ScheduleAdapter(ScheduleActivity.this, list);
        listView.setAdapter(scheduleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Schedule schedule = scheduleAdapter.getItem(i);
                Intent intent = new Intent(ScheduleActivity.this, ScheduleEditActivity.class);
                intent.putExtra("schedule", schedule);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
                builder.setTitle("删除提醒");
                builder.setMessage("确定要删除吗？此操作不可逆！");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Schedule schedule = scheduleAdapter.getItem(pos);
                        deleteInDB(schedule.getId());
                        list.remove(pos);
                        scheduleAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    public List<Schedule> readDB() {
        List<Schedule> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery(MyDatabaseHelper.SCHEDULE_TABLE_SELECT, null);
        while (cursor.moveToNext()) {
            String scid = cursor.getString(cursor.getColumnIndex("scid"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String start = cursor.getString(cursor.getColumnIndex("start"));
            String end = cursor.getString(cursor.getColumnIndex("end"));
            String call = cursor.getString(cursor.getColumnIndex("call"));
            String notes = cursor.getString(cursor.getColumnIndex("notes"));
            Schedule schedule = new Schedule(scid, title, address, start, end, call, notes);
            result.add(schedule);
        }
        Collections.reverse(result);
        cursor.close();
        dbRead.close();
        return result;
    }
    public void deleteInDB(String id) {
        SQLiteDatabase dbDelete = myDatabaseHelper.getWritableDatabase();
        dbDelete.execSQL(MyDatabaseHelper.SCHEDULE_TABLE_DELETE, new Object[]{id});
        dbDelete.close();
    }
}