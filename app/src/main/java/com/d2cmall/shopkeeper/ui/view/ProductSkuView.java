package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者:Created by sinbara on 2019/2/21.
 * 邮箱:hrb940258169@163.com
 */

public class ProductSkuView extends LinearLayout {

    @BindView(R.id.standard_tv)
    TextView standardTv;
    @BindView(R.id.standard_et)
    ClearEditText standardEt;
    @BindView(R.id.retail_price_tv)
    TextView retailPriceTv;
    @BindView(R.id.retail_price_et)
    ClearEditText retailPriceEt;
    @BindView(R.id.store_tv)
    TextView storeTv;
    @BindView(R.id.store_et)
    ClearEditText storeEt;
    @BindView(R.id.delete_tv)
    TextView deleteTv;
    @BindView(R.id.supply_price_tv)
    TextView supplyPriceTv;
    @BindView(R.id.profit_tv)
    TextView profitTv;
    @BindView(R.id.ll_purchase_info)
    LinearLayout llPurchaseInfo;
    @BindView(R.id.purchase_info_line)
    View purchaseInfoLine;


    private ProductBean.SkuListBean skuListBean;
    private boolean unDelete;
    public boolean isDeleted;
    public long id;
    private double profit;
    private double mSupplyPrice;

    public ProductSkuView(Context context) {
        this(context, null);
    }

    public ProductSkuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_product_sku, this, true);
        ButterKnife.bind(this, this);
        setOrientation(LinearLayout.VERTICAL);
        setBackgroundResource(R.drawable.sp_round2_gray);
        deleteTv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (unDelete && event.getX() > (ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(70))) {
                    if (getParent() != null) {
                        ((ViewGroup) getParent()).removeView(ProductSkuView.this);
                    }
                    isDeleted = true;
                }
                return false;
            }
        });
        retailPriceEt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(llPurchaseInfo.getVisibility()==GONE){
                    return;
                }
                if(!hasFocus){
                    double salePrice = StringUtil.getDoubleFromText(retailPriceEt.getText().toString().trim());
                    String[] split = supplyPriceTv.getText().toString().trim().split(" ");
                    if(split!=null && split.length==2){
                        double supplyPrice = StringUtil.getDoubleFromText(split[1]);
                        profitTv.setText("利润 "+ Util.getNumberFormat((salePrice - supplyPrice)));
                    }
                }
            }
        });
    }

    public ProductBean.SkuListBean getSku() {
        String standard = standardEt.getText().toString();
        String price = retailPriceEt.getText().toString();
        String store = storeEt.getText().toString();
        if (StringUtil.isEmpty(standard)) {
            toast("请输入规格");
            return null;
        }
        if (StringUtil.isEmpty(price) || Double.valueOf(price) <= 0) {
            toast("请输入正确价格");
            return null;
        }
        if (StringUtil.isEmpty(store)) {
            toast("请输入库存");
            return null;
        }
        if (skuListBean == null) {
            skuListBean = new ProductBean.SkuListBean();
        }
        if (id != 0) {
            skuListBean.setId(id);
        }
        skuListBean.setSellPrice(Double.valueOf(price));
        skuListBean.setStandard(standard);
        skuListBean.setStock(Integer.valueOf(store));

        //设置进货价
        if(llPurchaseInfo.getVisibility()==VISIBLE){
            skuListBean.setSupplyPrice(mSupplyPrice);
        }
        return skuListBean;
    }

    private void toast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void setUnDelete(boolean unDelete) {
        this.unDelete = unDelete;
        if (unDelete) {
            Drawable drawable = getResources().getDrawable(R.mipmap.icon_delete);
            drawable.setBounds(0, 0, ScreenUtil.dip2px(14), ScreenUtil.dip2px(18));
            deleteTv.setCompoundDrawables(null, null, drawable, null);
        }
    }

    public void setStandard(String standard) {
        if (StringUtil.isEmpty(standard)) return;
        standardEt.setText(standard);
    }

    public void setPrice(double price) {
        retailPriceEt.setText(String.valueOf(price));
    }

    public void setStore(int num) {
        storeEt.setText(String.valueOf(num));
    }


    public void setSupplyPrice(double supplyPrice) {
        mSupplyPrice = supplyPrice;
        if(llPurchaseInfo.getVisibility()==GONE){
            llPurchaseInfo.setVisibility(VISIBLE);
            purchaseInfoLine.setVisibility(VISIBLE);
        }
        supplyPriceTv.setText("进价 "+supplyPrice);
        double salePrice = StringUtil.getDoubleFromText(retailPriceEt.getText().toString().trim());
        profitTv.setText("利润 "+ Util.getNumberFormat((salePrice - supplyPrice)));
    }

    public void setId(long id) {
        this.id = id;
    }

    //调拨/买断入库的商品不支持修改库存
    public void setNoEditStore() {
        storeEt.setFocusable(false);
        storeEt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("调拨/采购商品不能修改库存");
            }
        });
    }

}
