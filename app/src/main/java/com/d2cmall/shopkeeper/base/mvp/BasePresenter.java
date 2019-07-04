package com.d2cmall.shopkeeper.base.mvp;


import android.app.Activity;
import android.support.v4.app.Fragment;

import com.d2cmall.shopkeeper.api.ApiRetrofit;
import com.d2cmall.shopkeeper.api.ApiServer;
import com.d2cmall.shopkeeper.base.BaseActivity;
import com.d2cmall.shopkeeper.base.BaseFragment;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者:Created by sinbara on 2019/2/13.
 * 邮箱:hrb940258169@163.com
 */

public class BasePresenter<V extends BaseView> {
    private CompositeDisposable compositeDisposable;
    public V baseView;
    private boolean isActivity;
    private boolean isFragment;
    protected ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    public BasePresenter(V baseView) {
        if (baseView instanceof BaseActivity){
            isActivity=true;
        }
        if (baseView instanceof BaseFragment){
            isFragment=true;
        }
        this.baseView = baseView;
    }
    /**
     * 解除绑定
     */
    public void detachView() {
        baseView = null;
        removeDisposable();
    }
    /**
     * 返回 view
     *
     * @return
     */
    public V getBaseView() {
        return baseView;
    }

    public void addDisposable(Observable<?> observable, BaseObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(Object object) throws Exception {
                        if (baseView==null){
                            return false;
                        }
                        if (isActivity){
                            if (((Activity)baseView).isFinishing()||((Activity)baseView).isDestroyed()){
                                return false;
                            }
                        }
                        if (isFragment){
                            if (((Fragment)baseView).isDetached()||!((Fragment)baseView).isVisible()){
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .subscribeWith(observer));
    }

    public void removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }
}
