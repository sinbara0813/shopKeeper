package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.CustomerDetialOrderHolder;
import com.d2cmall.shopkeeper.model.BrowseListBean;
import com.d2cmall.shopkeeper.model.OrderDetialBean;
import com.d2cmall.shopkeeper.model.OrderListBean;
import com.d2cmall.shopkeeper.ui.activity.OrderDetialActivity;
import com.d2cmall.shopkeeper.ui.activity.WebActivity;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

/**
 * Created by LWJ on 2019/2/21.
 * Description : OrderListAdapter
 */

public class ReshipRefundAdapter extends DelegateAdapter.Adapter<CustomerDetialOrderHolder> {
    private Context mContext;
    private RequestManager manager;
    private int mType;//0:行为记录 ,1:订单记录
    private List<OrderDetialBean.OrderItemListBean> mOrderList;
    private boolean buttonsGone;

    public ReshipRefundAdapter(Context context, RequestManager manager, int type , List<OrderDetialBean.OrderItemListBean> orderList) {
        this.mContext = context;
        this.manager=manager;
        this.mOrderList = orderList;
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
        if (mOrderList==null || mOrderList.get(position)==null)return;
        OrderDetialBean.OrderItemListBean orderBean=mOrderList.get(position);//数据源
        holder.tvAction.setTextColor(Color.parseColor("#111111"));
        holder.rlBottom.setVisibility(View.VISIBLE);
        holder.tvOrderStatus.setVisibility(View.VISIBLE);
        holder.tvOrderStatus.setText(orderBean.getStatusName());
        holder.tvAction.setText(mContext.getString(R.string.label_order_code,orderBean.getOrderSn()));
        holder.tvProductName.setText(orderBean.getProductName());
        holder.tvProductSpec.setText(orderBean.getStandard());
        holder.tvProductPrice.setText(mContext.getString(R.string.label_money,Util.getNumberFormat(orderBean.getRealPrice())));
        holder.tvProductNum.setText(mContext.getString(R.string.label_quantity,String.valueOf(orderBean.getQuantity())));
        holder.tvOrderDetial.setTextSize(12);
        holder.tvOrderDetial.setTextColor(Color.parseColor("#666666"));
        holder.tvOrderDetial.setText(mContext.getString(R.string.label_product_total,String.valueOf(orderBean.getQuantity()),Util.getNumberFormat(orderBean.getPayAmount())));
        ImageLoader.displayImage(manager,orderBean.getProductPic(),holder.ivProduct,R.mipmap.ic_logo_empty5,R.mipmap.ic_logo_empty5);
        if(!buttonsGone){
            setButtons(holder,position);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetialActivity.class).putExtra("orderId", orderBean.getId());
                ((BaseActivity)mContext).startActivityForResult(intent, Constants.RequestCode.EDIT_ORDER);
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


    private void setButtons(CustomerDetialOrderHolder holder, int position) {
        if (mOrderList==null)return;
        holder.llButtonContainer.removeAllViews();
        //        WAIT_PAY("待付款"), PAID("已付款"), WAIT_DELIVER("待发货"),
        //        DELIVERED("已发货"), RECEIVED("已收货"), SUCCESS("交易成功"),
        //        WAIT_REFUND("待退款"), REFUNDED("已退款"), CLOSED("交易关闭");
        if("WAIT_PAY".equals(mOrderList.get(position).getStatus())){ //代付款
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(72), ScreenUtil.dip2px(28));
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(32);
            layoutParams.setMargins(ScreenUtil.dip2px(10),0,0,0);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#8d92a3"));
            textView.setText("修改金额");
            textView.setBackgroundResource(R.drawable.sp_line);
            holder.llButtonContainer.addView(textView);
        }else if("WAIT_DELIVER".equals(mOrderList.get(position).getStatus()) && mOrderList.get(position).getVirtual()==0){

            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(72), ScreenUtil.dip2px(28));
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(32);
            layoutParams.setMargins(ScreenUtil.dip2px(10),0,0,0);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#8d92a3"));
            textView.setText("发货");
            textView.setBackgroundResource(R.drawable.sp_line);



            holder.llButtonContainer.addView(textView);
            TextView textView1 = new TextView(mContext);
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(32);
            layoutParams.setMargins(ScreenUtil.dip2px(10),0,0,0);
            textView1.setLayoutParams(layoutParams);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(Color.parseColor("#8d92a3"));
            textView1.setText("修改地址");
            textView1.setBackgroundResource(R.drawable.sp_line);
            holder.llButtonContainer.addView(textView1);
        }

        if(!StringUtil.isEmpty(mOrderList.get(position).getLogisticsNum())){
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(72), ScreenUtil.dip2px(28));
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(32);
            layoutParams.setMargins(ScreenUtil.dip2px(10),0,0,0);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#8d92a3"));
            textView.setText("查看物流");
            textView.setBackgroundResource(R.drawable.sp_line);
            holder.llButtonContainer.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lookLogistics(mOrderList.get(position));
                }
            });

        }

    }
    private void lookLogistics(OrderDetialBean.OrderItemListBean recordsBean) {
        Intent intent = new Intent(mContext, WebActivity.class);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://m.kuaidi100.com/");
        if(recordsBean!=null && !StringUtil.isEmpty(recordsBean.getReshipLogisticsCom())){
            stringBuilder.append("result.jsp?com=");
            stringBuilder.append(recordsBean.getLogisticsCom());
            stringBuilder.append("&");
        }
        if(recordsBean!=null && !StringUtil.isEmpty(recordsBean.getReshipLogisticsNum())){
            stringBuilder.append("nu=");
            stringBuilder.append(recordsBean.getLogisticsNum());
        }
        intent.putExtra("url",stringBuilder.toString());
        intent.putExtra("title","查询物流");
        mContext.startActivity(intent);
    }



    @Override
    public int getItemCount() {
            return mOrderList == null ? 0 : mOrderList.size();
    }

    public void setButtonsGone(boolean b) {
        buttonsGone = b;
    }
}
