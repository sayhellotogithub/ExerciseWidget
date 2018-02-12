package com.iblogstreet.exercisewidget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.iblogstreet.exerciselib.RethinkView;
import com.iblogstreet.exerciselib.utils.Loger;
import com.iblogstreet.exercisewidget.base.BasePopWin;


/**
 * 类描述：RethinkPopWin
 * 创建人：王军
 * 创建时间：2018/1/17
 */

public class RethinkPopWin extends BasePopWin implements RethinkView.OnRethinkViewListener {
    private RethinkView rethinkView;

    public RethinkPopWin(Context context, View mask) {
        super(context, mask);
    }

    @Override
    public int getLayoutById() {
        return R.layout.pop_rethink;
    }

    @Override
    public void initView() {
        mPopView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        rethinkView = (RethinkView) mPopView.findViewById(R.id.rethinkView);
    }

    @Override
    public void initEvent() {
        rethinkView.setmOnRethinkViewListener(this);
    }

    @Override
    public void initData() {
        rethinkView.setmAnswer(1);
    }

    @Override
    public void initPopWin() {
        mPopupWindow = new PopupWindow(mPopView,
                mPopView.getMeasuredWidth(), mPopView.getMeasuredHeight(), false);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(false);
    }

    boolean mIsFirst = true;

    public void showPopWin(final View anchorView) {

        mPopView.post(new Runnable() {
            @Override
            public void run() {
                // 自动调整箭头的位置
                autoAdjustArrowPos(mPopupWindow, mPopView, anchorView);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mIsFirst) {
                mPopupWindow.showAsDropDown(anchorView);
            } else {
                mPopupWindow.showAsDropDown(anchorView, -mPopView.getWidth() / 2 + anchorView.getWidth() / 2, 0, Gravity.CENTER_HORIZONTAL);
            }
        } else {
            mPopupWindow.showAsDropDown(anchorView);
        }
        mIsFirst = false;
    }

    /**
     * 用于计算箭头的位置
     *
     * @param popupWindow
     * @param contentView
     * @param anchorView
     */
    private void autoAdjustArrowPos(PopupWindow popupWindow, View contentView, View anchorView) {
        View upArrow = contentView.findViewById(R.id.upArrow);
        View downArrow = contentView.findViewById(R.id.downArrow);

        int pos[] = new int[2];
        contentView.getLocationOnScreen(pos);
        int popLeftPos = pos[0];
        anchorView.getLocationOnScreen(pos);
        int anchorLeftPos = pos[0];
        int arrowLeftMargin = anchorLeftPos - popLeftPos + anchorView.getWidth() / 2 - upArrow.getWidth() / 2;

        upArrow.setVisibility(popupWindow.isAboveAnchor() ? View.INVISIBLE : View.VISIBLE);
        downArrow.setVisibility(popupWindow.isAboveAnchor() ? View.VISIBLE : View.INVISIBLE);

        RelativeLayout.LayoutParams upArrowParams = (RelativeLayout.LayoutParams) upArrow.getLayoutParams();
        upArrowParams.leftMargin = arrowLeftMargin;
        RelativeLayout.LayoutParams downArrowParams = (RelativeLayout.LayoutParams) downArrow.getLayoutParams();
        downArrowParams.leftMargin = arrowLeftMargin;
    }

    @Override
    public void onRethinkItemClick(int result, String chooseString) {
        Loger.e("RethinkPopWin", "result" + result + ",chooseString" + chooseString);
    }
}
