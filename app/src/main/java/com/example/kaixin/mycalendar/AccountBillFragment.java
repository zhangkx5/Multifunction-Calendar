package com.example.kaixin.mycalendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.kaixin.mycalendar.Bean.AccountBill;
import com.example.kaixin.mycalendar.Utils.MyDatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaixin on 2018/2/7.
 */

public class AccountBillFragment extends Fragment {

    private ListView listView;
    private AccountBillAdapter accountAdapter;
    private List<AccountBill> list;
    private MyDatabaseHelper myDatabaseHelper;
    private FloatingActionButton fab;
    private MyExpandableListViewAdapter expandableListViewAdapter;
    private ExpandableListView expandableListView;

    @Override
    public void onResume() {
        super.onResume();
        //list = readDB();
        //accountAdapter = new AccountBillAdapter(getActivity(), list);
        //listView.setAdapter(accountAdapter);
        Map<Date, List<AccountBill>> map = new HashMap<>();
        try {
            map = DivideIntoGroup(readDB());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<List<AccountBill>> lists = new ArrayList<>();
        for (Date date : getAllKey(map)) {
            List<AccountBill> l = map.get(date);
            lists.add(l);
        }
        expandableListViewAdapter = new MyExpandableListViewAdapter(
                getActivity(), getAllKey(map), lists);
        expandableListView.setAdapter(expandableListViewAdapter);
        //expandableListView.expandGroup(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_bill, null);
        myDatabaseHelper = new MyDatabaseHelper(getActivity());
        /*listView = (ListView)view.findViewById(R.id.listView);
        list = readDB();
        accountAdapter = new AccountBillAdapter(getContext(), list);
        listView.setAdapter(accountAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), AccountEditActivity.class);
                AccountBill account = accountAdapter.getItem(i);
                intent.putExtra("account", account);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("删除提醒");
                builder.setMessage("确定要删除吗？此操作不可逆！");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AccountBill account = accountAdapter.getItem(pos);
                        deleteInDB(account.getId());
                        list.remove(pos);
                        accountAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });*/

        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AccountEditActivity.class);
                startActivity(intent);
            }
        });

        Map<Date, List<AccountBill>> map = new HashMap<>();
        try {
            map = DivideIntoGroup(readDB());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<List<AccountBill>> lists = new ArrayList<>();
        for (Date date : getAllKey(map)) {
            List<AccountBill> l = map.get(date);
            lists.add(l);
        }
        expandableListViewAdapter = new MyExpandableListViewAdapter(
                getActivity(), getAllKey(map), lists);
        expandableListView = (ExpandableListView)view.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(expandableListViewAdapter);
        //expandableListView.expandGroup(0);

        return view;
    }
    public List<AccountBill> readDB() {
        List<AccountBill> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery(MyDatabaseHelper.ACCOUNT_TABLE_SELECT, null);
        while (cursor.moveToNext()) {
            String acid = cursor.getString(cursor.getColumnIndex("acid"));
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            int label = cursor.getInt(cursor.getColumnIndex("label"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            double money = cursor.getDouble(cursor.getColumnIndex("money"));
            String notes = cursor.getString(cursor.getColumnIndex("notes"));
            AccountBill account = new AccountBill(acid, type, label, date, money, notes);
            result.add(account);
        }
        /*List<AccountBill> all = new ArrayList<>();
        try {
            Map<Date, List<AccountBill>> map = DivideIntoGroup(result);
            List<Date> dateList = getAllKey(map);
            for (int i = 0; i < dateList.size(); i++) {
                all.addAll(map.get(dateList.get(i)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        return result;
    }
    public void deleteInDB(String id) {
        SQLiteDatabase dbDelete = myDatabaseHelper.getWritableDatabase();
        dbDelete.execSQL(MyDatabaseHelper.ACCOUNT_TABLE_DELETE, new Object[]{id});
        dbDelete.close();
    }

    public List<AccountBill> OrderList(List<AccountBill> result) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        for (int i = 0; i < result.size()-1; i++) {
            Date d1 = sdf.parse(result.get(i).getDate());
            for (int j = i+1; j < result.size(); j++) {
                Date d2 = sdf.parse(result.get(j).getDate());
                if (d1.before(d2)) {
                    AccountBill accountBill = result.get(i);
                    result.set(i, result.get(j));
                    result.set(j, accountBill);
                }
            }
        }
        return result;
    }

    public Map<Date, List<AccountBill>> DivideIntoGroup(List<AccountBill> result) throws ParseException {
        Map<Date, List<AccountBill>> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        for (int i = 0; i < result.size(); i++) {
            AccountBill accountBill = result.get(i);
            Date date = sdf.parse(accountBill.getDate());
            if (map.containsKey(date)) {
                map.get(date).add(accountBill);
            } else {
                List<AccountBill> grouplist = new ArrayList<AccountBill>();
                grouplist.add(accountBill);
                map.put(date, grouplist);
            }
        }
        return map;
    }

    public List<Date> getAllKey(Map<Date, List<AccountBill>> map) {
        List<Date> dateList = new ArrayList<>();
        for (Date key : map.keySet()) {
            dateList.add(key);
        }
        for (int i = 0; i < dateList.size()-1; i++) {
            for (int j = i+1; j < dateList.size(); j++) {
                Date d1 = dateList.get(i);
                Date d2 = dateList.get(j);
                if (d1.before(d2)) {
                    dateList.set(i, d2);
                    dateList.set(j, d1);
                }
            }
        }
        return dateList;
    }
}
