package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/3/4.
 * Description : CollageSelectCouponHolder
 */

public class CollageSelectCouponHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.check_iv)
    public ImageView checkIv;
    @BindView(R.id.iv)
    public ImageView iv;
    @BindView(R.id.name_tv)
    public TextView nameTv;
    @BindView(R.id.describe_tv)
    public TextView describeTv;
    @BindView(R.id.valid_time_tv)
    public TextView validTimeTv;
    @BindView(R.id.count_tv)
    public TextView countTv;
    @BindView(R.id.product_tag_tv)
    public TextView productTagTv;

    public CollageSelectCouponHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
