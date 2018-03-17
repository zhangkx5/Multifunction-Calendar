package com.example.kaixin.mycalendar;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kaixin on 2018/3/16.
 */

public class TaskEditActivity extends AppCompatActivity {

    private ImageButton ib_back, ib_save;
    private EditText task_name, task_notes;
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        myDatabaseHelper = new MyDatabaseHelper(this);

        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_save = (ImageButton)findViewById(R.id.ib_save);
        task_name = (EditText)findViewById(R.id.task_name);
        task_notes = (EditText)findViewById(R.id.task_notes);

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskEditActivity.this.finish();
            }
        });

        ib_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(task_name.getText().toString())) {
                    Toast.makeText(TaskEditActivity.this, "请输入任务名称", Toast.LENGTH_SHORT).show();
                } else {
                    addInDB(task_name.getText().toString(), task_notes.getText().toString());
                    TaskEditActivity.this.finish();
                }
            }
        });
    }

    public void addInDB(String name, String notes) {
        String ts_id = String.valueOf(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date(System.currentTimeMillis());
        String date = simpleDateFormat.format(today);
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(MyDatabaseHelper.TASK_TABLE_INSERT, new Object[]{ts_id, date, name, notes});
        dbWrite.close();
        Toast.makeText(TaskEditActivity.this, ts_id + name, Toast.LENGTH_SHORT).show();
    }
}
