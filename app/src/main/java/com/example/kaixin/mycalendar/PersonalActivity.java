package com.example.kaixin.mycalendar;

import android.app.Dialog;
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
    private Button quit, reset;
    private LinearLayout user_name, user_photo, user_sex, user_notes;
    private static final int CHOOSE_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private File tempFile;
    private static final int CROP_SMALL_PICTURE = 2;
    private MyUser bmobUser;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        quit = (Button)findViewById(R.id.quit);
        reset = (Button)findViewById(R.id.reset);
        user_name = (LinearLayout)findViewById(R.id.user_name);
        user_photo = (LinearLayout)findViewById(R.id.user_photo);
        user_sex = (LinearLayout)findViewById(R.id.user_sex);
        user_notes = (LinearLayout)findViewById(R.id.user_notes);
        name = (TextView)findViewById(R.id.name);
        sex = (TextView)findViewById(R.id.sex);
        notes = (TextView)findViewById(R.id.notes);
        email = (TextView)findViewById(R.id.email);
        photo = (ImageView)findViewById(R.id.photo);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                sex.setText("");
                email.setText("");
                notes.setText("");
                photo.setImageResource(R.mipmap.ic_user);
                BmobUser.logOut();
                BmobUser currentUser = BmobUser.getCurrentUser();
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
                        }/* else if (TextUtils.isEmpty(confirmPassword.getText().toString())) {
                            Toast.makeText(PersonalActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
                        } else if (!(newPassword.getText().toString().equals(confirmPassword.getText().toString()))) {
                            Toast.makeText(PersonalActivity.this, "密码输入不一致", Toast.LENGTH_SHORT).show();
                        } */else {
                            final String oldpwd = oldPassword.getText().toString();
                            final String newpwd = newPassword.getText().toString();

                            bmobUser = BmobUser.getCurrentUser(MyUser.class);
                            BmobUser.updateCurrentUserPassword(oldpwd, newpwd, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(PersonalActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PersonalActivity.this, oldpwd+"\n"+newpwd+"\n"+"密码修改失败："+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
        bmobUser = BmobUser.getCurrentUser(MyUser.class);
        if (bmobUser != null) {
            name.setText(bmobUser.getUsername());
            email.setText(bmobUser.getEmail());
            sex.setText(bmobUser.getSex());
            notes.setText(bmobUser.getNotes());
            userId = bmobUser.getObjectId();
            String phtotUrl = Environment.getExternalStorageDirectory().getPath() + "/mycalendar/" + userId + ".jpg";
            File userPhoto = new File(phtotUrl);
            if (!userPhoto.exists()) {
                if (bmobUser.getUrlPic() != "") {
                    new ImageAsyncTack(photo).execute(phtotUrl);
                    /*PhotoAsyncTask photoAsyncTack = new PhotoAsyncTask();
                    photoAsyncTack.execute(phtotUrl);
                    /*BmobFile bmobFile = new BmobFile(userId + ".jpg", "", bmobUser.getUrlPic());
                    downloadImage(bmobFile);
                    try {
                        FileInputStream fileInputStream = new FileInputStream(phtotUrl);
                        Bitmap bm = BitmapFactory.decodeStream(fileInputStream);
                        photo.setImageBitmap(bm);
                        Toast.makeText(PersonalActivity.this, "这是下载的图片", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
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

    public void downloadImage(BmobFile file) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath()+"/mycalendar");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File saveFile = new File(appDir, file.getFilename());
        file.download(saveFile, new DownloadFileListener() {
            @Override
            public void onStart() {
                Toast.makeText(PersonalActivity.this, "开始下载...", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(PersonalActivity.this, "下载成功，保存路径："+s, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonalActivity.this, "下载失败："+e.getErrorCode()+","+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {

            }
        });
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
                            tempFile = new File(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis()+".jpg");
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
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
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PICTURE:
                if (resultCode == RESULT_OK) {
                    cutImage(data.getData());
                }
                break;
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(PersonalActivity.this,"com.example.kaixin.mycalendar.provider", tempFile);
                        cutImage(contentUri);
                    } else {
                        cutImage(Uri.fromFile(tempFile));
                    }
                }
                break;
            case CROP_SMALL_PICTURE:
                if (data != null) {
                    setImageToView(data);
                }
                break;
        }
    }

    protected void cutImage(Uri uri) {
        if (uri == null) {
            Toast.makeText(PersonalActivity.this, "图片路径不存在", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        /*intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        mCropImageFile = getmCropImageFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);*/
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    private File getmCropImageFile(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"temp.jpg");
            File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            return file;
        }
        return null;
    }
    protected void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            if (bitmap != null) {
                photo.setImageBitmap(bitmap);
                String path = saveImage(userId, bitmap);
                uploadImage(path);
            }
        }
    }
    public String saveImage(String name, Bitmap bitmap) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath()+"/mycalendar");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }
    public void uploadImage(String path) {
        //String picPath = Environment.getExternalStorageDirectory().getPath() + "/mycalendar/head.jpg";
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    MyUser newUser = new MyUser();
                    newUser.setUrlPic(bmobFile.getFileUrl());
                    newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(PersonalActivity.this, "更新信息成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PersonalActivity.this, "更新信息失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(PersonalActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void choiceSexDialog() {
        final String[] sex_list = {"男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择性别");
        builder.setSingleChoiceItems(sex_list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyUser newUser = new MyUser();
                newUser.setSex(sex_list[i]);
                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(PersonalActivity.this, "更新信息成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PersonalActivity.this, "更新信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                MyUser newUser = new MyUser();
                newUser.setNotes(et_notes.getText().toString());
                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(PersonalActivity.this, "更新信息成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PersonalActivity.this, "更新信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
                MyUser newUser = new MyUser();
                newUser.setNotes(et_name.getText().toString());
                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(PersonalActivity.this, "更新信息成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PersonalActivity.this, "更新信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                name.setText(et_name.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
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
            downloadImage(bmobFile);
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
}