package com.example.kaixin.mycalendar;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Created by kaixin on 2018/2/5.
 */

public class AnniversaryEditActivity extends AppCompatActivity {

    private ImageButton ib_back;
    private ImageButton ib_save;
    private EditText an_name;
    private EditText an_notes;
    private TextView an_date;
    private MyDatabaseHelper myDatabaseHelper;
    private AnniversaryDay anniversaryDay = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary_edit);
        myDatabaseHelper = new MyDatabaseHelper(this);

        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_save = (ImageButton)findViewById(R.id.ib_save);
        an_name = (EditText)findViewById(R.id.name);
        an_notes = (EditText)findViewById(R.id.notes);
        an_date = (TextView)findViewById(R.id.date);
        setDate();
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnniversaryEditActivity.this.finish();
            }
        });

        ib_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(getName())) {
                    Toast.makeText(AnniversaryEditActivity.this, "请输入标题", Toast.LENGTH_SHORT).show();
                } else if (anniversaryDay != null){
                    updateInBD(anniversaryDay, getName(), getDate(), getNotes());
                    AnniversaryEditActivity.this.finish();
                } else {
                    addInDB(getName(), getDate(), getNotes());
                    AnniversaryEditActivity.this.finish();
                }

            }
        });

        an_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        Intent intent = getIntent();
        anniversaryDay = (AnniversaryDay)intent.getSerializableExtra("anniversaryDay");
        if (anniversaryDay != null) {
            an_name.setText(anniversaryDay.getName());
            an_date.setText(anniversaryDay.getDate());
            an_notes.setText(anniversaryDay.getNotes());
        }
    }

    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear+1;
                String month = (monthOfYear < 10) ? ("0" + monthOfYear) : ("" + monthOfYear);
                String day = (dayOfMonth < 10) ? ("0" + dayOfMonth) :("" + dayOfMonth);
                an_date.setText(year+"年"+month+"月"+day+"日");
            }
        },
        calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(true);
        datePickerDialog.show();
    }

    public void setDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date ymd = new Date(System.currentTimeMillis());
        an_date.setText(simpleDateFormat.format(ymd));
    }

    public void addInDB(String name, String date, String notes) {
        String ad_id = String.valueOf(System.currentTimeMillis());
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(MyDatabaseHelper.ANNIVERSARY_TABLE_INSERT, new Object[]{ad_id, name, date, notes});
        dbWrite.close();
        Toast.makeText(AnniversaryEditActivity.this, ad_id + getName() + getDate() + getNotes(), Toast.LENGTH_SHORT).show();
    }

    public void updateInBD(AnniversaryDay anniversaryDay, String name, String date, String notes) {
        String ad_id = anniversaryDay.getId();
        SQLiteDatabase dbUpdate = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);;
        values.put("date", date);
        values.put("notes", notes);
        dbUpdate.update(MyDatabaseHelper.ANNIVERSARY_TABLE_NAME, values,
                "adid = ?", new String[] {ad_id});
    }
    public String getName() {
        return an_name.getText().toString();
    }

    public String getDate() {
        return an_date.getText().toString();
    }

    public String getNotes() {
        return an_notes.getText().toString();
    }
}
