package com.iloomo.brush.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.IBinder;

import com.iloomo.brush.utils.AlarmUtils;
import com.iloomo.brush.utils.CMDutils;
import com.iloomo.utils.L;


/**
 * Created by wupeitao on 16/3/3.
 */
public class IloomoBrushService extends Service {



    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        saveData();

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
     * 使用SharedPreferences
     */
    private void saveData() {
        try {
            SharedPreferences sh = this.getSharedPreferences("prefs",
                    Context.MODE_WORLD_READABLE);
            SharedPreferences.Editor pre = sh.edit();

            pre.putString("imei", getRandomNum(15));
            pre.putString("imsi", getRandomNum(15));
            pre.putString("number", this.randomPhone());
            pre.putString("simserial", randomNum(20));
            pre.putString("wifimac", this.randomMac());
            pre.putString("bluemac", this.randomMac1());
            pre.putString("androidid", this.randomABC(16));
            pre.putString("serial", this.randomNum(19) + "a");
            pre.putString("brand", getDevices() );
            pre.apply();
            L.e("保存成功" +sh.getString("imei",""));
        } catch (Throwable e) {
            e.printStackTrace();
            L.e("保存异常");
        }
//        cmd_reboot();
//        CMDutils.getInstance(this).excuteSuCMD("monkey -p com.iloomo.glucometer -v 50000");
    }


    public static final String[] devices = {"红米手机1s", "MI 2S", "三星GT-i9250", "三星Galaxy Note II", "MI 3", "MI 4LTE", "HM NOTE 1LTE", "HM NOTE 1S", "MI 2A", "HTC T328d", "MI 3W", "ZTE N880G", "HUAWEI C8812", "HM 2A", "Lenovo A320t", "m1 note", "红米手机", "MI 2", "Coolpad 5890", "HM 1s", "OPPO 1107", "三星Galaxy S IV", "Galaxy Note III", "三星Galaxy Note III", "vivo Y13L", "HM NOTE 1TD", "HUAWEI P7-L09", "vivo Y23L", "MI 2SC", "HUAWEI Y511-T00", "HM 1SC", "OPPO R7007", "m2 note", "HUAWEI C8815", "Galaxy Note II", "HM NOTE 1LTETD", "ZTE N909", "三星Galaxy SIII", "Coolpad 8720L", "HUAWEI A199", "MI NOTE LTE", "vivo X3L", "Coolpad 8297-T01", "A31", "MI 4W", "HUAWEI C8650+", "vivo Y27", "OPPO R7", "三星Galaxy Grand 2", "HUAWEI C8816", "华为荣耀畅玩4X", "三星Galaxy SII", "OPPO R831S", "Lenovo A360t", "HM 1S", "HUAWEI G750-T01", "Galaxy Grand 2", "m1 mote", "MI 2CS", "金立GN151", "OPPO R8207", "HUAWEI B199", "魅族MX4", "vivo Y13iL", "华为荣耀3C", "F103", "华为G621-TL00", "三星 SCH-I829", "Galaxy SIII", "MI PAD", "三星Galaxy S V", "MI 3C", "三星SCH-W2013", "小米M1(MIUI)", "4G", "三星Galaxy Win", "HUAWEI C8813", "Coolpad 5950", "华为 H60-L01", "三星Galaxy Trend", "Coolpad 8675", "Galaxy S IV", "vivo Y13", "Coolpad 9190L", "OPPO R2017", "Redmi Note 2", "华为荣耀Che1-CL10", "Coolpad 5891", "Coolpad 5891Q", "HUAWEI C8812E", "HUAWEI Y321-C00", "vivo X5M", "三星Galaxy Ace", "Coolpad 8705", "HUAWEI P6-C00", "Lenovo A788t", "海信E920", "LNV-Lenovo A385e", "LNV-Lenovo A380e", "HTC D316d", "HTC 609d", "vivo X5S L", "HTC 802d", "海信EG970", "A31t", "TCL J900", "三星SM-G5308W", "三星Galaxy Ace Dear", "LNV-Lenovo A370e", "ZTE N881E", "台电A11", "ZTE N880F", "ZTE V6700", "MI NOTE Pro", "三星SCH-i519", "ZTE N880E", "Coolpad 5218S", "Lenovo A820e", "HUAWEI C8813D", "HTC 709d", "ZTE N5S", "K-Touch C966e", "ZTE N855D+", "HTC T329d", "OPPO 3007", "HUAWEI C8950D", "海信E926", "Lenovo S850e", "MX4 Pro", "三星Galaxy SII Duos", "Lenovo A3800-d", "OPPO X9007", "OPPO R6007", "MI 1S", "TCL D706", "ZTE N900", "海信EG966", "TY-K Touch E80", "ZTE N919D", "Coolpad 8017-T00", "ZTE N983", "索尼Xperia SP", "ZTE Q701C", "LNV-Lenovo S870e", "ZTE N881F", "TCL D662", "Coolpad 8702", "TCL D668", "海信E936", "三星I909(电信版SPhone)", "vivo Y913", "vivo Y11", "Coolpad Y75", "LNV-Lenovo A790e", "TCL P301M", "OPPO R1s", "华为G620-L75", "A51", "Coolpad 8297", "华为c8817d", "GALAXY Note II", "2014112", "Lenovo A2800-d", "三星A5000", "CHM-TL00H", "vivo X5L", "smartisan", "三星Galaxy A7", "vivo Y28L", "vivo X5Pro D", "OPPO R821t", "HM NOTE 1W", "OPPO R831t", "Coolpad 8675-A", "SM-G9250", "大显MX5", "HUAWEI P7-L07", "GALAXY SIII", "vivo Y33", "Che2-TL00", "三星GT-S7568", "HM 1SW", "Coolpad 8702D", "GALAXY S4", "vivo Y11i T", "Lenovo K30-T", "vivo Y22L", "三星GT-i9000", "三星GT-S7572", "三星SCH-I679", "OPPO 1105", "Coolpad 8729", "vivo Y18L", "OPPO U701", "vivo Y29L", "vivo X5Max+", "金立S5.1", "小米M2", "三星Galaxy Mega 5.8"};

    public String getDevices() {
        int m = (int) ((Math.random() * devices.length));
        String device = devices[m];
        return device;
    }


    public void openApp() {
        Intent intent = new Intent();
        PackageManager packageManager = this.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage("com.secneo.apkwrapper");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    /**
     * 随机设置参数，自动保存。
     */
//    public void autoSaveData() {
//        try {
//            Person person = new Person();
//            person.imei = randomNum(15);
//            person.imsi = randomNum(15);
//            person.number = randomPhone();
//            person.simserial = randomNum(20);
//            person.wifimac = randomMac();
//            person.bluemac = randomMac1();
//            person.androidid = randomABC(16);
//            person.serial = randomNum(19) + "a";
//            person.brand = Devices.devices[ (new Random()).nextInt(Devices.devices.length-1)];
//
//            File f;
//            f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/iloomo.xml");
//            if (f.exists()) {
//                f.delete();
//            }
//            f.createNewFile();
//            FileOutputStream fos = new FileOutputStream(f);
//            Data.save(person, fos);
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }

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


    /**
     * @return
     */
    private String randomPhone() {
        String head[] = {"+8613", "+8615", "+8617", "+8618", "+8616"};
        Random rnd = new Random();
        String res = head[rnd.nextInt(head.length)];
        for (int i = 0; i < 9; i++) {
            res = res + rnd.nextInt(10);
        }
        return res;
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

    private String randomMac1() {
        String chars = "ABCDE0123456789";
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

    private String randomABC(int n) {
        String chars = "abcde0123456789";
        String res = "";
        Random rnd = new Random();
        int leng = chars.length();
        for (int i = 0; i < n; i++) {
            res = res + chars.charAt(rnd.nextInt(leng));
        }
        return res;
    }


}
