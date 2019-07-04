package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by LWJ on 2019/3/4.
 * Description : productHolder
 */


public class CollagePromotionSelectProductHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv)
    public ImageView iv;
    @BindView(R.id.describe_tv)
    public TextView describeTv;
    @BindView(R.id.price_tv)
    public TextView priceTv;
    @BindView(R.id.store_tv)
    public TextView storeTv;
    @BindView(R.id.et_promotion_price)
    public EditText etPromotionPrice;


    public CollagePromotionSelectProductHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
