package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Address;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.OrderRequest;
import com.d2cmall.shopkeeper.common.PayResult;
import com.d2cmall.shopkeeper.model.PurchaseListBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.ui.view.AddressPop;
import com.d2cmall.shopkeeper.ui.view.CartProductItemView;
import com.d2cmall.shopkeeper.ui.view.CheckBox;
import com.d2cmall.shopkeeper.ui.view.OrderAddressPop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.DateUtil;
import com.d2cmall.shopkeeper.utils.PayUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/5/5.
 * 邮箱:hrb940258169@163.com
 * 线上采购结算
 */

public class OrderPurchaseSettleActivity extends BaseActivity implements AddressPop.CallBack{

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.sure_tv)
    TextView sureTv;
    @BindView(R.id.content_ll)
    LinearLayout contentLl;
    @BindView(R.id.remark_et)
    EditText remarkEt;
    @BindView(R.id.total_price_tv)
    TextView totalPriceTv;
    @BindView(R.id.freight_tv)
    TextView freightTv;
    @BindView(R.id.actual_tv)
    TextView actualTv;
    @BindView(R.id.alipay_iv)
    ImageView alipayIv;
    @BindView(R.id.alipay_tv)
    TextView alipayTv;
    @BindView(R.id.alipay_check_box)
    CheckBox alipayCheckBox;
    @BindView(R.id.wx_iv)
    ImageView wxIv;
    @BindView(R.id.wx_tv)
    TextView wxTv;
    @BindView(R.id.wx_check_box)
    CheckBox wxCheckBox;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.address_info_tv)
    TextView addressInfoTv;

    private String cartIds;
    private int num;
    private long skuId;
    private OrderRequest request;
    private String prepay_id;
    private double totalPrice;
    private IWXAPI mApp;
    private Handler mHandler = new MyHandler(this);
    private String address="";
    private ShopBean shopBean;
    private String provinceInfo;
    private OrderAddressPop orderAddressPop;
    private String provinceName,cityName,districtName;
    private boolean hasAddress;

    @Override
    public void callback(String provinceName, int provinceCode, String cityName, int cityCode, String districtName, int districtCode) {
        if (orderAddressPop!=null){
            orderAddressPop.setAddress(provinceName+cityName+districtName);
        }
        this.provinceName=provinceName;
        this.cityName=cityName;
        this.districtName=districtName;
    }

    static class MyHandler extends Handler {
        WeakReference<OrderPurchaseSettleActivity> weakReference;

        public MyHandler(OrderPurchaseSettleActivity activity) {
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
                weakReference.get().toPayStateActivity(5);
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                weakReference.get().toPayStateActivity(6);
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_purchase;
    }

    @Override
    public void doBusiness() {
        remarkEt.setVisibility(View.VISIBLE);
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        mApp = WXAPIFactory.createWXAPI(this, Constants.WEIXINAPPID, false);
        mApp.registerApp(Constants.WEIXINAPPID);
        request=new OrderRequest();
        cartIds=getIntent().getStringExtra("cartIds");
        num=getIntent().getIntExtra("num",0);
        skuId=getIntent().getLongExtra("skuId",0);
        request.setQuantity(num);
        request.setSkuId(skuId);
        if (!StringUtil.isEmpty(cartIds)){
            String[] ids=cartIds.split(",");
            ArrayList<String> list=new ArrayList<String>();
            for (String id: ids) {
                list.add(id);
            }
            request.setPackageIds(list);
        }
        alipayCheckBox.setCheckColorId(R.mipmap.icon_dagou, R.mipmap.icon_bugou);
        alipayCheckBox.setChecked(true);
        wxCheckBox.setCheckColorId(R.mipmap.icon_dagou, R.mipmap.icon_bugou);
        orderSettle();
    }

    @Override
    protected void onResume() {
        getDeposit();
        new Thread() {
            @Override
            public void run() {
                try {
                    InputStream input = getResources().openRawResource
                            (R.raw.area);
                    provinceInfo = Util.readStreamToString(input);
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        super.onResume();
    }

    public void orderSettle(){
        addDisposable(ApiRetrofit.getInstance().getApiService().purchaseOrderSettle(request), new BaseObserver<BaseModel<PurchaseListBean.RecordsBean>>() {
            @Override
            public void onSuccess(BaseModel<PurchaseListBean.RecordsBean> o) {
                if (o.getData()!=null&&o.getData().getPurchItemList().size()>0){
                    titleTv.setText(o.getData().getFromName());
                    int size = o.getData().getPurchItemList().size();
                    int num=0;
                    totalPrice=0;
                    for (int i = 0; i < size; i++) {
                        num+=o.getData().getPurchItemList().get(i).getQuantity();
                        totalPrice+=o.getData().getPurchItemList().get(i).getSupplyPrice()*o.getData().getPurchItemList().get(i).getQuantity();
                        CartProductItemView productItem = new CartProductItemView(OrderPurchaseSettleActivity.this);
                        o.getData().getPurchItemList().get(i).setProfit(o.getData().getPurchItemList().get(i).getProductPrice()-o.getData().getPurchItemList().get(i).getSupplyPrice());
                        o.getData().getPurchItemList().get(i).setProductPrice(o.getData().getPurchItemList().get(i).getSupplyPrice());
                        productItem.setData(o.getData().getPurchItemList().get(i));
                        productItem.hideCheckBox();
                        contentLl.addView(productItem);
                    }
                    StringBuilder builder=new StringBuilder();
                    builder.append("共").append(num).append("件 ¥").append(Util.getNumberFormat(o.getData().getProductAmount()));
                    float[] sizes=new float[]{1.0f,0.85f};
                    int [] index=new int[]{String.valueOf(num).length()+3,String.valueOf(num).length()+4};
                    totalPriceTv.setText(Util.buildSizeSpan(sizes,index,builder.toString()));
                    freightTv.setText(Util.buildSizeSpan(new float[]{0.85f},new int[]{1},"¥"+Util.getNumberFormat(o.getData().getFreightAmount())));
                    actualTv.setText(Util.buildSizeSpan(new float[]{0.65f},new int[]{1},"¥"+Util.getNumberFormat(o.getData().getPayAmount())));
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                finish();
            }
        });
    }

    @OnClick({R.id.back_iv, R.id.sure_tv, R.id.alipay_tv, R.id.wx_tv,R.id.address_cl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.sure_tv:
                createOrder();
                break;
            case R.id.alipay_tv:
                changePayType(1);
                break;
            case R.id.wx_tv:
                changePayType(2);
                break;
            case R.id.address_cl:
                showPop(view);
                break;
        }
    }

    private void showPop(View view){
        if (orderAddressPop==null){
            orderAddressPop=new OrderAddressPop(this);
            orderAddressPop.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String n=orderAddressPop.getName();
                    String p=orderAddressPop.getPhone();
                    String region=orderAddressPop.getRegion();
                    String a=orderAddressPop.getDetailAddress();
                    if (StringUtil.isEmpty(n)||StringUtil.isEmpty(p)||StringUtil.isEmpty(region)||StringUtil.isEmpty(a)){
                        Util.showToast(OrderPurchaseSettleActivity.this,"地址不完善");
                        return;
                    }
                    orderAddressPop.dismiss();
                    if (shopBean!=null){
                        JsonObject biz_content=new JsonObject();
                        biz_content.addProperty("province",provinceName);
                        biz_content.addProperty("city",cityName);
                        biz_content.addProperty("district",districtName);
                        biz_content.addProperty("address",a);
                        biz_content.addProperty("name",n);
                        biz_content.addProperty("mobile",p);
                        String s=biz_content.toString();
                        address=s;
                        setAddress();
                        shopBean.setReciveAddress(s);
                        updateShop();
                    }
                }
            });
            orderAddressPop.setAddressListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderAddressPop.hideSoft();
                    showAddressPop(view);
                }
            });
        }
        if (shopBean!=null){
            if (!StringUtil.isEmpty(shopBean.getReciveAddress())) {
                if (shopBean.getReciveAddress().startsWith("{")){
                    Gson gson=new Gson();
                    Address add=gson.fromJson(shopBean.getReciveAddress(),Address.class);
                    orderAddressPop.setName(add.name);
                    orderAddressPop.setPhone(add.mobile);
                    provinceName=add.province;
                    cityName=add.city;
                    districtName=add.district;
                    orderAddressPop.setAddress(add.province+add.city+add.district);
                    orderAddressPop.setNegion(add.address);
                }
            }
        }
        orderAddressPop.show(view);
    }

    private void updateShop(){
        addDisposable(ApiRetrofit.getInstance().getApiService().updateShop(shopBean), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
            }
        });
    }

    private void showAddressPop(View view){
        AddressPop addressPop = new AddressPop(this, provinceInfo);
        addressPop.setCallBack(this);
        addressPop.show(view);
    }

    private void createOrder() {
        if (!hasAddress){
            Util.showToast(this,"请完善地址");
            return;
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().purchaseOrderCreate(request), new BaseObserver<BaseModel<PurchaseListBean.RecordsBean>>() {
            @Override
            public void onSuccess(BaseModel<PurchaseListBean.RecordsBean> o) {
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
                        wxPay(orderSn,orderId,Util.getIntValue(amount*100),expireDate);
                    }
                }
            }
        });
    }

    private void pay(String id,String payType){
        addDisposable(ApiRetrofit.getInstance().getApiService().purchasePay(buildPayParam(id, payType)), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
            }
        });
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
    private void aliPay(String orderId, String orderSn, double totalAmount, int expireTime) {
        String orderInfo = PayUtil.getOrderInfo(orderSn, orderId, totalAmount, expireTime);
        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderPurchaseSettleActivity.this);
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
    private void wxPay(String orderSn, String orderId, int price, String expireTime) {
        if (!StringUtil.isEmpty(prepay_id)) {
            wxPay(prepay_id);
            return;
        }
        final String ip = Util.getIp(this);
        if (!StringUtil.isEmpty(ip)) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        getPrepayId(orderSn, orderId, price, ip, expireTime);
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

    private void getPrepayId(String orderSn, String orderId, int price, String ip, String expireTime) throws Throwable {
        prepay_id = PayUtil.getPrepayId(orderSn, orderId, expireTime, price, ip);
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

    public void toPayStateActivity(int type) {
        Intent intent = new Intent(this, PayStateActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
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

    @Override
    public String[] getNeedPermission() {
        return new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
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
                    toPayStateActivity(5);
                    break;
                case -1: //支付失败
                case -2: //支付取消
                    toPayStateActivity(6);
                    break;
            }
        }
    }

    private void getDeposit() {
        if (Session.getInstance().getUserFromFile(this) != null) {
            long shopId = Session.getInstance().getUserFromFile(this).getShopId();
            addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(shopId), new BaseObserver<BaseModel<ShopBean>>() {
                @Override
                public void onSuccess(BaseModel<ShopBean> o) {
                    if (o.getData() != null) {
                        shopBean=o.getData();
                        address=o.getData().getReciveAddress();
                        setAddress();
                    }
                }
            });
        }
    }

    private void setAddress() {
        StringBuilder builder = new StringBuilder();
        if (!StringUtil.isEmpty(address)) {
            if (address.startsWith("{")){
                Gson gson=new Gson();
                Address add=gson.fromJson(address,Address.class);
                addressInfoTv.setText(add.province+add.city+add.district+add.address);
                if (!StringUtil.isEmpty(add.name)){
                    builder = new StringBuilder();
                    if (add.mobile.length() == 11) {
                        builder.append(add.name).append("   ").append(add.mobile.substring(0, 3)).append("-").append(add.mobile.substring(3, 7)).append("-").append(add.mobile.substring(7, 11));
                    } else {
                        builder.append(add.name).append("   ").append(add.mobile);
                    }
                    nameTv.setText(builder.toString());
                    hasAddress=true;
                }else {
                    nameTv.setText("请设置收货人姓名和手机号");
                    hasAddress=false;
                }
            }else {
                hasAddress=false;
            }
        }else {
            hasAddress=false;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (orderAddressPop!=null){
            orderAddressPop.setAddressListener(null);
            orderAddressPop.setListener(null);
        }
        if (mApp!=null){
            mApp.detach();
        }
        super.onDestroy();
    }
}
