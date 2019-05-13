package com.guestworker.bean;

import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/23
 * @Describe 生成订单bean
 */
public class OrderBean {

    private String addressID;
    private int userID;
    private String remark;
    private List<GoodsInfoBean> goodsInfo;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAddressID() {
        return addressID;
    }

    public void setAddressID(String addressID) {
        this.addressID = addressID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public List<GoodsInfoBean> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<GoodsInfoBean> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public static class GoodsInfoBean {

        private String goodsID;
        private int goodsCount;

        public String getGoodsID() {
            return goodsID;
        }

        public void setGoodsID(String goodsID) {
            this.goodsID = goodsID;
        }

        public int getGoodsCount() {
            return goodsCount;
        }

        public void setGoodsCount(int goodsCount) {
            this.goodsCount = goodsCount;
        }
    }
}
