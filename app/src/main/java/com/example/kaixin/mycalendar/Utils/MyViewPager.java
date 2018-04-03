package com.example.kaixin.mycalendar.Utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by kaixin on 2018/3/23.
 */

public class MyViewPager extends ViewPager {

    private boolean isCanSlide = false;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
    public void setCanSlide(boolean isCanSlide) {
        this.isCanSlide = isCanSlide;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return isCanSlide && super.onInterceptTouchEvent(motionEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isCanSlide && super.onTouchEvent(event);
    }
}
