package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/5/7.
 * 邮箱:hrb940258169@163.com
 */

public class OfflineProductHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv)
    public ImageView iv;
    @BindView(R.id.name_tv)
    public TextView nameTv;
    @BindView(R.id.price_tv)
    public TextView priceTv;
    @BindView(R.id.store_tv)
    public TextView storeTv;
    @BindView(R.id.provide_tv)
    public TextView provideTv;
    @BindView(R.id.add_iv)
    public ImageView addIv;

    public OfflineProductHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
