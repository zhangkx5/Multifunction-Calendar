package com.example.kaixin.mycalendar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by kaixin on 2018/3/23.
 */

public class HomeFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener{

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private MaterialCalendarView mcv;
    private TextView textView;
    private String selectedDate;

    private MyDatabaseHelper myDatabaseHelper;
    private AnniversaryAdapter anniversaryAdapter;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> list_schedule;
    private ListView lv_anniversary, lv_schedule;
    private List<AnniversaryDay> list_anniversary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mcv = (MaterialCalendarView)view.findViewById(R.id.calendarView);
        textView = (TextView)view.findViewById(R.id.textView);

        mcv.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        mcv.addDecorators(
                new HighlightWeekendsDecorator(),
                //new TodayDecorator(),
                new EventDecorator()
        );
        mcv.setSelectedDate(CalendarDay.today());
        mcv.setOnDateChangedListener(this);
        mcv.setOnMonthChangedListener(this);
        textView.setText(getSelectedDatesString());


        myDatabaseHelper = new MyDatabaseHelper(getActivity());
        lv_anniversary = (ListView)view.findViewById(R.id.lv_anniversary);
        lv_schedule = (ListView)view.findViewById(R.id.lv_schedule);
        list_anniversary = readAnniversaryDB();
        anniversaryAdapter = new AnniversaryAdapter(getActivity(), list_anniversary);
        lv_anniversary.setAdapter(anniversaryAdapter);
        list_schedule = readScheduleDB();
        scheduleAdapter = new ScheduleAdapter(getActivity(), list_schedule);
        lv_schedule.setAdapter(scheduleAdapter);
        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        textView.setText(getSelectedDatesString());
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        textView.setText("monthchange");
    }

    private String getSelectedDatesString() {
        CalendarDay date = mcv.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }
    public List<Schedule> readScheduleDB() {
        List<Schedule> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = dbRead.rawQuery(MyDatabaseHelper.SCHEDULE_TABLE_SELECT, null);
        while (cursor.moveToNext()) {
            String scid = cursor.getString(cursor.getColumnIndex("scid"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String start = cursor.getString(cursor.getColumnIndex("start"));
            String end = cursor.getString(cursor.getColumnIndex("end"));
            String call = cursor.getString(cursor.getColumnIndex("call"));
            String notes = cursor.getString(cursor.getColumnIndex("notes"));
            Schedule schedule = new Schedule(scid, title, address, start, end, call, notes);
            result.add(schedule);
        }
        Collections.reverse(result);
        cursor.close();
        dbRead.close();
        return result;
    }
    public List<AnniversaryDay> readAnniversaryDB() {
        List<AnniversaryDay> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String table = MyDatabaseHelper.ANNIVERSARY_TABLE_NAME;
        String selection = "date = ?";
        String[] selectionArgs = new String[]{getSelectedDatesString()};
        Cursor cursor = dbRead.query(table, null, selection, selectionArgs, null, null, null);
        Toast.makeText(getActivity(), "搜索", Toast.LENGTH_SHORT).show();
        //Cursor cursor = dbRead.rawQuery(MyDatabaseHelper.ANNIVERSARY_TABLE_SELECT, null);
        while (cursor.moveToNext()) {
            String adid = cursor.getString(cursor.getColumnIndex("adid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String days = cursor.getString(cursor.getColumnIndex("date"));
            String notes = cursor.getString(cursor.getColumnIndex("notes"));
            AnniversaryDay anniversaryDay = new AnniversaryDay(adid, name, days, notes);
            result.add(anniversaryDay);
        }
        Collections.reverse(result);
        return result;
    }
}
