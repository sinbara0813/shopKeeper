package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.view.nicevideo.NiceVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/3/4.
 * Description : CollageSelectCouponHolder
 */

public class MarketProductTopInfoHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_brand_logo)
    public ImageView ivBrandLogo;
    @BindView(R.id.ll_brand_wechat)
    public LinearLayout llBrandWechat;
    @BindView(R.id.line_layout)
    public View lineLayout;
    @BindView(R.id.tv_product_name)
    public TextView tvProductName;
    @BindView(R.id.tv_area)
    public TextView tvArea;
    @BindView(R.id.tv_in_price)
    public TextView tvInPrice;
    @BindView(R.id.tv_profit)
    public TextView tvProfit;
    @BindView(R.id.tv_sale_price)
    public TextView tvSalePrice;
    @BindView(R.id.tv_product_desc)
    public TextView tvProductDesc;
    @BindView(R.id.tv_brand_name)
    public TextView tvBrandName;
    @BindView(R.id.nice_video_player)
    public NiceVideoPlayer niceVideoPlayer;

    public MarketProductTopInfoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
