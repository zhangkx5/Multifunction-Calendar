package com.example.kaixin.mycalendar.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.AnniversaryDay;
import com.example.kaixin.mycalendar.MyUser;

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

public class AnniversaryUtils {

    private static MyDatabaseHelper myDatabaseHelper;
    public static final String ANNIVERSARY_TABLE_NAME = "anniversary_table";
    public static final String ANNIVERSARY_TABLE_INSERT = "insert into " + ANNIVERSARY_TABLE_NAME
            + " (id, user_id, anniversary_name, anniversary_date, anniversary_notes) values (?, ? ,?, ?, ?)";
    public static final String ANNIVERSARY_TABLE_DELETE = "delete from " + ANNIVERSARY_TABLE_NAME + " where id = ?";
    //添加纪念日到本地数据库
    public static void createLocalAnniversary(Context context, String id, String userid,
                                              String name, String date, String notes) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(ANNIVERSARY_TABLE_INSERT, new Object[]{id, userid, name, date, notes});
        dbWrite.close();
        Log.i("Anniversary", "createLocalAnniversary："+id);
    }
    //修改本地数据库中的纪念日
    public static void updateLocalAnniversary(Context context, String id, String name, String date, String notes) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbUpdate = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("anniversary_name", name);
        values.put("anniversary_date", date);
        values.put("anniversary_notes", notes);
        dbUpdate.update(ANNIVERSARY_TABLE_NAME, values, "id = ?", new String[] {id});
        dbUpdate.close();
    }
    //删除本地数据库中的某个纪念日
    public static void deleteLocalAnniversary(Context context, String id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbDelete = myDatabaseHelper.getWritableDatabase();
        dbDelete.execSQL(ANNIVERSARY_TABLE_DELETE, new Object[]{id});
        dbDelete.close();
    }
    //查找本地数据库中的所有纪念日
    public static List<AnniversaryDay> queryAllLocalAnniversary(Context context, String user_id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        List<AnniversaryDay> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ?";
        String[] selectionArgs = new String[]{user_id};
        Cursor cursor = dbRead.query(ANNIVERSARY_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String anniversary_name = cursor.getString(cursor.getColumnIndex("anniversary_name"));
            String anniversary_date = cursor.getString(cursor.getColumnIndex("anniversary_date"));
            String anniversary_notes = cursor.getString(cursor.getColumnIndex("anniversary_notes"));
            AnniversaryDay anniversaryDay = new AnniversaryDay();
            anniversaryDay.setObjectId(id);
            anniversaryDay.setUserId(user_id);
            anniversaryDay.setAnniversaryName(anniversary_name);
            anniversaryDay.setAnniversaryDate(anniversary_date);
            anniversaryDay.setAnniversaryNotes(anniversary_notes);
            result.add(anniversaryDay);
        }
        Collections.reverse(result);
        return result;
    }
    //添加纪念日到后端云
    public static AnniversaryDay createBmobAnniversary(final Context context, final String date, final String name, final String notes) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        AnniversaryDay anniversaryDay = new AnniversaryDay();
        if (bmobUser != null) {
            final String userId = bmobUser.getObjectId();
            anniversaryDay.setUserId(userId);
            anniversaryDay.setAnniversaryDate(date);
            anniversaryDay.setAnniversaryName(name);
            anniversaryDay.setAnniversaryNotes(notes);
            anniversaryDay.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        createLocalAnniversary(context, objectId, userId, date, name, notes);
                        Log.i("Bmob_anniversaryDay", "创建bmob纪念日成功:" + objectId);
                    } else {
                        Log.i("Bmob_anniversaryDay", "创建bmob纪念日失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        } else {
            Log.i("Bmob_anniversaryDay", "创建bmob纪念日失败");
        }
        return anniversaryDay;
    }
    //修改后端云中的纪念日
    public static void upadteBmobAnniversaryDay(String id, String name, String date, String notes) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        AnniversaryDay anniversaryDay = new AnniversaryDay();
        if (bmobUser != null) {
            anniversaryDay.setAnniversaryDate(date);
            anniversaryDay.setAnniversaryName(name);
            anniversaryDay.setAnniversaryNotes(notes);
            anniversaryDay.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("Bmob_anniversaryDay", "更新bmob纪念日成功");
                    } else {
                        Log.i("Bmob_anniversaryDay", "更新bmob纪念日失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }
    //删除后端云中的纪念日
    public static void deleteBmobAnniversaryDay(String id) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        AnniversaryDay anniversaryDay = new AnniversaryDay();
        if (bmobUser != null) {
            anniversaryDay.setObjectId(id);
            anniversaryDay.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("Bmob_anniversaryDay", "删除bmob纪念日成功");
                    } else {
                        Log.i("Bmob_anniversaryDay", "删除bmob纪念日失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }
    //查找后端云中的所有纪念日
    public static void queryAllBmobAnniversaryDay(final Context context) {
        final Context mContext = context;
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<AnniversaryDay> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.setLimit(500);
            query.findObjects(new FindListener<AnniversaryDay>() {
                @Override
                public void done(final List<AnniversaryDay> list, BmobException e) {
                    if (e == null) {
                        Toast.makeText(mContext, "共"+list.size()+"则纪念日", Toast.LENGTH_SHORT).show();
                        for (AnniversaryDay bmobAnniversaryDay : list) {
                            createLocalAnniversary(context, bmobAnniversaryDay.getObjectId(), bmobAnniversaryDay.getUserId(),
                                    bmobAnniversaryDay.getAnniversaryDate(), bmobAnniversaryDay.getAnniversaryName(),
                                    bmobAnniversaryDay.getAnniversaryNotes());
                        }
                    } else {
                        Toast.makeText(mContext, "失败："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
