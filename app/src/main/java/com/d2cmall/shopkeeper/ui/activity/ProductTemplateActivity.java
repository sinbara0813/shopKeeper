package com.d2cmall.shopkeeper.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.GroupProductTemplateDetailAdapter;
import com.d2cmall.shopkeeper.adapter.MarketProductDetailAdapter;
import com.d2cmall.shopkeeper.adapter.ProductTemplateDetailAdapter;
import com.d2cmall.shopkeeper.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/7/5.
 * 邮箱:hrb940258169@163.com
 */
public class ProductTemplateActivity extends BaseActivity {

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;

    private VirtualLayoutManager virtualLayoutManager;
    private ProductTemplateDetailAdapter productTemplateDetailAdapter;
    private GroupProductTemplateDetailAdapter groupProductTemplateDetailAdapter;
    private DelegateAdapter delegateAdapter;
    private boolean isGroup;

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_template;
    }

    @Override
    public void doBusiness() {
        isGroup=getIntent().getBooleanExtra("isGroup",false);
        virtualLayoutManager = new VirtualLayoutManager(this);
        delegateAdapter = new DelegateAdapter(virtualLayoutManager);
        recycleView.setLayoutManager(virtualLayoutManager);
        recycleView.setAdapter(delegateAdapter);
    }

    @OnClick({R.id.back_iv, R.id.copy_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.copy_tv:
                break;
        }
    }
}
