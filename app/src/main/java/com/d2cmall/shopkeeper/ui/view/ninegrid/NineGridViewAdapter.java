package com.d2cmall.shopkeeper.ui.view.ninegrid;

import android.content.Context;
import android.widget.ImageView;

import com.d2cmall.shopkeeper.utils.ScreenUtil;

import java.io.Serializable;
import java.util.List;

public abstract class NineGridViewAdapter implements Serializable {

    protected Context context;
    private List<ImageInfo> imageInfo;

    public NineGridViewAdapter(Context context, List<ImageInfo> imageInfo) {
        this.context = context;
        this.imageInfo = imageInfo;
    }

    /**
     * 需要子类实现该方法，以确定如何加载和显示图片
     *
     * @param context    上下文
     * @param imageView  需要展示图片的ImageView
     * @param imageInfo  图片地址
     * @param imageCount 图片数量
     */
    public abstract void onDisplayImage(Context context, ImageView imageView, ImageInfo imageInfo, int imageCount);

    /**
     * 如果要实现图片点击的逻辑，重写此方法即可
     *
     * @param context      上下文
     * @param nineGridView 九宫格控件
     * @param index        当前点击图片的的索引
     * @param imageInfo    图片地址的数据集合
     */
    protected void onImageItemClick(Context context, NineGridView nineGridView, int index, List<ImageInfo> imageInfo) {
    }

    /**
     * 生成ImageView容器的方式，默认使用NineGridImageViewWrapper类，即点击图片后，图片会有蒙板效果
     * 如果需要自定义图片展示效果，重写此方法即可
     *
     * @param context 上下文
     * @return 生成的 ImageView
     */
    protected ImageView generateImageView(Context context) {
        NineGridViewWrapper imageView = new NineGridViewWrapper(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setMaxWidth(ScreenUtil.dip2px(227));
        imageView.setMaxHeight(ScreenUtil.dip2px(227));
        return imageView;
    }

    public List<ImageInfo> getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(List<ImageInfo> imageInfo) {
        this.imageInfo = imageInfo;
    }
}