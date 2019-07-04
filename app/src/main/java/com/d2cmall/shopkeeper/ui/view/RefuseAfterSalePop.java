package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
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
import com.d2cmall.shopkeeper.ui.activity.OrderManagerActivity;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/3/18.
 * Description : 售后拒绝申请Pop,
 */

public class RefuseAfterSalePop implements TransparentPop.InvokeListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_reason)
    EditText etReason;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private Context mContext;
    private TransparentPop mPop;
    private View rootView;
    private boolean hasRefuse;
    private String status;
    private long orderId;
    public RefuseAfterSalePop(Context mContext, String status, long id) {
        this.mContext = mContext;
        this.status=status;
        this.orderId=id;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_refuse_after_sale_pop, new LinearLayout(mContext), false);
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
                if (StringUtil.isEmpty(etReason.getText().toString().trim())) {
                    Util.showToast(mContext, "请输入拒绝原因");
                    return;
                }
                refuseAfterSale();
            }
        });

    }
    public boolean isHasRefuse() {
        return hasRefuse;
    }
    private void refuseAfterSale() {
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("orderItemId",String.valueOf(orderId));
        arrayMap.put("refuseMemo",etReason.getText().toString().trim());
        if("WAIT_REFUND".equals(status)){//拒绝退款
            tvSure.setEnabled(false);
            ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().refuseRefund(arrayMap), new BaseObserver() {
                @Override
                public void onSuccess(Object o) {
                    Util.showToast(mContext,"已拒绝");
                    hasRefuse=true;
                    dismiss();
                }

                @Override
                public void onError(int errorCode, String msg) {
                    super.onError(errorCode, msg);
                    tvSure.setEnabled(true);
                }
            });
        }else if("WAIT_RESHIP".equals(status)){//拒绝退货退款
            tvSure.setEnabled(false);
            ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().refuseReship(arrayMap), new BaseObserver() {
                @Override
                public void onSuccess(Object o) {
                    Util.showToast(mContext,"已拒绝");
                    hasRefuse=true;
                    dismiss();
                }

                @Override
                public void onError(int errorCode, String msg) {
                    super.onError(errorCode, msg);
                    tvSure.setEnabled(true);
                }
            });
        }

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
