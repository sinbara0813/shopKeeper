package com.d2cmall.shopkeeper.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/7/5.
 * 邮箱:hrb940258169@163.com
 */
public class ProductGroupTemplateTopHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_product_name)
    public TextView tvProductName;
    @BindView(R.id.group_price)
    public TextView groupPrice;
    @BindView(R.id.original_price)
    public TextView originalPrice;
    @BindView(R.id.group_num)
    public TextView groupNum;
    @BindView(R.id.group_invalid_date)
    public TextView groupInvalidDate;
    @BindView(R.id.tv_product_desc)
    public TextView tvProductDesc;

    public ProductGroupTemplateTopHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
