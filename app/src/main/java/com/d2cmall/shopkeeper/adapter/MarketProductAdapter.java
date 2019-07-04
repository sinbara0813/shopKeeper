package com.d2cmall.shopkeeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.MarketProductHolder;
import com.d2cmall.shopkeeper.model.MarketProductListBean;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.ui.activity.MarketProductDetialActivity;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.d2cmall.shopkeeper.utils.ScreenUtil.dip2px;

/**
 * 作者:Created by sinbara on 2018/9/19.
 * 邮箱:hrb940258169@163.com
 */

public class MarketProductAdapter extends DelegateAdapter.Adapter<MarketProductHolder> {

    private int itemWidth;
    private Context context;
    private List<ProductBean> data;
    private boolean hasPadding;

    public MarketProductAdapter(Context context, List<ProductBean> data) {
        this.context = context;
        this.data = data;
        this.itemWidth = (ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(38)) / 2;
    }

    public MarketProductAdapter(Context context, List<ProductBean> data,boolean hasPadding) {
        this.context = context;
        this.data = data;
        this.itemWidth = (ScreenUtil.getDisplayWidth() - ScreenUtil.dip2px(38)) / 2;
        this.hasPadding=hasPadding;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        StaggeredGridLayoutHelper staggerLayoutHelper = new StaggeredGridLayoutHelper(2);
        //staggerLayoutHelper.setBgColor(context.getResources().getColor(R.color.shallow_white));
        staggerLayoutHelper.setPaddingLeft(dip2px(14));
        staggerLayoutHelper.setPaddingRight(dip2px(14));
        if (!hasPadding){
            staggerLayoutHelper.setPaddingTop(dip2px(14));
        }
        staggerLayoutHelper.setHGap(dip2px(10));
        staggerLayoutHelper.setVGap(dip2px(10));
        return staggerLayoutHelper;
    }

    @Override
    public MarketProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_market_product_item, parent, false);
        return new MarketProductHolder(view, itemWidth);
    }

    @Override
    public void onBindViewHolder(MarketProductHolder holder, int position) {
        ProductBean recordsBean = data.get(position);
        if (StringUtil.isEmpty(recordsBean.getName())){
            holder.productImage.setImageResource(0);
            holder.productName.setText("");
            holder.tagContent.setVisibility(View.GONE);
            holder.productPrice.setText("");
            holder.profitTv.setText("");
            return;
        }
        ImageLoader.displayImage((Activity) context, recordsBean.getFirstPic(), holder.productImage);
        holder.productName.setText(recordsBean.getName());
        setProductType(holder,recordsBean);
        holder.productPrice.setText("¥" + Util.getNumberFormat(recordsBean.getSupplyPrice()));
        holder.profitTv.setText("利润 ¥"+Util.getNumberFormat(recordsBean.getProfit()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MarketProductDetialActivity.class);
                intent.putExtra("id",recordsBean.getId());
                context.startActivity(intent);
            }
        });
    }

    private TextView getTagView(String text){
        TextView textView=new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,11);
        textView.setBackgroundResource(R.drawable.sp_round2_stroke_blue);
        textView.setTextColor(ContextCompat.getColor(context,R.color.color_blue6));
        textView.setText(text);
        return textView;
    }

    private void setProductType(MarketProductHolder holder,ProductBean recordsBean){
        holder.tagContent.removeAllViews();
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ScreenUtil.dip2px(52),ScreenUtil.dip2px(19));
        lp.setMargins(0,0,ScreenUtil.dip2px(10),0);
        if (recordsBean.getAllot()==1){
            holder.tagContent.addView(getTagView("免费拿货"),lp);
        }
        if (recordsBean.getBuyout()==1){
            holder.tagContent.addView(getTagView("支持买断"),lp);
        }
    }
  @Override
    public int getItemCount() {
        return data.size();
    }
}
