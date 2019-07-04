package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.ProductSettleAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.decoration.DividerDecoration;
import com.d2cmall.shopkeeper.model.AllotSkuBean;
import com.d2cmall.shopkeeper.model.CartBean;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/5/6.
 * 邮箱:hrb940258169@163.com
 * 货品结算
 */

public class ProductSettleActivity extends BaseActivity {
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;

    private VirtualLayoutManager layoutManager;
    private DelegateAdapter delegateAdapter;
    private ProductSettleAdapter cartAdapter;
    public double cash;
    private List<List<AllotSkuBean>> cartList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_settle;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        layoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(layoutManager, true);
        cartAdapter = new ProductSettleAdapter(this, cartList);
        delegateAdapter.addAdapter(cartAdapter);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(delegateAdapter);
        recycleView.addItemDecoration(new DividerDecoration(Color.parseColor("#fff6f7fa"), ScreenUtil.dip2px(10)));
        loadData();
    }

    private void loadData() {
        addDisposable(ApiRetrofit.getInstance().getApiService().allotStatement(), new BaseObserver<BaseModel<List<AllotSkuBean>>>() {
            @Override
            public void onSuccess(BaseModel<List<AllotSkuBean>> o) {
                if (o.getData() != null && o.getData().size() > 0) {
                    cartList.clear();
                    List<AllotSkuBean> list = o.getData();
                    Collections.sort(list, new Comparator<AllotSkuBean>() {
                        @Override
                        public int compare(AllotSkuBean o1, AllotSkuBean o2) {
                            return (int) (o1.getShopId() - o2.getShopId());
                        }
                    });
                    int size = list.size();
                    List<AllotSkuBean> cartBeans = null;
                    for (int i = 0; i < size; i++) {
                        if (i == 0 || (list.get(i).getShopId() != list.get(i - 1).getShopId())) {
                            cartBeans = new ArrayList<>();
                            cartList.add(cartBeans);
                        }
                        list.get(i).setProductName(list.get(i).getProduct().getName());
                        list.get(i).setProductPic(list.get(i).getProduct().getFirstPic());
                        list.get(i).setProductPrice(list.get(i).getSupplyPrice());
                        list.get(i).setQuantity(list.get(i).getSettleStock());
                        cartBeans.add(list.get(i));
                    }
                    cartAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public String[] getNeedPermission() {
        return new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public void refusePermission() {
        super.refusePermission();
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Action action) {
        if (action.type == Constants.ActionType.WXPAYRESULT) {
            int code = (int) action.get("code");
            switch (code) {
                case 0: //支付成功
                    toPayStateActivity(3);
                    break;
                case -1: //支付失败
                case -2: //支付取消
                    toPayStateActivity(4);
                    break;
            }
        }
    }

    public void toPayStateActivity(int type) {
        Intent intent = new Intent(this, PayStateActivity.class);
        intent.putExtra("type", type);
        if (cash != 0) {
            intent.putExtra("cash", cash);
        }
        startActivity(intent);
        if (type == 3) {
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.back_iv)
    public void onViewClicked() {
        finish();
    }
}
