package com.d2cmall.shopkeeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.holder.CustomerDetialOrderHolder;
import com.d2cmall.shopkeeper.model.OrderListBean;
import com.d2cmall.shopkeeper.ui.activity.OrderDetialActivity;
import com.d2cmall.shopkeeper.utils.CenterAlignImageSpan;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

/**
 * Created by LWJ on 2019/2/21.
 * Description : OrderListAdapter
 * 未拆单的列表适配器
 */

public class UndemolitionOrderListAdapter extends DelegateAdapter.Adapter<CustomerDetialOrderHolder> {
    private Context mContext;
    private int mType;//0:行为记录 ,1:订单记录
    private List<OrderListBean> mOrderList;
    public UndemolitionOrderListAdapter(Context context, int type, int orderType, List<OrderListBean> orderList) {
        this.mContext = context;
        this.mType=type;
        this.mOrderList=orderList;
    }
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        return linearLayoutHelper;
    }

    @Override
    public CustomerDetialOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_customer_order_item, parent, false);
        return new CustomerDetialOrderHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerDetialOrderHolder holder, int position) {
        holder.ivProduct.setImageResource(R.mipmap.pic_home_topbg);
        holder.tvProductName.setText(position+"订单");
        holder.tvProductPrice.setText(position+"¥258.00");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, OrderDetialActivity.class).putExtra("orderId",0));
            }
        });
        if(mType==0){
            holder.tvAction.setTextColor(Color.parseColor("#8d92a3"));
            holder.rlBottom.setVisibility(View.GONE);
            holder.tvOrderStatus.setVisibility(View.GONE);
        }else{

            if (position%2==0) {
                StringBuilder builder = new StringBuilder();
                builder.append("   " + "商品名称");
                SpannableString sb = new SpannableString(builder.toString());
                Drawable d = mContext.getResources().getDrawable(R.mipmap.pic_label_pintuan);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                sb.setSpan(new CenterAlignImageSpan(d), 0, 1, ImageSpan.ALIGN_BASELINE);
                holder.tvProductName.setText(sb);
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append("   " + "商品名称");
                SpannableString sb = new SpannableString(builder.toString());
                Drawable d = mContext.getResources().getDrawable(R.mipmap.pic_label_xuni);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                sb.setSpan(new CenterAlignImageSpan(d), 0, 1, ImageSpan.ALIGN_BASELINE);
                holder.tvProductName.setText(sb);
            }
            holder.tvAction.setTextColor(Color.parseColor("#111111"));
            holder.rlBottom.setVisibility(View.VISIBLE);
            holder.tvOrderStatus.setVisibility(View.VISIBLE);
            holder.tvOrderStatus.setText("已完结");
            holder.llButtonContainer.removeAllViews();
            int num=position%3+1;
              for (int i = 0; i <num ; i++) {
                  TextView textView = new TextView(mContext);
                  ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ScreenUtil.dip2px(72),ScreenUtil.dip2px(28));
                  layoutParams.width=ScreenUtil.dip2px(80);
                  layoutParams.height=ScreenUtil.dip2px(32);
                  textView.setLayoutParams(layoutParams);
                  textView.setGravity(Gravity.CENTER);
                  textView.setTextColor(Color.parseColor("#8d92a3"));
                  textView.setText("按钮"+num);
                  textView.setBackgroundResource(R.drawable.sp_line);
                  holder.llButtonContainer.addView(textView);
              }
        }
        holder.tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeliverGoodPop(position);
            }
        });
    }

    private void showEditPricePop(int position) {
        View popView = LayoutInflater.from(mContext).inflate(R.layout.layout_edit_order_price_pop, null);
        EditText etTotalAmount = popView.findViewById(R.id.et_total_amount);
        TextView tvSure = popView.findViewById(R.id.tv_sure);
        LinearLayout llRoot = popView.findViewById(R.id.ll_root);
        LinearLayout llContent = popView.findViewById(R.id.ll_content);
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        TextView tvDiscountAmount = popView.findViewById(R.id.tv_discount_amount);
        tvDiscountAmount.setText(mContext.getString(R.string.label_discount_amount,33.0));
        TextView tvActual = popView.findViewById(R.id.tv_actual_amount);
        tvActual.setText(mContext.getString(R.string.label_actual_amount,33.0));
        etTotalAmount.setText("66.0");
        PopupWindow popWindow = new PopupWindow(popView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.setFocusable(true);
        popWindow.showAtLocation(((Activity)mContext).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isEmpty(etTotalAmount.getText().toString().trim()) || Double.valueOf(etTotalAmount.getText().toString().trim())<0){
                    Util.showToast(mContext,"请输入正确的金额");
                    return;
                }
                editPrice(position,Util.getNumberFormat(Double.valueOf(etTotalAmount.getText().toString().trim())));
                if(popWindow!=null && popWindow.isShowing()){
                    popWindow.dismiss();
                }
            }
        });
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popWindow!=null && popWindow.isShowing()){
                    popWindow.dismiss();
                }
            }
        });
    }

    private void editPrice(int position, String newPrice) {
        ArrayMap<String, String> hashMap = new ArrayMap<>();
        hashMap.put("orderId",String.valueOf(mOrderList.get(position).getRecords().get(0).getId()));
        hashMap.put("productAmount",newPrice);
        ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().updateOrderAmount(hashMap), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(mContext,"修改成功");
            }
        });
    }

    private void showDeliverGoodPop(int position) {
        View popView = LayoutInflater.from(mContext).inflate(R.layout.layout_deliver_info_pop, null);
        LinearLayout llRoot = popView.findViewById(R.id.ll_root);
        LinearLayout llContent = popView.findViewById(R.id.ll_content);
        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
        ImageView ivScan = popView.findViewById(R.id.iv_scan);
        ImageView ivDeliverList = popView.findViewById(R.id.iv_deliver_company_list);
        TextView tvSure = popView.findViewById(R.id.tv_sure);
        TextView tvDeliver = popView.findViewById(R.id.tv_deliver);
        tvDeliver.setText("百世汇通");
        EditText etDeliverCode = popView.findViewById(R.id.et_deliver_code);
        etDeliverCode.setText("123456");
        PopupWindow popWindow = new PopupWindow(popView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.setFocusable(true);
        popWindow.showAtLocation(((Activity)mContext).getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isEmpty(etDeliverCode.getText().toString().trim())){
                    Util.showToast(mContext,"请输入正确的快递单号");
                    return;
                }
                if(StringUtil.isEmpty(tvDeliver.getText().toString().trim())){
                    Util.showToast(mContext,"请选择快递公司");
                    return;
                }
                deliverGood(position,etDeliverCode.getText().toString().trim(),tvDeliver.getText().toString().trim());
                if(popWindow!=null && popWindow.isShowing()){
                    popWindow.dismiss();
                }
            }
        });
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popWindow!=null && popWindow.isShowing()){
                    popWindow.dismiss();
                }
            }
        });
    }

    private void deliverGood(int position, String code, String name) {
        ArrayMap<String, String> hashMap = new ArrayMap<>();
        hashMap.put("orderItemId",String.valueOf(mOrderList.get(position).getRecords().get(0).getId()));
        hashMap.put("logisticsCom",name);
        hashMap.put("logisticsNum",code);
        ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().deliverGood(hashMap), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(mContext,"发货成功");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderList==null?0:mOrderList.size();
    }
}
