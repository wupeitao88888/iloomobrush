package com.iloomo.brush.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.iloomo.brush.receiver.AlarmBroadcastReceiver;

/**
 * Created by wupeitao on 16/3/3.
 */
public class AlarmUtils {
    public static AlarmUtils alarmUtils;
    public Context context;

    private AlarmManager alarmManager;
    private Intent intent;
    private PendingIntent pi;
    public static int sleep = 6;

    public AlarmUtils(Context context) {
        this.context = context;
    }

    public static AlarmUtils getInstance(Context context) {
        if (alarmUtils == null) {
            alarmUtils = new AlarmUtils(context);
        }
        return alarmUtils;
    }


    public void startAlarm() {
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("Action.Alarm");
        pi = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // 只执行一次
        // alarmManager.set(AlarmManager.RTC_WAKEUP,
        // System.currentTimeMillis(), pi);
        // 每隔1秒执行一次
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), sleep* 1000, pi);
    }

    public void cancelAlarm() {
        // 取消闹钟
        alarmManager.cancel(pi);
    }


}
