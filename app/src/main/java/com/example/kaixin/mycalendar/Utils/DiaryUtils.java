package com.example.kaixin.mycalendar.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.Diary;
import com.example.kaixin.mycalendar.Bean.MyUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by kaixin on 2018/3/24.
 */

public class DiaryUtils {

    private static MyDatabaseHelper myDatabaseHelper;
    public static final String DIARY_TABLE_NAME = "Table_Diary";
    public static final String DIARY_TABLE_INSERT = "insert or ignore into " + DIARY_TABLE_NAME
            + "(id, user_id, diary_date, diary_address, diary_weather, diary_title, diary_content) values (?, ?, ?, ?, ?, ?, ?)";
    public static final String DIARY_TABLE_DELETE = "delete from " + DIARY_TABLE_NAME + " where id = ?";
    //添加日记到本地数据库
    public static void createLocalDiary(Context context, String id, String userId, String date, String address,
                                       String weather, String title, String content) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(DIARY_TABLE_INSERT, new Object[]{id, userId, date, address, weather, title, content});
        dbWrite.close();
        Log.i("DIARY", "createLocalDiary 成功："+id);
    }
    //修改本地数据库中的日记
    public static void updateLocalDiary(Context context, String id, String title, String content) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbUpdate = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("diary_title", title);
        values.put("diary_content", content);
        dbUpdate.update(DIARY_TABLE_NAME, values, "id = ?", new String[] {id});
        dbUpdate.close();
        Log.i("DIARY", "updateLocalDiary 成功："+id);
    }
    //查找本地数据库中的所有日记
    public static List<Diary> queryAllLocalDiary(Context context, String user_id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        List<Diary> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ?";
        String[] selectionArgs = new String[]{user_id};
        Cursor cursor = dbRead.query(DIARY_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String diary_date = cursor.getString(cursor.getColumnIndex("diary_date"));
            String diary_address = cursor.getString(cursor.getColumnIndex("diary_address"));
            String diary_weather = cursor.getString(cursor.getColumnIndex("diary_weather"));
            String diary_title = cursor.getString(cursor.getColumnIndex("diary_title"));
            String diary_content = cursor.getString(cursor.getColumnIndex("diary_content"));
            Diary diary = new Diary();
            diary.setObjectId(id);
            diary.setUserId(user_id);
            diary.setDiaryDate(diary_date);
            diary.setDiaryAddress(diary_address);
            diary.setDiaryWeather(diary_weather);
            diary.setDiaryTitle(diary_title);
            diary.setDiaryContent(diary_content);
            result.add(diary);
            Log.i("DIARY", "queryAllLocalDiary 成功："+diary.getObjectId());
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(result);
        return result;
    }
    //删除本地数据库中的某则日记
    public static void deleteLocalDiary(Context context, String id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbDelete = myDatabaseHelper.getWritableDatabase();
        dbDelete.execSQL(DIARY_TABLE_DELETE, new Object[]{id});
        dbDelete.close();
        Log.i("DIARY", "deleteLocalDiary 成功："+id);
    }
    //添加日记到后端云
    public static String createBmobDiary(final Context context, final String date, final String address,
                                         final String weather, final String title, final String content) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        final Diary bmobDiary = new Diary();
        if (bmobUser != null) {
            final String userId = bmobUser.getObjectId();
            bmobDiary.setUserId(userId);
            bmobDiary.setDiaryAddress(address);
            bmobDiary.setDiaryDate(date);
            bmobDiary.setDiaryWeather(weather);
            bmobDiary.setDiaryTitle(title);
            bmobDiary.setDiaryContent(content);
            bmobDiary.save(new SaveListener<String>() {
                @Override
                public void done(final String objectId, BmobException e) {
                    if (e == null) {
                        createLocalDiary(context, objectId, userId, date, address, weather, title, content);
                        Log.i("DIARY", "createBmobDiary 成功:" + objectId);
                    } else {
                        Log.i("DIARY", "createBmobDiary 失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        } else {
            Log.i("DIARY", "createBmobDiary 失败");
        }
        return bmobDiary.getObjectId();
    }
    //修改后端云中的日记
    public static void upadteBmobDiary(String id, String title, String content) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        Diary bmobDiary = new Diary();
        if (bmobUser != null) {
            bmobDiary.setDiaryTitle(title);
            bmobDiary.setDiaryContent(content);
            bmobDiary.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("DIARY", "upadteBmobDiary 成功");
                    } else {
                        Log.i("DIARY", "upadteBmobDiary 失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }
    //删除后端云中的日记
    public static void deleteBmobDiary(String id) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        Diary bmobDiary = new Diary();
        if (bmobUser != null) {
            bmobDiary.setObjectId(id);
            bmobDiary.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("DIARY", "deleteBmobDiary 成功");
                    } else {
                        Log.i("DIARY", "deleteBmobDiary 失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }
    //查找后端云中的所有日记
    public static void queryAllBmobDiary(final Context context) {
        //final Context mContext = context;
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<Diary> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.setLimit(500);
            query.findObjects(new FindListener<Diary>() {
                @Override
                public void done(final List<Diary> list, BmobException e) {
                    if (e == null) {
                        Toast.makeText(context, "共"+list.size()+"则日记", Toast.LENGTH_SHORT).show();
                        for (Diary bmobDiary : list) {
                            createLocalDiary(context, bmobDiary.getObjectId(), bmobDiary.getUserId(),
                                    bmobDiary.getDiaryDate(), bmobDiary.getDiaryAddress(),
                                    bmobDiary.getDiaryWeather(), bmobDiary.getDiaryTitle(),
                                    bmobDiary.getDiaryContent());
                        }
                        Log.i("DIARY", "queryAllBmobDiary 成功："+list.size());
                    } else {
                        Log.i("DIARY", "queryAllBmobDiary 失败："+e.getMessage()+","+e.getErrorCode());
                        Toast.makeText(context, "失败："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
