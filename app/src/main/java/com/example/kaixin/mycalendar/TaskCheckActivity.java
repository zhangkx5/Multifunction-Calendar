package com.example.kaixin.mycalendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by kaixin on 2018/3/16.
 */

public class TaskCheckActivity extends AppCompatActivity{

    private MyDatabaseHelper myDatabaseHelper;
    private ImageButton ib_back, ib_save;
    private Button check;
    private Task task;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_check);
        myDatabaseHelper = new MyDatabaseHelper(this);

        TextView title = (TextView)findViewById(R.id.title);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_save = (ImageButton)findViewById(R.id.ib_save);
        check = (Button)findViewById(R.id.check);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskCheckActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            task = (Task)intent.getSerializableExtra("task");
            title.setText(task.getName());
        } else {
            TaskCheckActivity.this.finish();
        }
        listView = (ListView)findViewById(R.id.listView);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(TaskCheckActivity.this, readDB(), R.layout.list_anniversary,
                new String[]{"task", "date"}, new int[]{R.id.name, R.id.days});
        listView.setAdapter(simpleAdapter);

        hasChecked();
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check.getText().toString() != "今日已打卡") {
                    addInDB(task.getName());
                    check.setText("今日已打卡");
                    Toast.makeText(TaskCheckActivity.this, "打卡成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void hasChecked() {
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String table = MyDatabaseHelper.CHECK_TABLE_NAME;
        String selection = "task = ? and date = ?";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date(System.currentTimeMillis());
        String date = simpleDateFormat.format(today);
        String[] selectionArgs = new String[]{task.getName(), date};
        Cursor cursor = dbRead.query(table, null, selection, selectionArgs, null, null, null);
        if (cursor.getCount() != 0) {
            check.setText("今日已打卡");
        }
    }

    public void addInDB(String task) {
        String ch_id = String.valueOf(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date(System.currentTimeMillis());
        String date = simpleDateFormat.format(today);
        String notes = "";
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(MyDatabaseHelper.CHECK_TABLE_INSERT, new Object[]{ch_id, date, task, notes});
        dbWrite.close();
        Toast.makeText(TaskCheckActivity.this, ch_id + task, Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Map<String, Object>> readDB() {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String table = MyDatabaseHelper.CHECK_TABLE_NAME;
        String selection = "task = ?";
        String[] selectionArgs = new String[]{task.getName()};
        Cursor cursor = dbRead.query(table, null, selection, selectionArgs, null, null, null);
        Toast.makeText(TaskCheckActivity.this, "搜索", Toast.LENGTH_SHORT).show();
        while (cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String name = cursor.getString(cursor.getColumnIndex("task"));
            Map<String, Object> hashmap = new HashMap<String, Object>();
            hashmap.put("task", name);
            hashmap.put("date", date);
            list.add(hashmap);
        }
        cursor.close();
        dbRead.close();
        return list;
    }
}
