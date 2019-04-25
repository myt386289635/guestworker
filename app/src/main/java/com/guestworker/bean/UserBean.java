package com.guestworker.bean;

import java.io.Serializable;

/**
 * @author 莫小婷
 * @create 2019/4/18
 * @Describe
 */
public class UserBean implements Serializable {

    private Boolean tag;
    private AreaUserBean.AreaMemberListBean mBean;


    public UserBean(Boolean tag, AreaUserBean.AreaMemberListBean bean) {
        this.tag = tag;
        mBean = bean;
    }

    public Boolean getTag() {
        return tag;
    }

    public void setTag(Boolean tag) {
        this.tag = tag;
    }

    public AreaUserBean.AreaMemberListBean getBean() {
        return mBean;
    }

    public void setBean(AreaUserBean.AreaMemberListBean bean) {
        mBean = bean;
    }
}
