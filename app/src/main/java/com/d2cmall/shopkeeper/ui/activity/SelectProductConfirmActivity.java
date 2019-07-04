package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.SelectProductConfirmAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Util;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by LWJ on 2019/3/4.
 * Description : SelectProductConfirmActivity
 * 拼团选定商品界面
 */

public class SelectProductConfirmActivity extends BaseActivity {
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
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private SelectProductConfirmAdapter selectProductConfirmAdapter;
    private long productId;
    private ProductBean productBean;
    private boolean isEdit;
    private String name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_product_confirm;
    }

    @Override
    public void doBusiness() {
        init();

    }

    private void init() {
        initView();
        virtualLayoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(0, 2);
        recycleView.setRecycledViewPool(recycledViewPool);
        selectProductConfirmAdapter = new SelectProductConfirmAdapter(this, Glide.with(this));
        delegateAdapter.addAdapter(selectProductConfirmAdapter);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
        loadProductInfo();
    }

    private void initView() {
        tvName.setText("选择商品");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("确定");
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        productId = getIntent().getLongExtra("productId", 0);
        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if(isEdit){
            tvName.setText("编辑商品");
        }
        if(productId==0){
            return;
        }
        ptr.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadProductInfo();
            }
        });
    }

    private void loadProductInfo() {
        progressBar.setVisibility(View.VISIBLE);
        addDisposable(ApiRetrofit.getInstance().getApiService().getProduct(productId),new BaseObserver<BaseModel<ProductBean>>(){
            @Override
            public void onSuccess(BaseModel<ProductBean> o) {
                progressBar.setVisibility(View.GONE);
                ptr.refreshComplete();
                productBean=o.getData();
                name = productBean.getName();
                selectProductConfirmAdapter.setProductBean(productBean);
                selectProductConfirmAdapter.notifyDataSetChanged();
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
                imgHint.setVisibility(View.GONE);
                btnReload.setVisibility(View.GONE);
                loadProductInfo();
                break;
            case R.id.iv_back:
                if(selectProductConfirmAdapter!=null ){
                    double promotionPrice = selectProductConfirmAdapter.getPromotionPrice();
                    if( promotionPrice<=0){
                        finish();
                    }else{
                        Intent intent = new Intent();
                        if(!isEdit){
                            intent.putExtra("productId",productId);
                        }
                        intent.putExtra("promotionPrice",promotionPrice);
                        intent.putExtra("productName",name);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }
                break;
            case R.id.tv_right:
                if(selectProductConfirmAdapter!=null ){
                    double promotionPrice = selectProductConfirmAdapter.getPromotionPrice();
                    if( promotionPrice<=0){
                        Util.showToast(this,"请输入正确的拼团价");
                    }else{
                        Intent intent = new Intent();
                        if(!isEdit){
                            intent.putExtra("productId",productId);
                        }
                        intent.putExtra("promotionPrice",promotionPrice);
                        intent.putExtra("productName",name);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                }
                break;
        }
    }
}
