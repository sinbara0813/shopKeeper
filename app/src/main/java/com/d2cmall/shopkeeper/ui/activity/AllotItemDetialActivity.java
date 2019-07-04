package com.d2cmall.shopkeeper.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Address;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.AllotDetialBean;
import com.d2cmall.shopkeeper.model.AllotItemDetialBean;
import com.d2cmall.shopkeeper.model.PurchaseDetailBean;
import com.d2cmall.shopkeeper.model.PurchaseItemDetialBean;
import com.d2cmall.shopkeeper.model.PurchaseListBean;
import com.d2cmall.shopkeeper.ui.view.TransparentPop;
import com.d2cmall.shopkeeper.ui.view.WarehousingConfirmPop;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/5/8.
 * Description : AllotDetialActivity
 * 调拨单详情页
 */

public class AllotItemDetialActivity extends BaseActivity {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_order_status)
    TextView tvOrderStatus;
    @BindView(R.id.tv_whereabouts_desc)
    TextView tvWhereaboutsDesc;
    @BindView(R.id.tv_brand_name)
    TextView tvBrandName;
    @BindView(R.id.tv_consignee)
    TextView tvConsignee;
    @BindView(R.id.tv_consignee_phone)
    TextView tvConsigneePhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_product_container)
    LinearLayout llProductContainer;
    @BindView(R.id.tv_order_code)
    TextView tvOrderCode;
    @BindView(R.id.tv_copy_order_code)
    TextView tvCopyOrderCode;
    @BindView(R.id.tv_order_date)
    TextView tvOrderDate;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_product_total)
    TextView tvProductTotal;
    @BindView(R.id.line_layout)
    View lineLayout;
    @BindView(R.id.tv_bond_desc)
    TextView tvBondDesc;
    @BindView(R.id.tv_bond)
    TextView tvBond;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.tv_freight_desc)
    TextView tvFreightDesc;
    @BindView(R.id.tv_freight_amount)
    TextView tvFreightAmount;
    @BindView(R.id.tv_total_desc)
    TextView tvTotalDesc;
    @BindView(R.id.ll_freight)
    LinearLayout llFreight;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    @BindView(R.id.tv_copy_pay_type)
    TextView tvCopyPayType;
    @BindView(R.id.ll_pay_type)
    LinearLayout llPayType;
    @BindView(R.id.ll_receive)
    LinearLayout llReceive;
    @BindView(R.id.tv_receive_num)
    TextView tvReceiveNum;


    private long orderId;


    private boolean needRefresh;

    @Override
    public int getLayoutId() {
        return R.layout.activity_allocation_return_detail;
    }

    @Override
    public void doBusiness() {
        init();
    }

    private void init() {
        tvName.setText("订单详情");
        orderId = getIntent().getLongExtra("orderId", 0);
        String orderType = getIntent().getStringExtra("orderType");
        if (orderId > 0) {
            if ("allot".equals(orderType)) {//调拨单
                loadAllotItemDetail();
            } else if ("purchase".equals(orderType)) {//采购单
                loadPurchItemDetail();
            }
        }

    }


    private void loadAllotItemDetail() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getAllotItemDetial(orderId), new BaseObserver<BaseModel<AllotItemDetialBean>>() {
            @Override
            public void onSuccess(BaseModel<AllotItemDetialBean> bean) {
                if (bean != null) {
                    AllotItemDetialBean allotBean = bean.getData();
                    tvOrderStatus.setText(allotBean.getAllotItemList().get(0).getStatusName());
                    tvWhereaboutsDesc.setText("调拨去向");
                    tvTotalDesc.setText("调拨总计");
                    tvConsignee.setText(allotBean.getToName());
                    if (!StringUtil.isEmpty(allotBean.getToAddress())) {
                        if (allotBean.getToAddress().startsWith("{")){
                            Gson gson=new Gson();
                            Address add=gson.fromJson(allotBean.getToAddress(),Address.class);
                            tvAddress.setText(add.province+add.city+add.district+add.address);
                            tvBrandName.setText(add.province+add.city+add.district+add.address);
                        }else {
                            tvAddress.setText(allotBean.getToAddress());
                            tvBrandName.setText(allotBean.getToAddress());
                        }
                    }
                    tvOrderCode.setText(getString(R.string.label_order_code, allotBean.getSn()));
                    tvOrderDate.setText(getString(R.string.label_allot_date, allotBean.getCreateDate()));
                    tvConsigneePhone.setText(allotBean.getToMobile());
                    tvBondDesc.setText("保证金");
                    tvBond.setText(getString(R.string.label_money, Util.getNumberFormat(allotBean.getProductAmount())));
                    //有差异difference=1 差异处理完成difference=8
                    if(allotBean.getAllotItemList().get(0).getDifference()>=1){
                        llReceive.setVisibility(View.VISIBLE);
                        tvReceiveNum.setText(allotBean.getAllotItemList().get(0).getActualQuantity()+"件");
                    }

                    AllotDetialBean.AllotItemListBean allotItemListBean = allotBean.getAllotItemList().get(0);
                    llProductContainer.removeAllViews();
                    if (allotItemListBean != null) {
                            View view = LayoutInflater.from(AllotItemDetialActivity.this).inflate(R.layout.layout_purchase_allot_item, llProductContainer, false);
                            ImageView ivProduct = view.findViewById(R.id.iv_product);
                            TextView tvProductName = view.findViewById(R.id.tv_product_name);
                            TextView tvProductPrice = view.findViewById(R.id.tv_product_price);
                            TextView tvProductStock = view.findViewById(R.id.tv_product_num);
                            TextView tvProductSpec = view.findViewById(R.id.tv_product_spec);
                            tvProductSpec.setText(allotItemListBean.getStandard());
                            tvProductName.setText(allotItemListBean.getProductName());
                            tvProductPrice.setText(getString(R.string.label_money, Util.getNumberFormat(allotItemListBean.getSupplyPrice())));
                            tvProductStock.setText(getString(R.string.label_quantity, String.valueOf(allotItemListBean.getQuantity())));
                            if (!StringUtil.isEmpty(allotItemListBean.getProductPic())) {
                                ImageLoader.displayImage(Glide.with(AllotItemDetialActivity.this), getValidPic(allotItemListBean.getProductPic().split(",")), ivProduct, R.mipmap.ic_logo_empty5, R.mipmap.ic_logo_empty5);
                            }
                            llProductContainer.addView(view);
                    }
                    tvProductTotal.setText(getString(R.string.label_order_total, String.valueOf(allotItemListBean.getQuantity()), Util.getNumberFormat(allotBean.getProductAmount())));
                    setBottomButtons(allotBean.getStatus(), 0, allotBean.getId(),allotItemListBean.getId(),allotItemListBean.getLogisticsNum(),allotItemListBean.getDifference()==1,allotItemListBean.getStandard(),allotItemListBean.getQuantity());
                }

            }

        });

    }

    private void loadPurchItemDetail() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getPurchItemDetial(orderId), new BaseObserver<BaseModel<PurchaseItemDetialBean>>() {
            @Override
            public void onSuccess(BaseModel<PurchaseItemDetialBean> bean) {
                if (bean != null) {
                    PurchaseItemDetialBean purchaseBean = bean.getData();
                    tvOrderStatus.setText(purchaseBean.getPurchItemList().get(0).getStatusName());
                    tvWhereaboutsDesc.setText("采购去向");
                    tvTotalDesc.setText("采购总计");
                    if(!StringUtil.isEmpty(purchaseBean.getPaymentSn()) && !StringUtil.isEmpty(purchaseBean.getPaymentTypeName())){
                        llPayType.setVisibility(View.VISIBLE);
                        tvPayType.setText(getString(R.string.label_order_pay_type,purchaseBean.getPaymentTypeName()));
                        tvCopyPayType.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                cm.setPrimaryClip(ClipData.newPlainText("code", purchaseBean.getPaymentSn()));
                                Util.showToast(AllotItemDetialActivity.this, "已复制");
                            }
                        });
                    }
                    tvConsignee.setText(purchaseBean.getToName());
                    if (!StringUtil.isEmpty(purchaseBean.getToAddress())) {
                        if (purchaseBean.getToAddress().startsWith("{")){
                            Gson gson=new Gson();
                            Address add=gson.fromJson(purchaseBean.getToAddress(),Address.class);
                            tvAddress.setText(add.province+add.city+add.district+add.address);
                            tvBrandName.setText(add.province+add.city+add.district+add.address);
                        }else {
                            tvAddress.setText(purchaseBean.getToAddress());
                            tvBrandName.setText(purchaseBean.getToAddress());
                        }
                    }
                    tvOrderCode.setText(getString(R.string.label_order_code, purchaseBean.getSn()));
                    tvOrderDate.setText(getString(R.string.label_allot_date, purchaseBean.getCreateDate()));
                    tvConsigneePhone.setText(purchaseBean.getToMobile());
                    tvBondDesc.setText("实际支付");
                    tvBond.setText(getString(R.string.label_money, Util.getNumberFormat(purchaseBean.getProductAmount())));
                    //有差异difference=1 差异处理完成difference=8
                    if(purchaseBean.getPurchItemList().get(0).getDifference()>=1){
                        llReceive.setVisibility(View.VISIBLE);
                        tvReceiveNum.setText(purchaseBean.getPurchItemList().get(0).getActualQuantity()+"件");
                    }
//                    if(freight>0){//运费
//                        llFreight.setVisibility(View.VISIBLE);
//                        tvFreightAmount.setText(getText(R.string.label_money,Util.getNumberFormat(freight)));
//                    }
                    List<PurchaseDetailBean.PurchItemListBean> purchItemList = purchaseBean.getPurchItemList();
                    llProductContainer.removeAllViews();
                    if (purchItemList != null) {
                        PurchaseDetailBean.PurchItemListBean purchItemListBean = purchItemList.get(0);
                            View view = LayoutInflater.from(AllotItemDetialActivity.this).inflate(R.layout.layout_purchase_allot_item, llProductContainer, false);
                            ImageView ivProduct = view.findViewById(R.id.iv_product);
                            TextView tvProductName = view.findViewById(R.id.tv_product_name);
                            TextView tvProductPrice = view.findViewById(R.id.tv_product_price);
                            TextView tvProductStock = view.findViewById(R.id.tv_product_num);
                            TextView tvProductSpec = view.findViewById(R.id.tv_product_spec);
                            tvProductSpec.setText(purchItemListBean.getStandard());
                            tvProductName.setText(purchItemListBean.getProductName());
                            tvProductPrice.setText(getString(R.string.label_money, Util.getNumberFormat(purchItemListBean.getSupplyPrice())));
                            tvProductStock.setText(getString(R.string.label_quantity, String.valueOf(purchItemListBean.getQuantity())));
                            if (!StringUtil.isEmpty(purchItemListBean.getProductPic())) {
                                ImageLoader.displayImage(Glide.with(AllotItemDetialActivity.this), getValidPic(purchItemListBean.getProductPic().split(",")), ivProduct, R.mipmap.ic_logo_empty5, R.mipmap.ic_logo_empty5);
                            }
                            llProductContainer.addView(view);
                    }
                    tvProductTotal.setText(getString(R.string.label_order_total, String.valueOf(purchItemList.get(0).getQuantity()), Util.getNumberFormat(purchaseBean.getProductAmount())));
                    setBottomButtons(purchaseBean.getStatus(), 1, purchaseBean.getId(),purchItemList.get(0).getId(), purchItemList.get(0).getLogisticsNum(),purchItemList.get(0).getDifference()==1,purchItemList.get(0).getStandard(),purchItemList.get(0).getQuantity());
                }

            }

        });

    }

    /**
     * @param status 订单状态
     * @param type 订单类型采购or调拨
     * @param orderId 整单ID
     * @param itemId //item申请数量
     * @param logisticsNum 物流单号
     * @param isDiff 是否是异常单
     * @param standard //item规格
     * @param quantity //item申请数量
     */
    private void setBottomButtons(String status, int type, long orderId,long itemId ,String logisticsNum, boolean isDiff, String standard, int quantity) {
        //type=0调拨单 ,1是采购单
        if (StringUtil.isEmpty(status)) return;
        llBottom.removeAllViews();

        if(isDiff){
            //实收数量和申请数量不一致的异常单
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(32));
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#8d92a3"));
            textView.setText("取消差异");
            textView.setBackgroundResource(R.drawable.sp_line);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消采购单
                    new AlertDialog.Builder(AllotItemDetialActivity.this)
                            .setMessage("取消后差异的数量会增加到商品上哦!")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (type == 1) {
                                        cancelPurchaseDiff(itemId);
                                    } else {
                                        cancelAllotDiff(itemId);
                                    }
                                }
                            }).show();
                }
            });
            llBottom.addView(textView);
        }

        if ("DELIVER".equals(status)) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(32));
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            TextView textView1 = new TextView(this);
            textView1.setLayoutParams(layoutParams);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(Color.parseColor("#8d92a3"));
            textView1.setText("一键入库");
            textView1.setBackgroundResource(R.drawable.sp_line);
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    warehousingProduct(type,itemId,standard,quantity);
                }
            });
            llBottom.addView(textView1);
        }

        if (!StringUtil.isEmpty(logisticsNum)) { //已发货
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(32));
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            TextView textView = new TextView(this);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#8d92a3"));
            textView.setText("查看物流");
            textView.setBackgroundResource(R.drawable.sp_line);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lookLogistics(logisticsNum);
                }
            });
            llBottom.addView(textView);
        }
        if(llBottom.getChildCount()>0){
            llBottom.setVisibility(View.VISIBLE);
        }
    }

    private void warehousingProduct(int type, long itemId, String standard, int quantity) {
        WarehousingConfirmPop warehousingConfirmPop = new WarehousingConfirmPop(this, type, itemId, standard, quantity);
        warehousingConfirmPop.setDismissCallBack(new TransparentPop.DismissListener() {
            @Override
            public void dismissStart() {
                //入库成功
                if(warehousingConfirmPop!=null && warehousingConfirmPop.getWarehousingSuccess()){
                    tvOrderStatus.setText("已入库");
                    refreshData(type);
                    needRefresh=true;
                }
            }

            @Override
            public void dismissEnd() {

            }
        });
        warehousingConfirmPop.show(getWindow().getDecorView());
    }


    //取消调拨单异常
    private void cancelAllotDiff(long id) {
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("id", String.valueOf(id));
        addDisposable(ApiRetrofit.getInstance().getApiService().allotCancelDiff(arrayMap), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(AllotItemDetialActivity.this, "已取消");
                needRefresh = true;
                refreshData(0);
                llBottom.removeAllViews();
            }
        });
    }
    //取消采购单异常
    private void cancelPurchaseDiff(long id) {
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("id", String.valueOf(id));
        addDisposable(ApiRetrofit.getInstance().getApiService().purchCancelDiff(arrayMap), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(AllotItemDetialActivity.this, "已取消");
                needRefresh = true;
                refreshData(1);
                llBottom.removeAllViews();
            }
        });
    }



    @OnClick({R.id.iv_back, R.id.tv_copy_order_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (needRefresh) {
                    setResult(RESULT_OK);
                }
                finish();
                break;
            case R.id.tv_copy_order_code:
                copyOrderCode();
                break;
        }
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

    private void copyOrderCode() {
        String orderSn = tvOrderCode.getText().toString().trim();
        if (!StringUtil.isEmpty(orderSn)) {
            if(orderSn.contains(":")){
                orderSn=orderSn.substring(orderSn.indexOf(":")+1,orderSn.length());
            }
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("code", orderSn));
            Util.showToast(this, "订单号已复制");
        }
    }

    private void lookLogistics(String logisticsNum) {
        Intent intent = new Intent(this, WebActivity.class);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://m.kuaidi100.com/");
        if (!StringUtil.isEmpty(logisticsNum)) {
            stringBuilder.append("nu=");
            stringBuilder.append(logisticsNum);
        }
        intent.putExtra("url", stringBuilder.toString());
        intent.putExtra("title", "查询物流");
        startActivity(intent);
    }


    private void refreshData(int type){
        if(type==0){
            loadAllotItemDetail();
        }else{
            loadPurchItemDetail();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (needRefresh) {
                setResult(RESULT_OK);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
