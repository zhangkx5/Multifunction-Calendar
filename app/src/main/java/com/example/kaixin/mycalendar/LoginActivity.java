package com.example.kaixin.mycalendar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by kaixin on 2018/3/20.
 */

public class LoginActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String[] TITLES = {"登录", "注册"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        mPagerAdapter pagerAdapter = new mPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(new LoginFragment(), TITLES[0]);
        tabLayout.addTab(tabLayout.newTab().setText(TITLES[0]));
        pagerAdapter.addFragment(new SignUpFragment(), TITLES[1]);
        tabLayout.addTab(tabLayout.newTab().setText(TITLES[1]));
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ImageButton ib_back = (ImageButton)findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.finish();
            }
        });
    }

    public static class mPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> titles = new ArrayList<>();

        public mPagerAdapter(FragmentManager fragmentManager) {
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