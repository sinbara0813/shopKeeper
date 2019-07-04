package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/6/5.
 * 邮箱:hrb940258169@163.com
 */

public class InfoPop implements TransparentPop.InvokeListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.sure_tv)
    TextView sureTv;
    private View rooView;
    private TransparentPop mPop;

    public InfoPop(Context context) {
        rooView = LayoutInflater.from(context).inflate(R.layout.layout_info_pop, new CoordinatorLayout(context), false);
        ButterKnife.bind(this, rooView);
        mPop = new TransparentPop(context, this);
    }

    public void show(View view) {
        mPop.show(view, false);
    }

    public void dismiss() {
        mPop.dismiss(false);
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.CENTER);
        v.addView(rooView);
    }

    @Override
    public void releaseView(LinearLayout v) {
        v.removeView(rooView);
        sureTv.setOnClickListener(null);
        rooView = null;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setInfo(String info) {
        this.info.setText(info);
    }

    public void setSure(String sure){
        this.sureTv.setText(sure);
    }

    public void setListener(View.OnClickListener listener) {
        sureTv.setOnClickListener(listener);
    }
}
