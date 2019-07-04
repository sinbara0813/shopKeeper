package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.ui.view.ShopKeeperTabLayout;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.Session;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

/**
 * 作者:Created by sinbara on 2019/2/18.
 * 邮箱:hrb940258169@163.com
 * 首页
 */

public class ShopKeeperActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    ShopKeeperTabLayout tabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public String[] getNeedPermission() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Session.getInstance().getUserFromFile(this)==null){
            startActivityForResult(new Intent(this,LoginActivity.class),100); ;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_keeper;
    }

    @Override
    public void doBusiness() {
        tabLayout.init(0);
    }

    @Subscribe
    public void onEvent(Action action){
        if (action.type== Constants.ActionType.CLEAR_ALL_ACTIVITY){
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==100){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
