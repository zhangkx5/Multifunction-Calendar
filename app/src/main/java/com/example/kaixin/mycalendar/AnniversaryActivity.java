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

public class AnniversaryActivity extends AppCompatActivity {

    private MyDatabaseHelper myDatabaseHelper;
    private AnniversaryAdapter anniversaryAdapter;
    private ListView listView;
    private List<AnniversaryDay> list;

    @Override
    protected void onResume() {
        super.onResume();
        list = readDB();
        anniversaryAdapter = new AnniversaryAdapter(AnniversaryActivity.this, list);
        listView.setAdapter(anniversaryAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary);
        getSupportActionBar().setTitle("纪念日");
        myDatabaseHelper = new MyDatabaseHelper(this);

        listView = (ListView) findViewById(R.id.listView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AnniversaryEditActivity.class);
                startActivity(intent);
            }
        });

        list = readDB();
        anniversaryAdapter = new AnniversaryAdapter(AnniversaryActivity.this, list);
        listView.setAdapter(anniversaryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AnniversaryDay anniversaryDay = anniversaryAdapter.getItem(i);
                Intent intent = new Intent(AnniversaryActivity.this, AnniversaryDetailsActivity.class);
                intent.putExtra("anniversaryDay",anniversaryDay);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AnniversaryActivity.this);
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
                        AnniversaryDay anniversaryDay = anniversaryAdapter.getItem(pos);
                        deleteInDB(anniversaryDay.getId());
                        list.remove(pos);
                        anniversaryAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }

    public List<AnniversaryDay> readDB() {
        List<AnniversaryDay> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery(MyDatabaseHelper.ANNIVERSARY_TABLE_SELECT, null);
        while (cursor.moveToNext()) {
            String adid = cursor.getString(cursor.getColumnIndex("adid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String days = cursor.getString(cursor.getColumnIndex("date"));
            String notes = cursor.getString(cursor.getColumnIndex("notes"));
            AnniversaryDay anniversaryDay = new AnniversaryDay(adid, name, days, notes);
            result.add(anniversaryDay);
        }
        Collections.reverse(result);
        return result;
    }

    public void deleteInDB(String id) {
        SQLiteDatabase dbDelete = myDatabaseHelper.getWritableDatabase();
        dbDelete.execSQL(MyDatabaseHelper.ANNIVERSARY_TABLE_DELETE, new Object[]{id});
        dbDelete.close();
    }
}