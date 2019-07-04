package com.d2cmall.shopkeeper.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Address;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.AddressPop;
import com.d2cmall.shopkeeper.ui.view.RoundedImageView;
import com.d2cmall.shopkeeper.ui.view.SelectBusinessHourPop;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.ShopAddressPop;
import com.d2cmall.shopkeeper.ui.view.TransparentPop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/27.
 * Description : BrandSettingActivity
 * 编辑店铺信息
 */

public class BrandSettingActivity extends BaseActivity implements AddressPop.CallBack {
    @BindView(R.id.iv_brand_image)
    ImageView ivBrandImage;
    @BindView(R.id.iv_brand_wechat)
    ImageView ivBrandWechat;
    @BindView(R.id.ll_brand_image)
    LinearLayout llBrandImage;
    @BindView(R.id.iv_brand_head_pic)
    RoundedImageView ivBrandHeadPic;
    @BindView(R.id.ll_brand_head)
    LinearLayout llBrandHead;
    @BindView(R.id.et_brand_name)
    EditText etBrandName;
    @BindView(R.id.et_brand_brief_introduction)
    EditText etBrandBriefIntroduction;
    @BindView(R.id.et_brand_notice)
    EditText etBrandNotice;
    @BindView(R.id.et_business_categor)
    EditText etBusinessCategor;
    @BindView(R.id.tv_business_hour)
    TextView tvBusinessHour;
    @BindView(R.id.et_brand_adress)
    EditText etBrandAdress;
    @BindView(R.id.et_brand_phone)
    EditText etBrandPhone;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ll_brand_wechat)
    LinearLayout llBrandWechat;
    @BindView(R.id.tv_brand_adress)
    TextView tvBrandAdress;
    @BindView(R.id.ll_brand_address)
    LinearLayout llBrandAddress;
    @BindView(R.id.syn_tv1)
    TextView synTv1;
    @BindView(R.id.tv_brand_back_adress)
    TextView tvBrandBackAdress;
    @BindView(R.id.ll_brand_back_address)
    LinearLayout llBrandBackAddress;
    @BindView(R.id.et_brand_back_adress)
    EditText etBrandBackAdress;
    @BindView(R.id.syn_tv2)
    TextView synTv2;
    @BindView(R.id.tv_brand_receive_adress)
    TextView tvBrandReceiveAdress;
    @BindView(R.id.ll_brand_receive_address)
    LinearLayout llBrandReceiveAddress;
    @BindView(R.id.et_brand_receive_adress)
    EditText etBrandReceiveAdress;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.et_brand_back_name)
    EditText etBrandBackName;
    @BindView(R.id.et_brand_back_phone)
    EditText etBrandBackPhone;
    @BindView(R.id.et_brand_receive_name)
    EditText etBrandReceiveName;
    @BindView(R.id.et_brand_receive_phone)
    EditText etBrandReceivePhone;

    private int imageType = 456;
    private String newHeadPic;
    private String newBrandImage;
    private UserBean userBean;
    private BaseModel<ShopBean> mShopBean;
    private String newWechatPic;
    private String businessHourStr;
    private String address = "", backAddress = "", reciveAddress = "";
    private String realAddress;
    private String provinceInfo;
    private AddressPop addressPop;
    private TextView selectTv;
    private InputMethodManager im;
    private boolean scroll = false;
    private boolean isUpload;

    @Override
    public int getLayoutId() {
        return R.layout.activity_brand_setting;
    }

    @Override
    public void doBusiness() {
        init();
    }

    @Override
    protected void onResume() {
        new Thread() {
            @Override
            public void run() {
                try {
                    InputStream input = getResources().openRawResource
                            (R.raw.area);
                    provinceInfo = Util.readStreamToString(input);
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        super.onResume();
    }

    private void init() {
        scroll = getIntent().getBooleanExtra("scroll", false);
        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Sofia.with(this).statusBarDarkFont().statusBarBackground(Color.parseColor("#FFFFFF"));
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        tvName.setText("店铺管理");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextColor(getResources().getColor(R.color.normal_blue));
        tvRight.setText("提交");
        userBean = Session.getInstance().getUserFromFile(this);
        if (userBean == null) {
            return;
        }
        if (scroll) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
        loadBrandInfo();
    }

    private void loadBrandInfo() {

        addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(userBean.getShopId()), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBean) {
                mShopBean = shopBean;
                ImageLoader.displayRoundImage(BrandSettingActivity.this, shopBean.getData().getLogo(), ivBrandHeadPic, R.mipmap.icon_default_avatar);
                ImageLoader.displayImage(BrandSettingActivity.this, shopBean.getData().getBanner(), ivBrandImage);
                ImageLoader.displayImage(BrandSettingActivity.this, shopBean.getData().getWechat(), ivBrandWechat, R.mipmap.icon_addpic);
                if (!StringUtil.isEmpty(shopBean.getData().getName())) {
                    etBrandName.setText(shopBean.getData().getName());
                }
                //公告
                if (!StringUtil.isEmpty(shopBean.getData().getNotice())) {
                    etBrandNotice.setText(shopBean.getData().getNotice());
                }
                //店铺地址
                if (!StringUtil.isEmpty(shopBean.getData().getAddress())) {
                    if (shopBean.getData().getAddress().startsWith("{")) {
                        Gson gson = new Gson();
                        Address address = gson.fromJson(shopBean.getData().getAddress(), Address.class);
                        tvBrandAdress.setText(address.province + address.city + address.district);
                        etBrandAdress.setText(address.address);
                        BrandSettingActivity.this.address = address.province + "," + address.city + "," + address.district;
                    } else {
                        etBrandAdress.setText(shopBean.getData().getAddress());
                    }
                }
                //经营品类
                if (!StringUtil.isEmpty(shopBean.getData().getScope())) {
                    etBusinessCategor.setText(shopBean.getData().getScope());
                }

                //营业时间
                if (!StringUtil.isEmpty(shopBean.getData().getHours())) {
                    businessHourStr = shopBean.getData().getHours();
                    tvBusinessHour.setText(businessHourStr);
                }

                //退货地址
                if (!StringUtil.isEmpty(shopBean.getData().getBackAddress())) {
                    if (shopBean.getData().getBackAddress().startsWith("{")) {
                        Gson gson = new Gson();
                        Address address = gson.fromJson(shopBean.getData().getBackAddress(), Address.class);
                        tvBrandBackAdress.setText(address.province + address.city + address.district);
                        etBrandBackAdress.setText(address.address);
                        etBrandBackName.setText(address.name);
                        etBrandBackPhone.setText(address.mobile);
                        BrandSettingActivity.this.backAddress = address.province + "," + address.city + "," + address.district;
                    } else {
                        etBrandBackAdress.setText(shopBean.getData().getAddress());
                    }
                }

                if (!StringUtil.isEmpty(shopBean.getData().getReciveAddress())) {
                    if (shopBean.getData().getReciveAddress().startsWith("{")) {
                        Gson gson = new Gson();
                        Address address = gson.fromJson(shopBean.getData().getReciveAddress(), Address.class);
                        tvBrandReceiveAdress.setText(address.province + address.city + address.district);
                        etBrandReceiveAdress.setText(address.address);
                        etBrandReceiveName.setText(address.name);
                        etBrandReceivePhone.setText(address.mobile);
                        BrandSettingActivity.this.reciveAddress = address.province + "," + address.city + "," + address.district;
                    } else {
                        etBrandReceiveAdress.setText(shopBean.getData().getAddress());
                    }
                }

                //电话
                if (!StringUtil.isEmpty(shopBean.getData().getTelephone())) {
                    etBrandPhone.setText(shopBean.getData().getTelephone());
                }

                //店铺简介
                if (!StringUtil.isEmpty(shopBean.getData().getSummary())) {
                    etBrandBriefIntroduction.setText(shopBean.getData().getSummary());
                }

            }
        });
    }

    @OnClick({R.id.ll_brand_image, R.id.ll_brand_head, R.id.iv_back, R.id.tv_right, R.id.ll_brand_wechat, R.id.tv_business_hour, R.id.syn_tv1, R.id.syn_tv2, R.id.ll_brand_address, R.id.ll_brand_back_address, R.id.ll_brand_receive_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_brand_image:
                //选择店招图
                imageType = 456;
                handlePermission();
                break;
            case R.id.ll_brand_head:
                //选择店铺头像
                imageType = 457;
                handlePermission();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_business_hour:
                showSelectBusinessHourPop();
                break;
            case R.id.tv_right:
                updateBrandInfo();
                break;
            case R.id.ll_brand_wechat:
                //选择店铺头像
                imageType = 458;
                handlePermission();
                break;
            case R.id.syn_tv1:
                if (!StringUtil.isEmpty(address)) {
                    backAddress = address;
                } else {
                    mShopBean.getData().setBackAddress(mShopBean.getData().getAddress());
                }
                tvBrandBackAdress.setText(tvBrandAdress.getText().toString());
                etBrandBackAdress.setText(etBrandAdress.getText().toString());
                break;
            case R.id.syn_tv2:
                if (!StringUtil.isEmpty(address)) {
                    reciveAddress = address;
                } else {
                    mShopBean.getData().setReciveAddress(mShopBean.getData().getAddress());
                }
                tvBrandReceiveAdress.setText(tvBrandAdress.getText().toString());
                etBrandReceiveAdress.setText(etBrandAdress.getText().toString());
                break;
            case R.id.ll_brand_address:
                selectTv = tvBrandAdress;
                showAddressPop(view);
                break;
            case R.id.ll_brand_back_address:
                selectTv = tvBrandBackAdress;
                showAddressPop(view);
                break;
            case R.id.ll_brand_receive_address:
                selectTv = tvBrandReceiveAdress;
                showAddressPop(view);
                break;
        }
    }

    private void showAddressPop(View view) {
        if (getCurrentFocus() != null) {
            im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        addressPop = new AddressPop(this, provinceInfo);
        addressPop.setCallBack(this);
        addressPop.show(view);
    }

    private void showSelectBusinessHourPop() {
        Date openHour = null;
        Date closeHour = null;
        int openHourHours = 0;
        int openHourMinutes = 0;
        int closeHourHours = 0;
        int closeHourMinutes = 0;
        if (!StringUtil.isEmpty(businessHourStr) && businessHourStr.contains("-")) {
            String[] split = businessHourStr.split("-");
            if (split.length == 2 && split[0].contains(":") && split[1].contains(":")) {
                String format = "HH:mm";
                try {
                    openHour = new SimpleDateFormat(format).parse(split[0]);
                    closeHour = new SimpleDateFormat(format).parse(split[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (openHour != null && closeHour != null) {
            openHourHours = openHour.getHours();
            openHourMinutes = openHour.getMinutes();
            closeHourHours = closeHour.getHours();
            closeHourMinutes = closeHour.getMinutes();
        }
        SelectBusinessHourPop selectBusinessHourPop = new SelectBusinessHourPop(this, openHourHours, openHourMinutes, closeHourHours, closeHourMinutes);
        selectBusinessHourPop.setDismissCallBack(new TransparentPop.DismissListener() {
            @Override
            public void dismissStart() {
                if (selectBusinessHourPop != null) {
                    if (!StringUtil.isEmpty(selectBusinessHourPop.getBusinessHourStr())) {
                        businessHourStr = selectBusinessHourPop.getBusinessHourStr();
                        if (!StringUtil.isEmpty(businessHourStr) && !"00:00-00:00".equals(businessHourStr)) {
                            tvBusinessHour.setText(businessHourStr);
                        }
                    }
                }
            }

            @Override
            public void dismissEnd() {

            }
        });
        selectBusinessHourPop.show(tvBusinessHour);
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

    List<Uri> mSelected = new ArrayList<>();
    List<String> pathSelected = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case 457:
                    if (data != null) {
                        List<Uri> uris = Matisse.obtainResult(data);
                        List<String> strings = Matisse.obtainPathResult(data);
                        Intent intent = new Intent(this, CropActivity.class);
                        intent.setData(uris.get(0));
                        intent.putExtra("path", strings.get(0));
                        intent.putExtra("mode", 1);
                        startActivityForResult(intent, 1122);
                    }
                    break;
                case 456:
                    if (data != null) {
                        List<Uri> uris = Matisse.obtainResult(data);
                        List<String> strings = Matisse.obtainPathResult(data);
                        Intent intent = new Intent(this, CropActivity.class);
                        intent.setData(uris.get(0));
                        intent.putExtra("path", strings.get(0));
                        intent.putExtra("ratioX", 2);
                        intent.putExtra("ratioY", 1);
                        startActivityForResult(intent, 1121);
                    }
                    break;
                case 458:
                    if (data != null) {
                        mSelected = Matisse.obtainResult(data);
                        pathSelected = Matisse.obtainPathResult(data);
                        Intent intent = new Intent(this, CropActivity.class);
                        intent.setData(mSelected.get(0));
                        intent.putExtra("path", pathSelected.get(0));
                        startActivityForResult(intent, 1123);
                    }
                    break;
                case 1123:
                    if (data != null) {
                        Uri uri = data.getData();
                        String cropPath = data.getStringExtra("cropPath");
                        Glide.with(this)
                                .load(uri)
                                .error(R.mipmap.icon_addpic)
                                .crossFade()
                                .into(ivBrandWechat);
                        uploadFile(cropPath, 458);
                    }
                    break;
                case 1122:
                    if (data != null) {
                        Uri uri = data.getData();
                        String cropPath = data.getStringExtra("cropPath");
                        Glide.with(this)
                                .load(uri)
                                .error(R.mipmap.icon_addpic)
                                .crossFade()
                                .into(ivBrandHeadPic);
                        uploadFile(cropPath, 457);
                    }
                    break;
                case 1121:
                    if (data != null) {
                        Uri uri = data.getData();
                        String cropPath = data.getStringExtra("cropPath");
                        Glide.with(this)
                                .load(uri)
                                .error(R.mipmap.icon_addpic)
                                .crossFade()
                                .into(ivBrandImage);
                        uploadFile(cropPath, 456);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile(String filePath, int i) {
        if (StringUtil.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        isUpload = true;
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
                        newBrandImage = jsonObject.optString("url");
                    } else if (i == 457) {
                        newHeadPic = jsonObject.optString("url");
                    } else {
                        newWechatPic = jsonObject.optString("url");
                    }
                } catch (JSONException e) {
                }
                isUpload = false;
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
        if (isUpload) {
            Util.showToast(this, "图片上传中，请稍后");
            return;
        }
        if (!checkAddress()) {
            Util.showToast(this, "请输入店铺地址");
            return;
        }
        /*if (checkBackAddress()) {
            Util.showToast(this, "请输入退货地址");
            return;
        }
        if (checkBackAddress()) {
            Util.showToast(this, "请输入退货地址");
            return;
        }*/
        checkBackAddress();
        checkReceiveAddress();
        if (StringUtil.isEmpty(etBrandBriefIntroduction.getText().toString().trim())) {
            Util.showToast(this, "请输入店铺简介");
            return;
        }
        if (StringUtil.isEmpty(etBrandName.getText().toString().trim())) {
            Util.showToast(this, "请输入店铺名称");
            return;
        }
//        if(StringUtil.isEmpty(etBrandNotice.getText().toString().trim())){
//            Util.showToast(this,"请输入店铺公告");
//            return;
//        }
        if (StringUtil.isEmpty(etBrandPhone.getText().toString().trim())) {
            Util.showToast(this, "请输入店铺电话");
            return;
        }
        if (StringUtil.isEmpty(etBusinessCategor.getText().toString().trim())) {
            Util.showToast(this, "请输入店铺经营品类");
            return;
        }
        if (StringUtil.isEmpty(businessHourStr)) {
            Util.showToast(this, "请选择营业时间");
            return;
        }
        if (mShopBean == null) {
            return;
        }
        if (!StringUtil.isEmpty(newHeadPic)) {
            mShopBean.getData().setLogo(newHeadPic);
        }
        mShopBean.getData().setId(userBean.getShopId());
        if (!StringUtil.isEmpty(newBrandImage)) {
            mShopBean.getData().setBanner(newBrandImage);
        }
        if (!StringUtil.isEmpty(newWechatPic)) {
            mShopBean.getData().setWechat(newWechatPic);
        }
        mShopBean.getData().setNotice(etBrandNotice.getText().toString().trim());
        mShopBean.getData().setTelephone(etBrandPhone.getText().toString().trim());
        mShopBean.getData().setScope(etBusinessCategor.getText().toString().trim());
        mShopBean.getData().setHours(businessHourStr);
        mShopBean.getData().setName(etBrandName.getText().toString().trim());
        mShopBean.getData().setSummary(etBrandBriefIntroduction.getText().toString().trim());
        if (!StringUtil.isEmpty(mShopBean.getData().getEnterprise())) {
            mShopBean.getData().setEnterprise(null);
        }
        tvRight.setEnabled(false);
        addDisposable(ApiRetrofit.getInstance().getApiService().updateShop(mShopBean.getData()), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBean) {
                Util.showToast(BrandSettingActivity.this, "修改成功");
                finish();
            }

            @Override
            public void onError(int errorCode, String msg) {
                tvRight.setEnabled(true);
                super.onError(errorCode, msg);
            }

        });
    }

    private boolean checkAddress() {
        boolean result = false;
        if (!StringUtil.isEmpty(address)) {
            String[] s = address.split(",");
            JsonObject biz_content = new JsonObject();
            biz_content.addProperty("province", s[0]);
            biz_content.addProperty("city", s[1]);
            biz_content.addProperty("district", s[2]);
            biz_content.addProperty("address", etBrandAdress.getText().toString().trim());
            address = biz_content.toString();
            mShopBean.getData().setAddress(address);
            result = true;
        } else {
            if (!StringUtil.isEmpty(mShopBean.getData().getAddress())) {
                return true;
            }
        }
        return result;
    }

    private boolean checkBackAddress() {
        boolean result = false;
        if (!StringUtil.isEmpty(backAddress)) {
            String[] s = backAddress.split(",");
            JsonObject biz_content = new JsonObject();
            biz_content.addProperty("province", s[0]);
            biz_content.addProperty("city", s[1]);
            biz_content.addProperty("district", s[2]);
            biz_content.addProperty("address", etBrandBackAdress.getText().toString().trim());
            biz_content.addProperty("name",etBrandBackName.getText().toString().trim());
            biz_content.addProperty("mobile",etBrandBackPhone.getText().toString().trim());
            backAddress = biz_content.toString();
            mShopBean.getData().setBackAddress(backAddress);
            result = true;
        } else {
            if (!StringUtil.isEmpty(mShopBean.getData().getBackAddress())) {
                return true;
            }
        }
        return result;
    }

    private boolean checkReceiveAddress() {
        boolean result = false;
        if (!StringUtil.isEmpty(reciveAddress)) {
            String[] s = reciveAddress.split(",");
            JsonObject biz_content = new JsonObject();
            biz_content.addProperty("province", s[0]);
            biz_content.addProperty("city", s[1]);
            biz_content.addProperty("district", s[2]);
            biz_content.addProperty("address", etBrandReceiveAdress.getText().toString().trim());
            biz_content.addProperty("name",etBrandReceiveName.getText().toString().trim());
            biz_content.addProperty("mobile",etBrandReceivePhone.getText().toString().trim());
            reciveAddress = biz_content.toString();
            mShopBean.getData().setReciveAddress(reciveAddress);
            result = true;
        } else {
            if (!StringUtil.isEmpty(mShopBean.getData().getReciveAddress())) {
                return true;
            }
        }
        return result;
    }

    @Override
    public void callback(String provinceName, int provinceCode, String cityName, int cityCode, String districtName, int districtCode) {
        if (selectTv != null) {
            selectTv.setText(provinceName + cityName + districtName);
            if (selectTv == tvBrandAdress) {
                address = provinceName + "," + cityName + "," + districtName;
            } else if (selectTv == tvBrandBackAdress) {
                backAddress = provinceName + "," + cityName + "," + districtName;
            } else if (selectTv == tvBrandReceiveAdress) {
                reciveAddress = provinceName + "," + cityName + "," + districtName;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
