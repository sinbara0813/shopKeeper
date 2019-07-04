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
 * 作者:Created by sinbara on 2019/5/7.
 * 邮箱:hrb940258169@163.com
 */

public class OfflineCartProductHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv)
    public ImageView iv;
    @BindView(R.id.name_tv)
    public TextView nameTv;
    @BindView(R.id.store_tv)
    public TextView storeTv;
    @BindView(R.id.price_tv)
    public TextView priceTv;
    @BindView(R.id.add_iv)
    public ImageView addIv;
    @BindView(R.id.minus_iv)
    public ImageView minusIv;
    @BindView(R.id.num_et)
    public EditText numEt;

    public OfflineCartProductHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
