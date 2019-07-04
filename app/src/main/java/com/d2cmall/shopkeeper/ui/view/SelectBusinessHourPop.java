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
import com.d2cmall.shopkeeper.ui.view.loopview.LoopView;
import com.d2cmall.shopkeeper.ui.view.loopview.OnItemSelectedListener;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预约时间选择
 * Author: hrb
 * Date: 2017/03/16 11:29
 * Copyright (c) 2016 d2cmall. All rights reserved.
 */
public class SelectBusinessHourPop implements TransparentPop.InvokeListener {

    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.line_layout)
    View lineLayout;
    @BindView(R.id.select_month)
    LoopView selectMonth;
    @BindView(R.id.select_day)
    LoopView selectDay;
    @BindView(R.id.select_hour)
    LoopView selectHour;
    @BindView(R.id.select_minute)
    LoopView selectMinute;
    private Context mContext;
    private View bottomView;
    private TransparentPop mPop;
    private Animation inAnimation;
    private Animation outAnimation;
    private String dateStr;
    ArrayList<String> hours;
    ArrayList<String> minutes;
    public String openHour = "00";
    public String openMinute = "00";
    public String closeHour="00";
    public String closeMinute="00";
    public boolean isMonthEnd;

    public SelectBusinessHourPop(Context context, int openHourHours, int openHourMinutes, int closeHourHours, int closeHourMinutes) {
        this.mContext = context;
        openHour=addZero(openHourHours);
        openMinute=addZero(openHourMinutes);
        closeHour=addZero(closeHourHours);
        closeMinute=addZero(closeHourMinutes);
        init();
    }

    private void init() {
        bottomView = LayoutInflater.from(mContext).inflate(R.layout.selert_time_pop, new LinearLayout(mContext), false);
        ButterKnife.bind(this, bottomView);
        mPop = new TransparentPop(mContext, this);
        inAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_up);
        outAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_down);
        mPop.setInAnimation(inAnimation);
        mPop.setOutAnimation(outAnimation);
        mPop.dismissFromOut();
        initListener();
        initData();
    }

    private void initData() {
        hours = new ArrayList<>();
        minutes = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(addZero(i));
        }
        for (int i = 0; i < 60; i++) {
            minutes.add(addZero(i));
        }
        selectMonth.setItems(hours);

        if(hours.contains(openHour)){
            selectMonth.setCurrentPosition(hours.indexOf(openHour));
        }else{
            selectMonth.setCurrentPosition(0);
        }
        selectDay.setItems(minutes);
        if(minutes.contains(openMinute)){
            selectDay.setCurrentPosition(hours.indexOf(openMinute));
        }else{
            selectDay.setCurrentPosition(0);
        }
        selectHour.setItems(hours);
        if(hours.contains(openHour)){
            selectHour.setCurrentPosition(hours.indexOf(closeHour));
        }else{
            selectHour.setCurrentPosition(0);
        }
        selectMinute.setItems(minutes);
        if(minutes.contains(closeMinute)){
            selectMinute.setCurrentPosition(hours.indexOf(closeMinute));
        }else{
            selectMinute.setCurrentPosition(0);
        }
    }


    private void initListener() {
        selectMonth.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                openHour = hours.get(index);
            }
        });

        selectDay.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                openMinute = minutes.get(index);
            }
        });

        selectHour.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                closeHour =hours.get(index);
            }
        });

        selectMinute.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                closeMinute = minutes.get(index);
            }
        });

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(openHour)>Integer.valueOf(closeHour) || ( Integer.valueOf(openHour)==Integer.valueOf(closeHour) && Integer.valueOf(openMinute)>Integer.valueOf(closeMinute)) ){
                    Util.showToast(mContext,"请选择正确的营业时间");
                    return;
                }
                if(!StringUtil.isEmpty(getBusinessHourStr()) && "00:00-00:00".equals(getBusinessHourStr())){
                    Util.showToast(mContext,"请选择正确的营业时间");
                    return;
                }
                dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPop != null) {
                    mPop.dismiss();
                }
            }
        });
    }

    public void show(View parent) {
        mPop.show(parent, true);
    }

    public void dismiss() {
        if (mPop != null) {
            mPop.dismiss(true);
        }
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.BOTTOM);
        v.addView(bottomView);
    }

    @Override
    public void releaseView(LinearLayout v) {

    }

    public void setDismissCallBack(TransparentPop.DismissListener dismissListener) {
        mPop.setDismissListener(dismissListener);
    }

    public String getBusinessHourStr() {
        return openHour + ":" + openMinute + "-" + closeHour + ":" + closeMinute;
    }



    private String addZero(int number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return number + "";
        }
    }
}
