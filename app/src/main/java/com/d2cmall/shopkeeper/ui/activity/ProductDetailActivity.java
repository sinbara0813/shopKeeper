package com.d2cmall.shopkeeper.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
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
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.share.WxHandle;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.ui.view.SharePop;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/3/6.
 * 邮箱:hrb940258169@163.com
 * 商品详情
 */

public class ProductDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.edit_tv)
    TextView editTv;
    @BindView(R.id.sold_tv)
    TextView soldTv;
    @BindView(R.id.share_tv)
    TextView shareTv;
    @BindView(R.id.delete_tv)
    TextView deleteTv;

    private long id;
    private int status;
    private ProductBean productBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    public void doBusiness() {
        id = getIntent().getLongExtra("id", 0);
        status = getIntent().getIntExtra("status", 0);
        if (status == 0) {
            soldTv.setText("上架");
        } else {
            soldTv.setText("下架");
        }
        setShadowDrawable();
        addDisposable(ApiRetrofit.getInstance().getApiService().getProduct(id), new BaseObserver<BaseModel<ProductBean>>() {
            @Override
            public void onSuccess(BaseModel<ProductBean> o) {
                productBean = o.getData();
                tvName.setText(o.getData().getName());
                setContentView();
            }

            @Override
            public void onError(int errorCode, String msg) {
                super.onError(errorCode, msg);
                finish();
            }
        });
    }

    private void setShadowDrawable() {
        ShadowDrawable.setShadowDrawable(rlTitle, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), 0, 0, 0, ScreenUtil.dip2px(16), 0, 0);
        ShadowDrawable.setShadowDrawable(editTv, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(8), 0, 0);
        ShadowDrawable.setShadowDrawable(soldTv, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(8), 0, 0);
        ShadowDrawable.setShadowDrawable(shareTv, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(8), 0, 0);
        ShadowDrawable.setShadowDrawable(deleteTv, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                Color.parseColor("#11000000"), ScreenUtil.dip2px(8), 0, 0);
    }

    private void setContentView() {
        String[] pics = productBean.getPic().split(",");
        int size = pics.length;
        for (int i = 0; i < size; i++) {
            LinearLayout.LayoutParams LL = new LinearLayout.LayoutParams(-1, -2);
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.displayImage(this, StringUtil.getD2cPicUrlByWidth(pics[i],ScreenUtil.getDisplayWidth()), imageView);
            if (i != 0) {
                LL.setMargins(0, ScreenUtil.dip2px(8), 0, 0);
            }
            content.addView(imageView, LL);
        }
    }

    @OnClick({R.id.iv_back, R.id.edit_tv, R.id.sold_tv, R.id.share_tv, R.id.delete_tv})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.edit_tv:
                Intent intent = new Intent(this, AddProductActivity.class);
                intent.putExtra("product", productBean);
                startActivityForResult(intent, 100);
                break;
            case R.id.sold_tv:
                productStatus();
                break;
            case R.id.share_tv:
                showSharePop();
                break;
            case R.id.delete_tv:
                productDelete();
                break;
        }
    }

    private void showSharePop() {
        if (productBean==null)return;
        if (Util.wxInstalled()) {
            SharePop pop = new SharePop(this);
            pop.setTitle(productBean.getName());
            pop.setDescription("发现了一个好东西，赶紧看看吧！");
            String[] imgs=productBean.getPic().split(",");
            pop.setImage(StringUtil.getD2cPicUrl(imgs[0], 100, 100), false);
            pop.setWebUrl(Constants.SHARE_URL+"product/"+productBean.getId());
            pop.show(shareTv);
        } else {
            Util.showToast(this, "请先安装微信");
        }
    }

    private void productStatus(){
        addDisposable(ApiRetrofit.getInstance().getApiService().productStatus(productBean.getId(), status==0?1:0), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                if (status == 0) {
                    Util.showToast(ProductDetailActivity.this, "上架成功");
                } else {
                    Util.showToast(ProductDetailActivity.this, "下架成功");
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void productDelete(){
        new AlertDialog.Builder(this)
                .setMessage("确定删除该商品吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addDisposable(ApiRetrofit.getInstance().getApiService().productDelete(productBean.getId()), new BaseObserver() {
                            @Override
                            public void onSuccess(Object o) {
                                Util.showToast(ProductDetailActivity.this, "删除成功");
                                setResult(RESULT_OK);
                                finish();
                            }
                        });
                    }
                })
                .setNegativeButton("取消", null)
                .show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
