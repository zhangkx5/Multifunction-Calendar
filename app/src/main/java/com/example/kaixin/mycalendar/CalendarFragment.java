package com.example.kaixin.mycalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarUtil;
import com.codbking.calendar.CalendarView;
import com.example.kaixin.mycalendar.Adapter.AnniversaryAdapter;
import com.example.kaixin.mycalendar.Adapter.ScheduleAdapter;
import com.example.kaixin.mycalendar.Bean.AnniversaryDay;
import com.example.kaixin.mycalendar.Bean.Diary;
import com.example.kaixin.mycalendar.Bean.Schedule;
import com.example.kaixin.mycalendar.Utils.AnniversaryUtils;
import com.example.kaixin.mycalendar.Utils.DiaryUtils;
import com.example.kaixin.mycalendar.Utils.MyDatabaseHelper;
import com.example.kaixin.mycalendar.Utils.ScheduleUtils;
import com.example.kaixin.mycalendar.Utils.UserUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by kaixin on 2018/3/23.
 */

//public class CalendarFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener{
public class CalendarFragment extends Fragment {
    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private ImageView ib_add;
    private String selectedDate;

    private AnniversaryAdapter anniversaryAdapter;
    private ScheduleAdapter scheduleAdapter;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    //private List<AnniversaryDay> list_anniversary = new ArrayList<>();
    private List<Schedule> list_schedule = new ArrayList<>();

    private TextView mTitle;
    private CalendarDateView mCalendarDateView;
    //private ListView mList;


    private LinearLayout ll_weather;
    private TextView date, address, tv_weather, temperature, range;
    private ImageView  img_weather;

    @Override
    public void onResume() {
        super.onResume();
        CaledarAdapter myCalendarAdapter = new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar_date_view, null);
                }
                TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                TextView text = (TextView) convertView.findViewById(R.id.text);
                View redPoint = (View) convertView.findViewById(R.id.redPoint);
                text.setText("" + bean.day);
                if (bean.mothFlag != 0) {
                    text.setTextColor(0xff9299a1);
                } else {
                    text.setTextColor(0xff444444);
                }
                chinaText.setText(bean.chinaDay);
                if (hasEvents(bean)) {
                    redPoint.setVisibility(View.VISIBLE);
                } else {
                    redPoint.setVisibility(View.GONE);
                }
                return convertView;
            }
        };
        mCalendarDateView.setAdapter(myCalendarAdapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        ib_add = (ImageView)view.findViewById(R.id.ib_add);
        ib_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createChoice();
            }
        });

        mTitle = (TextView)view.findViewById(R.id.title);
        mCalendarDateView = (CalendarDateView)view.findViewById(R.id.calendarDateView);


        /*
        ll_weather = (LinearLayout)view.findViewById(R.id.ll_weather);
        date = (TextView)view.findViewById(R.id.date);
        tv_weather = (TextView)view.findViewById(R.id.tv_weather);
        temperature = (TextView)view.findViewById(R.id.temperature);
        range = (TextView)view.findViewById(R.id.range);
        address = (TextView)view.findViewById(R.id.address);
        img_weather = (ImageView)view.findViewById(R.id.img_weather);*/



        listView = (ListView) view.findViewById(R.id.listView);
        initView();
        showEvents(mTitle.getText().toString());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int type = listViewAdapter.getItemViewType(i);
                switch (type) {
                    case 0:
                        AnniversaryDay anniversaryDay = (AnniversaryDay)listViewAdapter.getItem(i);
                        Intent intent1 = new Intent(getActivity(), AnniversaryDetailsActivity.class);
                        intent1.putExtra("anniversaryDay",anniversaryDay);
                        startActivity(intent1);
                        break;
                    case 1:
                        Schedule schedule = (Schedule)listViewAdapter.getItem(i);
                        Intent intent2 = new Intent(getActivity(), ScheduleEditActivity.class);
                        intent2.putExtra("schedule", schedule);
                        startActivity(intent2);
                        break;
                    case 2:
                        Diary diary = (Diary)listViewAdapter.getItem(i);
                        Intent intent3 = new Intent(getActivity(), DiaryEditActivity.class);
                        intent3.putExtra("diary", diary);
                        startActivity(intent3);
                        break;
                    case 3:
                        createChoice();
                }
            }
        });
        return view;
    }

    private void createChoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("新建");
        String[] items = {"日程管理", "我的日记", "周年纪念日", "账单"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        Intent scheduleIntent = new Intent(getActivity(), ScheduleEditActivity.class);
                        startActivity(scheduleIntent);
                        dialogInterface.dismiss();
                        break;
                    case 1:
                        Intent DiaryIntent = new Intent(getActivity(), DiaryEditActivity.class);
                        startActivity(DiaryIntent);
                        dialogInterface.dismiss();
                        break;
                    case 2:
                        Intent AnniversaryIntent = new Intent(getActivity(), AnniversaryEditActivity.class);
                        startActivity(AnniversaryIntent);
                        dialogInterface.dismiss();
                        break;
                    case 3:
                        Intent AccountIntent = new Intent(getActivity(), AccountEditActivity.class);
                        startActivity(AccountIntent);
                        dialogInterface.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
        builder.show();
    }

   /* private List<String> weatherList = new ArrayList<>();
    public void showTodayWeather(final String address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                weatherList = WeatherUtils.postRequest(address);
                String result = weatherList.get(0);
                if ("查询结果为空".equals(result)) {
                    message.what = 1;
                    Toast.makeText(WeatherActivity.getAppContext(), "当前城市不存在，请重新输入", Toast.LENGTH_SHORT).show();
                } else if ("发现错误：免费用户不能使用高速访问。http://www.webxml.com.cn/".equals(result)){
                    message.what = 1;
                    Toast.makeText(WeatherActivity.getAppContext(), "您的点击速度过快，二次查询间隔<600ms", Toast.LENGTH_SHORT).show();
                } else if ("发现错误：免费用户 24 小时内访问超过规定数量。http://www.webxml.com.cn/".equals(result)) {
                    message.what = 1;
                    Toast.makeText(WeatherActivity.getAppContext(), "免费用户24小时内访问超过规定数量50次", Toast.LENGTH_SHORT).show();
                } else {
                    if (weatherList.size() > 28) {
                        List<String> weather = new ArrayList<String>();
                        message.what = 0;
                        message.obj = weatherList;
                    } else {
                        message.what = 1;
                        Toast.makeText(getActivity(), weatherList.get(0), Toast.LENGTH_SHORT).show();
                    }
                }
                handler.sendMessage(message);
            }
        }).start();

    }
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    weatherList = (ArrayList<String>)msg.obj;
                    //String result = weatherList.get(0);
                    String weather = weatherList.get(4);
                    if (!"今日天气实况：暂无实况".equals(weather)) {
                        Pattern p2 = Pattern.compile("(：+?)(.*?)((；+?)|$)");
                        Matcher m2 = p2.matcher(weather);
                        ArrayList<String> weathers = new ArrayList<>();
                        while (m2.find()) {
                            weathers.add(m2.group());
                        }
                        temperature.setText(weathers.get(0).substring(4,weathers.get(0).length()-1));
                    }
                    range.setText(weatherList.get(8));
                    weatherView.setVisibility(weatherList.get(1), weatherList.get(3), weatherList.get(8));
                    weatherView.showWeather();
                    weatherView.showAir();
                    weatherView.showList();
                    weatherView.showNextFiveDay();

                    break;
                case 1:
                    ll_weather.setVisibility(View.GONE);
                default:
                    break;
            }
        }
    };*/

    /*private void initList() {
        mList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_1, null);
                }

                TextView textView = (TextView) convertView;
                textView.setText("position:" + position);

                return convertView;
            }
        });
    }*/

    private void initView() {

        CaledarAdapter myCalendarAdapter = new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.item_calendar_date_view, null);
                }
                TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                TextView text = (TextView) convertView.findViewById(R.id.text);
                View redPoint = (View) convertView.findViewById(R.id.redPoint);
                text.setText("" + bean.day);
                if (bean.mothFlag != 0) {
                    text.setTextColor(0xff9299a1);
                } else {
                    text.setTextColor(0xff444444);
                }
                chinaText.setText(bean.chinaDay);
                if (hasEvents(bean)) {
                    redPoint.setVisibility(View.VISIBLE);
                } else {
                    redPoint.setVisibility(View.GONE);
                }
                return convertView;
            }
        };
        mCalendarDateView.setAdapter(myCalendarAdapter);

        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
                String month = (bean.moth < 10) ? ("0" + bean.moth) : ("" + bean.moth);
                String day = (bean.day < 10) ? ("0" + bean.day) : ("" + bean.day);
                mTitle.setText(bean.year + "-" + month + "-" + day);
                selectedDate = bean.year + "-" + month + "-" + day;
                //mTitle.setText(bean.year + "-" + bean.moth + "-" + bean.day);
                showEvents(selectedDate);

            }
        });

        int[] data = CalendarUtil.getYMD(new Date());
        String month = (data[1] < 10) ? ("0" + data[1]) : ("" + data[1]);
        String day = (data[2] < 10) ? ("0" + data[2]) : ("" + data[2]);
        mTitle.setText(data[0] + "-" + month + "-" + day);
        //showEvents(data[0] + "-" + month + "-" + day);
        //mTitle.setText(data[0] + "-" + data[1] + "-" + data[2]);
    }
    private void showEvents(String date) {
        String user_id = UserUtils.getUserId(getActivity());
        List<AnniversaryDay> list_anniversary = AnniversaryUtils.queryAllLocalAnniversaryInDate(getActivity(), user_id, date);
        List<Schedule> list_schedule = ScheduleUtils.queryAllLocalScheduleInDate(getActivity(), user_id, date);
        List<Diary> list_diary = DiaryUtils.queryAllLocalDiaryInDate(getActivity(), user_id, date);
        listViewAdapter = new ListViewAdapter(getActivity(), list_anniversary, list_schedule, list_diary);
        listView.setAdapter(listViewAdapter);
    }

    private boolean hasEvents(CalendarBean bean) {
        String month = (bean.moth < 10) ? ("0" + bean.moth) : ("" + bean.moth);
        String day = (bean.day < 10) ? ("0" + bean.day) : ("" + bean.day);
        String tempDate = bean.year + "-" + month + "-" + day;
        String user_id = UserUtils.getUserId(getActivity());
        List<AnniversaryDay> list_anniversary = AnniversaryUtils.queryAllLocalAnniversaryInDate(getActivity(), user_id, tempDate);
        List<Schedule> list_schedule = ScheduleUtils.queryAllLocalScheduleInDate(getActivity(), user_id, tempDate);
        /*myDatabaseHelper = new MyDatabaseHelper(getActivity());
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "schedule_start LIKE ?";
        String[] selectionArgs = new String[]{"%" + tempDate + "%"};
        Cursor cursor = dbRead.query(ScheduleUtils.SCHEDULE_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            dbRead.close();
            return true;
        }
        cursor.close();
        dbRead.close();*/
        if (list_anniversary.size() + list_schedule.size() > 0) return true;
        return false;
    }
}
