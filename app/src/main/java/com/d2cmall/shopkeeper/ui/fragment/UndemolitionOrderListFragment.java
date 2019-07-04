package com.d2cmall.shopkeeper.ui.fragment;

import android.graphics.Color;
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
import com.d2cmall.shopkeeper.adapter.UndemolitionOrderListAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseFragment;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.model.OrderListBean;
import com.d2cmall.shopkeeper.model.UndemolitionListBean;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by LWJ on 2019/2/21.
 * Description : OrderListFragment
 * 未拆单的订单列表    全部和未付款
 */

public class UndemolitionOrderListFragment extends BaseFragment implements BaseView{
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
    Unbinder unbinder;

    private int lastPosition = -1;
    private int lastOffer;
    private int p = 1;
    private boolean hasNext; //是否有下一页数据
    private boolean hasSetAdapter; //是否设置过设配器
    private int type;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private UndemolitionOrderListAdapter orderListAdapter;
    private List<OrderListBean> mOrderList;
    private int orderType;

    public static UndemolitionOrderListFragment newInstance(int type, int orderType) {
        UndemolitionOrderListFragment undemolitionOrderListFragment = new UndemolitionOrderListFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        undemolitionOrderListFragment.setArguments(args);
        return undemolitionOrderListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
            orderType = getArguments().getInt("orderType");
        }
    }

    @Override
    public View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void prepareView() {
        initView();

    }

    @Override
    public void doBusiness() {
        if(mOrderList!=null && mOrderList.size()==0){
            loadOrderList();
        }
    }

    public void initView() {
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
        mOrderList = new ArrayList<OrderListBean>();
        virtualLayoutManager = new VirtualLayoutManager(getActivity());
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(0, 2);
        recycleView.setRecycledViewPool(recycledViewPool);
        orderListAdapter = new UndemolitionOrderListAdapter(getActivity(), type, orderType,mOrderList);
        delegateAdapter.addAdapter(orderListAdapter);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
        if (lastPosition >= 0) {
            virtualLayoutManager.scrollToPositionWithOffset(lastPosition, lastOffer);
        }
        initListener();
        hasSetAdapter = true;
    }

    private void loadOrderList() {
        progressBar.setVisibility(View.VISIBLE);
        ArrayMap<String, String> map = Util.buildListParam(p, 20);
        if(orderType==0){
            map.put("type","NORMAL");
        }else{
            map.put("type","CROWD");
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().getUndemolitionOrderList(map), new BaseObserver<BaseModel<UndemolitionListBean>>(this) {
            @Override
            public void onSuccess(BaseModel<UndemolitionListBean> orderList) {
                ptr.refreshComplete();
                btnReload.setVisibility(View.GONE);
                imgHint.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                rlLayout.setBackgroundColor(Color.parseColor("#F1F3F8"));
                recycleView.setVisibility(View.VISIBLE);
                if (p == 1) {
                    mOrderList.clear();
                }
                if (orderList != null && orderList.getData() != null && orderList.getData().getRecords() != null && orderList.getData().getRecords().size() > 0) {
                    mOrderList.addAll(orderList.getData().getRecords());
                } else {
                    if (p == 1) {
                        setEmptyView(Constants.NO_DATA);
                    }
                }
                if (orderListAdapter != null) {
                    orderListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                progressBar.setVisibility(View.GONE);
                ptr.refreshComplete();
                setEmptyView(Constants.NET_DISCONNECT);
            }
        });
    }

    private void initListener() {
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisPosition = virtualLayoutManager.findLastVisibleItemPosition();
                int itemCount = virtualLayoutManager.getItemCount();
                if (lastVisPosition > delegateAdapter.getItemCount() - 3 && hasNext) {
                    p++;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                getLastLocation();
                super.onScrollStateChanged(recyclerView, newState);
            }}
        );
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

    private void refresh() {
        p = 1;
        imgHint.setVisibility(View.GONE);
        btnReload.setVisibility(View.GONE);
        recycleView.setVisibility(View.VISIBLE);
        loadOrderList();
    }

    @OnClick(R.id.btn_reload)
    public void onViewClicked() {
        rlLayout.setBackgroundColor(Color.parseColor("#F1F3F8"));
        refresh();
    }

    @Override
    public void requestStart() {

    }

    @Override
    public void requestFinish() {

    }
}
