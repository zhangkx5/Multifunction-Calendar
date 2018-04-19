package com.example.kaixin.mycalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarUtil;
import com.codbking.calendar.CalendarView;
import com.example.kaixin.mycalendar.Adapter.ListViewAdapter;
import com.example.kaixin.mycalendar.Bean.AccountBill;
import com.example.kaixin.mycalendar.Bean.AnniversaryDay;
import com.example.kaixin.mycalendar.Bean.Diary;
import com.example.kaixin.mycalendar.Bean.Schedule;
import com.example.kaixin.mycalendar.Utils.AccountBillUtils;
import com.example.kaixin.mycalendar.Utils.AnniversaryUtils;
import com.example.kaixin.mycalendar.Utils.DiaryUtils;
import com.example.kaixin.mycalendar.Utils.ScheduleUtils;
import com.example.kaixin.mycalendar.Utils.UserUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kaixin on 2018/3/23.
 */

public class CalendarFragment extends Fragment {
    private ImageView ib_add;
    private String selectedDate;

    private TextView mTitle;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private CalendarDateView mCalendarDateView;
    private List<AnniversaryDay> all_anniversary = new ArrayList<>();
    private List<Schedule> all_schedule = new ArrayList<>();


    @Override
    public void onResume() {
        super.onResume();

        initView();
        String today[] = mTitle.getText().toString().split("-");
        showEvents(today[0], today[1], today[2]);

        /*String user_id = UserUtils.getUserId(getActivity());
        all_anniversary = AnniversaryUtils.queryAllLocalAnniversary(getActivity(), user_id);
        all_schedule = ScheduleUtils.queryAllLocalSchedule(getActivity(), user_id);
        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar_date_view, null);
                }
                TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                TextView text = (TextView) convertView.findViewById(R.id.text);
                View redPoint = (View) convertView.findViewById(R.id.redPoint);
                text.setText("" + bean.day); // 显示公历文字
                if (bean.mothFlag != 0) { // 本月日期文字颜色
                    text.setTextColor(0xff9299a1);
                } else { // 非本月日期文字颜色
                    text.setTextColor(0xff444444);
                }
                chinaText.setText(bean.chinaDay); // 显示农历文字
                if (hasEvents(bean)) {  // 判断该日期是否有日历事件
                    redPoint.setVisibility(View.VISIBLE);// 有日历事件的日期下方显示红点标记
                } else {
                    redPoint.setVisibility(View.GONE);// 没有日历事件的日期下方不显示红点
                }
                return convertView;
            }
        });*/
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        ib_add = (ImageView)view.findViewById(R.id.ib_add);
        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChoice();
            }
        });


        mTitle = (TextView)view.findViewById(R.id.title);
        mCalendarDateView = (CalendarDateView)view.findViewById(R.id.calendarDateView);
        listView = (ListView) view.findViewById(R.id.listView);
        initView();
        String today[] = mTitle.getText().toString().split("-");
        showEvents(today[0], today[1], today[2]);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int type = listViewAdapter.getItemViewType(i); //获取点击项的日历事件类型
                switch (type) {
                    case 0: // 跳转到纪念日的详情
                        AnniversaryDay anniversaryDay = (AnniversaryDay)listViewAdapter.getItem(i);
                        Intent intent1 = new Intent(getActivity(), AnniversaryDetailsActivity.class);
                        intent1.putExtra("anniversaryDay",anniversaryDay);
                        startActivity(intent1);
                        break;
                    case 1: // 跳转到日程安排的详情
                        Schedule schedule = (Schedule)listViewAdapter.getItem(i);
                        Intent intent2 = new Intent(getActivity(), ScheduleEditActivity.class);
                        intent2.putExtra("schedule", schedule);
                        startActivity(intent2);
                        break;
                    case 2: //  跳转到日记的详情
                        Diary diary = (Diary)listViewAdapter.getItem(i);
                        Intent intent3 = new Intent(getActivity(), DiaryEditActivity.class);
                        intent3.putExtra("diary", diary);
                        startActivity(intent3);
                        break;
                    case 3: // 跳转到账单详情
                        AccountBill accountBill = (AccountBill)listViewAdapter.getItem(i);
                        Intent intent4 = new Intent(getActivity(), AccountEditActivity.class);
                        intent4.putExtra("accountBill", accountBill);
                        startActivity(intent4);
                        break;
                    case 4: // 弹出创建日历事件的事件类型选择框
                        addChoice();
                        break;
                }
            }
        });
        return view;
    }

    private void initView() {

        String user_id = UserUtils.getUserId(getActivity());
        if (all_anniversary == null || all_anniversary.size() == 0) {
            all_anniversary = new ArrayList<>();
        }
        if (all_schedule == null || all_schedule.size() == 0) {
            all_schedule = new ArrayList<>();
        }
        all_anniversary = AnniversaryUtils.queryAllLocalAnniversary(getActivity(), user_id);
        all_schedule = ScheduleUtils.queryAllLocalSchedule(getActivity(), user_id);

        // 设置适配器，为日历控件提供数据
        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar_date_view, null);
                }
                TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                TextView text = (TextView) convertView.findViewById(R.id.text);
                View redPoint = (View) convertView.findViewById(R.id.redPoint);
                text.setText("" + bean.day); // 显示公历文字
                if (bean.mothFlag != 0) { // 本月日期文字颜色
                    text.setTextColor(0xff9299a1);
                } else { // 非本月日期文字颜色
                    text.setTextColor(0xff444444);
                }
                chinaText.setText(bean.chinaDay); // 显示农历文字
                if (hasEvents(bean)) {  // 判断该日期是否有日历事件
                    redPoint.setVisibility(View.VISIBLE);// 有日历事件的日期下方显示红点标记
                } else {
                    redPoint.setVisibility(View.GONE);// 没有日历事件的日期下方不显示红点
                }
                return convertView;
            }
        });

        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                String month = (bean.moth < 10) ? ("0" + bean.moth) : ("" + bean.moth);
                String day = (bean.day < 10) ? ("0" + bean.day) : ("" + bean.day);
                mTitle.setText(bean.year + "-" + month + "-" + day);
                selectedDate = bean.year + "-" + month + "-" + day;
                //mTitle.setText(bean.year + "-" + bean.moth + "-" + bean.day);
                showEvents(""+bean.year, month, day);
            }
        });

        int[] data = CalendarUtil.getYMD(new Date());
        String month = (data[1] < 10) ? ("0" + data[1]) : ("" + data[1]);
        String day = (data[2] < 10) ? ("0" + data[2]) : ("" + data[2]);
        mTitle.setText(data[0] + "-" + month + "-" + day);
    }
    private void showEvents(String year, String month, String day) {
        String tempDate = year + "-" + month + "-" + day;
        String tempDate1 = month + "-" + day;
        String user_id = UserUtils.getUserId(getActivity()); //获取用户Id，本地用户Id默认为“unknown”
        // 获取查询日期的纪念日事件、日程安排事件、日记事件
        List<AnniversaryDay> list_anniversary = AnniversaryUtils.queryAllLocalAnniversaryInDate(getActivity(), user_id, tempDate1);
        List<Schedule> list_schedule = ScheduleUtils.queryAllLocalScheduleInDate(getActivity(), user_id, tempDate);
        List<Diary> list_diary = DiaryUtils.queryAllLocalDiaryInDate(getActivity(), user_id, tempDate);
        List<AccountBill> list_accountBill = AccountBillUtils.queryAllLocalAccountBillInDate(getActivity(), user_id, tempDate);
        // 为listView设置适配器，根据事件类型显示不同的布局Item
        listViewAdapter = new ListViewAdapter(getActivity(), list_anniversary, list_schedule, list_diary, list_accountBill);
        listView.setAdapter(listViewAdapter);
    }

    private boolean hasEvents(CalendarBean bean) {
        String month = (bean.moth < 10) ? ("0" + bean.moth) : ("" + bean.moth);
        String day = (bean.day < 10) ? ("0" + bean.day) : ("" + bean.day);
        String tempDate = bean.year + "-" + month + "-" + day;
        String tempDate1 = month + "-" + day;
        /*String user_id = UserUtils.getUserId(getActivity());
        List<AnniversaryDay> list_anniversary = AnniversaryUtils.queryAllLocalAnniversaryInDate(getActivity(), user_id, tempDate1);
        List<Schedule> list_schedule = ScheduleUtils.queryAllLocalScheduleInDate(getActivity(), user_id, tempDate);
        if (list_anniversary.size() + list_schedule.size() > 0) return true;*/
        if (all_anniversary == null || all_anniversary.size() == 0) {
            all_anniversary = new ArrayList<>();
        }
        if (all_schedule == null || all_schedule.size() == 0) {
            all_schedule = new ArrayList<>();
        }
        for (int i = 0; i < all_schedule.size(); i++) {
            if (tempDate.equals(all_schedule.get(i).getScheduleStart().split(" ")[0]))
                return true;
        }
        for (int i = 0; i < all_anniversary.size(); i++) {
            String temp[] = all_anniversary.get(i).getAnniversaryDate().split("-");
            if (tempDate1.equals(temp[1] + "-" + temp[2]))
                return true;
        }
        return false;
    }
    private void addChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("新建"); // 对话框标题
        String[] items = {"日程管理", "我的日记", "周年纪念日", "账单"}; // 对话框的选择列表
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:  // 跳转到日程管理添加页面
                        Intent scheduleIntent = new Intent(getActivity(), ScheduleEditActivity.class);
                        startActivity(scheduleIntent);
                        break;
                    case 1: // 跳转到日记添加页面
                        Intent DiaryIntent = new Intent(getActivity(), DiaryEditActivity.class);
                        startActivity(DiaryIntent);
                        break;
                    case 2: // 跳转到纪念日添加页面
                        Intent AnniversaryIntent = new Intent(getActivity(), AnniversaryEditActivity.class);
                        startActivity(AnniversaryIntent);
                        break;
                    case 3: // 跳转到账单创建页面
                        Intent AccountIntent = new Intent(getActivity(), AccountEditActivity.class);
                        startActivity(AccountIntent);
                        break;
                }
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}
