package com.d2cmall.shopkeeper.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.CouponAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseFragment;
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
import com.d2cmall.shopkeeper.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 作者:Created by sinbara on 2019/2/25.
 * 邮箱:hrb940258169@163.com
 */

public class CouponFragment extends BaseFragment implements BaseView {
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
    @BindView(R.id.rl_layout)
    RelativeLayout rlLayout;
    Unbinder unbinder;

    private int lastPosition = -1;
    private int lastOffer;
    private int p = 1;
    private boolean hasNext; //是否有下一页数据
    private boolean hasSetAdapter; //是否设置过设配器
    private int type;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private CouponAdapter couponAdapter;
    private List<CouponBean> couponList;
    private boolean needEnd;
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志

    public static CouponFragment newInstance(int type) {
        CouponFragment couponFragment = new CouponFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        couponFragment.setArguments(bundle);
        return couponFragment;
    }

    @Override
    public View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
        couponList=new ArrayList<>();
    }

    @Override
    public void prepareView() {
        virtualLayoutManager = new VirtualLayoutManager(mContext);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(0, 2);
        recycleView.setRecycledViewPool(recycledViewPool);
        couponAdapter = new CouponAdapter(mContext,couponList);
        delegateAdapter.addAdapter(couponAdapter);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.addItemDecoration(new SpaceItemDecoration(mContext, 10));
        recycleView.setAdapter(delegateAdapter);
        if (lastPosition >= 0) {
            virtualLayoutManager.scrollToPositionWithOffset(lastPosition, lastOffer);
        }
        initListener();
        hasSetAdapter = true;
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
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisPosition=virtualLayoutManager.findLastVisibleItemPosition();
                needEnd=virtualLayoutManager.getOffsetToStart()> ScreenUtil.dip2px(50);
                if (lastVisPosition>delegateAdapter.getItemCount()-3&&!hasNext&&needEnd){
                    if (endAdapter == null) {
                        ScrollEndBinder endBinder = new ScrollEndBinder();
                        endAdapter = new BaseVirtualAdapter<>(endBinder, 100);
                        delegateAdapter.addAdapter(endAdapter);
                    }
                }else {
                    if (endAdapter != null) {
                        delegateAdapter.removeAdapter(endAdapter);
                        endAdapter = null;
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                getLastLocation();
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int last = virtualLayoutManager.findLastVisibleItemPosition();
                        if (last > delegateAdapter.getItemCount() - 3 && hasNext) {
                            p++;
                            loadCouponList();
                        }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }


    private void getLastLocation() {
        View topView = virtualLayoutManager.getChildAt(0);
        if (topView != null) {
            //获取与该view的顶部的偏移量
            lastOffer = topView.getTop();
            //得到该View的数组位置
            lastPosition = virtualLayoutManager.getPosition(topView);
        }
    }

    public void refresh() {
        p = 1;
        recycleView.setVisibility(View.VISIBLE);
        imgHint.setVisibility(View.GONE);
        btnReload.setVisibility(View.GONE);
        loadCouponList();
    }

    @Override
    public void doBusiness() {
        if (couponList.size() == 0) {
            loadCouponList();
        }
    }

    private void loadCouponList() {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("p", String.valueOf(p));
        map.put("ps", String.valueOf(20));
        map.put("status",String.valueOf(type));
        rlLayout.setBackgroundColor(getResources().getColor(R.color.bg_color));
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
                if (couponAdapter != null) {
                    couponAdapter.notifyDataSetChanged();
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
        rlLayout.setBackgroundColor(getResources().getColor(R.color.color_white));
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

    @Override
    public void requestStart() {

    }

    @Override
    public void requestFinish() {

    }


    @OnClick(R.id.btn_reload)
    public void onViewClicked() {
        refresh();
    }
}
