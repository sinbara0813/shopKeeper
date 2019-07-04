package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.OrderRequest;
import com.d2cmall.shopkeeper.common.PayResult;
import com.d2cmall.shopkeeper.model.OrderDetialBean;
import com.d2cmall.shopkeeper.ui.view.CartProductItemView;
import com.d2cmall.shopkeeper.ui.view.CheckBox;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.DateUtil;
import com.d2cmall.shopkeeper.utils.PayUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/5/7.
 * 邮箱:hrb940258169@163.com
 * 线下结算
 */

public class OfflineSettleActivity extends BaseActivity {

    @BindView(R.id.address_name_tv)
    TextView addressNameTv;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;
    @BindView(R.id.remark_et)
    EditText remarkEt;
    @BindView(R.id.total_price_tv)
    TextView totalPriceTv;
    @BindView(R.id.alipay_check_box)
    CheckBox alipayCheckBox;
    @BindView(R.id.wx_check_box)
    CheckBox wxCheckBox;

    private String name;
    private String phone;
    private String cartIds;
    private long draweeId;
    private OrderRequest request;
    private String prepay_id;
    private IWXAPI mApp;
    private Handler mHandler=new MyHandler(this);

    static class MyHandler extends Handler {
        WeakReference<OfflineSettleActivity> weakReference;

        public MyHandler(OfflineSettleActivity activity) {
            weakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                weakReference.get().toPayStateActivity(1);
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                weakReference.get().toPayStateActivity(2);
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_offline_settle;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        mApp = WXAPIFactory.createWXAPI(this, Constants.WEIXINAPPID, false);
        mApp.registerApp(Constants.WEIXINAPPID);
        cartIds=getIntent().getStringExtra("cartIds");
        request=new OrderRequest();
        String[] ids=cartIds.split(",");
        ArrayList<String> list=new ArrayList<String>();
        for (String id: ids) {
            list.add(id);
        }
        request.setPackageIds(list);
        alipayCheckBox.setCheckColorId(R.mipmap.icon_dagou,R.mipmap.icon_bugou);
        alipayCheckBox.setChecked(true);
        wxCheckBox.setCheckColorId(R.mipmap.icon_dagou,R.mipmap.icon_bugou);
        orderSettle();
    }

    private void orderSettle(){
        addDisposable(ApiRetrofit.getInstance().getApiService().offlineOrderSettle(request), new BaseObserver<BaseModel<OrderDetialBean>>() {
            @Override
            public void onSuccess(BaseModel<OrderDetialBean> o) {
                if (o.getData()!=null&&o.getData().getOrderItemList().size()>0){
                    int size=o.getData().getOrderItemList().size();
                    int num=0;
                    for (int i=0;i<size;i++){
                        num+=o.getData().getOrderItemList().get(i).getQuantity();
                        CartProductItemView productItem=new CartProductItemView(OfflineSettleActivity.this);
                        productItem.setOffline();
                        productItem.setData(o.getData().getOrderItemList().get(i));
                        productItem.hideCheckBox();
                        contentLl.addView(productItem);
                    }
                    StringBuilder builder=new StringBuilder();
                    builder.append("共").append(num).append("件  总计¥").append(Util.getNumberFormat(o.getData().getProductAmount()));
                    int[] colors=new int[]{getResources().getColor(R.color.color_black3)};
                    int[] colorIndex=new int[]{String.valueOf(num).length()+6};
                    float[] sizes=new float[]{0.77f};
                    int[] sizeIndex=new int[]{String.valueOf(num).length()+7};
                    totalPriceTv.setText(Util.buildSpan(colors,colorIndex,sizes,sizeIndex,builder.toString()));
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                finish();
            }
        });
    }

    @OnClick({R.id.back_iv, R.id.sure_tv, R.id.address_ll,R.id.alipay_tv,R.id.wx_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.sure_tv:
                orderCreate();
                break;
            case R.id.address_ll:
                Intent intent=new Intent(this,AddOfflineAddressActivity.class);
                if (!StringUtil.isEmpty(name)){
                    intent.putExtra("name",name);
                }
                if (!StringUtil.isEmpty(phone)){
                    intent.putExtra("phone",phone);
                }
                if (draweeId!=0){
                    intent.putExtra("draweeId",draweeId);
                }
                startActivityForResult(intent,100);
                break;
            case R.id.alipay_tv:
                changePayType(1);
                break;
            case R.id.wx_tv:
                changePayType(2);
                break;
        }
    }

    private void orderCreate(){
        if (draweeId==0){
            Util.showToast(this,"请添加付款人信息！");
            return;
        }
        request.setDraweeId(draweeId);
        addDisposable(ApiRetrofit.getInstance().getApiService().offlineOrderCreate(request), new BaseObserver<BaseModel<OrderDetialBean>>() {
            @Override
            public void onSuccess(BaseModel<OrderDetialBean> o) {
                if (o.getData()!=null){
                    String orderSn=o.getData().getSn();
                    String orderId=String.valueOf(o.getData().getId());
                    double amount=o.getData().getPayAmount();
                    int expireTime=o.getData().getExpireMinute();
                    String expireDate=o.getData().getExpireDate();
                    if (alipayCheckBox.isChecked()){
                        pay(orderId,"ALI_PAY");
                        aliPay(orderId,orderSn,amount,expireTime);
                    }else if (wxCheckBox.isChecked()){
                        pay(orderId,"WX_PAY");
                        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
                        expireDate=dateFormat.format(DateUtil.defaultFormatDate(expireDate));
                        wxPay(orderSn,orderId,(int)(amount*100),expireDate);
                    }
                }
            }
        });
    }

    private void pay(String id,String payType){
        addDisposable(ApiRetrofit.getInstance().getApiService().orderPay(buildPayParam(id, payType)), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
            }
        });
    }

    private ArrayMap<String,String> buildPayParam(String id,String payType){
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
                PayTask alipay = new PayTask(OfflineSettleActivity.this);
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
        final String ip = Util.getIp(this);
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
            Util.showToast(this, "支付失败,无法获取手机ip,请确认wify是否连接!");
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

    public void toPayStateActivity(int type){
        Intent intent=new Intent(this,PayStateActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    /**
     *
     * @param type 1 支付宝 2微信
     */
    private void changePayType(int type){
        switch (type){
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

    @Override
    public String[] getNeedPermission() {
        return new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public void refusePermission() {
        super.refusePermission();
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Action action){
        if (action.type==Constants.ActionType.WXPAYRESULT){
            int code = (int) action.get("code");
            switch (code) {
                case 0: //支付成功
                    toPayStateActivity(1);
                    break;
                case -1: //支付失败
                case -2: //支付取消
                    toPayStateActivity(2);
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK&&requestCode==100){
            name=data.getStringExtra("name");
            phone=data.getStringExtra("phone");
            draweeId=data.getLongExtra("draweeId",0);
            setAddress();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mApp!=null){
            mApp.detach();
        }
        super.onDestroy();
    }

    private void setAddress(){
        StringBuilder builder=new StringBuilder();
        if (phone.length()==11){
            builder.append(name).append("   ").append(phone.substring(0,3)).append("-").append(phone.substring(3,7)).append("-").append(phone.substring(7,11));
        }else {
            builder.append(name).append("   ").append(phone);
        }
        addressNameTv.setText(builder.toString());
    }
}
