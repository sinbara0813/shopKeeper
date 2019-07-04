package com.d2cmall.shopkeeper.ui.view;

import android.app.Activity;
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
import com.d2cmall.shopkeeper.utils.KeyboardUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/3/18.
 * Description : DeliveryPop 发货
 */

public class EditAddressPop implements TransparentPop.InvokeListener {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    private Context mContext;
    private TransparentPop mPop;
    private View rootView;
    private long orderId;
    private String provinceInfo;
    private String mProvinceName;
    private String mCityName;
    private String mDistrictName;

    public EditAddressPop(Context mContext, long orderId) {
        this.mContext = mContext;
        this.orderId = orderId;
        init();
    }

    private void init() {
        getAddressInfo();
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_edit_order_address_pop, new LinearLayout(mContext), false);
        ButterKnife.bind(this, rootView);
        mPop = new TransparentPop(mContext, this);
        Animation inAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        mPop.dismissFromOut();
        tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(provinceInfo)) {
                    Util.showToast(mContext, "正在加载地址信息...");
                    return;
                }
                KeyboardUtil.hideKeyboard(v);
                AddressPop pop = new AddressPop(mContext, provinceInfo);
                pop.setCallBack(new AddressPop.CallBack() {
                    @Override
                    public void callback(String provinceName, int provinceCode, String cityName, int cityCode, String districtName, int districtCode) {
                        mProvinceName = provinceName;
                        mCityName = cityName;
                        mDistrictName = districtName;
                        StringBuilder builder = new StringBuilder();
                        builder.append(provinceName).append(cityName).append(districtName);
                        tvAddress.setText(builder.toString());
                    }
                });
                pop.show(((Activity) mContext).getWindow().getDecorView());
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isEmpty(etName.getText().toString().trim())) {
                    Util.showToast(mContext, "请输入收件人姓名");
                    return;
                }
                if (StringUtil.isEmpty(etPhone.getText().toString().trim())) {
                    Util.showToast(mContext, "请输入收件人电话");
                    return;
                }
                if (StringUtil.isEmpty(etAddress.getText().toString().trim())) {
                    Util.showToast(mContext, "请输入收件地址");
                    return;
                }
                if (StringUtil.isEmpty(tvAddress.getText().toString().trim())) {
                    Util.showToast(mContext, "请选择省市区");
                    return;
                }
                if (StringUtil.isEmpty(mProvinceName) || StringUtil.isEmpty(mCityName) || StringUtil.isEmpty(mDistrictName)) {
                    return;
                }
                ArrayMap<String, String> map = new ArrayMap<>();
                map.put("orderId", String.valueOf(orderId));
                map.put("province", mProvinceName);
                map.put("city", mCityName);
                map.put("district", mDistrictName);
                map.put("address", etAddress.getText().toString().trim());
                map.put("name", etName.getText().toString().trim());
                map.put("mobile", etPhone.getText().toString().trim());
                ((BaseActivity) mContext).addDisposable(ApiRetrofit.getInstance().getApiService().updateOrderAddress(map), new BaseObserver() {
                    @Override
                    public void onSuccess(Object o) {
                        Util.showToast(mContext, "修改成功");
                        dismiss();
                    }
                });
            }
        });
    }

    public void show(View parent) {
        mPop.show(parent, true);
    }

    public void dismiss() {
        if (mPop != null) {
            mPop.dismiss(true);
        }
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


    public String getAddressInfo() {
        new Thread() {
            @Override
            public void run() {
                try {
                    InputStream input = mContext.getResources().openRawResource
                            (R.raw.area);
                    provinceInfo = Util.readStreamToString(input);
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return provinceInfo;
    }

    public ArrayMap<String, String> getAddressMap() {
       if(etAddress==null || etName==null || etPhone==null){
           return null;
       }
        ArrayMap<String, String> map = new ArrayMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tvAddress.getText().toString().trim());
        stringBuilder.append(etAddress.getText().toString().trim());
        map.put("address",stringBuilder.toString());
        map.put("name",etName.getText().toString().trim());
        map.put("phone",etPhone.getText().toString().trim());
        return map;
    }
}
