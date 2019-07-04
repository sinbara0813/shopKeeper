package com.d2cmall.shopkeeper.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.common.Constants;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者:Created by sinbara on 2019/2/13.
 * 邮箱:hrb940258169@163.com
 */

public abstract class BaseActivity extends AppCompatActivity{

    private CompositeDisposable compositeDisposable;
    private boolean isGranted=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        doBusiness();
        checkPermission();
    }

    public void checkPermission(){
        String[] needPermission=getNeedPermission();
        if (needPermission!=null&&needPermission.length>0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean needRequest=false;
                    for (String s: needPermission) {
                        if (ContextCompat.checkSelfPermission(BaseActivity.this,s)!= PackageManager.PERMISSION_GRANTED){
                            needRequest=true;
                            isGranted=false;
                            break;
                        }
                    }
                    ActivityCompat.requestPermissions(BaseActivity.this,needPermission, Constants.RequestCode.REQUEST_PERMISSION);
                }
            },2000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==Constants.RequestCode.REQUEST_PERMISSION){
            // 用户取消了权限弹窗
            if (grantResults.length == 0) {
                refusePermission();
                return;
            }

            // 用户拒绝了某些权限
            for (int x : grantResults) {
                if (x == PackageManager.PERMISSION_DENIED) {
                    refusePermission();
                    return;
                }
            }
            isGranted=true;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public String[] getNeedPermission(){
        return null;
    }

    public void refusePermission(){

    }

    public abstract int getLayoutId();

    public abstract void doBusiness();

    public void addDisposable(Observable<?> observable, BaseObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(Object object) throws Exception {
                        if (isFinishing()||isDestroyed()){
                            return false;
                        }
                        return true;
                    }
                })
                .subscribeWith(observer));
    }

    public void removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable=null;
        }
    }

    @Override
    public void onLowMemory() {
        Glide.with(this).onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Glide.with(this).onTrimMemory(level);
        super.onTrimMemory(level);
    }

    @Override
    protected void onDestroy() {
        removeDisposable();
        super.onDestroy();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.anim_default);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_right, R.anim.anim_default);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

}
