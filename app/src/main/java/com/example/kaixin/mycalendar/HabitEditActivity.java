package com.example.kaixin.mycalendar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Adapter.HabitAdapter;
import com.example.kaixin.mycalendar.Bean.Habit;
import com.example.kaixin.mycalendar.Utils.HabitUtils;
import com.example.kaixin.mycalendar.Utils.ImageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by kaixin on 2018/3/16.
 */

public class HabitEditActivity extends AppCompatActivity {

    private ImageButton ib_back, ib_save;
    private EditText habit_name, habit_notes;
    private ImageView habit_img;
    private static final int CHOOSE_PICTURE = 0;
    private static final int CROP_SMALL_PICTURE = 2;
    private String name, notes, img = "", img_name = "";
    private Habit habit;
    private Intent intent;
    private Uri saveUri;

    ProgressDialog progressDialog;
    public void createTask() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(HabitEditActivity.this);
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
                HabitEditActivity.this.finish();
            }
            @Override
            protected Void doInBackground(String... params) {
                if (saveUri != null) {
                    //uploadImage(saveUri.getPath());
                    final BmobFile bmobFile = new BmobFile(new File(saveUri.getPath()));
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                img = bmobFile.getFileUrl();
                                Log.i("Habit", "图片上传成功");
                                HabitUtils.createBmobHabit(HabitEditActivity.this, name, notes, img, img_name);
                            } else {
                                Toast.makeText(HabitEditActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    HabitUtils.createBmobHabit(HabitEditActivity.this, name, notes, img, img_name);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }
    public void updateTask() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(HabitEditActivity.this);
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
                HabitEditActivity.this.finish();
            }
            @Override
            protected Void doInBackground(String... params) {
                if (saveUri != null) {
                    //uploadImage(saveUri.getPath());
                    final BmobFile bmobFile = new BmobFile(new File(saveUri.getPath()));
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                img = bmobFile.getFileUrl();
                                Log.i("Habit", "图片上传成功");
                                HabitUtils.upadteBmobHabit(name, notes, img, img_name);
                            } else {
                                Toast.makeText(HabitEditActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    HabitUtils.createBmobHabit(HabitEditActivity.this, name, notes, img, img_name);
                }
                try {
                    Thread.sleep(5000);
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
        setContentView(R.layout.activity_habit_edit);
        initView();

        habit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HabitEditActivity.this.finish();
            }
        });

        ib_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(habit_name.getText().toString())) {
                    Toast.makeText(HabitEditActivity.this, "请输入任务名称", Toast.LENGTH_SHORT).show();
                } else if (habit != null){
                    name = habit_name.getText().toString();
                    notes = habit_notes.getText().toString();
                    updateTask();
                } else {
                    name = habit_name.getText().toString();
                    notes = habit_notes.getText().toString();
                    createTask();
                }
            }
        });
    }

    public void uploadImage(String path) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    img = bmobFile.getFileUrl();
                    Log.i("Habit", "图片上传成功");
                    //createTask();
                } else {
                    Toast.makeText(HabitEditActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void choosePicture() {
        Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
        openAlbumIntent.setType("image/*");
        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PICTURE:
                if (resultCode == RESULT_OK) cutImage(data.getData());
                break;
            case CROP_SMALL_PICTURE:
                if (saveUri != null) setImageToView(saveUri);
                break;
        }
    }
    protected void cutImage(Uri uri) {
        img_name =  String.valueOf(System.currentTimeMillis());
        saveUri = ImageUtils.getSaveUri(img_name);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 200);
        intent.putExtra("aspectY", 150);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    protected void setImageToView(Uri uri) {
        Bitmap bitmap = ImageUtils.getBitmapFromUri(HabitEditActivity.this, uri);
        if (bitmap != null) {
            habit_img.setImageBitmap(bitmap);
        }
    }
    public String saveImage(String name, Bitmap bitmap) {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(HabitEditActivity.this, "无法写入", Toast.LENGTH_SHORT).show();
        }
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }
    private void initView() {
        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_save = (ImageButton)findViewById(R.id.ib_save);
        habit_name = (EditText)findViewById(R.id.habit_name);
        habit_notes = (EditText)findViewById(R.id.habit_notes);
        habit_img = (ImageView)findViewById(R.id.habit_img);

        intent = getIntent();
        habit = (Habit)intent.getSerializableExtra("habit");
        if (habit != null) {
            habit_name.setText(habit.getHabitName());
            habit_notes.setText(habit.getHabitNotes());
            habit_img.setTag(habit.getHabitImg());
            if (!"".equals(habit.getHabitImgName())) {
                new HabitAdapter.ImageAsyncTack(habit_img).execute(habit.getHabitImgName());
            }
        }
    }
}
