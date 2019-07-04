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
import com.d2cmall.shopkeeper.adapter.AllotPurchaseAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.list.BaseVirtualAdapter;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.binder.ScrollEndBinder;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.decoration.DividerDecoration;
import com.d2cmall.shopkeeper.holder.DefaultHolder;
import com.d2cmall.shopkeeper.model.AllotItemListBean;
import com.d2cmall.shopkeeper.model.AllotListBean;
import com.d2cmall.shopkeeper.model.PurchaseItemListBean;
import com.d2cmall.shopkeeper.ui.view.headerViewPager.HeaderViewPagerFragment;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/4/25.
 * Description : 进货管理 免费拿&买断商品列表
 */

public class PurchaseProductFragment extends HeaderViewPagerFragment implements BaseView{
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
    private int type;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private AllotPurchaseAdapter allotPurchaseAdapter;
    private List<Object> list;
    private boolean isRefresh;
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private boolean needEnd;

    public static PurchaseProductFragment newInstance(int type) {
        PurchaseProductFragment buyerSaleFragment = new PurchaseProductFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        buyerSaleFragment.setArguments(args);
        return buyerSaleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
        list=new ArrayList<>();
    }

    @Override
    public View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void doBusiness() {
        if (list.size()==0){
            loadData();
        }
    }

    private void loadData(){
        addDisposable(ApiRetrofit.getInstance().getApiService().getPurchItemList(buildParam(p, 20)), new BaseObserver<BaseModel<PurchaseItemListBean>>() {
            @Override
            public void onSuccess(BaseModel<PurchaseItemListBean> purchaseItemListBean) {
                if (p==1){
                    list.clear();
                }
                if (purchaseItemListBean.getData()!=null&&purchaseItemListBean.getData().getRecords()!=null&&purchaseItemListBean.getData().getRecords().size()>0){
                    hasNext=purchaseItemListBean.getData().getCurrent()<purchaseItemListBean.getData().getPages()?true:false;
                    list.addAll(purchaseItemListBean.getData().getRecords());
                }else {
                    if (p==1){
                        setEmptyView(Constants.NO_DATA);
                    }
                }
                allotPurchaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int errorCode, String msg) {
                if (errorCode==-401){
                    if (getActivity()!=null)getActivity().finish();
                }
                super.onError(errorCode, msg);
                setEmptyView(Constants.NET_DISCONNECT);
            }
        });
    }

    private ArrayMap<String,String> buildParam(int p,int ps){
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("p", String.valueOf(p));
        map.put("ps", String.valueOf(ps));
        map.put("status","RECEIVE");
        return map;
    }

    @Override
    public void prepareView() {
        ptr.setEnabled(false);
        virtualLayoutManager = new VirtualLayoutManager(getActivity());
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.bg_color), ScreenUtil.dip2px(10)));
        recycleView.setAdapter(delegateAdapter);
        allotPurchaseAdapter = new AllotPurchaseAdapter(getActivity(),getManager(),list,1);
        delegateAdapter.addAdapter(allotPurchaseAdapter);
        if (lastPosition >= 0) {
            virtualLayoutManager.scrollToPositionWithOffset(lastPosition, lastOffer);
        }
        initListener();
    }

    private void initListener(){
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
                            loadData();
                        }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }}
        );
    }
    private void getLastLocation() {
        if(virtualLayoutManager==null){
            return;
        }
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
        isRefresh=true;
        resetView();
        loadData();
    }

    @OnClick(R.id.btn_reload)
    public void onViewClicked() {
        p=1;
        resetView();
        loadData();
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

    private void resetView(){
        recycleView.setVisibility(View.VISIBLE);
        imgHint.setVisibility(View.GONE);
        btnReload.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        virtualLayoutManager=null;
        delegateAdapter=null;
        allotPurchaseAdapter =null;
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

    @Override
    public View getScrollableView() {
        return recycleView;
    }
}
