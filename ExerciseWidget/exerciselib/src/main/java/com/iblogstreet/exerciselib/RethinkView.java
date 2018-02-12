package com.iblogstreet.exerciselib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.iblogstreet.exerciselib.bean.RethinkBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：反思组件
 * 创建人：王军
 * 创建时间：2018/1/17
 */

public class RethinkView extends LinearLayout implements View.OnClickListener {

    public final static int[] KEY = {1, 2, 3, 4, 5, 65535};
    public final static String[] VALUE = {"知识记忆性错误", "理解性错误", "考虑不全面", "审题不仔细", "粗心大意", "其他"};
    private static final int DEFAULT_WIDTH = 30;
    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_ITEM_COUNT = 4;
    private static final int CHOOSE_COUNT = 6;
    private List<RethinkBean> mRethinkBeans;

    private int mTextSize = DEFAULT_SIZE;
    private Drawable mNormalBackGround;
    private Drawable mSelectedBackGround;
    private int mTextWidth = DEFAULT_WIDTH;
    private int mTextHeight = DEFAULT_WIDTH;
    protected int mChooseCount = CHOOSE_COUNT;
    private int mTextLeftMargin = 0;
    private int mTextRightMargin = 0;
    //行之间的Margin
    private int mLineBetweenMargin = 0;
    private int mLineItemCount = DEFAULT_ITEM_COUNT;

    /**
     * 正常的颜色
     */
    private int mTextNormalColor = DEFAULT_COLOR;
    /**
     * 选中的颜色
     */
    private int mTextSelectColor = DEFAULT_COLOR;
    private List<RethinkBean> mAnswerList;

    public void setmOnRethinkViewListener(OnRethinkViewListener mOnRethinkViewListener) {
        this.mOnRethinkViewListener = mOnRethinkViewListener;
    }

    private OnRethinkViewListener mOnRethinkViewListener;


    public RethinkView(Context context) {
        this(context, null);
    }

    public RethinkView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RethinkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initAttr(attrs);
        initData();
        initEvent();
    }

    private void initEvent() {
        for (int i = 0; i < mChooseCount; i++) {
            getChildViewAt(i).setClickable(true);
            getChildViewAt(i).setOnClickListener(this);
        }
    }

    public View getChildViewAt(int index) {
        int lineNumber = (mChooseCount - 1) / mLineItemCount;
        if (lineNumber <= 0) {
            return getChildAt(index);
        } else {
            int temLineNumber = index / mLineItemCount;
            if (getChildCount() >= temLineNumber) {
                int subItemCount = ((ViewGroup) getChildAt(temLineNumber)).getChildCount();
                int subIndex = index % mLineItemCount;
                if (subIndex < subItemCount) {
                    return ((ViewGroup) getChildAt(temLineNumber)).getChildAt(subIndex);
                }

            } else {
                return null;
            }
        }
        return null;

    }

    /**
     * 设置答案
     *
     * @param id
     */
    public void setmAnswer(int id) {
        for (int i = 0; i < mRethinkBeans.size(); i++) {
            if (mRethinkBeans.get(i).getId() == id) {
                //设置选中状态，并且设置
                if (mAnswerList.size() > 0)
                    setChooseText(getChildViewAt(mRethinkBeans.indexOf(mAnswerList.get(0))), State.NORMAL);
                this.mAnswerList.clear();
                this.mAnswerList.add(mRethinkBeans.get(i));
                setChooseText(getChildViewAt(i), State.SELECT);
                break;
            } else {
                if (mAnswerList.size() > 0)
                    setChooseText(getChildViewAt(mRethinkBeans.indexOf(mAnswerList.get(0))), State.NORMAL);
            }
        }
    }

    private void callRethinkViewListener(RethinkBean bean) {
        if (null != mOnRethinkViewListener) {
            mOnRethinkViewListener.onRethinkItemClick(bean.getId(), bean.getName());
        }
    }

    private void initData() {
        initRethinkBeans();
        mChooseCount = mRethinkBeans.size();
        mAnswerList = new ArrayList<>();
        addViewToRoot();
    }

    private void addViewToRoot() {
        this.removeAllViews();
        mChooseCount = mChooseCount > mRethinkBeans.size() ? mRethinkBeans.size() : mChooseCount;
        if (mChooseCount <= mLineItemCount) {
            setOrientation(HORIZONTAL);
            LayoutParams params = getSelfLayoutParams();
            for (int i = 0; i < mChooseCount; i++) {
                addView(getRethinkItemView(params, i));
            }
        } else {
            setOrientation(VERTICAL);
            int lineNumber = (mChooseCount - 1) / mLineItemCount;
            for (int i = 0; i <= lineNumber; i++) {
                LayoutParams params = getSelfLayoutParams();
                if (i != 0)
                    params.topMargin = mLineBetweenMargin;
                LinearLayout linearLayout = getLineLayout(params);
                for (int j = 0; j < mLineItemCount && ((i * mLineItemCount + j) < mChooseCount); j++) {
                    LayoutParams param = getSelfLayoutParams();
                    //一行第一个左边距不用设置
                    param.leftMargin = j == 0 ? 0 : mTextLeftMargin;
                    //一行最后一个右边距不用设置
                    param.rightMargin = (j + 1) == mLineItemCount ? 0 : mTextRightMargin;
                    linearLayout.addView(getRethinkItemView(params, j + i * mLineItemCount), param);
                }
                addView(linearLayout);
            }
        }
    }

    @NonNull
    private RethinkItemView getRethinkItemView(LayoutParams params, int i) {
        RethinkItemView rethinkItemView = new RethinkItemView(getContext());
        rethinkItemView.setChooseText(mRethinkBeans.get(i).getName());
        rethinkItemView.setChooseTextHeight(mTextHeight);
        rethinkItemView.setChoooseTextSize(mTextSize);
        rethinkItemView.setChooseTextWidth(mTextWidth);
        setChooseText(rethinkItemView, State.NORMAL);
        rethinkItemView.setLayoutParams(params);
        return rethinkItemView;
    }

    /**
     * 设置选项的状态
     *
     * @param view
     * @param state
     */
    private void setChooseText(View view, State state) {
        if (view == null)
            return;
        RethinkItemView rethinkItemView = (RethinkItemView) view;
        if (state == State.NORMAL) {
            rethinkItemView.setChooseTextBackGround(mNormalBackGround);
            rethinkItemView.setChooseTextColor(mTextNormalColor);
        }
        if (state == State.SELECT) {
            rethinkItemView.setChooseTextBackGround(mSelectedBackGround);
            rethinkItemView.setChooseTextColor(mTextSelectColor);
        }

    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < mChooseCount; i++) {
            if (v == getChildViewAt(i)) {
                if (mAnswerList.contains(mRethinkBeans.get(i))) {
                    callRethinkViewListener(mAnswerList.get(0));
                    return;
                } else {
                    if (mAnswerList.size() > 0) {
                        setChooseText(getChildViewAt(mRethinkBeans.indexOf(mAnswerList.get(0))), State.NORMAL);
                        mAnswerList.clear();
                    }
                    setChooseText(v, State.SELECT);
                    mAnswerList.add(mRethinkBeans.get(i));
                    callRethinkViewListener(mAnswerList.get(0));
                }
                break;
            }
        }
    }

    enum State {
        NORMAL,
        SELECT
    }

    private LinearLayout getLineLayout(LayoutParams params) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }

    @NonNull
    private LayoutParams getSelfLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );
        return params;
    }

    /**
     * 实始化默认数据
     */
    private void initRethinkBeans() {
        mRethinkBeans = new ArrayList<>();
        for (int i = 0; i < KEY.length; i++) {
            mRethinkBeans.add(new RethinkBean(KEY[i], VALUE[i]));
        }

    }

    private void initAttr(AttributeSet attrs) {
        //获得我们所定义的自定义样式属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RethinkView);
        if (typedArray != null) {
            int n = typedArray.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.RethinkView_rvTextSize) {
                    mTextSize = typedArray.getDimensionPixelSize(attr, DEFAULT_SIZE);
                } else if (attr == R.styleable.RethinkView_rvTextNormalBackGround) {
                    mNormalBackGround = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.RethinkView_rvTextSelectedBackGround) {
                    mSelectedBackGround = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.RethinkView_rvTextHeight) {
                    mTextHeight = typedArray.getDimensionPixelSize(attr, DEFAULT_WIDTH);
                } else if (attr == R.styleable.RethinkView_rvTextWidth) {
                    mTextWidth = typedArray.getDimensionPixelSize(attr, DEFAULT_WIDTH);
                } else if (attr == R.styleable.RethinkView_rvTextRightMargin) {
                    mTextRightMargin = typedArray.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.RethinkView_rvTextLeftMargin) {
                    mTextLeftMargin = typedArray.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.RethinkView_rvTextNormalColor) {
                    mTextNormalColor = typedArray.getColor(attr, DEFAULT_COLOR);
                } else if (attr == R.styleable.RethinkView_rvTextSelectedColor) {
                    mTextSelectColor = typedArray.getColor(attr, DEFAULT_COLOR);
                } else if (attr == R.styleable.RethinkView_rvLineBetweenMargin) {
                    mLineBetweenMargin = typedArray.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.RethinkView_rvLineItemCount) {
                    mLineItemCount = typedArray.getInteger(attr, DEFAULT_ITEM_COUNT);
                }
            }
            typedArray.recycle();
        }
    }

    private void initView() {

    }

    /**
     * 将Key转成Value
     *
     * @param key
     * @return
     */
    public static String convertKeyToString(int key) {
        for (int i = 0; i < KEY.length; i++) {
            if (key == KEY[i]) {
                return VALUE[i];
            }
        }
        return "";
    }

    /**
     * 获取答案
     *
     * @return
     */
    public String getAnswer() {
        if (null != mAnswerList && mAnswerList.size() > 0) {
            return mAnswerList.get(0).getName();
        }
        return "";
    }

    /**
     * 反思接口
     */
    public interface OnRethinkViewListener {
        void onRethinkItemClick(int result, String chooseString);//选择结果
    }
}
