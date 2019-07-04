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
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.productHolder;
import com.d2cmall.shopkeeper.model.AllotListBean;
import com.d2cmall.shopkeeper.ui.activity.AllotDetialActivity;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by LWJ on 2019/4/26.
 * Description : 进货管理商品列表适配器
 */

public class StockPurchaseAdapter extends DelegateAdapter.Adapter<productHolder> {

    private Context mContext;
    private RequestManager manager;
    private List<AllotListBean.RecordsBean> list;

    public StockPurchaseAdapter(Context context, RequestManager manager, List<AllotListBean.RecordsBean> list) {
        this.mContext = context;
        this.manager=manager;
        this.list=list;
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
        AllotListBean.RecordsBean.AllotItemListBean allotItemListBean = list.get(position).getAllotItemList().get(0);
        if(!StringUtil.isEmpty(allotItemListBean.getProductPic())){
            String[] pics=allotItemListBean.getProductPic().split(",");
            ImageLoader.displayImage(manager, StringUtil.getD2cPicUrl(getValidPic(pics)),holder.iv);
        }else{
            ImageLoader.displayImage(manager, "",holder.iv);
        }
        holder.infoTv.setText(allotItemListBean.getProductName());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("进价：¥");
        stringBuilder.append(Util.getNumberFormat(allotItemListBean.getSupplyPrice()));
        holder.priceTv.setText(stringBuilder.toString());
        holder.storeTv.setText("库存："+allotItemListBean.getQuantity());
//        holder.reviewTv.setText("供应商: D2C");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AllotDetialActivity.class).putExtra("orderId", allotItemListBean.getId());
                mContext.startActivity(intent);
            }
        });
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
