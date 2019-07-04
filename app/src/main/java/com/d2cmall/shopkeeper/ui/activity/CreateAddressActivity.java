package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.ui.view.AddressPop;
import com.d2cmall.shopkeeper.utils.LocationUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/5/7.
 * 邮箱:hrb940258169@163.com
 * 添加收货地址
 */

public class CreateAddressActivity extends BaseActivity implements AddressPop.CallBack{

    @BindView(R.id.consignee_et)
    EditText consigneeEt;
    @BindView(R.id.phone_et)
    EditText phoneEt;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.info_et)
    EditText infoEt;

    private InputMethodManager im;
    private LocationUtil mLocationUtil;
    private String provinceName;
    private String city;
    private AddressPop addressPop;
    private String provinceInfo;

    @Override
    protected void onResume() {
        new Thread() {
            @Override
            public void run() {
                try {
                    InputStream input = getResources().openRawResource
                            (R.raw.area);
                    provinceInfo = Util.readStreamToString(input);
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        super.onResume();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_address;
    }

    @Override
    public void doBusiness() {
        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        String name = getIntent().getStringExtra("name");
        String phone = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");
        if (!StringUtil.isEmpty(address)){
            String[] strings = address.split("\\*");
            addressTv.setText(strings[0]);
            infoEt.setText(strings[1]);
        }
        consigneeEt.setText(name);
        phoneEt.setText(phone);
    }

    @OnClick({R.id.back_iv, R.id.sure_tv,R.id.select_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.sure_tv:
                String name=consigneeEt.getText().toString();
                String phone=phoneEt.getText().toString();
                String address=addressTv.getText().toString();
                String info=infoEt.getText().toString();
                if (StringUtil.isEmpty(name)||StringUtil.isEmpty(phone)||StringUtil.isEmpty(address)||StringUtil.isEmpty(info)){
                    Util.showToast(this,"请填写正确地址！");
                    return;
                }
                Intent intent=new Intent();
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("address",address+"*"+info);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.select_tv:
                showAddressPop(view);
                break;
        }
    }

    private void showAddressPop(View view){
        if (getCurrentFocus() != null) {
            im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (addressPop==null){
            addressPop = new AddressPop(this, provinceInfo);
            addressPop.setCallBack(this);
            if (!StringUtil.isEmpty(provinceName) && !StringUtil.isEmpty(city)) {
                addressPop.refreshDataByProvinceCity(provinceName, city);
            }
        }
        addressPop.show(view);
    }

    @Override
    public String[] getNeedPermission() {
        return new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    @Override
    protected void onStart() {
        mLocationUtil = LocationUtil.getLoctionUtil(this);
        mLocationUtil.setIhasCity(new LocationUtil.IhasCity() {
            @Override
            public String getAddress(String province, String cityName, String street) {
                if (province != null) {
                    if (StringUtil.isEmpty(provinceName)) {
                        provinceName = province;
                    }
                }
                if (cityName != null) {
                    if (StringUtil.isEmpty(city)) {
                        city = cityName;
                    }
                }
                return null;
            }
        });
        super.onStart();
    }

    @Override
    protected void onStop() {
        mLocationUtil.destroyLocation();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (addressPop!=null)addressPop.setCallBack(null);
        mLocationUtil.destroyLocation();
        super.onDestroy();
    }

    @Override
    public void callback(String provinceName, int provinceCode, String cityName, int cityCode, String districtName, int districtCode) {
        StringBuilder builder=new StringBuilder();
        builder.append(provinceName).append(cityName).append(districtName);
        addressTv.setText(builder.toString());
    }
}
