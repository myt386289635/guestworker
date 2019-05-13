package com.guestworker.bean;

/**
 * @author 莫小婷
 * @create 2019/5/13
 * @Describe
 */
public class InvitationBean {

    /**
     * success : true
     * salesCode : 000007
     * url : http://weixin.qq.com/q/02PLLsJC-7dAD19Nt0NtcR
     */

    private boolean success;
    private String salesCode;
    private String url;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getSalesCode() {
        return salesCode;
    }

    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
