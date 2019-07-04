package com.d2cmall.shopkeeper.common;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class Short {

    private List<UrlsBean> urls;

    public List<UrlsBean> getUrls() {
        return urls;
    }

    public void setUrls(List<UrlsBean> urls) {
        this.urls = urls;
    }

    public static class UrlsBean {
        /**
         * result : true
         * url_short : http://t.cn/RUOn4qB
         * url_long : http://apistore.baidu.com/astore/shopready/1973.html
         * object_type :
         * type : 0
         * object_id :
         */

        private boolean result;
        private String url_short;
        private String url_long;
        private String object_type;
        private int type;
        private String object_id;

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }

        public String getUrl_short() {
            return url_short;
        }

        public void setUrl_short(String url_short) {
            this.url_short = url_short;
        }

        public String getUrl_long() {
            return url_long;
        }

        public void setUrl_long(String url_long) {
            this.url_long = url_long;
        }

        public String getObject_type() {
            return object_type;
        }

        public void setObject_type(String object_type) {
            this.object_type = object_type;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getObject_id() {
            return object_id;
        }

        public void setObject_id(String object_id) {
            this.object_id = object_id;
        }
    }
}
