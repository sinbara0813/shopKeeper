package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.holder.CouponHolder;
import com.d2cmall.shopkeeper.model.CouponBean;
import com.d2cmall.shopkeeper.ui.activity.AddCouponActivity;
import com.d2cmall.shopkeeper.ui.activity.AssignProductActivity;
import com.d2cmall.shopkeeper.ui.activity.CouponActivity;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

/**
 * 作者:Created by sinbara on 2019/2/25.
 * 邮箱:hrb940258169@163.com
 */

public class CouponAdapter extends DelegateAdapter.Adapter<CouponHolder> {

    private Context mContext;
    private  List<CouponBean> mCouponList;
    public CouponAdapter(Context context, List<CouponBean> couponList){
        this.mContext=context;
        this.mCouponList=couponList;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public CouponHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_coupon_item,parent,false);
        return new CouponHolder(view);
    }

    @Override
    public void onBindViewHolder(CouponHolder holder, int position) {
        CouponBean recordsBean = mCouponList.get(position);
        holder.nameTv.setText(recordsBean.getName());
        holder.describeTv.setText(mContext.getString(R.string.label_coupon_discount, Util.getNumberFormat(recordsBean.getNeedAmount()),Util.getNumberFormat(recordsBean.getAmount())));
        holder.countTv.setText(mContext.getString(R.string.label_coupon_num,recordsBean.getCirculation(),recordsBean.getConsumption(),recordsBean.getRestriction()));
        holder.productTagTv.setText(recordsBean.getTypeName());
        holder.validTimeTv.setText(mContext.getString(R.string.label_collage_valid_time,recordsBean.getReceiveStartDate().substring(0,11),recordsBean.getReceiveEndDate().substring(0,11)));
        holder.checkProductTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AssignProductActivity.class);
                intent.putExtra("coupon",recordsBean);
                intent.putExtra("isEditCoupon", true);
                ((CouponActivity)mContext).startActivityForResult(intent, Constants.RequestCode.ACTION_COUPON);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddCouponActivity.class);
                intent.putExtra("couponId",recordsBean.getId());
                ((CouponActivity)mContext).startActivityForResult(intent, Constants.RequestCode.ACTION_COUPON);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mCouponList==null?0:mCouponList.size();
    }
}
