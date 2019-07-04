package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.view.UploadImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/3/29.
 * 邮箱:hrb940258169@163.com
 */

public class SimpleImageHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv)
    public UploadImageView iv;
    @BindView(R.id.add_iv)
    public ImageView addIv;
    @BindView(R.id.delete)
    public ImageButton delete;

    public SimpleImageHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
