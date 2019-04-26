package com.guestworker.bean;

/**
 * @author 莫小婷
 * @create 2019/4/26
 * @Describe
 */
public class PayResultBean {

    private boolean success;
    private String msg;
    private OrderInfoBean orderInfo;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public OrderInfoBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoBean orderInfo) {
        this.orderInfo = orderInfo;
    }

    public static class OrderInfoBean {

        private String orderid;
        private int paymentstatus;

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public int getPaymentstatus() {
            return paymentstatus;
        }

        public void setPaymentstatus(int paymentstatus) {
            this.paymentstatus = paymentstatus;
        }
    }
}
