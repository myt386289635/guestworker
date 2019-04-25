package com.guestworker.bean;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public class IsLoginBean {

    /**
     * success : true
     * isLogin : 1
     * bindMobile : 1
     */

    private boolean success;
    private int isLogin;
    private int bindMobile;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(int isLogin) {
        this.isLogin = isLogin;
    }

    public int getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(int bindMobile) {
        this.bindMobile = bindMobile;
    }
}
