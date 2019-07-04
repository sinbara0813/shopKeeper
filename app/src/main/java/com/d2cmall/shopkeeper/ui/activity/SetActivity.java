package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/6/18.
 * 邮箱:hrb940258169@163.com
 */
public class SetActivity extends BaseActivity {

    @BindView(R.id.tv_login_out)
    TextView tvLoginOut;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(tvLoginOut, Color.parseColor("#ffffff"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"),  ScreenUtil.dip2px(20), 0, 0);
    }

    @OnClick({R.id.back_iv, R.id.info_ll, R.id.idea_rl, R.id.ll_about_us, R.id.tv_login_out})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.info_ll:
                intent=new Intent(this,EditPersonInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.idea_rl:
                intent=new Intent(this,FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_about_us:
                intent=new Intent(this,AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login_out:
                loginOut();
                break;
        }
    }

    private void loginOut() {
        addDisposable(ApiRetrofit.getInstance().getApiService().loginout(), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Session.getInstance().logout(SetActivity.this);
                Util.showToast(SetActivity.this,"退出登录成功");
                Intent intent = new Intent(SetActivity.this, LoginActivity.class);
                intent.putExtra("isFromeLoginOut",true);
                startActivity(intent);
                finish();
            }
        });
    }
}
