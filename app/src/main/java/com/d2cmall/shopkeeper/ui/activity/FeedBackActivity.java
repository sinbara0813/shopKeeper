package com.d2cmall.shopkeeper.ui.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.model.FeedBackBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/3/11.
 * 邮箱:hrb940258169@163.com
 * 意见反馈
 */

public class FeedBackActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.edit_tv)
    EditText editTv;
    private UserBean user;

    @Override
    public int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @OnClick({R.id.tv_right, R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                if(StringUtil.isEmpty(editTv.getText().toString().trim())){
                    Util.showToast(this,"请填写反馈内容!");
                    return;
                }
                insertFeedBack();
                break;
        }
    }

    private void insertFeedBack() {
        tvRight.setEnabled(false);
        FeedBackBean feedBackBean = new FeedBackBean();
        feedBackBean.setContent(editTv.getText().toString().trim());
        addDisposable(ApiRetrofit.getInstance().getApiService().insertFeedBack(feedBackBean), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(FeedBackActivity.this,"感谢您的反馈");
                finish();
            }

            @Override
            public void onError(int errorCode, String msg) {
                tvRight.setEnabled(true);
                super.onError(errorCode, msg);
            }
        });
    }


    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvName.setText("意见反馈");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        tvRight.setText("提交");
        user = Session.getInstance().getUserFromFile(this);
    }
}
