package com.d2cmall.shopkeeper.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.mvp.BaseModel;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.listener.CheckStateChangeListener;
import com.d2cmall.shopkeeper.model.BaseCartBean;
import com.d2cmall.shopkeeper.model.CartBean;
import com.d2cmall.shopkeeper.model.EmptyBean;
import com.d2cmall.shopkeeper.ui.activity.MarketProductDetialActivity;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Util;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者:Created by sinbara on 2019/5/10.
 * 邮箱:hrb940258169@163.com
 */

public class CartProductItemView extends RelativeLayout {

    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.standard_tv)
    TextView standardTv;
    @BindView(R.id.select_standard_tv)
    TextView selectStandardTv;
    @BindView(R.id.price_tv)
    TextView priceTv;
    @BindView(R.id.minus_iv)
    ImageView minusIv;
    @BindView(R.id.add_iv)
    ImageView addIv;
    @BindView(R.id.num_tv)
    TextView numTv;
    @BindView(R.id.quantity_tv)
    TextView quantityTv;
    @BindView(R.id.line)
    View line;

    private boolean isEdit;
    private BaseCartBean cartBean;
    private Context context;
    private int currentNum;
    private boolean isOffline;

    public CartProductItemView(Context context) {
        this(context, null);
    }

    public CartProductItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_cart_product_item, this, true);
        setPadding(ScreenUtil.dip2px(18),ScreenUtil.dip2px(14),0,0);
        ButterKnife.bind(this, this);
        init();
    }

    private void init() {
    }

    public void setData(BaseCartBean cartBean){
        this.cartBean = cartBean;
        this.currentNum=cartBean.getQuantity();
        if (cartBean instanceof CartBean){
            checkbox.setChecked(((CartBean)cartBean).isChecked());
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MarketProductDetialActivity.class);
                    intent.putExtra("id",((CartBean)cartBean).getProductId());
                    context.startActivity(intent);
                }
            });
        }
        setName();
        ImageLoader.displayImage((Activity)context,cartBean.getProductPic(),iv);
        standardTv.setText("规格: " + cartBean.getStandard());
        selectStandardTv.setText("规格: " + cartBean.getStandard());
        quantityTv.setText("X" + cartBean.getQuantity());
        numTv.setText(String.valueOf(cartBean.getQuantity()));
        if (isOffline){
            priceTv.setTextColor(getResources().getColor(R.color.color_red));
            priceTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
            priceTv.setText("¥"+Util.getNumberFormat(cartBean.getProductPrice()));
        }else {
            priceTv.setText(Util.getCartPriceSpan(context, cartBean.getProductPrice(), cartBean.getProfit()));
        }
        updateViewState();
        checkbox.setOnStatusChangedListener(new CheckStateChangeListener() {
            @Override
            public void onStatusChanged(View v, boolean isChecked) {
                ((CartBean)cartBean).setChecked(isChecked);
                Action action=new Action(Constants.ActionType.CART_REFRESH);
                EventBus.getDefault().post(action);
            }
        });
    }

    private void setName(){
        StringBuilder stringBuilder = new StringBuilder();
        TagSpan allotTag=null;
        TagSpan purchaseTag=null;
        if (cartBean.getAllot()==1){
            stringBuilder.append("免费拿");
            allotTag=new TagSpan(context,R.color.allot_tag);
        }
        if (cartBean.getBuyout()==1){
            stringBuilder.append("采购");
            purchaseTag=new TagSpan(context,R.color.purchase_tag);
        }
        stringBuilder.append(cartBean.getProductName());
        SpannableString spannableString = new SpannableString(stringBuilder.toString());
        if (allotTag!=null){
            spannableString.setSpan(allotTag, 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (purchaseTag!=null){
                spannableString.setSpan(purchaseTag,3,5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }else {
            if (purchaseTag!=null){
                spannableString.setSpan(purchaseTag,0,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        info.setText(spannableString);
    }

    public void setEdit(boolean isEdit){
        if (isEdit){
            priceTv.setVisibility(GONE);
            quantityTv.setVisibility(GONE);
            minusIv.setVisibility(VISIBLE);
            numTv.setVisibility(VISIBLE);
            addIv.setVisibility(VISIBLE);
        }else {
            priceTv.setVisibility(VISIBLE);
            quantityTv.setVisibility(VISIBLE);
            minusIv.setVisibility(GONE);
            numTv.setVisibility(GONE);
            addIv.setVisibility(GONE);
        }
    }

    @OnClick({R.id.minus_iv, R.id.add_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.minus_iv:
                updateCart(-1,currentNum-1);
                break;
            case R.id.add_iv:
                updateCart(1,currentNum+1);
                break;
        }
    }

    private void updateCart(int type,int num){
        ApiRetrofit.getInstance().getApiService().cartUpdate(cartBean.id,num).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<BaseModel<EmptyBean>>() {
                    @Override
                    public void onSuccess(BaseModel<EmptyBean> o) {
                        Util.showToast(context,"更新成功");
                        if (type<0){
                            currentNum--;
                        }else {
                            currentNum++;
                            updateViewState();
                        }
                        numTv.setText(String.valueOf(currentNum));
                        quantityTv.setText("X"+currentNum);
                        cartBean.setQuantity(currentNum);
                    }
                });
    }

    public void updateViewState(){
        if (currentNum<=1){
            minusIv.setImageResource(R.mipmap.icon_qingdan_bujian);
            minusIv.setEnabled(false);
        }else {
            if (!minusIv.isEnabled()){
                minusIv.setImageResource(R.mipmap.icon_qingdan_jian);
                minusIv.setEnabled(true);
            }
        }
    }

    public void hideCheckBox(){
        checkbox.setVisibility(GONE);
    }

    public boolean isChecked(){
        return checkbox.isChecked();
    }

    public boolean isAllot(){
        return cartBean.getAllot()==1;
    }

    public boolean isPurchase(){
        return cartBean.getBuyout()==1;
    }

    public void setCheck(boolean is){
        ((CartBean)cartBean).setChecked(is);
        checkbox.setChecked(is);
    }

    public long getCartId(){
        return cartBean.id;
    }

    public void hideLine(boolean is){
        if (is){
            line.setVisibility(GONE);
        }else {
            line.setVisibility(VISIBLE);
        }
    }

    public void setOffline(){
        isOffline=true;
    }
}
