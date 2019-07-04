package com.d2cmall.shopkeeper.model;

/**
 * 作者:Created by sinbara on 2019/6/21.
 * 邮箱:hrb940258169@163.com
 */
public class StatBean {
    private int buyCount;
    private int followUserCount;
    private long id;
    private int newUserCount;
    private int orderCount;
    private int payCount;
    private int payOrderCount;
    private double profit;
    private int returnOrderCount;
    private int hasOrderShopCount;
    private double returnOrderMoney;
    private long shopId;
    private String shopName;
    private int statisticsTime;
    private String time;
    private String timeType;
    private double turnover;
    private int visitUserCount;

    public int getHasOrderShopCount() {
        return hasOrderShopCount;
    }

    public void setHasOrderShopCount(int hasOrderShopCount) {
        this.hasOrderShopCount = hasOrderShopCount;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public int getFollowUserCount() {
        return followUserCount;
    }

    public void setFollowUserCount(int followUserCount) {
        this.followUserCount = followUserCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(int newUserCount) {
        this.newUserCount = newUserCount;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getPayCount() {
        return payCount;
    }

    public void setPayCount(int payCount) {
        this.payCount = payCount;
    }

    public int getPayOrderCount() {
        return payOrderCount;
    }

    public void setPayOrderCount(int payOrderCount) {
        this.payOrderCount = payOrderCount;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getReturnOrderCount() {
        return returnOrderCount;
    }

    public void setReturnOrderCount(int returnOrderCount) {
        this.returnOrderCount = returnOrderCount;
    }

    public double getReturnOrderMoney() {
        return returnOrderMoney;
    }

    public void setReturnOrderMoney(double returnOrderMoney) {
        this.returnOrderMoney = returnOrderMoney;
    }

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(int statisticsTime) {
        this.statisticsTime = statisticsTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }

    public int getVisitUserCount() {
        return visitUserCount;
    }

    public void setVisitUserCount(int visitUserCount) {
        this.visitUserCount = visitUserCount;
    }

}
