package com.guestworker.bean;

/**
 * @author 莫小婷
 * @create 2019/5/6
 * @Describe
 */
public class AddressBean {

    /**
     * success : true
     * msg : “创建成功！”
     */

    private boolean success;
    private String msg;
    private String addressId;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

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
}
