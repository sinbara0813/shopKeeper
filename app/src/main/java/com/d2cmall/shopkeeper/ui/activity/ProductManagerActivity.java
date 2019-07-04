package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.ui.fragment.ProductManagerFragment;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/3/18.
 * 邮箱:hrb940258169@163.com
 * 商品管理
 */

public class ProductManagerActivity extends BaseActivity {

    @BindView(R.id.product_manager_tv)
    TextView productManagerTv;

    private Fragment productManagerFragment;
    private Fragment marketFragment;
    private Fragment lastFragment;
    private SlidrInterface slidrInterface;

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_manager1;
    }

    @Override
    public void doBusiness() {
        slidrInterface=Slidr.attach(this);
        Sofia.with(this).statusBarBackground(getResources().getColor(R.color.normal_blue));
        changePage(true);
    }

    @OnClick({R.id.iv_back,R.id.product_manager_tv})
    public void click(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.product_manager_tv:
                changePage(true);
                break;
        }
    }

    private void changePage(boolean isProduct){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (lastFragment!=null){
            ft.hide(lastFragment);
        }
        if (isProduct){
            productManagerTv.setTextColor(getResources().getColor(R.color.color_white));
            if (productManagerFragment==null){
                productManagerFragment=new ProductManagerFragment();
                ft.add(R.id.frame_layout,productManagerFragment);
            }else {
                ft.show(productManagerFragment);
            }
            lastFragment=productManagerFragment;
        }else {
            productManagerTv.setTextColor(getResources().getColor(R.color.color_white1));
            if (marketFragment==null){
                //marketFragment=MarketFragment1.newInstance(false);
                ft.add(R.id.frame_layout,marketFragment);
            }else {
                ft.show(marketFragment);
            }
            lastFragment=marketFragment;
        }
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (lastFragment==productManagerFragment){
            productManagerFragment.onActivityResult(requestCode,resultCode,data);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
