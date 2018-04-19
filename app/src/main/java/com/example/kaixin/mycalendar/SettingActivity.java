package com.example.kaixin.mycalendar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Utils.UserUtils;

/**
 * Created by kaixin on 2018/3/29.
 */

public class SettingActivity extends AppCompatActivity {

    private Button quit, reset, login;
    private ImageView ib_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        quit = (Button)findViewById(R.id.quit);
        reset = (Button)findViewById(R.id.reset);
        login = (Button)findViewById(R.id.login);
        ib_back = (ImageView)findViewById(R.id.ib_back);

        if (UserUtils.getCurrentUser() != null) {
            login.setVisibility(View.GONE);
        } else {
            reset.setVisibility(View.GONE);
            quit.setVisibility(View.GONE);
        }



        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingActivity.this.finish();
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserUtils.LogOut(SettingActivity.this);
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                SettingActivity.this.finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(SettingActivity.this);
                final View vi = inflater.inflate(R.layout.dialog_resetpassword, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setView(vi);
                final Dialog dialog = builder.create();
                dialog.show();
                final EditText oldPassword = (EditText)vi.findViewById(R.id.pas_old);
                final EditText newPassword = (EditText)vi.findViewById(R.id.pas_new);
                final EditText confirmPassword = (EditText)vi.findViewById(R.id.pas_confirm);
                final Button confirm = (Button)vi.findViewById(R.id.confirm);
                Button clean = (Button)vi.findViewById(R.id.clean);
                clean.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oldPassword.setText("");
                        newPassword.setText("");
                        confirmPassword.setText("");
                    }
                });
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(oldPassword.getText().toString())) {
                            Toast.makeText(SettingActivity.this, "请输入原密码", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(newPassword.getText().toString())) {
                            Toast.makeText(SettingActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                        } else {
                            final String oldpwd = oldPassword.getText().toString();
                            final String newpwd = newPassword.getText().toString();
                            UserUtils.ResetPassword(SettingActivity.this, oldpwd, newpwd);
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
    }
}
