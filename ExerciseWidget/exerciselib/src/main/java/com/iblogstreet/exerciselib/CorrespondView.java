package com.iblogstreet.exerciselib;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * 类描述：对应题View
 * 创建人：王军
 * 创建时间：2018/1/18
 */

public class CorrespondView extends View {
    public final static int VERTICAL = 0;
    public final static int HORIZONTAL = 1;
    public final static int DEFAULT_ITEM_COUNT = 4;//默认小题数
    public List<View> mViewList;

    public CorrespondView(Context context) {
        this(context, null);
    }

    public CorrespondView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CorrespondView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void initView() {

    }

    public void initData() {

    }

    public void initAttr(AttributeSet attributeSet) {

    }

    public void initEvent() {

    }

    public void addViewToRoot() {

    }
}
