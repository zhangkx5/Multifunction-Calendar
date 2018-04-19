package com.example.kaixin.mycalendar.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.ClockingIn;
import com.example.kaixin.mycalendar.Bean.Habit;
import com.example.kaixin.mycalendar.Bean.MyUser;

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

public class HabitUtils {

    private static MyDatabaseHelper myDatabaseHelper;
    public static final String HABIT_TABLE_NAME = "Table_Habit";
    public static final String HABIT_TABLE_INSERT = "insert or ignore into " + HABIT_TABLE_NAME
            + "(id, user_id, habit_name, habit_notes, habit_img, img_name) values (?, ?, ?, ?, ?, ?)";
    public static final String HABIT_TABLE_DELETE = "delete from " + HABIT_TABLE_NAME + " where id = ?";
    //添加任务到本地数据库
    public static void createLocalHabit(Context context, String id, String user_id,
                                              String name, String notes, String img, String img_name) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(HABIT_TABLE_INSERT, new Object[]{id, user_id, name, notes, img, img_name});
        dbWrite.close();
        Log.i("HABIT", "createLocalHabit 成功："+id);
    }
    //修改本地数据库中的习惯任务
    public static void updateLocalHabit(Context context, String id, String name, String notes, String img, String img_name) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbUpdate = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("habit_name", name);
        values.put("habit_notes", notes);
        values.put("habit_img", img);
        values.put("img_name", img_name);
        dbUpdate.update(HABIT_TABLE_NAME, values, "id = ?", new String[] {id});
        dbUpdate.close();
        Log.i("HABIT", "updateLocalHabit 成功："+id);
    }
    //删除本地数据库中的某个习惯任务
    public static void deleteLocalHabit(Context context, String id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbDelete = myDatabaseHelper.getWritableDatabase();
        dbDelete.execSQL(HABIT_TABLE_DELETE, new Object[]{id});
        dbDelete.close();
        Log.i("HABIT", "deleteLocalHabit 成功："+id);
    }
    //查找本地数据库中的所有习惯任务
    public static List<Habit> queryAllLocalHabit(Context context, String user_id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        List<Habit> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ?";
        String[] selectionArgs = new String[]{user_id};
        Cursor cursor = dbRead.query(HABIT_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("habit_name"));
            String notes = cursor.getString(cursor.getColumnIndex("habit_notes"));
            String img = cursor.getString(cursor.getColumnIndex("habit_img"));
            String img_name = cursor.getString(cursor.getColumnIndex("img_name"));
            Habit habit = new Habit();
            habit.setObjectId(id);
            habit.setUserId(user_id);
            habit.setHabitName(name);
            habit.setHabitNotes(notes);
            habit.setHabitImg(img);
            habit.setHabitImgName(img_name);
            result.add(habit);
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(result);
        Log.i("HABIT", "queryAllLocalHabit 成功");
        return result;
    }
    //添加习惯任务到后端云
    public static String createBmobHabit(final Context context, final String name, final String notes,
                                        final String img, final String img_name) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        final String id = String.valueOf(System.currentTimeMillis());
        final String userId = UserUtils.getUserId(context);
        final Habit bmobHabit = new Habit();
        if (bmobUser != null) {
            bmobHabit.setUserId(userId);
            bmobHabit.setHabitName(name);
            bmobHabit.setHabitNotes(notes);
            bmobHabit.setHabitImg(img);
            bmobHabit.setHabitImgName(img_name);
            bmobHabit.save(new SaveListener<String>() {
                @Override
                public void done(final String objectId, BmobException e) {
                    if (e == null) {
                        createLocalHabit(context, objectId, userId, name, notes, img, img_name);
                        Log.i("HABIT", "createBmobHabit 成功:" + objectId);
                    } else {
                        Log.i("HABIT", "createBmobHabit 失败："+e.getMessage()+","+e.getErrorCode());
                        createLocalHabit(context, id, userId, name, notes, img, img_name);
                    }
                }
            });
            return bmobHabit.getObjectId();
        } else {
            Log.i("HABIT", "创建bmob习惯任务失败");
            createLocalHabit(context, id, userId, name, notes, img, img_name);
        }
        return id;
    }
    //修改后端云中的习惯任务
    public static void upadteBmobHabit(String id, String name, String notes, String img) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        Habit bmobHabit = new Habit();
        if (bmobUser != null) {
            bmobHabit.setHabitName(name);
            bmobHabit.setHabitNotes(notes);
            bmobHabit.setHabitImg(img);
            bmobHabit.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("HABIT", "upadteBmobHabit 成功");
                    } else {
                        Log.i("HABIT", "upadteBmobHabit 失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        } else {
            Log.i("HABIT", "更新bmob习惯任务失败");
        }
    }
    //删除后端云中的习惯任务
    public static void deleteBmobHabit(String id) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        Habit bmobHabit = new Habit();
        if (bmobUser != null) {
            bmobHabit.setObjectId(id);
            bmobHabit.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("HABIT", "deleteBmobHabit 成功");
                    } else {
                        Log.i("HABIT", "删deleteBmobHabit 失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        } else {
            Log.i("HABIT", "删除bmob习惯任务失败");
        }
    }
    //查找后端云中的所有习惯任务
    public static List<Habit> queryAllBmobHabit(final Context context) {
        final List<Habit> alist = new ArrayList<Habit>();
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<Habit> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.setLimit(500);
            query.findObjects(new FindListener<Habit>() {
                @Override
                public void done(final List<Habit> list, BmobException e) {
                    if (e == null) {
                        for (Habit bmobHabit : list) {
                            alist.add(bmobHabit);
                            createLocalHabit(context, bmobHabit.getObjectId(), bmobHabit.getUserId(),
                                    bmobHabit.getHabitName(), bmobHabit.getHabitNotes(), bmobHabit.getHabitImg(),
                                    bmobHabit.getHabitImgName());
                        }
                        Log.i("HABIT", "queryAllBmobHabit 成功："+list.size());
                    } else {
                        Log.i("HABIT", "queryAllBmobHabit 失败："+e.getMessage());
                    }
                }
            });
        }
        return alist;
    }
    //打卡
    public static final String CLOCKINGIN_TABLE_NAME = "Table_ClockingIn";
    public static final String CLOCKINGIN_TABLE_INSERT = "insert or ignore into " + CLOCKINGIN_TABLE_NAME
            + "(id, user_id, habit_id, date) values (?, ?, ?, ?)";
    //添加打卡纪录到本地数据库
    public static void createLocalClockingIn(Context context, String id, String userId,
                                       String taskId, String date) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(CLOCKINGIN_TABLE_INSERT, new Object[]{id, userId, taskId, date});
        dbWrite.close();
        Log.i("CLOCKINGIN", "createLocalClockingIn 成功："+id);
    }
    //查找本地数据库中的某条打卡纪录
    public static boolean queryOneLocalClockingIn(Context context, String user_id, String habit_id, String date) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ? and habit_id = ? and date = ?";
        String[] selectionArgs = new String[]{user_id, habit_id, date};
        Cursor cursor = dbRead.query(CLOCKINGIN_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.close();
            dbRead.close();
            Log.i("CLOCKINGIN", "queryOneLocalClockingIn 成功");
            return true;
        }
        cursor.close();
        dbRead.close();
        Log.i("CLOCKINGIN", "queryOneLocalClockingIn 失败");
        return false;
    }
    //查找本地数据库中某项任务的所有打卡纪录
    public static List<String> queryAllLocalClockingIn(Context context, String user_id, String habit_id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        //ArrayList<Map<String, Object>> result = new ArrayList<>();
        List<String> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ? and habit_id = ?";
        String[] selectionArgs = new String[]{user_id, habit_id};
        Cursor cursor = dbRead.query(CLOCKINGIN_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            //String id = cursor.getString(cursor.getColumnIndex("id"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            result.add(date);
            /*ClockingIn clockingIn = new ClockingIn();
            clockingIn.setObjectId(id);
            clockingIn.setUserId(user_id);
            clockingIn.setTaskId(habit_id);
            clockingIn.setDate(date);*/
            /*Map<String, Object> hashmap = new HashMap<String, Object>();
            hashmap.put("habit", habit_id);
            hashmap.put("date", date);
            result.add(hashmap);*/
            //result.add(clockingIn);
        }
        Collections.reverse(result);
        cursor.close();
        dbRead.close();
        Log.i("CLOCKINGIN", "queryAllLocalClockingIn 成功:" + result.size());
        return result;
    }
    //添加打卡纪录到后端云
    public static String createBmobClockingIn(final Context context, final String habitId, final String date) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        final ClockingIn bmobClock = new ClockingIn();
        final String userId = UserUtils.getUserId(context);
        final String id = String.valueOf(System.currentTimeMillis());
        if (bmobUser != null) {
            bmobClock.setUserId(userId);
            bmobClock.setHabitId(habitId);
            bmobClock.setDate(date);
            bmobClock.save(new SaveListener<String>() {
                @Override
                public void done(final String objectId, BmobException e) {
                    if (e == null) {
                        createLocalClockingIn(context, objectId, userId, habitId, date);
                        Log.i("CLOCKINGIN", "createBmobClockingIn 成功:" + objectId);
                    } else {
                        Log.i("CLOCKINGIN", "createBmobClockingIn 失败："+e.getMessage()+","+e.getErrorCode());
                        createLocalClockingIn(context, id, userId, habitId, date);
                    }
                }
            });
            return bmobClock.getObjectId();
        } else {
            Log.i("CLOCKINGIN", "createBmobClockingIn 失败");
            createLocalClockingIn(context, id, userId, habitId, date);
        }
        return id;
    }
    //查找后端云中某项任务的当天打卡纪录
    public static void queryOneBmobClockingIn(final Context context, String habit_id, String date) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<ClockingIn> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.addWhereEqualTo("taskId", habit_id);
            query.addWhereEqualTo("date", date);
            query.findObjects(new FindListener<ClockingIn>() {
                @Override
                public void done(final List<ClockingIn> list, BmobException e) {
                    if (e == null) {
                        for (ClockingIn bmobClock : list) {
                            createLocalClockingIn(context, bmobClock.getObjectId(), bmobClock.getUserId(),
                                    bmobClock.getHabitId(), bmobClock.getDate());
                        }
                        Log.i("CLOCKINGIN", "queryOneBmobClockingIn 成功："+list.size());
                    } else {
                        Log.i("CLOCKINGIN", "queryOneBmobClockingIn 失败："+e.getMessage());
                    }
                }
            });
        } else {
            Log.i("CLOCKINGIN", "queryOneBmobClockingIn 失败");
        }
    }
    //查找后端云中某项任务的所有打卡纪录
    public static void queryAllBmobClockingIn(final Context context, String habit_id) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<ClockingIn> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.addWhereEqualTo("habitId", habit_id);
            query.setLimit(500);
            query.findObjects(new FindListener<ClockingIn>() {
                @Override
                public void done(final List<ClockingIn> list, BmobException e) {
                    if (e == null) {
                        for (ClockingIn bmobClock : list) {
                            createLocalClockingIn(context, bmobClock.getObjectId(), bmobClock.getUserId(),
                                    bmobClock.getHabitId(), bmobClock.getDate());
                        }
                        Log.i("CLOCKINGIN", "queryAllBmobClockingIn 成功："+list.size());
                    } else {
                        Log.i("CLOCKINGIN", "queryAllBmobClockingIn 失败："+e.getMessage());
                    }
                }
            });
        } else {
            Log.i("CLOCKINGIN", "queryAllBmobClockingIn 失败");
        }
    }
    //查找后端云中某项任务的所有打卡纪录
    public static void queryAllAllBmobClockingIn(final Context context) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<ClockingIn> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.setLimit(500);
            query.findObjects(new FindListener<ClockingIn>() {
                @Override
                public void done(final List<ClockingIn> list, BmobException e) {
                    if (e == null) {
                        for (ClockingIn bmobClock : list) {
                            createLocalClockingIn(context, bmobClock.getObjectId(), bmobClock.getUserId(),
                                    bmobClock.getHabitId(), bmobClock.getDate());
                        }
                        Log.i("CLOCKINGIN", "queryAllBmobClockingIn 成功："+list.size());
                    } else {
                        Log.i("CLOCKINGIN", "queryAllBmobClockingIn 失败："+e.getMessage());
                    }
                }
            });
        } else {
            Log.i("CLOCKINGIN", "queryAllBmobClockingIn 失败");
        }
    }
}
