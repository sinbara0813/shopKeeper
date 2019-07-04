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
import android.widget.Toast;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.ui.fragment.CouponFragment;
import com.d2cmall.shopkeeper.ui.fragment.OrderManagerFragment;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/21.
 * Description : OrderManagerActivity
 * 订单管理
 */

public class OrderManagerActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_seach)
    ImageView ivSeach;
    @BindView(R.id.tv_order_self)
    TextView tvOrderSelf;
    @BindView(R.id.tv_order_collage)
    TextView tvOrderCollage;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    private OrderManagerFragment orderSelflFragment;
    private FragmentManager fragmentManager;
    private int currentIndex;
    private OrderManagerFragment collageOrderFragment;
    private int second;
    private int first;
    private SlidrInterface slidrInterface;


    public void setScanDeliveryCodeListener(ScanDeliveryCodeListener scanDeliveryCodeListener) {
        this.scanDeliveryCodeListener = scanDeliveryCodeListener;
    }

    private ScanDeliveryCodeListener scanDeliveryCodeListener;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_manager;
    }

    @Override
    public void doBusiness() {
        slidrInterface=Slidr.attach(this);
        Sofia.with(this).statusBarBackground(getResources().getColor(R.color.normal_blue));
        first = getIntent().getIntExtra("first", 0);
        second = getIntent().getIntExtra("second", 0);
        initFragment();
        if (first != 0) {
            changeFragment(first);
        }
    }

    private void initFragment() {
        //初始化两个fragment,一个自营,一个拼团
        orderSelflFragment = OrderManagerFragment.newInstance(0,second);
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
            tvOrderSelf.setTextColor(Color.parseColor("#FFFFFF"));
            tvOrderCollage.setTextColor(Color.parseColor("#b2ffffff"));
            if (collageOrderFragment != null) {
                ft.hide(collageOrderFragment);
            }
            ft.show(orderSelflFragment);
        } else {
            if (collageOrderFragment == null) {
                collageOrderFragment = OrderManagerFragment.newInstance(1,second);
                ft.add(R.id.fragment_container, collageOrderFragment);
            }
            tvOrderSelf.setTextColor(Color.parseColor("#b2ffffff"));
            tvOrderCollage.setTextColor(Color.parseColor("#FFFFFF"));
            ft.hide(orderSelflFragment);
            ft.show(collageOrderFragment);
        }
        currentIndex = index;
        ft.commit();
    }


    @OnClick({R.id.iv_back, R.id.iv_seach, R.id.tv_order_self, R.id.tv_order_collage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_seach:
                //售后
                startActivity(new Intent(this,CustomerAfterSaleListActivity.class));
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
            if(requestCode==Constants.RequestCode.SCAN_DELIVERY_CODE && data!=null){
                String deliveryCode = data.getStringExtra("deliveryCode");
                if(!StringUtil.isEmpty(deliveryCode) && scanDeliveryCodeListener!=null){
                    scanDeliveryCodeListener.scanDelivery(deliveryCode);
                } 
            }else if(requestCode==Constants.RequestCode.EDIT_ORDER){
                if(orderSelflFragment!=null){
                    orderSelflFragment.onActivityResult(requestCode,resultCode,data);
                }
                if(collageOrderFragment!=null){
                    collageOrderFragment.onActivityResult(requestCode,resultCode,data);
                }
            }
            
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(scanDeliveryCodeListener!=null){
            scanDeliveryCodeListener=null;
        }
    }

    public void removeScanDeliveryCodeListener(){
        if(scanDeliveryCodeListener!=null){
            scanDeliveryCodeListener=null;
        }
    }


    public interface ScanDeliveryCodeListener{
        void scanDelivery(String deliveryCode);
   }

    public void lock(){
        if (slidrInterface!=null){
            slidrInterface.lock();
        }
    }

    public void unLock(){
        if (slidrInterface!=null){
            slidrInterface.unlock();
        }
    }
}
