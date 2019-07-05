package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.holder.ProductTemplateHolder;

import java.util.List;

/**
 * 作者:Created by sinbara on 2019/7/5.
 * 邮箱:hrb940258169@163.com
 */
public class ProductTemplateAdapter extends DelegateAdapter.Adapter<ProductTemplateHolder> {

    private Context mContext;
    private List<Object> list;

    public ProductTemplateAdapter(Context context,List<Object> list){
        this.mContext=context;
        this.list=list;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public ProductTemplateHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_product_template_item,viewGroup,false);
        return new ProductTemplateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTemplateHolder productTemplateHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
