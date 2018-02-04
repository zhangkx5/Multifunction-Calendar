package com.example.kaixin.mycalendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kaixin on 2018/2/3.
 */

public class TextSpan implements LineBackgroundSpan {
    public static final String DEFAULT_TEXT = "日历";
    private final String lunar;

    public TextSpan() {
        this.lunar = DEFAULT_TEXT;
    }

    public TextSpan(String text) {
        this.lunar = text;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint, int left, int right, int top, int baseline,
            int bottom, CharSequence text, int start, int end, int lineNum
            ) {
        canvas.drawText(lunar, start, end, left, bottom+50, paint);
        //paint.setTextAlign(Paint.Align.CENTER);
    }
}
