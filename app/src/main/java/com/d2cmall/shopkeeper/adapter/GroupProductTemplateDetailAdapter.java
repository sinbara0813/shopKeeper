package com.d2cmall.shopkeeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.ProductContentItemHolder;
import com.d2cmall.shopkeeper.holder.ProductGroupTemplateTopHolder;
import com.d2cmall.shopkeeper.holder.ProductTemplateTopHolder;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.view.nicevideo.TxVideoPlayerController;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;


/**
 * Created by LWJ on 2019/5/6.
 * Description : MarketProductDetailAdapter
 * 选货市场商品详情头部adapter
 */

public class GroupProductTemplateDetailAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> imgList;
    private ProductBean mProductBean;
    private boolean hasVideo;

    public GroupProductTemplateDetailAdapter(Context context, ProductBean bean) {
        this.mContext = context;
        this.mProductBean=bean;
        imgList=new ArrayList<>();
        if (mProductBean != null) {
            hasVideo=!StringUtil.isEmpty(mProductBean.getVideo());
            if (!StringUtil.isEmpty(mProductBean.getPic())) {
                String[] split = mProductBean.getPic().split(",");
                imgList.clear();
                for (int i = 0; i < split.length; i++) {
                    imgList.add(split[i]);
                }
            }
        }
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        if(viewType==1){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_product_group_template_top, parent, false);
            return new ProductGroupTemplateTopHolder(view);
        }else{
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_product_content_item,parent,false);
            return new ProductContentItemHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position==0){
            ProductGroupTemplateTopHolder productTopHolder = (ProductGroupTemplateTopHolder) holder;
            if(mProductBean!=null){
                productTopHolder.tvProductName.setText(mProductBean.getName());
                if(!StringUtil.isEmpty(mProductBean.getDescription())){
                    productTopHolder.tvProductDesc.setVisibility(View.VISIBLE);
                    productTopHolder.tvProductDesc.setText(mProductBean.getDescription());
                }else{
                    productTopHolder.tvProductDesc.setVisibility(View.GONE);
                }
                setPrice(productTopHolder,mProductBean);
            }
        }else{
            ProductContentItemHolder imageListHolder = (ProductContentItemHolder) holder;
            if (hasVideo&&position==1){
                imageListHolder.videoFl.setVisibility(View.VISIBLE);
                imageListHolder.imageIv.setVisibility(View.GONE);
                setVideoView(imageListHolder);
            }else {
                imageListHolder.videoFl.setVisibility(View.GONE);
                imageListHolder.imageIv.setVisibility(View.VISIBLE);
                ImageLoader.displayImage((Activity) mContext,StringUtil.getD2cPicUrlByWidth(imgList.get(position-(hasVideo?2:1)),ScreenUtil.getDisplayWidth()-ScreenUtil.dip2px(30)),imageListHolder.imageIv,R.mipmap.ic_logo_empty5);
            }
        }
    }

    private void setPrice(ProductGroupTemplateTopHolder holder,ProductBean bean) {
        StringBuilder builder=new StringBuilder();
        builder.append("拼团价 ").append("¥").append(Util.getNumberFormat(bean.getPrice()));
        holder.groupPrice.setText(Util.buildSpan(new int[]{mContext.getResources().getColor(R.color.color_black3)},new int[]{3},new float[]{0.8f},new int[]{3},builder.toString()));
        builder=new StringBuilder();
        builder.append("原价 ").append("¥").append(Util.getNumberFormat(bean.getPrice()));
        holder.originalPrice.setText(Util.buildSpan(new int[]{mContext.getResources().getColor(R.color.color_black3)},new int[]{2},new float[]{0.8f},new int[]{3},builder.toString()));
        builder=new StringBuilder();
        builder.append("成团人数 ").append(3).append("人成团");
        holder.originalPrice.setText(Util.buildColorSpan(new int[]{mContext.getResources().getColor(R.color.color_black3)},new int[]{4},builder.toString()));
        builder=new StringBuilder();
        builder.append("成团有效期 ").append(24).append("小时");
        holder.originalPrice.setText(Util.buildColorSpan(new int[]{mContext.getResources().getColor(R.color.color_black3)},new int[]{5},builder.toString()));
    }

    @Override
    public int getItemCount() {
        return hasVideo?imgList.size()+2:imgList.size()+1;
    }

    private void setVideoView(ProductContentItemHolder holder) {
        String videoUrl=mProductBean.getVideo();
        if (StringUtil.isEmpty(videoUrl)) return;

        if (!videoUrl.startsWith("http")) {
            videoUrl = Constants.IMG_HOST + videoUrl;
        }

        TxVideoPlayerController txVideoPlayerController = new TxVideoPlayerController(mContext);
        holder.videoFl.setController(txVideoPlayerController);
        if (imgList != null && imgList.size() > 0) {
            ImageLoader.displayImage((Activity) mContext, StringUtil.getD2cPicUrl(imgList.get(0)), txVideoPlayerController.getImage());
        }
        holder.videoFl.setUp(videoUrl, null);
        holder.videoFl.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 1;
        }else{
            return super.getItemViewType(position);
        }
    }
}
