package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TipPop implements TransparentPop.InvokeListener {

    TextView tvContent;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.btn_sure)
    TextView btnSure;
    private ClearEditText etInput;
    private TransparentPop mPop;
    private Context mContext;
    private View tipLayout;
    private boolean isEditTip;

    public TipPop(Context context, boolean isEdit) {
        this.isEditTip = isEdit;
        this.mContext = context;
        init();
    }

    private void init() {
        if (isEditTip) {
            tipLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_tip_edit, new LinearLayout(mContext), false);
            etInput = (ClearEditText) tipLayout.findViewById(R.id.et_input);
        } else {
            tipLayout = LayoutInflater.from(mContext).inflate(R.layout.layout_tip_msg, new LinearLayout(mContext), false);
            tvContent = (TextView) tipLayout.findViewById(R.id.tv_content);
        }
        ButterKnife.bind(this, tipLayout);
        mPop = new TransparentPop(mContext, this);
        if (isEditTip){
            mPop.setFocusable(false);
        }
        initListener();
    }

    public void initListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (back != null) back.cancel();
                dismiss();
            }
        });
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (back != null) back.sure();
                dismiss();
            }
        });
    }

    public void setContent(String s) {
        if (StringUtil.isEmpty(s)) {
            tvContent.setVisibility(View.GONE);
        } else {
            tvContent.setText(s);
        }
    }

    public void setFocus(boolean is) {
        mPop.setFocusable(is);
    }

    public void setContent(int id) {
        tvContent.setText(id);
    }

    public TextView getContentView() {
        return tvContent;
    }

    public TextView getBtnCancelView() {
        return btnCancel;
    }

    public TextView getBtnSureView() {
        return btnSure;
    }

    public void showSingleBtn() {
        View line = tipLayout.findViewById(R.id.line);
        line.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
    }

    public void setBtnSure(int id) {
        btnSure.setText(id);
    }

    public void setBtnCancel(int id) {
        btnCancel.setText(id);
    }

    public void show(View parent) {
        mPop.show(parent, false);
    }

    public void showAsLocation(View parent) {
        mPop.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void dismiss() {
        if (mPop != null) {
            mPop.dismiss(false);
        }
    }

    public void setHint(String hint) {
        if (etInput != null) etInput.setHint(hint);
    }

    public String getInput() {
        if (etInput != null) {
            return etInput.getText().toString().trim();
        } else {
            return null;
        }
    }

    public boolean isShow() {
        return mPop.isShowing();
    }

    @Override
    public void invokeView(LinearLayout v) {
        v.setGravity(Gravity.CENTER);
        int width = 0;
        Point point = ScreenUtil.getDeviceSize(mContext);
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        if (isEditTip) {
            width = Math.round(point.x * 4 / 5);
        } else {
            width = Math.round(250 * dm.density);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, -2);
        v.addView(tipLayout, lp);
    }

    @Override
    public void releaseView(LinearLayout v) {
//        if (tipLayout != null) {
//            ((ViewGroup) tipLayout).removeAllViews();
//            tipLayout = null;
//        }
        back=null;
    }

    public CallBack back;

    public void setBack(CallBack callBack) {
        this.back = callBack;
    }

    public interface CallBack {
        void sure();

        void cancel();
    }
}
