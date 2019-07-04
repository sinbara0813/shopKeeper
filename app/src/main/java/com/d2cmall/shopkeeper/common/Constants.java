package com.d2cmall.shopkeeper.common;

import com.d2cmall.shopkeeper.BuildConfig;

public final class Constants {

    //Api请求地址
    private static final String API_URL_ONLINE = "https://fune.store/";
    private static final String API_URL_DEV = "http://shop.d2c.cn/";
    private static final String API_URL_TEST1="http://192.168.5.20:8001/";
    private static final String API_URL_TEST2="http://192.168.5.17:8001/";

    public static final String API_URL = BuildConfig.DEBUG ? API_URL_ONLINE:API_URL_ONLINE;

    //分享地址
    private static final String SHARE_URL_ONLINE = "https://fune.store/";
    private static final String SHARE_URL_DEV = "http://shop.d2c.cn/";
    public static final String SHARE_URL = BuildConfig.DEBUG ? SHARE_URL_DEV : SHARE_URL_ONLINE;

    //支付回调地址
    public static final String PAY_URL = API_URL+"c_api/callback";

    public static final String INVOKE_URL = API_URL + "/v3/api/invoke/android?token=%1$s&url=%2$s&openInBrow";
    public static final String SCAN_URL = "http://app.d2cmall.com/download/d2cmall-install.apk";
    public static final String WEBP = "/format/webp";
    public static final String MY_SUFFIX = "SUFFIX";
    public static final String PHOTO_URL = "!/both/%1$sx%2$s";
    public static final String PHOTO_URL2 = "!/fw/%s/format/webp";
    public static final String VIDEO_PIC_URL = "!/fw/%s/format/webp/lossless/true";
    public static final String WULIU_URL = "/logistics/info?sn=%1$s&com=%2$s&productImg=%3$s";
    public static final String USER_AGENT_URL = " Ver/buyer-%1$s NetType/%2$s";
    public static final String IMG_HOST = "https://s.fune.store";
    public static final String IMG_HOST1 = "https://s.fune.store";
    public static final String VIDEO_HOST = "http://video.d2cmall.com";
    public static final String DEFAULT_AVATAR_URL = "http://d2c-app.b0.upaiyun.com/img/logo/android_default_avatar.png";
    public static final String APP_SECRET = "8811d44df3c0b408f6fa4a31002db44d";
    public static final String MSG_SECRET="3dcb6a2698036913d0bf235a448b9118";
    public static final String D2C_LOGO = "http://img.d2c.cn/app/a/16/05/10/fa55b70135c181482ae5c6d39c3277b1";
    public static final String detailWebUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.d2cmall.buyer";

    public static final String USER_FILE = "user_session.json";

    public static final String TYPE_AVATAR = "a";
    public static final String TYPE_FRIEND = "f";
    public static final String TYPE_VIDEO = "v";
    public static final String TYPE_CUSTOMER = "c";


    public static final String DATE_FORMAT_LONG = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMAT_LONG1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_SHORT = "yyyy/MM/dd";
    public static final String DATE_FORMAT_SHORT1 = "yyyy-MM-dd";
    public static final String DATE_FORMAT_MD = "MM/dd";
    public static final String DATE_FORMAT_HM = "HH:mm";
    public static final String DATE_FORMAT_MD_HM = "MM/dd HH:mm";
    public static final String DATE_FORMAT_MDHM = "MM月dd日 HH:mm";
    public static final String DATE_FORMAT_YMD = "yyyy年MM月dd日";

    //QQ、微信、微博
    public static final String QQKEY = "1104624486";

    public static final String WEIBOKEY = "1542490164";
    public static final String WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    //支付宝
    public static final String ALIPAYPARTNER = "2019031363524450";
    public static final String ALIPAYSELLER = "payment@d2cmall.com";
    public static final String ALIPAYPRIVATEKEY = "MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQC8OoEBsk2G4TIsrSpcTyTgvSKFL8mpapkP9E3NyjvoWOCh8IczqRA0XepozHFtoGJ8sW26jZMVkooEfR2uyg53dEsBxs8+Ol6VfMhdF+1SCaXLPcA02MPYOahR2623iRiM3njmw6FAxavZ6yOh+nYKgVRre26SN+08RC3Xhm7P1Y851iu3FZj2oOqZhgM/7D+0CzSygSTYZYO4dTlQqfFL8R9NCenvIt8bbVrrfqEWR/NwYGB0uO78sPwqRNdETcfrVVvQkqI1HcnSMX7tzBA5A/01PYQ5YentgO7RoaXONSm86pw7WZZIDVTDg+k725i9A4tOi39IBLuW5VJfabULAgMBAAECggEBAKhhJXphCF06Imo41gYRfonUZ4cBQsLc6uOMxfTrjj/BEeGLi0a5XBecU88/49APg0rMZb5WW1cW6YQucFklw180lsyWDlo6WNYc9BfjCZczri5rpP8hUcChg2MGwxXF/EI32auFIUESEzMz14JUzTeEjgJAPp9qjDw3OVLtnT8o3WS0EeW0HHsFJpYX0is2z+MvA+6rVOcmzyrmCuHXP3Tgc2ZKgy5RMuU9HnqfWbyh/JfbnT+K2/pa+ctvfHAes7n/l2qY2IKWTBGCO58+ONQcE0e/KRjNPGC11kQ5mfrIy/o+09tzRjTACTYTxVGFXNDOEFt2OYQe748sEjGuBgECgYEA+tRcxB7c1DxslknEek9MFbV9nBD5Gkk2Loy55qr1MRMPN236Rvt6yVInXIvSKx/uKnlzhW6jR6UsZSbdgHuw/agcul0+MXXnrKYO5XzbatOUs2U7ALXbf9O9fesfGcK410Qp69gw7pAX0HVXZx4F8twxgTo8RtvuhliEdlNRU4ECgYEAwBvLIyKTN/5cZA1jwVyHxF1ZdBDgNL92x1WKpykC1HKyjWz+2Drtd1FvRMG5NbqQRo8nqTQtuY3lbKMsnd2sGavyDOwIMZKQJtUSQR+Sgl2LaYzbexYWFwnHz+j272k1U1uh3CPdCyOAC4XhJG2ypuzrx7eD6/gbL/fgNjGYXosCgYEA5zAXNLjRh1kOKNNjIey2zweR9+f5AY5bPzp8GiyWhB3yF3/pf8VIngh3uSgylYb/qOx6WpDoNB/8tFrHLCZVsuZhXDCXI1VuLMZ90m1re6H6XANBB92Wq9IIxOeSqda9McRk8zk3mJr9KCkPXz7f8sGba2W2qwUJa5Bovvr6aYECgYEAgXp6GG02QlgK11h68pk13HB6WRMCuiqNmN4bvHyUrrG7jDx5Ky3UXUYis3nKJd9fXF8iARXs95IennEAIkqEsyzD0FfJUfgRdkrHqeLosTrxOqu4RiZlrxNle8jtngEP8uXgmuoFEnvT0nBihVX2QjGO3KXgoUswr8zmbkBRDO8CgYEAoEY6ggORn44Iuslc7L6u7TJ+tkkmOt6xV9vJxz3papY7rYULpD0FMAhDlK2V/qFOPFteCH8qoSAR7Xvt2SpJ1PF+0a7V40Vs5W8UtdYssFozTAc//68XsBZoGBAmsNUfCI/IZLuc+ER2XVsS6wryTSmFajaS0eMrpvp6ofIfQEQ=";
    public static final String DEBUGALIPAYPRIVATEKEY="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCchKnYbLyUgRUQ7laiguGKWF8SQKog2+ozPNMFy9/wn/3VNc74MgOpjnpW7EDZ3Ow/3gQ1lp6GkmHVIaiXNbRz5Y71Jp9DjPFFd6UNmyGqJtjdmaBAhs4IdX2Kd5tleGVxAJxQDMm3C3ZX+VlJdJmUNY+xmpC65Il372/OODI4xWkPvNwbD79ptXELt9BklrL7NsiQZrZqiNWI7F2CbOMNQ4tLZljdSJ5xNkKrg1sBTF3vRlONa7jElk3mXTaxmznEC0TpRoUvjkp/BHBuEOV9maVF19k2JgR4PxE3en2+jHsiaLTz22pUQ7rFJA5XnspFcYdZCjPgXIxEiUgAkVM7AgMBAAECggEABVBXt9pROLV9Mpr05AWPhLPlXmhFnmH4/I5XF75JNxEnfAKcqT6Bc1M8AhF5Tiw69RwjhnI7pZP4sFS5NZEENF8Z0GbkR1XUpygduW2H4g/5kuRxVV9+RTk/7uCKrRI+V2zO4cTdYFKKTQhRKLZFtItw+nWl7kJuDF1brWOpCNZVTtiEFl1yGaj4cZLZw9K+/2CCdoLJTE15gvCyMFnqA6WwPdipE+6ye3rBW+ikD4aZ5nVnulzhgDTbnUXPiT4H2ny1vdZY5QODei6AodnrhiJvwyx4HCfSeiCW3AX8zhAMdypMI2uxsN9qKIdKSXvgpHT/dGFkmzAmTHjGaAqf6QKBgQDXcUi78giuQMMbmBv/1UDmDyTrNWZ1QboKOWxo8fAxtKJhNHSFXy7KFOhl2t9vySN19ImT773lhWm2TS7bCnaVkYrEgw/esUmE4mUGHDKyywlucHK4WKJ+U29CFGelRV5OG3mBj2wnXmq3v8eggexpopXjOiRbXC8rxOuNh/aDdwKBgQC5+6tcO7huHPj+wG6seEPfUSVT3GkXupww23/TTsc1BVzy6otR3wELc7+OgBna3LqiIiqbDgTSxsi+eOqcp07bInG/gI0WoaizAf1Sk9i/SjZAiwrG0cks3bML7mU+3hn68/o2EjBQIGgF52UUWNeGrmestNVk7tzIF7Y/16M3XQKBgCchFSPFltwHOSiG3Pb/RXVDZOIvZbeAQ4jd2xnIb2antyJOg6dBnV7oozj0osZL0tv2mRI8ZdINBCEvLYZtBRKvR3Pflf9NVdwDvnN4paChjcn4NG3U81ZcUwW+bQCwc6teBBiK8wrHdDyBYYF+d3DMLcsCYV4ShSK+9kVTX2uLAoGAEaPVNQJVE9yboU4PMTyW82924uVc8ms4tUmcbiZirpy7nC6y2CkWqEdqO9Haoe+dpBMph1tlCbjhAW/mkMUtrYsuzsManrLmlVPy+woa3MnlK2oEs6biNecxPEh4QeY/AJZzfP1sp6zrMOQpqixjBNs16iOcud4OAsx89ME3MeECgYEAorchDp7+fGEPRFoXesjADC85aSIkr5gOSEaSFFyUuWLfZ0sP1KVfNATKfrXAl0CpsEaege7Zy9TM5e79A6TTy8hZJQHsBRkd3SXz3iVkpfAxdWL4PKMkyTaBcZ0Swr//U1I5O1EdquDFR72LDWYXXa1NiRnZkHMhLk00VnmAHYk=";
    //微信支付
    public static final String WEIXINAPPID = "wx996ba580dad5ea7f";
    public static final String WXMCHID = "1526398121";
    public static final String WXPARTNERID = "0a79d94431a7ea89577aaaaf58a2a053";
    //又拍云
    public static final String UPYUN_KEY = "f9Qo0bynvsI9qW7a6Or95oT7zYc=";
    public static final String UPYUN_SPACE = "fune-static";
    public static final String UPYUN_VIDEO_SPACE = "app-video";

    //zxing
    public static final boolean KEY_AUTO_FOCUS = true;
    public static final boolean KEY_DISABLE_CONTINUOUS_FOCUS = false;
    public static final boolean KEY_VIBRATE = false;
    public static final boolean KEY_PLAY_BEEP = true;


    public static final int NO_DATA = 0;
    public static final int NET_DISCONNECT = 1;

    public static class ActionType {
        public static final int KEY_BACK = 1;
        public static final int UPLOAD_STATE = 2;
        public static final int UPLOAD_PROGRESS = 3;
        public static final int MAGIC_WINDOW = 4;
        public static final int ADD_CART_SUCCESS = 5;
        public static final int CLEAR_ALL_ACTIVITY = 6;
        public static final int REFRESH_CART = 7;
        public static final int CALCULTE_CART = 8;
        public static final int FINISH_CART = 9;
        public static final int CHANGE_PAGE = 10;
        public static final int CHANGE_PRODUCT_PAGE = 11;
        public static final int REFRESH_CART_COUNT = 12;
        public static final int HOME_UP = 13;
        public static final int HOME_UP_VISIABLE = 14;
        public static final int LIVE_LIST = 15;
        public static final int SPECAIL_UP = 16;
        public static final int MESSAGE_ALL_UNREANED_CHANGE = 17;
        public static final int XINREN=18;
        public static final int PARTENER_USER_INFO_CHANGE = 19;
        public static final int WXPAYRESULT=20;
        public static final int LOGOUT=21;
        public static final int CART_OFFLINE_ADD =22;
        public static final int CART_DELETE =23;
        public static final int CART_UPDATE =24;
        public static final int CART_TO_ALLOT =25;
        public static final int CART_TO_PURCHASE =26;
        public static final int CART_REFRESH =27;
        public static final int AFTER_SALE_REFRESH=28;
        public static final int PURCHASE_ALLOT_REFRESH=29;
        public static final int BRAND=30;
        public static final int LOGIN_OK=31;
    }

    public static class RequestCode {
        public static final int REQUEST_PERMISSION=1000;
        public static final int COUNTRY_CODE=123;
        public static final int SELECT_BANK_CODE=124;
        public static final int EDIT_BRAND_INFO=125;
        public static final int ADD_STAFF_SUCCESS=126;
        public static final int COUPON_SETTING=127;
        public static final int COLLAGE_TO_PRODUCTLIST=128;
        public static final int COLLAGE_TO_SELECTPRODUCT=129;
        public static final int EDIT_COLLAGE_PRODUCT=130;
        public static final int ADD_COLLAGE_PRODUCT=131;
        public static final int ADD_COLLAGE_COUPON=132;
        public static final int SELECT_COUPON=133;
        public static final int CREAT_BRAND=134;
        public static final int REGISTER_ACCOUNT=135;
        public static final int COUPON_BIND_PRODUCT=136;
        public static final int ACTION_COUPON=137;
        public static final int SCAN_DELIVERY_CODE=138;
        public static final int EDIT_ORDER=139;
        public static final int CROP_PIC=140;
        public static final int WITHDRAW_SUCCESS=141;
        public static final int CLEAR_BOND=142;
    }

}