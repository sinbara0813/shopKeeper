package com.d2cmall.shopkeeper.ui.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.common.Address;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.OrderDetialBean;
import com.d2cmall.shopkeeper.ui.view.DeliveryPop;
import com.d2cmall.shopkeeper.ui.view.EditAddressPop;
import com.d2cmall.shopkeeper.ui.view.EditPricePop;
import com.d2cmall.shopkeeper.ui.view.PayPop;
import com.d2cmall.shopkeeper.ui.view.TransparentPop;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/3/1.
 * Description : OrderDetialActivity
 * 订单详情
 */

public class OrderDetialActivity extends BaseActivity implements BaseView {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
    @BindView(R.id.tv_consignee)
    TextView tvConsignee;
    @BindView(R.id.tv_consignee_phone)
    TextView tvConsigneePhone;
    @BindView(R.id.iv_product)
    ImageView ivProduct;
    @BindView(R.id.tv_product_name)
    TextView tvProductName;
    @BindView(R.id.tv_product_spec)
    TextView tvProductSpec;
    @BindView(R.id.tv_product_price)
    TextView tvProductPrice;
    @BindView(R.id.tv_product_num)
    TextView tvProductNum;
    @BindView(R.id.tv_order_code)
    TextView tvOrderCode;
    @BindView(R.id.tv_copy_order_code)
    TextView tvCopyOrderCode;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    @BindView(R.id.tv_remarks)
    TextView tvRemarks;
    @BindView(R.id.tv_product_total)
    TextView tvProductTotal;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_freight)
    TextView tvFreight;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_other)
    TextView tvOther;
    @BindView(R.id.tv_actual_amount)
    TextView tvActualAmount;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_logistics_company)
    TextView tvLogisticsCompany;
    @BindView(R.id.ll_logistics_company)
    LinearLayout llLogisticsCompany;
    @BindView(R.id.tv_logistics_code)
    TextView tvLogisticsCode;
    @BindView(R.id.tv_copy_logistics_code)
    TextView tvCopyLogisticsCode;
    @BindView(R.id.ll_logistics_code)
    LinearLayout llLogisticsCode;
    private long orderId;
    private OrderDetialBean.OrderItemListBean orderItemListBean;
    private BaseModel<OrderDetialBean> mOrderDetialBean;
    private DeliveryPop deliveryPop;
    private boolean hasEditPrice;
    private boolean hasDeliverGood;
    private PayPop payPop;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detial;
    }

    @Override
    public void doBusiness() {
        init();
    }

    private void init() {
        tvName.setText("订单详情");
        orderId = getIntent().getLongExtra("orderId", 0);
        if (orderId > 0) {
            loadOrderDetial();
        }
    }

    private void loadOrderDetial() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getOrderDetial(orderId), new BaseObserver<BaseModel<OrderDetialBean>>(this) {
            @Override
            public void onSuccess(BaseModel<OrderDetialBean> orderDetialBean) {
                orderItemListBean = orderDetialBean.getData().getOrderItemList().get(0);
                mOrderDetialBean = orderDetialBean;
                ImageLoader.displayImage(OrderDetialActivity.this, orderItemListBean.getProductPic(), ivProduct, R.mipmap.ic_logo_empty5);
                int quantity = orderItemListBean.getQuantity();
                tvOrderStatus.setText(orderItemListBean.getStatusName());
                tvConsignee.setText(orderDetialBean.getData().getName());
                tvConsigneePhone.setText(orderDetialBean.getData().getMobile());
                if (!StringUtil.isEmpty(orderDetialBean.getData().getAddress())) {
                    if (orderDetialBean.getData().getAddress().startsWith("{")){
                        Gson gson=new Gson();
                        Address address=gson.fromJson(orderDetialBean.getData().getAddress(),Address.class);
                        tvAddress.setText(address.province+address.city+address.district+address.address);
                    }else {
                        tvAddress.setText(orderDetialBean.getData().getProvince()+orderDetialBean.getData().getCity()+orderDetialBean.getData().getDistrict()+orderDetialBean.getData().getAddress());
                    }
                }else {
                    StringBuilder stringBuilder = new StringBuilder();
                if(!StringUtil.isEmpty(orderDetialBean.getData().getProvince())){
                    stringBuilder.append(orderDetialBean.getData().getProvince());
                    stringBuilder.append(" ");
                }
                if(!StringUtil.isEmpty(orderDetialBean.getData().getCity())){
                    stringBuilder.append(orderDetialBean.getData().getCity());
                    stringBuilder.append(" ");
                }
                if(!StringUtil.isEmpty(orderDetialBean.getData().getDistrict())){
                    stringBuilder.append(orderDetialBean.getData().getDistrict());
                    stringBuilder.append(" ");
                }
                if(!StringUtil.isEmpty(orderDetialBean.getData().getAddress())){
                    stringBuilder.append(orderDetialBean.getData().getAddress());
                }
                tvAddress.setText(stringBuilder.toString());
                }
                tvProductName.setText(orderItemListBean.getProductName());
                tvProductSpec.setText(orderItemListBean.getTypeName());
                tvProductPrice.setText(getString(R.string.label_money, Util.getNumberFormat(orderItemListBean.getRealPrice())));
                tvProductNum.setText(getString(R.string.label_quantity, String.valueOf(quantity)));
                tvOrderCode.setText(getString(R.string.label_order_code, orderDetialBean.getData().getSn()));
                tvOrderDate.setText(getString(R.string.label_order_date, orderDetialBean.getData().getCreateDate()));
                tvPayType.setText(getString(R.string.label_order_pay_type, orderDetialBean.getData().getPaymentTypeName()));
                tvProductTotal.setText(getString(R.string.label_order_total, String.valueOf(quantity), Util.getNumberFormat(quantity * orderDetialBean.getData().getOrderItemList().get(0).getRealPrice())));
                tvCoupon.setText(getString(R.string.label_money, Util.getNumberFormat(orderItemListBean.getCouponWeightingAmount())));
                String string = getString(R.string.label_money, Util.getNumberFormat(orderItemListBean.getPayAmount()));
                int length = string.length();
                SpannableString textSpan = new SpannableString(string);
                textSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(12)), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                textSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(16)), 1, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                tvActualAmount.setText(textSpan);
                if (!StringUtil.isEmpty(orderItemListBean.getLogisticsCom())) {
                    llLogisticsCompany.setVisibility(View.VISIBLE);
                    tvLogisticsCompany.setText(getString(R.string.label_logistics_company,orderItemListBean.getLogisticsCom()));
                }
                if(!StringUtil.isEmpty(orderItemListBean.getLogisticsNum())){
                    llLogisticsCode.setVisibility(View.VISIBLE);
                    tvLogisticsCode.setText(getString(R.string.label_logistics_code,orderItemListBean.getLogisticsNum()));
                }
                if(orderItemListBean.getVirtual()==0){
                    setActionButtons();
                }
            }

        });
    }

    private void setActionButtons() {
        llBottom.removeAllViews();
        if (orderItemListBean == null) {
            return;
        }
        if(orderItemListBean.getOffline()==1){
            return;
        }
        if ("WAIT_PAY".equals(orderItemListBean.getStatus())) { //代付款
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(72), ScreenUtil.dip2px(28));
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(32);
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#8d92a3"));
            textView.setText("修改金额");
            textView.setBackgroundResource(R.drawable.sp_line);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderItemListBean == null) {
                        return;
                    }
                    showEditPricePop();
                }
            });
            llBottom.addView(textView);
        } else if ("WAIT_DELIVER".equals(orderItemListBean.getStatus())) {

            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(72), ScreenUtil.dip2px(28));
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(32);
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#8d92a3"));
            textView.setText("发货");
            textView.setBackgroundResource(R.drawable.sp_line);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeliverGoodPop();
                }
            });
            llBottom.addView(textView);


            TextView textView1 = new TextView(this);
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(32);
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            textView1.setLayoutParams(layoutParams);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(Color.parseColor("#8d92a3"));
            textView1.setText("修改地址");
            textView1.setBackgroundResource(R.drawable.sp_line);
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditAddressPop();
                }
            });
            llBottom.addView(textView1);
        }

        if (!StringUtil.isEmpty(orderItemListBean.getLogisticsNum())) {
            TextView textView1 = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(72), ScreenUtil.dip2px(28));
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(32);
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            textView1.setLayoutParams(layoutParams);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(Color.parseColor("#8d92a3"));
            textView1.setText("查看物流");
            textView1.setBackgroundResource(R.drawable.sp_line);
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lookLogistics();
                }
            });
            llBottom.addView(textView1);
            
        }
    }


    private void lookLogistics() {
        Intent intent = new Intent(this, WebActivity.class);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://m.kuaidi100.com/");
        if(orderItemListBean!=null && !StringUtil.isEmpty(orderItemListBean.getLogisticsCom())){
            stringBuilder.append("result.jsp?com=");
            stringBuilder.append(orderItemListBean.getLogisticsCom());
            stringBuilder.append("&");
        }
        if(orderItemListBean!=null && !StringUtil.isEmpty(orderItemListBean.getLogisticsNum())){
            stringBuilder.append("nu=");
            stringBuilder.append(orderItemListBean.getLogisticsNum());
        }
        intent.putExtra("url",stringBuilder.toString());
        intent.putExtra("title","查询物流");
        startActivity(intent);
    }


    private void showEditAddressPop() {
        if (mOrderDetialBean == null) {
            return;
        }
        EditAddressPop editAddressPop = new EditAddressPop(this, mOrderDetialBean.getData().getId());
        editAddressPop.setDismissCallBack(new TransparentPop.DismissListener() {
            @Override
            public void dismissStart() {
                if(editAddressPop!=null){
                    ArrayMap<String, String> addressMap = editAddressPop.getAddressMap();
                    if(addressMap!=null){
                        if(!StringUtil.isEmpty(addressMap.get("address"))){
                            tvAddress.setText(addressMap.get("address"));
                        }
                        if(!StringUtil.isEmpty(addressMap.get("name"))){
                            tvConsignee.setText(addressMap.get("name"));
                        }
                        if(!StringUtil.isEmpty(addressMap.get("phone"))){
                            tvConsigneePhone.setText(addressMap.get("phone"));
                        }
                    }
                }
            }

            @Override
            public void dismissEnd() {

            }
        });
        editAddressPop.show(getWindow().getDecorView());
    }

    private void showEditPricePop() {
        EditPricePop editPricePop = new EditPricePop(this, orderItemListBean.getId(), orderItemListBean.getCouponWeightingAmount(), orderItemListBean.getRealPrice(),0);
        editPricePop.setDismissCallBack(new TransparentPop.DismissListener() {
            @Override
            public void dismissStart() {
                if(editPricePop!=null){
                    hasEditPrice = editPricePop.isHasEdit();
                    if(hasEditPrice){
                        loadOrderDetial();
                    }
                }
            }

            @Override
            public void dismissEnd() {

            }
        });
        editPricePop.show(getWindow().getDecorView());
    }


    private void showDeliverGoodPop() {
        if (orderItemListBean == null) {
            return;
        }
        if(deliveryPop==null){
            deliveryPop = new DeliveryPop(this, orderItemListBean.getId());
        }
        deliveryPop.setDismissCallBack(new TransparentPop.DismissListener() {
            @Override
            public void dismissStart() {
                if(deliveryPop!=null ){
                    hasDeliverGood = deliveryPop.isHasDeliverGood();
                    if(hasDeliverGood){
                        loadOrderDetial();
                    }
                }
            }

            @Override
            public void dismissEnd() {

            }
        });
        deliveryPop.show(getWindow().getDecorView());
    }


    @Override
    public void onBackPressed() {
            if(hasEditPrice || hasDeliverGood){
                setResult(RESULT_OK);
            }
            finish();
            overridePendingTransition(0, R.anim.slide_out_right);
    }

    @OnClick({R.id.iv_back, R.id.iv_product, R.id.tv_copy_order_code,R.id.tv_copy_logistics_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if(hasEditPrice || hasDeliverGood){
                    setResult(RESULT_OK);
                }
                finish();
                break;
            case R.id.iv_product:
                //商品详情
                break;
            case R.id.tv_copy_order_code:
                //复制订单号
                copyOrderCode();
                break;
            case R.id.tv_copy_logistics_code:
                //复制物流单号
                copyLogisticsCode();
                break;
        }
    }

    private void copyLogisticsCode() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("LogisticsCode", orderItemListBean.getLogisticsNum()));
        Util.showToast(this, "物流单号已复制");
    }

    private void copyOrderCode() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("code", orderItemListBean.getOrderSn()));
        Util.showToast(this, "订单号已复制");
    }

    @Override
    public void requestStart() {

    }

    @Override
    public void requestFinish() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode== Constants.RequestCode.SCAN_DELIVERY_CODE && data!=null){
            String deliveryCode = data.getStringExtra("deliveryCode");
            if(!StringUtil.isEmpty(deliveryCode)){
                if(deliveryPop!=null){
                    deliveryPop.setDerliverCode(deliveryCode);
                }
            }
        }
    }

}
