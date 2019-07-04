package com.d2cmall.shopkeeper.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.utils.Session;

import org.greenrobot.eventbus.EventBus;

/**
 * 作者:Created by sinbara on 2019/5/23.
 * 邮箱:hrb940258169@163.com
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=null;
                if (Session.getInstance().getUser()!=null){
                    if ("REPORT".equals(Session.getInstance().getUser().getRole())){
                        intent=new Intent(SplashActivity.this,StatActivity.class);
                    }else {
                        intent=new Intent(SplashActivity.this,ShopKeeperActivity.class);
                    }
                }else {
                    intent=new Intent(SplashActivity.this,ShopKeeperActivity.class);
                }
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.anim_default);
            }
        },400);
    }
}
