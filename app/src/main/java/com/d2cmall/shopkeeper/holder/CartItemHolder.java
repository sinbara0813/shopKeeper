package com.d2cmall.shopkeeper.holder;

import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.view.CheckBox;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/4/30.
 * 邮箱:hrb940258169@163.com
 */

public class CartItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.content_ll)
    public LinearLayout contentLl;
    @BindView(R.id.select_all_iv)
    public CheckBox selectAllIv;
    @BindView(R.id.total_price_tv)
    public TextView totalPriceTv;
    @BindView(R.id.free_tv)
    public TextView freeTv;
    @BindView(R.id.buy_tv)
    public TextView buyTv;
    @BindView(R.id.delete_iv)
    public TextView deleteIv;
    @BindView(R.id.group)
    public Group group;
    @BindView(R.id.shop_name_tv)
    public TextView shopNameTv;

    public CartItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
