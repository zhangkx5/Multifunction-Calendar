package com.example.kaixin.mycalendar;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by kaixin on 2018/2/3.
 */

public class EventDecorator implements DayViewDecorator {
    private int color =  Color.parseColor("#FFFF0011");
    //private HashSet<CalendarDay> dates;
    private CalendarDay date;

    public EventDecorator() {
        date = CalendarDay.today();
        //this.color = color;
        //this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, color));
    }
}
