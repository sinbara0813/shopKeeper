package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/2/21.
 * Description : productHolder
 */

public class CustomerAfterSaleSubHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_action)
    public TextView tvAction;
    @BindView(R.id.iv_product)
    public ImageView ivProduct;
    @BindView(R.id.tv_order_status)
    public TextView tvOrderStatus;
    @BindView(R.id.tv_product_name)
    public TextView tvProductName;
    @BindView(R.id.tv_product_spec)
    public TextView tvProductSpec;
    @BindView(R.id.tv_product_price)
    public  TextView tvProductPrice;
    @BindView(R.id.tv_product_num)
    public TextView tvProductNum;
    @BindView(R.id.tv_return_info)
    public TextView tvReturnInfo;
    @BindView(R.id.tv_return_reason)
    public TextView tvReturnReason;
    @BindView(R.id.ll_button_container)
    public  LinearLayout llButtonContainer;
    @BindView(R.id.rl_bottom)
    public RelativeLayout rlBottom;


    public CustomerAfterSaleSubHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
