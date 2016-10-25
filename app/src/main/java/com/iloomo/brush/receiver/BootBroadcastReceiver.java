package com.iloomo.brush.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.iloomo.brush.StartMain;
import com.iloomo.brush.service.IloomoBrushService;
import com.iloomo.brush.utils.AlarmUtils;
import com.iloomo.utils.L;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by wupeitao on 16/3/2.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            L.e("程序开始啦！");
            Intent mainActivityIntent = new Intent(context, StartMain.class);  // 要启动的Activity
            mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
//            AlarmUtils.getInstance(context).startAlarm();
        }
    }

}
