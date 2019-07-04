package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/2/26.
 * 邮箱:hrb940258169@163.com
 */

public class SelectProductHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.check_iv)
    public ImageView checkIv;
    @BindView(R.id.iv)
    public ImageView iv;
    @BindView(R.id.describe_tv)
    public TextView describeTv;
    @BindView(R.id.price_tv)
    public TextView priceTv;
    @BindView(R.id.store_tv)
    public TextView storeTv;

    public SelectProductHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
