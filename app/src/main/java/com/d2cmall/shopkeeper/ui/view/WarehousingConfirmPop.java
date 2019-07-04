package com.d2cmall.shopkeeper.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.activity.AddProductActivity;
import com.d2cmall.shopkeeper.ui.activity.OrderManagerActivity;
import com.d2cmall.shopkeeper.ui.activity.WriteOffActivity;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/3/18.
 * Description : 一件入库,入库确认弹窗
 */

public class WarehousingConfirmPop implements TransparentPop.InvokeListener {
    @BindView(R.id.tv_spec)
    TextView tvSpec;
    @BindView(R.id.ll_delivery_company)
    LinearLayout llDeliveryCompany;
    @BindView(R.id.tv_deliver_num)
    TextView tvDeliverNum;
    @BindView(R.id.et_receive_num)
    EditText etReceiveNum;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private Context mContext;
    private TransparentPop mPop;
    private View rootView;
    private long orderId;
    private int type;
    private String standard;
    private int quantity;

    private boolean  warehousingSuccess;//是否入库成功

    public boolean getWarehousingSuccess() {
        return warehousingSuccess;
    }


    public WarehousingConfirmPop(Context mContext, int type, long itemId, String standard, int quantity) {
        this.mContext = mContext;
        this.orderId = itemId;
        this.type=type;
        this.standard=standard;
        this.quantity=quantity;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_warehousing_confirm_pop, new LinearLayout(mContext), false);
        ButterKnife.bind(this, rootView);
        mPop = new TransparentPop(mContext, this);
        Animation inAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        mPop.dismissFromOut();
        tvDeliverNum.setText(String.valueOf(quantity));
        if(!StringUtil.isEmpty(standard)){
            tvSpec.setText(standard);
        }
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(etReceiveNum.getText().toString().trim())) {
                    Util.showToast(mContext, "请输入实收数量");
                    return;
                }
                if(Integer.valueOf(etReceiveNum.getText().toString().trim())>quantity){
                    Util.showToast(mContext, "实收数量不能大于发货数量");
                    return;
                }
                //type=0调拨单 ,1是采购单
                if(type==0){
                    warehousingAllot();
                }else{
                    warehousingPurch();
                }
            }
        });
        etReceiveNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("0")){
                    etReceiveNum.setText("");
                }
            }
        });
    }

    private void warehousingPurch() {
        //入库采购单
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("id", String.valueOf(orderId));
        arrayMap.put("actualQuantity", etReceiveNum.getText().toString().trim());
        tvSure.setEnabled(false);
        ((BaseActivity) mContext).addDisposable(ApiRetrofit.getInstance().getApiService().purchReceive(arrayMap), new BaseObserver<BaseModel<String>>() {

            @Override
            public void onSuccess(BaseModel<String> o) {
                warehousingSuccess=true;
                dismiss();
                String productId = (String) o.getData();
                loadProductInfo(Long.valueOf(productId));
                Util.showToast(mContext,"已入库,请编辑商品信息");
            }

            @Override
            public void onError(int errorCode, String msg) {
                tvSure.setEnabled(true);
                super.onError(errorCode, msg);
            }
        });
    }

    private void warehousingAllot() {
        //入库调拨单
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("id", String.valueOf(orderId));
        arrayMap.put("actualQuantity", etReceiveNum.getText().toString().trim());
        tvSure.setEnabled(false);
        ((BaseActivity) mContext).addDisposable(ApiRetrofit.getInstance().getApiService().allotReceive(arrayMap), new BaseObserver<BaseModel<String>>() {
            @Override
            public void onSuccess(BaseModel<String> o) {
                warehousingSuccess=true;
                String productId = (String) o.getData();
                loadProductInfo(Long.valueOf(productId));
                dismiss();
                Util.showToast(mContext,"已入库,请编辑商品信息");
            }

            @Override
            public void onError(int errorCode, String msg) {
                tvSure.setEnabled(true);
                super.onError(errorCode, msg);
            }
        });
    }

    private void loadProductInfo(long productId) {
        ((BaseActivity) mContext).addDisposable(ApiRetrofit.getInstance().getApiService().getProduct(productId), new BaseObserver<BaseModel<ProductBean>>() {
            @Override
            public void onSuccess(BaseModel<ProductBean> o) {
                mContext.startActivity(new Intent(mContext, AddProductActivity.class).putExtra("product",o.getData()));
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
            }
        });
    }

    public void show(View parent) {
        mPop.show(parent, true);
    }

    public void setDismissCallBack(TransparentPop.DismissListener dismissListener) {
        mPop.setDismissListener(dismissListener);
    }

    public void dismiss() {
        if (mPop != null) {
            mPop.dismiss(true);
        }
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.CENTER);
        v.addView(rootView);
    }

    @Override
    public void releaseView(LinearLayout v) {
        if (mContext instanceof OrderManagerActivity) {
            ((OrderManagerActivity) mContext).removeScanDeliveryCodeListener();
        }
    }


}
