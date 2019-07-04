package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
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
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.view.SelectTimePop;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.TransparentPop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.KeyboardUtil;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/3/4.
 * Description : AddCollageProductActivity
 * 添加拼团商品
 */

public class AddCollageProductActivity extends BaseActivity {
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.ll_start_time)
    LinearLayout llStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.ll_end_time)
    LinearLayout llEndTime;
    @BindView(R.id.et_collage_success_time)
    EditText etCollageSuccessTime;
    @BindView(R.id.et_add_time)
    EditText etAddTime;
    @BindView(R.id.ll_select_product)
    LinearLayout llSelectProduct;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_action)
    TextView tvAction;
    @BindView(R.id.tv_selected_productName)
    TextView tvSelectedProductName;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.switch_view)
    Switch switchView;

    private String startTime;
    private String endTime;
    private Date startDate;
    private Date endDate;
    private double newPromotionPrice;
    private long newProductId;
    private ProductBean productBean;
    private long editProductId;
    private boolean isSwitchViewChecked=true;
    private int promotionType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_collage_product;
    }

    @Override
    public void doBusiness() {
        init();

    }

    private void init() {
        tvName.setText("添加拼团商品");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("提交");
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        editProductId = getIntent().getLongExtra("productId", 0);
        promotionType = getIntent().getIntExtra("promotionType", -2);
        if(promotionType==1 || promotionType==-1){
            tvRight.setVisibility(View.GONE);
        }
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSwitchViewChecked=isChecked;
            }
        });
        if(editProductId>0){
            loadProductInfo(editProductId);
            tvAction.setText("编辑商品");
        }
    }

    private void loadProductInfo(Long productId) {
        addDisposable(ApiRetrofit.getInstance().getApiService().getProduct(productId),new BaseObserver<BaseModel<ProductBean>>(){
            @Override
            public void onSuccess(BaseModel<ProductBean> o) {
                productBean=o.getData();
                if(editProductId>0){
                    etAddTime.setText(String.valueOf(productBean.getCrowdGroupNum()));
                    etCollageSuccessTime.setText(String.valueOf(productBean.getCrowdGroupTime()));
                    tvStartTime.setText(productBean.getCrowdStartDate());
                    tvEndTime.setText(productBean.getCrowdEndDate());
                    if(productBean.getStatus()==1){
                        switchView.setChecked(true);
                        isSwitchViewChecked=true;
                    }else{
                        switchView.setChecked(false);
                        isSwitchViewChecked=false;
                    }
                    tvSelectedProductName.setText(productBean.getName());
                    startDate = parseDate(productBean.getCrowdStartDate());
                    endDate = parseDate(productBean.getCrowdEndDate());
                }
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
            }
        });
    }

    private Date parseDate(String  dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if(!StringUtil.isEmpty(dateString)){
                return simpleDateFormat.parse(dateString);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    @OnClick({R.id.ll_start_time, R.id.tv_end_time, R.id.ll_select_product, R.id.iv_back,R.id.tv_right})
    public void onViewClicked(View view) {
        KeyboardUtil.hideKeyboard(view);
        switch (view.getId()) {
            case R.id.ll_start_time:
                //开始时间
                SelectTimePop selectStartTimePop = new SelectTimePop(this);
                selectStartTimePop.setDismissCallBack(new TransparentPop.DismissListener() {
                    @Override
                    public void dismissStart() {
                        startTime = selectStartTimePop.getDateStr2();
                        startDate = getSelectDate(selectStartTimePop.currentYear, selectStartTimePop.month, selectStartTimePop.day, selectStartTimePop.hour, selectStartTimePop.minute);
                        tvStartTime.setText(startTime);
                    }

                    @Override
                    public void dismissEnd() {

                    }
                });
                selectStartTimePop.show(getWindow().getDecorView());
                break;
            case R.id.tv_end_time:
                //结束时间
                SelectTimePop selectEndTimePop = new SelectTimePop(this);
                selectEndTimePop.setDismissCallBack(new TransparentPop.DismissListener() {
                    @Override
                    public void dismissStart() {
                        endTime = selectEndTimePop.getDateStr2();
                        endDate = getSelectDate(selectEndTimePop.currentYear, selectEndTimePop.month, selectEndTimePop.day, selectEndTimePop.hour, selectEndTimePop.minute);
                        if(startDate!=null && endDate!=null && endDate.getTime()<=startDate.getTime()){
                            Util.showToast(AddCollageProductActivity.this,"结束时间应大于开始时间");
                        }
                        tvEndTime.setText(endTime);
                    }

                    @Override
                    public void dismissEnd() {

                    }
                });
                selectEndTimePop.show(getWindow().getDecorView());
                break;
            case R.id.ll_select_product:
                //选择商品
                if(editProductId>0){
                    Intent intent = new Intent(this, SelectProductConfirmActivity.class);
                    intent.putExtra("productId", editProductId);
                    intent.putExtra("isEdit", true);
                    startActivityForResult(intent,Constants.RequestCode.EDIT_COLLAGE_PRODUCT);
                }else{
                    Intent intent = new Intent(this, CollagePromotionSelectProductListActivity.class);
                    if(newProductId>0){
                        intent.putExtra("productId",newProductId);
                    }
                    startActivityForResult(intent, Constants.RequestCode.COLLAGE_TO_PRODUCTLIST);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                boolean isOk = checkInput();
                if(!isOk){
                    return;
                }
                if(editProductId>0){
                    updateCollageProduct();
                }else{
                    insertCollageProduct();
                }
                break;
        }
    }

    private boolean checkInput(){
        if(productBean==null){
            if(editProductId==0){
                Util.showToast(this,"请选择拼团商品");
            }
            return false;
        }
        if(startDate==null){
            Util.showToast(this,"请选择拼团开始时间");
            return false;
        }
        if(endDate==null){
            Util.showToast(this,"请选择拼团结束时间");
            return false;
        }
        if(endDate!=null && endDate.getTime()<System.currentTimeMillis()){
            Util.showToast(this,"拼团活动结束时间不得小于当前时间");
            return false;
        }
        if(startDate!=null && endDate!=null && endDate.getTime()<=startDate.getTime()){
            Util.showToast(AddCollageProductActivity.this,"结束时间应大于开始时间");
            return false;
        }
        if(StringUtil.isEmpty(etAddTime.getText().toString().trim()) || Integer.valueOf(etAddTime.getText().toString().trim())<=1){
            Util.showToast(this,"请输入正确的成团人数");
            return false;
        }
        if(StringUtil.isEmpty(etCollageSuccessTime.getText().toString().trim()) || Integer.valueOf(etCollageSuccessTime.getText().toString().trim())<=0){
            Util.showToast(this,"请输入正确的成团时间");
            return false;
        }
        return true;
    }

    private void updateCollageProduct() {
        if(newPromotionPrice>0){
            productBean.setCrowdPrice(newPromotionPrice);
        }
        productBean.setCrowdEndDate(getTimeString(endDate));
        productBean.setCrowdStartDate(getTimeString(startDate));
        productBean.setCrowdGroupNum(Integer.valueOf(etAddTime.getText().toString().trim()));
        productBean.setCrowdGroupTime(etCollageSuccessTime.getText().toString().trim());
        productBean.setStatus(isSwitchViewChecked==true?1:0);
        tvRight.setEnabled(false);
        addDisposable(ApiRetrofit.getInstance().getApiService().productUpdate(productBean), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(AddCollageProductActivity.this,"拼团商品更新成功");
                finish();
            }

            @Override
            public void onError(int errorCode, String msg) {
                tvRight.setEnabled(true);
                super.onError(errorCode, msg);
            }
        });
    }

    private void insertCollageProduct() {

        if(newPromotionPrice<=0){
            Util.showToast(this,"请输入正确的拼团价格");
            return;
        }
        tvRight.setEnabled(false);
        productBean.setCrowdEndDate(getTimeString(endDate));
        productBean.setCrowdStartDate(getTimeString(startDate));
        productBean.setCrowdGroupNum(Integer.valueOf(etAddTime.getText().toString().trim()));
        productBean.setCrowdGroupTime(etCollageSuccessTime.getText().toString().trim());
        productBean.setCrowdPrice(newPromotionPrice);
        productBean.setCrowd(1);
        productBean.setStatus(isSwitchViewChecked==true?1:0);
        addDisposable(ApiRetrofit.getInstance().getApiService().productUpdate(productBean), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(AddCollageProductActivity.this,"创建拼团商品成功");
                setResult(RESULT_OK);
                finish();
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

    private Date getSelectDate(int currentYear, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear,month-1,day,hour,minute);
        Date time = calendar.getTime();
        return time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK ){
            if(requestCode==Constants.RequestCode.COLLAGE_TO_PRODUCTLIST){
                if(data!=null){
                    newProductId = data.getLongExtra("productId",0);
                    newPromotionPrice = data.getDoubleExtra("promotionPrice",0);
                    String productName = data.getStringExtra("productName");
                    if(!StringUtil.isEmpty(productName)) {
                        tvSelectedProductName.setText(productName);
                    }
                    loadProductInfo(newProductId);
                }
            }else if(requestCode==Constants.RequestCode.EDIT_COLLAGE_PRODUCT){
                if(data!=null){
                    newPromotionPrice = data.getDoubleExtra("promotionPrice",0);
                }
            }

        }
    }
}
