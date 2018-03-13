package com.example.kaixin.mycalendar;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kaixin on 2018/3/9.
 */

public class DiaryEditActivity extends AppCompatActivity {

    private MyDatabaseHelper myDatabaseHelper;
    private ImageButton ib_back, ib_save, ib_edit;
    private TextView title, date, weather, address, content_tv;
    private EditText content_et;
    private Intent intent;
    private Diary diary;
    private String strDate, strWea, strAdd, strContent;
    private ArrayList<String> response;
    private final int UPDATE_CONTENT = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CONTENT:
                    response = (ArrayList<String>)msg.obj;
                    String result = response.get(0);
                    if ("查询结果为空".equals(result)) {
                        strWea = "多云";
                    } else if ("发现错误：免费用户不能使用高速访问。http://www.webxml.com.cn/".equals(result)){
                        strWea = "多云";
                    } else if ("发现错误：免费用户 24 小时内访问超过规定数量。http://www.webxml.com.cn/".equals(result)) {
                        strWea = "多云";
                    } else {
                        if (response.size() > 28) {
                            strWea = response.get(7).substring(response.get(7).indexOf(" ")+1, response.get(7).length());
                        } else {
                            strWea = "多云";
                        }
                    }
                    weather.setText(strWea);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_edit);
        myDatabaseHelper = new MyDatabaseHelper(this);

        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_save = (ImageButton)findViewById(R.id.ib_save);
        ib_edit = (ImageButton)findViewById(R.id.ib_edit);
        title = (TextView)findViewById(R.id.title);
        date = (TextView)findViewById(R.id.date);
        weather = (TextView)findViewById(R.id.weather);
        address = (TextView)findViewById(R.id.address);
        content_tv = (TextView)findViewById(R.id.diary_content_tv);
        content_et = (EditText) findViewById(R.id.diary_content_et);
        showView();

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DiaryEditActivity.this.finish();
            }
        });

        ib_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(getContent())) {
                    Toast.makeText(DiaryEditActivity.this, "还没写日记哦...", Toast.LENGTH_SHORT).show();
                } else if (diary != null){
                    updateInBD(diary, getContent());
                    DiaryEditActivity.this.finish();
                } else {
                    strDate = getDate();
                    strAdd = getAddress();
                    strWea = getWeather();
                    strContent = getContent();
                    addInDB(strDate, strAdd, strWea, strContent);
                    DiaryEditActivity.this.finish();
                }
            }
        });

        ib_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content_et.setVisibility(View.VISIBLE);
                content_tv.setVisibility(View.GONE);
                ib_edit.setVisibility(View.GONE);
                ib_save.setVisibility(View.VISIBLE);
                title.setText("编辑日记");
                content_et.setText(diary.getContent());
            }
        });
    }

    public void addInDB(String date, String address, String weather, String content) {
        String dy_id = String.valueOf(System.currentTimeMillis());
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(MyDatabaseHelper.DIARY_TABLE_INSERT, new Object[]{dy_id, date, address, weather, content});
        dbWrite.close();
        Toast.makeText(DiaryEditActivity.this, dy_id + getContent(), Toast.LENGTH_SHORT).show();
    }

    public void updateInBD(Diary diary, String content) {
        String dy_id = diary.getId();
        SQLiteDatabase dbUpdate = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", content);
        dbUpdate.update(MyDatabaseHelper.DIARY_TABLE_NAME, values, "dyid = ?", new String[] {dy_id});
        dbUpdate.close();
    }
    public void showView() {
        intent = getIntent();
        diary = (Diary)intent.getSerializableExtra("diary");
        if (diary != null) {
            content_et.setVisibility(View.GONE);
            content_tv.setVisibility(View.VISIBLE);
            ib_edit.setVisibility(View.VISIBLE);
            ib_save.setVisibility(View.GONE);
            title.setText("查看日记");
            content_tv.setText(diary.getContent());
            strDate = diary.getDate();
            strWea = diary.getWeather();
            strAdd = diary.getAddress();
        } else {
            ib_edit.setVisibility(View.GONE);
            ib_save.setVisibility(View.VISIBLE);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());
            strDate = simpleDateFormat.format(curDate);
            strWea = "多云";
            strAdd = "广州";
            PackageManager pm = getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", "com.example.kaixin.mycalendar"));
            if (permission) {
                LocationUtils.getCNBylocation(DiaryEditActivity.this);
                strAdd = LocationUtils.cityName;
            }
            if (CheckNetwork.isNetworkAvailable(DiaryEditActivity.this)) {
                postRequest(strAdd);
            }
            content_et.setVisibility(View.VISIBLE);
            content_tv.setVisibility(View.GONE);
            title.setText("写日记");
        }
        date.setText(strDate);
        weather.setText(strWea);
        address.setText(strAdd);
    }

    private void postRequest(final String request) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = UPDATE_CONTENT;
                message.obj = WeatherUtils.postRequest(request);
                handler.sendMessage(message);
            }
        }).start();
    }
    public String getDate() {
        return date.getText().toString();
    }
    public String getWeather() {
        return weather.getText().toString();
    }
    public String getAddress() {
        return address.getText().toString();
    }
    public String getContent() {
        return content_et.getText().toString();
    }

}
