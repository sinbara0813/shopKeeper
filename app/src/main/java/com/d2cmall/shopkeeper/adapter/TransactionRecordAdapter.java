package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.holder.CustomerDetialOrderHolder;
import com.d2cmall.shopkeeper.holder.TransactionRecordHolder;
import com.d2cmall.shopkeeper.model.TransactionRecordListBean;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

/**
 * Created by LWJ on 2019/2/26.
 * Description : TransactionRecordAdapter
 */

public class TransactionRecordAdapter extends DelegateAdapter.Adapter<TransactionRecordHolder> {
    private Context mContext;
    private int mType;//0:进行中 ,1:已完成
    private  List<TransactionRecordListBean.RecordsBean> mTransactionRecordList;
    public TransactionRecordAdapter(Context context, int type, List<TransactionRecordListBean.RecordsBean> transactionRecordList) {
        this.mContext = context;
        this.mType=type;
        this.mTransactionRecordList=transactionRecordList;
    }
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        return linearLayoutHelper;
    }

    @Override
    public TransactionRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_transaction_record_item, parent, false);
        return new TransactionRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionRecordHolder holder, int position) {
        TransactionRecordListBean.RecordsBean recordsBean = mTransactionRecordList.get(position);
        String amountString=null;
        if (recordsBean.getAmount()>0){
            amountString = mContext.getString(R.string.label_transaction_amount1, "+", Util.getNumberFormat(recordsBean.getAmount()));
        }else {
            amountString=Util.getNumberFormat(recordsBean.getAmount())+"元";
        }
        //字体大小不一样
        int length = amountString.length();
        SpannableString textSpan = new SpannableString(amountString);
        textSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(18)), 0, length-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(12)), length-1, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.normal_black)),length-1,length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvAmount.setText(textSpan);
//        holder.tvProductName.setText(mContext.getString(R.string.label_product_name, recordsBean.getProductName()));
        holder.tvPayCode.setText(mContext.getString(R.string.label_pay_code, recordsBean.getPaymentSn()));
        holder.tvPayDate.setText(mContext.getString(R.string.label_pay_date, recordsBean.getCreateDate()));
        holder.tvOrderCode.setText(mContext.getString(R.string.label_order_code, recordsBean.getOrderSn()));
    }

    @Override
    public int getItemCount() {
        return mTransactionRecordList==null?0:mTransactionRecordList.size();
    }
}
