package com.iloomo.brush;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.iloomo.base.ActivitySupport;
import com.iloomo.brush.bean.ExeTime;
import com.iloomo.brush.bean.MpData;
import com.iloomo.brush.bean.PackageData;
import com.iloomo.brush.bean.Task;
import com.iloomo.brush.bean.VpnData;
import com.iloomo.brush.db.DBControl;
import com.iloomo.brush.db.DbHelper;
import com.iloomo.brush.global.AppConfig;
import com.iloomo.brush.service.IloomoBrushService;
import com.iloomo.brush.utils.AlarmUtils;
import com.iloomo.brush.utils.CMDutils;
import com.iloomo.global.MApplication;
import com.iloomo.net.AsyncHttpGet;
import com.iloomo.net.BaseRequest;
import com.iloomo.net.DefaultThreadPool;
import com.iloomo.net.ThreadCallBack;
import com.iloomo.threadpool.MyThreadPool;
import com.iloomo.utils.AppUtil;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;

/**
 * Created by wupeitao on 16/3/3.
 */
public class StartMain extends ActivitySupport {

    private LCSharedPreferencesHelper lcSharedPreferencesHelper;
    private Button start;

    private SVProgressHUD mSVProgressHUD;
    private InputMethodManager imm;
    private AlertView mAlertView;//避免创建重复View，先创建View，然后需要的时候show出来，推荐这个做法
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.layout_startmain);
        isLeftVisibility(false);
        wakeUpAndUnlock(context);
        start = (Button) findViewById(R.id.start);
        mSVProgressHUD = new SVProgressHUD(this);
        lcSharedPreferencesHelper = LCSharedPreferencesHelper.instance(context, LCSharedPreferencesHelper.ILOOMO);
        if (!TextUtils.isEmpty(lcSharedPreferencesHelper.getValue(lcSharedPreferencesHelper.START))) {
            if (lcSharedPreferencesHelper.getValue(lcSharedPreferencesHelper.START).equals("true")) {
                start.setVisibility(View.GONE);
                task();
            }
        } else {
            setCtenterTitle("量刷 V:" + getVersion(context) + " imei:" + ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId());
            start.setVisibility(View.VISIBLE);
        }

    }



    public void showWithStatus() {
        mSVProgressHUD.showWithStatus("正在检查运行环境...");
    }

    private static boolean bool = true;
    private static boolean boolcheckroot = true;

    public void onTaskStart(View view) {
        if (!checkInstalled("com.kingroot.kinguser")) {
            showDialogs("未安装rootking.apk", "是否安装？不安装该程序无法执行", 0);
            start.setText("下一步");
            return;
        } else if (!checkInstalled("com.iloomo.appcontrol")) {
            showDialogs("未安装AppControl.apk", "是否安装？不安装该程序无法执行", 1);
            start.setText("下一步");
            return;
        } else if (!checkInstalled("de.robv.android.xposed.installer")) {
            showDialogs("未安装xposedinstaller.apk", "是否安装？不安装该程序无法执行", 2);
            start.setText("下一步");
            return;
        } else if (!checkInstalled("com.did.vpnroot")) {
            showDialogs("未安装vpnroot.apk", "是否安装？不安装该程序无法执行", 3);
            start.setText("下一步");
            return;
        } else if (boolcheckroot) {
            showDialogsJsp("提示", "请详情参照文档进行设置，然后再进行下一步操作，不然程序会出现未知问题！");
            boolcheckroot = false;
            start.setText("下一步");
            return;
        } else if (!AppUtil.getRootPermission(context)) {
            showDialogs("提示", "没有获取root权限,请参考相关文档");
            start.setText("下一步");
            return;
        } else if (bool) {
            showDialogs("提示", "第三方应用都配置了吗？详情查看相关文档");
            bool = false;
            start.setText("重启手机，开始任务");
            return;
        } else {
            lcSharedPreferencesHelper.putValue(lcSharedPreferencesHelper.START, "true");
            start.setVisibility(View.GONE);
            MyThreadPool.getInstance().submit(new Runnable() {
                @Override
                public void run() {
                    CMDutils.getInstance(context).cmd_reboot();
                }
            });
        }

    }


    public boolean checkInstalled(String packagename) {

        PackageInfo packageInfo;

        try {
            packageInfo = this.getPackageManager().getPackageInfo(
                    packagename, 0);

        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
        }
        if (packageInfo == null) {
            L.e(packagename + " not installed");
            return false;
        } else {
            L.e(packagename + "is installed");
            return true;
        }
    }


    public void task() {
        DbHelper dbHelper = new DbHelper(context);
        DBControl dbControl = new DBControl(context, dbHelper);
        L.e(DBControl.DB_VERSION + "////" + lcSharedPreferencesHelper.getValue(LCSharedPreferencesHelper.UPDATE_DB));
        L.e("数据库：" + dbControl.selectPhoneTab() + "   imei" + ((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
                .getDeviceId());
        try {
            if (TextUtils.isEmpty(lcSharedPreferencesHelper.getValue(LCSharedPreferencesHelper.UPDATE_DB))) {
                lcSharedPreferencesHelper.putValue(LCSharedPreferencesHelper.UPDATE_DB, DBControl.DB_VERSION);
                dbControl.deleteAllTab();
                dbControl.createAllTab();
            } else {
                if (!DBControl.DB_VERSION.equals(lcSharedPreferencesHelper.getValue(LCSharedPreferencesHelper.UPDATE_DB))) {
                    lcSharedPreferencesHelper.putValue(LCSharedPreferencesHelper.UPDATE_DB, DBControl.DB_VERSION);
                    dbControl.deleteAllTab();
                    dbControl.createAllTab();
                }
            }
        } catch (Exception e) {
        }


        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {

            wifiManager.setWifiEnabled(true);
        }
        dbControl.insertPhoneTab(((TelephonyManager) getSystemService(TELEPHONY_SERVICE))
                .getDeviceId());

        setCtenterTitle("刷量V:" + getVersion(context) + "imei:" + dbControl.selectPhoneTab());

        AlarmUtils.getInstance(this).startAlarm();

        final LinearLayout ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
        final ScrollView sv_show = (ScrollView) findViewById(R.id.sv_show);
        final TextView tv_show = (TextView) findViewById(R.id.tv_show);
        tv_show.setText("+++++++++++++++++日志++++++++++++++++\n\n");
        final Handler handler = new Handler();
        L.setLogCalBack(new L.LogCallBack() {
            @Override
            public void message(final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Looper.prepare();
                        } catch (Exception e) {

                        }
                        tv_show.append(msg + "\n");
                        if (tv_show.length() > 20000) {
                            tv_show.setText("+++++++++++++++++日志清空++++++++++++++\n\n");
                        }
                        scroll2Bottom(sv_show, ll_layout);
                    }
                });

            }
        });
        systemOut();
        isNetworkAvailable(context);
    }


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
                    L.e(i + "===状态===" + networkInfo[i].getState());
                    L.e(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void wakeUpAndUnlock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

    public void scroll2Bottom(final ScrollView scroll, final View inner) {
        if (scroll == null || inner == null) {
            return;
        }
        // 内层高度超过外层
        int offset = inner.getMeasuredHeight()
                - scroll.getMeasuredHeight();
        if (offset < 0) {
            System.out.println("定位...");
            offset = 0;
        }
        scroll.scrollTo(0, offset);

    }


    public void systemOut() {
        //BOARD 主板
        String phoneInfo = "BOARD: " + android.os.Build.BOARD;
        phoneInfo += ", BOOTLOADER: " + android.os.Build.BOOTLOADER;
//BRAND 运营商
        phoneInfo += ", BRAND: " + android.os.Build.BRAND;
        phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
        phoneInfo += ", CPU_ABI2: " + android.os.Build.CPU_ABI2;
//DEVICE 驱动
        phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
//DISPLAY 显示
        phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
//指纹
        phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
//HARDWARE 硬件
        phoneInfo += ", HARDWARE: " + android.os.Build.HARDWARE;
        phoneInfo += ", HOST: " + android.os.Build.HOST;
        phoneInfo += ", ID: " + android.os.Build.ID;
//MANUFACTURER 生产厂家
        phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
//MODEL 机型
        phoneInfo += ", MODEL: " + android.os.Build.MODEL;
        phoneInfo += ", PRODUCT: " + android.os.Build.PRODUCT;
        phoneInfo += ", RADIO: " + android.os.Build.RADIO;
        phoneInfo += ", RADITAGSO: " + android.os.Build.TAGS;
        phoneInfo += ", TIME: " + android.os.Build.TIME;
        phoneInfo += ", TYPE: " + android.os.Build.TYPE;
        phoneInfo += ", USER: " + android.os.Build.USER;
//VERSION.RELEASE 固件版本
        phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
        phoneInfo += ", VERSION.CODENAME: " + android.os.Build.VERSION.CODENAME;
//VERSION.INCREMENTAL 基带版本
        phoneInfo += ", VERSION.INCREMENTAL: " + android.os.Build.VERSION.INCREMENTAL;
//VERSION.SDK SDK版本
        phoneInfo += ", VERSION.SDK: " + android.os.Build.VERSION.SDK;
        phoneInfo += ", VERSION.SDK_INT: " + android.os.Build.VERSION.SDK_INT;
        L.e(phoneInfo);
        L.e(GetNetworkType());
    }


    public String GetNetworkType() {
        String strNetworkType = "";
        ConnectivityManager systemService = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = systemService.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

                Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }

        }

        Log.e("cocos2d-x", "Network Type : " + strNetworkType);
        getYUNYING();
        return strNetworkType;
    }


    public void getYUNYING() {
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telManager.getSubscriberId();

        if (imsi != null) {

            if (imsi.startsWith("46000") || imsi.startsWith("46002"))

            {
                L.e("方法一：移动网络");

//因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号 //中国移动

            } else if (imsi.startsWith("46001")) {
                L.e("方法一：中国联通");
//中国联通

            } else if (imsi.startsWith("46003")) {
                L.e("方法一：中国电信");
//中国电信

            }

        }
        getYUNYINGSHANG();
    }

    public void getYUNYINGSHANG() {
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String operator = telManager.getSimOperator();

        if (operator != null) {

            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {

//中国移动
                L.e("方法二：移动网络");

            } else if (operator.equals("46001")) {

//中国联通
                L.e("方法二：中国联通");
            } else if (operator.equals("46003")) {

//中国电信
                L.e("方法二：中国电信");
            }

        }
    }


    public void showDialogs(String title, String content, final int status) {
        mAlertView = new AlertView(title, content, "取消", new String[]{"安装"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (mAlertView.isShowing()) {
                    mAlertView.dismiss();
                }
                showWithStatus();
                MyThreadPool.getInstance().submit(new Runnable() {
                    @Override
                    public void run() {
                        chooseInstallAPK(status);
                    }
                });
            }
        }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                if (mAlertView.isShowing()) {
                    mAlertView.dismiss();
                }
            }
        });
        mAlertView.show();
    }

    public void showDialogs(String title, String content) {
        mAlertView = new AlertView(title, content, "取消", new String[]{"确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (mAlertView.isShowing()) {
                    mAlertView.dismiss();
                }

            }
        }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                if (mAlertView.isShowing()) {
                    mAlertView.dismiss();
                }

            }
        });
        mAlertView.show();
    }

    public void showDialogsJsp(String title, String content) {
        mAlertView = new AlertView(title, content, "取消", new String[]{"跳出程序"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (mAlertView.isShowing()) {
                    mAlertView.dismiss();
                }
                finish();
            }
        }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                if (mAlertView.isShowing()) {
                    mAlertView.dismiss();
                }

            }
        });
        mAlertView.show();
    }

    public void chooseInstallAPK(int status) {
        switch (status) {
            case 0:
                installapk("kingroot.apk", Environment.getExternalStorageDirectory().getAbsolutePath() + "/iloomobrush/");
                break;
            case 1:
                installapk("updateapp.apk", Environment.getExternalStorageDirectory().getAbsolutePath() + "/iloomobrush/");
                break;
            case 2:
                installapk("xposedinstaller.apk", Environment.getExternalStorageDirectory().getAbsolutePath() + "/iloomobrush/");
                break;
            case 3:
                installapk("vpnroot.apk", Environment.getExternalStorageDirectory().getAbsolutePath() + "/iloomobrush/");
                break;
        }
    }

    public void installapk(final String name, final String path) {

        if (copyApkFromAssets(context, name, path)) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(new File(path + name)),
                            "application/vnd.android.package-archive");
                    context.startActivity(intent);
                    if (mSVProgressHUD.isShowing())
                        mSVProgressHUD.dismiss();
                }
            });

        }

    }

    public boolean copyApkFromAssets(Context context, String fileName, String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File dbFolder = new File(path);
            // 目录不存在则自动创建目录
            if (!dbFolder.exists()) {
                dbFolder.mkdirs();
            }
            File file = new File(path + fileName);

            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
        }
        return copyIsFinish;
    }
}
