package com.example.kaixin.mycalendar.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Bean.Habit;
import com.example.kaixin.mycalendar.R;
import com.example.kaixin.mycalendar.Utils.ImageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by kaixin on 2018/3/16.
 */

public class HabitAdapter extends ArrayAdapter<Habit> {

    private int resourceId = R.layout.list_habit;
    private List<Habit> list = new ArrayList<>();
    private Context context;
    public HabitAdapter(Context context, List<Habit> habitList) {
        super(context, R.layout.list_habit, habitList);
        this.context = context;
        this.list = habitList;
    }

    class ViewHolder {
        TextView task_name;
        ImageView task_img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        Habit habit = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder.task_name = (TextView)convertView.findViewById(R.id.task_name);
            viewHolder.task_img = (ImageView)convertView.findViewById(R.id.task_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.task_name.setText(habit.getHabitName());
        viewHolder.task_img.setTag(habit.getHabitImg());
        if (!"".equals(habit.getHabitImg())) {
            new ImageAsyncTack(viewHolder.task_img).execute(habit.getHabitImgName());
        } else {
            viewHolder.task_img.setImageResource(R.mipmap.bg_task);
        }
        return convertView;
    }

    @Override
    public Habit getItem(int position) {
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

    public static class ImageAsyncTack extends AsyncTask<String, Void, Bitmap> {

        private ImageView photo;
        private String bmobUrl;
        public ImageAsyncTack(ImageView photo) {
            this.photo = photo;
            this.bmobUrl = (String)photo.getTag();
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bm = null;
            String localUrl = Environment.getExternalStorageDirectory().getPath() + "/mycalendar/" + params[0] + ".jpg";
            File taskPhoto = new File(localUrl);
            if (taskPhoto.exists()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(taskPhoto);
                    bm = BitmapFactory.decodeStream(fileInputStream);
                    Log.i("HabitAdapter", "本地图片");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                BmobFile bmobFile = new BmobFile(params[0] + ".jpg", "", bmobUrl);
                ImageUtils.downloadImage(bmobFile);
                Log.i("HabitAdapter", "下载图片");
                try {
                    FileInputStream fileInputStream = new FileInputStream(params[0]);
                    bm = BitmapFactory.decodeStream(fileInputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return bm;
        }
        @Override
        protected void onPostExecute(Bitmap bm) {
            super.onPostExecute(bm);
            photo.setImageBitmap(bm);
        }
    }
}
