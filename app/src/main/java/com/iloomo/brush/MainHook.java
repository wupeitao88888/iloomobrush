package com.iloomo.brush;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.iloomo.utils.L;

import java.util.HashMap;
import java.util.Random;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

/**
 *
 *
 */
public class MainHook implements IXposedHookLoadPackage {
    public static String TAG = "MainHook";
    private static boolean init = false;

    /**
     * 使用SharedPreferences做共享数据失败
     *
     * @param lpparam
     */
    private void readData(LoadPackageParam lpparam) {
        if (!init) {
            try {
                XSharedPreferences pre = new XSharedPreferences(this.getClass()
                        .getPackage().getName(), "prefs");
//                String ks[] = {"imei", "imsi", "number", "simserial",
//                        "wifimac", "bluemac", "androidid", "serial", "brand", "release""};
                if (TextUtils.isEmpty(pre.getString("imei", null))) {
                    return;
                }

                String ks[] = {"imei", "number",
                        "wifimac", "brand", "release", "simoperator", "simoperatorname", "networktype"};
                HashMap<String, String> maps = new HashMap<String, String>();
                for (String k : ks) {
                    String v = pre.getString(k, "");
                    Log.e(TAG, "{" + lpparam.packageName
                            + "}" + v);
                    maps.put(k, v);
                    if (TextUtils.isEmpty(v)) {
//						Logger.d("{" + lpparam.packageName + "}读取储存内容失败: " + k
//								+ " 为Null");
                        Log.e(TAG, "{" + lpparam.packageName + "}读取储存内容失败: " + k + " 为Null");
                        break;
                    }
                }
                if (maps.isEmpty()) {
//					Logger.d("{" + lpparam.packageName
//							+ "}读取储存内容失败:  SharedPreferences 为Null");
                    Log.e(TAG, "{" + lpparam.packageName
                            + "}读取储存内容失败:  SharedPreferences 为Null");
                } else {
                    HookAll(maps);
                }
            } catch (Throwable e) {
                Log.e(TAG, "{" + lpparam.packageName + "}读取储存内容失败:"
                        + e.getMessage());
            }
        } else {
            Log.e(TAG, "读取缓存内容");
        }

    }

    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        if ("".equals(lpparam.packageName))// System
        {
            return;
        }
        readData(lpparam);
    }

    private String getRandomNum(int n) {
        String res = "";
        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            res = res + rnd.nextInt(10);
        }
        return res;
    }


    /**
     * @return
     */
    private Bundle randomPhone() {
        String head[] = {"+86186"};
        Random rnd = new Random();
        String res = "+86186";
        for (int i = 0; i < 9; i++) {
            res = res + rnd.nextInt(10);
        }
        Bundle bundle = new Bundle();
        bundle.putString("phonenumber", "+8618612464312");
        bundle.putString("imsi", "46001" + getRandomNum(10));

        return bundle;
    }

    public static final String[] devices = {"红米手机1s", "MI 2S", "三星GT-i9250", "三星Galaxy Note II", "MI 3", "MI 4LTE", "HM NOTE 1LTE", "HM NOTE 1S", "MI 2A", "HTC T328d", "MI 3W", "ZTE N880G", "HUAWEI C8812", "HM 2A", "Lenovo A320t", "m1 note", "红米手机", "MI 2", "Coolpad 5890", "HM 1s", "OPPO 1107", "三星Galaxy S IV", "Galaxy Note III", "三星Galaxy Note III", "vivo Y13L", "HM NOTE 1TD", "HUAWEI P7-L09", "vivo Y23L", "MI 2SC", "HUAWEI Y511-T00", "HM 1SC", "OPPO R7007", "m2 note", "HUAWEI C8815", "Galaxy Note II", "HM NOTE 1LTETD", "ZTE N909", "三星Galaxy SIII", "Coolpad 8720L", "HUAWEI A199", "MI NOTE LTE", "vivo X3L", "Coolpad 8297-T01", "A31", "MI 4W", "HUAWEI C8650+", "vivo Y27", "OPPO R7", "三星Galaxy Grand 2", "HUAWEI C8816", "华为荣耀畅玩4X", "三星Galaxy SII", "OPPO R831S", "Lenovo A360t", "HM 1S", "HUAWEI G750-T01", "Galaxy Grand 2", "m1 mote", "MI 2CS", "金立GN151", "OPPO R8207", "HUAWEI B199", "魅族MX4", "vivo Y13iL", "华为荣耀3C", "F103", "华为G621-TL00", "三星 SCH-I829", "Galaxy SIII", "MI PAD", "三星Galaxy S V", "MI 3C", "三星SCH-W2013", "小米M1(MIUI)", "4G", "三星Galaxy Win", "HUAWEI C8813", "Coolpad 5950", "华为 H60-L01", "三星Galaxy Trend", "Coolpad 8675", "Galaxy S IV", "vivo Y13", "Coolpad 9190L", "OPPO R2017", "Redmi Note 2", "华为荣耀Che1-CL10", "Coolpad 5891", "Coolpad 5891Q", "HUAWEI C8812E", "HUAWEI Y321-C00", "vivo X5M", "三星Galaxy Ace", "Coolpad 8705", "HUAWEI P6-C00", "Lenovo A788t", "海信E920", "LNV-Lenovo A385e", "LNV-Lenovo A380e", "HTC D316d", "HTC 609d", "vivo X5S L", "HTC 802d", "海信EG970", "A31t", "TCL J900", "三星SM-G5308W", "三星Galaxy Ace Dear", "LNV-Lenovo A370e", "ZTE N881E", "台电A11", "ZTE N880F", "ZTE V6700", "MI NOTE Pro", "三星SCH-i519", "ZTE N880E", "Coolpad 5218S", "Lenovo A820e", "HUAWEI C8813D", "HTC 709d", "ZTE N5S", "K-Touch C966e", "ZTE N855D+", "HTC T329d", "OPPO 3007", "HUAWEI C8950D", "海信E926", "Lenovo S850e", "MX4 Pro", "三星Galaxy SII Duos", "Lenovo A3800-d", "OPPO X9007", "OPPO R6007", "MI 1S", "TCL D706", "ZTE N900", "海信EG966", "TY-K Touch E80", "ZTE N919D", "Coolpad 8017-T00", "ZTE N983", "索尼Xperia SP", "ZTE Q701C", "LNV-Lenovo S870e", "ZTE N881F", "TCL D662", "Coolpad 8702", "TCL D668", "海信E936", "三星I909(电信版SPhone)", "vivo Y913", "vivo Y11", "Coolpad Y75", "LNV-Lenovo A790e", "TCL P301M", "OPPO R1s", "华为G620-L75", "A51", "Coolpad 8297", "华为c8817d", "GALAXY Note II", "2014112", "Lenovo A2800-d", "三星A5000", "CHM-TL00H", "vivo X5L", "smartisan", "三星Galaxy A7", "vivo Y28L", "vivo X5Pro D", "OPPO R821t", "HM NOTE 1W", "OPPO R831t", "Coolpad 8675-A", "SM-G9250", "大显MX5", "HUAWEI P7-L07", "GALAXY SIII", "vivo Y33", "Che2-TL00", "三星GT-S7568", "HM 1SW", "Coolpad 8702D", "GALAXY S4", "vivo Y11i T", "Lenovo K30-T", "vivo Y22L", "三星GT-i9000", "三星GT-S7572", "三星SCH-I679", "OPPO 1105", "Coolpad 8729", "vivo Y18L", "OPPO U701", "vivo Y29L", "vivo X5Max+", "金立S5.1", "小米M2", "三星Galaxy Mega 5.8"};

    public String getDevices() {
        int m = (int) ((Math.random() * devices.length));
        String device = devices[m];
        return device;
    }


    private void hookAll() {

//        HookMethod(NetworkInfo.class, "getState", "CONNECTED");
//        HookMethod(NetworkInfo.class, "getSubtypeName", "HSPAP");
//        HookMethod(NetworkInfo.class, "getSubtype", TelephonyManager.NETWORK_TYPE_HSPAP);
        L.e("开始刷机");
//        本地存储的IMEI： MAC:
//        HookMethod(TelephonyManager.class, "getDeviceId", getRandomNum(15));
//        HookMethod(WifiInfo.class, "getMacAddress", randomMac());
//        String imsi = "46002" + getRandomNum(10);
//        HookMethod(TelephonyManager.class, "getSubscriberId", imsi);
//        HookMethod(TelephonyManager.class, "getSimState", 5);
//        HookMethod(TelephonyManager.class, "getSimOperator", "46002");
//        HookMethod(TelephonyManager.class, "getNetworkOperator", "46002");
//        String name = "中国移动";
//        HookMethod(TelephonyManager.class, "getSimOperatorName", name);
//        HookMethod(TelephonyManager.class, "getNetworkOperatorName", name);
//        HookMethod(TelephonyManager.class, "getLine1Number", "+8613911717866");
//        HookMethod(TelephonyManager.class, "getSimSerialNumber",
//                randomNum(20));

//        try {
//            XposedHelpers.findField(Build.class, "BRAND").set(null, getDevices());
//            XposedHelpers.findField(android.os.Build.class, "MODEL").set(null, getDevices());
//            String api = "4.4";
//            XposedHelpers.findField(Build.VERSION.class, "SDK_INT").set(null, getSDK(api));
//            XposedHelpers.findField(Build.VERSION.class, "RELEASE").set(null, api);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//		HookMethod(BluetoothAdapter.class, "getAddress", map.get("bluemac"));
    }


    private void HookAll(final HashMap<String, String> map) {

        try {
            String networktype = map.get("networktype");
            if (!TextUtils.isEmpty(networktype)) {
                if (!networktype.equals("wifi") && !networktype.equals("3g")) {
                    HookMethod(NetworkInfo.class, "getSubtypeName", getNETName(networktype));
                    HookMethod(NetworkInfo.class, "getSubtype", getNET(networktype));
                }
            }
        } catch (Exception e) {
            L.e("修改网络失败");
        }
        try {
            String MNC = map.get("simoperator");
            String imsi = MNC + getRandomNum(10);
            HookMethod(TelephonyManager.class, "getDeviceId", map.get("imei"));
            HookMethod(TelephonyManager.class, "getSubscriberId", imsi);
            HookMethod(TelephonyManager.class, "getSimState", 5);
            HookMethod(TelephonyManager.class, "getSimOperator", MNC);
            HookMethod(TelephonyManager.class, "getNetworkOperator", MNC);
            String SimOperatorName = map.get("simoperatorname");
            HookMethod(TelephonyManager.class, "getSimOperatorName", SimOperatorName);
            HookMethod(TelephonyManager.class, "getNetworkOperatorName", SimOperatorName);
            HookMethod(TelephonyManager.class, "getLine1Number", map.get("number"));
            HookMethod(TelephonyManager.class, "getSimSerialNumber",
                    randomNum(20));
            HookMethod(WifiInfo.class, "getMacAddress", map.get("wifimac"));
        } catch (Exception e) {
            L.e("修改手机信息错误imet=" + map.get("imei"));
        }

        try {
            XposedHelpers.findField(android.os.Build.class, "BRAND").set(null, map.get("brand"));
            XposedHelpers.findField(android.os.Build.class, "MODEL").set(null, map.get("brand"));

            String api = getAPI(map.get("release"));
            XposedHelpers.findField(Build.VERSION.class, "SDK_INT").set(null, getSDK(api));
            XposedHelpers.findField(Build.VERSION.class, "RELEASE").set(null, api);
        } catch (Throwable e) {
            Log.e(TAG, "修改 Build1 失败!" + e.getMessage());
        }
    }

    public static final String[] release = {"6.0", "5.1.1", "5.0.1", "4.4w.2", "4.4", "4.0.3", "4.0"};

    public String getRelease() {
        int m = (int) ((Math.random() * release.length));
        String device = release[m];
        return device;
    }


    public int getNET(String net) {
        int gnet = TelephonyManager.NETWORK_TYPE_HSPAP;
        switch (net) {
            case "3g":
                gnet = TelephonyManager.NETWORK_TYPE_HSPAP;
                break;
            case "4g":
                gnet = TelephonyManager.NETWORK_TYPE_LTE;
                break;
        }
        return gnet;
    }

    public String getNETName(String net) {
        String gnet = "HSPAP";
        switch (net) {
            case "3g":
                gnet = "HSPAP";
                break;
            case "4g":
                gnet = "LTE";
                break;
        }
        return gnet;
    }


    public String getAPI(String sdk) {
        switch (sdk) {
            case "4.3.1":
                return "4.4";
            case "4.2.2":
                return "4.4";
            case "4.1.2":
                return "4.4";
            case "4.0.3":
                return "4.4";
            case "4.0":
                return "4.4";
            default:
                return sdk;
        }
    }


    private String randomMac() {
        String chars = "abcde0123456789";
        String res = "";
        Random rnd = new Random();
        int leng = chars.length();
        for (int i = 0; i < 17; i++) {
            if (i % 3 == 2) {
                res = res + ":";
            } else {
                res = res + chars.charAt(rnd.nextInt(leng));
            }

        }
        return res;
    }

    /**
     * @param n
     * @return
     */
    private String randomNum(int n) {
        String res = "";
        Random rnd = new Random();
        for (int i = 0; i < n; i++) {
            res = res + rnd.nextInt(10);
        }
        return res;
    }

    public int getSDK(String release) {
        int sdk = 16;
        switch (release) {
            case "6.0":
                sdk = 23;
                break;
            case "5.1.1":
                sdk = 22;
                break;
            case "5.0.1":
                sdk = 21;
                break;
            case "4.4w.2":
                sdk = 20;
                break;
            case "4.4":
                sdk = 19;
                break;
//            case "4.3.1":
//                sdk = 15;
//                break;
//            case "4.2.2":
//                sdk = 17;
//                break;
//            case "4.1.2":
//                sdk = 16;
//                break;
//            case "4.0.3":
//                sdk = 15;
//                break;
//            case "4.0":
//                sdk = 14;
//                break;
//            case "3.2":
//                sdk = 13;
//                break;
//            case "3.1":
//                sdk = 12;
//                break;
//            case "3.0":
//                sdk = 11;
//                break;
//            case "2.3.3":
//                sdk = 10;
//                break;
//            case "2.3.1":
//                sdk = 9;
//                break;
//            case "2.2":
//                sdk = 8;
//                break;
//            case "2.1":
//                sdk = 7;
//                break;
        }
        return sdk;
    }

    private void HookMethod(final Class cl, final String method,
                            final String result) {
        try {
            XposedHelpers.findAndHookMethod(cl, method,
                    new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            param.setResult(result);
                        }

                    }});
        } catch (Throwable e) {
            Log.e(TAG, "修改" + method + "失败!" + e.getMessage());
        }
    }

    private void HookMethod(final Class cl, final String method,
                            final int result) {
        try {
            XposedHelpers.findAndHookMethod(cl, method,
                    new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            param.setResult(result);
                        }

                    }});
        } catch (Throwable e) {
            Log.e(TAG, "修改" + method + "失败!" + e.getMessage());
        }
    }

    private void HookMethod(final Class cl, final String method,
                            final Object result) {
        try {
            XposedHelpers.findAndHookMethod(cl, method,
                    new Object[]{new XC_MethodHook() {
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            param.setResult(result);
                        }

                    }});
        } catch (Throwable e) {
            Log.e(TAG, "修改" + method + "失败!" + e.getMessage());
        }
    }
}
