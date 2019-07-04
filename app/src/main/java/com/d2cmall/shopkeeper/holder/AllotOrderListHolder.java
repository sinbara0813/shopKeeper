package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class AllotOrderListHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_action)
    public TextView tvAction;
    @BindView(R.id.tv_order_status)
    public TextView tvOrderStatus;
    @BindView(R.id.ll_product_container)
    public LinearLayout llProductContainer;
    @BindView(R.id.tv_order_detial)
    public TextView tvOrderDetial;
    @BindView(R.id.tv_diff)
    public TextView tvDiff;
    @BindView(R.id.ll_button_container)
    public LinearLayout llButtonContainer;
    @BindView(R.id.rl_bottom)
    public RelativeLayout rlBottom;
    @BindView(R.id.rl_root)
    public RelativeLayout rlRoot;

    public AllotOrderListHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
