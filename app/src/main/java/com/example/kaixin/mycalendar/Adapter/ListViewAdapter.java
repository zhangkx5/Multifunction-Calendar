package com.example.kaixin.mycalendar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Bean.AnniversaryDay;
import com.example.kaixin.mycalendar.Bean.Diary;
import com.example.kaixin.mycalendar.Bean.Schedule;
import com.example.kaixin.mycalendar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaixin on 2018/4/4.
 */

public class ListViewAdapter extends BaseAdapter {
    private List<AnniversaryDay> list_anniversary = new ArrayList<>();
    private List<Schedule> list_schedule = new ArrayList<>();
    private List<Diary> list_diay = new ArrayList<>();
    private Context context;
    private int flag = 0;

    public ListViewAdapter(Context context, List<AnniversaryDay> list_anniversary, List<Schedule> list_schedule, List<Diary> list_diay) {
        this.context = context;
        this.list_anniversary = list_anniversary;
        this.list_schedule = list_schedule;
        this.list_diay = list_diay;
    }
    @Override
    public int getCount() {
        int count = list_anniversary.size() + list_schedule.size() + list_diay.size();
        if (list_schedule.size() == 0) {
            flag = 1;
            count = count + 1;
        }
        if (list_anniversary.size() == 0 && list_schedule.size() == 0 && list_diay.size() == 0) {
            flag = 2;
        }
        return count;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public Object getItem(int position) {
        if (position < list_anniversary.size())
            return list_anniversary.get(position);
        if (position < list_anniversary.size() + list_schedule.size())
            return list_schedule.get(position - list_anniversary.size());
        if (position < list_anniversary.size() + list_schedule.size() + list_diay.size())
            return list_diay.get(position - list_anniversary.size() - list_schedule.size());
        return null;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AnniversaryViewHolder holder1 = null;
        ScheduleViewHolder holder2 = null;
        DiaryViewHolder holder3 = null;
        int type = getItemViewType(position);
        switch (type) {
            case 0:
                if (convertView == null) {
                    holder1 = new AnniversaryViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.list_anniversary, parent, false);
                    holder1.an_name = (TextView)convertView.findViewById(R.id.name);
                    holder1.an_days = (TextView)convertView.findViewById(R.id.days);
                    convertView.setTag(holder1);
                } else {
                    holder1 = (AnniversaryViewHolder)convertView.getTag();
                }
                AnniversaryDay anniversaryDay = list_anniversary.get(position);
                holder1.an_name.setText(anniversaryDay.getAnniversaryName());

                //holder1.an_name.setText("这是纪念日");
                break;
            case 1:
                if (convertView == null) {
                    holder2 = new ScheduleViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.list_schedule, parent, false);
                    holder2.schedule_title = (TextView)convertView.findViewById(R.id.schedule_title);
                    holder2.schedule_startTime = (TextView)convertView.findViewById(R.id.startTime);
                    holder2.schedule_startDay = (TextView)convertView.findViewById(R.id.startDay);
                    holder2.schedule_endTime = (TextView)convertView.findViewById(R.id.endTime);
                    holder2.schedule_endDay = (TextView)convertView.findViewById(R.id.endDay);
                    convertView.setTag(holder2);
                } else {
                    holder2 = (ScheduleViewHolder)convertView.getTag();
                }
                //holder2.schedule_title.setText("这是日程");
                Schedule schedule = list_schedule.get(position - list_anniversary.size());
                holder2.schedule_title.setText(schedule.getScheduleTitle());
                holder2.schedule_startTime.setText("开始时间: "+schedule.getScheduleStart().split(" ")[1]);
                holder2.schedule_endTime.setText("结束时间: "+schedule.getScheduleEnd().split(" ")[1]);
                break;
            case 2:
                if (convertView == null) {
                    holder3 = new DiaryViewHolder();
                    convertView = LayoutInflater.from(context).inflate(R.layout.list_diary, parent, false);
                    holder3.diary_address = (TextView)convertView.findViewById(R.id.diary_address);
                    holder3.diary_weather = (TextView)convertView.findViewById(R.id.diary_weather);
                    holder3.diary_date = (TextView)convertView.findViewById(R.id.diary_date);
                    holder3.diary_content = (TextView)convertView.findViewById(R.id.diary_content);
                    convertView.setTag(holder3);
                } else {
                    holder3 = (DiaryViewHolder)convertView.getTag();
                }
                //.diary_content.setText("这是日记");
                Diary diary = list_diay.get(position - list_anniversary.size() - list_schedule.size());
                holder3.diary_content.setText(diary.getDiaryContent());
                holder3.diary_address.setText(diary.getDiaryAddress());
                holder3.diary_weather.setText(diary.getDiaryWeather());
                holder3.diary_date.setText(diary.getDiaryDate());
                break;
            case 3:
                convertView = LayoutInflater.from(context).inflate(R.layout.list_calendar_event, parent, false);
                TextView text = (TextView)convertView.findViewById(R.id.text);
                if (flag == 2) {
                    text.setText("暂无日历事件\n点击添加");
                } else {
                    text.setText("暂无日程安排\n点击添加");
                }
                break;
        }
        return convertView;
    }
    @Override
    public int getViewTypeCount() {
        return 4;
    }
    @Override
    public int getItemViewType(int position) {
        if (position < list_anniversary.size()) return 0;
        if (position < list_anniversary.size() + list_schedule.size()) return 1;
        if (position < list_anniversary.size() + list_schedule.size() + list_diay.size()) return 2;
        return 3;
    }


    class AnniversaryViewHolder {
        TextView an_name;
        TextView an_days;
    }

    class DiaryViewHolder {
        TextView diary_date;
        TextView diary_content;
        TextView diary_address;
        TextView diary_weather;
    }
    class ScheduleViewHolder {
        TextView schedule_title;
        TextView schedule_startDay;
        TextView schedule_endDay;
        TextView schedule_startTime;
        TextView schedule_endTime;
    }
}
