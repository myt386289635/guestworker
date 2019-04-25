package com.guestworker.bean;

/**
 * @author 莫小婷
 * @create 2019/4/22
 * @Describe
 */
public class ShoppingCartBean {

    private Boolean isSelect;//是否选择了

    private CartBean mCartBean;

    public ShoppingCartBean(Boolean isSelect, CartBean cartBean) {
        this.isSelect = isSelect;
        mCartBean = cartBean;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }

    public CartBean getCartBean() {
        return mCartBean;
    }

    public void setCartBean(CartBean cartBean) {
        mCartBean = cartBean;
    }
}
