package com.example.kaixin.mycalendar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.kaixin.mycalendar.AccountReportActivity;
import com.example.kaixin.mycalendar.Bean.AccountBill;
import com.example.kaixin.mycalendar.R;

import java.util.Date;
import java.util.List;

/**
 * Created by kaixin on 2018/2/9.
 */

public class AccountBillAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<Date> mGroup;
    private List<List<AccountBill>> mData;
    private String[] TYPES = new String[] {"支出", "收入"};
    private String[] LABLES = new String[] {"购物", "餐饮", "居住", "交通", "娱乐", "其他",
            "工资", "红包", "收益", "奖金", "报销", "其他"};

    public AccountBillAdapter(Context context, List<Date> group, List<List<AccountBill>> list) {
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
        ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_account_bill, null);
            holder = new ChildViewHolder();
            holder.type = (TextView)convertView.findViewById(R.id.type);
            holder.label = (TextView)convertView.findViewById(R.id.label);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.money = (TextView)convertView.findViewById(R.id.money);
            holder.notes = (TextView)convertView.findViewById(R.id.notes);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder)convertView.getTag();
        }
        String date = getChild(groupPosition, childPosition).getAccountDate().substring(5) + " ";
        String type = TYPES[getChild(groupPosition, childPosition).getAccountType()] + " ";
        String label = LABLES[getChild(groupPosition, childPosition).getAccountLabel()] + " ";
        String money = getChild(groupPosition, childPosition).getAccountMoney() + " 元";
        String notes = getChild(groupPosition, childPosition).getAccountNotes();
        holder.label.setText(label);
        holder.type.setText(type);
        holder.date.setText(date);
        holder.money.setText(money);
        if ("".equals(notes)) {
            holder.notes.setText("备注：无");
        } else {
            holder.notes.setText("备注：" + notes);
        }
        //holder.notes.setText("备注：" + getChild(groupPosition, childPosition).getAccountNotes());
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.group, null);
            holder = new GroupViewHolder();
            holder.groupto = (TextView)convertView.findViewById(R.id.groupto);
            holder.btn_report = (Button)convertView.findViewById(R.id.report);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        int year = mGroup.get(groupPosition).getYear() + 1900;
        int monthOfYear = mGroup.get(groupPosition).getMonth() + 1;
        String month = (monthOfYear < 10) ? ("0" + monthOfYear) : ("" + monthOfYear);
        final String year_month = year + "-" + month;
        holder.groupto.setText(year_month);
        holder.btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AccountReportActivity.class);
                List<AccountBill> temp = mData.get(groupPosition);
                double[] mon = new double[] {0,0,0,0,0,0,0,0,0,0,0,0};
                for (int i = 0; i < temp.size(); i++) {
                    AccountBill bill = temp.get(i);
                    mon[bill.getAccountLabel()] += bill.getAccountMoney();
                }
                intent.putExtra("group", year_month);
                intent.putExtra("mon", mon);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private class GroupViewHolder {
        TextView groupto;
        Button btn_report;
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
