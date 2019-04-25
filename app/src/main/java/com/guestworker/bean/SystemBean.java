package com.guestworker.bean;

public class SystemBean {

    /**
     * success : true
     * variableMap : {"image_path":"http://ele.test.out.eascs.com","imageserver_path":"http://ele.test.out.eascs.com/","frontTest2":"2"}
     */

    private boolean success;
    private VariableMapBean variableMap;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public VariableMapBean getVariableMap() {
        return variableMap;
    }

    public void setVariableMap(VariableMapBean variableMap) {
        this.variableMap = variableMap;
    }

    public static class VariableMapBean {
        /**
         * image_path : http://ele.test.out.eascs.com
         * imageserver_path : http://ele.test.out.eascs.com/
         * frontTest2 : 2
         */

        private String image_path;
        private String imageserver_path;
        private String frontTest2;

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        public String getImageserver_path() {
            return imageserver_path;
        }

        public void setImageserver_path(String imageserver_path) {
            this.imageserver_path = imageserver_path;
        }

        public String getFrontTest2() {
            return frontTest2;
        }

        public void setFrontTest2(String frontTest2) {
            this.frontTest2 = frontTest2;
        }
    }
}
