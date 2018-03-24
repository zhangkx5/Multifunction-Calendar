package com.example.kaixin.mycalendar.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.kaixin.mycalendar.MyUser;
import com.example.kaixin.mycalendar.PersonalActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by kaixin on 2018/3/24.
 */

public class UserUtils {

    //sign up user
    public static void SignUpUser(Context context, String username, String password, String email) {
        final Context mContext = context;
        MyUser bmobUser = new MyUser();
        bmobUser.setUsername(username);
        bmobUser.setPassword(password);
        bmobUser.setEmail(email);
        bmobUser.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser s, BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "注册成功："+s.toString(), Toast.LENGTH_SHORT).show();
                    //getActivity().finish();
                } else {
                    Toast.makeText(mContext, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //login
    public static void Login(Context context, String email, String password) {
        final Context mContext = context;
        BmobUser.loginByAccount(email, password, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser user, BmobException e) {
                if (user != null) {
                    Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                    Log.i("smile", "用户登录成功");
                    Intent intent = new Intent(mContext, PersonalActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    //is user online
    public static MyUser IsOnLine() {
        return BmobUser.getCurrentUser(MyUser.class);
    }

    //lost password
    public static void LostPassword(Context context, String email) {
        final Context mContext = context;
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "重置密码请求成功，请到邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "请求失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //upate user notes
    public static void UpdateUserNotes(Context context, String notes) {
        final Context mContext = context;
        MyUser newUser = new MyUser();
        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        newUser.setNotes(notes);
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "更新信息成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "更新信息失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //upate user name
    public static void UpdateUserName(Context context, String name) {
        final Context mContext = context;
        MyUser newUser = new MyUser();
        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        newUser.setUsername(name);
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "更新信息成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "更新信息失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //upate user sex
    public static void UpdateUserSex(Context context, String sex) {
        final Context mContext = context;
        MyUser newUser = new MyUser();
        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        newUser.setSex(sex);
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "更新信息成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "更新信息失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //upate user uriPic
    public static void UpdateUserUriPic(Context context, String uriPic) {
        final Context mContext = context;
        MyUser newUser = new MyUser();
        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        newUser.setUrlPic(uriPic);
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "更新信息成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "更新信息失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //reset password
    public static void ResetPassword(Context context, String old, String reset) {
        final Context mContext = context;
        BmobUser.updateCurrentUserPassword(old, reset, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(mContext, "密码修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "密码修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // logout
    public static void LogOut() {
        BmobUser.logOut();
        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
    }
}
