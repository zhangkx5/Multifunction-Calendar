package com.example.kaixin.mycalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by kaixin on 2018/2/9.
 */

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Date> mGroup;
    private List<List<AccountBill>> mData;

    public MyExpandableListViewAdapter(Context context, List<Date> group, List<List<AccountBill>> list) {
        mContext = context;
        mData = list;
        mGroup = group;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public AccountBill getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_account_bill, null);
        }
        ChildViewHolder holder = new ChildViewHolder();
        holder.type = (TextView)convertView.findViewById(R.id.type);
        holder.label = (TextView)convertView.findViewById(R.id.label);
        holder.date = (TextView)convertView.findViewById(R.id.date);
        holder.money = (TextView)convertView.findViewById(R.id.money);
        holder.notes = (TextView)convertView.findViewById(R.id.notes);
        if (getChild(groupPosition, childPosition).getType() == 0) {
            holder.type.setText("支出");
        } else {
            holder.type.setText("收入");
        }
        switch (getChild(groupPosition, childPosition).getLabel()) {
            case 0:
                holder.label.setText("购物");
                break;
            case 1:
                holder.label.setText("餐饮");
                break;
            case 2:
                holder.label.setText("居住");
                break;
            case 3:
                holder.label.setText("交通");
                break;
            case 4:
                holder.label.setText("娱乐");
                break;
            case 5:
                holder.label.setText("其他");
                break;
            case 6:
                holder.label.setText("工资");
                break;
            case 7:
                holder.label.setText("红包");
                break;
            case 8:
                holder.label.setText("收益");
                break;
            case 9:
                holder.label.setText("奖金");
                break;
            case 10:
                holder.label.setText("报销");
                break;
            case 11:
                holder.label.setText("其他");
                break;
        }
        holder.date.setText("日期：" + getChild(groupPosition, childPosition).getDate());
        holder.money.setText("金额：" + getChild(groupPosition, childPosition).getMoney());
        holder.notes.setText("备注：" + getChild(groupPosition, childPosition).getNotes());
        return convertView;
    }

    private class ChildViewHolder {
        TextView type;
        TextView label;
        TextView date;
        TextView money;
        TextView notes;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mData.get(groupPosition).size();
    }

    @Override
    public List<AccountBill> getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group, null);
        }
        GroupViewHolder holder = new GroupViewHolder();
        holder.groupto = (TextView)convertView.findViewById(R.id.groupto);
        int year = mGroup.get(groupPosition).getYear() + 1900;
        int monthOfYear = mGroup.get(groupPosition).getMonth() + 1;
        String month = (monthOfYear < 10) ? ("0" + monthOfYear) : ("" + monthOfYear);
        holder.groupto.setText(year + "-" + month);
        return convertView;
    }

    private class GroupViewHolder {
        TextView groupto;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
