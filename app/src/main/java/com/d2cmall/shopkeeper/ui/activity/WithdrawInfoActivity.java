package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.model.BankInfoBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.NumberPicker;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/26.
 * Description : WithdrawInfoActivity
 * 编辑提现信息
 */

public class WithdrawInfoActivity extends BaseActivity implements NumberPicker.OnValueChangeListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.rl_bank)
    RelativeLayout rlBank;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.et_area)
    EditText etArea;
    @BindView(R.id.rl_area)
    RelativeLayout rlArea;
    @BindView(R.id.tv_open_bank_name)
    TextView tvOpenBankName;
    @BindView(R.id.et_open_bank_name)
    EditText etOpenBankName;
    @BindView(R.id.rl_open_bank_name)
    RelativeLayout rlOpenBankName;
    @BindView(R.id.tv_bank_account)
    TextView tvBankAccount;
    @BindView(R.id.et_bank_account)
    EditText etBankAccount;
    @BindView(R.id.rl_bank_account)
    RelativeLayout rlBankAccount;
    @BindView(R.id.et_man)
    EditText etMan;
    private int currentFirstIndex;
    private String[] banks;
    private UserBean user;
    private BankInfoBean bankInfoBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_withdraw_info;
    }

    @Override
    public void doBusiness() {
        initTitle();
        initBankInfo();
    }


    private void initTitle() {
        banks = getResources().getStringArray(R.array.banks);
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvName.setText("提现信息");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        tvRight.setText("确定");
        user = Session.getInstance().getUserFromFile(this);
    }

    private void initBankInfo() {
        bankInfoBean = (BankInfoBean) getIntent().getSerializableExtra("bankInfoBean");
        if (bankInfoBean != null) {
            etMan.setText(bankInfoBean.getTrueName());
            tvBankName.setText(bankInfoBean.getBankType());
            etBankAccount.setText(bankInfoBean.getCardNum());
            etOpenBankName.setText(bankInfoBean.getBankName());
            etArea.setText(bankInfoBean.getBankAddress());
        }
    }

    @OnClick({R.id.iv_back, R.id.rl_bank, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_bank:
                //选择银行
                showSelectBankPop();
                break;
            case R.id.tv_right:
                //确定
                upDateInfo();
                break;
        }
    }

    private void showSelectBankPop() {
        View popView = LayoutInflater.from(this).inflate(R.layout.layout_bank_select_pop, null);
        TextView popCancel = popView.findViewById(R.id.pop_cancel);
        TextView popSure = popView.findViewById(R.id.pop_sure);
        NumberPicker numberPicker = popView.findViewById(R.id.number_picker);
        PopupWindow popWindow = new PopupWindow(popView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popWindow.setBackgroundDrawable(new ColorDrawable());
        popWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        numberPicker.setDisplayedValues(banks);
        numberPicker.setMaxValue(banks.length - 1);
        numberPicker.setMinValue(0);
        numberPicker.setValue(currentFirstIndex);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(this);
        popCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popWindow != null && popWindow.isShowing()) {
                    popWindow.dismiss();
                }
            }
        });
        popSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (banks != null && currentFirstIndex < banks.length) {
                    String bank = banks[currentFirstIndex];
                    tvBankName.setText(bank);
                    popWindow.dismiss();
                }
            }
        });
    }

    private void upDateInfo() {
        if (StringUtil.isEmpty(etMan.getText().toString().trim())) {
            Util.showToast(this, "请输入开户人姓名");
            return;
        }
        if (StringUtil.isEmpty(tvBankName.getText().toString().trim())) {
            Util.showToast(this, "请选择银行类别");
            return;
        }
        if (StringUtil.isEmpty(etArea.getText().toString().trim())) {
            Util.showToast(this, "请输入开户行地区");
            return;
        }
        if (StringUtil.isEmpty(etOpenBankName.getText().toString().trim())) {
            Util.showToast(this, "请输入开户行名称");
            return;
        }
        if (StringUtil.isEmpty(etBankAccount.getText().toString().trim())) {
            Util.showToast(this, "请输入银行卡号");
            return;
        }

        if (bankInfoBean == null) {//新增银行卡信息
            BankInfoBean newBankInfoBean = new BankInfoBean();
            newBankInfoBean.setBankAddress(etArea.getText().toString().trim());
            newBankInfoBean.setTrueName(etMan.getText().toString().trim());
            newBankInfoBean.setBankName(etOpenBankName.getText().toString().trim());
            newBankInfoBean.setBankType(tvBankName.getText().toString().trim());
            newBankInfoBean.setCardNum(etBankAccount.getText().toString().trim());
            tvRight.setEnabled(false);
            addDisposable(ApiRetrofit.getInstance().getApiService().insertBankCard(newBankInfoBean), new BaseObserver<BaseModel<BankInfoBean>>() {
                @Override
                public void onSuccess(BaseModel<BankInfoBean> bankInfoBean) {
                    Util.showToast(WithdrawInfoActivity.this, "添加成功");
                    Intent intent = new Intent().putExtra("bankInfoBean", bankInfoBean.getData());
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onError(int errorCode, String msg) {
                    tvRight.setEnabled(true);
                    super.onError(errorCode, msg);
                }
            });
        } else {//更新银行卡信息
            bankInfoBean.setBankAddress(etArea.getText().toString().trim());
            bankInfoBean.setTrueName(etMan.getText().toString().trim());
            bankInfoBean.setBankName(etOpenBankName.getText().toString().trim());
            bankInfoBean.setBankType(tvBankName.getText().toString().trim());
            bankInfoBean.setCardNum(etBankAccount.getText().toString().trim());
            tvRight.setEnabled(false);
            addDisposable(ApiRetrofit.getInstance().getApiService().updateBankCard(bankInfoBean), new BaseObserver<BaseModel<BankInfoBean>>() {
                @Override
                public void onSuccess(BaseModel<BankInfoBean> bankInfoBean) {
                    Util.showToast(WithdrawInfoActivity.this, "更新成功");
                    Intent intent = new Intent().putExtra("bankInfoBean", bankInfoBean.getData());
                    setResult(RESULT_OK, intent);
                    finish();
                }

                @Override
                public void onError(int errorCode, String msg) {
                    tvRight.setEnabled(true);
                    super.onError(errorCode, msg);
                }
            });

        }


    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        currentFirstIndex = newVal;
    }
}
