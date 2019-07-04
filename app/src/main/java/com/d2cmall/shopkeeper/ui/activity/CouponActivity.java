package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.ui.fragment.CouponFragment;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/2/25.
 * 邮箱:hrb940258169@163.com
 * 优惠券列表
 */

public class CouponActivity extends BaseActivity {

    @BindView(R.id.title_ll)
    LinearLayout titleLl;
    @BindView(R.id.back_iv)
    ImageView backIv;
    @BindView(R.id.add_iv)
    ImageView addIv;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private ArrayList fragments;
    private ArrayList titles;
    private TabPagerAdapter tabPagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_coupon;
    }

    @Override
    public void doBusiness() {
        initView();
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("可领用");
        titles.add("未开始");
        titles.add("不可领");
        fragments.add(CouponFragment.newInstance(1));//可领取
        fragments.add(CouponFragment.newInstance(0));//未开始
        fragments.add(CouponFragment.newInstance(-1));//不可领
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setViewPager(viewPager);
    }

    private void initView() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(titleLl, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
    }


    @OnClick({R.id.back_iv, R.id.add_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.add_iv:
                startActivityForResult(new Intent(this, AddCouponActivity.class),Constants.RequestCode.ACTION_COUPON);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== Constants.RequestCode.ACTION_COUPON && resultCode==RESULT_OK){
            int currentItem = viewPager.getCurrentItem();
            if (!((CouponFragment)fragments.get(currentItem)).isDetached()){
                ((CouponFragment)fragments.get(currentItem)).refresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
