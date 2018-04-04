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
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Adapter.HabitAdapter;
import com.example.kaixin.mycalendar.Bean.Habit;
import com.example.kaixin.mycalendar.Utils.EventDecorator;
import com.example.kaixin.mycalendar.Utils.HabitUtils;
import com.example.kaixin.mycalendar.Utils.MyDatabaseHelper;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kaixin on 2018/3/16.
 */

public class HabitClockingInActivity extends AppCompatActivity{

    private ImageButton ib_back, ib_edit;
    private ImageView habit_img;
    private TextView habit_notes;
    private MaterialCalendarView mcv;
    private Button check;
    private Habit habit;
    private List<String> dateList = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        dateList = queryAllLocalClockingIn(HabitClockingInActivity.this, habit.getUserId(), habit.getObjectId());
        mcv.addDecorators(new EventDecorator(dateList));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_clockingin);

        TextView title = (TextView)findViewById(R.id.title);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_edit = (ImageButton)findViewById(R.id.ib_edit);
        mcv = (MaterialCalendarView)findViewById(R.id.calendarView);
        habit_img = (ImageView)findViewById(R.id.habit_img);
        habit_notes = (TextView)findViewById(R.id.habit_notes);
        check = (Button)findViewById(R.id.check);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabitClockingInActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            habit = (Habit)intent.getSerializableExtra("habit");
            title.setText(habit.getHabitName());
        } else {
            HabitClockingInActivity.this.finish();
        }
        dateList = queryAllLocalClockingIn(HabitClockingInActivity.this, habit.getUserId(), habit.getObjectId());
        if (dateList.size() == 0) {

        }

        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        mcv.addDecorators(
                //new HighlightWeekendsDecorator(),
                //new TodayDecorator(),
                new EventDecorator(dateList)
        );
        habit_notes.setText(habit.getHabitNotes());
        habit_img.setTag(habit.getHabitImg());
        if (!"".equals(habit.getHabitImgName())) {
            new HabitAdapter.ImageAsyncTack(habit_img).execute(habit.getHabitImgName());
        }
        //hasClockingIn();
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createClockingIn();
                Log.i("CLOCKINGIN", "打卡成功");
                Toast.makeText(HabitClockingInActivity.this, "打卡成功", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //查找本地数据库中某项任务的所有打卡纪录
    public static ArrayList<String> queryAllLocalClockingIn(Context context, String user_id, String habit_id) {
        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(context);
        ArrayList<String> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ? and habit_id = ?";
        String[] selectionArgs = new String[]{user_id, habit_id};
        Cursor cursor = dbRead.query(HabitUtils.CLOCKINGIN_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            //String id = cursor.getString(cursor.getColumnIndex("id"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            /*ClockingIn clockingIn = new ClockingIn();
            clockingIn.setObjectId(id);
            clockingIn.setUserId(user_id);
            clockingIn.setTaskId(habit_id);
            clockingIn.setDate(date);*/
            result.add(date);
            //result.add(clockingIn);
        }
        //Collections.reverse(result);
        cursor.close();
        dbRead.close();
        Log.i("CLOCKINGIN", "queryAllLocalClockingIn 成功");
        return result;
    }
    public void hasClockingIn() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date(System.currentTimeMillis());
        String date = simpleDateFormat.format(today);
        boolean flag = false;
        flag = HabitUtils.queryOneLocalClockingIn(HabitClockingInActivity.this, habit.getUserId(), habit.getObjectId(), date);
        if (flag == false) {
            HabitUtils.queryOneBmobClockingIn(HabitClockingInActivity.this, habit.getObjectId(), date);
            flag = HabitUtils.queryOneLocalClockingIn(HabitClockingInActivity.this, habit.getUserId(), habit.getObjectId(), date);
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
                progressDialog = new ProgressDialog(HabitClockingInActivity.this);
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
                HabitUtils.createBmobClockingIn(HabitClockingInActivity.this, habit.getObjectId(), date);
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
