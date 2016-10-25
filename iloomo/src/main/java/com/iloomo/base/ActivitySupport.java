package com.iloomo.base;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.baidu.mobstat.StatService;
import com.iloomo.global.AppConfig;
import com.iloomo.global.MApplication;
import com.iloomo.net.AsyncHttpGet;
import com.iloomo.net.AsyncHttpPost;
import com.iloomo.net.BaseRequest;
import com.iloomo.net.DefaultThreadPool;
import com.iloomo.net.ThreadCallBack;
import com.iloomo.paysdk.R;
import com.iloomo.utils.LCSharedPreferencesHelper;
import com.iloomo.widget.TitleBar;
import com.umeng.analytics.MobclickAgent;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


/**
 * Actity 基类
 *
 * @author wpt
 */
@SuppressLint("NewApi")
public class ActivitySupport extends FragmentActivity implements
        IActivitySupport {


    protected Context context = null;
    protected MApplication jxbApplication;
    protected LCSharedPreferencesHelper sharedPreferencesHelper = null;
    private Toast mToast;
    private TitleBar titleBar;
    private LinearLayout linearLayout;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeInput();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 沉浸通知栏
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
        super.onCreate(savedInstanceState);
        initDate();
    }

    private void initDate() {
        // TODO Auto-generated method stub
        context = this;
        jxbApplication = (MApplication) getApplication();
        jxbApplication.addActivity(this);
        sharedPreferencesHelper = new LCSharedPreferencesHelper(context,
                AppConfig.SHARED_PATH);
    }

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);// 设置布局LinearLayout的属性
        View customtitle = LayoutInflater.from(this).inflate(
                R.layout.layout_activitytitle, null);
        titleBar = (TitleBar) customtitle.findViewById(R.id.lc_title);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        // 自己的布局
        View customContentView = LayoutInflater.from(this).inflate(layoutResID,
                null);
        linearLayout.addView(customtitle, lp1);
        LinearLayout.LayoutParams lpcontent = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.addView(customContentView, lpcontent);

        super.setContentView(linearLayout);
    }

    protected void mIntent(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        packageContext.startActivity(intent);
    }

    /**
     * 设置titleb中间
     *
     * @param title
     */
    protected void setRemoveTitle() {
        linearLayout.removeViewAt(0);
    }

    /**
     * 设置titleb中间
     *
     * @param title
     */
    protected void setCtenterTitle(String title) {
        titleBar.setCenterTitle(title);
    }

    /**
     * 设置titleb中间
     *
     * @param title
     */
    protected void setCtenterTitle(int title) {
        titleBar.setCenterTitle(mString(title));
    }

    /**
     * 设置左边的是否显示
     */
    protected void isLeftVisibility(boolean isVisibit) {
        titleBar.isLeftVisibility(isVisibit);
    }

    /**
     * 设置中间标题是否显示
     */
    protected void isCenterVisibility(boolean isVisibit) {
        titleBar.isCenterVisibility(isVisibit);
    }

    /**
     * 设置右边标题内容
     */
    protected void setRightTitle(String msg) {
        titleBar.setRightTitle(msg);
    }

    /**
     * 设置右边标题内容字体颜色
     */
    protected void setRightTitle(int msg) {
        titleBar.setRightTitle(msg);
    }

    /**
     * 设置右边图片内容
     */
    protected void setRightTitleRes(int msg) {
        titleBar.setRightTitleRes(msg);
    }

    /**
     * 设置右边标题内容
     */
    protected void setRightTitleListener(OnClickListener l) {
        titleBar.setRightTitleListener(l);
    }

    /**
     * 设置右边图片内容
     */
    protected void setRightTitleImageListener(OnClickListener l) {
        titleBar.setRightTitleImageListener(l);
    }

    /**
     * 设置title背景颜色
     */
    public void setBackGb(int color) {
        titleBar.setBackGb(color);
    }


    /**
     * 设置”返回“是否隐藏
     */
    public void isLeftTitleVisibility(boolean isVisibit) {
        titleBar.isLeftTitleVisibility(isVisibit);
    }

    /**
     * 设置返回监听
     */
    public void setOnclickBackListener(OnClickListener l) {
        titleBar.setOnclickBackListener(l);
    }

    /**
     * 设置左边的内容
     */
    public void setLeftTitle(String msg) {
        titleBar.setLeftTitle(msg);
    }

    /**
     * 设置左边的内容字体颜色
     */
    public void setLeftTitleColor(int msg) {
        titleBar.setLeftTitleColor(msg);
    }


    /**
     * 设置左边的图片
     */
    public void setLeftImage(int draw) {
        titleBar.setLeftImage(draw);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 百度统计
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onResume(this);
/**
 * 友盟统计
 */
        String centerTitle = titleBar.getCenterTitle();
        if (TextUtils.isEmpty(centerTitle))
            centerTitle = "Activity";
        MobclickAgent.onPageStart(centerTitle);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 百度统计
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onPause(this);
/**
 * 友盟统计
 */
        String centerTitle = titleBar.getCenterTitle();
        if (TextUtils.isEmpty(centerTitle))
            centerTitle = "Activity";
        MobclickAgent.onPageEnd(centerTitle);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(linearLayout);
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view != null) {
            return;
        }
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

    }


    /**
     * 描述：tuichu
     *
     * @param
     */
    @Override
    public void isExit() {
        jxbApplication.exit();
    }

    @Override
    public boolean hasInternetConnected() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateInternet() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            openWirelessSet();
            return false;
        } else {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        openWirelessSet();
        return false;
    }

    @Override
    public boolean hasLocationNetWork() {
        LocationManager manager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        if (manager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void checkMemoryCard() {
        //
        // new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
        // .setTitle("警告")
        // .setMessage("请检查内存卡")
        // .setPositiveButton("设置",
        // new DialogInterface.OnClickListener() {
        // @Override
        // public void onClick(DialogInterface dialog,
        // int which) {
        // dialog.cancel();
        // Intent intent = new Intent(
        // Settings.ACTION_SETTINGS);
        // context.startActivity(intent);
        // }
        // })
        // .setNegativeButton("�?�?",
        // new DialogInterface.OnClickListener() {
        // @Override
        // public void onClick(DialogInterface dialog,
        // int which) {
        // dialog.cancel();
        // jxbApplication.exit();
        // }
        // }).create().show();

        // }
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            // showDialog(new OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            // // TODO Auto-generated method stub
            // dialog.cancel();
            jxbApplication.exit();
            // }
            // }, new OnClickListener() {
            //
            // @Override
            // public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
            // dialog.cancel();
            // }
            // }, mString(string.check_sd), mString(string.cancel),
            // mString(string.checking));
        }
    }

    public void openWirelessSet() {
        // showDialog(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // dialog.cancel();
        // jxbApplication.exit();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        context.startActivity(intent);
        // dialog.cancel();
        // }
        // }, new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // // TODO Auto-generated method stub
        // // Intent intent = new
        // // Intent(Settings.ACTION_WIRELESS_SETTINGS);
        // // context.startActivity(intent);
        // // dialog.cancel();
        // }
        // }, mString(string.check_network), mString(string.checking),
        // mString(string.checking));

    }

    /**
     * 显示toast
     *
     * @param text
     * @param longint
     * @author wpt
     * @update 2012-6-28 下午3:46:18
     */
    public void showToast(String text, int longint) {
        Toast.makeText(context, text, longint).show();
    }

    @Override
    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 描述：Toast提示文本.
     *
     * @param resId 文本的资源ID
     */
    public void showToast(int resId) {
        Toast.makeText(this, "" + this.getResources().getText(resId),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        closeInput();
        super.onBackPressed();
    }

    /**
     * 关闭键盘事件
     *
     * @author wpt
     * @update 2012-7-4 下午2:34:34
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public MApplication getJXBApplication() {
        return jxbApplication;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jxb.activity.IActivitySupport#mString(int)
     */
    @Override
    public String mString(int string) {
        // TODO Auto-generated method stub
        return getResources().getString(string);
    }

    // 转换unicode
    public String loadConvert(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    private static String key = "a6U&1$Ip[Jr/sed]Rfvn=O>Mz+}lXN*%-gLcGD|0";

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    // SHA1 加密实例
    public static String encryptToSHA(String info) {
        byte[] digesta = null;
        try {
            // 得到一个SHA-1的消息摘要
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            // 添加要进行计算摘要的信息
            alga.update(info.getBytes());
            // 得到该摘要
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将摘要转为字符串a
        String rs = byte2hex(digesta);
        return rs + key;
    }

    // 获取唯一标示
    public String getOnly() {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        LCSharedPreferencesHelper lc = new LCSharedPreferencesHelper(context,
                "1");
        WifiInfo info = wifi.getConnectionInfo();
        String IMEI = 0 + "";
        try {

            IMEI = telephonyManager.getDeviceId();
            if (TextUtils.isEmpty(IMEI)) {
                IMEI = info.getMacAddress();
            }
        } catch (Exception e) {
            if (TextUtils.isEmpty(lc.getValue("uuid"))) {
                lc.putValue("uuid", getUUID());
            } else {
                IMEI = lc.getValue("uuid");
            }
        }
        return IMEI.toLowerCase();
    }

    public String getUUID() {

        String uuidStr = UUID.randomUUID().toString();
        uuidStr = uuidStr.substring(0, 8) + uuidStr.substring(9, 13)
                + uuidStr.substring(14, 18) + uuidStr.substring(19, 23)
                + uuidStr.substring(24);

        return uuidStr;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    // 时间戳
    public String gettime() {

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式

        String hehe = dateFormat.format(now);
        Date date;
        try {
            date = dateFormat.parse(hehe);
            return date.getTime() + "";
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return System.currentTimeMillis() + "";
        }

    }

    public void releaseImageView(ImageView imageView) {
        Drawable d = imageView.getDrawable();
        if (d != null)
            d.setCallback(null);
        imageView.setImageDrawable(null);
        imageView.setBackgroundDrawable(null);
    }

    public void releaseRelativeLayout(RelativeLayout imageView) {
        Drawable d = imageView.getBackground();
        if (d != null)
            d.setCallback(null);
        imageView.setBackgroundDrawable(null);
    }


}
