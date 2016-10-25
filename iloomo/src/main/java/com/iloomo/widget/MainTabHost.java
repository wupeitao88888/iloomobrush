package com.iloomo.widget;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;


import com.iloomo.paysdk.R;

public class MainTabHost extends LinearLayout {


    private Context context;
    public TabFragmentHost mTabHost;
    // 标签
    private String[] tabTag;
    // 自定义tab布局显示文本和顶部的图片
    private Integer[] imgTab;
    private Class[] classTab;
    // tab选中背景 drawable 样式图片 背景都是同一个,背景颜色都是 白色。。。
    private Integer[] styleTab;
    private View inflate;
    private FragmentActivity fragmentActivity;
    public MainTabHost(Context context, FragmentActivity fragmentActivity) {
        super(context);
        this.context=context;
        this.fragmentActivity=fragmentActivity;
        init(context);

    }

    public MainTabHost(Context context, AttributeSet attrs, FragmentActivity fragmentActivity) {
        super(context, attrs);
        this.context=context;
        this.fragmentActivity=fragmentActivity;
        init(context);
    }


    protected void init(Context context) {
        inflate = LayoutInflater.from(context).inflate(R.layout.maintabs, null);
        setupView();
        setLinstener();
        fillData();
        addView(inflate);
    }


    /***
     * 初始化第一步
     *
     * @param tagcount 个数
     */
    public void setTag(int tagcount) {
        tabTag = new String[tagcount];
        for (int i = 0; i < tagcount; i++) {
            tabTag[i] = "tab" + i;
        }

    }

    /***
     * 第二步、初始化图片
     *
     * @param imgTab
     */
    public void setTabDrawable(Integer[] imgTab) {
        this.imgTab = imgTab;
    }

    /***
     * 第三步、初始化Fragment
     *
     * @param classTab
     */
    public void setTabFragment(Class[] classTab) {
        this.classTab = classTab;
    }
    public void setTabBackground(Integer[] styleTab) {
        this.styleTab = styleTab;
        initValue();
    }




    private void setupView() {

        // 实例化framentTabHost
        mTabHost = (TabFragmentHost) inflate.findViewById(android.R.id.tabhost);
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        mTabHost.setup(context, supportFragmentManager,
                android.R.id.tabcontent);
    }

    private void initValue() {

        // 初始化tab选项卡
        InitTabView();
    }

    private void setLinstener() {
        // imv_back.setOnClickListener(this);

    }

    private void fillData() {
        // TODO Auto-generated method stub

    }

    // 初始化 tab 自定义的选项卡 ///////////////
    private void InitTabView() {
        // 可以传递参数 b;传递公共的userid,version,sid
        Bundle bundle = new Bundle();
        // 循环加入自定义的tab
        for (int i = 0; i < tabTag.length; i++) {
            // 封装的自定义的tab的样式
            View indicator = getIndicatorView(i);
            mTabHost.addTab(
                    mTabHost.newTabSpec(tabTag[i]).setIndicator(indicator),
                    classTab[i], bundle);// 传递公共参数
        }
        mTabHost.getTabWidget().setDividerDrawable(R.color.white);
    }

    // 设置tab自定义样式:注意 每一个tab xml子布局的linearlayout 的id必须一样
    private View getIndicatorView(int i) {
        // 找到tabhost的子tab的布局视图
        LinearLayout tv_lay= (LinearLayout) LayoutInflater.from(context).inflate(imgTab[i], null);
        tv_lay.setBackgroundResource(styleTab[i]);
        return tv_lay;
    }
}
