package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.CategoryBean;
import com.d2cmall.shopkeeper.model.CouponBean;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.view.CategoryPop;
import com.d2cmall.shopkeeper.ui.view.SelectTimePop;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.TransparentPop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.KeyboardUtil;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadManager;
import com.upyun.library.listener.SignatureListener;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.utils.UpYunUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/3/4.
 * Description : AddCollageCouponActivity
 * 添加拼团优惠券
 */

public class AddCollageCouponActivity extends BaseActivity {
    @BindView(R.id.et_promotion_name)
    EditText etPromotionName;
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
    @BindView(R.id.ll_select_coupon)
    LinearLayout llSelectCoupon;
    @BindView(R.id.iv_coupon)
    ImageView ivCoupon;
    @BindView(R.id.sort_tv)
    TextView sortTv;
    @BindView(R.id.tv_store)
    TextView tvStore;
    @BindView(R.id.select_sort_iv)
    ImageView selectSortIv;
    @BindView(R.id.rl_category)
    RelativeLayout rlCategory;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.select_category_iv)
    ImageView selectCategoryIv;
    @BindView(R.id.rl_class)
    RelativeLayout rlClass;
    @BindView(R.id.tv_product_price)
    TextView tvProductPrice;
    @BindView(R.id.tv_product_promotion_price)
    EditText tvProductPromotionPrice;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_selected)
    TextView tvSelected;
    @BindView(R.id.switch_view)
    Switch switchView;

    private String startTime;
    private String endTime;
    private Date startDate;
    private Date endDate;
    private BaseModel<CouponBean> couponBean;
    private String newCouponUrl;
    private long categoryId;
    private long classifyId;
    private ProductBean productBean;
    private long productId;
    private ProductBean.SkuListBean skuListBean;
    private long couponId;
    private boolean isSwitchViewChecked=true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_collage_coupon;
    }

    @Override
    public void doBusiness() {
        init();
    }

    private void init() {
        tvName.setText("添加拼团优惠券");
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("提交");
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        productId=  getIntent().getLongExtra("productId",0);
        int promotionType = getIntent().getIntExtra("promotionType", -2);
        if(promotionType==1 || promotionType==-1){
            tvRight.setVisibility(View.GONE);
        }
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSwitchViewChecked=isChecked;
            }
        });
        if(productId>0){
            llSelectCoupon.setVisibility(View.GONE);
            loadProductInfo();
        }
    }

    private void setDataToView() {
        ImageLoader.displayImage(Glide.with(this),productBean.getFirstPic(),ivCoupon,R.mipmap.pic_coupon,R.mipmap.pic_coupon);
        etPromotionName.setText(productBean.getName());
        tvStartTime.setText(productBean.getCrowdStartDate());
        tvEndTime.setText(productBean.getCrowdEndDate());
        etCollageSuccessTime.setText(String.valueOf(productBean.getCrowdGroupTime()));
        etAddTime.setText(String.valueOf(productBean.getCrowdGroupNum()));
        if(productBean.getCategory()!=null){
            tvCategory.setText(productBean.getCategory().getName());
        }
        if(productBean.getClassify()!=null){
            tvStore.setText(productBean.getClassify().getName());
        }

        if(productBean.getStatus()==1){
            switchView.setChecked(true);
            isSwitchViewChecked=true;
        }else{
            switchView.setChecked(false);
            isSwitchViewChecked=false;
        }

        tvProductPrice.setText(Util.getNumberFormat(productBean.getPrice()));
        tvProductPromotionPrice.setText(Util.getNumberFormat(productBean.getCrowdPrice()));
        if(!StringUtil.isEmpty(productBean.getCrowdStartDate())){
            startDate=parseDate(productBean.getCrowdStartDate());
        }

        if(!StringUtil.isEmpty(productBean.getCrowdEndDate())){
            endDate=parseDate(productBean.getCrowdEndDate());
        }

        categoryId=productBean.getCategoryId();
        classifyId=productBean.getClassifyId();
        newCouponUrl=productBean.getFirstPic();
        couponId=productBean.getCouponId();
    }
    private Date parseDate( String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void loadCouponInfo(long couponId) {
        addDisposable(ApiRetrofit.getInstance().getApiService().getCouponDetial(couponId), new BaseObserver<BaseModel<CouponBean>>() {
            @Override
            public void onSuccess(BaseModel<CouponBean> couponBeanBaseModel) {
                couponBean = couponBeanBaseModel;
            }
        });
    }

    private void loadProductInfo() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getProduct(productId), new BaseObserver<BaseModel<ProductBean>>() {
            @Override
            public void onSuccess(BaseModel<ProductBean> o) {
                productBean = o.getData();
                setDataToView();
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
            }
        });
    }

    private void checkInput() {
        if (productBean==null && couponBean == null ) {
            Util.showToast(this, "请选择拼团优惠券");
            return;
        }
        if (StringUtil.isEmpty(newCouponUrl)) {
            Toast.makeText(this, "请选择优惠券图片", Toast.LENGTH_SHORT).show();
            return;
        }
        if (startDate == null) {
            Util.showToast(this, "请选择拼团开始时间");
            return;
        }
        if (endDate == null) {
            Util.showToast(this, "请选择拼团结束时间");
            return;
        }
        if(endDate!=null && endDate.getTime()<System.currentTimeMillis()){
            Util.showToast(this,"拼团活动结束时间不得小于当前时间");
            return;
        }
        if (startDate != null && endDate != null && endDate.getTime() <= startDate.getTime()) {
            Util.showToast(AddCollageCouponActivity.this, "结束时间应大于开始时间");
            return;
        }
        if (StringUtil.isEmpty(etPromotionName.getText().toString().trim())) {
            Util.showToast(this, "请输入活动名称");
            return;
        }
        if (StringUtil.isEmpty(etAddTime.getText().toString().trim()) || Integer.valueOf(etAddTime.getText().toString().trim())<=1) {
            Util.showToast(this, "请输入正确的成团人数");
            return;
        }
        if (StringUtil.isEmpty(etCollageSuccessTime.getText().toString().trim()) || Integer.valueOf(etCollageSuccessTime.getText().toString().trim())<=0) {
            Util.showToast(this, "请输入正确的成团时间");
            return;
        }
        if ( getDoubleValue(tvProductPrice.getText().toString().trim()) <= 0) {
            Util.showToast(this, "请输入正确的售价");
            return;
        }

        if ( getDoubleValue(tvProductPromotionPrice.getText().toString().trim()) <= 0) {
            Util.showToast(this, "请输入正确的拼团价");
            return;
        }

        if (categoryId <= 0) {
            Util.showToast(this, "请选择分类");
            return;
        }

        if (classifyId <= 0) {
            Util.showToast(this, "请选择品类");
            return;
        }

        if (getDoubleValue(tvProductPromotionPrice.getText().toString().trim()) <= 0) {
            Util.showToast(this, "请输入正确的拼团价");
            return;
        }

            updateCouponProduct();
    }


    /**
     * 新增更新拼团优惠券
     */
    private void updateCouponProduct() {
        if (categoryId == 0 || classifyId == 0) return;
        ProductBean bean = productBean;
        if (bean == null) {
            bean = new ProductBean();
        }
        bean.setName(etPromotionName.getText().toString());
        bean.setCategoryId(categoryId);
        bean.setClassifyId(classifyId);
        ArrayList<ProductBean.SkuListBean> skuListBeans = new ArrayList<>();
        if(getSku()==null){
            return;
        }
        skuListBeans.add(getSku());
        bean.setSkuList(skuListBeans);
        bean.setPrice(bean.getSkuList().get(0).getSellPrice());
        bean.setPic(newCouponUrl);
        bean.setCrowd(1);
        if (couponId > 0) {
            bean.setCouponId(couponId);
        }
        bean.setVirtual(1);
        bean.setCrowdEndDate(getTimeString(endDate));
        bean.setCrowdStartDate(getTimeString(startDate));
        if(couponBean!=null){
            if(couponBean.getData().getCirculation()-couponBean.getData().getConsumption()<=0){
                Util.showToast(this, "所选优惠券已被领完");
                return;
            }
        }
        bean.setCrowdGroupNum(Integer.valueOf(etAddTime.getText().toString().trim()));
        bean.setCrowdGroupTime(etCollageSuccessTime.getText().toString().trim());
        bean.setCrowdPrice(getDoubleValue(tvProductPromotionPrice.getText().toString().trim()));
        //上架状态进行中的拼团活动,不允许直接编辑
        if( couponId > 0 && "1".equals(bean.getCrowd()) && isSwitchViewChecked==true && bean.getStatus()==1){
            Util.showToast(this, "进行中的拼团活动请先下架再编辑~");
            return;
        }
        bean.setStatus(isSwitchViewChecked==true?1:0);
        if (productBean != null) {
            tvRight.setEnabled(false);
            addDisposable(ApiRetrofit.getInstance().getApiService().productUpdate(bean), new BaseObserver() {
                @Override
                public void onSuccess(Object o) {
                    Util.showToast(AddCollageCouponActivity.this, "拼团优惠券更新成功");
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
           if(couponBean!=null && endDate!=null && !StringUtil.isEmpty(couponBean.getData().getServiceEndDate())){
               if(endDate.getTime()> parseDate(couponBean.getData().getServiceEndDate()).getTime()){
                   Util.showToast(AddCollageCouponActivity.this,"所选优惠券使用结束时间必须大于拼团活动结束时间");
                   return;
               }
              
           }
           //,设置优惠券为拼团优惠券
            setCouPonCrowd();
            tvRight.setEnabled(false);
            addDisposable(ApiRetrofit.getInstance().getApiService().productInsert(bean), new BaseObserver() {
                @Override
                public void onSuccess(Object o) {
                    Util.showToast(AddCollageCouponActivity.this, "拼团优惠券添加成功");
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
    }

    private void setCouPonCrowd() {
        if(couponBean!=null){
            couponBean.getData().setCrowd("1");
            addDisposable(ApiRetrofit.getInstance().getApiService().upDateCoupon(couponBean.getData()), new BaseObserver() {
                @Override
                public void onSuccess(Object o) {

                }

                @Override
                public void onError(int errorCode, String msg) {
                    tvRight.setEnabled(true);
                    super.onError(errorCode, msg);
                }
            });
        }

    }


    public ProductBean.SkuListBean getSku() {
        String price = tvProductPrice.getText().toString().trim();
        String warningStore = "0";
        if (skuListBean == null) {
            skuListBean = new ProductBean.SkuListBean();
        }
        skuListBean.setSellPrice(getDoubleValue(price));
        skuListBean.setStandard("拼团优惠券");
        if(couponBean!=null){
            if(couponBean.getData().getCirculation()-couponBean.getData().getConsumption()<=0){
                Util.showToast(this,"所选优惠券已被领完");
                return null;
            }
            String store = String.valueOf(couponBean.getData().getCirculation()-couponBean.getData().getConsumption());
            skuListBean.setStock(Integer.valueOf(store));
        }
        skuListBean.setWarnStock(Integer.valueOf(warningStore));
        return skuListBean;
    }


    @OnClick({R.id.ll_start_time, R.id.ll_end_time, R.id.iv_coupon, R.id.rl_category, R.id.tv_end_time,R.id.rl_class, R.id.iv_back, R.id.tv_right, R.id.ll_select_coupon})
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
            case R.id.ll_end_time:
            case  R.id.tv_end_time:
                //结束时间
                SelectTimePop selectEndTimePop = new SelectTimePop(this);
                selectEndTimePop.setDismissCallBack(new TransparentPop.DismissListener() {
                    @Override
                    public void dismissStart() {
                        endTime = selectEndTimePop.getDateStr2();
                        endDate = getSelectDate(selectEndTimePop.currentYear, selectEndTimePop.month, selectEndTimePop.day, selectEndTimePop.hour, selectEndTimePop.minute);
                        if (startDate != null && endDate != null && endDate.getTime() <= startDate.getTime()) {
                            Util.showToast(AddCollageCouponActivity.this, "结束时间应大于开始时间");
                        }
                        tvEndTime.setText(endTime);
                    }

                    @Override
                    public void dismissEnd() {

                    }
                });
                selectEndTimePop.show(getWindow().getDecorView());
                break;
            case R.id.iv_coupon:
                //优惠券图片
                handlePermission();
                break;
            case R.id.rl_category:
                //分类
                rlCategory.setEnabled(false);
                showCategoryPop(false);
                break;
            case R.id.rl_class:
                //品类
                showCategoryPop(true);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //提交
                checkInput();
                break;
            case R.id.ll_select_coupon:
                Intent intent = new Intent(this, CollagePromotionSelectCouponActivity.class);
                if(couponId>0){
                    intent.putExtra("couponId",couponId);
                }
                startActivityForResult(intent, Constants.RequestCode.SELECT_COUPON);

                break;
        }
    }

    private void showCategoryPop(boolean isCategory) {
        if (getCurrentFocus() != null) {
            KeyboardUtil.hideKeyboard(rlCategory);
        }
        if (isCategory) {
            addDisposable(ApiRetrofit.getInstance().getApiService().getCategoryList(Util.buildListParam(1, 20)), new BaseObserver<BaseModel<List<CategoryBean>>>() {
                @Override
                public void onSuccess(BaseModel<List<CategoryBean>> o) {
                    if (o.getData().size() == 0) {
                        Util.showToast(AddCollageCouponActivity.this, "暂无品类数据,请先添加品类", false);
                        startActivity(new Intent(AddCollageCouponActivity.this,CategoryManagerActivity.class));
                        return;
                    }
                    CategoryPop pop = new CategoryPop(AddCollageCouponActivity.this, o.getData(), false);
                    pop.setCallBack(new CategoryPop.CallBack() {
                        @Override
                        public void callback(long id, String... selectName) {
                            categoryId = id;
                            tvCategory.setText(getValue(selectName));
                        }
                    });
                    pop.show(rlCategory);
                }

                @Override
                public void onError(int errorCode, String msg) {
                    super.onError(errorCode, msg);
                }
            });
        } else {
            addDisposable(ApiRetrofit.getInstance().getApiService().getClassifyList(Util.buildListParam(1, 20)), new BaseObserver<BaseModel<List<CategoryBean>>>() {
                @Override
                public void onSuccess(BaseModel<List<CategoryBean>> o) {
                    rlCategory.setEnabled(true);
                    if (o.getData().size() == 0) {
                        Util.showToast(AddCollageCouponActivity.this, "暂无分类数据", false);
                        return;
                    }
                    CategoryPop pop = new CategoryPop(AddCollageCouponActivity.this, o.getData(), false);
                    pop.setCallBack(new CategoryPop.CallBack() {
                        @Override
                        public void callback(long id, String... selectName) {
                            classifyId = id;
                            tvStore.setText(getValue(selectName));
                        }
                    });
                    pop.show(rlCategory);
                }

                @Override
                public void onError(int errorCode, String msg) {
                    rlCategory.setEnabled(true);
                    super.onError(errorCode, msg);
                }
            });
        }
    }

    private String getValue(String... strings) {
        int size = strings.length;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (!StringUtil.isEmpty(strings[i])) {
                if (i != 0) {
                    builder.append(" ");
                }
                builder.append(strings[i]);
            }
        }
        return builder.toString();
    }

    public void handlePermission() {
        PackageManager pkgManager = getPackageManager();
        boolean cameraPermission =
                pkgManager.checkPermission(Manifest.permission.CAMERA, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        boolean storagePermission =
                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= 23 && (!cameraPermission || !storagePermission)) {
            requestPermission();
        } else {
            choosePic();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                Constants.RequestCode.REQUEST_PERMISSION);
    }

    private void choosePic() {

        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .theme(R.style.Matisse_Dracula)
                .countable(true)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.d2cmall.shopkeeper.fileprovider"))
                .maxSelectable(1)
                .gridExpectedSize(ScreenUtil.dip2px(120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(456);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constants.RequestCode.REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                choosePic();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 456:
                    if (data != null) {
                        newCouponUrl = null;
                        List<Uri> uris = Matisse.obtainResult(data);
                        List<String> strings = Matisse.obtainPathResult(data);
                        Glide.with(this)
                                .load(uris.get(0))
                                .error(R.mipmap.icon_addpic)
                                .crossFade()
                                .into(ivCoupon);
                        uploadFile(strings.get(0));
                    }
                    break;
                case Constants.RequestCode.SELECT_COUPON:
                    couponId = data.getLongExtra("couponId", 0);
                    if (couponId > 0) {
                        tvSelected.setText("已选择");
                        loadCouponInfo(couponId);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        final Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(Params.BUCKET, Constants.UPYUN_SPACE);
        paramsMap.put(Params.SAVE_KEY, Util.getUPYunSavePath(0, Constants.TYPE_AVATAR));
        paramsMap.put(Params.RETURN_URL, "httpbin.org/post");
        UpCompleteListener completeListener = new UpCompleteListener() {

            @Override
            public void onComplete(boolean isSuccess, String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    newCouponUrl = jsonObject.optString("url");
                } catch (JSONException e) {
                }
            }
        };
        SignatureListener signatureListener = new SignatureListener() {
            @Override
            public String getSignature(String raw) {
                return UpYunUtils.md5(raw + Constants.UPYUN_KEY);
            }
        };
        UploadManager.getInstance().blockUpload(file, paramsMap, signatureListener, completeListener, null);
    }

    private String getTimeString(Date timeDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = df.format(timeDate);
        return timeString;
    }

    private Date getSelectDate(int currentYear, int month, int day, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, month - 1, day, hour, minute);
        Date time = calendar.getTime();
        return time;
    }

    private  double getDoubleValue(String str){
        if(StringUtil.isEmpty(str)){
            return 0.0;
        }else if(str.contains(",")){
            str = str.replaceAll(",","");
        }
        return Double.valueOf(str);
    }

}
