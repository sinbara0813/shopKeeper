package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.view.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/2/20.
 * Description : AddStaffHolder
 */

public class AddStaffHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_add_staff)
    public TextView tvAddStaff;

    public AddStaffHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
