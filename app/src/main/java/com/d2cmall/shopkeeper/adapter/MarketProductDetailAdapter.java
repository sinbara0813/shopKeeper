package com.d2cmall.shopkeeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.ProductTopHolder;
import com.d2cmall.shopkeeper.holder.ProductContentItemHolder;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.ui.view.ScanCodePop;
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

public class MarketProductDetailAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<String> imgList;
    private ProductBean mProductBean;
    private ShopBean mShopBean;
    private boolean hasVideo;

    public MarketProductDetailAdapter(Context context,ProductBean bean) {
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
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_product_top, parent, false);
            return new ProductTopHolder(view);
        }else{
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_product_content_item,parent,false);
            return new ProductContentItemHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position==0){
            ProductTopHolder productTopHolder = (ProductTopHolder) holder;
            if(mProductBean!=null){
                productTopHolder.tvProductName.setText(mProductBean.getName());
                if(mShopBean!=null){
                    productTopHolder.tvBrandName.setText(mShopBean.getName());
                    ImageLoader.displayImage((Activity) mContext,mShopBean.getLogo(), productTopHolder.ivBrandLogo);
                    productTopHolder.bg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //点击微信有礼
                            if(!StringUtil.isEmpty(mShopBean.getWechat())){
                                showQrCode(mShopBean.getWechat());
                            }else{
                                Util.showToast(mContext,"商家尚未上传微信二维码!");
                            }
                        }
                    });
                }
                if(!StringUtil.isEmpty(mProductBean.getDescription())){
                    productTopHolder.tvProductDesc.setVisibility(View.VISIBLE);
                    productTopHolder.tvProductDesc.setText(mProductBean.getDescription());
                }else{
                    productTopHolder.tvProductDesc.setVisibility(View.GONE);
                }
                setPrice(productTopHolder);
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

    private void showQrCode(String qrCode){
        ScanCodePop codePop=new ScanCodePop(mContext,qrCode,true);
        codePop.show(((Activity)mContext).getWindow().getDecorView());
    }

    private void setPrice(ProductTopHolder holder) {
        //进价
        String inPriceStr = "进价 ¥ " + Util.getNumberFormat(mProductBean.getSupplyPrice());
        int length = inPriceStr.length();
        SpannableString textSpan = new SpannableString(inPriceStr);
        textSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(12)), 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(16)), 5, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#E93435"));
        textSpan.setSpan(colorSpan, 5, length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.tvInPrice.setText(textSpan);
        //利润
        String profitStr = "利润 ¥ " + Util.getNumberFormat(mProductBean.getProfit());
        int profitLength = profitStr.length();
        SpannableString profitTextSpan = new SpannableString(profitStr);
        profitTextSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(12)), 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        profitTextSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(16)), 5, profitLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ForegroundColorSpan profitColorSpan = new ForegroundColorSpan(Color.parseColor("#9C6C4B"));
        profitTextSpan.setSpan(profitColorSpan, 5, profitLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.tvProfit.setText(profitTextSpan);
        //售价
        String salePriceStr = "售价 ¥ " + Util.getNumberFormat(mProductBean.getPrice());
        int salePriceLength = salePriceStr.length();
        SpannableString salePriceTextSpan = new SpannableString(salePriceStr);
        salePriceTextSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(12)), 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        salePriceTextSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(16)), 5, salePriceLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        ForegroundColorSpan salePriceColorSpan = new ForegroundColorSpan(Color.parseColor("#333333"));
        salePriceTextSpan.setSpan(salePriceColorSpan, 5, salePriceLength, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        holder.tvSalePrice.setText(salePriceTextSpan);
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

    public void setShopInfo(ShopBean shopBean) {
        mShopBean = shopBean;
        notifyDataSetChanged();
    }
}
