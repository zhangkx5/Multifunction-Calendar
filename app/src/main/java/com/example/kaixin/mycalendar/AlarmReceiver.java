package com.example.kaixin.mycalendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by kaixin on 2018/4/6.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        String id = intent.getStringExtra("id");
        Intent intent1 = new Intent(context,MainActivity.class);
        intent1.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_about)
                .setContentTitle("生活日历")
                .setContentText("您有日历事件提醒哦")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(1, builder.build());
    }
}
