package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.holder.CartItemHolder;
import com.d2cmall.shopkeeper.listener.CheckStateChangeListener;
import com.d2cmall.shopkeeper.model.CartBean;
import com.d2cmall.shopkeeper.ui.activity.MarketProductDetialActivity;
import com.d2cmall.shopkeeper.ui.view.CartProductItemView;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 作者:Created by sinbara on 2019/4/30.
 * 邮箱:hrb940258169@163.com
 */

public class CartAdapter extends DelegateAdapter.Adapter<CartItemHolder> {

    private Context mContext;
    private boolean isEdit;
    private List<List<CartBean>> cartList;

    public CartAdapter(Context context,List<List<CartBean>> cartList){
        this.mContext=context;
        this.cartList=cartList;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public CartItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_cart_item,parent,false);
        return new CartItemHolder(view);
    }

    @Override
    public void onBindViewHolder(CartItemHolder holder, int position) {
        holder.shopNameTv.setText(cartList.get(position).get(0).getShop().getName());
        setData(holder.contentLl,cartList.get(position));
        if (isEdit){
            holder.group.setVisibility(View.GONE);
            holder.deleteIv.setVisibility(View.VISIBLE);
        }else {
            holder.group.setVisibility(View.VISIBLE);
            holder.deleteIv.setVisibility(View.GONE);
        }
        holder.totalPriceTv.setText(getSumTex(calculateSum(position)));
        holder.buyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ids=getIds(holder.contentLl,2);
                if (StringUtil.isEmpty(ids)){
                    Util.showToast(mContext,"请选择至少一项");
                    return;
                }
                Action action=new Action(Constants.ActionType.CART_TO_PURCHASE);
                action.put("ids",ids);
                EventBus.getDefault().post(action);
            }
        });
        holder.freeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ids=getIds(holder.contentLl,1);
                if (StringUtil.isEmpty(ids)){
                    Util.showToast(mContext,"请选择至少一项");
                    return;
                }
                Action action=new Action(Constants.ActionType.CART_TO_ALLOT);
                action.put("ids",ids);
                EventBus.getDefault().post(action);
            }
        });
        holder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ids=getIds(holder.contentLl,0);
                if (StringUtil.isEmpty(ids)){
                    Util.showToast(mContext,"请选择至少一项");
                    return;
                }
                Action action=new Action(Constants.ActionType.CART_DELETE);
                action.put("ids",ids);
                EventBus.getDefault().post(action);
            }
        });
        holder.selectAllIv.setOnStatusChangedListener(new CheckStateChangeListener() {
            @Override
            public void onStatusChanged(View v, boolean isChecked) {
                int childCount=holder.contentLl.getChildCount();
                for (int i=0;i<childCount;i++){
                    if (holder.contentLl.getChildAt(i) instanceof CartProductItemView){
                        ((CartProductItemView)(holder.contentLl.getChildAt(i))).setCheck(isChecked);
                    }
                }
                holder.totalPriceTv.setText(getSumTex(calculateSum(position)));
            }
        });
    }

    private double calculateSum(int position){
        double result=0;
        int size=cartList.get(position).size();
        for (int i=0;i<size;i++){
            if (cartList.get(position).get(i).isChecked()){
                result+=cartList.get(position).get(i).getProductPrice()*cartList.get(position).get(i).getQuantity();
            }
        }
        return result;
    }

    private SpannableString getSumTex(double price){
        StringBuilder builder=new StringBuilder();
        builder.append("合计 ").append("¥").append(Util.getNumberFormat(price));
        SpannableString sb=new SpannableString(builder.toString());
        ForegroundColorSpan colorSpan=new ForegroundColorSpan(Color.parseColor("#111111"));
        sb.setSpan(colorSpan,0,2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        RelativeSizeSpan sizeSpan=new RelativeSizeSpan(0.85f);
        sb.setSpan(sizeSpan,0,2, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return sb;
    }

    private String getIds(LinearLayout content,int type){
        int childCount=content.getChildCount();
        StringBuilder builder=new StringBuilder();
        for (int i=0;i<childCount;i++){
            CartProductItemView itemView= (CartProductItemView) content.getChildAt(i);
            if (itemView.isChecked()){
                switch (type){
                    case 0:
                        builder.append(((CartProductItemView)(content.getChildAt(i))).getCartId()).append(",");
                        break;
                    case 1:
                        if (itemView.isAllot()){
                            builder.append(((CartProductItemView)(content.getChildAt(i))).getCartId()).append(",");
                        }
                        break;
                    case 2:
                        if (itemView.isPurchase()){
                            builder.append(((CartProductItemView)(content.getChildAt(i))).getCartId()).append(",");
                        }
                        break;
                }
            }
        }
        String ids=builder.toString();
        if (!StringUtil.isEmpty(ids)){
            ids=ids.substring(0,ids.length()-1);
        }
        Log.e("han","ids=="+ids);
        return ids;
    }

    private void setData(LinearLayout contentLl,List<CartBean> data) {
        contentLl.removeAllViews();
        boolean hideLine=false;
        for (int i = 0; i < data.size(); i++) {
            if (i==data.size()-1){
                hideLine=true;
            }
            addView(contentLl,data.get(i),hideLine);
        }
    }

    private void addView(LinearLayout contentLl,CartBean cartBean,boolean hideLine) {
        CartProductItemView productItem=new CartProductItemView(mContext);
        productItem.setEdit(isEdit);
        productItem.setData(cartBean);
        productItem.hideLine(hideLine);
        contentLl.addView(productItem);
    }

    public void isEdit(boolean isEdit){
        this.isEdit=isEdit;
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
