package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/28.
 * Description : BrandAuthenticationActivity
 * 店铺认证
 */

public class BrandAuthenticationActivity extends BaseActivity {
    @BindView(R.id.et_brand_name)
    EditText etBrandName;
    @BindView(R.id.et_business_license_code)
    EditText etBusinessLicenseCode;
    @BindView(R.id.iv_business_license)
    ImageView ivBusinessLicense;
    @BindView(R.id.ll_business_license)
    LinearLayout llBusinessLicense;
    @BindView(R.id.et_legal_person_name)
    EditText etLegalPersonName;
    @BindView(R.id.et_legal_person_id_number)
    EditText etLegalPersonIdNumber;
    @BindView(R.id.iv_id_card_positive)
    ImageView ivIdCardPositive;
    @BindView(R.id.ll_id_card_positive)
    LinearLayout llIdCardPositive;
    @BindView(R.id.iv_id_card_opposit)
    ImageView ivIdCardOpposit;
    @BindView(R.id.ll_id_card_opposit)
    LinearLayout llIdCardOpposit;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;

    private String businessLicenseImageUrl;
    private String idcardPositiveImageUrl;
    private String idcardOppositImageUrl;
    private boolean businessLicenseUpLoading;
    private boolean idcardPositiveImageUpLoading;
    private boolean idcardOppositImageUpLoading;
    private int imageType;
    private long shopId;
    private BaseModel<ShopBean> mShopBean;
    private UserBean userBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_brand_authentication;
    }

    @Override
    public void doBusiness() {
        init();
    }

    private void init() {
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvName.setText("店铺认证");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("提交");
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        userBean = Session.getInstance().getUserFromFile(this);
        initTextWatcher();
        if(userBean !=null){
            shopId=userBean.getShopId();
        }else{
            return;
        }
        loadBrandInfo();
    }

    private void initTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkConmmitStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etBrandName.addTextChangedListener(textWatcher);
        etBusinessLicenseCode.addTextChangedListener(textWatcher);
        etLegalPersonIdNumber.addTextChangedListener(textWatcher);
        etLegalPersonName.addTextChangedListener(textWatcher);
    }

    private void loadBrandInfo() {

        addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(shopId), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBean) {
                mShopBean = shopBean;
                ImageLoader.displayImage(Glide.with(BrandAuthenticationActivity.this), shopBean.getData().getLicensePic(), ivBusinessLicense, R.mipmap.icon_addpic);
                if(!StringUtil.isEmpty(shopBean.getData().getLicensePic())){
                    businessLicenseImageUrl=shopBean.getData().getLicensePic();
                }

                if (!StringUtil.isEmpty(shopBean.getData().getCorporationPic())) {
                    String[] split = shopBean.getData().getCorporationPic().split(",");
                    idcardPositiveImageUrl=split[0];
                    idcardOppositImageUrl=split[1];
                    ImageLoader.displayImage(BrandAuthenticationActivity.this, split[0], ivIdCardPositive, R.mipmap.icon_addpic);
                    ImageLoader.displayImage(BrandAuthenticationActivity.this, split[1], ivIdCardOpposit, R.mipmap.icon_addpic);
                }
                if (!StringUtil.isEmpty(shopBean.getData().getEnterprise())) {
                    etBrandName.setText(shopBean.getData().getEnterprise());
                }
                if (!StringUtil.isEmpty(shopBean.getData().getCorporationName())) {
                    etLegalPersonName.setText(shopBean.getData().getCorporationName());
                }
                if (!StringUtil.isEmpty(shopBean.getData().getLicenseNum())) {
                    etBusinessLicenseCode.setText(shopBean.getData().getLicenseNum());
                }
                if (!StringUtil.isEmpty(shopBean.getData().getCorporationCard())) {
                    etLegalPersonIdNumber.setText(shopBean.getData().getCorporationCard());
                }
            }
        });
    }

    private void checkConmmitStatus() {
        if (StringUtil.isEmpty(etBrandName.getText().toString().trim())
                || StringUtil.isEmpty(etBusinessLicenseCode.getText().toString().trim())
                || StringUtil.isEmpty(etLegalPersonIdNumber.getText().toString().trim())
                || StringUtil.isEmpty(idcardOppositImageUrl)
                || StringUtil.isEmpty(idcardPositiveImageUrl)
                || StringUtil.isEmpty(businessLicenseImageUrl)
                || StringUtil.isEmpty(etLegalPersonName.getText().toString().trim())) {
            tvRight.setTextColor(getResources().getColor(R.color.normal_black));
        } else {
            tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        }

    }


    @OnClick({R.id.ll_business_license, R.id.ll_id_card_positive, R.id.ll_id_card_opposit, R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_business_license:
                //营业执照
                imageType = 456;
                handlePermission();
                break;
            case R.id.ll_id_card_positive:
                //身份证正面
                imageType = 457;
                handlePermission();
                break;
            case R.id.ll_id_card_opposit:
                //身份证反面
                imageType = 458;
                handlePermission();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //提交
                updateBrandInfo();
                break;
        }
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
                .forResult(imageType);
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
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case 456:
                    if (data != null) {
                        businessLicenseImageUrl = null;
                        List<Uri> uris = Matisse.obtainResult(data);
                        List<String> strings = Matisse.obtainPathResult(data);
                        Glide.with(this)
                                .load(uris.get(0))
                                .error(R.mipmap.icon_addpic)
                                .crossFade()
                                .into(ivBusinessLicense);
                        businessLicenseUpLoading = true;
                        uploadFile(strings.get(0), 456);
                    }
                    break;
                case 457:
                    if (data != null) {
                        List<Uri> uris = Matisse.obtainResult(data);
                        List<String> strings = Matisse.obtainPathResult(data);
                        Glide.with(this)
                                .load(uris.get(0))
                                .error(R.mipmap.icon_addpic)
                                .crossFade()
                                .into(ivIdCardPositive);
                        idcardPositiveImageUpLoading = true;
                        uploadFile(strings.get(0), 457);
                    }
                    break;
                case 458:
                    if (data != null) {
                        List<Uri> uris = Matisse.obtainResult(data);
                        List<String> strings = Matisse.obtainPathResult(data);
                        Glide.with(this)
                                .load(uris.get(0))
                                .error(R.mipmap.icon_addpic)
                                .crossFade()
                                .into(ivIdCardOpposit);
                        idcardOppositImageUpLoading = true;
                        uploadFile(strings.get(0), 458);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile(String filePath, int i) {
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
                    if (i == 456) {
                        businessLicenseUpLoading = false;
                        businessLicenseImageUrl = jsonObject.optString("url");
                    } else if (i == 457) {
                        idcardPositiveImageUpLoading = false;
                        idcardPositiveImageUrl = jsonObject.optString("url");
                    } else {
                        idcardOppositImageUpLoading = false;
                        idcardOppositImageUrl = jsonObject.optString("url");
                    }
                    checkConmmitStatus();
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


    private void updateBrandInfo() {
        if (StringUtil.isEmpty(etBrandName.getText().toString().trim())) {
            Util.showToast(this, "请输入店铺名称");
            return;
        }
        if (StringUtil.isEmpty(etBusinessLicenseCode.getText().toString().trim())) {
            Util.showToast(this, "请输入营业执照编号");
            return;
        }
        if (StringUtil.isEmpty(etLegalPersonName.getText().toString().trim())) {
            Util.showToast(this, "请输入法人姓名");
            return;
        }
        if (StringUtil.isEmpty(etLegalPersonIdNumber.getText().toString().trim())) {
            Util.showToast(this, "请输入法人身份证号");
            return;
        }
        if (businessLicenseUpLoading == true || idcardPositiveImageUpLoading == true || idcardOppositImageUpLoading == true) {
            Util.showToast(this, "图片上传中,请稍等");
            return;
        }

        if (StringUtil.isEmpty(businessLicenseImageUrl)) {
            Util.showToast(this, "请上传营业执照");
            return;
        }

        if (StringUtil.isEmpty(idcardPositiveImageUrl)) {
            Util.showToast(this, "身份证正面照");
            return;
        }

        if (StringUtil.isEmpty(idcardOppositImageUrl)) {
            Util.showToast(this, "身份证反面照");
            return;
        }
        if (mShopBean == null) {
            return;
        }
        mShopBean.getData().setEnterprise(etBrandName.getText().toString().trim());
        mShopBean.getData().setCorporationName(etLegalPersonName.getText().toString().trim());
        mShopBean.getData().setLicenseNum(etBusinessLicenseCode.getText().toString().trim());
        mShopBean.getData().setCorporationCard(etLegalPersonIdNumber.getText().toString().trim());
        mShopBean.getData().setId(shopId);
        if (!StringUtil.isEmpty(businessLicenseImageUrl)) {
            mShopBean.getData().setLicensePic(businessLicenseImageUrl);
        }
        if (!StringUtil.isEmpty(idcardPositiveImageUrl) && !StringUtil.isEmpty(idcardOppositImageUrl)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(idcardPositiveImageUrl);
            stringBuilder.append(",");
            stringBuilder.append(idcardOppositImageUrl);
            mShopBean.getData().setCorporationPic(stringBuilder.toString());
        }
        tvRight.setEnabled(false);
        addDisposable(ApiRetrofit.getInstance().getApiService().updateShop(mShopBean.getData()), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBean) {
                if(mShopBean.getData().getAuthenticate()==0){
                    Util.showToast(BrandAuthenticationActivity.this, "上传成功,请耐心等待审核");
                }
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
