package com.d2cmall.shopkeeper.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.SharePrefConstant;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.ui.view.AddressPop;
import com.d2cmall.shopkeeper.ui.view.ClearEditText;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.SharedPreUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/22.
 * Description : CreateBrandActivity
 * 创建店铺
 */

public class CreateBrandActivity extends BaseActivity implements AddressPop.CallBack {
    @BindView(R.id.single_iv)
    ImageView singleIv;
    @BindView(R.id.coll_iv)
    ImageView collIv;
    @BindView(R.id.et_brand_name)
    ClearEditText etBrandName;
    @BindView(R.id.et_brand_business)
    ClearEditText etBrandBusiness;
    @BindView(R.id.et_brand_address)
    ClearEditText etBrandAddress;
    @BindView(R.id.et_brand_phone)
    ClearEditText etBrandPhone;
    @BindView(R.id.sure_tv)
    TextView sureTv;
    @BindView(R.id.single_rl)
    RelativeLayout singleRl;
    @BindView(R.id.coll_rl)
    RelativeLayout collRl;
    @BindView(R.id.single_tv)
    TextView singleTv;
    @BindView(R.id.coll_tv)
    TextView collTv;
    @BindView(R.id.tv_brand_address)
    TextView tvBrandAddress;
    private InputMethodManager im;

    private String provinceInfo;
    private String name;
    private String phone;
    private String businessDirection;
    private String addressName;
    private BaseModel<ShopBean> mShopBean;
    private String type;
    private String address="";
    private AddressPop addressPop;

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
        return R.layout.activity_create_brand1;
    }


    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private boolean checkAddress(){
        boolean result=false;
        if (!StringUtil.isEmpty(address)){
            String[] s=address.split(",");
            JsonObject biz_content=new JsonObject();
            biz_content.addProperty("province",s[0]);
            biz_content.addProperty("city",s[1]);
            biz_content.addProperty("district",s[2]);
            biz_content.addProperty("address",etBrandAddress.getText().toString().trim());
            address=biz_content.toString();
            result=true;
        }
        return result;
    }

    private void requestCreateBrand() {
        if (StringUtil.isEmpty(etBrandName.getText().toString().trim())) {
            Toast.makeText(this, "请填写店铺名称", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etBrandName.getText().toString().trim().length() < 4 || etBrandName.getText().toString().trim().length() > 20) {
            Toast.makeText(this, "店铺名称长度须在4-20字符之间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(etBrandPhone.getText().toString().trim())) {
            Toast.makeText(this, "请填写联系电话", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!checkAddress()) {
            Toast.makeText(this, "请输入正确的店铺地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etBrandAddress.getText().toString().trim().length() < 2 || etBrandAddress.getText().toString().trim().length() > 40) {
            Toast.makeText(this, "店铺地址长度须在2-40字符之间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(etBrandBusiness.getText().toString().trim())) {
            Toast.makeText(this, "请选择主营业务", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etBrandBusiness.getText().toString().trim().length() < 2 || etBrandBusiness.getText().toString().trim().length() > 12) {
            Toast.makeText(this, "主营业务长度须在2-12字符之间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(type)) {
            Toast.makeText(this, "请选择店铺类型", Toast.LENGTH_SHORT).show();
            return;
        }
        ShopBean shopBean = new ShopBean();
        shopBean.setName(etBrandName.getText().toString().trim());
        shopBean.setTelephone(etBrandPhone.getText().toString().trim());
        shopBean.setAddress(address);
        shopBean.setScope(etBrandBusiness.getText().toString().trim());
        shopBean.setType(type);
        sureTv.setEnabled(false);
        addDisposable(ApiRetrofit.getInstance().getApiService().createShop(shopBean), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBeanBaseModel) {
                mShopBean = shopBeanBaseModel;
                Toast.makeText(CreateBrandActivity.this, "店铺创建成功,店铺名:" + shopBeanBaseModel.getData().getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("shopId", shopBeanBaseModel.getData().getId());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(int errorCode, String msg) {
                sureTv.setEnabled(true);
                super.onError(errorCode, msg);
            }
        });
    }


    private void showAddressPop(View view){
        if (getCurrentFocus() != null) {
            im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        addressPop = new AddressPop(this, provinceInfo);
        addressPop.setCallBack(this);
        addressPop.show(view);
    }

    public void onBackPressed() {
        if (mShopBean == null) {
            if (getCurrentFocus() != null) {
                im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            new AlertDialog.Builder(this)
                    .setMessage("确定退出吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(RESULT_CANCELED);
                            finish();
                            overridePendingTransition(0, R.anim.slide_out_right);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        } else {
            finish();
            overridePendingTransition(0, R.anim.slide_out_right);
        }
    }

    private void errorBack() {
        SharedPreUtil.setString(SharePrefConstant.TOKEN, "");
        finish();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            errorBack();
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @OnClick({R.id.back_iv, R.id.single_rl, R.id.coll_rl, R.id.sure_tv,R.id.tv_brand_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                errorBack();
                break;
            case R.id.single_rl:
                if (singleIv.getVisibility() == View.VISIBLE) {
                    return;
                }
                singleIv.setVisibility(View.VISIBLE);
                collIv.setVisibility(View.GONE);
                singleRl.setBackgroundResource(R.drawable.sp_round2_stroke_blue);
                collRl.setBackground(null);
                singleTv.setTextColor(ContextCompat.getColor(this,R.color.normal_blue));
                collTv.setTextColor(ContextCompat.getColor(this,R.color.flash_time_bg_color));
                type="PERSON";
                break;
            case R.id.coll_rl:
                if (collIv.getVisibility() == View.VISIBLE) {
                    return;
                }
                collIv.setVisibility(View.VISIBLE);
                singleIv.setVisibility(View.GONE);
                collRl.setBackgroundResource(R.drawable.sp_round2_stroke_blue);
                singleRl.setBackground(null);
                collTv.setTextColor(ContextCompat.getColor(this,R.color.normal_blue));
                singleTv.setTextColor(ContextCompat.getColor(this,R.color.flash_time_bg_color));
                type="BRAND";
                break;
            case R.id.sure_tv:
                requestCreateBrand();
                break;
            case R.id.tv_brand_address:
                showAddressPop(view);
                break;
        }
    }

    @Override
    public void callback(String provinceName, int provinceCode, String cityName, int cityCode, String districtName, int districtCode) {
        tvBrandAddress.setText(provinceName+cityName+districtName);
        address=provinceName+","+cityName+","+districtName;
    }
}
