package com.guestworker.bean;

import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/17
 * @Describe
 */
public class ClassifyBean {
    /**
     * msg : 成功
     * childTypes : [{"gtid":3,"parentgid":1,"gtname":"电视","gtorder":1,"areaid":"10000001","thumbnail":"thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png"},{"gtid":9,"parentgid":1,"gtname":"空调","gtorder":1,"areaid":"10000001","thumbnail":"thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png"},{"gtid":19,"parentgid":1,"gtname":"洗衣机","gtorder":1,"areaid":"10000001","thumbnail":"thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png"},{"gtid":2,"parentgid":1,"gtname":"冰箱","gtorder":2,"areaid":"10000001","thumbnail":"thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png"},{"gtid":13,"parentgid":1,"gtname":"家电配件","gtorder":2,"areaid":"10000001","thumbnail":"thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png"}]
     * parentTypes : [{"gtid":1,"parentgid":0,"gtname":"电器","gtorder":1,"areaid":"10000001","thumbnail":"thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png"},{"gtid":4,"parentgid":0,"gtname":"家具","gtorder":1,"areaid":"10000001","thumbnail":"thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png"},{"gtid":7,"parentgid":0,"gtname":"平台优选","gtorder":1,"areaid":"10000001","thumbnail":"thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png"}]
     * success : true
     */

    private String msg;
    private boolean success;
    private List<ChildTypesBean> childTypes;
    private List<ParentTypesBean> parentTypes;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ChildTypesBean> getChildTypes() {
        return childTypes;
    }

    public void setChildTypes(List<ChildTypesBean> childTypes) {
        this.childTypes = childTypes;
    }

    public List<ParentTypesBean> getParentTypes() {
        return parentTypes;
    }

    public void setParentTypes(List<ParentTypesBean> parentTypes) {
        this.parentTypes = parentTypes;
    }

    public static class ChildTypesBean {
        /**
         * gtid : 3
         * parentgid : 1
         * gtname : 电视
         * gtorder : 1
         * areaid : 10000001
         * thumbnail : thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png
         */

        private String gtid;
        private int parentgid;
        private String gtname;
        private int gtorder;
        private String areaid;
        private String thumbnail;

        public String getGtid() {
            return gtid;
        }

        public void setGtid(String gtid) {
            this.gtid = gtid;
        }

        public int getParentgid() {
            return parentgid;
        }

        public void setParentgid(int parentgid) {
            this.parentgid = parentgid;
        }

        public String getGtname() {
            return gtname;
        }

        public void setGtname(String gtname) {
            this.gtname = gtname;
        }

        public int getGtorder() {
            return gtorder;
        }

        public void setGtorder(int gtorder) {
            this.gtorder = gtorder;
        }

        public String getAreaid() {
            return areaid;
        }

        public void setAreaid(String areaid) {
            this.areaid = areaid;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }

    public static class ParentTypesBean {
        /**
         * gtid : 1
         * parentgid : 0
         * gtname : 电器
         * gtorder : 1
         * areaid : 10000001
         * thumbnail : thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png
         */

        private String gtid;
        private int parentgid;
        private String gtname;
        private int gtorder;
        private String areaid;
        private String thumbnail;

        public String getGtid() {
            return gtid;
        }

        public void setGtid(String gtid) {
            this.gtid = gtid;
        }

        public int getParentgid() {
            return parentgid;
        }

        public void setParentgid(int parentgid) {
            this.parentgid = parentgid;
        }

        public String getGtname() {
            return gtname;
        }

        public void setGtname(String gtname) {
            this.gtname = gtname;
        }

        public int getGtorder() {
            return gtorder;
        }

        public void setGtorder(int gtorder) {
            this.gtorder = gtorder;
        }

        public String getAreaid() {
            return areaid;
        }

        public void setAreaid(String areaid) {
            this.areaid = areaid;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
    }
}
