package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

import java.util.List;

/**
 * Created by LWJ on 2019/3/11.
 * Description : CouponListBean
 */

public class CouponListBean {

        /**
         * current : 0
         * pages : 0
         * records : [{"amount":0,"circulation":0,"consumption":0,"createDate":"2019-03-11T07:23:21.172Z","createMan":"string","id":0,"modifyDate":"2019-03-11T07:23:21.172Z","modifyMan":"string","name":"string","needAmount":0,"receiveEndDate":"2019-03-11T07:23:21.172Z","receiveStartDate":"2019-03-11T07:23:21.172Z","remark":"string","restriction":0,"serviceEndDate":"2019-03-11T07:23:21.172Z","serviceStartDate":"2019-03-11T07:23:21.172Z","serviceSustain":0,"shopId":0,"status":0,"type":0,"typeName":"string"}]
         * searchCount : true
         * size : 0
         * total : 0
         */

        private int current;
        private int pages;
        private boolean searchCount;
        private int size;
        private int total;
        private List<CouponBean> records;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getPages() {
            return pages;
        }

        public void setPages(int pages) {
            this.pages = pages;
        }

        public boolean isSearchCount() {
            return searchCount;
        }

        public void setSearchCount(boolean searchCount) {
            this.searchCount = searchCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<CouponBean> getRecords() {
            return records;
        }

        public void setRecords(List<CouponBean> records) {
            this.records = records;
        }
}
