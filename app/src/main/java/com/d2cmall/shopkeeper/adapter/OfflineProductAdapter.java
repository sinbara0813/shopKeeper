package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.OfflineProductHolder;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.activity.ProductDetailActivity;
import com.d2cmall.shopkeeper.ui.view.SelectStandardPop;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者:Created by sinbara on 2019/5/7.
 * 邮箱:hrb940258169@163.com
 */

public class OfflineProductAdapter extends DelegateAdapter.Adapter<OfflineProductHolder> {

    private Context mContext;
    private RequestManager manager;
    private List<ProductBean> list;

    public OfflineProductAdapter(Context context, RequestManager requestManager, List<ProductBean> list){
        this.mContext = context;
        this.manager=requestManager;
        this.list=list;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public OfflineProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_offline_product_item,parent,false);
        return new OfflineProductHolder(view);
    }

    @Override
    public void onBindViewHolder(OfflineProductHolder holder, int position) {
        ImageLoader.displayImage(manager,list.get(position).getFirstPic(),holder.iv);
        holder.nameTv.setText(list.get(position).getName());
        holder.priceTv.setText("售价: ¥"+ Util.getNumberFormat(list.get(position).getPrice()));
        holder.storeTv.setText("库存: "+list.get(position).getStock());
        if (!StringUtil.isEmpty(list.get(position).getFromShopName())){
            holder.provideTv.setVisibility(View.VISIBLE);
            holder.provideTv.setText(list.get(position).getFromShopName());
        }else {
            holder.provideTv.setVisibility(View.GONE);
        }
        holder.addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStandardPop(v,list.get(position));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("id",list.get(position).getId());
                intent.putExtra("status",list.get(position).getStatus());
                mContext.startActivity(intent);
            }
        });
    }

    public void showStandardPop(View v,ProductBean productBean){
        ApiRetrofit.getInstance().getApiService().getProduct(productBean.getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseModel<ProductBean>>() {
                    @Override
                    public void onSuccess(BaseModel<ProductBean> o) {
                        SelectStandardPop pop=new SelectStandardPop(mContext);
                        pop.setOffline(true);
                        pop.setData(o.getData());
                        pop.show(v);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
