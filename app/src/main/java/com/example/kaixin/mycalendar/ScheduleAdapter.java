package com.example.kaixin.mycalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Bean.Schedule;

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
            viewHolder.schedule_start = (TextView)convertView.findViewById(R.id.startTime);
            viewHolder.schedule_end = (TextView)convertView.findViewById(R.id.endTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.schedule_title.setText(schedule.getScheduleTitle());
        viewHolder.schedule_start.setText(schedule.getScheduleStart());
        viewHolder.schedule_end.setText(schedule.getScheduleEnd());
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
        TextView schedule_start;
        TextView schedule_end;
    }
}
