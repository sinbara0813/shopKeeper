package com.d2cmall.shopkeeper.model;

/**
 * Created by LWJ on 2019/3/18.
 * Description : HomeBean
 */

public class HomeBean {

        /**
         * daily : {"visitorCount":0,"memberCount":0,"orderCount":0,"quantityCount":0,"newlyCount":0,"paidAmount":0}
         * orders : {"waitRefundCount":51,"waitDeliverCount":51,"waitPayCount":1}
         * manage : {"memberTotal":1,"productTotal":32,"orderTotal":34}
         */

        private DailyBean daily;
        private OrdersBean orders;
        private ManageBean manage;

        public DailyBean getDaily() {
            return daily;
        }

        public void setDaily(DailyBean daily) {
            this.daily = daily;
        }

        public OrdersBean getOrders() {
            return orders;
        }

        public void setOrders(OrdersBean orders) {
            this.orders = orders;
        }

        public ManageBean getManage() {
            return manage;
        }

        public void setManage(ManageBean manage) {
            this.manage = manage;
        }

        public static class DailyBean {
            /**
             * visitorCount : 0
             * memberCount : 0
             * orderCount : 0
             * quantityCount : 0
             * newlyCount : 0
             * paidAmount : 0.0
             */

            private int visitorCount;
            private int memberCount;
            private int orderCount;
            private int quantityCount;
            private int newlyCount;
            private double paidAmount;

            public int getVisitorCount() {
                return visitorCount;
            }

            public void setVisitorCount(int visitorCount) {
                this.visitorCount = visitorCount;
            }

            public int getMemberCount() {
                return memberCount;
            }

            public void setMemberCount(int memberCount) {
                this.memberCount = memberCount;
            }

            public int getOrderCount() {
                return orderCount;
            }

            public void setOrderCount(int orderCount) {
                this.orderCount = orderCount;
            }

            public int getQuantityCount() {
                return quantityCount;
            }

            public void setQuantityCount(int quantityCount) {
                this.quantityCount = quantityCount;
            }

            public int getNewlyCount() {
                return newlyCount;
            }

            public void setNewlyCount(int newlyCount) {
                this.newlyCount = newlyCount;
            }

            public double getPaidAmount() {
                return paidAmount;
            }

            public void setPaidAmount(double paidAmount) {
                this.paidAmount = paidAmount;
            }
        }

        public static class OrdersBean {
            /**
             * waitRefundCount : 51
             * waitDeliverCount : 51
             * waitPayCount : 1
             */

            private int waitRefundCount;
            private int waitDeliverCount;
            private int waitPayCount;

            public int getWaitRefundCount() {
                return waitRefundCount;
            }

            public void setWaitRefundCount(int waitRefundCount) {
                this.waitRefundCount = waitRefundCount;
            }

            public int getWaitDeliverCount() {
                return waitDeliverCount;
            }

            public void setWaitDeliverCount(int waitDeliverCount) {
                this.waitDeliverCount = waitDeliverCount;
            }

            public int getWaitPayCount() {
                return waitPayCount;
            }

            public void setWaitPayCount(int waitPayCount) {
                this.waitPayCount = waitPayCount;
            }
        }

        public static class ManageBean {
            /**
             * memberTotal : 1
             * productTotal : 32
             * orderTotal : 34
             */

            private int memberTotal;
            private int productTotal;
            private int orderTotal;

            public int getMemberTotal() {
                return memberTotal;
            }

            public void setMemberTotal(int memberTotal) {
                this.memberTotal = memberTotal;
            }

            public int getProductTotal() {
                return productTotal;
            }

            public void setProductTotal(int productTotal) {
                this.productTotal = productTotal;
            }

            public int getOrderTotal() {
                return orderTotal;
            }

            public void setOrderTotal(int orderTotal) {
                this.orderTotal = orderTotal;
            }
        }
}
