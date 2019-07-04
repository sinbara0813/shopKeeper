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

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.base.BaseFragment;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.ui.activity.ProductManagerActivity;
import com.d2cmall.shopkeeper.ui.activity.PurchaseAllocationWarehousingListActivity;
import com.d2cmall.shopkeeper.ui.view.CustomerTabView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LWJ on 2019/4/28.
 * Description : PurchaseAllocationWarehousingFragment
 * 采购入库&调拨入库
 */

public class PurchaseAllocationWarehousingFragment extends BaseFragment {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private ArrayList fragments;
    private ArrayList titles;
    private TabPagerAdapter tabPagerAdapter;
    private int type;//0:采购入库,1:调拨入库
    private int position;

    public static PurchaseAllocationWarehousingFragment newInstance(int type, int position) {
        PurchaseAllocationWarehousingFragment buyerSaleFragment = new PurchaseAllocationWarehousingFragment();
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
        tabLayout.setBackgroundColor(mContext.getResources().getColor(R.color.normal_blue));
        tabLayout.setTabTextColors(getResources().getColor(R.color.trans_60_color_white),getResources().getColor(R.color.color_white));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //采购//
        //
        //WAIT_PAY("待付款"), PAID("已付款"), DELIVER("已发货"), RECEIVE("已入库"), CLOSE("已关闭");
        //
        if(type==0){
            titles.add("全部");
            titles.add("待付款");
            titles.add("待发货");
            titles.add("待入库");
            titles.add("待确认");
            titles.add("已入库");
            titles.add("已关闭");
            fragments.add(PurchaseWarehousingWholeFragment.newInstance(null, type));//全部订单
            fragments.add(PurchaseWarehousingWholeFragment.newInstance("WAIT_PAY", type));//待付款
            fragments.add(PurchaseWarehousingSplitFragment.newInstance("PAID", type));//待发货
            fragments.add(PurchaseWarehousingSplitFragment.newInstance("DELIVER", type));//待入库
            fragments.add(PurchaseWarehousingSplitFragment.newInstance("DIFFERENCE", type));//待确认
            fragments.add(PurchaseWarehousingSplitFragment.newInstance("RECEIVE", type));//已入库
            fragments.add(PurchaseWarehousingSplitFragment.newInstance("CLOSE", type));//已关闭
        }else{
            //调拨//
            //APPLY("待审核"), AGREE("待发货"), DELIVER("已发货"), RECEIVE("已入库"), CLOSE("已关闭");
            //
            titles.add("全部");
            titles.add("待审核");
            titles.add("待发货");
            titles.add("已发货");
            titles.add("待确认");
            titles.add("已入库");
            titles.add("已关闭");
            fragments.add(AllotWarehousingWholeFragment.newInstance(null, type));//全部订单
            fragments.add(AllotWarehousingWholeFragment.newInstance("APPLY", type));//待审核
            fragments.add(AllotWarehousingSplitFragment.newInstance("AGREE", type));//待发货
            fragments.add(AllotWarehousingSplitFragment.newInstance("DELIVER", type));//已发货
            fragments.add(AllotWarehousingSplitFragment.newInstance("DIFFERENCE", type));//待确认
            fragments.add(AllotWarehousingSplitFragment.newInstance("RECEIVE", type));//已入库
            fragments.add(AllotWarehousingSplitFragment.newInstance("CLOSE", type));//已关闭
        }


        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabPagerAdapter.notifyDataSetChanged();
        if (position > 0) {
            viewPager.setCurrentItem(position);
        }
//        initTab();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (getActivity() instanceof PurchaseAllocationWarehousingListActivity){
                    PurchaseAllocationWarehousingListActivity managerActivity= (PurchaseAllocationWarehousingListActivity) getActivity();
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
                allTab.setText(123, "全部订单");
                tabLayout.getTabAt(0).setCustomView(allTab);
            }
            if (tabLayout.getTabAt(1) != null) {
                CustomerTabView pendingPayTab = new CustomerTabView(getActivity());
                pendingPayTab.setText(6, "代付款");
                tabLayout.getTabAt(1).setCustomView(pendingPayTab);
            }
            if (tabLayout.getTabAt(2) != null) {
                CustomerTabView pendingDeliverTab = new CustomerTabView(getActivity());
                pendingDeliverTab.setText(7, "待发货");
                tabLayout.getTabAt(2).setCustomView(pendingDeliverTab);
            }
            if (tabLayout.getTabAt(3) != null) {
                CustomerTabView deliveredTab = new CustomerTabView(getActivity());
                deliveredTab.setText(8, "已发货");
                tabLayout.getTabAt(3).setCustomView(deliveredTab);
            }
            if (tabLayout.getTabAt(4) != null) {
                CustomerTabView overTab = new CustomerTabView(getActivity());
                overTab.setText(9, "已完结");
                tabLayout.getTabAt(4).setCustomView(overTab);
            }
        }
    }

    @Subscribe
    public void onEvent(Action action){
        if (action.type==Constants.ActionType.PURCHASE_ALLOT_REFRESH){
            Log.e("han","调拨采购全部预刷新");
            if (fragments.get(0)!=null&&fragments.get(0) instanceof AllotWarehousingWholeFragment){
                ((AllotWarehousingWholeFragment) fragments.get(0)).preRefresh();
            }
            if (fragments.get(0)!=null&&fragments.get(0) instanceof PurchaseWarehousingWholeFragment){
                ((PurchaseWarehousingWholeFragment) fragments.get(0)).preRefresh();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Constants.RequestCode.EDIT_ORDER) {
            int currentItem = viewPager.getCurrentItem();
            if(fragments.get(currentItem) instanceof AllotWarehousingSplitFragment){
                if (!((AllotWarehousingSplitFragment) fragments.get(currentItem)).isDetached()) {
                    ((AllotWarehousingSplitFragment) fragments.get(currentItem)).refresh();
                }
            }else if(fragments.get(currentItem) instanceof PurchaseWarehousingSplitFragment){
                if (!((PurchaseWarehousingSplitFragment) fragments.get(currentItem)).isDetached()) {
                    ((PurchaseWarehousingSplitFragment) fragments.get(currentItem)).refresh();
                }
            }else if(fragments.get(currentItem) instanceof PurchaseWarehousingWholeFragment){
                if (!((PurchaseWarehousingWholeFragment) fragments.get(currentItem)).isDetached()) {
                    ((PurchaseWarehousingWholeFragment) fragments.get(currentItem)).refresh();
                }
            }else if(fragments.get(currentItem) instanceof AllotWarehousingWholeFragment){
                if (!((AllotWarehousingWholeFragment) fragments.get(currentItem)).isDetached()) {
                    ((AllotWarehousingWholeFragment) fragments.get(currentItem)).refresh();
                }
            }
            if (currentItem!=0){
                Log.e("han","调拨采购全部预刷新");
                if (fragments.get(0)!=null&&fragments.get(0) instanceof AllotWarehousingWholeFragment){
                    ((AllotWarehousingWholeFragment) fragments.get(0)).preRefresh();
                }
                if (fragments.get(0)!=null&&fragments.get(0) instanceof PurchaseWarehousingWholeFragment){
                    ((PurchaseWarehousingWholeFragment) fragments.get(0)).preRefresh();
                }
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
