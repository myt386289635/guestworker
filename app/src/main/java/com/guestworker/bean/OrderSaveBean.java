package com.guestworker.bean;

/**
 * @author 莫小婷
 * @create 2019/4/23
 * @Describe
 */
public class OrderSaveBean {

    /**
     * msg :
     * orderID : 190329308955824875769856
     * success : true
     */

    private String msg;
    private String orderID;
    private boolean success;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
