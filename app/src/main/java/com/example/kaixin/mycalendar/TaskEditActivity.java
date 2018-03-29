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

import com.example.kaixin.mycalendar.Bean.Task;
import com.example.kaixin.mycalendar.Utils.ImageUtils;
import com.example.kaixin.mycalendar.Utils.MyDatabaseHelper;
import com.example.kaixin.mycalendar.Utils.TaskUtils;
import com.example.kaixin.mycalendar.Utils.UserUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by kaixin on 2018/3/16.
 */

public class TaskEditActivity extends AppCompatActivity {

    private ImageButton ib_back, ib_save;
    private EditText task_name, task_notes;
    private ImageView task_img;
    private static final int CHOOSE_PICTURE = 0;
    private static final int CROP_SMALL_PICTURE = 2;
    private Bitmap bitmap;
    private String name, notes, img = "", img_name = "";
    private Task task;
    private Intent intent;
    private Uri saveUri;

    ProgressDialog progressDialog;
    public void createTask() {
        new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(TaskEditActivity.this);
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
                TaskEditActivity.this.finish();
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
                                Log.i("Task", "图片上传成功");
                                TaskUtils.createBmobTask(TaskEditActivity.this, name, notes, img, img_name);
                            } else {
                                Toast.makeText(TaskEditActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    TaskUtils.createBmobTask(TaskEditActivity.this, name, notes, img, img_name);
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
                progressDialog = new ProgressDialog(TaskEditActivity.this);
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
                TaskEditActivity.this.finish();
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
                                Log.i("Task", "图片上传成功");
                                TaskUtils.createBmobTask(TaskEditActivity.this, name, notes, img, img_name);
                            } else {
                                Toast.makeText(TaskEditActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    TaskUtils.createBmobTask(TaskEditActivity.this, name, notes, img, img_name);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        initView();

        task_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TaskEditActivity.this.finish();
            }
        });

        ib_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(task_name.getText().toString())) {
                    Toast.makeText(TaskEditActivity.this, "请输入任务名称", Toast.LENGTH_SHORT).show();
                } else if (task != null){
                    name = task_name.getText().toString();
                    notes = task_notes.getText().toString();
                    updateTask();
                } else {
                    name = task_name.getText().toString();
                    notes = task_notes.getText().toString();
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
                    Log.i("Task", "图片上传成功");
                    //createTask();
                } else {
                    Toast.makeText(TaskEditActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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
                if (resultCode == RESULT_OK) {
                    cutImage(data.getData());
                }
                break;
            case CROP_SMALL_PICTURE:
                if (saveUri != null) {
                    setImageToView(saveUri);
                }
                break;
        }
    }
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Toast.makeText(TaskEditActivity.this, "图片路径不存在", Toast.LENGTH_SHORT).show();
        }
        int dp = 500;
        img_name =  String.valueOf(System.currentTimeMillis());
        saveUri = ImageUtils.getSaveUri(TaskEditActivity.this, img_name);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 200);
        intent.putExtra("aspectY", 150);
        intent.putExtra("outputX", dp);
        intent.putExtra("outputY", dp);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    protected void setImageToView(Uri uri) {
        Bitmap bitmap = ImageUtils.getBitmapFromUri(TaskEditActivity.this, uri);
        if (bitmap != null) {
            task_img.setImageBitmap(bitmap);
        }
    }
    public String saveImage(String name, Bitmap bitmap) {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(TaskEditActivity.this, "无法写入", Toast.LENGTH_SHORT).show();
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
        task_name = (EditText)findViewById(R.id.task_name);
        task_notes = (EditText)findViewById(R.id.task_notes);
        task_img = (ImageView)findViewById(R.id.task_img);

        intent = getIntent();
        task = (Task)intent.getSerializableExtra("task");
        if (task != null) {
            task_name.setText(task.getTaskName());
            task_notes.setText(task.getTaskNotes());

        }
    }
}
