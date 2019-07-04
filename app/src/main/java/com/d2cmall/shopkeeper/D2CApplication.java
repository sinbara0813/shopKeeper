package com.d2cmall.shopkeeper;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.d2cmall.shopkeeper.utils.App;

public class D2CApplication extends Application {

    private App app;

    @Override
    public void onCreate() {
        super.onCreate();
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);*/
        app=new App();
        app.onCreate(this);
        if (BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(new CrashExceptionHandler(this));
        }
        String name=getCurProcessName(this);
        if (name.equals("com.d2cmall.buyer")){

        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

}