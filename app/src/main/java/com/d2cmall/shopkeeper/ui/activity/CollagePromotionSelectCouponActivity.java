package com.d2cmall.shopkeeper.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.CollagePromotionSelectCouponAdapter;
import com.d2cmall.shopkeeper.adapter.CollagePromotionSelectProductAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.list.BaseVirtualAdapter;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.binder.ScrollEndBinder;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.decoration.SpaceItemDecoration;
import com.d2cmall.shopkeeper.holder.DefaultHolder;
import com.d2cmall.shopkeeper.model.CouponBean;
import com.d2cmall.shopkeeper.model.CouponListBean;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by LWJ on 2019/3/4.
 * Description : CollagePromotionSelectCouponActivity
 * 拼团优惠券绑定优惠
 */

public class CollagePromotionSelectCouponActivity extends BaseActivity implements BaseView, CollagePromotionSelectProductAdapter.SelectedItemListener {
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.ptr)
    PtrStoreHouseFrameLayout ptr;
    @BindView(R.id.img_hint)
    ImageView imgHint;
    @BindView(R.id.btn_reload)
    TextView btnReload;
    @BindView(R.id.empty_hint_layout)
    LinearLayout emptyHintLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rl_layout)
    RelativeLayout rlLayout;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private CollagePromotionSelectCouponAdapter collagePromotionSelectCouponAdapter;
    private int p = 1;
    private boolean hasNext = true;
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private List<CouponBean> couponList;
    private int currentSelectPosition=-1;
    private long couponId;//已选中的优惠券ID
    private boolean needEnd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_collage_promotion_select_coupon;
    }

    @Override
    public void doBusiness() {
        tvName.setText("选择优惠券");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("确定");
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        couponList=new ArrayList<>();
        couponId = getIntent().getLongExtra("couponId", 0);
        initListener();
        virtualLayoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(0, 2);
        recycleView.setRecycledViewPool(recycledViewPool);
        collagePromotionSelectCouponAdapter = new CollagePromotionSelectCouponAdapter(this,couponList,couponId);
        collagePromotionSelectCouponAdapter.setSelectedItemListener(this);
        recycleView.addItemDecoration(new SpaceItemDecoration(this, 10));
        delegateAdapter.addAdapter(collagePromotionSelectCouponAdapter);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
        loadCouponList();
    }

    private void initListener() {
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
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int last = virtualLayoutManager.findLastVisibleItemPosition();
                        if (last > virtualLayoutManager.getItemCount() - 3 && hasNext) {
                            p++;
                            loadCouponList();
                        }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisPosition = virtualLayoutManager.findLastVisibleItemPosition();
                int itemCount = virtualLayoutManager.getItemCount();
                needEnd=virtualLayoutManager.getOffsetToStart()> ScreenUtil.dip2px(50);
                if (lastVisPosition >= itemCount - 3 && !hasNext && needEnd) {
                    if (endAdapter == null) {
                        ScrollEndBinder endBinder = new ScrollEndBinder();
                        endAdapter = new BaseVirtualAdapter<>(endBinder, 100);
                        delegateAdapter.addAdapter(endAdapter);
                    }
                } else {
                    if (endAdapter != null) {
                        delegateAdapter.removeAdapter(endAdapter);
                        endAdapter = null;
                    }
                }
            }
        });
    }


    private void loadCouponList() {
        ArrayMap<String, String> map = Util.buildListParam(p, 20);
        map.put("enable", String.valueOf(1)); //拉取领取结束时间大于当前的优惠券
        addDisposable(ApiRetrofit.getInstance().getApiService().getCouponList(map), new BaseObserver<BaseModel<CouponListBean>>(this) {
            @Override
            public void onSuccess(BaseModel<CouponListBean> couponListBean) {
                ptr.refreshComplete();
                progressBar.setVisibility(View.GONE);
                imgHint.setVisibility(View.GONE);
                btnReload.setVisibility(View.GONE);
                if (p == 1) {
                    couponList.clear();
                    if (couponListBean.getData() != null && couponListBean.getData().getRecords() != null && couponListBean.getData().getRecords().size() == 0) {
                        setEmptyView(Constants.NO_DATA);
                        Util.showToast(CollagePromotionSelectCouponActivity.this,"请先创建优惠券!");
                    }
                }
                if (couponListBean.getData() != null && couponListBean.getData().getRecords() != null && couponListBean.getData().getRecords().size() > 0) {
                    if (couponListBean.getData().getPages() > couponListBean.getData().getCurrent()) {
                        hasNext = true;
                    } else {
                        hasNext = false;
                    }
                    couponList.addAll(couponListBean.getData().getRecords());
                }
                if (collagePromotionSelectCouponAdapter != null) {
                    collagePromotionSelectCouponAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                ptr.refreshComplete();
                progressBar.setVisibility(View.GONE);
                setEmptyView(Constants.NET_DISCONNECT);
            }

        });
    }

    private void setEmptyView(int type) {
        recycleView.setVisibility(View.GONE);
        if (type == Constants.NO_DATA) {
            imgHint.setVisibility(View.VISIBLE);
            imgHint.setImageResource(R.mipmap.icon_empty_default);
            btnReload.setVisibility(View.VISIBLE);
            btnReload.setText("暂无数据");
            btnReload.setBackgroundColor(getResources().getColor(R.color.transparent));
        } else {
            btnReload.setText("重新加载");
            btnReload.setBackgroundResource(R.drawable.sp_line);
            btnReload.setVisibility(View.VISIBLE);
            imgHint.setVisibility(View.VISIBLE);
            imgHint.setImageResource(R.mipmap.ic_no_net);
        }
    }

    private void refresh() {
        currentSelectPosition=-1;
        p = 1;
        imgHint.setVisibility(View.GONE);
        btnReload.setVisibility(View.GONE);
        loadCouponList();
    }

    @OnClick({R.id.btn_reload, R.id.iv_back,R.id.tv_right})
    public void onViewClicked(View view) {
        Intent intent ;
        switch (view.getId()) {
            case R.id.btn_reload:
                refresh();
                break;
            case R.id.iv_back:
                if(currentSelectPosition<0){
                    Util.showToast(this,"请选择一张优惠券");
                    return;
                }
                intent=new Intent();
                intent.putExtra("couponId",couponList.get(currentSelectPosition).getId());
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.tv_right:
                if(currentSelectPosition<0){
                    Util.showToast(this,"请选择一张优惠券");
                    return;
                }
                intent = new Intent();
                intent.putExtra("couponId",couponList.get(currentSelectPosition).getId());
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(currentSelectPosition<0){
                finish();
                return true;
            }
            Intent intent = new Intent();
            intent.putExtra("couponId",couponList.get(currentSelectPosition).getId());
            setResult(RESULT_OK,intent);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void requestStart() {

    }

    @Override
    public void requestFinish() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void selectedItem(int position) {
        currentSelectPosition=position;
    }
}
