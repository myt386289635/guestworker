package com.guestworker.ui.fragment.shoppingcart;

import com.guestworker.bean.OrderSaveBean;
import com.guestworker.bean.PayCodeBean;

/**
 * @author 莫小婷
 * @create 2019/4/23
 * @Describe
 */
public interface ShoppingCartView {

    void onSuccess(OrderSaveBean bean);
    void onFile(String error);

    void onPaySuccess(PayCodeBean bean);
    void onPayFile(String error);
}
