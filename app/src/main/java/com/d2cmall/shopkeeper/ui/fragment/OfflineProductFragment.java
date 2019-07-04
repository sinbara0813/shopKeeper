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
import com.d2cmall.shopkeeper.adapter.OfflineProductAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseFragment;
import com.d2cmall.shopkeeper.base.list.BaseVirtualAdapter;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.binder.ScrollEndBinder;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.decoration.DividerDecoration;
import com.d2cmall.shopkeeper.holder.DefaultHolder;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.model.ProductListBean;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 作者:Created by sinbara on 2019/5/15.
 * 邮箱:hrb940258169@163.com
 */

public class OfflineProductFragment extends BaseFragment implements BaseView {

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

    private int lastPosition = -1;
    private int lastOffer;
    private int p = 1;
    private boolean hasNext; //是否有下一页数据
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private OfflineProductAdapter orderListAdapter;
    private List<ProductBean> list;
    private boolean isRefresh;
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private boolean needEnd;
    private String id;

    public static OfflineProductFragment newInstance(String id) {
        OfflineProductFragment fragment = new OfflineProductFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            id=getArguments().getString("id");
        }
        list=new ArrayList<>();
    }

    @Override
    public void prepareView() {
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
        virtualLayoutManager = new VirtualLayoutManager(getActivity());
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        orderListAdapter = new OfflineProductAdapter(getActivity(),getManager(), list);
        delegateAdapter.addAdapter(orderListAdapter);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
        recycleView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.line), true));
        recycleView.setBackgroundColor(getResources().getColor(R.color.color_white));
        recycleView.setPadding(ScreenUtil.dip2px(14),0,0,0);
        if (lastPosition >= 0) {
            virtualLayoutManager.scrollToPositionWithOffset(lastPosition, lastOffer);
        }
        initListener();
    }

    private void initListener(){
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                needEnd=virtualLayoutManager.getOffsetToStart()> ScreenUtil.dip2px(50);
                int lastVisPosition=virtualLayoutManager.findLastVisibleItemPosition();
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
                switch (newState){
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int lastVisPosition=virtualLayoutManager.findLastVisibleItemPosition();
                        if (lastVisPosition > delegateAdapter.getItemCount() - 3 && hasNext) {
                            p++;
                            loadProductList();
                        }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }}
        );
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

    private void resetView(){
        recycleView.setVisibility(View.VISIBLE);
        imgHint.setVisibility(View.GONE);
        btnReload.setVisibility(View.GONE);
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
    private void refresh() {
        p=1;
        isRefresh=true;
        resetView();
        loadProductList();
    }

    @OnClick(R.id.btn_reload)
    public void onViewClicked() {
        p=1;
        resetView();
        loadProductList();
    }

    @Override
    public void doBusiness() {
        if (list.size()==0){
            loadProductList();
        }
    }

    private void loadProductList() {
        ArrayMap<String,String> map= Util.buildListParam(p,20);
        if (!StringUtil.isEmpty(id)){
            map.put("categoryId",id);
        }
        map.put("status",String.valueOf(1));
        map.put("virtual",String.valueOf(0));
        addDisposable(ApiRetrofit.getInstance().getApiService().getProductList(map), new BaseObserver<BaseModel<ProductListBean>>(this) {

            @Override
            public void onSuccess(BaseModel<ProductListBean> customerBean) {
                if (p == 1) {
                    list.clear();
                }
                if (customerBean != null && customerBean.getData() != null && customerBean.getData().getRecords() != null && customerBean.getData().getRecords().size() > 0) {
                    hasNext=customerBean.getData().getCurrent()<customerBean.getData().getPages()?true:false;
                    list.addAll(customerBean.getData().getRecords());
                } else {
                    if (p == 1) {
                        setEmptyView(Constants.NO_DATA);
                    }
                }
                orderListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                setEmptyView(Constants.NET_DISCONNECT);
            }

        });
    }

    @Override
    public void requestStart() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void requestFinish() {
        progressBar.setVisibility(View.GONE);
        if (isRefresh){
            ptr.refreshComplete();
            isRefresh=false;
        }
    }
}
