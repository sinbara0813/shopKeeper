package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseTabFragment;
import com.d2cmall.shopkeeper.ui.fragment.MarketFragment;
import com.d2cmall.shopkeeper.ui.fragment.ShopFragment;
import com.d2cmall.shopkeeper.ui.fragment.ShopKeeperFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/2/18.
 * 邮箱:hrb940258169@163.com
 */

public class ShopKeeperTabLayout extends LinearLayout {

    @BindView(R.id.shop_keeper_iv)
    ImageView shopKeeperIv;
    @BindView(R.id.mine_iv)
    ImageView mineIv;
    @BindView(R.id.market_iv)
    ImageView marketIv;

    private BaseTabFragment shopKeeperFragment;
    private BaseTabFragment marketFragment;
    private BaseTabFragment mineFragment;

    private int lastShowIndex=-1;
    private BaseTabFragment lastShowFragment;
    private FragmentManager fragmentManager;

    public ShopKeeperTabLayout(Context context) {
        super(context);
    }

    public ShopKeeperTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater.from(context).inflate(R.layout.layout_shop_keeper_tab, this);
        ButterKnife.bind(this, this);
        if (context instanceof FragmentActivity) {
            fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        } else {
            throw new NullPointerException("context must be FragmentActivity");
        }
    }

    @OnClick({R.id.shop_keeper_iv,R.id.market_iv,R.id.mine_iv})
    public void onClick(View view){
        int index=0;
        switch (view.getId()){
            case R.id.shop_keeper_iv:
                index=0;
                break;
            case R.id.market_iv:
                index=1;
                break;
            case R.id.mine_iv:
                index=2;
                break;
        }
        switchFragment(index);
    }

    public void init(int... position) {
        switchFragment(position);
    }

    private void switchFragment(int... position) {
        if (position.length == 0) {
            return;
        }
        int index = position[0];
        if (index == lastShowIndex) {
            return;
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (lastShowFragment != null) {
            lastShowFragment.hide();
            ft.hide(lastShowFragment);
        }
        checkSelect(index);
        switch (index) {
            case 0://工作台
                shopKeeperIv.setSelected(true);
                if (shopKeeperFragment == null) {
                    shopKeeperFragment = new ShopKeeperFragment();
                    ft.add(R.id.fragment_container, shopKeeperFragment);
                } else {
                    ft.show(shopKeeperFragment);
                }
                lastShowFragment = shopKeeperFragment;
                break;
            case 1://选货市场
                marketIv.setSelected(true);
                if (marketFragment==null){
                    marketFragment=new MarketFragment();
                    ft.add(R.id.fragment_container,marketFragment);
                }else {
                    ft.show(marketFragment);
                }
                lastShowFragment=marketFragment;
                break;
            case 2://我的
                mineIv.setSelected(true);
                if (mineFragment == null) {
                    mineFragment = new ShopFragment();
                    ft.add(R.id.fragment_container, mineFragment);
                } else {
                    ft.show(mineFragment);
                }
                lastShowFragment = mineFragment;
                break;
        }
        lastShowFragment.show();
        ft.commitAllowingStateLoss();
        lastShowIndex = index;
    }

    private void checkSelect(int index){
        setUnSelect();
        switch (index){
            case 0:
                shopKeeperIv.setImageResource(R.mipmap.icon_tab_workbench_pre);
                break;
            case 1:
                marketIv.setImageResource(R.mipmap.icon_tab_market_pre);
                break;
            case 2:
                mineIv.setImageResource(R.mipmap.icon_tab_mine_pre);
                break;
        }
    }

    private void setUnSelect(){
        shopKeeperIv.setImageResource(R.mipmap.icon_tab_workbench_nor);
        mineIv.setImageResource(R.mipmap.icon_tab_mine_nor);
        marketIv.setImageResource(R.mipmap.icon_tab_market_nor);
    }

}
