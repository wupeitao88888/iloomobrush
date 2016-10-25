package com.iloomo.brush.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.iloomo.brush.bean.ExeTime;
import com.iloomo.brush.bean.Task;
import com.iloomo.brush.bean.VpnData;
import com.iloomo.brush.global.AppConfig;
import com.iloomo.net.AsyncHttpGet;
import com.iloomo.net.BaseRequest;
import com.iloomo.net.DefaultThreadPool;
import com.iloomo.net.ThreadCallBack;
import com.iloomo.threadpool.MyThreadPool;
import com.iloomo.utils.L;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自定连接VPN  必须保证1、获取root权限。2、必须是竖屏。3、用户不能干涉
 * <p/>
 * <p/>
 * Created by wupeitao on 16/6/13.
 */
public class VPN {


    public static final int SUCCESS = 999;
    public static final int FILED = 998;
    public static final int FINISH = 997;
    public static final int DELETE = 996;

    public static VPN vpn;
    private Context context;

    public static VPN getInstance(Context context) {
        if (vpn == null)
            vpn = new VPN(context);
        return vpn;
    }


    public VPN(Context context) {
        this.context = context;
    }

    private static int i = 0;

    public boolean checkInstalled(String packagename) {

        PackageInfo packageInfo;

        try {
            packageInfo = context.getPackageManager().getPackageInfo(
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

    public void autoOpenVpn(final VpnData vpnData, final Handler mHandler, final ExeTime exeTimes) {
        MyThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
//                if (checkInstalled("com.did.vpnroot")) {
//                    CMDutils.getInstance(context).cmd_uninstall("com.did.vpnroot");
//                }
//                if (copyApkFromAssets(context, "vpnroot.apk", Environment.getExternalStorageDirectory().getAbsolutePath() + "/iloomobrush/")) {
//                   String path= Environment.getExternalStorageDirectory().getAbsolutePath() + "/iloomobrush/" + "vpnroot.apk";
//                    L.e("path="+path);
//                    CMDutils.getInstance(context).cmd_install(path);
//
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
                mIntent();
                start(vpnData, mHandler, exeTimes);
//                        }
//                    });
//                }
//
            }
        });
    }

//    public final String[] ips = {"157.122.178.246","59.45.168.225","112.83.69.201"};
//    String name = "72357";
//    String pw = "72357";

//    //七七网络
//    public final String[] ips = {
////        "218.92.225.187",
//        "112.83.69.201"};
//    String name = "wupeitaoqiqi";
//    String pw = "wu123456";


    //    public final String[] ips = {"122.228.236.191"};
//    String name = "wupeitaoyuanke";
//    String pw = "wu123456";
    public final String[] ips = { "112.83.69.201"};
//            "218.2.0.204", "157.122.178.246", "59.45.168.225",
    String name = "wupeitaokele";
    String pw = "wu123456";

    public String getIP() {
        int m = (int) ((Math.random() * ips.length));
        String device = ips[m];
        return device;
    }


    String ip = getIP();

    String nowIP = "";

    public void start(final VpnData vpnData, final Handler mHandler, final ExeTime exeTimes) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> parameter = new HashMap<String, Object>();
                BaseRequest httpRequest = new AsyncHttpGet<>(new ThreadCallBack() {
                    @Override
                    public void onCallbackFromThread(String resultJson, Object modelClass) {
                    }

                    @Override
                    public void onCallBackFromThread(String resultJson, int resultCode, Object modelClass) {
                        L.e("网络链接..成功");
                        String htmlStr = resultJson.toString();
                        nowIP = htmlStr.substring(htmlStr.indexOf("[") + 1, htmlStr.lastIndexOf("]"));
                        getNowIP(vpnData, mHandler, exeTimes);
                    }

                    @Override
                    public void onCallbackFromThreadError(String resultJson, Object modelClass) {


                    }

                    @Override
                    public void onCallBackFromThreadError(String resultJson, int resultCode, Object modelClass) {

                        L.e("网络链接失败..失败");
                        L.e("连接VPN...失败");
                        if (vpnCallBack != null)
                            vpnCallBack.filed(exeTimes);
                    }
                }, AppConfig.VPNCHECK, parameter, 1001, Task.class);
                DefaultThreadPool.getInstance().execute(httpRequest);
            }
        });


    }


    public void getNowIP(final VpnData vpnData, final Handler mHandler, final ExeTime exeTimes) {

        MyThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {

                L.e("检查VPN");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
//                //获取焦点


                CMDutils.getInstance(context).excuteSuCMD("input touchscreen swipe 410 350 410 350 900");
//                onClick(410, 350);
                CMDutils.getInstance(context).excuteSuCMD("input keyevent 67");
                CMDutils.getInstance(context).excuteSuCMD("input text " + ip);

                CMDutils.getInstance(context).excuteSuCMD("input touchscreen swipe 410 450 410 450 900");
//                onClick(410, 450);
                CMDutils.getInstance(context).excuteSuCMD("input keyevent 67");
                CMDutils.getInstance(context).excuteSuCMD("input text " + name);
                CMDutils.getInstance(context).excuteSuCMD("input touchscreen swipe 410 550 410 550 900");
//                onClick(410, 550);
                CMDutils.getInstance(context).excuteSuCMD("input keyevent 67");
                CMDutils.getInstance(context).excuteSuCMD("input text " + pw);

                CMDutils.getInstance(context).excuteSuCMD("input keyevent 4");

                onClick(200, 680);

                try {
                    Thread.sleep(30 * 1000);
                } catch (InterruptedException e) {
                }
                CMDutils.getInstance(context).excuteSuCMD("input keyevent 4");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        checkVPN(vpnData.getConip(), mHandler, exeTimes);
                    }
                });
            }
        });
    }


    public void checkVPN(final String conip, final Handler mHandler, final ExeTime exeTimes) {
        Map<String, Object> parameter = new HashMap<String, Object>();
        BaseRequest httpRequest = new AsyncHttpGet<>(new ThreadCallBack() {
            @Override
            public void onCallbackFromThread(String resultJson, Object modelClass) {
            }

            @Override
            public void onCallBackFromThread(String resultJson, int resultCode, Object modelClass) {
                L.e("网络链接..成功");
                String htmlStr = resultJson.toString();
                if (TextUtils.isEmpty(htmlStr)) {
                    L.e("连接VPN...失败");
                    if (vpnCallBack != null)
                        vpnCallBack.filed(exeTimes);
                    return;
                }
                int status = htmlStr.indexOf(nowIP);
                String netIp = htmlStr.substring(htmlStr.indexOf("[") + 1, htmlStr.lastIndexOf("]"));
                L.e("IPStatus:原来的IP：" + nowIP + "  现在的IP：" + netIp);
                if (status > -1) {
                    L.e("连接VPN...失败");
                    if (vpnCallBack != null)
                        vpnCallBack.filed(exeTimes);

                } else {
                    //连接成功
                    L.e("连接VPN...成功");
                    if (vpnCallBack != null)
                        vpnCallBack.success(exeTimes);
                }
            }

            @Override
            public void onCallbackFromThreadError(String resultJson, Object modelClass) {


            }

            @Override
            public void onCallBackFromThreadError(String resultJson, int resultCode, Object modelClass) {

                L.e("网络链接失败..失败");

                //未连接
                L.e("连接VPN...失败");
                if (vpnCallBack != null)
                    vpnCallBack.filed(exeTimes);
            }
        }, AppConfig.VPNCHECK, parameter, 1001, Task.class);
        DefaultThreadPool.getInstance().execute(httpRequest);

    }


    public void onClick(int X, int Y) {
        CMDutils.getInstance(context).excuteSuCMD("input tap " + X + "  " + Y);
    }


    public void disconnect(ExeTime exeTimes, int code) {
        mIntent();
        MyThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                }
                CMDutils.getInstance(context).excuteSuCMD("input keyevent 4");
                onClick(500, 680);
                if (vpnCallBack != null)
                    vpnCallBack.disconnect();
            }
        });

    }

    public void mIntent() {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage("com.did.vpnroot");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public interface VPNCallBack {
        void success(ExeTime exeTimes);

        void filed(ExeTime exeTimes);

        void disconnect();

    }

    public VPNCallBack vpnCallBack;

    public void setVPNCallBack(VPNCallBack vpnCallBack) {
        this.vpnCallBack = vpnCallBack;
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
            e.printStackTrace();
        }
        return copyIsFinish;
    }
}
