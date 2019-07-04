package com.d2cmall.shopkeeper.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.bumptech.glide.RequestManager;
import com.d2cmall.shopkeeper.base.mvp.BaseObserver;
import com.d2cmall.shopkeeper.base.mvp.BasePresenter;
import com.d2cmall.shopkeeper.base.mvp.BaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Name: d2c
 * Anthor: hrb
 * Date: 2017/7/13 16:39
 * Copyright (c) 2016 d2cmall. All rights reserved.
 */
public abstract class BaseTabFragment extends Fragment{

    private CompositeDisposable compositeDisposable;
    public View rootView;
    public boolean isVisibleToUser;
    public boolean isCreated;
    public Context mContext;
    private Unbinder unBinder;
    private RequestManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = getRootView(inflater,container,savedInstanceState);
        unBinder= ButterKnife.bind(this, rootView);
        isCreated = true;
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        mContext=context;
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        removeDisposable();
        mContext=null;
        super.onDetach();
    }

    public abstract View getRootView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        doBusiness();
    }

    /**
     * 当前用户可见
     */
    public void show(){
        isVisibleToUser=true;
    }

    /**
     * 当前用户不可见
     */
    public void hide(){
        isVisibleToUser=false;
        if (isCreated){
            releaseOnInVisible();
        }
    }

    public abstract void doBusiness();

    public void releaseOnInVisible(){};

    public void addDisposable(Observable<?> observable, BaseObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Object>() {
                    @Override
                    public boolean test(Object object) throws Exception {
                        if (isDetached()){
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
    public void onDestroyView() {
        unBinder.unbind();
        super.onDestroyView();
    }
}
