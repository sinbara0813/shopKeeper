package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.model.CouponBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.SelectTimePop;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.SingleSelectPop;
import com.d2cmall.shopkeeper.ui.view.TransparentPop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.KeyboardUtil;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/2/25.
 * 邮箱:hrb940258169@163.com
 * 新建和编辑优惠券
 */

public class AddCouponActivity extends BaseActivity implements SingleSelectPop.CallBack {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.promotion_name_tv)
    EditText promotionNameTv;
    @BindView(R.id.et_sustain)
    EditText etSustain;
    @BindView(R.id.tv_receive_date)
    TextView tvReceiveDate;
    @BindView(R.id.tv_coupon_setting)
    TextView tvCouponSetting;
    @BindView(R.id.ll_receive_start_date)
    LinearLayout llReceiveStartDate;
    @BindView(R.id.tv_receive_end_date)
    TextView tvReceiveEndDate;
    @BindView(R.id.ll_receive_end_date)
    LinearLayout llReceiveEndDate;
    @BindView(R.id.tv_use_start_date)
    TextView tvUseStartDate;
    @BindView(R.id.ll_use_start_date)
    LinearLayout llUseStartDate;
    @BindView(R.id.tv_use_end_date)
    TextView tvUseEndDate;
    @BindView(R.id.tv_scope)
    TextView tvScope;
    @BindView(R.id.ll_use_end_date)
    LinearLayout llUseEndDate;
    @BindView(R.id.ll_discount_setting)
    LinearLayout llDiscountSetting;
    @BindView(R.id.ll_product_scope)
    LinearLayout llProductScope;
    @BindView(R.id.ll_time_type)
    LinearLayout llTimeType;
    @BindView(R.id.tv_time_type)
    TextView tvTimeType;
    @BindView(R.id.ll_sustain)
    LinearLayout llSustain;
    @BindView(R.id.switch_view)
    Switch switchView;


    private String receiveStartTime;
    private Date receiveStartDate;
    private String receiveEndTime;
    private Date receiveEndDate;
    private String useStartTime;
    private Date useStartDate;
    private String useEndTime;
    private Date useEndDate;
    private int scopeType = -2;//0是全部商品,1是指定商品可用 -1,指定商品不可用
    private double discountAmount;
    private double thresholdAmount;
    private int limitNum;
    private int totalNum;
    private boolean hasSetConponDiscount = false;
    private String scopeTypeString;
    private UserBean user;
    private long couponId;
    private BaseModel<CouponBean> mCoupon;
    private boolean isSwitchViewChecked=true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_coupon;
    }

    @Override
    public void doBusiness() {
        tvName.setText("新增优惠券");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("下一步");
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        couponId = getIntent().getLongExtra("couponId", -1);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSwitchViewChecked=isChecked;
                changeRightTv();
            }
        });
        if (couponId > 0) {
            loadCouponInfo();
        }
    }

    private void changeRightTv() {
            if(scopeType==0){
                tvRight.setText("提交");
            }else{
                tvRight.setText("下一步");
            }
    }


    private void loadCouponInfo() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getCouponDetial(couponId), new BaseObserver<BaseModel<CouponBean>>() {
            @Override
            public void onSuccess(BaseModel<CouponBean> coupon) {
                mCoupon = coupon;
                promotionNameTv.setText(mCoupon.getData().getName());
                tvReceiveDate.setText(mCoupon.getData().getReceiveStartDate());
                tvReceiveEndDate.setText(mCoupon.getData().getReceiveEndDate());
                tvUseStartDate.setText(mCoupon.getData().getServiceStartDate());
                tvUseEndDate.setText(mCoupon.getData().getServiceEndDate());
                parseDate();
                tvScope.setText(mCoupon.getData().getTypeName());
                scopeType = mCoupon.getData().getType();
                scopeTypeString = mCoupon.getData().getTypeName();
                discountAmount = mCoupon.getData().getAmount();
                thresholdAmount = mCoupon.getData().getNeedAmount();
                totalNum = mCoupon.getData().getCirculation();
                limitNum = mCoupon.getData().getRestriction();
                mCoupon.getData().getType();
                tvCouponSetting.setText("已设置,点击修改");
                if (mCoupon.getData().getServiceSustain() != null && mCoupon.getData().getServiceSustain() > 0) {
                    tvTimeType.setText("领券当日计算");
                    etSustain.setText(String.valueOf(mCoupon.getData().getServiceSustain()));
                    llSustain.setVisibility(View.VISIBLE);
                    llUseStartDate.setVisibility(View.GONE);
                    llUseEndDate.setVisibility(View.GONE);
                } else {
                    tvTimeType.setText("固定日期范围");
                    llSustain.setVisibility(View.GONE);
                    llUseStartDate.setVisibility(View.VISIBLE);
                    llUseEndDate.setVisibility(View.VISIBLE);
                }
                if (mCoupon.getData().getType() == 0) {
                    tvRight.setText("完成");
                }
                if(mCoupon.getData().getStatus()==1){
                    switchView.setChecked(true);
                }else{
                    switchView.setChecked(false);
                }
            }

        });
    }

    private void parseDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(!StringUtil.isEmpty(mCoupon.getData().getServiceStartDate())){
                useStartDate = simpleDateFormat.parse(mCoupon.getData().getServiceStartDate());
            }
            if(!StringUtil.isEmpty(mCoupon.getData().getServiceEndDate())){
                useEndDate = simpleDateFormat.parse(mCoupon.getData().getServiceEndDate());
            }
            receiveStartDate = simpleDateFormat.parse(mCoupon.getData().getReceiveStartDate());
            receiveEndDate = simpleDateFormat.parse(mCoupon.getData().getReceiveEndDate());

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    @OnClick({R.id.iv_back, R.id.tv_right, R.id.ll_receive_start_date, R.id.ll_receive_end_date, R.id.ll_use_start_date, R.id.ll_use_end_date, R.id.ll_discount_setting, R.id.ll_product_scope, R.id.ll_time_type})
    public void onViewClicked(View view) {
        KeyboardUtil.hideKeyboard(view);
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                nextStep();
                break;
            case R.id.ll_receive_start_date:
                //开始领取时间
                SelectTimePop selectStartTimePop = new SelectTimePop(this);
                selectStartTimePop.setDismissCallBack(new TransparentPop.DismissListener() {
                    @Override
                    public void dismissStart() {
                        receiveStartTime = selectStartTimePop.getDateStr2();
                        receiveStartDate = getSelectDate(selectStartTimePop.currentYear, selectStartTimePop.month, selectStartTimePop.day, selectStartTimePop.hour, selectStartTimePop.minute);
                        tvReceiveDate.setText(receiveStartTime);
                    }

                    @Override
                    public void dismissEnd() {

                    }
                });
                selectStartTimePop.show(getWindow().getDecorView());
                break;
            case R.id.ll_receive_end_date:
                //领取结束时间
                SelectTimePop selectEndTimePop = new SelectTimePop(this);
                selectEndTimePop.setDismissCallBack(new TransparentPop.DismissListener() {
                    @Override
                    public void dismissStart() {
                        receiveEndTime = selectEndTimePop.getDateStr2();
                        receiveEndDate = getSelectDate(selectEndTimePop.currentYear, selectEndTimePop.month, selectEndTimePop.day, selectEndTimePop.hour, selectEndTimePop.minute);
                        if (receiveStartDate != null && receiveEndDate != null && receiveEndDate.getTime() <= receiveStartDate.getTime()) {
                            Util.showToast(AddCouponActivity.this, "领用结束时间应大于领用开始时间");
                        }
                        tvReceiveEndDate.setText(receiveEndTime);
                    }

                    @Override
                    public void dismissEnd() {

                    }
                });
                selectEndTimePop.show(getWindow().getDecorView());
                break;
            case R.id.ll_use_start_date:
                //试用开始时间
                SelectTimePop useStartTimePop = new SelectTimePop(this);
                useStartTimePop.setDismissCallBack(new TransparentPop.DismissListener() {
                    @Override
                    public void dismissStart() {
                        useStartTime = useStartTimePop.getDateStr2();
                        useStartDate = getSelectDate(useStartTimePop.currentYear, useStartTimePop.month, useStartTimePop.day, useStartTimePop.hour, useStartTimePop.minute);
                        if (useStartDate != null && receiveStartDate != null && useStartDate.getTime() < receiveStartDate.getTime()) {
                            Util.showToast(AddCouponActivity.this, "使用开始时间应大于领用开始时间");
                        }
                        tvUseStartDate.setText(useStartTime);
                    }

                    @Override
                    public void dismissEnd() {

                    }
                });
                useStartTimePop.show(getWindow().getDecorView());
                break;
            case R.id.ll_use_end_date:
                //使用结束时间
                SelectTimePop useEndTimePop = new SelectTimePop(this);
                useEndTimePop.setDismissCallBack(new TransparentPop.DismissListener() {
                    @Override
                    public void dismissStart() {
                        useEndTime = useEndTimePop.getDateStr2();
                        useEndDate = getSelectDate(useEndTimePop.currentYear, useEndTimePop.month, useEndTimePop.day, useEndTimePop.hour, useEndTimePop.minute);
                        if (useStartDate != null && useEndDate != null && useEndDate.getTime() <= useStartDate.getTime()) {
                            Util.showToast(AddCouponActivity.this, "使用结束时间应大于使用开始时间");
                        }
                        tvUseEndDate.setText(useEndTime);
                    }

                    @Override
                    public void dismissEnd() {

                    }
                });
                useEndTimePop.show(getWindow().getDecorView());
                break;
            case R.id.ll_discount_setting:
                //优惠设置
                Intent intent = new Intent(this, CouponSettingActivity.class);
                if (mCoupon != null) {
                    intent.putExtra("discountAmount", mCoupon.getData().getAmount());
                    intent.putExtra("thresholdAmount", mCoupon.getData().getNeedAmount());
                    intent.putExtra("limitNum", mCoupon.getData().getRestriction());
                    intent.putExtra("totalNum", mCoupon.getData().getCirculation());
                } else {
                    intent.putExtra("discountAmount", discountAmount);
                    intent.putExtra("thresholdAmount", thresholdAmount);
                    intent.putExtra("limitNum", limitNum);
                    intent.putExtra("totalNum", totalNum);
                }
                startActivityForResult(intent, Constants.RequestCode.COUPON_SETTING);
                break;
            case R.id.ll_product_scope:
                //商品范围
                selectProductScopePop();
                break;
            case R.id.ll_time_type:
                //时间类型
                selectTimeTypePop();
                break;
        }
    }

    private void selectTimeTypePop() {
        String[] titles = getResources().getStringArray(R.array.label_coupon_time_type);
        SingleSelectPop singleSelectPop = new SingleSelectPop(this, titles);
        singleSelectPop.show(getWindow().getDecorView(), llTimeType);
        singleSelectPop.setCallBack(this);
    }

    private Date getSelectDate(int currentYear, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, month - 1, day, hour, minute);
        Date time = calendar.getTime();
        return time;
    }


    private void selectProductScopePop() {
        String[] titles = getResources().getStringArray(R.array.label_coupon_product_scope);
        SingleSelectPop singleSelectPop = new SingleSelectPop(this, titles);
        singleSelectPop.show(getWindow().getDecorView(), llProductScope);
        singleSelectPop.setCallBack(this);
    }

    private void nextStep() {
        if (!hasSetConponDiscount && mCoupon == null) {
            Util.showToast(this, "请先设置优惠信息");
            return;
        }
        if (StringUtil.isEmpty(promotionNameTv.getText().toString().trim())) {
            Util.showToast(this, "请输入优惠券名称");
            return;
        }
        if (receiveStartDate == null) {
            Util.showToast(this, "请设置领取开始时间");
            return;
        }
        if (!StringUtil.isEmpty(tvTimeType.getText().toString().trim()) && "固定日期范围".equals(tvTimeType.getText().toString().trim())) {
            if (useEndDate == null) {
                Util.showToast(this, "请设置使用结束时间");
                return;
            }
            if (useStartDate == null) {
                Util.showToast(this, "请设置使用开始时间");
                return;
            }
            if (receiveStartDate == null) {
                Util.showToast(this, "请设置领取开始时间");
                return;
            }
            if (receiveEndDate == null) {
                Util.showToast(this, "请设置领取结束时间");
                return;
            }

            if(receiveEndDate!=null && receiveEndDate.getTime()<System.currentTimeMillis()){
                Util.showToast(this,"优惠券领取结束时间不得小于当前时间");
                return;
            }

            if (receiveStartDate != null && receiveEndDate != null && receiveEndDate.getTime() <= receiveStartDate.getTime()) {
                Util.showToast(AddCouponActivity.this, "领取结束时间应大于领取开始时间");
                return;
            }
            if (useStartDate != null && receiveStartDate != null && useStartDate.getTime() < receiveStartDate.getTime()) {
                Util.showToast(AddCouponActivity.this, "使用开始时间应大于领取开始时间");
                return;
            }
            if (useStartDate != null && useEndDate != null && useEndDate.getTime() <= useStartDate.getTime()) {
                Util.showToast(AddCouponActivity.this, "使用结束时间应大于使用开始时间");
                return;
            }
            if (receiveEndDate != null && useEndDate != null && useEndDate.getTime() <= receiveEndDate.getTime()) {
                Util.showToast(AddCouponActivity.this, "使用结束时间应大于领取结束时间");
                return;
            }
        } else {
            if (StringUtil.isEmpty(etSustain.getText().toString().trim()) || Integer.valueOf(etSustain.getText().toString().trim()) <= 0) {
                Util.showToast(AddCouponActivity.this, "请填写正确的有效时长");
                return;
            }
        }

        if (scopeType == -2) {
            Util.showToast(AddCouponActivity.this, "请设置优惠券使用范围");
            return;
        }
        if (mCoupon == null) {
            creatCoupon();
        } else {
            upDateCoupon();
        }
    }

    private void upDateCoupon() {
        user = Session.getInstance().getUserFromFile(this);
        if (user == null) {
            return;
        }
        if (user.getShopId() <= 0) {
            Util.showToast(this, "请先创建店铺");
            return;
        }
        tvRight.setEnabled(false);
        mCoupon.getData().setAmount(discountAmount);
        mCoupon.getData().setNeedAmount(thresholdAmount);
        mCoupon.getData().setName(promotionNameTv.getText().toString().trim());
        mCoupon.getData().setReceiveEndDate(getTimeString(receiveEndDate));
        mCoupon.getData().setReceiveStartDate(getTimeString(receiveStartDate));
        if(useStartDate!=null){
            mCoupon.getData().setServiceStartDate(getTimeString(useStartDate));
        }
        if(useEndDate!=null){
            mCoupon.getData().setServiceEndDate(getTimeString(useEndDate));
        }
        mCoupon.getData().setShopId(user.getShopId());
        mCoupon.getData().setType(scopeType);
        if(isSwitchViewChecked){
            mCoupon.getData().setStatus(1);
        }else{
            mCoupon.getData().setStatus(0);
        }
        mCoupon.getData().setCirculation(totalNum);
        mCoupon.getData().setRestriction(limitNum);
        if (!StringUtil.isEmpty(etSustain.getText().toString().trim()) && Integer.valueOf(etSustain.getText().toString().trim()) > 0) {
            mCoupon.getData().setServiceSustain(Integer.valueOf(etSustain.getText().toString().trim()));
        }
        addDisposable(ApiRetrofit.getInstance().getApiService().upDateCoupon(mCoupon.getData()), new BaseObserver<BaseModel<CouponBean>>() {
            @Override
            public void onSuccess(BaseModel<CouponBean> couponBean) {
                tvRight.setEnabled(true);
                if (scopeType == 0 || !isSwitchViewChecked) {//全场券或下架
                    Util.showToast(AddCouponActivity.this, "修改成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Util.showToast(AddCouponActivity.this, "修改成功,请选择绑定商品");
                    Intent intent = new Intent(AddCouponActivity.this, AssignProductActivity.class);
                    intent.putExtra("scopeTypeString", scopeTypeString);
                    intent.putExtra("coupon", mCoupon.getData());
                    intent.putExtra("isEditCoupon", true);
                    startActivityForResult(intent, Constants.RequestCode.COUPON_BIND_PRODUCT);
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                tvRight.setEnabled(true);
                super.onError(errorCode, msg);
            }
        });


    }

    private String getTimeString(Date timeDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = df.format(timeDate);
        return timeString;
    }

    @Override
    public void callback(View trigger, int index, String value) {
        if (trigger == llProductScope) {

            if(index==2){
                scopeType=-1;
            }else{
                scopeType = index;
            }
            if (scopeType == 0) {
                tvRight.setText("提交");
            } else {
                tvRight.setText("下一步");
            }
            scopeTypeString = value;
            tvScope.setText(scopeTypeString);
        } else if (trigger == llTimeType) {
            if (index == 0) {
                tvTimeType.setText("固定日期范围");
                llSustain.setVisibility(View.GONE);
                llUseStartDate.setVisibility(View.VISIBLE);
                llUseEndDate.setVisibility(View.VISIBLE);
            } else {
                tvTimeType.setText("领券当日计算");
                llSustain.setVisibility(View.VISIBLE);
                llUseStartDate.setVisibility(View.GONE);
                llUseEndDate.setVisibility(View.GONE);
            }
        }

    }

    private void creatCoupon() {
        user = Session.getInstance().getUserFromFile(this);
        if (user == null) {
            return;
        }
        if (user.getShopId() <= 0) {
            Util.showToast(this, "请先创建店铺");
            return;
        }
        CouponBean couponBean = new CouponBean();
        couponBean.setAmount(discountAmount);
        couponBean.setNeedAmount(thresholdAmount);
        couponBean.setName(promotionNameTv.getText().toString().trim());
        couponBean.setReceiveEndDate(getTimeString(receiveEndDate));
        couponBean.setReceiveStartDate(getTimeString(receiveStartDate));
        if (useStartDate != null && useEndDate != null) {
            couponBean.setServiceStartDate(getTimeString(useStartDate));
            couponBean.setServiceEndDate(getTimeString(useEndDate));
        }
        couponBean.setShopId(user.getShopId());
        couponBean.setType(scopeType == 2 ? -1 : scopeType);
        if(isSwitchViewChecked){
            couponBean.setStatus(1);
        }else{
            couponBean.setStatus(0);
        }
        couponBean.setCrowd("0");
        couponBean.setCirculation(totalNum);
        couponBean.setRestriction(limitNum);
        if (!StringUtil.isEmpty(etSustain.getText().toString().trim()) && Integer.valueOf(etSustain.getText().toString().trim()) > 0) {
            couponBean.setServiceSustain(Integer.valueOf(etSustain.getText().toString().trim()));
        }
        if (scopeType == 0 || !isSwitchViewChecked) {//全场券或下架
            tvRight.setEnabled(false);
            addDisposable(ApiRetrofit.getInstance().getApiService().insertCoupon(couponBean), new BaseObserver<BaseModel<CouponBean>>() {
                @Override
                public void onSuccess(BaseModel<CouponBean> coupon) {
                    tvRight.setEnabled(true);
                    Util.showToast(AddCouponActivity.this, "创建成功");
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onError(int errorCode, String msg) {
                    tvRight.setEnabled(true);
                    super.onError(errorCode, msg);
                }
            });
        } else {
            Intent intent = new Intent(AddCouponActivity.this, AssignProductActivity.class);
            intent.putExtra("scopeTypeString", scopeTypeString);
            intent.putExtra("coupon", couponBean);
            startActivityForResult(intent, Constants.RequestCode.COUPON_BIND_PRODUCT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null && requestCode == Constants.RequestCode.COUPON_SETTING) {
                //优惠金额
                discountAmount = data.getDoubleExtra("discountAmount", 0);
                //优惠门槛
                thresholdAmount = data.getDoubleExtra("thresholdAmount", 0);
                //每人限领
                limitNum = data.getIntExtra("limitNum", 0);
                //总张数
                totalNum = data.getIntExtra("totalNum", 0);
                if (mCoupon != null) {
                    mCoupon.getData().setAmount(discountAmount);
                    mCoupon.getData().setNeedAmount(thresholdAmount);
                    mCoupon.getData().setCirculation(totalNum);
                    mCoupon.getData().setRestriction(limitNum);
                }
                hasSetConponDiscount = true;
                tvCouponSetting.setText("已设置,点击修改");
            } else if (requestCode == Constants.RequestCode.COUPON_BIND_PRODUCT) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

}
