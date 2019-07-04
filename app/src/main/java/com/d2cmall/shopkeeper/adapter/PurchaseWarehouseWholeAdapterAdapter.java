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
import com.d2cmall.shopkeeper.model.PurchaseListBean;
import com.d2cmall.shopkeeper.ui.activity.AllotDetialActivity;
import com.d2cmall.shopkeeper.ui.activity.WebActivity;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

/**
 * Created by LWJ on 2019/2/21.
 * Description : AllotWarehouseWholeAdapterAdapter
 * 采购单列表适配器
 */

public class PurchaseWarehouseWholeAdapterAdapter extends DelegateAdapter.Adapter<AllotOrderListHolder> {
    private Context mContext;
    private RequestManager manager;
    private List<PurchaseListBean.RecordsBean> mOrderList;
    private boolean buttonsGone;
    private RefreshListener refreshListener;

    public PurchaseWarehouseWholeAdapterAdapter(Context context, RequestManager manager, List<PurchaseListBean.RecordsBean> orderList) {
        this.mContext = context;
        this.manager = manager;
        this.mOrderList = orderList;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
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
        if (mOrderList.get(position).getPurchItemList() == null || mOrderList.get(position).getPurchItemList().size() == 0) {
            return;
        }
        List<PurchaseListBean.RecordsBean.PurchItemListBean> purchItemList = mOrderList.get(position).getPurchItemList();
        holder.llProductContainer.removeAllViews();
        int quantity=0;
        for (int i = 0; i < purchItemList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_purchase_allot_item, holder.llProductContainer, false);
            ImageView ivProduct = view.findViewById(R.id.iv_product);
            TextView tvProductName = view.findViewById(R.id.tv_product_name);
            TextView tvProductPrice = view.findViewById(R.id.tv_product_price);
            TextView tvProductStock = view.findViewById(R.id.tv_product_num);
            TextView tvProductSpec = view.findViewById(R.id.tv_product_spec);
            tvProductSpec.setText(purchItemList.get(i).getStandard());
            tvProductName.setText(purchItemList.get(i).getProductName());
            tvProductPrice.setText(mContext.getString(R.string.label_money, Util.getNumberFormat(purchItemList.get(i).getSupplyPrice())));
            tvProductStock.setText(mContext.getString(R.string.label_quantity, String.valueOf(purchItemList.get(i).getQuantity())));
            quantity+=purchItemList.get(i).getQuantity();
            if (!StringUtil.isEmpty(purchItemList.get(i).getProductPic())) {
                ImageLoader.displayImage(manager, getValidPic(purchItemList.get(i).getProductPic().split(",")), ivProduct, R.mipmap.ic_logo_empty5, R.mipmap.ic_logo_empty5);
            } else {
                ImageLoader.displayImage(manager, "", ivProduct, R.mipmap.ic_logo_empty5, R.mipmap.ic_logo_empty5);
            }
            holder.llProductContainer.addView(view);

        }

        holder.tvAction.setTextColor(Color.parseColor("#111111"));
        holder.rlBottom.setVisibility(View.VISIBLE);
        holder.tvOrderStatus.setVisibility(View.VISIBLE);
        holder.tvOrderStatus.setText(mOrderList.get(position).getStatusName());
        holder.tvAction.setText(mContext.getString(R.string.label_order_code, mOrderList.get(position).getSn()));
        holder.tvOrderDetial.setText(mContext.getString(R.string.label_product_total, String.valueOf(quantity), Util.getNumberFormat(mOrderList.get(position).getProductAmount())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AllotDetialActivity.class);
                intent.putExtra("orderId", mOrderList.get(position).getId());
                intent.putExtra("orderType", "purchase");
                ((BaseActivity) mContext).startActivityForResult(intent, Constants.RequestCode.EDIT_ORDER);
            }
        });
        setButtons(holder, position);

    }

    private void setButtons(AllotOrderListHolder holder, int position) {
        if (mOrderList == null) return;
        holder.llButtonContainer.removeAllViews();
        //
        // WAIT_PAY("待付款"), PAID("已付款"), DELIVER("已发货"), RECEIVE("已入库"), CLOSE("已关闭");
        //

        if ("WAIT_PAY".equals(mOrderList.get(position).getStatus())) { //待付款
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(32));
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            TextView textView = new TextView(mContext);
            textView.setLayoutParams(layoutParams);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.parseColor("#8d92a3"));
            textView.setText("取消");
            textView.setBackgroundResource(R.drawable.sp_line);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //取消采购单
                    new AlertDialog.Builder(mContext)
                            .setMessage("确认取消该采购单?")
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ArrayMap<String, String> arrayMap = new ArrayMap<>();
                                    arrayMap.put("id", String.valueOf(mOrderList.get(position).getId()));
                                    ((BaseActivity) mContext).addDisposable(ApiRetrofit.getInstance().getApiService().purchaseCancel(arrayMap), new BaseObserver() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            if (refreshListener != null) {
                                                refreshListener.refreshFragment();
                                            }
                                            Util.showToast(mContext, "已取消");
                                        }
                                    });
                                }
                            }).show();
                }
            });
            holder.llButtonContainer.addView(textView);
            TextView textView1 = new TextView(mContext);
            textView1.setLayoutParams(layoutParams);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(Color.parseColor("#8d92a3"));
            textView1.setText("付款");
            textView1.setBackgroundResource(R.drawable.sp_line);
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AllotDetialActivity.class);
                    intent.putExtra("orderId", mOrderList.get(position).getId());
                    intent.putExtra("orderType", "purchase");
                    ((BaseActivity)mContext).startActivityForResult(intent, Constants.RequestCode.EDIT_ORDER);
                }
            });
            holder.llButtonContainer.addView(textView1);

        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(80), ScreenUtil.dip2px(32));
            layoutParams.setMargins(ScreenUtil.dip2px(10), 0, 0, 0);
            TextView textView1 = new TextView(mContext);
            textView1.setLayoutParams(layoutParams);
            textView1.setGravity(Gravity.CENTER);
            textView1.setTextColor(Color.parseColor("#8d92a3"));
            textView1.setText("查看订单");
            textView1.setBackgroundResource(R.drawable.sp_line);
            holder.llButtonContainer.addView(textView1);
        }
    }


    @Override
    public int getItemCount() {
        return mOrderList == null ? 0 : mOrderList.size();
    }

    public void setButtonsGone(boolean b) {
        buttonsGone = b;
    }

    public interface RefreshListener {
        void refreshFragment();
    }

}
