package com.d2cmall.shopkeeper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.d2cmall.shopkeeper.R;
import com.d2cmall.shopkeeper.common.Constants;
import com.d2cmall.shopkeeper.model.UserBean;
import com.d2cmall.shopkeeper.ui.activity.LoginActivity;
import com.d2cmall.shopkeeper.ui.view.DefineToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by LWJ on 2019/2/22.
 * Description : Util
 */

public class Util {
    public static String readStreamToString(InputStream is) {
        return new String(readStreamToByteArray(is));
    }

    public static byte[] readStreamToByteArray(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = -1;
        byte[] data = null;
        try {
            while ((length = is.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            baos.flush();
            data = baos.toByteArray();
            is.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static void showToast(Context context, int stringId) {
        showToast(context, stringId, false);
    }
    public static void showToast(Context context, String string) {
        showToast(context, string, false);
    }
    public static void showToast(Context context, String string, boolean needDefine) {
        if (needDefine) {
            DefineToast defineToast = new DefineToast(context, string, 1);
            defineToast.show();
        } else {
            Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
        }
    }
    public static void showToast(Context context, int stringId, boolean needDefine) {
        String text = context.getString(stringId);
        showToast(context,text,needDefine);
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

    public static String hmacSha256(String str){
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(Constants.WXPARTNERID.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            //  utf-8 : 解决中文加密不一致问题,必须指定编码格式
            return byteArrayToHexString(sha256_HMAC.doFinal(str.getBytes("utf-8")));
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    //去掉-的随机数
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static String getNumberFormat(double value) {
        return getNumberFormat(value, true, false);
    }

    public static String getNumberFormat(double value, boolean isGroupingUsed) {
        return getNumberFormat(value, isGroupingUsed, false);
    }

    public static String getNumberFormat(double value, boolean isGroupingUsed, boolean isMinFraction) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        if (isMinFraction) {
            nf.setMinimumFractionDigits(2);
        }
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setGroupingUsed(isGroupingUsed);
        return nf.format(value);
    }

    public static String getNumberFormat(double value, int minNumber) {
        if (value % 1 == 0) {
            return (int) value + "";
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(minNumber);
        nf.setMinimumFractionDigits(minNumber);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setGroupingUsed(true);
        return nf.format(value);
    }

    /**
     *
     * @param value
     * @param patten 例 #,###.00
     * @return
     */
    public static String getNumberFormat(double value,String patten){
        DecimalFormat format=new DecimalFormat(patten);
        return format.format(value);
    }

    public static ArrayMap<String,String> buildListParam(int p, int ps){
        ArrayMap<String,String> map=new ArrayMap<>();
        map.put("p",String.valueOf(p));
        map.put("ps",String.valueOf(ps));
        return map;
    }

    public static ArrayMap<String,String> buildBrowseListParam(int p,int ps,long id){
        ArrayMap<String,String> map=new ArrayMap<>();
        map.put("p",String.valueOf(p));
        map.put("ps",String.valueOf(ps));
        map.put("memberId",String.valueOf(id));
        return map;
    }

    public static ArrayMap<String,String> buildSecurityParam(String mobile){
        ArrayMap<String,String> map=new ArrayMap<>();
        map.put("mobile",mobile);
        String sign=getMD5(mobile+ Constants.MSG_SECRET);
        Log.e("han","sign=="+sign);
        map.put("sign",sign);
        return map;
    }

    public static boolean loginChecked(Activity activity, int requestCode) {
        UserBean user = Session.getInstance().getUserFromFile(activity);
        if (user != null && user.getId() > 0) {
            return true;
        } else {
            Intent intent = new Intent(activity, LoginActivity.class);
            if (requestCode > 0) {
                activity.startActivityForResult(intent, requestCode);
            } else {
                activity.startActivity(intent);
            }
            activity.overridePendingTransition(R.anim.slide_in_up, R.anim.anim_default);
            return false;
        }
    }

    public static boolean wxInstalled(){
        final PackageManager packageManager = App.getContext().getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static String getIp(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    public static int getIntValue(double value){
        return (int)(value+0.5);
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static SpannableString getCartPriceSpan(Context context,double price,double profit){
        StringBuilder builder=new StringBuilder();
        String priceStr=getNumberFormat(price);
        String profitStr=getNumberFormat(profit);
        builder.append("进价¥ ").append(priceStr).append("  ").append("利润¥ ").append(profitStr);
        String text=builder.toString();
        int[] colors=new int[4];
        colors[0]=context.getResources().getColor(R.color.color_black85);
        colors[1]=context.getResources().getColor(R.color.color_red);
        colors[2]=context.getResources().getColor(R.color.color_black85);
        colors[3]=context.getResources().getColor(R.color.color_orange1);
        int[] colorIndex=new int[4];
        colorIndex[0]=2;
        colorIndex[1]=4+priceStr.length();
        colorIndex[2]=8+priceStr.length();
        colorIndex[3]=text.length();
        float[] sizes=new float[2];
        sizes[0]=1.0f;
        sizes[1]=1.25f;
        int[] sizeIndex=new int[2];
        sizeIndex[0]=2;
        sizeIndex[1]=4+priceStr.length();
        return Util.buildSpan(colors,colorIndex,sizes,sizeIndex,text);
    }

    public static SpannableString buildSpan(int[] color,int colorIndex[],float[] size,int sizeIndex[],String text){
        int length=color.length;
        int[][] colors=new int[length][2];
        for (int i=0;i<length;i++){
            colors[i][0]=color[i];
            colors[i][1]=colorIndex[i];
        }
        length=size.length;
        float[][] sizes=new float[length][2];
        for (int i=0;i<length;i++){
            sizes[i][0]=size[i];
            sizes[i][1]=sizeIndex[i];
        }
        return buildSpan(colors,sizes,text);
    }

    public static SpannableString buildSizeSpan(float[] size,int sizeIndex[],String text){
        int length=size.length;
        float[][] sizes=new float[length][2];
        for (int i=0;i<length;i++){
            sizes[i][0]=size[i];
            sizes[i][1]=sizeIndex[i];
        }
        return buildSpan(new int[][]{},sizes,text);
    }

    public static SpannableString buildColorSpan(int[] color,int colorIndex[],String text){
        int length=color.length;
        int[][] colors=new int[length][2];
        for (int i=0;i<length;i++){
            colors[i][0]=color[i];
            colors[i][1]=colorIndex[i];
        }
        return buildSpan(colors,new float[][]{},text);
    }

    public static SpannableString buildSpan(int[][] colors,float[][] sizes,String text){
        SpannableString span=new SpannableString(text);
        if (colors==null||sizes==null)return span;

        int size=colors.length;
        int start,end;
        for (int i=0;i<size;i++){
            if (i==0){
                start=0;
            }else {
                start=colors[i-1][1];
            }
            end=colors[i][1];
            if (end>text.length()){
                break;
            }
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(colors[i][0]);
            span.setSpan(colorSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        size=sizes.length;
        for (int i=0;i<size;i++){
            if (i==0){
                start=0;
            }else {
                start=(int) sizes[i-1][1];
            }
            end=(int) sizes[i][1];
            if (end>text.length()){
                break;
            }
            if (sizes[i][0]!=1){
                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(sizes[i][0]);
                span.setSpan(sizeSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return span;
    }

    public static String formatUrlMap(Map<String, String> paraMap,
                                      boolean urlEncode, boolean keyToLower) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(
                    tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds,
                    new Comparator<Map.Entry<String, String>>() {

                        @Override
                        public int compare(Map.Entry<String, String> o1,
                                           Map.Entry<String, String> o2) {
                            return (o1.getKey()).toString().compareTo(
                                    o2.getKey());
                        }
                    });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (item.getKey() != null) {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode ) {
                        val = Uri.encode( val, "utf-8");
                    }
                    if (keyToLower) {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }

}
