package com.d2cmall.shopkeeper.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseFragment;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.activity.AddProductActivity;
import com.d2cmall.shopkeeper.ui.activity.BrandAuthenticationActivity;
import com.d2cmall.shopkeeper.ui.activity.CategoryManagerActivity;
import com.d2cmall.shopkeeper.ui.activity.ProductManagerActivity;
import com.d2cmall.shopkeeper.ui.view.FilterPop;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.Util;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 作者:Created by sinbara on 2019/4/28.
 * 邮箱:hrb940258169@163.com
 */

public class ProductManagerFragment extends BaseFragment {

    @BindView(R.id.filter_tv)
    TextView filterTv;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_add_product)
    TextView tvAddProduct;
    @BindView(R.id.tv_classification_manager)
    TextView tvClassificationManager;

    private ArrayList fragments;
    private ArrayList titles;
    private TabPagerAdapter tabPagerAdapter;
    private FilterPop pop;

    @Override
    public View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_manager, container, false);
    }

    @Override
    public void prepareView() {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("出售商品");
        titles.add("下架商品");
        fragments.add(ProductFragment.newInstance(1));//出售商品
        fragments.add(ProductFragment.newInstance(0));//仓库商品
        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setViewPager(viewPager);
        tabPagerAdapter.notifyDataSetChanged();
        ShadowDrawable.setShadowDrawable(tvAddProduct, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(7), 0, 0);
        ShadowDrawable.setShadowDrawable(tvClassificationManager, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(7), 0, 0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (getActivity() instanceof ProductManagerActivity){
                    ProductManagerActivity managerActivity= (ProductManagerActivity) getActivity();
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

    @OnClick({R.id.tv_add_product, R.id.tv_classification_manager, R.id.filter_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.filter_tv:
                showFilterPop();
                break;
            case R.id.tv_add_product:
                //添加商品
                addProduct();
                break;
            case R.id.tv_classification_manager:
                //分类管理
                Intent intent = new Intent(mContext, CategoryManagerActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    private void showFilterPop() {
        if (pop!=null&&pop.isShow()){
            pop.dismiss();
            return;
        }
        if (pop!=null){
            pop=null;
        }
        String[][] data = new String[][]{{"拼团"}
        };
        pop =new FilterPop(mContext, data, new FilterPop.SelectListener() {
            @Override
            public void select(int... value) {
                if (!((ProductFragment) fragments.get(viewPager.getCurrentItem())).isDetached()) {
                    ((ProductFragment) fragments.get(viewPager.getCurrentItem())).setAllot(value[0]);
                    ((ProductFragment) fragments.get(viewPager.getCurrentItem())).setBuyout(value[1]);
                    ((ProductFragment) fragments.get(viewPager.getCurrentItem())).setCrowd(value[2]);
                    ((ProductFragment) fragments.get(viewPager.getCurrentItem())).refresh();
                }
            }
        });
        /*pop=new FilterPop1(mContext, data, new FilterPop1.SelectListener() {
            @Override
            public void select(@NotNull int... value) {
                if (!((ProductFragment) fragments.get(viewPager.getCurrentItem())).isDetached()) {
                    ((ProductFragment) fragments.get(viewPager.getCurrentItem())).setAllot(value[0]);
                    ((ProductFragment) fragments.get(viewPager.getCurrentItem())).setBuyout(value[1]);
                    ((ProductFragment) fragments.get(viewPager.getCurrentItem())).setCrowd(value[2]);
                    ((ProductFragment) fragments.get(viewPager.getCurrentItem())).refresh();
                }
            }
        });*/
        pop.show(filterTv);
    }

    private void addProduct() {
        UserBean userBean = Session.getInstance().getUserFromFile(mContext);
        if (userBean == null) {
            Util.showToast(mContext, "请先登录");
            return;
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(userBean.getShopId()), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBean) {
                if (shopBean.getData().getAuthenticate() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("发布商品前请先认证店铺")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext, BrandAuthenticationActivity.class);
                                    getActivity().startActivity(intent);
                                }
                            }).show();

                } else {
                    Intent intent = new Intent(mContext, AddProductActivity.class);
                    getActivity().startActivityForResult(intent, 100);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (!((ProductFragment) fragments.get(0)).isDetached()) {
                ((ProductFragment) fragments.get(0)).refresh();
            }
            if (!((ProductFragment) fragments.get(1)).isDetached()) {
                ((ProductFragment) fragments.get(1)).refresh();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
