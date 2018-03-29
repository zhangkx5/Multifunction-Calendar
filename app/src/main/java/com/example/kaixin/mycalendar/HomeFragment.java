package com.example.kaixin.mycalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Bean.AnniversaryDay;
import com.example.kaixin.mycalendar.Bean.Schedule;
import com.example.kaixin.mycalendar.Utils.AnniversaryUtils;
import com.example.kaixin.mycalendar.Utils.MyDatabaseHelper;
import com.example.kaixin.mycalendar.Utils.ScheduleUtils;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by kaixin on 2018/3/23.
 */

public class HomeFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener{

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private MaterialCalendarView mcv;
    private TextView textView;
    private Toolbar toolbar;
    private ImageView ib_add;
    private String selectedDate;

    private AnniversaryAdapter anniversaryAdapter;
    private ScheduleAdapter scheduleAdapter;
    private List<Schedule> list_schedule;
    private ListView lv_anniversary, lv_schedule;
    private List<AnniversaryDay> list_anniversary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mcv = (MaterialCalendarView)view.findViewById(R.id.calendarView);
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle("生活日历");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorTitle));
        ib_add = (ImageView)view.findViewById(R.id.ib_add);
        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createChoice();
            }
        });
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
        selectedDate = getSelectedDatesString();
        textView.setText(getSelectedDatesString());

        lv_anniversary = (ListView) view.findViewById(R.id.lv_anniversary);
        lv_schedule = (ListView) view.findViewById(R.id.lv_schedule);
        list_anniversary = getAnniversaryList(selectedDate);
        anniversaryAdapter = new AnniversaryAdapter(getActivity(), list_anniversary);
        lv_anniversary.setAdapter(anniversaryAdapter);
        list_schedule = getScheduleList(selectedDate);
        scheduleAdapter = new ScheduleAdapter(getActivity(), list_schedule);
        lv_schedule.setAdapter(scheduleAdapter);
        setListViewHeightBaseOnChildren(lv_anniversary);
        setListViewHeightBaseOnChildren(lv_schedule);
        lv_anniversary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AnniversaryDay anniversaryDay = anniversaryAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), AnniversaryDetailsActivity.class);
                intent.putExtra("anniversaryDay",anniversaryDay);
                startActivity(intent);
            }
        });
        lv_schedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Schedule schedule = scheduleAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), ScheduleEditActivity.class);
                intent.putExtra("schedule", schedule);
                startActivity(intent);
            }
        });
        return view;
    }
    private MyDatabaseHelper myDatabaseHelper;
    public List<AnniversaryDay> getAnniversaryList(String date) {
        myDatabaseHelper = new MyDatabaseHelper(getActivity());
        List<AnniversaryDay> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "anniversary_date LIKE ?";
        String[] selectionArgs = new String[]{"%" + date + "%"};
        Cursor cursor = dbRead.query(AnniversaryUtils.ANNIVERSARY_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
            String anniversary_name = cursor.getString(cursor.getColumnIndex("anniversary_name"));
            String anniversary_date = cursor.getString(cursor.getColumnIndex("anniversary_date"));
            String anniversary_notes = cursor.getString(cursor.getColumnIndex("anniversary_notes"));
            AnniversaryDay anniversaryDay = new AnniversaryDay();
            anniversaryDay.setObjectId(id);
            anniversaryDay.setUserId(user_id);
            anniversaryDay.setAnniversaryName(anniversary_name);
            anniversaryDay.setAnniversaryDate(anniversary_date);
            anniversaryDay.setAnniversaryNotes(anniversary_notes);
            result.add(anniversaryDay);
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(result);
        return result;
    }
    public List<Schedule> getScheduleList(String date) {
        myDatabaseHelper = new MyDatabaseHelper(getActivity());
        List<Schedule> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "schedule_start LIKE ?";
        String[] selectionArgs = new String[]{"%" + date + "%"};
        Cursor cursor = dbRead.query(ScheduleUtils.SCHEDULE_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
            String title = cursor.getString(cursor.getColumnIndex("schedule_title"));
            String address = cursor.getString(cursor.getColumnIndex("schedule_address"));
            String start = cursor.getString(cursor.getColumnIndex("schedule_start"));
            String end = cursor.getString(cursor.getColumnIndex("schedule_end"));
            String call = cursor.getString(cursor.getColumnIndex("schedule_call"));
            String notes = cursor.getString(cursor.getColumnIndex("schedule_notes"));
            Schedule schedule = new Schedule();
            schedule.setObjectId(id);
            schedule.setUserId(user_id);
            schedule.setScheduleTitle(title);
            schedule.setScheduleAddress(address);
            schedule.setScheduleStart(start);
            schedule.setScheduleEnd(end);
            schedule.setScheduleCall(call);
            schedule.setScheduleNotes(notes);
            result.add(schedule);
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(result);
        return result;
    }
    public void setListViewHeightBaseOnChildren(ListView lv) {
        ListAdapter listAdapter = lv.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null ,lv);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = lv.getLayoutParams();
        params.height = totalHeight + (lv.getDividerHeight() * (listAdapter.getCount() - 1));
        lv.setLayoutParams(params);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        selectedDate = getSelectedDatesString();
        textView.setText(getSelectedDatesString());
        list_anniversary.clear();
        list_anniversary = getAnniversaryList(selectedDate);
        anniversaryAdapter = new AnniversaryAdapter(getActivity(), list_anniversary);
        lv_anniversary.setAdapter(anniversaryAdapter);
        list_schedule.clear();
        list_schedule = getScheduleList(selectedDate);
        scheduleAdapter = new ScheduleAdapter(getActivity(), list_schedule);
        lv_schedule.setAdapter(scheduleAdapter);
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
        //SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date.getDate());
        //return FORMATTER.format(date.getDate());
    }

    private void createChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("新建");
        String[] items = {"日程管理", "我的日记", "周年纪念日", "账单"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        Intent scheduleIntent = new Intent(getActivity(), ScheduleEditActivity.class);
                        startActivity(scheduleIntent);
                        dialogInterface.dismiss();
                        break;
                    case 1:
                        Intent DiaryIntent = new Intent(getActivity(), DiaryEditActivity.class);
                        startActivity(DiaryIntent);
                        dialogInterface.dismiss();
                        break;
                    case 2:
                        Intent AnniversaryIntent = new Intent(getActivity(), AnniversaryEditActivity.class);
                        startActivity(AnniversaryIntent);
                        dialogInterface.dismiss();
                        break;
                    case 3:
                        Intent AccountIntent = new Intent(getActivity(), AccountEditActivity.class);
                        startActivity(AccountIntent);
                        dialogInterface.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();
    }
}
