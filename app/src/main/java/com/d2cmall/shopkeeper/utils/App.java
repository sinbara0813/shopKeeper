package com.d2cmall.shopkeeper.utils;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.d2cmall.shopkeeper.BuildConfig;

/**
 * 作者:Created by sinbara on 2019/2/13.
 * 邮箱:hrb940258169@163.com
 */

public class App {

    private static Context context;

    public void onCreate(Application context) {
        this.context = context;
        ScreenUtil.GetInfo(context);
        if (BuildConfig.DEBUG) {
            StrictModeUtil.startStrictMode();
        }
    }

    public static String getPageVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    public static int getPageVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public static final Context getContext() {
        return context;
    }
}
