package com.example.kaixin.mycalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private Button login, lost;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);

        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        lost = (Button) view.findViewById(R.id.lostPassword);
        login = (Button) view.findViewById(R.id.login);
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
                    /*BmobUser.loginByAccount(email.getText().toString(), password.getText().toString(), new LogInListener<MyUser>() {
                        @Override
                        public void done(MyUser user, BmobException e) {
                            if (user != null) {
                                Toast.makeText(getActivity(), "登录成功", Toast.LENGTH_SHORT).show();
                                Log.i("smile", "用户登录成功");
                                getActivity().finish();
                                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                                startActivity(intent);
                            }
                        }
                    });*/
                    UserUtils.Login(getContext(), email.getText().toString(), password.getText().toString());
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
}
