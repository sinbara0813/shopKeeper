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
import com.d2cmall.shopkeeper.adapter.CollagePromotionAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseFragment;
import com.d2cmall.shopkeeper.base.list.BaseVirtualAdapter;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.binder.ScrollEndBinder;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.decoration.DividerDecoration;
import com.d2cmall.shopkeeper.decoration.SpaceItemDecoration;
import com.d2cmall.shopkeeper.holder.DefaultHolder;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.model.ProductListBean;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
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
 * Created by LWJ on 2019/3/1.
 * Description : CollagePromotionFragment
 * 拼团活动Fragment
 */

public class CollagePromotionFragment extends BaseFragment implements BaseView {
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
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private CollagePromotionAdapter collagePromotionAdapter;
    private List<ProductBean> productBeanList;
    private boolean needEnd;
    private boolean virtual=false;

    public static CollagePromotionFragment newInstance(int type,boolean isVirtual) {
        CollagePromotionFragment buyerSaleFragment = new CollagePromotionFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putBoolean("virtual",isVirtual);
        buyerSaleFragment.setArguments(args);
        return buyerSaleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
            virtual=getArguments().getBoolean("virtual");
        }
        productBeanList=new ArrayList<>();
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
        if(productBeanList.size()==0){
            loadProducts();
        }
    }

    public void initView() {
        virtualLayoutManager = new VirtualLayoutManager(getActivity());
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(0,2);
        recycleView.setRecycledViewPool(recycledViewPool);
        collagePromotionAdapter = new CollagePromotionAdapter(mContext,getManager(),productBeanList,type);
        delegateAdapter.addAdapter(collagePromotionAdapter);
        recycleView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.bg_color), ScreenUtil.dip2px(10)));
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.addItemDecoration(new SpaceItemDecoration(mContext,10));
        recycleView.setAdapter(delegateAdapter);
        if (lastPosition >= 0) {
            virtualLayoutManager.scrollToPositionWithOffset(lastPosition, lastOffer);
        }
        initListener();
        hasSetAdapter = true;
    }

    private void initListener(){
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
                            loadProducts();
                        }
                }
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
    public void refresh() {
        p=1;
        imgHint.setVisibility(View.GONE);
        btnReload.setVisibility(View.GONE);
        recycleView.setVisibility(View.VISIBLE);
        loadProducts();
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);
        ArrayMap<String, String> map = Util.buildListParam(p, 20);
        map.put("crowd",String.valueOf(type));
        map.put("virtual",virtual?"1":"0");
        addDisposable(ApiRetrofit.getInstance().getApiService().getProductList(map), new BaseObserver<BaseModel<ProductListBean>>(this) {
            @Override
            public void onSuccess(BaseModel<ProductListBean> o) {
                ptr.refreshComplete();
                progressBar.setVisibility(View.GONE);
                imgHint.setVisibility(View.GONE);
                btnReload.setVisibility(View.GONE);
                if (p == 1) {
                    productBeanList.clear();
                    if (o.getData() != null && o.getData().getRecords() != null && o.getData().getRecords().size() == 0) {
                        setEmptyView(Constants.NO_DATA);
                    }
                }
                if (o.getData() != null && o.getData().getRecords() != null && o.getData().getRecords().size() > 0) {
                    if (o.getData().getPages() > o.getData().getCurrent()) {
                        hasNext = true;
                    } else {
                        hasNext = false;
                    }
                    productBeanList.addAll(o.getData().getRecords());
                }
                if (collagePromotionAdapter != null) {
                    collagePromotionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                ptr.refreshComplete();
                progressBar.setVisibility(View.GONE);
                setEmptyView(Constants.NET_DISCONNECT);
                super.onError(errorCode, msg);
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

    @OnClick(R.id.btn_reload)
    public void onViewClicked() {
        refresh();
    }

    @Override
    public void requestStart() {

    }

    @Override
    public void requestFinish() {

    }
}
