package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.d2cmall.shopkeeper.BuildConfig;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.model.DraweeBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/5/7.
 * 邮箱:hrb940258169@163.com
 * 添加线下付款地址
 */

public class AddOfflineAddressActivity extends BaseActivity {

    @BindView(R.id.name_et)
    EditText nameEt;
    @BindView(R.id.phone_et)
    EditText phoneEt;

    private long draweeId;
    private long shopId;
    private String address;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_offline_address;
    }

    @Override
    public void doBusiness() {
        String name=getIntent().getStringExtra("name");
        String phone=getIntent().getStringExtra("phone");
        draweeId=getIntent().getLongExtra("draweeId",0);
        nameEt.setText(name);
        if (!StringUtil.isEmpty(name)){
            nameEt.setSelection(name.length());
        }
        phoneEt.setText(phone);
        getAddress();
    }

    @OnClick({R.id.back_iv, R.id.sure_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.sure_tv:
                String name=nameEt.getText().toString();
                String phone=phoneEt.getText().toString();
                if (StringUtil.isEmpty(name)||StringUtil.isEmpty(phone)){
                    Util.showToast(this,"请输入姓名和手机号！");
                    return;
                }
                insert(name,phone);
                break;
        }
    }

    private void insert(String name,String phone){
        DraweeBean bean=new DraweeBean();
        bean.setAddress(address);
        bean.setMobile(phone);
        bean.setName(name);
        bean.setShopId(shopId);
        addDisposable(ApiRetrofit.getInstance().getApiService().draweeInsert(bean), new BaseObserver<BaseModel<DraweeBean>>() {
            @Override
            public void onSuccess(BaseModel<DraweeBean> o) {
                if (o.getData()!=null){
                    Intent intent=new Intent();
                    intent.putExtra("name",name);
                    intent.putExtra("phone",phone);
                    intent.putExtra("draweeId",o.getData().getId());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });
    }

    private void getAddress(){
        if (Session.getInstance().getUserFromFile(this)!=null){
            shopId= Session.getInstance().getUserFromFile(this).getShopId();
            addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(shopId), new BaseObserver<BaseModel<ShopBean>>() {
                @Override
                public void onSuccess(BaseModel<ShopBean> o) {
                   if (o.getData()!=null){
                       address=o.getData().getAddress();
                   }
                }
            });
        }
    }
}
