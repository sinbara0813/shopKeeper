package com.d2cmall.shopkeeper.ui.view.web;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.d2cmall.shopkeeper.utils.BitmapUtils;
import com.d2cmall.shopkeeper.utils.ScreenUtil;
import com.d2cmall.shopkeeper.utils.StringUtil;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * WebHandler
 * Author: Blend
 * Date: 2016/11/30 16:19
 * Copyright (c) 2016 d2c. All rights reserved.
 */
public class WebHandler implements BridgeHandler {

    public Context context;
    public String functionName;

    public WebHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handler(String data, CallBackFunction function) {
        try {
            JSONObject myJsonObject = new JSONObject(data);
            functionName = myJsonObject.optString("handlefunc");
            if (!StringUtil.isEmpty(functionName)) {
                switch (functionName) {
                }
            }
        } catch (JSONException e) {
        }
    }



}
