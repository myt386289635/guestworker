package com.guestworker.bean;

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

        public String getCode_url() {
            return code_url;
        }

        public void setCode_url(String code_url) {
            this.code_url = code_url;
        }
    }
}
