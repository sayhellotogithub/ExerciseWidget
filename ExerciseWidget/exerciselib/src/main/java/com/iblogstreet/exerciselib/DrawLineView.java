package com.iblogstreet.exerciselib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import com.iblogstreet.exerciselib.bean.Coordinate;
import com.iblogstreet.exerciselib.utils.Loger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对应题连线部分
 *
 * @author chenchao
 * @time 2016/12/20 11:11
 */
public class DrawLineView extends View {
    public final static int DEFAULT_ITEM_COUNT = 6;
    public final static int DEFAULT_CIRCLE = 34;
    public static final String TAG = "DrawLineView";
    /**
     * 被选中按钮的序号
     */
    private List<ChooseBean> mNumbers;
    /**
     * 线条的坐标点,初始化时生成
     */
    public List<Coordinate> mCoordinates = new ArrayList<>();
    /**
     * 正常的画笔
     */
    private Paint mNormalPaint = new Paint();
    private Paint mErrorPaint = new Paint();
    /**
     * 按下的Item
     */
    private int mFirstSelectedItem;

    /**
     * 方向
     */
    private boolean mIsHorizontal;

    public void setmItemCount(int mItemCount) {
        this.mItemCount = mItemCount;
    }

    /**
     * 选项数量
     */
    private int mItemCount = DEFAULT_ITEM_COUNT;
    /**
     * 圆半径
     */
    private int mCircle = DEFAULT_CIRCLE;

    public void setmIsHorizontal(boolean mIsHorizontal) {
        this.mIsHorizontal = mIsHorizontal;
    }

    public interface OnDrawLineViewListener {
        void onGetAnswer(String answer);//获得绘制的答案

        void onSelectedButton(int i);

        void onClearSelectedButton(int i);

        void onErrorButton(int i);//显示错误的状态
    }

    private OnDrawLineViewListener mOnDrawLineViewListener;

    public void setOnDrawLineViewListener(OnDrawLineViewListener l) {
        mOnDrawLineViewListener = l;
    }

    public DrawLineView(Context context) {
        this(context, null);
    }

    public DrawLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        canvas.drawPath(mPath, mNormalPaint);
    }

    /**
     * 起始点X
     */
    private float mStartX;
    /**
     * 起始点Y
     */
    private float mStartY;
    private final Path mPath = new Path();
    /**
     * 按下的item
     */
    private int mDownPoint = -1;
    /**
     * 是否允许触模
     */
    private boolean mIsEnableOnTouchEvent = true;

    public void setmIsEnableOnTouchEvent(boolean mIsEnableOnTouchEvent) {
        this.mIsEnableOnTouchEvent = mIsEnableOnTouchEvent;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsEnableOnTouchEvent) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return touchDown(event);
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                //更新绘制
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp(event);
                //更新绘制
                postInvalidate();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 手指点下屏幕时调用
     *
     * @param event
     * @return 返回true代表找到点 返回false代表未找到点，则不画线
     */
    private boolean touchDown(MotionEvent event) {
        Loger.e(TAG, "touchDown ");
        mStartX = event.getX();
        mStartY = event.getY();
        mDownPoint = findPointIndex(mStartX, mStartY);
        if (mDownPoint != -1) {
            refresh("" + mDownPoint, false);
            return true;
        }
        return false;
    }

    //手指在屏幕上滑动时调用
    private void touchMove(MotionEvent event) {
        Loger.e(TAG, "touchMove ");
        mPath.reset();
        mPath.moveTo(mStartX, mStartY);
        mPath.lineTo(event.getX(), event.getY());
    }

    //手指离开屏幕
    private void touchUp(MotionEvent event) {
        Loger.e(TAG, "touchUp ");
        int pointIndex = findPointIndex(event.getX(), event.getY());
        if (pointIndex != -1 && mDownPoint != -1) {
            refresh("" + pointIndex, true);
        } else {
            if (mDownPoint != -1) {
                refresh("" + mDownPoint, true);  //up时没有选中，立刻清除down存下的数据
            } else {
                Loger.e(TAG, "mDownPoint != -1");
            }
        }
        mPath.reset();
        mFirstSelectedItem = 0;
    }

    private void initData() {
        mNumbers = new ArrayList<>();
    }

    /**
     * 设置半径
     *
     * @param mCircle
     */
    public void setmCircle(int mCircle) {
        this.mCircle = mCircle;
    }

    /**
     * 初始化画笔
     *
     * @param normalColor
     * @param errorColor
     */
    public void initPaint(int normalColor, int errorColor) {
        mNormalPaint.setAntiAlias(true);
        mNormalPaint.setStyle(Paint.Style.STROKE);
        mNormalPaint.setStrokeWidth(4);
        mNormalPaint.setColor(normalColor);

        mErrorPaint.setAntiAlias(true);
        mErrorPaint.setStyle(Paint.Style.STROKE);
        mErrorPaint.setStrokeWidth(4);
        mErrorPaint.setColor(errorColor);
    }


    /**
     * 判断手指触碰到的圆
     *
     * @author chenchao
     * @time 2016/10/26 10:13
     */
    private int findPointIndex(float x, float y) {
        float cellX;
        float cellY;
        int result = -1;
        for (int i = 0; i < mCoordinates.size(); i++) {
            cellX = mCoordinates.get(i).getX();//圆的中心点
            cellY = mCoordinates.get(i).getY();//圆的中心点
            //由以前的在范围内，改成在按钮的
            float tempX = Math.abs(cellX - x);
            float tempY = Math.abs(cellY - y);

            float distance = (float) Math.sqrt(tempX * tempX + tempY * tempY);
            if (distance < mCircle) {
                result = i + 1;
                break;
            }
        }
        return result;
    }

    /**
     * 刷新界面
     * 王军
     *
     * @param sequence
     * @param isUp
     */
    private void refresh(String sequence, boolean isUp) {
        Loger.e(TAG, "refresh(String sequence,boolean isUp)" + sequence + "mFirstSelectedItem" + mFirstSelectedItem);
        int number = Integer.parseInt(sequence);
        //如果isUp是true的话，则进行判断是否需要连线
        //如果isUp是false的话，说明是down
        if (!isUp) {
            //如果是down的话,更新界面,并设置选中状态
            mFirstSelectedItem = number;
            callSelectedButton(number);
        } else {
            ChooseBean tempBean = null;
            ChooseBean numberBean = null;
            //查询一下是否存在
            tempBean = findValueInNumbers(mFirstSelectedItem);
            numberBean = findValueInNumbers(number);
            //过滤不符合条件的值,如果两个值，在同一个区间，则不进行添加,并且去掉选中状态
            if ((mFirstSelectedItem > mItemCount && number > mItemCount) || (mFirstSelectedItem <= mItemCount && number <= mItemCount)) {
                if (mOnDrawLineViewListener != null && null == tempBean) {
                    callClearSelectedButton(mFirstSelectedItem);
                }
                if (mOnDrawLineViewListener != null && null == numberBean) {
                    callClearSelectedButton(number);
                }
                return;
            }
            //判断值是否存在
            if (mNumbers.size() > 0) {
                //如果不存在，则进行添加
                if (null == tempBean && null == numberBean) {
                    mNumbers.add(new ChooseBean(mFirstSelectedItem, number));
                    callSelectedButton(number);
                    callSelectedButton(mFirstSelectedItem);
                    callGetAnswer();
                } else {
                    if (mOnDrawLineViewListener != null) {
                        //如果两个都存在的话，则两个同时取消选中状态
                        if (null != tempBean && numberBean != null && tempBean == numberBean) {
                            callClearSelectedButton(number);
                            callClearSelectedButton(mFirstSelectedItem);
                            mNumbers.remove(tempBean);
                            callGetAnswer();
                        } else {
                            //如果至少存在一个话，则清空之前的选中效果,并添加新的选中
                            removeChooseItem(mFirstSelectedItem);
                            removeChooseItem(number);
                            callSelectedButton(mFirstSelectedItem);
                            callSelectedButton(number);
                            mNumbers.add(new ChooseBean(mFirstSelectedItem, number));
                            callGetAnswer();
                        }
                    }
                }

            } else {
                //如mNumbers值为空的话，直接添加
                mNumbers.add(new ChooseBean(mFirstSelectedItem, number));
                callSelectedButton(number);
                callSelectedButton(mFirstSelectedItem);
                callGetAnswer();
            }
            //最终抬起，设置为0
            mFirstSelectedItem = 0;
        }

    }

    private void removeChooseItem(int number) {
        for (int i = 0; i < mNumbers.size(); i++) {
            if (mNumbers.get(i).getKey1() == number || mNumbers.get(i).getKey2() == number) {
                callClearSelectedButton(mNumbers.get(i).getKey2());
                callClearSelectedButton(mNumbers.get(i).getKey1());
                mNumbers.remove(mNumbers.get(i));
                break;
            }
        }
    }

    private void callClearSelectedButton(int position) {
        if (null != mOnDrawLineViewListener)
            mOnDrawLineViewListener.onClearSelectedButton(position - 1);
    }

    private void callGetAnswer() {
        if (null != mOnDrawLineViewListener) {
            mOnDrawLineViewListener.onGetAnswer(getAnswers());
        }
    }

    private void callSelectedButton(int position) {
        if (null != mOnDrawLineViewListener) {
            mOnDrawLineViewListener.onSelectedButton(position - 1);
        }
    }

    private void callErrorButton(int position) {
        if (null != mOnDrawLineViewListener) {
            mOnDrawLineViewListener.onErrorButton(position - 1);
        }
    }

    private ChooseBean findValueInNumbers(int number) {
        if (mNumbers.size() > 0) {
            for (int i = 0; i < mNumbers.size(); i++) {
                //如果包含的话，则不取消选中状态
                if (mNumbers.get(i).getKey1() == number || mNumbers.get(i).getKey2() == number) {
                    return mNumbers.get(i);
                }
            }
        }
        return null;
    }

    /**
     * 得到的答案类型
     * 1A2B3C
     *
     * @return
     */
    public String getAnswers() {
        Collections.sort(mNumbers, new Comparator<ChooseBean>() {
            @Override
            public int compare(ChooseBean o1, ChooseBean o2) {
                return o1.getKey1() - o2.getKey1();
            }
        });
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < mNumbers.size(); i++) {
            stringBuilder.append(mNumbers.get(i).getKey1());//如1,2,3,4,5,6
            stringBuilder.append((switchNumberToLetter(mNumbers.get(i).getKey2()-1)));//将下排序号转为字母,如A,B,C,D,E,F
        }
        return stringBuilder.toString();
    }

    private void drawLine(Canvas canvas) {
        if (mNumbers.size() == 0) return;
        for (int i = 0; i < mNumbers.size(); i++) {
            int i1 = mNumbers.get(i).getKey1();
            if (mCoordinates.size() < i1 - 1) {
                return;
            }
            Coordinate coordinate_start = mCoordinates.get(i1 - 1);
            //  Loger.i(TAG, "i1:" + i1);
            int i2 = mNumbers.get(i).getKey2();
            // Loger.i(TAG, "i2:" + i2);
            Coordinate coordinate_end = mCoordinates.get(i2 - 1);
            float a = (float) Math.sqrt(Math.abs(coordinate_end.getX() - coordinate_start.getX()) / Math.abs(coordinate_end.getY() - coordinate_start.getY()) + 1);
            float b = (float) Math.sqrt(Math.abs(coordinate_end.getY() - coordinate_start.getY()) / Math.abs(coordinate_end.getX() - coordinate_start.getX()) + 1);
            if (mIsHorizontal) {
                canvas.drawLine(coordinate_start.getX(), coordinate_start.getY() + mCircle, coordinate_end.getX(), coordinate_end.getY() - mCircle, mNumbers.get(i).isRight ? mNormalPaint : mErrorPaint);
            } else {
                if (coordinate_end.getY() > coordinate_start.getY()) {
                    canvas.drawLine(coordinate_start.getX() + mCircle / b, coordinate_start.getY() + mCircle / a, coordinate_end.getX() - mCircle / b, coordinate_end.getY() - mCircle / a, mNumbers.get(i).isRight ? mNormalPaint : mErrorPaint);
                } else {
                    canvas.drawLine(coordinate_start.getX() + mCircle / b, coordinate_start.getY() - mCircle / a, coordinate_end.getX() - mCircle / b, coordinate_end.getY() + mCircle / a, mNumbers.get(i).isRight ? mNormalPaint : mErrorPaint);
                }
            }

        }
    }

    public void reset() {
        if (mNumbers != null) {
            mNumbers.clear();
        }
        postInvalidate();
    }

    /**
     * 清空选项
     */
    private void clearAnswerList() {
        if (null != mNumbers && mNumbers.size() > 0) {
            for (int i = 0; i < mNumbers.size(); i++) {
                callClearSelectedButton(mNumbers.get(i).getKey1());
                callClearSelectedButton(mNumbers.get(i).getKey2());
            }
            mNumbers.clear();
        }
    }

    private Pattern pattern = Pattern.compile("[0-9][A-Z]*");

    public void setAnswer(String answer) {
        clearAnswerList();
        if (answer == null || answer.isEmpty()) {
            postInvalidate();
            return;
        }
        List<String> strings = new ArrayList<>();
        conertStringToList(answer, strings);
        for (int i = 0; i < strings.size(); i++) {
            String answer_bottom = strings.get(i);
            int num = Integer.parseInt(answer_bottom.substring(0, 1));
            char[] chars = answer_bottom.substring(1, answer_bottom.length()).toCharArray();
            for (char c : chars) {
                int letter = switchLetterToNumber(c);
                callSelectedButton(num);
                callSelectedButton(letter);
                mNumbers.add(new ChooseBean(num, letter));
            }
        }
        Loger.i(TAG, "答案：" + mNumbers);
        postInvalidate();
    }

    public int switchLetterToNumber(char letter) {
        int temp = 0;
        if (letter == 'A') {
            temp = mItemCount;
        } else if (letter == 'B') {
            temp = mItemCount + 1;
        } else if (letter == 'C') {
            temp = mItemCount + 2;
        } else if (letter == 'D') {
            temp = mItemCount + 3;
        } else if (letter == 'E') {
            temp = mItemCount + 4;
        } else if (letter == 'F') {
            temp = mItemCount + 5;
        }
        return temp + 1;
    }

    public char switchNumberToLetter(int number) {
        char temp = 'A';
        temp += number % mItemCount;

        return temp;
    }

    public void showAnswer(String answer, String rightAnswer) {
        clearAnswerList();
        if (answer == null || answer.isEmpty()) {
            postInvalidate();
            return;
        }
        mNumbers.clear();
        List<String> answerList = new ArrayList<>();
        conertStringToList(answer, answerList);

        List<String> rightAnswerList = new ArrayList<>();
        conertStringToList(rightAnswer, rightAnswerList);
        for (int i = 0; i < answerList.size(); i++) {
            String answer_bottom = answerList.get(i);
            int num = Integer.parseInt(answer_bottom.substring(0, 1));
            char[] chars = answer_bottom.substring(1, answer_bottom.length()).toCharArray();
            for (char c : chars) {
                int letter = switchLetterToNumber(c);
                if (rightAnswerList.contains(answer_bottom)) {
                    mNumbers.add(new ChooseBean(letter, num, true));
                    callSelectedButton(num);
                    callSelectedButton(letter);
                } else {
                    callErrorButton(num);
                    callErrorButton(letter);
                    mNumbers.add(new ChooseBean(letter, num, false));
                }
            }
        }
        postInvalidate();
    }

    private void conertStringToList(String answer, List<String> answerList) {
        Matcher matcher = pattern.matcher(answer);

        boolean match_result = matcher.find();
        while (match_result) {
            String group = matcher.group();
            answerList.add(group);
            match_result = matcher.find();
        }
    }

    static class ChooseBean {
        /**
         * key1小于key2
         */
        int key1;
        /**
         * key2大于key1
         */
        int key2;
        /**
         * 答案是否正确
         */
        boolean isRight;

        public int getKey1() {
            return key1;
        }

        public ChooseBean(int key1, int key2) {
            if (key1 > key2) {
                this.key1 = key2;
                this.key2 = key1;
            } else {
                this.key1 = key1;
                this.key2 = key2;
            }
            this.isRight = true;

        }

        public ChooseBean(int key1, int key2, boolean isRight) {
            if (key1 > key2) {
                this.key1 = key2;
                this.key2 = key1;
            } else {
                this.key1 = key1;
                this.key2 = key2;
            }
            this.isRight = isRight;

        }

        public void setKey1(int key1) {
            this.key1 = key1;
        }

        public int getKey2() {
            return key2;
        }

        public void setKey2(int key2) {
            this.key2 = key2;
        }
    }
}
