package com.example.kaixin.mycalendar;

/**
 * Created by kaixin on 2018/3/9.
 */

public class WeatherModel implements IWeatherModel{
    @Override
    public int findImage(String gif) {
        int image;
        switch (gif) {
            case "0.gif":
                image = R.mipmap.weather_sunny;
                break;
            case "1.gif":
                image = R.mipmap.weather_cloudy;
                break;
            case "2.gif":
                image = R.mipmap.weather_overcast;
                break;
            case "3.gif":
                image = R.mipmap.weather_bigrain;
                break;
            case "4.gif":
            case "5.gif":
                image = R.mipmap.weather_thundershower;
                break;
            case "6.gif":
                image = R.mipmap.weather_bigrain;
                break;
            case "7.gif":
                image = R.mipmap.weather_smallrain;
                break;
            case "8.gif":
                image = R.mipmap.weather_midrain;
                break;
            case "9.gif":
                image = R.mipmap.weather_bigrain;
                break;
            case "10.gif":
                image = R.mipmap.weather_rainstorm;
                break;
            case "11.gif":
                image = R.mipmap.weather_rainstorm;
                break;
            case "12.gif":
                image = R.mipmap.weather_rainstorm;
                break;
            case "13.gif":
                image = R.mipmap.weather_bigsnow;
                break;
            case "14.gif":
                image = R.mipmap.weather_smallsnow;
                break;
            case "15.gif":
                image = R.mipmap.weather_midsnow;
                break;
            case "16.gif":
                image = R.mipmap.weather_bigrain;
                break;
            case "17.gif":
                image = R.mipmap.weather_snowstorm;
                break;
            case "18.gif":
                image = R.mipmap.weather_fog;
                break;
            case "19.gif":
                image = R.mipmap.weather_midrain;
                break;
            case "20.gif":
                image = R.mipmap.weather_sand;
                break;
            case "21.gif":
                image = R.mipmap.weather_midrain;
                break;
            case "22.gif":
                image = R.mipmap.weather_bigrain;
                break;
            case "23.gif":
                image = R.mipmap.weather_rainstorm;
                break;
            case "24.gif":
                image = R.mipmap.weather_rainstorm;
                break;
            case "25.gif":
                image = R.mipmap.weather_rainstorm;
                break;
            case "26.gif":
                image = R.mipmap.weather_midsnow;
                break;
            case "27.gif":
                image = R.mipmap.weather_bigsnow;
                break;
            case "28.gif":
                image = R.mipmap.weather_snowstorm;
                break;
            case "29.gif":
                image = R.mipmap.weather_sand;
                break;
            case "30.gif":
                image = R.mipmap.weather_sand;
                break;
            case "31.gif":
                image = R.mipmap.weather_sand;
                break;
            default:
                image = R.mipmap.weather_overcast;
                break;
        }
        return image;
    }
}
