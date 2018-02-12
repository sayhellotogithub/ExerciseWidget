package com.iblogstreet.exerciselib.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iblogstreet.exerciselib.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;


public class ShowThumnailTakePhotoDelegate implements ItemViewDelegate<String> {

    private int layoutId;
    private ShowThumnailViewAdapter.OnShowThumnailViewListener mOnShowThumnailViewListener;
    private boolean mIsClickable = true;
    private List<String> mUrlList = new ArrayList<>();
    private int mLimitCount = 4;

    public ShowThumnailTakePhotoDelegate(int layoutId, ShowThumnailViewAdapter.OnShowThumnailViewListener listener) {
        this.layoutId = layoutId;
        this.mOnShowThumnailViewListener = listener;

    }

    public ShowThumnailTakePhotoDelegate(int layoutId, ShowThumnailViewAdapter.OnShowThumnailViewListener listener, List<String> urlList, int limitCount) {
        this.layoutId = layoutId;
        this.mOnShowThumnailViewListener = listener;
        this.mUrlList = urlList;
        this.mLimitCount = limitCount;
    }

    public void setmIsClickable(boolean isClickable) {
        this.mIsClickable = isClickable;
    }

    @Override
    public int getItemViewLayoutId() {
        return layoutId;
    }

    @Override
    public boolean isForViewType(String item, int position) {
        return item.equals(ShowThumnailViewAdapter.PHOTO_FLAG);
    }

    @Override
    public void convert(final ViewHolder holder, String s, final int position) {
        ImageView ivTakePhoto = holder.getView(R.id.ivTakePhoto);
        TextView tvDesc = holder.getView(R.id.tvDesc);
        if (null == ivTakePhoto)
            return;
        ivTakePhoto.setEnabled(mIsClickable);
        holder.getView(R.id.ivTakePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canTakePhoto()) {
                    if (null != mOnShowThumnailViewListener) {
                        mOnShowThumnailViewListener.onTakePhoto();
                    }
                }
            }
        });
        if (mIsClickable) {
            tvDesc.setText((this.mUrlList.size() - 1) + "/" + mLimitCount);
            if (mUrlList.size() > mLimitCount) {
                holder.getConvertView().setVisibility(View.GONE);
            } else {
                holder.getConvertView().setVisibility(View.VISIBLE);
            }
        } else {
        }
    }

    public boolean canTakePhoto() {
        return this.mUrlList.size() - 1 < mLimitCount;
    }

}
