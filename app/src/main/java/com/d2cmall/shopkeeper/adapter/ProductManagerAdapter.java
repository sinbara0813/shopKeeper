package com.d2cmall.shopkeeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.productHolder;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.activity.ProductDetailActivity;
import com.d2cmall.shopkeeper.utils.CenterAlignImageSpan;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by LWJ on 2019/2/19.
 * Description : ProductManagerAdapter
 */

public class ProductManagerAdapter extends DelegateAdapter.Adapter<productHolder> {

    private Context mContext;
    private RequestManager manager;
    private List<ProductBean> list;
    private PopListener popListener;

    public ProductManagerAdapter(Context context,RequestManager manager, List<ProductBean> list) {
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
        String[] pics=list.get(position).getPic().split(",");
        ImageLoader.displayImage(manager, StringUtil.getD2cPicUrl(getValidPic(pics)),holder.iv);
        if(list.get(position).getCrowd()==1 && !StringUtil.isEmpty(list.get(position).getCrowdEndDate()) && parseDate(list.get(position).getCrowdEndDate()).getTime()>System.currentTimeMillis()){
            Drawable d;
            d = mContext.getResources().getDrawable(R.mipmap.pic_label_pintuan);
            StringBuilder builder = new StringBuilder();
            builder.append("   " + list.get(position).getName());
            SpannableString sb = new SpannableString(builder.toString());
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            sb.setSpan(new CenterAlignImageSpan(d), 0, 1, ImageSpan.ALIGN_BASELINE);
            holder.infoTv.setText(sb);
        }else{
            holder.infoTv.setText(list.get(position).getName());
        }
        holder.storeTv.setText("库存："+list.get(position).getStock()+"   销量: "+list.get(position).getSales());
        if (list.get(position).getAllot()==0){
            holder.reviewTv.setVisibility(View.GONE);
            holder.priceTv.setText("售价: ¥"+ Util.getNumberFormat(list.get(position).getPrice()));
        }else {
            if (!StringUtil.isEmpty(list.get(position).getFromShopName())){
                holder.reviewTv.setVisibility(View.VISIBLE);
                holder.reviewTv.setText("供应商: "+list.get(position).getFromShopName());
            }
            holder.priceTv.setText("售价: ¥ "+Util.getNumberFormat(list.get(position).getPrice())+"   进价: ¥ "+Util.getNumberFormat(list.get(position).getSupplyPrice()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("status",list.get(position).getStatus());
                intent.putExtra("id",list.get(position).getId());
                ((BaseActivity)mContext).startActivityForResult(intent,100);
            }
        });
        holder.moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popView = LayoutInflater.from(mContext).inflate(R.layout.layout_product_action_pop, null);
                TextView tvCancle = popView.findViewById(R.id.tv_cancle);
                TextView tvEdit = popView.findViewById(R.id.tv_edit);
                TextView tvShare = popView.findViewById(R.id.tv_share);
                TextView tvLowerShelf = popView.findViewById(R.id.tv_lower_shelf);
                if (list.get(position).getStatus()==0){
                    tvLowerShelf.setText("上架");
                }else {
                    tvLowerShelf.setText("下架");
                }
                TextView tvDelet = popView.findViewById(R.id.tv_delete);
                PopupWindow popWindow = new PopupWindow(popView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                popWindow.setBackgroundDrawable(new ColorDrawable());
                popWindow.showAtLocation(((Activity)mContext).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                tvCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(popWindow!=null && popWindow.isShowing()){
                            popWindow.dismiss();
                        }
                    }
                });
                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(popWindow!=null && popWindow.isShowing()){
                            popWindow.dismiss();
                            if (popListener!=null){
                                popListener.edit(list.get(position));
                            }
                        }
                    }
                });
                tvShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(popWindow!=null && popWindow.isShowing()){
                            popWindow.dismiss();
                            if (popListener!=null){
                                popListener.share(list.get(position));
                            }
                        }
                    }
                });
                tvLowerShelf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(popWindow!=null && popWindow.isShowing()){
                            popWindow.dismiss();
                            if (popListener!=null){
                                popListener.sold(list.get(position).getId(),list.get(position).getStatus());
                            }
                        }
                    }
                });
                tvDelet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(popWindow!=null && popWindow.isShowing()){
                            popWindow.dismiss();
                            if (popListener!=null){
                                popListener.delete(list.get(position).getId());
                            }
                        }
                    }
                });
            }
        });
    }

    public PopListener getPopListener() {
        return popListener;
    }

    public void setPopListener(PopListener popListener) {
        this.popListener = popListener;
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

    public interface PopListener{
        void sold(long id,int status);
        void edit(ProductBean bean);
        void share(ProductBean bean);
        void delete(long id);
    }
}
