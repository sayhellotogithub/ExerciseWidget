package com.iblogstreet.exercisewidget.base;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;


public abstract class BaseActivity extends Activity {

    FragmentManager mFm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSet();
        setContentView(getLayoutById());
        ButterKnife.bind(this);
        mFm = getFragmentManager();


        initView();
        initData();
        initEvent();
        initAnimation();
    }

    /**
     * 创建人：王军
     * 创建时间：2016/12/19 15:19
     * 描述：初始化动画
     */
    public void initAnimation() {
    }

    /**
     * 创建人：王军
     * 创建时间：2016/12/19 10:56
     * 描述：初始化事件，如Button的OnClick
     */
    public void initEvent() {
    }

    /**
     * 创建人：王军
     * 创建时间：2016/12/19 10:56
     * 描述： 初始化数据
     */
    public void initData() {
    }

    /**
     * 创建人：王军
     * 创建时间：2016/12/19 10:56
     * 描述：初始化View
     */
    public  void initView(){}

    /**
     * 创建人：王军
     * 创建时间：2016/12/19 10:56
     * 描述：得到布局ID
     */
    public abstract int getLayoutById();

    /**
     * 创建人：王军
     * 创建时间：2016/12/19 10:56
     * 描述：初始化基本设置
     */
    private void initSet() {
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置屏幕方向为水平
//
//        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏虚拟按键
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                // bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN);// hide status bar
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
