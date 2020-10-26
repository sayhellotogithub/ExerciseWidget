package com.iblogstreet.exerciselib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.iblogstreet.exerciselib.bean.Coordinate;
import com.iblogstreet.exerciselib.listener.OnAnswerChangeListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 描述：对应题
 *
 * @author 王军
 * @time 2018/01/1811:20
 */
public class DrawLineViewWrapper extends ViewGroup implements DrawLineView.OnDrawLineViewListener {
    private static final String TAG = "DrawLineView_wrapper";
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 0;
    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_SIZE = 16;
    public static final int DEFAULT_WIDTH = 40;
    public static final int DEFAULT_COUNT = 6;
    public static final String[] NUMBERS = {"1", "2", "3", "4", "5", "6"};
    public static final String[] LETTERS = {"A", "B", "C", "D", "E", "F"};

    private int mItemCount = DEFAULT_COUNT;//默认题目最大数量
    /**
     * 数字列表
     */
    private List<String> mNumberList;
    /**
     * 字母列表
     */
    private List<String> mLetterList;
    /**
     * 上层绘制区
     */
    private DrawLineView mDrawLv;


    private OnAnswerChangeListener mAnswerChangeListener;
    /**
     * 方向
     */
    private int mOrientation = HORIZONTAL;
    /**
     * 线的正常颜色
     */
    private int mLineNormalColor = DEFAULT_COLOR;
    /**
     * 线的错误颜色
     */
    private int mLineErrorColor = DEFAULT_COLOR;
    /**
     * 字体的大小
     */
    private int mItemTextSize = DEFAULT_SIZE;
    /**
     * 选项的高度
     */
    private int mItemWidth = DEFAULT_WIDTH;
    /**
     * 选项的宽度
     */
    private int mItemHeight = DEFAULT_WIDTH;
    /**
     * 选项的选中的背景
     */
    private Drawable mItemTextSelectedBackground;

    /**
     * 选项的正常背景
     */
    private Drawable mItemTextNormalBackground;
    /**
     * 选项的错误背景
     */
    private Drawable mItemTextErrorBackground;
    /**
     * 选项的正常字体颜色
     */
    private int mItemTextNormalColor = DEFAULT_COLOR;
    /**
     * 选项的选中字体颜色
     */
    private int mItemTextSelectedColor = DEFAULT_COLOR;
    /**
     * 选项错误的颜色
     */
    private int mItemTextErrorColor = DEFAULT_COLOR;
    /**
     * Item之间的距离
     */
    private int mItemMargin = 0;
    /**
     * 行之间的距离
     */
    private int mLineBetweenMargin = 0;
    /**
     * 正确答案
     */
    private StringBuilder mRightAnswer;
    /**
     * 错误答案
     */
    private StringBuilder mAnswer;
    /**
     * 总宽度
     */
    private int mTotalWidth;
    /**
     * 总高度
     */
    private int mTotalHeight;
    /**
     * Item的宽度
     */
    private int mChildWidth = 0;
    /**
     * Item的高度
     */
    private int mChildHeight = 0;

    public void setOnAnswerchangeListener(OnAnswerChangeListener listener) {
        mAnswerChangeListener = listener;
    }

    public DrawLineViewWrapper(Context context) {
        this(context, null);
    }

    public DrawLineViewWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
        initData();
        initEvent();
    }

    private void initAttrs(AttributeSet attrs) {
        if (null != attrs) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DrawLineViewWrapper);
            if (null != typedArray) {
                int n = typedArray.getIndexCount();
                for (int i = 0; i < n; i++) {
                    int attr = typedArray.getIndex(i);
                    if (attr == R.styleable.DrawLineViewWrapper_dlvwOrientation) {
                        mOrientation = typedArray.getInteger(attr, HORIZONTAL);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwLineNormalColor) {
                        mLineNormalColor = typedArray.getColor(attr, DEFAULT_COLOR);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwLineErrorColor) {
                        mLineErrorColor = typedArray.getColor(attr, DEFAULT_COLOR);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwItemTextSize) {
                        mItemTextSize = typedArray.getDimensionPixelSize(attr, DEFAULT_SIZE);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwItemTextWidth) {
                        mItemWidth = typedArray.getDimensionPixelSize(attr, DEFAULT_WIDTH);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwItemTextHeight) {
                        mItemHeight = typedArray.getDimensionPixelSize(attr, DEFAULT_WIDTH);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwItemTextSelectedBackground) {
                        mItemTextSelectedBackground = typedArray.getDrawable(attr);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwItemTextNormalBackground) {
                        mItemTextNormalBackground = typedArray.getDrawable(attr);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwItemTextErrorBackground) {
                        mItemTextErrorBackground = typedArray.getDrawable(attr);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwItemTextNormalColor) {
                        mItemTextNormalColor = typedArray.getColor(attr, DEFAULT_COLOR);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwItemTextSelectedColor) {
                        mItemTextSelectedColor = typedArray.getColor(attr, DEFAULT_COLOR);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwItemTextErrorColor) {
                        mItemTextErrorColor = typedArray.getColor(attr, DEFAULT_COLOR);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwItemCount) {
                        mItemCount = typedArray.getInteger(attr, DEFAULT_COUNT);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwItemMargin) {
                        mItemMargin = typedArray.getDimensionPixelSize(attr, 0);
                    } else if (attr == R.styleable.DrawLineViewWrapper_dlvwLineBetweenMargin) {
                        mLineBetweenMargin = typedArray.getDimensionPixelSize(attr, 0);
                    }
                }
                typedArray.recycle();

            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    private void initView() {

    }

    /**
     * 初始化点在屏幕的位置
     */
    private void initPoint() {
        mDrawLv.mCoordinates.clear();
        int[] parentLocation = new int[2];
        getLocationOnScreen(parentLocation);
        mDrawLv.setmCircle(mChildWidth / 2);
        //父View的左边距
        int mLeft = parentLocation[0];
        //父View的右边距
        int mTop = parentLocation[1];
        // Loger.e(TAG, "parent left:" + mLeft + ",top:" + mTop);
        int[] location = new int[2];
        int circle = mChildWidth / 2;
        for (int i = 0; i < mItemCount * 2; i++) {
            getChildAt(i).getLocationOnScreen(location);
            int x = location[0] - mLeft;
            int y = location[1] - mTop;
            // Loger.e(TAG, " child  x:" + location[0] + ",y:" + location[1]);
            mDrawLv.mCoordinates.add(new Coordinate(x + circle, y + circle));
        }

    }

    private LayoutParams getSelfLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        return params;
    }

    private LayoutParams getDrawLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        return params;
    }

    /**
     * 将子View添加到根View
     */
    private void addViewToRoot() {
        if (mDrawLv != null) {
            mDrawLv.setOnDrawLineViewListener(null);
        }
        removeAllViews();
        //Item添加距离
        LayoutParams parm = getSelfLayoutParams();
        //添加上边的View
        for (int i = 0; i < mItemCount; i++) {
            addView(getChooseQuestionItemView(parm, mNumberList.get(i)));
        }
        //添加下边的View
        for (int i = 0; i < mItemCount; i++) {
            addView(getChooseQuestionItemView(parm, mLetterList.get(i)));
        }
        //添加遮罩层
        LayoutParams drawParams = getDrawLayoutParams();
        mDrawLv = new DrawLineView(getContext());
        mDrawLv.setLayoutParams(drawParams);
        addView(mDrawLv);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        mTotalHeight = 0;
        mChildWidth = getChildAt(0).getMeasuredWidth();
        mChildHeight = getChildAt(0).getMeasuredHeight();
        if (mOrientation == HORIZONTAL) {
            //计算总宽度
            mTotalWidth = mItemCount * mChildWidth + (mItemCount - 1) * mItemMargin;
            mTotalHeight = mLineBetweenMargin + mChildHeight * 2;

        } else {
            mTotalWidth = mChildWidth * 2 + mLineBetweenMargin;
            mTotalHeight = mItemCount * mChildHeight + (mItemCount - 1) * mItemMargin;
        }
        //  Loger.e(TAG, "onMeasure mTotalHeight:" + mTotalHeight + " mTotalWidth" + mTotalWidth);
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(mTotalWidth, widthMode), MeasureSpec.makeMeasureSpec(mTotalHeight, heightMode));
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //  Loger.e(TAG, "left:" + l + ",top:" + t + ",right:" + r + ",bottom:" + b);
        int left = 0, top = 0, right = 0, bottom = 0;
        //水平
        if (mOrientation == HORIZONTAL) {
            //重新布局位置
            for (int i = 0; i < getChildCount(); i++) {
                if (i < mItemCount) {
                    left = i * mChildWidth + i * mItemMargin;
                    top = 0;
                    right = left + mChildWidth;
                    bottom = top + mChildHeight;
                    getChildAt(i).layout(left, top, right, bottom);
                } else if (i < mItemCount * 2) {
                    left = (i - mItemCount) * mChildWidth + (i - mItemCount) * mItemMargin;
                    top = mChildHeight + mLineBetweenMargin;
                    right = left + mChildWidth;
                    bottom = top + mChildHeight;
                    getChildAt(i).layout(left, top, right, bottom);

                } else {
                    View drawLineView = getChildAt(i);
                    // Loger.e(TAG, "DrawLineView getMeasuredWidth:" + drawLineView.getMeasuredWidth() + "getMeasuredHeight:" + drawLineView.getMeasuredHeight());
                    drawLineView.layout(0, 0, mTotalWidth, mTotalHeight);
                }
            }
        } else {
            //垂直
            for (int i = 0; i < getChildCount(); i++) {
                if (i < mItemCount) {
                    left = 0;
                    top = i * mChildHeight + i * mItemMargin;
                    right = left + mChildWidth;
                    bottom = top + mChildHeight;
                    getChildAt(i).layout(left, top, right, bottom);
                } else if (i < mItemCount * 2) {
                    left = mChildWidth + mLineBetweenMargin;
                    top = (i - mItemCount) * mChildHeight + (i - mItemCount) * mItemMargin;
                    right = left + mChildWidth;
                    bottom = top + mChildHeight;
                    getChildAt(i).layout(left, top, right, bottom);

                } else {
                    View drawLineView = getChildAt(i);
                    //Loger.e(TAG, "DrawLineView getMeasuredWidth:" + drawLineView.getMeasuredWidth() + "getMeasuredHeight:" + drawLineView.getMeasuredHeight());
                    drawLineView.layout(0, 0, mTotalWidth, mTotalHeight);
                }
            }
        }

    }

    private void initEvent() {
        if (mDrawLv != null) mDrawLv.setOnDrawLineViewListener(this);
    }

    private void initData() {
        mNumberList = new ArrayList<>();
        mLetterList = new ArrayList<>();
        mRightAnswer = new StringBuilder();
        mAnswer = new StringBuilder();
        mItemCount = mItemCount > NUMBERS.length ? NUMBERS.length : mItemCount;
        for (int i = 0; i < mItemCount; i++) {
            mNumberList.add(NUMBERS[i]);
            mLetterList.add(LETTERS[i]);
        }
        addViewToRoot();

        mDrawLv.setmIsHorizontal(mOrientation == HORIZONTAL);
        mDrawLv.initPaint(mLineNormalColor, mLineErrorColor);
        mDrawLv.setmItemCount(mItemCount);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        post(new Runnable() {
            @Override
            public void run() {
                initPoint();
            }
        });
    }

    @NonNull
    private ChooseQuestionItemView getChooseQuestionItemView(LayoutParams params, String text) {
        ChooseQuestionItemView chooseQuestionItemView = new ChooseQuestionItemView(getContext());
        chooseQuestionItemView.setChooseText(text);
        chooseQuestionItemView.setChooseTextHeight(mItemHeight);
        chooseQuestionItemView.setChoooseTextSize(mItemTextSize);
        chooseQuestionItemView.setChooseTextWidth(mItemWidth);
        setChooseText(chooseQuestionItemView, ChooseQuestionView.State.NORMAL);
        chooseQuestionItemView.setLayoutParams(params);
        return chooseQuestionItemView;
    }

    private void setChooseText(View view, ChooseQuestionView.State state) {
        if (view == null)
            return;
        ChooseQuestionItemView chooseQuestionItemView = (ChooseQuestionItemView) view;
        if (state == ChooseQuestionView.State.NORMAL) {
            chooseQuestionItemView.setChooseTextBackGround(mItemTextNormalBackground);
            chooseQuestionItemView.setChooseTextColor(mItemTextNormalColor);
        } else if (state == ChooseQuestionView.State.ERROR) {
            chooseQuestionItemView.setChooseTextBackGround(mItemTextErrorBackground);
            chooseQuestionItemView.setChooseTextColor(mItemTextErrorColor);
        } else if (state == ChooseQuestionView.State.SELECT) {
            chooseQuestionItemView.setChooseTextBackGround(mItemTextSelectedBackground);
            chooseQuestionItemView.setChooseTextColor(mItemTextSelectedColor);
        }

    }

    /**
     * 设置答案(1A2B3D)
     *
     * @param answer
     */
    public void setAnswer(final String answer) {
        if (mDrawLv == null) {
            return;
        }
        addAnswer(answer);
        post(new Runnable() {
            @Override
            public void run() {
                mDrawLv.post(new Runnable() {
                    @Override
                    public void run() {
                        //   initPoint();
                        mDrawLv.setAnswer(answer);
                    }
                });
            }
        });

    }

    /**
     * 展示答案
     */
    public void showAnswer(String answer, String rightAnswer) {

        addAnswer(answer);
        addRightAnswer(rightAnswer);
        stopDrawLine();
        post(new Runnable() {
            @Override
            public void run() {
                mDrawLv.post(new Runnable() {
                    @Override
                    public void run() {
                        mDrawLv.showAnswer(mAnswer.toString(), mRightAnswer.toString());
                    }
                });
            }
        });
    }

    /**
     * 展示答案
     */
    public void showAnswer(final String rightAnswer) {
        addRightAnswer(rightAnswer);
        stopDrawLine();

        post(new Runnable() {
            @Override
            public void run() {
                mDrawLv.post(new Runnable() {
                    @Override
                    public void run() {
                        //   initPoint();
                        mDrawLv.showAnswer(mAnswer.toString(), mRightAnswer.toString());
                    }
                });
            }
        });

    }

    private void addAnswer(String answer) {
        if (null != answer) {
            this.mAnswer.delete(0, mAnswer.length());
            this.mAnswer.append(answer);
        }
    }

    private void addRightAnswer(String rightAnswer) {
        if (null != rightAnswer) {
            this.mRightAnswer.delete(0, mRightAnswer.length());
            this.mRightAnswer.append(rightAnswer);
        }
    }

    /**
     * 停止画线
     */
    public void stopDrawLine() {
        mDrawLv.setmIsEnableOnTouchEvent(false);
    }

    /**
     * 清除按钮状态
     */
    public void clearButtonState() {
        for (int i = 0; i < mItemCount * 2; i++) {
            onClearSelectedButton(i);
        }
    }

    public void setItemCount(int itemCount) {
        if (this.mItemCount == itemCount) {
            setAnswer("");
            return;
        }
        this.mItemCount = itemCount;
        initData();
        initEvent();
    }

    @Override
    public void onGetAnswer(String answer) {
        Log.e(TAG, "getAnswer设置答案监听" + answer);
        mAnswer.delete(0, mAnswer.length());
        mAnswer.append(answer);
        if (mAnswerChangeListener == null) {
            Log.e(TAG, "没有设置答案监听");
            return;
        }
        mAnswerChangeListener.getAnswer(answer);
    }

    @Override
    public void onSelectedButton(int position) {
        //  Loger.e("DrawLineView", "onSelectedButton" + position);
        if (position < 0)
            return;
        if (getChildCount() - 1 > position) {
            ChooseQuestionItemView chooseQuestionItemView = (ChooseQuestionItemView) getChildAt(position);
            chooseQuestionItemView.setChooseTextBackGround(mItemTextSelectedBackground);
            chooseQuestionItemView.setChooseTextColor(mItemTextSelectedColor);
        } else {
        }

    }

    @Override
    public void onClearSelectedButton(int position) {
        //  Loger.e("DrawLineView", "onClearSelectedButton" + position + "position");
        if (position < 0)
            return;
        if (getChildCount() - 1 > position) {
            ChooseQuestionItemView chooseQuestionItemView = (ChooseQuestionItemView) getChildAt(position);
            chooseQuestionItemView.setChooseTextBackGround(mItemTextNormalBackground);
            chooseQuestionItemView.setChooseTextColor(mItemTextNormalColor);
        } else {
            Log.e("DrawLineView", "can't find position：" + position);
        }
    }

    @Override
    public void onErrorButton(int position) {
        //  Loger.e("DrawLineView", "onErrorButton" + position);
        if (position < 0)
            return;
        if (getChildCount() - 1 > position) {
            ChooseQuestionItemView chooseQuestionItemView = (ChooseQuestionItemView) getChildAt(position);
            chooseQuestionItemView.setChooseTextBackGround(mItemTextErrorBackground);
            chooseQuestionItemView.setChooseTextColor(mItemTextErrorColor);
        } else {
            Log.e("DrawLineView", "can't find position：" + position);
        }

    }

}
