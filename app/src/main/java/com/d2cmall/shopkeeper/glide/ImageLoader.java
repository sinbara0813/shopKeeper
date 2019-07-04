package com.d2cmall.shopkeeper.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.utils.StringUtil;

import javax.microedition.khronos.opengles.GL;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ImageLoader {

    public static void displayImage(RequestManager manager, String url, ImageView imageView) {
        displayImage(manager,url,imageView,0,0);
    }

    public static void displayImage(RequestManager manager, String url, ImageView imageView,int id) {
        displayImage(manager,url,imageView,id,id);
    }

    public static void displayImage(Activity activity, String url, ImageView imageView) {
        displayImage(Glide.with(activity),url,imageView,0,0);
    }

    public static void displayImage(Activity activity, String url, ImageView imageView, int id) {
        displayImage(Glide.with(activity),url,imageView,id,id);
    }

    public static void displayImage(RequestManager manager, String url, ImageView imageView, int loadingResId, int failOrEmptyResId) {
        if (imageView == null) {
            return;
        }
        if (isGif(url)) {
            manager.load(StringUtil.getD2cPicUrl(url))
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(loadingResId)
                    .error(failOrEmptyResId)
                    .into(imageView);
        } else {
                manager.load(StringUtil.getD2cPicUrl(url))
                .placeholder(loadingResId)
                .crossFade()
                .error(failOrEmptyResId)
                .into(imageView);
        }
    }

    public static void displayRoundImage(Context context, String url, ImageView imageView, int errorId) {
        Glide.with(context).load(StringUtil.getD2cPicUrl(url)).bitmapTransform(new CropCircleTransformation(context)).error(errorId).crossFade().into(imageView);
    }

    public static void displayRoundedCornerImage(Context context, String url, ImageView imageView, int radius, int margin, int errorId) {
        Glide.with(context).load(StringUtil.getD2cPicUrl(url)).bitmapTransform(new RoundedCornersTransformation(context, radius, margin)).error(errorId).crossFade().into(imageView);
    }

    public static void displayBlurImage(Context context, String url, ImageView imageView, int radius) {
        Glide.with(context).load( StringUtil.getD2cPicUrl(url)).bitmapTransform(new BlurTransformation(context, radius)).crossFade().into(imageView);
    }

    public static void displayImageByPb(Context context, String url, int loadingId, final ImageView imageView, final ProgressBar progressBar) {
        url = url + Constants.MY_SUFFIX;
        Glide.with(context).load(url).asBitmap().placeholder(loadingId).error(loadingId).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                progressBar.setVisibility(View.GONE);
                imageView.setImageBitmap(resource);
            }
        });
    }

    private static boolean isGif(String url) {
        if (StringUtil.isEmpty(url)) {
            return false;
        }
        boolean is = false;
        int index = url.lastIndexOf(".");
        int length = url.length();
        if (index > 0 && (index + 4) <= length) {
            String suffix = url.substring((index + 1), (index + 4));
            if (suffix.equals("gif")) {
                return true;
            }
        }
        return is;
    }

}
