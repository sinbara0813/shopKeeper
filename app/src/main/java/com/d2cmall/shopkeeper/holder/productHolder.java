package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/2/19.
 * Description : productHolder
 */

public class productHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv)
    public ImageView iv;
    @BindView(R.id.info_tv)
    public TextView infoTv;
    @BindView(R.id.price_tv)
    public TextView priceTv;
    @BindView(R.id.store_tv)
    public TextView storeTv;
    @BindView(R.id.review_tv)
    public TextView reviewTv;
    @BindView(R.id.more_iv)
    public ImageView moreIv;

    public productHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
