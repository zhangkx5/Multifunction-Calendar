package com.example.kaixin.mycalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kaixin on 2018/2/8.
 */

public class AccountBillAdapter extends ArrayAdapter<AccountBill> {

    private int resourceId = R.layout.list_account_bill;
    private List<AccountBill> list;
    private Context context;
    public AccountBillAdapter(Context context, List<AccountBill> accountList) {
        super(context, R.layout.list_account_bill, accountList);
        this.context = context;
        this.list = accountList;
    }

    class ViewHolder {
        TextView ac_type;
        TextView ac_label;
        TextView ac_date;
        TextView ac_money;
        TextView ac_notes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        AccountBill account = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder.ac_type = (TextView)convertView.findViewById(R.id.type);
            viewHolder.ac_label = (TextView)convertView.findViewById(R.id.label);
            viewHolder.ac_date = (TextView)convertView.findViewById(R.id.date);
            viewHolder.ac_money = (TextView)convertView.findViewById(R.id.money);
            viewHolder.ac_notes = (TextView)convertView.findViewById(R.id.notes);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.ac_type.setText(""+account.getType());
        viewHolder.ac_label.setText(""+account.getLabel());
        viewHolder.ac_date.setText(account.getDate());
        viewHolder.ac_money.setText(account.getMoney() + "元");
        viewHolder.ac_notes.setText(account.getNotes());
        return convertView;
    }

    @Override
    public AccountBill getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }
    public List<AccountBill> OrderList(List<AccountBill> result) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        for (int i = 0; i < result.size()-1; i++) {
            for (int j = i+1; j < result.size(); j++) {
                Date d1 = sdf.parse(result.get(i).getDate());
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
