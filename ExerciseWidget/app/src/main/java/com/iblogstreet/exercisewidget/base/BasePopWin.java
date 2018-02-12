package com.iblogstreet.exercisewidget.base;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;


/**
 * 类描述：PopWin基类
 * 创建人：王军
 * 创建时间：2017/12/15
 */

public abstract class BasePopWin {
    /**
     * 遮罩层
     */
    private View mMask;
    /**
     * PopWin中的View
     */
    protected View mPopView;
    private Context mContext;
    /**
     * PopWin
     */
    protected PopupWindow mPopupWindow;

    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    public void setPopupWindow(PopupWindow mPopupWindow) {
        this.mPopupWindow = mPopupWindow;
    }


    public View getmMask() {
        return mMask;
    }

    public void setmMask(View mMask) {
        this.mMask = mMask;
    }

    public View getmPopView() {
        return mPopView;
    }

    public void setmPopView(View mPopView) {
        this.mPopView = mPopView;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public BasePopWin(Context context, View mask) {
        this.mMask = mask;
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mPopView = inflater.inflate(getLayoutById(), null);
        initView();
        initPopWin();
        initData();
        initEvent();
    }

    public void setViewByInflate(int layoutId) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mPopView = inflater.inflate(layoutId, null);
    }

    /**
     * 创建人：王军
     * 创建时间：2016/12/19 10:56
     * 描述：得到布局ID
     */
    public abstract int getLayoutById();

    public abstract void initView();

    public abstract void initEvent();

    public abstract void initData();

    public void initPopWin() {
        mPopupWindow = new PopupWindow(mPopView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(true);
        //为popWindow添加动画效果
        //mPopupWindow.setAnimationStyle(R.style.popWindow_animation);
        initPopWinEvent();
    }

    private void initPopWinEvent() {
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mMask != null)
                    mMask.setVisibility(View.GONE);
                popWinDismiss();
            }
        });
    }

    public void popWinDismiss() {

    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (mMask != null)
            mMask.setVisibility(View.VISIBLE);
        mPopupWindow.showAtLocation(parent, gravity, x, y);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        mPopupWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void hidePopWin() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            if (mMask != null)
                mMask.setVisibility(View.GONE);
            mPopupWindow.dismiss();
        }
    }
}
