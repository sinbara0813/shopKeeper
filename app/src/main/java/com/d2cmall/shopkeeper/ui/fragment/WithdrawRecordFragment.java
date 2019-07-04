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
import com.d2cmall.shopkeeper.adapter.WithdrawRecordAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseFragment;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.decoration.DividerDecoration;
import com.d2cmall.shopkeeper.decoration.SpaceItemDecoration;
import com.d2cmall.shopkeeper.model.WithdrawRecordListBean;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by LWJ on 2019/2/26.
 * Description : WithdrawRecordFragment
 * 提现记录
 */

public class WithdrawRecordFragment extends BaseFragment implements BaseView {
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
    private String status;//全部 申请中 已完成  已关闭
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private WithdrawRecordAdapter withdrawRecordAdapter;
    private List<WithdrawRecordListBean.RecordsBean> withdrawRecordList;

    public static WithdrawRecordFragment newInstance(String status) {
        WithdrawRecordFragment buyerSaleFragment = new WithdrawRecordFragment();
        Bundle args = new Bundle();
        args.putString("status", status);
        buyerSaleFragment.setArguments(args);
        return buyerSaleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getString("status");
        }
    }

    @Override
    public View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void doBusiness() {
        init();
    }

    private void init() {
        withdrawRecordList=new ArrayList<>();
        virtualLayoutManager = new VirtualLayoutManager(getActivity());
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(0,2);
        recycleView.setRecycledViewPool(recycledViewPool);
        withdrawRecordAdapter = new WithdrawRecordAdapter(getActivity(), withdrawRecordList);
        delegateAdapter.addAdapter(withdrawRecordAdapter);
        recycleView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.bg_color), ScreenUtil.dip2px(10)));
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
        if (lastPosition >= 0) {
            virtualLayoutManager.scrollToPositionWithOffset(lastPosition, lastOffer);
        }
        initListener();
        hasSetAdapter = true;
        loadWithDrawRecord();
    }

    @Override
    public void prepareView() {

    }

    private void loadWithDrawRecord() {
        progressBar.setVisibility(View.VISIBLE);
        ArrayMap<String, String> arrayMap = Util.buildListParam(p, 20);
        if(!StringUtil.isEmpty(status)){
            arrayMap.put("status",status);
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().getWithdrawRecordList(arrayMap), new BaseObserver<BaseModel<WithdrawRecordListBean>>(this) {
            @Override
            public void onSuccess(BaseModel<WithdrawRecordListBean> withdrawRecordListBean) {
                ptr.refreshComplete();
                btnReload.setVisibility(View.GONE);
                imgHint.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                recycleView.setVisibility(View.VISIBLE);
                if (p == 1) {
                    withdrawRecordList.clear();
                }
                if (withdrawRecordListBean != null && withdrawRecordListBean.getData() != null && withdrawRecordListBean.getData().getRecords() != null && withdrawRecordListBean.getData().getRecords().size() > 0) {
                    withdrawRecordList.addAll(withdrawRecordListBean.getData().getRecords());
                } else {
                    if (p == 1) {
                        setEmptyView(Constants.NO_DATA);
                    }
                }
                if (withdrawRecordAdapter != null) {
                    withdrawRecordAdapter.notifyDataSetChanged();
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
        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                p=1;
                refresh();
            }
        });
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisPosition = virtualLayoutManager.findLastVisibleItemPosition();
                int itemCount = virtualLayoutManager.getItemCount();
                if (lastVisPosition > delegateAdapter.getItemCount() - 3 && hasNext) {
                    p++;
                    loadWithDrawRecord();
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
        loadWithDrawRecord();
    }



    @OnClick(R.id.btn_reload)
    public void onViewClicked() {
        p=1;
        refresh();
    }

    @Override
    public void requestStart() {

    }

    @Override
    public void requestFinish() {

    }
}
