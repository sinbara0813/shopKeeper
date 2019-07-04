package com.d2cmall.shopkeeper.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.ShopBean;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.view.RoundedImageView;
import com.d2cmall.shopkeeper.ui.view.ScanCodePop;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.SharePop;
import com.d2cmall.shopkeeper.ui.view.fitStatusbar.Sofia;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LWJ on 2019/2/27.
 * Description : BrandManagerActivity
 * 店铺详情
 */

public class BrandManagerActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_brand_name)
    TextView tvBrandName;
    @BindView(R.id.tv_edit_brand)
    TextView tvEditBrand;
    @BindView(R.id.tv_brand_date)
    TextView tvBrandDate;
    @BindView(R.id.iv_brand_head_pic)
    RoundedImageView ivUserHeadPic;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_brand_level)
    TextView tvBrandLevel;
    @BindView(R.id.tv_brand_score)
    TextView tvBrandScore;
    @BindView(R.id.iv_brand_authentication)
    ImageView ivBrandAuthentication;
    @BindView(R.id.ll_brand_authentication)
    LinearLayout llBrandAuthentication;
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
    @BindView(R.id.tv_brand_qrcode)
    TextView tvBrandQrcode;
    @BindView(R.id.tv_preview_brand)
    TextView tvPreviewBrand;
    @BindView(R.id.tv_extension_brand)
    TextView tvExtensionBrand;
    private UserBean userBean;

    private ShopBean shopBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_brand_manager;
    }

    @Override
    public void doBusiness() {
        init();
    }

    private void init() {
        int width = ScreenUtil.getDisplayWidth()/3;
        tvBrandQrcode.setWidth(width+ScreenUtil.dip2px(10));
        tvPreviewBrand.setWidth(width+ScreenUtil.dip2px(10));
        tvExtensionBrand.setWidth(width+ScreenUtil.dip2px(10));
        Sofia.with(this).statusBarBackground(Color.parseColor("#4050D2"));
        ShadowDrawable.setShadowDrawable(tvBrandQrcode, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(16), 0, 0);
        ShadowDrawable.setShadowDrawable(tvPreviewBrand, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(16), 0, 0);
        ShadowDrawable.setShadowDrawable(tvExtensionBrand, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(16), 0, 0);
        userBean = Session.getInstance().getUserFromFile(this);
        if(userBean==null){
            return;
        }
        if(userBean.getShopId()<=0){
            Util.showToast(this,"请先创建店铺");
            startActivity(new Intent(this,CreateBrandActivity.class));
            finish();
        }
        loadBrandInfo();
    }

    private void loadBrandInfo() {
        addDisposable(ApiRetrofit.getInstance().getApiService().getBrandInfo(userBean.getShopId()), new BaseObserver<BaseModel<ShopBean>>() {
            @Override
            public void onSuccess(BaseModel<ShopBean> shopBean) {
                BrandManagerActivity.this.shopBean=shopBean.getData();
                tvBrandNotice.setText(shopBean.getData().getNotice());
                tvBrandName.setText(shopBean.getData().getName());
                tvBrandDate.setText(getString(R.string.label_shop_valid_time,shopBean.getData().getValidDate().substring(0,10)));
                tvBussinessHour.setText(shopBean.getData().getHours());
                if(shopBean.getData().getAuthenticate()==0){
                    ivBrandAuthentication.setImageResource(R.mipmap.pic_weirenzheng);
                }else{
                    ivBrandAuthentication.setImageResource(R.mipmap.pic_yirenzheng);
                }
                tvReturnAddress.setText(shopBean.getData().getReturnAddress());
                ImageLoader.displayRoundImage(BrandManagerActivity.this,shopBean.getData().getLogo(),ivUserHeadPic,R.mipmap.icon_default_avatar);
            }
        });
    }


    @OnClick({R.id.tv_brand_name,R.id.iv_back, R.id.tv_edit_brand, R.id.iv_brand_head_pic, R.id.ll_brand_authentication, R.id.ll_bussiness_hour, R.id.ll_staff_manager, R.id.tv_brand_qrcode, R.id.tv_preview_brand, R.id.tv_extension_brand,R.id.ll_return_address,R.id.ll_brand_notice})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_edit_brand:
                //编辑店铺信息
                if( Util.loginChecked(this,999)){
                    intent=new Intent(this,BrandSettingActivity.class);
                    startActivityForResult(intent, Constants.RequestCode.EDIT_BRAND_INFO);
                }
                break;
            case R.id.tv_brand_name:
            case R.id.iv_brand_head_pic:
                //店铺头像
                intent=new Intent(this,BrandSettingActivity.class);
                startActivityForResult(intent, Constants.RequestCode.EDIT_BRAND_INFO);
                break;
            case R.id.ll_brand_authentication:
                //认证
                if( Util.loginChecked(this,999)){
                    intent=new Intent(this,BrandAuthenticationActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_bussiness_hour:
            case R.id.ll_return_address:
            case R.id.ll_brand_notice:
                //营业时间
                if( Util.loginChecked(this,999)){
                    intent=new Intent(this,BrandSettingActivity.class);
                    startActivityForResult(intent, Constants.RequestCode.EDIT_BRAND_INFO);
                }
                break;
            case R.id.ll_staff_manager:
                //员工管理
                if( Util.loginChecked(this,999)){
                    intent=new Intent(this,StaffManagerListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_brand_qrcode:
                //二维码
                showScanCode();
                break;
            case R.id.tv_preview_brand:
                //预览店铺
                if (shopBean==null)return;
                intent=new Intent(this,WebActivity.class);
                if(shopBean.getName()!=null){
                    intent.putExtra("title",shopBean.getName());
                }
                intent.putExtra("url",Constants.SHARE_URL+shopBean.getId());
                startActivity(intent);
                break;
            case R.id.tv_extension_brand:
                //推广店铺
                share();
                break;
        }
    }

    private void showScanCode(){
        if (shopBean==null)return;
        ScanCodePop codePop=new ScanCodePop(this,Constants.SHARE_URL+shopBean.getId(),false);
        codePop.show(tvBrandQrcode);
    }

    private void share(){
        if (shopBean==null)return;
        if (Util.wxInstalled()) {
            SharePop pop = new SharePop(this);
            pop.setTitle(shopBean.getName());
            pop.setDescription("发现了一家好店，快来看看吧！");
            pop.setImage(StringUtil.getD2cPicUrl(shopBean.getLogo(), 100, 100), false);
            pop.setWebUrl(Constants.SHARE_URL+shopBean.getId());
            pop.show(tvExtensionBrand);
        } else {
            Util.showToast(this, "请先安装微信");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==Constants.RequestCode.EDIT_BRAND_INFO){
                String brandName = data.getStringExtra("brandName");
                if(!StringUtil.isEmpty(brandName)){
                    tvBrandName.setText(brandName);
                    if(shopBean!=null){
                        shopBean.setName(brandName);
                    }
                }
                String brandLogo = data.getStringExtra("brandLogo");
                if(!StringUtil.isEmpty(brandLogo)){
                    ImageLoader.displayImage(BrandManagerActivity.this,brandLogo,ivUserHeadPic,R.mipmap.icon_default_avatar);
                    if(shopBean!=null){
                        shopBean.setLogo(brandLogo);
                    }
                }
                String brandNotice = data.getStringExtra("brandNotice");
                if(!StringUtil.isEmpty(brandNotice)){
                    tvBrandNotice.setText(brandNotice);
                }
                String brandHour = data.getStringExtra("brandHour");
                if(!StringUtil.isEmpty(brandHour)){
                    tvBussinessHour.setText(brandHour);
                }
                String returnAddress = data.getStringExtra("returnAddress");
                if(!StringUtil.isEmpty(returnAddress)){
                    tvReturnAddress.setText(returnAddress);
                }
            }
        }
    }
}
