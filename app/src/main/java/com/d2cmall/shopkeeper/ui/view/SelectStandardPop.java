package com.d2cmall.shopkeeper.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Action;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.view.flowLayout.FlowLayout;
import com.d2cmall.shopkeeper.ui.view.flowLayout.TagAdapter;
import com.d2cmall.shopkeeper.ui.view.flowLayout.TagFlowLayout;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者:Created by sinbara on 2019/5/6.
 * 邮箱:hrb940258169@163.com
 */

public class SelectStandardPop implements TransparentPop.InvokeListener {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.standard_tv)
    TextView standardTv;
    @BindView(R.id.price_tv)
    TextView priceTv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.standard_tl)
    TagFlowLayout standardTl;
    @BindView(R.id.num_tag_tv)
    TextView numTagTv;
    @BindView(R.id.minus_iv)
    ImageView minusIv;
    @BindView(R.id.add_iv)
    ImageView addIv;
    @BindView(R.id.num_et)
    EditText numEt;
    private Context mContext;
    private TransparentPop mPop;
    private View standardView;

    private int currentNum=1;
    private int type;
    private long skuId;
    private ProductBean productBean;
    private boolean isOffline;

    public SelectStandardPop(Context context) {
        this.mContext = context;
        standardView = LayoutInflater.from(context).inflate(R.layout.layout_select_standard, new CoordinatorLayout(context), false);
        ButterKnife.bind(this, standardView);
        mPop = new TransparentPop(context, this);
        numEt.setCursorVisible(false);
        numEt.setEnabled(false);
        numEt.setTextColor(context.getResources().getColor(R.color.color_black85));
    }

    public void show(View view) {
        mPop.show(view, false);
    }

    public void dismiss() {
        mPop.dismiss(false);
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.CENTER);
        v.addView(standardView);
    }

    @Override
    public void releaseView(LinearLayout v) {
    }

    @OnClick({R.id.close_iv, R.id.minus_iv, R.id.add_iv, R.id.sure_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                dismiss();
                break;
            case R.id.minus_iv:
                changeNum(-1);
                break;
            case R.id.add_iv:
                changeNum(1);
                break;
            case R.id.sure_tv:
                if (skuId>0&&currentNum>0){
                    Action action=new Action(Constants.ActionType.CART_OFFLINE_ADD);
                    action.put("type",type);
                    action.put("skuId",skuId);
                    action.put("num",currentNum);
                    EventBus.getDefault().post(action);
                    dismiss();
                }else {
                    Util.showToast(mContext,"请选择规格和数量!");
                }
                break;
        }
    }

    public void setData(ProductBean productBean){
        this.productBean=productBean;
        ImageLoader.displayImage((Activity) mContext,productBean.getFirstPic(),iv);
        nameTv.setText(productBean.getName());
        setStandard();
    }

    private void setStandard(){
        standardTl.setMaxSelectCount(1);
        List<String> standards = new ArrayList<>();
        for (int i = 0; i < productBean.getSkuList().size(); i++) {
            standards.add(productBean.getSkuList().get(i).getStandard());
        }
        TagAdapter<String> tagAdapter = new TagAdapter<String>(standards) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.layout_tag,
                        parent, false);
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tv.getLayoutParams();
                if ((position + 1) % 3 == 0) {
                    layoutParams.rightMargin = 0;
                } else {
                    layoutParams.rightMargin = ScreenUtil.dip2px(14);
                }
                layoutParams.width = ScreenUtil.dip2px(89);
                tv.setText(s);
                tv.setTag(productBean.getSkuList().get(position).getId());
                if (productBean.getSkuList().get(position).getStock()<=0){
                    tv.setTextColor(Color.parseColor("#CDCDD7"));
                    tv.setBackgroundResource(R.drawable.sp_standard_enable);
                    standardTl.setEnable(position,true);
                    tv.setEnabled(false);
                }
                return tv;
            }
        };
        standardTl.setAdapter(tagAdapter);
        standardTl.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                Object[] array = selectPosSet.toArray();
                if (array.length >= 1) {
                    int position = (int) array[0];
                    selectStandard(position);
                } else {
                    standardTv.setText("请选择规格");
                    skuId = 0;
                }
            }
        });
    }

    private void selectStandard(int position){
        if (isOffline){
            priceTv.setText("¥ "+ Util.getNumberFormat(productBean.getSkuList().get(position).getSellPrice()));
        }else {
            priceTv.setText("¥ "+ Util.getNumberFormat(productBean.getSkuList().get(position).getSupplyPrice()));
        }
        standardTv.setText("已选择 "+productBean.getSkuList().get(position).getStandard()+" | "+"库存"+productBean.getSkuList().get(position).getStock()+"件");
        skuId=productBean.getSkuList().get(position).getId();
    }

    /**
     *
     * @param type -1 减 1 加
     */
    private void changeNum(int type){
        if (type>0){
            currentNum++;
        }else {
            currentNum--;
        }
        if (currentNum<=1){
            minusIv.setImageResource(R.mipmap.icon_jian_hui);
            minusIv.setEnabled(false);
        }else if (currentNum==2){
            minusIv.setImageResource(R.mipmap.icon_jian_lan);
            minusIv.setEnabled(true);
        }
        numEt.setText(String.valueOf(currentNum));
    }

    public int getCurrentNum() {
        return currentNum;
    }

    public long getSkuId() {
        return skuId;
    }

    public void setType(int type){
        this.type=type;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }
}
