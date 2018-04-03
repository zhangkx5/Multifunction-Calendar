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
import com.example.kaixin.mycalendar.Bean.Schedule;
import com.example.kaixin.mycalendar.Utils.AnniversaryUtils;
import com.example.kaixin.mycalendar.Utils.MyDatabaseHelper;
import com.example.kaixin.mycalendar.Utils.ScheduleUtils;

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
    private List<Schedule> list_schedule;
    private ListView lv_anniversary, lv_schedule;
    private List<AnniversaryDay> list_anniversary = new ArrayList<>();

    private TextView mTitle;
    private CalendarDateView mCalendarDateView;
    //private ListView mList;


    private LinearLayout ll_weather;
    private TextView date, address, tv_weather, temperature, range;
    private ImageView  img_weather;

    @Override
    public void onResume() {
        super.onResume();
        /*int[] data = CalendarUtil.getYMD(new Date());
        String month = (data[1] < 10 ? "0" + data[1] : "" + data[1]);
        String day = (data[2] < 10 ? "0" + data[2] : "" + data[2]);
        mTitle.setText(data[0] + "-" + month + "-" + day);
        selectedDate = data[0] + "-" + month + "-" + day;
        list_anniversary = getAnniversaryList(selectedDate);
        anniversaryAdapter = new AnniversaryAdapter(getActivity(), list_anniversary);
        lv_anniversary.setAdapter(anniversaryAdapter);
        list_schedule = getScheduleList(selectedDate);
        scheduleAdapter = new ScheduleAdapter(getActivity(), list_schedule);
        lv_schedule.setAdapter(scheduleAdapter);
        setListViewHeightBaseOnChildren(lv_anniversary);
        setListViewHeightBaseOnChildren(lv_schedule);*/
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
        //mList = (ListView)view.findViewById(R.id.list);
        initView();
        //initList();



        /*
        ll_weather = (LinearLayout)view.findViewById(R.id.ll_weather);
        date = (TextView)view.findViewById(R.id.date);
        tv_weather = (TextView)view.findViewById(R.id.tv_weather);
        temperature = (TextView)view.findViewById(R.id.temperature);
        range = (TextView)view.findViewById(R.id.range);
        address = (TextView)view.findViewById(R.id.address);
        img_weather = (ImageView)view.findViewById(R.id.img_weather);*/



        lv_anniversary = (ListView) view.findViewById(R.id.lv_anniversary);
        lv_schedule = (ListView) view.findViewById(R.id.lv_schedule);

        lv_anniversary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AnniversaryDay anniversaryDay = anniversaryAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), AnniversaryDetailsActivity.class);
                intent.putExtra("anniversaryDay",anniversaryDay);
                startActivity(intent);
            }
        });
        lv_schedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Schedule schedule = scheduleAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), ScheduleEditActivity.class);
                intent.putExtra("schedule", schedule);
                startActivity(intent);
            }
        });
        return view;
    }
    private MyDatabaseHelper myDatabaseHelper;
    public List<AnniversaryDay> getAnniversaryList(String date) {
        Log.i("getAnniversaryList", "运行到这里了");
        myDatabaseHelper = new MyDatabaseHelper(getActivity());
        List<AnniversaryDay> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "anniversary_date LIKE ?";
        String[] selectionArgs = new String[]{"%" + date + "%"};
        Cursor cursor = dbRead.query(AnniversaryUtils.ANNIVERSARY_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
            String anniversary_name = cursor.getString(cursor.getColumnIndex("anniversary_name"));
            String anniversary_date = cursor.getString(cursor.getColumnIndex("anniversary_date"));
            String anniversary_notes = cursor.getString(cursor.getColumnIndex("anniversary_notes"));
            AnniversaryDay anniversaryDay = new AnniversaryDay();
            anniversaryDay.setObjectId(id);
            anniversaryDay.setUserId(user_id);
            anniversaryDay.setAnniversaryName(anniversary_name);
            anniversaryDay.setAnniversaryDate(anniversary_date);
            anniversaryDay.setAnniversaryNotes(anniversary_notes);
            result.add(anniversaryDay);
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(result);
        Log.i("getAnniversaryList", "运行到那里了");
        return result;
    }
    public List<Schedule> getScheduleList(String date) {
        myDatabaseHelper = new MyDatabaseHelper(getActivity());
        List<Schedule> result = new ArrayList<>();
        SQLiteDatabase dbRead = myDatabaseHelper.getReadableDatabase();
        String selection = "schedule_start LIKE ?";
        String[] selectionArgs = new String[]{"%" + date + "%"};
        Cursor cursor = dbRead.query(ScheduleUtils.SCHEDULE_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
            String title = cursor.getString(cursor.getColumnIndex("schedule_title"));
            String address = cursor.getString(cursor.getColumnIndex("schedule_address"));
            String start = cursor.getString(cursor.getColumnIndex("schedule_start"));
            String end = cursor.getString(cursor.getColumnIndex("schedule_end"));
            String call = cursor.getString(cursor.getColumnIndex("schedule_call"));
            String notes = cursor.getString(cursor.getColumnIndex("schedule_notes"));
            Schedule schedule = new Schedule();
            schedule.setObjectId(id);
            schedule.setUserId(user_id);
            schedule.setScheduleTitle(title);
            schedule.setScheduleAddress(address);
            schedule.setScheduleStart(start);
            schedule.setScheduleEnd(end);
            schedule.setScheduleNotes(notes);
            result.add(schedule);
        }
        cursor.close();
        dbRead.close();
        Collections.reverse(result);
        return result;
    }
    public void setListViewHeightBaseOnChildren(ListView lv) {
        ListAdapter listAdapter = lv.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null ,lv);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = lv.getLayoutParams();
        params.height = totalHeight + (lv.getDividerHeight() * (listAdapter.getCount() - 1));
        lv.setLayoutParams(params);
    }

    /*@Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        selectedDate = getSelectedDatesString();
        textView.setText(getSelectedDatesString());
        list_anniversary.clear();
        list_anniversary = getAnniversaryList(selectedDate);
        anniversaryAdapter = new AnniversaryAdapter(getActivity(), list_anniversary);
        lv_anniversary.setAdapter(anniversaryAdapter);
        list_schedule.clear();
        list_schedule = getScheduleList(selectedDate);
        scheduleAdapter = new ScheduleAdapter(getActivity(), list_schedule);
        lv_schedule.setAdapter(scheduleAdapter);
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        //textView.setText("monthchange");
    }*/
/*
    private String getSelectedDatesString() {
        CalendarDay date = mcv.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        //SimpleDateFormat sdf = new SimpleDateFormat("M月d日");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date.getDate());
        //return FORMATTER.format(date.getDate());
    }*/

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
                /*if (hasEvents(bean)) {
                    redPoint.setVisibility(View.VISIBLE);
                } else {
                    redPoint.setVisibility(View.GONE);
                }*/
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
                Log.i("selectedDate", selectedDate);
                if (list_anniversary.size() != 0) list_anniversary.clear();
                list_anniversary = getAnniversaryList(selectedDate);
                anniversaryAdapter = new AnniversaryAdapter(getActivity(), list_anniversary);
                lv_anniversary.setAdapter(anniversaryAdapter);
                setListViewHeightBaseOnChildren(lv_anniversary);
                /*list_schedule.clear();
                list_schedule = getScheduleList(selectedDate);
                scheduleAdapter = new ScheduleAdapter(getActivity(), list_schedule);
                lv_schedule.setAdapter(scheduleAdapter);*/
            }
        });

        int[] data = CalendarUtil.getYMD(new Date());
        String month = (data[1] < 10) ? ("0" + data[1]) : ("" + data[1]);
        String day = (data[2] < 10) ? ("0" + data[2]) : ("" + data[2]);
        mTitle.setText(data[0] + "-" + month + "-" + day);
        //mTitle.setText(data[0] + "-" + data[1] + "-" + data[2]);
    }
    private boolean hasEvents(CalendarBean bean) {
        String month = (bean.moth < 10) ? ("0" + bean.moth) : ("" + bean.moth);
        String day = (bean.day < 10) ? ("0" + bean.day) : ("" + bean.day);
        String tempDate = bean.year + "-" + month + "-" + day;
        myDatabaseHelper = new MyDatabaseHelper(getActivity());
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
        dbRead.close();
        return false;
    }
}
