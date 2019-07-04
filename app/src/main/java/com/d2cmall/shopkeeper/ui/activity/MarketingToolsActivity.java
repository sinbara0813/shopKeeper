package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/3/1.
 * Description : MarketingToolsActivity
 * 拼团活动
 */

public class MarketingToolsActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_coupon)
    ImageView ivCoupon;
    @BindView(R.id.iv_collage_coupon)
    ImageView ivCollageCoupon;
    @BindView(R.id.iv_collage_product)
    ImageView ivCollageProduct;

    @Override
    public int getLayoutId() {
        return R.layout.activity_marketing_tools;
    }

    @Override
    public void doBusiness() {
        init();
    }

    private void init() {
        tvName.setText("营销工具");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
    }


    @OnClick({R.id.iv_back, R.id.iv_coupon, R.id.iv_collage_coupon, R.id.iv_collage_product})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_coupon:
                //优惠券
                intent=new Intent(this,CouponActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_collage_coupon:
                //拼团优惠券
                intent=new Intent(this,CollagePromotionActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_collage_product:
                //拼团商品
                intent=new Intent(this,CollagePromotionActivity.class);
                startActivity(intent);
                break;
        }
    }
}
