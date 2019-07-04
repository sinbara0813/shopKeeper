package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ImageListHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_image)
    public ImageView ivImage;

    public ImageListHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
