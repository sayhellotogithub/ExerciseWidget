package com.iblogstreet.exercisewidget;

import android.view.View;

import com.iblogstreet.exerciselib.ChooseQuestionView;
import com.iblogstreet.exerciselib.DrawLineViewWrapper;
import com.iblogstreet.exerciselib.ShowThumbnailView;
import com.iblogstreet.exerciselib.bean.RethinkBean;
import com.iblogstreet.exerciselib.utils.Loger;
import com.iblogstreet.exercisewidget.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：ViewPictureActivity
 * 类描述： 展示图片界面
 * 创建人：王军
 * 创建时间 ：2016/12/24 19:36
 * 修改人：
 * 修改时间：2016/12/24 19:36
 * 修改备注：
 */
public class ChooseActivity extends BaseActivity implements ChooseQuestionView.OnChooseQuestionListener {
    ChooseQuestionView mChooseQuestionSingleView;
    ChooseQuestionView mChooseQuestionMultiView;
    ChooseQuestionView mRightView;
    ShowThumbnailView mShowThumbnailView;
    DrawLineViewWrapper mDrawLineViewWrapper;
    DrawLineViewWrapper mDrawVerticalLine;

    public void initView() {
        mChooseQuestionSingleView = (ChooseQuestionView) findViewById(R.id.cqvSingle);
        mChooseQuestionMultiView = (ChooseQuestionView) findViewById(R.id.cqvMulti);
        mRightView = (ChooseQuestionView) findViewById(R.id.rightView);
        mShowThumbnailView = (ShowThumbnailView) findViewById(R.id.stv);
        mDrawLineViewWrapper = (DrawLineViewWrapper) findViewById(R.id.drawLine);

        mDrawVerticalLine = (DrawLineViewWrapper) findViewById(R.id.drawVerticalLine);

    }

    public void initData() {
        try {
            mChooseQuestionSingleView.setChooseCount(4);
            mChooseQuestionSingleView.setmAnswerList(new ArrayList<String>() {
                {
                    add("AB");
                }
            });
            mChooseQuestionMultiView.setmAnswerList(new ArrayList<String>() {
                {
                    add("AB");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        mShowThumbnailView.setUrlList(new ArrayList<String>() {
            {
                add("a");
            }

            {
                add("b");
            }

            {
                add("c");
            }

            {
                add("d");
            }
        });

        generalRethinkList();
    }

    private void generalRethinkList() {
        List<RethinkBean> mRethinkBean = new ArrayList<>();
        mRethinkBean.add(new RethinkBean(1, "知识记忆性错误"));
        mRethinkBean.add(new RethinkBean(2, "理解性错误"));
        mRethinkBean.add(new RethinkBean(3, "考虑不全面"));
        mRethinkBean.add(new RethinkBean(4, "审题不仔细"));
        mRethinkBean.add(new RethinkBean(5, "粗心大意"));
        mRethinkBean.add(new RethinkBean(65535, "其他"));
    }

    int count = 0;

    public void initEvent() {
        mChooseQuestionMultiView.setOnChooseQuestionListener(this);
        mChooseQuestionSingleView.setOnChooseQuestionListener(this);
        mRightView.setOnChooseQuestionListener(this);
        mShowThumbnailView.setUrlPrefix("10.0.0.9");
        mShowThumbnailView.setOnShowThumbnailListener(new ShowThumbnailView.OnShowThumbnailListener() {
            @Override
            public void onAnswerList(List<String> answerList, ShowThumbnailView showThumbnailView) {
                Loger.e("ShowThumbnailView", answerList.toString());
            }

            @Override
            public void onTakePhoto() {
                mShowThumbnailView.addUrl("c" + count);
                count++;
            }
        });

    }

    @Override
    public int getLayoutById() {
        return R.layout.activity_exercise_choose;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    RethinkPopWin rethinkPopWin;

    public void showRethink(View view) {
        if (rethinkPopWin == null)
            rethinkPopWin = new RethinkPopWin(this, null);
        rethinkPopWin.showPopWin(view);
    }

    public void showLine(View view) {
        mChooseQuestionMultiView.setmLineItemCount(4);
        mChooseQuestionSingleView.setmLineItemCount(3);
    }

    public void showAnswer(View view) {
        mChooseQuestionSingleView.showAnswer(new ArrayList<String>() {
            {
                add("C");
            }
        });

        mChooseQuestionMultiView.showAnswer(new ArrayList<String>() {
            {
                add("AC");
            }
        });
        mShowThumbnailView.showAnswerResult();
        mDrawLineViewWrapper.showAnswer("1B2A3C4E5D6F");
        mDrawVerticalLine.showAnswer("1C2A3D4B");
    }

    public void setAnswer(View view) {
        mDrawLineViewWrapper.setAnswer("1A2B");
        mDrawVerticalLine.setAnswer("2A");
        mChooseQuestionSingleView.setmAnswerList(new ArrayList<String>() {

        });
    }

    int countItem = 6;

    public void setItemCount(View view) {
        mDrawVerticalLine.setItemCount(countItem);
        countItem--;

    }

    @Override
    public void onAnswerList(List<String> answerList, ChooseQuestionView chooseQuestionView) {
        Loger.e("mChooseQuestionSingleView", answerList.toString());
    }
}
