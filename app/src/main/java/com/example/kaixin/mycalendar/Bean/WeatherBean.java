package com.example.kaixin.mycalendar.Bean;

/**
 * Created by kaixin on 2018/3/9.
 */

public class WeatherBean {
    private String date;
    private String weather;
    private String temperature;
    private int img;

    public WeatherBean(String date, String temperature, int img, String weather) {
        this.date = date;
        this.weather = weather;
        this.temperature = temperature;
        this.img = img;
    }

    public String getDate() {
        return date;
    }
    public String getWeather() {
        return weather;
    }
    public String getTemperature() {
        return temperature;
    }
    public int getImg() {
        return img;
    }
}
