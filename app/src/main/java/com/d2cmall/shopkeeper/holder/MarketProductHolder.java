package com.d2cmall.shopkeeper.holder;

import android.support.constraint.ConstraintLayout;
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
 * 作者:Created by sinbara on 2019/5/9.
 * 邮箱:hrb940258169@163.com
 */

public class MarketProductHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.product_image)
    public ImageView productImage;
    @BindView(R.id.product_name)
    public TextView productName;
    @BindView(R.id.tag_content)
    public LinearLayout tagContent;
    @BindView(R.id.city_tv)
    public TextView cityTv;
    @BindView(R.id.product_price)
    public TextView productPrice;
    @BindView(R.id.profit_tv)
    public TextView profitTv;
    @BindView(R.id.root_rl)
    public ConstraintLayout rootRl;

    public MarketProductHolder(View itemView, int itemWith) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        ConstraintLayout.LayoutParams ll = (ConstraintLayout.LayoutParams) productImage.getLayoutParams();
        ll.width = itemWith - 3;
        ll.height = ll.width * 4 / 3;
    }
}
