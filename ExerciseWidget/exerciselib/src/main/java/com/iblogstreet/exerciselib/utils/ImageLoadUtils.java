package com.iblogstreet.exerciselib.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.iblogstreet.exerciselib.transform.GlideRoundTransform;

/**
 * 项目名称：trunk
 * 类描述：图片加载
 * 创建人：王军
 * 创建时间：2016/11/7 16:33
 */
public class ImageLoadUtils {
    public static void loadImageIntoView(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }

    public static void loadImageIntoViewAsBitmap(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).asBitmap().into(imageView);
    }

    public static void loadImageIntoView(Context context, String url, ImageView imageView, int placeholderId, int errorId) {
        Glide.with(context).load(url).placeholder(placeholderId).error(errorId).into(imageView);
    }

    public static void loadImageIntoViewRound(Context context, String url, ImageView imageView, int placeholderId, int errorId, GlideRoundTransform glideRoundTransform) {
        Glide.with(context).load(url).placeholder(placeholderId).transform(new CenterCrop(context), glideRoundTransform).error(errorId).into(imageView);
    }

    public static void loadImageIntoView(Context context, String url, ImageView imageView, RequestListener<String, GlideDrawable> listener) {
        if (listener == null) {
            loadImageIntoView(context, url, imageView);
        } else {
            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(listener).into(imageView);
        }
    }

    public static void loadImageIntoTarget(Context context, String url, SimpleTarget simpleTarget) {
        Glide.with(context).load(url).asBitmap().into(simpleTarget);
    }


    /**
     * @param context
     * @param url
     * @param imageView
     * @param id        默认图片
     */
    public static void loadImageIntoViewAvatar(Context context, String url, ImageView imageView, int id) {
        Glide.with(context).load(url).error(id).into(imageView);
    }

    public static void loadImageIntoView(Context context, String url, ImageView imageView, int id) {
        Glide.with(context).load(url).error(id).placeholder(id).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 创建人：王军
     * 创建时间：2016/11/7 16:56
     * 描述：加载试卷     *
     *
     * @param context
     * @param url
     * @param imageView
     * @param id        默认图片
     */
    public static void loadImageIntoViewEPaper(Context context, String url, ImageView imageView, int id) {
        Glide.with(context).load(url).error(id).placeholder(id)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 清除target请求
     *
     * @param target
     */
    public static void clearTarget(Target<?> target) {
        Glide.clear(target);
    }

    public static void loadGifImageIntoView(Context context, int resId, ImageView imageView) {
        Glide.with(context).load(resId).asGif().into(imageView);
    }


}
