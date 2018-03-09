package com.example.kaixin.mycalendar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kaixin on 2018/2/4.
 */

public class WeatherActivity extends AppCompatActivity implements IWeatherView{

    private WeatherPresenter weatherPresenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView name, change_city, time_update, wendu, shidu, air, degrees, wind, weather_now;
    private ImageView weather_img_now;
    private ListView listView;
    private LinearLayout linearLayout;
    private RecyclerView mRecyclerView;
    private ArrayList<Exponent> listItems;
    private ArrayList<WeatherBean> weather_list;
    private String[] list_exponents = new String[] {
            "紫外线指数", "感冒指数", "穿衣指数", "洗车指数", "运动指数", "空气污染指数"
    };
    private int[] list_img = new int[] {
            R.mipmap.ic_uv, R.mipmap.ic_pill, R.mipmap.ic_shirt, R.mipmap.ic_wash_car,
            R.mipmap.ic_sport, R.mipmap.ic_weather
    };
    private static Context mContext;
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherPresenter = new WeatherPresenter(this);
        mContext = getApplicationContext();
        name = (TextView)findViewById(R.id.name_search);
        change_city = (TextView)findViewById(R.id.change_city);
        listView = (ListView)findViewById(R.id.zs_list);
        time_update = (TextView)findViewById(R.id.time_update);
        linearLayout = (LinearLayout)findViewById(R.id.nowlayout);
        wendu = (TextView)findViewById(R.id.wendu);
        shidu = (TextView)findViewById(R.id.shidu);
        air = (TextView)findViewById(R.id.air_quality);
        wind = (TextView)findViewById(R.id.wind);
        degrees = (TextView)findViewById(R.id.degrees);
        weather_now = (TextView)findViewById(R.id.weather_now);
        weather_img_now = (ImageView)findViewById(R.id.weather_img_now);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(WeatherActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        change_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et_city = new EditText(WeatherActivity.this);
                et_city.setTextColor(Color.rgb(0, 0, 0));
                AlertDialog.Builder builder = new AlertDialog.Builder(WeatherActivity.this);
                builder.setTitle("请输入城市名，如：广州");
                builder.setView(et_city);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (CheckNetwork.isNetworkAvailable(WeatherActivity.this)) {
                            weatherPresenter.postRequest(et_city.getText().toString());
                        } else {
                            Toast.makeText(WeatherActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                        dialogInterface.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        if (CheckNetwork.isNetworkAvailable(WeatherActivity.this)) {
            String request = "广州";
            PackageManager pm = WeatherActivity.this.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED ==
                    pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", "com.example.kaixin.mycalendar"));
            if (permission) {
                LocationUtils.getCNBylocation(WeatherActivity.this);
                request = LocationUtils.cityName;
            }
            weatherPresenter.postRequest(request);
        } else {
            Toast.makeText(WeatherActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
        }

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshWeather();
            }
        });
    }

    public void refreshWeather() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        if (CheckNetwork.isNetworkAvailable(WeatherActivity.this)) {
            String request = name.getText().toString();
            if ("".equals(request)) {
                request = "广州";
                PackageManager pm = WeatherActivity.this.getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED ==
                        pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", "com.example.kaixin.mycalendar"));
                if (permission) {
                    LocationUtils.getCNBylocation(WeatherActivity.this);
                    request = LocationUtils.cityName;
                }
            }
            weatherPresenter.postRequest(request);
        } else {
            Toast.makeText(WeatherActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setVisibility(String where, String timestr, String degr) {
        linearLayout.setVisibility(View.VISIBLE);
        name.setText(where);
        time_update.setText(timestr.substring(11, timestr.length()) + " 更新");
        degrees.setText(degr);
    }
    @Override
    public void showWeather() {
        ArrayList<String> response = weatherPresenter.getResponse();
        String weather = response.get(4);
        if (!"今日天气实况：暂无实况".equals(weather)) {
            Pattern p2 = Pattern.compile("(：+?)(.*?)((；+?)|$)");
            Matcher m2 = p2.matcher(weather);
            ArrayList<String> weathers = new ArrayList<>();
            while (m2.find()) {
                weathers.add(m2.group());
            }
            wendu.setText(weathers.get(0).substring(4,weathers.get(0).length()-1));
            wind.setText(weathers.get(1).substring(1, weathers.get(1).length()-1));
            shidu.setText("湿度：" + weathers.get(2).substring(1, weathers.get(2).length()));
        } else {
            wendu.setText("暂无");
            wind.setText("风力：暂无");
            shidu.setText("湿度：暂无");
        }
    }
    @Override
    public void showAir() {
        ArrayList<String> response = weatherPresenter.getResponse();
        String airQ = response.get(5);
        if (!"空气质量：暂无预报；紫外线强度：暂无预报".equals(airQ)) {
            Pattern p3 = Pattern.compile("(：+?)(.*?)(。+?)|$");
            Matcher m3 = p3.matcher(airQ);
            m3.find();
            m3.find();
            air.setText("空气质量：" + m3.group().substring(1, m3.group().length()-1));
        } else {
            air.setText("空气质量：暂无预报");
        }
    }
    @Override
    public void showList() {
        ArrayList<String> response = weatherPresenter.getResponse();
        String exponent = response.get(6);
        if (!"暂无预报".equals(exponent)) {
            Pattern p1 = Pattern.compile("(：+?)(.*?)(。+?)");
            Matcher m1 = p1.matcher(exponent);
            ArrayList<String> list_content = new ArrayList<>();
            while (m1.find()) {
                list_content.add(m1.group());
            }
            listItems = new ArrayList<Exponent>();
            for (int i = 0; i < list_exponents.length; i++) {
                String temp = list_content.get(i).substring(1, list_content.get(i).length());
                String[] t = temp.split("，");
                temp = temp.replace(t[0],"");
                Exponent ex = new Exponent(list_img[i], list_exponents[i],t[0], temp.substring(1, temp.length()-1));
                listItems.add(ex);
            }
        } else {
            listItems = new ArrayList<>();
            for (int i = 0; i < list_exponents.length; i++) {
                Exponent ex = new Exponent(list_img[i], list_exponents[i],"暂无", "");
                listItems.add(ex);
            }
        }
        ExponentAdapter exponentAdapter = new ExponentAdapter(WeatherActivity.this, listItems);
        listView.setAdapter(exponentAdapter);
        setListViewHeightBasedOnChildren(listView);
    }
    @Override
    public void showNextFiveDay() {
        ArrayList<String> response = weatherPresenter.getResponse();
        weather_img_now.setImageResource(setWeatherImage(response.get(11)));
        weather_now.setText(response.get(7).substring(response.get(7).indexOf(" ")+1, response.get(7).length()));
        weather_list = new ArrayList<WeatherBean>();
        WeatherBean first = new WeatherBean(response.get(7).substring(0,response.get(7).indexOf("日")+2),
                response.get(8),
                setWeatherImage(response.get(11)),
                response.get(7).substring(response.get(7).indexOf(" ")+1, response.get(7).length()));
        weather_list.add(first);
        WeatherBean second = new WeatherBean(response.get(12).substring(0,response.get(7).indexOf("日")+2),
                response.get(13),
                setWeatherImage(response.get(16)),
                response.get(12).substring(response.get(7).indexOf(" ")+1, response.get(12).length()));
        weather_list.add(second);
        WeatherBean third = new WeatherBean(response.get(17).substring(0,response.get(7).indexOf("日")+2),
                response.get(18),
                setWeatherImage(response.get(21)),
                response.get(17).substring(response.get(7).indexOf(" ")+1, response.get(17).length()));
        weather_list.add(third);
        WeatherBean fourth = new WeatherBean(response.get(22).substring(0,response.get(7).indexOf("日")+2),
                response.get(23),
                setWeatherImage(response.get(26)),
                response.get(22).substring(response.get(7).indexOf(" ")+1, response.get(22).length()));
        weather_list.add(fourth);
        WeatherBean fifth = new WeatherBean(response.get(27).substring(0,response.get(7).indexOf("日")+2),
                response.get(28),
                setWeatherImage(response.get(31)),
                response.get(27).substring(response.get(7).indexOf(" ")+1, response.get(27).length()));
        weather_list.add(fifth);
        mRecyclerView.setAdapter(new WeatherAdapter(WeatherActivity.this, weather_list));
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    @Override
    public int setWeatherImage(String gif) {
        return weatherPresenter.setImage(gif);
    }

    public static Context getAppContext() {
        return mContext;
    }
}
