package com.d2cmall.shopkeeper.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.AddStaffHolder;
import com.d2cmall.shopkeeper.holder.CustomerManagerHolder;
import com.d2cmall.shopkeeper.model.StaffListBean;
import com.d2cmall.shopkeeper.ui.activity.StaffEditActivity;
import com.d2cmall.shopkeeper.ui.activity.StaffManagerListActivity;
import com.d2cmall.shopkeeper.ui.view.ShadowDrawable;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

/**
 * Created by LWJ on 2019/2/29.
 * Description : StaffManagerAdapter
 */

public class StaffManagerAdapter extends DelegateAdapter.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private RequestManager manager;
    private List<StaffListBean.RecordsBean> mStaffList;
    public StaffManagerAdapter(Context context,RequestManager manager, List<StaffListBean.RecordsBean> staffList) {
        this.mContext = context;
        this.manager=manager;
        this.mStaffList=staffList;
    }
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==0){
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_customer_manager_item, parent, false);
            return new CustomerManagerHolder(view);
        }else{
            view= LayoutInflater.from(mContext).inflate(R.layout.layout_add_staff, parent, false);
            return new AddStaffHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if( mStaffList!=null && position<mStaffList.size()){
            CustomerManagerHolder customerManagerHolder = (CustomerManagerHolder) holder;
            StaffListBean.RecordsBean recordsBean = mStaffList.get(position);
            String name= StringUtil.isEmpty(recordsBean.getNickname())?recordsBean.getAccount():recordsBean.getNickname();
            customerManagerHolder.tvUserName.setText(name);
            ImageLoader.displayRoundImage(mContext,recordsBean.getAvatar(),customerManagerHolder.ivUserHeadPic,R.mipmap.icon_default_avatar);
            customerManagerHolder.tvConsume.setText(mContext.getString(R.string.label_add_staff_time,recordsBean.getCreateDate().substring(0,11)));
            customerManagerHolder.tvLevel.setBackground(mContext.getResources().getDrawable(R.drawable.sp_line));
            customerManagerHolder.tvLevel.setText(recordsBean.getRoleName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Util.loginChecked((Activity) mContext,999)){
                        mContext.startActivity(new Intent(mContext, StaffEditActivity.class).putExtra("action","edit").putExtra("staffId",mStaffList.get(position).getId()));
                    }
                }
            });
        }else{
            AddStaffHolder addStaffHolder = (AddStaffHolder)holder;
            ShadowDrawable.setShadowDrawable(addStaffHolder.tvAddStaff, Color.parseColor("#FFFFFF"), ScreenUtil.dip2px(2),
                    Color.parseColor("#11000000"),  ScreenUtil.dip2px(20), 0, 0);
            addStaffHolder.tvAddStaff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addStaff();
                }
            });
        }

    }

    private void addStaff() {
        Intent intent = new Intent(mContext, StaffEditActivity.class).putExtra("action", "add");
        ((StaffManagerListActivity)mContext).startActivityForResult(intent, Constants.RequestCode.ADD_STAFF_SUCCESS);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0 && mStaffList==null){
            return 1;
        }
        if(position<mStaffList.size()){
            return super.getItemViewType(position);
        }else{
            return 1;
        }

    }

    @Override
    public int getItemCount() {
        return mStaffList==null?1:mStaffList.size()+1;
    }
}
