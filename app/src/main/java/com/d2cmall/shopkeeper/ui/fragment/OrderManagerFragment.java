package com.d2cmall.shopkeeper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.base.BaseFragment;
import com.d2cmall.shopkeeper.base.BaseTabFragment;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.ui.activity.OrderManagerActivity;
import com.d2cmall.shopkeeper.ui.activity.ProductManagerActivity;
import com.d2cmall.shopkeeper.ui.view.CustomerTabView;

import java.util.ArrayList;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LWJ on 2019/2/21.
 * Description : OrderManagerFragment
 * 自营订单&拼团订单
 */

public class OrderManagerFragment extends BaseFragment {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private ArrayList fragments;
    private ArrayList titles;
    private TabPagerAdapter tabPagerAdapter;
    private int type;//0:自营,1:拼团
    private int position;

    public static OrderManagerFragment newInstance(int type,int position) {
        OrderManagerFragment buyerSaleFragment = new OrderManagerFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("position", position);
        buyerSaleFragment.setArguments(args);
        return buyerSaleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
            position = getArguments().getInt("position");
        }
    }

    @Override
    public View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_manager, container, false);
    }

    @Override
    public void prepareView() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();

//        WAIT_PAY("待付款"), PAID("已付款"), WAIT_DELIVER("待发货"),
//                DELIVERED("已发货"), RECEIVED("已收货"), SUCCESS("交易成功"),
//                WAIT_REFUND("待退款"), REFUNDED("已退款"), CLOSED("交易关闭");
        if(type==0){
            titles.add("全部");
            titles.add("待付款");
            titles.add("待发货");
            titles.add("已发货");
            titles.add("已完结");
            fragments.add(OrderListFragment.newInstance(null,type));//全部订单
            fragments.add(OrderListFragment.newInstance("WAIT_PAY",type));//待付款
            fragments.add(OrderListFragment.newInstance("WAIT_DELIVER",type));//待发货
            fragments.add(OrderListFragment.newInstance("DELIVERED",type));//已发货
            fragments.add(OrderListFragment.newInstance("SUCCESS",type));//已完结
        }else{
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            titles.add("全部");
            titles.add("待付款");
            titles.add("已付款");
            titles.add("待发货");
            titles.add("已发货");
            titles.add("待退款");
            titles.add("退款成功");
            fragments.add(OrderListFragment.newInstance(null,type));//全部订单
            fragments.add(OrderListFragment.newInstance("WAIT_PAY",type));//待付款
            fragments.add(OrderListFragment.newInstance("PAID",type));//已付款
            fragments.add(OrderListFragment.newInstance("WAIT_DELIVER",type));//待发货
            fragments.add(OrderListFragment.newInstance("DELIVERED",type));//已发货
            fragments.add(OrderListFragment.newInstance("WAIT_REFUND",type));//待退款
            fragments.add(OrderListFragment.newInstance("REFUNDED",type));//退款成功

        }

        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabPagerAdapter.notifyDataSetChanged();
        if(position>0){
            viewPager.setCurrentItem(position);
        }
//        initTab();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (getActivity() instanceof OrderManagerActivity){
                    OrderManagerActivity managerActivity= (OrderManagerActivity) getActivity();
                    if (i==0){
                        managerActivity.unLock();
                    }else {
                        managerActivity.lock();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void doBusiness() {
    }

    private void initTab() {
        if (tabLayout != null) {
            if (tabLayout.getTabAt(0) != null) {
                CustomerTabView allTab = new CustomerTabView(getActivity());
                allTab.setText(123,"全部订单");
                tabLayout.getTabAt(0).setCustomView(allTab);
            }
            if (tabLayout.getTabAt(1) != null) {
                CustomerTabView pendingPayTab = new CustomerTabView(getActivity());
                pendingPayTab.setText(6,"代付款");
                tabLayout.getTabAt(1).setCustomView(pendingPayTab);
            }
            if (tabLayout.getTabAt(2) != null) {
                CustomerTabView pendingDeliverTab = new CustomerTabView(getActivity());
                pendingDeliverTab.setText(7,"待发货");
                tabLayout.getTabAt(2).setCustomView(pendingDeliverTab);
            }
            if (tabLayout.getTabAt(3) != null) {
                CustomerTabView deliveredTab = new CustomerTabView(getActivity());
                deliveredTab.setText(8,"已发货");
                tabLayout.getTabAt(3).setCustomView(deliveredTab);
            }
            if (tabLayout.getTabAt(4) != null) {
                CustomerTabView overTab = new CustomerTabView(getActivity());
                overTab.setText(9,"已完结");
                tabLayout.getTabAt(4).setCustomView(overTab);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode== Constants.RequestCode.EDIT_ORDER){
            int currentItem = viewPager.getCurrentItem();
            if (!((OrderListFragment)fragments.get(currentItem)).isDetached()){
                ((OrderListFragment)fragments.get(currentItem)).refresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
