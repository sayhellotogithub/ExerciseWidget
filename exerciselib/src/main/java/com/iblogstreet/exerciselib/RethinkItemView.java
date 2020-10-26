package com.iblogstreet.exerciselib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 描述：单选题，多选题选项封装
 *
 * @author 王军
 * @time 2018/01/12 11:20
 */

public class RethinkItemView extends RelativeLayout {
    private TextView mTvChoose;
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFULAT_WIDTH = 170;
    private static final int DEFAULT_SIZE = 20;
    private int mTextSelectedColor = DEFAULT_COLOR;
    private int mTextSize = DEFAULT_SIZE;
    private Drawable mNormalBackGround;
    private Drawable mSelectedBackGround;
    private int mTextNormalColor = DEFAULT_COLOR;
    private int mTextWidth = DEFULAT_WIDTH;
    private int mTextHeight = DEFULAT_WIDTH;
    private String mTextString = "Text";

    public RethinkItemView(Context context) {
        this(context, null);
    }

    public RethinkItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttrs(attrs);
        initData();
    }

    private void initData() {
        mTvChoose.setTextColor(mTextNormalColor);
        mTvChoose.setTextSize(mTextSize);
        mTvChoose.setBackground(mNormalBackGround);
        mTvChoose.setHeight(mTextHeight);
        mTvChoose.setWidth(mTextWidth);
        mTvChoose.setText(mTextString);
    }

    /**
     * 实始化属性
     *
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        //获得我们所定义的自定义样式属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RethinkItemView);
        if (typedArray != null) {
            int n = typedArray.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.RethinkItemView_rivTextNormalColor) {
                    mTextNormalColor = typedArray.getColor(attr, DEFAULT_COLOR);
                } else if (attr == R.styleable.RethinkItemView_rivTextSelectedColor) {
                    mTextSelectedColor = typedArray.getColor(attr, DEFAULT_COLOR);
                } else if (attr == R.styleable.RethinkItemView_rivTextSize) {
                    mTextSize = typedArray.getDimensionPixelSize(attr, DEFAULT_SIZE);
                } else if (attr == R.styleable.RethinkItemView_rivTextNormalBackGround) {
                    mNormalBackGround = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.RethinkItemView_rivTextSelectedBackGround) {
                    mSelectedBackGround = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.RethinkItemView_rivTextHeight) {
                    mTextHeight = typedArray.getDimensionPixelSize(attr, DEFULAT_WIDTH);
                } else if (attr == R.styleable.RethinkItemView_rivTextWidth) {
                    mTextWidth = typedArray.getDimensionPixelSize(attr, DEFULAT_WIDTH);
                } else if (attr == R.styleable.RethinkItemView_rivTextString) {
                    mTextString = typedArray.getString(attr);
                }
            }
            typedArray.recycle();
        }
    }


    /**
     * 实始化View
     */
    private void initView() {
        mTvChoose = new TextView(getContext());
        mTvChoose.setGravity(Gravity.CENTER);
        addView(mTvChoose);
    }

    public void setChooseText(String msg) {
        mTvChoose.setText(msg);
    }

    public void setChooseTextWidth(int width) {
        mTvChoose.setWidth(width);
    }

    public void setChooseTextBackGround(Drawable drawable) {
        mTvChoose.setBackground(drawable);
    }

    public void setChooseTextHeight(int height) {
        mTvChoose.setHeight(height);
    }

    public void setChooseTextColor(int color) {
        mTvChoose.setTextColor(color);
    }

    public void setChoooseTextSize(float size) {
        mTvChoose.setTextSize(size);
    }
}


