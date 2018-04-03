package com.example.kaixin.mycalendar.Utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;

/**
 * Created by kaixin on 2018/2/3.
 */

public class HighlightWeekendsDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
    private final Drawable highlighDrawable;
    private static final int color = Color.parseColor("#228BC34A");
    //private String lunar = new LunarCalendar(calendar).toString();

    public HighlightWeekendsDecorator() {
        highlighDrawable = new ColorDrawable(color);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        /*Calendar cal = day.getCalendar();
        LunarCalendar lunarCalendar = new LunarCalendar(cal);
        lunar = lunarCalendar.toString();*/
        return weekDay == Calendar.SATURDAY || weekDay == Calendar.SUNDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(highlighDrawable);
        //view.addSpan(new TextSpan(lunar));
    }
}
