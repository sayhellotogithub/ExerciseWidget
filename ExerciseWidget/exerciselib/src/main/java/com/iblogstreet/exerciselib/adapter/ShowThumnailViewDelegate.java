package com.iblogstreet.exerciselib.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;


import com.iblogstreet.exerciselib.R;
import com.iblogstreet.exerciselib.transform.GlideRoundTransform;
import com.iblogstreet.exerciselib.utils.ImageLoadUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;


public class ShowThumnailViewDelegate implements ItemViewDelegate<String> {

    private int layoutId;
    private boolean mIsClickable;
    private String mUrlPrefix;

    public void setmIsShowDelete(boolean mIsShowDelete) {
        this.mIsShowDelete = mIsShowDelete;
    }

    private GlideRoundTransform mGlideRoundTransform;
    private boolean mIsShowDelete;
    private ShowThumnailViewAdapter.OnShowThumnailViewListener mOnShowThumnailViewListener;
    private Context mContext;

    public void setRadius(int radius) {
        if (radius > 0)
            this.mGlideRoundTransform = new GlideRoundTransform(mContext, radius);
        else {
            this.mGlideRoundTransform = null;
        }
    }

    public ShowThumnailViewDelegate(int layoutId, ShowThumnailViewAdapter.OnShowThumnailViewListener listener, Context context) {
        this.layoutId = layoutId;
        this.mContext = context;
        this.mOnShowThumnailViewListener = listener;
        this.mIsClickable = true;
        this.mIsShowDelete = true;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.mUrlPrefix = urlPrefix;
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
        return !item.equals(ShowThumnailViewAdapter.PHOTO_FLAG);
    }

    @Override
    public void convert(final ViewHolder holder, final String s, int position) {
        ImageView ivThumb = holder.getView(R.id.ivThumb);
        if (null != mGlideRoundTransform) {
            ImageLoadUtils.loadImageIntoViewRound(holder.getConvertView().getContext(), getUrl(mUrlPrefix, s), ivThumb, R.mipmap.loading, R.mipmap.loading_fail, mGlideRoundTransform);
        } else {
            ImageLoadUtils.loadImageIntoView(holder.getConvertView().getContext(), getUrl(mUrlPrefix, s), ivThumb, R.mipmap.loading, R.mipmap.loading_fail);

        }
        holder.getView(R.id.ivDelete).setVisibility(mIsShowDelete ? View.VISIBLE : View.INVISIBLE);
        holder.getView(R.id.ivDelete).setEnabled(mIsClickable);
        holder.getView(R.id.ivDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnShowThumnailViewListener) {
                    mOnShowThumnailViewListener.onRemove(holder.getAdapterPosition(), s);
                }
            }
        });
    }


    public String getUrl(String server, String url) {
        if (TextUtils.isEmpty(url))
            return "";

        if (!url.contains("http://")) {
            return "http://" + server + "/" + url;
        } else {
            return url;
        }
    }

}
