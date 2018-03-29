package com.example.kaixin.mycalendar.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.ClockingIn;
import com.example.kaixin.mycalendar.Bean.Task;
import com.example.kaixin.mycalendar.MyUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by kaixin on 2018/3/25.
 */

public class TaskUtils {

    private static MyDatabaseHelper myDatabaseHelper;
    public static final String TASK_TABLE_NAME = "task_table";
    public static final String TASK_TABLE_INSERT = "insert into " + TASK_TABLE_NAME
            + "(id, user_id, task_name, task_notes, task_img, img_name) values (?, ?, ?, ?, ?, ?)";
    public static final String TASK_TABLE_DELETE = "delete from " + TASK_TABLE_NAME + " where id = ?";
    //添加任务到本地数据库
    public static void createLocalTask(Context context, String id, String userid,
                                              String name, String notes, String img, String img_name) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(TASK_TABLE_INSERT, new Object[]{id, userid, name, notes, img, img_name});
        dbWrite.close();
        Log.i("DIARY", "createLocalTask成功："+id);
    }
    //修改本地数据库中的纪念日
    public static void updateLocalTask(Context context, String id, String name, String notes, String img, String img_name) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbUpdate = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("task_name", name);
        values.put("task_notes", notes);
        values.put("task_img", img);
        values.put("img_name", img_name);
        dbUpdate.update(TASK_TABLE_NAME, values, "id = ?", new String[] {id});
        dbUpdate.close();
    }
    //删除本地数据库中的某个纪念日
    public static void deleteLocalTask(Context context, String id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbDelete = myDatabaseHelper.getWritableDatabase();
        dbDelete.execSQL(TASK_TABLE_DELETE, new Object[]{id});
        dbDelete.close();
    }
    //查找本地数据库中的所有纪念日
    public static List<Task> queryAllLocalTask(Context context, String user_id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        List<Task> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ?";
        String[] selectionArgs = new String[]{user_id};
        Cursor cursor = dbRead.query(TASK_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("task_name"));
            String notes = cursor.getString(cursor.getColumnIndex("task_notes"));
            String img = cursor.getString(cursor.getColumnIndex("task_img"));
            String img_name = cursor.getString(cursor.getColumnIndex("img_name"));
            Task task = new Task();
            task.setObjectId(id);
            task.setUserId(user_id);
            task.setTaskName(name);
            task.setTaskNotes(notes);
            task.setTaskImg(img);
            task.setTaskImgName(img_name);
            result.add(task);
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(result);
        return result;
    }
    //添加任务到后端云
    public static String createBmobTask(final Context context, final String name, final String notes,
                                        final String img, final String img_name) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        final Task bmobTask = new Task();
        if (bmobUser != null) {
            final String userId = bmobUser.getObjectId();
            bmobTask.setUserId(userId);
            bmobTask.setTaskName(name);
            bmobTask.setTaskNotes(notes);
            bmobTask.setTaskImg(img);
            bmobTask.setTaskImgName(img_name);
            bmobTask.save(new SaveListener<String>() {
                @Override
                public void done(final String objectId, BmobException e) {
                    if (e == null) {
                        createLocalTask(context, objectId, userId, name, notes, img, img_name);
                        Log.i("Bmob_task", "创建bmob任务成功:" + objectId);
                    } else {
                        Log.i("Bmob_task", "创建bmob任务失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        } else {
            Log.i("Bmob_task", "创建bmob任务失败");
        }
        return bmobTask.getObjectId();
    }
    //修改后端云中的任务
    public static void upadteBmobTask(String id, String name, String notes, String img) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        Task bmobTask = new Task();
        if (bmobUser != null) {
            bmobTask.setTaskName(name);
            bmobTask.setTaskNotes(notes);
            bmobTask.setTaskImg(img);
            bmobTask.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("Bmob_task", "更新bmob任务成功");
                    } else {
                        Log.i("Bmob_task", "更新bmob任务失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }
    //删除后端云中的任务
    public static void deleteBmobTask(String id) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        Task bmobTask = new Task();
        if (bmobUser != null) {
            bmobTask.setObjectId(id);
            bmobTask.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("Bmob_task", "删除bmob任务成功");
                    } else {
                        Log.i("Bmob_task", "删除bmob任务失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }
    //查找后端云中的所有任务
    public static void queryAllBmobTask(final Context context) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<Task> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.setLimit(500);
            query.findObjects(new FindListener<Task>() {
                @Override
                public void done(final List<Task> list, BmobException e) {
                    if (e == null) {
                        Toast.makeText(context, "共"+list.size()+"则任务", Toast.LENGTH_SHORT).show();
                        for (Task bmobTask : list) {
                            createLocalTask(context, bmobTask.getObjectId(), bmobTask.getUserId(),
                                    bmobTask.getTaskName(), bmobTask.getTaskNotes(), bmobTask.getTaskImg(),
                                    bmobTask.getTaskImgName());
                        }
                        Log.i("TASK", "queryAllBmobTask成功："+list.size());
                    } else {
                        Toast.makeText(context, "失败："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    //打卡
    public static final String CLOCKINGIN_TABLE_NAME = "clocking_in_table";
    public static final String CLOCKINGIN_TABLE_INSERT = "insert into " + CLOCKINGIN_TABLE_NAME
            + "(id, user_id, task_id, date) values (?, ?, ?, ?)";
    //添加打卡纪录到本地数据库
    public static void createLocalClockingIn(Context context, String id, String userId,
                                       String taskId, String date) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(CLOCKINGIN_TABLE_INSERT, new Object[]{id, userId, taskId, date});
        dbWrite.close();
        Log.i("CLOCKINGIN", "createLocalClockingIn："+id);
    }
    //查找本地数据库中的某条打卡纪录
    public static boolean queryOneLocalClockingIn(Context context, String user_id, String task_id, String date) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ? and task_id = ? and date = ?";
        String[] selectionArgs = new String[]{user_id, task_id, date};
        Cursor cursor = dbRead.query(CLOCKINGIN_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.close();
            dbRead.close();
            return true;
        }
        cursor.close();
        dbRead.close();
        return false;
    }
    //查找本地数据库中某项任务的所有打卡纪录
    public static ArrayList<Map<String, Object>> queryAllLocalClockingIn(Context context, String user_id, String task_id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ? and task_id = ?";
        String[] selectionArgs = new String[]{user_id, task_id};
        Cursor cursor = dbRead.query(CLOCKINGIN_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            //String id = cursor.getString(cursor.getColumnIndex("id"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            /*ClockingIn clockingIn = new ClockingIn();
            clockingIn.setObjectId(id);
            clockingIn.setUserId(user_id);
            clockingIn.setTaskId(task_id);
            clockingIn.setDate(date);*/
            Map<String, Object> hashmap = new HashMap<String, Object>();
            hashmap.put("task", task_id);
            hashmap.put("date", date);
            result.add(hashmap);
            //result.add(clockingIn);
        }
        //Collections.reverse(result);
        cursor.close();
        dbRead.close();
        return result;
    }
    //添加打卡纪录到后端云
    public static String createBmobClockingIn(final Context context, final String taskId, final String date) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        final ClockingIn bmobClock = new ClockingIn();
        if (bmobUser != null) {
            final String userId = bmobUser.getObjectId();
            bmobClock.setUserId(userId);
            bmobClock.setTaskId(taskId);
            bmobClock.setDate(date);
            bmobClock.save(new SaveListener<String>() {
                @Override
                public void done(final String objectId, BmobException e) {
                    if (e == null) {
                        createLocalClockingIn(context, objectId, userId, taskId, date);
                        Log.i("Bmob_clock", "创建bmob打卡纪录成功:" + objectId);
                    } else {
                        Log.i("Bmob_clock", "创建bmob打卡纪录失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        } else {
            Log.i("Bmob_task", "创建bmob打卡纪录失败");
        }
        return bmobClock.getObjectId();
    }
    //查找后端云中某项任务的当天打卡纪录
    public static void queryOneBmobClockingIn(final Context context, String task_id, String date) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<ClockingIn> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.addWhereEqualTo("taskId", task_id);
            query.addWhereEqualTo("date", date);
            query.findObjects(new FindListener<ClockingIn>() {
                @Override
                public void done(final List<ClockingIn> list, BmobException e) {
                    if (e == null) {
                        for (ClockingIn bmobClock : list) {
                            createLocalClockingIn(context, bmobClock.getObjectId(), bmobClock.getUserId(),
                                    bmobClock.getTaskId(), bmobClock.getDate());
                        }
                        Log.i("ClockingIn", "queryAllBmobClockingIn成功："+list.size());
                    } else {
                        Toast.makeText(context, "失败："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    //查找后端云中某项任务的所有打卡纪录
    public static void queryAllBmobClockingIn(final Context context, String task_id) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<ClockingIn> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.addWhereEqualTo("taskId", task_id);
            query.setLimit(500);
            query.findObjects(new FindListener<ClockingIn>() {
                @Override
                public void done(final List<ClockingIn> list, BmobException e) {
                    if (e == null) {
                        Toast.makeText(context, "共"+list.size()+"则任务", Toast.LENGTH_SHORT).show();
                        for (ClockingIn bmobClock : list) {
                            createLocalClockingIn(context, bmobClock.getObjectId(), bmobClock.getUserId(),
                                    bmobClock.getTaskId(), bmobClock.getDate());
                        }
                        Log.i("ClockingIn", "queryAllBmobClockingIn："+list.size());
                    } else {
                        Toast.makeText(context, "失败："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
