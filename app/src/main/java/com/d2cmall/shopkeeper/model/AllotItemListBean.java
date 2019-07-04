package com.d2cmall.shopkeeper.model;

import java.util.List;

/**
 * Created by LWJ on 2019/5/24.
 * Description : AllotItemListBean
 */

public class AllotItemListBean {

    /**
     * code : 0
     * data : {"current":0,"pages":0,"records":[{"actualQuantity":0,"allotId":0,"allotSn":"string","createDate":"2019-05-24T01:16:40.289Z","createMan":"string","deleted":0,"difference":0,"fromShopId":0,"id":0,"logisticsCom":"string","logisticsNum":"string","modifyDate":"2019-05-24T01:16:40.289Z","modifyMan":"string","productId":0,"productName":"string","productPic":"string","productSkuId":0,"quantity":0,"standard":"string","status":"string","statusName":"string","supplyPrice":0,"toShopId":0,"totalAmount":0}],"searchCount":true,"size":0,"total":0}
     * msg : string
     */

        /**
         * current : 0
         * pages : 0
         * records : [{"actualQuantity":0,"allotId":0,"allotSn":"string","createDate":"2019-05-24T01:16:40.289Z","createMan":"string","deleted":0,"difference":0,"fromShopId":0,"id":0,"logisticsCom":"string","logisticsNum":"string","modifyDate":"2019-05-24T01:16:40.289Z","modifyMan":"string","productId":0,"productName":"string","productPic":"string","productSkuId":0,"quantity":0,"standard":"string","status":"string","statusName":"string","supplyPrice":0,"toShopId":0,"totalAmount":0}]
         * searchCount : true
         * size : 0
         * total : 0
         */

        private int current;
        private int pages;
        private boolean searchCount;
        private int size;
        private int total;
        private List<AllotListBean.RecordsBean.AllotItemListBean> records;

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

        public List<AllotListBean.RecordsBean.AllotItemListBean> getRecords() {
            return records;
        }

        public void setRecords(List<AllotListBean.RecordsBean.AllotItemListBean> records) {
            this.records = records;
        }

}
