package com.example.kaixin.mycalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Bean.AnniversaryDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kaixin on 2018/2/5.
 */

public class AnniversaryAdapter extends ArrayAdapter<AnniversaryDay>{

    private int resourceId = R.layout.list_anniversary;
    private List<AnniversaryDay> list;
    private Context context;
    public AnniversaryAdapter(Context context, List<AnniversaryDay> anniversaryDayList) {
        super(context, R.layout.list_anniversary, anniversaryDayList);
        this.context = context;
        this.list = anniversaryDayList;
    }

    class ViewHolder {
        TextView an_name;
        TextView an_days;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        AnniversaryDay anniversaryDay = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder.an_name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.an_days = (TextView)convertView.findViewById(R.id.days);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.an_name.setText(anniversaryDay.getAnniversaryName());
        String from = "";
        try {
            from = "" + fromThatDay(anniversaryDay.getAnniversaryDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.an_days.setText(from);
        return convertView;
    }

    @Override
    public AnniversaryDay getItem(int position) {
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

    public static Date stringToDate(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date that = simpleDateFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(that);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static String toNextDay(String date) throws ParseException {
        String result = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date that = simpleDateFormat.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(that);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        if (cal.before(calendar)) {
            cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+1);
        }
        long a = cal.getTime().getTime();
        long b = calendar.getTime().getTime();
        long c = (a - b) / (24*60*60*1000);
        result = c + "天";
        return result;
    }
    public static String fromThatDay(String date) throws ParseException {
        String result = "";
        long that = dateToLong(stringToDate(date));
        Date today = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long now = calendar.getTime().getTime();
        long days = 0;
        if (now > that) {
            days = (now - that) / (24*60*60*1000);
            result = days+"天";
        } else if (that > now){
            days = (that - now) / (24*60*60*1000);
            result = days+"天";
        } else if (that == now) {
            result = "今天";
        }
        return result;
    }
}
