package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.holder.CollageSelectCouponHolder;
import com.d2cmall.shopkeeper.model.CouponBean;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;


/**
 * Created by LWJ on 2019/3/4.
 * Description : CollagePromotionAdapter
 */

public class CollagePromotionSelectCouponAdapter extends DelegateAdapter.Adapter<CollageSelectCouponHolder> {

    private Context mContext;
    private List<CouponBean> mCouponList;
    private long selectedCouponId;
    private CollagePromotionSelectProductAdapter.SelectedItemListener selectedItemListener;
    private int currentSelectPosition=-1;

    public void setSelectedItemListener(CollagePromotionSelectProductAdapter.SelectedItemListener selectedItemListener) {
        this.selectedItemListener = selectedItemListener;
    }
    public CollagePromotionSelectCouponAdapter(Context context, List<CouponBean> couponList, long couponId){
        this.mContext=context;
        this.mCouponList=couponList;
        this.selectedCouponId=couponId;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public CollageSelectCouponHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.layout_collage_select_coupon_item,parent,false);
        return new CollageSelectCouponHolder(view);
    }

    @Override
    public void onBindViewHolder(CollageSelectCouponHolder holder, int position) {
        CouponBean recordsBean = mCouponList.get(position);
        holder.nameTv.setText(recordsBean.getName());
        holder.describeTv.setText(mContext.getString(R.string.label_coupon_discount, Util.getNumberFormat(recordsBean.getNeedAmount()),Util.getNumberFormat(recordsBean.getAmount())));
        holder.countTv.setText(mContext.getString(R.string.label_coupon_num,recordsBean.getCirculation(),recordsBean.getConsumption(),recordsBean.getRestriction()));
        holder.productTagTv.setText(recordsBean.getTypeName());
//        if(!StringUtil.isEmpty(recordsBean.getServiceStartDate()) && !StringUtil.isEmpty(recordsBean.getServiceEndDate())){
//            holder.validTimeTv.setText(mContext.getString(R.string.label_collage_valid_time,recordsBean.getServiceStartDate().substring(0,11),recordsBean.getServiceEndDate().substring(0,11)));
//        }else{
//            holder.validTimeTv.setText(mContext.getString(R.string.label_collage_valid_time2,recordsBean.getServiceSustain()));
//        }
        holder.validTimeTv.setText(mContext.getString(R.string.label_collage_valid_time,recordsBean.getReceiveStartDate().substring(0,11),recordsBean.getReceiveEndDate().substring(0,11)));
        if(recordsBean.getId()!=null && recordsBean.getId()==selectedCouponId ){
            holder.checkIv.setImageResource(R.mipmap.icon_checkbox_all);
            if(selectedItemListener!=null){
                selectedItemListener.selectedItem(position);
            }
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
                selectedCouponId=-1;
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
        return mCouponList==null?0:mCouponList.size();
    }

    public interface SelectedItemListener{
        void selectedItem(int position);
    }
}
