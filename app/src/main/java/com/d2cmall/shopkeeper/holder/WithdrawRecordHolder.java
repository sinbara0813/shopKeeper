package com.d2cmall.shopkeeper.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LWJ on 2019/2/26.
 * Description : WithdrawRecordHolder
 */

public class WithdrawRecordHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_amount)
    public TextView tvAmount;
    @BindView(R.id.tv_status)
    public  TextView tvStatus;
    @BindView(R.id.tv_apply_amount)
    public TextView tvApplyAmount;
    @BindView(R.id.tv_withdraw_channel)
    public TextView tvWithdrawChannel;
    @BindView(R.id.tv_withdraw_date)
    public TextView tvWithdrawDate;
    @BindView(R.id.tv_pay_serial_number)
    public TextView tvPaySerialNumber;
    @BindView(R.id.tv_pay_account)
    public TextView tvPayAccount;

    public WithdrawRecordHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
