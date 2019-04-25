package com.guestworker.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public class AreaUserBean implements Serializable{

    /**
     * page : {"pageon":1,"rowcount":3,"pagecount":1,"row":5,"start":0,"end":3,"pageNumber":11}
     * areaMemberList : [{"mobile":"18664330004","userid":1,"username":"柏江辉"}]
     * success : true
     */

    private PageBean page;
    private boolean success;
    private List<AreaMemberListBean> areaMemberList;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<AreaMemberListBean> getAreaMemberList() {
        return areaMemberList;
    }

    public void setAreaMemberList(List<AreaMemberListBean> areaMemberList) {
        this.areaMemberList = areaMemberList;
    }

    public static class PageBean implements Serializable{
        /**
         * pageon : 1
         * rowcount : 3
         * pagecount : 1
         * row : 5
         * start : 0
         * end : 3
         * pageNumber : 11
         */

        private int pageon;
        private int rowcount;
        private int pagecount;
        private int row;
        private int start;
        private int end;
        private int pageNumber;

        public int getPageon() {
            return pageon;
        }

        public void setPageon(int pageon) {
            this.pageon = pageon;
        }

        public int getRowcount() {
            return rowcount;
        }

        public void setRowcount(int rowcount) {
            this.rowcount = rowcount;
        }

        public int getPagecount() {
            return pagecount;
        }

        public void setPagecount(int pagecount) {
            this.pagecount = pagecount;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }
    }

    public static class AreaMemberListBean implements Serializable{
        /**
         * mobile : 18664330004
         * userid : 1
         * username : 柏江辉
         */

        private String mobile;
        private int userid;
        private String username;
        private String userheadpath;
        private String address;
        private String uaid;// 地址id

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUaid() {
            return uaid;
        }

        public void setUaid(String uaid) {
            this.uaid = uaid;
        }

        public String getUserheadpath() {
            return userheadpath;
        }

        public void setUserheadpath(String userheadpath) {
            this.userheadpath = userheadpath;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
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
