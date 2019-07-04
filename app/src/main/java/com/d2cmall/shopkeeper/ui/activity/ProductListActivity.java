package com.d2cmall.shopkeeper.ui.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.ui.fragment.CategoryProductFragment;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/3/18.
 * 邮箱:hrb940258169@163.com
 * 分类商品列表
 */

public class ProductListActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private ArrayList fragments;
    private ArrayList titles;
    private TabPagerAdapter tabPagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_list;
    }

    @Override
    public void doBusiness() {
        init();
    }

    private void init() {
        String name=getIntent().getStringExtra("name");
        nameTv.setText(name);
        String categoryId = getIntent().getStringExtra("categoryIds");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("上架");
        titles.add("下架");
        fragments.add(CategoryProductFragment.newInstance(1,categoryId));//行为记录
        fragments.add(CategoryProductFragment.newInstance(0,categoryId));//订单记录
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setViewPager(viewPager);
        tabPagerAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.iv_back)
    public void click(View view){
        finish();
    }
}
