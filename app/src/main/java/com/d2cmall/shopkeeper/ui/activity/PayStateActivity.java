package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/5/5.
 * 邮箱:hrb940258169@163.com
 * 支付结果
 */

public class PayStateActivity extends BaseActivity {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.state_iv)
    ImageView stateIv;
    @BindView(R.id.state_tv)
    TextView stateTv;
    @BindView(R.id.state_tag_tv)
    TextView stateTagTv;
    @BindView(R.id.check_tv)
    TextView checkTv;

    private int type;
    private double cash;//保证金

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_state;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        type=getIntent().getIntExtra("type",0);
        cash=getIntent().getDoubleExtra("cash",0);
        setView();
    }

    private void setView(){
        switch (type){
            case 1://线下收银
                stateIv.setImageResource(R.mipmap.icon_fuqian);
                stateTv.setText("付款成功");
                stateTagTv.setText("可在我的零售订单查看");
                checkTv.setText("查看订单");
                break;
            case 2:
                stateIv.setImageResource(R.mipmap.icon_fuqian_shibai);
                stateTv.setText("付款失败");
                stateTagTv.setText("可在我的零售订单查看");
                checkTv.setText("查看订单");
                break;
            case 3://货品结算
                stateIv.setImageResource(R.mipmap.icon_fuqian);
                stateTv.setText("成功结算保证金");
                stateTagTv.setText("已成功返还保证金"+ Util.getNumberFormat(cash)+"元");
                checkTv.setText("查看保证金");
                break;
            case 4:
                stateIv.setImageResource(R.mipmap.icon_fuqian_shibai);
                stateTv.setText("结算失败");
                stateTagTv.setText("未返还保证金"+ Util.getNumberFormat(cash)+"元");
                checkTv.setText("重新结算");
                break;
            case 5:
                stateIv.setImageResource(R.mipmap.icon_fuqian);
                stateTv.setText("付款成功");
                stateTagTv.setText("成功购买商品,可在我的采购订单查看");
                checkTv.setText("查看订单");
                break;
            case 6:
                stateIv.setImageResource(R.mipmap.icon_fuqian_shibai);
                stateTv.setText("未付款成功");
                stateTagTv.setText("支付遇到问题,可以咨询我们的客服哦");
                checkTv.setText("查看订单");
                break;
            case 7:
                stateIv.setImageResource(R.mipmap.icon_fuqian);
                stateTv.setText("成功发起调拨申请");
                stateTagTv.setText("可在调拨单中查看该订单状态");
                checkTv.setText("查看调拨单");
                break;
            case 8:
                stateIv.setImageResource(R.mipmap.icon_fuqian_shibai);
                stateTv.setText("未成功发起调拨");
                stateTagTv.setText("可重新发起调拨申请");
                checkTv.setText("查看调拨单");
                break;
        }
    }

    @OnClick({R.id.back_iv, R.id.check_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.check_tv:
                Intent intent=null;
                switch (type){
                    case 1:
                    case 2:
                        intent=new Intent(this,OrderManagerActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 3:
                        finish();
                        break;
                    case 4:
                        intent=new Intent(this,ProductSettleActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case 5:
                    case 6:
                        intent=new Intent(this,PurchaseAllocationWarehousingListActivity.class);
                        intent.putExtra("first",0);
                        intent.putExtra("second",0);
                        startActivity(intent);
                        finish();
                        break;
                    case 7:
                    case 8:
                        intent=new Intent(this,PurchaseAllocationWarehousingListActivity.class);
                        intent.putExtra("first",1);
                        intent.putExtra("second",0);
                        startActivity(intent);
                        finish();
                        break;
                }
                break;
        }
    }
}
