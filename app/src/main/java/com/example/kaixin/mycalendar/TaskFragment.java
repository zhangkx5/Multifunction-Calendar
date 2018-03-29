package com.example.kaixin.mycalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kaixin.mycalendar.Bean.Task;
import com.example.kaixin.mycalendar.Utils.TaskUtils;
import com.example.kaixin.mycalendar.Utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaixin on 2018/2/4.
 */

public class TaskFragment extends Fragment {

    private ListView listView;
    private TaskAdapter taskAdapter;
    private List<Task> list = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        list = TaskUtils.queryAllLocalTask(getActivity(), UserUtils.getUserId(getActivity()));
        if (list.size() == 0) {
            TaskUtils.queryAllBmobTask(getActivity());
            list = TaskUtils.queryAllLocalTask(getActivity(), UserUtils.getUserId(getActivity()));
        }
        taskAdapter = new TaskAdapter(getActivity(), list);
        listView.setAdapter(taskAdapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle("任务");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorTitle));

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TaskEditActivity.class);
                startActivity(intent);
            }
        });

        listView = (ListView)view.findViewById(R.id.listView);
        /*list = TaskUtils.queryAllLocalTask(getActivity(), UserUtils.getUserId(getActivity()));
        if (list.size() == 0) {
            TaskUtils.queryAllBmobTask(getActivity());
            list = TaskUtils.queryAllLocalTask(getActivity(), UserUtils.getUserId(getActivity()));
        }
            taskAdapter = new TaskAdapter(getActivity(), list);
            listView.setAdapter(taskAdapter);*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), TaskCheckActivity.class);
                Task task = taskAdapter.getItem(i);
                intent.putExtra("task", task);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("删除提醒");
                builder.setMessage("确定要删除吗？此操作不可逆！");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Task task = taskAdapter.getItem(pos);
                        TaskUtils.deleteBmobTask(task.getObjectId());
                        TaskUtils.deleteLocalTask(getActivity(), task.getObjectId());
                        list.remove(pos);
                        taskAdapter.notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
        return view;
    }
}