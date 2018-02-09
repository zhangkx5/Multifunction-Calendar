package com.example.kaixin.mycalendar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaixin on 2018/2/4.
 */

public class AccountActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] TITLES = {"账单", "报表"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        AccountPagerAdapter accountPagerAdapter = new AccountPagerAdapter(getSupportFragmentManager());

        accountPagerAdapter.addFragment(new AccountBillFragment(), TITLES[0]);
        tabLayout.addTab(tabLayout.newTab().setText(TITLES[0]));
        accountPagerAdapter.addFragment(new AccountReportFragment(), TITLES[1]);
        tabLayout.addTab(tabLayout.newTab().setText(TITLES[1]));
        viewPager.setAdapter(accountPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public static class AccountPagerAdapter extends FragmentPagerAdapter {
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
    }
}