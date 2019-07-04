package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.Util;
import com.r0adkll.slidr.Slidr;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/25.
 * Description : FundManagerActivity
 * 资金管理
 */

public class FundManagerActivity extends BaseActivity {
    @BindView(R.id.status_bar_line)
    View statusBarLine;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.guide_line)
    Guideline guideLine;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv_total_foz)
    TextView tvTotalFoz;
    @BindView(R.id.rl_withdraw)
    RelativeLayout rlWithdraw;
    @BindView(R.id.tv_pending_settlement_amount)
    TextView tvPendingSettlementAmount;
    @BindView(R.id.pending_settlement)
    RelativeLayout pendingSettlement;
    @BindView(R.id.rl_transaction_trade)
    RelativeLayout rlTransactionTrade;
    @BindView(R.id.rl_transaction_record)
    RelativeLayout rlTransactionRecord;
    @BindView(R.id.tv_bond)
    TextView tvBond;
    @BindView(R.id.rl_bond)
    RelativeLayout rlBond;
    @BindView(R.id.rl_margin_record)
    RelativeLayout rlMarginRecord;
    @BindView(R.id.rl_settle_margin)
    RelativeLayout rlSettleMargin;
    private UserBean user;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fund_manager;
    }

    @Override
    public void doBusiness() {
        Slidr.attach(this);
        Sofia.with(this)
                .invasionStatusBar()
                .statusBarBackground(Color.TRANSPARENT);
        ConstraintLayout.LayoutParams cl= (ConstraintLayout.LayoutParams) statusBarLine.getLayoutParams();
        cl.setMargins(0, ScreenUtil.getStatusBarHeight(this),0,0);
        user = Session.getInstance().getUserFromFile(this);
        if (user != null) {
            loadBrandInfo();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void loadBrandInfo() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(user.getShopId()), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBean) {
                tvTotalAmount.setText(Util.getNumberFormat(shopBean.getData().getBalance()));
                tvBond.setText(getString(R.string.label_money, Util.getNumberFormat(shopBean.getData().getDeposit())));
                tvTotalFoz.setText(Util.getNumberFormat(shopBean.getData().getFrozen()));
            }
        });
    }


    @OnClick({R.id.tv2,R.id.back_iv, R.id.rl_withdraw, R.id.pending_settlement, R.id.rl_transaction_record, R.id.rl_transaction_trade, R.id.rl_margin_record, R.id.rl_settle_margin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv2:
                Util.showToast(this,"冻结金额:申请提现的金额");
                break;
            case R.id.back_iv:
                finish();
                break;
            case R.id.rl_withdraw:
                //提现
                if (Util.loginChecked(this, 999)) {
                    Intent intent = new Intent(this, ApplayWithdrawActivity.class);
                    startActivityForResult(intent, Constants.RequestCode.WITHDRAW_SUCCESS);
                }
                break;
            case R.id.pending_settlement:
                //待结算
                startActivity(new Intent(this, TransactionRecordActivity.class));
                break;
            case R.id.rl_transaction_record:
                //交易记录
                startActivity(new Intent(this, TransactionRecordActivity.class));
                break;
            case R.id.rl_transaction_trade:
                //交易记录
                startActivity(new Intent(this, WithdrawRecordActivity.class));
                break;
            case R.id.rl_margin_record:
                //保证金记录
                startActivity(new Intent(this,MarginReportActivity.class));
                break;
            case R.id.rl_settle_margin:
                //结算保证金
                startActivity(new Intent(this,ProductSettleActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Constants.RequestCode.WITHDRAW_SUCCESS) {
            loadBrandInfo();
        }
    }

}
