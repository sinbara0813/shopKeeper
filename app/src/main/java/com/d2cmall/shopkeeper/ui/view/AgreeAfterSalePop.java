package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Address;
import com.d2cmall.shopkeeper.common.SharePrefConstant;
import com.d2cmall.shopkeeper.utils.SharedPreUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/3/18.
 * Description : AgreeAfterSalePop
 * 同意退货退款弹窗
 */

public class AgreeAfterSalePop implements TransparentPop.InvokeListener {
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.address_region_tv)
    TextView addressRegionTv;
    @BindView(R.id.address_ll)
    LinearLayout addressLl;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.sure_tv)
    TextView sureTv;
    private Context mContext;
    private TransparentPop mPop;
    private View rootView;
    private long mOrderId;
    private double mPayAmount;
    private double applyAmount;
    public String provinceName,cityName,districtName;

    private InputMethodManager im;

    public AgreeAfterSalePop(Context mContext, long orderId, double applyAmount, double payAmount) {
        this.mContext = mContext;
        this.mOrderId = orderId;
        this.mPayAmount = payAmount;
        this.applyAmount = applyAmount;
        init();
    }

    private void init() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_agree_after_sale_pop, new LinearLayout(mContext), false);
        ButterKnife.bind(this, rootView);
        mPop = new TransparentPop(mContext, this);
        im = (InputMethodManager) mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        phoneTv.setText("会员申请退款: ¥" + Util.getNumberFormat(applyAmount));
        nameTv.setText("交易金额: ¥" + Util.getNumberFormat(mPayAmount));
        String backAddress = SharedPreUtil.getString(SharePrefConstant.SHOP_BACK_ADDRESS, "");
        if (!StringUtil.isEmpty(backAddress)) {
            if (backAddress.startsWith("{")) {
                Gson gson = new Gson();
                Address address = gson.fromJson(backAddress, Address.class);
                provinceName=address.province;
                cityName=address.city;
                districtName=address.district;
                addressRegionTv.setText(address.province + address.city + address.district);
                etAddress.setText(address.address);
                etName.setText(address.name);
                etPhone.setText(address.mobile);
            }
        }
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

    public void setAddressListener(View.OnClickListener listener) {
        addressLl.setOnClickListener(listener);
    }

    public void setAddress(String s){
        addressRegionTv.setText(s);
    }

    public void setName(String s){
        etName.setText(s);
    }

    public void setPhone(String s){
        etPhone.setText(s);
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
        return etName.getText().toString();
    }

    public String getPhone(){
        return etPhone.getText().toString();
    }

    public void setListener(View.OnClickListener listener) {
        sureTv.setOnClickListener(listener);
    }

    public void hideSoft(){
        im.hideSoftInputFromWindow(etAddress.getWindowToken(), 0);
    }

}
