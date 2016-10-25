package com.iloomo.utils;


import android.os.Environment;
import android.util.Log;

import com.iloomo.global.MApplication;
import com.iloomo.threadpool.MyThreadPool;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class L {

    private L() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isDebug = true;
    private static final String TAG = "ledoing";

    public static void i(final String msg) {
        if (isDebug)
            Log.i(TAG, msg);
        sendMsg(msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
        sendMsg(msg);
    }

    public static void sendMsg(final String msg) {

        final String dateToString = DateUtil.getDateToString(System.currentTimeMillis(), DateUtil.dateFormatYMDHMS);
        if (L.logCallBack != null)
            L.logCallBack.message(dateToString + ":" + msg);
        MyThreadPool.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                try {
//                    StringBuffer stringBuffer = new StringBuffer(readSDFile() == null ? "" : readSDFile());
//                    stringBuffer.append(dateToString + ":" + msg
//                            + "\n");
                    writeSDFile(dateToString + ":"+msg);
                } catch (IOException e) {

                }
            }
        });
    }


    public static void e(final String msg) {
        if (isDebug)
            Log.e(TAG, msg);
        sendMsg(msg);
    }

    public static void v(final String msg) {
        if (isDebug)
            Log.v(TAG, msg);
        sendMsg(msg);
    }

    public static void i(String tag, final String msg) {
        if (isDebug)
            Log.i(tag, msg);
        sendMsg(msg);
    }

    public static void d(String tag, final String msg) {
        if (isDebug)
            Log.i(tag, msg);
        sendMsg(msg);
    }

    public static void e(String tag, final String msg) {
        if (isDebug)
            Log.i(tag, msg);
        if (L.logCallBack != null)
            L.logCallBack.message(msg);
        sendMsg(msg);
    }

    public static void v(String tag, final String msg) {
        if (isDebug)
            Log.i(tag, msg);
        sendMsg(msg);
    }


    //    //读文件
//    public static String readSDFile() throws IOException {
//        File sdDir = null;
//        boolean sdCardExist = Environment.getExternalStorageState()
//                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
//        if (sdCardExist) {
//            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
//        }
//        String replace = MApplication.context.getPackageName();
//        String path = sdDir.toString() + "/Android/data/" + replace + "/document/";
//        File dbFolder = new File(path);
//        // 目录不存在则自动创建目录
//        if (!dbFolder.exists()) {
//            dbFolder.mkdirs();
//        }
//
//        File file = new File(path + "log" + DateUtil.getDateToString(System.currentTimeMillis(), DateUtil.dateFormatYMD) + ".txt");
//        if (!file.exists()) {
//            file.createNewFile();
//        }
////        FileInputStream fis = new FileInputStream(file);
////
////        int length = fis.available();
////
////        byte[] buffer = new byte[length];
////        fis.read(buffer);
//
////        String res = EncodingUtils.getString(buffer, "UTF-8");
//
////        fis.close();
//
//
//        InputStreamReader read = new InputStreamReader(
//                new FileInputStream(file), "UTF-8");//考虑到编码格式
//        BufferedReader bufferedReader = new BufferedReader(read);
//        String lineTxt = null;
//        while ((lineTxt = bufferedReader.readLine()) != null) {
//            lineTxt = lineTxt + "\n";
//        }
//        read.close();
//        return lineTxt;
//    }

    //写文件
    public static void writeSDFile(String write_str) throws IOException {
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

        File file = new File(path + "log" + DateUtil.getDateToString(System.currentTimeMillis(), DateUtil.dateFormatYMD) + ".txt");
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8")); // 指定编码格式，以免读取时中文字符异常
        fw.append(write_str);
        fw.newLine();
        fw.flush(); // 全部写入缓存中的内容

        fw.close();
    }


    public interface LogCallBack {
        void message(String msg);
    }

    public static LogCallBack logCallBack;

    public static void setLogCalBack(LogCallBack logCallBack) {
        L.logCallBack = logCallBack;
    }


}