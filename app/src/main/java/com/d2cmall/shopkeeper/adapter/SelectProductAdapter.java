package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.SelectProductHolder;
import com.d2cmall.shopkeeper.model.CouponBean;
import com.d2cmall.shopkeeper.model.CouponInsertProductBean;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 作者:Created by sinbara on 2019/2/26.
 * 邮箱:hrb940258169@163.com
 */

public class SelectProductAdapter extends DelegateAdapter.Adapter<SelectProductHolder> {

    private Context mContext;
    private RequestManager manager;
    private List<ProductBean> mProductBeanList;
    private CouponBean couponBean;
    private boolean mIsEditCoupon;


    public SelectProductAdapter(Context context, RequestManager manager, List<ProductBean> productBeanList, boolean isEditCoupon){
        this.mContext=context;
        this.manager=manager;
        this.mProductBeanList=productBeanList;
        this.mIsEditCoupon=isEditCoupon;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public SelectProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_select_product_item,parent,false);
        return new SelectProductHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectProductHolder holder, int position) {
        ProductBean productBean = mProductBeanList.get(position);
        ImageLoader.displayImage(manager,productBean.getFirstPic(),holder.iv,R.mipmap.ic_logo_empty5,R.mipmap.ic_logo_empty5);
        holder.describeTv.setText(productBean.getName());
        holder.priceTv.setText(mContext.getString(R.string.label_money, Util.getNumberFormat(productBean.getPrice())));
        if(productBean.getHasCouponSelect()){
            holder.checkIv.setImageResource(R.mipmap.icon_checkbox_all);
        }else{
            holder.checkIv.setImageResource(R.mipmap.icon_checkbox_nor);
        }
        //全场优惠券
        if(couponBean!=null && couponBean.getType()==0 && mIsEditCoupon){
            holder.checkIv.setVisibility(View.GONE);
        }
        holder.checkIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productBean.getHasCouponSelect()){
                    if (mIsEditCoupon){
                        requestUnbindProduct(productBean, holder.checkIv);
                    }else {
                        productBean.setHasCouponSelect(false);
                        holder.checkIv.setImageResource(R.mipmap.icon_checkbox_nor);
                    }
                }else{
                    if (mIsEditCoupon){
                        requestBindProduct(productBean, holder.checkIv);
                    }else {
                        productBean.setHasCouponSelect(true);
                        holder.checkIv.setImageResource(R.mipmap.icon_checkbox_all);
                    }
                }
            }
        });
    }

    private void requestUnbindProduct(ProductBean productBean, ImageView checkIv) {
        if(couponBean==null){
            return;
        }
        checkIv.setEnabled(false);
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("couponId",String.valueOf(couponBean.getId()));
        map.put("productId",String.valueOf(productBean.getId()));
        ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().unbindProduct(map), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(mContext,"解绑成功");
                productBean.setHasCouponSelect(false);
                checkIv.setEnabled(true);
                checkIv.setImageResource(R.mipmap.icon_checkbox_nor);
            }

            @Override
            public void onError(int errorCode, String msg) {
                checkIv.setEnabled(true);
                productBean.setHasCouponSelect(!productBean.getHasCouponSelect());
                super.onError(errorCode, msg);
            }
        });
    }

    private void requestBindProduct(ProductBean productBean, ImageView checkIv) {
        if(couponBean==null){
            return;
        }
        checkIv.setEnabled(false);
            ArrayList<CouponInsertProductBean> couponInsertProductBeans = new ArrayList<>();
                    CouponInsertProductBean couponInsertProductBean = new CouponInsertProductBean();
                    couponInsertProductBean.setProductId(productBean.getId());
                    couponInsertProductBean.setCouponId(couponBean.getId());
                    couponInsertProductBean.setType(couponBean.getType());
                    couponInsertProductBeans.add(couponInsertProductBean);
        ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().couponBindProduct(couponInsertProductBeans), new BaseObserver() {
                @Override
                public void onSuccess(Object o) {
                    Util.showToast(mContext,"绑定成功");
                    productBean.setHasCouponSelect(true);
                    checkIv.setEnabled(true);
                    checkIv.setImageResource(R.mipmap.icon_checkbox_all);
                }
            @Override
            public void onError(int errorCode, String msg) {
                checkIv.setEnabled(true);
                super.onError(errorCode, msg);
            }
            });

    }

    @Override
    public int getItemCount() {
        return mProductBeanList==null?0:mProductBeanList.size();
    }

    public void setCouponBean(CouponBean couponBean) {
        this.couponBean = couponBean;
    }

}
