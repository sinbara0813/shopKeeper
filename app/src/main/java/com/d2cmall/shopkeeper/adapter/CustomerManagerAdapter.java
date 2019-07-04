package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.CustomerManagerHolder;
import com.d2cmall.shopkeeper.model.CustomerListBean;
import com.d2cmall.shopkeeper.ui.activity.CustomerDetailActivity;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

/**
 * Created by LWJ on 2019/2/20.
 * Description : CustomerManagerAdapter
 */

public class CustomerManagerAdapter extends DelegateAdapter.Adapter<CustomerManagerHolder> {
    private Context mContext;
    private RequestManager manager;
    private List<CustomerListBean.RecordsBean> mCustomerList;
    private int mType;
    public CustomerManagerAdapter(Context context, RequestManager manager, List<CustomerListBean.RecordsBean> customerList, int type) {
        this.mContext = context;
        this.manager=manager;
        this.mCustomerList=customerList;
        this.mType=type;
    }
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }


    @Override
    public CustomerManagerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_customer_manager_item, parent, false);
        return new CustomerManagerHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerManagerHolder holder, int position) {
        CustomerListBean.RecordsBean recordsBean = mCustomerList.get(position);
        if(StringUtil.isEmpty(recordsBean.getNickname())){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("昵称");
            String s = String.valueOf(recordsBean.getId());
            String substring = s.substring(s.length() - 4, s.length());
            stringBuilder.append(substring);
            holder.tvUserName.setText(stringBuilder.toString());
        }else{
            holder.tvUserName.setText(recordsBean.getNickname());
        }
        ImageLoader.displayRoundImage(mContext,recordsBean.getAvatar(),holder.ivUserHeadPic,R.mipmap.icon_default_avatar);
        if(mType==0){
            holder.tvConsume.setText(mContext.getString(R.string.label_consume_amount,Util.getNumberFormat(recordsBean.getConsumeAmount())));
        }else{
            holder.tvConsume.setText(recordsBean.getAccount());
        }

        holder.tvLevel.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,CustomerDetailActivity.class);
                intent.putExtra("id",mCustomerList.get(position).getId());
                intent.putExtra("nickName",mCustomerList.get(position).getNickname());
                intent.putExtra("phone",mCustomerList.get(position).getAccount());
                intent.putExtra("avatar",mCustomerList.get(position).getAvatar());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCustomerList==null?0:mCustomerList.size();
    }
}
