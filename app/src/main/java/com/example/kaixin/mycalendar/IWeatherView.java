package com.example.kaixin.mycalendar;

/**
 * Created by kaixin on 2018/3/9.
 */

public interface IWeatherView {
    void showWeather();
    void showAir();
    void showList();
    void showNextFiveDay();
    void setVisibility(String where, String timestr, String degr);
    int setWeatherImage(String gif);
}
