package com.example.kaixin.mycalendar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by kaixin on 2018/2/3.
 */

public class EventDecorator implements DayViewDecorator {
    private int color =  Color.parseColor("#FFFF0011");
    private final Drawable highlighDrawable;
    //private HashSet<CalendarDay> dates;
    private String date;
    private List<String> dateList;

    public EventDecorator(List<String> dateList) {
        highlighDrawable = new ColorDrawable(Color.parseColor("#228BC34A"));
        //date = CalendarDay.today();
        this.dateList = dateList;
        //this.color = color;
        //this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if (day.isAfter(CalendarDay.today())) return false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(day.getDate());
        for (int i = 0; i < dateList.size(); i++) {
            if (dateList.get(i).equals(date)) {
                Log.i("EventDecorator", date + "已打卡");
                return true;
            }
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, color));
        view.setBackgroundDrawable(highlighDrawable);
    }
}
