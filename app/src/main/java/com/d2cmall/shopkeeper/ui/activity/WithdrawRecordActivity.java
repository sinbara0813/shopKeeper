package com.d2cmall.shopkeeper.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.ui.fragment.TransactionRecordFragment;
import com.d2cmall.shopkeeper.ui.fragment.WithdrawRecordFragment;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/26.
 * Description : WithdrawRecordActivity
 * 提现记录
 */

public class WithdrawRecordActivity extends BaseActivity {
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

    @Override
    public int getLayoutId() {
        return R.layout.activity_transaction_record;
    }

    @Override
    public void doBusiness() {
        initTitle();
    }

    private void initTitle() {
        tvName.setText("提现记录");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(llTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
//        CLOSE("已关闭"),
//        APPLY("申请中"),
//        SUCCESS("已完成");
        titles.add("全部");
        titles.add("申请中");
        titles.add("已完成");
        titles.add("已关闭");
        fragments.add(WithdrawRecordFragment.newInstance(null));//全部
        fragments.add(WithdrawRecordFragment.newInstance("APPLY"));//申请中
        fragments.add(WithdrawRecordFragment.newInstance("SUCCESS"));//已完成
        fragments.add(WithdrawRecordFragment.newInstance("CLOSE"));//已关闭
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setViewPager(viewPager);
        tabPagerAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
