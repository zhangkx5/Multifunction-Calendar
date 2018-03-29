package com.example.kaixin.mycalendar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.AnniversaryDay;
import com.example.kaixin.mycalendar.Utils.AnniversaryUtils;
import com.example.kaixin.mycalendar.Utils.UserUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Created by kaixin on 2018/2/5.
 */

public class AnniversaryEditActivity extends AppCompatActivity {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private ImageButton ib_back;
    private ImageButton ib_save;
    private EditText an_name;
    private EditText an_notes;
    private TextView an_date;
    private AnniversaryDay anniversaryDay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary_edit);

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
                    updateAnniversary();
                } else {
                    createAnniversary();
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
            an_name.setText(anniversaryDay.getAnniversaryName());
            an_date.setText(anniversaryDay.getAnniversaryDate());
            an_notes.setText(anniversaryDay.getAnniversaryNotes());
        }
    }

    ProgressDialog progressDialog;
    public void createAnniversary() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(AnniversaryEditActivity.this);
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
                AnniversaryEditActivity.this.finish();
            }
            @Override
            protected Void doInBackground(String... params) {
                AnniversaryUtils.createBmobAnniversary(AnniversaryEditActivity.this, getName(), getDate(), getNotes());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
    public void updateAnniversary() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(AnniversaryEditActivity.this);
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
                AnniversaryEditActivity.this.finish();
            }
            @Override
            protected Void doInBackground(String... params) {
                AnniversaryUtils.updateLocalAnniversary(AnniversaryEditActivity.this,
                        anniversaryDay.getObjectId(), getName(), getDate(), getNotes());
                AnniversaryUtils.upadteBmobAnniversaryDay(anniversaryDay.getObjectId(), getName(), getDate(), getNotes());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear+1;
                String month = (monthOfYear < 10) ? ("0" + monthOfYear) : ("" + monthOfYear);
                String day = (dayOfMonth < 10) ? ("0" + dayOfMonth) :("" + dayOfMonth);
                //String month = monthOfYear+"";
                //String day = dayOfMonth+"";
                an_date.setText(year+"-"+month+"-"+day);
            }
        },
        calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(true);
        datePickerDialog.show();
    }

    public void setDate() {
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        //Date ymd = new Date(System.currentTimeMillis());
        //an_date.setText(simpleDateFormat.format(ymd));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        an_date.setText(sdf.format(System.currentTimeMillis()));
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
