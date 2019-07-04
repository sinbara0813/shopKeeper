package com.d2cmall.shopkeeper.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.view.NineGridTestLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/6/20.
 * 邮箱:hrb940258169@163.com
 */
public class MarketProductHolder1 extends RecyclerView.ViewHolder {

    @BindView(R.id.iv)
    public ImageView iv;
    @BindView(R.id.name_tv)
    public TextView nameTv;
    @BindView(R.id.join_tv)
    public TextView joinTv;
    @BindView(R.id.info_tv)
    public TextView infoTv;
    @BindView(R.id.nine_rl)
    public NineGridTestLayout nineRl;
    @BindView(R.id.product_price)
    public TextView productPrice;
    @BindView(R.id.profit_tv)
    public TextView profitTv;
    @BindView(R.id.tag_ll)
    public LinearLayout tagLl;
    @BindView(R.id.date_tv)
    public TextView dateTv;
    @BindView(R.id.down_tv)
    public TextView downTv;
    @BindView(R.id.share_tv)
    public TextView shareTv;

    public MarketProductHolder1(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
