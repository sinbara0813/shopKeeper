package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.OfflineCartProductAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.decoration.DividerDecoration;
import com.d2cmall.shopkeeper.model.CartBean;
import com.d2cmall.shopkeeper.model.EmptyBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者:Created by sinbara on 2019/5/6.
 * 邮箱:hrb940258169@163.com
 */

public class CartListPop implements TransparentPop.InvokeListener {

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;

    private Context mContext;
    private TransparentPop mPop;
    private View cartListView;
    private VirtualLayoutManager layoutManager;
    private DelegateAdapter delegateAdapter;
    private OfflineCartProductAdapter offlineCartProductAdapter;
    private List<CartBean> list=new ArrayList<>();
    private long shopId;

    public CartListPop(Context context) {
        this.mContext = context;
        cartListView = LayoutInflater.from(context).inflate(R.layout.layout_cart_pop, new CoordinatorLayout(context), false);
        ButterKnife.bind(this, cartListView);
        UserBean userBean= Session.getInstance().getUserFromFile(context);
        if (userBean!=null){
            shopId=userBean.getShopId();
        }
        int height = ScreenUtil.screenHeight - ScreenUtil.dip2px(71) - ScreenUtil.getStatusBarHeight(context);
        mPop = new TransparentPop(context, -1, height, true, this);
        mPop.setFocusable(false);
        Animation inAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        layoutManager = new VirtualLayoutManager(context);
        delegateAdapter = new DelegateAdapter(layoutManager, true);
        offlineCartProductAdapter = new OfflineCartProductAdapter(context,list);
        delegateAdapter.addAdapter(offlineCartProductAdapter);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(delegateAdapter);
        recycleView.addItemDecoration(new DividerDecoration(context.getResources().getColor(R.color.line),false));
        loadCartList();
    }

    private void loadCartList(){
        ArrayMap<String,String> param= Util.buildListParam(1,20);
        param.put("shopId",String.valueOf(shopId));
        ApiRetrofit.getInstance().getApiService().getCartList(param).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseModel<List<CartBean>>>() {
                    @Override
                    public void onSuccess(BaseModel<List<CartBean>> o) {
                        if (o.getData()!=null&&o.getData().size()>0){
                            list.addAll(o.getData());
                            offlineCartProductAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void show(View view) {
        mPop.show(view, Gravity.BOTTOM, -1, ScreenUtil.dip2px(71), true);
    }

    public void dismiss() {
        mPop.dismiss(true);
    }

    public boolean isShow(){
        return mPop.isShowing();
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.BOTTOM);
        v.addView(cartListView);
    }

    @Override
    public void releaseView(LinearLayout v) {
    }

    @OnClick(R.id.clear_tv)
    public void onViewClicked() {
        StringBuilder builder=new StringBuilder();
        int size=list.size();
        if (size==0){
            Util.showToast(mContext,"没有商品");
            return;
        }
        for (int i=0;i<size;i++){
            builder.append(list.get(i).getId());
            if (i!=size-1){
                builder.append(",");
            }
        }
        ApiRetrofit.getInstance().getApiService().cartDelete(builder.toString()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseModel<EmptyBean>>() {
                    @Override
                    public void onSuccess(BaseModel<EmptyBean> o) {
                        list.clear();
                        offlineCartProductAdapter.notifyDataSetChanged();
                        EventBus.getDefault().post(new Action(Constants.ActionType.CART_REFRESH));
                    }
                });
    }
}
