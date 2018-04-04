package com.example.kaixin.mycalendar.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.kaixin.mycalendar.Bean.AccountBill;
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
 * Created by kaixin on 2018/3/30.
 */

public class AccountBillUtils {
    private static MyDatabaseHelper myDatabaseHelper;
    public static final String ACCOUNT_TABLE_NAME = "Table_AccountBill";
    public static final String ACCOUNT_TABLE_INSERT = "insert or ignore into " + ACCOUNT_TABLE_NAME
            + " (id, user_id, bill_type, bill_label, bill_date, bill_money, bill_notes) values (?, ?, ?, ?, ?, ?, ?)";
    public static final String ACCOUNT_TABLE_DELETE = "delete from " + ACCOUNT_TABLE_NAME + " where id = ?";

    //添加账单到本地数据库
    public static void createLocalAccountBill(Context context, String id, String userId, int type, int label,
                                              String date, double money, String notes) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbWrite = myDatabaseHelper.getWritableDatabase();
        dbWrite.execSQL(ACCOUNT_TABLE_INSERT, new Object[]{id, userId, type, label, date, money, notes});
        dbWrite.close();
        Log.i("ACCOUNTBILL", "createLocalAccountBill 成功："+id);
    }
    //修改本地数据库中的账单
    public static void updateLocalAccountBill(Context context, String id, int type, int label,
                                              String date, double money, String notes) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbUpdate = myDatabaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bill_type", type);
        values.put("bill_label", label);
        values.put("bill_date", date);
        values.put("bill_money", money);
        values.put("bill_notes", notes);
        dbUpdate.update(ACCOUNT_TABLE_NAME, values, "id = ?", new String[] {id});
        dbUpdate.close();
        Log.i("ACCOUNTBILL", "updateLocalAccountBill成功："+id);
    }
    //查找本地数据库中的所有账单
    public static List<AccountBill> queryAllLocalAccountBill(Context context, String user_id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        List<AccountBill> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "user_id = ?";
        String[] selectionArgs = new String[]{user_id};
        Cursor cursor = dbRead.query(ACCOUNT_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            int bill_type = cursor.getInt(cursor.getColumnIndex("bill_type"));
            int bill_label = cursor.getInt(cursor.getColumnIndex("bill_label"));
            String bill_date = cursor.getString(cursor.getColumnIndex("bill_date"));
            double bill_money = cursor.getDouble(cursor.getColumnIndex("bill_money"));
            String bill_notes = cursor.getString(cursor.getColumnIndex("bill_notes"));
            AccountBill accountBill = new AccountBill();
            accountBill.setObjectId(id);
            accountBill.setUserId(user_id);
            accountBill.setAccountType(bill_type);
            accountBill.setAccountLabel(bill_label);
            accountBill.setAccountDate(bill_date);
            accountBill.setAccountMoney(bill_money);
            accountBill.setAccountNotes(bill_notes);
            result.add(accountBill);
            Log.i("ACCOUNTBILL", "queryAllLocalAccountBill 成功："+accountBill.getObjectId());
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(result);
        return result;
    }
    //删除本地数据库中的某则日记
    public static void deleteLocalAccountBill(Context context, String id) {
        myDatabaseHelper = new MyDatabaseHelper(context);
        SQLiteDatabase dbDelete = myDatabaseHelper.getWritableDatabase();
        dbDelete.execSQL(ACCOUNT_TABLE_DELETE, new Object[]{id});
        dbDelete.close();
        Log.i("ACCOUNTBILL", "deleteLocalAccountBill 成功："+id);
    }
    //添加账单到后端云
    public static String createBmobAccountBill(final Context context, final int type, final int label,
                                               final String date, final double money, final String notes) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        final AccountBill accountBill = new AccountBill();
        if (bmobUser != null) {
            final String userId = bmobUser.getObjectId();
            accountBill.setUserId(userId);
            accountBill.setAccountType(type);
            accountBill.setAccountLabel(label);
            accountBill.setAccountDate(date);
            accountBill.setAccountMoney(money);
            accountBill.setAccountNotes(notes);
            accountBill.save(new SaveListener<String>() {
                @Override
                public void done(final String objectId, BmobException e) {
                    if (e == null) {
                        createLocalAccountBill(context, objectId, userId, type, label, date, money, notes);
                        Log.i("ACCOUNTBILL", "createBmobAccountBill 成功:" + objectId);
                    } else {
                        Log.i("ACCOUNTBILL", "createBmobAccountBill 失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        } else {
            Log.i("ACCOUNTBILL", "createBmobAccountBill 失败");
        }
        return accountBill.getObjectId();
    }
    //修改后端云中的账单
    public static void upadteBmobAccountBill(String id, int type, int label,
                                             String date, double money, String notes) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        AccountBill accountBill = new AccountBill();
        if (bmobUser != null) {
            accountBill.setAccountType(type);
            accountBill.setAccountLabel(label);
            accountBill.setAccountDate(date);
            accountBill.setAccountMoney(money);
            accountBill.setAccountNotes(notes);
            accountBill.update(id, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("ACCOUNTBILL", "upadteBmobAccountBill 成功");
                    } else {
                        Log.i("ACCOUNTBILL", "upadteBmobAccountBill 失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }
    //删除后端云中的账单
    public static void deleteBmobAccountBill(String id) {
        MyUser bmobUser = UserUtils.getCurrentUser();
        AccountBill accountBill = new AccountBill();
        if (bmobUser != null) {
            accountBill.setObjectId(id);
            accountBill.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("ACCOUNTBILL", "deleteBmobAccountBill 成功");
                    } else {
                        Log.i("ACCOUNTBILL", "deleteBmobAccountBill 失败："+e.getMessage()+","+e.getErrorCode());
                    }
                }
            });
        }
    }
    //查找后端云中的所有日记
    public static List<AccountBill> queryAllBmobAccountBill(final Context context) {
        final List<AccountBill> alist = new ArrayList<AccountBill>();
        MyUser bmobUser = UserUtils.getCurrentUser();
        if (bmobUser != null) {
            BmobQuery<AccountBill> query = new BmobQuery<>();
            query.addWhereEqualTo("userId", bmobUser.getObjectId());
            query.setLimit(500);
            query.findObjects(new FindListener<AccountBill>() {
                @Override
                public void done(final List<AccountBill> list, BmobException e) {
                    if (e == null) {
                        Toast.makeText(context, "共"+list.size()+"条账单", Toast.LENGTH_SHORT).show();
                        for (AccountBill accountBill : list) {
                            alist.add(accountBill);
                            createLocalAccountBill(context, accountBill.getObjectId(), accountBill.getUserId(),
                                    accountBill.getAccountType(), accountBill.getAccountLabel(),
                                    accountBill.getAccountDate(), accountBill.getAccountMoney(),
                                    accountBill.getAccountNotes());
                        }
                        Log.i("ACCOUNTBILL", "queryAllBmobAccountBill 成功："+list.size());
                    } else {
                        Log.i("ACCOUNTBILL", "queryAllBmobAccountBill 失败："+e.getMessage());
                        Toast.makeText(context, "失败："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return alist;
    }
}
