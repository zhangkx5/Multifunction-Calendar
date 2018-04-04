package com.example.kaixin.mycalendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.MyUser;
import com.example.kaixin.mycalendar.Utils.AccountBillUtils;
import com.example.kaixin.mycalendar.Utils.AnniversaryUtils;
import com.example.kaixin.mycalendar.Utils.DiaryUtils;
import com.example.kaixin.mycalendar.Utils.HabitUtils;
import com.example.kaixin.mycalendar.Utils.ScheduleUtils;
import com.example.kaixin.mycalendar.Utils.UserUtils;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by kaixin on 2018/3/22.
 */

public class LoginFragment extends Fragment{

    private EditText email, password;
    private Button login, lost, pass;
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);

        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        lost = (Button) view.findViewById(R.id.lostPassword);
        pass = (Button) view.findViewById(R.id.btn_pass);
        login = (Button) view.findViewById(R.id.login);
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入邮箱账号", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    //判断网络

                    //登录成功
                    BmobUser.loginByAccount(email.getText().toString(), password.getText().toString(), new LogInListener<MyUser>() {
                        @Override
                        public void done(MyUser user, BmobException e) {
                            if (user != null) {
                                Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                                Log.i("smile", "用户登录成功");
                                loadingDatas();
                            } else {
                                Toast.makeText(getActivity(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //UserUtils.Login(getContext(), email.getText().toString(), password.getText().toString());
                    if (UserUtils.getCurrentUser() != null) {
                        getActivity().finish();
                    }
                }
            }
        });
        lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入邮箱账号", Toast.LENGTH_SHORT).show();
                } else {
                    /*BmobUser.resetPasswordByEmail(email.getText().toString(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "重置密码请求成功，请到邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
                    UserUtils.LostPassword(getContext(), email.getText().toString());
                }
            }
        });
        return view;
    }
    public void loadingDatas() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("加载中...");
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
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
            @Override
            protected Void doInBackground(String... params) {
                HabitUtils.queryAllBmobHabit(getActivity());
                HabitUtils.queryAllAllBmobClockingIn(getActivity());
                AccountBillUtils.queryAllBmobAccountBill(getActivity());
                AnniversaryUtils.queryAllBmobAnniversaryDay(getActivity());
                DiaryUtils.queryAllBmobDiary(getActivity());
                ScheduleUtils.queryAllBmobSchedule(getActivity());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
}
