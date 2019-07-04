package com.d2cmall.shopkeeper.glide;

import android.content.Context;

import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.utils.StringUtil;

/**
 * Name: d2c
 * Anthor: hrb
 * Date: 2017/7/29 11:36
 * Copyright (c) 2016 d2cmall. All rights reserved.
 */
public class GlideModelLoader extends BaseGlideUrlLoader<String> {

    public GlideModelLoader(Context context){
        super(context);
    }

    @Override
    protected String getUrl(String model, int width, int height) {
        if (isGif(model)){
            //Log.e("han","type=1:model="+model);
            return model;
        }else if (model.endsWith(Constants.MY_SUFFIX)){
            model=model.substring(0,model.length()-6);
            //Log.e("han","type=2:model="+model);
            return model;
        }else if (model.startsWith("file://")||!model.startsWith(Constants.IMG_HOST)){
            //Log.e("han","type=3:model="+model);
            return model;
        } else {
            if (model.contains("!/")){
                if (!model.contains(Constants.WEBP)){
                    model=model+Constants.WEBP;
                    //Log.e("han","type=4");
                }
            }else {
                if (width>0&&height>0){
                    model=model+"!/both/"+width+"x"+height+Constants.WEBP;
                    //Log.e("han","type=5");
                }else if (width>0){
                    model=model+"!/fw/"+width+Constants.WEBP;
                }
            }
            //Log.e("han","model="+model);
            return model;
        }
    }

    private static boolean isGif(String url) {
        if (StringUtil.isEmpty(url)){
            return false;
        }
        boolean is = false;
        int index = url.lastIndexOf(".");
        int length = url.length();
        if (index > 0) {
            String suffix = url.substring((index + 1), length);
            if (suffix.equals("gif")) {
                return true;
            }
        }
        return is;
    }
}
