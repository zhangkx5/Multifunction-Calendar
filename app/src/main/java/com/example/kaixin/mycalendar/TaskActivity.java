package com.example.kaixin.mycalendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

/**
 * Created by kaixin on 2018/2/4.
 */

public class TaskActivity extends AppCompatActivity {

    private PinnedHeaderExpandableListView explistview;
    private String[][] childrenData = new String[10][10];
    private String[] groupData = new String[10];
    private int expandFlag = -1;
    private PinnedHeaderExpandableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        explistview = (PinnedHeaderExpandableListView)findViewById(R.id.explistview);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            groupData[i] = "分组" + i;
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                childrenData[i][j] = "详情" + i + "-" + j;
            }
        }
        explistview.setHeaderView(getLayoutInflater().inflate(R.layout.group_head,
                explistview, false));
        adapter = new PinnedHeaderExpandableAdapter(childrenData, groupData,
                getApplicationContext(), explistview);
        explistview.setAdapter(adapter);
        //explistview.setOnGroupClickListener(new GroupClickListener());
    }

    class GroupClickListener implements OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
            if (expandFlag == -1) {
                explistview.expandGroup(groupPosition);
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            } else if(expandFlag == groupPosition) {
                explistview.collapseGroup(expandFlag);
                expandFlag = -1;
            } else {
                explistview.collapseGroup(expandFlag);
                explistview.expandGroup(groupPosition);
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            }
            return true;
        }
    }
}