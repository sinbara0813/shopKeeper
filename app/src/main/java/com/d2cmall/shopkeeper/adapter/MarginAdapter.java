package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.holder.MarginHolder;
import com.d2cmall.shopkeeper.model.MarginBean;
import com.d2cmall.shopkeeper.utils.DateUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

/**
 * 作者:Created by sinbara on 2019/6/18.
 * 邮箱:hrb940258169@163.com
 */
public class MarginAdapter extends DelegateAdapter.Adapter<MarginHolder> {

    private Context mContext;
    private List<MarginBean.RecordsBean> data;

    public MarginAdapter(Context context,List<MarginBean.RecordsBean> data){
        this.mContext=context;
        this.data=data;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public MarginHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_margin_item,viewGroup,false);
        return new MarginHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarginHolder marginHolder, int i) {
        MarginBean.RecordsBean bean=data.get(i);
        marginHolder.cashTv.setText(bean.getAmount()>0?"+"+ Util.getNumberFormat(bean.getAmount())+"元":Util.getNumberFormat(bean.getAmount())+"元");
        marginHolder.proposerTv.setText("申请人: "+bean.getCreateMan());
        marginHolder.dateTv.setText("时间: "+ bean.getCreateDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
