package com.guestworker.bean;

/**
 * @author 莫小婷
 * @create 2019/4/17
 * @Describe
 */
public class LoginBean {

    /**
     * success : false
     * msg : 请求失败
     */

    private boolean success;
    private String msg;

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
