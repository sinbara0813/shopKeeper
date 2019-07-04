package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.r0adkll.slidr.Slidr;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/6/17.
 * 邮箱:hrb940258169@163.com
 */
public class MarketingUtilActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_market_util;
    }

    @Override
    public void doBusiness() {
        Slidr.attach(this);
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
    }

    @OnClick({R.id.back_iv, R.id.coupon_rl, R.id.product_book_rl, R.id.coupon_book_rl})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.coupon_rl:
                intent=new Intent(this,CouponActivity.class);
                startActivity(intent);
                break;
            case R.id.product_book_rl:
                intent = new Intent(this, CollagePromotionActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                break;
            case R.id.coupon_book_rl:
                intent = new Intent(this, CollagePromotionActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                break;
        }
    }
}
