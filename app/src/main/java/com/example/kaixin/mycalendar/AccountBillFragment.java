package com.example.kaixin.mycalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Adapter.AccountAdapter;
import com.example.kaixin.mycalendar.Adapter.AccountBillAdapter;
import com.example.kaixin.mycalendar.Bean.AccountBill;
import com.example.kaixin.mycalendar.Utils.AccountBillUtils;
import com.example.kaixin.mycalendar.Utils.MyDatabaseHelper;
import com.example.kaixin.mycalendar.Utils.UserUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by kaixin on 2018/2/7.
 */

public class AccountBillFragment extends Fragment {

    //private ListView listView;
    private AccountAdapter accountAdapter;
    private List<AccountBill> list;
    private MyDatabaseHelper myDatabaseHelper;
    private FloatingActionButton fab;
    //private ExpandableStickyListHeadersListView listView;

    private AccountBillAdapter expandableListViewAdapter;
    private ExpandableListView expandableListView;

    @Override
    public void onResume() {
        super.onResume();
        //list = readDB();
        //accountAdapter = new AccountAdapter(getActivity(), list);
        //listView.setAdapter(accountAdapter);
        Map<Date, List<AccountBill>> map = new HashMap<>();
        try {
            //map = DivideIntoGroup(readDB());
            map = DivideIntoGroup(AccountBillUtils.queryAllLocalAccountBill(getActivity(), UserUtils.getUserId(getActivity())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<List<AccountBill>> lists = new ArrayList<>();
        for (Date date : getAllKey(map)) {
            List<AccountBill> l = map.get(date);
            lists.add(l);
        }
        expandableListViewAdapter = new AccountBillAdapter(
                getActivity(), getAllKey(map), lists);
        expandableListView.setAdapter(expandableListViewAdapter);
        //expandableListView.expandGroup(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_bill, null);
        myDatabaseHelper = new MyDatabaseHelper(getActivity());

        /*listView = (ExpandableStickyListHeadersListView) view.findViewById(R.id.listView);
        MyAdapter myAdapter = new MyAdapter(getActivity());
        listView.setAdapter(myAdapter);
        listView.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if (listView.isHeaderCollapsed(headerId)) {
                    listView.expand(headerId);
                } else {
                    listView.collapse(headerId);
                }
            }
        });*/
        /*listView = (ListView)view.findViewById(R.id.listView);
        list = readDB();
        accountAdapter = new AccountAdapter(getContext(), list);
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
            //map = DivideIntoGroup(readDB());
            map = DivideIntoGroup(AccountBillUtils.queryAllLocalAccountBill(getActivity(), UserUtils.getUserId(getActivity())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<List<AccountBill>> lists = new ArrayList<>();
        for (Date date : getAllKey(map)) {
            List<AccountBill> l = map.get(date);
            lists.add(l);
        }
        expandableListViewAdapter = new AccountBillAdapter(
                getActivity(), getAllKey(map), lists);
        expandableListView = (ExpandableListView)view.findViewById(R.id.expandableListView);
        expandableListView.setAdapter(expandableListViewAdapter);
        //expandableListView.expandGroup(0);
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int h, int c, long l) {
                List<AccountBill> header = expandableListViewAdapter.getGroup(h);
                AccountBill child = header.get(c);
                Intent intent = new Intent(getActivity(), AccountEditActivity.class);
                intent.putExtra("accountBill",child);
                startActivity(intent);
                return false;
            }
        });

        return view;
    }

    public List<AccountBill> OrderList(List<AccountBill> result) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < result.size()-1; i++) {
            Date d1 = sdf.parse(result.get(i).getAccountDate());
            for (int j = i+1; j < result.size(); j++) {
                Date d2 = sdf.parse(result.get(j).getAccountDate());
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i < result.size(); i++) {
            AccountBill accountBill = result.get(i);
            Date date = sdf.parse(accountBill.getAccountDate());
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
    public List<List<AccountBill>> getAllData(Map<Date, List<AccountBill>> map) {
        List<List<AccountBill>> lists = new ArrayList<>();
        for (Date date : getAllKey(map)) {
            List<AccountBill> l = map.get(date);
            lists.add(l);
        }
        return lists;
    }

    public class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter {
        private String[] countries;
        private LayoutInflater inflater;
        private Map<Date, List<AccountBill>> map = new HashMap<>();
        private List<Date> header;
        private List<List<AccountBill>> data;

        public MyAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            try {
                //map = DivideIntoGroup(readDB());
                map = DivideIntoGroup(AccountBillUtils.queryAllLocalAccountBill(getActivity(), UserUtils.getUserId(getActivity())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            header = getAllKey(map);
            data = getAllData(map);
            countries = new String[] {"k", "a"};
        }

        @Override
        public int getCount() {
            return data.size();
            //return countries.length;
        }
        @Override
        public Object getItem(int position) {
            return countries[position];
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                //convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
                holder.text = (TextView)convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.text.setText(countries[position]);
            return convertView;
        }
        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();
                //convertView = inflater.inflate(R.layout.header, parent, false);
                holder.text = (TextView)convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder)convertView.getTag();
            }
            String headerText = "" + countries[position].subSequence(0,1).charAt(0);
            holder.text.setText(countries[position]);
            return convertView;
        }
        @Override
        public long getHeaderId(int position) {
            return countries[position].subSequence(0, 1).charAt(0);
        }
        class ViewHolder {
            TextView text;
        }
        class HeaderViewHolder {
            TextView text;
        }
    }
}
