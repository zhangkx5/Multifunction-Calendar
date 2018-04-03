package com.example.kaixin.mycalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.kaixin.mycalendar.Adapter.HabitAdapter;
import com.example.kaixin.mycalendar.Bean.Habit;
import com.example.kaixin.mycalendar.Utils.HabitUtils;
import com.example.kaixin.mycalendar.Utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaixin on 2018/2/4.
 */

public class HabitFragment extends Fragment {

    private ListView listView;
    private HabitAdapter habitAdapter;
    private List<Habit> list = new ArrayList<>();
    private ImageView ib_add;

    @Override
    public void onResume() {
        super.onResume();
        list = HabitUtils.queryAllLocalHabit(getActivity(), UserUtils.getUserId(getActivity()));
        if (list.size() == 0) {
            HabitUtils.queryAllBmobHabit(getActivity());
            list = HabitUtils.queryAllLocalHabit(getActivity(), UserUtils.getUserId(getActivity()));
        }
        habitAdapter = new HabitAdapter(getActivity(), list);
        listView.setAdapter(habitAdapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_habit, container, false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle("习惯");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorTitle));
        ib_add = (ImageView)view.findViewById(R.id.ib_add);
        listView = (ListView) view.findViewById(R.id.listView);

        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HabitEditActivity.class);
                startActivity(intent);
            }
        });
        /*list = HabitUtils.queryAllLocalTask(getActivity(), UserUtils.getUserId(getActivity()));
        if (list.size() == 0) {
            HabitUtils.queryAllBmobTask(getActivity());
            list = HabitUtils.queryAllLocalTask(getActivity(), UserUtils.getUserId(getActivity()));
        }
            habitAdapter = new HabitAdapter(getActivity(), list);
            listView.setAdapter(habitAdapter);*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), HabitClockingInActivity.class);
                Habit habit = habitAdapter.getItem(i);
                intent.putExtra("habit", habit);
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
                        Habit habit = habitAdapter.getItem(pos);
                        HabitUtils.deleteBmobHabit(habit.getObjectId());
                        HabitUtils.deleteLocalHabit(getActivity(), habit.getObjectId());
                        list.remove(pos);
                        habitAdapter.notifyDataSetChanged();
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