package com.d2cmall.shopkeeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.OfflineCartProductHolder;
import com.d2cmall.shopkeeper.model.CartBean;
import com.d2cmall.shopkeeper.model.EmptyBean;
import com.d2cmall.shopkeeper.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者:Created by sinbara on 2019/5/7.
 * 邮箱:hrb940258169@163.com
 */

public class OfflineCartProductAdapter extends DelegateAdapter.Adapter<OfflineCartProductHolder> {

    private Context mContext;
    private List<CartBean> cartBeans;

    public OfflineCartProductAdapter(Context context,List<CartBean> cartBeans){
        this.mContext=context;
        this.cartBeans=cartBeans;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public OfflineCartProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_offline_cart_product_item,parent,false);
        return new OfflineCartProductHolder(view);
    }

    @Override
    public void onBindViewHolder(OfflineCartProductHolder holder, int position) {
        CartBean bean=cartBeans.get(position);
        ImageLoader.displayImage((Activity)mContext,bean.getProductPic(),holder.iv);
        holder.nameTv.setText(bean.getProductName());
        holder.storeTv.setText("规格: "+bean.getStandard());
        holder.priceTv.setText("¥"+Util.getNumberFormat(bean.getProductPrice()));
        holder.numEt.setText(String.valueOf(bean.getQuantity()));
        holder.numEt.setCursorVisible(false);
        holder.numEt.setEnabled(false);
        holder.numEt.setTextColor(mContext.getResources().getColor(R.color.color_black85));
        holder.addIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNum(holder.numEt,bean,1);
            }
        });
        holder.minusIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNum(holder.numEt,bean,-1);
            }
        });
    }

    private void changeNum(EditText editText,CartBean bean,int type){
        int current=Integer.valueOf(editText.getText().toString());
        if (type>0){
            current++;
        }else {
            current--;
        }
        int finalCurrent = current;
        if (finalCurrent>0){
            ApiRetrofit.getInstance().getApiService().cartUpdate(bean.getId(),current).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<BaseModel<EmptyBean>>() {
                        @Override
                        public void onSuccess(BaseModel<EmptyBean> o) {
                            bean.setQuantity(finalCurrent);
                            editText.setText(String.valueOf(finalCurrent));
                            EventBus.getDefault().post(new Action(Constants.ActionType.CART_REFRESH));
                        }
                    });
        }else {
            ApiRetrofit.getInstance().getApiService().cartDelete(String.valueOf(bean.getId())).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<BaseModel<EmptyBean>>() {
                        @Override
                        public void onSuccess(BaseModel<EmptyBean> o) {
                            cartBeans.remove(bean);
                            notifyDataSetChanged();
                            EventBus.getDefault().post(new Action(Constants.ActionType.CART_REFRESH));
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return cartBeans.size();
    }
}
