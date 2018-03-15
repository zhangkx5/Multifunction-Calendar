package com.example.kaixin.mycalendar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kaixin on 2018/3/15.
 */

public class ScheduleEditActivity extends AppCompatActivity{

    private ImageButton ib_back, ib_save;
    private EditText schedule_title, schedule_address, schedule_notes;
    private TextView schedule_start, schedule_end, schedule_call;
    private DateTimePicker dateTimePicker1, dateTimePicker2, dateTimePicker3;
    private MyDatabaseHelper myDatabaseHelper;
    private Schedule schedule = null;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_schedule_edit);
        myDatabaseHelper = new MyDatabaseHelper(this);

        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_save = (ImageButton)findViewById(R.id.ib_save);
        schedule_title = (EditText)findViewById(R.id.schedule_title);
        schedule_address = (EditText)findViewById(R.id.schedule_address);
        schedule_start = (TextView)findViewById(R.id.startTime);
        schedule_end = (TextView)findViewById(R.id.endTime);
        schedule_call = (TextView)findViewById(R.id.callTime);
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
                    updateInDB(schedule);
                    ScheduleEditActivity.this.finish();
                } else {
                    addInDB(getSchedultTitle(), getAddress(), getStartTime(), getEndTime(), getCallTime(), getNotes());
                    ScheduleEditActivity.this.finish();
                }
            }
        });

        initDateTimePicker();
        schedule_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimePicker1.show(schedule_start.getText().toString());
            }
        });
        schedule_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimePicker2.show(schedule_end.getText().toString());
            }
        });
        schedule_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimePicker3.show(schedule_call.getText().toString());
            }
        });

        Intent intent = getIntent();
        schedule = (Schedule)intent.getSerializableExtra("schedule");
        if (schedule != null) {
            schedule_title.setText(schedule.getTitle());
            schedule_address.setText(schedule.getAddress());
            schedule_start.setText(schedule.getStart());
            schedule_end.setText(schedule.getEnd());
            schedule_call.setText(schedule.getCall());
            schedule_notes.setText(schedule.getNotes());
        }
    }

    public void addInDB(String title, String address, String start, String end, String call, String notes) {
        String sc_id = String.valueOf(System.currentTimeMillis());
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(MyDatabaseHelper.SCHEDULE_TABLE_INSERT, new Object[]{sc_id, title, address, start, end, call, notes});
        dbWrite.close();
        Toast.makeText(ScheduleEditActivity.this, sc_id + title + "?", Toast.LENGTH_SHORT).show();
    }

    public void updateInDB(Schedule schedule) {
        String sc_id = schedule.getId();
        SQLiteDatabase dbUpdate = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", getSchedultTitle());
        values.put("address", getAddress());
        values.put("start", getStartTime());
        values.put("end", getEndTime());
        values.put("call", getCallTime());
        values.put("notes", getNotes());
        dbUpdate.update(MyDatabaseHelper.SCHEDULE_TABLE_NAME, values,
                "scid = ?", new String[]{sc_id});
    }
    private void initDateTimePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        schedule_start.setText(now);
        schedule_end.setText(now);
        schedule_call.setText(now);

        dateTimePicker1 = new DateTimePicker(this, new DateTimePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                schedule_start.setText(time);
            }
        }, "2018-03-15 15:00", now);
        dateTimePicker1.showSpecificTime(true);
        dateTimePicker1.setIsLoop(true);

        dateTimePicker2 = new DateTimePicker(this, new DateTimePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                schedule_start.setText(time);
            }
        }, "2018-03-15 15:00", now);
        dateTimePicker2.showSpecificTime(true);
        dateTimePicker2.setIsLoop(true);

        dateTimePicker3 = new DateTimePicker(this, new DateTimePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                schedule_start.setText(time);
            }
        }, "2018-03-15 15:00", now);
        dateTimePicker3.showSpecificTime(true);
        dateTimePicker3.setIsLoop(true);
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

    public String getCallTime() {
        return schedule_call.getText().toString();
    }
    public String getNotes() {
        return ""+schedule_notes.getText().toString();
    }
}