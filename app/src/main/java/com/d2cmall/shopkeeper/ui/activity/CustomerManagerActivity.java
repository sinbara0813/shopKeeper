package com.d2cmall.shopkeeper.ui.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.ui.fragment.CustomerManagerFragment;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/20.
 * Description : CustomerManagerActivity
 * 客户管理
 */

public class CustomerManagerActivity extends BaseActivity {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    private ArrayList fragments;
    private ArrayList titles;
    private TabPagerAdapter tabPagerAdapter;
    private SlidrInterface slidrInterface;

    @Override
    public int getLayoutId() {
        return R.layout.activity_transaction_record;
    }

    @Override
    public void doBusiness() {
        initTitle();
    }

    private void initTitle() {
        slidrInterface=Slidr.attach(this);
        tvName.setText("客户管理");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(llTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0,0,0,ScreenUtil.dip2px(16), 0, 0);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("消费客户");
        titles.add("关注客户");
        fragments.add(CustomerManagerFragment.newInstance(0));//消费客户
        fragments.add(CustomerManagerFragment.newInstance(1));//关注客户
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setViewPager(viewPager);
        tabPagerAdapter.notifyDataSetChanged();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i==0){
                    slidrInterface.unlock();
                }else {
                    slidrInterface.lock();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
