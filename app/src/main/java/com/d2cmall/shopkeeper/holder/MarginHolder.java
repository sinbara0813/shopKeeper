package com.d2cmall.shopkeeper.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/6/18.
 * 邮箱:hrb940258169@163.com
 */
public class MarginHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cash_tv)
    public TextView cashTv;
    @BindView(R.id.proposer_tv)
    public TextView proposerTv;
    @BindView(R.id.date_tv)
    public TextView dateTv;

    public MarginHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
