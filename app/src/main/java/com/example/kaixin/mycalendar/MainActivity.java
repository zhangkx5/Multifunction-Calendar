package com.example.kaixin.mycalendar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity/*
        implements NavigationView.OnNavigationItemSelectedListener */{

    //by kaixin

    //viewpager
    private TabLayout tabLayout;
    //private final int[] TAB_TITLES = new int[]{R.string.calendar, R.string.habit,  R.string.me};
    //private final int[] TAB_IMGS = new int[]{R.drawable.selector_tab_calendar, R.drawable.selector_tab_habit, R.drawable.selector_tab_me};
    //private final Fragment[] TAB_FRAGMENTS = new Fragment[]{new CalendarFragment(), new HabitFragment(), new MeFragment()};
    //private final int COUNT = TAB_TITLES.length;
    private ViewPager viewPager;
    private MyViewPagerAdapter viewPagerAdapter;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setPermissions();
        Bmob.initialize(this, "9b0b1ad0d1c9c081739ab99a3e05fe98");
        Boolean isFirst = getBoolean();
        if (isFirst) {
            saveBoolean(false);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        initViews();

    }

    public void saveBoolean(boolean value) {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("calendar", Context.MODE_PRIVATE);
        }
        sharedPreferences.edit().putBoolean("isFirst", value).commit();
    }
    public boolean getBoolean() {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("calendar", Context.MODE_PRIVATE);
        }
        return sharedPreferences.getBoolean("isFirst", true);
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
    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    /*@Override
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
    }*/

    private void initViews() {
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        // 设置适配器，适配器为主界面添加3个Fragment，支持左右滑动切换
        viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        // 设置导航栏
        tabLayout.setupWithViewPager(viewPager);
        int[] TAB_TITLES = new int[]{ // 导航栏模块名字：日历、习惯、我
                R.string.calendar,
                R.string.habit,
                R.string.me};
        int[] TAB_IMGS = new int[]{ // 导航栏模块图片，支持点击改变颜色
                R.drawable.selector_tab_calendar,
                R.drawable.selector_tab_habit,
                R.drawable.selector_tab_me};
        for (int i = 0; i < TAB_TITLES.length; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            Drawable drawable = getResources().getDrawable(TAB_IMGS[i]);
            tab.setIcon(drawable); //  设置图片
            tab.setText(TAB_TITLES[i]); // 设置文字
        }
    }

    private class MyViewPagerAdapter extends FragmentPagerAdapter {
        Fragment[] FRAGMENTS = new Fragment[]{
                new CalendarFragment(), // 日历信息模块Fragment
                new HabitFragment(), // 习惯管理模块Fragment
                new MeFragment() // 个人信息模块Fragment
        };
        public MyViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        @Override
        public Fragment getItem(int position) {
            return FRAGMENTS[position]; // 返回对应Fragment
        }
        @Override
        public int getCount() {
            return FRAGMENTS.length; // 返回Fragment数量
        }
    }

}
