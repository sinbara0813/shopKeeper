package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/2/20.
 * Description : CustomerManagerHolder
 */

public class CustomerManagerHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv_user_head_pic)
    public ImageView ivUserHeadPic;
    @BindView(R.id.tv_user_name)
    public TextView tvUserName;
    @BindView(R.id.tv_consume)
    public TextView tvConsume;
    @BindView(R.id.tv_level)
    public TextView tvLevel;

    public CustomerManagerHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
