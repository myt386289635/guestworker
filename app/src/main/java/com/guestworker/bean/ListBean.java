package com.guestworker.bean;

import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public class ListBean {

    /**
     * msg : 成功
     * goodsList : [{"thumbnail":"thumbnail/2019/04/13/471b5ec13eff40988739a0ccd9729caa.png","gid":41,"salesvolumes":10,"price":6599,"gname":"卡萨帝洗衣机C1 HU10G3","stock":20,"iscourse":0},{"thumbnail":"thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png","gid":34,"salesvolumes":1,"price":6900,"tagprice":7900,"gname":"长虹电视60Q5T","stock":12,"iscourse":0}]
     * page : {"pageon":1,"rowcount":11,"pagecount":2,"row":10,"start":0,"end":10,"pageNumber":11}
     * success : true
     */

    private String msg;
    private PageBean page;
    private boolean success;
    private List<GoodsListBean> goodsList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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

    public List<GoodsListBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsListBean> goodsList) {
        this.goodsList = goodsList;
    }

    public static class PageBean {
        /**
         * pageon : 1
         * rowcount : 11
         * pagecount : 2
         * row : 10
         * start : 0
         * end : 10
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

    public static class GoodsListBean {
        /**
         * thumbnail : thumbnail/2019/04/13/471b5ec13eff40988739a0ccd9729caa.png
         * gid : 41
         * salesvolumes : 10.0
         * price : 6599.0
         * gname : 卡萨帝洗衣机C1 HU10G3
         * stock : 20
         * iscourse : 0
         * tagprice : 7900.0
         */

        private String thumbnail;
        private String gid;
        private double salesvolumes;
        private double price;
        private String gname;
        private int stock;
        private int iscourse;
        private double tagprice;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public double getSalesvolumes() {
            return salesvolumes;
        }

        public void setSalesvolumes(double salesvolumes) {
            this.salesvolumes = salesvolumes;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getIscourse() {
            return iscourse;
        }

        public void setIscourse(int iscourse) {
            this.iscourse = iscourse;
        }

        public double getTagprice() {
            return tagprice;
        }

        public void setTagprice(double tagprice) {
            this.tagprice = tagprice;
        }
    }
}
