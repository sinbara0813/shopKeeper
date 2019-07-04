package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.ArrayMap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.glide.ImageLoader;
import com.d2cmall.shopkeeper.holder.AllotOrderListHolder;
import com.d2cmall.shopkeeper.model.AllotListBean;
import com.d2cmall.shopkeeper.ui.activity.AllotItemDetialActivity;
import com.d2cmall.shopkeeper.ui.activity.WebActivity;
import com.d2cmall.shopkeeper.ui.view.TransparentPop;
import com.d2cmall.shopkeeper.ui.view.WarehousingConfirmPop;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

/**
 * Created by LWJ on 2019/2/21.
 * Description : AllotWarehouseWholeAdapterAdapter
 * 调拨单未拆单适配器
 */

public class AllotWarehouseSplitAdapterAdapter extends DelegateAdapter.Adapter<AllotOrderListHolder> {
    private Context mContext;
    private RequestManager manager;
    private List<AllotListBean.RecordsBean.AllotItemListBean> mOrderList;
    private boolean buttonsGone;

    private RefreshListener refreshListener;

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public AllotWarehouseSplitAdapterAdapter(Context context, RequestManager manager, List<AllotListBean.RecordsBean.AllotItemListBean> orderList) {
        this.mContext = context;
        this.manager = manager;
        this.mOrderList = orderList;
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        return linearLayoutHelper;
    }

    @Override
    public AllotOrderListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_allot_order_list_item, parent, false);
        return new AllotOrderListHolder(view);
    }

    @Override
    public void onBindViewHolder(AllotOrderListHolder holder, int position) {
                setOrderData(holder, position);
    }


    private String getValidPic(String[] pic) {
        int size = pic.length;
        for (int i = 0; i < size; i++) {
            if (!StringUtil.isEmpty(pic[i])) {
                return pic[i];
            }
        }
        return null;
    }

    /**
     * 设置订单记录数据
     *
     * @param holder
     * @param position
     */
    private void setOrderData(AllotOrderListHolder holder, int position) {
        if (mOrderList == null) return;
        AllotListBean.RecordsBean.AllotItemListBean allotItemList = mOrderList.get(position);
        holder.llProductContainer.removeAllViews();
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_purchase_allot_item, holder.llProductContainer, false);
            ImageView ivProduct = view.findViewById(R.id.iv_product);
            TextView tvProductName = view.findViewById(R.id.tv_product_name);
            TextView tvProductPrice = view.findViewById(R.id.tv_product_price);
            TextView tvProductStock = view.findViewById(R.id.tv_product_num);
            TextView tvProductSpec = view.findViewById(R.id.tv_product_spec);
            tvProductSpec.setText(allotItemList.getStandard());
            tvProductName.setText(allotItemList.getProductName());
            tvProductPrice.setText(mContext.getString(R.string.label_money, Util.getNumberFormat(allotItemList.getSupplyPrice())));
            tvProductStock.setText(mContext.getString(R.string.label_quantity, String.valueOf(allotItemList.getQuantity())));
            if (!StringUtil.isEmpty(allotItemList.getProductPic())) {
                ImageLoader.displayImage(manager, getValidPic(allotItemList.getProductPic().split(",")), ivProduct, R.mipmap.ic_logo_empty5, R.mipmap.ic_logo_empty5);
            } else {
                ImageLoader.displayImage(manager, "", ivProduct, R.mipmap.ic_logo_empty5, R.mipmap.ic_logo_empty5);
            }
            holder.llProductContainer.addView(view);
        holder.tvAction.setTextColor(Color.parseColor("#111111"));
        holder.rlBottom.setVisibility(View.VISIBLE);
        holder.tvOrderStatus.setVisibility(View.VISIBLE);
        //已取消差异的difference=8也显示收货差异标示
        if(allotItemList.getDifference()>=1){
            holder.tvDiff.setVisibility(View.VISIBLE);
        }else{
            holder.tvDiff.setVisibility(View.GONE);
        }
        holder.tvOrderStatus.setText(allotItemList.getStatusName());
        holder.tvAction.setText(mContext.getString(R.string.label_order_code, allotItemList.getAllotSn()));
        holder.tvOrderDetial.setText(mContext.getString(R.string.label_product_total, String.valueOf(allotItemList.getQuantity()), Util.getNumberFormat(allotItemList.getTotalAmount())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AllotItemDetialActivity.class);
                intent.putExtra("orderId",mOrderList.get(position).getId());
                intent.putExtra("orderType","allot");
                ((BaseActivity) mContext).startActivityForResult(intent, Constants.RequestCode.EDIT_ORDER);
            }
        });
        setButtons(holder,position);

    }

    private void setButtons(AllotOrderListHolder holder, int position) {
        if (mOrderList == null) return;
        holder.llButtonContainer.removeAllViews();
        //
        //APPLY("待审核"), AGREE("待发货"), DELIVER("已发货"), RECEIVE("已入库"), CLOSE("已关闭");
        //

        if(mOrderList.get(position).getDifference()==1){
            //实收数量和申请数量不一致的异常单
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(32));
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#8d92a3"));
            textView.setText("取消差异");
            textView.setBackgroundResource(R.drawable.sp_line);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消采购单
                    new AlertDialog.Builder(mContext)
                            .setMessage("取消后差异的数量会增加到商品上哦!")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelAllotDiff(mOrderList.get(position).getId());
                                }
                            }).show();
                }
            });
            holder.llButtonContainer.addView(textView);
        }

        if ("DELIVER".equals(mOrderList.get(position).getStatus())) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(32));
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            TextView textView1 = new TextView(mContext);
            textView1.setLayoutParams(layoutParams);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(Color.parseColor("#8d92a3"));
            textView1.setText("一键入库");
            textView1.setBackgroundResource(R.drawable.sp_line);
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WarehousingConfirmPop warehousingConfirmPop = new WarehousingConfirmPop(mContext, 0, mOrderList.get(position).getId(), mOrderList.get(position).getStandard(), mOrderList.get(position).getQuantity());
                    warehousingConfirmPop.setDismissCallBack(new TransparentPop.DismissListener() {
                        @Override
                        public void dismissStart() {
                            //入库成功
                            if(warehousingConfirmPop!=null && warehousingConfirmPop.getWarehousingSuccess()){
                                if(refreshListener!=null){
                                    refreshListener.refreshFragment();
                                }
                            }
                        }

                        @Override
                        public void dismissEnd() {

                        }
                    });
                    warehousingConfirmPop.show(((BaseActivity)mContext).getWindow().getDecorView());
                }
            });
            holder.llButtonContainer.addView(textView1);
        }

        if(!StringUtil.isEmpty(mOrderList.get(position).getLogisticsNum())){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(32));
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            TextView textView1 = new TextView(mContext);
            textView1.setLayoutParams(layoutParams);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(Color.parseColor("#8d92a3"));
            textView1.setText("查看物流");
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lookLogistics(mOrderList.get(position).getLogisticsNum());
                }
            });
            textView1.setBackgroundResource(R.drawable.sp_line);
            holder.llButtonContainer.addView(textView1);
        }

        if( holder.llButtonContainer.getChildCount()>0){
            holder.llButtonContainer.setVisibility(View.VISIBLE);
        }else{
            holder.llButtonContainer.setVisibility(View.GONE);
        }

    }


    //取消调拨单异常
    private void cancelAllotDiff(long id) {
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        arrayMap.put("id", String.valueOf(id));
        ((BaseActivity)mContext).addDisposable(ApiRetrofit.getInstance().getApiService().allotCancelDiff(arrayMap), new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                Util.showToast(mContext, "已取消");
                if(refreshListener!=null){
                    refreshListener.refreshFragment();
                }
            }
        });
    }

    private void lookLogistics(String logisticsNum) {
        Intent intent = new Intent(mContext, WebActivity.class);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://m.kuaidi100.com/");
        if (!StringUtil.isEmpty(logisticsNum)) {
            stringBuilder.append("nu=");
            stringBuilder.append(logisticsNum);
        }
        intent.putExtra("url", stringBuilder.toString());
        intent.putExtra("title", "查询物流");
        mContext.startActivity(intent);
    }


    @Override
    public int getItemCount() {
            return mOrderList == null ? 0 : mOrderList.size();
    }

    public void setButtonsGone(boolean b) {
        buttonsGone = b;
    }

    public interface RefreshListener{
        void refreshFragment();
    }
}
