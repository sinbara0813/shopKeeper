package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Address;
import com.d2cmall.shopkeeper.common.OrderRequest;
import com.d2cmall.shopkeeper.model.AllotListBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.ui.view.AddressPop;
import com.d2cmall.shopkeeper.ui.view.CartProductItemView;
import com.d2cmall.shopkeeper.ui.view.OrderAddressPop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/5/5.
 * 邮箱:hrb940258169@163.com
 * 线上免费拿货结算
 */

public class OrderAllotSettleActivity extends BaseActivity implements AddressPop.CallBack {

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
    @BindView(R.id.describe_tv)
    TextView describeTv;
    @BindView(R.id.cash_deposit_tv)
    TextView cashDepositTv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.address_info_tv)
    TextView addressInfoTv;

    private String cartIds;
    private int num;
    private long skuId;
    private OrderRequest request;
    private double deposit;
    private double totalPrice;
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_allot;
    }

    @Override
    public void doBusiness() {
        remarkEt.setVisibility(View.VISIBLE);
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        request = new OrderRequest();
        cartIds = getIntent().getStringExtra("cartIds");
        num = getIntent().getIntExtra("num", 0);
        skuId = getIntent().getLongExtra("skuId", 0);
        request.setQuantity(num);
        request.setSkuId(skuId);
        if (!StringUtil.isEmpty(cartIds)) {
            String[] ids = cartIds.split(",");
            ArrayList<String> list = new ArrayList<String>();
            for (String id : ids) {
                list.add(id);
            }
            request.setPackageIds(list);
        }
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

    private void orderSettle() {
        addDisposable(ApiRetrofit.getInstance().getApiService().allotOrderSettle(request), new BaseObserver<BaseModel<AllotListBean.RecordsBean>>() {
            @Override
            public void onSuccess(BaseModel<AllotListBean.RecordsBean> o) {
                if (o.getData() != null && o.getData().getAllotItemList().size() > 0) {
                    titleTv.setText(o.getData().getFromName());
                    int size = o.getData().getAllotItemList().size();
                    int num = 0;
                    totalPrice = 0;
                    for (int i = 0; i < size; i++) {
                        totalPrice += o.getData().getAllotItemList().get(i).getSupplyPrice() * o.getData().getAllotItemList().get(i).getQuantity();
                        num += o.getData().getAllotItemList().get(i).getQuantity();
                        CartProductItemView productItem = new CartProductItemView(OrderAllotSettleActivity.this);
                        o.getData().getAllotItemList().get(i).setProfit(o.getData().getAllotItemList().get(i).getProductPrice() - o.getData().getAllotItemList().get(i).getSupplyPrice());
                        o.getData().getAllotItemList().get(i).setProductPrice(o.getData().getAllotItemList().get(i).getSupplyPrice());
                        productItem.setData(o.getData().getAllotItemList().get(i));
                        productItem.hideCheckBox();
                        contentLl.addView(productItem);
                    }
                    int length = String.valueOf(num).length();
                    totalPriceTv.setText(Util.buildSpan(new int[]{getResources().getColor(R.color.color_black)}
                            , new int[]{2 + length}, new float[]{0.77f, 0.65f}, new int[]{2 + length, 4 + length}, "共" + num + "件 " + "¥" + Util.getNumberFormat(totalPrice)));
                    describeTv.setText("继续操作将冻结" + Util.getNumberFormat(totalPrice) + "元保证金 商品售出后，结算货品后增加保证金");
                    if (totalPrice > deposit) {
                        sureTv.setEnabled(false);
                        sureTv.setBackgroundResource(R.color.enable_color);
                    }
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                finish();
            }
        });
    }

    private void orderCreate() {
        if (!hasAddress){
            Util.showToast(this,"请完善地址");
            return;
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().allotOrderCreate(request), new BaseObserver<BaseModel<AllotListBean.RecordsBean>>() {
            @Override
            public void onSuccess(BaseModel<AllotListBean.RecordsBean> o) {
                toPayStateActivity(7);
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                toPayStateActivity(8);
            }
        });
    }

    private void getDeposit() {
        if (Session.getInstance().getUserFromFile(this) != null) {
            long shopId = Session.getInstance().getUserFromFile(this).getShopId();
            addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(shopId), new BaseObserver<BaseModel<ShopBean>>() {
                @Override
                public void onSuccess(BaseModel<ShopBean> o) {
                    if (o.getData() != null) {
                        shopBean=o.getData();
                        deposit = o.getData().getDeposit();
                        cashDepositTv.setText(Util.buildSizeSpan(new float[]{0.85f}, new int[]{1}, "¥" + Util.getNumberFormat(deposit)));
                        if (totalPrice > deposit) {
                            sureTv.setEnabled(false);
                            sureTv.setBackgroundResource(R.color.enable_color);
                        }
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

    @OnClick({R.id.back_iv, R.id.sure_tv,R.id.address_cl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.sure_tv:
                orderCreate();
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
                        Util.showToast(OrderAllotSettleActivity.this,"地址不完善");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void toPayStateActivity(int type) {
        Intent intent = new Intent(this, PayStateActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (orderAddressPop!=null){
            orderAddressPop.setAddressListener(null);
            orderAddressPop.setListener(null);
        }
        super.onDestroy();
    }
}
