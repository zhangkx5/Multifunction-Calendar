package com.example.kaixin.mycalendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kaixin on 2018/2/4.
 */

public class TaskActivity extends AppCompatActivity {

    private ListView listView;
    private TaskAdapter taskAdapter;
    private List<Task> list;
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onResume() {
        super.onResume();
        list = readDB();
        taskAdapter = new TaskAdapter(TaskActivity.this, list);
        listView.setAdapter(taskAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        getSupportActionBar().setTitle("任务打卡");
        myDatabaseHelper = new MyDatabaseHelper(this);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaskActivity.this, TaskEditActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView)findViewById(R.id.listView);
        list = readDB();
        taskAdapter = new TaskAdapter(TaskActivity.this, list);
        listView.setAdapter(taskAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TaskActivity.this, TaskCheckActivity.class);
                Task task = taskAdapter.getItem(i);
                intent.putExtra("task", task);
                startActivity(intent);
            }
        });
    }

    public List<Task> readDB() {
        List<Task> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery(MyDatabaseHelper.TASK_TABLE_SELECT, null);
        while (cursor.moveToNext()) {
            String tsid = cursor.getString(cursor.getColumnIndex("tsid"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String notes = cursor.getString(cursor.getColumnIndex("notes"));
            Task task = new Task(tsid, date, name, notes);
            result.add(task);
        }
        Collections.reverse(result);
        return result;
    }
}