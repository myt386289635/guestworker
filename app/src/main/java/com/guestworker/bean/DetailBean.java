package com.guestworker.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author 莫小婷
 * @create 2019/4/19
 * @Describe
 */
public class DetailBean implements Serializable{

    /**
     * success : true
     * defaultgoods : {"gid":7,"gname":"测试商品7","price":23,"tagprice":55,"shopdescribe":"人生第七个商品","salesvolumes":53,"parentgoodsid":1,"model":"土色+7米","isCollection":1,"thumbnail":"/thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png","goodsPhotos":[{"goodspid":1,"gid":7,"photopath":"/banner/2019/03/18/afdsfdsafds.jpg","photoorder":"1"},{"goodspid":2,"gid":7,"photopath":"/banner/2019/03/18/afdsfdsafds.jpg","photoorder":"2"}]}
     * goods : [{"gid":7,"gname":"测试商品7","price":23,"tagprice":55,"salesvolumes":53,"parentgoodsid":1,"model":"土色+7米"},{"gid":6,"gname":"测试商品7","price":23,"tagprice":55,"salesvolumes":53,"parentgoodsid":1,"model":"土色+6米"}]
     */

    private boolean success;
    private String msg;
    private DefaultgoodsBean defaultgoods;
    private List<GoodsBean> goods;

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

    public DefaultgoodsBean getDefaultgoods() {
        return defaultgoods;
    }

    public void setDefaultgoods(DefaultgoodsBean defaultgoods) {
        this.defaultgoods = defaultgoods;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class DefaultgoodsBean implements Serializable{
        /**
         * gid : 7
         * gname : 测试商品7
         * price : 23.0
         * tagprice : 55.0
         * shopdescribe : 人生第七个商品
         * salesvolumes : 53
         * parentgoodsid : 1
         * model : 土色+7米
         * isCollection : 1
         * thumbnail : /thumbnail/2019/03/29/12aa5246369947d39e10d392c01c40ec.png
         * goodsPhotos : [{"goodspid":1,"gid":7,"photopath":"/banner/2019/03/18/afdsfdsafds.jpg","photoorder":"1"},{"goodspid":2,"gid":7,"photopath":"/banner/2019/03/18/afdsfdsafds.jpg","photoorder":"2"}]
         */

        private String gid;
        private String gname;
        private double price;
        private double tagprice;
        private String shopdescribe;
        private int salesvolumes;
        private int parentgoodsid;
        private String model;
        private int isCollection;
        private String thumbnail;
        private String shopname;
        private String shopid;
        private List<GoodsPhotosBean> goodsPhotos;
        private int stock;//库存
        private int isputon;//是否下架（1是 0否）
        private int isnegativestock;//是否允许零库存下单（1允许 0不允许）
        private String overview;

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public int getIsputon() {
            return isputon;
        }

        public void setIsputon(int isputon) {
            this.isputon = isputon;
        }

        public int getIsnegativestock() {
            return isnegativestock;
        }

        public void setIsnegativestock(int isnegativestock) {
            this.isnegativestock = isnegativestock;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getTagprice() {
            return tagprice;
        }

        public void setTagprice(double tagprice) {
            this.tagprice = tagprice;
        }

        public String getShopdescribe() {
            return shopdescribe;
        }

        public void setShopdescribe(String shopdescribe) {
            this.shopdescribe = shopdescribe;
        }

        public int getSalesvolumes() {
            return salesvolumes;
        }

        public void setSalesvolumes(int salesvolumes) {
            this.salesvolumes = salesvolumes;
        }

        public int getParentgoodsid() {
            return parentgoodsid;
        }

        public void setParentgoodsid(int parentgoodsid) {
            this.parentgoodsid = parentgoodsid;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getIsCollection() {
            return isCollection;
        }

        public void setIsCollection(int isCollection) {
            this.isCollection = isCollection;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public List<GoodsPhotosBean> getGoodsPhotos() {
            return goodsPhotos;
        }

        public void setGoodsPhotos(List<GoodsPhotosBean> goodsPhotos) {
            this.goodsPhotos = goodsPhotos;
        }

        public static class GoodsPhotosBean implements Serializable{
            /**
             * goodspid : 1
             * gid : 7
             * photopath : /banner/2019/03/18/afdsfdsafds.jpg
             * photoorder : 1
             */

            private int goodspid;
            private int gid;
            private String photopath;
            private String photoorder;

            public int getGoodspid() {
                return goodspid;
            }

            public void setGoodspid(int goodspid) {
                this.goodspid = goodspid;
            }

            public int getGid() {
                return gid;
            }

            public void setGid(int gid) {
                this.gid = gid;
            }

            public String getPhotopath() {
                return photopath;
            }

            public void setPhotopath(String photopath) {
                this.photopath = photopath;
            }

            public String getPhotoorder() {
                return photoorder;
            }

            public void setPhotoorder(String photoorder) {
                this.photoorder = photoorder;
            }

            @Override
            public String toString() {
                return "GoodsPhotosBean{" +
                        "goodspid=" + goodspid +
                        ", gid=" + gid +
                        ", photopath='" + photopath + '\'' +
                        ", photoorder='" + photoorder + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "DefaultgoodsBean{" +
                    "gid='" + gid + '\'' +
                    ", gname='" + gname + '\'' +
                    ", price=" + price +
                    ", tagprice=" + tagprice +
                    ", shopdescribe='" + shopdescribe + '\'' +
                    ", salesvolumes=" + salesvolumes +
                    ", parentgoodsid=" + parentgoodsid +
                    ", model='" + model + '\'' +
                    ", isCollection=" + isCollection +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", shopname='" + shopname + '\'' +
                    ", shopid='" + shopid + '\'' +
                    ", goodsPhotos=" + goodsPhotos +
                    ", stock=" + stock +
                    '}';
        }
    }

    public static class GoodsBean implements Serializable{
        /**
         * gid : 7
         * gname : 测试商品7
         * price : 23.0
         * tagprice : 55.0
         * salesvolumes : 53
         * parentgoodsid : 1
         * model : 土色+7米
         */

        private String gid;
        private String gname;
        private double price;
        private double tagprice;
        private int salesvolumes;
        private int parentgoodsid;
        private String model;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getTagprice() {
            return tagprice;
        }

        public void setTagprice(double tagprice) {
            this.tagprice = tagprice;
        }

        public int getSalesvolumes() {
            return salesvolumes;
        }

        public void setSalesvolumes(int salesvolumes) {
            this.salesvolumes = salesvolumes;
        }

        public int getParentgoodsid() {
            return parentgoodsid;
        }

        public void setParentgoodsid(int parentgoodsid) {
            this.parentgoodsid = parentgoodsid;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        @Override
        public String toString() {
            return "GoodsBean{" +
                    "gid='" + gid + '\'' +
                    ", gname='" + gname + '\'' +
                    ", price=" + price +
                    ", tagprice=" + tagprice +
                    ", salesvolumes=" + salesvolumes +
                    ", parentgoodsid=" + parentgoodsid +
                    ", model='" + model + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DetailBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                ", defaultgoods=" + defaultgoods +
                ", goods=" + goods +
                '}';
    }
}
