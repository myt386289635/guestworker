package com.guestworker.bean;

/**
 * @author 莫小婷
 * @create 2019/4/22
 * @Describe 购物车
 */
public class CartBean {

    private int num;//商品购买数量
    private DetailBean mBean;//商品信息

    public CartBean(int num, DetailBean bean) {
        this.num = num;
        mBean = bean;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public DetailBean getBean() {
        return mBean;
    }

    public void setBean(DetailBean bean) {
        mBean = bean;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "num=" + num +
                ", mBean=" + mBean +
                '}';
    }
}
