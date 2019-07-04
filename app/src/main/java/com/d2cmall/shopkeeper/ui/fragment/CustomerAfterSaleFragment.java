package com.d2cmall.shopkeeper.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.base.BaseFragment;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.ui.view.CustomerTabView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LWJ on 2019/2/21.
 * Description : OrderManagerFragment
 * 退货退款,退款(客户发起,B端发起)
 */

public class CustomerAfterSaleFragment extends BaseFragment {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private ArrayList fragments;
    private ArrayList titles;
    private TabPagerAdapter tabPagerAdapter;
    private int type;//0:退货退款,1:退款
    private int position;

    public static CustomerAfterSaleFragment newInstance(int type, int position) {
        CustomerAfterSaleFragment buyerSaleFragment = new CustomerAfterSaleFragment();
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
        return inflater.inflate(R.layout.fragment_customer_after_sale_list, container, false);
    }

    @Override
    public void prepareView() {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        tabLayout.setBackgroundColor(mContext.getResources().getColor(R.color.color_white));

//        WAIT_PAY("待付款"), PAID("已付款"), WAIT_DELIVER("待发货"),
//                DELIVERED("已发货"), RECEIVED("已收货"), SUCCESS("交易成功"),
//                WAIT_REFUND("待退款"), REFUNDED("已退款"), CLOSED("交易关闭");

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        if(type==0){
            titles.add("全部");
            titles.add("待审核");
            titles.add("待客户发货");
            titles.add("待收货");
            titles.add("待退款");
            titles.add("退款成功");
            titles.add("审核拒绝");
            fragments.add(CustomerAfterSaleSubFragment.newInstance(null,type));//全部
            fragments.add(CustomerAfterSaleSubFragment.newInstance("WAIT_RESHIP",type));//待审核
            fragments.add(CustomerAfterSaleSubFragment.newInstance("AGREE_RESHIP",type));//待客户发货
            fragments.add(CustomerAfterSaleSubFragment.newInstance("RESHIPED",type));//待收货
            fragments.add(CustomerAfterSaleSubFragment.newInstance("AGREE_REFUND",type));//待退款
            fragments.add(CustomerAfterSaleSubFragment.newInstance("REFUNDED",type));//退款成功
            fragments.add(CustomerAfterSaleSubFragment.newInstance("REFUSE_RESHIP",type));//审核拒绝
        }else{
            titles.add("全部");
            titles.add("待审核");
            titles.add("待退款");
            titles.add("退款成功");
            titles.add("审核拒绝");
            fragments.add(CustomerAfterSaleSubFragment.newInstance(null,type));//全部订单
            fragments.add(CustomerAfterSaleSubFragment.newInstance("WAIT_REFUND",type));//待审核
            fragments.add(CustomerAfterSaleSubFragment.newInstance("AGREE_REFUND",type));//待退款
            fragments.add(CustomerAfterSaleSubFragment.newInstance("REFUNDED",type));//退款成功
            fragments.add(CustomerAfterSaleSubFragment.newInstance("REFUSE_REFUND",type));//审核拒绝

        }

        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabPagerAdapter.notifyDataSetChanged();
        if(position>0){
            viewPager.setCurrentItem(position);
        }
//        initTab();
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

    @Subscribe
    public void onEvent(Action action){
        if (action.type==Constants.ActionType.AFTER_SALE_REFRESH){
            Log.e("han","售后全部预刷新");
            if (viewPager.getCurrentItem()!=0 && fragments.get(0)!=null){
                ((CustomerAfterSaleSubFragment)fragments.get(0)).preRefresh();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode== Constants.RequestCode.EDIT_ORDER){
            if (!((CustomerAfterSaleSubFragment)fragments.get(viewPager.getCurrentItem())).isDetached()){
                ((CustomerAfterSaleSubFragment)fragments.get(viewPager.getCurrentItem())).refresh();
            }
            if (viewPager.getCurrentItem()!=0 && fragments.get(0)!=null){
                Log.e("han","售后全部预刷新");
                ((CustomerAfterSaleSubFragment)fragments.get(0)).preRefresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
