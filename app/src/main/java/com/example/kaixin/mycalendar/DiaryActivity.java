package com.example.kaixin.mycalendar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.BmobDiary;
import com.example.kaixin.mycalendar.Utils.DiaryUtils;
import com.example.kaixin.mycalendar.Utils.UserUtils;

import java.util.List;

/**
 * Created by kaixin on 2018/2/4.
 */

public class DiaryActivity extends AppCompatActivity {

    private ListView listView;
    private DiaryAdapter diaryAdapter;
    private List<BmobDiary> diary_list;
    ProgressDialog progressDialog;
    public void getList() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(DiaryActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                diaryAdapter = new DiaryAdapter(DiaryActivity.this, diary_list);
                listView.setAdapter(diaryAdapter);
            }
            @Override
            protected Void doInBackground(String... params) {
                diary_list = DiaryUtils.queryAllLocalDiary(DiaryActivity.this, UserUtils.getUserId(DiaryActivity.this));
                if (diary_list == null) {
                    DiaryUtils.queryAllBmobDiary(DiaryActivity.this);
                    diary_list = DiaryUtils.queryAllLocalDiary(DiaryActivity.this, UserUtils.getUserId(DiaryActivity.this));
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        diary_list = DiaryUtils.queryAllLocalDiary(this, UserUtils.getUserId(this));
        if (diary_list.size() == 0) {
            DiaryUtils.queryAllBmobDiary(this);
            diary_list = DiaryUtils.queryAllLocalDiary(this, UserUtils.getUserId(this));
        }
        diaryAdapter = new DiaryAdapter(DiaryActivity.this, diary_list);
        listView.setAdapter(diaryAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        getSupportActionBar().setTitle("我的日记");

        listView = (ListView) findViewById(R.id.listView);
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryActivity.this, DiaryEditActivity.class);
                startActivity(intent);
            }
        });

        //getList();
        diary_list = DiaryUtils.queryAllLocalDiary(this, UserUtils.getUserId(this));
        if (diary_list.size() == 0) {
            DiaryUtils.queryAllBmobDiary(this);
            diary_list = DiaryUtils.queryAllLocalDiary(this, UserUtils.getUserId(this));
        }
        diaryAdapter = new DiaryAdapter(DiaryActivity.this, diary_list);
        listView.setAdapter(diaryAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BmobDiary diary = diaryAdapter.getItem(i);
                Log.i("DIARY", "选中的DIARY信息为:"+diary.getObjectId()+","+diary.getDiaryContent());
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
                        BmobDiary diary = diaryAdapter.getItem(pos);
                        DiaryUtils.deleteLocalDiary(DiaryActivity.this, diary.getObjectId());
                        diary_list.remove(pos);
                        diaryAdapter.notifyDataSetChanged();
                        DiaryUtils.deleteBmobDiary(diary.getObjectId());
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
    }
}