package com.example.kaixin.mycalendar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Utils.ImageUtils;
import com.example.kaixin.mycalendar.Utils.UserUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by kaixin on 2018/3/20.
 */

public class PersonalActivity extends AppCompatActivity {

    private ImageView photo;
    private TextView name, email, sex, notes;
    private LinearLayout user_name, user_photo, user_sex, user_notes;
    private static final int CHOOSE_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private File tempFile;
    private Uri tempUri, saveUri;
    private static final int CROP_SMALL_PICTURE = 2;
    private MyUser bmobUser;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        initViews();

        /*quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                sex.setText("");
                email.setText("");
                notes.setText("");
                photo.setImageResource(R.mipmap.ic_user);
                UserUtils.LogOut(PersonalActivity.this);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(PersonalActivity.this);
                final View vi = inflater.inflate(R.layout.dialog_resetpassword, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
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
                            Toast.makeText(PersonalActivity.this, "请输入原密码", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(newPassword.getText().toString())) {
                            Toast.makeText(PersonalActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                        } else {
                            final String oldpwd = oldPassword.getText().toString();
                            final String newpwd = newPassword.getText().toString();
                            UserUtils.ResetPassword(PersonalActivity.this, oldpwd, newpwd);
                        }
                        dialog.dismiss();
                    }
                });

            }
        });*/
        bmobUser = BmobUser.getCurrentUser(MyUser.class);
        if (bmobUser != null) {
            name.setText(bmobUser.getUsername());
            email.setText(bmobUser.getEmail());
            sex.setText(bmobUser.getSex());
            notes.setText(bmobUser.getNotes());
            userId = bmobUser.getObjectId();
            if (!("".equals(bmobUser.getUrlPic()) && bmobUser.getUrlPic() == null)) {
                String phtotUrl = Environment.getExternalStorageDirectory().getPath() + "/mycalendar/" + userId + ".jpg";
                File userPhoto = new File(phtotUrl);
                if (!userPhoto.exists()) {
                    try {
                        new ImageAsyncTack(photo).execute(phtotUrl);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(phtotUrl);
                        Bitmap bm = BitmapFactory.decodeStream(fileInputStream);
                        photo.setImageBitmap(bm);
                        Toast.makeText(PersonalActivity.this, "这是本地图片", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Intent intent = new Intent(PersonalActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        user_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceSexDialog();
            }
        });
        user_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePersonalNotes();
            }
        });
        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePersonalName();
            }
        });
        user_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePersonalPhote();
            }
        });
        ImageButton ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonalActivity.this.finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PICTURE:
                if (resultCode == RESULT_OK) {
                    tempUri = data.getData();
                    cutImage(tempUri);
                }
                break;
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(PersonalActivity.this,"com.example.kaixin.mycalendar.provider", tempFile);
                        tempUri = contentUri;
                        cutImage(tempUri);
                    } else {
                        tempUri = Uri.fromFile(tempFile);
                        cutImage(tempUri);
                    }
                }
                break;
            case CROP_SMALL_PICTURE:
                saveUri = ImageUtils.getSaveUri(PersonalActivity.this, UserUtils.getUserId(PersonalActivity.this));
                if (saveUri != null) {
                    setImageToView(saveUri);
                }
                break;
        }
    }

    protected void setImageToView(Uri uri) {
        Bitmap bitmap = ImageUtils.getBitmapFromUri(PersonalActivity.this, uri);
        if (bitmap != null) {
            photo.setImageBitmap(bitmap);
            ImageUtils.uploadImage(PersonalActivity.this, uri.getPath());
        }
    }

    class ImageAsyncTack extends AsyncTask<String, Void, Bitmap> {

        private ImageView photo;
        public ImageAsyncTack(ImageView photo) {
            this.photo = photo;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bm = null;
            BmobFile bmobFile = new BmobFile(userId + ".jpg", "", bmobUser.getUrlPic());
            ImageUtils.downloadImage(PersonalActivity.this, bmobFile);
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
            Toast.makeText(PersonalActivity.this, "这是下载的图片", Toast.LENGTH_SHORT).show();
        }
    }

    protected void cutImage(Uri uri) {
        Intent intent = ImageUtils.cutAndSaveImage(PersonalActivity.this, uri);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    private void updatePersonalPhote() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalActivity.this);
        builder.setTitle("头像信息");
        String[] items = {"相册", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case CHOOSE_PICTURE:
                        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        dialogInterface.dismiss();
                        break;
                    case TAKE_PICTURE:
                        try {
                            tempFile = new File(Environment.getExternalStorageDirectory().getPath(), String.valueOf(System.currentTimeMillis())+".jpg");
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                Uri contentUri = FileProvider.getUriForFile(PersonalActivity.this, "com.example.kaixin.mycalendar.provider", tempFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                            } else {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                            }
                            startActivityForResult(intent, TAKE_PICTURE);
                        } catch (Exception e) {
                            Toast.makeText(PersonalActivity.this, "相机无法启动，请先开启相机权限", Toast.LENGTH_SHORT).show();
                        }
                        dialogInterface.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();
    }
    private void choiceSexDialog() {
        final String[] sex_list = {"男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择性别");
        builder.setSingleChoiceItems(sex_list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UserUtils.UpdateUserSex(PersonalActivity.this, sex_list[i]);
                sex.setText(sex_list[i]);
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void updatePersonalNotes() {
        final EditText et_notes = new EditText(PersonalActivity.this);
        et_notes.setText(notes.getText().toString());
        et_notes.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("个性签名");
        builder.setView(et_notes);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserUtils.UpdateUserNotes(PersonalActivity.this, et_notes.getText().toString());
                notes.setText(et_notes.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void updatePersonalName() {
        final EditText et_name = new EditText(PersonalActivity.this);
        et_name.setText(name.getText().toString());
        et_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改昵称");
        builder.setView(et_name);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserUtils.UpdateUserName(PersonalActivity.this, et_name.getText().toString());
                name.setText(et_name.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void initViews() {
        /*quit = (Button)findViewById(R.id.quit);
        reset = (Button)findViewById(R.id.reset);*/
        user_name = (LinearLayout)findViewById(R.id.user_name);
        user_photo = (LinearLayout)findViewById(R.id.user_photo);
        user_sex = (LinearLayout)findViewById(R.id.user_sex);
        user_notes = (LinearLayout)findViewById(R.id.user_notes);
        name = (TextView)findViewById(R.id.name);
        sex = (TextView)findViewById(R.id.sex);
        notes = (TextView)findViewById(R.id.notes);
        email = (TextView)findViewById(R.id.email);
        photo = (ImageView)findViewById(R.id.photo);
    }
}