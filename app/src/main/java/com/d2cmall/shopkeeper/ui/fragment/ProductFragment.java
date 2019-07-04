package com.d2cmall.shopkeeper.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.d2cmall.shopkeeper.adapter.ProductManagerAdapter;
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
import com.d2cmall.shopkeeper.ui.activity.AddProductActivity;
import com.d2cmall.shopkeeper.ui.activity.ProductDetailActivity;
import com.d2cmall.shopkeeper.ui.view.SharePop;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by LWJ on 2019/2/19.
 * Description : ProductFragment
 * 商品管理
 */

public class ProductFragment extends BaseFragment implements BaseView{
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
    private int status;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private ProductManagerAdapter productManagerAdapter;
    private List<ProductBean> list;
    private boolean isRefresh;
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private boolean needEnd;
    private int allot,buyout,crowd;

    public static ProductFragment newInstance(int status) {
        ProductFragment buyerSaleFragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putInt("status", status);
        buyerSaleFragment.setArguments(args);
        return buyerSaleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            status = getArguments().getInt("status");
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
        addDisposable(ApiRetrofit.getInstance().getApiService().getProductList(buildParam(p, 20)), new BaseObserver<BaseModel<ProductListBean>>() {
            @Override
            public void onSuccess(BaseModel<ProductListBean> o) {
                ptr.refreshComplete();
                if (p==1){
                    list.clear();
                }
                if (o.getData()!=null&&o.getData().getRecords()!=null&&o.getData().getRecords().size()>0){
                    hasNext=o.getData().getCurrent()<o.getData().getPages()?true:false;
                    list.addAll(o.getData().getRecords());
                }else {
                    if (p==1){
                        setEmptyView(Constants.NO_DATA);
                    }
                }
                productManagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int errorCode, String msg) {
                ptr.refreshComplete();
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
        map.put("virtual",String.valueOf(0));
        map.put("status",String.valueOf(status));
        if (allot==1){
            map.put("allot",String.valueOf(allot));
        }
        if (buyout==1){
            map.put("buyout",String.valueOf(buyout));
        }
        if (crowd==1){
            map.put("crowd",String.valueOf(crowd));
        }
        return map;
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
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.line), ScreenUtil.dip2px(10)));
        recycleView.setAdapter(delegateAdapter);
        productManagerAdapter = new ProductManagerAdapter(getActivity(),getManager(),list);
        delegateAdapter.addAdapter(productManagerAdapter);
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
        productManagerAdapter.setPopListener(new ProductManagerAdapter.PopListener() {
            @Override
            public void sold(long id, int status) {
                addDisposable(ApiRetrofit.getInstance().getApiService().productStatus(id, status==0?1:0), new BaseObserver() {
                    @Override
                    public void onSuccess(Object o) {
                        if (status == 0) {
                            Util.showToast(mContext, "上架成功");
                        } else {
                            Util.showToast(mContext, "下架成功");
                        }
                        refresh();
                    }
                });
            }

            @Override
            public void edit(ProductBean productBean) {
                toEditActivity(productBean.getId());
            }

            @Override
            public void share(ProductBean productBean) {
                if (Util.wxInstalled()) {
                    SharePop pop = new SharePop(mContext);
                    pop.setTitle(productBean.getName());
                    pop.setDescription("发现了一个好东西，赶紧看看吧！");
                    String[] imgs=productBean.getPic().split(",");
                    pop.setImage(StringUtil.getD2cPicUrl(imgs[0], 100, 100), false);
                    pop.setWebUrl(Constants.SHARE_URL+"product/"+productBean.getId());
                    pop.show(recycleView);
                } else {
                    Util.showToast(mContext, "请先安装微信");
                }
            }

            @Override
            public void delete(long id) {
                new AlertDialog.Builder(mContext)
                        .setMessage("确定删除该商品吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addDisposable(ApiRetrofit.getInstance().getApiService().productDelete(id), new BaseObserver() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Util.showToast(mContext, "删除成功");
                                        refresh();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    private void toEditActivity(long id){
        addDisposable(ApiRetrofit.getInstance().getApiService().getProduct(id), new BaseObserver<BaseModel<ProductBean>>() {
            @Override
            public void onSuccess(BaseModel<ProductBean> o) {
                if (o.getData()!=null){
                    Intent intent = new Intent(mContext, AddProductActivity.class);
                    intent.putExtra("product", o.getData());
                    getActivity().startActivityForResult(intent, 100);
                }
            }
        });
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

    public void setAllot(int allot) {
        this.allot = allot;
    }

    public void setBuyout(int buyout) {
        this.buyout = buyout;
    }

    public void setCrowd(int crowd) {
        this.crowd = crowd;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        virtualLayoutManager=null;
        delegateAdapter=null;
        productManagerAdapter.setPopListener(null);
        productManagerAdapter=null;
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
