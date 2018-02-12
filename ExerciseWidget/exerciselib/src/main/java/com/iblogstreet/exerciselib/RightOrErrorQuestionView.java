package com.iblogstreet.exerciselib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 描述：对错题按钮封装
 *
 * @author 王军
 * @time 2018/01/12 11:20
 */

public class RightOrErrorQuestionView extends ChooseQuestionView implements View.OnClickListener {
    private static final String[] CHOOSE_ITEM_DATA = {"A", "B"};
    private static final String[] CHOOSE_SHOW_ITEM_DATA = {"对", "错"};
    private static final int DEFAULT_COUNT = 2;

    public RightOrErrorQuestionView(Context context) {
        this(context, null);
    }

    public RightOrErrorQuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void initData() {
        mChooseList.addAll(Arrays.asList(CHOOSE_ITEM_DATA));
        mChooseShowList.addAll(Arrays.asList(CHOOSE_SHOW_ITEM_DATA));
        this.mChooseCount = DEFAULT_COUNT;
        addViewToRoot();
        mAnswerList = new ArrayList<>();
        mRightAnswerList = new ArrayList<>();

        mIsEnable = true;
        setChooseType(SINGLE_CHOOSE);

    }

    @Override
    public void setChooseCount(int chooseCount) {
        this.mChooseCount = DEFAULT_COUNT;
    }

    @Override
    public void setmLineItemCount(int count) {
    }
}
