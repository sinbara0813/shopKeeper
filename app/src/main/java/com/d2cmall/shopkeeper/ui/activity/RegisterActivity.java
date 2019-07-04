package com.d2cmall.shopkeeper.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.SharePrefConstant;
import com.d2cmall.shopkeeper.model.CountryBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.ClearEditText;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.SharedPreUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/22.
 * Description : RegisterActivity
 * 注册&找回密码
 */

public class RegisterActivity extends BaseActivity implements BaseView{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.account_line)
    View accountLine;
    @BindView(R.id.tv_nation_code)
    TextView tvNationCode;
    @BindView(R.id.et_bind_phone_number)
    ClearEditText etBindPhoneNumber;
    @BindView(R.id.rl_account)
    RelativeLayout rlAccount;
    @BindView(R.id.security_line)
    View securityLine;
    @BindView(R.id.et_bind_security_code)
    ClearEditText etBindSecurityCode;
    @BindView(R.id.register_security_tv)
    TextView registerSecurityTv;
    @BindView(R.id.register_security_rl)
    RelativeLayout registerSecurityRl;
    @BindView(R.id.password_line)
    View passwordLine;
    @BindView(R.id.password_see)
    ImageView passwordSee;
    @BindView(R.id.et_password)
    ClearEditText etPassword;
    @BindView(R.id.password_rl)
    RelativeLayout passwordRl;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_regagreement)
    TextView tvRegagreement;
    @BindView(R.id.area_line)
    View areaLine;

    private boolean isLoginSuccess=false;
    private boolean isSendCode=false;

    private CountryBean countryBean;
    private boolean loginPassWordSee;
    private TimeDown timeDown;
    private int type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void doBusiness() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        type = getIntent().getIntExtra("type", 0);
        initDefaultCountry();
        if(type ==0){//注册
            tvName.setText("注册账号");
            tvRegagreement.setVisibility(View.VISIBLE);
            tvRegagreement.setText(Html.fromHtml("  注册视为您已同意<b>《赋能电铺商家使用协议》</b>"));
        }else{//找回密码
            String phone = getIntent().getStringExtra("phone");
            if(!StringUtil.isEmpty(phone)){
                etBindPhoneNumber.setText(phone);
                registerSecurityTv.setBackgroundResource(R.drawable.sp_round2_bg_blue);
            }
            tvNationCode.setVisibility(View.GONE);
            areaLine.setVisibility(View.GONE);
            tvName.setText("忘记密码");
            btnRegister.setText("完成");
        }
        initWatcher();
    }

    private void initWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!StringUtil.isEmpty(etBindPhoneNumber.getText().toString().trim())&&!StringUtil.isEmpty(etPassword.getText().toString().trim())
                        &&!StringUtil.isEmpty(etBindSecurityCode.getText().toString().trim())){
                    btnRegister.setBackgroundResource(R.drawable.sp_round2_bg_blue);
                }else{
                    btnRegister.setBackgroundResource(R.drawable.sp_round2_bg_cbcbcb);
                }
                if(!StringUtil.isEmpty(etBindPhoneNumber.getText().toString().trim()) && !isSendCode){
                    registerSecurityTv.setBackgroundResource(R.drawable.sp_round2_bg_blue);
                }else{
                    registerSecurityTv.setBackgroundResource(R.drawable.sp_round2_bg_cbcbcb);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etBindPhoneNumber.addTextChangedListener(textWatcher);
        etBindSecurityCode.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);

    }
    private void initDefaultCountry() {
        String jsonStr = Util.readStreamToString(getResources().openRawResource(R.raw.country));
        Gson gson = new Gson();
        List<CountryBean> countryBeanList = gson.fromJson(jsonStr, new TypeToken<List<CountryBean>>() {
        }.getType());
        countryBean = countryBeanList.get(0);
        if (tvNationCode != null) {
            tvNationCode.setText(getString(R.string.label_plus, countryBean.getMobileCode()));
        }
    }

    @OnClick({R.id.iv_back, R.id.register_security_tv, R.id.btn_register, R.id.tv_regagreement, R.id.password_see,R.id.tv_nation_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.register_security_tv:
                //获取验证码
                sendCode();
                break;
            case R.id.btn_register:
                //注册
                if(StringUtil.isEmpty(etBindPhoneNumber.getText().toString().trim())){
                    Toast.makeText(this,"请填写手机号",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isEmpty(etBindSecurityCode.getText().toString().trim())){
                    Toast.makeText(this,"请填写验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isEmpty(etPassword.getText().toString().trim()) || etPassword.getText().toString().trim().length()<6||etPassword.getText().toString().trim().length()>20){
                    Toast.makeText(this,"请输入正确密码位数",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(type==0){
                    requestRegister();
                }else{
                    resetPassword();
                }
                break;
            case R.id.tv_regagreement:
                //协议
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("url","https://s.fune.store/doc/excise.html");
                intent.putExtra("title","用户协议");
                startActivity(intent);
                break;
            case  R.id.tv_nation_code:
                //地区
//                Intent intent = new Intent(this, CountryCodeActivity.class);
//                startActivityForResult(intent, Constants.RequestCode.COUNTRY_CODE);
                break;
            case R.id.password_see:
                //密码可见
                if (!loginPassWordSee) {
                    passwordSee.setImageResource(R.mipmap.icon_login_eyezheng);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    passwordSee.setImageResource(R.mipmap.icon_login_eyeclose);
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                loginPassWordSee = !loginPassWordSee;
                CharSequence text = etPassword.getText();
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }
                break;
        }
    }

    private void resetPassword() {
        btnRegister.setEnabled(false);
        ArrayMap<String,String> map=new ArrayMap<>();
        map.put("account",etBindPhoneNumber.getText().toString().trim());
        map.put("password",StringUtil.getMD5(etPassword.getText().toString().trim()));
        map.put("code",etBindSecurityCode.getText().toString().trim());
        addDisposable(ApiRetrofit.getInstance().getApiService().resetPassword(map), new BaseObserver<BaseModel<UserBean>>() {
            @Override
            public void onSuccess(BaseModel<UserBean> bean) {
                Toast.makeText(RegisterActivity.this,"密码重置成功",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(int errorCode, String msg) {
                btnRegister.setEnabled(true);
                super.onError(errorCode, msg);
            }
        });
    }

    private void sendCode() {
        final String phoneNumber = etBindPhoneNumber.getText().toString().trim();
        if (StringUtil.isEmpty(phoneNumber)) {
            Util.showToast(this, R.string.msg_phone_empty);
            return;
        }
        if (countryBean.getMobileCode().equals("86") && phoneNumber.length() != 11) {
            Util.showToast(this, R.string.msg_phone_error);
            return;
        }

        registerSecurityTv.setEnabled(false);
        addDisposable(ApiRetrofit.getInstance().getApiService().getSecurityCode(Util.buildSecurityParam(phoneNumber)), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                timeDown = new TimeDown(60 * 1000, 1000);
                timeDown.tv = registerSecurityTv;
                timeDown.start();
                isSendCode=true;
                registerSecurityTv.setBackgroundResource(R.drawable.sp_round2_bg_cbcbcb);
                registerSecurityTv.setEnabled(false);
                Util.showToast(RegisterActivity.this,"获取验证码成功");
            }

            @Override
            public void onError(int errorCode, String msg) {
                registerSecurityTv.setEnabled(true);
                super.onError(errorCode, msg);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.RequestCode.COUNTRY_CODE:
                    countryBean = (CountryBean) data.getSerializableExtra("country");
                    tvNationCode.setText(getString(R.string.label_plus, countryBean.getMobileCode()));
                    break;
                case Constants.RequestCode.CREAT_BRAND:
                    setResult(RESULT_OK,data);
                    finish();
                    break;
            }
        }else if(resultCode==RESULT_CANCELED){//未创建店铺
            setResult(RESULT_OK);
            if(data==null){
                data=new Intent();
            }
            data.putExtra("isLoginSuccess",isLoginSuccess);
            finish();
        }
    }



    private void requestRegister() {
        btnRegister.setEnabled(false);
        ArrayMap<String,String> map=new ArrayMap<>();
        map.put("account",etBindPhoneNumber.getText().toString().trim());
        map.put("password",StringUtil.getMD5(etPassword.getText().toString().trim()));
        map.put("code",etBindSecurityCode.getText().toString().trim());
        addDisposable(ApiRetrofit.getInstance().getApiService().register(map), new BaseObserver<BaseModel<UserBean>>() {
            @Override
            public void onSuccess(BaseModel<UserBean> bean) {
                btnRegister.setEnabled(true);
                isLoginSuccess=true;
                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                SharedPreUtil.setString(SharePrefConstant.TOKEN,bean.getData().getLoginToken());
                Session.getInstance().saveUserToFile(RegisterActivity.this,bean.getData());
                Intent intent = new Intent(RegisterActivity.this, CreateBrandActivity.class);
                startActivityForResult(intent,Constants.RequestCode.CREAT_BRAND);
            }

            @Override
            public void onError(int errorCode, String msg) {
                btnRegister.setEnabled(true);
                super.onError(errorCode, msg);
            }
        });
    }
    @Override
    protected void onDestroy() {
        if (timeDown != null) {
            timeDown.cancel();
            timeDown = null;
        }
        super.onDestroy();
    }

    @Override
    public void requestStart() {

    }

    @Override
    public void requestFinish() {

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public class TimeDown extends CountDownTimer {

        public TextView tv;

        public TimeDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            if (!registerSecurityTv.isEnabled()) {
                isSendCode=false;
                registerSecurityTv.setEnabled(true);
                registerSecurityTv.setBackgroundResource(R.drawable.sp_round2_bg_blue);
                registerSecurityTv.setText(R.string.label_get_code);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int secUntilFinished = (int) (millisUntilFinished / 1000);
            if (!isFinishing()) {
                if (tv != null) {
                    if (tv == registerSecurityTv) {
                        tv.setText(getString(R.string.label_retry_code,
                                secUntilFinished > 9 ? secUntilFinished
                                        : ("0" + secUntilFinished)));
                    }
                }
            }
        }
    }
}
