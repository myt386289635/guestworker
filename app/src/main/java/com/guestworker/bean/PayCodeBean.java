package com.guestworker.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author 莫小婷
 * @create 2019/4/23
 * @Describe
 */
public class PayCodeBean {

    /**
     * data : {"code_url":"weixin://wxpay/bizpayurl?pr=5nUIrEd"}
     * success : true
     */

    private DataBean data;
    private boolean success;
    private String err_code;

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static class DataBean {
        /**
         * code_url : weixin://wxpay/bizpayurl?pr=5nUIrEd
         */

        private String code_url;
        private String timeStamp;
        @SerializedName("package")
        private String packageX;
        private String paySign;
        private String appId;
        private String signType;
        private String nonceStr;

        public String getCode_url() {
            return code_url;
        }

        public void setCode_url(String code_url) {
            this.code_url = code_url;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPaySign() {
            return paySign;
        }

        public void setPaySign(String paySign) {
            this.paySign = paySign;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getSignType() {
            return signType;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }
    }
}
