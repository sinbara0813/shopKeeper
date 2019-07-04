package com.d2cmall.shopkeeper.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

/**
 * 作者:Created by sinbara on 2019/2/13.
 * 邮箱:hrb940258169@163.com
 */

public class StrictModeUtil {

    public static final void startStrictMode() {
        if (Build.VERSION.SDK_INT > 9) {
            startThreadStrictMode();
            startVmStrictMode();
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static final void startThreadStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().permitDiskWrites().permitDiskReads().penaltyLog().penaltyDialog().build());
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static final void startVmStrictMode() {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
    }
}
