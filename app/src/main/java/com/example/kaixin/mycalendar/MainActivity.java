package com.example.kaixin.mycalendar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity/*
        implements NavigationView.OnNavigationItemSelectedListener */{

    //by kaixin
    MaterialCalendarView materialCalendarView;
    CalendarDay selectedDate;

    //viewpager
    private TabLayout mTabLayout;
    private final int[] TAB_TITLES = new int[]{R.string.home, R.string.task, R.string.more, R.string.me};
    private final int[] TAB_IMGS = new int[]{R.drawable.selector_tab_weixin, R.drawable.selector_tab_contacts, R.drawable.selector_tab_find, R.drawable.selector_tab_me};
    private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new HomeFragment(), new TaskFragment(), new MoreFragment(), new MeFragment()};
    private final int COUNT = TAB_TITLES.length;
    private MyViewPager myViewPager;
    private MyViewPagerAdapter myViewPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_mainmain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setPermissions();
        Bmob.initialize(this, "9b0b1ad0d1c9c081739ab99a3e05fe98");
        initViews();

    }

    final String[] PERMISSION = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,  //写入权限
            Manifest.permission.CAMERA,// 相机权限
            Manifest.permission.ACCESS_COARSE_LOCATION,// 定位权限

    };
    private void setPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION, 0);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION, 1);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION, 2);
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_personal) {
            Intent intent = new Intent(MainActivity.this, PersonalActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        mTabLayout = (TabLayout)findViewById(R.id.tabLayout);
        myViewPager = (MyViewPager)findViewById(R.id.viewPager);
        //setTabs(mTabLayout, this.getLayoutInflater(), TAB_TITLES, TAB_IMGS);
        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myViewPagerAdapter);
        //myViewPager.setCanSlide(false);
        //myViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        //mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(myViewPager));

        mTabLayout.setupWithViewPager(myViewPager);
        for (int i = 0; i < COUNT; i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            Drawable drawable = getResources().getDrawable(TAB_IMGS[i]);
            tab.setIcon(drawable);
            tab.setText(TAB_TITLES[i]);
        }
    }

    private void setTabs(TabLayout tabLayout, LayoutInflater inflater, int[] tabTitles, int[] tabImgs) {
        for (int i = 0; i < tabImgs.length; i++) {
            View view  = inflater.inflate(R.layout.tab_custom, null);
            TextView textView = (TextView)view.findViewById(R.id.tv_tab);
            ImageView imageView = (ImageView)view.findViewById(R.id.img_tab);
            textView.setText(tabTitles[i]);
            imageView.setImageResource(tabImgs[i]);

            /*TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(view);
            tabLayout.addTab(tab);*/
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i]).setIcon(tabImgs[i]));
            tabLayout.getTabAt(i).setCustomView(view);
        }
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        public MyViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        @Override
        public Fragment getItem(int position) {
            return TAB_FRAGMENTS[position];
        }
        @Override
        public int getCount() {
            return COUNT;
        }
    }

}
