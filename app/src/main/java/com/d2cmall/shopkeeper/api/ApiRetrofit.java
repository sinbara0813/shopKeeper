package com.d2cmall.shopkeeper.api;

import android.util.Log;

import com.d2cmall.shopkeeper.BuildConfig;
import com.d2cmall.shopkeeper.base.gson.DoubleDefault0Adapter;
import com.d2cmall.shopkeeper.base.gson.IntegerDefault0Adapter;
import com.d2cmall.shopkeeper.base.gson.LongDefault0Adapter;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.common.SharePrefConstant;
import com.d2cmall.shopkeeper.utils.App;
import com.d2cmall.shopkeeper.utils.SharedPreUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.d2cmall.shopkeeper.utils.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者:Created by sinbara on 2019/2/13.
 * 邮箱:hrb940258169@163.com
 */

public class ApiRetrofit implements Interceptor {

    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";
    private static final String F_BREAK = " %n";
    private static final String F_URL = " %s";
    private static final String F_TIME = " in %.1fms";
    private static final String F_HEADERS = "%s";
    private static final String F_RESPONSE = F_BREAK + "Response: %d";
    private static final String F_BODY = "body: %s";

    private static final String F_BREAKER = F_BREAK + "-------------------------------------------" + F_BREAK;
    private static final String F_REQUEST_WITHOUT_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS;
    private static final String F_RESPONSE_WITHOUT_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BREAKER;
    private static final String F_REQUEST_WITH_BODY = F_URL + F_TIME + F_BREAK + F_HEADERS + F_BODY + F_BREAK;
    private static final String F_RESPONSE_WITH_BODY = F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + F_BREAKER;
    public final String BASE_SERVER_URL = Constants.API_URL;
    private Retrofit retrofit;
    private ApiServer apiServer;
    private static Gson gson;
    private static final int DEFAULT_TIMEOUT = 15;

    private ApiRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        else
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder
                .addInterceptor(interceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)//错误重联
                .addInterceptor(this)
                .cache(cache());
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(buildGson()))//添加json自定义（根据需求）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        apiServer = retrofit.create(ApiServer.class);
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        String sign="";
        String original="";
        if (request.method().equals("GET")){
            int index=request.url().toString().indexOf("?");
            String urlString=request.url().toString().substring(index+1, request.url().toString().length());
            String[] s=urlString.split("&");
            HashMap<String,String> param=new HashMap<>();
            for (int i = 0; i < s.length; i++) {
                String string = s[i];
                String[] item=string.split("=");
                if (item.length==2){
                    param.put(item[0],item[1]);
                }
            }
            String formString= Util.formatUrlMap(param,false,false);
            if (!StringUtil.isEmpty(formString)){
                original=formString+getToken();
                Log.e("han","original="+original);
            }
        }else if (request.method().equals("POST")){
            String bodyStr=stringifyRequestBody(request);
            if (!StringUtil.isEmpty(bodyStr)){
                original=bodyStr+getToken();
                Log.e("han","original="+original);
            }
        }
        if (!StringUtil.isEmpty(original)){
            sign=Util.getMD5(original);
            Log.e("han","sign="+sign);
        }
        Request authorised = request.newBuilder()
                .header("User-Agent", "d2c")
                .header("Device", "android")
                .header("Accept", getAcceptHeader())
                .header("appVersion", "1.0.0")
                .header("appTerminal", "APP.Buyer.Android")
                .header("accesstoken",getToken())
                .header("sign",sign)
                .header("Content-Type",getAcceptHeader())
                .build();

        if (BuildConfig.DEBUG) {
            long t1 = System.nanoTime();
            Response response = chain.proceed(authorised);
            long t2 = System.nanoTime();
            MediaType contentType = null;
            String bodyString = null;
            if (response.body() != null) {
                contentType = response.body().contentType();
                bodyString = response.body().string();
            }
            // 请求响应时间
            request.body();
            double time = (t2 - t1) / 1e6d;
             if (request.method().equals("GET")) {
                Log.e("retrofit-->", String.format("GET " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), response.code(), response.headers(), stringifyResponseBody(bodyString)));
            } else if (request.method().equals("POST")) {
                Log.e("retrofit-->", String.format("POST " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), stringifyRequestBody(request), response.code(), response.headers(), stringifyResponseBody(bodyString)));
            } else if (request.method().equals("PUT")) {
                Log.e("retrofit-->", String.format("PUT " + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url(), time, request.headers(), request.body().toString(), response.code(), response.headers(), stringifyResponseBody(bodyString)));
            } else if (request.method().equals("DELETE")) {
                Log.e("retrofit-->", String.format("DELETE " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITHOUT_BODY, request.url(), time, request.headers(), response.code(), response.headers()));
            }
            if (response.body() != null) {
                ResponseBody body = ResponseBody.create(contentType, bodyString);
                return response.newBuilder().body(body).build();
            } else {
                return response;
            }
        } else {
            return chain.proceed(authorised);
        }
    }

    /**
     * 增加后台返回""和"null"的处理
     * 1.int=>0
     * 2.double=>0.00
     * 3.long=>0L
     *
     * @return
     */
    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
                    .create();
        }
        return gson;
    }

    public static ApiRetrofit getInstance() {
        /*if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }*/
        return ApiRetrofitHolder.instance;
    }

    public static class ApiRetrofitHolder{
        private static ApiRetrofit instance=new ApiRetrofit();
    }

    private static String stringifyRequestBody(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private String getToken(){
       return SharedPreUtil.getString(SharePrefConstant.TOKEN,"");
    }

    public String stringifyResponseBody(String responseBody) {
        return responseBody;
    }

    public String getAcceptHeader() {
        return "application/json";
    }

    public String getBodyContentType(){
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }

    public String getParamsEncoding(){
        return DEFAULT_PARAMS_ENCODING;
    }

    public ApiServer getApiService() {
        return apiServer;
    }

    public Cache cache() {
        File cacheFile = new File(App.getContext().getCacheDir().getAbsolutePath(), "httpCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        return cache;
    }
}
