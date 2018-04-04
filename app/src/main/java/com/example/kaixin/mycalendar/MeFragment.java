package com.example.kaixin.mycalendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.MyUser;
import com.example.kaixin.mycalendar.Utils.ImageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by kaixin on 2018/3/23.
 */

public class MeFragment extends Fragment {

    private CircleImageView user_photo;
    private ImageView ib_setting;
    private TextView user_name, user_notes;
    private LinearLayout personal, diary, weather, anniversary, schedule, account, about;
    private Button login;
    private MyUser bmobUser;
    private String userId;
    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle("我");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorTitle));
        ib_setting = (ImageView)view.findViewById(R.id.ib_setting);
        user_photo = (CircleImageView)view.findViewById(R.id.user_photo);
        user_name = (TextView)view.findViewById(R.id.user_name);
        user_notes = (TextView)view.findViewById(R.id.user_notes);
        login = (Button)view.findViewById(R.id.login);
        personal = (LinearLayout)view.findViewById(R.id.personal);
        diary = (LinearLayout) view.findViewById(R.id.diary);
        weather = (LinearLayout)view.findViewById(R.id.weather);
        anniversary = (LinearLayout)view.findViewById(R.id.anniversary);
        schedule = (LinearLayout)view.findViewById(R.id.schedule);
        account = (LinearLayout)view.findViewById(R.id.account);
        about = (LinearLayout) view.findViewById(R.id.about);

        setUserInfo();

        ib_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                startActivity(intent);
            }
        });
        diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DiaryActivity.class);
                startActivity(intent);
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DiaryActivity.class);
                startActivity(intent);
            }
        });
        anniversary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AnniversaryActivity.class);
                startActivity(intent);
            }
        });
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                startActivity(intent);
            }
        });
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                startActivity(intent);
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
    class ImageAsyncTack extends AsyncTask<String, Void, Bitmap> {

        private ImageView photo;
        public ImageAsyncTack(CircleImageView photo) {
            this.photo = photo;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bm = null;
            BmobFile bmobFile = new BmobFile(userId + ".jpg", "", bmobUser.getUrlPic());
            ImageUtils.downloadImage(bmobFile);
            try {
                FileInputStream fileInputStream = new FileInputStream(params[0]);
                bm = BitmapFactory.decodeStream(fileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bm;
        }
        @Override
        protected void onPostExecute(Bitmap bm) {
            super.onPostExecute(bm);
            photo.setImageBitmap(bm);
            Toast.makeText(getActivity(), "这是下载的图片", Toast.LENGTH_SHORT).show();
        }
    }
    private void setUserInfo() {
        bmobUser = BmobUser.getCurrentUser(MyUser.class);
        if (bmobUser != null) {
            login.setVisibility(View.GONE);
            user_name.setText(bmobUser.getUsername());
            user_notes.setText(bmobUser.getNotes());
            userId = bmobUser.getObjectId();
            if (!("".equals(bmobUser.getUrlPic()) && bmobUser.getUrlPic() == null)) {
                String phtotUrl = Environment.getExternalStorageDirectory().getPath() + "/mycalendar/" + userId + ".jpg";
                File userPhoto = new File(phtotUrl);
                if (!userPhoto.exists()) {
                    try {
                        new ImageAsyncTack(user_photo).execute(phtotUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(phtotUrl);
                        Bitmap bm = BitmapFactory.decodeStream(fileInputStream);
                        user_photo.setImageBitmap(bm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            user_name.setText("unknown");
            user_notes.setText("登录可云存储数据哦");
            user_photo.setImageResource(R.mipmap.ic_user);
            login.setVisibility(View.VISIBLE);
        }
    }
}
