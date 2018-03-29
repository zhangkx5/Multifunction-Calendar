package com.example.kaixin.mycalendar.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.kaixin.mycalendar.PersonalActivity;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by kaixin on 2018/3/28.
 */

public class ImageUtils {
    public static Intent cutAndSaveImage(Context context, Uri uri) {
        if (uri == null) {
            Toast.makeText(context, "图片路径不存在", Toast.LENGTH_SHORT).show();
        }
        int dp = 500;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("aspectX", 99998);
        intent.putExtra("aspectY", 99999);
        intent.putExtra("outputX", dp);
        intent.putExtra("outputY", dp);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getSaveUri(UserUtils.getUserId(context)));
        return intent;
    }

    public static Uri getSaveUri(String img_name) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath()+"/mycalendar");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = img_name + ".jpg";
        return Uri.fromFile(new File(appDir, fileName));
    }
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void uploadImage(final Context context, String path) {
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    UserUtils.UpdateUserUriPic(context, bmobFile.getFileUrl());
                } else {
                    Toast.makeText(context, "上传失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void downloadImage(BmobFile file) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath()+"/mycalendar");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File saveFile = new File(appDir, file.getFilename());
        file.download(saveFile, new DownloadFileListener() {
            @Override
            public void onStart() {}
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.i("下载成功", "保存路径："+s);
                } else {
                    Log.i("下载失败", e.getErrorCode()+","+e.getMessage());
                }
            }
            @Override
            public void onProgress(Integer integer, long l) {}
        });
    }
}
