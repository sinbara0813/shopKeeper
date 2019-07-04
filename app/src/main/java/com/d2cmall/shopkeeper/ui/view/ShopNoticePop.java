package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/6/29.
 * 邮箱:hrb940258169@163.com
 */
public class ShopNoticePop implements TransparentPop.InvokeListener {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.sure_tv)
    TextView sureTv;
    private View rooView;
    private TransparentPop mPop;

    public ShopNoticePop(Context context) {
        rooView = LayoutInflater.from(context).inflate(R.layout.layout_shop_notice_pop, new CoordinatorLayout(context), false);
        ButterKnife.bind(this, rooView);
        mPop = new TransparentPop(context, this);
        mPop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void show(View view) {
        mPop.show(view, false);
    }

    public void dismiss() {
        mPop.dismiss(false);
    }

    public void setCategory(String s){
        etAddress.setText(s);
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.CENTER);
        v.addView(rooView);
    }

    @Override
    public void releaseView(LinearLayout v) {
        v.removeView(rooView);
        sureTv.setOnClickListener(null);
        rooView = null;
    }

    public String getNotice(){
        return etAddress.getText().toString();
    }

    public void setListener(View.OnClickListener listener) {
        sureTv.setOnClickListener(listener);
    }
}
