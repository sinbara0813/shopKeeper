package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/2/26.
 * Description : TransactionRecordHolder
 */

public class TransactionRecordHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_amount)
    public TextView tvAmount;
    @BindView(R.id.tv_product_name)
    public TextView tvProductName;
    @BindView(R.id.tv_order_code)
    public TextView tvOrderCode;
    @BindView(R.id.tv_pay_code)
    public TextView tvPayCode;
    @BindView(R.id.tv_pay_date)
    public TextView tvPayDate;

    public TransactionRecordHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
