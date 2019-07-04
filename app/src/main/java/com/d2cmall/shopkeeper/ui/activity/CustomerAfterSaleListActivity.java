package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.ui.fragment.CollagePromotionFragment;
import com.d2cmall.shopkeeper.ui.fragment.CustomerAfterSaleFragment;
import com.d2cmall.shopkeeper.ui.fragment.OrderManagerFragment;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/4/26.
 * Description : 客户申请售后列表页
 */

public class CustomerAfterSaleListActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_order_self)
    TextView tvOrderSelf;
    @BindView(R.id.tv_order_collage)
    TextView tvOrderCollage;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    private CustomerAfterSaleFragment orderSelflFragment;
    private FragmentManager fragmentManager;
    private int currentIndex;
    private CustomerAfterSaleFragment collageOrderFragment;
    private int second;
    private int first;



    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_after_sale_list;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().invasionNavigationBar().statusBarBackground(Color.parseColor("#FFFFFF"));
        first = getIntent().getIntExtra("first", 0);
        second = getIntent().getIntExtra("second", 0);
        initFragment();
        if (first != 0) {
            changeFragment(first);
        }
    }

    private void initFragment() {
        //初始化两个fragment,一个退货退款,一个退款
        orderSelflFragment = CustomerAfterSaleFragment.newInstance(0,second);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.fragment_container, orderSelflFragment);
        ft.show(orderSelflFragment);
        ft.commit();
    }

    private void changeFragment(int index) {
        //传入index判断切换哪个fragment
        if (currentIndex == index) {
            return;
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (index == 0) {
            tvOrderSelf.setTextColor(Color.parseColor("#333333"));
            tvOrderCollage.setTextColor(Color.parseColor("#8d92a3"));
            if (collageOrderFragment != null) {
                ft.hide(collageOrderFragment);
            }
            ft.show(orderSelflFragment);
        } else {
            if (collageOrderFragment == null) {
                collageOrderFragment = CustomerAfterSaleFragment.newInstance(1,second);
                ft.add(R.id.fragment_container, collageOrderFragment);
            }
            tvOrderSelf.setTextColor(Color.parseColor("#8d92a3"));
            tvOrderCollage.setTextColor(Color.parseColor("#333333"));
            ft.hide(orderSelflFragment);
            ft.show(collageOrderFragment);
        }
        currentIndex = index;
        ft.commit();
    }


    @OnClick({R.id.iv_back, R.id.tv_order_self, R.id.tv_order_collage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_order_self:
                //自营订单
                changeFragment(0);
                break;
            case R.id.tv_order_collage:
                //拼团订单
                changeFragment(1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK  ){
           if(requestCode==Constants.RequestCode.EDIT_ORDER){
                if(orderSelflFragment!=null){
                    orderSelflFragment.onActivityResult(requestCode,resultCode,data);
                }
                if(collageOrderFragment!=null){
                    collageOrderFragment.onActivityResult(requestCode,resultCode,data);
                }
            }
            
        }
    }

}
