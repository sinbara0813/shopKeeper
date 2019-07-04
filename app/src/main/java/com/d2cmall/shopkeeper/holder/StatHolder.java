package com.d2cmall.shopkeeper.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/6/21.
 * 邮箱:hrb940258169@163.com
 */
public class StatHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name_tv)
    public TextView nameTv;
    @BindView(R.id.sell_tv)
    public TextView sellTv;
    @BindView(R.id.order_tv)
    public TextView orderTv;
    @BindView(R.id.product_num_tv)
    public TextView productNumTv;
    @BindView(R.id.people_tv)
    public TextView peopleTv;

    public StatHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
