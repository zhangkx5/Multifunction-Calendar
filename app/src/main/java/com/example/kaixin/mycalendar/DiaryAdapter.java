package com.example.kaixin.mycalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Bean.BmobDiary;

import java.util.List;

/**
 * Created by kaixin on 2018/3/13.
 */

public class DiaryAdapter extends ArrayAdapter<BmobDiary> {

    private int resourceId = R.layout.list_diary;
    private List<BmobDiary> list;
    private Context context;

    public DiaryAdapter(Context context, List<BmobDiary> diaryList) {
        super(context, R.layout.list_diary, diaryList);
        this.context = context;
        this.list = diaryList;
    }

    class ViewHolder {
        TextView diary_date;
        TextView diary_content;
        TextView diary_address;
        TextView diary_weather;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BmobDiary diary = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder.diary_address = (TextView)convertView.findViewById(R.id.diary_address);
            viewHolder.diary_weather = (TextView)convertView.findViewById(R.id.diary_weather);
            viewHolder.diary_date = (TextView)convertView.findViewById(R.id.diary_date);
            viewHolder.diary_content = (TextView)convertView.findViewById(R.id.diary_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.diary_content.setText(diary.getDiaryContent());
        viewHolder.diary_address.setText(diary.getDiaryAddress());
        viewHolder.diary_weather.setText(diary.getDiaryWeather());
        viewHolder.diary_date.setText(diary.getDiaryDate());
        return convertView;
    }

    @Override
    public BmobDiary getItem(int position) {
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
}
