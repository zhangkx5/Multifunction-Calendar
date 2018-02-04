package com.example.kaixin.mycalendar;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

/**
 * Created by kaixin on 2018/2/3.
 */

public class LunarDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
    private String lunar = new LunarCalendar(calendar).toString();

    public LunarDecorator() {}

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        Calendar cal = day.getCalendar();
        LunarCalendar lunarCalendar = new LunarCalendar(cal);
        lunar = lunarCalendar.toString();
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new TextSpan(lunar));
    }
}
