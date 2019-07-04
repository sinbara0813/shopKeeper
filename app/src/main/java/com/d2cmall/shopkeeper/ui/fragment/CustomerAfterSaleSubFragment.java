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
import com.d2cmall.shopkeeper.adapter.CustomerAfterSaleSubAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseFragment;
import com.d2cmall.shopkeeper.base.list.BaseVirtualAdapter;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.binder.ScrollEndBinder;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.decoration.DividerDecoration;
import com.d2cmall.shopkeeper.holder.DefaultHolder;
import com.d2cmall.shopkeeper.listener.ExpandListener;
import com.d2cmall.shopkeeper.model.OrderDetialBean;
import com.d2cmall.shopkeeper.model.OrderListBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by LWJ on 2019/4/26.
 * Description : OrderListFragment
 * 退货退款,退款二级(客户发起,B端发起)
 */

public class CustomerAfterSaleSubFragment extends BaseFragment implements BaseView, ExpandListener, CustomerAfterSaleSubAdapter.RefreshFragmentListener {
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.ptr)
    com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout ptr;
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

    private int lastPosition = -1;
    private int lastOffer;
    private int p = 1;
    private boolean hasNext; //是否有下一页数据
    private String status;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private CustomerAfterSaleSubAdapter customerAfterSaleSubAdapter;
    private List<OrderDetialBean.OrderItemListBean> mOrderList;
    private int orderType;
    private boolean isRefresh;
    private boolean canRefresh = true;
    private boolean isExpand;
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private boolean needEnd;

    public static CustomerAfterSaleSubFragment newInstance(String status, int orderType) {//orderType区分拼团和普通
        CustomerAfterSaleSubFragment orderListFragment = new CustomerAfterSaleSubFragment();
        Bundle args = new Bundle();
        args.putString("status", status);
        args.putInt("orderType", orderType);
        orderListFragment.setArguments(args);
        return orderListFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString("status");
            orderType = getArguments().getInt("orderType");
        }
        mOrderList = new ArrayList<>();
    }

    @Override
    public View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void prepareView() {
        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header)&&canRefresh;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });
        virtualLayoutManager = new VirtualLayoutManager(getActivity());
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(0, 2);
        recycleView.setRecycledViewPool(recycledViewPool);
        recycleView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.bg_color), ScreenUtil.dip2px(10)));
        customerAfterSaleSubAdapter = new CustomerAfterSaleSubAdapter(getActivity(), getManager(),1, mOrderList);
        customerAfterSaleSubAdapter.setRefreshFragmentListener(this);
        delegateAdapter.addAdapter(customerAfterSaleSubAdapter);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
        if (lastPosition >= 0) {
            virtualLayoutManager.scrollToPositionWithOffset(lastPosition, lastOffer);
        }
        initListener();
    }

    @Override
    public void doBusiness() {
        if (mOrderList.size() == 0) {
            loadOrderList();
            loadShopBean();
        }
        if (isRefresh){
            loadOrderList();
        }
    }

    private void loadShopBean(){
        UserBean userBean= Session.getInstance().getUserFromFile(mContext);
        if (userBean!=null&&userBean.getShopId()>0){
            addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(userBean.getShopId()), new BaseObserver<BaseModel<ShopBean>>() {
                @Override
                public void onSuccess(BaseModel<ShopBean> o) {
                    if (customerAfterSaleSubAdapter!=null){
                        customerAfterSaleSubAdapter.setShopBean(o.getData());
                    }
                }
            });

        }
    }

    private void loadOrderList() {
        ArrayMap<String, String> map = Util.buildListParam(p, 20);
        if (!StringUtil.isEmpty(status)) {
            map.put("status", status);
        }
        if (orderType == 0) {
            map.put("afterType", "2");
        } else {
            map.put("afterType", "1");
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().getOrderList(map), new BaseObserver<BaseModel<OrderListBean>>(this) {
            @Override
            public void onSuccess(BaseModel<OrderListBean> orderList) {
                if (p == 1) {
                    mOrderList.clear();
                }
                if (orderList.getData().getRecords() != null && orderList.getData().getRecords().size() > 0) {
                    hasNext = orderList.getData().getCurrent() < orderList.getData().getPages() ? true : false;
                    mOrderList.addAll(orderList.getData().getRecords());
                } else {
                    if (p == 1) {
                        setEmptyView(Constants.NO_DATA);
                    }
                }
                customerAfterSaleSubAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                setEmptyView(Constants.NET_DISCONNECT);
            }
        });
    }

    private void initListener() {
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                getLastLocation();
                switch (newState){
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int lastVisPosition = virtualLayoutManager.findLastVisibleItemPosition();
                        if (lastVisPosition > delegateAdapter.getItemCount() - 3 && hasNext) {
                            p++;
                            loadOrderList();
                        }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                needEnd=virtualLayoutManager.getOffsetToStart()> ScreenUtil.dip2px(50);
                isExpand = virtualLayoutManager.getOffsetToStart() == 0;
                int lastVisPosition = virtualLayoutManager.findLastVisibleItemPosition();
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

    private void resetView() {
        recycleView.setVisibility(View.VISIBLE);
        imgHint.setVisibility(View.GONE);
        btnReload.setVisibility(View.GONE);
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

    public void refresh() {
        p = 1;
        isRefresh = true;
        resetView();
        loadOrderList();
    }

    public void preRefresh(){
        p = 1;
        isRefresh = true;
        resetView();
    }

    @OnClick(R.id.btn_reload)
    public void onViewClicked() {
        p = 1;
        resetView();
        loadOrderList();
    }

    @Override
    public void requestStart() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void requestFinish() {
        progressBar.setVisibility(View.GONE);
        if (isRefresh) {
            ptr.refreshComplete();
            isRefresh = false;
        }
    }

    @Override
    public boolean isExpand() {
        return isExpand;
    }

    @Override
    public void canRefresh(boolean is) {
        canRefresh = is;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(customerAfterSaleSubAdapter!=null){
            customerAfterSaleSubAdapter.setRefreshFragmentListener(null);
        }
    }

    @Override
    public void refreshFreagment() {
        refresh();
        if (!StringUtil.isEmpty(status)){
            EventBus.getDefault().post(new Action(Constants.ActionType.AFTER_SALE_REFRESH));
        }
    }
}
