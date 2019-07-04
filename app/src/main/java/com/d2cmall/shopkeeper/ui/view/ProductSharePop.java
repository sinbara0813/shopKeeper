package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/6/24.
 * 邮箱:hrb940258169@163.com
 */
public class ProductSharePop implements TransparentPop.InvokeListener {

    @BindView(R.id.share_tv)
    TextView shareTv;
    @BindView(R.id.down_tv)
    TextView downTv;
    @BindView(R.id.cancel_tv)
    TextView cancelTv;
    private View view;
    private TransparentPop mPop;

    public ProductSharePop(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_product_share_pop, new LinearLayout(context), false);
        ButterKnife.bind(this,view);
        mPop = new TransparentPop(context, this);
        Animation inAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up);
        Animation outAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setListener(View.OnClickListener listener){
        shareTv.setOnClickListener(listener);
        downTv.setOnClickListener(listener);
    }

    public void show(View view) {
        mPop.show(view, true);
    }

    public boolean isShow() {
        return mPop.isShowing();
    }

    public void dismiss() {
        mPop.dismiss(true);
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.BOTTOM);
        v.addView(view);
    }

    @Override
    public void releaseView(LinearLayout v) {
        shareTv.setOnClickListener(null);
        downTv.setOnClickListener(null);
        v.removeAllViews();
        view=null;
    }


}
