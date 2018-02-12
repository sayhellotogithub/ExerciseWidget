package com.iblogstreet.exerciselib.adapter;


import android.content.Context;

import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;


public class ShowThumnailViewAdapter extends MultiItemTypeAdapter<String> {

    public final static String PHOTO_FLAG = "-1";
    private ShowThumnailViewDelegate mShowThumnailViewDelegate;
    private ShowThumnailTakePhotoDelegate mShowThumnailTakePhotoDelegate;
    private int mLimitCount = 4;

    public ShowThumnailViewAdapter(Context context, List<String> datas, int itemLayout, int photoLayout, OnShowThumnailViewListener listener) {
        super(context, datas);
        mShowThumnailViewDelegate = new ShowThumnailViewDelegate(itemLayout, listener, context);
        mShowThumnailTakePhotoDelegate = new ShowThumnailTakePhotoDelegate(photoLayout, listener);
        addItemViewDelegate(mShowThumnailViewDelegate);
        addItemViewDelegate(mShowThumnailTakePhotoDelegate);
    }

    public ShowThumnailViewAdapter(Context context, List<String> datas, int itemLayout, int photoLayout, int limitCount, OnShowThumnailViewListener listener) {
        super(context, datas);
        this.mLimitCount = limitCount;
        mShowThumnailViewDelegate = new ShowThumnailViewDelegate(itemLayout, listener, context);
        mShowThumnailTakePhotoDelegate = new ShowThumnailTakePhotoDelegate(photoLayout, listener, datas, mLimitCount);

        addItemViewDelegate(mShowThumnailViewDelegate);
        addItemViewDelegate(mShowThumnailTakePhotoDelegate);
    }

    public void setRadius(int radius) {
        mShowThumnailViewDelegate.setRadius(radius);
    }

    public interface OnShowThumnailViewListener {
        void onRemove(int postion, String url);

        void onTakePhoto();
    }

    public void setUrlPrefix(String urlPrefix) {
        mShowThumnailViewDelegate.setUrlPrefix(urlPrefix);
    }

    private OnShowThumnailViewListener mOnShowThumnailViewListener;

    public void setOnShowThumnailViewListener(OnShowThumnailViewListener listener) {
        this.mOnShowThumnailViewListener = listener;
    }

    public void setClickable(boolean clickable) {
        getDatas().remove(PHOTO_FLAG);
        mShowThumnailViewDelegate.setmIsClickable(clickable);
        mShowThumnailTakePhotoDelegate.setmIsClickable(clickable);
        notifyDataSetChanged();
    }

    public void setIsShowDelete(boolean isShowDelete) {
        mShowThumnailViewDelegate.setmIsShowDelete(isShowDelete);
        notifyDataSetChanged();
    }

    public void setShowAnswer(List<String> urlList) {
        getDatas().clear();
        mShowThumnailViewDelegate.setmIsClickable(false);
        mShowThumnailTakePhotoDelegate.setmIsClickable(false);
        mShowThumnailViewDelegate.setmIsShowDelete(false);
        getDatas().addAll(urlList);
        notifyDataSetChanged();
    }

    public void addUrl(String url) {
        if (getDatas().contains(url)) {
            return;
        }
        getDatas().remove(PHOTO_FLAG);
        getDatas().add(url);
        getDatas().add(PHOTO_FLAG);
        notifyDataSetChanged();
    }

    public void removeUrl(String url) {
        getDatas().remove(url);
        notifyDataSetChanged();
    }

    public void setUrlList(List<String> urlList) {
        getDatas().clear();
        getDatas().addAll(urlList);
        getDatas().add(PHOTO_FLAG);
        notifyDataSetChanged();
    }


}
