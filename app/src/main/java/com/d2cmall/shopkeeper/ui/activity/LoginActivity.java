package com.d2cmall.shopkeeper.ui.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.d2cmall.shopkeeper.BuildConfig;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.SharePrefConstant;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.ClearEditText;
import com.d2cmall.shopkeeper.ui.view.MeasuredLayout;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.SharedPreUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/22.
 * Description : LoginActivity
 * 登录
 */

public class LoginActivity extends BaseActivity implements MeasuredLayout.OnKeyboardHideListener {
    @BindView(R.id._ll_welcome)
    LinearLayout LlWelcome;
    @BindView(R.id.et_account)
    ClearEditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @BindView(R.id.password_see)
    ImageView passwordSee;
    @BindView(R.id.ll_contianer)
    LinearLayout llContianer;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.password_line)
    View passwordLine;
    private boolean loginPassWordSee = false;
    private boolean isTranslationUp = false;
    private UserBean userBean;
    private boolean isExit=false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        tvName.setText("登录账号");
        if (!BuildConfig.DEBUG){
            ivBack.setVisibility(View.GONE);
        }
        ButterKnife.bind(this);
        boolean isFromeLoginOut = getIntent().getBooleanExtra("isFromeLoginOut", false);
        if(isFromeLoginOut){
            rlTitle.setVisibility(View.GONE);
        }
    }

    public View getContentView() {
        MeasuredLayout measuredLayout = new MeasuredLayout(this, null, R.layout.activity_login);
        measuredLayout.setOnKeyboardHideListener(this);
        return measuredLayout;
    }

    @Override
    public void doBusiness() {
        initWatcher();
    }

    private void initWatcher() {
        Sofia.with(this).statusBarDarkFont().invasionNavigationBar().statusBarBackground(Color.parseColor("#FFFFFF"));
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!StringUtil.isEmpty(etAccount.getText().toString().trim()) && !StringUtil.isEmpty(etPassword.getText().toString().trim())) {
                    btnLogin.setBackgroundResource(R.drawable.sp_round2_bg_blue);
                } else {
                    btnLogin.setBackgroundResource(R.drawable.sp_round2_bg_cbcbcb);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etAccount.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
    }


    @OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forget_password, R.id.password_see,R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //退出app
                exitApp();
                break;
            case R.id.btn_login:
                //登录
                requestLogin();
                break;
            case R.id.tv_register:
                //注册
                Intent intent1 = new Intent(this, RegisterActivity.class).putExtra("type", 0);
                startActivityForResult(intent1, Constants.RequestCode.REGISTER_ACCOUNT);
                break;
            case R.id.tv_forget_password:
                //忘记密码
                Intent intent = new Intent(this, RegisterActivity.class).putExtra("type", 1);
                if (!StringUtil.isEmpty(etAccount.getText().toString().trim())) {
                    intent.putExtra("phone", etAccount.getText().toString().trim());
                }
                startActivity(intent);
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

    private void exitApp() {
        setResult(RESULT_OK);
        finish();
    }

    private void requestLogin() {
        if (StringUtil.isEmpty(etAccount.getText().toString().trim())) {
            Toast.makeText(this, "请填写账号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtil.isEmpty(etPassword.getText().toString().trim())) {
            Toast.makeText(this, "请填写密码", Toast.LENGTH_SHORT).show();
            return;
        }
        ArrayMap<String, String> map = new ArrayMap<>();
        map.put("account", etAccount.getText().toString().trim());
        map.put("password", StringUtil.getMD5(etPassword.getText().toString().trim()));
        addDisposable(ApiRetrofit.getInstance().getApiService().login(map), new BaseObserver<BaseModel<UserBean>>() {
            @Override
            public void onSuccess(BaseModel<UserBean> bean) {
                SharedPreUtil.setString(SharePrefConstant.TOKEN, bean.getData().getLoginToken());
                if ("REPORT".equals(bean.getData().getRole())){
                    Session.getInstance().saveUserToFile(LoginActivity.this, bean.getData());
                    EventBus.getDefault().post(new Action(Constants.ActionType.CLEAR_ALL_ACTIVITY));
                    Intent intent=new Intent(LoginActivity.this,StatActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    if (bean.getData().getShopId() <= 0) {
                        userBean = bean.getData();
                        Intent intent = new Intent(LoginActivity.this, CreateBrandActivity.class);
                        startActivityForResult(intent, 100);
                    } else {
                        SharedPreUtil.setString(SharePrefConstant.TOKEN, bean.getData().getLoginToken());
                        Session.getInstance().setUser(userBean);
                        Session.getInstance().saveUserToFile(LoginActivity.this, bean.getData());
                        EventBus.getDefault().post(new Action(Constants.ActionType.LOGIN_OK));
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onKeyboardHide(boolean hide) {
        if (hide) {
            if (isTranslationUp) {
                isTranslationUp = false;
                startTranslateAnim(false);
            }
        } else {
            if (!isTranslationUp) {
                isTranslationUp = true;
                startTranslateAnim(true);
            }
        }
    }

    private void startTranslateAnim(boolean up) {
        int startY = 0;
        int endY = 0;
        if (up) {
            startY = ScreenUtil.dip2px(34);
            endY = ScreenUtil.dip2px(-168);
        } else {
            startY = ScreenUtil.dip2px(-168);
            endY = ScreenUtil.dip2px(34);
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(llContianer, "translationY", startY, endY);
        animator.setDuration(300);
        animator.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            //一种是登录->注册->创建店铺    另一种登录->创建店铺
            if (requestCode == 100 || requestCode == Constants.RequestCode.REGISTER_ACCOUNT) {
                long shopId = data.getLongExtra("shopId", 0);
                if (userBean == null) {
                    userBean = Session.getInstance().getUserFromFile(LoginActivity.this);
                }
                userBean.setShopId(shopId);
                Session.getInstance().setUser(userBean);
                Session.getInstance().saveUserToFile(LoginActivity.this, userBean);
                EventBus.getDefault().post(new Action(Constants.ActionType.LOGIN_OK));
                finish();
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {//未创建店铺,注册成功,后台已经使用户处于登录状态,调一下退出登录的接口
            boolean isLoginSuccess = data.getBooleanExtra("isLoginSuccess", false);
            if (isLoginSuccess) {
                loginOut();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loginOut() {
        addDisposable(ApiRetrofit.getInstance().getApiService().loginout(), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Session.getInstance().logout(LoginActivity.this);
                finish();
            }
        });
    }

    /**
     * 双击返回键退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
               exitApp();
            } else {
                Toast.makeText(this, "再按一次退出App", Toast.LENGTH_SHORT).show();
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit= false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
