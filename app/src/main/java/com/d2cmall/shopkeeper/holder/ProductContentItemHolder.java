package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.view.nicevideo.NiceVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductContentItemHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_iv)
    public ImageView imageIv;
    @BindView(R.id.video_fl)
    public NiceVideoPlayer videoFl;

    public ProductContentItemHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
