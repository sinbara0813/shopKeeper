package com.d2cmall.shopkeeper.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
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
import com.d2cmall.shopkeeper.adapter.SelectProductAdapter;
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
import com.d2cmall.shopkeeper.model.CouponInsertProductBean;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.model.ProductListBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
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
 * 作者:Created by sinbara on 2019/2/26.
 * 邮箱:hrb940258169@163.com
 */

public class AssignProductActivity extends BaseActivity implements BaseView {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.select_all_iv)
    ImageView selectAllIv;
    @BindView(R.id.select_all_tv)
    TextView selectAllTv;
    @BindView(R.id.product_count_tv)
    TextView productCountTv;
    @BindView(R.id.add_iv)
    TextView addIv;
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
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private SelectProductAdapter selectProductAdapter;
    private double discountAmount;
    private double thresholdAmount;
    private int limitNum;
    private int totalNum;
    private boolean hasSelectAll = false;
    private String couponName;
    private String receiveStartDate;
    private String receiveEndDate;
    private String useStartDate;
    private String useEndDate;
    private UserBean user;
    private int scopeType;

    private int p = 1;
    private boolean hasNext; //是否有下一页数据

    private boolean isRefresh;

    private List<ProductBean> productBeanList;

    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志

    private CouponBean couponBean;
    private boolean isEditCoupon;
    private boolean needEnd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_assign_product;
    }

    @Override
    public void doBusiness() {
        init();
        initListener();
    }


    private void init() {
        //适用范围
        productBeanList = new ArrayList<>();
        couponBean= (CouponBean) getIntent().getSerializableExtra("coupon");
        isEditCoupon=getIntent().getBooleanExtra("isEditCoupon",false);
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        ShadowDrawable.setShadowDrawable(addIv, Color.parseColor("#4050D2"), ScreenUtil.dip2px(2),
                Color.parseColor("#668d92a3"), ScreenUtil.dip2px(15), 0, 0);
        virtualLayoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        selectProductAdapter = new SelectProductAdapter(this, Glide.with(this), productBeanList,isEditCoupon);
        recycleView.addItemDecoration(new SpaceItemDecoration(this, 10));
        delegateAdapter.addAdapter(selectProductAdapter);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
        selectProductAdapter.setCouponBean(couponBean);
        selectProductAdapter.notifyDataSetChanged();
        if(couponBean.getType()==0 && isEditCoupon){
            tvRight.setVisibility(View.GONE);
        }else{
            tvRight.setText("完成");
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        }

        if(StringUtil.isEmpty(getIntent().getStringExtra("scopeTypeString"))){
            tvName.setText(couponBean.getTypeName());
        }else{
            tvName.setText(getIntent().getStringExtra("scopeTypeString"));
        }
        loadProducts();
    }

    private void initListener() {
        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                p = 1;
                loadProducts();
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

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);
        ArrayMap<String, String> map = buildParam(p, 20);
        map.put("virtual",String.valueOf(0));
        //普通商品,拼团已结束商品
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
                    }
                }
                if (o.getData() != null && o.getData().getRecords() != null && o.getData().getRecords().size() > 0) {
                    if (o.getData().getPages() > o.getData().getCurrent()) {
                        hasNext = true;
                    } else {
                        hasNext = false;
                    }
                    productBeanList.addAll(o.getData().getRecords());
                    if (isEditCoupon) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i < o.getData().getRecords().size(); i++) {
                            stringBuilder.append(o.getData().getRecords().get(i).getId());
                            if(i!=o.getData().getRecords().size()-1){
                                stringBuilder.append(",");
                            }
                        }
                        loadRelationProduct(stringBuilder.toString());
                    }
                }
                if (selectProductAdapter != null) {
                    selectProductAdapter.notifyDataSetChanged();
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

    private void loadRelationProduct(String productIds) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("productIds", productIds);
        map.put("couponId", String.valueOf(couponBean.getId()));
        addDisposable(ApiRetrofit.getInstance().getApiService().couponRelationProduct(map), new BaseObserver<BaseModel<ArrayList<Long>>>() {
            @Override
            public void onSuccess(BaseModel<ArrayList<Long>> o) {
                for (int i = 0; i < productBeanList.size(); i++) {
                    for (int j = 0; j < o.getData().size(); j++) {
                        if (productBeanList.get(i).getId() == o.getData().get(j)) {
                            productBeanList.get(i).setHasCouponSelect(true);
                        }
                    }

                }
                if (selectProductAdapter != null) {
                    selectProductAdapter.notifyDataSetChanged();
                }
            }

        });
    }

    private ArrayMap<String, String> buildParam(int p, int ps) {
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("p", String.valueOf(p));
        map.put("ps", String.valueOf(ps));
        return map;
    }


    @OnClick({R.id.iv_back, R.id.select_all_iv, R.id.select_all_tv, R.id.add_iv, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
            case R.id.tv_right:
                if (isEditCoupon){
                    setResult(RESULT_OK);
                    finish();
                }else {
                    createCoupon();
                }
                break;
        }
    }

    private void createCoupon(){
        List<ProductBean> list=new ArrayList<>();
        for (ProductBean bean:
                productBeanList) {
            if (bean.getHasCouponSelect()){
                list.add(bean);
            }
        }
        if (list.isEmpty()){
            Util.showToast(this,"请先绑定商品");
            return;
        }
        tvRight.setEnabled(false);
        addDisposable(ApiRetrofit.getInstance().getApiService().insertCoupon(couponBean), new BaseObserver<BaseModel<CouponBean>>() {
            @Override
            public void onSuccess(BaseModel<CouponBean> coupon) {
                couponBean.setId(coupon.getData().getId());
                requestBindProduct(list);
            }
        });
    }

    private void requestBindProduct(List<ProductBean> list) {
        if(couponBean==null){
            return;
        }
        ArrayList<CouponInsertProductBean> couponInsertProductBeans = new ArrayList<>();
        int size=list.size();
        for (int i=0;i<size;i++){
            CouponInsertProductBean couponInsertProductBean = new CouponInsertProductBean();
            couponInsertProductBean.setProductId(list.get(i).getId());
            couponInsertProductBean.setCouponId(couponBean.getId());
            couponInsertProductBean.setType(couponBean.getType());
            couponInsertProductBeans.add(couponInsertProductBean);
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().couponBindProduct(couponInsertProductBeans), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                tvRight.setEnabled(true);
                setResult(RESULT_OK);
                finish();
            }
        });

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

}
