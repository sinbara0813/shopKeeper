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
import com.d2cmall.shopkeeper.ui.fragment.CustomerAfterSaleFragment;
import com.d2cmall.shopkeeper.ui.fragment.PurchaseAllocationWarehousingFragment;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/4/28.
 * Description : PurchaseAllocationWarehousingListActivity
 * 采购入库调拨入库列表页
 */

public class PurchaseAllocationWarehousingListActivity extends BaseActivity {
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
    private PurchaseAllocationWarehousingFragment orderSelflFragment;
    private FragmentManager fragmentManager;
    private int currentIndex;
    private PurchaseAllocationWarehousingFragment collageOrderFragment;
    private int second;
    private int first;
    private boolean isSpecial;
    private SlidrInterface slidrInterface;

    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_after_sale_list;
    }

    public void doBusiness() {
        slidrInterface=Slidr.attach(this);
        Sofia.with(this).statusBarBackground(getResources().getColor(R.color.normal_blue));
        first = getIntent().getIntExtra("first", 0);
        second = getIntent().getIntExtra("second", 0);
        isSpecial=getIntent().getBooleanExtra("special",false);
        tvOrderSelf.setText("采购入库");
        tvOrderCollage.setText("调拨入库");
        if (isSpecial){
            tvOrderCollage.setVisibility(View.GONE);
        }
        ivBack.setImageResource(R.mipmap.icon_nav_back_white);
        tvOrderSelf.setTextColor(Color.parseColor("#ffffff"));
        tvOrderCollage.setTextColor(Color.parseColor("#b2ffffff"));
        rlTitle.setBackgroundColor(getResources().getColor(R.color.normal_blue));
        initFragment();
        if (first != 0) {
            changeFragment(first);
        }
    }

    private void initFragment() {
        //初始化两个fragment,一个采购入库,一个调拨入库
        orderSelflFragment = PurchaseAllocationWarehousingFragment.newInstance(0,second);
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
            tvOrderSelf.setTextColor(Color.parseColor("#ffffff"));
            tvOrderCollage.setTextColor(Color.parseColor("#b2ffffff"));
            if (collageOrderFragment != null) {
                ft.hide(collageOrderFragment);
            }
            ft.show(orderSelflFragment);
        } else {
            if (collageOrderFragment == null) {
                collageOrderFragment = PurchaseAllocationWarehousingFragment.newInstance(1,second);
                ft.add(R.id.fragment_container, collageOrderFragment);
            }
            tvOrderSelf.setTextColor(Color.parseColor("#b2ffffff"));
            tvOrderCollage.setTextColor(Color.parseColor("#ffffff"));
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
            if(requestCode== Constants.RequestCode.EDIT_ORDER){
                if(orderSelflFragment!=null){
                    orderSelflFragment.onActivityResult(requestCode,resultCode,data);
                }
                if(collageOrderFragment!=null){
                    collageOrderFragment.onActivityResult(requestCode,resultCode,data);
                }
            }

        }
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
