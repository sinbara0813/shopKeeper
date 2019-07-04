package com.d2cmall.shopkeeper.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
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
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.model.ProductListBean;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
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
 * Created by LWJ on 2019/3/4.
 * Description : CollagePromotionSelectProductListActivity
 * 拼团活动绑定商品
 */

public class CollagePromotionSelectProductListActivity extends BaseActivity implements BaseView, CollagePromotionSelectProductAdapter.SelectedItemListener {
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
    private CollagePromotionSelectProductAdapter collagePromotionSelectProductAdapter;
    private int p = 1;
    private boolean hasNext; //是否有下一页数据


    private List<ProductBean> productBeanList;

    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private int currentSelectPosition=-1;
    private boolean needEnd;
    private long productId;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_product;
    }

    @Override
    public void doBusiness() {
        init();
        initListener();
        loadProducts();
    }

    private void init() {
        tvName.setText("选择商品");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("下一步");
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        productBeanList=new ArrayList<>();
        virtualLayoutManager = new VirtualLayoutManager(this);
        productId = getIntent().getLongExtra("productId", 0);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(0, 2);
        recycleView.setRecycledViewPool(recycledViewPool);
        collagePromotionSelectProductAdapter = new CollagePromotionSelectProductAdapter(this, Glide.with(this),productBeanList,productId);
        recycleView.addItemDecoration(new SpaceItemDecoration(this, 10));
        collagePromotionSelectProductAdapter.setSelectedItemListener(this);
        delegateAdapter.addAdapter(collagePromotionSelectProductAdapter);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
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
                            loadProducts();
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


    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);
        ArrayMap<String, String> map = Util.buildListParam(p, 20);
        //非虚拟
        map.put("virtual",String.valueOf(0));
        //上架状态
        map.put("status","1");
        //正常商品和拼团已结束
        map.put("crowd","8");
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
                        Util.showToast(CollagePromotionSelectProductListActivity.this,"请先创建商品!");
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
                if (collagePromotionSelectProductAdapter != null) {
                    collagePromotionSelectProductAdapter.notifyDataSetChanged();
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

    @OnClick({R.id.btn_reload, R.id.iv_back,R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_reload:
                refresh();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                if(currentSelectPosition>=0){
                    Intent intent = new Intent(this, SelectProductConfirmActivity.class);
                    intent.putExtra("productId", productBeanList.get(currentSelectPosition).getId());
                    startActivityForResult(intent,Constants.RequestCode.COLLAGE_TO_SELECTPRODUCT);
                }else{
                    Util.showToast(this,"请选择一件商品");
                }
                break;
        }
    }

    private void refresh() {
        currentSelectPosition=-1;
        p=1;
        recycleView.setVisibility(View.VISIBLE);
        imgHint.setVisibility(View.GONE);
        btnReload.setVisibility(View.GONE);
        loadProducts();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==Constants.RequestCode.COLLAGE_TO_SELECTPRODUCT){
            setResult(RESULT_OK,data);
            finish();
        }
    }
}
