package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.holder.ProductSettleItemHolder;
import com.d2cmall.shopkeeper.model.AllotSettleBean;
import com.d2cmall.shopkeeper.model.AllotSkuBean;
import com.d2cmall.shopkeeper.model.EmptyBean;
import com.d2cmall.shopkeeper.ui.activity.ProductSettleActivity;
import com.d2cmall.shopkeeper.ui.view.CartProductItemView;
import com.d2cmall.shopkeeper.ui.view.CheckBox;
import com.d2cmall.shopkeeper.ui.view.PayPop;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者:Created by sinbara on 2019/5/6.
 * 邮箱:hrb940258169@163.com
 */

public class ProductSettleAdapter extends DelegateAdapter.Adapter<ProductSettleItemHolder> {

    private Context mContext;
    private PayPop payPop;
    private List<List<AllotSkuBean>> lists;

    public ProductSettleAdapter(Context context, List<List<AllotSkuBean>> lists){
        this.mContext=context;
        this.lists=lists;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public ProductSettleItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_product_settle_item,parent,false);
        return new ProductSettleItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductSettleItemHolder holder, int position) {
        holder.titleTv.setText(lists.get(position).get(0).getFromShopId()+"");
        setData(holder.contentLl,lists.get(position));
        double allPrice=calculatePrice(lists.get(position));
        StringBuilder builder=new StringBuilder();
        builder.append("合计  ¥").append(Util.getNumberFormat(allPrice));
        holder.priceTv.setText(Util.buildSpan(new int[]{mContext.getResources().getColor(R.color.black111111)},new int[]{2},new float[]{0.77f},new int[]{5},builder.toString()));
        holder.cashDepositTv.setText("结算后返还保证金"+ Util.getNumberFormat(allPrice)+"元");
        holder.settleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayPop(v,lists.get(position).get(0).getFromShopId());
            }
        });
    }

    private double calculatePrice(List<AllotSkuBean> list){
        double result=0;
        int size=list.size();
        for (int i=0;i<size;i++){
            result+=list.get(i).getSupplyPrice()*list.get(i).getSettleStock();
        }
        return result;
    }

    private void showPayPop(View v,long shopId){
        ApiRetrofit.getInstance().getApiService().allotStatementCreate(shopId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseModel<AllotSettleBean>>() {
                    @Override
                    public void onSuccess(BaseModel<AllotSettleBean> o) {
                        if (o.getData()!=null){
                            if (mContext instanceof ProductSettleActivity){
                                ((ProductSettleActivity)mContext).cash=o.getData().getPayAmount();
                            }
                            if (payPop!=null){
                                payPop=null;
                            }
                            payPop=new PayPop(mContext);
                            payPop.setOrderId(String.valueOf(o.getData().getId()));
                            payPop.setOrderSn(o.getData().getSn());
                            payPop.setPrice(o.getData().getPayAmount());
                            payPop.setExpireDate(o.getData().getExpireDate());
                            payPop.setExpireTime(o.getData().getExpireMinute());
                            payPop.show(v);
                        }
                    }
                });
    }

    private void setData(LinearLayout contentLl,List<AllotSkuBean> data) {
        contentLl.removeAllViews();
        boolean hideLine=false;
        for (int i = 0; i < data.size(); i++) {
            if (i==data.size()-1){
                hideLine=true;
            }
            addView(contentLl,data.get(i),hideLine);
        }
    }

    private void addView(LinearLayout contentLl,AllotSkuBean cartBean,boolean hideLine) {
        CartProductItemView itemView=new CartProductItemView(mContext);
        itemView.hideCheckBox();
        itemView.hideLine(hideLine);
        itemView.setOffline();
        itemView.setData(cartBean);
        contentLl.addView(itemView);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
