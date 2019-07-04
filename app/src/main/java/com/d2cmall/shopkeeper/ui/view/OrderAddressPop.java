package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
public class OrderAddressPop implements TransparentPop.InvokeListener {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.name_tv)
    EditText nameTv;
    @BindView(R.id.phone_tv)
    EditText phoneTv;
    @BindView(R.id.address_region_tv)
    TextView addressRegionTv;
    @BindView(R.id.address_ll)
    LinearLayout addressLl;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.sure_tv)
    TextView sureTv;

    private View rooView;
    private TransparentPop mPop;
    private InputMethodManager im;

    public OrderAddressPop(Context context) {
        rooView = LayoutInflater.from(context).inflate(R.layout.layout_order_address_pop, new CoordinatorLayout(context), false);
        ButterKnife.bind(this, rooView);
        mPop = new TransparentPop(context, this);
        im = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void show(View view) {
        mPop.show(view, false);
    }

    public void dismiss() {
        mPop.dismiss(false);
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.CENTER);
        v.addView(rooView);
    }

    @Override
    public void releaseView(LinearLayout v) {
    }

    public void setAddressListener(View.OnClickListener listener) {
        addressLl.setOnClickListener(listener);
    }

    public void setAddress(String s){
        addressRegionTv.setText(s);
    }

    public void setName(String s){
        nameTv.setText(s);
    }

    public void setPhone(String s){
        phoneTv.setText(s);
    }

    public void setNegion(String s){
        etAddress.setText(s);
    }

    public String getRegion(){
        return addressRegionTv.getText().toString();
    }

    public String getDetailAddress(){
        return etAddress.getText().toString();
    }

    public String getName(){
        return nameTv.getText().toString();
    }

    public String getPhone(){
        return phoneTv.getText().toString();
    }

    public void setListener(View.OnClickListener listener) {
        sureTv.setOnClickListener(listener);
    }

    public void hideSoft(){
        im.hideSoftInputFromWindow(etAddress.getWindowToken(), 0);
    }
}
