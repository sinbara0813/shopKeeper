package com.d2cmall.shopkeeper.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/7/5.
 * 邮箱:hrb940258169@163.com
 */
public class ProductTemplateHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv)
    public ImageView iv;
    @BindView(R.id.name_tv)
    public TextView nameTv;
    @BindView(R.id.price_tv)
    public TextView priceTv;
    @BindView(R.id.normal_product_tv)
    public TextView normalProductTv;
    @BindView(R.id.copy_tv)
    public TextView copyTv;
    @BindView(R.id.group_tag_tv)
    public TextView groupTagTv;
    @BindView(R.id.group_num_tv)
    public TextView groupNumTv;

    public ProductTemplateHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
