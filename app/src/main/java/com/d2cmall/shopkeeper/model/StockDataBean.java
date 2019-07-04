package com.d2cmall.shopkeeper.model;

/**
 * Created by LWJ on 2019/5/23.
 * Description : StockDataBean
 */

public class StockDataBean {

        /**进货管理的数据面板
         * afterCount : 0
         * allotCount : 0
         * purchCount : 3
         */

        private int afterCount;
        private int allotCount;
        private int purchCount;

        public int getAfterCount() {
            return afterCount;
        }

        public void setAfterCount(int afterCount) {
            this.afterCount = afterCount;
        }

        public int getAllotCount() {
            return allotCount;
        }

        public void setAllotCount(int allotCount) {
            this.allotCount = allotCount;
        }

        public int getPurchCount() {
            return purchCount;
        }

        public void setPurchCount(int purchCount) {
            this.purchCount = purchCount;
        }
}
