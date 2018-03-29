package com.example.kaixin.mycalendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.Task;
import com.example.kaixin.mycalendar.Utils.AnniversaryUtils;
import com.example.kaixin.mycalendar.Utils.MyDatabaseHelper;
import com.example.kaixin.mycalendar.Utils.TaskUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
            title.setText(task.getTaskName());
        } else {
            TaskCheckActivity.this.finish();
        }
        listView = (ListView)findViewById(R.id.listView);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(TaskCheckActivity.this,
                TaskUtils.queryAllLocalClockingIn(TaskCheckActivity.this, task.getUserId(), task.getObjectId()),
                R.layout.list_anniversary,
                new String[]{"task", "date"}, new int[]{R.id.name, R.id.days});
        listView.setAdapter(simpleAdapter);

        //hasClockingIn();
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createClockingIn();
                Log.i("CLOCKINGIN", "打卡成功");
                Toast.makeText(TaskCheckActivity.this, "打卡成功", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void hasClockingIn() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date(System.currentTimeMillis());
        String date = simpleDateFormat.format(today);
        boolean flag = false;
        flag = TaskUtils.queryOneLocalClockingIn(TaskCheckActivity.this, task.getUserId(), task.getObjectId(), date);
        if (flag == false) {
            TaskUtils.queryOneBmobClockingIn(TaskCheckActivity.this, task.getObjectId(), date);
            flag = TaskUtils.queryOneLocalClockingIn(TaskCheckActivity.this, task.getUserId(), task.getObjectId(), date);
            if (flag == false) {

            }
        }
    }
    ProgressDialog progressDialog;
    public void createClockingIn() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(TaskCheckActivity.this);
                progressDialog.setMessage("保存中...");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TaskCheckActivity.this.finish();
            }
            @Override
            protected Void doInBackground(String... params) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date today = new Date(System.currentTimeMillis());
                String date = simpleDateFormat.format(today);
                TaskUtils.createBmobClockingIn(TaskCheckActivity.this, task.getObjectId(), date);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
