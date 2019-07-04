package com.d2cmall.shopkeeper.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.util.ArrayMap;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BaseView;
import com.d2cmall.shopkeeper.model.StaffListBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/28.
 * Description : StaffEditActivity
 * 员工编辑界面
 */

public class StaffEditActivity extends BaseActivity implements BaseView {
    @BindView(R.id.et_role)
    EditText etRole;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_add_time)
    TextView tvAddTime;
    @BindView(R.id.et_remarks)
    EditText etRemarks;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.switch_view)
    Switch switchView;
    @BindView(R.id.ll_add_time)
    LinearLayout llAddTime;
    @BindView(R.id.ll_password)
    LinearLayout llPassword;



    private boolean isSwitchViewChecked;
    private String action;
    private long staffId;
    private BaseModel<StaffListBean.RecordsBean>  mStaffBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_staff_edit;
    }

    @Override
    public void doBusiness() {
        init();
    }

    private void init() {
        //add是添加  ,edit是编辑
        action = getIntent().getStringExtra("action");
        tvName.setText("员工管理");
        staffId = getIntent().getLongExtra("staffId", 0);
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("提交");
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSwitchViewChecked=isChecked;
            }
        });
        if(staffId>0){
            llPassword.setVisibility(View.GONE);
            loadStaffInfo();
        }else{
            llAddTime.setVisibility(View.GONE);
            etRole.setText("店员");
            etRole.setEnabled(false);
        }
    }

    private void loadStaffInfo() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getStaffInfo(staffId), new BaseObserver<BaseModel<StaffListBean.RecordsBean>>() {
            @Override
            public void onSuccess(BaseModel<StaffListBean.RecordsBean> saffBean) {
                mStaffBean = saffBean;
                if(!StringUtil.isEmpty(saffBean.getData().getAccount())){
                    etPhone.setText(saffBean.getData().getAccount());
                }
                if(!StringUtil.isEmpty(saffBean.getData().getRoleName())){
                    etRole.setText(saffBean.getData().getRoleName());
                }
//                if(!StringUtil.isEmpty(saffBean.getData().getPassword())){
//                    etPassword.setText(saffBean.getData().getPassword());
//                }
                if(!StringUtil.isEmpty(saffBean.getData().getRemark())){
                    etRemarks.setText(saffBean.getData().getRemark());
                }
                tvAddTime.setText(saffBean.getData().getCreateDate().substring(0,11));
                if(saffBean.getData().getStatus()==1){
                    switchView.setChecked(true);
                }else{
                    switchView.setChecked(false);
                }
                etPassword.setEnabled(false);
                etPhone.setEnabled(false);
                etRole.setEnabled(false);
            }
        });
    }


    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                boolean result = checkContent();
                if(result){
                    break;
                }
                if("edit".equals(action)){//编辑
                    updateStaffInfo();
                }else{ //添加
                    insertStaff();
                }
                break;
        }
    }

    private boolean checkContent() {
        if(staffId==0 && StringUtil.isEmpty(etPassword.getText().toString().trim())){
            Util.showToast(this,"请填写密码");
            return true;
        }
        if(StringUtil.isEmpty(etPhone.getText().toString().trim())){
            Util.showToast(this,"请填写手机号");
            return true;
        }
        if(StringUtil.isEmpty(etRole.getText().toString().trim())){
            Util.showToast(this,"请填写角色");
            return true;
        }
        return false;
    }

    private void updateStaffInfo() {

            if(staffId<=0){
                return;
            }
            checkContent();
        ArrayMap<String,String> map = new ArrayMap<>();
            map.put("id",String.valueOf(staffId));
            map.put("status",String.valueOf(switchView.isChecked()?1:0));
            addDisposable(ApiRetrofit.getInstance().getApiService().updatePersonInfo(map), new BaseObserver<BaseModel<UserBean>>(this) {
                @Override
                public void onSuccess(BaseModel<UserBean> bean) {
                    Util.showToast(StaffEditActivity.this,"修改成功");
                    finish();
                }
            });

    }

    private void insertStaff() {
        checkContent();
        StaffListBean.RecordsBean staffBean = new StaffListBean.RecordsBean();
        staffBean.setAccount(etPhone.getText().toString().trim());
        staffBean.setRole(etRole.getText().toString().trim());
        staffBean.setPassword(Util.getMD5(etPassword.getText().toString().trim()));
        staffBean.setStatus(switchView.isChecked()?1:0);
        addDisposable(ApiRetrofit.getInstance().getApiService().insertStaff(staffBean), new BaseObserver<BaseModel<StaffListBean.RecordsBean>>(this) {
            @Override
            public void onSuccess(BaseModel<StaffListBean.RecordsBean> staffBean) {
                if(staffBean!=null){
                    setResult(RESULT_OK);
                    Util.showToast(StaffEditActivity.this,"添加成功");
                    finish();
                }
            }
        });

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
}
