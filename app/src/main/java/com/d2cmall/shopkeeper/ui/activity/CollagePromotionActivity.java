package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.ui.fragment.CollagePromotionFragment;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/3/1.
 * Description : CollagePromotionActivity
 * 拼团活动
 */

public class CollagePromotionActivity extends BaseActivity {
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
    private int type; //1优惠券 2商品

    @Override
    public int getLayoutId() {
        return R.layout.activity_transaction_record;
    }

    @Override
    public void doBusiness() {
        init();
    }

    private void init() {
        type=getIntent().getIntExtra("type",1);
        if (type==1){
            tvName.setText("优惠券拼团");
        }else {
            tvName.setText("商品拼团");
        }
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setBackgroundResource(R.mipmap.icon_add_black);
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(llTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("进行中");
        titles.add("未开始");
        titles.add("已结束");
        fragments.add(CollagePromotionFragment.newInstance(1,type==1?true:false));//进行中
        fragments.add(CollagePromotionFragment.newInstance(0,type==1?true:false));//未开始
        fragments.add(CollagePromotionFragment.newInstance(-1,type==1?true:false));//已结束
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setViewPager(viewPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //添加拼团活动弹窗
                //showAddPop();
                if (type==1){
                    startActivityForResult(new Intent(CollagePromotionActivity.this,AddCollageCouponActivity.class), Constants.RequestCode.ADD_COLLAGE_COUPON);
                }else {
                    startActivityForResult(new Intent(CollagePromotionActivity.this,AddCollageProductActivity.class), Constants.RequestCode.ADD_COLLAGE_PRODUCT);
                }
                break;
        }
    }

    private void showAddPop() {
        View popView = LayoutInflater.from(this).inflate(R.layout.layout_add_collage_promotion_pop, null);
        TextView tvCancle = popView.findViewById(R.id.tv_cancle);
        TextView tvAddCoupon = popView.findViewById(R.id.tv_add_coupon);
        LinearLayout llRoot = popView.findViewById(R.id.ll_root);
        TextView tvAddProduct = popView.findViewById(R.id.tv_add_product);
        PopupWindow popWindow = new PopupWindow(popView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popWindow!=null && popWindow.isShowing()){
                    popWindow.dismiss();
                }
            }
        });
        tvAddCoupon.setOnClickListener(new View.OnClickListener() {//添加优惠券
            @Override
            public void onClick(View v) {
                if(popWindow!=null && popWindow.isShowing()){
                    popWindow.dismiss();
                    startActivityForResult(new Intent(CollagePromotionActivity.this,AddCollageCouponActivity.class), Constants.RequestCode.ADD_COLLAGE_COUPON);
                }
            }
        });
        tvAddProduct.setOnClickListener(new View.OnClickListener() {//添加商品
            @Override
            public void onClick(View v) {
                if(popWindow!=null && popWindow.isShowing()){
                    popWindow.dismiss();
                    startActivityForResult(new Intent(CollagePromotionActivity.this,AddCollageProductActivity.class), Constants.RequestCode.ADD_COLLAGE_PRODUCT);
                }
            }
        });
        llRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popWindow!=null && popWindow.isShowing()){
                    popWindow.dismiss();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK && (requestCode==Constants.RequestCode.ADD_COLLAGE_PRODUCT || requestCode==Constants.RequestCode.ADD_COLLAGE_COUPON)  ){
            int currentItem = viewPager.getCurrentItem();
            if (fragments.get(currentItem)!=null && !((CollagePromotionFragment)fragments.get(currentItem)).isDetached()){
                ((CollagePromotionFragment)fragments.get(currentItem)).refresh();
            }
        }
    }

    public void notifyFragment() {
        if(fragments==null || fragments.size()==0){
            return;
        }
        for (int i = 0; i < fragments.size(); i++) {
            if ( (fragments.get(i)) !=null && ((CollagePromotionFragment) fragments.get(i)).isAdded() && !((CollagePromotionFragment) fragments.get(i)).isDetached()) {
                ((CollagePromotionFragment) fragments.get(i)).refresh();
            }
        }
    }
}
