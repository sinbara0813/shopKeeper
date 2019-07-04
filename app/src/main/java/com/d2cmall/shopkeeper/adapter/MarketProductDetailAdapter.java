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
import com.d2cmall.shopkeeper.holder.ImageListHolder;
import com.d2cmall.shopkeeper.holder.MarketProductTopInfoHolder;
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
    private String videoUrl;
    private ArrayList<String> imgList;
    private ProductBean mProductBean;
    private ShopBean mShopBean;

    public MarketProductDetailAdapter(Context context) {
        this.mContext = context;
        imgList=new ArrayList<>();
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view ;
        if(viewType==1){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_market_product_top_info, parent, false);
            return new MarketProductTopInfoHolder(view);
        }else{
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_image_list_item,parent,false);
            return new ImageListHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position==0){
            MarketProductTopInfoHolder marketProductTopInfoHolder = (MarketProductTopInfoHolder) holder;
            if(mProductBean!=null){
                marketProductTopInfoHolder.tvProductName.setText(mProductBean.getName());
                if(mShopBean!=null){
                    marketProductTopInfoHolder.tvBrandName.setText(mShopBean.getName());
//                    店铺地区
//                    marketProductTopInfoHolder.tvArea.setText(mShopBean.getAddress());
                    ImageLoader.displayImage((Activity) mContext,mShopBean.getLogo(),marketProductTopInfoHolder.ivBrandLogo);
                    marketProductTopInfoHolder.llBrandWechat.setOnClickListener(new View.OnClickListener() {
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
                    marketProductTopInfoHolder.tvProductDesc.setVisibility(View.VISIBLE);
                    marketProductTopInfoHolder.tvProductDesc.setText(mProductBean.getDescription());
                }else{
                    marketProductTopInfoHolder.tvProductDesc.setVisibility(View.GONE);
                }
                setPrice(marketProductTopInfoHolder);
                setVideoView(marketProductTopInfoHolder);
            }
        }else{
            ImageListHolder imageListHolder = (ImageListHolder) holder;
            ImageLoader.displayImage((Activity) mContext,StringUtil.getD2cPicUrlByWidth(imgList.get(position-1),ScreenUtil.getDisplayWidth()),imageListHolder.ivImage,R.mipmap.ic_logo_empty5);
        }

    }

    private void showQrCode(String qrCode){
        ScanCodePop codePop=new ScanCodePop(mContext,qrCode,true);
        codePop.show(((Activity)mContext).getWindow().getDecorView());
    }

    private void setPrice(MarketProductTopInfoHolder holder) {
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
        return imgList==null?1:imgList.size()+1;
    }

    private void setVideoView(MarketProductTopInfoHolder holder) {
        if (StringUtil.isEmpty(videoUrl)) return;

        if (!videoUrl.startsWith("http")) {
            videoUrl = Constants.IMG_HOST + videoUrl;
        }

        TxVideoPlayerController txVideoPlayerController = new TxVideoPlayerController(mContext);
        holder.niceVideoPlayer.setController(txVideoPlayerController);
        if (imgList != null && imgList.size() > 0) {
            ImageLoader.displayImage((Activity) mContext, StringUtil.getD2cPicUrl(imgList.get(0)), txVideoPlayerController.getImage());
        }
        holder.niceVideoPlayer.setUp(videoUrl, null);
        holder.niceVideoPlayer.setVisibility(View.VISIBLE);
        //视频时长
//        int timeLong = 1100;
//        if (timeLong > 0) {
//            int m = timeLong / 60 % 60;
//            int s = timeLong % 60;
//            StringBuffer sb = new StringBuffer();
//
//            sb.append(String.format("%2d:", m));
//
//            sb.append(String.format("%02d", s));
//            txVideoPlayerController.setTime(sb.toString());
//        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 1;
        }else{
            return super.getItemViewType(position);
        }
    }

    public void setProductInfo(ProductBean productInfo) {
        this.mProductBean = productInfo;
        if (productInfo != null) {
            videoUrl = productInfo.getVideo();
            if (!StringUtil.isEmpty(productInfo.getPic())) {
                String[] split = productInfo.getPic().split(",");
                imgList.clear();
                for (int i = 0; i < split.length; i++) {
                    imgList.add(split[i]);
                }
            }
        }
    }

    public void setShopInfo(ShopBean shopBean) {
        mShopBean = shopBean;
        notifyDataSetChanged();
    }
}
