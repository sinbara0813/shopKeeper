package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.CollagePromotionSelectProductHolder;
import com.d2cmall.shopkeeper.holder.SelectProductConfirmHolder;
import com.d2cmall.shopkeeper.holder.SelectProductHolder;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

/**
 * Created by LWJ on 2019/3/4.
 * Description : CollagePromotionAdapter
 */

public class SelectProductConfirmAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private RequestManager manager;
    private ProductBean mProductBean;
    private CollagePromotionSelectProductHolder collagePromotionSelectProductHolder;

    public SelectProductConfirmAdapter(Context context,RequestManager manager){
        this.mContext=context;
        this.manager=manager;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==1){
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_select_product_confirm_item,parent,false);
            return new CollagePromotionSelectProductHolder(view);

        }else{
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_product_spec_item,parent,false);
            return new SelectProductConfirmHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position==0){
            collagePromotionSelectProductHolder = (CollagePromotionSelectProductHolder) holder;
            ImageLoader.displayImage(manager,mProductBean.getFirstPic(), collagePromotionSelectProductHolder.iv,R.mipmap.ic_logo_empty5,R.mipmap.ic_logo_empty5);
            collagePromotionSelectProductHolder.describeTv.setText(mProductBean.getName());
            if(mProductBean.getCrowdPrice()>0){
                collagePromotionSelectProductHolder.etPromotionPrice.setText(mContext.getString(R.string.label_money, Util.getNumberFormat(mProductBean.getCrowdPrice())));
            }
            collagePromotionSelectProductHolder.priceTv.setText(mContext.getString(R.string.label_money, Util.getNumberFormat(mProductBean.getPrice())));
        }else{
            SelectProductConfirmHolder selectProductConfirmHolder = (SelectProductConfirmHolder) holder;
            ProductBean.SkuListBean skuListBean = mProductBean.getSkuList().get(position - 1);
            selectProductConfirmHolder.tvProductPrice.setText(mContext.getString(R.string.label_money, Util.getNumberFormat(skuListBean.getSellPrice())));
            selectProductConfirmHolder.tvProductSpec.setText(skuListBean.getStandard());
            selectProductConfirmHolder.tvProductStock.setText(skuListBean.getStock()+"件");
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 1;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if(mProductBean==null){
            return 0;
        }
        return mProductBean.getSkuList()==null?0:mProductBean.getSkuList().size()+1;
    }

    public void setProductBean(ProductBean productBean) {
        this.mProductBean = productBean;
    }

    public double getPromotionPrice() {
        if(collagePromotionSelectProductHolder!=null){
            String trim = collagePromotionSelectProductHolder.etPromotionPrice.getText().toString().trim();
            if(StringUtil.isEmpty(trim)){
                return 0.0;
            }
            if(trim.contains("¥")){
                trim=trim.replace("¥","");
            }
            if(trim.contains(",")){
                trim=trim.replace(",","");
            }
            return Double.valueOf(trim);
        }
        return 0.0;
    }
}
