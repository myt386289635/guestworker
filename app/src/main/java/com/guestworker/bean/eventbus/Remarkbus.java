package com.guestworker.bean.eventbus;

/**
 * @author 莫小婷
 * @create 2019/5/6
 * @Describe 刷新购物车添加备注
 */
public class Remarkbus {

    private String remark;

    public Remarkbus(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
