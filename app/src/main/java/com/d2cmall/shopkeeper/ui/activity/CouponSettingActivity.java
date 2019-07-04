package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/2/26.
 * 邮箱:hrb940258169@163.com
 * 优惠券设置
 */

public class CouponSettingActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_threshold_amount)
    EditText etThresholdAmount;
    @BindView(R.id.et_discount_amount)
    EditText etDiscountAmount;
    @BindView(R.id.et_total_num)
    EditText etTotalNum;
    @BindView(R.id.et_limit_num)
    EditText etLimitNum;
    private double discountAmount;
    private double thresholdAmount;
    private int limitNum;
    private int totalNum;

    @Override
    public int getLayoutId() {
        return R.layout.activity_coupon_setting;
    }

    @Override
    public void doBusiness() {
        tvName.setText("优惠设置");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("完成");
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        discountAmount = getIntent().getDoubleExtra("discountAmount", 0);
        if(discountAmount>0){
            etDiscountAmount.setText(Util.getNumberFormat(discountAmount));
        }
        //优惠门槛
        thresholdAmount =  getIntent().getDoubleExtra("thresholdAmount", 0);
        if(thresholdAmount>0){
            etThresholdAmount.setText(Util.getNumberFormat(thresholdAmount));
        }
        //每人限领
        limitNum =  getIntent().getIntExtra("limitNum", 0);
        if(limitNum>0){
            etLimitNum.setText(String.valueOf(limitNum));
        }
        //总张数
        totalNum =  getIntent().getIntExtra("totalNum", 0);
        if(totalNum>0){
            etTotalNum.setText(String.valueOf(totalNum));
        }
    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                settingCompelte();
                break;
        }
    }

    private void settingCompelte() {
        if(getDoubleValue(etDiscountAmount.getText().toString().trim())<=0){
            Util.showToast(this,"请输入正确的优惠金额");
            return;
        }
        if(getDoubleValue(etThresholdAmount.getText().toString().trim())<=0){
            Util.showToast(this,"请输入正确的优惠券使用门槛");
            return;
        }
        if(getDoubleValue(etThresholdAmount.getText().toString().trim())<getDoubleValue(etDiscountAmount.getText().toString().trim())){
            Util.showToast(this,"优惠力度已大于优惠门槛!");
            return;
        }
        if(StringUtil.isEmpty(etLimitNum.getText().toString().trim()) || Integer.valueOf(etLimitNum.getText().toString().trim())<=0){
            Util.showToast(this,"请输入正确的领用上限");
            return;
        }
        if(StringUtil.isEmpty(etTotalNum.getText().toString().trim()) || Integer.valueOf(etTotalNum.getText().toString().trim())<=0){
            Util.showToast(this,"请输入正确的发行总数");
            return;
        }
        if(Integer.valueOf(etLimitNum.getText().toString().trim())>Integer.valueOf(etTotalNum.getText().toString().trim())){
            Util.showToast(this,"领用上限已大于发行总数");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("discountAmount",getDoubleValue(etDiscountAmount.getText().toString().trim()));
        intent.putExtra("thresholdAmount",getDoubleValue(etThresholdAmount.getText().toString().trim()));
        intent.putExtra("limitNum",Integer.valueOf(etLimitNum.getText().toString().trim()));
        intent.putExtra("totalNum",Integer.valueOf(etTotalNum.getText().toString().trim()));
        setResult(RESULT_OK,intent);
        finish();
    }

    private  double getDoubleValue(String str){
        if(StringUtil.isEmpty(str)){
            return 0.0;
        }else if(str.contains(",")){
            str=str.replaceAll(",","");
        }
        return Double.valueOf(str);
    }
}
