package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.CartAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.decoration.DividerDecoration;
import com.d2cmall.shopkeeper.model.CartBean;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 作者:Created by sinbara on 2019/4/30.
 * 邮箱:hrb940258169@163.com
 * 购物车
 */

public class CartActivity extends BaseActivity {

    @BindView(R.id.edit_tv)
    TextView editTv;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.ptr)
    PtrStoreHouseFrameLayout ptr;

    private VirtualLayoutManager layoutManager;
    private DelegateAdapter delegateAdapter;
    private CartAdapter cartAdapter;
    private List<List<CartBean>> cartList = new ArrayList<>();

    private boolean isEdit = false;
    private boolean isRefresh;
    private int p=1;
    private long selfShopId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cart;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        if (Session.getInstance().getUserFromFile(this)!=null){
            selfShopId= Session.getInstance().getUserFromFile(this).getShopId();
        }
        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });
        layoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(layoutManager, true);
        cartAdapter = new CartAdapter(this, cartList);
        delegateAdapter.addAdapter(cartAdapter);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(delegateAdapter);
        editTv.setEnabled(false);
        recycleView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.bg_color), ScreenUtil.dip2px(10)));
        loadCartList();
    }

    @OnClick({R.id.back_iv, R.id.edit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.edit_tv:
                isEdit = !isEdit;
                if (isEdit) {
                    editTv.setText("完成");
                } else {
                    editTv.setText("编辑");
                }
                cartAdapter.isEdit(isEdit);
                cartAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void refresh(){
        isRefresh=true;
        loadCartList();
    }

    public void loadCartList() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getCartList(Util.buildListParam(p,40)), new BaseObserver<BaseModel<List<CartBean>>>() {
            @Override
            public void onSuccess(BaseModel<List<CartBean>> o) {
                if (isRefresh){
                    ptr.refreshComplete();
                    isRefresh=false;
                }
                if (o.getData() != null && o.getData().size() > 0) {
                    if (p==1){
                        cartList.clear();
                    }
                    List<CartBean> list = o.getData();
                    Collections.sort(list, new Comparator<CartBean>() {
                        @Override
                        public int compare(CartBean o1, CartBean o2) {
                            return (int) (o1.getShopId() - o2.getShopId());
                        }
                    });
                    int size = list.size();
                    List<CartBean> cartBeans = null;
                    for (int i = 0; i < size; i++) {
                        if (list.get(i).getShopId()==selfShopId){
                            continue;
                        }
                        if (i == 0 || (list.get(i).getShopId() != list.get(i - 1).getShopId())) {
                            cartBeans = new ArrayList<>();
                            cartList.add(cartBeans);
                        }
                        list.get(i).setProfit(list.get(i).getProductPrice()-list.get(i).getRealPrice());
                        list.get(i).setProductPrice(list.get(i).getRealPrice());
                        cartBeans.add(list.get(i));
                    }
                    editTv.setEnabled(true);
                    cartAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                if (isRefresh){
                    ptr.refreshComplete();
                    isRefresh=false;
                }
            }
        });
    }

    public void deleteCart(String ids){
        addDisposable(ApiRetrofit.getInstance().getApiService().cartDelete(ids), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(CartActivity.this,"刪除成功");
                loadCartList();
            }
        });
    }

    public void updateCart(long id,int num){
        addDisposable(ApiRetrofit.getInstance().getApiService().cartUpdate(id,num), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(CartActivity.this,"更新成功");
            }
        });
    }

    private void toAllotSettle(String ids){
        Intent intent=new Intent(this,OrderAllotSettleActivity.class);
        intent.putExtra("cartIds",ids);
        startActivityForResult(intent,100);
    }

    private void toPurchaseSettle(String ids){
        Intent intent=new Intent(this,OrderPurchaseSettleActivity.class);
        intent.putExtra("cartIds",ids);
        startActivityForResult(intent,101);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Action action){
        if (action.type== Constants.ActionType.CART_DELETE){
            String ids= (String) action.get("ids");
            deleteCart(ids);
        }else if (action.type==Constants.ActionType.CART_UPDATE){
            long id= (long) action.get("id");
            int num= (int) action.get("num");
            updateCart(id,num);
        }else if (action.type== Constants.ActionType.CART_TO_ALLOT){
            String ids= (String) action.get("ids");
            toAllotSettle(ids);
        }else if (action.type== Constants.ActionType.CART_TO_PURCHASE){
            String ids= (String) action.get("ids");
            toPurchaseSettle(ids);
        }else if (action.type==Constants.ActionType.CART_REFRESH){
            if (cartAdapter!=null){
                cartAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case 100:
                case 101:
                    finish();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
