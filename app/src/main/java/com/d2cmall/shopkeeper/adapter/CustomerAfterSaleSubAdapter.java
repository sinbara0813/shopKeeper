package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.ArrayMap;
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
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.SharePrefConstant;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.CustomerAfterSaleSubHolder;
import com.d2cmall.shopkeeper.holder.CustomerDetialOrderHolder;
import com.d2cmall.shopkeeper.model.BrowseListBean;
import com.d2cmall.shopkeeper.model.OrderDetialBean;
import com.d2cmall.shopkeeper.model.OrderListBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.ui.activity.AfterSaleDetailActivity;
import com.d2cmall.shopkeeper.ui.activity.BrandSettingActivity;
import com.d2cmall.shopkeeper.ui.activity.OrderDetialActivity;
import com.d2cmall.shopkeeper.ui.activity.OrderPurchaseSettleActivity;
import com.d2cmall.shopkeeper.ui.activity.WebActivity;
import com.d2cmall.shopkeeper.ui.view.AddressPop;
import com.d2cmall.shopkeeper.ui.view.AgreeAfterSalePop;
import com.d2cmall.shopkeeper.ui.view.EditPricePop;
import com.d2cmall.shopkeeper.ui.view.RefuseAfterSalePop;
import com.d2cmall.shopkeeper.ui.view.TransparentPop;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.SharedPreUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by LWJ on 2019/2/21.
 * Description : OrderListAdapter
 */

public class CustomerAfterSaleSubAdapter extends DelegateAdapter.Adapter<CustomerAfterSaleSubHolder> {
    private Context mContext;
    private RequestManager manager;
    private List<OrderDetialBean.OrderItemListBean> mOrderList;
    private ShopBean shopBean;
    private String provinceInfo;
    private String provinceName,cityName,districtName;

    public void setRefreshFragmentListener(RefreshFragmentListener refreshFragmentListener) {
        this.refreshFragmentListener = refreshFragmentListener;
    }

    private RefreshFragmentListener refreshFragmentListener;

    public CustomerAfterSaleSubAdapter(Context context, RequestManager manager, int type , List<OrderDetialBean.OrderItemListBean> orderList) {
        this.mContext = context;
        this.manager=manager;
        this.mOrderList = orderList;
        new Thread() {
            @Override
            public void run() {
                try {
                    InputStream input = mContext.getResources().openRawResource
                            (R.raw.area);
                    provinceInfo = Util.readStreamToString(input);
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        return linearLayoutHelper;
    }

    @Override
    public CustomerAfterSaleSubHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_customer_after_sale_order_item, parent, false);
        return new CustomerAfterSaleSubHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerAfterSaleSubHolder holder, int position) {
        if (mOrderList==null || mOrderList.get(position)==null)return;
        OrderDetialBean.OrderItemListBean orderBean = mOrderList.get(position);
        holder.tvAction.setTextColor(Color.parseColor("#111111"));
        holder.rlBottom.setVisibility(View.VISIBLE);
        holder.tvOrderStatus.setVisibility(View.VISIBLE);
        holder.tvOrderStatus.setText(orderBean.getStatusName());
        holder.tvAction.setText(mContext.getString(R.string.label_order_code,orderBean.getOrderSn()));
        holder.tvProductName.setText(orderBean.getProductName());
        holder.tvProductSpec.setText(orderBean.getStandard());
        holder.tvProductPrice.setText(mContext.getString(R.string.label_money,Util.getNumberFormat(orderBean.getRealPrice())));
        holder.tvProductNum.setText(mContext.getString(R.string.label_quantity,String.valueOf(orderBean.getQuantity())));
        setReshipTextStyle(holder,orderBean);
        if(!StringUtil.isEmpty(orderBean.getProductPic())){
            ImageLoader.displayImage(manager,getValidPic(orderBean.getProductPic().split(",")),holder.ivProduct,R.mipmap.ic_logo_empty5,R.mipmap.ic_logo_empty5);
        }
        setButtons(holder,position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AfterSaleDetailActivity.class).putExtra("orderId", orderBean.getId());
                ((BaseActivity)mContext).startActivityForResult(intent, Constants.RequestCode.EDIT_ORDER);
            }
        });
    }

    private void setReshipTextStyle(CustomerAfterSaleSubHolder holder, OrderDetialBean.OrderItemListBean orderBean) {
       if(orderBean.getAfterType()==2){
           String reshipInfo = mContext.getString(R.string.label_reship_info, String.valueOf(orderBean.getAfterQuantity()), Util.getNumberFormat(orderBean.getAfterAmount()));
           int length = reshipInfo.length();
           int index1 = reshipInfo.indexOf(" ");
           int index2 = reshipInfo.lastIndexOf("件");
           int index3 = reshipInfo.indexOf("¥");
           SpannableStringBuilder style = new SpannableStringBuilder(reshipInfo);
           style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black111111)),index1,index2+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
           style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black111111)),index3,length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
           holder.tvReturnInfo.setText(style);
       }else{
           String reshipInfo = mContext.getString(R.string.label_refund_amount, String.valueOf(orderBean.getAfterQuantity()), Util.getNumberFormat(orderBean.getAfterAmount()));
           SpannableStringBuilder style1 = new SpannableStringBuilder(reshipInfo);
           style1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black111111)),4,reshipInfo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
           holder.tvReturnInfo.setText(style1);
       }
        String reshipReason;
       if(orderBean.getAfterType()==2){
           reshipReason = mContext.getString(R.string.label_reship_reason, orderBean.getAfterMemo());
       }else{
           reshipReason = mContext.getString(R.string.label_refund_reason, orderBean.getAfterMemo());
       }
        SpannableStringBuilder style1 = new SpannableStringBuilder(reshipReason);
        style1.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.black111111)),4,reshipReason.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvReturnReason.setText(style1);
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


    private void setButtons(CustomerAfterSaleSubHolder holder, int position) {
        if (mOrderList==null)return;
        holder.llButtonContainer.removeAllViews();
        //订单状态说明:
        //退款涉及的状态
        //WAIT_REFUND("待退款"), REFUSE_REFUND("拒绝退款"), AGREE_REFUND("同意退款"), REFUNDED("退款完成");

        //退货退款涉及的状态
        //WAIT_RESHIP("待退货"), REFUSE_RESHIP("拒绝退货"), AGREE_RESHIP("同意退货"), RESHIPED("退货发出"),	AGREE_REFUND("同意退款"), REFUNDED("退款完成")

        //WAIT_DELIVER => DELIVERED  订单发货
        //WAIT_REFUND => AGREE_REFUND  同意退款
        //WAIT_REFUND => REFUSE_REFUND 拒绝退款
        //WAIT_RESHIP => AGREE_RESHIP 同意退货退款
        //WAIT_RESHIP 、RESHIPED => REFUSE_RESHIP 拒绝退货退款
        //RESHIPED => AGREE_REFUND 退货收货
        //AGREE_REFUND => REFUNDED 退款完成

//        if(!StringUtil.isEmpty(mOrderList.get(position).getOrderItemList().get(0).getReshipLogisticsNum())){
//            TextView textView = new TextView(mContext);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(72), ScreenUtil.dip2px(28));
//            layoutParams.width = ScreenUtil.dip2px(80);
//            layoutParams.height = ScreenUtil.dip2px(32);
//            layoutParams.setMargins(ScreenUtil.dip2px(10),0,0,0);
//            textView.setLayoutParams(layoutParams);
//            textView.setGravity(Gravity.CENTER);
//            textView.setTextColor(Color.parseColor("#8d92a3"));
//            textView.setText("查看物流");
//            textView.setBackgroundResource(R.drawable.sp_line);
//            holder.llButtonContainer.addView(textView);
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    lookLogistics(mOrderList.get(position).getOrderItemList().get(0).getReshipLogisticsNum());
//                }
//            });
//
//        }
        if("WAIT_REFUND".equals(mOrderList.get(position).getStatus()) || "WAIT_RESHIP".equals(mOrderList.get(position).getStatus())){ //待审核(退货退款、退款)
            TextView agreeText = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(30));
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(30);
            layoutParams.setMargins(ScreenUtil.dip2px(10),0,0,0);
            agreeText.setLayoutParams(layoutParams);
            agreeText.setGravity(Gravity.CENTER);
            agreeText.setTextColor(mContext.getResources().getColor(R.color.normal_black));
            agreeText.setText("通过");
            agreeText.setBackgroundResource(R.drawable.sp_line);
            holder.llButtonContainer.addView(agreeText);
            agreeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agreeRequest(agreeText,position);
                }
            });

            TextView refuseText = new TextView(mContext);
            refuseText.setLayoutParams(layoutParams);
            refuseText.setGravity(Gravity.CENTER);
            refuseText.setTextColor(mContext.getResources().getColor(R.color.normal_black));
            refuseText.setText("拒绝");
            refuseText.setBackgroundResource(R.drawable.sp_line);
            refuseText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refuseReques(refuseText,position);
                }
            });
            holder.llButtonContainer.addView(refuseText);
        }else if("AGREE_REFUND".equals(mOrderList.get(position).getStatus())){
            TextView receiveText = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(30));
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(30);
            layoutParams.setMargins(ScreenUtil.dip2px(10),0,0,0);
            receiveText.setLayoutParams(layoutParams);
            receiveText.setGravity(Gravity.CENTER);
            receiveText.setTextColor(mContext.getResources().getColor(R.color.color_white));
            receiveText.setText("退款");
            receiveText.setBackgroundResource(R.drawable.sp_round2_bg_blue);
            holder.llButtonContainer.addView(receiveText);
            receiveText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refundSuccess(receiveText,position);
                }
            });
        }else if("RESHIPED".equals(mOrderList.get(position).getStatus())){ //退货退款待收货
            TextView receiveText = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(30));
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(30);
            layoutParams.setMargins(ScreenUtil.dip2px(10),0,0,0);
            receiveText.setLayoutParams(layoutParams);
            receiveText.setGravity(Gravity.CENTER);
            receiveText.setTextColor(mContext.getResources().getColor(R.color.color_white));
            receiveText.setText("确认收货");
            receiveText.setBackgroundResource(R.drawable.sp_round2_bg_blue);
            holder.llButtonContainer.addView(receiveText);
            receiveText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    receiveSure(receiveText,position);
                }
            });

        }

    }

    private void refundSuccess(TextView receiveText, int position) {
        new AlertDialog.Builder(mContext)
                .setMessage("确认退款?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayMap<String, String> arrayMap = new ArrayMap<>();
                        arrayMap.put("orderItemId",String.valueOf(mOrderList.get(position).getId()));
                        receiveText.setEnabled(false);
                        ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().refundSuccess(arrayMap), new BaseObserver() {
                            @Override
                            public void onSuccess(Object o) {
                                Util.showToast(mContext,"已退款");
                                if(refreshFragmentListener!=null){
                                    refreshFragmentListener.refreshFreagment();
                                }
                            }

                            @Override
                            public void onError(int errorCode, String msg) {
                                super.onError(errorCode, msg);
                                receiveText.setEnabled(true);
                            }
                        });
                    }
                }).show();

    }

    private void receiveSure(TextView receiveText, int position) {

        new AlertDialog.Builder(mContext)
                .setMessage("确认收到该商品?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayMap<String, String> arrayMap = new ArrayMap<>();
                        arrayMap.put("orderItemId",String.valueOf(mOrderList.get(position).getId()));
                        receiveText.setEnabled(false);
                        ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().receiveSure(arrayMap), new BaseObserver() {
                            @Override
                            public void onSuccess(Object o) {
                                Util.showToast(mContext,"已确认");
                                if(refreshFragmentListener!=null){
                                    refreshFragmentListener.refreshFreagment();
                                }
                            }

                            @Override
                            public void onError(int errorCode, String msg) {
                                super.onError(errorCode, msg);
                                receiveText.setEnabled(true);
                            }
                        });
                    }
                }).show();

    }



    private void refuseReques(TextView refuseText, int position) {
        RefuseAfterSalePop refuseAfterSalePop=null;
        OrderDetialBean.OrderItemListBean orderItemListBean = mOrderList.get(position);
        if("WAIT_REFUND".equals(orderItemListBean.getStatus())){//拒绝退款
            refuseAfterSalePop = new RefuseAfterSalePop(mContext,"WAIT_REFUND",orderItemListBean.getId());
        }else if("WAIT_RESHIP".equals(orderItemListBean.getStatus())){//拒绝退货退款
            refuseAfterSalePop = new RefuseAfterSalePop(mContext,"WAIT_RESHIP",orderItemListBean.getId());
        }
        if(refuseAfterSalePop!=null){
            RefuseAfterSalePop finalRefuseAfterSalePop = refuseAfterSalePop;
            refuseAfterSalePop.setDismissCallBack(new TransparentPop.DismissListener() {
                @Override
                public void dismissStart() {
                    if(finalRefuseAfterSalePop !=null){
                        boolean hasRefuse = finalRefuseAfterSalePop.isHasRefuse();
                        if(hasRefuse){
                            if(refreshFragmentListener!=null){
                                refreshFragmentListener.refreshFreagment();
                            }
                        }
                    }
                }

                @Override
                public void dismissEnd() {

                }
            });
            refuseAfterSalePop.show(((BaseActivity)mContext).getWindow().getDecorView());
        }

    }

    private void agreeRequest(TextView agreeText, int position) {
        OrderDetialBean.OrderItemListBean orderItemListBean = mOrderList.get(position);
        if("WAIT_REFUND".equals(orderItemListBean.getStatus())){ //同意退款
            agreeText.setEnabled(false);
            EditPricePop editPricePop = new EditPricePop(mContext, orderItemListBean.getId(), orderItemListBean.getAfterAmount(), orderItemListBean.getRealPrice(), 1);
            editPricePop.setDismissCallBack(new TransparentPop.DismissListener() {
                @Override
                public void dismissStart() {
                    agreeText.setEnabled(true);
                    if(editPricePop!=null){
                        boolean hasEdit = editPricePop.isHasEdit();
                        if(hasEdit){
                            if(refreshFragmentListener!=null){
                                refreshFragmentListener.refreshFreagment();
                            }
                        }
                    }
                }

                @Override
                public void dismissEnd() {

                }
            });
            editPricePop.show(((BaseActivity)mContext).getWindow().getDecorView());


        }else if("WAIT_RESHIP".equals(orderItemListBean.getStatus())){//同意退货退款
            AgreeAfterSalePop agreeAfterSalePop = new AgreeAfterSalePop(mContext,orderItemListBean.getId(),orderItemListBean.getAfterAmount(),orderItemListBean.getRealPrice());
            agreeAfterSalePop.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String n=agreeAfterSalePop.getName();
                    String p=agreeAfterSalePop.getPhone();
                    String region=agreeAfterSalePop.getRegion();
                    String a=agreeAfterSalePop.getDetailAddress();
                    if (StringUtil.isEmpty(n)||StringUtil.isEmpty(p)||StringUtil.isEmpty(region)||StringUtil.isEmpty(a)){
                        Util.showToast(mContext,"地址不完善");
                        return;
                    }
                    JsonObject biz_content=new JsonObject();
                    biz_content.addProperty("province",StringUtil.isEmpty(provinceName)?agreeAfterSalePop.provinceName:provinceName);
                    biz_content.addProperty("city",StringUtil.isEmpty(cityName)?agreeAfterSalePop.cityName:cityName);
                    biz_content.addProperty("district",StringUtil.isEmpty(districtName)?agreeAfterSalePop.districtName:districtName);
                    biz_content.addProperty("address",a);
                    biz_content.addProperty("name",n);
                    biz_content.addProperty("mobile",p);
                    String s=biz_content.toString();
                    ArrayMap<String, String> arrayMap = new ArrayMap<>();
                    arrayMap.put("orderItemId", String.valueOf(orderItemListBean.getId()));
                    arrayMap.put("reshipName", n);
                    arrayMap.put("reshipMobile", p);
                    arrayMap.put("reshipAddress", s);
                    ((BaseActivity) mContext).addDisposable(ApiRetrofit.getInstance().getApiService().agreeReship(arrayMap), new BaseObserver() {
                        @Override
                        public void onSuccess(Object o) {
                            agreeAfterSalePop.dismiss();
                            Util.showToast(mContext, "已同意");
                            if(refreshFragmentListener!=null){
                                refreshFragmentListener.refreshFreagment();
                            }
                        }

                        @Override
                        public void onError(int errorCode, String msg) {
                            super.onError(errorCode, msg);
                        }
                    });
                    if (shopBean!=null){
                        shopBean.setBackAddress(s);
                        ((BaseActivity) mContext).addDisposable(ApiRetrofit.getInstance().getApiService().updateShop(shopBean), new BaseObserver() {
                            @Override
                            public void onSuccess(Object o) {

                            }
                        });
                    }
                }
            });
            agreeAfterSalePop.setAddressListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agreeAfterSalePop.hideSoft();
                    AddressPop addressPop = new AddressPop(mContext, provinceInfo);
                    addressPop.setCallBack(new AddressPop.CallBack() {
                        @Override
                        public void callback(String provinceName, int provinceCode, String cityName, int cityCode, String districtName, int districtCode) {
                            if (agreeAfterSalePop!=null){
                                agreeAfterSalePop.setAddress(provinceName+cityName+districtName);
                            }
                            CustomerAfterSaleSubAdapter.this.provinceName=provinceName;
                            CustomerAfterSaleSubAdapter.this.cityName=cityName;
                            CustomerAfterSaleSubAdapter.this.districtName=districtName;
                        }
                    });
                    addressPop.show(agreeText);
                }
            });
            agreeAfterSalePop.show(agreeText);
        }

    }

    public  interface  RefreshFragmentListener{
        void refreshFreagment();
    }


    private void lookLogistics(String code) {
        Intent intent = new Intent(mContext, WebActivity.class);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://m.kuaidi100.com/");
        if(!StringUtil.isEmpty(code)){
            stringBuilder.append("nu=");
            stringBuilder.append(code);
        }
        intent.putExtra("url",stringBuilder.toString());
        intent.putExtra("title","查询物流");
        mContext.startActivity(intent);
    }



    @Override
    public int getItemCount() {
            return mOrderList == null ? 0 : mOrderList.size();
    }

    public void setShopBean(ShopBean shopBean){
        this.shopBean=shopBean;
    }
}
