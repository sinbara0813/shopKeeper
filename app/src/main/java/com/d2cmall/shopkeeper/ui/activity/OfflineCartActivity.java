package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.TabPagerAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.model.CartBean;
import com.d2cmall.shopkeeper.model.CategoryBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.fragment.OfflineProductFragment;
import com.d2cmall.shopkeeper.ui.view.CartListPop;
import com.d2cmall.shopkeeper.ui.view.badge.BadgeFactory;
import com.d2cmall.shopkeeper.ui.view.badge.BadgeView;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.Util;
import com.flyco.tablayout.SlidingTabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/5/7.
 * 邮箱:hrb940258169@163.com
 */

public class OfflineCartActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.cart_iv)
    ImageView cartIv;
    @BindView(R.id.settle_tv)
    TextView settleTv;
    @BindView(R.id.price_tv)
    TextView priceTv;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private ArrayList fragments = new ArrayList();
    private ArrayList titles = new ArrayList();
    private TabPagerAdapter tabPagerAdapter;

    private BadgeView badgeView;
    private int cartCount;
    private double totalPrice;
    private long shopId;
    private String cartIds;
    private CartListPop cartListPop;

    @Override
    public int getLayoutId() {
        return R.layout.activity_offline_cart;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        UserBean userBean=Session.getInstance().getUser();
        if (userBean!=null){
            shopId=userBean.getShopId();
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().getCategoryList(Util.buildListParam(1, 40)), new BaseObserver<BaseModel<List<CategoryBean>>>() {
            @Override
            public void onSuccess(BaseModel<List<CategoryBean>> o) {
                if (o.getData().size() == 0) {
                    return;
                }
                int size = o.getData().size();
                for (int i = 0; i < size; i++) {
                    int num = o.getData().get(i).getChildren().size();
                    if (num==0){
                        titles.add(o.getData().get(i).getName());
                        fragments.add(OfflineProductFragment.newInstance(String.valueOf(o.getData().get(i).getId())));
                    }else {
                        for (int j = 0; j < num; j++) {
                            titles.add(o.getData().get(i).getChildren().get(j).getName());
                            fragments.add(OfflineProductFragment.newInstance(String.valueOf(o.getData().get(i).getChildren().get(j).getId())));
                        }
                    }
                }
                tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), fragments, titles);
                viewPager.setAdapter(tabPagerAdapter);
                tabLayout.setViewPager(viewPager);
                tabPagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int errorCode, String msg) {
            }
        });
        getCartList();
    }

    @OnClick({R.id.back_iv, R.id.cart_iv, R.id.settle_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.cart_iv:
                showCartPop(view);
                break;
            case R.id.settle_tv:
                if (cartListPop!=null&&cartListPop.isShow()){
                    cartListPop.dismiss();
                }
                settle();
                break;
        }
    }

    private void settle() {
        Intent intent = new Intent(this, OfflineSettleActivity.class);
        intent.putExtra("cartIds",cartIds);
        startActivityForResult(intent,100);
    }

    private void showCartPop(View view) {
        if (cartListPop!=null){
            if (cartListPop.isShow()){
                cartListPop.dismiss();
                return;
            }
        }
        if (cartListPop!=null){
            cartListPop=null;
        }
        cartListPop = new CartListPop(this);
        cartListPop.show(view);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Action action) {
        if (action.type == Constants.ActionType.CART_OFFLINE_ADD) {
            int type = (int) action.get("type");
            switch (type) {
                case 0://加入清单
                    long skuId = (long) action.get("skuId");
                    int num = (int) action.get("num");
                    addCart(skuId, num);
                    break;
            }
        }else if (action.type==Constants.ActionType.CART_REFRESH){
            getCartList();
        }
    }

    private void addCart(long skuId, int num) {
        addDisposable(ApiRetrofit.getInstance().getApiService().cartInsert(skuId, num), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(OfflineCartActivity.this, "加入购物车成功");
                getCartList();
            }
        });
    }

    private void getCartList(){
        ArrayMap<String,String> param=Util.buildListParam(1,20);
        param.put("shopId",String.valueOf(shopId));
        addDisposable(ApiRetrofit.getInstance().getApiService().getCartList(param),new BaseObserver<BaseModel<List<CartBean>>>() {
            @Override
            public void onSuccess(BaseModel<List<CartBean>> o) {
                if (o.getData()!=null){
                    cartCount=o.getData().size();
                    totalPrice=0;
                    StringBuilder builder=new StringBuilder();
                    for (int i=0;i<cartCount;i++){
                        totalPrice+=(o.getData().get(i).getProductPrice()*o.getData().get(i).getQuantity());
                        builder.append(o.getData().get(i).getId());
                        if (i!=cartCount-1){
                            builder.append(",");
                        }
                    }
                    cartIds=builder.toString();
                    updateView();
                }
            }
        });
    }

    private void updateView(){
        if (cartCount>0){
            cartIv.setImageResource(R.mipmap.icon_cart);
            if (badgeView!=null){
                badgeView.unbind();
                badgeView=null;
            }
            if (cartCount<=9){
                badgeView = BadgeFactory.createCircle1(this).setBadgeCount(cartCount).bind(cartIv);
            }else {
                badgeView = BadgeFactory.createOval1(this).setBadgeCount(cartCount).bind(cartIv);
            }
            badgeView.setBadgeBackground(getResources().getColor(R.color.normal_blue));
            priceTv.setText(getPriceStr(totalPrice));
            settleTv.setBackgroundResource(R.drawable.sp_round2_bg_blue);
            settleTv.setEnabled(true);
        }else {
            cartIv.setImageResource(R.mipmap.icon_cart_hui);
            if (badgeView!=null){
                badgeView.unbind();
                badgeView=null;
            }
            priceTv.setText("");
            settleTv.setBackgroundResource(R.drawable.sp_round2_bg_cbcbcb);
            settleTv.setEnabled(false);
        }
    }

    private SpannableString getPriceStr(double price){
        StringBuilder builder=new StringBuilder();
        builder.append("总计").append("¥").append(Util.getNumberFormat(price));
        SpannableString sb=new SpannableString(builder.toString());
        ForegroundColorSpan colorSpan=new ForegroundColorSpan(getResources().getColor(R.color.color_black3));
        sb.setSpan(colorSpan,0,2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan sizeSpan=new RelativeSizeSpan(0.76f);
        sb.setSpan(sizeSpan,0,3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    protected void onDestroy() {
        if (badgeView != null) {
            badgeView.unbind();
            badgeView=null;
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK){
            finish();
        }
    }
}
