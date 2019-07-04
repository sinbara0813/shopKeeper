package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.adapter.CountryAdapter;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.model.CountryBean;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.headerview.StickyListHeadersListView;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/25.
 * Description : CountryCodeActivity
 * 选择国家地区
 */
public class CountryCodeActivity extends BaseActivity implements CountryAdapter.OnItemClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.listView)
    StickyListHeadersListView listView;
    private CountryAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_country_code;
    }

    @Override
    public void doBusiness() {
        initTitle();
        adapter = new CountryAdapter(this);
        listView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        getCountryFile();
    }

    private void initTitle() {
        tvName.setText(getResources().getString(R.string.label_country_code));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0,0,0,ScreenUtil.dip2px(16), 0, 0);
    }

    private void getCountryFile() {
        String jsonStr = Util.readStreamToString(getResources().openRawResource(R.raw.country));
        Gson gson = new Gson();
        List<CountryBean> countryBeanList = gson.fromJson(jsonStr, new TypeToken<List<CountryBean>>() {
        }.getType());
        adapter.refresh(countryBeanList);
    }

    @Override
    public void OnItemClickListener(CountryBean countryBean, int position) {
        if (countryBean != null) {
            Intent intent = getIntent();
            intent.putExtra("country", countryBean);
            setResult(RESULT_OK, intent);
            super.onBackPressed();
        }
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

}
