package com.d2cmall.shopkeeper.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.common.SharePrefConstant;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.OrderDetialBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.ui.view.AddressPop;
import com.d2cmall.shopkeeper.ui.view.AgreeAfterSalePop;
import com.d2cmall.shopkeeper.ui.view.EditPricePop;
import com.d2cmall.shopkeeper.ui.view.RefuseAfterSalePop;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.TransparentPop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.SharedPreUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/4/29.
 * Description : AfterSaleDetailActivity
 * 退货退款详情(向供货商申请的退货退款&客户申请的退货退款)
 */

public class AfterSaleDetailActivity extends BaseActivity implements BaseView {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.img_product)
    ImageView imgProduct;
    @BindView(R.id.tv_product_title)
    TextView tvProductTitle;
    @BindView(R.id.tv_spec)
    TextView tvSpec;
    @BindView(R.id.tv_product_num)
    TextView tvProductNum;
    @BindView(R.id.tv_product_status)
    TextView tvProductStatus;
    @BindView(R.id.tv_status_title)
    TextView tvStatusTitle;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_refuse_reason)
    TextView tvRefuseReason;
    @BindView(R.id.ll_refuse_reason)
    LinearLayout llRefuseReason;
    @BindView(R.id.tv_logistics_code)
    TextView tvLogisticsCode;
    @BindView(R.id.tv_logistics_company)
    TextView tvLogisticsCompany;
    @BindView(R.id.ll_logistics_info)
    LinearLayout llLogisticsInfo;
    @BindView(R.id.tv_apply_num)
    TextView tvApplyNum;
    @BindView(R.id.tv_apply_amount)
    TextView tvApplyAmount;
    @BindView(R.id.tv_check_amount)
    TextView tvCheckAmount;
    @BindView(R.id.ll_check_amount)
    LinearLayout llCheckAmount;
    @BindView(R.id.tv_reason_title)
    TextView tvReasonTitle;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.tv_after_sale_sn)
    TextView tvAfterSaleSn;
    @BindView(R.id.tv_apply_date)
    TextView tvApplyDate;
    @BindView(R.id.tv_apply_desc)
    TextView tvApplyDesc;
    @BindView(R.id.tv_return_money_type)
    TextView tvReturnMoneyType;
    @BindView(R.id.ll_img_container)
    LinearLayout llImgContainer;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.ll_button_container)
    LinearLayout llButtonContainer;
    @BindView(R.id.tv_return_address)
    TextView tvReturnAddress;
    @BindView(R.id.tv_consignee)
    TextView tvConsignee;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.ll_return_address_info)
    LinearLayout llReturnAddressInfo;
    @BindView(R.id.ll_return_num)
    LinearLayout llReturnNum;
    @BindView(R.id.tv_apply_desc_title)
    TextView tvApplyDescTitle;
    @BindView(R.id.tv_after_sale_sn_title)
    TextView tvAfterSaleSnTitle;
    @BindView(R.id.tv_return_money_type_title)
    TextView tvReturnMoneyTypeTitle;


    private int type;
    private long orderId;
    private OrderDetialBean.OrderItemListBean orderItemListBean;
    private String provinceInfo;
    private ShopBean shopBean;
    private AgreeAfterSalePop orderAddressPop;
    private String provinceName,cityName,districtName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_after_sale_detail;
    }

    @Override
    public void doBusiness() {
        initView();
        orderId = getIntent().getLongExtra("orderId", 0);
        type = getIntent().getIntExtra("type", 0);
        if (orderId > 0) {
            loadOrderDetial();
        }
    }

    private void initView() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
    }

    private void loadOrderDetial() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getOrderDetial(orderId), new BaseObserver<BaseModel<OrderDetialBean>>(this) {
            @Override
            public void onSuccess(BaseModel<OrderDetialBean> orderDetialBean) {
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
                orderItemListBean = orderDetialBean.getData().getOrderItemList().get(0);
                if (orderItemListBean != null) {
                    tvProductTitle.setText(orderItemListBean.getProductName());
                    tvProductNum.setText("x" + orderItemListBean.getQuantity());
                    if (!StringUtil.isEmpty(orderItemListBean.getProductPic())) {
                        ImageLoader.displayImage(Glide.with(AfterSaleDetailActivity.this), getValidPic(orderItemListBean.getProductPic().split(",")), imgProduct, R.mipmap.ic_logo_empty5);
                    }
                    if (orderItemListBean.getAfterType() == 1) {
                        tvName.setText("退款详情");
                        llReturnNum.setVisibility(View.GONE);
                        tvApplyDescTitle.setText("退款说明");
                        tvAfterSaleSnTitle.setText("退款编号");
                        tvStatusTitle.setText("退款状态");
                    } else if (orderItemListBean.getAfterType() == 2) {
                        tvName.setText("退货退款详情");
                        llReturnNum.setVisibility(View.VISIBLE);
                        tvApplyDescTitle.setText("退货说明");
                        tvAfterSaleSnTitle.setText("退货编号");
                        tvStatusTitle.setText("退货状态");
                    }
                    tvStatus.setText(orderItemListBean.getStatusName());
                    tvApplyNum.setText(orderItemListBean.getAfterQuantity() + "件");
                    tvApplyDate.setText(orderItemListBean.getAfterDate());
                    tvApplyAmount.setText(Util.getNumberFormat(orderItemListBean.getAfterAmount()));
                    tvSpec.setText(orderItemListBean.getStandard());
                    tvApplyDesc.setText(orderItemListBean.getAfterMemo());
                    tvAfterSaleSn.setText(orderItemListBean.getOrderSn());
                    tvReturnMoneyType.setText("原路退回");
                    setAfterPics();//售后图片
                    setBottomButtons();//设置按钮状态
                }

            }

        });

    }

    private String getValidPic(String[] pic) {
        int size = pic.length;
        for (int i = 0; i < size; i++) {
            if (!StringUtil.isEmpty(pic[i])) {
                return pic[i];
            }
        }
        return null;
    }

    private void setBottomButtons() {
        llButtonContainer.removeAllViews();
        if ("WAIT_REFUND".equals(orderItemListBean.getStatus()) || "WAIT_RESHIP".equals(orderItemListBean.getStatus())) { //待审核(退货退款、退款)
            TextView agreeText = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(30));
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(30);
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            agreeText.setLayoutParams(layoutParams);
            agreeText.setGravity(Gravity.CENTER);
            agreeText.setTextColor(getResources().getColor(R.color.normal_black));
            agreeText.setText("通过");
            agreeText.setBackgroundResource(R.drawable.sp_line);
            llButtonContainer.addView(agreeText);
            agreeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    agreeRequest(agreeText);
                }
            });

            TextView refuseText = new TextView(this);
            refuseText.setLayoutParams(layoutParams);
            refuseText.setGravity(Gravity.CENTER);
            refuseText.setTextColor(getResources().getColor(R.color.normal_black));
            refuseText.setText("拒绝");
            refuseText.setBackgroundResource(R.drawable.sp_line);
            refuseText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refuseReques(refuseText);
                }
            });
            llButtonContainer.addView(refuseText);
        }

        if ("RESHIPED".equals(orderItemListBean.getStatus())) { //退货退款待收货
            TextView receiveText = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(30));
            layoutParams.width = ScreenUtil.dip2px(80);
            layoutParams.height = ScreenUtil.dip2px(30);
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            receiveText.setLayoutParams(layoutParams);
            receiveText.setGravity(Gravity.CENTER);
            receiveText.setTextColor(getResources().getColor(R.color.color_white));
            receiveText.setText("确认收货");
            receiveText.setBackgroundResource(R.drawable.sp_round2_bg_blue);
            llButtonContainer.addView(receiveText);
            receiveText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    receiveSure(receiveText);
                }
            });

        }
    }

    private void receiveSure(TextView receiveText) {

        new AlertDialog.Builder(this)
                .setMessage("确认收到该商品?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayMap<String, String> arrayMap = new ArrayMap<>();
                        arrayMap.put("orderItemId", String.valueOf(orderItemListBean.getId()));
                        receiveText.setEnabled(false);
                        addDisposable(ApiRetrofit.getInstance().getApiService().receiveSure(arrayMap), new BaseObserver() {
                            @Override
                            public void onSuccess(Object o) {
                                Util.showToast(AfterSaleDetailActivity.this, "已确认");
                                loadOrderDetial();
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


    private void refuseReques(TextView refuseText) {
        RefuseAfterSalePop refuseAfterSalePop = null;
        if ("WAIT_REFUND".equals(orderItemListBean.getStatus())) {//拒绝退款
            refuseAfterSalePop = new RefuseAfterSalePop(this, "WAIT_REFUND", orderItemListBean.getId());
        } else if ("WAIT_RESHIP".equals(orderItemListBean.getStatus())) {//拒绝退货退款
            refuseAfterSalePop = new RefuseAfterSalePop(this, "WAIT_RESHIP", orderItemListBean.getId());
        }
        if (refuseAfterSalePop != null) {
            RefuseAfterSalePop finalRefuseAfterSalePop = refuseAfterSalePop;
            refuseAfterSalePop.setDismissCallBack(new TransparentPop.DismissListener() {
                @Override
                public void dismissStart() {
                    if (finalRefuseAfterSalePop != null) {
                        boolean hasRefuse = finalRefuseAfterSalePop.isHasRefuse();
                        if (hasRefuse) {
                            loadOrderDetial();
                        }
                    }
                }

                @Override
                public void dismissEnd() {

                }
            });
            refuseAfterSalePop.show(getWindow().getDecorView());
        }

    }

    private void agreeRequest(TextView agreeText) {
        if ("WAIT_REFUND".equals(orderItemListBean.getStatus())) { //同意退款
            EditPricePop editPricePop = new EditPricePop(this, orderItemListBean.getId(), orderItemListBean.getAfterAmount(), orderItemListBean.getRealPrice(), 1);
            editPricePop.setDismissCallBack(new TransparentPop.DismissListener() {
                @Override
                public void dismissStart() {
                    if (editPricePop != null) {
                        boolean hasEdit = editPricePop.isHasEdit();
                        if (hasEdit) {
                            loadOrderDetial();
                        }
                    }
                }

                @Override
                public void dismissEnd() {

                }
            });
            editPricePop.show(getWindow().getDecorView());


        } else if ("WAIT_RESHIP".equals(orderItemListBean.getStatus())) {//同意退货退款
            String backAddress= SharedPreUtil.getString(SharePrefConstant.SHOP_BACK_ADDRESS,"");
            if (StringUtil.isEmpty(backAddress)){
                Util.showToast(this,"请先设置退货地址");
                Intent intent=new Intent(this, BrandSettingActivity.class);
                intent.putExtra("scroll",true);
                startActivity(intent);
                return;
            }
            if (orderAddressPop==null){
                orderAddressPop = new AgreeAfterSalePop(this, orderItemListBean.getId(), orderItemListBean.getAfterAmount(), orderItemListBean.getRealPrice());
                orderAddressPop.setListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String n=orderAddressPop.getName();
                        String p=orderAddressPop.getPhone();
                        String region=orderAddressPop.getRegion();
                        String a=orderAddressPop.getDetailAddress();
                        if (StringUtil.isEmpty(n)||StringUtil.isEmpty(p)||StringUtil.isEmpty(region)||StringUtil.isEmpty(a)){
                            Util.showToast(AfterSaleDetailActivity.this,"地址不完善");
                            return;
                        }
                        JsonObject biz_content=new JsonObject();
                        biz_content.addProperty("province",StringUtil.isEmpty(provinceName)?orderAddressPop.provinceName:provinceName);
                        biz_content.addProperty("city",StringUtil.isEmpty(cityName)?orderAddressPop.cityName:cityName);
                        biz_content.addProperty("district",StringUtil.isEmpty(districtName)?orderAddressPop.districtName:districtName);
                        biz_content.addProperty("address",a);
                        biz_content.addProperty("name",n);
                        biz_content.addProperty("mobile",p);
                        String s=biz_content.toString();
                        ArrayMap<String, String> arrayMap = new ArrayMap<>();
                        arrayMap.put("orderItemId", String.valueOf(orderItemListBean.getId()));
                        arrayMap.put("reshipName", n);
                        arrayMap.put("reshipMobile", p);
                        arrayMap.put("reshipAddress", s);
                        addDisposable(ApiRetrofit.getInstance().getApiService().agreeReship(arrayMap), new BaseObserver() {
                            @Override
                            public void onSuccess(Object o) {
                                orderAddressPop.dismiss();
                                Util.showToast(AfterSaleDetailActivity.this, "已同意");
                                loadOrderDetial();
                            }

                            @Override
                            public void onError(int errorCode, String msg) {
                                super.onError(errorCode, msg);
                            }
                        });
                        if (shopBean!=null){
                            shopBean.setBackAddress(s);
                            addDisposable(ApiRetrofit.getInstance().getApiService().updateShop(shopBean), new BaseObserver() {
                                @Override
                                public void onSuccess(Object o) {

                                }
                            });
                        }
                    }
                });
                orderAddressPop.setAddressListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderAddressPop.hideSoft();
                        AddressPop addressPop = new AddressPop(AfterSaleDetailActivity.this, provinceInfo);
                        addressPop.setCallBack(new AddressPop.CallBack() {
                            @Override
                            public void callback(String provinceName, int provinceCode, String cityName, int cityCode, String districtName, int districtCode) {
                                if (orderAddressPop!=null){
                                    orderAddressPop.setAddress(provinceName+cityName+districtName);
                                }
                                AfterSaleDetailActivity.this.provinceName=provinceName;
                                AfterSaleDetailActivity.this.cityName=cityName;
                                AfterSaleDetailActivity.this.districtName=districtName;
                            }
                        });
                        addressPop.show(agreeText);
                    }
                });
            }
            orderAddressPop.show(getWindow().getDecorView());
        }

    }

    private void setAfterPics() {
        String afterPic = orderItemListBean.getAfterPic();
        llImgContainer.removeAllViews();
        if (!StringUtil.isEmpty(afterPic)) {
            String[] split = afterPic.split(",");
            for (int i = 0; i < split.length; i++) {
                ImageView imageView = new ImageView(this);
                llImgContainer.addView(imageView);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ScreenUtil.dip2px(70), ScreenUtil.dip2px(70));
                params.height = ScreenUtil.dip2px(70);
                params.width = ScreenUtil.dip2px(70);
                params.setMargins(0, 0, ScreenUtil.dip2px(10), 0);
                imageView.setLayoutParams(params);
                ImageLoader.displayImage(this, split[i], imageView, R.mipmap.ic_logo_empty5);
            }
        }

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

    private void getDeposit() {
        if (Session.getInstance().getUserFromFile(this) != null) {
            long shopId = Session.getInstance().getUserFromFile(this).getShopId();
            addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(shopId), new BaseObserver<BaseModel<ShopBean>>() {
                @Override
                public void onSuccess(BaseModel<ShopBean> o) {
                    if (o.getData() != null) {
                        shopBean=o.getData();
                    }
                }
            });
        }
    }
    
    @OnClick({R.id.iv_back, R.id.img_product})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.img_product:
                break;
        }
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

}
