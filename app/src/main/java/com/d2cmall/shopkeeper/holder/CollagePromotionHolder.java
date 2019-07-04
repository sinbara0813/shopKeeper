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
 * Created by LWJ on 2019/2/20.
 * Description : AddStaffHolder
 */

public class CollagePromotionHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.iv)
    public  ImageView iv;
    @BindView(R.id.name_tv)
    public TextView nameTv;
    @BindView(R.id.describe_tv)
    public TextView describeTv;
    @BindView(R.id.ll_collage_info)
    public LinearLayout llCollageInfo;
    @BindView(R.id.valid_time_tv)
    public TextView validTimeTv;
    @BindView(R.id.tv_price)
    public TextView tvPrice;
    @BindView(R.id.tv_edit)
    public  TextView tvEdit;
    @BindView(R.id.tv_invalid)
    public  TextView tvInvalid;
    @BindView(R.id.tv_collage_num)
    public  TextView tvCollageNum;
    @BindView(R.id.tv_preview)
    public  TextView tvPreview;


    public CollagePromotionHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
