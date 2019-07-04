package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/2/25.
 * 邮箱:hrb940258169@163.com
 */

public class CouponHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv)
    public ImageView iv;
    @BindView(R.id.name_tv)
    public TextView nameTv;
    @BindView(R.id.describe_tv)
    public TextView describeTv;
    @BindView(R.id.valid_time_tv)
    public TextView validTimeTv;
    @BindView(R.id.count_tv)
    public TextView countTv;
    @BindView(R.id.product_tag_tv)
    public TextView productTagTv;
    @BindView(R.id.check_product_tv)
    public TextView checkProductTv;

    public CouponHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
