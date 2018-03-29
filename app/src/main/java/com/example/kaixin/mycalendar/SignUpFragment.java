package com.example.kaixin.mycalendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by kaixin on 2018/3/22.
 */

public class SignUpFragment extends Fragment{

    private EditText username, password, email, confirm;
    private Button signup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, null);

        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        email = (EditText) view.findViewById(R.id.email);
        confirm = (EditText)view.findViewById(R.id.confirm);
        signup = (Button) view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入邮箱账号", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(username.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(getActivity(), "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(confirm.getText().toString())) {
                    Toast.makeText(getActivity(), "请确认密码", Toast.LENGTH_SHORT).show();
                } else {
                    //判断网络

                    //注册失败

                    //注册成功
                    UserUtils.SignUpUser(getContext(), username.getText().toString(),password.getText().toString(),email.getText().toString());
                    LoginActivity loginActivity = (LoginActivity) getActivity();
                    loginActivity.goToLogin();
                    /*FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment login_fragment = new LoginFragment();
                    fm.beginTransaction().replace(R.id.content, login_fragment).commit();*/
                    /*MyUser bmobUser = new MyUser();
                    bmobUser.setUsername(username.getText().toString());
                    bmobUser.setPassword(password.getText().toString());
                    bmobUser.setEmail(email.getText().toString());
                    bmobUser.signUp(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "注册成功："+s.toString(), Toast.LENGTH_SHORT).show();
                                //getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });*/
                }
            }
        });
        return view;
    }
}
