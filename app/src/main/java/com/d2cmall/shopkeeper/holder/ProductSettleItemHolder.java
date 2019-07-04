package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/5/6.
 * 邮箱:hrb940258169@163.com
 */

public class ProductSettleItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title_tv)
    public TextView titleTv;
    @BindView(R.id.content_ll)
    public LinearLayout contentLl;
    @BindView(R.id.price_tv)
    public TextView priceTv;
    @BindView(R.id.cash_deposit_tv)
    public TextView cashDepositTv;
    @BindView(R.id.settle_tv)
    public TextView settleTv;

    public ProductSettleItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
