package com.example.kaixin.mycalendar.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.kaixin.mycalendar.MyUser;
import com.example.kaixin.mycalendar.Bean.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by kaixin on 2018/3/25.
 */

public class ScheduleUtils {
    private static MyDatabaseHelper myDatabaseHelper;
    public static final String SCHEDULE_TABLE_NAME = "schedule_table";
    public static final String SCHEDULE_TABLE_INSERT = "insert into " + SCHEDULE_TABLE_NAME
            + " (id, user_id, schedule_title, schedule_address, schedule_start, schedule_end, schedule_call, schedule_notes) values (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SCHEDULE_TABLE_DELETE = "delete from " + SCHEDULE_TABLE_NAME + " where id = ?";
    //添加日程管理到本地数据库
    public static void createLocalSchedule(Context context, String id, String userid, String title,
                                           String address, String start, String end, String call, String notes) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(SCHEDULE_TABLE_INSERT, new Object[]{id, userid, title, address, start, end, call, notes});
        dbWrite.close();
        Log.i("Bmob_schedule", "createLocalSchedule成功");
    }
    //修改本地数据库中的日程管理
    public static void updateLocalSchedule(Context context, String id, String title, String address,
                                           String start, String end, String call, String notes) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbUpdate = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("schedule_title", title);
        values.put("schedule_address", address);
        values.put("schedule_start", start);
        values.put("schedule_end", end);
        values.put("schedule_call", call);
        values.put("schedule_notes", notes);
        dbUpdate.update(SCHEDULE_TABLE_NAME, values, "id = ?", new String[] {id});
        dbUpdate.close();
    }
    //删除本地数据库中的某个日程管理
    public static void deleteLocalSchedule(Context context, String id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbDelete = myDatabaseHelper.getWritableDatabase();
        dbDelete.execSQL(SCHEDULE_TABLE_DELETE, new Object[]{id});
        dbDelete.close();
    }
    //查找本地数据库中的所有日程管理
    public static List<Schedule> queryAllLocalSchedule(Context context, String user_id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        List<Schedule> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ?";
        String[] selectionArgs = new String[]{user_id};
        Cursor cursor = dbRead.query(SCHEDULE_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("schedule_title"));
            String address = cursor.getString(cursor.getColumnIndex("schedule_address"));
            String start = cursor.getString(cursor.getColumnIndex("schedule_start"));
            String end = cursor.getString(cursor.getColumnIndex("schedule_end"));
            String call = cursor.getString(cursor.getColumnIndex("schedule_call"));
            String notes = cursor.getString(cursor.getColumnIndex("schedule_notes"));
            Schedule schedule = new Schedule();
            schedule.setObjectId(id);
            schedule.setUserId(user_id);
            schedule.setScheduleTitle(title);
            schedule.setScheduleAddress(address);
            schedule.setScheduleStart(start);
            schedule.setScheduleEnd(end);
            schedule.setScheduleCall(call);
            schedule.setScheduleNotes(notes);
            result.add(schedule);
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(result);
        return result;
    }
    //添加日程管理到后端云
    public static Schedule createBmobSchedule(final Context context, final String title, final String address,
                                              final String start, final String end, final String call, final String notes) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        Schedule schedule = new Schedule();
        if (bmobUser != null) {
            final String userid = bmobUser.getObjectId();
            schedule.setUserId(userid);
            schedule.setScheduleTitle(title);
            schedule.setScheduleAddress(address);
            schedule.setScheduleStart(start);
            schedule.setScheduleEnd(end);
            schedule.setScheduleCall(call);
            schedule.setScheduleNotes(notes);
            schedule.save(new SaveListener<String>() {
                @Override
                public void done(String objectId, BmobException e) {
                    if (e == null) {
                        createLocalSchedule(context, objectId, userid, title, address, start, end, call, notes);
                        Log.i("Bmob_schedule", "创建bmob日程管理成功");
                    } else {
                        Log.i("Bmob_schedule", "创建bmob日程管理失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        } else {
            Log.i("Bmob_schedule", "创建bmob日程管理失败");
        }
        return schedule;
    }
    //修改后端云中的日程管理
    public static void upadteBmobSchedule(String id, String title, String address,
                                          String start, String end, String call, String notes) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        Schedule schedule = new Schedule();
        if (bmobUser != null) {
            schedule.setScheduleTitle(title);
            schedule.setScheduleAddress(address);
            schedule.setScheduleStart(start);
            schedule.setScheduleEnd(end);
            schedule.setScheduleCall(call);
            schedule.setScheduleNotes(notes);
            schedule.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("Bmob_schedule", "更新bmob日程管理成功");
                    } else {
                        Log.i("Bmob_schedule", "更新bmob日程管理失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }
    //删除后端云中的纪念日
    public static void deleteBmobSchedule(String id) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        Schedule schedule = new Schedule();
        if (bmobUser != null) {
            schedule.setObjectId(id);
            schedule.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("Bmob_schedule", "删除bmob日程管理成功");
                    } else {
                        Log.i("Bmob_schedule", "删除bmob日程管理失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }
    //查找后端云中的所有日程管理
    public static void queryAllBmobSchedule(final Context context) {
        final Context mContext = context;
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<Schedule> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.setLimit(500);
            query.findObjects(new FindListener<Schedule>() {
                @Override
                public void done(final List<Schedule> list, BmobException e) {
                    if (e == null) {
                        Toast.makeText(mContext, "共"+list.size()+"则日程管理", Toast.LENGTH_SHORT).show();
                        for (Schedule bmobSchedule : list) {
                            createLocalSchedule(context, bmobSchedule.getObjectId(), bmobSchedule.getUserId(),
                                    bmobSchedule.getScheduleTitle(), bmobSchedule.getScheduleAddress(),
                                    bmobSchedule.getScheduleStart(), bmobSchedule.getScheduleEnd(),
                                    bmobSchedule.getScheduleCall(), bmobSchedule.getScheduleNotes());
                        }
                    } else {
                        Toast.makeText(mContext, "失败："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
