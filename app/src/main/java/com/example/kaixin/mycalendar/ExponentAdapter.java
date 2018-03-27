package com.example.kaixin.mycalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Bean.Exponent;

import java.util.List;

/**
 * Created by kaixin on 2018/3/9.
 */

public class ExponentAdapter extends BaseAdapter {
    private List<Exponent> mlist;
    private LayoutInflater mInflater;

    public ExponentAdapter(Context context, List<Exponent> data) {
        mlist = data;
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }
    @Override
    public int getCount() {
        return mlist.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExponentAdapter.ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ExponentAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.item_weather,null);
            viewHolder.img = (ImageView)convertView.findViewById(R.id.exImg);
            viewHolder.expo = (TextView) convertView.findViewById(R.id.exponent);
            viewHolder.describ = (TextView) convertView.findViewById(R.id.content);
            viewHolder.details = (TextView) convertView.findViewById(R.id.details);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ExponentAdapter.ViewHolder)convertView.getTag();
        }
        viewHolder.img.setImageResource(mlist.get(position).getImageId());
        viewHolder.expo.setText(mlist.get(position).getExpo());
        viewHolder.describ.setText(mlist.get(position).getDescrib());
        viewHolder.details.setText(mlist.get(position).getDetails());
        return convertView;

    }

    class ViewHolder {
        public ImageView img;
        public TextView describ;
        public TextView expo;
        public TextView details;
    }
}