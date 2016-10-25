package com.iloomo.brush.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.iloomo.utils.L;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by wupeitao on 16/3/4.
 */
public class CMDutils {
    public CMDutils(Context context) {
        this.context = context;
    }

    public Context context;
    public static CMDutils alarmUtils;

    public static CMDutils getInstance(Context context) {
        if (alarmUtils == null) {
            alarmUtils = new CMDutils(context);
        }
        return alarmUtils;
    }


    private String cmd_install = "pm install -r ";
    private String cmd_uninstall = "pm uninstall ";
    String apkLocation = Environment.getExternalStorageDirectory().toString() + "/";


    public int excuteSuCMD(String cmd) {
        String s = "\n";
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream((OutputStream) process.getOutputStream());
            // 部分手机Root之后Library path 丢失，导入path可解决该问题
            dos.writeBytes((String) "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
            cmd = String.valueOf(cmd);
            dos.writeBytes((String) (cmd + "\n"));
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                s += line + "\n";
            }

            String error = null;
            DataInputStream ise = new DataInputStream(process.getErrorStream());

            while ((error = ise.readLine()) != null) {
                s += error + "\n";
            }
            L.e(s);
            process.waitFor();
            int result = process.exitValue();
            return (Integer) result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }

    public int cmd_install(String path) {
        String cmd = cmd_install + path;
        return excuteSuCMD(cmd);
    }

    public int cmd_uninstall(String packages) {
        String cmd = cmd_uninstall + packages;
        return excuteSuCMD(cmd);

    }


    public static boolean startApk(String packageName, String activityName) {
        boolean isSuccess = false;

        String cmd = "am start -n " + packageName + "/" + activityName + " \n";
        try {
            Process process = Runtime.getRuntime().exec(cmd);

            isSuccess = waitForProcess(process);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    private static boolean waitForProcess(Process p) {
        boolean isSuccess = false;
        int returnCode;
        try {
            returnCode = p.waitFor();
            switch (returnCode) {
                case 0:
                    isSuccess = true;
                    break;
                case 1:
                    break;

                default:
                    break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return isSuccess;
    }

    public Integer cmd_reboot() {
        String cmd = "reboot";

        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream((OutputStream) process.getOutputStream());
            // 部分手机Root之后Library path 丢失，导入path可解决该问题
            dos.writeBytes((String) "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
            cmd = String.valueOf(cmd);
            dos.writeBytes((String) (cmd + "\n"));
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
            int result = process.exitValue();
            return (Integer) result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }

    }

    public void install() {
        String apkAbsolutePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/a.apk";
        File f = new File(apkAbsolutePath);
        if (!f.exists()) {
            L.d("can not found " + apkAbsolutePath);
            // Toast.makeText(this, "can not found "+apkAbsolutePath,
            // Toast.LENGTH_SHORT).show();
        }
        String[] args = {"pm", "install", "-r", apkAbsolutePath};// 先卸载后安装
        String result = "";
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            process = processBuilder.start();
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write('\n');
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            result = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }

        if (result.endsWith("Success")) {//
            L.d("install Success ");
            // Toast.makeText(this, "install Success ",
            // Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent();
//            intent.setClassName(this, "com.diaobaosq.application.DiaoBaoApplication");
//            startActivity(intent);
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
        }
    }
}
