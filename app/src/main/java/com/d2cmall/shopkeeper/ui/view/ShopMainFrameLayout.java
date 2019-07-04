package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.SharedPreUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class ShopMainFrameLayout extends FrameLayout implements PtrUIHandler {

    private ImageView imgProgress;
    private TextView tvLabel;
    private TextView tvRefreshTime;
    private RotateAnimation rotateAnimation;
    private ImageView ivStatus;

    public ShopMainFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public ShopMainFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShopMainFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View headView = View.inflate(context, R.layout.layout_main_rotate_header, this);
        imgProgress = (ImageView) headView.findViewById(R.id.iv_progress);
        tvLabel = (TextView) headView.findViewById(R.id.tv_loading);
        ivStatus = (ImageView) headView.findViewById(R.id.iv_loading);
        tvRefreshTime = (TextView) headView.findViewById(R.id.tv_refresh_time);
        long homeRefreshTime = SharedPreUtil.getLong("homeRefreshTime", System.currentTimeMillis());
        tvRefreshTime.setText(getContext().getString(R.string.label_main_refresh_time,parseDate(homeRefreshTime)));
    }

    public void setLabel(String label) {
        tvLabel.setText(label);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        imgProgress.clearAnimation();
        tvLabel.setText("下拉即可刷新...");
        long homeRefreshTime = SharedPreUtil.getLong("homeRefreshTime", System.currentTimeMillis());
        tvRefreshTime.setText(getContext().getString(R.string.label_main_refresh_time,parseDate(homeRefreshTime)));
        ivStatus.setImageResource(R.mipmap.icon_money);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {

    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        tvLabel.setText("刷新加载中...");
        startMyAnimation(0,360);
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        imgProgress.clearAnimation();
        tvLabel.setText("刷新完成...");
        tvRefreshTime.setText(getContext().getString(R.string.label_main_refresh_time,parseDate(System.currentTimeMillis())));
        ivStatus.setImageResource(R.mipmap.icon_wancheng);
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        final int currentPosition = ptrIndicator.getCurrentPosY();
        final int lastPosition = ptrIndicator.getLastPosY();
        if (isUnderTouch && currentPosition > ScreenUtil.dip2px(70)) {
            tvLabel.setText("释放即可刷新...");
        } else if (isUnderTouch && currentPosition < ScreenUtil.dip2px(70)) {
            tvLabel.setText("下拉即可刷新...");
        }
        if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
            if (currentPosition > lastPosition) {
                startMyAnimation(lastPosition, currentPosition);
            } else if (currentPosition < lastPosition) {
                startMyAnimation(lastPosition, currentPosition);
            } else {
                imgProgress.clearAnimation();
            }
        }

        if (!isUnderTouch) {
            imgProgress.clearAnimation();
            buildAnimation(0, 360);
            imgProgress.startAnimation(rotateAnimation);
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_LOADING) {
                tvLabel.setText("刷新加载中...");
            }
        }
        invalidate();
    }

    private void startMyAnimation(float fromDegrees, float toDegrees) {
        if (imgProgress != null) {
            imgProgress.clearAnimation();
            buildAnimation(fromDegrees,toDegrees);
            imgProgress.startAnimation(rotateAnimation);
        }
    }

    private String parseDate(Long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(time);
        format.substring(10,16);
        return format.substring(10,16);
    }

    private void buildAnimation(float fromDegrees, float toDegrees) {
        rotateAnimation = new RotateAnimation(fromDegrees, toDegrees, RotateAnimation.RELATIVE_TO_SELF,
                0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(600);
        rotateAnimation.setRepeatCount(-1);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setFillAfter(true);
    }

}
