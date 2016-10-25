package com.iloomo.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.mobstat.StatService;
import com.iloomo.global.AppConfig;
import com.iloomo.global.MApplication;
import com.iloomo.paysdk.R;
import com.iloomo.utils.LCSharedPreferencesHelper;
import com.iloomo.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 用于tabhost
 * Created by wupeitao on 16/3/8.
 */
public class TabFragmentActivity extends FragmentActivity {
    private long exitTime = 0;
    protected MApplication jxbApplication;
    protected Context context = null;
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

    }
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        StatService.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        StatService.onResume(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showShort(this,getResources().getString(R.string.exit));
            exitTime = System.currentTimeMillis();
        } else {
            isExit();
        }
    }
    /**
     * 描述：tuichu
     *
     * @param
     */
    public void isExit() {
        jxbApplication.exit();
    }
}
