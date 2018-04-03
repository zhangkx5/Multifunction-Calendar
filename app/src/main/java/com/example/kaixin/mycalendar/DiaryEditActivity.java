package com.example.kaixin.mycalendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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

import com.example.kaixin.mycalendar.Bean.Diary;
import com.example.kaixin.mycalendar.Utils.CheckNetwork;
import com.example.kaixin.mycalendar.Utils.DiaryUtils;
import com.example.kaixin.mycalendar.Utils.LocationUtils;
import com.example.kaixin.mycalendar.Utils.WeatherUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kaixin on 2018/3/9.
 */

public class DiaryEditActivity extends AppCompatActivity {

    private ImageButton ib_back, ib_save, ib_edit;
    private TextView title, date, weather, address, content_tv, title_tv;
    private EditText content_et, title_et;
    private Intent intent;
    private Diary diary;
    private String strDate, strWea, strAdd, strContent, strTitle, id;
    private ArrayList<String> response;
    private final int UPDATE_CONTENT = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CONTENT:
                    response = (ArrayList<String>)msg.obj;
                    handlerWeather(response);
                    break;
                case 1:
                    String id = (String)msg.obj;
                    Toast.makeText(DiaryEditActivity.this, id, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    ProgressDialog progressDialog;
    public void createDiary() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(DiaryEditActivity.this);
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
                DiaryEditActivity.this.finish();
            }
            @Override
            protected Void doInBackground(String... params) {
                DiaryUtils.createBmobDiary(DiaryEditActivity.this, strDate, strAdd, strWea, strTitle, strContent);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
    public void updateDiary() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(DiaryEditActivity.this);
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
                DiaryEditActivity.this.finish();
            }
            @Override
            protected Void doInBackground(String... params) {
                DiaryUtils.updateLocalDiary(DiaryEditActivity.this, diary.getObjectId(), getDiaryTitle(), getContent());
                DiaryUtils.upadteBmobDiary(diary.getObjectId(), getDiaryTitle(), getContent());
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_edit);
        initView();

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
                    updateDiary();
                } else {
                    strDate = getDate();
                    strAdd = getAddress();
                    strWea = getWeather();
                    strTitle = getDiaryTitle();
                    strContent = getContent();
                    createDiary();
                }
            }
        });

        ib_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title_et.setVisibility(View.VISIBLE);
                title_tv.setVisibility(View.GONE);
                content_et.setVisibility(View.VISIBLE);
                content_tv.setVisibility(View.GONE);
                ib_edit.setVisibility(View.GONE);
                ib_save.setVisibility(View.VISIBLE);
                title.setText("编辑日记");
                content_et.setText(diary.getDiaryContent());
            }
        });
    }

    public void initView() {
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_save = (ImageButton)findViewById(R.id.ib_save);
        ib_edit = (ImageButton)findViewById(R.id.ib_edit);
        title = (TextView)findViewById(R.id.title);
        date = (TextView)findViewById(R.id.date);
        weather = (TextView)findViewById(R.id.weather);
        address = (TextView)findViewById(R.id.address);
        title_tv = (TextView)findViewById(R.id.diary_title_tv);
        title_et = (EditText)findViewById(R.id.diary_title_et);
        content_tv = (TextView)findViewById(R.id.diary_content_tv);
        content_et = (EditText) findViewById(R.id.diary_content_et);

        intent = getIntent();
        diary = (Diary)intent.getSerializableExtra("diary");
        if (diary != null) {
            content_et.setVisibility(View.GONE);
            content_tv.setVisibility(View.VISIBLE);
            ib_edit.setVisibility(View.VISIBLE);
            ib_save.setVisibility(View.GONE);
            title.setText("查看日记");
            title_tv.setText(diary.getDiaryTitle());
            content_tv.setText(diary.getDiaryContent());
            strDate = diary.getDiaryDate();
            strWea = diary.getDiaryWeather();
            strAdd = diary.getDiaryAddress();
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
    public String getDiaryTitle() {
        return title_et.getText().toString();
    }
    private void handlerWeather(List<String> response) {
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
    }
}
