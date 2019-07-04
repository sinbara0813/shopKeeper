package com.d2cmall.shopkeeper.model;

import com.d2cmall.shopkeeper.common.Id;

import java.io.Serializable;

/**
 * Created by LWJ on 2019/3/9.
 * Description : CouponBean
 */

public class CouponBean extends Id{

        /**
         * amount : 0
         * circulation : 0
         * consumption : 0
         * createDate : 2019-03-09T05:04:06.141Z
         * createMan : string
         * id : 0
         * modifyDate : 2019-03-09T05:04:06.141Z
         * modifyMan : string
         * name : string
         * needAmount : 0
         * receiveEndDate : 2019-03-09T05:04:06.141Z
         * receiveStartDate : 2019-03-09T05:04:06.141Z
         * remark : string
         * restriction : 0
         * serviceEndDate : 2019-03-09T05:04:06.141Z
         * serviceStartDate : 2019-03-09T05:04:06.141Z
         * serviceSustain : 0
         * shopId : 0
         * status : 0
         * type : 0
         * typeName : string
         */

        private double amount;
        private int circulation;
        private int consumption;
        private String createDate;
        private String createMan;
        private String modifyDate;
        private String modifyMan;
        private String name;
        private double needAmount;
        private String receiveEndDate;
        private String receiveStartDate;
        private String remark;
        private int restriction;
        private String serviceEndDate;
        private String serviceStartDate;
        private Integer serviceSustain;
        private long shopId;
        private int status;
        private int type;
        private String typeName;
        private String crowd;

        public String getCrowd() {
            return crowd;
        }

        public void setCrowd(String crowd) {
            this.crowd = crowd;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public int getCirculation() {
            return circulation;
        }

        public void setCirculation(int circulation) {
            this.circulation = circulation;
        }

        public int getConsumption() {
            return consumption;
        }

        public void setConsumption(int consumption) {
            this.consumption = consumption;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCreateMan() {
            return createMan;
        }

        public void setCreateMan(String createMan) {
            this.createMan = createMan;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public String getModifyMan() {
            return modifyMan;
        }

        public void setModifyMan(String modifyMan) {
            this.modifyMan = modifyMan;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getNeedAmount() {
            return needAmount;
        }

        public void setNeedAmount(double needAmount) {
            this.needAmount = needAmount;
        }

        public String getReceiveEndDate() {
            return receiveEndDate;
        }

        public void setReceiveEndDate(String receiveEndDate) {
            this.receiveEndDate = receiveEndDate;
        }

        public String getReceiveStartDate() {
            return receiveStartDate;
        }

        public void setReceiveStartDate(String receiveStartDate) {
            this.receiveStartDate = receiveStartDate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getRestriction() {
            return restriction;
        }

        public void setRestriction(int restriction) {
            this.restriction = restriction;
        }

        public String getServiceEndDate() {
            return serviceEndDate;
        }

        public void setServiceEndDate(String serviceEndDate) {
            this.serviceEndDate = serviceEndDate;
        }

        public String getServiceStartDate() {
            return serviceStartDate;
        }

        public void setServiceStartDate(String serviceStartDate) {
            this.serviceStartDate = serviceStartDate;
        }

        public Integer getServiceSustain() {
            return serviceSustain;
        }

        public void setServiceSustain(Integer serviceSustain) {
            this.serviceSustain = serviceSustain;
        }

        public long getShopId() {
            return shopId;
        }

        public void setShopId(long shopId) {
            this.shopId = shopId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
}
