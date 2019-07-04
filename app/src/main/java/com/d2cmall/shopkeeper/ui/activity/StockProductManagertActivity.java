package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.StockDataBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.fragment.AllotProductFragment;
import com.d2cmall.shopkeeper.ui.fragment.PurchaseProductFragment;
import com.d2cmall.shopkeeper.ui.view.RoundedImageView;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.ui.view.headerViewPager.HeaderViewPager;
import com.d2cmall.shopkeeper.ui.view.headerViewPager.HeaderViewPagerFragment;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.Util;
import com.flyco.tablayout.SlidingTabLayout;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/4/25.
 * Description : StockProductManagertActivity
 * 进货管理界面
 */

public class StockProductManagertActivity extends BaseActivity {
    @BindView(R.id.tv_brand_name)
    TextView tvBrandName;
    @BindView(R.id.tv_brand_id)
    TextView tvBrandId;
    @BindView(R.id.iv_brand_head_pic)
    RoundedImageView ivBrandHeadPic;
    @BindView(R.id.rl_brand_info)
    RelativeLayout rlBrandInfo;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_clear_margin)
    TextView tvClearMargin;
    @BindView(R.id.rl_margin)
    RelativeLayout rlMargin;
    @BindView(R.id.iv_pending_refund_tip)
    ImageView ivPendingRefundTip;
    @BindView(R.id.tv_purchase_num)
    TextView tvPurchaseNum;
    @BindView(R.id.rl_pending_refund)
    RelativeLayout rlPendingRefund;
    @BindView(R.id.tv_allocation_num)
    TextView tvAllocationNum;
    @BindView(R.id.pending_deliver)
    RelativeLayout pendingDeliver;
    @BindView(R.id.tv_pending_return_num)
    TextView tvPendingReturnNum;
    @BindView(R.id.rl_pending_writeoff)
    RelativeLayout rlPendingWriteoff;
    @BindView(R.id.ll_pending)
    LinearLayout llPending;
    @BindView(R.id.tv_stock_goods)
    TextView tvStockGoods;
    @BindView(R.id.tv_after_sale)
    TextView tvAfterSale;
    @BindView(R.id.tv_allocation)
    TextView tvAllocation;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.scrollableLayout)
    HeaderViewPager scrollableLayout;
    @BindView(R.id.back_iv1)
    ImageView backIv1;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.title_fl)
    RelativeLayout titleFl;

    private ArrayList fragments;
    private ArrayList titles;
    private TabPagerAdapter tabPagerAdapter;
    private UserBean userBean;
    private SlidrInterface slidrInterface;

    @Override
    public int getLayoutId() {
        return R.layout.activity_stock_product_manager;
    }

    @Override
    public void doBusiness() {
        slidrInterface=Slidr.attach(this);
        Sofia.with(this).statusBarDarkFont().statusBarBackground(ContextCompat.getColor(this,R.color.normal_blue));
        initView();
        loadBrandInfo(false);
        loadStockData();
    }

    private void loadStockData() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getStockData(),  new BaseObserver<BaseModel<StockDataBean>>() {
            @Override
            public void onSuccess(BaseModel<StockDataBean> stockDataBean) {
                tvPurchaseNum.setText(String.valueOf(stockDataBean.getData().getPurchCount()));
                tvAllocationNum.setText(String.valueOf(stockDataBean.getData().getAllotCount()));
                tvPendingReturnNum.setText(String.valueOf(stockDataBean.getData().getAfterCount()));
            }


        });
    }

    private void initView() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("调拨记录");
        titles.add("采购记录");
        fragments.add(AllotProductFragment.newInstance(1));//免费拿记录
        fragments.add(PurchaseProductFragment.newInstance(0));//买断记录
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setViewPager(viewPager);
        tabPagerAdapter.notifyDataSetChanged();
        scrollableLayout.setCurrentScrollableContainer((HeaderViewPagerFragment)fragments.get(0));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.setCurrentScrollableContainer((HeaderViewPagerFragment)fragments.get(position));
                if (position==0){
                    slidrInterface.unlock();
                }else {
                    slidrInterface.lock();
                }
            }
        });
        ShadowDrawable.setShadowDrawable(llPending, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"),  ScreenUtil.dip2px(16), 0, 0);
    }

    private void loadBrandInfo(boolean refresh) {
        userBean = Session.getInstance().getUserFromFile(this);
        if(userBean==null){
            return;
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(userBean.getShopId()), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBean) {
                ShopBean data = shopBean.getData();
                if(!refresh){
                    tvBrandName.setText(data.getName());
                    tvBrandId.setText("店铺id: "+data.getId());
                }
                tvAmount.setText(Util.getNumberFormat(data.getDeposit()));
                ImageLoader.displayRoundImage(StockProductManagertActivity.this,shopBean.getData().getLogo(),ivBrandHeadPic,R.mipmap.icon_default_avatar);

            }
        });
    }

    @OnClick({R.id.tv_clear_margin, R.id.rl_pending_refund, R.id.pending_deliver, R.id.rl_pending_writeoff, R.id.tv_stock_goods, R.id.tv_after_sale, R.id.tv_allocation, R.id.tv_return, R.id.back_iv1})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_clear_margin:
                //结算保证金
                if(Util.loginChecked(this,999)){
                    intent = new Intent(this, ProductSettleActivity.class);
                    startActivityForResult(intent, Constants.RequestCode.CLEAR_BOND);
                }
                break;
            case R.id.rl_pending_refund:
                //采购待发货
                if(Util.loginChecked(this,999)){
                    intent = new Intent(this, PurchaseAllocationWarehousingListActivity.class);
                    intent.putExtra("second",2);
                    startActivity(intent);
                }
                break;
            case R.id.pending_deliver:
                //调拨待发货
                if(Util.loginChecked(this,999)){
                    intent = new Intent(this, PurchaseAllocationWarehousingListActivity.class);
                    intent.putExtra("first",1);
                    intent.putExtra("second",2);
                    startActivity(intent);
                }
                break;
            case R.id.rl_pending_writeoff:
                //待调拨退回
                Util.showToast(this,"即将开通，敬请期待~");
                break;
            case R.id.tv_stock_goods:
                //采购订单
                if(Util.loginChecked(this,999)){
                    intent = new Intent(this, PurchaseAllocationWarehousingListActivity.class);
                    intent.putExtra("first",0);
                    startActivity(intent);
                }
                break;
            case R.id.tv_after_sale:
                //售后
                Util.showToast(this,"即将开通，敬请期待~");
                break;
            case R.id.tv_allocation:
                //调拨订单
                if(Util.loginChecked(this,999)){
                    intent = new Intent(this, PurchaseAllocationWarehousingListActivity.class);
                    intent.putExtra("first",1);
                    startActivity(intent);
                }
                break;
            case R.id.tv_return:
                //调拨退回
                Util.showToast(this,"即将开通，敬请期待~");
                break;
            case R.id.back_iv1:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode== Constants.RequestCode.CLEAR_BOND){
            loadBrandInfo(true);
        }
    }
}
