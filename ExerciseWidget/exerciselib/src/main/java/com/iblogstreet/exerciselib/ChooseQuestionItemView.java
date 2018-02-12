package com.iblogstreet.exerciselib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 描述：单选题，多选题选项封装
 *
 * @author 王军
 * @time 2018/01/12 11:20
 */

public class ChooseQuestionItemView extends RelativeLayout {
    public final static int DEFAULT_COLOR = Color.BLACK;
    public final static int DEFAULT_SIZE = 16;
    public final static int DEFAULT_WIDTH = 30;
    private TextView mTvChoose;
    private int textColor = DEFAULT_COLOR;
    private int textSize = DEFAULT_SIZE;
    private Drawable mNormalBackGround;
    private Drawable mErrorBackGround;
    private Drawable mSelectedBackGround;
    private int mTextWidth;
    private int mTextHeight;

    public ChooseQuestionItemView(Context context) {
        this(context, null);
    }

    public ChooseQuestionItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttrs(attrs);
        initData();
    }

    private void initData() {
        mTvChoose.setTextColor(textColor);
        mTvChoose.setTextSize(textSize);
        mTvChoose.setBackground(mNormalBackGround);
        mTvChoose.setText("A");
        mTvChoose.setWidth(mTextWidth);
        mTvChoose.setHeight(mTextHeight);
    }

    /**
     * 实始化属性
     *
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        //获得我们所定义的自定义样式属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ChooseQuestionItemView);
        if (typedArray != null) {
            int n = typedArray.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.ChooseQuestionItemView_textErrorBackGround) {
                    mErrorBackGround = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.ChooseQuestionItemView_textColor) {
                    textColor = typedArray.getColor(attr, DEFAULT_COLOR);
                } else if (attr == R.styleable.ChooseQuestionItemView_textSize) {
                    textSize = typedArray.getDimensionPixelSize(attr, DEFAULT_SIZE);
                } else if (attr == R.styleable.ChooseQuestionItemView_textNormalBackGround) {
                    mNormalBackGround = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.ChooseQuestionItemView_textSelectedBackGround) {
                    mSelectedBackGround = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.ChooseQuestionItemView_textWidth) {
                    mTextWidth = typedArray.getDimensionPixelSize(attr, DEFAULT_WIDTH);
                } else if (attr == R.styleable.ChooseQuestionItemView_textHeight) {
                    mTextHeight = typedArray.getDimensionPixelSize(attr, DEFAULT_WIDTH);
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
        }

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


