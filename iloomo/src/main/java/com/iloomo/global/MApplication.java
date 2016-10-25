package com.iloomo.global;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.baidu.mobstat.StatService;
import com.iloomo.db.DbHelperBase;
import com.squareup.otto.Bus;
import com.umeng.analytics.MobclickAgent;


/**
 * @author wpt
 */
public class MApplication extends Application {
    private static List<Activity> activityList = new LinkedList<Activity>();
    //    public static  String VERSION;
    public static Context context;
    private static MApplication app;
    public static String DBMANE = "iloomo.db";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        context = getApplicationContext();
        app = this;
        checkDataBase();
        // 调试百度统计SDK的Log开关，可以在Eclipse中看到sdk打印的日志，发布时去除调用，或者设置为false
        StatService.setDebugOn(true);
        /***
         * 友盟统计
         */
        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);
    }

    public static synchronized MApplication getInstance() {
        return app;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        MobclickAgent.onKillProcess(context);
        System.gc();
        System.exit(0);
    }


    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = getSDPath() + DBMANE;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            //database does't exist yet.
            File dbFile = new File(getSDPath());
            if (!dbFile.exists()) {
                dbFile.mkdirs();
            }
            File dbFiles = new File(getSDPath() + DBMANE);
            try {
                dbFiles.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        } else {
            return "";
        }
        String replace = MApplication.context.getPackageName();
        String path = sdDir.toString() + "/Android/data/" + replace + "/database/";
        File dbFolder = new File(path);
        // 目录不存在则自动创建目录
        if (!dbFolder.exists()) {
            dbFolder.mkdirs();
        }
        return path;
    }


}
