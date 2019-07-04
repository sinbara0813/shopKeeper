package com.d2cmall.shopkeeper.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;

/**
 * 自定义toast
 * Author: hrb
 * Date: 2016/09/20 15:57
 * Copyright (c) 2016 d2cmall. All rights reserved.
 */
public class DefineToast {
    private WindowManager wdm;
    private double second;
    private View mView;
    private WindowManager.LayoutParams params;
    private Handler handler;

    public DefineToast(Context context) {
        this(context, null, 1);
    }

    public DefineToast(Context context, String toastContent, double time) {
        Context mContext = context.getApplicationContext();
        if (mContext==null){
            mContext=context;
        }
        wdm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(mContext)) {
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                context.startActivity(intent);
                return;
            } else {
                //执行6.0以上绘制代码
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }
        } else {
            //执行6.0以下绘制代码
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.y = ScreenUtil.dip2px( 125);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        if (!StringUtil.isEmpty(toastContent)) {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 1000) {
                        if (mView != null) {
                            if (mView.getParent() != null) {
                                wdm.removeView(mView);
                            }
                            mView = null;
                        }
                    }
                }
            };
            mView = LayoutInflater.from(context).inflate(R.layout.layout_define_toast, null);
            ((TextView) mView).setText(toastContent);
        }
        this.second = time;
    }

    public void setDefineView(View view) {
        this.mView = view;
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1000) {
                    if (mView != null) {
                        if (mView.getParent() != null) {
                            wdm.removeView(mView);
                        }
                        mView = null;
                    }
                }
            }
        };
    }

    public void setTime(double second) {
        this.second = second;
    }

    public void setLayoutParams(WindowManager.LayoutParams layoutParams) {
        this.params = layoutParams;
    }

    public void show() {
        if (mView.getParent() != null) {
            wdm.removeView(mView);
        }
        wdm.addView(mView, params);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1000);
            }
        }, (long) (second * 1000));
    }

    public void cancel() {
        wdm.removeView(mView);
        handler.removeMessages(1000);
    }
}
