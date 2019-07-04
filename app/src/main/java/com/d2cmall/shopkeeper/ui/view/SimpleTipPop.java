package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.activity.OrderManagerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/3/18.
 * Description : 调拨拒绝,调拨退回提示框,
 */

public class SimpleTipPop implements TransparentPop.InvokeListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private Context mContext;
    private TransparentPop mPop;
    private View rootView;
    private int popType;

    public SimpleTipPop(Context mContext, int popType) {
        this.mContext = mContext;
        this.popType = popType;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_simple_tip_pop, new LinearLayout(mContext), false);
        ButterKnife.bind(this, rootView);
        mPop = new TransparentPop(mContext, this);
        Animation inAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        mPop.dismissFromOut();
        if(popType==0){
            tvTitle.setText("拒绝原因");
            tvTitle.setText("暂无库存，不能调拨！");
        }else{
            tvTitle.setText("商品退回");
            tvTitle.setText("点击确定，商品需要在“调拨退回”中退回");
        }
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dismiss();
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
