package com.example.kaixin.mycalendar;

import java.util.ArrayList;

/**
 * Created by kaixin on 2018/3/9.
 */

public interface IWeatherPresenter {
    ArrayList<String> getResponse();
    void postRequest(final String request);
    int setImage(String gif);
}
