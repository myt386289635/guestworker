package com.guestworker.bean;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public class UserInfoBean {

    /**
     * success : true
     * salesInfo : {"salescode":"000004","mobile":"13988888888","userheadpath":"http://thirdwx.qlogo.cn/mmopen/vi_32/P0Qp09yYLLUiaIHknXcOGF5lCIX6eoAqHaHX9BOBiadXVVRBx4ftW07LHVQXlR3PdKXENO8RIhUrfoRTYJOCicPdQ/132","userid":17,"username":"李茹"}
     */

    private boolean success;
    private SalesInfoBean salesInfo;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public SalesInfoBean getSalesInfo() {
        return salesInfo;
    }

    public void setSalesInfo(SalesInfoBean salesInfo) {
        this.salesInfo = salesInfo;
    }

    public static class SalesInfoBean {
        /**
         * salescode : 000004
         * mobile : 13988888888
         * userheadpath : http://thirdwx.qlogo.cn/mmopen/vi_32/P0Qp09yYLLUiaIHknXcOGF5lCIX6eoAqHaHX9BOBiadXVVRBx4ftW07LHVQXlR3PdKXENO8RIhUrfoRTYJOCicPdQ/132
         * userid : 17
         * username : 李茹
         */

        private String salescode;
        private String mobile;
        private String userheadpath;
        private String userid;
        private String username;

        public String getSalescode() {
            return salescode;
        }

        public void setSalescode(String salescode) {
            this.salescode = salescode;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUserheadpath() {
            return userheadpath;
        }

        public void setUserheadpath(String userheadpath) {
            this.userheadpath = userheadpath;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
