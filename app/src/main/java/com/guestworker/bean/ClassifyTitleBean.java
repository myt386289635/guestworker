package com.guestworker.bean;

/**
 * @author 莫小婷
 * @create 2019/4/12
 * @Describe
 */
public class ClassifyTitleBean {

    private ClassifyBean.ParentTypesBean mTypesBean;
    private Boolean up;

    public Boolean getUp() {
        return up;
    }

    public void setUp(Boolean up) {
        this.up = up;
    }

    public ClassifyBean.ParentTypesBean getTypesBean() {
        return mTypesBean;
    }

    public void setTypesBean(ClassifyBean.ParentTypesBean typesBean) {
        mTypesBean = typesBean;
    }

    public ClassifyTitleBean(ClassifyBean.ParentTypesBean typesBean, Boolean up) {
        mTypesBean = typesBean;
        this.up = up;
    }
}
