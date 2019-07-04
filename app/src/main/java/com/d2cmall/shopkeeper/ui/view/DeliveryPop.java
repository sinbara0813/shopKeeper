package com.d2cmall.shopkeeper.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.ui.activity.OrderManagerActivity;
import com.d2cmall.shopkeeper.ui.activity.WriteOffActivity;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/3/18.
 * Description : DeliveryPop 发货
 */

public class DeliveryPop implements TransparentPop.InvokeListener, OrderManagerActivity.ScanDeliveryCodeListener, SingleSelectPop.CallBack {
    @BindView(R.id.tv_deliver)
    TextView tvDeliver;
    @BindView(R.id.iv_deliver_company_list)
    ImageView ivDeliverCompanyList;
    @BindView(R.id.et_deliver_code)
    EditText etDeliverCode;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.ll_delivery_company)
    LinearLayout llDeliveryCompany;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private Context mContext;
    private TransparentPop mPop;
    private View rootView;
    private long orderId;

    public boolean isHasDeliverGood() {
        return hasDeliverGood;
    }

    private boolean hasDeliverGood;

    public DeliveryPop(Context mContext,long orderId) {
        this.mContext = mContext;
        this.orderId=orderId;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_deliver_info_pop, new LinearLayout(mContext), false);
        ButterKnife.bind(this, rootView);
        mPop = new TransparentPop(mContext, this);
        Animation inAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        mPop.dismissFromOut();
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isEmpty(etDeliverCode.getText().toString().trim())){
                    Util.showToast(mContext,"请输入运单编号");
                    return;
                }
                if(StringUtil.isEmpty(tvDeliver.getText().toString().trim())){
                    Util.showToast(mContext,"请选择快递公司");
                    return;
                }
                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("orderItemId",String.valueOf(orderId));
                map.put("logisticsCom",tvDeliver.getText().toString().trim());
                map.put("logisticsNum",etDeliverCode.getText().toString().trim());
                ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().deliverGood(map), new BaseObserver() {
                    @Override
                    public void onSuccess(Object o) {
                        hasDeliverGood=true;
                        Util.showToast(mContext,"发货成功");
                        dismiss();
                    }
                });
            }
        });

        tvDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] titles = mContext.getResources().getStringArray(R.array.label_logistic_titles);
                SingleSelectPop singleSelectPop = new SingleSelectPop(mContext, titles);
                singleSelectPop.show(((Activity)mContext).getWindow().getDecorView(), llDeliveryCompany);
                singleSelectPop.setCallBack(DeliveryPop.this);
            }
        });
        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mContext instanceof OrderManagerActivity){
                    ((OrderManagerActivity)mContext).setScanDeliveryCodeListener(DeliveryPop.this);
                }
                Intent intent = new Intent(mContext, WriteOffActivity.class);
                intent.putExtra("isFromOrder", true);
                ((BaseActivity) mContext).startActivityForResult(intent,Constants.RequestCode.SCAN_DELIVERY_CODE);
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
        if(mContext instanceof OrderManagerActivity){
            ((OrderManagerActivity)mContext).removeScanDeliveryCodeListener();
        }
    }


    @Override
    public void scanDelivery(String deliveryCode) {
        etDeliverCode.setText(deliveryCode);
    }

    @Override
    public void callback(View trigger, int index, String value) {
        tvDeliver.setText(value);
    }

    public void setDerliverCode(String deliveryCode) {
        etDeliverCode.setText(deliveryCode);
    }
}
