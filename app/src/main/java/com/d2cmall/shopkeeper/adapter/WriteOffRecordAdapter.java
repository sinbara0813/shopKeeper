package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.holder.CollageSelectCouponHolder;
import com.d2cmall.shopkeeper.holder.WriteOffRecordHolder;


/**
 * Created by LWJ on 2019/3/4.
 * Description : CollagePromotionAdapter
 */

public class WriteOffRecordAdapter extends DelegateAdapter.Adapter<WriteOffRecordHolder> {

    private Context mContext;

    public WriteOffRecordAdapter(Context context){
        this.mContext=context;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public WriteOffRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_writeoff_record_item,parent,false);
        return new WriteOffRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(WriteOffRecordHolder holder, int position) {
        holder.describeTv.setText("这是商品名称");
        holder.priceTv.setText("¥12345");
        holder.writeoffName.setText("核销时间:12/12");
        holder.writeoffName.setText("核销员:迪尔西");
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
