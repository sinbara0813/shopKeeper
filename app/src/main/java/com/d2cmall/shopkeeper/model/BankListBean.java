package com.d2cmall.shopkeeper.model;

import java.util.List;

/**
 * Created by LWJ on 2019/5/9.
 * Description : BankListBean
 */

public class BankListBean {

        /**
         * current : 0
         * pages : 0
         * records : [{"bankAddress":"string","bankName":"string","bankType":"string","cardNum":"string","createDate":"2019-05-09T01:58:01.820Z","createMan":"string","deleted":0,"id":0,"modifyDate":"2019-05-09T01:58:01.820Z","modifyMan":"string","shopId":0}]
         * searchCount : true
         * size : 0
         * total : 0
         */

        private int current;
        private int pages;
        private boolean searchCount;
        private int size;
        private int total;
        private List<BankInfoBean> records;

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

        public List<BankInfoBean> getRecords() {
            return records;
        }

        public void setRecords(List<BankInfoBean> records) {
            this.records = records;
        }

}
