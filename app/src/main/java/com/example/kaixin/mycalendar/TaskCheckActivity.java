package com.example.kaixin.mycalendar;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.Task;
import com.example.kaixin.mycalendar.Utils.AnniversaryUtils;
import com.example.kaixin.mycalendar.Utils.MyDatabaseHelper;
import com.example.kaixin.mycalendar.Utils.TaskUtils;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaixin on 2018/3/16.
 */

public class TaskCheckActivity extends AppCompatActivity{

    private ImageButton ib_back, ib_edit;
    private ImageView task_img;
    private TextView task_notes;
    private MaterialCalendarView mcv;
    private Button check;
    private Task task;
    private List<String> dateList = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        dateList = queryAllLocalClockingIn(TaskCheckActivity.this, task.getUserId(), task.getObjectId());
        mcv.addDecorators(new EventDecorator(dateList));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_check);

        TextView title = (TextView)findViewById(R.id.title);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_edit = (ImageButton)findViewById(R.id.ib_edit);
        mcv = (MaterialCalendarView)findViewById(R.id.calendarView);
        task_img = (ImageView)findViewById(R.id.task_img);
        task_notes = (TextView)findViewById(R.id.task_notes);
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
        dateList = queryAllLocalClockingIn(TaskCheckActivity.this, task.getUserId(), task.getObjectId());

        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        mcv.addDecorators(
                //new HighlightWeekendsDecorator(),
                //new TodayDecorator(),
                new EventDecorator(dateList)
        );
        task_notes.setText(task.getTaskNotes());
        task_img.setTag(task.getTaskImg());
        if (!"".equals(task.getTaskImgName())) {
            new TaskAdapter.ImageAsyncTack(task_img).execute(task.getTaskImgName());
        }
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
    //查找本地数据库中某项任务的所有打卡纪录
    public static ArrayList<String> queryAllLocalClockingIn(Context context, String user_id, String task_id) {
        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(context);
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ? and task_id = ?";
        String[] selectionArgs = new String[]{user_id, task_id};
        Cursor cursor = dbRead.query(TaskUtils.CLOCKINGIN_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            //String id = cursor.getString(cursor.getColumnIndex("id"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            /*ClockingIn clockingIn = new ClockingIn();
            clockingIn.setObjectId(id);
            clockingIn.setUserId(user_id);
            clockingIn.setTaskId(task_id);
            clockingIn.setDate(date);*/
            result.add(date);
            //result.add(clockingIn);
        }
        //Collections.reverse(result);
        cursor.close();
        dbRead.close();
        return result;
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
