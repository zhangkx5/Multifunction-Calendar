package com.example.kaixin.mycalendar;

import android.content.Context;

import java.util.Date;
import java.util.List;

/**
 * Created by kaixin on 2018/2/8.
 */

public class AccountAdapter {
    private Context mContext;
    private List<Date> dates;
    public AccountAdapter(Context mContext, List<Date> dates) {
        this.mContext = mContext;
        this.dates = dates;
    }
}
