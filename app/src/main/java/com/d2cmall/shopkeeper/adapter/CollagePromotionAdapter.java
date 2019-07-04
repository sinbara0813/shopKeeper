package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.CollagePromotionHolder;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.activity.AddCollageCouponActivity;
import com.d2cmall.shopkeeper.ui.activity.AddCollageProductActivity;
import com.d2cmall.shopkeeper.ui.activity.CollagePromotionActivity;
import com.d2cmall.shopkeeper.ui.activity.WebActivity;
import com.d2cmall.shopkeeper.utils.CenterAlignImageSpan;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by LWJ on 2019/3/1.
 * Description : CollagePromotionAdapter
 */

public class CollagePromotionAdapter extends DelegateAdapter.Adapter<CollagePromotionHolder> {

    private Context mContext;
    private RequestManager manager;
    private List<ProductBean> mProductBeanList;
    private int mType;

    public CollagePromotionAdapter(Context context,RequestManager manager, List<ProductBean> productBeanList, int type) {
        this.mContext = context;
        this.manager=manager;
        this.mProductBeanList = productBeanList;
        this.mType=type;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public CollagePromotionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_collage_promotion_item, parent, false);
        return new CollagePromotionHolder(view);
    }

    @Override
    public void onBindViewHolder(CollagePromotionHolder holder, int position) {
        ProductBean productBean = mProductBeanList.get(position);
        StringBuilder builder = new StringBuilder();
        if (!StringUtil.isEmpty(productBean.getName())) {
            builder.append("   " + productBean.getName());
        }
        ImageLoader.displayImage(manager, productBean.getFirstPic(), holder.iv, R.mipmap.ic_logo_empty5,R.mipmap.ic_logo_empty5);
        Drawable d;
        if(productBean.getVirtual()>0){
            holder.tvPreview.setVisibility(View.GONE);
            d = mContext.getResources().getDrawable(R.mipmap.pic_label_yhq);
        }else{
            holder.tvPreview.setVisibility(View.VISIBLE);
            d = mContext.getResources().getDrawable(R.mipmap.pic_label_sp);
        }
        SpannableString sb = new SpannableString(builder.toString());
        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        sb.setSpan(new CenterAlignImageSpan(d), 0, 1, ImageSpan.ALIGN_BASELINE);
        holder.nameTv.setText(sb);
        if(mType==1){//进行中
            holder.tvEdit.setVisibility(View.VISIBLE);
            if(productBean.getStatus()==1){
                holder.tvEdit.setText("下架");
            }else{
                holder.tvEdit.setText("上架");
            }
        }else if(mType==0){//未开始
            holder.tvEdit.setVisibility(View.VISIBLE);
            holder.tvEdit.setText("编辑");
        }else{//已结束
            holder.tvEdit.setVisibility(View.GONE);
        }

        holder.tvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("title",productBean.getName());
                intent.putExtra("url",Constants.SHARE_URL+"product/"+productBean.getId());
                intent.putExtra("productId",productBean.getId());
                intent.putExtra("productImage",productBean.getFirstPic());
                intent.putExtra("isShareShow",true);
                mContext.startActivity(intent);
            }
        });

        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mType==1){//进行中
                    productStatus(productBean,holder.tvEdit);
                }else if(mType==0){//未开始
                   toEdit(productBean);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //编辑
                toEdit(productBean);
            }
        });
        if(productBean.getCrowd()==1){
            if(!StringUtil.isEmpty(productBean.getCrowdEndDate()) && parseDate(productBean.getCrowdEndDate())!=null && parseDate(productBean.getCrowdEndDate()).getTime()> System.currentTimeMillis()){
                holder.tvInvalid.setVisibility(View.VISIBLE);
                holder.tvInvalid.setText("使失效");
            }else{
                holder.tvInvalid.setVisibility(View.GONE);
            }
        }else{
            if(!StringUtil.isEmpty(productBean.getCrowdEndDate()) && parseDate(productBean.getCrowdEndDate())!=null && parseDate(productBean.getCrowdEndDate()).getTime()> System.currentTimeMillis()){
                holder.tvInvalid.setVisibility(View.VISIBLE);
                holder.tvInvalid.setText("使生效");
            }else{
                holder.tvInvalid.setVisibility(View.GONE);
            }
        }

        holder.tvInvalid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCrowdStatus(position);
            }
        });
        holder.tvPrice.setText(mContext.getString(R.string.label_collage_product_price, Util.getNumberFormat(productBean.getCrowdPrice()), Util.getNumberFormat(productBean.getPrice())));
        if(!StringUtil.isEmpty(productBean.getCrowdStartDate()) && !StringUtil.isEmpty(productBean.getCrowdEndDate())){
            holder.validTimeTv.setText(mContext.getString(R.string.label_collage_valid_time3, productBean.getCrowdStartDate().substring(0, 11), productBean.getCrowdEndDate().substring(0, 11)));
        }
        holder.tvCollageNum.setText("成团人数: "+productBean.getCrowdGroupNum());
        holder.describeTv.setText(mContext.getString(R.string.label_collage_sucess_time, productBean.getCrowdGroupTime()));
    }

    private void toEdit(ProductBean productBean) {
        if (productBean.getVirtual() <= 0) {//拼团商品
            Intent intent = new Intent(mContext, AddCollageProductActivity.class);
            intent.putExtra("productId", productBean.getId());
            intent.putExtra("promotionType", mType);
            ((CollagePromotionActivity)mContext).startActivityForResult(intent, Constants.RequestCode.ADD_COLLAGE_PRODUCT);
        } else {//拼团优惠券
            Intent intent = new Intent(mContext, AddCollageCouponActivity.class);
            intent.putExtra("productId", productBean.getId());
            intent.putExtra("editCouponId", productBean.getCouponId());
            intent.putExtra("promotionType", mType);
            ((CollagePromotionActivity)mContext).startActivityForResult(intent, Constants.RequestCode.ADD_COLLAGE_COUPON);
        }
    }

    private void productStatus(ProductBean productBean, TextView tvEdit){//上下架
        tvEdit.setEnabled(false);
        int status = productBean.getStatus();
        ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().productStatus(productBean.getId(), status==0?1:0), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                tvEdit.setEnabled(true);
                productBean.setStatus(status==0?1:0);
                if (status == 0) {
                    Util.showToast(mContext, "上架成功");
                    tvEdit.setText("下架");
                } else {
                    Util.showToast(mContext, "下架成功");
                    tvEdit.setText("上架");
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                tvEdit.setEnabled(true);
                super.onError(errorCode, msg);
            }
        });
    }

    private void changeCrowdStatus(int position) {
        ProductBean productBean = mProductBeanList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        int crowdStatus=productBean.getCrowd();
        if(productBean.getCrowd()==1){
            builder.setMessage("确定取消这件商品的拼团活动?");
            crowdStatus=-1;
        }else{
            if(!StringUtil.isEmpty(productBean.getCrowdEndDate()) && parseDate(productBean.getCrowdEndDate())!=null && parseDate(productBean.getCrowdEndDate()).getTime()> System.currentTimeMillis()){
               if(!StringUtil.isEmpty(productBean.getCrowdStartDate()) && parseDate(productBean.getCrowdStartDate())!=null && parseDate(productBean.getCrowdStartDate()).getTime()> System.currentTimeMillis()){
                   //未开始
                   builder.setMessage("确定将这件商品添加到未开始?");
                   crowdStatus=1;
               }else if(!StringUtil.isEmpty(productBean.getCrowdStartDate()) && parseDate(productBean.getCrowdStartDate())!=null && parseDate(productBean.getCrowdStartDate()).getTime() <= System.currentTimeMillis()){
                   //进行中
                   builder.setMessage("确定将这件商品添加到进行中?");
                   crowdStatus=1;
               }
            }
        }
        int finalCrowdStatus = crowdStatus;
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayMap<String, String> map = new ArrayMap<>();
                        map.put("crowd",String.valueOf(finalCrowdStatus));
                        map.put("id",String.valueOf(productBean.getId()));
                        ((BaseActivity) mContext).addDisposable(ApiRetrofit.getInstance().getApiService().changeCrowdStatus(map), new BaseObserver() {
                            @Override
                            public void onSuccess(Object o) {
                                Util.showToast(mContext, "设置成功");
                                mProductBeanList.remove(position);
                                if(mContext instanceof CollagePromotionActivity){
                                    ((CollagePromotionActivity)mContext).notifyFragment();
                                }
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onError(int errorCode, String msg) {
                                super.onError(errorCode, msg);
                            }
                        });
                    }
                }).setNegativeButton("取消",null)
                .show();
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
        return mProductBeanList == null ? 0 : mProductBeanList.size();
    }
}
