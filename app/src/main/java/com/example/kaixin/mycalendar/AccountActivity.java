package com.example.kaixin.mycalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

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

/**
 * Created by kaixin on 2018/2/4.
 */

public class AccountActivity extends AppCompatActivity {

    private List<AccountBill> list;
    private MyDatabaseHelper myDatabaseHelper;
    private ImageButton ib_add, ib_back;
    //private ExpandableStickyListHeadersListView listView;

    private MyExpandableListViewAdapter expandableListViewAdapter;
    private ExpandableListView expandableListView;
    /*private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] TITLES = {"账单", "报表"};*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_account_bill);
        setContentView(R.layout.activity_account);
        myDatabaseHelper = new MyDatabaseHelper(AccountActivity.this);

        ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_add = (ImageButton) findViewById(R.id.ib_add);
        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, AccountEditActivity.class);
                startActivity(intent);
            }
        });
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountActivity.this.finish();
            }
        });

        Map<Date, List<AccountBill>> map = new HashMap<>();
        try {
            //map = DivideIntoGroup(readDB());
            map = DivideIntoGroup(AccountBillUtils.queryAllLocalAccountBill(AccountActivity.this, UserUtils.getUserId(AccountActivity.this)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<List<AccountBill>> lists = new ArrayList<>();
        for (Date date : getAllKey(map)) {
            List<AccountBill> l = map.get(date);
            lists.add(l);
        }
        expandableListViewAdapter = new MyExpandableListViewAdapter(
                AccountActivity.this, getAllKey(map), lists);
        expandableListView = (ExpandableListView)findViewById(R.id.expandableListView);
        expandableListView.setAdapter(expandableListViewAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int h, int c, long l) {
                List<AccountBill> header = expandableListViewAdapter.getGroup(h);
                AccountBill child = header.get(c);
                Intent intent = new Intent(AccountActivity.this, AccountEditActivity.class);
                intent.putExtra("accountBill",child);
                startActivity(intent);
                return false;
            }
        });
        /*tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        AccountPagerAdapter accountPagerAdapter = new AccountPagerAdapter(getSupportFragmentManager());

        accountPagerAdapter.addFragment(new AccountBillFragment(), TITLES[0]);
        tabLayout.addTab(tabLayout.newTab().setText(TITLES[0]));
        accountPagerAdapter.addFragment(new AccountReportActivity(), TITLES[1]);
        tabLayout.addTab(tabLayout.newTab().setText(TITLES[1]));
        viewPager.setAdapter(accountPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);*/
    }

    /*public static class AccountPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> titles = new ArrayList<>();

        public AccountPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public String getPageTitle(int position) {
            return titles.get(position);
        }
    }*/
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
}