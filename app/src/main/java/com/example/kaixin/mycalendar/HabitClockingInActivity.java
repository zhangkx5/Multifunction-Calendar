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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarUtil;
import com.example.kaixin.mycalendar.Adapter.HabitAdapter;
import com.example.kaixin.mycalendar.Bean.ClockingIn;
import com.example.kaixin.mycalendar.Bean.Habit;
import com.example.kaixin.mycalendar.Utils.EventDecorator;
import com.example.kaixin.mycalendar.Utils.HabitUtils;
import com.example.kaixin.mycalendar.Utils.MyDatabaseHelper;
import com.example.kaixin.mycalendar.Utils.UserUtils;
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
    private CalendarDateView calendarDateView;
    private CaledarAdapter myCalendarAdapter;
    private Button check;
    private Habit habit;
    private List<String> list = new ArrayList<>();
    private CalendarBean todayBean;
    private View chidView;

    @Override
    public void onResume() {
        super.onResume();
        //dateList = queryAllLocalClockingIn(HabitClockingInActivity.this, habit.getUserId(), habit.getObjectId());
        //mcv.addDecorators(new EventDecorator(dateList));
        list = HabitUtils.queryAllLocalClockingIn(this, habit.getUserId(), habit.getObjectId());
        initView();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_clockingin);

        TextView title = (TextView)findViewById(R.id.title);
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_edit = (ImageButton)findViewById(R.id.ib_edit);
        calendarDateView = (CalendarDateView)findViewById(R.id.calendarDateView);
        habit_img = (ImageView)findViewById(R.id.habit_img);
        habit_notes = (TextView)findViewById(R.id.habit_notes);
        check = (Button)findViewById(R.id.check);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabitClockingInActivity.this.finish();
            }
        });
        ib_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            habit = (Habit)intent.getSerializableExtra("habit");
            title.setText(habit.getHabitName());
        } else {
            HabitClockingInActivity.this.finish();
        }
        createClockingIn();
        list = HabitUtils.queryAllLocalClockingIn(this, habit.getUserId(), habit.getObjectId());
        initView();

        habit_notes.setText(habit.getHabitNotes());
        habit_img.setTag(habit.getHabitImg());
        if (!"".equals(habit.getHabitImgName())) {
            new HabitAdapter.ImageAsyncTack(habit_img).execute(habit.getHabitImgName());
        }
        //hasClockingIn();
        check.setText("打卡成功");

    }
    private boolean hasEvents(CalendarBean bean, List<String> stringList) {
        /*Date calDate = new Date(bean.year, bean.moth, bean.day);
        if (calDate.after(new Date(System.currentTimeMillis()))) return false;*/
        String month = (bean.moth < 10) ? ("0" + bean.moth) : ("" + bean.moth);
        String day = (bean.day < 10) ? ("0" + bean.day) : ("" + bean.day);
        String tempDate = bean.year + "-" + month + "-" + day;
        for (int i = 0; i < stringList.size(); i++) {
            if (tempDate.equals(stringList.get(i)))
                return true;
        }
        return false;
    }
    private void initView() {
        int[] data = CalendarUtil.getYMD(new Date());
        todayBean = new CalendarBean(data[0], data[1], data[2]);
        myCalendarAdapter = new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar_date_view, null);
                }
                TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                TextView text = (TextView) convertView.findViewById(R.id.text);
                View redPoint = (View) convertView.findViewById(R.id.redPoint);
                text.setText("" + bean.day);
                if (bean.mothFlag != 0) {
                    text.setTextColor(0xff9299a1);
                } else {
                    text.setTextColor(0xff444444);
                }
                chinaText.setText(bean.chinaDay);
                if (hasEvents(bean, list)) {
                    redPoint.setVisibility(View.VISIBLE);
                } else {
                    redPoint.setVisibility(View.GONE);
                }
                return convertView;
            }
        };
        calendarDateView.setAdapter(myCalendarAdapter);
    }

    /*public void hasClockingIn() {
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
    }*/
    ProgressDialog progressDialog;
    public void createClockingIn() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                /*progressDialog = new ProgressDialog(HabitClockingInActivity.this);
                progressDialog.setMessage("保存中...");
                progressDialog.setCancelable(true);
                progressDialog.show();*/
            }
            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                /*progressDialog.dismiss();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
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
