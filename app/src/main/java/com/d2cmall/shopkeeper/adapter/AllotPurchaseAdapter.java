package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.productHolder;
import com.d2cmall.shopkeeper.model.AllotListBean;
import com.d2cmall.shopkeeper.model.PurchaseItemListBean;
import com.d2cmall.shopkeeper.ui.activity.ProductDetailActivity;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by LWJ on 2019/2/19.
 * Description : AllotPurchaseAdapter
 */

public class AllotPurchaseAdapter extends DelegateAdapter.Adapter<productHolder> {

    private Context mContext;
    private RequestManager manager;
    private List<Object> list;
    private int type;

    public AllotPurchaseAdapter(Context context, RequestManager manager, List<Object> list, int type) {
        this.mContext = context;
        this.manager=manager;
        this.list=list;
        this.type=type;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public productHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_product_item, parent, false);
        return new productHolder(view);
    }

    @Override
    public void onBindViewHolder(productHolder holder, int position) {
        if(list.get(position) instanceof AllotListBean.RecordsBean.AllotItemListBean){
            AllotListBean.RecordsBean.AllotItemListBean itemListBean = (AllotListBean.RecordsBean.AllotItemListBean) list.get(position);
            String[] pics=itemListBean.getProductPic().split(",");
            ImageLoader.displayImage(manager, StringUtil.getD2cPicUrl(getValidPic(pics)),holder.iv);
            holder.infoTv.setText(itemListBean.getProductName());
            holder.priceTv.setText("售价：¥"+ Util.getNumberFormat(itemListBean.getProductPrice()));
            holder.storeTv.setText("件数："+itemListBean.getQuantity());
            holder.reviewTv.setText(itemListBean.getFromShopName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemListBean.getTargetId()!=null && itemListBean.getTargetId()>0){
                        Intent intent=new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("id",itemListBean.getTargetId());
                        ((BaseActivity)mContext).startActivityForResult(intent,100);
                    }
                }
            });
        }else if(list.get(position) instanceof PurchaseItemListBean.RecordsBean){
            PurchaseItemListBean.RecordsBean itemBean = (PurchaseItemListBean.RecordsBean) list.get(position);
            String[] pics=itemBean.getProductPic().split(",");
            ImageLoader.displayImage(manager, StringUtil.getD2cPicUrl(getValidPic(pics)),holder.iv);
            holder.infoTv.setText(itemBean.getProductName());
            holder.priceTv.setText("售价：¥"+ Util.getNumberFormat(itemBean.getProductPrice()));
            holder.storeTv.setText("件数："+itemBean.getQuantity());
            holder.reviewTv.setText(itemBean.getFromShopName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemBean.getTargetId()!=null && itemBean.getTargetId()>0){
                        Intent intent=new Intent(mContext, ProductDetailActivity.class);
                        intent.putExtra("id",itemBean.getTargetId());
                        ((BaseActivity)mContext).startActivityForResult(intent,100);
                    }
                }
            });

        }
        holder.moreIv.setVisibility(View.INVISIBLE);

    }

    private String getValidPic(String[] pic){
        int size=pic.length;
        for (int i=0;i<size;i++){
            if (!StringUtil.isEmpty(pic[i])){
                return pic[i];
            }
        }
        return null;
    }

    private Date parseDate(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

}
