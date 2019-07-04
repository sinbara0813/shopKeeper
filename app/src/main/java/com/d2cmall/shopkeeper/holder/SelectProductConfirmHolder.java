package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/3/4.
 * Description : SelectProductConfirmHolder
 */

public class SelectProductConfirmHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_product_spec)
    public TextView tvProductSpec;
    @BindView(R.id.tv_product_stock)
    public TextView tvProductStock;
    @BindView(R.id.tv_product_price)
    public  TextView tvProductPrice;

    public SelectProductConfirmHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
