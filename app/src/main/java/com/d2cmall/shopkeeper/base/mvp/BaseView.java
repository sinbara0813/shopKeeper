package com.d2cmall.shopkeeper.base.mvp;

import android.app.Activity;
import android.content.Context;

/**
 * 作者:Created by sinbara on 2019/2/13.
 * 邮箱:hrb940258169@163.com
 */

public interface BaseView {
    /**
     * 请求开始
     */
    void requestStart();
    /**
     * 请求结束
     */
    void requestFinish();

    /**
     * 获取activity对象
     * @return
     */
    Activity getActivity();

}
