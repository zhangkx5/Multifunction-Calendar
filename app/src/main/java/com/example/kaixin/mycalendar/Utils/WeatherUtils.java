package com.example.kaixin.mycalendar.Utils;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by kaixin on 2018/3/9.
 */

public class WeatherUtils {
    private static final String web = "http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather";

    public static ArrayList<String> list = new ArrayList<>();

    public static ArrayList<String> postRequest(String request) {
        HttpURLConnection connection = null;
        try {
            Log.i("Key", "Begin the connection");
            URL url = new URL(web);
            connection = (HttpURLConnection)(url.openConnection());
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            request = URLEncoder.encode(request, "utf-8");
            out.writeBytes("theCityCode="+ request + "&theUserID=");

            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            parseXMLWithPull(response.toString());
            Log.i("Key", response.toString());
        } catch (Exception e) {
            Log.i("Key", "Fail to connect");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
                Log.i("Key", "Disconnect the connection");
            }
        }
        return list;
    }

    private static void parseXMLWithPull(String str_xml) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(str_xml));
            int eventType = parser.getEventType();
            list = new ArrayList<>();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("string".equals(parser.getName())) {
                            String str = parser.nextText();
                            list.add(str);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

