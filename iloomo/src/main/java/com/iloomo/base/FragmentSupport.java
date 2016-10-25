package com.iloomo.base;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.baidu.mobstat.StatService;
import com.iloomo.utils.L;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by wupeitao on 16/1/7.
 */
public class FragmentSupport extends Fragment implements IFragmentSupport {
    public Context context;
    public String title;
    public View rootView;// 缓存Fragment view

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();
        if (rootView == null) {
            rootView = initView();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return this.rootView;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public void onResume() {
        super.onResume();
        /**
         * 百度统计
         * 页面起始（注意： 每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 如果该FragmentActivity包含了几个全页面的fragment，那么可以在fragment里面加入就可以了，这里可以不加入。如果不加入将不会记录该Activity页面。
         */
//        StatService.onResume(this);
        /***
         * 百度统计
         */
        StatService.onPageStart(getActivity(), title);
        MobclickAgent.onPageStart(title);
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         * 百度统计
         * 页面结束（注意： 每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 如果该FragmentActivity包含了几个全页面的fragment，那么可以在fragment里面加入就可以了，这里可以不加入。如果不加入将不会记录该Activity页面。
         */
//        StatService.onPause(this);
        StatService.onPageEnd(getActivity(), title);
        MobclickAgent.onPageEnd(title);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindDrawables(rootView);
        System.gc();
    }

    private void unbindDrawables(View view) {
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

    public void setContent(int layout) {
        this.rootView = LayoutInflater.from(context).inflate(layout, null);
    }

    @Override
    public View initView() {

        return rootView;
    }
}
