package com.example.kaixin.mycalendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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

        Bmob.initialize(this, "9b0b1ad0d1c9c081739ab99a3e05fe98");
        //by kaixin
        initViews();


        /*widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        widget.setArrowColor(getResources().getColor(R.color.sample_primary));
        widget.setLeftArrowMask(getResources().getDrawable(R.drawable.ic_navigation_arrow_back));
        widget.setRightArrowMask(getResources().getDrawable(R.drawable.ic_navigation_arrow_forward));
        widget.setSelectionColor(getResources().getColor(R.color.sample_primary));
        widget.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        widget.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Medium);
        widget.setDateTextAppearance(R.style.CustomDayTextAppearance);
        widget.setTitleFormatter(new MonthArrayTitleFormatter(getResources().getTextArray(R.array.custom_months)));
        widget.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.custom_weekdays)));
        widget.setTileSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics()));*/

        /*ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });*/


        //20180323
        /*final TextView textView = (TextView) findViewById(R.id.textView);
        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setWeekDayFormatter(new ArrayWeekDayFormatter(getResources().getTextArray(R.array.weekdaysTitle)));
        materialCalendarView.state().edit()
                .setMinimumDate(CalendarDay.from(2018, 0, 1))
                .setMaximumDate(CalendarDay.from(2018, 4, 1))
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.addDecorators(
                new HighlightWeekendsDecorator(),
                new TodayDecorator(),
                new EventDecorator()
        );
        CalendarDay today = CalendarDay.today();
        int tMonth = today.getMonth()+1;
        getSupportActionBar().setTitle(today.getYear()+"年"+tMonth+"月"+today.getDay()+"日");
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectedDate = date;
                Calendar lunar = date.getCalendar();
                LunarCalendar lunarCalendar = new LunarCalendar(lunar);
                textView.setText(""+date.getDay() + " " + lunarCalendar.toString());
                int dMonth = date.getMonth()+1;
                getSupportActionBar().setTitle(date.getYear()+"年"+dMonth+"月"+date.getDay()+"日");
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // by kaixin
                getTime();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
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

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_schedule) {
            Intent intent = new Intent(this, ScheduleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_diary) {
            Intent intent = new Intent(this, DiaryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_anniversary) {
            Intent intent = new Intent(this, AnniversaryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_account) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_weather) {
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_task) {
            Intent intent = new Intent(this, TaskFragment.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    //by kaixin
    boolean flag = false;
    public void getTime() {
        if (flag == false) {
            materialCalendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit();
            flag = true;
        } else {
            materialCalendarView.state().edit()
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
            flag = false;
        }
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
