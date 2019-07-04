package com.d2cmall.shopkeeper.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.ui.fragment.CustomerActionRecordFragment;
import com.d2cmall.shopkeeper.ui.fragment.OrderListFragment;
import com.d2cmall.shopkeeper.ui.fragment.ReshipRefundFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/4/26.
 * Description : B端->供应商 退货退款&调拨退回列表
 */

public class ReshipRefundListActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_seach)
    ImageView ivSeach;
    @BindView(R.id.tv_order_self)
    TextView tvOrderSelf;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private ArrayList fragments;
    private ArrayList titles;
    private TabPagerAdapter tabPagerAdapter;
    private int pageType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reship_refund;
    }

    @Override
    public void doBusiness() {
        pageType = getIntent().getIntExtra("pageType",0);
        initView();
    }

    private void initView() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        if(pageType==0){
            tvOrderSelf.setText("退货退款");
            titles.add("退货退款");
            titles.add("退款");
            fragments.add(ReshipRefundFragment.newInstance(null,0));//退货退款
            fragments.add(ReshipRefundFragment.newInstance(null,0));//退款
        }else{
            tvOrderSelf.setText("调拨退回");
            titles.add("全部");
            titles.add("待发货");
            titles.add("已发货");
            titles.add("已完结");
            fragments.add(ReshipRefundFragment.newInstance(null,0));//全部
            fragments.add(ReshipRefundFragment.newInstance(null,0));//待发货
            fragments.add(ReshipRefundFragment.newInstance(null,0));//已发货
            fragments.add(ReshipRefundFragment.newInstance(null,0));//已完结
        }


        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setViewPager(viewPager);
        tabPagerAdapter.notifyDataSetChanged();
    }


    @OnClick({R.id.iv_back, R.id.iv_seach})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_seach:
                break;
        }
    }
}
