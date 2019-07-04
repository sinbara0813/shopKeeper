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

public class WriteOffRecordHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_product)
    public  ImageView ivProduct;
    @BindView(R.id.describe_tv)
    public  TextView describeTv;
    @BindView(R.id.price_tv)
    public  TextView priceTv;
    @BindView(R.id.writeoff_time_tv)
    public  TextView writeoffTimeTv;
    @BindView(R.id.writeoff_name)
    public  TextView writeoffName;

    public WriteOffRecordHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
