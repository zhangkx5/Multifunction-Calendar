package com.example.kaixin.mycalendar.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.Task;
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
 * Created by kaixin on 2018/3/25.
 */

public class TaskUtils {

    private static MyDatabaseHelper myDatabaseHelper;
    public static final String TASK_TABLE_NAME = "task_table";
    public static final String TASK_TABLE_INSERT = "insert into " + TASK_TABLE_NAME
            + "(id, user_id, task_name, task_notes, task_img) values (?, ?, ?, ?, ?)";
    public static final String TASK_TABLE_SELECT = "select * from " + TASK_TABLE_NAME;
    public static final String TASK_TABLE_DELETE = "delete from " + TASK_TABLE_NAME + " where id = ?";
    //添加任务到本地数据库
    public static void createLocalTask(Context context, String id, String userid,
                                              String name, String notes, String img) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(TASK_TABLE_INSERT, new Object[]{id, userid, name, notes, img});
        dbWrite.close();
        Log.i("DIARY", "createLocalTask成功："+id);
    }
    //修改本地数据库中的纪念日
    public static void updateLocalTask(Context context, String id, String name, String notes, String img) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbUpdate = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("task_name", name);
        values.put("task_notes", notes);
        values.put("task_img", img);
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
    public static List<Task> queryAllLocalTask(Context context) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        List<Task> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery(TASK_TABLE_SELECT, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
            String name = cursor.getString(cursor.getColumnIndex("task_name"));
            String notes = cursor.getString(cursor.getColumnIndex("task_notes"));
            String img = cursor.getString(cursor.getColumnIndex("task_img"));
            Task task = new Task(id, user_id, name, notes, img);
            result.add(task);
        }
        Collections.reverse(result);
        return result;
    }
    //添加任务到后端云
    public static String createBmobTask(final Context context, final String name, final String notes, final String img) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        final Task bmobTask = new Task();
        if (bmobUser != null) {
            final String userId = bmobUser.getObjectId();
            bmobTask.setUserId(userId);
            bmobTask.setTaskName(name);
            bmobTask.setTaskNotes(notes);
            bmobTask.setTaskImg(img);
            bmobTask.save(new SaveListener<String>() {
                @Override
                public void done(final String objectId, BmobException e) {
                    if (e == null) {
                        createLocalTask(context, objectId, userId, name, notes, img);
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
        //final Context mContext = context;
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<Task> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.setLimit(500);
            query.findObjects(new FindListener<Task>() {
                @Override
                public void done(final List<Task> list, BmobException e) {
                    if (e == null) {
                        Toast.makeText(context, "共"+list.size()+"则日记", Toast.LENGTH_SHORT).show();
                        for (Task bmobTask : list) {
                            createLocalTask(context, bmobTask.getObjectId(), bmobTask.getUserId(),
                                    bmobTask.getTaskName(), bmobTask.getTaskNotes(), bmobTask.getTaskImg());
                        }
                        Log.i("TASK", "queryAllBmobTask成功："+list.size());
                    } else {
                        Toast.makeText(context, "失败："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
