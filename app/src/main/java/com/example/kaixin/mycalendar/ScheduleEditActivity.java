package com.example.kaixin.mycalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.example.kaixin.mycalendar.Bean.Schedule;
import com.example.kaixin.mycalendar.Utils.ScheduleUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by kaixin on 2018/3/15.
 */

public class ScheduleEditActivity extends AppCompatActivity{


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private ImageButton ib_back, ib_save;
    private EditText schedule_title, schedule_address, schedule_notes;
    private TextView schedule_start, schedule_end;
    private Schedule schedule = null;
    private String id = String.valueOf(System.currentTimeMillis());

    ProgressDialog progressDialog;
    public void createSchedule() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(ScheduleEditActivity.this);
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
                ScheduleEditActivity.this.finish();
            }
            @Override
            protected Void doInBackground(String... params) {
                id = ScheduleUtils.createBmobSchedule(ScheduleEditActivity.this, getSchedultTitle(),
                        getAddress(), getStartTime(), getEndTime(), getNotes());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
    public void updateSchedule() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(ScheduleEditActivity.this);
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
                ScheduleEditActivity.this.finish();
            }
            @Override
            protected Void doInBackground(String... params) {
                ScheduleUtils.updateLocalSchedule(ScheduleEditActivity.this,
                        schedule.getObjectId(), getSchedultTitle(), getAddress(),
                        getStartTime(), getEndTime(), getNotes());
                ScheduleUtils.upadteBmobSchedule(schedule.getObjectId(), getSchedultTitle(),
                        getAddress(), getStartTime(), getEndTime(), getNotes());
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
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_schedule_edit);

        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_save = (ImageButton)findViewById(R.id.ib_save);
        schedule_title = (EditText)findViewById(R.id.schedule_title);
        schedule_address = (EditText)findViewById(R.id.schedule_address);
        schedule_start = (TextView)findViewById(R.id.startTime);
        schedule_end = (TextView)findViewById(R.id.endTime);
        schedule_notes = (EditText)findViewById(R.id.schedule_notes);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScheduleEditActivity.this.finish();
            }
        });
        ib_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(schedule_title.getText().toString())) {
                    Toast.makeText(ScheduleEditActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                } else if (schedule != null){
                    updateSchedule();
                } else {
                    createSchedule();
                    AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                    Intent receiverIntent = new Intent(ScheduleEditActivity.this, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(ScheduleEditActivity.this, new Random().nextInt(10000), receiverIntent, 0);
                    long callTime = System.currentTimeMillis() + 5 * 1000;
                    try {
                        callTime = stringToLong(schedule_start.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    alarmManager.set(AlarmManager.RTC_WAKEUP, callTime, pendingIntent);
                }
            }
        });

        initDateTimePicker();
        schedule_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickDialog(schedule_start);
                schedule_end.setText(schedule_start.getText());
            }
        });
        schedule_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickDialog(schedule_end);
            }
        });

        Intent intent = getIntent();
        schedule = (Schedule)intent.getSerializableExtra("schedule");
        if (schedule != null) {
            schedule_title.setText(schedule.getScheduleTitle());
            schedule_address.setText(schedule.getScheduleAddress());
            schedule_start.setText(schedule.getScheduleStart());
            schedule_end.setText(schedule.getScheduleEnd());
            schedule_notes.setText(schedule.getScheduleNotes());
        }
    }
    private long stringToLong(String strDate) throws ParseException {
        return sdf.parse(strDate).getTime();
    }

    private void showDatePickDialog(final TextView text) {
        final DatePickDialog dialog = new DatePickDialog(ScheduleEditActivity.this);
        dialog.setYearLimt(5);
        dialog.setTitle("选择时间");
        dialog.setType(DateType.TYPE_YMDHM);
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        dialog.setOnChangeLisener(null);
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                text.setText(sdf.format(date));
            }
        });
        dialog.show();
    }
    private void initDateTimePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        schedule_start.setText(now);
        schedule_end.setText(now);
    }

    public String getSchedultTitle() {
        return ""+schedule_title.getText().toString();
    }
    public String getAddress() {
        return ""+schedule_address.getText().toString();
    }
    public String getStartTime() {
        return schedule_start.getText().toString();
    }
    public String getEndTime() {
        return schedule_end.getText().toString();
    }
    public String getNotes() {
        return ""+schedule_notes.getText().toString();
    }
}
