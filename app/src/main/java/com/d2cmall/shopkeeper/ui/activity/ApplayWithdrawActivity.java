package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.model.BankInfoBean;
import com.d2cmall.shopkeeper.model.BankListBean;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.model.WithdrawBean;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/25.
 * Description : ApplayWithdrawActivity
 * 申请提现
 */

public class ApplayWithdrawActivity extends BaseActivity {
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.rl_withdraw_info)
    RelativeLayout rlWithdrawInfo;
    @BindView(R.id.et_apply_amount)
    EditText etApplyAmount;
    @BindView(R.id.tv_available_balance)
    TextView tvAvailableBalance;
    @BindView(R.id.tv_withdraw_all)
    TextView tvWithdrawAll;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.btn_withdraw)
    Button btnWithdraw;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    private UserBean user;
    private double validAmount;
    private BankInfoBean bankInfoBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_apply_withdraw;
    }

    @Override
    public void doBusiness() {
        initTitle();
    }

    private void initTitle() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        ShadowDrawable.setShadowDrawable(btnWithdraw, Color.parseColor("#8d92a3"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"),  ScreenUtil.dip2px(16), 0, 0);
        btnWithdraw.setEnabled(false);
        tvName.setText("提现");
        tvRight.setVisibility(View.GONE);
        user = Session.getInstance().getUserFromFile(this);
        if(user!=null){
            loadBrandInfo();
            loadBankList();
        }else{
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
    }

    private void loadBrandInfo() {

        addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(user.getShopId()), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBean) {
                tvAvailableBalance.setText("可提现余额:"+Util.getNumberFormat(shopBean.getData().getBalance()));
                validAmount = shopBean.getData().getBalance();
                setWatcher();
            }
        });
    }


    private void loadBankList() {
        ArrayMap<String, String> map = Util.buildListParam(0, 20);
        addDisposable(ApiRetrofit.getInstance().getApiService().getBankList(map), new BaseObserver<BaseModel<BankListBean>>() {
            @Override
            public void onSuccess(BaseModel<BankListBean> bankListBean) {
                if(bankListBean!=null && bankListBean.getData().getRecords()!=null && bankListBean.getData().getRecords().size()>0){
                    bankInfoBean = bankListBean.getData().getRecords().get(0);
                    if(bankInfoBean!=null){
                        String bankName = bankListBean.getData().getRecords().get(0).getBankType();
                        String bankNum = bankListBean.getData().getRecords().get(0).getCardNum();
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(bankName);
                        if(!StringUtil.isEmpty(bankNum) && bankNum.length()>4){
                            stringBuilder.append("(");
                            stringBuilder.append(bankNum.substring(bankNum.length()-4,bankNum.length()));
                            stringBuilder.append(")");
                        }
                        tvBankName.setText(stringBuilder.toString());
                    }
                }
            }
        });
    }

    private void setWatcher() {
        etApplyAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!StringUtil.isEmpty(etApplyAmount.getText().toString().trim())){
                    if (etApplyAmount.getText().toString().trim().equals(".")){
                        Util.showToast(ApplayWithdrawActivity.this,"请输入正确金额");
                        etApplyAmount.setText("");
                        return;
                    }
                    Double applyAmount = StringUtil.getDoubleFromText(etApplyAmount.getText().toString().trim());
                    if(applyAmount>validAmount){
                        etApplyAmount.setText(String.valueOf(validAmount));
                        etApplyAmount.setSelection(etApplyAmount.getText().length());
                        Util.showToast(ApplayWithdrawActivity.this,"输入金额大于可提现金额");
                    }
                    if (tvBankName.getText().toString().contains("银行")){
                        ShadowDrawable.setShadowDrawable(btnWithdraw, Color.parseColor("#4050D2"), ScreenUtil.dip2px(2),
                                Color.parseColor("#668d92a3"),  ScreenUtil.dip2px(16), 0, 0);
                        btnWithdraw.setEnabled(true);
                    }
                }else{
                    ShadowDrawable.setShadowDrawable(btnWithdraw, Color.parseColor("#8d92a3"), ScreenUtil.dip2px(2),
                            Color.parseColor("#11000000"),  ScreenUtil.dip2px(16), 0, 0);
                    btnWithdraw.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick({R.id.rl_withdraw_info, R.id.tv_withdraw_all, R.id.btn_withdraw, R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_withdraw_info:
                //提现信息
                if(Util.loginChecked(this,999)){
                    Intent intent = new Intent(this, WithdrawInfoActivity.class);
                    if(bankInfoBean!=null){
                        intent.putExtra("bankInfoBean",bankInfoBean);
                    }
                    startActivityForResult(intent, Constants.RequestCode.SELECT_BANK_CODE);
                }
                break;
            case R.id.tv_withdraw_all:
                //全部提现
                applyAll();
                break;
            case R.id.btn_withdraw:
                //申请提现
                applyWithdraw();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //提现记录
                startActivity(new Intent(this,WithdrawRecordActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==Constants.RequestCode.SELECT_BANK_CODE){
            BankInfoBean newBankInfoBean = (BankInfoBean) data.getSerializableExtra("bankInfoBean");
            if(newBankInfoBean!=null){
                bankInfoBean=newBankInfoBean;
            }
            String bankNum = newBankInfoBean.getCardNum();
            String bankName = newBankInfoBean.getBankType();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(bankName);
            if(!StringUtil.isEmpty(bankNum) && bankNum.length()>4){
                stringBuilder.append("(");
                stringBuilder.append(bankNum.substring(bankNum.length()-4,bankNum.length()));
                stringBuilder.append(")");
            }
            tvBankName.setText(stringBuilder.toString());
            if (!StringUtil.isEmpty(etApplyAmount.getText().toString())){
                ShadowDrawable.setShadowDrawable(btnWithdraw, Color.parseColor("#4050D2"), ScreenUtil.dip2px(2),
                        Color.parseColor("#668d92a3"),  ScreenUtil.dip2px(16), 0, 0);
                btnWithdraw.setEnabled(true);
            }
        }
    }

    private void applyAll() {
        etApplyAmount.setText(Util.getNumberFormat(validAmount));
    }

    private void applyWithdraw() {
        if(StringUtil.isEmpty(etApplyAmount.getText().toString().trim())){
            Util.showToast(this,"请输入提现金额");
            return;
        }
        if(StringUtil.getDoubleFromText(etApplyAmount.getText().toString().trim())<=0 || StringUtil.getDoubleFromText(etApplyAmount.getText().toString().trim())>validAmount){
            Util.showToast(this,"请输入正确的提现金额");
            return;
        }
        if(bankInfoBean==null){
            Util.showToast(this,"请填写完整的银行卡信息");
            return;
        }
        WithdrawBean withdrawBean = new WithdrawBean();
        withdrawBean.setAmount(StringUtil.getDoubleFromText(etApplyAmount.getText().toString().trim()));
        withdrawBean.setBankAddress(bankInfoBean.getBankAddress());
        withdrawBean.setBankName(bankInfoBean.getBankName());
        withdrawBean.setBankType(bankInfoBean.getBankType());
        withdrawBean.setCardNum(bankInfoBean.getCardNum());
        withdrawBean.setTrueName(bankInfoBean.getTrueName());
        btnWithdraw.setEnabled(false);
        addDisposable(ApiRetrofit.getInstance().getApiService().applyWithdraw(withdrawBean), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(ApplayWithdrawActivity.this,"申请提现成功,请耐心等待审核");
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int errorCode, String msg) {
                btnWithdraw.setEnabled(true);
                super.onError(errorCode, msg);
            }
        });
    }
}
