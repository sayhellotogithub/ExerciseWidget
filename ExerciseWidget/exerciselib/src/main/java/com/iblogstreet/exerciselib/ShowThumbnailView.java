package com.iblogstreet.exerciselib;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.iblogstreet.exerciselib.adapter.ShowThumnailViewAdapter;
import com.iblogstreet.exerciselib.decoration.SpacesItemDecoration;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：显示缩略图的View
 *
 * @author 王军
 * @time 2018/01/15 11:20
 */

public class ShowThumbnailView extends LinearLayout implements MultiItemTypeAdapter.OnItemClickListener, ShowThumnailViewAdapter.OnShowThumnailViewListener {
    /**
     * 图片列表RecyclerView
     */
    private RecyclerView mRvPic;
    private ShowThumnailViewAdapter mShowThumnailViewAdapter;
    public final static int DEFAULT_ITEM_LAYOUT = R.layout.item_thumb;
    public final static int DEFAULT_ITEM_PHOTO_LAYOUT = R.layout.item_take_photo;
    public final static int DEFAULT_ITEM_COUNT = 4;
    private final static int LIMIT_COUNT = 4;
    private final static int DEFAULT_RADIUS = 0;
    private int mItemLayout = DEFAULT_ITEM_LAYOUT;
    private int mPhotoItemLayout = DEFAULT_ITEM_PHOTO_LAYOUT;
    private int mItemCount = DEFAULT_ITEM_COUNT;
    private int mRoundRadius = DEFAULT_RADIUS;
    private OnShowThumbnailListener mOnShowThumbnailListener;
    private RecyclerView.ItemDecoration mItemDecoration;
    /**
     * 限制图片的数目
     */
    private int mLimitCount;

    public void setOnShowThumbnailListener(OnShowThumbnailListener listener) {
        this.mOnShowThumbnailListener = listener;
    }

    public ShowThumbnailView(Context context) {
        this(context, null);
    }

    public ShowThumbnailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowThumbnailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        initView();
        initData();
        initEvent();
    }

    public void initView() {
        mRvPic = new RecyclerView(getContext());
    }

    private void initData() {
        mShowThumnailViewAdapter = new ShowThumnailViewAdapter(getContext(), new ArrayList<String>(), mItemLayout, mPhotoItemLayout, mLimitCount, this);
        mShowThumnailViewAdapter.setRadius(mRoundRadius);
        mRvPic.setLayoutManager(new GridLayoutManager(getContext(), 1));
        mItemDecoration = new SpacesItemDecoration(0);
        setItemDecoration(mItemDecoration);
        mRvPic.setAdapter(mShowThumnailViewAdapter);
        mRvPic.setLayoutParams(getSelfLayoutParams());
        addViewToRoot();
    }

    /**
     * 设置ItemDecoration
     *
     * @param itemDecoration
     */
    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (null != mItemDecoration)
            mRvPic.removeItemDecoration(mItemDecoration);
        mItemDecoration = itemDecoration;
        mRvPic.addItemDecoration(mItemDecoration);
    }

    /**
     * 得到Url
     *
     * @return
     */
    private List<String> getUrlList() {
        List<String> result = new ArrayList<>();
        if (null != mShowThumnailViewAdapter) {
            result.addAll(mShowThumnailViewAdapter.getDatas());
            result.remove(ShowThumnailViewAdapter.PHOTO_FLAG);
        }

        return result;
    }

    public void showAnswerResult() {
        mShowThumnailViewAdapter.setClickable(false);
        mShowThumnailViewAdapter.setIsShowDelete(false);
        setGridLayoutManagerItemCount();
    }

    /**
     * @param urlList
     */
    public void showAnswerResult(List<String> urlList) {
        if (null != mShowThumnailViewAdapter) {
            mShowThumnailViewAdapter.setShowAnswer(urlList);
        }
        setGridLayoutManagerItemCount();
    }

    private void removeUrl(String url) {
        if (null != mShowThumnailViewAdapter) {
            mShowThumnailViewAdapter.removeUrl(url);
        }
        setGridLayoutManagerItemCount();
    }

    /**
     * 设置Url前辍
     *
     * @param urlPrefix
     */
    public void setUrlPrefix(String urlPrefix) {
        mShowThumnailViewAdapter.setUrlPrefix(urlPrefix);
    }

    /**
     * 添加答案
     *
     * @param url
     */
    public void addUrl(String url) {
        mShowThumnailViewAdapter.addUrl(url);
        setGridLayoutManagerItemCount();
        callChooseQuestionListener(getUrlList());
    }

    /**
     * 设置GridLayout的ItemCount
     */
    private void setGridLayoutManagerItemCount() {
        int count = mShowThumnailViewAdapter.getDatas().size() > mItemCount ? mItemCount : mShowThumnailViewAdapter.getDatas().size();
        mRvPic.setLayoutManager(new GridLayoutManager(getContext(), count == 0 ? 1 : count));
    }

    /**
     * 设置答案列表
     *
     * @param urlList
     */
    public void setUrlList(List<String> urlList) {
        if (null != mShowThumnailViewAdapter) {
            mShowThumnailViewAdapter.setUrlList(urlList);
        }
        setGridLayoutManagerItemCount();
    }

    private void initAttr(AttributeSet attributeSet) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.ShowThumbnailView);
        if (typedArray != null) {
            int n = typedArray.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = typedArray.getIndex(i);
                if (attr == R.styleable.ShowThumbnailView_stvItemLayout) {
                    mItemLayout = typedArray.getResourceId(attr, DEFAULT_ITEM_LAYOUT);
                } else if (attr == R.styleable.ShowThumbnailView_stvLineItemCount) {
                    mItemCount = typedArray.getInteger(attr, DEFAULT_ITEM_COUNT);
                } else if (attr == R.styleable.ShowThumbnailView_stvPhotoLayout) {
                    mPhotoItemLayout = typedArray.getResourceId(attr, DEFAULT_ITEM_LAYOUT);
                } else if (attr == R.styleable.ShowThumbnailView_stvLimitCount) {
                    mLimitCount = typedArray.getInteger(attr, LIMIT_COUNT);
                } else if (attr == R.styleable.ShowThumbnailView_stvRoundRadius) {
                    mRoundRadius = typedArray.getDimensionPixelSize(attr, DEFAULT_RADIUS);
                }
            }
            typedArray.recycle();

        }
    }

    private void initEvent() {
        mShowThumnailViewAdapter.setOnItemClickListener(this);
    }

    private void addViewToRoot() {
        addView(mRvPic);
    }

    private LinearLayout.LayoutParams getSelfLayoutParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        return params;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    public void onRemove(int postion, String url) {
        removeUrl(url);
        callChooseQuestionListener(getUrlList());
    }

    @Override
    public void onTakePhoto() {
        if (mOnShowThumbnailListener != null) {
            mOnShowThumbnailListener.onTakePhoto();
        }
    }

    private void callChooseQuestionListener(List<String> answerList) {
        if (mOnShowThumbnailListener != null) {
            mOnShowThumbnailListener.onAnswerList(answerList, this);
        }
    }

    /**
     * 选择题接口
     */
    public interface OnShowThumbnailListener {
        void onAnswerList(List<String> answerList, ShowThumbnailView showThumbnailView);//选择结果

        void onTakePhoto();
    }
}
