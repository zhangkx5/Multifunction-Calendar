package com.example.kaixin.mycalendar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Bean.Schedule;
import com.example.kaixin.mycalendar.R;

import java.util.List;

/**
 * Created by kaixin on 2018/3/15.
 */

public class ScheduleAdapter extends ArrayAdapter<Schedule>{

    private int resourceId;
    private List<Schedule> list;
    private Context context;

    public ScheduleAdapter(Context context, List<Schedule> scheduleList) {
        super(context, R.layout.list_schedule, scheduleList);
        this.context = context;
        this.resourceId = R.layout.list_schedule;
        this.list = scheduleList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Schedule schedule = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder.schedule_title = (TextView)convertView.findViewById(R.id.schedule_title);
            viewHolder.schedule_startTime = (TextView)convertView.findViewById(R.id.startTime);
            viewHolder.schedule_startDay = (TextView)convertView.findViewById(R.id.startDay);
            viewHolder.schedule_endTime = (TextView)convertView.findViewById(R.id.endTime);
            viewHolder.schedule_endDay = (TextView)convertView.findViewById(R.id.endDay);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.schedule_startDay.setText(schedule.getScheduleStart().split(" ")[0]);
        if (schedule.getScheduleStart().split(" ")[0].equals(schedule.getScheduleEnd().split(" ")[0])) {
            viewHolder.schedule_endDay.setText(schedule.getScheduleEnd().split(" ")[0]);
        } else {
            viewHolder.schedule_endDay.setText("");
        }
        viewHolder.schedule_title.setText(schedule.getScheduleTitle());
        viewHolder.schedule_startTime.setText(schedule.getScheduleStart().split(" ")[1]);
        viewHolder.schedule_endTime.setText(schedule.getScheduleEnd().split(" ")[1]);
        return convertView;
    }

    @Override
    public Schedule getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    class ViewHolder {
        TextView schedule_title;
        TextView schedule_startDay;
        TextView schedule_endDay;
        TextView schedule_startTime;
        TextView schedule_endTime;
    }
}
