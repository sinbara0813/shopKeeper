package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/3/4.
 * Description : CollageSelectCouponHolder
 */

public class ProductTopHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_brand_logo)
    public ImageView ivBrandLogo;
    @BindView(R.id.tv_product_name)
    public TextView tvProductName;
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
    @BindView(R.id.bg)
    public View bg;

    public ProductTopHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
