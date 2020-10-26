package com.iblogstreet.exerciselib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 描述：单选题，多选题按钮封装
 *
 * @author 王军
 * @time 2018/01/12 11:20
 */

public class ChooseQuestionView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "ChooseQuestionView";

    public final static int SINGLE_CHOOSE = 0;
    public final static int MULTI_CHOOSE = 1;

    private int mChooseType = 0;//0代表单选题,1 代表多选题
    private static final int CHOOSE_COUNT = 4;//选项数

    private static final String[] CHOOSE_ITEM_DATA = {"A", "B", "C", "D", "E", "F", "G", "H"};
    private static final int DEFAULT_WIDTH = 30;
    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final int DEFAULT_ITEM_COUNT = 4;
    /**
     * 用于返回的数据
     */
    protected List<String> mChooseList = new ArrayList<>();
    /**
     * 用于显示的数据
     */
    public List<String> mChooseShowList = new ArrayList<>();
    private int mTextSize = DEFAULT_SIZE;
    private Drawable mNormalBackGround;
    private Drawable mErrorBackGround;
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
     * 错误的颜色
     */
    private int mTextErrorColor = DEFAULT_COLOR;
    /**
     * 正常的颜色
     */
    private int mTextNormalColor = DEFAULT_COLOR;
    /**
     * 选中的颜色
     */
    private int mTextSelectColor = DEFAULT_COLOR;
    /**
     * 答案列表
     */
    protected List<String> mAnswerList;
    /**
     * 正确答案列表
     */
    protected List<String> mRightAnswerList;
    /**
     * 反馈接口
     */
    private OnChooseQuestionListener mOnChooseQuestionListener;
    /**
     * 标识是否可以点击
     */
    protected boolean mIsEnable;

    public ChooseQuestionView(Context context) {
        this(context, null);
    }

    public ChooseQuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttr(attrs);
        initData();
        initEvent();
    }

    protected void initEvent() {
        for (int i = 0; i < mChooseCount; i++) {
            getChildViewAt(i).setClickable(true);
            getChildViewAt(i).setOnClickListener(this);
        }
    }

    private void initAttr(AttributeSet attrs) {
        //获得我们所定义的自定义样式属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ChooseQuestionView);
        if (typedArray != null) {
            int n = typedArray.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.ChooseQuestionView_cqvTextErrorBackGround) {
                    mErrorBackGround = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.ChooseQuestionView_cqvTextSize) {
                    mTextSize = typedArray.getDimensionPixelSize(attr, DEFAULT_SIZE);
                } else if (attr == R.styleable.ChooseQuestionView_cqvTextNormalBackGround) {
                    mNormalBackGround = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.ChooseQuestionView_cqvTextSelectedBackGround) {
                    mSelectedBackGround = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.ChooseQuestionView_cqvCount) {
                    //题目选项数
                    mChooseCount = typedArray.getInteger(attr, CHOOSE_COUNT);
                } else if (attr == R.styleable.ChooseQuestionView_cqvChooseType) {
                    mChooseType = typedArray.getInteger(attr, SINGLE_CHOOSE);
                } else if (attr == R.styleable.ChooseQuestionView_cqvTextHeight) {
                    mTextHeight = typedArray.getDimensionPixelSize(attr, DEFAULT_WIDTH);
                } else if (attr == R.styleable.ChooseQuestionView_cqvTextWidth) {
                    mTextWidth = typedArray.getDimensionPixelSize(attr, DEFAULT_WIDTH);
                } else if (attr == R.styleable.ChooseQuestionView_cqvTextRightMargin) {
                    mTextRightMargin = typedArray.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.ChooseQuestionView_cqvTextLeftMargin) {
                    mTextLeftMargin = typedArray.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.ChooseQuestionView_cqvTextErrorColor) {
                    mTextErrorColor = typedArray.getColor(attr, DEFAULT_COLOR);
                } else if (attr == R.styleable.ChooseQuestionView_cqvTextNormalColor) {
                    mTextNormalColor = typedArray.getColor(attr, DEFAULT_COLOR);
                } else if (attr == R.styleable.ChooseQuestionView_cqvTextSelectedColor) {
                    mTextSelectColor = typedArray.getColor(attr, DEFAULT_COLOR);
                } else if (attr == R.styleable.ChooseQuestionView_cqvLineBetweenMargin) {
                    mLineBetweenMargin = typedArray.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.ChooseQuestionView_cqvLineItemCount) {
                    mLineItemCount = typedArray.getInteger(attr, DEFAULT_ITEM_COUNT);
                }
            }
            typedArray.recycle();

        }
    }

    protected void initData() {
        mChooseList.addAll(Arrays.asList(CHOOSE_ITEM_DATA));
        addViewToRoot();
        mAnswerList = new ArrayList<>();
        mRightAnswerList = new ArrayList<>();
        mIsEnable = true;
    }

    private void initView() {
        setOrientation(HORIZONTAL);
    }

    public void setOnChooseQuestionListener(OnChooseQuestionListener listener) {
        this.mOnChooseQuestionListener = listener;
    }

    public void setmLineItemCount(int count) {
        this.mLineItemCount = count;
        addViewToRoot();
        initEvent();
    }

    public void setmIsEnable(boolean enable) {
        this.mIsEnable = enable;
    }

    /**
     * 设置选项数，选项数的大小必须小于等于选项列表size
     *
     * @param count
     */
    public void setChooseCount(int count) throws Exception {
        if (count > mChooseList.size()) {
            throw new Exception("ChooseCount above ChooseList size");
        }
        if (this.mChooseCount == count) {
            return;
        }
        this.mChooseCount = count;
        addViewToRoot();
        initEvent();
    }

    /**
     * 设置选项列表
     *
     * @param chooseList
     */
    public void setmChooseList(List<String> chooseList) {
        if (chooseList == null && chooseList.size() <= 0)
            return;
        this.mChooseList = chooseList;
        this.mChooseCount = chooseList.size();
    }

    /**
     * 添加View到根布局
     */
    protected void addViewToRoot() {
        this.removeAllViews();

        mChooseCount = mChooseCount > mChooseList.size() ? mChooseList.size() : mChooseCount;
        if (mChooseCount <= mLineItemCount) {
            setOrientation(HORIZONTAL);
            for (int i = 0; i < mChooseCount; i++) {
                LayoutParams params = getSelfLayoutParams();
                params.leftMargin = i == 0 ? 0 : mTextLeftMargin;
                //一行最后一个右边距不用设置
                params.rightMargin = (i + 1) == mChooseCount ? 0 : mTextRightMargin;
                addView(getChooseQuestionItemView(params, i));
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
                    param.leftMargin = j == 0 ? 0 : mTextLeftMargin;
                    //一行最后一个右边距不用设置
                    param.rightMargin = (j + 1) == mLineItemCount ? 0 : mTextRightMargin;
                    param.topMargin = 0;
                    linearLayout.addView(getChooseQuestionItemView(params, j + i * mLineItemCount), param);
                }
                addView(linearLayout);
            }
        }

    }

    @NonNull
    private LayoutParams getSelfLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        return params;
    }

    @NonNull
    private ChooseQuestionItemView getChooseQuestionItemView(LayoutParams params, int i) {
        ChooseQuestionItemView chooseQuestionItemView = new ChooseQuestionItemView(getContext());
        chooseQuestionItemView.setChooseText(convertShowStringToValue(mChooseList.get(i), i));
        chooseQuestionItemView.setChooseTextHeight(mTextHeight);
        chooseQuestionItemView.setChoooseTextSize(mTextSize);
        chooseQuestionItemView.setChooseTextWidth(mTextWidth);
        setChooseText(chooseQuestionItemView, State.NORMAL);
        chooseQuestionItemView.setLayoutParams(params);
        return chooseQuestionItemView;
    }

    private LinearLayout getLineLayout(LayoutParams params) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }


    /**
     * 设置正确答案
     *
     * @param rightAnswerList
     */
    public void setmRightAnswerList(List<String> rightAnswerList) {
        if (rightAnswerList != null) {
            this.mRightAnswerList = handlerAnswerList(rightAnswerList);
        }
    }

    /**
     * 设置答题结果
     *
     * @param answerList
     */
    public void setmAnswerList(List<String> answerList) {
        clearSelected();
        if (answerList != null) {
            this.mAnswerList = handlerAnswerList(answerList);
            showAnswerResult(mAnswerList, mAnswerList);
        }
    }

    /**
     * 用于处理学生的答案，防止错误的答案
     *
     * @param answerList
     * @return
     */
    public static List<String> handlerAnswerList(List<String> answerList) {
        //处理答案数据，如果是不符合的话，就处理
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < answerList.size(); i++) {
            if (answerList.get(i).length() > 0) {
                resultList.addAll(convertStringToList(answerList.get(i)));
            }
        }
        return resultList;
    }

    /**
     * 将String 转成List
     *
     * @param answer
     * @return
     */
    public static List<String> convertStringToList(String answer) {
        List<String> temp = new ArrayList<>();
        if (null == answer || answer.length() == 0) {
            return temp;
        }
        for (int i = 0; i < answer.length(); i++) {
            temp.add(answer.charAt(i) + "");
        }
        return temp;
    }

    /**
     * 将List转成String
     *
     * @param answerList
     * @return
     */
    public static List<String> convertListToString(List<String> answerList) {
        StringBuilder stringBuilder = new StringBuilder();
        if (null != answerList && answerList.size() > 0) {
            for (int i = 0; i < answerList.size(); i++) {
                stringBuilder.append(answerList.get(i));
            }
        }

        return Collections.singletonList(stringBuilder.toString());
    }


    protected String convertShowStringToValue(String showString, int i) {
        if (mChooseShowList == null || mChooseShowList.size() == 0) {
            return showString;
        } else {
            return mChooseShowList.get(i);
        }
    }

    /**
     * 显示结果
     */
    public void showAnswer() {
        showAnswerResult();
    }

    /**
     * 显示答案
     *
     * @param rightAnswerList
     */
    public void showAnswer(List<String> rightAnswerList) {
        setmRightAnswerList(rightAnswerList);
        showAnswerResult();
    }

    /**
     * 这里用于内部显示答案
     */
    private void showAnswerResult() {
        mIsEnable = false;
        //这里作健壮性处理
        showAnswerResult(mRightAnswerList, mAnswerList);
    }

    /**
     * 这里用于内部显示答案
     */
    private void showAnswerResult(List<String> rightAnswerList, List<String> answerList) {
        //这里作健壮性处理
        if (answerList != null && answerList.size() > 0) {
            for (int i = 0; i < answerList.size(); i++) {
                if (rightAnswerList != null) {
                    if (rightAnswerList.contains(answerList.get(i))) {
                        setChooseText(getChildViewAt(mChooseList.indexOf(answerList.get(i))), State.SELECT);
                    } else {
                        setChooseText(getChildViewAt(mChooseList.indexOf(answerList.get(i))), State.ERROR);
                    }
                }
            }
        }
    }

    public void clearSelected() {
        if (this.mAnswerList != null) {
            for (int i = 0; i < this.mAnswerList.size(); i++) {
                setChooseText(getChildViewAt(mChooseList.indexOf(this.mAnswerList.get(i))), State.NORMAL);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!mIsEnable) {
            return;
        }
        for (int i = 0; i < mChooseCount; i++) {
            if (v == getChildViewAt(i)) {
                setChooseState(v, i);
                break;
            }
        }
    }

    /**
     * 根据选择题类型设置选中状态
     * 如果是单选题，只能选中一个选项，并且，同一个选项点击，不能撤消
     * 如果是多选题，可以撤消
     *
     * @param view
     * @param i
     */
    private void setChooseState(View view, int i) {
        //如果是单选题，只能选中一个选项，并且，同一个选项点击，不能撤消
        if (mChooseType == SINGLE_CHOOSE) {
            if (mAnswerList.contains(mChooseList.get(i))) {

            } else {
                if (mAnswerList.size() > 0) {
                    setChooseText(getChildViewAt(mChooseList.indexOf(mAnswerList.get(0))), State.NORMAL);
                    mAnswerList.clear();
                }
                setChooseText(view, State.SELECT);
                //这里要保证数据添加的顺序性，即先添加B后添加A的话，顺序应该是A,B
                mAnswerList.add(mChooseList.get(i));
                callChooseQuestionListener();
            }
        } else if (mChooseType == MULTI_CHOOSE) {
            if (mAnswerList.contains(mChooseList.get(i))) {
                setChooseText(view, State.NORMAL);
                mAnswerList.remove(mChooseList.get(i));
            } else {
                setChooseText(view, State.SELECT);
                //这里要保证数据添加的顺序性，即先添加B后添加A的话，顺序应该是A,B
                addAnswerToList(mChooseList.get(i));
            }
            callChooseQuestionListener();
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

    private void callChooseQuestionListener() {
        if (mOnChooseQuestionListener != null) {
            mOnChooseQuestionListener.onAnswerList(convertListToString(mAnswerList), this);
        }
    }

    /**
     * 这里要保证数据添加的顺序性，即先添加B后添加A的话，顺序应该是A,B
     *
     * @param answer
     */
    private void addAnswerToList(String answer) {
        if (mAnswerList.contains(answer)) {
            return;
        }
        mAnswerList.add(answer);
        Collections.sort(mAnswerList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
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
        ChooseQuestionItemView chooseQuestionItemView = (ChooseQuestionItemView) view;
        if (state == State.NORMAL) {
            chooseQuestionItemView.setChooseTextBackGround(mNormalBackGround);
            chooseQuestionItemView.setChooseTextColor(mTextNormalColor);
        } else if (state == State.ERROR) {
            chooseQuestionItemView.setChooseTextBackGround(mErrorBackGround);
            chooseQuestionItemView.setChooseTextColor(mTextErrorColor);
        } else if (state == State.SELECT) {
            chooseQuestionItemView.setChooseTextBackGround(mSelectedBackGround);
            chooseQuestionItemView.setChooseTextColor(mTextSelectColor);
        }

    }

    enum State {
        NORMAL,
        SELECT,
        ERROR
    }

    public void setChooseType(int chooseType) {
        this.mChooseType = chooseType;
    }

    /**
     * 选择题接口
     */
    public interface OnChooseQuestionListener {
        void onAnswerList(List<String> answerList, ChooseQuestionView chooseQuestionView);//选择结果
    }

    /**
     * 获取答案
     *
     * @return
     */
    public List<String> getmAnswerList() {
        return convertListToString(mAnswerList);
    }
}
