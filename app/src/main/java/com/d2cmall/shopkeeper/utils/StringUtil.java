package com.d2cmall.shopkeeper.utils;

import com.d2cmall.shopkeeper.common.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 作者:Created by sinbara on 2019/2/14.
 * 邮箱:hrb940258169@163.com
 */

public class StringUtil {

    public static boolean isEmpty(String s){
        return s==null||s.trim().equals("")||s.toLowerCase().equals("null");
    }

    //返回D2C图片路径
    public static String getD2cPicUrl(String url) {
        if (isEmpty(url)) {
            return null;
        } else if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else if (url.startsWith("/storage/") || url.startsWith("/system/")) {
            return url;//"file://" + url;
        } else {
            return Constants.IMG_HOST + url;
        }
    }

    public static String getD2cPicUrlByWidth(String url,int width){
        if (isEmpty(url)) {
            return null;
        } else if (url.startsWith("http://")|| url.startsWith("https://")) {
            return url;
        } else if (url.startsWith("/storage/emulated")) {
            return url;//"file://" + url;
        } else {
            if (isGif(url)) {
                return Constants.IMG_HOST + url;
            } else {
                return Constants.IMG_HOST + url + String.format(Constants.PHOTO_URL2, width);
            }
        }
    }

    public static String getD2cPicUrl(String url, int width, int height) {
        if (isEmpty(url)) {
            return null;
        } else if (url.startsWith("http://")) {
            return url;
        } else if (url.startsWith("/storage/emulated")) {
            return url;//"file://" + url;
        } else {
            if (isGif(url)) {
                return Constants.IMG_HOST + url;
            } else {
                return Constants.IMG_HOST + url + String.format(Constants.PHOTO_URL, width, height);
            }
        }
    }

    private static boolean isGif(String url) {
        boolean is = false;
        int index = url.lastIndexOf(".");
        int length = url.length();
        if (index > 0) {
            String suffix = url.substring((index + 1), (index + 4));
            if (suffix.equals("gif")) {
                return true;
            }
        }
        return is;
    }

    //去掉-的随机数
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    //又拍云服务器储存路径
    public static String getUPYunSavePath(long userId, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");
        return "/app/" + type + "/" + sdf.format(new Date()) + "/" + getMD5(String.valueOf(userId) + uuid());
    }

    public static String getMD5(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                int temp = 0xff & b;
                if (temp <= 0x0F) {
                    sb.append("0").append(Integer.toHexString(temp));
                } else {
                    sb.append(Integer.toHexString(temp));
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double getDoubleFromText(String str){
        if(isEmpty(str)){
            return 0.0;
        }
        if(str.contains(",")){
            str=str.replaceAll(",","");
        }
        if(str.contains(" ")){
            str=str.replaceAll(" ","");
        }
        if(str.contains("¥")){
            str=str.replaceAll("¥","");
        }
        return Double.valueOf(str);
    }

}
