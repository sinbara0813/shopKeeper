package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
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
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/3/18.
 * Description : EditPricePop
 * 编辑价格 & 同意退款
 */

public class EditPricePop implements TransparentPop.InvokeListener {
    @BindView(R.id.et_total_amount)
    EditText etTotalAmount;
    @BindView(R.id.tv_discount_amount)
    TextView tvDiscountAmount;
    @BindView(R.id.tv_actual_amount)
    TextView tvActualAmount;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.ll_total_amount)
    LinearLayout llTotalAmount;

    private Context mContext;
    private TransparentPop mPop;
    private View rootView;
    private long mOrderId;
    private double mDiscountAmount;
    private double mPayAmount;
    private int actionType;//0:订单修改价格  1:同意退款

    public boolean isHasEdit() {
        return hasEdit;
    }

    private boolean hasEdit;

    public EditPricePop(Context mContext,long orderId, double discountAmount ,double payAmount ,int actionType) {
        this.mContext = mContext;
        this.mOrderId=orderId;
        this.mDiscountAmount=discountAmount;
        this.mPayAmount=payAmount;
        this.actionType=actionType;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_edit_order_price_pop, new LinearLayout(mContext), false);
        ButterKnife.bind(this, rootView);
        mPop = new TransparentPop(mContext, this);
        Animation inAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        mPop.dismissFromOut();

        if(actionType==0){
            tvDiscountAmount.setText(mContext.getString(R.string.label_discount_amount,Util.getNumberFormat(mDiscountAmount)));
            tvActualAmount.setText(mContext.getString(R.string.label_actual_amount,Util.getNumberFormat(mPayAmount)));
        }else{
            tvActualAmount.setText("会员申请退款: ¥"+Util.getNumberFormat(mDiscountAmount));
            tvDiscountAmount.setText("交易金额: ¥"+Util.getNumberFormat(mPayAmount));
            llTotalAmount.setVisibility(View.GONE);
            tvTitle.setText("确定要同意此申请吗");
        }

        etTotalAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(getDoubleValue(etTotalAmount.getText().toString().trim())>mDiscountAmount && actionType==0){
                    tvActualAmount.setText(mContext.getString(R.string.label_actual_amount,Util.getNumberFormat(getDoubleValue(etTotalAmount.getText().toString().trim())-mDiscountAmount)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.getDoubleFromText(etTotalAmount.getText().toString().trim())==0){
                    if(actionType==0){
                        Util.showToast(mContext,"请输入商品总价");
                        return;
                    }
                }
                if(getDoubleValue(etTotalAmount.getText().toString().trim())<mDiscountAmount && actionType==0){
                        Util.showToast(mContext,"商品总价不得小于优惠金额");
                        return;
                }

                if(actionType==0){
                    editPrice();
                }else{
                    agreeRefund();
                }

            }
        });
    }

    //同意退款
    private void agreeRefund() {
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("orderItemId",String.valueOf(mOrderId));
        tvSure.setEnabled(false);
        ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().agreeRefund(arrayMap), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(mContext,"已同意");
                hasEdit=true;
                dismiss();
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                tvSure.setEnabled(true);
            }
        });

    }
    //编辑订单价格
    private void editPrice() {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("orderItemId",String.valueOf(mOrderId));
        map.put("realPrice",String.valueOf(getDoubleValue(etTotalAmount.getText().toString().trim())));
        ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().updateOrderAmount(map), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(mContext,"修改成功");
                hasEdit=true;
                dismiss();
            }
        });
    }

    public void setDismissCallBack(TransparentPop.DismissListener dismissListener) {
        mPop.setDismissListener(dismissListener);
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.CENTER);
        v.addView(rootView);
    }

    @Override
    public void releaseView(LinearLayout v) {

    }

    public void show(View parent) {
        mPop.show(parent, true);
    }

    public void dismiss() {
        if (mPop != null) {
            mPop.dismiss(true);
        }
    }

    private  double getDoubleValue(String str){
        if(StringUtil.isEmpty(str)){
            return 0.0;
        }else if(str.contains(",")){
            str=str.replaceAll(",","");
        }
        return Double.valueOf(str);
    }

}
