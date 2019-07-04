package com.d2cmall.shopkeeper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseTabFragment;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Address;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.activity.BrandAuthenticationActivity;
import com.d2cmall.shopkeeper.ui.activity.BrandSettingActivity;
import com.d2cmall.shopkeeper.ui.activity.SetActivity;
import com.d2cmall.shopkeeper.ui.activity.StaffManagerListActivity;
import com.d2cmall.shopkeeper.ui.view.AddressPop;
import com.d2cmall.shopkeeper.ui.view.SelectBusinessHourPop;
import com.d2cmall.shopkeeper.ui.view.ShopAddressPop;
import com.d2cmall.shopkeeper.ui.view.ShopCategoryPop;
import com.d2cmall.shopkeeper.ui.view.ShopNoticePop;
import com.d2cmall.shopkeeper.ui.view.TransparentPop;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/6/18.
 * 邮箱:hrb940258169@163.com
 */
public class ShopFragment extends BaseTabFragment implements AddressPop.CallBack {

    @BindView(R.id.status_bar_line)
    View statusBarLine;
    @BindView(R.id.set_tv)
    TextView setTv;
    @BindView(R.id.iv_brand_head_pic)
    ImageView ivBrandHeadPic;
    @BindView(R.id.tv_brand_name)
    TextView tvBrandName;
    @BindView(R.id.tv_edit_brand)
    TextView tvEditBrand;
    @BindView(R.id.tv_brand_date)
    TextView tvBrandDate;
    @BindView(R.id.iv_brand_authentication)
    ImageView ivBrandAuthentication;
    @BindView(R.id.ll_brand_authentication)
    LinearLayout llBrandAuthentication;
    @BindView(R.id.tv_bussiness_category)
    TextView tvBussinessCategory;
    @BindView(R.id.ll_bussiness_category)
    LinearLayout llBussinessCategory;
    @BindView(R.id.tv_bussiness_hour)
    TextView tvBussinessHour;
    @BindView(R.id.ll_bussiness_hour)
    LinearLayout llBussinessHour;
    @BindView(R.id.tv_return_address)
    TextView tvReturnAddress;
    @BindView(R.id.ll_return_address)
    LinearLayout llReturnAddress;
    @BindView(R.id.tv_staff_num)
    TextView tvStaffNum;
    @BindView(R.id.ll_staff_manager)
    LinearLayout llStaffManager;
    @BindView(R.id.tv_brand_notice)
    TextView tvBrandNotice;
    @BindView(R.id.ll_brand_notice)
    LinearLayout llBrandNotice;
    @BindView(R.id.line3)
    View line3;

    private ShopBean bean;
    private UserBean userBean;
    private boolean isFirst = true;
    private String businessHourStr;
    private String provinceInfo;
    private ShopAddressPop shopAddressPop;
    private String provinceName,cityName,districtName;
    private InputMethodManager im;

    @Override
    public void callback(String provinceName, int provinceCode, String cityName, int cityCode, String districtName, int districtCode) {
        if (shopAddressPop!=null){
            shopAddressPop.setAddress(provinceName+cityName+districtName);
        }
        this.provinceName=provinceName;
        this.cityName=cityName;
        this.districtName=districtName;
    }

    @Override
    public View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    public void doBusiness() {
        im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
        ConstraintLayout.LayoutParams cl = (ConstraintLayout.LayoutParams) statusBarLine.getLayoutParams();
        cl.setMargins(0, ScreenUtil.getStatusBarHeight(mContext), 0, 0);
        loadBrandInfo();
    }



    private void loadBrandInfo() {
        userBean = Session.getInstance().getUserFromFile(mContext);
        if (userBean != null && userBean.getShopId() > 0) {
            addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(userBean.getShopId()), new BaseObserver<BaseModel<ShopBean>>() {
                @Override
                public void onSuccess(BaseModel<ShopBean> shopBean) {
                    bean = shopBean.getData();
                    tvBrandNotice.setText(shopBean.getData().getNotice());
                    tvEditBrand.setText(shopBean.getData().getFocusCount() + "人关注");
                    tvBrandName.setText(shopBean.getData().getName());
                    tvBrandDate.setText(getString(R.string.label_shop_valid_time, shopBean.getData().getValidDate().substring(0, 10)));
                    tvBussinessHour.setText(shopBean.getData().getHours());
                    tvBussinessCategory.setText(shopBean.getData().getScope());
                    if (shopBean.getData().getAuthenticate() == 0) {
                        ivBrandAuthentication.setImageResource(R.mipmap.pic_weirenzheng);
                    } else {
                        ivBrandAuthentication.setImageResource(R.mipmap.pic_yirenzheng);
                    }
                    if (!StringUtil.isEmpty(shopBean.getData().getAddress())) {
                        if (shopBean.getData().getAddress().startsWith("{")) {
                            Gson gson = new Gson();
                            Address address = gson.fromJson(shopBean.getData().getAddress(), Address.class);
                            tvReturnAddress.setText(address.province + address.city + address.district + address.address);
                        } else {
                            tvReturnAddress.setText(shopBean.getData().getAddress());
                        }
                    }
                    if (!StringUtil.isEmpty(shopBean.getData().getHours())){
                        businessHourStr = shopBean.getData().getHours();
                    }
                    ImageLoader.displayRoundImage(getActivity(), shopBean.getData().getLogo(), ivBrandHeadPic, R.mipmap.icon_default_avatar);
                    if ("BOSS".equals(userBean.getRole())){
                        line3.setVisibility(View.VISIBLE);
                        llStaffManager.setVisibility(View.VISIBLE);
                    }else {
                        line3.setVisibility(View.GONE);
                        llStaffManager.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    @OnClick({R.id.ll_bussiness_category,R.id.tv_brand_name, R.id.tv_edit_brand, R.id.iv_brand_head_pic, R.id.ll_brand_authentication, R.id.ll_bussiness_hour, R.id.ll_staff_manager, R.id.ll_return_address, R.id.ll_brand_notice, R.id.set_tv})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_brand_name:
                if (bean != null) {
                    intent = new Intent(mContext, BrandSettingActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.tv_edit_brand:
                //关注人数
                /*if( Util.loginChecked(getActivity(),999)){
                    intent=new Intent(mContext, BrandSettingActivity.class);
                    startActivityForResult(intent, Constants.RequestCode.EDIT_BRAND_INFO);
                }*/
                if (bean != null) {
                    intent = new Intent(mContext, BrandSettingActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.iv_brand_head_pic:
                //店铺头像
                if (bean != null) {
                    intent = new Intent(mContext, BrandSettingActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.ll_brand_authentication:
                //认证
                if (Util.loginChecked(getActivity(), 999)) {
                    intent = new Intent(mContext, BrandAuthenticationActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.ll_bussiness_category://品类
                showCategoryPop(view);
                break;
            case R.id.ll_bussiness_hour: //营业时间
                showSelectBusinessHourPop(view);
                break;
            case R.id.ll_return_address: //店铺地址
                showPop(view);
                break;
            case R.id.ll_brand_notice: //店铺公告
                showNoticePop(view);
                break;
            case R.id.ll_staff_manager:
                //员工管理
                if (Util.loginChecked(getActivity(), 999)) {
                    intent = new Intent(mContext, StaffManagerListActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.set_tv:
                intent = new Intent(mContext, SetActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }

    private void showCategoryPop(View view){
        ShopCategoryPop categoryPop=new ShopCategoryPop(mContext);
        categoryPop.setCategory(bean.getScope());
        categoryPop.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=categoryPop.getCategory();
                if (StringUtil.isEmpty(category)){
                    Util.showToast(mContext,"请输入");
                    return;
                }
                categoryPop.dismiss();
                bean.setScope(category);
                tvBussinessCategory.setText(category);
                updateShop();
            }
        });
        categoryPop.show(view);
    }

    private void showNoticePop(View view){
        ShopNoticePop categoryPop=new ShopNoticePop(mContext);
        categoryPop.setCategory(bean.getNotice());
        categoryPop.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category=categoryPop.getNotice();
                if (StringUtil.isEmpty(category)){
                    Util.showToast(mContext,"请输入");
                    return;
                }
                categoryPop.dismiss();
                tvBrandNotice.setText(category);
                bean.setNotice(category);
                updateShop();
            }
        });
        categoryPop.show(view);
    }

    private void showSelectBusinessHourPop(View view) {
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
        SelectBusinessHourPop selectBusinessHourPop = new SelectBusinessHourPop(mContext, openHourHours, openHourMinutes, closeHourHours, closeHourMinutes);
        selectBusinessHourPop.setDismissCallBack(new TransparentPop.DismissListener() {
            @Override
            public void dismissStart() {
                if (selectBusinessHourPop != null) {
                    if (!StringUtil.isEmpty(selectBusinessHourPop.getBusinessHourStr())) {
                        businessHourStr = selectBusinessHourPop.getBusinessHourStr();
                        if (!StringUtil.isEmpty(businessHourStr) && !"00:00-00:00".equals(businessHourStr)) {
                            tvBussinessHour.setText(businessHourStr);
                            if (bean!=null){
                                bean.setHours(businessHourStr);
                                updateShop();
                            }
                        }
                    }
                }
            }

            @Override
            public void dismissEnd() {

            }
        });
        selectBusinessHourPop.show(view);
    }

    private void showPop(View view){
        if (shopAddressPop==null){
            shopAddressPop=new ShopAddressPop(mContext);
            shopAddressPop.setListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String region=shopAddressPop.getRegion();
                    String a=shopAddressPop.getDetailAddress();
                    if (StringUtil.isEmpty(region)||StringUtil.isEmpty(a)){
                        Util.showToast(mContext,"地址不完善");
                        return;
                    }
                    shopAddressPop.dismiss();
                    tvReturnAddress.setText(region+a);
                    if (bean!=null){
                        JsonObject biz_content=new JsonObject();
                        biz_content.addProperty("province",provinceName);
                        biz_content.addProperty("city",cityName);
                        biz_content.addProperty("district",districtName);
                        biz_content.addProperty("address",a);
                        String s=biz_content.toString();
                        bean.setAddress(s);
                        updateShop();
                    }
                }
            });
            shopAddressPop.setAddressListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shopAddressPop.hideSoft();
                    showAddressPop(view);
                }
            });
        }
        if (bean!=null){
            if (!StringUtil.isEmpty(bean.getAddress())) {
                if (bean.getAddress().startsWith("{")){
                    Gson gson=new Gson();
                    Address add=gson.fromJson(bean.getAddress(),Address.class);
                    shopAddressPop.setAddress(add.province+add.city+add.district);
                    shopAddressPop.setNegion(add.address);
                }
            }
        }
        shopAddressPop.show(view);
    }

    private void showAddressPop(View view){
        if (getActivity().getCurrentFocus() != null) {
            im.hideSoftInputFromWindow(this.getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        AddressPop addressPop = new AddressPop(mContext, provinceInfo);
        addressPop.setCallBack(this);
        addressPop.show(view);
    }

    private void updateShop(){
        addDisposable(ApiRetrofit.getInstance().getApiService().updateShop(bean), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst && mContext != null) {
            loadBrandInfo();
        }
        if (isFirst) {
            isFirst = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == Constants.RequestCode.EDIT_BRAND_INFO) {
                String brandName = data.getStringExtra("brandName");
                if (!StringUtil.isEmpty(brandName)) {
                    tvBrandName.setText(brandName);
                    if (bean != null) {
                        bean.setName(brandName);
                    }
                }
                String brandLogo = data.getStringExtra("brandLogo");
                if (!StringUtil.isEmpty(brandLogo)) {
                    ImageLoader.displayImage(getActivity(), brandLogo, ivBrandHeadPic, R.mipmap.icon_default_avatar);
                    if (bean != null) {
                        bean.setLogo(brandLogo);
                    }
                }
                String brandNotice = data.getStringExtra("brandNotice");
                if (!StringUtil.isEmpty(brandNotice)) {
                    tvBrandNotice.setText(brandNotice);
                }
                String brandHour = data.getStringExtra("brandHour");
                if (!StringUtil.isEmpty(brandHour)) {
                    tvBussinessHour.setText(brandHour);
                }
                String returnAddress = data.getStringExtra("returnAddress");
                if (!StringUtil.isEmpty(returnAddress)) {
                    tvReturnAddress.setText(returnAddress);
                }
            }
        }
    }

    @Override
    public void show() {
        super.show();
        if (!isFirst && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getActivity().getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void onDestroyView() {
        if (shopAddressPop!=null){
            shopAddressPop.setAddressListener(null);
            shopAddressPop.setListener(null);
        }
        super.onDestroyView();
    }
}
