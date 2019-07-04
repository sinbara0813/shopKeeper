package com.d2cmall.shopkeeper.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.MarginAdapter;
import com.d2cmall.shopkeeper.adapter.WithdrawRecordAdapter;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.decoration.DividerDecoration;
import com.d2cmall.shopkeeper.model.MarginBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.model.WithdrawRecordListBean;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/6/18.
 * 邮箱:hrb940258169@163.com
 * 保证金记录
 */
public class MarginReportActivity extends BaseActivity {

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;

    private VirtualLayoutManager virtualLayoutManager;
    private DelegateAdapter delegateAdapter;
    private MarginAdapter marginAdapter;
    private List<MarginBean.RecordsBean> data=new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_margin_report;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        virtualLayoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager, true);
        marginAdapter = new MarginAdapter(this,data);
        delegateAdapter.addAdapter(marginAdapter);
        recycleView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.bg_color), ScreenUtil.dip2px(10)));
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
        loadData();
    }

    private void loadData() {
        UserBean userBean= Session.getInstance().getUserFromFile(this);
        ArrayMap<String,String> map=Util.buildListParam(1,50);
        if (userBean!=null){
            map.put("shopId",userBean.getShopId()+"");
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().getMarginList(map), new BaseObserver<BaseModel<MarginBean>>() {
            @Override
            public void onSuccess(BaseModel<MarginBean> o) {
                if (o.getData().getRecords().size()>0){
                    data.addAll(o.getData().getRecords());
                    marginAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @OnClick(R.id.back_iv)
    public void onViewClicked() {
        finish();
    }
}
