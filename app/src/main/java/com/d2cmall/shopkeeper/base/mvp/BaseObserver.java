package com.d2cmall.shopkeeper.base.mvp;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.d2cmall.shopkeeper.BuildConfig;
import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.ui.activity.LoginActivity;
import com.d2cmall.shopkeeper.utils.App;
import com.d2cmall.shopkeeper.utils.Session;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * 作者:Created by sinbara on 2019/2/13.
 * 邮箱:hrb940258169@163.com
 */

public abstract class BaseObserver<T> extends DisposableObserver<T> {

    public final String TAG=getClass().getSimpleName();

    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1008;
    /**
     * 网络问题
     */
    public static final int BAD_NETWORK = 1007;
    /**
     * 连接错误
     */
    public static final int CONNECT_ERROR = 1006;
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1005;

    /**
     * 登录过期
     */
    public static final int LOGIN_INVALID = -401;

    /**
     * 其他错误
     */
    public static final int OTHER_ERROR = 1000;

    protected BaseView view;

    public BaseObserver(BaseView view) {
        this.view = view;
    }

    public BaseObserver(){

    }

    @Override
    protected void onStart() {
        if (view != null) {
            view.requestStart();
        }
    }

    @Override
    public void onNext(T o) {
        try {
            if (view != null) {
                view.requestFinish();
            }
            BaseModel model = (BaseModel) o;
            if (model.getCode()==1){//操作成功
                onSuccess(o);
            }else {
                onError(model.getCode(),model.getMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(-1,e.toString());
        }
    }

    @Override
    public void onError(Throwable e) {
        if (view != null) {
            view.requestFinish();
        }
        if (e instanceof HttpException) {
            //   HTTP错误
            onException(BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            //   连接错误
            onException(CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            //  连接超时
            onException(CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //  解析错误
            onException(PARSE_ERROR);
        }else {
            onException(OTHER_ERROR);
        }
    }

    private void onException(int unknownError) {
        switch (unknownError) {
            case CONNECT_ERROR:
                onError(unknownError,"连接错误");
                break;
            case CONNECT_TIMEOUT:
                onError(unknownError,"连接超时");
                break;
            case BAD_NETWORK:
                onError(unknownError,"网络超时");
                break;
            case PARSE_ERROR:
                onError(unknownError,"数据解析失败");
                break;
            case OTHER_ERROR:
                onError(unknownError,"其他错误");
                break;
            default:
                break;
        }
    }

    @Override
    public void onComplete() {
    }

    public abstract void onSuccess(T o);

    public void onError(int errorCode,String msg){
        if (BuildConfig.DEBUG){
            Log.e(TAG,"errorCode=="+errorCode);
        }
        if (errorCode==LOGIN_INVALID){
            Session.getInstance().logout(App.getContext());
            Intent intent=null;
            if (view!=null){
                intent=new Intent(view.getActivity(),LoginActivity.class);
                view.getActivity().startActivity(intent);
                view.getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.anim_default);
            }else {
                intent=new Intent(App.getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getContext().startActivity(intent);
            }
        }
        switch (errorCode){
            case -1:
                break;
            case -401:
                msg="登录已经过期";
                break;
            case -403:
                msg="没有访问权限";
                break;
            case -500:
                msg="服务器异常";
                break;
            case -501:
                msg="请求参数为空";
                break;
            case -502:
                msg="返回数据为空";
                break;
        }
        Util.showToast(App.getContext(),msg);
    }
}
