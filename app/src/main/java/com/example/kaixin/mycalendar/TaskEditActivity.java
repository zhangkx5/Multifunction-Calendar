package com.example.kaixin.mycalendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.Task;
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
    private static final int TAKE_PICTURE = 1;
    private File tempFile;
    private static final int CROP_SMALL_PICTURE = 2;
    private Bitmap bitmap;
    private MyDatabaseHelper myDatabaseHelper;
    private String id, userId, name, notes, img = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        myDatabaseHelper = new MyDatabaseHelper(this);

        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_save = (ImageButton)findViewById(R.id.ib_save);
        task_name = (EditText)findViewById(R.id.task_name);
        task_notes = (EditText)findViewById(R.id.task_notes);
        task_img = (ImageView)findViewById(R.id.task_img);

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
                } else {
                    id = String.valueOf(System.currentTimeMillis());
                    userId = "unknown";
                    name = task_name.getText().toString();
                    notes = task_notes.getText().toString();

                    TaskUtils.createLocalTask(TaskEditActivity.this, id, "unknown", name, notes, img);

                    MyUser bmobUser = UserUtils.getCurrentUser();
                    if (bmobUser != null) {
                        String urlPic = uploadImage(img);
                        String userId = bmobUser.getObjectId();
                        Task bmobTask = new Task();
                        bmobTask.setUserId(userId);
                        bmobTask.setTaskId(id);
                        bmobTask.setTaskName(name);
                        bmobTask.setTaskNotes(notes);
                        bmobTask.setTaskImg(urlPic);
                        bmobTask.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(TaskEditActivity.this, "创建数据成功："+s, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(TaskEditActivity.this, "失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    TaskEditActivity.this.finish();
                }
            }
        });
    }

    public String uploadImage(String path) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(TaskEditActivity.this, "上传图片成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TaskEditActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return bmobFile.getFileUrl();
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
                if (data != null) {
                    setImageToView(data);
                }
                break;
        }
    }
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Toast.makeText(TaskEditActivity.this, "图片路径不存在", Toast.LENGTH_SHORT).show();
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
    protected void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            bitmap = bundle.getParcelable("data");
            if (bitmap != null) {
                task_img.setImageBitmap(bitmap);
                img = saveImage("123", bitmap);
                //uploadImage(img);
            }
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
}
