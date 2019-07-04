package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/4/30.
 * 邮箱:hrb940258169@163.com
 */

public class CartItemView extends LinearLayout {

    @BindView(R.id.content_ll)
    LinearLayout contentLl;
    @BindView(R.id.select_all_iv)
    CheckBox selectAllIv;
    @BindView(R.id.total_price_tv)
    TextView totalPriceTv;
    @BindView(R.id.free_tv)
    TextView freeTv;
    @BindView(R.id.buy_tv)
    TextView buyTv;

    public CartItemView(Context context) {
        this(context, null);
    }

    public CartItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(context).inflate(R.layout.layout_cart_item, this, true);
        ButterKnife.bind(this, this);
    }

    public void setData() {
        for (int i = 0; i < 3; i++) {
            addView();
        }

    }

    private void addView() {
        /*View productItem = LayoutInflater.from(getContext()).inflate(R.layout.layout_cart_product_item, new RelativeLayout(getContext()),false);
        contentLl.addView(productItem);*/
    }
}
