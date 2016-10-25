package com.iloomo.brush.receiver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.iloomo.bean.BaseModel;
import com.iloomo.brush.R;
import com.iloomo.brush.bean.ExeTime;
import com.iloomo.brush.bean.MpData;
import com.iloomo.brush.bean.PackageData;
import com.iloomo.brush.bean.Task;
import com.iloomo.brush.bean.VpnData;
import com.iloomo.brush.db.DBControl;
import com.iloomo.brush.db.DbHelper;
import com.iloomo.brush.global.AppConfig;
import com.iloomo.brush.utils.AlarmUtils;
import com.iloomo.brush.utils.CMDutils;
import com.iloomo.brush.utils.VPN;
import com.iloomo.global.MApplication;
import com.iloomo.net.AsyncHttpPost;
import com.iloomo.net.BaseRequest;
import com.iloomo.net.DefaultThreadPool;
import com.iloomo.net.ThreadCallBack;
import com.iloomo.net.downloader.Downloader;
import com.iloomo.threadpool.MyThreadPool;
import com.iloomo.utils.BigDecimalUtil;
import com.iloomo.utils.DateUtil;
import com.iloomo.utils.L;
import com.iloomo.utils.LCSharedPreferencesHelper;
import com.iloomo.utils.ToastUtil;
import com.iloomo.vpn.manager.VpnManager;

import com.iloomo.vpn.manager.VpnProfile;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wupeitao on 16/3/2.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION = "Action.Alarm";
    public Context context;
    private static int Timer = 0;
    public static boolean TASK = false;//true 是当前任务正在执行
    public static boolean NETTASK = false;//true 正在请求当前任务
    public static boolean ISUPDATA = false;//true 正在更新
    public static boolean IS_CIRCULATE = false;//true 结束任务之后不循环
    public static boolean ISSLEEP = false;//延时网络请求
    public static boolean ISFRISTVPN = false;//是否是第一次
    private static int sleepcount = 0;//1~10是正常时间请求，
    private static int sleeptime = 0;//多长时间执行一次的计数器
    private static int sleepAll = 100; //耗时操作（每次停留AlarmUtils.sleep分，停留的次数）
    //    private DBControl dbControl;
    private int count = 0;//执行任务的进度

    private int countDownload = 0;//下载任务的进度
    private int countAll = 0;
    public static final int SORT = 1001;
    public static final int TASKING = 1002;
    public static final int DOWNLOAD = 1003;
    public static final int IS_TODAYTASK = 1004;
    public static final int CMDFINISH = 1005;
    public static final int UNINSTALL = 1006;
    public static final int UPDATEFINISH = 1007;


    private LCSharedPreferencesHelper lcSharedPreferencesHelper;
    private int fileLen;
    private ExeTime exeTimesTask;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SORT:
                    ExeTime exeTime = (ExeTime) msg.obj;

                    if (exeTime.getMpdata() == null) {
                        L.e("任务机型为空");
                        updataTodayTask("2", exeTime.getExec_id());
                        TASK = false;
                        return;
                    }
                    if (exeTime.getMpdata().size() <= 0) {
                        L.e("任务机型,数据为空");
                        updataTodayTask("2", exeTime.getExec_id());
                        TASK = false;
                        return;
                    }
                    MpData mpData = exeTime.getMpdata().get(0);
                    String pnumber = exeTime.getPnumber();
                    String exec_id = exeTime.getExec_id();
                    L.e("保存机型数据");
                    saveData(context, mpData, pnumber, exec_id, exeTime.getNetworktype());
                    break;
                case TASKING:
                    exeTimesTask = (ExeTime) msg.obj;
                    L.e("开启单个任务");
                    startTask(exeTimesTask);
                    break;
                case DOWNLOAD:
                    L.e("开启下载循环");
                    ExeTime exeTimeVPN = (ExeTime) msg.obj;
//                    startVpn(exeTimeVPN);
                    downLoad(exeTimeVPN);
                    break;
                case Downloader.FILELENTH:
                    //获取文件的大小
                    fileLen = msg.getData().getInt("fileLen");
                    L.e(fileLen + "");
                    break;

                case IS_TODAYTASK:
                    L.e("读秒中...！");
                    if ((boolean) msg.obj) {
                        if (NETTASK == false) {
                            NETTASK = true;
                            //第一步获取今天的任务列表，如果没有获取到则轮询获取
                            //第二步校验时间
                            L.e(sleepcount + "：计数器sleepcount");
                            L.e(sleeptime + "：计数器sleeptime");
                            if (sleepcount > 10) {
                                sleeptime++;
                                if (sleeptime > sleepAll) {
                                    L.e("开始重启");
                                    CMDutils.getInstance(context).cmd_reboot();
                                }
                                NETTASK = false;
                                return;
                            }
                            sleepcount++;
                            L.e(sleepcount + "：计数器");
                            initData(context);
                        }
                    } else {
                        L.e("任务执行中1...！");
                        if (TASK == false) {
                            TASK = true;
                            L.e("任务执行中2...！");
                            sleepcount = 0;
                            sleeptime = 0;
                            getMinTime();
                            //第六步判断是否需要修改IP，如果需要则执行拨号，拨号成功后，开始依次执行任务。如果不需要修改IP，依次执行任务并且修改任务状态
                            //第七步卸载app,断开VPN，修改今天的任务状态，然后重启，今天的整个任务结束，进入第一步
                        }
                    }
                    break;
                case CMDFINISH:
                    L.e("结束这一个包名的任务服务器端修改");
                    dataUpdata(exeTimesTask.getPackagedata().get(count).getTask_id());
                    L.e("结束这一个包名的任务修改状态为1");
                    break;
                case UNINSTALL:
                    L.e("卸载完成");
                    count++;
                    startTask(exeTimesTask);
                    break;
            }
        }
    };


    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        if (ACTION.equals(intent.getAction())) {
            lcSharedPreferencesHelper = LCSharedPreferencesHelper.instance(context, LCSharedPreferencesHelper.ILOOMO);
            Vibrator vibrator = null;
            if (isNetworkAvailable(context)) {
//                Toast.makeText(getApplicationContext(), "当前有可用网络！", Toast.LENGTH_LONG).show();
//                try {
//                    vibrator.cancel();
//                }catch (Exception e){
//
//                }


            } else {
//                Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
  /*
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         * */
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                long[] pattern = {100, 400, 100, 400};   // 停止 开启 停止 开启
                vibrator.vibrate(pattern, 3);           //重复两次上面的pattern 如果只想震动一次，index设为-1
                return;
            }
            setCleatlog();
            setClearDB();
            isTaskedToday();
            setCheckTodayTask();
        }
    }

    /**
     * 判断今天的任务是否执行
     * IS_CIRCULATE 是否循环请求
     */
    public void isTaskedToday() {
        if (IS_CIRCULATE) {
            if (!TextUtils.isEmpty(lcSharedPreferencesHelper.getValue("tasked"))) {
                if ("true".equals(lcSharedPreferencesHelper.getValue("tasked"))) {
                    return;
                }
            }
        }
    }

    /****
     * 清除日志
     */
    public void setCleatlog() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        String replace = MApplication.context.getPackageName();
        String path = sdDir.toString() + "/Android/data/" + replace + "/document/";
        File dbFolder = new File(path);
        // 目录不存在则自动创建目录
        if (!dbFolder.exists()) {
            dbFolder.mkdirs();
        }
        File fa[] = dbFolder.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {

            } else {
//                System.out.println(fs.getName());
                long oldTime = getTime();
                long nowTime = DateUtil.Date_time(fs.getName().replace("log", ""), DateUtil.dateFormatYMD);
                int offectDay = DateUtil.getOffectDay(oldTime, nowTime);

                if (offectDay > 7) {
                    fs.delete();
                }

            }
        }

        String pathApk = sdDir.toString() + "/Android/data/" + replace + "/apk/";
        File dbFolderApk = new File(pathApk);
        // 目录不存在则自动创建目录
        if (!dbFolderApk.exists()) {
            dbFolderApk.mkdirs();
        }
        long fileSize = getFileSize(dbFolderApk);

        double mul = BigDecimalUtil.mul(1d, 1073741824d);

        if (fileSize > mul) {
            DeleteFile(dbFolderApk);
        }

    }


    /**
     * 得到文件夹下得文件所占用的空间大小
     *
     * @param f 文件夹
     * @return 空间大小
     */
    public long getFileSize(File f) {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /***
     * 删除文件
     *
     * @param f 文件夹
     */
    public void DeleteFile(File f) {

        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                DeleteFile(flist[i]);
            } else {
                flist[i].delete();
            }
        }

    }

    /**
     * 清除数据库（每日一清）
     */
    public void setClearDB() {

        String stringFormat = DateUtil.getStringFormat(getTime(), "HH");
        String sFormat = DateUtil.getStringFormat(getTime(), "mm");
        if ("00".equals(stringFormat) && 00 <= Integer.parseInt(sFormat) && 06 >= Integer.parseInt(sFormat)) {
//            DbHelper dbHelper = new DbHelper(context);
//            DBControl dbControl = new DBControl(context, dbHelper);
//            dbControl.deleteAllTab();

            sleepcount = 0;
            sleeptime = 0;
            try {
                lcSharedPreferencesHelper.putValue("tasked", "false");
            } catch (Exception e) {

            }
        }
    }

    /**
     * 启动安装更新
     *
     * @param urlp
     */
    private void installUpdate(final String urlp) {
        ComponentName componetName = new ComponentName(
                //这个是另外一个应用程序的包名
                "com.iloomo.appcontrol",
                //这个参数是要启动的Activity
                "com.iloomo.appcontrol.MainActivity");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", urlp);
        intent.setComponent(componetName);
        context.startActivity(intent);
    }

    /****
     * 执行monkey任务
     *
     * @param url
     */
    private void cmdExe(final String url) {
        MyThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                L.e("开始安装包");
                int i1 = CMDutils.getInstance(context).cmd_install(url);
                if (i1 > -1) {
                    L.e("执行monkey命令");
//                    int i = CMDutils.getInstance(context).excuteSuCMD("monkey --throttle " + Integer.parseInt(exeTimesTask.getPackagedata().get(count).getTimeonpage()) + " -p " + exeTimesTask.getPackagedata().get(count).getPackage_name() + " -s 500 --ignore-crashes --ignore-timeouts --monitor-native-crashes -v -v 100");
//                    int i = CMDutils.getInstance(context).excuteSuCMD("monkey --throttle " + Integer.parseInt(exeTimesTask.getPackagedata().get(count).getTimeonpage()) + " -p " + exeTimesTask.getPackagedata().get(count).getPackage_name() + " -v " + 100);


                    CMDutils.getInstance(context).excuteSuCMD("input tap 200 1100");
                    int i = CMDutils.getInstance(context).excuteSuCMD("input tap 500 660 ");
                    if (i > -1) {
                        try {
                            L.e("延时操作。。。。。。");
                            Thread.sleep(15 * 1000);
                        } catch (InterruptedException e) {
                        }

                        L.e("monkey成功");
                        Message message = new Message();
                        message.what = CMDFINISH;
                        message.obj = "";
                        handler.sendMessage(message);
                    } else {
                        L.e("monkey失败");
                        Message message = new Message();
                        message.what = CMDFINISH;
                        message.obj = "";
                        handler.sendMessage(message);
                    }
                } else {
                    L.e("monkey失败");
                    Message message = new Message();
                    message.what = CMDFINISH;
                    message.obj = "";
                    handler.sendMessage(message);
                }
            }
        });
    }

    /***
     * 卸载任务
     *
     * @param exec_id
     * @param task_id
     * @param packagename
     */
    public void updateTask(final String exec_id, final String task_id, final String packagename) {
        MyThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                DbHelper dbHelper = new DbHelper(context);
                DBControl dbControl = new DBControl(context, dbHelper);
                dbControl.updatePackagedata(exec_id, task_id);
                L.e("卸载" + packagename);
//                CMDutils.getInstance(context).cmd_uninstall(packagename);
                CMDutils.getInstance(context).cmd_uninstall("net.nat123.wpt");
                Message message = new Message();
                message.what = UNINSTALL;
                message.obj = "";
                handler.sendMessage(message);

            }
        });
    }

    /***
     * 修改任务的状态
     *
     * @param type
     * @param exec_id
     */
    public void updataTodayTask(final String type, final String exec_id) {
        MyThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                DbHelper dbHelper = new DbHelper(context);
                DBControl dbControl = new DBControl(context, dbHelper);
                dbControl.updateExec(type, exec_id);
                if (dbControl.isOverTask(DateUtil.getStringFormat(getTime(), DateUtil.dateFormatYMD))) {
                    L.e("今天的任务执行完毕！");
                    lcSharedPreferencesHelper.putValue("tasked", "true");

                    L.e("++++++++++++++++++++++++清空数据+++++++++++++++++++++");
                    try {
                        dbControl.deleteAllTab();
                    } catch (Exception e) {
                    }
                    L.e("++++++++++++++++++++++++重启恢复+++++++++++++++++++++");
                    CMDutils.getInstance(context).cmd_reboot();

                }
            }
        });
    }


    public void setCheckTodayTask() {
        if (ISUPDATA) {
            return;
        }
        L.e(sleepcount + "：计数器sleepcount");
        L.e(sleeptime + "：计数器sleeptime");
        MyThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                DbHelper dbHelper = new DbHelper(context);
                DBControl dbControl = new DBControl(context, dbHelper);
                boolean todayTask = false;
                try {
                    todayTask = dbControl.isTodayTask(DateUtil.getStringFormat(getTime(), DateUtil.dateFormatYMD));
                } catch (Exception e) {
                    todayTask = true;
                }
                Message message = new Message();
                message.what = IS_TODAYTASK;
                message.obj = todayTask;
                handler.sendMessage(message);
            }
        });
    }


    //第三步分析数据，根据时间从小到大执行程序
    public void getMinTime() {
        MyThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                //重启后，判断出正在执行中得程序
                DbHelper dbHelper = new DbHelper(context);
                DBControl dbControl = new DBControl(context, dbHelper);
                L.e("+++++++++++剩余任务个数：" + dbControl.isTaskSelect(DateUtil.getStringFormat(getTime(), DateUtil.dateFormatYMD)) + "+++++++++++++");
                List<ExeTime> exeTimesIng = dbControl.selectExec("1", DateUtil.getStringFormat(getTime(), DateUtil.dateFormatYMD));
                if (exeTimesIng.size() > 0) {
                    L.e("进入任务");
                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = wifi.getConnectionInfo();
                    ExeTime exeTime = setData(exeTimesIng.get(0), dbControl);

                    L.e("修改后的IMEI：" + tm.getDeviceId() + " MAC:" + info.getMacAddress());
                    try {
                        L.e("本地存储的IMEI：" + exeTime.getMpdata().get(0).getM_apparatus() + " MAC:" + exeTime.getMpdata().get(0).getMac_address());
                    } catch (Exception e) {

                    }

                    Message message = new Message();
                    message.what = DOWNLOAD;
                    message.obj = exeTime;
                    handler.sendMessage(message);
                    return;
                }
                String stringFormat = DateUtil.getStringFormat(getTime(), DateUtil.dateFormatYMD);
                List<ExeTime> exeTimes = dbControl.selectExec("0", stringFormat);
                if (exeTimes.size() == 0) {
                    L.e("当前没有任务");
                    TASK = false;
                    return;
                }
                long min = DateUtil.Date_time(exeTimes.get(0).getExec_date() + " " + exeTimes.get(0).getExec_time(), DateUtil.dateFormatYMDHM);
                int index = 0;
                for (int i = 0; i < exeTimes.size(); i++) {
                    long begin = DateUtil.Date_time(exeTimes.get(i).getExec_date() + " " + exeTimes.get(i).getExec_time(), DateUtil.dateFormatYMDHM);
                    if (begin < min) { // 判断最小值
                        min = begin;
                        index = i;
                    }
                }
                long timelocal = DateUtil.Date_time(exeTimes.get(index).getExec_date() + " " + exeTimes.get(index).getExec_time(), DateUtil.dateFormatYMDHM);
                long timenet = getTime();
                //判断时间有没有到
                L.e("本地时间：" + timelocal + "，执行时间：" + timenet);
                if (timelocal > timenet) {
                    L.e("执行时间未到...");
                    TASK = false;
                    return;
                }
                L.e("排序完成");
                Message message = new Message();
                message.what = SORT;
                message.obj = setData(exeTimes.get(index), dbControl);
                handler.sendMessage(message);
            }
        });
    }

    public ExeTime setData(ExeTime exeTime, DBControl dbControl) {
        List<MpData> mpDatas = dbControl.selectMpData(exeTime.getExec_id());
        List<PackageData> packageDatas = dbControl.selectPackagedata("0", exeTime.getExec_id());
        List<VpnData> vpnDatas = dbControl.selectVpnData(exeTime.getExec_id());
        exeTime.setPackagedata(packageDatas);
        exeTime.setMpdata(mpDatas);
        exeTime.setVpndata(vpnDatas);
        return exeTime;
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Downloader.FILELENTH:
                    //获取文件的大小

                    L.e(msg.getData().getInt("fileLen") + "");
                    break;
                case Downloader.PROGRESS:
                    //获取当前下载的总量
                    int done = msg.getData().getInt("done");
                    L.e(done + "");
                    break;
                case Downloader.ERROR:
                    break;
                case Downloader.FINISH:
                    L.e("下载完成：" + msg.obj.toString() + "");
                    installUpdate(msg.obj.toString());
                    break;
            }
        }
    };

    public void initData(final Context context) {

        Map<String, Object> parameter = new HashMap<String, Object>();
        DbHelper dbHelper = new DbHelper(context);
        final DBControl dbControl = new DBControl(context, dbHelper);
        parameter.put("nowdays", DateUtil.getStringFormat(getTime(), DateUtil.dateFormatYMD));
        parameter.put("mac_address", dbControl.selectPhoneTab());
//        parameter.put("mac_address","351746050838690");

        BaseRequest httpRequest = new AsyncHttpPost(new ThreadCallBack() {
            @Override
            public void onCallbackFromThread(String resultJson, Object modelClass) {

            }

            @Override
            public void onCallBackFromThread(String resultJson, int resultCode, Object modelClass) {
                L.e("" + resultJson);
                final Task task = (Task) modelClass;
                if (!TextUtils.isEmpty(task.getCode())) {
                    if (!"200".equals(task.getCode())) {
                        if (dbControl.isOverTask(DateUtil.getStringFormat(getTime(), DateUtil.dateFormatYMD))) {
                            NETTASK = false;
                        }
                        return;
                    }
                }

                if (!TextUtils.isEmpty(task.getAppversion())) {
                    if (Integer.parseInt(task.getAppversion()) > getVersion(context)) {
                        Downloader downloader = new Downloader(context, mHandler);
                        downloader.download(task.getAppurl(), 3);
                        return;
                    }
                }

                DbHelper dbHelper = new DbHelper(context);
                final DBControl dbControl = new DBControl(context, dbHelper);
                MyThreadPool.getInstance().submit(new Runnable() {
                    @Override
                    public void run() {

                        dbControl.insertServar(setCheckSub(Long.parseLong(task.getServertime()), DateUtil.getTime()) + "");
                        for (int i = 0; i < task.getList().size(); i++) {
                            ExeTime exeTime = task.getList().get(i);
                            dbControl.insertExec(exeTime);
                            dbControl.insertMpData(exeTime.getMpdata().get(0), exeTime.getExec_id());
                            for (int p = 0; p < exeTime.getPackagedata().size(); p++) {
                                dbControl.insertPackagedata(exeTime.getPackagedata().get(p), exeTime.getExec_id());
                            }
                            for (int y = 0; y < exeTime.getVpndata().size(); y++) {
                                dbControl.insertVpndata(exeTime.getVpndata().get(y), exeTime.getExec_id());
                            }
                        }

                        L.e("+++++++++++任务总个数：" + dbControl.isTaskSelect(DateUtil.getStringFormat(getTime(), DateUtil.dateFormatYMD)) + "+++++++++++++");
                        if (dbControl.isOverTask(DateUtil.getStringFormat(getTime(), DateUtil.dateFormatYMD))) {
                            NETTASK = false;
                        }
                    }
                });
            }

            @Override
            public void onCallbackFromThreadError(String resultJson, Object modelClass) {
                if (dbControl.isOverTask(DateUtil.getStringFormat(getTime(), DateUtil.dateFormatYMD))) {
                    NETTASK = false;
                }
            }

            @Override
            public void onCallBackFromThreadError(String resultJson, int resultCode, Object modelClass) {
                if (dbControl.isOverTask(DateUtil.getStringFormat(getTime(), DateUtil.dateFormatYMD))) {
                    NETTASK = false;
                }
            }
        }, AppConfig.TASK_GETOWErtASHBYDAY,
                parameter, 100, Task.class);
        DefaultThreadPool.getInstance().execute(httpRequest);
    }


    public void dataUpdata(final String task_id) {

        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
        final Request request = new Request.Builder()
                .url(AppConfig.TASK_UPDATETASKSTATUS + "?task_id=" + task_id.substring(0, task_id.indexOf("_")))
                .build();
//new call
        Call call = mOkHttpClient.newCall(request);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                L.e(e.toString());
                updateTask(exeTimesTask.getExec_id(), exeTimesTask.getPackagedata().get(count).getTask_id(), exeTimesTask.getPackagedata().get(count).getPackage_name());
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String htmlStr = response.body().string();
                L.e(htmlStr);
                updateTask(exeTimesTask.getExec_id(), exeTimesTask.getPackagedata().get(count).getTask_id(), exeTimesTask.getPackagedata().get(count).getPackage_name());
            }
        });


//        Map<String, Object> parameter = new HashMap<String, Object>();
//
//        parameter.put("task_id", task_id.substring(0, task_id.indexOf("_")));
//        BaseRequest httpRequest = new AsyncHttpPost(new ThreadCallBack() {
//            @Override
//            public void onCallbackFromThread(String resultJson, Object modelClass) {
//
//            }
//
//            @Override
//            public void onCallBackFromThread(String resultJson, int resultCode, Object modelClass) {
//
//            }
//
//            @Override
//            public void onCallbackFromThreadError(String resultJson, Object modelClass) {
//
//            }
//
//            @Override
//            public void onCallBackFromThreadError(String resultJson, int resultCode, Object modelClass) {
//
//            }
//        }, AppConfig.TASK_UPDATETASKSTATUS,
//                parameter, 101, BaseModel.class);
//        DefaultThreadPool.getInstance().execute(httpRequest);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public int getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            return 1;
        }
    }

    public long setCheckSub(long netwokre, long local) {
        long sub = netwokre - local;
        return sub;
    }


    public long getTime() {
        DbHelper dbHelper = new DbHelper(context);
        DBControl dbControl = new DBControl(context, dbHelper);
        String selectServar = dbControl.selectServar();
        long serTime = 0;
        if (TextUtils.isEmpty(selectServar))
            serTime = DateUtil.getTime();
        else
            serTime = DateUtil.getTime() + Long.parseLong(selectServar);
        return serTime * 1000;
    }

    /**
     * 第四步修改手机参数, 任务执行修改状态，然后重启
     * 使用SharedPreferences保存修改的数据
     */
    private void saveData(final Context context, MpData mpdata, String pnumber, final String exeid, String networktype) {
        try {
            SharedPreferences sh = context.getSharedPreferences("prefs",
                    Context.MODE_WORLD_READABLE);
            SharedPreferences.Editor pre = sh.edit();

            pre.putString("imei", mpdata.getM_apparatus());
//            pre.putString("imsi", getRandomNum(15));
            pre.putString("number", pnumber);
//                             pre.putString("simserial", randomNum(20));
            pre.putString("wifimac", mpdata.getMac_address());
//            pre.putString("bluemac", this.randomMac1());
//            pre.putString("androidid", this.randomABC(16));
//            pre.putString("serial", this.randomNum(19) + "a");
            pre.putString("brand", mpdata.getM_products());
            pre.putString("release", mpdata.getRom_version());
            pre.putString("simoperatorname", mpdata.getSimoperatorname());
            pre.putString("simoperator", mpdata.getSimoperator());
            pre.putString("networktype", networktype);
            pre.apply();
            L.e("保存成功:" + mpdata.getM_apparatus());
        } catch (Throwable e) {
            L.e("保存异常:");
        }


        MyThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                DbHelper dbHelper = new DbHelper(context);
                DBControl dbControl = new DBControl(context, dbHelper);
                dbControl.updateExec("1", exeid);
                L.e("开始重启");
                CMDutils.getInstance(context).cmd_reboot();
            }
        });
//        CMDutils.getInstance(this).excuteSuCMD("monkey -p com.iloomo.glucometer -v 50000");
    }

    private Handler vpnHandler = new Handler();


    public void startVpn(final ExeTime exeTimes) {
        try {


            L.e("开始连接VPN");
//            VPN.getInstance(context).autoOpenVpn(exeTimes.getVpndata().get(0), vpnHandler, exeTimes);
//            VPN.getInstance(context).setVPNCallBack(new VPN.VPNCallBack() {
//                @Override
//                public void success(ExeTime exeTimes) {
            ISFRISTVPN = false;

            Message message = new Message();
            message.what = TASKING;
            message.obj = exeTimes;
            handler.sendMessage(message);
//                }
//
//                @Override
//                public void filed(final ExeTime exeTimes) {
//                    //判断是否是第一次请求
//                    vpnHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (!ISFRISTVPN) {
//                                startVpn(exeTimes);
//                                ISFRISTVPN = true;
//                            } else {
//                                ISFRISTVPN = false;
//                                updataTodayTask("2", exeTimes.getExec_id());
//                                TASK = false;
//                            }
//                        }
//                    });
//                }
//
//                @Override
//                public void disconnect() {
//                    vpnHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            updataTodayTask("2", exeTimesTask.getExec_id());
//                            count = 0;
//                            TASK = false;
//                        }
//                    });
//                }
//            });
        } catch (Exception e) {
            ISFRISTVPN = false;
            Message message = new Message();
            message.what = TASKING;
            message.obj = exeTimes;
            handler.sendMessage(message);
        }
    }

    //第五部当手机重启后会进入第五步，判断出任务的数量 ，开启下载任务开始下载任务，查询出未执行任务，安装app 进入第六步
    private void startTask(ExeTime exeTimes) {
        L.e("判断count:" + count + "   " + exeTimesTask.getPackagedata().size());
        if ("wifi".equals(exeTimesTask.getNetworktype())) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
        } else {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);
        }


        if (count < exeTimesTask.getPackagedata().size()) {
//            L.e("开始下载");
//            Downloader downloader = new Downloader(context, handler);
//            downloader.download(, 3);
//            String path = exeTimes.getPackagedata().get(count).getDown_url();
            String path = "/mnt/sdcard/app-release.apk";
            L.e("路径：" + getSDPath() + ",名字" + path.substring(path.lastIndexOf("/") + 1));
//            File fileapk = new File(getSDPath(), path.substring(path.lastIndexOf("/") + 1));
            File fileapk = new File("/mnt/sdcard/", path.substring(path.lastIndexOf("/") + 1));

            L.e("文件是否存在：" + fileapk.exists());
            if (fileapk.exists()) {
                cmdExe(fileapk.getAbsolutePath());
            } else {
                L.e(path.substring(path.lastIndexOf("/") + 1) + "文件不存在");
            }
        } else {
            L.e("结束这一时间段的任务修改状态为2,任务状态改为false,重新开始！");
            VPN.getInstance(context).disconnect(exeTimes, VPN.FINISH);
            updataTodayTask("2", exeTimes.getExec_id());
            TASK = false;
        }
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        } else {
            return "";
        }
        String replace = MApplication.context.getPackageName();
        String path = sdDir.toString() + "/Android/data/" + replace + "/apk/";
        File dbFolder = new File(path);
        // 目录不存在则自动创建目录
        if (!dbFolder.exists()) {
            dbFolder.mkdirs();
        }
        return path;
    }

    /**
     * 检查当前网络是否可用
     *
     * @param activity
     * @return
     */

    public boolean isNetworkAvailable(Context activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态===" + networkInfo[i].getState());
                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /***
     * 下载
     */
    public void downLoad(final ExeTime exeTime) {
//        Handler dHandler = new Handler() {
//            @Override
//            public void dispatchMessage(Message msg) {
//                super.dispatchMessage(msg);
//                switch (msg.what) {
//                    case Downloader.PROGRESS:
//                        //获取当前下载的总量
//                        int done = msg.getData().getInt("done");
//                        L.e("" + done);
//                        break;
//                    case Downloader.ERROR:
//                        L.e("下载失败，跳过该任务");
//                        updataTodayTask("2", exeTime.getExec_id());
//                        TASK = false;
//                        break;
//                    case Downloader.FINISH:
//                        if (countAll > countDownload) {
//                            downLoad(exeTime);
//                        } else {
        startVpn(exeTime);
//                        }
//                        break;
//                }
//            }
//        };
//        countAll = exeTime.getPackagedata().size();
//        L.e("开始下载:" + countDownload + " 总数：" + countAll);
//        if (countAll == 0) {
//            updataTodayTask("2", exeTime.getExec_id());
//            TASK = false;
//            return;
//        }
//        Downloader downloader = new Downloader(context, dHandler);
//        downloader.download(exeTime.getPackagedata().get(countDownload).getDown_url(), 1);
//        countDownload++;
    }


}
