package com.d2cmall.shopkeeper.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.alipay.sdk.app.PayTask;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.PayResult;
import com.d2cmall.shopkeeper.model.EmptyBean;
import com.d2cmall.shopkeeper.ui.activity.OfflineSettleActivity;
import com.d2cmall.shopkeeper.ui.activity.PayStateActivity;
import com.d2cmall.shopkeeper.utils.DateUtil;
import com.d2cmall.shopkeeper.utils.PayUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者:Created by sinbara on 2019/5/6.
 * 邮箱:hrb940258169@163.com
 */

public class PayPop implements TransparentPop.InvokeListener {

    @BindView(R.id.alipay_check_box)
    CheckBox alipayCheckBox;
    @BindView(R.id.wx_check_box)
    CheckBox wxCheckBox;
    private View payLayout;
    private Context mContext;
    private TransparentPop mPop;
    private String orderSn;
    private String orderId;
    private double price;
    private int expireTime;
    private String expireDate;
    private IWXAPI mApp;
    private String prepay_id;
    private boolean changeType;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                if(changeType){
                    toPayStateActivity(5);
                }else{
                    toPayStateActivity(3);
                }
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                if(changeType){
                    toPayStateActivity(6);
                }else{
                    toPayStateActivity(4);
                }
            }
            super.handleMessage(msg);
        }
    };

    public PayPop(Context context) {
        this.mContext = context;
        mApp = WXAPIFactory.createWXAPI(context, Constants.WEIXINAPPID, false);
        mApp.registerApp(Constants.WEIXINAPPID);
        payLayout = LayoutInflater.from(context).inflate(R.layout.layout_pay_style, new CoordinatorLayout(context), false);
        ButterKnife.bind(this, payLayout);
        mPop = new TransparentPop(context, this);
        Animation inAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
    }

    public void show(View view) {
        mPop.show(view, true);
    }

    public boolean isShow() {
        return mPop.isShowing();
    }

    public void dismiss() {
        mPop.dismiss(true);
    }

    public void toPayStateActivity(int type){
        Intent intent=new Intent(mContext,PayStateActivity.class);
        intent.putExtra("type",type);
        if (type==3) {
            intent.putExtra("cash",price);
        }
        mContext.startActivity(intent);
        ((Activity)mContext).setResult(Activity.RESULT_OK);
        ((Activity)mContext).finish();
        dismiss();
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.BOTTOM);
        v.addView(payLayout);
    }

    @Override
    public void releaseView(LinearLayout v) {
        v.removeAllViews();
        payLayout = null;
        if (mApp!=null){
            mApp.detach();
            mApp=null;
        }
    }

    @OnClick({R.id.alipay_tv, R.id.wx_tv, R.id.settle_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.alipay_tv:
                changePayType(1);
                break;
            case R.id.wx_tv:
                changePayType(2);
                break;
            case R.id.settle_tv:
                if (alipayCheckBox.isChecked()){
                    if (StringUtil.isEmpty(orderId)||StringUtil.isEmpty(orderSn)||price==0||expireTime==0){
                        Util.showToast(mContext,"参数少传了!");
                        return;
                    }
                    pay(orderId,"ALI_PAY");
                    aliPay(orderId,orderSn,price,expireTime);
                }else if (wxCheckBox.isChecked()){
                    if (StringUtil.isEmpty(orderId)||StringUtil.isEmpty(orderSn)||price==0||StringUtil.isEmpty(expireDate)){
                        Util.showToast(mContext,"参数少传了!");
                        return;
                    }
                    pay(orderId,"WX_PAY");
                    wxPay(orderSn,orderId,Util.getIntValue(price*100),DateUtil.getExpireTime(expireDate));
                    dismiss();
                }
                break;
        }
    }

    private void pay(String id,String payType){ //接口地址要改
        if (changeType){
            ApiRetrofit.getInstance().getApiService().purchasePay(buildPayParam(id, payType)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<BaseModel<EmptyBean>>() {
                        @Override
                        public void onSuccess(BaseModel<EmptyBean> o) {

                        }
                    });
        }else {
            ApiRetrofit.getInstance().getApiService().allotStatementPay(buildPayParam(id, payType)).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<BaseModel<EmptyBean>>() {
                        @Override
                        public void onSuccess(BaseModel<EmptyBean> o) {

                        }
                    });
        }
    }

    private ArrayMap<String,String> buildPayParam(String id, String payType){
        ArrayMap<String,String> map=new ArrayMap<>();
        map.put("id",id);
        map.put("paymentType",payType);
        return map;
    }

    /**
     * 支付宝支付 支付宝 "subject"--订单ID  "out_trade_no"--订单SN "total_amount"--付款金额 "time_expire"--订单超时时间
     */
    private void aliPay(String orderId, String orderSn, double totalAmount, int expireTime){
        String orderInfo= PayUtil.getOrderInfo(orderSn,orderId,totalAmount,expireTime);
        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) mContext);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 微信支付 微信 "body"--订单ID "out_trade_no"--订单SN  "total_fee"--付款金额 当然微信单位是分 "time_expire"--订单超时时间
     */
    private void wxPay(String orderSn, String orderId, int price,String expireTime) {
        if (!StringUtil.isEmpty(prepay_id)){
            wxPay(prepay_id);
            return;
        }
        final String ip = Util.getIp(mContext);
        if (!StringUtil.isEmpty(ip)) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        getPrepayId(orderSn,orderId,price,ip,expireTime);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            };
            thread.start();
        } else {
            Util.showToast(mContext, "支付失败,无法获取手机ip,请确认wify是否连接!");
        }
    }

    private void getPrepayId(String orderSn, String orderId, int price, String ip,String expireTime) throws Throwable {
        prepay_id = PayUtil.getPrepayId(orderSn,orderId,expireTime,price,ip);
        if (!StringUtil.isEmpty(prepay_id)) {
            wxPay(prepay_id);
        }
    }

    private void wxPay(String prepay_id) {
        PayReq request = new PayReq();
        request.appId = Constants.WEIXINAPPID;
        request.nonceStr = Util.getRandomString(15);
        request.packageValue = "Sign=WXPay";
        request.partnerId = Constants.WXMCHID;
        request.prepayId = prepay_id;
        request.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        StringBuilder builder = new StringBuilder();
        builder.append("appid=").append(request.appId).append("&").append("noncestr=").append(request.nonceStr).append("&").append("package=").append(request.packageValue).append("&").append("partnerid=").append(request.partnerId).append("&")
                .append("prepayid=").append(request.prepayId).append("&").append("timestamp=").append(request.timeStamp);
        String temp = builder.toString() + "&key=" + Constants.WXPARTNERID;
        String s = Util.getMD5(temp).toUpperCase();
        request.sign = s;
        mApp.sendReq(request);
    }

    /**
     * @param type 1 支付宝 2微信
     */
    private void changePayType(int type) {
        switch (type) {
            case 1:
                alipayCheckBox.setChecked(true);
                wxCheckBox.setChecked(false);
                break;
            case 2:
                alipayCheckBox.setChecked(false);
                wxCheckBox.setChecked(true);
                break;
        }
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public void setChangeType(boolean changeType) {
        this.changeType = changeType;
    }
    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
