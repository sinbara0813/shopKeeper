package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
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
import com.d2cmall.shopkeeper.holder.SelectProductHolder;
import com.d2cmall.shopkeeper.model.ProductBean;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;


/**
 * Created by LWJ on 2019/3/4.
 * Description : CollagePromotionAdapter
 */

public class CollagePromotionSelectProductAdapter extends DelegateAdapter.Adapter<SelectProductHolder> {

    private Context mContext;
    private RequestManager manager;
    private List<ProductBean> mProductBeanList;
    private int currentSelectPosition=-1;
    private long selectedProductId;

    private SelectedItemListener selectedItemListener;

    public void setSelectedItemListener(SelectedItemListener selectedItemListener) {
        this.selectedItemListener = selectedItemListener;
    }

    public CollagePromotionSelectProductAdapter(Context context,RequestManager manager,List<ProductBean> productBeanList, long productId){
        this.mContext=context;
        this.manager=manager;
        this.mProductBeanList=productBeanList;
        this.selectedProductId=productId;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public SelectProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_select_product_item,parent,false);
        return new SelectProductHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectProductHolder holder, int position) {
        ProductBean productBean = mProductBeanList.get(position);
        ImageLoader.displayImage(manager,productBean.getFirstPic(),holder.iv,R.mipmap.ic_logo_empty5,R.mipmap.ic_logo_empty5);
        holder.describeTv.setText(productBean.getName());
        holder.priceTv.setText(mContext.getString(R.string.label_money, Util.getNumberFormat(productBean.getPrice())));
        if( productBean.getId()==selectedProductId ){
            holder.checkIv.setImageResource(R.mipmap.icon_checkbox_all);
        }else{
            if(position==currentSelectPosition){
                holder.checkIv.setImageResource(R.mipmap.icon_checkbox_all);
            }else{
                holder.checkIv.setImageResource(R.mipmap.icon_checkbox_nor);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedProductId=-1;
                holder.checkIv.setImageResource(R.mipmap.icon_checkbox_all);
                if(currentSelectPosition!=position){
                    currentSelectPosition=position;
                    notifyDataSetChanged();
                    if(selectedItemListener!=null){
                        selectedItemListener.selectedItem(position);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductBeanList==null?0:mProductBeanList.size();
    }

    public interface SelectedItemListener{
        void selectedItem(int position);
    }
}
