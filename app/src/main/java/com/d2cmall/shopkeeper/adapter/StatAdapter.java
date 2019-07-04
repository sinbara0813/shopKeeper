package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.holder.StatHolder;
import com.d2cmall.shopkeeper.model.StatBean;
import com.d2cmall.shopkeeper.ui.activity.ShopStatActivity;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

/**
 * 作者:Created by sinbara on 2019/6/21.
 * 邮箱:hrb940258169@163.com
 */
public class StatAdapter extends DelegateAdapter.Adapter<StatHolder> {

    private Context context;
    private List<StatBean> data;

    public StatAdapter(Context context, List<StatBean> data){
        this.context=context;
        this.data=data;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public StatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_stat_item,viewGroup,false);
        return new StatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatHolder statHolder, int i) {
        if (i%2==0){
            statHolder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }else {
            statHolder.itemView.setBackgroundColor(Color.parseColor("#f2f8fd"));
        }
        StatBean bean=data.get(i);
        statHolder.nameTv.setText(bean.getShopName());
        statHolder.orderTv.setText(bean.getOrderCount()+"");
        statHolder.peopleTv.setVisibility(View.VISIBLE);
        statHolder.sellTv.setVisibility(View.VISIBLE);
        statHolder.productNumTv.setVisibility(View.VISIBLE);
        statHolder.peopleTv.setText(bean.getBuyCount()+"");
        statHolder.sellTv.setText(Util.getNumberFormat(bean.getTurnover()));
        statHolder.productNumTv.setText(bean.getPayCount()+"");
        statHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ShopStatActivity.class);
                intent.putExtra("shopId",bean.getShopId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
