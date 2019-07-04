package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

/**
 * Created by LWJ on 2019/2/22.
 * Description : CustomerTabView
 * 订单管理自定义TabItem
 */

public class CustomerTabView extends FrameLayout {


    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_tab_title)
    TextView tvTabTitle;
    @BindView(R.id.iv_indicator)
    ImageView ivIndicator;
    private Context context;

    public CustomerTabView(Context context) {
        super(context);
        this.context = context;
        init(context);
    }

    public CustomerTabView(Context context, boolean isColor) {
        super(context);
        this.context = context;
        init(context);
    }

    public CustomerTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.layout_order_manager_tab_item, this);
        ButterKnife.bind(this, this);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            tvTabTitle.setTextColor(context.getResources().getColor(R.color.color_white));
            tvTabTitle.getPaint().setFakeBoldText(true);
            tvNum.getPaint().setFakeBoldText(true);
            ivIndicator.setVisibility(VISIBLE);
        } else {
            tvTabTitle.getPaint().setFakeBoldText(false);
            tvNum.getPaint().setFakeBoldText(false);
            tvTabTitle.setTextColor(context.getResources().getColor(R.color.trans_60_color_white));
            ivIndicator.setVisibility(INVISIBLE);
        }
    }



    public void setTextColor(int color) {
        tvTabTitle.setTextColor(color);
    }

    public void setText(int num ,String text) {
        tvNum.setText(num+"");
        tvTabTitle.setText(text);
    }

    public void setData(int num) {
        tvNum.setText(num+"");
    }
}

