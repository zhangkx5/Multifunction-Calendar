package com.example.kaixin.mycalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kaixin.mycalendar.Bean.Task;

import java.util.List;

/**
 * Created by kaixin on 2018/3/16.
 */

public class TaskAdapter extends ArrayAdapter<Task> {

    private int resourceId = R.layout.list_task;
    private List<Task> list;
    private Context context;
    public TaskAdapter(Context context, List<Task> taskList) {
        super(context, R.layout.list_task, taskList);
        this.context = context;
        this.list = taskList;
    }

    class ViewHolder {
        TextView task_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        Task task = getItem(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceId, parent, false);
            viewHolder.task_name = (TextView)convertView.findViewById(R.id.task_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.task_name.setText(task.getTaskName());
        return convertView;
    }

    @Override
    public Task getItem(int position) {
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
