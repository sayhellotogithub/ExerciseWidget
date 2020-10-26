package com.iblogstreet.exerciselib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：缩略图选项封装
 *
 * @author 王军
 * @time 2018/01/15 11:20
 */
public class ThumbnailItemView extends FrameLayout {

    private final static int DEFAULT_WIDTH = 90;
    private final static int DEFAULT_MARGIN = 0;
    private Drawable mDeleteBackGround;
    private int mHeight;
    private int mWidth;
    private int mMarginLeft = DEFAULT_MARGIN;
    private int mMarginRight = DEFAULT_MARGIN;
    private int mMarginBottom = DEFAULT_MARGIN;
    private int mMarginTop = DEFAULT_MARGIN;

    private List<ImageView> mImageViewList = new ArrayList<>();
    private Drawable mThumbBackGround;

    public ThumbnailItemView(Context context) {
        this(context, null);
    }

    public ThumbnailItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttrs(attrs);
        initData();
    }

    /**
     * 实始化属性
     *
     * @param attrs
     */
    private void initAttrs(AttributeSet attrs) {
        //获得我们所定义的自定义样式属性
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ThumbnailItemView);
        if (typedArray != null) {
            int n = typedArray.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.ThumbnailItemView_tivDeleteBackGround) {
                    mDeleteBackGround = typedArray.getDrawable(attr);
                } else if (attr == R.styleable.ThumbnailItemView_tivHeight) {
                    mHeight = typedArray.getDimensionPixelSize(attr, DEFAULT_WIDTH);
                } else if (attr == R.styleable.ThumbnailItemView_tivWidth) {
                    mWidth = typedArray.getDimensionPixelSize(attr, DEFAULT_WIDTH);
                } else if (attr == R.styleable.ThumbnailItemView_tivThumbMarginLeft) {
                    mMarginLeft = typedArray.getDimensionPixelSize(attr, DEFAULT_MARGIN);
                } else if (attr == R.styleable.ThumbnailItemView_tivThumbMarginRight) {
                    mMarginRight = typedArray.getDimensionPixelSize(attr, DEFAULT_MARGIN);
                } else if (attr == R.styleable.ThumbnailItemView_tivThumbMarginTop) {
                    mMarginTop = typedArray.getDimensionPixelSize(attr, DEFAULT_MARGIN);
                } else if (attr == R.styleable.ThumbnailItemView_tivThumbMarginBottom) {
                    mMarginBottom = typedArray.getDimensionPixelSize(attr, DEFAULT_MARGIN);
                } else if (attr == R.styleable.ThumbnailItemView_tivThumbBackGround) {
                    mThumbBackGround = typedArray.getDrawable(attr);
                }
            }
            typedArray.recycle();

        }
    }


    /**
     * 实始化View
     */
    private void initView() {
        addViewToRoot();
    }

    private void initData() {

        if (null == mImageViewList || mImageViewList.size() < 2) {
            return;
        }
        //设置ivThumb的属性
        LayoutParams selfLayoutParams = getSelfLayoutParams();
        selfLayoutParams.width = mWidth;
        selfLayoutParams.height = mHeight;
        mImageViewList.get(0).setLayoutParams(selfLayoutParams);
        if (mThumbBackGround != null)
            mImageViewList.get(0).setBackground(mThumbBackGround);

        selfLayoutParams = getSelfLayoutParams();
        selfLayoutParams.gravity = Gravity.RIGHT;
        mImageViewList.get(1).setLayoutParams(selfLayoutParams);
        //设置添加ivDelete
        if (mDeleteBackGround != null)
            mImageViewList.get(1).setBackground(mDeleteBackGround);

    }

    public ImageView getIvDelete() {
        if (null == mImageViewList || mImageViewList.size() < 2)
            return null;
        return mImageViewList.get(1);
    }

    /**
     * 得到缩略图控件
     *
     * @return
     */
    public ImageView getIvThumb() {
        if (null == mImageViewList || mImageViewList.size() == 0)
            return null;
        return mImageViewList.get(0);
    }

    private FrameLayout.LayoutParams getSelfLayoutParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        return params;
    }

    public void addViewToRoot() {
        this.removeAllViews();
        //添加ivThumb
        ImageView ivThumb = new ImageView(getContext());
        addView(ivThumb);
        mImageViewList.add(ivThumb);
        //添加ivDelete
        ImageView ivDelete = new ImageView(getContext());
        addView(ivDelete);
        mImageViewList.add(ivDelete);
    }


}
