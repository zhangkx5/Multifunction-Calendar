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

public class DiaryActivity extends AppCompatActivity {

    private MyDatabaseHelper myDatabaseHelper;
    private ListView listView;
    private DiaryAdapter diaryAdapter;
    private List<Diary> list;

    @Override
    protected void onResume() {
        super.onResume();
        list = readDB();
        diaryAdapter = new DiaryAdapter(DiaryActivity.this, list);
        listView.setAdapter(diaryAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        getSupportActionBar().setTitle("我的日记");
        myDatabaseHelper = new MyDatabaseHelper(this);

        listView = (ListView) findViewById(R.id.listView);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryActivity.this, DiaryEditActivity.class);
                startActivity(intent);
            }
        });

        list = readDB();
        diaryAdapter = new DiaryAdapter(DiaryActivity.this, list);
        listView.setAdapter(diaryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Diary diary = diaryAdapter.getItem(i);
                Intent intent = new Intent(DiaryActivity.this, DiaryEditActivity.class);
                intent.putExtra("diary", diary);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DiaryActivity.this);
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
                        Diary diary = diaryAdapter.getItem(pos);
                        deleteInDB(diary.getId());
                        list.remove(pos);
                        diaryAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    public List<Diary> readDB() {
        List<Diary> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery(MyDatabaseHelper.DIARY_TABLE_SELECT, null);
        while (cursor.moveToNext()) {
            String dyid = cursor.getString(cursor.getColumnIndex("dyid"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String weather = cursor.getString(cursor.getColumnIndex("weather"));
            String content = cursor.getString(cursor.getColumnIndex("content"));
            Diary diary = new Diary(dyid, date, address, weather, content);
            result.add(diary);
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(result);
        return result;
    }

    public void deleteInDB(String id) {
        SQLiteDatabase dbDelete = myDatabaseHelper.getWritableDatabase();
        dbDelete.execSQL(MyDatabaseHelper.DIARY_TABLE_DELETE, new Object[]{id});
        dbDelete.close();
    }
}