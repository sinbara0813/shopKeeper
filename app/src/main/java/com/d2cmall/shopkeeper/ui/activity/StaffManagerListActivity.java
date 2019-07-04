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
import com.d2cmall.shopkeeper.adapter.StaffManagerAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.list.BaseVirtualAdapter;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.binder.ScrollEndBinder;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.holder.DefaultHolder;
import com.d2cmall.shopkeeper.model.StaffListBean;
import com.d2cmall.shopkeeper.ui.view.PtrStoreHouseFrameLayout;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by LWJ on 2019/2/28.
 * Description : StaffManagerListActivity
 * 员工管理
 */

public class StaffManagerListActivity extends BaseActivity implements BaseView {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
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
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private StaffManagerAdapter staffManagerAdapter;
    private int pageNumber=1;
    private boolean hasNext = true;
    private BaseVirtualAdapter<DefaultHolder> endAdapter; //列表结束标志
    private List<StaffListBean.RecordsBean> staffList;
    private boolean needEnd;


    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_manager;
    }

    @Override
    public void doBusiness() {
        initListener();
        init();
        loadStaffList();
    }

    private void init() {
        tvName.setText("员工管理");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        virtualLayoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(0, 2);
        recycleView.setRecycledViewPool(recycledViewPool);
        staffList=new ArrayList<>();
        staffManagerAdapter = new StaffManagerAdapter(this, Glide.with(this),staffList);
        delegateAdapter.addAdapter(staffManagerAdapter);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);

    }

    private void loadStaffList() {
        progressBar.setVisibility(View.VISIBLE);
        ArrayMap<String, String> hashMap = new ArrayMap<>();
        hashMap.put("ps", "20");
        hashMap.put("p", String.valueOf(pageNumber));
        addDisposable(ApiRetrofit.getInstance().getApiService().getStaffList(hashMap), new BaseObserver<BaseModel<StaffListBean>>(this) {

            @Override
            public void onSuccess(BaseModel<StaffListBean> staffListBean) {
                ptr.refreshComplete();
                btnReload.setVisibility(View.GONE);
                imgHint.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                recycleView.setVisibility(View.VISIBLE);
                if (pageNumber == 1) {
                    staffList.clear();
                }
                if (staffListBean != null && staffListBean.getData() != null && staffListBean.getData().getRecords() != null && staffListBean.getData().getRecords().size() > 0) {
                    staffList.addAll(staffListBean.getData().getRecords());
                }
                if(staffListBean != null && staffListBean.getData() != null ){
                    hasNext=staffListBean.getData().getCurrent()<staffListBean.getData().getPages();
                }
                if (staffManagerAdapter != null) {
                    staffManagerAdapter.notifyDataSetChanged();
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
                pageNumber = 1;
                loadStaffList();
            }
        });
        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int last = virtualLayoutManager.findLastVisibleItemPosition();
                        if (last > staffManagerAdapter.getItemCount() - 3 && hasNext) {
                            pageNumber++;
                            loadStaffList();
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

    @OnClick({R.id.iv_back, R.id.btn_reload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_reload:
                imgHint.setVisibility(View.GONE);
                btnReload.setVisibility(View.GONE);
                pageNumber = 1;
                loadStaffList();
                break;
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==Constants.RequestCode.ADD_STAFF_SUCCESS){
                pageNumber = 1;
                loadStaffList();
            }
        }
    }
}
