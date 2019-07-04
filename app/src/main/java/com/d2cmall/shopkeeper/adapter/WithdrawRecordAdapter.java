package com.d2cmall.shopkeeper.adapter;

import android.content.Context;
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
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.holder.TransactionRecordHolder;
import com.d2cmall.shopkeeper.holder.WithdrawRecordHolder;
import com.d2cmall.shopkeeper.model.WithdrawRecordListBean;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.Util;

import java.util.List;

/**
 * Created by LWJ on 2019/2/26.
 * Description : TransactionRecordAdapter
 */

public class WithdrawRecordAdapter extends DelegateAdapter.Adapter<WithdrawRecordHolder> {
    private Context mContext;
    private List<WithdrawRecordListBean.RecordsBean> mWithdrawRecordList;
    public WithdrawRecordAdapter(Context context,  List<WithdrawRecordListBean.RecordsBean> withdrawRecordList) {
        this.mContext = context;
        this.mWithdrawRecordList=withdrawRecordList;
    }
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        return linearLayoutHelper;
    }

    @Override
    public WithdrawRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_wrathdraw_item, parent, false);
        return new WithdrawRecordHolder(view);
    }

    @Override
    public void onBindViewHolder(WithdrawRecordHolder holder, int position) {
        WithdrawRecordListBean.RecordsBean recordsBean = mWithdrawRecordList.get(position);
        String amountString = mContext.getString(R.string.label_transaction_amount,  Util.getNumberFormat(recordsBean.getArrivalAmount()));
        //字体大小不一样
        int length = amountString.length();
        SpannableString textSpan = new SpannableString(amountString);
        textSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(18)), 0, length-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textSpan.setSpan(new AbsoluteSizeSpan(ScreenUtil.dip2px(12)), length-1, length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textSpan.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.normal_black)),length-1,length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tvAmount.setText(textSpan);
        holder.tvApplyAmount.setText(mContext.getString(R.string.label_withdraw_apply_amount, Util.getNumberFormat(recordsBean.getAmount())));
        if("SUCCESS".equals(recordsBean.getStatus())){
            holder.tvPayAccount.setVisibility(View.VISIBLE);
            holder.tvPayAccount.setText(mContext.getString(R.string.label_pay_account, recordsBean.getPayAccount()));
            holder.tvPaySerialNumber.setVisibility(View.VISIBLE);
            holder.tvPaySerialNumber.setText(mContext.getString(R.string.label_withdraw_serial_number, recordsBean.getPaySn()));
        }else{
            holder.tvPayAccount.setVisibility(View.GONE);
            holder.tvPaySerialNumber.setVisibility(View.GONE);
        }
        holder.tvStatus.setText(recordsBean.getStatusName());
        holder.tvWithdrawChannel.setText(mContext.getString(R.string.label_withdraw_channle, recordsBean.getBankType()));
        holder.tvWithdrawDate.setText(mContext.getString(R.string.label_withdraw_date, recordsBean.getCreateDate()));
    }

    @Override
    public int getItemCount() {
        return mWithdrawRecordList==null?0:mWithdrawRecordList.size();
    }
}
