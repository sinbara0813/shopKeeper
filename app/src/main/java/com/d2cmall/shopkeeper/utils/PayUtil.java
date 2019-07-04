package com.d2cmall.shopkeeper.utils;

import android.os.Looper;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.d2cmall.shopkeeper.BuildConfig;
import com.d2cmall.shopkeeper.common.Constants;
import com.google.gson.JsonObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class PayUtil {

    private static final String ALGORITHM = "RSA";
    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private static final String SING_ALGORITHMS2="SHA256WithRSA";
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     *
     * @param sn
     * @param orderId
     * @param expireTime 订单过期时间 格式yyyyMMddHHmmss
     * @param price 金额 分
     * @param ip
     * @return
     * @throws Throwable
     */
    public static String getPrepayId(String sn, String orderId, String expireTime, int price, String ip) throws Throwable {
        if ( Looper.getMainLooper() == Looper.myLooper()) throw new Throwable("this can not run in mainThread");
        String notice_str = Util.getRandomString(15);
        StringBuilder params = new StringBuilder();
        params.append("appid=").append(Constants.WEIXINAPPID).append("&");
        params.append("body=").append(orderId).append("&");
        params.append("device_info=").append("APP-002").append("&");
        params.append("mch_id=").append(Constants.WXMCHID).append("&");
        params.append("nonce_str=").append(notice_str).append("&");
        params.append("notify_url=").append(Constants.PAY_URL + "/wx_paid").append("&");
        params.append("out_trade_no=").append(sn).append("&");
        params.append("spbill_create_ip=").append(ip).append("&");
        params.append("total_fee=").append(price).append("&");
        params.append("trade_type=").append("APP").append("&");
        params.append("key=").append(Constants.WXPARTNERID);
        String s = Util.getMD5(params.toString()).toUpperCase();
        StringBuilder param = new StringBuilder();
        param.append("<xml>").append("\n")
                .append("<appid>").append(Constants.WEIXINAPPID).append("</appid>").append("\n")
                .append("<body>").append(orderId).append("</body>").append("\n")
                .append("<device_info>").append("APP-002").append("</device_info>").append("\n")
                .append("<mch_id>").append(Constants.WXMCHID).append("</mch_id>").append("\n")
                .append("<nonce_str>").append(notice_str).append("</nonce_str>").append("\n")
                .append("<notify_url>").append(Constants.PAY_URL + "/wx_paid").append("</notify_url>").append("\n")
                .append("<out_trade_no>").append(sn).append("</out_trade_no>").append("\n")
                .append("<spbill_create_ip>").append(ip).append("</spbill_create_ip>").append("\n")
                .append("<total_fee>").append(price).append("</total_fee>").append("\n")
                .append("<trade_type>").append("APP").append("</trade_type>").append("\n")
                .append("<sign>").append(s).append("</sign>").append("\n")
                .append("</xml>");
        return doRequest(param.toString());
    }

    public static String doRequest(String param) throws Throwable{
        String path = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        byte[] postData = param.toString().getBytes();
        URL url = new URL(path);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.setConnectTimeout(5 * 1000);
        urlConn.setDoOutput(true);
        urlConn.setUseCaches(false);
        urlConn.setRequestMethod("POST");
        urlConn.setInstanceFollowRedirects(true);
        urlConn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencode");
        urlConn.connect();
        DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
        dos.write(postData);
        dos.flush();
        dos.close();
        if (urlConn.getResponseCode() == 200) {
            byte[] data = readStream(urlConn.getInputStream());
            Log.d("han",new String(data));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new ByteArrayInputStream(data)));
            String pre_id = null;
            Element root = doc.getDocumentElement();
            NodeList firstList = root.getChildNodes();
            for (int i = 0; i < firstList.getLength(); i++) {
                Node itemNode = firstList.item(i);
                if (itemNode.getNodeName().equals("prepay_id")) {
                    pre_id = itemNode.getTextContent();
                    break;
                }
            }
            Log.d("han", "pre_id=" + pre_id);
            return pre_id;
        } else {
            Log.i("han", "Post方式请求失败");
        }
        return null;
    }

    public static byte[] readStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        inputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    /**
     *
     * @param sn
     * @param orderId
     * @param price 价格
     * @param expireTime 订单超时时间 分钟
     * @return
     */
    public static String getOrderInfo(String sn,String orderId,double price,int expireTime){
        JsonObject biz_content=new JsonObject();
        biz_content.addProperty("subject",orderId);
        biz_content.addProperty("out_trade_no",sn);
        biz_content.addProperty("timeout_express",expireTime+"m");
        biz_content.addProperty("total_amount",price);
        biz_content.addProperty("product_code","QUICK_MSECURITY_PAY");
        HashMap<String,String> map=new HashMap<>();
        map.put("app_id",Constants.ALIPAYPARTNER);
        map.put("biz_content",biz_content.toString());
        map.put("charset","utf-8");
        map.put("format","JSON");
        map.put("method","alipay.trade.app.pay");
        map.put("notify_url",Constants.PAY_URL + "/ali_pay");
        map.put("sign_type","RSA2");
        map.put("timestamp", DateUtil.formatString(new Date(),1));
        map.put("version","1.0");
        String orderParam=buildOrderParam(map);
        String orderInfo = orderParam + "&" + sign(map);
        return orderInfo;
    }

    public static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<String>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    private static String sign(Map<String,String> map){
        StringBuilder info=new StringBuilder();
        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            info.append(buildKeyValue(key, value, false));
            info.append("&");
        }
        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        info.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = rsa256sign(info.toString(), BuildConfig.DEBUG?Constants.ALIPAYPRIVATEKEY:Constants.ALIPAYPRIVATEKEY);
        String encodedSign = "";
        try {
            encodedSign = URLEncoder.encode(oriSign, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "sign=" + encodedSign;
    }

    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    public static String rsa256sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature
                    .getInstance(SING_ALGORITHMS2);

            signature.initSign(priKey);
            signature.update(content.getBytes(DEFAULT_CHARSET));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
